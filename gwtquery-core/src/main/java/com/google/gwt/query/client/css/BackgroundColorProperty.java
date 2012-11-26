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

/**
 * This property sets the background color of an element, either a <i>color</i>
 * value or the keyword 'transparent', to make the underlying colors shine
 * through.
 */
public class BackgroundColorProperty extends CssProperty<RGBColor> {

  private static final String CSS_PROPERTY = "backgroundColor";

  public static void init() {
    CSS.BACKGROUND_COLOR = new BackgroundColorProperty();
  }

  private BackgroundColorProperty() {
    super(CSS_PROPERTY);
  }
}
