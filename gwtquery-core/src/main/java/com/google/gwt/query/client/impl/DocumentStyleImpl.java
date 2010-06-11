/*
 * Copyright 2009 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.gwt.query.client.impl;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.query.client.SelectorEngine;

/**
 * A helper class to get computed CSS styles for elements.
 */
public class DocumentStyleImpl {

  /**
   * Camelize style property names.
   *  for instance:
   *   font-name -> fontName 
   */
  public static native String camelize(String s)/*-{
    return s.replace(/\-(\w)/g, function(all, letter) {
    return letter.toUpperCase();
    });
  }-*/;

  /**
   * Hyphenize style property names.
   *  for instance:
   *   fontName -> font-name 
   */
  public static native String hyphenize(String name) /*-{
    return name.replace(/([A-Z])/g, "-$1" ).toLowerCase();
  }-*/;

  /**
   * Return the string value of a css property of an element. 
   */
  public String curCSS(Element elem, String prop) {
    prop = fixPropertyName(prop);
    Style s = elem.getStyle();
    if (SelectorEngine.truth(s.getProperty(prop))) {
      return s.getProperty(prop);
    } else {
      prop = hyphenize(prop);
      return getComputedStyle(elem, prop, null);
    }
  }

  /**
   * Fix style property names.
   */
  public String fixPropertyName(String name) {
    if ("float".equalsIgnoreCase(name)) {
      return "cssFloat";
    } else if ("for".equalsIgnoreCase(name)) {
      return "htmlFor";
    }
    return camelize(name);
  }

  /**
   * Remove a style property from an element.
   */
  public void removeStyleProperty(Element elem, String prop) {
    elem.getStyle().setProperty(prop, "");
  }

  /**
   * Set the value of a style property of an element.
   */
  public void setStyleProperty(String prop, String val, Element e) {
    prop = fixPropertyName(prop);
    // put it in lower-case only when all letters are upper-case, to avoid 
    // modifying already camelized properties
    if (prop.matches("^[A-Z]+$")) {
      prop = prop.toLowerCase();
    }
    prop = camelize(prop);
    if (val == null || val.trim().length() == 0) {
      removeStyleProperty(e, prop);
    } else {
      e.getStyle().setProperty(prop, val);
    }
  }

  protected native String getComputedStyle(Element elem, String name,
      String pseudo) /*-{
    var cStyle = $doc.defaultView.getComputedStyle( elem, pseudo );
    return cStyle ? cStyle.getPropertyValue( name ) : null;
  }-*/;

}
