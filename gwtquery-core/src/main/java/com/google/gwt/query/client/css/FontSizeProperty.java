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

/**
 * This property specifies the size of a font.
 */
public class FontSizeProperty extends
    AbstractCssProperty<FontSizeProperty.FontSize> implements TakesLength {

  /**
   * Enum for the font-size property.
   */
  public static enum FontSize implements HasCssName {
    /**
     * Specifies that the font size should be inherited from the parent element
     */
    INHERIT {
      @Override
      public String getCssName() {
        return CSS.INHERIT_VALUE;
      }
    },
    /**
     * Sets the font-size to a large size
     */
    LARGE {
      @Override
      public String getCssName() {
        return "large";
      }
    },
    /**
     * Sets the font-size to a larger size than the parent element
     */
    LARGER {
      @Override
      public String getCssName() {
        return "larger";
      }
    },
    /**
     * Sets the font-size to a medium size
     */
    MEDIUM {
      @Override
      public String getCssName() {
        return "medium";
      }
    },
    /**
     * Sets the font-size to a small size
     */
    SMALL {
      @Override
      public String getCssName() {
        return "small";
      }
    },
    /**
     * Sets the font-size to a smaller size than the parent element
     */
    SMALLER {
      @Override
      public String getCssName() {
        return "smaller";
      }
    },
    /**
     * Sets the font-size to an extra large size
     */
    X_LARGE {
      @Override
      public String getCssName() {
        return "x-large";
      }
    },
    /**
     * Sets the font-size to an extra small size
     */
    X_SMALL {
      @Override
      public String getCssName() {
        return "x-small";
      }
    },
    /**
     * Sets the font-size to an extra extra large size
     */
    XX_LARGE {
      @Override
      public String getCssName() {
        return "xx-large";
      }
    },
    /**
     * Sets the font-size to an extra extra small size
     */
    XX_SMALL {
      @Override
      public String getCssName() {
        return "xx-small";
      }
    };
    public abstract String getCssName();
  }

  private static final String CSS_PROPERTY = "fontSize";

  public static void init() {
    CSS.FONT_SIZE = new FontSizeProperty();
  }

  private FontSizeProperty() {
  }

  public String getCssName() {
    return CSS_PROPERTY;
  }

  /**
   * Sets the font-size to a fixed size in px, cm,..
   */
  public void set(Style s, Length p) {
    s.setProperty(CSS_PROPERTY, p.getCssName());

  }
}
