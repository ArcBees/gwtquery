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
 * This property describes the foreground color of an element's text content.
 */
public class Color implements CssProperty<RGBColor> {

  public static void init() {
    CSS.COLOR = new Color();
  }

  public Color() {
  }

  public String get(Style s) {
    return s.getProperty("color");
  }

  public void set(Style s, RGBColor value) {
    s.setProperty("color", value.value());
  }
}
