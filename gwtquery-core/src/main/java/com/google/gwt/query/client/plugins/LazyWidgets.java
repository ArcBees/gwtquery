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
package com.google.gwt.query.client.plugins;

import com.google.gwt.query.client.LazyBase;
import com.google.gwt.query.client.plugins.widgets.WidgetFactory;
import com.google.gwt.query.client.plugins.widgets.WidgetInitializer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * LazyWidgets.
 * @param <T>
 */
public interface LazyWidgets<T> extends LazyBase<T> {

  /**
   * Try to create a widget using the given factory and the given options for
   * each element of the query. Returns a new gquery set of elements with the
   * new widgets created.
   */
  <W extends Widget> LazyWidgets<T> widgets(WidgetFactory<W> factory,
      WidgetInitializer<W> initializers);

  /**
   * Create a {@link Button} widget for each selected element.
   */
  LazyWidgets<T> button();

  /**
   * Create a {@link Button} widget for each selected element. The
   * <code>initializers</code> will be called on each new {@link Button} created
   * by passing them in parameter.
   *
   */
  LazyWidgets<T> button(WidgetInitializer<Button> initializers);

  /**
   * Create a {@link Panel} widget for each selected element.
   */
  LazyWidgets<T> panel();

  /**
   * Create a {@link Label} widget for each selected element.
   */
  LazyWidgets<T> label();

  /**
   * Create a {@link Label} widget for each selected element. The
   * <code>initializers</code> will be called on each new {@link Label} created
   * by passing them in parameter.
   */
  LazyWidgets<T> label(WidgetInitializer<Label> initializers);

  /**
   * Create a {@link PasswordTextBox} widget for each selected element.
   */
  LazyWidgets<T> passwordBox();

  /**
   * Create a {@link PasswordTextBox} widget for each selected element. The
   * <code>initializers</code> will be called on each new
   * {@link PasswordTextBox} created by passing them in parameter.
   *
   */
  LazyWidgets<T> passwordBox(WidgetInitializer<PasswordTextBox> initializers);

  /**
   * Create a {@link TextBox} widget for each selected element. The
   * <code>initializers</code> will be called on each new {@link TextBox}
   * created by passing them in parameter.
   *
   */
  LazyWidgets<T> textBox();

  /**
   * Create a {@link TextBox} widget for each selected element. The
   * <code>initializers</code> will be called on each new {@link TextBox}
   * created by passing them in parameter.
   *
   */
  LazyWidgets<T> textBox(WidgetInitializer<TextBox> initializers);

  /**
   * Create a {@link TextArea} widget for each selected element. The
   * <code>initializers</code> will be called on each new {@link TextBox}
   * created by passing them in parameter.
   *
   */
  LazyWidgets<T> textArea();

  /**
   * Create a {@link TextArea} widget for each selected element. The
   * <code>initializers</code> will be called on each new {@link TextBox}
   * created by passing them in parameter.
   *
   */
  LazyWidgets<T> textArea(WidgetInitializer<TextArea> initializers);

}
