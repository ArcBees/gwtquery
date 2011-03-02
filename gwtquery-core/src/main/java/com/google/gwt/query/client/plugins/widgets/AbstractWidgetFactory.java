package com.google.gwt.query.client.plugins.widgets;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Widget;

/**
 * Abstract factory containing useful methods for widget creation
 * 
 */
public abstract class AbstractWidgetFactory<W extends Widget, O extends WidgetOptions>
    implements WidgetFactory<W, O> {

  public W create(Element e, O options) {
    W w = createWidget(e);

    initialize(w, options, e);

    return w;
  }

  protected abstract void initialize(W widget, O options, Element e);

  protected abstract W createWidget(Element e);

  /**
   * Test if the tag name of the element is one of tag names given in parameter
   * 
   * @param tagNames
   * @return
   */
  protected boolean matchesTags(Element e, String... tagNames) {

    assert e != null : "Element cannot be null";

    StringBuilder regExp = new StringBuilder("^(");
    int tagNameLenght = tagNames != null ? tagNames.length : 0;
    for (int i = 0; i < tagNameLenght; i++) {
      regExp.append(tagNames[i].toUpperCase());
      if (i < tagNameLenght - 1) {
        regExp.append("|");
      }
    }
    regExp.append(")$");

    return e.getTagName().toUpperCase().matches(regExp.toString());

  }

}