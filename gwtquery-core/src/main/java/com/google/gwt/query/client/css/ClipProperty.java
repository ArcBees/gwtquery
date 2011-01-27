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
 * The clip property lets you specify the dimensions of an absolutely positioned
 * element that should be visible, and the element is clipped into this shape,
 * and displayed.
 * 
 * The clip property does not work if the overflow property is set to visible.
 */
public class ClipProperty extends AbstractCssProperty<ClipProperty.Shape> {

  public static class Shape implements HasCssName {

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
  }

  public String getCssName() {
    return CSS_PROPERTY;
  }
}
