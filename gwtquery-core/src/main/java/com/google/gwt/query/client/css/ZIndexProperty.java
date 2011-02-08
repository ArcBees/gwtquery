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

/**
 * The z-index property specifies the stack order of an element.
 * 
 * An element with greater stack order is always in front of an element with a
 * lower stack order.
 * 
 * The z-index property only works on positioned elements (position:absolute,
 * position:relative, or position:fixed).
 */
public class ZIndexProperty extends AbstractCssProperty<CssNumber> {

  private static final String CSS_PROPERTY = "zIndex";

  public static void init() {
    CSS.ZINDEX = new ZIndexProperty();
  }

  private ZIndexProperty() {
    super(CSS_PROPERTY);
  }

  @Override
  public String get(Style s) {
    return getZIndex(s);
  }

  /**
   * See GWT issue 5548
   * http://code.google.com/p/google-web-toolkit/issues/detail?id=5548
   */
  private native String getZIndex(Style s) /*-{
    //force to return a string
    return ""+s["zIndex"];
  }-*/;

}
