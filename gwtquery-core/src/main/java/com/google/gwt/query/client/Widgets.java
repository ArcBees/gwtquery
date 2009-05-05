package com.google.gwt.query.client;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.user.client.ui.Button;

/**
 * Experimental Gwt Query plugin for integrating Gwt Widgets.
 */
public class Widgets extends GQuery  {
  /**
   * Used to register the plugin.
   */
  private static class WidgetsPlugin implements Plugin<Widgets> {

    public Widgets init(GQuery gq) {
      return new Widgets(gq.get());
    }
  }

  public static final Class<Widgets> Widgets = Widgets.class;

  static {
    GQuery.registerPlugin(Widgets.class, new WidgetsPlugin());
  }

  public Widgets(Element element) {
    super(element);
  }

  public Widgets(JSArray elements) {
    super(elements);
  }

  public Widgets(NodeList list) {
    super(list);
  }

  /**
   * Create a builder capable of instantiating a GWT Button object over
   * every matched element. Call end() to execute builder and return to the
   * current query object.
   * @return a ButtonBuilder
   */
  public ButtonBuilder button() {
    return new ButtonBuilder("*");
  }

  public class ButtonBuilder {

    private String selector;

    private String label;

    private String labelSelector;

    public ButtonBuilder(String selector) {
      this.selector=selector;
    }

    public ButtonBuilder label(String label) {
      this.labelSelector = label;
      return this;
    }

    public Widgets end() {
      for(Element e: elements()) {
        Button b = Button.wrap(e);
        b.setText($(labelSelector, e).text());

      }
      return Widgets.this;
    }
  }
}
