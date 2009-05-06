package com.google.gwt.query.client.css;

import com.google.gwt.dom.client.Style;

/**
 * Interface to be implemented by properties which take percentage units.
 */
public interface TakesPercentage {

  void setPercentage(Style s, Percentage p);
}
