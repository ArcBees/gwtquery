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
import com.google.gwt.query.client.css.BackgroundAttachmentProperty.BackgroundAttachment;
import com.google.gwt.query.client.css.BackgroundPositionProperty.BackgroundPosition;
import com.google.gwt.query.client.css.BackgroundRepeatProperty.BackgroundRepeat;
import com.google.gwt.query.client.css.TakesCssValue.CssSetter;

/**
 * The <i>'background'</i> property is a shorthand property for setting the
 * individual background properties (i.e., <i>'background-color'</i>,
 * <i>'background-image'</i>, <i>'background-repeat'</i>,
 * <i>'background-attachment'</i> and <i>'background-position'</i>) at the same
 * place in the style sheet.
 *
 */
public class BackgroundProperty implements HasCssValue {

  private static final String CSS_PROPERTY = "background";

  static void init() {
    CSS.BACKGROUND = new BackgroundProperty();
    BackgroundAttachmentProperty.init();
    BackgroundColorProperty.init();
    BackgroundImageProperty.init();
    BackgroundRepeatProperty.init();
    BackgroundPositionProperty.init();
  }

  private BackgroundProperty() {
  }

  public String getCssValue(Style s) {
    return s.getProperty(CSS_PROPERTY);
  }

  public String getCssName() {
    return CSS_PROPERTY;
  }

  public CssSetter with(final RGBColor backgroundColor,
      UriValue backgroundImage, BackgroundRepeat backgroundRepeat,
      BackgroundAttachment backgroundAttachment,
      BackgroundPosition backgroundPosition) {

    return new MultipleValueCssSetter(CSS_PROPERTY, backgroundColor,
        backgroundImage, backgroundRepeat, backgroundAttachment,
        backgroundPosition);
  }
}
