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
 * A clipping region defines what portion of an element's rendered content is
 * visible. By default, the clipping region has the same size and shape as the
 * element's box(es). The <i>clip</i> property allows you to modify this
 * clipping region by defining a shape.
 */
public class ClipProperty extends CssProperty<ClipProperty.Shape> {

  /**
   * Object defining a clipping region used as value for <i>clip</i> property.
   *
   */
  public static class Shape implements HasCssName {

    /**
     * Define a rectangular shape.
     */
    public static Shape rect(int top, int right, int bottom, int left) {
      return new Shape("rect(" + top + "px," + right + "px," + bottom + "px,"
          + left + "px)");
    }

    private String value;

    private Shape(String value) {
      this.value = value;
    }

    public String getCssName() {
      return value;
    }
  }

  private static final String CSS_PROPERTY = "clip";

  public static void init() {
    CSS.CLIP = new ClipProperty();
  }

  private ClipProperty() {
    super(CSS_PROPERTY);
  }
}
