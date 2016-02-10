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
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.Predicate;

/**
 * GWT clone of jQueryUi-core. This class define some function present in the
 * jQuery-ui core and not directly in jQuery.
 *
 */
public class UiPlugin extends GQuery {

  /**
   * A POJO used to store dimension of an element.
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
     * return the width value.
     */
    public int getWidth() {
      return width;
    }
  }

  private static class GQueryUiImpl {

    public GQuery scrollParent(final UiPlugin gQueryUi) {
      GQuery scrollParent;

      if ("fixed".equals(gQueryUi.css("position", false))) {
        return GQuery.$(getViewportElement());
      }

      if (scrollParentPositionTest(gQueryUi)) {
        scrollParent = gQueryUi.parents().filter(new Predicate() {

          public boolean f(Element e, int index) {
            GQuery g = GQuery.$(e);
            String position = g.css("position", true);
            return ("relative".equals(position) || "absolute".equals(position) || "fixed"
                .equals(position))
                && isOverflowEnabled(g);
          }
        });
      } else {
        scrollParent = gQueryUi.parents().filter(new Predicate() {

          public boolean f(Element e, int index) {
            return isOverflowEnabled(GQuery.$(e));
          }
        });
      }
      return scrollParent.length() > 0 ? $(scrollParent.get(0))
          : $(getViewportElement());
    }

    protected boolean scrollParentPositionTest(UiPlugin gQueryUi) {
      return "absolute".equals(gQueryUi.css("position"));
    }

    private Element getViewportElement() {
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
    protected boolean scrollParentPositionTest(UiPlugin gQueryUi) {
      String position = gQueryUi.css("position", false);
      return "absolute".equals(position) || "relative".equals(position) || "static"
          .equals(position);
    }
  }

  public static Class<UiPlugin> GQueryUi = UiPlugin.class;

  // Register the plugin in GQuery
  static {
    GQuery.registerPlugin(UiPlugin.class, new Plugin<UiPlugin>() {
      public UiPlugin init(GQuery gq) {
        return new UiPlugin(gq);
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
      callback.f(element.<com.google.gwt.dom.client.Element> cast());
    }
  }

  protected HasHandlers eventBus;

  private GQueryUiImpl impl = GWT.create(GQueryUiImpl.class);

  protected UiPlugin(GQuery gq) {
    super(gq);
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
