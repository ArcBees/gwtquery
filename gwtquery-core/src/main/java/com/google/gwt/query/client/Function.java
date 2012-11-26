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

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.query.client.js.JsUtils;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;

/**
 * Extend this class to implement functions callbacks.
 */
public abstract class Function {

  private com.google.gwt.dom.client.Element element = null;
  private Event event = null;
  private int index = -1;
  private Object[] data = new Object[0];

  public <T extends com.google.gwt.dom.client.Element> T getElement() {
    return element.<T>cast();
  }

  public <T extends com.google.gwt.dom.client.Element> void setElement(T e) {
    element = e;
  }

  public void setEvent(Event e) {
    event = e;
    element = e != null ? e.getCurrentEventTarget().<com.google.gwt.dom.client.Element>cast() : null;
  }

  public Event getEvent() {
    return event;
  }

  public void setIndex(int i) {
    index = i;
  }

  public Object[] getData() {
    return data;
  }

  public void setData(Object...data) {
    this.data = data;
  }

  public Object getDataObject() {
    return getDataObject(0);
  }

  public Object getDataObject(int idx) {
    return data != null && data.length > idx ? data[idx] : null;
  }

  public Properties getDataProperties() {
    return getDataProperties(0);
  }

  public Properties getDataProperties(int idx) {
    Object o = getDataObject(idx);
    if (o != null && o instanceof JavaScriptObject) {
      return (Properties)o;
    }
    return null;
  }

  public void setData(boolean b) {
    setData(Boolean.valueOf(b));
  }

  public void setData(double b) {
    setData(Double.valueOf(b));
  }

  public void setDataObject(Object data) {
    setData(data);
  }

  public int getIndex() {
    return index;
  }

  /**
   * Override this for methods which invoke a cancel action.
   *
   * @param e takes a com.google.gwt.dom.client.Element.
   *
   */
  public void cancel(com.google.gwt.dom.client.Element e) {
    setElement(e);
   // This has to be the order of calls
    cancel(e.<com.google.gwt.user.client.Element>cast());
  }

  /**
   * Override this for methods which invoke a cancel action.
   *
   * @param e takes a com.google.gwt.user.client.Element.
   *
   */
  public void cancel(com.google.gwt.user.client.Element e) {
    setElement(e);
  }

  /**
   * Override this to define a function which does not need any parameter.
   */
  public void f() {
    throw new RuntimeException("You have to override the adequate method to handle " +
        "this action, or you have to override 'public void f()' to avoid this error");
  }

  /**
   * Override this for GQuery methods which loop over matched elements and
   * invoke a callback on each element.
   *
   * @param e takes a com.google.gwt.dom.client.Element.
   *
   */
  public Object f(com.google.gwt.dom.client.Element e, int i) {
    setElement(e);
    setIndex(i);
    // This has to be the order of calls
    return f(e.<com.google.gwt.user.client.Element>cast(), i);
  }

  /**
   * Override this for GQuery methods which loop over matched elements and
   * invoke a callback on each element.
   *
   * @param e takes a com.google.gwt.user.client.Element.
   *
   */
  public Object f(com.google.gwt.user.client.Element e, int i) {
    setElement(e);
    setIndex(i);
    Widget w = GQuery.getAssociatedWidget(e);
    if (w != null){
      f(w, i);
    } else {
      f(e.<com.google.gwt.dom.client.Element>cast());
    }
    return null;
  }

  /**
   * Override this for GQuery methods which loop over matched widgets and
   * invoke a callback on each widget.
   *
   *  NOTE: If your query has non-widget elements you might need to override
   * 'public void f()' or 'public void f(Element e)' to handle these elements and
   *  avoid a runtime exception.
   */
  public Object f(Widget w, int i) {
    setElement(w.getElement());
    setIndex(i);
    f(w);
    return null;
  }

  /**
   * Override this method for bound callbacks
   */
  public void f(Object... data) {
    fe(data);
  }

  /**
   * Override this method for bound callbacks
   */
  public void f(int i, Object data) {
    f(i, new Object[]{data});
  }

