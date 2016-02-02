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

import static com.google.gwt.query.client.GQuery.document;
import static com.google.gwt.query.client.GQuery.window;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.query.client.Predicate;
import com.google.gwt.query.client.js.JsMap;
import com.google.gwt.query.client.js.JsNodeArray;
import com.google.gwt.query.client.js.JsUtils;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;

import java.util.HashSet;

/**
 * Core Selector engine functions, and native JS utility functions.
 */
public class SelectorEngine implements HasSelector {

  private static DocumentStyleImpl styleImpl;

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

  public static native NodeList<Element> elementsByTagName(String selector,
      Node ctx) /*-{
    return ctx.getElementsByTagName(selector);
  }-*/;

  public static native NodeList<Element> elementsByClassName(String selector,
      Node ctx) /*-{
    return ctx.getElementsByClassName(selector);
  }-*/;

  public static NodeList<Element> veryQuickId(String id, Node ctx) {
    Document d = ctx.getNodeType() == Node.DOCUMENT_NODE
        ? ctx.<Document> cast() : ctx.getOwnerDocument();
    return JsNodeArray.create(d.getElementById(id));
  }

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

  /**
   * Set it to false if all your elements are attached to the DOM and you want to
   * increase filter performance using {@link GQuery#getSelectorEngine()}
   * method.
   */
  public boolean filterDetached = true;

  protected Node root = Document.get();

  public static final boolean hasQuerySelector = hasQuerySelectorAll();

  public static JsMap<String, Predicate> filters;

  static {
    filters = JsMap.create();
    filters.put("visible", new Predicate() {
      public boolean f(Element e, int index) {
        return (e.getOffsetWidth() + e.getOffsetHeight()) > 0 &&
            !"none".equalsIgnoreCase(styleImpl.curCSS(e, "display", true));
      }
    });
    filters.put("hidden", new Predicate() {
      public boolean f(Element e, int index) {
        return !filters.get("visible").f(e, index);
      }
    });
    filters.put("selected", new Predicate() {
      public boolean f(Element e, int index) {
        return e.getPropertyBoolean("selected");
      }
    });
    filters.put("input", new Predicate() {
      public boolean f(Element e, int index) {
        return e.getNodeName().toLowerCase().matches("input|select|textarea|button");
      }
    });
    filters.put("header", new Predicate() {
      public boolean f(Element e, int index) {
        return e.getNodeName().toLowerCase().matches("h\\d");
      }
    });
  }

  public SelectorEngine() {
    impl = (SelectorEngineImpl) GWT.create(SelectorEngineImpl.class);
    GWT.log("GQuery - Created SelectorEngineImpl: " + impl.getClass().getName());
    styleImpl = GWT.create(DocumentStyleImpl.class);
    GWT.log("GQuery - Created DocumentStyleImpl: " + styleImpl.getClass().getName());
  }

  public Node getRoot() {
    return root;
  }

  public NodeList<Element> filter(NodeList<Element> nodes, Predicate p) {
    JsNodeArray res = JsNodeArray.create();
    for (int i = 0, l = nodes.getLength(), j = 0; i < l; i++) {
      Element e = nodes.getItem(i);
      if (p.f(e, i)) {
        res.addNode(e, j++);
      }
    }
    return res;
  }

  public NodeList<Element> filter(NodeList<Element> nodes, String selector) {
    return filter(nodes, selector, filterDetached);
  }

