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
 * Specify position of element's edges.
 */
public class EdgePositionProperty extends CssProperty<Length> {

  public static void init() {
    CSS.BOTTOM = new EdgePositionProperty("bottom");
    CSS.LEFT = new EdgePositionProperty("left");
    CSS.RIGHT = new EdgePositionProperty("right");
    CSS.TOP = new EdgePositionProperty("top");
  }

  private EdgePositionProperty(String value) {
    super(value);
  }
}
