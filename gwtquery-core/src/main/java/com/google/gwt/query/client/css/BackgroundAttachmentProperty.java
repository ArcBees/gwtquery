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
 * If a background image is specified, this property specifies whether it is
 * fixed with regard to the viewport (<i>'fixed'</i>) or scrolls along with the
 * containing block (<i>'scroll'</i>).
 */
public class BackgroundAttachmentProperty extends
    CssProperty<BackgroundAttachmentProperty.BackgroundAttachment> {

  /**
   * BackgroundAttachment.
   */
  public static enum BackgroundAttachment implements HasCssName {
    /**
     * Background image is fixed.
     */
    FIXED {
      @Override
      public String getCssName() {
        return FIXED_VALUE;
      }
    },
    /**
     * Background image scrolls when viewport is scrolled.
     */
    SCROLL {
      @Override
      public String getCssName() {
        return SCROLL_VALUE;
      }
    };

    public abstract String getCssName();
  }

  private static final String CSS_PROPERTY = "backgroundAttachment";
  private static final String FIXED_VALUE = "fixed";
  private static final String SCROLL_VALUE = "scroll";

  public static void init() {
    CSS.BACKGROUND_ATTACHMENT = new BackgroundAttachmentProperty();
  }

  private BackgroundAttachmentProperty() {
    super(CSS_PROPERTY);
  }
}