  /**
   * Override this method for bound callbacks
   */
  public void f(int i, Object... data) {
    setIndex(i);
    setData(data);
    if (data.length == 1 && data[0] instanceof JavaScriptObject) {
      if (JsUtils.isElement((JavaScriptObject)data[0])) {
        setElement((com.google.gwt.dom.client.Element)data[0]);
        f(getElement(), i);
      } else if (JsUtils.isEvent((JavaScriptObject)data[0])) {
        setEvent((Event)data[0]);
        f(getEvent());
      } else {
        f();
      }
    }
  }

  /**
   * Override this method for bound event handlers if you wish to deal with
   * per-handler user data.
   */
  public boolean f(Event e, Object data) {
    setData(data);
    setEvent(e);
    return f(e);
  }

  /**
   * Override this method for bound event handlers.
   *
   * @return boolean: false means stop propagation and prevent default
   */
  public boolean f(Event e) {
    setEvent(e);
    f(element);
    return true;
  }

  /**
   * Override this for GQuery methods which take a callback and do not expect a
   * return value.
   *
   * @param e takes a com.google.gwt.dom.client.Element
   */
  public void f(com.google.gwt.dom.client.Element e) {
    setElement(e);
   // This has to be the order of calls
    f(e.<com.google.gwt.user.client.Element>cast());
  }

  /**
   * Override this for GQuery methods which take a callback and do not expect a
   * return value.
   *
   * @param e takes a com.google.gwt.user.client.Element
   */
  private boolean loop = false;
  public void f(com.google.gwt.user.client.Element e) {
    setElement(e);
    Widget w = e != null ? GQuery.getAssociatedWidget(e) : null;
    if (w != null){
      loop = true;
      f(w);
    } else {
      f();
    }
  }

  /**
   * Override this for GQuery methods which take a callback, but do not expect a
   * return value, apply to a single widget.
   *
   *  NOTE: If your query has non-widget elements you might need to override
   * 'public void f()' or 'public void f(Element e)' to handle these elements and
   *  avoid a runtime exception.
   */
  public void f(Widget w){
    setElement(w.getElement());
    if (loop) {
      loop = false;
      f();
    } else {
      f(w.getElement().<com.google.gwt.dom.client.Element>cast());
    }
  }

  /**
   * Methods fe(...) should be used from asynchronous contexts so as we can
   * catch the exception and send it to the GWT UncaughtExceptionHandler.
   * They are intentionally final to avoid override them
   */
  public final void fe() {
    fe(data);
  }

  /**
   * Methods fe(...) should be used from asynchronous contexts so as we can
   * catch the exception and send it to the GWT UncaughtExceptionHandler
   * They are intentionally final to avoid override them
   */
  public final void fe(Object data) {
    fe(new Object[]{data});
  }

  /**
   * Methods fe(...) should be used from asynchronous contexts so as we can
   * catch the exception and send it to the GWT UncaughtExceptionHandler
   * They are intentionally final to avoid override them
   */
  public final void fe(Object... data) {
    setData(data);
    if (GWT.getUncaughtExceptionHandler() != null) {
      try {
        f();
      } catch (Exception e) {
        GWT.getUncaughtExceptionHandler().onUncaughtException(e);
      }
      return;
    }
    f();
  }

  /**
   * Methods fe(...) should be used from asynchronous contexts so as we can
   * catch the exception and send it to the GWT UncaughtExceptionHandler
   * They are intentionally final to avoid override them
   */
  public final boolean fe(Event ev, Object  data) {
    if (GWT.getUncaughtExceptionHandler() != null) {
      try {
        return f(ev, data);
      } catch (Exception e) {
        GWT.getUncaughtExceptionHandler().onUncaughtException(e);
      }
      return true;
    }
    return f(ev, data);
  }

  /**
   * Methods fe(...) should be used from asynchronous contexts so as we can
   * catch the exception and send it to the GWT UncaughtExceptionHandler
   * They are intentionally final to avoid override them
   */
  public final void fe(com.google.gwt.dom.client.Element elem) {
    if (GWT.getUncaughtExceptionHandler() != null) {
      try {
        f(elem.<com.google.gwt.dom.client.Element>cast());
      } catch (Exception e) {
        GWT.getUncaughtExceptionHandler().onUncaughtException(e);
      }
      return;
    }
    f(elem.<com.google.gwt.dom.client.Element>cast());
  }
}
