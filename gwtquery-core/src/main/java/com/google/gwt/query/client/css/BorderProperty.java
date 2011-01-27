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
import com.google.gwt.query.client.css.BorderStyleProperty.BorderStyle;
import com.google.gwt.query.client.css.BorderWidthProperty.BorderWidth;

/**
 * 
 *
 */
public class BorderProperty
    implements
    CssShorthandProperty3<BorderWidthProperty.BorderWidth, BorderStyleProperty.BorderStyle, RGBColor> {

  private static final String CSS_PROPERTY = "border";

  static void init() {
    CSS.BORDER = new BorderProperty();
    BorderColorProperty.init();
    BorderStyleProperty.init();
    BorderWidthProperty.init();
  }

  private BorderProperty() {
  }

  public String get(Style s) {
    return s.getProperty(CSS_PROPERTY);
  }

  public String getCssName() {
    return CSS_PROPERTY;
  }

  public void set(Style s, BorderWidth borderWidth, BorderStyle borderStyle,
      RGBColor borderColor) {
    String value = notNull(borderWidth) + notNull(borderStyle)
        + notNull(borderColor).trim();
    s.setProperty(CSS_PROPERTY, value);
  }

  private String notNull(HasCssName value) {
    return value != null ? value.getCssName() + " " : "";
  }

}
