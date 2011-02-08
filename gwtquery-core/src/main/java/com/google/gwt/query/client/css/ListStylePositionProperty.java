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
 * This property specifies if the list-item markers should appear inside or
 * outside the content flow.
 */
public class ListStylePositionProperty extends
    AbstractCssProperty<ListStylePositionProperty.ListStylePosition> {

  public static enum ListStylePosition implements HasCssName {

    /**
     * Specifies that the value of the list-style-position property should be
     * inherited from the parent element
     */
    INHERIT {
      @Override
      public String getCssName() {
        return "inherit";
      }
    },
    /**
     * Indents the marker and the text. The bullets appear inside the content
     * flow
     */
    INSIDE {
      @Override
      public String getCssName() {
        return "inside";
      }
    },
    /**
     * Keeps the marker to the left of the text. The bullets appears outside the
     * content flow. This is default
     */
    OUTSIDE {
      @Override
      public String getCssName() {
        return "outside";
      }
    };

    public abstract String getCssName();

  }

  private static final String CSS_PROPERTY = "listStylePosition";

  public static void init() {
    CSS.LIST_STYLE_POSITION = new ListStylePositionProperty();
  }

  private ListStylePositionProperty() {
    super(CSS_PROPERTY);
  }
}
