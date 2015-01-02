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
package com.google.gwt.query.client.plugins.widgets;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.TextAreaElement;
import com.google.gwt.user.client.ui.TextArea;

/**
 * Factory used to create a {@link TextArea} widget.
 *
 */
public class TextAreaWidgetFactory extends TextBoxBaseWidgetFactory<TextArea> {

  @Override
  protected void copyAttributes(Element src, Element dest) {
    TextAreaElement source = src.cast();
    TextAreaElement destination = dest.cast();

    destination.setAccessKey(source.getAccessKey());
    destination.setCols(source.getCols());
    destination.setDefaultValue(source.getDefaultValue());
    destination.setDisabled(source.isDisabled());
    destination.setName(source.getName());
    destination.setReadOnly(source.isReadOnly());
    destination.setRows(source.getRows());
    destination.setValue(source.getValue());
  }

  @Override
  protected String getEquivalentTagName() {
    return "textarea";
  }

  protected TextArea createWidget() {
    return new TextArea();
  }
}
