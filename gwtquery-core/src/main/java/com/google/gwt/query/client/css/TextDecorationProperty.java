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

import com.google.gwt.dom.client.Style.TextDecoration;

/**
 * This property describes how inline content of a block is aligned.
 */
public class TextDecorationProperty extends
    AbstractCssProperty<TextDecoration> {

  
  private static final String CSS_PROPERTY = "textDecoration";
 

  public static void init() {
    CSS.TEXT_DECORATION= new TextDecorationProperty();
  }

  private TextDecorationProperty() {
    super(CSS_PROPERTY);
  }

}
