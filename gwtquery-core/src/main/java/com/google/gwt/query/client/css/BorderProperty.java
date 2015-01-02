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
import com.google.gwt.query.client.css.BorderStyleProperty.BorderStyle;
import com.google.gwt.query.client.css.BorderWidthProperty.BorderWidth;
import com.google.gwt.query.client.css.TakesCssValue.CssSetter;

/**
 * The <i>border</i> property is a shorthand property for setting the same
 * width, color, and style for all four borders of a box. Unlike the shorthand
 * <i>margin</i> and <i>padding</i> properties, the <i>border</i> property
 * cannot set different values on the four borders. To do so, one or more of the
 * other border properties must be used.
 *
 */
public class BorderProperty implements HasCssValue {

  private static final String BORDER_BOTTOM_PROPERTY = "borderBottom";
  private static final String BORDER_LEFT_PROPERTY = "borderLeft";
  private static final String BORDER_PROPERTY = "border";
  private static final String BORDER_RIGHT_PROPERTY = "borderRight";
  private static final String BORDER_TOP_PROPERTY = "borderTop";

  static void init() {
    CSS.BORDER = new BorderProperty(BORDER_PROPERTY);
    CSS.BORDER_BOTTOM = new BorderProperty(BORDER_BOTTOM_PROPERTY);
    CSS.BORDER_TOP = new BorderProperty(BORDER_TOP_PROPERTY);
    CSS.BORDER_RIGHT = new BorderProperty(BORDER_RIGHT_PROPERTY);
    CSS.BORDER_LEFT = new BorderProperty(BORDER_LEFT_PROPERTY);

    BorderColorProperty.init();
    BorderStyleProperty.init();
    BorderWidthProperty.init();
  }

  private String cssProperty;

  private BorderProperty(String property) {
    cssProperty = property;
  }

  public String getCssValue(Style s) {
    return s.getProperty(cssProperty);
  }

  public String getCssName() {
    return cssProperty;
  }

  public CssSetter with(BorderWidth borderWidth, BorderStyle borderStyle,
      RGBColor borderColor) {
    return new MultipleValueCssSetter(getCssName(), borderWidth, borderStyle, borderColor);
  }

  public CssSetter with(Length borderWidth, BorderStyle borderStyle,
      RGBColor borderColor) {
    return new MultipleValueCssSetter(getCssName(), borderWidth, borderStyle, borderColor);
  }
}
