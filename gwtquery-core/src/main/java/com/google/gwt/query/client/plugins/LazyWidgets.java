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
import com.google.gwt.dom.client.Element;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.plugins.widgets.ButtonWidgetFactory;
import com.google.gwt.query.client.plugins.widgets.DateBoxWidgetFactory;
import com.google.gwt.query.client.plugins.widgets.DisclosurePanelWidgetFactory;
import com.google.gwt.query.client.plugins.widgets.ListBoxWidgetFactory;
import com.google.gwt.query.client.plugins.widgets.PasswordTextBoxWidgetFactory;
import com.google.gwt.query.client.plugins.widgets.RichTextWidgetFactory;
import com.google.gwt.query.client.plugins.widgets.TabPanelWidgetFactory;
import com.google.gwt.query.client.plugins.widgets.TextBoxWidgetFactory;
import com.google.gwt.query.client.plugins.widgets.WidgetFactory;
import com.google.gwt.query.client.plugins.widgets.WidgetInitializer;
import com.google.gwt.query.client.plugins.widgets.DisclosurePanelWidgetFactory.DisclosurePanelOptions;
import com.google.gwt.query.client.plugins.widgets.ListBoxWidgetFactory.ListBoxOptions;
import com.google.gwt.query.client.plugins.widgets.TabPanelWidgetFactory.TabPanelOptions;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import java.util.ArrayList;
import java.util.List;
import com.google.gwt.query.client.LazyBase;

public interface LazyWidgets<T> extends LazyBase<T>{

  /**
   * Create {@link TabPanel} widget for each selected elements. Each div element
   * will create a tab and the first h3 element inside the div will be used as
   * title
   */
  LazyWidgets<T> tabPanel(WidgetInitializer... initializers);

  /**
   * Create a {@link DisclosurePanel} widget for each selected elements.
   */
  LazyWidgets<T> disclosurePanel(DisclosurePanelOptions o, WidgetInitializer... initializers);

  /**
   * Create {@link DisclosurePanel} widget for each selected elements.
   */
  LazyWidgets<T> disclosurePanel(WidgetInitializer... initializers);

  /**
   * Create a {@link TabPanel} widget for each selected elements. Each div
   * element inside a selected element will create a tab and the first h3
   * element inside the div will be used as title
   */
  LazyWidgets<T> tabPanel(TabPanelOptions o, WidgetInitializer... initializers);

  /**
   * Create a {@link Button} widget for each selected element. The
   * <code>initializers</code> will be called on each new {@link Button}
   * created by passing them in parameter.
   * 
   */
  LazyWidgets<T> button(WidgetInitializer... initializers);

  /**
   * Create a {@link DateBox} widget for each selected element. The
   * <code>initializers</code> will be called on each new {@link Button}
   * created by passing them in parameter.
   */
  LazyWidgets<T> datebox(WidgetInitializer... initializers);

  LazyWidgets<T> richtext(WidgetInitializer... initializers);

  /**
   * Create a {@link TextBox} widget for each selected element. The
   * <code>initializers</code> will be called on each new {@link TextBox}
   * created by passing them in parameter.
   * 
   * A {@link TextBox} is created if the element is a <i>input</i> with type
   * text, a <i>div</i> or a<i>span</i> element.
   */
  LazyWidgets<T> textBox(WidgetInitializer... initializers);

  /**
   * Create a {@link PasswordTextBox} widget for each selected element. The
   * <code>initializers</code> will be called on each new
   * {@link PasswordTextBox} created by passing them in parameter.
   * 
   * A {@link PasswordTextBox} is created if the element is a <i>input</i> with
   * type <i>password</i>, a <i>div</i> or a<i>span</i> element.
   */
  LazyWidgets<T> passwordBox(WidgetInitializer... initializers);

  /**
   * Create a {@link ListBox} widget for each selected element. The
   * <code>initializers</code> will be called on each new {@link ListBox}
   * created by passing them in parameter.
   * 
   * A {@link ListBox} is created if the element is a <i>input</i> with type
   * <i>password</i>, a <i>div</i> or a<i>span</i> element.
   */
  LazyWidgets<T> listBox(WidgetInitializer... initializers);

  /**
   * Create a {@link ListBox} widget for each selected element. The
   * <code>initializers</code> will be called on each new {@link ListBox}
   * created by passing them in parameter.
   * 
   * A {@link ListBox} is created if the element is a <i>input</i> with type
   * <i>password</i>, a <i>div</i> or a<i>span</i> element.
   */
  LazyWidgets<T> listBox(ListBoxOptions options, WidgetInitializer... initializers);

  /**
   * Try to create a widget using the given factory and the given options for
   * each element of the query. Returns a new gquery set of elements with the
   * new widgets created.
   */
  <W extends Widget> LazyWidgets<T> widgets(WidgetFactory<W> factory, WidgetInitializer... initializers);

}
