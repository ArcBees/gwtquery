package com.google.gwt.query.client.plugins.widgets;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Widget;

public interface WidgetInitializer {
    /**
     * Initialize the newly created widget <code>w</code>. The element <code>e</code>
     * is the element used to construct the widget.
     *
     */
    void initialize(Widget w, Element e);
  }
