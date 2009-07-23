package com.google.gwt.query.client.css;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;

/**
 * Length type constructors.
 */
public class Length extends JavaScriptObject {

  protected Length() {
  }

  /**
   * Size in pixels.
   */
  public static Length px(int amt) {
    return GWT.isScript() ? createWeb(amt + "px") : createHosted(amt + "px");
  }

  /**
   * Size as multiple of the 'font-size' of the relevant font.
   */
  public static Length em(int amt) {
    return GWT.isScript() ? createWeb(amt + "em") : createHosted(amt + "em");
  }

  /**
   * Size as multiple of the 'x-height' of the relevant font.
   */
  public static Length ex(int amt) {
    return GWT.isScript() ? createWeb(amt + "ex") : createHosted(amt + "ex");
  }

  /**
   * Size in picas.
   */
  public static Length pc(int amt) {
    return GWT.isScript() ? createWeb(amt + "pc") : createHosted(amt + "pc");
  }

  /**
   * Size in millimeters.
   */
  public static Length mm(int amt) {
    return GWT.isScript() ? createWeb(amt + "mm") : createHosted(amt + "mm");
  }

  /**
   * Size in centimeters.
   */
  public static Length cm(int amt) {
    return GWT.isScript() ? createWeb(amt + "cm") : createHosted(amt + "cm");
  }

  /**
   * Size in inches.
   */
  public static Length in(int amt) {
    return GWT.isScript() ? createWeb(amt + "in") : createHosted(amt + "in");
  }

  private static native Length createWeb(String pct) /*-{
    return pct;
  }-*/;

  private static native Length createHosted(String pct) /*-{
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
