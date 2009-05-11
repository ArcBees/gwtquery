package com.google.gwt.query.client.css;

import com.google.gwt.dom.client.Style;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.GWT;

/**
 * This property sets the background color of an element, either a <color> value
 * or the keyword 'transparent', to make the underlying colors shine through.
 */
public class BackgroundColor
    implements CssProperty<RGBColor> {

  public static void init() {
    CSS.BACKGROUND_COLOR = new BackgroundColor();
  }

  public void set(Style s, RGBColor value) {
    s.setProperty("backgroundColor", value.value());
  }

  public String get(Style s) {
    return s.getProperty("backgroundColor");
  }
}