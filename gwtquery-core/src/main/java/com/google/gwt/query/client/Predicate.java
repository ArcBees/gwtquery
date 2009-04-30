package com.google.gwt.query.client;

import com.google.gwt.dom.client.Element;

/**
 * A predicate function used by some GQuery methods.
 */
public interface Predicate {
    boolean f(Element e, int index);
}
