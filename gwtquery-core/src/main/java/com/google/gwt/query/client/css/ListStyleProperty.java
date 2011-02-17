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
import com.google.gwt.dom.client.Style.HasCssName;
import com.google.gwt.dom.client.Style.ListStyleType;
import com.google.gwt.query.client.css.ListStylePositionProperty.ListStylePosition;

/**
 * The <i>list-style</i> property is a shorthand notation for setting the three
 * properties <i>list-style-type</i>, <i>list-style-image</i>, and
 * <i>list-style-position</i> at the same place in the style sheet.
 */
public class ListStyleProperty implements
    CssShorthandProperty3<ListStyleType, ListStylePosition, ImageValue> {

  private static final String CSS_PROPERTY = "listStyle";

  static void init() {
    CSS.LIST_STYLE = new ListStyleProperty();
    ListStyleImageProperty.init();
    ListStylePositionProperty.init();
    ListStyleTypeProperty.init();
  }

  private ListStyleProperty() {
  }

  public String get(Style s) {
    return s.getProperty(CSS_PROPERTY);
  }

  public String getCssName() {
    return CSS_PROPERTY;
  }

  public void set(Style s, ListStyleType listStyleType,
      ListStylePosition listStylePosition, ImageValue listStyleImage) {
    String value = notNull(listStyleType) + notNull(listStylePosition)
        + notNull(listStyleImage).trim();
    s.setProperty(CSS_PROPERTY, value);
  }

  private String notNull(HasCssName value) {
    return value != null ? value.getCssName() + " " : "";
  }

}
