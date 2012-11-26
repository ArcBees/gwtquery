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
 * In a small-caps font, the glyphs for lowercase letters look similar to the
 * uppercase ones, but in a smaller size and with slightly different
 * proportions. The <i>font-variant</i> property requests such a font for
 * bicameral (having two cases, as with Latin script). This property has no
 * visible effect for scripts that are unicameral (having only one case, as with
 * most of the world's writing systems).
 */
public class FontVariantProperty extends
    CssProperty<FontVariantProperty.FontVariant> {

  /**
   * Possible values for the <i>font-variant</i> property.
   */
  public static enum FontVariant implements HasCssName {

    /**
     * The browser displays a normal font
     */
    NORMAL,

    /**
     * The browser displays a small-caps font
     */
    SMALL_CAPS;

    public String getCssName() {
      return name().toLowerCase().replace('_', '-');
    }
  }

  private static final String CSS_PROPERTY = "fontVariant";

  public static void init() {
    CSS.FONT_VARIANT = new FontVariantProperty();
  }

  private FontVariantProperty() {
    super(CSS_PROPERTY);
  }
}
