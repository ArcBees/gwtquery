package com.google.gwt.query.client.css;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Style;

/**
 * This property specifies whether a box should float to the left, right, or not
 * at all. It may be set for any element, but only applies to elements that
 * generate boxes that are not absolutely positioned.
 */
public class Float implements CssProperty<Float.FloatValue> {

  public static void init() {
    CSS.FLOAT = new Float();
    CSS.FLOAT_LEFT = FloatValue.create("left");
    CSS.FLOAT_RIGHT = FloatValue.create("right");
    CSS.FLOAT_NONE = FloatValue.create("none");
  }

  public void set(Style s, FloatValue value) {
    s.setProperty("float", value.value());
  }

  public String get(Style s) {
    return s.getProperty("float");
  }

  final static public class FloatValue extends JavaScriptObject {

    protected FloatValue() {
    }

    protected static FloatValue create(String val) {
      return GWT.isScript() ? createWeb(val) : createHosted(val);
    }

    public String value() {
      return GWT.isScript() ? valueWeb() : valueHosted();
    }

    private static native FloatValue createWeb(String val) /*-{
      return val;
    }-*/;

    private static native FloatValue createHosted(String val) /*-{
      return [val];
    }-*/;

    private native String valueWeb() /*-{
       return this;
    }-*/;

    private native String valueHosted() /*-{
       return this[0];
    }-*/;
  }
}