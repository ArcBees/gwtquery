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
 * The <i>direction</i> specifies the base writing direction of blocks and the
 * direction of embeddings and overrides (see 'unicode-bidi') for the Unicode
 * bidirectional algorithm. In addition, it specifies the direction of table
 * column layout, the direction of horizontal overflow, and the position of an
 * incomplete last line in a block in case of 'text-align: justify'.
 */
public class DirectionProperty extends
    CssProperty<DirectionProperty.Direction> {

  /**
   * Possible values for <i>direction</i> property.
   *
   */
  public static enum Direction implements HasCssName {

    /**
     * Left-to-right direction.
     */
    LTR,

    /**
     * Right-to-left direction.
     */
    RTL;

    public String getCssName() {
      return name().toLowerCase();
    }
  }

  private static final String CSS_PROPERTY = "direction";

  public static void init() {
    CSS.DIRECTION = new DirectionProperty();
  }

  private DirectionProperty() {
    super(CSS_PROPERTY);
  }
}
