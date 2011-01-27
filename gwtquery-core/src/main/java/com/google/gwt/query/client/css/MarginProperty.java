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
 * The margin property sets the margins of an element.
 */
public class MarginProperty extends AbstractCssProperty<Length> {

  private static String MARGIN_BOTTOM_PROPERTY = "marginBottom";
  private static String MARGIN_LEFT_PROPERTY = "marginLeft";
  private static String MARGIN_PROPERTY = "margin";
  private static String MARGIN_RIGHT_PROPERTY = "marginRight";
  private static String MARGIN_TOP_PROPERTY = "marginTop";

  public static void init() {
    CSS.MARGIN = new MarginProperty(MARGIN_PROPERTY);
    CSS.MARGIN_LEFT = new MarginProperty(MARGIN_LEFT_PROPERTY);
    CSS.MARGIN_RIGHT = new MarginProperty(MARGIN_RIGHT_PROPERTY);
    CSS.MARGIN_TOP = new MarginProperty(MARGIN_TOP_PROPERTY);
    CSS.MARGIN_BOTTOM = new MarginProperty(MARGIN_BOTTOM_PROPERTY);
  }

  private String cssName;

  private MarginProperty(String cssName) {
    this.cssName = cssName;
  }

  public String getCssName() {
    return cssName;
  }

}
