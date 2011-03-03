package com.google.gwt.query.client.plugins.widgets;

import com.google.gwt.dom.client.Element;
import com.google.gwt.query.client.GQuery;

public class WidgetsUtils {

  /**
   * Test if the tag name of the element is one of tag names given in parameter
   * 
   * @param tagNames
   * @return
   */
  static boolean matchesTags(Element e, String... tagNames) {

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

  /**
   * replace the <code>oldElement</code> by the <code>newElement</code>
   * 
   * @param oldElement
   * @param newElement
   */
   static void replace(Element oldElement, Element newElement) {
    assert oldElement != null && newElement != null;
    
    GQuery.$(oldElement).replaceWith(newElement);

  }
}
