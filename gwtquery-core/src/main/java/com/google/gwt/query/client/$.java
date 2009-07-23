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
package com.google.gwt.query.client;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;

/**
 * A facade class of forwarding functions which allow end users to refer to the
 * GQuery class as '$' if they desire.
 */
public class $ {

  public static GQuery $(String selectorOrHtml) {
    return GQuery.$(selectorOrHtml);
  }

  public static <T extends GQuery> T $(T gq) {
    return GQuery.$(gq);
  }

  /**
   * This function accepts a string containing a CSS selector which is then used
   * to match a set of elements, or it accepts raw HTML creating a GQuery
   * element containing those elements. The second parameter is is a class
   * reference to a plugin to be used.
   */
  public static <T extends GQuery> T $(String selector, Class<T> plugin) {
    return GQuery.$(selector, plugin);
  }

  /**
   * This function accepts a string containing a CSS selector which is then used
   * to match a set of elements, or it accepts raw HTML creating a GQuery
   * element containing those elements. The second parameter is the context to
   * use for the selector.
   */
  public static GQuery $(String selector, Node context) {
    return GQuery.$(selector, context);
  }

  /**
   * This function accepts a string containing a CSS selector which is then used
   * to match a set of elements, or it accepts raw HTML creating a GQuery
   * element containing those elements. The second parameter is the context to
   * use for the selector. The third parameter is the class plugin to use.
   */
  public static <T extends GQuery> GQuery $(String selector, Node context,
      Class<T> plugin) {
    return GQuery.$(selector, context, plugin);
  }

  /**
   * Wrap a GQuery around  existing Elements.
   */
  public static GQuery $(NodeList<Element> elements) {
    return GQuery.$(elements);
  }

  /**
   * Wrap a GQuery around an existing Element.
   */
  public static GQuery $(Element element) {
    return GQuery.$(element);
  }

  /**
   * Wrap a JSON object.
   */
  public static Properties $$(String properties) {
    return GQuery.$$(properties);
  }

  /**
   * Return a lazy version of the GQuery interface. Lazy function calls are
   * simply queued up and not executed immediately.
   */
  public static LazyGQuery lazy() {
    return GQuery.lazy();
  }

  /**
   * Registers a GQuery plugin.
   */
  public static void registerPlugin(Class<? extends GQuery> plugin,
      Plugin<? extends GQuery> pluginFactory) {
    GQuery.registerPlugin(plugin, pluginFactory);
  }
}
