package com.google.gwt.query.client.impl;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;

public interface HasSelector {
  
  /**
   * Parse and execute a given selector expression given a context.
   *
   * @param selector the CSS selector expression
   * @param ctx      the DOM node to use as a context
   * @return a list of matched nodes
   */
  NodeList<Element> select(String selector, Node ctx);

}
