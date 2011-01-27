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
 * This property describes the foreground color of an element's text content.
 */
public class BorderColorProperty extends AbstractCssProperty<RGBColor> {

  private static final String BORDER_BOTTOM_COLOR_PROPERTY = "borderBottomColor";
  private static final String BORDER_COLOR_PROPERTY = "borderColor";
  private static final String BORDER_LEFT_COLOR_PROPERTY = "borderLeftColor";
  private static final String BORDER_RIGHT_COLOR_PROPERTY = "borderRightColor";
  private static final String BORDER_TOP_COLOR_PROPERTY = "borderTopColor";

  public static void init() {
    CSS.BORDER_COLOR = new BorderColorProperty(BORDER_COLOR_PROPERTY);
    CSS.BORDER_BOTTOM_COLOR = new BorderColorProperty(
        BORDER_BOTTOM_COLOR_PROPERTY);
    CSS.BORDER_LEFT_COLOR = new BorderColorProperty(BORDER_LEFT_COLOR_PROPERTY);
    CSS.BORDER_RIGHT_COLOR = new BorderColorProperty(
        BORDER_RIGHT_COLOR_PROPERTY);
    CSS.BORDER_TOP_COLOR = new BorderColorProperty(BORDER_TOP_COLOR_PROPERTY);
  }

  private String cssName;

  private BorderColorProperty(String value) {
    this.cssName = value;
  }

  public String getCssName() {
    return cssName;
  }
}
