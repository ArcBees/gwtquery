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

import java.util.Set;

import javax.validation.ConstraintViolation;

import com.google.gwt.query.client.plugins.deferred.Deferred.DeferredPromiseImpl;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

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
public class PromiseRF extends DeferredPromiseImpl {
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