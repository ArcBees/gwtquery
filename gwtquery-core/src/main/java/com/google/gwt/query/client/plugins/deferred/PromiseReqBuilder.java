/*
 * Copyright 2014, The gwtquery team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.gwt.query.client.plugins.deferred;

/*
 * Copyright 2013, The gwtquery team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.RequestPermissionException;
import com.google.gwt.http.client.Response;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.IsProperties;
import com.google.gwt.query.client.js.JsCache;
import com.google.gwt.query.client.js.JsUtils;
import com.google.gwt.query.client.plugins.ajax.Ajax.Settings;
import com.google.gwt.query.client.plugins.deferred.Deferred.DeferredPromiseImpl;
import com.google.gwt.xhr.client.ReadyStateChangeHandler;
import com.google.gwt.xhr.client.XMLHttpRequest;

/**
 * Utility class used to create promises for RequestBuilder.
 * <pre>
 *        RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, "http://127.0.0.1:8888/whatever");
 *        PromiseRequest gettingResponse = new PromiseReqBuilder(builder);
 *
 *        gettingResponse.fail(new Function() {
 *          public void f() {
 *            Throwable exception = arguments(0);
 *          }
 *        }).done(new Function() {
 *          public void f() {
 *            Response response = arguments(0);
 *          }
 *        });
 * </pre>
 */
public class PromiseReqBuilder extends DeferredPromiseImpl implements RequestCallback {

  public PromiseReqBuilder(RequestBuilder builder) {
    builder.setCallback(this);
    try {
      builder.send();
    } catch (RequestException e) {
      onError(null, e);
    }
  }

  /**
   * Using this constructor we access to some things in the xmlHttpRequest
   * which are not available in GWT, like adding progress handles or sending
   * javascript data (like forms in modern html5 file api).
   */
  public PromiseReqBuilder(Settings settings) {
    String httpMethod = settings.getType();
    String url = settings.getUrl();
    IsProperties data = settings.getData();
    String ctype = settings.getContentType();
    Boolean isFormData =
        data != null && data.getDataImpl() instanceof JavaScriptObject
            && JsUtils.isFormData(data.<JavaScriptObject> getDataImpl());

    XMLHttpRequest xmlHttpRequest = XMLHttpRequest.create();
    try {
      if (settings.getUsername() != null && settings.getPassword() != null) {
        xmlHttpRequest.open(httpMethod, url, settings.getUsername(), settings.getPassword());
      } else if (settings.getUsername() != null) {
        xmlHttpRequest.open(httpMethod, url, settings.getUsername());
      } else {
        xmlHttpRequest.open(httpMethod, url);
      }
    } catch (JavaScriptException e) {
      RequestPermissionException requestPermissionException = new RequestPermissionException(url);
      requestPermissionException.initCause(new RequestException(e.getMessage()));
      onError(null, e);
      return;
    }

    JsUtils.prop(xmlHttpRequest, "onprogress", JsUtils.wrapFunction(new Function() {
      public void f() {
        JsCache p = arguments(0);
        double total = p.getDouble("total");
        double loaded = p.getDouble("loaded");
        double percent = loaded == 0 ? 0 : total == 0 ? 100 : (100 * loaded / total);
        dfd.notify(total, loaded, percent, "download");
      }
    }));

    JavaScriptObject upload = JsUtils.prop(xmlHttpRequest, "upload");
    JsUtils.prop(upload, "onprogress", JsUtils.wrapFunction(new Function() {
      public void f() {
        JsCache p = arguments(0);
        double total = p.getDouble("total");
        double loaded = p.getDouble("loaded");
        double percent = 100 * loaded / total;
        dfd.notify(total, loaded, percent, "upload");
      }
    }));

    IsProperties headers = settings.getHeaders();
    if (headers != null) {
      for (String headerKey : headers.getFieldNames()) {
        xmlHttpRequest.setRequestHeader(headerKey, String.valueOf(headers.get(headerKey)));
      }
    }

    if (data != null && !isFormData && !"GET".equalsIgnoreCase(httpMethod)) {
      xmlHttpRequest.setRequestHeader("Content-Type", ctype);
    }

    // Using gQuery to set credentials since this method was added in 2.5.1
    // xmlHttpRequest.setWithCredentials(true);
    JsUtils.prop(xmlHttpRequest, "withCredentials", settings.getWithCredentials());

    final Request request = createRequestVltr(xmlHttpRequest, settings.getTimeout(), this);

    xmlHttpRequest.setOnReadyStateChange(new ReadyStateChangeHandler() {
      public void onReadyStateChange(XMLHttpRequest xhr) {
        if (xhr.getReadyState() == XMLHttpRequest.DONE) {
          xhr.clearOnReadyStateChange();
          fireOnResponseReceivedVltr(request, PromiseReqBuilder.this);
        }
      }
    });

    try {
      JsUtils.runJavascriptFunction(xmlHttpRequest, "send", isFormData ? data.getDataImpl()
          : settings.getDataString());
    } catch (JavaScriptException e) {
      onError(null, e);
    }
  }

  public void onError(Request request, Throwable exception) {
    dfd.reject(exception, request);
  }

  public void onResponseReceived(Request request, Response response) {
    int status = response.getStatusCode();
    if (status <= 0 || status >= 400) {
      String statusText = status <= 0 ? "Bad CORS" : response.getStatusText();
      onError(request, new RequestException("HTTP ERROR: " + status + " " + statusText + "\n"
          + response.getText()));
    } else {
      dfd.resolve(response, request);
    }
  }

  /**
   * Using violator pattern to execute private method.
   */
  private native void fireOnResponseReceivedVltr(Request rq, RequestCallback cb) /*-{
    rq.@com.google.gwt.http.client.Request::fireOnResponseReceived(Lcom/google/gwt/http/client/RequestCallback;)(cb);
  }-*/;

  /**
   * Using violator pattern to use protected constructor.
   */
  private native Request createRequestVltr(XMLHttpRequest rq, int ms, RequestCallback cb) /*-{
    return @com.google.gwt.http.client.Request::new(Lcom/google/gwt/xhr/client/XMLHttpRequest;ILcom/google/gwt/http/client/RequestCallback;)(rq,ms,cb);
  }-*/;
}
