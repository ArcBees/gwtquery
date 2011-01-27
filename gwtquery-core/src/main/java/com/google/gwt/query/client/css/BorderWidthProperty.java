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
import com.google.gwt.dom.client.Style.Unit;

/**
 * This property set the width of an element's border.
 */
public class BorderWidthProperty extends
    AbstractCssProperty<BorderWidthProperty.BorderWidth> implements TakesLength {

  public static class BorderWidth implements HasCssName {

    public static BorderWidth INHERIT;
    public static BorderWidth MEDIUM;
    public static BorderWidth THICK;
    public static BorderWidth THIN;

    static {
      MEDIUM = new BorderWidth("medium");
      THICK = new BorderWidth("thick");
      THIN = new BorderWidth("thin");
      INHERIT = new BorderWidth(CSS.INHERIT_VALUE);

    }

    public static BorderWidth length(int l, Unit unit) {
      return new BorderWidth("" + l + (unit != null ? unit.getType() : ""));
    }

    public static BorderWidth length(Length l) {
      return new BorderWidth(l.getCssName());
    }

    private String cssValue;

    private BorderWidth(String value) {
      cssValue = value;
    }

    public String getCssName() {
      return cssValue;
    }

  }

  private static final String BORDER_BOTTOM_WIDTH_PROPERTY = "borderBottomWidth";
  private static final String BORDER_LEFT_WIDTH_PROPERTY = "borderLeftWidth";
  private static final String BORDER_RIGHT_WIDTH_PROPERTY = "borderRightWidth";
  private static final String BORDER_TOP_WIDTH_PROPERTY = "borderTopWidth";
  private static final String BORDER_WIDTH_PROPERTY = "borderWidth";

  public static void init() {
    CSS.BORDER_WIDTH = new BorderWidthProperty(BORDER_WIDTH_PROPERTY);
    CSS.BORDER_BOTTOM_WIDTH = new BorderWidthProperty(
        BORDER_BOTTOM_WIDTH_PROPERTY);
    CSS.BORDER_LEFT_WIDTH = new BorderWidthProperty(BORDER_LEFT_WIDTH_PROPERTY);
    CSS.BORDER_RIGHT_WIDTH = new BorderWidthProperty(
        BORDER_RIGHT_WIDTH_PROPERTY);
    CSS.BORDER_TOP_WIDTH = new BorderWidthProperty(BORDER_TOP_WIDTH_PROPERTY);
  }

  private String cssName;

  private BorderWidthProperty(String value) {
    this.cssName = value;
  }

  public String getCssName() {
    return cssName;
  }

  public void set(Style s, Length p) {
    s.setProperty(cssName, p.getCssName());

  }
}
