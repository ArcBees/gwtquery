/*
 * Copyright 2014, The gwtquery team.
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
package com.google.gwt.query.client.plugins.events;

import com.google.gwt.dom.client.Element;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.js.JsUtils;
import com.google.gwt.user.client.Event;

/**
 * This object allows you to have a full copy of the original Event and
 * implements some useful method of the jQuery event model.
 *
 * This is also useful in Internet Explorer because it use the same javascript
 * object to fire MouseDownEvent, MouseMoveEvent or MouseStopEvent on the same
 * element. So, we cannot keep a copy of the MouseDownEvent during a dragging
 * for example.
 *
 * Be Careful : the methods preventDefault() and stopPropagation must be called directly on the
 * original event.
 *
 */
public class GqEvent extends Event {

  // Gwt Events class has not this event defined,
  // so we have to select one power of 2 which is unused in Event class
  public static int ONSUBMIT = 0x10000000;
  public static int ONRESIZE = 0x8000000;

  /**
   * Create a new {@link GqEvent} by copying the <code>originalEvent</code>.
   */
  public static GqEvent create(Event originalEvent) {
    GqEvent gQueryEvent = createObject().cast();
    copy(originalEvent, gQueryEvent);
    return gQueryEvent;
  }

  private static native void copy(
      Event originalEvent, GqEvent gQueryEvent) /*-{
    for ( var field in originalEvent) {
      gQueryEvent[field] = originalEvent[field];
    }
    gQueryEvent.originalEvent = originalEvent;
  }-*/;

  protected GqEvent() {
  }

  /**
   * Return the original event (the one created by the browser).
   */
  public final native Event getOriginalEvent() /*-{
    return this.originalEvent;
  }-*/;

  public final native void setCurrentElementTarget(Element e) /*-{
    this.currentTarget = e;

    // ie don't have a currentEventTarget field on event
    try{
      @com.google.gwt.dom.client.DOMImplTrident::currentEventTarget = e;
    }catch(e){}
  }-*/;

  /**
   * Tell whether ctrl or cmd key is pressed.
   *
   */
  public final boolean isMetaKeyPressed() {
    return getMetaKey() || getCtrlKey();
  }

  /**
   * The mouse position relative to the left edge of the document.
   *
   */
  public final int pageX() {
    if (getTouches() != null && getTouches().length() > 0) {
      return getTouches().get(0).getPageX();
    } else {
      return getClientX() + GQuery.document.getScrollLeft();
    }
  }

  /**
   * The mouse position relative to the top edge of the document.
   *
   */
  public final int pageY() {
    if (getTouches() != null &&  getTouches().length() > 0) {
      return getTouches().get(0).getPageY();
    } else {
      return getClientY() + GQuery.document.getScrollTop();
    }
  }

  public static final GqEvent as(Event e) {
    return e.cast();
  }

  public static Event create(Event event, String eventName) {
    event = create(event);
    JsUtils.prop(event, "type", eventName);
    return event;
  }
}
