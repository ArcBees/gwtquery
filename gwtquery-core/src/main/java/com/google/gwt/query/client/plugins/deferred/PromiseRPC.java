package com.google.gwt.query.client.plugins.deferred;

import com.google.gwt.query.client.plugins.deferred.Deferred.DeferredPromiseImpl;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Utility class used to create promises for RPC services.
 * <pre>
 *        PromiseRPC<String> greeting = new PromiseRPC<String>();
 *        
 *        GreetingServiceAsync greetingService = GWT.create(GreetingService.class);
 *        greetingService.greetServer("hi", greeting);
 *        
 *        greeting.fail(new Function(){
 *          public void f() {
 *            Throwable error = arguments(0);
 *          }
 *        }).done(new Function(){
 *          public void f() {
 *            String response = arguments(0);
 *          }
 *        });
 * </pre>
 */
public class PromiseRPC<T> extends DeferredPromiseImpl implements AsyncCallback<T> {
  public void onFailure(Throwable caught) {
    dfd.reject(caught);
  }

  public void onSuccess(T result) {
    dfd.resolve(result);
  }
}