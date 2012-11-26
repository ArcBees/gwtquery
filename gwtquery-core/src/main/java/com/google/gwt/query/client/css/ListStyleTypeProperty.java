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

import com.google.gwt.dom.client.Style.ListStyleType;

/**
 * The <i>list-style-type</i> property specifies appearance of the list item
 * marker if <i>list-style-image</i> has the value 'none' or if the image
 * pointed to by the URI cannot be displayed. The value 'none' specifies no
 * marker, otherwise there are three types of marker: glyphs, numbering systems,
 * and alphabetic systems. Note. Numbered lists improve document accessibility
 * by making lists easier to navigate.
 */
public class ListStyleTypeProperty extends CssProperty<ListStyleType> {

  private static final String CSS_PROPERTY = "listStyleType";

  public static void init() {
    CSS.LIST_STYLE_TYPE = new ListStyleTypeProperty();
  }

  private ListStyleTypeProperty() {
    super(CSS_PROPERTY);
  }
}
