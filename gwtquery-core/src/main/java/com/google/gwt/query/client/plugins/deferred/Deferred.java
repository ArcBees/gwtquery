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

import static com.google.gwt.query.client.Promise.PENDING;
import static com.google.gwt.query.client.Promise.REJECTED;
import static com.google.gwt.query.client.Promise.RESOLVED;

import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.Promise;
import com.google.gwt.query.client.plugins.Plugin;

/**
 * Implementation of jQuery.Deferred for gwtquery.
 */
public class Deferred extends GQuery implements Promise.Deferred {
  
  /**
   * Implementation of the Promise interface which is used internally by Deferred.
   */
  static class DeferredPromiseImpl implements Promise {
    protected com.google.gwt.query.client.plugins.deferred.Deferred dfd;
    
    protected DeferredPromiseImpl() {
      dfd = new com.google.gwt.query.client.plugins.deferred.Deferred();
    }

    protected DeferredPromiseImpl(com.google.gwt.query.client.plugins.deferred.Deferred o) {
      dfd = o;
    }
    
    public Promise always(Function... f) {
      return done(f).fail(f);
    }

    public Promise done(Function... f) {
      dfd.resolve.add(f);
      return this;
    }
    
    public Promise fail(Function... f) {
      dfd.reject.add(f);
      return this;
    }

    public Promise pipe(Function... f) {
      return then(f);
    }
    
    public Promise progress(Function... f) {
      dfd.notify.add(f);
      return this;
    }
    
    public String state() {
      return dfd.state;
    }
    
    public Promise then(final Function... f) {
      final Deferred newDfd = new com.google.gwt.query.client.plugins.deferred.Deferred();
      progress(new Function() {
        public void f() {
          newDfd.notify(f.length > 2 ? f[2].f(getArguments()) : getArguments());
        }
      });
      fail(new Function() {
        public void f() {
          newDfd.reject(f.length > 1 ? f[1].f(getArguments()) : getArguments());
        }
      });
      done(new Function() {
        public void f() {
          newDfd.resolve(f.length > 0 ? f[0].f(getArguments()) : getArguments());
        }
      });
      return newDfd.promise();
    }

    public boolean isResolved() {
      return Promise.RESOLVED.equals(state());
    }

    public boolean isRejected() {
      return Promise.REJECTED.equals(state());
    }
  }
  
  /**
   * Internal Deferred class used to combine a set of subordinate promises. 
   */
  private static class WhenDeferredImpl extends Deferred {
    /**
     * Internal function used to track whether all deferred 
     * subordinates are resolved. 
     */
    private class DoneFnc extends Function {
      final int idx;

      public DoneFnc(int i, Deferred d) {
        idx = i; 
      }

      public Object f(Object... args) {
        values[idx] = args;
        if (--remaining == 0) {
          WhenDeferredImpl.this.resolve(values);
        }
        return true;
      }
    }
    
    private Function failFnc = new Function() {
      public Object f(Object... o) {
        WhenDeferredImpl.this.reject(o);
        return true;
      }
    };
    
    private Function progressFnc = new Function() {
      public Object f(Object... o) {
        WhenDeferredImpl.this.notify(o);
        return true;
      }
    };
    
    // Remaining counter
    private int remaining;
    
    // An indexed array with the fired values of all subordinated
    private final Object[] values;
    
    public WhenDeferredImpl(Promise[] sub) {
      int l = remaining = sub.length;
      values = new Object[l];
      for (int i = 0; i < l; i++) {
        sub[i].done(new DoneFnc(i, this)).progress(progressFnc).fail(failFnc);
      }
    }
  }
  
  // Register Deferred as a GQuery plugin
  public static final Class<Deferred> Deferred = GQuery.registerPlugin(
      Deferred.class, new Plugin<Deferred>() {
        public Deferred init(GQuery gq) {
          return new Deferred(gq);
        }
      });
  
  public static Promise when(Promise... d) {
    final int n = d.length;
    switch (n) {
      case 1:
        return d[0];
      case 0:
        return new Deferred().resolve().promise();
      default:
        return new WhenDeferredImpl(d).promise();
    }
  }
  
  private Callbacks notify = new Callbacks("memory");
  
  private Promise promise = null;
  
  private Callbacks reject = new Callbacks("once memory");
  
  private Callbacks resolve = new Callbacks("once memory");
  
  private String state = PENDING;
  
  public Deferred() {
    this(null);
  }
  
  protected Deferred(GQuery gq) {
    super(gq);
    resolve.add(new Function() {
      public void f() {
        state = RESOLVED;
        resolve.disable();
        notify.lock();
      }
    });

    reject.add(new Function() {
      public void f() {
        state = REJECTED;
        reject.disable();
        notify.lock();
      }
    });
  }
  
  /**
   * Call the progressCallbacks on a Deferred object with the given args.
   */
  public Deferred notify(Object... o) {
    notify.fire(o);
    return this;
  }

  /**
   * Return a Deferred’s Promise object.
   */
  public Promise promise() {
    if (promise == null) {
      promise = new DeferredPromiseImpl(this);
    }
    return promise;
  }
  
  /**
   * Reject a Deferred object and call any failCallbacks with the given args.
   */
  public Deferred reject(Object... o) {
    reject.fire(o);
    return this;
  }
  
  /**
   * Resolve a Deferred object and call any doneCallbacks with the given args.
   */
  public Deferred resolve(Object... o) {
    resolve.fire(o);
    return this;
  }
}
