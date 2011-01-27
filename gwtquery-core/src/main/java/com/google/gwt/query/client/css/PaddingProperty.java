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
 * The padding properties define the space between the element border and the
 * element content.
 */
public class PaddingProperty extends AbstractCssProperty<Length> {

  private static String PADDING_BOTTOM_PROPERTY = "paddingBottom";
  private static String PADDING_LEFT_PROPERTY = "paddingLeft";
  private static String PADDING_PROPERTY = "padding";
  private static String PADDING_RIGHT_PROPERTY = "paddingRight";
  private static String PADDING_TOP_PROPERTY = "paddingTop";

  public static void init() {
    CSS.PADDING = new PaddingProperty(PADDING_PROPERTY);
    CSS.PADDING_LEFT = new PaddingProperty(PADDING_LEFT_PROPERTY);
    CSS.PADDING_RIGHT = new PaddingProperty(PADDING_RIGHT_PROPERTY);
    CSS.PADDING_TOP = new PaddingProperty(PADDING_TOP_PROPERTY);
    CSS.PADDING_BOTTOM = new PaddingProperty(PADDING_BOTTOM_PROPERTY);
  }

  private String cssName;

  private PaddingProperty(String cssName) {
    this.cssName = cssName;
  }

  public String getCssName() {
    return cssName;
  }

}
