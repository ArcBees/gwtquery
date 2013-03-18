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