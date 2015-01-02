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
package com.google.gwt.query.client.plugins.ajax;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.dom.client.ScriptElement;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.Promise;
import com.google.gwt.query.client.plugins.ajax.Ajax.AjaxTransport;
import com.google.gwt.query.client.plugins.ajax.Ajax.Settings;
import com.google.gwt.query.client.plugins.deferred.PromiseFunction;
import com.google.gwt.query.client.plugins.deferred.PromiseReqBuilder;
import com.google.gwt.query.client.plugins.deferred.PromiseReqBuilderJSONP;

/**
 * Ajax transport for Client side.
 */
public class AjaxTransportJs implements AjaxTransport {

  @Override
  public Promise getJsonP(Settings settings) {
    return new PromiseReqBuilderJSONP(settings.getUrl(), settings.getTimeout());
  }

  @Override
  public Promise getLoadScript(final Settings settings) {
    return new PromiseFunction() {
      private ScriptElement scriptElement;

      public void f(final Deferred dfd) {
        scriptElement = ScriptInjector.fromUrl(settings.getUrl()).setWindow(GQuery.window)
            .setCallback(new Callback<Void, Exception>() {
              public void onSuccess(Void result) {
                GQuery.$(GQuery.window).delay(0, new Function() {
                  public void f() {
                    dfd.resolve(scriptElement);
                  }
                });
              }

              public void onFailure(Exception reason) {
                dfd.reject(reason);
              }
            }).inject().cast();
      }
    };
  }

  public Promise getXhr(Settings settings) {
    return new PromiseReqBuilder(settings);
  }
}
