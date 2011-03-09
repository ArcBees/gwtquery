package com.google.gwt.user.client.ui;


/**
 * GQuery Utility class to access protected methods in ui package. 
 */
public class GqUi {
  public static void attachWidget(Widget widget) {
    if (widget != null) {
      widget.onAttach();
      if (widget instanceof RichTextArea != true) {
        RootPanel.detachOnWindowClose(widget);
      }
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
