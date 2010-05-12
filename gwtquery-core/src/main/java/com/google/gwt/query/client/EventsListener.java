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

import com.google.gwt.core.client.Duration;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;

/**
 * This class implements an event queue instance for one Element. The queue
 * instance is configured as the default event listener in GWT.
 * 
 * The reference to this queue is stored as a unique variable in the element's
 * DOM
 * 
 * The class takes care of calling the appropriate functions for each browser
 * event and it also calls sinkEvents method.
 */
class EventsListener implements EventListener {

  private static class BindFunction {

    Object data;

    Function function;

    int times = -1;

    int type;

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
    EventsListener ret = getGQueryEventLinstener(e);
    return ret != null ? ret : new EventsListener(e);
  }

  private static native EventsListener getGQueryEventLinstener(Element elem) /*-{
    return elem.__gqueryevent;
  }-*/;

  private static native EventListener getOriginalEventListener(Element elem) /*-{
    return elem.__listener;
  }-*/;

  private static native void setFocusable(Element elem) /*-{
    elem.tabIndex = 0;
  }-*/;

  private static native void setGQueryEventListener(Element elem,
      EventsListener gqevent) /*-{
    elem.__gqueryevent = gqevent;
  }-*/;

  private Element element;

  private JsObjectArray<EventsListener.BindFunction> elementEvents = JsObjectArray
      .createArray().cast();
  private EventListener originalEventListener;

  private EventsListener(Element element) {
    this.element = element;
    originalEventListener = getOriginalEventListener(element);
    setGQueryEventListener(element, this);
    DOM.setEventListener((com.google.gwt.user.client.Element) element, this);
  }

  public void bind(int eventbits, final Object data, Function...funcs) {
    for (Function function: funcs) {
      bind(eventbits, data, function, -1);
    }
  }

  public void bind(int eventbits, final Object data, final Function function,
      int times) {
    if (function == null) {
      unbind(eventbits);
    } else {
      DOM.sinkEvents((com.google.gwt.user.client.Element) element, eventbits
          | DOM.getEventsSunk((com.google.gwt.user.client.Element) element));

      if ((eventbits | Event.FOCUSEVENTS) == Event.FOCUSEVENTS) {
        setFocusable(element);
      }

      elementEvents.add(new EventsListener.BindFunction(eventbits, function,
          data, times));
    }
  }

  double lastEvnt=0;
  int lastType=0;
  
  public void onBrowserEvent(Event event) {
    // Workaround for Issue_20
    if (lastType == event.getTypeInt()
        && lastEvnt - Duration.currentTimeMillis() < 10
        && "body".equalsIgnoreCase(element.getTagName())) {
      return;
    }
    lastEvnt = Duration.currentTimeMillis();
    lastType = event.getTypeInt();

    if (originalEventListener != null) {
      originalEventListener.onBrowserEvent(event);
    }

    int etype = DOM.eventGetType(event);
    for (int i = 0; i < elementEvents.length(); i++) {
      EventsListener.BindFunction listener = elementEvents.get(i);
      if (listener.hasEventType(etype)) {
        if (!listener.fire(event)) {
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
