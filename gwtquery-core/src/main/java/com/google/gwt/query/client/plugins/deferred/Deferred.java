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

/**
 * Implementation of jQuery.Deferred for gwtquery.
 */
public class Deferred implements Promise.Deferred {

  /**
   * Implementation of the Promise interface which is used internally by
   * Deferred.
   */
  public static class DeferredPromiseImpl implements Promise {
    // Using 'int' instead of 'enum' because we use them as indexes as well
    private static final int DONE = 0, FAIL = 1, PROGRESS = 2;

    // Private class used to handle `Promise.then()`
    private static class ThenFunction extends Function {

      // Original filter function
      private final Function filter;
      // Type of action (DONE/FAIL/PROGRESS)
      private final int type;
      // Original deferred object
      private final Deferred dfd;
      // Whether break the flow if old deferred fails
      boolean cont = false;

      public ThenFunction(Deferred newDfd, Function[] subordinates, int funcType,
          boolean continueFlow) {
        type = funcType;
        filter = subordinates.length > type ? subordinates[type] : null;
        dfd = newDfd;
        cont = continueFlow;
      }

      public void f() {
        final Object[] oldArgs = getArguments();
        if (filter != null) {
          // We filter resolved arguments with the filter function
          Object newArgs = filter.setArguments(oldArgs).f(oldArgs);

          if (newArgs instanceof Promise) {
            // If filter function returns a promise we pipeline it.
            final Promise p = (Promise) newArgs;
            if (type == PROGRESS) {
              p.progress(new Function() {
                public void f() {
                  settle(PROGRESS, getArguments());
                }
              });
            } else {
              p.always(new Function() {
                public void f() {
                  settle((type == DONE || type == FAIL && cont) && p.isResolved() ? DONE : FAIL,
                      getArguments());
                }
              });
            }
          } else {
            // Otherwise we change the arguments by the new ones
            newArgs = Boolean.TRUE.equals(newArgs) ? oldArgs :
                newArgs != null && newArgs.getClass().isArray() ? (Object[]) newArgs : newArgs;
            settle(type, newArgs);
          }
        } else {
          // just continue the flow when filter is null
          settle(type, oldArgs);
        }
      }

      private void settle(int action, Object... args) {
        if (action == DONE)
          dfd.resolve(args);
        if (action == FAIL)
          dfd.reject(args);
        if (action == PROGRESS)
          dfd.notify(args);
      }
    }

    protected com.google.gwt.query.client.plugins.deferred.Deferred dfd;

    /**
     * Utility function which can be used inside classes extending this to
     * resolve this deferred in a call to any other promise method.
     *
     * Example:
     * <pre>
     * new PromiseFunction() {
     *   public void f(final Deferred dfd) {
     *     anotherPromise.done( resolve );
     *   }
     * }
     * </pre>
     */
    public Function resolve = new Function() {
      public void f() {
        dfd.resolve(arguments);
      };
    };

    /**
     * Utility function which can be used inside classes extending this to
     * reject this deferred in a call to any other promise method.
     *
     * Example:
     * <pre>
     * new PromiseFunction() {
     *   public void f(final Deferred dfd) {
     *     anotherPromise.done( reject );
     *   }
     * }
     * </pre>
     */
    public Function reject = new Function() {
      public void f() {
        dfd.reject(arguments);
      };
    };

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

    public Promise and(Function f) {
      return then(f);
    }

    public Promise done(Function... f) {
      dfd.resolve.add(f);
      return this;
    }

    public Promise fail(Function... f) {
      dfd.reject.add(f);
      return this;
    }

    public Promise or(final Function f) {
      return then(true, null, f);
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

    private Promise then(boolean continueFlow, final Function... f) {
      final Deferred newDfd = new com.google.gwt.query.client.plugins.deferred.Deferred();
      done(new ThenFunction(newDfd, f, DONE, continueFlow));
      fail(new ThenFunction(newDfd, f, FAIL, continueFlow));
      progress(new ThenFunction(newDfd, f, PROGRESS, continueFlow));
      return newDfd.promise();
    }

    public Promise then(final Function... f) {
      return then(false, f);
    }

    public boolean isResolved() {
      return Promise.RESOLVED.equals(state());
    }

    public boolean isRejected() {
      return Promise.REJECTED.equals(state());
    }

    public boolean isPending() {
      return Promise.PENDING.equals(state());
    }

    public String toString() {
      return "Promise this=" + hashCode() + " " + dfd;
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
        values[idx] = args.length == 1 ? args[0] : args;
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

  /**
   * Provides a way to execute callback functions based on one or more objects,
   * usually Deferred objects that represent asynchronous events.
   *
   * Returns the Promise from a new "master" Deferred object that tracks the aggregate
   * state of all the Deferreds passed. The method will resolve its master
   * Deferred as soon as all the Deferreds resolve, or reject the master Deferred as
   * soon as one of the Deferreds is rejected
   */
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
      return (Promise) o;
    } else if (o instanceof Function) {
      return makePromise(((Function) o).f(new Object[0]));
    } else if (o instanceof GQuery) {
      return ((GQuery) o).promise();
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
    if (state == PENDING)
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
    if (state == PENDING)
      reject.fire(o);
    return this;
  }

  /**
   * Resolve a Deferred object and call any doneCallbacks with the given args.
   */
  public Deferred resolve(Object... o) {
    if (state == PENDING)
      resolve.fire(o);
    return this;
  }

  @Override
  public String toString() {
    return "Deferred this=" + hashCode() + " promise=" + promise().hashCode() + " state="
        + promise.state() + " restatus=" + resolve.status();
  }
}
