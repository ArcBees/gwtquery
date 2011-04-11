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
package com.google.gwt.query.client.plugins.events;

import static com.google.gwt.query.client.GQuery.$;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.Duration;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.js.JsObjectArray;
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
 * 
 */
public class EventsListener implements EventListener {

  private static class BindFunction {

    Object data;
    Function function;
    String nameSpace = "";
    int times = -1;
    int type;

    BindFunction(int t, String n, Function f, Object d) {
      type = t;
      function = f;
      data = d;
      if (n != null) {
        nameSpace = n;
      }
    }

    BindFunction(int t, String n, Function f, Object d, int times) {
      this(t, n, f, d);
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
      return (type & etype) == type;
    }

    @Override
    public String toString() {
      return "bind function for event type " + type;
    }
  }

  /**
   * {@link BindFunction} used for live() method.
   * 
   */
  private static class LiveBindFunction extends BindFunction {

    Map<String, List<BindFunction>> bindFunctionBySelector;

    LiveBindFunction(int type, String namespace) {

      super(type, namespace, null, null, -1);
      bindFunctionBySelector = new HashMap<String, List<BindFunction>>();
    }

    /**
     * Add a {@link BindFunction} for a specific css selector
     */
    public void addBindFunctionForSelector(String cssSelector, BindFunction f) {
      List<BindFunction> bindFunctions = bindFunctionBySelector.get(cssSelector);
      if (bindFunctions == null) {
        bindFunctions = new ArrayList<BindFunction>();
        bindFunctionBySelector.put(cssSelector, bindFunctions);
      }
      
      bindFunctions.add(f);
    }
    
    public void clean(){
      bindFunctionBySelector = new HashMap<String, List<BindFunction>>();
    }

    @Override
    public boolean fire(Event event) {

      if (isEmpty()) {
        return true;
      }

      String[] selectors = bindFunctionBySelector.keySet().toArray(
          new String[0]);

      // first element where the event was fired
      Element eventTarget = getEventTarget(event);
      // last element where the event was dispatched on
      Element liveContextElement = getCurrentEventTarget(event);

      if (eventTarget == null || liveContextElement == null) {
        return true;
      }

      Map<String, List<Element>> realCurrentTargetBySelector = $(eventTarget).closest(
          selectors, liveContextElement);

      // nothing match the selectors
      if (realCurrentTargetBySelector.isEmpty()) {
        return true;
      }

      boolean result = true;

      com.google.gwt.query.client.plugins.events.Event gqEvent = com.google.gwt.query.client.plugins.events.Event.create(event);

      for (String cssSelector : realCurrentTargetBySelector.keySet()) {
        List<BindFunction> bindFunctions = bindFunctionBySelector.get(cssSelector);

        if (bindFunctions == null){
          continue;
        }
        
        for (BindFunction f : bindFunctions) {
          for (Element element : realCurrentTargetBySelector.get(cssSelector)) {
            gqEvent.setCurrentElementTarget(element);
            boolean subResult = f.fire(gqEvent);
            result &= subResult;
            if (!subResult) {
              // Event should not continue to be bubbled, break the second for
              break;
            }
          }
        }
      }

      // trick to reset the right currentTarget on the original event on ie
      gqEvent.setCurrentElementTarget(liveContextElement);

      return result;

    }

    /**
     * Remove the BindFunction associated to this cssSelector
     */
    public void removeBindFunctionForSelector(String cssSelector) {
      bindFunctionBySelector.remove(cssSelector);
    }

    /**
     * Tell if no {@link BindFunction} are linked to this object
     * 
     * @return
     */
    public boolean isEmpty() {
      return bindFunctionBySelector.isEmpty();
    }

    @Override
    public String toString() {
      return "live bind function for selector "
          + bindFunctionBySelector.keySet();
    }

    /**
     * Return the element whose the listener fired last. It represent the
     * context element where the {@link LiveBindFunction} was binded
     * 
     */
    private Element getCurrentEventTarget(Event e) {
      EventTarget currentEventTarget = e.getCurrentEventTarget();

      if (!Element.is(currentEventTarget)) {
        return null;
      }

      return Element.as(currentEventTarget);
    }

    /**
     * Return the element that was the actual target of the element
     */
    private Element getEventTarget(Event e) {
      EventTarget eventTarget = e.getEventTarget();

      if (!Element.is(eventTarget)) {
        return null;
      }

      return Element.as(eventTarget);
    }

  }

