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

import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.Promise.Deferred;

/**
 * Utility class used to create customized functions with a deferred
 * execution in pipelined processes.
 * 
 * They have access to the associated deferred object via a method which
 * will be called only in the case the previous promise is resolved.
 *
 * We can reuse the function and it will create new promises or will return
 * the last one depending on the value of cache. It is very useful for caching
 * server calls which we just want execute once like login, etc.
 *
 * <pre>
 *    Function login = new FunctionDeferred() {
 *      @Override
 *      public void f(Deferred dfd) {
 *        dfd.resolve("logged in as james-bond at " + new Date());
 *      }
 *    }.withCache(RESOLVED);
 *
 *    when(login)
 *    .then(new Function() {
 *      public void f() {
 *        String loginMessage = arguments(0);
 *      }
 *    });
 * </pre>
 */
public abstract class FunctionDeferred extends Function {

  /**
   * Cache types.
   */
  public static enum CacheType {
    NONE, ALL, RESOLVED, REJECTED
  };

  protected Deferred dfd;
  public Function resolve, reject;
  private CacheType cache = CacheType.NONE;

  /**
   * This function is called once the the previous promise in the
   * pipe is resolved, and the new created deferred is available.
   * 
   * You have to override it, and resolve the new promise
   */
  protected abstract void f(Deferred dfd);

  /**
   * This function is called when the previous promise in the pipe
   * is resolved.
   */
  public final Object f(Object... args) {
    return dfd != null &&
        (cache == CacheType.ALL ||
            cache == CacheType.RESOLVED && dfd.promise().isResolved() ||
        cache == CacheType.REJECTED && dfd.promise().isRejected())
        ? dfd.promise()
        : new PromiseFunction() {
          public void f(Deferred dfd) {
            FunctionDeferred.this.resolve = resolve;
            FunctionDeferred.this.reject = reject;
            FunctionDeferred.this.dfd = dfd;
            FunctionDeferred.this.f(dfd);
          }
        };
  }

  /**
   * Configure whether cache the promise the first time it is resolved
   * or create new promises each time the function is executed.
   *
   * By default cache is disabled.
   */
  public FunctionDeferred withCache(CacheType type) {
    cache = type;
    return this;
  }

  /**
   * Reset the cache so as a new invocation to this function will
   * execute it instead of restoring old values from cache.
   */
  public FunctionDeferred resetCache() {
    if (dfd != null && !dfd.promise().isPending()) {
      dfd = null;
    }
    return this;
  }
}
