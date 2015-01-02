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
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.user.client.ui.TextBoxBase;

/**
 * TextBoxBaseWidgetFactory.
 *
 * @param <T>
 */
public abstract class TextBoxBaseWidgetFactory<T extends TextBoxBase>
    implements WidgetFactory<T> {

  public T create(Element e) {
    T textBox = createWidget();

    if (getEquivalentTagName().equalsIgnoreCase(e.getTagName())) {
      copyAttributes((InputElement) e.cast(),
          (InputElement) textBox.getElement().cast());
    } else {
      textBox.setValue(e.getInnerText());
    }
    WidgetsUtils.replaceOrAppend(e, textBox);

    return (T) textBox;
  }

  protected String getEquivalentTagName() {
    return "input";
  }

  protected void copyAttributes(Element src, Element dest) {
    InputElement source = src.cast();
    InputElement destination = dest.cast();

    destination.setAccessKey(source.getAccessKey());
    destination.setDefaultValue(source.getDefaultValue());
    destination.setDisabled(source.isDisabled());
    if (source.getMaxLength() > 0)
      destination.setMaxLength(source.getMaxLength());
    destination.setReadOnly(source.isReadOnly());
    destination.setSize(source.getSize());
    destination.setName(source.getName());
    destination.setValue(source.getValue());
  }

  protected abstract T createWidget();
}
