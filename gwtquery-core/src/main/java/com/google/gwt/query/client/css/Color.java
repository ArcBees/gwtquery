package com.google.gwt.query.client.css;

import com.google.gwt.dom.client.Style;

/**
 * This property describes the foreground color of an element's text content.
 */
public class Color implements CssProperty<RGBColor> {

  public static void init() {
    CSS.COLOR = new Color();
  }

  public Color() {
  }

  public String get(Style s) {
    return s.getProperty("color");
  }

  public void set(Style s, RGBColor value) {
    s.setProperty("color", value.value());
  }
}