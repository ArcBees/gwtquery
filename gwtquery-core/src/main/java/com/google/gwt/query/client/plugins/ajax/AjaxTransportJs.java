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
            GQuery.$(GQuery.window).delay(0, new Function(){
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
