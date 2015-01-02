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

import com.google.gwt.dom.client.Style;
import com.google.gwt.query.client.css.TakesCssValue.CssSetter;

/**
 * The z-index property specifies the stack order of an element.
 *
 * An element with greater stack order is always in front of an element with a
 * lower stack order.
 *
 * The z-index property only works on positioned elements (position:absolute,
 * position:relative, or position:fixed).
 */
public class ZIndexProperty implements TakesInteger {

  private static final String CSS_PROPERTY = "zIndex";

  public static void init() {
    CSS.ZINDEX = new ZIndexProperty();
  }

  private ZIndexProperty() {
  }

  public String getCssName() {
    return CSS_PROPERTY;
  }

  /**
   * return a {@link String} containing the value of the <i>z-index</i>
   * property.
   */
  public String getCssValue(Style s) {
    return getZIndex(s);
  }

  /**
   * returns a {@link CssSetter} object setting <i>z-index</i> property to the
   * specified <code>value</code>.
   */
  public CssSetter with(Integer value) {
    return new SimpleCssSetter(CSS_PROPERTY, value != null ? value.toString() : null);
  }

  /**
   * See GWT issue 5548.
   * http://code.google.com/p/google-web-toolkit/issues/detail?id=5548
   */
  private native String getZIndex(Style s) /*-{
    // force to return a string
    return "" + s["zIndex"];
  }-*/;
}
