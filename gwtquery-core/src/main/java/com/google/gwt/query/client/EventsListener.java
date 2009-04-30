package com.google.gwt.query.client;

import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.DOM;
import com.google.gwt.dom.client.Element;

import java.util.List;
import java.util.ArrayList;


/**
 * This class implements an event queue instance for one element.
 * This queue instance is configured as the default event listener in GWT.
 * 
 * The reference to this queue is stored as a uniq variable in the element's DOM
 * 
 * The class takes care of calling the appropiate functions for each browser event
 * and also calls sinkEvents methods.
 * 
 */
class EventsListener implements EventListener {

  private class BindFunction {
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

    public boolean hasEventType(int etype) {
      return (type | etype) == type;
    }

    public boolean fire(Event event) {
      if (times != 0) {
        times--;
        return function.f(event, data);
      }
      return true;
    }
  }
  
  private native static EventsListener getWidgetElementImpl(
      Element elem) /*-{
    return elem.__gqueryevent;
  }-*/;

  private native static void setWidgetElementImpl(Element elem, EventsListener gqevent) /*-{
    elem.__gqueryevent = gqevent;
  }-*/;

  private native static void setFocusable(Element elem) /*-{
    elem.tabIndex = 0;
  }-*/;


  private List<EventsListener.BindFunction> elementEvents = new ArrayList<EventsListener.BindFunction>();
  private Element element;

  private EventsListener(Element e) {
    element = e;
    setWidgetElementImpl(element, this);
    DOM.setEventListener((com.google.gwt.user.client.Element) e, this);
  }

  public static EventsListener getInstance(Element e) {
    EventsListener ret = getWidgetElementImpl(e);
    return ret != null ? ret : new EventsListener(e);
  }

  public void bind(int eventbits, final Object data, final Function function, int times) {
    if (function == null) {
      unbind(eventbits);
    } else {
      DOM.sinkEvents((com.google.gwt.user.client.Element) element, eventbits
          | DOM.getEventsSunk((com.google.gwt.user.client.Element) element));

      if ((eventbits | Event.FOCUSEVENTS) == Event.FOCUSEVENTS)
        setFocusable(element);

      elementEvents.add(new EventsListener.BindFunction(eventbits, function, data, times));
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
    for (EventsListener.BindFunction listener : elementEvents) {
      if (listener.hasEventType(etype)) {
        if (!listener.fire(event)) {
          event.cancelBubble(true);
          event.preventDefault();
        }
      }
    }
  }

  public void unbind(int eventbits) {
    ArrayList<EventsListener.BindFunction> newList = new ArrayList<EventsListener.BindFunction>();
    for (EventsListener.BindFunction listener : elementEvents)
      if (!listener.hasEventType(eventbits))
        newList.add(listener);
    elementEvents = newList;
  }

}
