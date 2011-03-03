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
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.plugins.widgets.ButtonWidgetFactory;
import com.google.gwt.query.client.plugins.widgets.TabPanelWidgetFactory;
import com.google.gwt.query.client.plugins.widgets.TextBoxWidgetFactory;
import com.google.gwt.query.client.plugins.widgets.WidgetFactory;
import com.google.gwt.query.client.plugins.widgets.WidgetOptions;
import com.google.gwt.query.client.plugins.widgets.TabPanelWidgetFactory.TabPanelOptions;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import java.util.ArrayList;
import java.util.List;
import com.google.gwt.query.client.LazyBase;

public interface LazyWidgets<T> extends LazyBase<T>{

  /**
   * Create an return a {@link TabPanel} widget with the first selected
   * elements. Each div element will create a tab and the first h3 element
   * inside the div will be used as title. The <code>initFunctions</code> will
   * be called on the new {@link TabPanel} created by passing it in parameter.
   * 
   */
  TabPanel tabPanel(Function... initFunctions);

  /**
   * Create an return a {@link TabPanel} widget with the first selected elements
   * by using a {@link TabPanelOptions}. The <code>initFunctions</code> will be
   * called on each new {@link Button} created by passing them in parameter.
   */
  TabPanel tabPanel(TabPanelOptions o, Function... initFunctions);

  /**
   * Create {@link TabPanel} widget for each selected elements. Each div element
   * will create a tab and the first h3 element inside the div will be used as
   * title
   */
  LazyWidgets<T> tabPanels(Function... initFunctions);

  /**
   * Create a {@link TabPanel} widget for each selected elements. Each div
   * element inside a selected element will create a tab and the first h3
   * element inside the div will be used as title
   */
  LazyWidgets<T> tabPanels(TabPanelOptions o, Function... initFunctions);

  /**
   * Create an return a {@link Button} widget with the first element of the
   * query.The <code>initFunctions</code> will be called on the new
   * {@link Button} created by passing it in parameter.
   * 
   */
  Button button(Function... initFunctions);

  /**
   * Create a {@link Button} widget for each selected element. The
   * <code>initFunctions</code> will be called on each new {@link Button}
   * created by passing them in parameter.
   * 
   */
  LazyWidgets<T> buttons(Function... initFunctions);

  /**
   * Create an return a {@link TextBox} widget with the first element of the
   * query.The <code>initFunctions</code> will be called on the new
   * {@link TextBox} created by passing it in parameter.
   * 
   * A {@link TextBox} is created if the element is a <i>input</i> with type
   * text, a <i>div</i> or a<i>span</i> element.
   */
  TextBox textBox(Function... initFunctions);

  /**
   * Create a {@link TextBox} widget for each selected element. The
   * <code>initFunctions</code> will be called on each new {@link TextBox}
   * created by passing them in parameter.
   *
   * A {@link TextBox} is created if the element is a <i>input</i> with type
   * text, a <i>div</i> or a<i>span</i> element.
   */
  LazyWidgets<T> textBoxes(Function... initFunctions);

  /**
   * Create and return a widget using the given factory and the given options
   */
  <W extends Widget> W widget(WidgetFactory<W> factory, Function... initFunctions);

  /**
   * Try to create a widget using the given factory and the given options for
   * each element of the query. Returns a new gquery set of elements with the
   * new widgets created.
   */
  <W extends Widget> LazyWidgets<T> widgets(WidgetFactory<W> factory, Function... initFunctions);

}
