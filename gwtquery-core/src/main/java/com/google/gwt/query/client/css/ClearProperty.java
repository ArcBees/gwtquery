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
 * The clear property specifies which sides of an element where other floating
 * elements are not allowed.
 */
public class ClearProperty extends AbstractCssProperty<ClearProperty.Clear> {

  public static enum Clear implements HasCssName {
    /**
     * No floating elements allowed on either the left or the right side
     */
    BOTH {
      @Override
      public String getCssName() {
        return "both";
      }

    },
    /**
     * Specifies that the value of the clear property should be inherited from
     * the parent element
     */
    INHERIT {
      @Override
      public String getCssName() {
        return CSS.INHERIT_VALUE;
      }

    },
    /**
     * No floating elements allowed on the left side
     */
    LEFT {
      @Override
      public String getCssName() {
        return "left";
      }

    },
    /**
     * Allows floating elements on both sides
     */
    NONE {
      @Override
      public String getCssName() {
        return "none";
      }

    },
    /**
     * No floating elements allowed on the right side
     */
    RIGHT {
      @Override
      public String getCssName() {
        return "right";
      }

    };

    public abstract String getCssName();

  }

  private static final String CSS_PROPERTY = "clear";

  public static void init() {
    CSS.CLEAR = new ClearProperty();
  }

  private ClearProperty() {
    super(CSS_PROPERTY);
  }
}
