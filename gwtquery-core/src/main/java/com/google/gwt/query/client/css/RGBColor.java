package com.google.gwt.query.client.css;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;

/**
 * Color type constructors.
 */
public class RGBColor extends JavaScriptObject {

  protected RGBColor() {
  }

  public static void init() {
    CSS.AQUA = rgb("aqua");
    CSS.BLACK = rgb("black");
    CSS.FUSCHIA = rgb("fuschia");
    CSS.GRAY = rgb("gray");
    CSS.GREEN = rgb("green");
    CSS.LIME = rgb("lime");
    CSS.MAROON = rgb("lime");
    CSS.NAVY = rgb("navy");
    CSS.OLIVE = rgb("olive");
    CSS.ORANGE = rgb("orange");
    CSS.PURPLE = rgb("purple");
    CSS.RED = rgb("red");
    CSS.SILVER = rgb("silver");
    CSS.TEAL = rgb("teal");
    CSS.WHITE = rgb("white");
    CSS.YELLOW = rgb("yellow");
    CSS.TRANSPARENT = rgb("transparent");
  }

  /**
   * RGB color in hexidecimal.
   */
  public static RGBColor rgb(String hex) {
    return GWT.isScript() ? createWeb(hex) : createHosted(hex);
  }

  /**
   * RGB color as r,g,b triple.
   */
  public static RGBColor rgb(int r, int g, int b) {
    return GWT.isScript() ? createWeb(makeRgb(r, g, b))
        : createHosted(makeRgb(r, g, b));
  }

  private static String makeRgb(int r, int g, int b) {
    return "rgb(" + r + "," + g + "," + b + ")";
  }

  private static native RGBColor createWeb(String pct) /*-{
    return pct;
  }-*/;

  private static native RGBColor createHosted(String pct) /*-{
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