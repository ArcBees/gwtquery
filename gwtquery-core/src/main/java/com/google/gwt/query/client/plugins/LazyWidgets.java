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
import com.google.gwt.query.client.plugins.widgets.CheckBoxWidgetFactory;
import com.google.gwt.query.client.plugins.widgets.DateBoxWidgetFactory;
import com.google.gwt.query.client.plugins.widgets.DisclosurePanelWidgetFactory;
import com.google.gwt.query.client.plugins.widgets.ListBoxWidgetFactory;
import com.google.gwt.query.client.plugins.widgets.PasswordTextBoxWidgetFactory;
import com.google.gwt.query.client.plugins.widgets.RadioButtonWidgetFactory;
import com.google.gwt.query.client.plugins.widgets.StackPanelWidgetFactory;
import com.google.gwt.query.client.plugins.widgets.SuggestBoxWidgetFactory;
import com.google.gwt.query.client.plugins.widgets.TabPanelWidgetFactory;
import com.google.gwt.query.client.plugins.widgets.TextAreaWidgetFactory;
import com.google.gwt.query.client.plugins.widgets.TextBoxWidgetFactory;
import com.google.gwt.query.client.plugins.widgets.WidgetFactory;
import com.google.gwt.query.client.plugins.widgets.WidgetInitializer;
import com.google.gwt.query.client.plugins.widgets.WidgetsUtils;
import com.google.gwt.query.client.plugins.widgets.DisclosurePanelWidgetFactory.DisclosurePanelOptions;
import com.google.gwt.query.client.plugins.widgets.ListBoxWidgetFactory.ListBoxOptions;
import com.google.gwt.query.client.plugins.widgets.RadioButtonWidgetFactory.RadioButtonOption;
import com.google.gwt.query.client.plugins.widgets.StackPanelWidgetFactory.StackPanelOptions;
import com.google.gwt.query.client.plugins.widgets.SuggestBoxWidgetFactory.SuggestBoxOptions;
import com.google.gwt.query.client.plugins.widgets.TabPanelWidgetFactory.TabPanelOptions;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import java.util.ArrayList;
import java.util.List;
import com.google.gwt.query.client.LazyBase;

public interface LazyWidgets<T> extends LazyBase<T>{

  /**
   * Create a {@link Button} widget for each selected element. The
   * <code>initializers</code> will be called on each new {@link Button} created
   * by passing them in parameter.
   * 
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
   * Create a {@link DateBox} widget for each selected element. The
   * <code>initializers</code> will be called on each new {@link Button} created
   * by passing them in parameter.
   */
  LazyWidgets<T> datebox();

  /**
   * Create a {@link DateBox} widget for each selected element. The
   * <code>initializers</code> will be called on each new {@link Button} created
   * by passing them in parameter.
   */
  LazyWidgets<T> datebox(WidgetInitializer<DateBox> initializers);

  /**
   * Create a {@link DisclosurePanel} widget for each selected elements.
   */
  LazyWidgets<T> disclosurePanel();

  /**
   * Create a {@link DisclosurePanel} widget for each selected elements.
   */
  LazyWidgets<T> disclosurePanel(DisclosurePanelOptions o, WidgetInitializer<DisclosurePanel> initializers);

  /**
   * Create a {@link DisclosurePanel} widget for each selected elements.
   */
  LazyWidgets<T> disclosurePanel(DisclosurePanelOptions o);

  /**
   * Create {@link DisclosurePanel} widget for each selected elements.
   */
  LazyWidgets<T> disclosurePanel(WidgetInitializer<DisclosurePanel> initializers);

  /**
   * Create a {@link ListBox} widget for each selected element. The
   * <code>initializers</code> will be called on each new {@link ListBox}
   * created by passing them in parameter.
   * 
   */
  LazyWidgets<T> listBox(ListBoxOptions options, WidgetInitializer<ListBox> initializers);

  /**
   * Create a {@link ListBox} widget for each selected element. The
   * <code>initializers</code> will be called on each new {@link ListBox}
   * created by passing them in parameter.
   * 
   */
  LazyWidgets<T> listBox(ListBoxOptions options);

  /**
   * Create a {@link ListBox} widget for each selected element. The
   * <code>initializers</code> will be called on each new {@link ListBox}
   * created by passing them in parameter.
   */
  LazyWidgets<T> listBox(WidgetInitializer<ListBox> initializers);

  /**
   * Create a {@link ListBox} widget for each selected element.
   */
  LazyWidgets<T> listBox();

  /**
   * Create a {@link PasswordTextBox} widget for each selected element.
   */
  LazyWidgets<T> passwordBox();

