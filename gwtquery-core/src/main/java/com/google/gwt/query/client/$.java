package com.google.gwt.query.client;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;

/**
 * A facade class of forwarding functions which allow end users to refer to
 * the GQuery class as '$' if they desire.
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
   * Wrap a JSON object
   */
  public static Properties $$(String properties) {
    return GQuery.$$(properties);
  }

  /**
   * Registers a GQuery plugin
   * @param plugin
   * @param pluginFactory
   */
  public static void registerPlugin(Class<? extends GQuery> plugin,
      Plugin<? extends GQuery> pluginFactory) {
    GQuery.registerPlugin(plugin, pluginFactory);
  }
}
