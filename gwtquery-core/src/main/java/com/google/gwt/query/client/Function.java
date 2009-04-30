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
   * @param e
   * @param i
   * @return
   */
  public String f(Element e,  int i) {
    return "";  
  }

  /**
   * Override this for GQuery methods which take a callback, but do not expect
   * a return value, apply to a single element only.
   * @param e
   */
  public void f(Element e) {
  }

  /**
   * Override this method for bound event handlers if you wish to deal with
   * per-handler user data.
   * @param e
   * @param data
   * @return
   */
  public boolean f(Event e, Object data) {
    return f(e);
  }

  /**
   * Override this method for bound event handlers.
   * @param e
   * @return
   */
  public boolean f(Event e) {
    f(e.getCurrentTarget());
    return true;
  }
}
