package com.google.gwt.query.client.css;

import com.google.gwt.dom.client.Style;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.GWT;

/**
 * This property describes the foreground color of an element's text content.
 */
public class Color implements CssProperty<RGBColor> {

  public Color() {
  }

  public static void init() {
    CSS.COLOR = new Color();
  }

  public void set(Style s, RGBColor value) {
    s.setProperty("color", value.value());
  }

  public String get(Style s) {
    return s.getProperty("color");
  }
}