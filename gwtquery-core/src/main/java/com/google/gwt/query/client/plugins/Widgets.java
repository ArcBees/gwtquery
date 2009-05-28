package com.google.gwt.query.client.plugins;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.JSArray;
import com.google.gwt.query.client.Plugin;
import com.google.gwt.user.client.ui.Button;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Experimental Gwt Query plugin for integrating Gwt Widgets.
 */
public class Widgets extends GQuery {

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
   * Create a builder capable of instantiating a GWT Button object over every
   * matched element. Call end() to execute builder and return to the current
   * query object.
   *
   * @return a ButtonBuilder
   */
  public ButtonBuilder button() {
    return new ButtonBuilder("*");
  }

  public class ButtonBuilder {

    private String selector;

    private String label = null;

    private String labelSelector = null;

    private Collection<ClickHandler> handlers = new ArrayList<ClickHandler>();

    public ButtonBuilder(String selector) {
      this.selector = selector;
    }

    public ButtonBuilder labelQuery(String label) {
      this.labelSelector = label;
      return this;
    }

    public ButtonBuilder label(String label) {
      this.label = label;
      return this;
    }

    public ButtonBuilder addClickHandler(ClickHandler handler) {
      handlers.add(handler);
      return this;
    }

    public Widgets end() {
      for (Element e : elements()) {
        Button b = null;
        if ("button".equalsIgnoreCase(e.getTagName())) {
          b = Button.wrap(e);
        } else {
          Element bElt = $("<button name='button' value='Click Me'>").get(0);
          $(e).hide().before(bElt);
          b = Button.wrap(bElt);
        }

        b.setText(label != null ? label
            : (labelSelector == null ? $(e) : $(labelSelector, e)).text());
        for (ClickHandler handler : handlers) {
          b.addClickHandler(handler);
        }
      }
      return Widgets.this;
    }
  }
}
