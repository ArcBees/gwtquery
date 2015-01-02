/*
 * Copyright 2011, The gwtquery team.
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

  @Override
  public int getHeight(Element e) {
    return (int) (e.getOffsetHeight() - num(curCSS(e, "paddingTop", true))
        - num(curCSS(e, "paddingBottom", true))
        - num(curCSS(e, "borderTopWidth", true)) - num(curCSS(e, "borderBottomWidth", true)));
  }

  @Override
  public double getOpacity(Element e) {
    Style s = e.getStyle();
    String o = s.getProperty("filter");
    if (o != null) {
      return !o.matches(".*opacity=.*") ? 1 : Double.valueOf(o.replaceAll("[^\\d]", "")) / 100;
    }
    return super.getOpacity(e);
  }

  @Override
  public int getWidth(Element e) {
    return (int) (e.getOffsetWidth() - num(curCSS(e, "paddingLeft", true))
        - num(curCSS(e, "paddingRight", true))
        - num(curCSS(e, "borderRightWidth", true)) - num(curCSS(e, "borderRightWidth", true)));
  }

  /**
   * Remove a style property from an element.
   */
  public native void removeStyleProperty(Element e, String prop) /*-{
    if (e && e.style && 'removeAttribute' in e)
      e.style.removeAttribute(prop);
  }-*/;

  /**
   * Set the value of a style property of an element.
   * IE needs a special workaround to handle opacity
   */
  @Override
  public void setStyleProperty(Element e, String prop, String val) {
    if ("opacity".equals(prop)) {
      setOpacity(e, val);
    } else {
      super.setStyleProperty(e, prop, val);
    }
  }

  @Override
  protected native String getComputedStyle(Element elem, String hyphenName,
      String camelName, String pseudo) /*-{
    // code lifted from jQuery
    if (!elem.style || !'currentStyle' in elem || !'runtimeStyle' in elem) return null;
    var style = elem.style;
    var ret = elem.currentStyle[hyphenName] || elem.currentStyle[camelName];
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
    return ret ? ""+ret : null;
  }-*/;

  private void setOpacity(Element e, String val) {
    if (val == null || val.trim().length() == 0) {
      val = "1";
    }
    e.getStyle().setProperty("zoom", "1");
    e.getStyle().setProperty("filter",
        "alpha(opacity=" + (int) (Double.valueOf(val) * 100) + ")");
  }
}
