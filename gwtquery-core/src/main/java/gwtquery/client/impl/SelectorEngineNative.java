package gwtquery.client.impl;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Node;

import gwtquery.client.SelectorEngine;

/**
 *
 */
public class SelectorEngineNative extends SelectorEngineImpl {
  public NodeList<Element> select(String selector, Node ctx) {
    return SelectorEngine.querySelectorAll(selector, ctx);
  }
}
