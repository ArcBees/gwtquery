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
package com.google.gwt.query.client.plugins;

import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.Promise;
import com.google.gwt.query.client.plugins.callbacks.Callbacks;

/**
 * Implementation of jQuery.Deffered for gwtquery.
 */
public class Deferred extends GQuery {
  
  private class DeferredPromise extends GQuery implements Promise {
    private Deferred dfd;

    protected DeferredPromise(GQuery gq) {
      super(gq);
      if (gq instanceof Deferred) {
        dfd = (Deferred) gq;
      } else  if (gq instanceof DeferredPromise) {
        dfd = ((DeferredPromise)gq).dfd;;
      } else {
        dfd = gq.as(Deferred);
      }
    }
    
    public Promise always(Function... o) {
      return done(o).fail(o);
    }

    public Promise done(Function... o) {
      dfd.resolve.add(o);
      return this;
    }
    
    public Promise fail(Function... o) {
      dfd.reject.add(o);
      return this;
    }

    public Promise pipe(Function... f) {
      return then(f);
    }
    
    public Promise progress(Function... o) {
      dfd.notify.add(o);
      return this;
    }
    
    public String state() {
      return dfd.state;
    }
    
    public Promise then(Function... f) {
      switch (f.length) {
        case 3: progress(f[0]);
        case 2: fail(f[0]);
        case 1: done(f[0]);
      }
      return this;
    }
  }
  public static final Class<Deferred> Deferred = GQuery.registerPlugin(
      Deferred.class, new Plugin<Deferred>() {
        public Deferred init(GQuery gq) {
          return new Deferred(gq);
        }
      });
  public static Promise when(Deferred d) {
    return d.promise();
  }
  private Callbacks notify = new Callbacks("memory");
  
  private Promise promise = null;
  
  private Callbacks reject = new Callbacks("once memory");
  
  private Callbacks resolve = new Callbacks("once memory");
  
  private String state = Promise.PENDING;
  
  protected Deferred(GQuery gq) {
    super(gq);
    resolve.add(new Function() {
      public void f() {
        state = Promise.RESOLVED;
        resolve.disable();
        notify.lock();
      }
    });

    reject.add(new Function() {
      public void f() {
        state = Promise.REJECTED;
        reject.disable();
        notify.lock();
      }
    });
  }
  // private, used from jsni
  @SuppressWarnings("unused")
  private void err(Object o) {
    reject(o);
  }
  public Deferred notify(Object... o) {
    notify.fire(o);
    return this;
  }
  
  // private, used from jsni
  @SuppressWarnings("unused")
  private void ok(Object o) {
    resolve(o);
  }
  
  public Promise promise() {
    if (promise == null) {
      promise = new DeferredPromise(this);
    }
    return promise;
  }

  
  public Deferred reject(Object... o) {
    reject.fire(o);
    return this;
  }
  
  public Deferred resolve(Object... o) {
    resolve.fire(o);
    return this;
  }
}
