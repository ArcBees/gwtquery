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

import com.google.gwt.query.client.css.BorderWidthProperty.BorderWidth;

/**
 * An outline is a line that is drawn around elements (outside the borders) to
 * make the element "stand out". The outline-width specifies the width of an
 * outline
 */
public class OutlineWidthProperty extends CssProperty<BorderWidth>
    implements TakesLength {

  private static final String CSS_PROPERTY = "outlineWidth";

  public static void init() {
    CSS.OUTLINE_WIDTH = new OutlineWidthProperty();
  }

  private OutlineWidthProperty() {
    super(CSS_PROPERTY);
  }

  public CssSetter with(Length value) {
    return new SimpleCssSetter(this, value);
  }
}
