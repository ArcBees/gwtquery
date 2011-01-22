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
 * This property sets the background color of an element, either a <color> value
 * or the keyword 'transparent', to make the underlying colors shine through.
 */
public class BackgroundColor implements CssProperty<RGBColor> {

  public static void init() {
    CSS.BACKGROUND_COLOR = new BackgroundColor();
  }

  public void set(Style s, RGBColor value) {
    s.setProperty("backgroundColor", value.value());
  }

  public String get(Style s) {
    return s.getProperty("backgroundColor");
  }
}
