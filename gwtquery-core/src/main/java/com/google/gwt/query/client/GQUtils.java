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
package com.google.gwt.query.client;

import java.util.HashSet;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;

/**
 * A bunch of utility methods for GQuery.
 * 
 * These methods could be moved to $ class, but the class
 * doesn't work right now.
 */
public class GQUtils {

  /**
   * Returns the numeric value of a css property.
   *  
   * The parameter force has a special meaning:
   * - When force is false, returns the value of the css property defined
   *   in the set of style attributes. 
   * - Otherwise it returns the real computed value.   
   */
  public static double cur(Element elem, String prop, boolean force) {
    if (elem.getPropertyString(prop) != null
        && (elem.getStyle() == null || elem.getStyle().getProperty(prop) == null)) {
      return elem.getPropertyDouble(prop);
    }
    GQuery g = GQuery.$(elem);
    String val = g.css(prop, force);
    if ("thick".equalsIgnoreCase(val)) {
      return (5);
    } else if ("medium".equalsIgnoreCase(val)) {
      return (3);
    } else if ("thin".equalsIgnoreCase(val)) {
      return (1);
    }
    if (!val.matches("^[\\d\\.]+.*$")) {
      val = g.css(prop, false); 
    }
    val = val.trim().replaceAll("[^\\d\\.\\-]+.*$", "");
    return val.length() == 0 ? 0 : Double.parseDouble(val);
  }

  /**
   * Compare two numbers using javascript equality.
   */
  public static native boolean eq(double s1, double s2) /*-{
     return s1 == s2;
  }-*/;

  /**
   * Compare two objects using javascript equality.
   */
  public static native boolean eq(Object s1, Object s2) /*-{
     return s1 == s2;
  }-*/;

  /**
   * Load an external javascript library.
   * The inserted script replaces the element with the
   * given id in the document.
   */
  public static void loadScript(String url, String id) {
    GQuery gs = GQuery.$(DOM.createElement("script"));
    GQuery gp = GQuery.$("#" + id).parent();
    if (gp.size() != 1) {
      gp = GQuery.$(GQuery.document.getBody());
    }
    GQuery.$("#" + id).remove();
    gp.append(gs.attr("src", url).attr("type", "text/javascript").attr("id", id));
  }
  
  /**
   * Return the element which is truth in the double scope. 
   */
  public static native double or(double s1, double s2) /*-{
    return s1 || s2;
  }-*/;

  /**
   * Return the element which is truth in the javascript scope.
   */
  public static native <T> T or(T s1, T s2) /*-{
    return s1 || s2;
  }-*/;

  /**
   * Check if a number is true in the javascript scope. 
   */
  public static native boolean truth(double a) /*-{
    return a ? true : false;
  }-*/;

  /**
   * Check if an object is true in the javascript scope. 
   */
  public static native boolean truth(Object a) /*-{
    return a ? true : false;
  }-*/;
  
  /**
   * Remove duplicates from an elements array
   */
  public static JsArray<Element> unique(JsArray<Element> a) {
    JsArray<Element> ret = JavaScriptObject.createArray().cast();
    HashSet<Integer> f = new HashSet<Integer>();
    for (int i = 0; i < a.length(); i++) {
      Element e = a.get(i);
      if (!f.contains(e.hashCode())) {
        f.add(e.hashCode());
        ret.push(e);
      }
    }    
    return ret;
  }

}
