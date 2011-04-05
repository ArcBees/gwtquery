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

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.plugins.widgets.WidgetFactory;
import com.google.gwt.query.client.plugins.widgets.WidgetInitializer;
import com.google.gwt.query.client.plugins.widgets.WidgetsUtils;
import com.google.gwt.user.client.ui.Widget;

/**
 * Widgets plugin for Gwt Query. Be careful, this plugin is still experimental.
 * The api can change in next releases.
 */
public class Widgets extends QueuePlugin<Widgets> {

  public static final Class<Widgets> Widgets = Widgets.class;

  // list of html tags that cannot be replaced by a widget, in order to avoid to
  // break the html structure
  private static final String[] excludedTags = {
      "html", "body", "head", "tr", "thead", "tfoot", "options", "script",
      "noscript", "style", "title"};

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
   * Try to create a widget using the given factory and the given options for
   * each element of the query. Returns a new gquery set of elements with the
   * new widgets created.
   */
  public <W extends Widget> Widgets widgets(WidgetFactory<W> factory,
      WidgetInitializer<W> initializers) {
    List<Element> result = new ArrayList<Element>();
    for (Element e : elements()) {
      W w = widget(e, factory, initializers);
      if (w != null) {
        result.add(w.getElement());
      }
    }
    return $(result).as(Widgets);
  }

  protected boolean isWidgetCreationAuthorizedFrom(Element e) {
    return !WidgetsUtils.matchesTags(e, excludedTags);
  }

  /**
   * Create and return a widget using the given factory and the given options
   */
  protected <W extends Widget> W widget(Element e, WidgetFactory<W> factory,
      WidgetInitializer<W> initializer) {

    if (!isWidgetCreationAuthorizedFrom(e)) {
      return null;
    }

    W widget = factory.create(e);
    if (initializer != null) {
      initializer.initialize(widget, e);
    }
    return widget;
  }

  /**
   * Create and return a widget using the given factory and the given options
   */
  protected <W extends Widget> W widget(WidgetFactory<W> factory,
      WidgetInitializer<W> initializers) {
    return widget(get(0), factory, initializers);
  }

}
