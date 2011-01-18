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
import com.google.gwt.query.client.GQUtils;

/**
 * A helper class to get computed CSS styles for elements.
 */
public class DocumentStyleImpl {

  /**
   * Camelize style property names. for instance: font-name -> fontName
   */
  public static native String camelize(String s)/*-{
    return s.replace(/\-(\w)/g, function(all, letter) {
    return letter.toUpperCase();
    });
  }-*/;

  /**
   * Hyphenize style property names. for instance: fontName -> font-name
   */
  public static native String hyphenize(String name) /*-{
    return name.replace(/([A-Z])/g, "-$1" ).toLowerCase();
  }-*/;

  /**
   * Return the string value of a css property of an element.
   * 
   * The parameter force has a special meaning here: - When force is false,
   * returns the value of the css property defined in the style attribute of the
   * element. - Otherwise it returns the real computed value.
   * 
   * For instance if you define 'display=none' not in the element style but in
   * the css stylesheet, it returns an empty string unless you pass the
   * parameter force=true.
   */
  public String curCSS(Element elem, String name, boolean force) {
    name = fixPropertyName(name);
    //value defined in the element style
    String ret = elem.getStyle().getProperty(name);
    
    if ("height".equalsIgnoreCase(name)) {
      return force ? String.valueOf(getHeight(elem))+"px" : ret;
    }
    if ("width".equalsIgnoreCase(name)) {
      return force ? String.valueOf(getWidth(elem))+"px" : ret;
    }    
    if ("opacity".equalsIgnoreCase(name)) {
      return force ? String.valueOf(getOpacity(elem)) : ret;
    }
    if (!force) {   
      return ret == null ? "" : ret;
    } else {
      return getComputedStyle(elem, hyphenize(name), name, null);
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

  public int getHeight(Element e) {
    return (int) (e.getClientHeight() - num(curCSS(e, "paddingTop", true)) - num(curCSS(e, "paddingBottom", true)));
  }
  
  public double getOpacity(Element e) {
    String o = e.getStyle().getOpacity();
    return GQUtils.truth(o) ? num(o) : 1;
  }
  
  public int getWidth(Element e) {
    return (int) (e.getClientWidth() - num(curCSS(e, "paddingLeft", true)) - num(curCSS(e, "paddingRight", true)));
  }

  /**
   * Return whether the element is visible
   */
  public boolean isVisible(Element e) {
    return !"none".equalsIgnoreCase(curCSS(e, "display", true));
  }

  public double num(String val) {
    val = val.trim().replaceAll("[^\\d\\.\\-]+.*$", "");
    return GQUtils.truth(val) ? Double.parseDouble(val) : 0;
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
  public void setStyleProperty(Element e, String prop, String val) {
    if (e == null || prop == null) {
      return;
    }
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

  protected native String getComputedStyle(Element elem, String hyphenName,
      String camelName, String pseudo) /*-{
    var cStyle = $doc.defaultView.getComputedStyle(elem, pseudo);
    return cStyle ? cStyle.getPropertyValue(hyphenName) : null;
  }-*/;
  
}
