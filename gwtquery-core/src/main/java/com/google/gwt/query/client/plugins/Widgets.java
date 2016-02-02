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
import com.google.gwt.query.client.plugins.widgets.HtmlPanelWidgetFactory;
import com.google.gwt.query.client.plugins.widgets.LabelWidgetFactory;
import com.google.gwt.query.client.plugins.widgets.PasswordTextBoxWidgetFactory;
import com.google.gwt.query.client.plugins.widgets.TextAreaWidgetFactory;
import com.google.gwt.query.client.plugins.widgets.TextBoxWidgetFactory;
import com.google.gwt.query.client.plugins.widgets.WidgetFactory;
import com.google.gwt.query.client.plugins.widgets.WidgetInitializer;
import com.google.gwt.query.client.plugins.widgets.WidgetsUtils;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

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
   * Try to create a widget using the given factory and the given options for
   * each element of the query. Returns a new gquery set of elements with the
   * new widgets created.
   */
  public <W extends Widget> Widgets widgets(WidgetFactory<W> factory,
      WidgetInitializer<W> initializers) {
    List<Element> result = new ArrayList<>();
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
   * Create and return a widget using the given factory and the given options.
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
   * Create and return a widget using the given factory and the given options.
   */
  protected <W extends Widget> W widget(WidgetFactory<W> factory,
      WidgetInitializer<W> initializers) {
    return widget(get(0), factory, initializers);
  }

  /**
   * Create a {@link Button} widget for each selected element.
   */
  public Widgets button() {
    return widgets(new ButtonWidgetFactory(), null);
  }

  /**
   * Create a {@link Button} widget for each selected element. The
   * <code>initializers</code> will be called on each new {@link Button} created
   * by passing them in parameter.
   *
   */
  public Widgets button(WidgetInitializer<Button> initializers) {
    return widgets(new ButtonWidgetFactory(), initializers);
  }

  /**
   * Create a {@link Panel} widget for each selected element.
   */
  public Widgets panel() {
    return widgets(new HtmlPanelWidgetFactory(), null);
  }

  /**
   * Create a {@link Label} widget for each selected element.
   */
  public Widgets label() {
    return widgets(new LabelWidgetFactory(), null);
  }

  /**
   * Create a {@link Label} widget for each selected element. The
   * <code>initializers</code> will be called on each new {@link Label} created
   * by passing them in parameter.
   */
  public Widgets label(WidgetInitializer<Label> initializers) {
    return widgets(new LabelWidgetFactory(), initializers);
  }

  /**
   * Create a {@link PasswordTextBox} widget for each selected element.
   */
  public Widgets passwordBox() {
    return widgets(new PasswordTextBoxWidgetFactory(), null);
  }

  /**
   * Create a {@link PasswordTextBox} widget for each selected element. The
   * <code>initializers</code> will be called on each new
   * {@link PasswordTextBox} created by passing them in parameter.
   *
   */
  public Widgets passwordBox(WidgetInitializer<PasswordTextBox> initializers) {
    return widgets(new PasswordTextBoxWidgetFactory(), initializers);
  }

  /**
   * Create a {@link TextBox} widget for each selected element. The
   * <code>initializers</code> will be called on each new {@link TextBox}
   * created by passing them in parameter.
   *
   */
  public Widgets textBox() {
    return widgets(new TextBoxWidgetFactory(), null);
  }

  /**
   * Create a {@link TextBox} widget for each selected element. The
   * <code>initializers</code> will be called on each new {@link TextBox}
   * created by passing them in parameter.
   *
   */
  public Widgets textBox(WidgetInitializer<TextBox> initializers) {
    return widgets(new TextBoxWidgetFactory(), initializers);
  }

  /**
   * Create a {@link TextArea} widget for each selected element. The
   * <code>initializers</code> will be called on each new {@link TextBox}
   * created by passing them in parameter.
   *
   */
  public Widgets textArea() {
    return widgets(new TextAreaWidgetFactory(), null);
  }

  /**
   * Create a {@link TextArea} widget for each selected element. The
   * <code>initializers</code> will be called on each new {@link TextBox}
   * created by passing them in parameter.
   *
   */
  public Widgets textArea(WidgetInitializer<TextArea> initializers) {
    return widgets(new TextAreaWidgetFactory(), initializers);
  }
}