  public NodeList<Element> filter(NodeList<Element> nodes, String selector, boolean filterDetached) {
    JsNodeArray res = JsNodeArray.create();
    if (selector.isEmpty()) {
      return res;
    }
    Element ghostParent = null;
    HashSet<Node> parents = new HashSet<>();
    HashSet<Node> elmList = new HashSet<>();
    for (int i = 0, l = nodes.getLength(); i < l; i++) {
      Node e = nodes.getItem(i);
      if (e == window || e == document || e.getNodeName() == null
          || "html".equalsIgnoreCase(e.getNodeName())) {
        continue;
      }
      elmList.add(e);
      if (filterDetached) {
        Element p = e.getParentElement();
        if (p == null) {
          if (ghostParent == null) {
            ghostParent = Document.get().createDivElement();
            parents.add(ghostParent);
          }
          p = ghostParent;
          p.appendChild(e);
        } else if (!parents.contains(p)) {
          parents.add(p);
        }
      } else if (parents.isEmpty()) {
        parents.add(document);
      }
    }
    for (Node e : parents) {
      NodeList<Element> n = select(selector, e);
      for (int i = 0, l = n.getLength(); i < l; i++) {
        Element el = n.getItem(i);
        if (elmList.remove(el)) {
          res.addNode(el);
        }
      }
    }
    if (ghostParent != null) {
      ghostParent.setInnerHTML(null);
    }
    return res;
  }

  // pseudo selectors which are computed by gquery in runtime
  RegExp gQueryPseudo =
      RegExp.compile(
      "(.*):((visible|hidden|selected|input|header)|((button|checkbox|file|hidden|image|password|radio|reset|submit|text)\\s*(,|$)))(.*)", "i");
  // pseudo selectors which work in engine
  RegExp nativePseudo = RegExp.compile(
      "(.*):([\\w]+):(disabled|checked|enabled|empty|focus)\\s*([:,].*|$)", "i");

  public NodeList<Element> select(String selector, Node ctx) {

    if (nativePseudo.test(selector)) {
      // move gQuery filters at the end to improve performance, and deal with issue #220
      MatchResult r;
      while ((r = nativePseudo.exec(selector)) != null) {
        selector = r.getGroup(1) + ":" + r.getGroup(3);
        if (!r.getGroup(3).equals(r.getGroup(2))) {
          selector += ":" + r.getGroup(2);
        }
        selector += r.getGroup(4);
      }
    }

    if (gQueryPseudo.test(selector)) {
      JsNodeArray res = JsNodeArray.create();
      for (String s : selector.trim().split("\\s*,\\s*")) {
        NodeList<Element> nodes;
        MatchResult a = gQueryPseudo.exec(s);
        if (a != null) {
          String select = a.getGroup(1).isEmpty() ? "*" : a.getGroup(1);
          String pseudo = a.getGroup(2);
          Predicate pred = filters.get(pseudo.toLowerCase());
          if (pred != null) {
            nodes = filter(select(select, ctx), pred);
          } else if (nativePseudo.test(pseudo)) {
            nodes = select(select, ctx);
          } else {
            nodes = select(select + "[type=" + pseudo + "]", ctx);
          }
        } else {
          nodes = select(s, ctx);
        }
        JsUtils.copyNodeList(res, nodes, false);
      }
      return res.<NodeList<Element>> cast();
    } else {
      return impl.select(selector, ctx);
    }
  }

  public native boolean contains(Element a, Element b) /*-{
    return a.contains ? a != b && a.contains(b) : !!(a.compareDocumentPosition(b) & 16)
  }-*/;

  public void setRoot(Node root) {
    assert root != null;
    this.root = root;
  }

  public String getName() {
    return getClass().getName().replaceAll("^.*\\.", "");
  }

  public boolean isDegradated() {
    return !hasQuerySelector;
  }

  /**
   * Check if the browser has native support for css selectors.
   */
  public static native boolean hasQuerySelectorAll() /*-{
    return $doc.location.href.indexOf("_force_no_native") < 0 &&
           typeof $doc.querySelectorAll == 'function';
  }-*/;

  public static native boolean hasXpathEvaluate() /*-{
    return !!$doc.evaluate;
  }-*/;

  /**
   * Return the DocumentStyleImpl used by this selector engine.
   */
  public DocumentStyleImpl getDocumentStyleImpl() {
    return styleImpl;
  }
}
