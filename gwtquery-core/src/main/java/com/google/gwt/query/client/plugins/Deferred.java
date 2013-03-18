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

import static com.google.gwt.query.client.Promise.PENDING;
import static com.google.gwt.query.client.Promise.REJECTED;
import static com.google.gwt.query.client.Promise.RESOLVED;

import java.util.Set;

import javax.validation.ConstraintViolation;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.Promise;
import com.google.gwt.query.client.plugins.callbacks.Callbacks;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

/**
 * Implementation of jQuery.Deferred for gwtquery.
 */
public class Deferred extends GQuery implements Promise.Deferred {
  
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
  public static abstract class PromiseFunction extends DeferredPromiseImpl {
    public PromiseFunction() {
      f(dfd);
    }
    
    /**
     * This function is called once when the promise is created and the
     * new deferred is available.
     */
    public abstract void f(Deferred dfd);
  }
  
  /**
   * Utility class used to create promises for RequestBuilder.
   * <pre>
   *        RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, "http://127.0.0.1:8888/whatever");
   *        PromiseRequest gettingResponse = new PromiseReqBuilder(builder);
   *        
   *        gettingResponse.fail(new Function() {
   *          public void f() {
   *            Throwable exception = arguments(0);
   *          }
   *        }).done(new Function() {
   *          public void f() {
   *            Response response = arguments(0);
   *          }
   *        });
   * </pre>
   */
  public static class PromiseReqBuilder extends DeferredPromiseImpl implements RequestCallback {
    public PromiseReqBuilder(RequestBuilder builder) {
      builder.setCallback(this);
      try {
        builder.send();
      } catch (RequestException e) {
        onError(null, e);
      }
    }

    public void onError(Request request, Throwable exception) {
      dfd.reject(exception, request);
    }

    public void onResponseReceived(Request request, Response response) {
      int status = response.getStatusCode();
      if (status <= 0 || status >= 400) {
        String statusText = status <= 0 ? "Bad CORS" : response.getStatusText();
        onError(request, new RequestException("HTTP ERROR: " + status + " " + statusText + "\n" + response.getText()));
      } else {
        dfd.resolve(response, request);
      }
    }
  }
  
  
  /**
   * Utility class used to create promises for RequestFactory services.
   * <pre>
   *    Request<SessionProxy> req1 = loginFact.api().login(null, null);
   *    Request<UserProxy> req2 = srvFact.api().getCurrentUser();
   *    
   *    Promise requestingAll = Deferred.when(new PromiseRF(req1), new PromiseRF(req2);
   *    
   *    requestingAll.done(new Function() {
   *        public void f() {
   *          SessionProxy session = arguments(0, 0);
   *          UserProxy user = arguments(1, 0);
   *        }
   *      })
   *      .fail(new Function() {
   *        public void f() {
   *          ServerFailure failure = arguments(0);
   *        }
   *      }); 
   * </pre>
   */
  public static class PromiseRF extends DeferredPromiseImpl {
    public <T> PromiseRF(com.google.web.bindery.requestfactory.shared.Request<T> request) {
      request.fire(new Receiver<T>() {
        public void onConstraintViolation(Set<ConstraintViolation<?>> violations) {
          dfd.reject(new ServerFailure("ConstraintViolation"), violations);
        }

        public void onFailure(ServerFailure error) {
          dfd.reject(error);
        }

        public void onSuccess(T response) {
          dfd.resolve(response);
        }
      });
    }
  }
  
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
  public static class PromiseRPC<T> extends DeferredPromiseImpl implements AsyncCallback<T> {
    public void onFailure(Throwable caught) {
      dfd.reject(caught);
    }

    public void onSuccess(T result) {
      dfd.resolve(result);
    }
  }

  
  /**
   * Implementation of the Promise interface which is used internally by Deferred.
   */
  private static class DeferredPromiseImpl implements Promise {
    com.google.gwt.query.client.plugins.Deferred dfd;
    
    protected DeferredPromiseImpl() {
      dfd = new com.google.gwt.query.client.plugins.Deferred();
    }

    protected DeferredPromiseImpl(com.google.gwt.query.client.plugins.Deferred o) {
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
    
    public Promise then(Function... f) {
      assert f.length < 4 : "Promise.then: Too much arguments";
      switch (f.length) {
        case 3: progress(f[2]);
        case 2: fail(f[1]);
        case 1: done(f[0]);
      }
      return this;
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

  // private, used from jsni because it does not handles variable arguments
  @SuppressWarnings("unused")
  private void err(Object o) {
    reject(o);
  }
  
  // private, used from jsni because it does not handles variable arguments
  @SuppressWarnings("unused")
  private void ok(Object o) {
    resolve(o);
  }
}
