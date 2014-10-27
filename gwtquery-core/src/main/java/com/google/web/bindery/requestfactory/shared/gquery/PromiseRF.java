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
package com.google.web.bindery.requestfactory.shared.gquery;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;

import com.google.gwt.query.client.functions.Func1;
import com.google.gwt.query.client.functions.FuncN;
import com.google.gwt.query.client.functions.Functions;
import com.google.gwt.query.client.plugins.deferred.Deferred;
import com.google.gwt.query.client.plugins.deferred.Deferred.DeferredPromiseImpl;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

/**
 * Utility class used to create promises for RequestFactory services.
 * <pre>
 *    Request<SessionProxy> req1 = loginFact.api().login(null, null);
 *    Request<UserProxy> req2 = srvFact.api().getCurrentUser();
 *    
 *    // We can use `when` to append different requests
 *    Promise requestingAll = Deferred.when(new PromiseRF(req1), new PromiseRF(req2);
 *    // Or we can use just one promise for multiple RF requests
 *    Promise requestingAll = new PromiseRF(req1, req2);
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
public class PromiseRF<T> extends DeferredPromiseImpl<T> {
  private int total = 0;
  private List<Object> responses = new ArrayList<Object>();
  private List<RequestContext> contexts = new ArrayList<RequestContext>();
  
  /**
   * Fire a RF Request.
   */
  public PromiseRF(Request<T> request) {
    this(new Request<?>[] {request}, new FuncN<T>() {
      @Override
      public T call(Object... args) {
        return (T) args[0];
      }
    });
  }

  /**
   * Fire multiple RF Requests.
   *
   * Unlike RequestContext.append which only supports compatible requests,
   * we can append any kind of requestContexts here.
   */
  public PromiseRF(Request<?>[] requests) {
    this(requests, new FuncN<T>() {
      @Override
      public T call(Object... args) {
        return (T) args;
      }
    });
  }

  public PromiseRF(Request<?>[] requests, final FuncN<T> doneFunc) {
    for (Request<?> request : requests) {
      total ++;
      request.to(new Receiver<Object>() {
        public void onConstraintViolation(Set<ConstraintViolation<?>> violations) {
          dfd.reject(new ServerFailure("ConstraintViolation"), violations);
        }
        public void onFailure(ServerFailure error) {
          dfd.reject(error);
        }
        public void onSuccess(Object response) {
          responses.add(response);
          // Resolve only when all requests have been received
          if (responses.size() == total) {
            dfd.resolve(doneFunc.call(responses.toArray(new Object[responses.size()])));
          }
        }
      });
      if (!contexts.contains(request.getRequestContext())) {
        contexts.add(request.getRequestContext());
      }
    }

    // We fire each context instead of appending them so as we can deal
    // with different request factories.
    for (RequestContext ctx : contexts) {
      ctx.fire();
    }
  }
}
