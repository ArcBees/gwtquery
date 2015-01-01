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

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.HasCssName;
import com.google.gwt.query.client.css.TakesCssValue.CssSetter;

/**
 * Simple implementation of {@link CssSetter} interface. It does its job for the
 * most CSS property.
 *
 */
public class SimpleCssSetter implements CssSetter {

  private String property;
  private String value;

  public SimpleCssSetter(HasCssValue property, HasCssName value) {
    this(property.getCssName(), value != null ? value.getCssName() : null);
  }

  public SimpleCssSetter(String property, String value) {
    this.property = property;
    this.value = value;
  }

  public void applyCss(Element e) {
    assert e != null : "Impossible to apply css to a null object";
    e.getStyle().setProperty(property, value != null ? value : "");
  }
}
