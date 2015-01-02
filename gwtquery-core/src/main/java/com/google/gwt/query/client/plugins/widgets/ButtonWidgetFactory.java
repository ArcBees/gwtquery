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

import com.google.gwt.dom.client.ButtonElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Button;

/**
 * Factory used to create a {@link Button} widget. A {@link Button} is created
 * if the element is a <i>button</i>, <i>div></i>, <i>span</i> or <i>a</i>
 * element (should be extends to other element).
 */
public class ButtonWidgetFactory implements WidgetFactory<Button> {

  public Button create(Element e) {
    Button button = new Button();
    button.getElement().setInnerText(e.getInnerText());

    if ("button".equalsIgnoreCase(e.getTagName())) {
      copyAttributes((ButtonElement) e.cast(), (ButtonElement) button.getElement().cast());
    }

    WidgetsUtils.replaceOrAppend(e, button);
    return button;
  }

  protected void copyAttributes(ButtonElement source, ButtonElement destination) {
    destination.setAccessKey(source.getAccessKey());
    destination.setDisabled(source.isDisabled());
    destination.setName(source.getName());
    destination.setValue(source.getValue());
  }
}
