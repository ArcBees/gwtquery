/*
 * Copyright 2011, The gwtquery team.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.gwt.user.client.ui;

import java.util.Iterator;

/**
 * GQuery Utility class to access protected methods in ui package.
 */
public class GqUi {

  public static void attachWidget(Widget widget, Widget firstParentWidget) {
    if (widget != null && widget.getParent() == null) {

      if (firstParentWidget == null) {
        RootPanel.detachOnWindowClose(widget);
        widget.onAttach();
      } else if (firstParentWidget instanceof HTMLPanel) {
        ((HTMLPanel) firstParentWidget).add(widget,
            widget.getElement().getParentElement()
            .<com.google.gwt.user.client.Element>cast());
      } else {
        throw new RuntimeException(
            "No HTMLPanel available to attach the widget.");
      }
    }
  }

  public static void detachWidget(final Widget widget) {
    if (widget != null) {
      widget.removeFromParent();
    }
  }

 

  /**
   * This method detach a widget of its parent without do a physical detach (DOM
   * manipulation)
   * 
   * @param w
   */
  public static void doLogicalDetachFromHtmlPanel(Widget w) {
    Widget parent = w.getParent();
    
    if (parent instanceof HTMLPanel) {
      ((HTMLPanel) parent).getChildren().remove(w);
      w.setParent(null);
      
    } else{
      throw new IllegalStateException(
          "You can only use this method to detach a child from an HTMLPanel");
    }
   

  }
  
  public static Iterator<Widget> getChildren(Widget w){
    if(w instanceof Panel){
      return ((Panel)w).iterator();
    }
    if(w instanceof Composite){
      return getChildren(((Composite)w).getWidget());
    }
    
    return null;
  }
}
