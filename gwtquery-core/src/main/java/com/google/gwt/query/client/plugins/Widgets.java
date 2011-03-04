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
import com.google.gwt.query.client.plugins.widgets.DateBoxWidgetFactory;
import com.google.gwt.query.client.plugins.widgets.RichTextWidgetFactory;
import com.google.gwt.query.client.plugins.widgets.TabPanelWidgetFactory;
import com.google.gwt.query.client.plugins.widgets.TextBoxWidgetFactory;
import com.google.gwt.query.client.plugins.widgets.WidgetFactory;
import com.google.gwt.query.client.plugins.widgets.TabPanelWidgetFactory.TabPanelOptions;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Widgets plugin for Gwt Query. Be careful, this plugin is still experimental.
 * The api can change in next releases.
 */
public class Widgets extends QueuePlugin<Widgets> {

  public static final Class<Widgets> Widgets = Widgets.class;

  static {
    GQuery.registerPlugin(Widgets.class, new Plugin<Widgets>() {
      public Widgets init(GQuery gq) {
        return new Widgets(gq);
      }
    });
  }

  protected Widgets(GQuery gq) {
    super(gq);
  }


  /**
   * Create {@link TabPanel} widget for each selected elements. Each div element
   * will create a tab and the first h3 element inside the div will be used as
   * title
   */
  public Widgets tabPanel(Function... initFunctions) {
    return tabPanel(new TabPanelOptions(), initFunctions);
  }

  /**
   * Create a {@link TabPanel} widget for each selected elements. Each div
   * element inside a selected element will create a tab and the first h3
   * element inside the div will be used as title
   */
  public Widgets tabPanel(TabPanelOptions o, Function... initFunctions) {
    return widgets(new TabPanelWidgetFactory(o), initFunctions);
  }


  /**
   * Create a {@link Button} widget for each selected element. The
   * <code>initFunctions</code> will be called on each new {@link Button}
   * created by passing them in parameter.
   * 
   */
  public Widgets button(Function... initFunctions) {
    return widgets(new ButtonWidgetFactory(), initFunctions);
  }

  /**
   * Create a {@link DateBox} widget for each selected element. The
   * <code>initFunctions</code> will be called on each new {@link Button}
   * created by passing them in parameter.
   */
  public Widgets datebox(Function... initFunctions) {
    return widgets(new DateBoxWidgetFactory(), initFunctions);
  }
  
  public Widgets richtext(Function... initFunctions) {
    return widgets(new RichTextWidgetFactory(), initFunctions);
  }
  
  /**
   * Create a {@link TextBox} widget for each selected element. The
   * <code>initFunctions</code> will be called on each new {@link TextBox}
   * created by passing them in parameter.
   *
   * A {@link TextBox} is created if the element is a <i>input</i> with type
   * text, a <i>div</i> or a<i>span</i> element.
   */
  public Widgets textBox(Function... initFunctions) {
    return widgets(new TextBoxWidgetFactory(), initFunctions);
  }

  /**
   * Create and return a widget using the given factory and the given options
   */
  protected <W extends Widget> W widget(WidgetFactory<W> factory,
      Function... initFunctions) {

    return widget(get(0), factory, initFunctions);
  }

  /**
   * Try to create a widget using the given factory and the given options for
   * each element of the query. Returns a new gquery set of elements with the
   * new widgets created.
   */
  public <W extends Widget> Widgets widgets(WidgetFactory<W> factory,
      Function... initFunctions) {
    List<Element> result = new ArrayList<Element>();
    for (Element e : elements()) {
      W w = widget(e, factory, initFunctions);
      if (w != null) {
        result.add(w.getElement());
      }
    }
    return $(result).as(Widgets);
  }

  /**
   * Create and return a widget using the given factory and the given options
   */
  protected <W extends Widget> W widget(Element e,
      WidgetFactory<W> factory, Function... initFunctions) {

    W widget = factory.create(e);

    if (initFunctions != null) {
      for (Function initFunction : initFunctions) {
        initFunction.f(widget);
      }
    }

    return widget;

  }


}
