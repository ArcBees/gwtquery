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
 * The <i>text-align</i> property describes how inline-level content of a block
 * container is aligned.
 */
public class TextAlignProperty extends
    CssProperty<TextAlignProperty.TextAlign> {

  /**
   * Possible value for <i>text-align</i> property.
   */
  public static enum TextAlign implements HasCssName {

    /**
     * Align the line box with the left side of the containing box.
     */
    CENTER,

    /**
     * this property specifies that the inline boxes are to be made flush with
     * both sides of the block. If the computed value of text-align is 'justify'
     * while the computed value of white-space is 'pre' or 'pre-line', the
     * actual value of text-align is set to the initial value.
     */
    JUSTIFY,

    /**
     * Align the line box with the left side of the containing box.
     */
    LEFT,

    /**
     * Align the line box with the right side of the containing box.
     */
    RIGHT;

    public String getCssName() {
      return name().toLowerCase();
    }
  }

  private static final String CSS_PROPERTY = "textAlign";

  public static void init() {
    CSS.TEXT_ALIGN = new TextAlignProperty();
  }

  private TextAlignProperty() {
    super(CSS_PROPERTY);
  }
}
