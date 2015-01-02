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
 *The <i>border-collapse</i> selects a table's border model.
 */
public class BorderCollapseProperty extends
    CssProperty<BorderCollapseProperty.BorderCollapse> {

  /**
   * BorderCollapse.
   */
  public static enum BorderCollapse implements HasCssName {
    /**
     * Borders are collapsed into a single border when possible (border-spacing
     * and empty-cells properties will be ignored)
     */
    COLLAPSE,

    /**
     * Borders are detached (border-spacing and empty-cells properties will not
     * be ignored).
     */
    SEPARATE;

    public String getCssName() {
      return name().toLowerCase();
    }
  }

  private static final String CSS_PROPERTY = "borderCollapse";

  public static void init() {
    CSS.BORDER_COLLAPSE = new BorderCollapseProperty();
  }

  private BorderCollapseProperty() {
    super(CSS_PROPERTY);
  }
}
