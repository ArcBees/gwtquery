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

import com.google.gwt.user.client.ui.PasswordTextBox;

/**
 * Factory used to create a {@link PasswordTextBox} widget. A
 * {@link PasswordTextBox} is created if the element is a <i>input</i> with type
 * <i>password</i>, a <i>div</i> or a<i>span</i> element.
 *
 */
public class PasswordTextBoxWidgetFactory extends
    TextBoxBaseWidgetFactory<PasswordTextBox> {

  protected PasswordTextBox createWidget() {
    return new PasswordTextBox();
  }
}
