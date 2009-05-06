package com.google.gwt.query.client.css;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;

/**
 * A special value for inherited CSS properties,
 */
public class Inherit extends JavaScriptObject {

  protected Inherit() {
  }

  public static Inherit create() {
    return GWT.isScript() ? createWeb() : createHosted();
  }

  private static native Inherit createWeb() /*-{
    return "inherit";
  }-*/;

  private static native Inherit createHosted() /*-{
    return ["inherit"];
  }-*/;

  final public String value() {
    return GWT.isScript() ? valueWeb() : valueHosted();
  }

  private native String valueWeb() /*-{
    return this;
  }-*/;

  private native String valueHosted() /*-{
    return this[0];
  }-*/;
}