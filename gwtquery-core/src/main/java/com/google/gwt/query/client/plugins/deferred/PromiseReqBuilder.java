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
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.query.client.plugins.deferred.Deferred.DeferredPromiseImpl;

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

  public void onError(Request request, Throwable exception) {
    dfd.reject(exception, request);
  }

  public void onResponseReceived(Request request, Response response) {
    int status = response.getStatusCode();
    if (status <= 0 || status >= 400) {
      String statusText = status <= 0 ? "Bad CORS" : response.getStatusText();
      onError(request, new RequestException("HTTP ERROR: " + status + " " + statusText + "\n" + response.getText()));
    } else {
      dfd.resolve(response, request);
    }
  }
}
