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

import com.google.gwt.dom.client.Style.HasCssName;

/**
 * The <i>white-space</i> property declares how white space inside the element
 * is handled.
 */
public class WhiteSpaceProperty extends
    CssProperty<WhiteSpaceProperty.WhiteSpace> {

  /**
   * WhiteSpace.
   */
  public enum WhiteSpace implements HasCssName {

    /**
     * This value directs user agents to collapse sequences of white space, and
     * break lines as necessary to fill line boxes.
     */
    NORMAL,

    /**
     * This value collapses white space as for 'normal', but suppresses line
     * breaks within text.
     */
    NOWRAP,

    /**
     * This value prevents user agents from collapsing sequences of white space.
     * Lines are only broken at newlines in the source, or at occurrences of
     * "\A" in generated content.
     */
    PRE,

    /**
     * This value directs user agents to collapse sequences of white space.
     * Lines are broken at newlines in the source, at occurrences of "\A" in
     * generated content, and as necessary to fill line boxes.
     */
    PRE_LINE,

    /**
     * This value prevents user agents from collapsing sequences of white space.
     * Lines are broken at newlines in the source, at occurrences of "\A" in
     * generated content, and as necessary to fill line boxes.
     */
    PRE_WRAP;

    public String getCssName() {
      return name().toLowerCase().replace('_', '-');
    };
  }

  private static final String CSS_PROPERTY = "whiteSpace";

  public static void init() {
    CSS.WHITE_SPACE = new WhiteSpaceProperty();
  }

  private WhiteSpaceProperty() {
    super(CSS_PROPERTY);
  }
}
