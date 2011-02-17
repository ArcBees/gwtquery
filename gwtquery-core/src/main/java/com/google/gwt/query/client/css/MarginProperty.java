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
 * The margin property sets the margins of an element.
 */
public class MarginProperty extends AbstractCssProperty<Length> {

  public static class ShorthandMarginProperty implements CssShorthandProperty4<Length, Length, Length, Length>{

    private  ShorthandMarginProperty() {
    }
    
    public String get(Style s) {
      return s.getMargin();
    }

    public String getCssName() {
      return MARGIN_PROPERTY;
    }

    public void set(Style s, Length margin1, Length margin2, Length margin3,
        Length margin4) {
      String margin = notNull(margin1)+notNull(margin2)+notNull(margin3)+notNull(margin4);
      s.setProperty(MARGIN_PROPERTY, margin.trim());
      
    }
    
    private String notNull(HasCssName value) {
      return value != null ? value.getCssName() + " " : "";
    }
    
  }
  private static String MARGIN_BOTTOM_PROPERTY = "marginBottom";
  private static String MARGIN_LEFT_PROPERTY = "marginLeft";
  private static String MARGIN_PROPERTY = "margin";
  private static String MARGIN_RIGHT_PROPERTY = "marginRight";
  private static String MARGIN_TOP_PROPERTY = "marginTop";

  public static void init() {
    CSS.MARGIN = new ShorthandMarginProperty();
    CSS.MARGIN_LEFT = new MarginProperty(MARGIN_LEFT_PROPERTY);
    CSS.MARGIN_RIGHT = new MarginProperty(MARGIN_RIGHT_PROPERTY);
    CSS.MARGIN_TOP = new MarginProperty(MARGIN_TOP_PROPERTY);
    CSS.MARGIN_BOTTOM = new MarginProperty(MARGIN_BOTTOM_PROPERTY);
  }

  private MarginProperty(String cssName) {
    super(cssName);
  }

}
