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
package com.google.gwt.query.client.plugins.deferred;

import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.query.client.plugins.deferred.Deferred.DeferredPromiseImpl;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Utility class used to create promises for JsonpRequestBuilder.
 * <pre>
 *    Promise p = new PromiseJsonpReqBuilder(url, 4000);
 * 
 *    p.done(new Function() {
 *      public void f() {
 *        Properties p = arguments(0);
 *      }
 *    }).fail(new Function() {
 *      public void f() {
 *        Throwable error = arguments(0);
 *      }
 *    });
 * </pre>
 */
public class PromiseReqBuilderJSONP extends DeferredPromiseImpl {

  private static final RegExp callbackRegex = RegExp.compile("^(.+[\\?&])([^=]+)=\\?(.*)$");

  public PromiseReqBuilderJSONP(String url) {
    this(url, null, 0);
  }

  public PromiseReqBuilderJSONP(String url, int timeout) {
    this(url, null, timeout);
  }

  public PromiseReqBuilderJSONP(String url, String callbackParam, int timeout) {
    JsonpRequestBuilder builder = new JsonpRequestBuilder();
    if (timeout > 0) {
      builder.setTimeout(timeout);
    }
    // jQuery allows a parameter callback=? to figure out the callback parameter
    if (callbackParam == null) {
      MatchResult tmp = callbackRegex.exec(url);
      if (tmp != null && tmp.getGroupCount() == 4) {
        callbackParam = tmp.getGroup(2);
        url = tmp.getGroup(1) + tmp.getGroup(3);
      }
    }
    if (callbackParam != null) {
      builder.setCallbackParam(callbackParam);
    }
    send(builder, url, new AsyncCallback<Object>() {
      public void onFailure(Throwable caught) {
        dfd.reject(caught);
      }

      public void onSuccess(Object result) {
        dfd.resolve(result);
      }
    });
  }

  // Using jsni because method send in JsonpRequestBuilder is private
  private native void send(JsonpRequestBuilder bld, String url, AsyncCallback<?> cb) /*-{
    bld.@com.google.gwt.jsonp.client.JsonpRequestBuilder::send(Ljava/lang/String;Lcom/google/gwt/user/client/rpc/AsyncCallback;Z)(url,cb,false);
  }-*/;
}
