package com.google.gwt.user.client.ui;

import com.google.gwt.dom.client.BodyElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Event;


/**
 * GQuery Utility class to access protected methods in ui package. 
 */
public class GqUi {
  
  
  public static void attachWidget(Widget widget) {
    if (widget != null) {
      
      boolean hasParentWidget = false;
      Element e = widget.getElement();
      e = e.getParentElement();
      BodyElement body = Document.get().getBody();
      while ((e != null) && (body != e)) {
        if (Event.getEventListener(e) != null) {
          hasParentWidget = true;
          break;
        }
        e = e.getParentElement();
      }
      
      if (!hasParentWidget) {
        RootPanel.detachOnWindowClose(widget);
      }
      
      widget.onAttach();
    }
  }
  
  public static void detachWidget(Widget widget){
    if (widget != null) {
      if (widget.isAttached()) {
        widget.removeFromParent();
      }
    }
  }
}
