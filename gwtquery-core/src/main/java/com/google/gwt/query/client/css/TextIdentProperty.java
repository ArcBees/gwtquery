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
 * The <i>text-ident</i> property specifies the indentation of the first line of
 * text in a block container. More precisely, it specifies the indentation of
 * the first box that flows into the block's first line box. The box is indented
 * with respect to the left (or right, for right-to-left layout) edge of the
 * line box. User agents must render this indentation as blank space.
 */
public class TextIdentProperty extends CssProperty<Length> {

  private static final String CSS_PROPERTY = "textIdent";

  public static void init() {
    CSS.TEXT_IDENT = new TextIdentProperty();
  }

  private TextIdentProperty() {
    super(CSS_PROPERTY);
  }
}
