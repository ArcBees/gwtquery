package com.google.gwt.query.client.css;

import com.google.gwt.dom.client.Style;

/**
 * An enumerated CSS property with values of type T.
 */
public interface CssProperty<T> {

  /**
   * Set the style to the given value.
   *
   * @param value a value from the enumerated type T
   */
  void set(Style s, T value);

  /**
   * Return the value of the property as an enumerated type, or null, if the
   * value falls outside the enumerated set.
   */
  String get(Style s);
}
