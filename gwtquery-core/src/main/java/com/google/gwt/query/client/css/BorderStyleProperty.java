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
 * This property describes the foreground color of an element's text content.
 */
public class BorderStyleProperty extends
    AbstractCssProperty<BorderStyleProperty.LineStyle> {

  public static enum LineStyle implements HasCssName {
    DASHED {
      @Override
      public String getCssName() {
        return "dashed";
      }
    },
    DOTTED {
      @Override
      public String getCssName() {
        return "dotted";
      }
    },
    DOUBLE {
      @Override
      public String getCssName() {
        return "double";
      }
    },
    GROOVE {
      @Override
      public String getCssName() {
        return "groove";
      }
    },
    HIDDEN {
      @Override
      public String getCssName() {
        return "hidden";
      }
    },
    INHERIT {
      @Override
      public String getCssName() {
        return CSS.INHERIT_VALUE;
      }
    },
    INSET {
      @Override
      public String getCssName() {
        return "inset";
      }
    },
    NONE {
      @Override
      public String getCssName() {
        return "none";
      }
    },
    OUTSET {
      @Override
      public String getCssName() {
        return "outset";
      }
    },
    RIDGE {
      @Override
      public String getCssName() {
        return "ridge";
      }
    },
    SOLID {
      @Override
      public String getCssName() {
        return "solid";
      }
    };

    public abstract String getCssName();
  }

  private static final String BORDER_BOTTOM_STYLE_PROPERTY = "borderBottomStyle";
  private static final String BORDER_LEFT_STYLE_PROPERTY = "borderLeftStyle";
  private static final String BORDER_RIGHT_STYLE_PROPERTY = "borderRightStyle";
  private static final String BORDER_STYLE_PROPERTY = "borderStyle";

  private static final String BORDER_TOP_STYLE_PROPERTY = "borderTopStyle";

  public static void init() {
    CSS.BORDER_STYLE = new BorderStyleProperty(BORDER_STYLE_PROPERTY);
    CSS.BORDER_BOTTOM_STYLE = new BorderStyleProperty(
        BORDER_BOTTOM_STYLE_PROPERTY);
    CSS.BORDER_LEFT_STYLE = new BorderStyleProperty(BORDER_LEFT_STYLE_PROPERTY);
    CSS.BORDER_RIGHT_STYLE = new BorderStyleProperty(
        BORDER_RIGHT_STYLE_PROPERTY);
    CSS.BORDER_TOP_STYLE = new BorderStyleProperty(BORDER_TOP_STYLE_PROPERTY);
  }

  private String cssName;

  private BorderStyleProperty(String value) {
    this.cssName = value;
  }

  public String getCssName() {
    return cssName;
  }
}
