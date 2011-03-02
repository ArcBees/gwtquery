/*
 * Copyright 2011, The gwtquery team.
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
import com.google.gwt.user.client.ui.Widget;

/**
 * Extend this class to implement functions callbacks.
 */
public abstract class Function {
  
  /**
   * Override this for methods which invoke a cancel action.
   */
  public void cancel(Element e) {
  }

  /**
   * Override this to define a function which does not need any parameter.
   */
  public void f() {
    throw new RuntimeException("You have to override the adequate method to handle this action, or you have to override 'public void f()' to avoid this error");
  }

  /**
   * Override this for GQuery methods which loop over matched elements and
   * invoke a callback on each element.
   */
  public Object f(Element e, int i) {
    Widget w = GQuery.getAssociatedWidget(e);
    if (w != null){
      return f(w, i);
    } else {
      f((com.google.gwt.user.client.Element)e);
      return "";
    }
  }

  /**
   * Override this for GQuery methods which loop over matched widgets and
   * invoke a callback on each widget.
   */
  public Object f(Widget w, int i) {
    f(w.getElement());
    return "";
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
    f((com.google.gwt.user.client.Element)e.getCurrentEventTarget().cast());
    return true;
  }
  
  /**
   * Override this for GQuery methods which take a callback and do not expect a
   * return value.
   * 
   * @param e takes a com.google.gwt.dom.client.Element
   */
  public void f(Element e) {
    Widget w = GQuery.getAssociatedWidget(e);
    if (w != null){
      f(w);
    }else{
      f();
    }
  }
  
  /**
   * Override this for GQuery methods which take a callback and do not expect a
   * return value.
   * 
   * @param e takes a com.google.gwt.user.client.Element
   */
  public void f(com.google.gwt.user.client.Element e) {
   f((Element)e);
  }
  
  /**
   * Override this for GQuery methods which take a callback, but do not expect a
   * return value, apply to a single widget.
   * 
   * NOTE: If your query is returning non-widget elements you might need to override 
   * 'public void f()' or 'public void f(Element e)' to handle these elements and
   * avoid a runtime exception. 
   */
  public void f(Widget w){
    f();
  }

}
