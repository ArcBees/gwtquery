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
package com.google.gwt.query.client.css;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.HasCssName;

/**
 * Base class for Css property
 * 
 * @param <T> Class of the value associated with the css property
 */
public abstract class AbstractCssProperty<T extends HasCssName> implements
    TakeCssValue<T> {

  protected class CssSetterImpl implements CssSetter {

    private T cssValue;

    public CssSetterImpl(T cssValue) {

      this.cssValue = cssValue;
    }

    public void applyCss(Element e) {
      assert e != null : "Impossible to apply css to a null object";
      set(e.getStyle(), cssValue);
    }
  }
  
  protected class LengthCssSetter implements CssSetter{
    
    private Length length;
    
    public LengthCssSetter(Length length) {
      this.length = length;
    }
      
    public void applyCss(Element e) {
      assert e != null : "Impossible to apply css to a null object";
      e.getStyle().setProperty(getCssName(), length.getCssName());   
    }
  }

  private String cssName;

  protected AbstractCssProperty(String cssName) {
    this.cssName = cssName;
  }

  public String get(Style s) {
    return s.getProperty(getCssName());
  }

  public String getCssName() {
    return cssName;
  }

  public void set(Style s, T value) {
    s.setProperty(getCssName(), value.getCssName());
  }

  public CssSetter with(T value) {
    return new CssSetterImpl(value);
  }
}