  /**
   * Create a {@link CheckBox} widget for each selected element.
   */
  LazyWidgets<T> checkBox(WidgetInitializer<CheckBox> initializers);

  /**
   * Create a {@link CheckBox} widget for each selected element.
   */
  LazyWidgets<T> checkBox();

  /**
   * Create a {@link PasswordTextBox} widget for each selected element. The
   * <code>initializers</code> will be called on each new
   * {@link PasswordTextBox} created by passing them in parameter.
   * 
   */
  LazyWidgets<T> passwordBox(WidgetInitializer<PasswordTextBox> initializers);

  /**
   * Create a {@link StackPanel} widget for each selected elements. Each div
   * element inside a selected element will create a tab and the first h3
   * element inside the div will be used as title
   */
  LazyWidgets<T> stackPanel(StackPanelOptions o, WidgetInitializer<StackPanel> initializers);

  /**
   * Create a {@link StackPanel} widget for each selected elements. Each div
   * element inside a selected element will create a tab and the first h3
   * element inside the div will be used as title
   */
  LazyWidgets<T> stackPanel(StackPanelOptions o);

  /**
   * Create {@link TabPanel} widget for each selected elements. Each div element
   * will create a tab and the first h3 element inside the div will be used as
   * title
   */
  LazyWidgets<T> stackPanel(WidgetInitializer<StackPanel> initializers);

  /**
   * Create {@link TabPanel} widget for each selected elements. Each div element
   * will create a tab and the first h3 element inside the div will be used as
   * title
   */
  LazyWidgets<T> stackPanel();

  /**
   * Create {@link RadioButton} widget for each selected elements. All
   * {@link RadioButton} created will be group under the same name specified in
   * the {@link RadioButtonOption o}
   */
  LazyWidgets<T> radioButton(RadioButtonOption o, WidgetInitializer<RadioButton> initializers);

  /**
   * Create {@link RadioButton} widget for each selected elements. All
   * {@link RadioButton} created will be group under the same name specified in
   * the {@link RadioButtonOption o}
   */
  LazyWidgets<T> radioButton(RadioButtonOption o);

  /**
   * Create {@link RadioButton} widget for each selected elements. All
   * {@link RadioButton} created will be group under the same name
   */
  LazyWidgets<T> radioButton(WidgetInitializer<RadioButton> initializers);

  /**
   * Create {@link RadioButton} widget for each selected elements. All
   * {@link RadioButton} created will be group under the same name
   */
  LazyWidgets<T> radioButton();

  /**
   * Create a {@link SuggestBox} widget for each selected element. The
   * <code>initializers</code> will be called on each new {@link SuggestBox}
   * created by passing them in parameter.
   * 
   */
  LazyWidgets<T> suggestBox(SuggestBoxOptions options, WidgetInitializer<SuggestBox> initializers);

  /**
   * Create a {@link SuggestBox} widget for each selected element. The
   * <code>initializers</code> will be called on each new {@link SuggestBox}
   * created by passing them in parameter.
   * 
   */
  LazyWidgets<T> suggestBox(SuggestBoxOptions options);

  /**
   * Create a {@link SuggestBox} widget for each selected element. The
   * <code>initializers</code> will be called on each new {@link SuggestBox}
   * created by passing them in parameter.
   * 
   */
  LazyWidgets<T> suggestBox(WidgetInitializer<SuggestBox> initializers);

  /**
   * Create a {@link SuggestBox} widget for each selected element.
   */
  LazyWidgets<T> suggestBox();

  /**
   * Create a {@link TabPanel} widget for each selected elements. Each div
   * element inside a selected element will create a tab and the first h3
   * element inside the div will be used as title
   */
  LazyWidgets<T> tabPanel(TabPanelOptions o, WidgetInitializer<TabPanel> initializers);

  /**
   * Create a {@link TabPanel} widget for each selected elements. Each div
   * element inside a selected element will create a tab and the first h3
   * element inside the div will be used as title
   */
  LazyWidgets<T> tabPanel(TabPanelOptions o);

  /**
   * Create {@link TabPanel} widget for each selected elements. Each div element
   * will create a tab and the first h3 element inside the div will be used as
   * title
   */
  LazyWidgets<T> tabPanel(WidgetInitializer<TabPanel> initializers);

  /**
   * Create {@link TabPanel} widget for each selected elements. Each div element
   * will create a tab and the first h3 element inside the div will be used as
   * title
   */
  LazyWidgets<T> tabPanel();

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

  /**
   * Try to create a widget using the given factory and the given options for
   * each element of the query. Returns a new gquery set of elements with the
   * new widgets created.
   */
  <W extends Widget> LazyWidgets<T> widgets(WidgetFactory<W> factory, WidgetInitializer<W> initializers);

}
