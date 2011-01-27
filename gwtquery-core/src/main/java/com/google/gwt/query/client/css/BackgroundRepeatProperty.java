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
 * The background-repeat property sets if/how a background image will be
 * repeated. By default, a background-image is repeated both vertically and
 * horizontally.
 */
public class BackgroundRepeatProperty extends
    AbstractCssProperty<BackgroundRepeatProperty.BackgroundRepeat> {

  public static enum BackgroundRepeat implements HasCssName {
    /**
     * Specifies that the setting of the background-repeat property should be
     * inherited from the parent element
     */
    INHERIT {
      @Override
      public String getCssName() {
        return INHERIT_VALUE;
      }
    },
    /**
     * The background-image will not be repeated
     */
    NO_REPEAT {
      @Override
      public String getCssName() {
        return NO_REPEAT_VALUE;
      }
    },
    /**
     * The background image will be repeated both vertically and horizontally.
     */
    REPEAT {
      @Override
      public String getCssName() {
        return REPEAT_VALUE;
      }
    },
    /**
     * The background image will be repeated only horizontally
     */
    REPEAT_X {
      @Override
      public String getCssName() {
        return REPEAT_X_VALUE;
      }
    },
    /**
     * The background image will be repeated only vertically
     */
    REPEAT_Y {
      @Override
      public String getCssName() {
        return REPEAT_Y_VALUE;
      }
    };

    public abstract String getCssName();
  }

  private static final String CSS_PROPERTY = "backgroundRepeat";
  private static final String INHERIT_VALUE = "inherit";
  private static final String NO_REPEAT_VALUE = "no-repeat";
  private static final String REPEAT_VALUE = "repeat";
  private static final String REPEAT_X_VALUE = "repeat-x";

  private static final String REPEAT_Y_VALUE = "repeat-y";

  public static void init() {
    CSS.BACKGROUND_REPEAT = new BackgroundRepeatProperty();
  }

  private BackgroundRepeatProperty() {
  }

  public String getCssName() {
    return CSS_PROPERTY;
  }
}
