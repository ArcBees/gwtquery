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
 * The <i>border-style</i> property specifies the line style of a box's border
 * (solid, double, dashed, etc.).
 */
public class BorderStyleProperty extends
    CssProperty<BorderStyleProperty.BorderStyle> {

  /**
   * BorderStyle.
   */
  public static enum BorderStyle implements HasCssName {
    /**
     * The border is a series of short line segments.
     */
    DASHED,

    /**
     * The border is a series of dots.
     */
    DOTTED,

    /**
     * The border is two solid lines. The sum of the two lines and the space
     * between them equals the value of 'border-width'.
     */
    DOUBLE,

    /**
     * The border looks as though it were carved into the canvas.
     */
    GROOVE,

    /**
     * Same as NONE, except in terms of border conflict resolution for table
     * elements.
     */
    HIDDEN,

    /**
     * The border makes the entire box look as though it were embedded in the
     * canvas.
     */
    INSET,

    /**
     * No border. This value forces the computed value of <i>border-width</i> to
     * be '0'.
     */
    NONE,

    /**
     * The opposite of INSET: the border makes the entire box look as though it
     * were coming out of the canvas.
     */
    OUTSET,

    /**
     * The opposite of GROOVE: the border looks as though it were coming out of
     * the canvas.
     */
    RIDGE,

    /**
     * The border is a single line segment.
     */
    SOLID;

    public String getCssName() {
      return name().toLowerCase();
    }
  }

  private static final String BORDER_BOTTOM_STYLE_PROPERTY = "borderBottomStyle";
  private static final String BORDER_LEFT_STYLE_PROPERTY = "borderLeftStyle";
  private static final String BORDER_RIGHT_STYLE_PROPERTY = "borderRightStyle";
  private static final String BORDER_STYLE_PROPERTY = "borderStyle";

  private static final String BORDER_TOP_STYLE_PROPERTY = "borderTopStyle";

  public static void init() {
    CSS.BORDER_STYLE = new BorderStyleProperty(BORDER_STYLE_PROPERTY);
    CSS.BORDER_BOTTOM_STYLE = new BorderStyleProperty(
        BORDER_BOTTOM_STYLE_PROPERTY);
    CSS.BORDER_LEFT_STYLE = new BorderStyleProperty(BORDER_LEFT_STYLE_PROPERTY);
    CSS.BORDER_RIGHT_STYLE = new BorderStyleProperty(
        BORDER_RIGHT_STYLE_PROPERTY);
    CSS.BORDER_TOP_STYLE = new BorderStyleProperty(BORDER_TOP_STYLE_PROPERTY);
  }

  private BorderStyleProperty(String value) {
    super(value);
  }
}
