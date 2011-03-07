package com.google.gwt.query.client.plugins.widgets;

import com.google.gwt.dom.client.Element;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.user.client.ui.Widget;

public class WidgetsUtils {

  /**
   * Test if the tag name of the element is one of tag names given in parameter
   * 
   * @param tagNames
   * @return
   */
  public static boolean matchesTags(Element e, String... tagNames) {

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
   * Old element classes will be copied to the new widget.
   */
   public static void replace(Element oldElement, Element newElement) {
    assert oldElement != null && newElement != null;
    GQuery.$(oldElement).replaceWith(newElement);

    String c = oldElement.getClassName();
    if (!c.isEmpty()) {
      newElement.addClassName(c);
    }
   }
   
   /**
    * Replace a dom element by a widget.
    * If the widget implements Attachable the method attach will be called.
    * Old element classes will be copied to the new widget.
    */
   public static void replace(Element e, Widget widget)  {
     assert e != null && widget != null;
     replace(e, widget.getElement());

     if (widget instanceof Attachable) {
       ((Attachable)widget).attach();
     }
   }

   
}
