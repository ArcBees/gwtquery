package com.google.gwt.query.client.css;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;

/**
 * Percentage type constructors.
 */
public class Percentage extends JavaScriptObject {

  protected Percentage() {
  }

  /**
   * Size in percentage units.
   */
  public static Percentage pct(int amt) {
    return GWT.isScript() ? createWeb(amt + "%") : createHosted(amt + "%");
  }

  private static native Percentage createWeb(String pct) /*-{
    return pct;
  }-*/;

  private static native Percentage createHosted(String pct) /*-{
    return [pct];
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

