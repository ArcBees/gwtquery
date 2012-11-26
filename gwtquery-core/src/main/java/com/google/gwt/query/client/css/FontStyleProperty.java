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

import com.google.gwt.dom.client.Style.FontStyle;

/**
 * The <i>font-style</i> property requests normal (sometimes referred to as
 * "roman" or "upright"), italic, and oblique faces within a font family.
 */
public class FontStyleProperty extends CssProperty<FontStyle> {

  private static final String CSS_PROPERTY = "fontStyle";

  public static void init() {
    CSS.FONT_STYLE = new FontStyleProperty();
  }

  private FontStyleProperty() {
    super(CSS_PROPERTY);
  }
}
