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
package com.google.gwt.query.client.plugins.callbacks;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.Properties;
import com.google.gwt.query.client.builders.JsonBuilder;
import com.google.gwt.query.client.js.JsObjectArray;

/**
 * Implementation of jQuery.Callbacks for gwtquery.
 */
public class Callbacks {
  
  /**
   * Iterface used for callbacks which could cancel the execution 
   * when returning false;
   *
   */
  public static interface Callback {
    /**
     * Return false to avoid executing the rest of functions 
     */
    boolean f(Object ...objects);
  }
  
  /**
   * Interface representing the options of a Callbacks collection.
   * 
   * To create an implementation of this interface just call: Callbacks.createOptions()
   */
  public static interface CallbackOptions extends JsonBuilder {
    boolean getMemory();
    boolean getOnce();
    boolean getStopOnFalse();
    boolean getUnique();
    CallbackOptions setMemory();
    CallbackOptions setOnce();
    CallbackOptions setStopOnFalse();
    CallbackOptions setUnique();
  }
  
  public static CallbackOptions createOptions() {
    return GWT.create(CallbackOptions.class);
  }
  
  private JsObjectArray<Object> stack = JsObjectArray.create();
  
  private boolean done = false;
    
  private JsObjectArray<Object> memory = null;
  
  public final CallbackOptions opts;
  
  /**
   * Create a new Callbacks object with default options
   */
  public Callbacks() {
    opts = createOptions();
  }

  /**
   * Create a new Callbacks object with given options
   */
  public Callbacks(CallbackOptions options) {
    opts = options;
  }

  /**
   * Create a new Callbacks object with options given as a space delimited string.
   * 
   * Valid options are:
   * 
   * once, memory, unique, stopOnFalse
   */
  public Callbacks(String options) {
    this();
    opts.load(Properties.create(options.replaceAll("[^\\S]+|$", ":1,")));
  }
  
  /**
   * Add a Callback or a collection of callbacks to a callback list.
   * 
   */
  public Callbacks add(Callback... c) {
    addAll((Object[])c);
    return this;
  }
  
  /**
   * Add a Callback or a collection of callbacks to a callback list.
   */
  public Callbacks add(com.google.gwt.core.client.Callback<?, ?>... c) {
    addAll((Object[])c);
    return this;
  }

  /**
   * Add a Function or a collection of Function to a callback list.
   */
  public Callbacks add(Function... f) {
    addAll((Object[])f);
    return this;
  }
  
  /**
   * Disable a callback list from doing anything more.
   */
  public Callbacks disable() {
    stack = memory = null;
    done = true;
    return this;
  }
  
  /**
   * lock
   */
  public Callbacks lock() {
    if (!opts.getMemory()) {
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
      done = opts.getOnce();
      for (Object c : stack.elements()) {
        if (!run(c, o) && opts.getStopOnFalse()) {
          break;
        }
      }
      if (opts.getMemory()) {
        memory = JsObjectArray.create().add(o);
      }
    }
    return this;
  }
  
  /**
   * Remove a callback or a collection of callbacks from a callback list.
   */
  public Callbacks remove(Object... o) {
    stack.remove(o);
    return this;
  }
  
  private void addAll(Object...o) {
    if (stack != null) {
      for (Object c : o) {
        if (!opts.getUnique() || !stack.contains(c)) {
          stack.add(c);
        }
        // In jQuery add always is run when memory is true even when unique is set
        if (opts.getMemory() && memory != null) {
          run(c, memory.elements());
        }
      }
    }
  }  

  @SuppressWarnings({"unchecked", "rawtypes"})
  private boolean run(Object c, Object...o) {
    if (c instanceof Callback) {
      return ((Callback)c).f(o);
    } else if (c instanceof Function) {
      Object r = ((Function)c).f(o);
      return (r instanceof Boolean) ? (Boolean)r : true;
    } else if (c instanceof com.google.gwt.core.client.Callback) {
      ((com.google.gwt.core.client.Callback)c).onSuccess(o);
    }
    return true;
  }
}
