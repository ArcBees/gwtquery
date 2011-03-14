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
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.RadioButton;

/**
 * 
 * 
 */
public class RadioButtonWidgetFactory implements WidgetFactory<RadioButton> {

  public static class RadioButtonOption{
    
    private String name;
    
    public RadioButtonOption() {
    }
    
    public RadioButtonOption(String name){
      this.name = name;
    }
    
    public String getName() {
      return name;
    }
    
    public void setName(String name) {
      this.name = name;
    }
    
  }
  
  private RadioButtonOption option;
  private String radioName;
  
  public RadioButtonWidgetFactory(RadioButtonOption option) {
    this.option = option;
  }
  
  
  public RadioButton create(Element e) {
    resolveName(e);
    
    RadioButton radioButton = new RadioButton(radioName);
    if ("input".equalsIgnoreCase(e.getTagName())){
      copyAttributes((InputElement) e, (InputElement) radioButton.getElement().cast());
    }else{
      radioButton.setText(e.getInnerText());
    }
    
    WidgetsUtils.replaceOrAppend(e, radioButton);
    
    return radioButton;

  }
  
  protected void resolveName(Element e) {
    if (radioName != null){
      return;
    }
    if (option.getName() != null){
      radioName = option.getName();
    }else if ("input".equals(e.getTagName()) && "radio".equals(((InputElement)e).getType())){
      radioName =  ((InputElement)e).getName();
    }else{
      //create an unique string via DOM.createUniqueId...
      radioName = DOM.createUniqueId();
    }
  }


  protected String getEquivalentTagName(){
    return "input";
  }

  protected void copyAttributes(InputElement source, InputElement destination) {
    
    destination.setAccessKey(source.getAccessKey());
    destination.setDisabled(source.isDisabled());
    destination.setSize(source.getSize());
    destination.setValue(source.getValue());    
  }
  
  

}