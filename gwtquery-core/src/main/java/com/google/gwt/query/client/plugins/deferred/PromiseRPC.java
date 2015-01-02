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
 *
 * @param <T>
 */
public class PromiseRPC<T> extends DeferredPromiseImpl implements AsyncCallback<T> {
  public void onFailure(Throwable caught) {
    dfd.reject(caught);
  }

  public void onSuccess(T result) {
    dfd.resolve(result);
  }
}
