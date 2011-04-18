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
package com.google.gwt.query.client.js;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Element;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.user.client.DOM;

/**
 * A bunch of utility methods for GQuery.
 * 
 * These methods could be moved to $ class, but the class
 * doesn't work right now.
 */
public class JsUtils {

  /**
   * Use the method in the gquery class $(elem).cur(prop, force);
   */
  @Deprecated
  public static double cur(Element elem, String prop, boolean force) {
    return GQuery.$(elem).cur(prop, force);
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
    JsCache cache = JsCache.create();
    for (int i = 0; i < a.length(); i++) {
      Element e = a.get(i);
      int id = e.hashCode();
      if (!cache.exists(id)) {
        cache.put(id, true);
        ret.push(e);
      }
    }    
    return ret;
  }

}
