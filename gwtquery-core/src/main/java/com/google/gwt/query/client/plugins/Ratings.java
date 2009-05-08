package com.google.gwt.query.client.plugins;

import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.Plugin;
import com.google.gwt.query.client.JSArray;
import com.google.gwt.query.client.SelectorEngine;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.InputElement;

/**
 * Star Rating plugin.
 */
public class Ratings extends GQuery {

  /**
   * Used to register the plugin.
   */
  private static class RatingsPlugin implements Plugin<Ratings> {

    public Ratings init(GQuery gq) {
      return new Ratings(gq.get());
    }
  }

  public static final Class<Ratings> Ratings = Ratings.class;

  static {
    GQuery.registerPlugin(Ratings.class, new RatingsPlugin());
  }

  public Ratings(Element element) {
    super(element);
  }

  public Ratings(JSArray elements) {
    super(elements);
  }

  public Ratings(NodeList<Element> list) {
    super(list);
  }

  public Ratings rating() {
    if (size() == 0) {
      return this;
    }
    not(".star-rating-applied").addClass("star-rating-applied");
    Control control;
    for (Element e : elements()) {
      GQuery input = $(e);
      String eid = SelectorEngine
          .or(e.getPropertyString("name"), "unnamed-rating")
          .replaceAll("\\[|\\]", "_").replaceAll("^\\_+|\\_$", "");
      GQuery context = $(getContext(e));
      Raters raters = (Raters) context.data("rating");
    }

    return this;
  }

  public static class Control {

  }

  public static class Raters {

  }

  private static native Element getContext(Element e) /*-{
    return this.form || $doc.body;
  }-*/;
}
