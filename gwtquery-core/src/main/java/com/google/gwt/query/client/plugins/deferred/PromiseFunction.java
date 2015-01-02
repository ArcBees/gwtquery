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

/**
 * Utility class used to create customized promises which can manipulate
 * the associated deferred object.
 * <pre>
 *    Promise doSomething = new PromiseFunction() {
 *      @Override
 *      public void f(Deferred dfd) {
 *        dfd.notify("hi");
 *        dfd.resolve("done");
 *      }
 *    };
 * 
 *    doSomething.progress(new Function() {
 *      public void f() {
 *        String hi = arguments(0);
 *      }
 *    }).done(new Function() {
 *      public void f() {
 *        String done = arguments(0);
 *      }
 *    });
 * </pre>
 */
public abstract class PromiseFunction extends DeferredPromiseImpl {
  public PromiseFunction() {
    f(dfd);
  }

  /**
   * This function is called once when the promise is created and the
   * new deferred is available.
   */
  public abstract void f(Deferred dfd);
}
