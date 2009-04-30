package com.google.gwt.query.client;

import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;

/**
 * A compiled selector that can be lazily turned into a GQuery.
 */
public interface DeferredGQuery {

  /**
   * The selector which was compiled.
   * @return
   */
    String getSelector();

  /**
   * Evaluate the compiled selector with the given DOM node as a context.
   * Returns the result as a GQuery object.
   * @param ctx
   * @return
   */
    GQuery eval(Node ctx);

  /**
   * Evaluate the compiled selector with the given DOM node as a context.
   * Returns a NodeList as a result.
   * @param ctx
   * @return
   */
    NodeList<Element> array(Node ctx);
}
