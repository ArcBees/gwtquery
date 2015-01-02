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
 * The clear property specifies which sides of an element where other floating
 * elements are not allowed.
 */
public class ClearProperty extends CssProperty<ClearProperty.Clear> {

  /**
   * Possible values for <i>clear</i> property.
   *
   */
  public static enum Clear implements HasCssName {

    /**
     * No floating elements allowed on either the left or the right side
     */
    BOTH,

    /**
     * No floating elements allowed on the left side
     */
    LEFT,

    /**
     * Allows floating elements on both sides
     */
    NONE,

    /**
     * No floating elements allowed on the right side
     */
    RIGHT;

    public String getCssName() {
      return name().toLowerCase();
    }
  }

  private static final String CSS_PROPERTY = "clear";

  public static void init() {
    CSS.CLEAR = new ClearProperty();
  }

  private ClearProperty() {
    super(CSS_PROPERTY);
  }
}
