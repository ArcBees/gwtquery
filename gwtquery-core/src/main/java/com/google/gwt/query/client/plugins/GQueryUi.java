package com.google.gwt.query.client.plugins;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.JSArray;
import com.google.gwt.query.client.Plugin;
import com.google.gwt.query.client.Predicate;

/**
 * GWT clone of jQueryUi-core
 * 
 * @author Julien Dramaix (julien.dramaix@gmail.com, @jdramaix)
 * 
 */
public class GQueryUi extends GQuery {

  /**
   * This object allows you to have a full copy of the original Event and
   * implements some useful method of the jQuery event model. This is also
   * useful in Internet Explorer because it use the same javascript object to
   * fire MouseDownEvent, MouseMoveEvent or MouseStopEvent on the same element.
   * So, we cannot keep a copy of the MouseDownEvent during a dragginf for
   * example. Now we can !
   * 
   * TOBEFIXED : the method preventDefault() must be called directly on the
   * original event
   * 
   * 
   * @author jdramaix
   * 
   */
  public static class Event extends com.google.gwt.user.client.Event {

    public static Event create(com.google.gwt.user.client.Event originalEvent) {
      Event gQueryEvent = createObject().cast();
      copy(originalEvent, gQueryEvent);
      return gQueryEvent;
    }

    private static native void copy(
        com.google.gwt.user.client.Event originalEvent, Event gQueryEvent) /*-{
      for ( var field in originalEvent  ) {
        gQueryEvent[field] = originalEvent[field];
      }   
      gQueryEvent.originalEvent = originalEvent;
    }-*/;

    protected Event() {
    }

    public final native com.google.gwt.user.client.Event getOriginalEvent()/*-{
      return this.originalEvent;
    }-*/;

    /**
     * The mouse position relative to the left edge of the document
     * 
     * @param mouseEvent
     * @return the mouse x-position in the page
     */
    public final int pageX() {
      return getClientX() + document.getScrollLeft();
    }

    /**
     * The mouse position relative to the top edge of the document.
     * 
     * @param mouseEvent
     * @return the mouse y-position in the page
     */
    public final int pageY() {
      return getClientY() + document.getScrollTop();
    }

    /**
     * Tell if ctrl or cmd key is pressed
     * 
     * @return
     */
    public final boolean isMetaKeyPressed() {
      return getMetaKey() || getCtrlKey();
    }

  }

  public static class Dimension {
    private int width = 0;
    private int height = 0;

    public Dimension(Element e) {
      width = e.getOffsetWidth();
      height = e.getOffsetHeight();
    }

    public Dimension(int width, int height) {
      this.width = width;
      this.height = height;
    }

    public int getHeight() {
      return height;
    }

    public int getWidth() {
      return width;
    }
  }

  private static class GQueryUiImpl {

    private final Element getViewportElement() {
      return GQuery.document.isCSS1Compat() ? GQuery.document
          .getDocumentElement() : GQuery.document.getBody();
    }

    public GQuery scrollParent(final GQueryUi gQueryUi) {
      GQuery scrollParent;

      if ("fixed".equals(gQueryUi.css("position"))) {
        return GQuery.$(getViewportElement());
      }

      if (scrollParentPositionTest(gQueryUi)) {
        scrollParent = gQueryUi.parents().filter(new Predicate() {

          public boolean f(Element e, int index) {
            GQuery $e = GQuery.$(e);
            String position = $e.css("position", true);
            return ("relative".equals(position) || "absolute".equals(position) || "fixed"
                .equals(position))
                && isOverflowEnabled($e);
          }
        });

      } else {
        scrollParent = gQueryUi.parents().filter(new Predicate() {

          public boolean f(Element e, int index) {
            return isOverflowEnabled(GQuery.$(e));
          }
        });
      }
      return scrollParent.length() > 0 ? new GQuery(scrollParent.get(0))
          : GQuery.$(getViewportElement());

    }

    private boolean isOverflowEnabled(GQuery e) {
      String overflow = e.css("overflow", true) + e.css("overflow-x", true)
          + e.css("overflow-y", true);
      return overflow.contains("auto") || overflow.contains("scroll");
    }

    protected boolean scrollParentPositionTest(GQueryUi gQueryUi) {
      return "absolute".equals(gQueryUi.css("position"));
    }

  }

  @SuppressWarnings("unused")
  private static class GQueryUiImplTrident extends GQueryUiImpl {

    @Override
    protected boolean scrollParentPositionTest(GQueryUi gQueryUi) {
      String position = gQueryUi.css("position");
      return ("absolute".equals(position) || "relative".equals(position) || "static"
          .equals(position));
    }

  }

  public static Class<GQueryUi> GQueryUi = GQueryUi.class;

  // Register the plugin in GQuery
  static {
    GQuery.registerPlugin(GQueryUi.class, new Plugin<GQueryUi>() {
      public GQueryUi init(GQuery gq) {
        return new GQueryUi(gq);
      }
    });
  }

  protected HasHandlers eventBus;

  private GQueryUiImpl impl = GWT.create(GQueryUiImpl.class);

  public static boolean contains(Element parent, Element descendant) {
    return parent != descendant && parent.isOrHasChild(descendant);
  }

  public GQueryUi() {
    super();
  }

  public GQueryUi(Element element) {
    super(element);
  }

  public GQueryUi(GQuery gq) {
    super(gq);
  }

  public GQueryUi(JSArray elements) {
    super(elements);
  }

  public GQueryUi(NodeList<Element> list) {
    super(list);
  }

  /**
   * this function returns the immediate scrolling parent.
   * 
   * @return the immediate scrolling parent
   */
  public GQuery scrollParent() {
    return impl.scrollParent(this);
  }

  /**
   * fire event and call callback function.
   * 
   * @param e
   * @param callback
   * @param element
   */
  protected void trigger(GwtEvent<?> e, Function callback, Element element) {
    trigger(e, callback, element, eventBus);
  }

  protected static void trigger(GwtEvent<?> e, Function callback,
      Element element, HasHandlers handlerManager) {
    if (handlerManager != null && e != null) {
      handlerManager.fireEvent(e);
    }
    if (callback != null) {
      callback.f(element);
    }
  }

}
