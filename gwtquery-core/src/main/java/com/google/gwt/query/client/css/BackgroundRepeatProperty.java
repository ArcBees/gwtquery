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
 * If a background image is specified, this property specifies whether the image
 * is repeated (tiled), and how. All tiling covers the content and padding areas
 * of a box
 */
public class BackgroundRepeatProperty extends
    CssProperty<BackgroundRepeatProperty.BackgroundRepeat> {

  /**
   * Define possible values for <i>backgroung-repeat</i> property.
   *
   */
  public static enum BackgroundRepeat implements HasCssName {
    /**
     * The image is not repeated: only one copy of the image is drawn.
     */
    NO_REPEAT,
    /**
     * The image is repeated both horizontally and vertically.
     */
    REPEAT,
    /**
     * The image is repeated horizontally only.
     */
    REPEAT_X,
    /**
     * The image is repeated vertically only.
     */
    REPEAT_Y;

    public String getCssName() {
      return name().toLowerCase().replace('_', '-');
    }
  }

  private static final String CSS_PROPERTY = "backgroundRepeat";

  public static void init() {
    CSS.BACKGROUND_REPEAT = new BackgroundRepeatProperty();
  }

  private BackgroundRepeatProperty() {
    super(CSS_PROPERTY);
  }
}
