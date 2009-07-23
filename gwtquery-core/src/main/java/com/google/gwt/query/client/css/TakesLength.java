package com.google.gwt.query.client.css;

import com.google.gwt.dom.client.Style;

/**
 * Interface to be implemented by properties which take length units.
 */
public interface TakesLength {

  void setLength(Style s, Length p);
}
