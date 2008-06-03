package gwtquery.client;

import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;

/**
 * A compiled selector that can be lazily turned into a GQuery
 */
public interface DeferredGQuery {
    String getSelector();
    GQuery eval(Node ctx);
    NodeList<Element> array(Node ctx);
}
