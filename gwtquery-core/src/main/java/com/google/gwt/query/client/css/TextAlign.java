package com.google.gwt.query.client.css;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Style;

/**
 * This property describes how inline content of a block is aligned.
 */
public class TextAlign implements CssProperty<TextAlign.TAlignValue> {

  public static void init() {
    CSS.TEXT_ALIGN = new TextAlign();
    CSS.LEFT = TAlignValue.create("left");
    CSS.RIGHT = TAlignValue.create("right");
    CSS.CENTER = TAlignValue.create("center");
    CSS.JUSTIFY = TAlignValue.create("justify");
  }

  public void set(Style s, TAlignValue value) {
    s.setProperty("textAlign", value.value());
  }

  public String get(Style s) {
    return s.getProperty("textAlign");
  }

  final static public class TAlignValue extends JavaScriptObject {

    protected TAlignValue() {
    }

    protected static TAlignValue create(String val) {
      return GWT.isScript() ? createWeb(val) : createHosted(val);
    }

    public String value() {
      return GWT.isScript() ? valueWeb() : valueHosted();
    }

    private static native TAlignValue createWeb(String val) /*-{
      return val;
    }-*/;

    private static native TAlignValue createHosted(String val) /*-{
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
