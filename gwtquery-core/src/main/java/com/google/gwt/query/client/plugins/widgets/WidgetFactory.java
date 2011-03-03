package com.google.gwt.query.client.plugins.widgets;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Widget;

/**
   * Factory interface
   * 
   * @param <W>
   * @param <O>
   */
  public interface WidgetFactory<W extends Widget> {
    public W create(Element e);
  }