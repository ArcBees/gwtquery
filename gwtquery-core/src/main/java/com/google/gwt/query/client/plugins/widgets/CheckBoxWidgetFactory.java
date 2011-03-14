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
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.user.client.ui.CheckBox;

/**
 * 
 * 
 */
public class CheckBoxWidgetFactory implements WidgetFactory<CheckBox> {

  public CheckBox create(Element e) {
    
    CheckBox checkBox = new CheckBox(e.getInnerText());
    if ("input".equalsIgnoreCase(e.getTagName())){
      copyAttributes((InputElement) e.cast(), (InputElement) checkBox.getElement().cast());
    }
    WidgetsUtils.replaceOrAppend(e, checkBox);
    return checkBox;

  }
  
  protected String getEquivalentTagName(){
    return "input";
  }

  protected void copyAttributes(InputElement source, InputElement destination) {
    
    destination.setAccessKey(source.getAccessKey());
    destination.setDisabled(source.isDisabled());
    destination.setSize(source.getSize());
    destination.setName(source.getName());
    destination.setValue(source.getValue());    
  }

}