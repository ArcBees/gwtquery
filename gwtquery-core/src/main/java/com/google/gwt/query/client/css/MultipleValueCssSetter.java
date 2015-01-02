/*
 * Copyright 2014, The gwtquery team.
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
 * MultipleValueCssSetter.
 */
public class MultipleValueCssSetter extends SimpleCssSetter {

  public MultipleValueCssSetter(String cssPropertyName, HasCssName... values) {
    super(cssPropertyName, computeValue(values));
  }

  protected static String computeValue(HasCssName... values) {
    StringBuilder valueBuilder = new StringBuilder();

    for (HasCssName cssValue : values) {
      valueBuilder.append(notNull(cssValue));
    }

    return valueBuilder.toString().trim();
  }

  private static String notNull(HasCssName value) {
    return value != null ? value.getCssName() + " " : "";
  }
}
