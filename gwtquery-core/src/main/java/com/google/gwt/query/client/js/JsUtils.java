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
import com.google.gwt.dom.client.NodeList;
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
   * Camelize style property names. for instance: font-name -> fontName
   */
  public static native String camelize(String s)/*-{
    return s.replace(/\-(\w)/g, function(all, letter) {
    return letter.toUpperCase();
    });
  }-*/;

  /**
   * Merge the oldNodes list into the newNodes one.
   * If oldNodes is null, a new list will be created and returned.
   * If oldNodes is not null, a new list will be created depending on
   * the create flag.
   */
  public static NodeList<Element> copyNodeList(NodeList<Element> oldNodes, NodeList<Element> newNodes, boolean create) {
    NodeList<Element> ret = oldNodes == null || create ? JsNodeArray.create() : oldNodes;
    JsCache idlist = JsCache.create();
    for (int i = 0; oldNodes != null && i < oldNodes.getLength(); i++) {
      Element e = oldNodes.getItem(i);
      idlist.put(e.hashCode(), 1);
      if (create) {
        ret.<JsNodeArray>cast().addNode(e, i);
      }
    }
    for (int i = 0, l = newNodes.getLength(), j = ret.getLength(); i < l; i++) {
      Element e = newNodes.getItem(i);
      if (!idlist.exists(e.hashCode())) {
        ret.<JsNodeArray>cast().addNode(newNodes.getItem(i), j++);
      }
    }
    return ret;
  }

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
   * Hyphenize style property names. for instance: fontName -> font-name
   */
  public static native String hyphenize(String name) /*-{
    return name.replace(/([A-Z])/g, "-$1" ).toLowerCase();
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
        cache.put(id, 1);
        ret.push(e);
      }
    }    
    return ret;
  }
}
