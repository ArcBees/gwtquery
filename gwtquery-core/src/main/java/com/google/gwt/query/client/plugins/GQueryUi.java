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
 * GWT clone of jQueryUi-core. This class define some function present in the
 * jQuery-ui core and not directly in jQuery
 * 
 */
public class GQueryUi extends GQuery {

  /**
   * A POJO used to store dimension of an element
   * 
   */
  public static class Dimension {
    private int height = 0;
    private int width = 0;

    public Dimension(Element e) {
      width = e.getOffsetWidth();
      height = e.getOffsetHeight();
    }

    public Dimension(int width, int height) {
      this.width = width;
      this.height = height;
    }

    /**
     * return the height value.
     */
    public int getHeight() {
      return height;
    }

    /**
     * return the width value
     */
    public int getWidth() {
      return width;
    }
  }

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
   */
  public static class Event extends com.google.gwt.user.client.Event {

    /**
     * Create a new {@link Event} by copying the <code>originalEvent</code>.
     */
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

    /**
     * Return the original event (the one created by the browser)
     */
    public final native com.google.gwt.user.client.Event getOriginalEvent()/*-{
      return this.originalEvent;
    }-*/;

    /**
     * Tell whether ctrl or cmd key is pressed
     * 
     */
    public final boolean isMetaKeyPressed() {
      return getMetaKey() || getCtrlKey();
    }

    /**
     * The mouse position relative to the left edge of the document
     * 
     */
    public final int pageX() {
      return getClientX() + document.getScrollLeft();
    }

    /**
     * The mouse position relative to the top edge of the document.
     * 
     */
    public final int pageY() {
      return getClientY() + document.getScrollTop();
    }

  }

  private static class GQueryUiImpl {

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

    protected boolean scrollParentPositionTest(GQueryUi gQueryUi) {
      return "absolute".equals(gQueryUi.css("position"));
    }

    private final Element getViewportElement() {
      return GQuery.document.isCSS1Compat() ? GQuery.document
          .getDocumentElement() : GQuery.document.getBody();
    }

    private boolean isOverflowEnabled(GQuery e) {
      String overflow = e.css("overflow", true) + e.css("overflow-x", true)
          + e.css("overflow-y", true);
      return overflow.contains("auto") || overflow.contains("scroll");
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

  /**
   * Return true if the <code>descendant</code> is a child of the parent. Return false elsewhere.
   */
  public static boolean contains(Element parent, Element descendant) {
    return parent != descendant && parent.isOrHasChild(descendant);
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

  protected HasHandlers eventBus;

  private GQueryUiImpl impl = GWT.create(GQueryUiImpl.class);

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
   * Return the immediate scrolling parent.
   */
  public GQuery scrollParent() {
    return impl.scrollParent(this);
  }

  /**
   * fire event and call callback function.
   * 
   */
  protected void trigger(GwtEvent<?> e, Function callback, Element element) {
    trigger(e, callback, element, eventBus);
  }

}
