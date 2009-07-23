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

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.query.client.impl.SelectorEngineImpl;

/**
 * Core Selector engine functions, and native JS utility functions.
 */
public class SelectorEngine {

  public static native boolean eq(String s1, String s2) /*-{
       return s1 == s2;
    }-*/;

  public static native NodeList<Element> getElementsByClassName(String clazz,
      Node ctx) /*-{
        return ctx.getElementsByClassName(clazz);
    }-*/;

  public static native Node getNextSibling(Node n) /*-{
       return n.nextSibling || null; 
    }-*/;

  public static native Node getPreviousSibling(Node n) /*-{
       return n.previousSibling || null; 
    }-*/;

  public static native <T> T or(T s1, T s2) /*-{
       return s1 || s2;
    }-*/;

  public static native NodeList<Element> querySelectorAll(String selector) /*-{
      return $doc.querySelectorAll(selector);
  }-*/;

  public static native NodeList<Element> querySelectorAll(String selector,
      Node ctx) /*-{
      return ctx.querySelectorAll(selector);
  }-*/;

  public static boolean truth(String a) {
    return GWT.isScript() ? truth0(a) : a != null && !"".equals(a);
  }

  public static boolean truth(JavaScriptObject a) {
    return GWT.isScript() ? truth0(a) : a != null;
  }

  public static NodeList<Element> xpathEvaluate(String selector, Node ctx) {
    return xpathEvaluate(selector, ctx, JSArray.create());
  }

  public static native NodeList<Element> xpathEvaluate(String selector,
      Node ctx, JSArray r) /*-{
      var node;
      var result = $doc.evaluate(selector, ctx, null, 0, null);
      while ((node = result.iterateNext())) {
          r.push(node);
      }
      return r;
  }-*/;

  private static native boolean truth0(String a) /*-{
       return a;
    }-*/;

  private static native boolean truth0(JavaScriptObject a) /*-{
         return a;
      }-*/;

  private SelectorEngineImpl impl;

  public SelectorEngine() {
    impl = (SelectorEngineImpl) GWT.create(SelectorEngineImpl.class);
  }

  public NodeList<Element> select(String selector, Node ctx) {
    return impl.select(selector, ctx);
  }

  protected JSArray veryQuickId(Node context, String id) {
    JSArray r = JSArray.create();
    if (context.getNodeType() == Node.DOCUMENT_NODE) {
      r.addNode(((Document) context).getElementById(id));
      return r;
    } else {
      r.addNode(context.getOwnerDocument().getElementById(id));
      return r;
    }
  }
}
