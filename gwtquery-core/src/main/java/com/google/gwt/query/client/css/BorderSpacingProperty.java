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
 * The <i>border-spacing</i> property specifies the distance that separates
 * adjacent cell borders in a table context. If one length is specified, it
 * gives both the horizontal and vertical spacing. If two are specified, the
 * first gives the horizontal spacing and the second the vertical spacing.
 * Lengths may not be negative.
 */
public class BorderSpacingProperty extends
    CssProperty<BorderSpacingProperty.BorderSpacing> {

  /**
   * value for <i>border-spacing</i> property.
   *
   */
  public static class BorderSpacing implements HasCssName {

    private Length horizontalSpacing;
    private Length verticalSpacing;

    /**
     * Construct a {@link BorderSpacing} object with same horizontal and
     * vertical spacing.
     */
    public BorderSpacing(Length spacing) {
      this(spacing, spacing);
    }

    /**
     * Construct a {@link BorderSpacing} object by specifying a horizontal and
     * avertical spacings.
     */
    public BorderSpacing(Length horizontalSpacing, Length verticalSpacing) {
      assert horizontalSpacing != null : "horizontal spacing cannot be null";
      assert verticalSpacing != null : "vertical spacing cannot be null";

      this.verticalSpacing = verticalSpacing;
      this.horizontalSpacing = horizontalSpacing;
    }

    public String getCssName() {
      return horizontalSpacing.getCssName() + " "
          + verticalSpacing.getCssName();
    }
  }

  private static final String CSS_PROPERTY = "borderSpacing";

  public static void init() {
    CSS.BORDER_SPACING = new BorderSpacingProperty();
  }

  private BorderSpacingProperty() {
    super(CSS_PROPERTY);
  }
}