  // Gwt Events class has not this event defined, so we have to select ane available power of 2 
  public static int ONSUBMIT = 0x8000000;

  public static void clean(Element e) {
    EventsListener ret = getGQueryEventListener(e);
    if (ret != null) {
      ret.clean();
    }
  }

  public static EventsListener getInstance(Element e) {
    EventsListener ret = getGQueryEventListener(e);
    return ret != null ? ret : new EventsListener(e);
  }

  public static void rebind(Element e) {
    EventsListener ret = getGQueryEventListener(e);
    if (ret != null && ret.eventBits != 0) {
      ret.sink();
    }
  }

  private static native void cleanGQListeners(Element elem) /*-{
		if (elem.__gwtlistener) {
			elem.__listener = elem.__gwtlistener;
		}
		elem.__gquerysubmit = null;
		elem.__gqueryevent = null

  }-*/;

  private static native EventsListener getGQueryEventListener(Element elem) /*-{
		return elem.__gqueryevent;
  }-*/;

  private static native EventListener getGwtEventListener(Element elem) /*-{
		return elem.__gwtlistener;
  }-*/;

  private static native void setGQueryEventListener(Element elem,
      EventsListener gqevent) /*-{
		if (elem.__gqueryevent) {
			elem.__listener = elem.__gqueryevent;
		} else {
			elem.__gwtlistener = elem.__listener;
			elem.__gqueryevent = gqevent;
		}
  }-*/;

  // Gwt does't handle submit events in DOM.sinkEvents
  private static native void sinkSubmitEvent(Element elem) /*-{
		if (elem.__gquerysubmit)
			return;
		elem.__gquerysubmit = true;

		var handle = function(event) {
			elem.__gqueryevent.@com.google.gwt.query.client.plugins.events.EventsListener::dispatchEvent(Lcom/google/gwt/user/client/Event;)(event);
		};

		if (elem.addEventListener)
			elem.addEventListener("submit", handle, true);
		else
			elem.attachEvent("onsubmit", handle);
  }-*/;

  int eventBits = 0;
  double lastEvnt = 0;

  int lastType = 0;

  private Element element;

  private JsObjectArray<BindFunction> elementEvents = JsObjectArray.createArray().cast();
  private Map<Integer, LiveBindFunction> liveBindFunctionByEventType = new HashMap<Integer, LiveBindFunction>();

  private EventsListener(Element element) {
    this.element = element;
  }

  public void bind(int eventbits, final Object data, Function... funcs) {
    bind(eventbits, null, data, funcs);
  }

  public void bind(int eventbits, final Object data, final Function function,
      int times) {
    bind(eventbits, null, data, function, times);
  }

  public void bind(int eventbits, String name, final Object data,
      Function... funcs) {
    for (Function function : funcs) {
      bind(eventbits, name, data, function, -1);
    }
  }

  public void bind(int eventbits, String namespace, final Object data,
      final Function function, int times) {
    if (function == null) {
      unbind(eventbits, namespace);
      return;
    }
    eventBits |= eventbits;
    sink();
    elementEvents.add(new BindFunction(eventbits, namespace, function, data,
        times));
  }

  public void bind(String event, final Object data, Function... funcs) {
    // TODO: nameSpaces in event lists
    String nameSpace = event.replaceFirst("^[^\\.]+\\.*(.*)$", "$1");
    String eventName = event.replaceFirst("^([^\\.]+).*$", "$1");
    int b = getEventBits(eventName);
    for (Function function : funcs) {
      bind(b, nameSpace, data, function, -1);
    }
  }
  
  public void die(String eventNames, String cssSelector) {
    die(getEventBits(eventNames), cssSelector);
  }

  public void die(int eventbits, String cssSelector) {
    if (eventbits == 0) {
      for (LiveBindFunction liveBindFunction : liveBindFunctionByEventType.values()) {
        liveBindFunction.removeBindFunctionForSelector(cssSelector);
      }
    } else {
      LiveBindFunction liveBindFunction = liveBindFunctionByEventType.get(eventbits);
      if (liveBindFunction != null) {
        liveBindFunction.removeBindFunctionForSelector(cssSelector);
      }
    }
  }

  public void dispatchEvent(Event event) {
    int etype = getTypeInt(event.getType());
    for (int i = 0; i < elementEvents.length(); i++) {
      BindFunction listener = elementEvents.get(i);
      if (listener.hasEventType(etype)) {
        if (!listener.fire(event)) {
          event.stopPropagation();
          event.preventDefault();
        }
      }
    }
  }

