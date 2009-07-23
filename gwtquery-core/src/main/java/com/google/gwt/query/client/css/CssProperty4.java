package com.google.gwt.query.client.css;

import com.google.gwt.dom.client.Style;

/**
 * An enumerated CSS property with values of type X,Y,W,Z.
 */
public interface CssProperty4<X, Y, Z, W> {

  void set(Style s, X value1, Y value2, Z value3, W value4);

  String get(Style s);
}
