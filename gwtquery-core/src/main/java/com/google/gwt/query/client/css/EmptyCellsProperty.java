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
 * This property controls the rendering of borders around cells that have no
 * visible content. Empty cells and cells with the 'visibility' property set to
 * 'hidden' are considered to have no visible content. Visible content includes
 * "&nbsp;" and other whitespace except ASCII CR ("\0D"), LF ("\0A"), tab
 * ("\09"), and space ("\20").
 */
public class EmptyCellsProperty extends
    CssProperty<EmptyCellsProperty.EmptyCells> {

  /**
   * Possible values for <i>empty-cells</i> property.)
   *
   */
  public enum EmptyCells implements HasCssName {
    /**
     * No background or borders are shown on empty cells
     */
    HIDE,
    /**
     * Background and borders are shown on empty cells.
     */
    SHOW;

    public String getCssName() {
      return name().toLowerCase();
    };
  }

  private static final String CSS_PROPERTY = "emptyCells";

  public static void init() {
    CSS.EMPTY_CELLS = new EmptyCellsProperty();
  }

  private EmptyCellsProperty() {
    super(CSS_PROPERTY);
  }
}
