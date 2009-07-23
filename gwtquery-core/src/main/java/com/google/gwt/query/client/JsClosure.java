package com.google.gwt.query.client;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Overlay type representing a Javascript closure.
 */
public class JsClosure extends JavaScriptObject {

  protected JsClosure() {
  }

  /**
   * Invoke the closure with no arguments and expecting no return value.
   */
  public final native void invoke() /*-{
     return this();
  }-*/;
}
