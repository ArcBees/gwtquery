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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Implementation of jQuery.Callbacks for gwtquery.
 */
public class Callbacks {

  /**
   * Iterface used for callbacks which could cancel the execution
   * when returning false.
   */
  public interface Callback {
    /**
     * Return false to avoid executing the rest of functions.
     */
    boolean f(Object... objects);
  }

  private List<Object> stack = new ArrayList<>();

  private boolean done = false;

  private List<Object> memory = null;

  private boolean isOnce, isMemory, isUnique, stopOnFalse;

  public Callbacks() {
    this("");
  }

  /**
   * Create a new Callbacks object with options given as a space delimited string.
   * 
   * Valid options are:
   * 
   * once, memory, unique, stopOnFalse
   */
  public Callbacks(String options) {
    isOnce = options.contains("once");
    isMemory = options.contains("memory");
    isUnique = options.contains("unique");
    stopOnFalse = options.contains("stopOnFalse");
  }

  /**
   * Add a Callback or a collection of callbacks to a callback list.
   * 
   */
  public Callbacks add(Callback... c) {
    addAll((Object[]) c);
    return this;
  }

  /**
   * Add a Callback or a collection of callbacks to a callback list.
   */
  public Callbacks add(com.google.gwt.core.client.Callback<?, ?>... c) {
    addAll((Object[]) c);
    return this;
  }

  /**
   * Add a Function or a collection of Function to a callback list.
   */
  public Callbacks add(Function... f) {
    addAll((Object[]) f);
    return this;
  }

  /**
   * Disable a callback list from doing anything more.
   */
  public Callbacks disable() {
    stack = null;
    done = true;
    return this;
  }

  /**
   * lock.
   */
  public Callbacks lock() {
    if (!isMemory) {
      disable();
    }
    stack = null;
    return this;
  }

  /**
   * Call all of the callbacks with the given arguments.
   */
  public Callbacks fire(Object... o) {
    if (!done) {
      done = isOnce;
      if (isMemory) {
        memory = new ArrayList<>(Arrays.asList(o));
      }
      if (stack != null)
        for (Object c : stack) {
          if (!run(c, o) && stopOnFalse) {
            break;
          }
        }
    }
    return this;
  }

  /**
   * Remove a callback or a collection of callbacks from a callback list.
   */
  public Callbacks remove(Object... o) {
    stack.removeAll(Arrays.asList(o));
    return this;
  }

  private void addAll(Object... o) {
    for (Object c : o) {
      if (!done && stack != null && c != null && (!isUnique || !stack.contains(c))) {
        stack.add(c);
      }
      // In jQuery add always is run when memory is true even when unique is set
      if (isMemory && memory != null) {
        run(c, memory.toArray());
      }
    }
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private boolean run(Object c, Object... o) {
    // Unbox array inside array when there is only an element.
    // It happens when running filters in Promise.then()
    if (o != null && o.length == 1 && o[0] != null && o[0].getClass().isArray()) {
      o = (Object[]) o[0];
    }
    if (c instanceof Callback) {
      return ((Callback) c).f(o);
    } else if (c instanceof Function) {
      Object r = ((Function) c).f(o);
      return (r instanceof Boolean) ? (Boolean) r : true;
    } else if (c instanceof com.google.gwt.core.client.Callback) {
      ((com.google.gwt.core.client.Callback) c).onSuccess(o);
    }
    return true;
  }

  public String status() {
    return "stack=" + (stack == null ? "null" : stack.size()) + " " + done;
  }
}
