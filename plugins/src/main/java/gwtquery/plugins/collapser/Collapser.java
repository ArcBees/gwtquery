/*
 * Copyright 2009 Google Inc.
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
package gwtquery.plugins.collapser;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.query.client.Effects;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.Plugin;
import com.google.gwt.user.client.Event;

/**
 * Collapser sample plugin. For all matched elements, adds a clickable [X] next
 * to the element which toggles its visibility.
 */
public class Collapser extends GQuery {

  /**
   * Used to register the plugin.
   */
  private static class CollapserPlugin implements Plugin<Collapser> {

    public Collapser init(GQuery gq) {
      return new Collapser(gq.get());
    }
  }

  /**
   * Plugin key for Collapser.
   */
  public static final Class<Collapser> Collapser = Collapser.class;

  static {
    GQuery.registerPlugin(Collapser.class, new CollapserPlugin());
  }

  public Collapser(NodeList<Element> list) {
    super(list);
  }

  /**
   * Adds a [X] link button before each matched element with a bound click
   * handler that toggles visibility of the element.
   */
  public Collapser apply() {
    for (final Element e : elements()) {
      GQuery button = $("<a href='#'>[X]</a>");
      $(e).before(button);
      button.click(new Function() {
        public boolean f(Event evt) {
          $(e).as(Effects.Effects).toggle();
          return true;
        }
      });
    }

    return this;
  }
}