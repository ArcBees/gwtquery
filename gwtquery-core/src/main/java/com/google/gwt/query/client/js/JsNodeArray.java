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
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;

/**
 * A Lightweight JSO class easily handle a node list.
 */
public class JsNodeArray extends NodeList<Element> {

  public static JsNodeArray create() {
    return create((Node) null);
  }

  public static native JsNodeArray create(Node node) /*-{
    return node ? [node] : [];
  }-*/;

  public static JsNodeArray create(NodeList<?> nl) {
    JsNodeArray ret = create((Node) null);
    ret.pushAll(nl);
    return ret;
  }

  protected JsNodeArray() {
  }

  public final void addNode(Node n) {
    c().add(n);
  }

  public final void addNode(Node n, int i) {
    c().add(i, n);
  }

  public final void concat(JsNodeArray ary) {
    c().concat(ary.c());
  }

  public final Element get(int i) {
    return getElement(i);
  }

  public final Element getElement(int i) {
    return c().get(i).cast();
  }

  public final Node getNode(int i) {
    return c().get(i);
  }

  public final int size() {
    return c().length();
  }

  private JsObjectArray<Node> c() {
    return cast();
  }

  public final void pushAll(JavaScriptObject prevElem) {
    c().pushAll(prevElem);
  }

  public final Element[] elements() {
    Element[] ret = new Element[size()];
    for (int i = 0; i < size(); i++) {
      ret[i] = getElement(i);
    }
    return ret;
  }
}
