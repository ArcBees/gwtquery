/*
 * Copyright 2013, The gwtquery team.
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
   * Implementation of the Promise interface which is used internally by
   * Deferred.
   */
  static class DeferredPromiseImpl implements Promise {
    
    // Private class used to handle `Promise.then()`
    private static class ThenFunction extends Function {
      // Used internally in ThenFunction, to resolve deferred object
      private class DoFunction extends Function {
        public void f() {
          if (type == 0) dfd.resolve(getArguments());
          if (type == 1) dfd.reject(getArguments());
          if (type == 2) dfd.notify(getArguments());
        }
      }

      // Original filter function
      private final Function filter;
      // Type of action (0 = done, 1 = fail, 2 = progress)
      private final int type;
      // Original deferred object
      private final Deferred dfd;

      public ThenFunction(Deferred newDfd, Function[] subordinates, int funcType) {
        type = funcType;
        filter = subordinates.length > type ? subordinates[type] : null;
        dfd = newDfd;
      }

      public void f() {
        Object[] args = getArguments();
        Function doIt = new DoFunction().setArguments(args);
        if (filter != null) {
          // We filter resolved arguments with the filter function
          Object newArgs = filter.setArguments(args).f(args);
          // If filter function returns a promise we pipeline it
          if (newArgs instanceof Promise) {
            Promise p = (Promise) newArgs;
            if (type == 0) p.done(doIt);
            if (type == 1) p.fail(doIt);
            if (type == 2) p.progress(doIt);
            return;
          } else if (newArgs.getClass().isArray()) {
            doIt.setArguments((Object[])newArgs);
          } else {
            doIt.setArguments(newArgs);
          }
        }
        doIt.f();
      }
    }    
    
    protected com.google.gwt.query.client.plugins.deferred.Deferred dfd;

    protected DeferredPromiseImpl() {
      dfd = new com.google.gwt.query.client.plugins.deferred.Deferred();
    }

    protected DeferredPromiseImpl(
        com.google.gwt.query.client.plugins.deferred.Deferred o) {
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
      done(new ThenFunction(newDfd, f, 0));
      fail(new ThenFunction(newDfd, f, 1));
      progress(new ThenFunction(newDfd, f, 2));
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
     * Internal function used to track whether all deferred subordinates are
     * resolved.
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
  
  public static Promise when(Object... d) {
    int l = d.length;
    Promise[] p = new Promise[l];
    for (int i = 0; i < l; i++) {
      p[i] = makePromise(d[i]);
    }
    return when(p);
  }
  
  private static Promise makePromise(final Object o) {
    if (o instanceof Promise) {
      return (Promise)o;
    } else if (o instanceof Function) {
      return makePromise(((Function)o).f(new Object[0]));
    } else if (o instanceof GQuery) {
      return ((GQuery)o).promise();
    } else {
      return new PromiseFunction() {
        public void f(Deferred dfd) {
          dfd.resolve(o);
        }
      };
    }
  }
  
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
