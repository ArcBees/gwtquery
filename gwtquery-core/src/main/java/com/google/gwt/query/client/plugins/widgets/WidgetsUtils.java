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
package com.google.gwt.query.client.plugins.widgets;

import com.google.gwt.dom.client.Element;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.user.client.ui.GqUi;
import com.google.gwt.user.client.ui.Widget;

public class WidgetsUtils {

  static final String[] appendingTags = {
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
  
      //copy class
      String c = oldElement.getClassName();
      if (!c.isEmpty()) {
        newElement.addClassName(c);
      }
      //copy id
      newElement.setId(oldElement.getId());
      //ensure no duplicate id
      oldElement.setId("");
      
    }
   }
   
   /**
    * Replace a dom element by a widget.
    * Old element classes will be copied to the new widget.
    */
   public static void replaceOrAppend(Element e, Widget widget)  {
     assert e != null && widget != null;
     GqUi.detachWidget(widget);
     replaceOrAppend(e, widget.getElement());
     GqUi.attachWidget(widget);
   }
  
}
