/*
 * Copyright 2009 Google Inc.
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
package com.google.gwt.query.client;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Event;

/**
 * Extend this class to implement functions callbacks.
 */
public abstract class Function {

  /**
   * Override this for GQuery methods which loop over matched elements and
   * invoke a callback on each element.
   */
  public String f(Element e, int i) {
    return "";
  }

  /**
   * Override this for GQuery methods which take a callback, but do not expect a
   * return value, apply to a single element only.
   */
  public void f(Element e) {
  }

  /**
   * Override this method for bound event handlers if you wish to deal with
   * per-handler user data.
   */
  public boolean f(Event e, Object data) {
    return f(e);
  }

  /**
   * Override this method for bound event handlers.
   */
  public boolean f(Event e) {
    f(e.getCurrentTarget());
    return true;
  }
}
