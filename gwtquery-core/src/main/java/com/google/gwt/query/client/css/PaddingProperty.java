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

/**
 * The padding properties define the space between the element border and the
 * element content.
 */
public class PaddingProperty extends CssProperty<Length> {

  /**
   * ShorthandPaddingProperty.
   */
  public static class ShorthandPaddingProperty implements HasCssValue {

    private ShorthandPaddingProperty() {
    }

    public String getCssName() {
      return PADDING_PROPERTY;
    }

    public String getCssValue(Style s) {
      return s.getPadding();
    }

    /**
     * Apply the same padding to all sides.
     */
    public CssSetter with(Length padding) {
      return new MultipleValueCssSetter(getCssName(), padding);
    }

    /**
     * The top and bottom paddings are set to the first value and the right and
     * left paddings are set to the second.
     */
    public CssSetter with(Length topAndBottom, Length leftAndRight) {
      return new MultipleValueCssSetter(getCssName(), topAndBottom,
          leftAndRight);
    }

    /**
     * The top padding is set to the first value, the left and right paddings
     * are set to the second, and the bottom padding is set to the third.
     */
    public CssSetter with(Length top, Length leftAndRight, Length bottom) {
      return new MultipleValueCssSetter(getCssName(), top, leftAndRight, bottom);
    }

    /**
     * Apply padding to all sides.
     */
    public CssSetter with(Length padding1, Length padding2, Length padding3,
        Length padding4) {
      return new MultipleValueCssSetter(getCssName(), padding1, padding2,
          padding3, padding4);
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
