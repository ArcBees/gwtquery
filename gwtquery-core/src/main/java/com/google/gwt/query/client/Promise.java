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
package com.google.gwt.query.client;

/**
 * Definition of jquery Promise interface used in gquery.
 */
public interface Promise {

  /**
   * Definition of jquery Deferred interface used in gquery.
   */
  public interface Deferred {
    /**
     * Call the progressCallbacks on a Deferred object with the given args.
     */
    Deferred notify(Object... o);

    /**
     * Return a Deferred’s Promise object.
     */
    Promise promise();

    /**
     * Reject a Deferred object and call any failCallbacks with the given args.
     */
    Deferred reject(Object... o);

    /**
     * Resolve a Deferred object and call any doneCallbacks with the given args.
     */
    Deferred resolve(Object... o);
  }

  String PENDING = "pending";
  String REJECTED = "rejected";
  String RESOLVED = "resolved";

  /**
   * Add handlers to be called when the Deferred object is either resolved or rejected.
   */
  Promise always(Function... o);

  /**
   * Add handlers to be called when the Deferred object is resolved.
   */
  Promise done(Function... o);

  /**
   * Add handlers to be called when the Deferred object is rejected.
   */
  Promise fail(Function... o);

  /**
   * Utility method to filter and/or chain Deferreds.
   * 
   * @deprecated use 'then' instead.
   *   it was deprecated in jquery, and we maintain it here for compatibility.
   */
  @Deprecated
  Promise pipe(Function... f);

  /**
   * Utility method to filter and/or chain Deferreds.
   */
  Promise progress(Function... o);

  /**
   * Return the status of the deferred object.
   * 
   * Valid values are: Promise.PENDING, Promise.REJECTED, Promise.RESOLVED
   * 
   */
  String state();

  /**
   * Add filters to be called when the Deferred object is resolved, rejected, or still in progress.
   *
   * These handlers are used to eventually change the values used to resolve the promise.
   *
   * `then` returns a new Promise which will be resolved with the values returned by the handlers
   * in old jquery versions `then` was called `pipe`.
   *
   * @param f a list of 1, 2, or 3 functions, which will be used in this way:
   *   1st function will be called when the deferred is resolved.
   *   2nd function that is called when the deferred is rejected.
   *   3rd one will be called when progress notifications are sent.
   */
  Promise then(Function... f);

  /**
   * Add filters to be called just in case the Deferred object is rejected returning
   * a new valid promise so as we can continue the flow control of the chain.
   *
   * It works in the same way than adding a second parameter to {@link then} method but
   * continuing the flow and making more readable the code.
   *
   * Example:
   * <pre>
     GQuery.when(gettingConfigurationFromCache())
           .or(gettingConfigurationFromServer())
           .done(drawUI());
   * </pre>
   *
   * NOTE: this is a convenience method in gQuery not present in jQuery.
   */
  Promise or(Function f);

  /**
   * Add filters to be called just in case the Deferred object is resolved.
   *
   * It works in the same way than {@link then} does but making more readable
   * the code flow.
   *
   * NOTE: this method is in gQuery but not in jQuery.
   *
   * Example:
   * <pre>
     GQuery.when(login())
           .and(getConfiguration())
           .done(drawUI());
   * </pre>
   *
   * NOTE: this is a convenience method in gQuery not present in jQuery.
   */
  Promise and(Function f);

  /**
   * Determine whether a Deferred object has been resolved.
   */
  boolean isResolved();

  /**
   * Determine whether a Deferred object has been rejected.
   */
  boolean isRejected();

  /**
   * Determine whether a Deferred object is pending.
   */
  boolean isPending();
}
