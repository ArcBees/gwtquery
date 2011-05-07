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

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.query.client.js.JsNodeArray;

/**
 * Core Selector engine functions, and native JS utility functions.
 */
public class SelectorEngine implements HasSelector {

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
  
  public NodeList<Element> querySelectorAll(String selector, Node ctx) {
    if (!hasQuerySelector) {
      return impl.select(selector, ctx);
    }
    try {
      return querySelectorAllImpl(selector, ctx);
    } catch (Exception e) {
      return impl.select(selector, ctx);
    }
  }

  public static native NodeList<Element> querySelectorAllImpl(String selector,
      Node ctx) /*-{
      return ctx.querySelectorAll(selector);
  }-*/;

  public static NodeList<Element> xpathEvaluate(String selector, Node ctx) {
    return xpathEvaluate(selector, ctx, JsNodeArray.create());
  }

  public static native NodeList<Element> xpathEvaluate(String selector,
      Node ctx, JsNodeArray r) /*-{
      var node;
      var ownerDoc = ctx && (ctx.ownerDocument || ctx );
      var evalDoc = ownerDoc ? ownerDoc : $doc;
      var result = evalDoc.evaluate(selector, ctx, null, 0, null);      
      while ((node = result.iterateNext())) {
          r.push(node);
      }
      return r;
  }-*/;

  public final SelectorEngineImpl impl;

  protected Node root = Document.get();
  
  public static final boolean hasQuerySelector = hasQuerySelectorAll();

  public SelectorEngine() {
    impl = (SelectorEngineImpl) GWT.create(SelectorEngineImpl.class);
  }

  public Node getRoot() {
    return root;
  }
  
  public NodeList<Element> select(String selector, Node ctx) {
    return impl.select(selector, ctx);
  }
 
  public void setRoot(Node root) {
    assert root != null;
    this.root = root;
  }
  
  protected JsNodeArray veryQuickId(Node context, String id) {
    JsNodeArray r = JsNodeArray.create();
    if (context.getNodeType() == Node.DOCUMENT_NODE) {
      r.addNode(((Document) context).getElementById(id));
      return r;
    } else {
      r.addNode(context.getOwnerDocument().getElementById(id));
      return r;
    }
  }
  
  public String getName() {
    return getClass().getName().replaceAll("^.*\\.", "");
  }
  
  public boolean isDegradated() {
    return !hasQuerySelector;
  }
  
  /**
   * Check if the browser has native support for css selectors
   */
  public static native boolean hasQuerySelectorAll() /*-{
    return $doc.location.href.indexOf("_force_no_native") < 0 &&
           $doc.querySelectorAll && 
           /native/.test(String($doc.querySelectorAll)) ? true : false; 
  }-*/;
  
}
