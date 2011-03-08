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
import com.google.gwt.query.client.plugins.widgets.SuggestBoxWidgetFactory;
import com.google.gwt.query.client.plugins.widgets.TabPanelWidgetFactory;
import com.google.gwt.query.client.plugins.widgets.TextBoxWidgetFactory;
import com.google.gwt.query.client.plugins.widgets.WidgetFactory;
import com.google.gwt.query.client.plugins.widgets.WidgetInitializer;
import com.google.gwt.query.client.plugins.widgets.WidgetsUtils;
import com.google.gwt.query.client.plugins.widgets.DisclosurePanelWidgetFactory.DisclosurePanelOptions;
import com.google.gwt.query.client.plugins.widgets.ListBoxWidgetFactory.ListBoxOptions;
import com.google.gwt.query.client.plugins.widgets.SuggestBoxWidgetFactory.SuggestBoxOptions;
import com.google.gwt.query.client.plugins.widgets.TabPanelWidgetFactory.TabPanelOptions;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.SuggestBox;
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
   * Create a {@link Button} widget for each selected element. The
   * <code>initializers</code> will be called on each new {@link Button} created
   * by passing them in parameter.
   * 
   */
  public Widgets button(WidgetInitializer... initializers) {
    return widgets(new ButtonWidgetFactory(), initializers);
  }

  /**
   * Create a {@link DateBox} widget for each selected element. The
   * <code>initializers</code> will be called on each new {@link Button} created
   * by passing them in parameter.
   */
  public Widgets datebox(WidgetInitializer... initializers) {
    return widgets(new DateBoxWidgetFactory(), initializers);
  }

  /**
   * Create a {@link DisclosurePanel} widget for each selected elements.
   */
  public Widgets disclosurePanel(DisclosurePanelOptions o,
      WidgetInitializer... initializers) {
    return widgets(new DisclosurePanelWidgetFactory(o), initializers);
  }

  /**
   * Create {@link DisclosurePanel} widget for each selected elements.
   */
  public Widgets disclosurePanel(WidgetInitializer... initializers) {
    return disclosurePanel(new DisclosurePanelOptions(), initializers);
  }

  /**
   * Create a {@link ListBox} widget for each selected element. The
   * <code>initializers</code> will be called on each new {@link ListBox}
   * created by passing them in parameter.
   * 
   * A {@link ListBox} is created if the element is a <i>input</i> with type
   * <i>password</i>, a <i>div</i> or a<i>span</i> element.
   */
  public Widgets listBox(ListBoxOptions options,
      WidgetInitializer... initializers) {
    return widgets(new ListBoxWidgetFactory(options), initializers);
  }

  /**
   * Create a {@link ListBox} widget for each selected element. The
   * <code>initializers</code> will be called on each new {@link ListBox}
   * created by passing them in parameter.
   * 
   * A {@link ListBox} is created if the element is a <i>input</i> with type
   * <i>password</i>, a <i>div</i> or a<i>span</i> element.
   */
  public Widgets listBox(WidgetInitializer... initializers) {
    return listBox(new ListBoxOptions(), initializers);
  }

  /**
   * Create a {@link PasswordTextBox} widget for each selected element. The
   * <code>initializers</code> will be called on each new
   * {@link PasswordTextBox} created by passing them in parameter.
   * 
   * A {@link PasswordTextBox} is created if the element is a <i>input</i> with
   * type <i>password</i>, a <i>div</i> or a<i>span</i> element.
   */
  public Widgets passwordBox(WidgetInitializer... initializers) {
    return widgets(new PasswordTextBoxWidgetFactory(), initializers);
  }

  public Widgets richtext(WidgetInitializer... initializers) {
    return widgets(new RichTextWidgetFactory(), initializers);
  }

  /**
   * Create a {@link SuggestBox} widget for each selected element. The
   * <code>initializers</code> will be called on each new {@link SuggestBox}
   * created by passing them in parameter.
   * 
   */
  public Widgets suggestBox(SuggestBoxOptions options,
      WidgetInitializer... initializers) {
    return widgets(new SuggestBoxWidgetFactory(options), initializers);
  }

  /**
   * Create a {@link SuggestBox} widget for each selected element. The
   * <code>initializers</code> will be called on each new {@link SuggestBox}
   * created by passing them in parameter.
   * 
   */
  public Widgets suggestBox(WidgetInitializer... initializers) {
    return suggestBox(new SuggestBoxOptions(), initializers);
  }

  /**
   * Create a {@link TabPanel} widget for each selected elements. Each div
   * element inside a selected element will create a tab and the first h3
   * element inside the div will be used as title
   */
  public Widgets tabPanel(TabPanelOptions o, WidgetInitializer... initializers) {
    return widgets(new TabPanelWidgetFactory(o), initializers);
  }

  /**
   * Create {@link TabPanel} widget for each selected elements. Each div element
   * will create a tab and the first h3 element inside the div will be used as
   * title
   */
  public Widgets tabPanel(WidgetInitializer... initializers) {
    return tabPanel(new TabPanelOptions(), initializers);
  }

  /**
   * Create a {@link TextBox} widget for each selected element. The
   * <code>initializers</code> will be called on each new {@link TextBox}
   * created by passing them in parameter.
   * 
   * A {@link TextBox} is created if the element is a <i>input</i> with type
   * text, a <i>div</i> or a<i>span</i> element.
   */
  public Widgets textBox(WidgetInitializer... initializers) {
    return widgets(new TextBoxWidgetFactory(), initializers);
  }

  /**
   * Try to create a widget using the given factory and the given options for
   * each element of the query. Returns a new gquery set of elements with the
   * new widgets created.
   */
  public <W extends Widget> Widgets widgets(WidgetFactory<W> factory,
      WidgetInitializer... initializers) {
    List<Element> result = new ArrayList<Element>();
    for (Element e : elements()) {
      W w = widget(e, factory, initializers);
      if (w != null) {
        result.add(w.getElement());
      }
    }
    return $(result).as(Widgets);
  }

  /**
   * Create and return a widget using the given factory and the given options
   */
  protected <W extends Widget> W widget(Element e, WidgetFactory<W> factory,
      WidgetInitializer... initializers) {

    if (!isWidgetCreationAuthorizedFrom(e)) {
      return null;
    }

    W widget = factory.create(e);
    if (initializers != null) {
      for (WidgetInitializer initializer : initializers) {
        initializer.initialize(widget, e);
      }
    }
    return widget;
  }
  /**
   * Create and return a widget using the given factory and the given options
   */
  protected <W extends Widget> W widget(WidgetFactory<W> factory,
      WidgetInitializer... initializers) {

    return widget(get(0), factory, initializers);
  }

  protected boolean isWidgetCreationAuthorizedFrom(Element e) {
    return $(e).widget() == null && !WidgetsUtils.matchesTags(e, excludedTags);
  }
}
