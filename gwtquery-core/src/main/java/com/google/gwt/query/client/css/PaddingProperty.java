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

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.HasCssName;

/**
 * The padding properties define the space between the element border and the
 * element content.
 */
public class PaddingProperty extends AbstractCssProperty<Length> {

  public static class ShorthandPaddingProperty implements CssShorthandProperty4<Length, Length, Length, Length>{

    private  ShorthandPaddingProperty() {
    }
    
    public void set(Style s, Length margin1, Length margin2, Length margin3,
        Length margin4) {
      String margin = notNull(margin1)+notNull(margin2)+notNull(margin3)+notNull(margin4);
      s.setProperty(PADDING_PROPERTY, margin.trim());
      
    }

    public String get(Style s) {
      return s.getPadding();
    }

    public String getCssName() {
      return PADDING_PROPERTY;
    }
    
    private String notNull(HasCssName value) {
      return value != null ? value.getCssName() + " " : "";
    }
    
  }
  
  private static String PADDING_BOTTOM_PROPERTY = "paddingBottom";
  private static String PADDING_LEFT_PROPERTY = "paddingLeft";
  private static String PADDING_PROPERTY = "padding";
  private static String PADDING_RIGHT_PROPERTY = "paddingRight";
  private static String PADDING_TOP_PROPERTY = "paddingTop";

  public static void init() {
    CSS.PADDING = new ShorthandPaddingProperty();
    CSS.PADDING_LEFT = new PaddingProperty(PADDING_LEFT_PROPERTY);
    CSS.PADDING_RIGHT = new PaddingProperty(PADDING_RIGHT_PROPERTY);
    CSS.PADDING_TOP = new PaddingProperty(PADDING_TOP_PROPERTY);
    CSS.PADDING_BOTTOM = new PaddingProperty(PADDING_BOTTOM_PROPERTY);
  }

  private PaddingProperty(String cssName) {
    super(cssName);
  }

}
