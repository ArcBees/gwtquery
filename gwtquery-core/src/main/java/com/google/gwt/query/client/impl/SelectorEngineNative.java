package com.google.gwt.query.client.impl;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Node;

import com.google.gwt.query.client.SelectorEngine;

/**
 * Runtime selector engine implementation for browsers with native 
 * querySelectorAll support.
 */
public class SelectorEngineNative extends SelectorEngineImpl {
  public NodeList<Element> select(String selector, Node ctx) {
    return SelectorEngine.querySelectorAll(selector, ctx);
  }
}
