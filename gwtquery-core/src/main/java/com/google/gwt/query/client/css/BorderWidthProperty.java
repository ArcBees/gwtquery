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

import com.google.gwt.dom.client.Style.HasCssName;

/**
 * The <i>border-width</i> property specifies the width of the border of a box.
 */
public class BorderWidthProperty extends
    CssProperty<BorderWidthProperty.BorderWidth> implements TakesLength {

  /**
   * Object allowing to specify a width of a line
   */
  public static enum BorderWidth implements HasCssName {

    /**
     * Define a medium border.
     */
    MEDIUM,

    /**
     * Define a thick border.
     */
    THICK,

    /**
     * Define a thin border.
     */
    THIN;

    public String getCssName() {
      return name().toLowerCase();
    }
  }

  private static final String BORDER_BOTTOM_WIDTH_PROPERTY = "borderBottomWidth";
  private static final String BORDER_LEFT_WIDTH_PROPERTY = "borderLeftWidth";
  private static final String BORDER_RIGHT_WIDTH_PROPERTY = "borderRightWidth";
  private static final String BORDER_TOP_WIDTH_PROPERTY = "borderTopWidth";
  private static final String BORDER_WIDTH_PROPERTY = "borderWidth";

  public static void init() {
    CSS.BORDER_WIDTH = new BorderWidthProperty(BORDER_WIDTH_PROPERTY);
    CSS.BORDER_BOTTOM_WIDTH = new BorderWidthProperty(
        BORDER_BOTTOM_WIDTH_PROPERTY);
    CSS.BORDER_LEFT_WIDTH = new BorderWidthProperty(BORDER_LEFT_WIDTH_PROPERTY);
    CSS.BORDER_RIGHT_WIDTH = new BorderWidthProperty(
        BORDER_RIGHT_WIDTH_PROPERTY);
    CSS.BORDER_TOP_WIDTH = new BorderWidthProperty(BORDER_TOP_WIDTH_PROPERTY);
  }

  private BorderWidthProperty(String value) {
    super(value);
  }

  public CssSetter with(Length value) {
    return new SimpleCssSetter(this, value);
  }
}
