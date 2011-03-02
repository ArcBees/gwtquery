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
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.JSArray;
import com.google.gwt.query.client.plugins.GQueryQueue;
import com.google.gwt.query.client.plugins.Plugin;
import com.google.gwt.query.client.plugins.widgets.widgetfactory.ButtonWidgetFactory;
import com.google.gwt.query.client.plugins.widgets.widgetfactory.TabPanelWidgetFactory;
import com.google.gwt.query.client.plugins.widgets.widgetfactory.WidgetFactory;
import com.google.gwt.query.client.plugins.widgets.widgetfactory.WidgetOptions;
import com.google.gwt.query.client.plugins.widgets.widgetfactory.ButtonWidgetFactory.ButtonOptions;
import com.google.gwt.query.client.plugins.widgets.widgetfactory.TabPanelWidgetFactory.TabPanelOptions;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Widgets plugin for Gwt Query.
 */
public class Widgets extends GQueryQueue<Widgets> {

  public static final Class<Widgets> Widgets = Widgets.class;

  static {
    GQuery.registerPlugin(Widgets.class, new Plugin<Widgets>() {
      public Widgets init(GQuery gq) {
        return new Widgets(gq);
      }
    });
  }

  public Widgets(final Element element) {
    super(element);
  }

  public Widgets(GQuery gq) {
    super(gq);
  }

  public Widgets(final JSArray elements) {
    super(elements);
  }

  public Widgets(final NodeList<Element> list) {
    super(list);
  }


  /**
   * Create an return a {@link TabPanel} widget with the first selected
   * elements. Each div element will create a tab and the first h3 element
   * inside the div will be used as title
   */
  public TabPanel tabPanel() {
    return tabPanel(new TabPanelOptions());
  }

  /**
   * Create an return a {@link TabPanel} widget with the first selected elements
   * by using a {@link TabPanelOptions}
   */
  public TabPanel tabPanel(TabPanelOptions o) {
    return widget(new TabPanelWidgetFactory(), o);
  }

  /**
   * Create {@link TabPanel} widget for each selected elements. Each div element
   * will create a tab and the first h3 element inside the div will be used as
   * title
   */
  public Widgets tabPanels() {
    return tabPanels(new TabPanelOptions());
  }

  /**
   * Create a {@link TabPanel} widget for each selected elements. Each div
   * element inside a selected element will create a tab and the first h3
   * element inside the div will be used as title
   */
  public Widgets tabPanels(TabPanelOptions o) {
    return widgets(new TabPanelWidgetFactory(), o);
  }

  /**
   * Create an return a {@link Button} widget with the first element of the
   * query
   */
  public Button button() {
    return button(new ButtonOptions());
  }

  /**
   * Create and return a {@link Button} widget with the first element of the
   * query by using a {@link ButtonOptions}
   */
  public Button button(ButtonOptions o) {
    return widget(new ButtonWidgetFactory(), o);
  }

  /**
   * Create a {@link Button} widget for each selected element.
   * 
   */
  public Widgets buttons() {
    return buttons(new ButtonOptions());
  }

  /**
   * Create a {@link Button} widget for each selected element by using a
   * {@link ButtonOptions}
   * 
   */
  public Widgets buttons(ButtonOptions o) {
    return widgets(new ButtonWidgetFactory(), o);
  }

  /**
   * Create and return a widget using the given factory and the given options
   */
  public <W extends Widget, O extends WidgetOptions> W widget(
      WidgetFactory<W, O> factory, O options) {
    return widget(get(0), factory, options);
  }

  /**
   * Try to create a widget using the given factory and the given options for
   * each element of the query.
   * Returns a new gquery set of elements with the new widgets created.
   */
  public <W extends Widget, O extends WidgetOptions> Widgets widgets(
      WidgetFactory<W, O> factory, O options) {
    JSArray result = JSArray.create();
    for (Element e : elements()) {
      result.addNode(widget(e, factory, options).getElement());
    }
    return new Widgets(result);
  }

  /**
   * Create and return a widget using the given factory and the given options
   */
  protected <W extends Widget, O extends WidgetOptions> W widget(Element e,
      WidgetFactory<W, O> factory, O options) {
    return factory.create(e, options);
  }

}
