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

/**
 * A helper class to get computed CSS styles for elements on IE6.
 */
public class DocumentStyleImplIE extends DocumentStyleImpl {

  /**
   * Return the string value of a css property of an element. 
   * IE needs a special workaround to handle opacity.
   */
  @Override
  public String curCSS(Element elem, String name) {
    if ("opacity".equalsIgnoreCase(name)) {
      Style s = elem.getStyle();
      String o = s.getProperty("filter");
      if (o != null) {
        return !o.matches(".*opacity=.*") ? "1" : ("" + 
            (Double.valueOf(o.replaceAll("[^\\d]", "")) / 100));
      }
      o = s.getProperty("opacity");
      return o == null || o.length() == 0 ? "1" : o;
    }
    return super.curCSS(elem, name);
  }

  /**
   * Fix style property names.
   */
  @Override
  public String fixPropertyName(String name) {
    name = super.fixPropertyName(name);
    if ("cssFloat".equals(name)) {
      return "styleFloat";
    } else if ("class".equals(name)) {
      return "className";
    }
    return name;
  }

  /**
   * Remove a style property from an element.
   */
  public native void removeStyleProperty(Element elem, String prop) /*-{
    elem.style.removeAttribute(prop);
  }-*/;

  /**
   * Set the value of a style property of an element. 
   * IE needs a special workaround to handle opacity
   */
  @Override
  public void setStyleProperty(String prop, String val, Element e) {
    if ("opacity".equals(prop)) {
      e.getStyle().setProperty("zoom", "1");
      e.getStyle().setProperty("filter",
          "alpha(opacity=" + (int) (Double.valueOf(val) * 100) + ")");
    } else {
      super.setStyleProperty(prop, val, e);
    }
  }

  @Override
  protected native String getComputedStyle(Element elem, String name,
      String pseudo) /*-{
    // code lifted from jQuery
    var style = elem.style;
    var camelCase = name.replace(/\-(\w)/g, function(all, letter){
        return letter.toUpperCase();
    });
    var ret = elem.currentStyle[ name ] || elem.currentStyle[ camelCase ];
    // From the awesome hack by Dean Edwards
    // http://erik.eae.net/archives/2007/07/27/18.54.15/#comment-102291
    // If we're not dealing with a regular pixel number
    // but a number that has a weird ending, we need to convert it to pixels
    if ( !/^\d+(px)?$/i.test( ret ) && /^\d/.test( ret ) ) {
    // Remember the original values
    var left = style.left, rsLeft = elem.runtimeStyle.left;
    // Put in the new values to get a computed value out
    elem.runtimeStyle.left = elem.currentStyle.left;
    style.left = ret || 0;
    ret = style.pixelLeft + "px";
    // Revert the changed values
    style.left = left;
    elem.runtimeStyle.left = rsLeft;
    }
    return ret;
  }-*/;
}