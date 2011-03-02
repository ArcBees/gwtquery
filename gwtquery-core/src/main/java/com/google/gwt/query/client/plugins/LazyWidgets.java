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
import com.google.gwt.query.client.plugins.widgets.TabPanelWidgetFactory;
import com.google.gwt.query.client.plugins.widgets.WidgetFactory;
import com.google.gwt.query.client.plugins.widgets.WidgetOptions;
import com.google.gwt.query.client.plugins.widgets.ButtonWidgetFactory.ButtonOptions;
import com.google.gwt.query.client.plugins.widgets.TabPanelWidgetFactory.TabPanelOptions;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;
import java.util.ArrayList;
import java.util.List;
import com.google.gwt.query.client.LazyBase;

public interface LazyWidgets<T> extends LazyBase<T>{

  /**
   * Create an return a {@link TabPanel} widget with the first selected
   * elements. Each div element will create a tab and the first h3 element
   * inside the div will be used as title
   */
  TabPanel tabPanel();

  /**
   * Create an return a {@link TabPanel} widget with the first selected elements
   * by using a {@link TabPanelOptions}
   */
  TabPanel tabPanel(TabPanelOptions o);

  /**
   * Create {@link TabPanel} widget for each selected elements. Each div element
   * will create a tab and the first h3 element inside the div will be used as
   * title
   */
  LazyWidgets<T> tabPanels();

  /**
   * Create a {@link TabPanel} widget for each selected elements. Each div
   * element inside a selected element will create a tab and the first h3
   * element inside the div will be used as title
   */
  LazyWidgets<T> tabPanels(TabPanelOptions o);

  /**
   * Create an return a {@link Button} widget with the first element of the
   * query
   */
  Button button();

  /**
   * Create and return a {@link Button} widget with the first element of the
   * query by using a {@link ButtonOptions}
   */
  Button button(ButtonOptions o);

  /**
   * Create a {@link Button} widget for each selected element.
   * 
   */
  LazyWidgets<T> buttons();

  /**
   * Create a {@link Button} widget for each selected element by using a
   * {@link ButtonOptions}
   * 
   */
  LazyWidgets<T> buttons(ButtonOptions o);

  /**
   * Create and return a widget using the given factory and the given options
   */
  <W extends Widget, O extends WidgetOptions> W widget( WidgetFactory<W, O> factory, O options);

  /**
   * Try to create a widget using the given factory and the given options for
   * each element of the query. Returns a new gquery set of elements with the
   * new widgets created.
   */
  <W extends Widget, O extends WidgetOptions> LazyWidgets<T> widgets( WidgetFactory<W, O> factory, O options);

}
