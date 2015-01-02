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
 * All height css properties : <i>max-height</i>, <i>min-height</i>,
 * <i>height</i>.
 */
public class HeightProperty extends CssProperty<Length> {

  private static final String HEIGHT_PROPERTY = "height";
  private static final String MAX_HEIGHT_PROPERTY = "maxHeight";
  private static final String MIN_HEIGHT_PROPERTY = "minHeight";

  public static void init() {
    CSS.HEIGHT = new HeightProperty(HEIGHT_PROPERTY);
    CSS.MAX_HEIGHT = new HeightProperty(MAX_HEIGHT_PROPERTY);
    CSS.MIN_HEIGHT = new HeightProperty(MIN_HEIGHT_PROPERTY);
  }

  private HeightProperty(String cssName) {
    super(cssName);
  }
}