  /**
   * Return the original gwt EventListener associated with this element, before
   * gquery replaced it to introduce its own event handler.
   */
  public EventListener getOriginalEventListener() {
    return getGwtEventListener(element);
  }

  public void live(String eventNames, String cssSelector, Object data, 
      Function... f) {
    live(getEventBits(eventNames), cssSelector, data, f);
  }
  
  public void live(int eventbits, String cssSelector, Object data, 
      Function... funcs) {
    for (int i = 0; i < 28; i++) {
      int event = (int)Math.pow(2,i);
      if ((eventbits & event) == event) {

        // is a LiveBindFunction already attached for this kind of event
        LiveBindFunction liveBindFunction = liveBindFunctionByEventType.get(event);
        if (liveBindFunction == null) {
          liveBindFunction = new LiveBindFunction(event, "live");
          eventBits |= event;
          sink();
          elementEvents.add(liveBindFunction);
          liveBindFunctionByEventType.put(event, liveBindFunction);
        }

        for (Function f: funcs) {
          liveBindFunction.addBindFunctionForSelector(cssSelector, 
              new BindFunction(event, "live", f, data));
        }
      }
    }
  }

  public void onBrowserEvent(Event event) {
    double now = Duration.currentTimeMillis();
    // Workaround for Issue_20
    if (lastType == event.getTypeInt() && now - lastEvnt < 10
        && "body".equalsIgnoreCase(element.getTagName())) {
      return;
    }
    lastEvnt = now;
    lastType = event.getTypeInt();

    // Execute the original Gwt listener
    if (getOriginalEventListener() != null) {
      getOriginalEventListener().onBrowserEvent(event);
    }

    dispatchEvent(event);
  }

  public void unbind(int eventbits) {
    unbind(eventbits, null);
  }

  public void unbind(int eventbits, String namespace) {
    JsObjectArray<BindFunction> newList = JsObjectArray.createArray().cast();
    for (int i = 0; i < elementEvents.length(); i++) {
      BindFunction listener = elementEvents.get(i);
      boolean matchNS = namespace == null || namespace.isEmpty()
          || listener.nameSpace.equals(namespace);
      boolean matchEV = eventbits <= 0 || listener.hasEventType(eventbits);
      if (matchNS && matchEV) {
        continue;
      }
      newList.add(listener);
    }
    elementEvents = newList;
  }

  public void unbind(String event) {
    // TODO: nameSpaces in event lists
    String nameSpace = event.replaceFirst("^[^\\.]+\\.*(.*)$", "$1");
    String eventName = event.replaceFirst("^([^\\.]+).*$", "$1");
    int b = getEventBits(eventName);
    unbind(b, nameSpace);
  }

  private void clean() {
    cleanGQListeners(element);
    elementEvents = JsObjectArray.createArray().cast();
    liveBindFunctionByEventType = new HashMap<Integer, LiveBindFunction>();
  }

  private void sink() {
    setGQueryEventListener(element, this);
    DOM.setEventListener((com.google.gwt.user.client.Element) element, this);
    if (eventBits == ONSUBMIT) {
      sinkSubmitEvent(element);
    } else {
      if ((eventBits | Event.FOCUSEVENTS) == Event.FOCUSEVENTS
          && element.getAttribute("tabIndex").length() == 0) {
        element.setAttribute("tabIndex", "0");
      }
      DOM.sinkEvents((com.google.gwt.user.client.Element) element, eventBits
          | DOM.getEventsSunk((com.google.gwt.user.client.Element) element));

    }
  }
  
  private int getEventBits(String... events) {
    int ret = 0;
    for (String e: events) {
      String[] parts = e.split("[\\s,]+");
      for (String s : parts) {
        if ("submit".equals(s)) {
          ret |= ONSUBMIT;
        } else {
          int event = Event.getTypeInt(s);
          if (event > 0) {
            ret |= event;
          }
        }
      }
    }
    return ret;
  }
  
  private int getTypeInt(String eventName) {
    return "submit".equals(eventName) ? ONSUBMIT : Event.getTypeInt(eventName);
  }

  public void cleanEventDelegation() {
    for (LiveBindFunction function : liveBindFunctionByEventType.values()){
      function.clean();
    }
  }
}
