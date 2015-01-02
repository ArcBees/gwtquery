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
 * All width css properties : <i>max-width</i>, <i>min-width</i>, <i>width</i>.
 */
public class WidthProperty extends CssProperty<Length> {

  private static final String WIDTH_PROPERTY = "width";
  private static final String MAX_WIDTH_PROPERTY = "maxWidth";
  private static final String MIN_WIDTH_PROPERTY = "minWidth";

  public static void init() {
    CSS.WIDTH = new WidthProperty(WIDTH_PROPERTY);
    CSS.MAX_WIDTH = new WidthProperty(MAX_WIDTH_PROPERTY);
    CSS.MIN_WIDTH = new WidthProperty(MIN_WIDTH_PROPERTY);
  }

  private WidthProperty(String cssName) {
    super(cssName);
  }
}
