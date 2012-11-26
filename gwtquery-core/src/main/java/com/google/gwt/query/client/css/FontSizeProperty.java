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
 * This property specifies the size of a font.
 */
public class FontSizeProperty extends
    CssProperty<FontSizeProperty.FontSize> implements TakesLength {

  /**
   * Possible values for the <i>font-size</i> property.
   */
  public static enum FontSize implements HasCssName {

    /**
     * Sets the font-size to a large size
     */
    LARGE,

    /**
     * Sets the font-size to a larger size than the parent element
     */
    LARGER,

    /**
     * Sets the font-size to a medium size
     */
    MEDIUM,

    /**
     * Sets the font-size to a small size
     */
    SMALL,

    /**
     * Sets the font-size to a smaller size than the parent element
     */
    SMALLER,

    /**
     * Sets the font-size to an extra large size
     */
    X_LARGE,

    /**
     * Sets the font-size to an extra small size
     */
    X_SMALL,

    /**
     * Sets the font-size to an extra extra large size
     */
    XX_LARGE,

    /**
     * Sets the font-size to an extra extra small size
     */
    XX_SMALL;
    public String getCssName() {
      return name().toLowerCase().replace('_', '-');
    }
  }

  private static final String CSS_PROPERTY = "fontSize";

  public static void init() {
    CSS.FONT_SIZE = new FontSizeProperty();
  }

  private FontSizeProperty() {
    super(CSS_PROPERTY);
  }

  public CssSetter with(Length value) {
    return new SimpleCssSetter(this, value);
  }
}
