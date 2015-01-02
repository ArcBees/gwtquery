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
 * This property defines the text direction/writing direction.
 */
public class UnicodeBidiProperty extends
    CssProperty<UnicodeBidiProperty.UnicodeBidi> {

  /**
   * Define possible values for <i>unicode-bidi</i> property.
   *
   */
  public static enum UnicodeBidi implements HasCssName {

    /**
     * For inline-level elements this creates an override. For block container
     * elements this creates an override for inline-level descendants not within
     * another block-level, table-cell, table-caption, or inline-block element.
     * This means that inside the element, reordering is strictly in sequence
     * according to the <i>direction</i> property; the implicit part of the
     * bidirectional algorithm is ignored. This corresponds to adding a LRO
     * (U+202D; for <i>direction: ltr</i>) or RLO (U+202E; for <i>direction:
     * rtl</i>) at the start of the element or at the start of each anonymous
     * child block box, if any, and a PDF (U+202C) at the end of the element.
     */
    BIDI_OVERRIDE,

    /**
     * If the element is inline, this value opens an additional level of
     * embedding with respect to the bidirectional algorithm. The direction of
     * this embedding level is given by the <i>direction</i> property. Inside
     * the element, reordering is done implicitly. This corresponds to adding a
     * LRE (U+202A; for <i>direction: ltr</i>) or RLE (U+202B; for <i>direction:
     * rtl</i>) at the start of the element and a PDF (U+202C) at the end of the
     * element.
     */
    EMBED,

    /**
     * The element does not open an additional level of embedding with respect
     * to the bidirectional algorithm. For inline-level elements, implicit
     * reordering works across element boundaries.
     */
    NORMAL;

    public String getCssName() {
      return name().toLowerCase().replace('_', '-');
    }
  }

  private static final String CSS_PROPERTY = "unicodeBidi";

  public static void init() {
    CSS.UNICODE_BIDI = new UnicodeBidiProperty();
  }

  private UnicodeBidiProperty() {
    super(CSS_PROPERTY);
  }
}
