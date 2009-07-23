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
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;

/**
 * This class implements an event queue instance for one element. This queue
 * instance is configured as the default event listener in GWT.
 *
 * The reference to this queue is stored as a uniq variable in the element's
 * DOM
 *
 * The class takes care of calling the appropiate functions for each browser
 * event and also calls sinkEvents methods.
 */
class EventsListener implements EventListener {

  private static class BindFunction {

    int type;

    Function function;

    Object data;

    int times = -1;

    BindFunction(int t, Function f, Object d) {
      type = t;
      function = f;
      data = d;
    }

    BindFunction(int t, Function f, Object d, int times) {
      this(t, f, d);
      this.times = times;
    }

    public boolean fire(Event event) {
      if (times != 0) {
        times--;
        return function.f(event, data);
      }
      return true;
    }

    public boolean hasEventType(int etype) {
      return (type | etype) == type;
    }
  }

  public static EventsListener getInstance(Element e) {
    EventsListener ret = getWidgetElementImpl(e);
    return ret != null ? ret : new EventsListener(e);
  }

  private static native EventsListener getWidgetElementImpl(Element elem) /*-{
    return elem.__gqueryevent;
  }-*/;

  private static native void setFocusable(Element elem) /*-{
    elem.tabIndex = 0;
  }-*/;

  private static native void setWidgetElementImpl(Element elem,
      EventsListener gqevent) /*-{
    elem.__gqueryevent = gqevent;
  }-*/;

  private JsObjectArray<EventsListener.BindFunction> elementEvents
      = JsObjectArray.createArray().cast();

  private Element element;

  private EventsListener(Element e) {
    element = e;
    setWidgetElementImpl(element, this);
    DOM.setEventListener((com.google.gwt.user.client.Element) e, this);
  }

  public void bind(int eventbits, final Object data, final Function function,
      int times) {
    if (function == null) {
      unbind(eventbits);
    } else {
      DOM.sinkEvents((com.google.gwt.user.client.Element) element,
          eventbits | DOM
              .getEventsSunk((com.google.gwt.user.client.Element) element));

      if ((eventbits | Event.FOCUSEVENTS) == Event.FOCUSEVENTS) {
        setFocusable(element);
      }

      elementEvents.add(
          new EventsListener.BindFunction(eventbits, function, data, times));
    }
  }

  public void bind(int eventbits, final Object data, final Function function) {
    bind(eventbits, data, function, -1);
  }

  public void fire(int eventbits, int... keys) {
    FireEvents.fire(element, eventbits, keys);
  }

  public void onBrowserEvent(Event event) {
    int etype = DOM.eventGetType(event);
    for (int i = 0; i < elementEvents.length(); i++) {
      EventsListener.BindFunction listener = elementEvents.get(i);
      if (listener.hasEventType(etype)) {
        if (!listener.fire(event)) {
          event.cancelBubble(true);
          event.stopPropagation();
          event.preventDefault();
        }
      }
    }
  }

  public void unbind(int eventbits) {
    JsObjectArray<EventsListener.BindFunction> newList = JsObjectArray
        .createArray().cast();
    for (int i = 0; i < elementEvents.length(); i++) {
      EventsListener.BindFunction listener = elementEvents.get(i);
      if (!listener.hasEventType(eventbits)) {
        newList.add(listener);
      }
    }
    elementEvents = newList;
  }
}
