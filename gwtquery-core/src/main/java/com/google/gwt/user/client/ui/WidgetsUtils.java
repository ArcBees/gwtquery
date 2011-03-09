package com.google.gwt.user.client.ui;

import com.google.gwt.dom.client.Element;
import com.google.gwt.query.client.GQuery;

public class WidgetsUtils {

  private static final String[] appendingTags = {
    "td", "th", "li"};

  
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
   * If the <code>oldElement</code> is a td, th, li tags, the new element will replaced its content.
   * In other cases, the <code>oldElement</code> will be replaced by the <code>newElement</code>
   *  and the old element classes will be copied to the new element.
   */
   private static void replaceOrAppend(Element oldElement, Element newElement) {
    assert oldElement != null && newElement != null;
    
    if(matchesTags(oldElement, appendingTags)){
      GQuery.$(oldElement).html("").append(newElement);
    }else{
      GQuery.$(oldElement).replaceWith(newElement);
  
      String c = oldElement.getClassName();
      if (!c.isEmpty()) {
        newElement.addClassName(c);
      }
    }
   }
   
   /**
    * Replace a dom element by a widget.
    * If the widget implements Attachable the method attach will be called.
    * Old element classes will be copied to the new widget.
    */
   public static void replaceOrAppend(Element e, Widget widget)  {
     assert e != null && widget != null;
     
     if (widget.isAttached()) {
       widget.removeFromParent();
     }

     replaceOrAppend(e, widget.getElement());
     
     widget.onAttach();
     
     if (widget instanceof RichTextArea != true) {
       RootPanel.detachOnWindowClose(widget);
     }
   }
   
}
