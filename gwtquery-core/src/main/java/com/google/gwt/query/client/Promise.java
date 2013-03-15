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

import com.google.gwt.query.client.Function;

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

  public static final String PENDING = "pending";
  public static final String REJECTED = "rejected";
  public static final String RESOLVED = "resolved";

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
   * Add handlers to be called when the Deferred object is resolved, rejected, or still in progress.
   * 
   * @param f a list of 1, 2, or 3 functions, which will be used in this way:
   *   1st function will be called when the deferred is resolved.
   *   2nd function that is called when the deferred is rejected.
   *   3rd one will be called when progress notifications are sent.  
   */
  Promise then(Function... f);
}
