/*
 * Copyright 2011, The gwtquery team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.gwt.query.client.plugins.events;

import static com.google.gwt.query.client.GQuery.$;

import com.google.gwt.core.client.Duration;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.js.JsCache;
import com.google.gwt.query.client.js.JsMap;
import com.google.gwt.query.client.js.JsNamedArray;
import com.google.gwt.query.client.js.JsObjectArray;
import com.google.gwt.query.client.js.JsUtils;
import com.google.gwt.query.client.plugins.events.SpecialEvent.DefaultSpecialEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class implements an event queue instance for one Element. The queue instance is configured
 * as the default event listener in GWT.
 *
 * The reference to this queue is stored as a unique variable in the element's DOM.
 *
 * The class takes care of calling the appropriate functions for each browser event and it also
 * calls sinkEvents method.
 *
 */
public class EventsListener implements EventListener {

  /**
   * Used for simulating mouseenter and mouseleave events.
   */
  private static class MouseSpecialEvent extends DefaultSpecialEvent {
    public MouseSpecialEvent(final String type, String delegateType) {
      super(type, delegateType);
      handler =  new Function() {
        public boolean f(Event e, Object... arg) {
          EventTarget eventTarget = e.getCurrentEventTarget();
          Element target = eventTarget != null ? eventTarget.<Element> cast() : null;

          EventTarget relatedEventTarget = e.getRelatedEventTarget();
          Element related = relatedEventTarget != null ? relatedEventTarget.<Element> cast() : null;

          if (related == null || (related != target && !GQuery.contains(target, related))) {
            getInstance(target).dispatchEvent(e, type);
          }
          return true;
        };
      };
    }
  }

  /**
   * Utility class to split a list of events with or without namespaces.
   */
  public static class EventName {
    public final String nameSpace;
    public final String eventName;
    public EventName(String n, String e) {
      nameSpace = n;
      eventName = e;
    }

    public static List<EventName> split(String events) {
      List<EventName> ret = new ArrayList<>();
      String[] parts = events.split("[\\s,]+");
      for (String event : parts) {
        String[] tmp = event.split("\\.", 2);
        String eventName = tmp[0];
        String nameSpace = tmp.length > 1 ? tmp[1] : null;
        ret.add(new EventName(nameSpace, eventName));
      }
      return ret;
    }
  }

  /**
   * The function used per each element event.
   */
  private static class BindFunction {
    Object data;
    Function function;
    String nameSpace;
    int times;
    int type;
    String eventName;

    BindFunction(int type, String eventName, String nameSpace, Function function, Object data,
        int times) {
      this.times = times;
      this.eventName = eventName;
      this.type = type;
      this.function = function;
      this.data = data;
      this.nameSpace = nameSpace != null ? nameSpace : "";
    }

    public boolean fire(Event event, Object[] eventData) {
      return fire(event, event.getTypeInt(), event.getType(), eventData);
    }

    public boolean fire(Event event, int typeInt, String type, Object[] eventData) {
      if (times != 0) {
        times--;
        Object[] arguments;
        eventData = eventData != null ? eventData : new Object[0];
        // The argument of the function will be first the data attached to the handler then the
        // data attached to the event.
        if (data != null) {
          Object[] handlerData = data.getClass().isArray() ? (Object[]) data : new Object[]{data};
          arguments = new Object[handlerData.length + eventData.length];
          System.arraycopy(handlerData, 0, arguments, 0, handlerData.length);
          System.arraycopy(eventData, 0, arguments, handlerData.length, eventData.length);
        } else {
          arguments = eventData;
        }
        // FIXME(manolo): figure out when this is null, and fix or comment it.
        if (function != null) {
          return function.fe(event, arguments);
        }
      }
      return true;
    }

    public boolean hasEventType(int etype) {
      return  type != BITLESS && etype != BITLESS && (type & etype) != 0;
    }

    public boolean isTypeOf(String eName) {
      return eventName != null && eventName.equalsIgnoreCase(eName);
    }

    /**
     * Remove a set of events. The bind function will not be fire anymore for those events
     *
     * @param eventBits the set of events to unsink
     *
     */
    public int unsink(int eventBits) {
      if (eventBits <= 0) {
        type = 0;
      } else {
        type = type & ~eventBits;
      }

      return type;
    }

    @Override
    public String toString() {
      return "bind function for event type " + (eventName != null ? eventName : "" + type);
    }

    public boolean isEquals(Function f) {
      assert f != null : "function f cannot be null";
      return f.equals(function);
    }
  }

  /**
   * {@link BindFunction} used for live() method.
   *
   */
  private static class LiveBindFunction extends BindFunction {

    JsNamedArray<JsObjectArray<BindFunction>> bindFunctionBySelector;

    LiveBindFunction(String eventName, String namespace, Object data) {
      super(BITLESS, eventName, namespace, null, data, -1);
      clean();
    }

    LiveBindFunction(int type, String namespace, Object data) {
      super(type, null, namespace, null, data, -1);
      clean();
    }

    /**
     * Add a {@link BindFunction} for a specific css selector.
     */
    public void addBindFunctionForSelector(String cssSelector, BindFunction f) {
      JsObjectArray<BindFunction> bindFunctions = bindFunctionBySelector.get(cssSelector);
      if (bindFunctions == null) {
        bindFunctions = JsObjectArray.create();
        bindFunctionBySelector.put(cssSelector, bindFunctions);
      }

      bindFunctions.add(f);
    }

    public void clean() {
      bindFunctionBySelector = JsNamedArray.create();
    }

    @Override
    public boolean fire(Event event, int typeInt, String type, Object[] eventData) {
      if (isEmpty()) {
        return true;
      }

      // first element where the event was fired
      Element eventTarget = getEventTarget(event);
      // last element where the event was dispatched on
      Element liveContextElement = getCurrentEventTarget(event);

      if (eventTarget == null || liveContextElement == null) {
        return true;
      }

      // Compute the live selectors which respond to this event type
      List<String> validSelectors = new ArrayList<>();
      for (String cssSelector : bindFunctionBySelector.keys()) {
        JsObjectArray<BindFunction> bindFunctions = bindFunctionBySelector.get(cssSelector);
        for (int i = 0; bindFunctions != null && i < bindFunctions.length(); i++) {
          BindFunction f = bindFunctions.get(i);
          if (f.hasEventType(typeInt) || f.isTypeOf(type)) {
            validSelectors.add(cssSelector);
            break;
          }
        }
      }

      // Create a structure of elements which matches the selectors
      JsNamedArray<NodeList<Element>> realCurrentTargetBySelector =
          $(eventTarget).closest(validSelectors.toArray(new String[0]), liveContextElement);
      // nothing matches the selectors
      if (realCurrentTargetBySelector.length() == 0) {
        return true;
      }

      Element stopElement = null;
      GqEvent gqEvent = GqEvent.create(event);
      for (String cssSelector : realCurrentTargetBySelector.keys()) {
        JsObjectArray<BindFunction> bindFunctions = bindFunctionBySelector.get(cssSelector);
        for (int i = 0; bindFunctions != null && i < bindFunctions.length(); i++) {
          BindFunction f = bindFunctions.get(i);
          if (f.hasEventType(typeInt) || f.isTypeOf(type)) {
            NodeList<Element> n = realCurrentTargetBySelector.get(cssSelector);
            for (int j = 0; n != null && j < n.getLength(); j++) {
              Element element = n.getItem(j);
              // When an event fired in an element stops bubbling we have to fire also all other
              // handlers for this element bound to this element
              if (stopElement == null || element.equals(stopElement)) {
                gqEvent.setCurrentElementTarget(element);
                // data
                eventData = $(element).data(EVENT_DATA);
                if (!f.fire(gqEvent, eventData)) {
                  stopElement = element;
                }
              }
            }
          }
        }
      }

      // trick to reset the right currentTarget on the original event on ie
      gqEvent.setCurrentElementTarget(liveContextElement);
      return stopElement == null;
    }

    /**
     * Remove the BindFunction associated to this cssSelector.
     */
    public void removeBindFunctionForSelector(String cssSelector, String nameSpace) {
      if (nameSpace == null) {
        bindFunctionBySelector.delete(cssSelector);
      } else {
        JsObjectArray<BindFunction> functions = bindFunctionBySelector.get(cssSelector);

        if (functions == null || functions.length() == 0) {
          return;
        }
        JsObjectArray<BindFunction> newFunctions = JsObjectArray.create();

        for (int i = 0; i < functions.length(); i++) {
          BindFunction f = functions.get(i);
          if (nameSpace != null && !nameSpace.equals(f.nameSpace)) {
            newFunctions.add(f);
          }
        }

        bindFunctionBySelector.delete(cssSelector);
        if (newFunctions.length() > 0) {
          bindFunctionBySelector.put(cssSelector, newFunctions);
        }
      }
    }

    /**
     * Tell if no {@link BindFunction} are linked to this object.
     *
     * @return
     */
    public boolean isEmpty() {
      return bindFunctionBySelector.length() == 0;
    }

    @Override
    public String toString() {
      return "live bind function for selector "
          + bindFunctionBySelector.<JsCache> cast().tostring();
    }

    /**
     * Return the element whose the listener fired last. It represent the context element where the
     * {@link LiveBindFunction} was binded.
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
     * Return the element that was the actual target of the element.
     */
    private Element getEventTarget(Event e) {
      EventTarget eventTarget = e.getEventTarget();

      if (!Element.is(eventTarget)) {
        return null;
      }

      return Element.as(eventTarget);
    }
  }

  public static final String EVENT_DATA = "___event_datas";
  public static final int BITLESS = -1;

  public static String MOUSEENTER = "mouseenter";
  public static String MOUSELEAVE = "mouseleave";

  public static Map<String, SpecialEvent> special;

  static {
    // Register some special events which already exist in jQuery
    special = new HashMap<>();
    special.put(MOUSEENTER, new MouseSpecialEvent(MOUSEENTER, "mouseover"));
    special.put(MOUSELEAVE, new MouseSpecialEvent(MOUSELEAVE, "mouseout"));
  }

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

  /**
   * We have to set the gQuery event listener to the element again when
   * the element is a widget, because when GWT detaches a widget it removes the
   * event listener.
   */
  public static void rebind(Element e) {
    EventsListener ret = getGQueryEventListener(e);
    if (ret != null) {
      DOM.setEventListener((com.google.gwt.user.client.Element) e, ret);
    }
  }

  private static native void cleanGQListeners(Element elem) /*-{
    if (elem.__gwtlistener) {
      @com.google.gwt.user.client.DOM::setEventListener(*)(elem, elem.__gwtlistener);
    }
    elem.__gwtlistener = elem.__gqueryevent = elem.__gquery = null;
  }-*/;

  private static native EventsListener getGQueryEventListener(Element elem) /*-{
    return elem.__gqueryevent;
  }-*/;

  private static native EventListener getGwtEventListener(Element elem) /*-{
    return elem.__gwtlistener;
  }-*/;

  private static native void init(Element elem, EventsListener gqevent) /*-{
    elem.__gwtlistener = @com.google.gwt.user.client.DOM::getEventListener(*)(elem);
    elem.__gqueryevent = gqevent;
    // Someone has reported that in IE the init can be called multiple times
    // causing a loop. We need some test to demonstrate this behaviour though.
    // Anyway we check this condition to avoid looping
    if (elem.__gwtlistener == gqevent) elem.__gwtlistener = null;
  }-*/;

  private static native void sinkBitlessEvent(Element elem, String name) /*-{
    if (!elem.__gquery)
      elem.__gquery = [];
    if (elem.__gquery[name])
      return;
    elem.__gquery[name] = true;

    var handle = function(event) {
      elem.__gqueryevent.@com.google.gwt.query.client.plugins.events.EventsListener::onBrowserEvent(Lcom/google/gwt/user/client/Event;)(event);
    };

    if (elem.addEventListener)
      elem.addEventListener(name, handle, true);
    else
      elem.attachEvent("on" + name, handle);
  }-*/;

  int eventBits = 0;
  double lastEvnt = 0;
  String lastType = "";

  private Element element;
  private JsObjectArray<BindFunction> elementEvents = JsObjectArray.createArray().cast();
  private JsMap<Integer, LiveBindFunction> liveBindFunctionByEventType = JsMap.create();
  private JsMap<String, LiveBindFunction> liveBindFunctionByEventName = JsMap.create();

  private EventsListener(Element element) {
    this.element = element;
    init(element, this);
  }

  public void bind(int eventbits, final Object data, Function... funcs) {
    bind(eventbits, null, data, funcs);
  }

  public void bind(int eventbits, final Object data, final Function function, int times) {
    bind(eventbits, null, null, data, function, times);
  }

  public void bind(int eventbits, String name, final Object data, Function... funcs) {
    for (Function function : funcs) {
      bind(eventbits, name, null, data, function, -1);
    }
  }

  public void bind(int eventbits, String namespace, Object data, Function function, int times) {
    bind(eventbits, namespace, null, data, function, times);
  }

  public void bind(String events, final Object data, Function... funcs) {
    if (funcs.length == 0 || funcs[0] == null) {
      unbind(events, null);
    }

    for (EventName ev : EventName.split(events)) {
      SpecialEvent hook = special.get(ev.eventName);
      boolean bind = hook == null || hook.setup(element) == false;
      for (Function function : funcs) {
        int b = Event.getTypeInt(ev.eventName);
        if (bind) {
          bind(b, ev.nameSpace, ev.eventName, data, function, -1);
        }
        if (hook != null) {
          hook.add(element, ev.eventName, ev.nameSpace, data, function);
        }
      }
    }
  }

  public void bind(int eventbits, String namespace, String eventName, Object data, Function function, int times) {
    sink(eventbits, eventName);
    elementEvents.add(new BindFunction(eventbits, eventName, namespace, function, data, times));
  }

  public void die(String events, String cssSelector) {
    for (EventName ev : EventName.split(events)) {
      SpecialEvent hook = special.get(ev.eventName);
      boolean unbind = hook == null || hook.tearDown(element) == false;
      if (unbind) {
        die(Event.getTypeInt(ev.eventName), ev.nameSpace, ev.eventName, cssSelector);
      }
      if (hook != null) {
        hook.remove(element, ev.eventName, ev.nameSpace, null);
      }
    }
  }

  public void die(int eventbits, String nameSpace, String eventName, String cssSelector) {
    if (eventbits <= 0) {
      if (eventName != null && eventName.length() > 0) {
        LiveBindFunction liveBindFunction = liveBindFunctionByEventName.get(eventName);
        maybeRemoveLiveBindFunction(liveBindFunction, cssSelector, BITLESS, eventName, nameSpace);
      } else {
        // if eventbits == -1 and eventName is null, remove all event handlers for this selector
        for (String k : liveBindFunctionByEventType.keys()) {
          int bits = Integer.parseInt(k);
          LiveBindFunction liveBindFunction = liveBindFunctionByEventType.get(bits);
          maybeRemoveLiveBindFunction(liveBindFunction, cssSelector, bits, null, nameSpace);
        }

        for (String k : liveBindFunctionByEventName.keys()) {
          int realKey = Integer.parseInt(k);
          LiveBindFunction liveBindFunction = liveBindFunctionByEventName.get(realKey);
          if (liveBindFunction != null) {
            String eName = liveBindFunction.eventName;
            maybeRemoveLiveBindFunction(liveBindFunction, cssSelector, BITLESS, eName, nameSpace);
          }
        }
      }
    } else {
      LiveBindFunction liveBindFunction = liveBindFunctionByEventType.get(eventbits);
      maybeRemoveLiveBindFunction(liveBindFunction, cssSelector, eventbits, null, nameSpace);
    }
  }

  private void maybeRemoveLiveBindFunction(LiveBindFunction liveBindFunction, String cssSelector,
      int eventbits, String eventName, String nameSpace) {
    if (liveBindFunction != null) {
      liveBindFunction.removeBindFunctionForSelector(cssSelector, nameSpace);
      if (liveBindFunction.isEmpty()) {
        if (eventbits != BITLESS) {
          liveBindFunctionByEventType.remove(eventbits);
        } else {
          liveBindFunctionByEventName.remove(eventName);
        }
      }
    }
  }

  /**
   * Dispatch an event in this element.
   */
  public void dispatchEvent(Event event) {
    dispatchEvent(event, event.getType());
  }

  /**
   * Dispatch an event in this element but changing the type,
   * it's useful for special events.
   */
  public void dispatchEvent(Event event, String eventName) {
    int typeInt = Event.getTypeInt(eventName);
    Object[] handlerData = $(element).data(EVENT_DATA);
    for (int i = 0, l = elementEvents.length(); i < l; i++) {
      BindFunction listener = elementEvents.get(i);
      String namespace = JsUtils.prop(event, "namespace");
      boolean matchEV = listener != null && (listener.hasEventType(typeInt) || listener.isTypeOf(eventName));
      boolean matchNS = matchEV && (isNullOrEmpty(namespace) || listener.nameSpace.equals(namespace));
      if (matchEV && matchNS) {
        if (!listener.fire(event, typeInt, eventName, handlerData)) {
          event.stopPropagation();
          event.preventDefault();
        }
      }
    }
  }

  /**
   * Return the original gwt EventListener associated with this element, before gquery replaced it
   * to introduce its own event handler.
   */
  public EventListener getOriginalEventListener() {
    return getGwtEventListener(element);
  }

  public void live(String events, String cssSelector, Object data, Function... funcs) {
    for (EventName ev : EventName.split(events)) {
      SpecialEvent hook = special.get(ev.eventName);
      boolean bind = hook == null || hook.setup(element) == false;
      for (Function function : funcs) {
        int b = Event.getTypeInt(ev.eventName);
        if (bind) {
          live(b, ev.nameSpace, ev.eventName, cssSelector, data, function);
        }
        if (hook != null) {
          hook.add(element, ev.eventName, ev.nameSpace, data, function);
        }
      }
    }
  }

  public void live(int eventbits, String nameSpace, String eventName, String cssSelector,
      Object data, Function... funcs) {
    if (eventbits != BITLESS) {
      liveBitEvent(eventbits, nameSpace, cssSelector, data, funcs);
    } else {
      liveBitlessEvent(eventName, nameSpace, cssSelector, data, funcs);
    }
  }

  private void liveBitlessEvent(String eventName, String nameSpace, String cssSelector,
      Object data, Function... funcs) {
    LiveBindFunction liveBindFunction = liveBindFunctionByEventName.get(eventName);

    if (liveBindFunction == null) {
      liveBindFunction = new LiveBindFunction(eventName, "live", data);
      sink(BITLESS, eventName);
      elementEvents.add(liveBindFunction);
      liveBindFunctionByEventName.put(eventName, liveBindFunction);
    }

    for (Function f : funcs) {
      liveBindFunction.addBindFunctionForSelector(cssSelector, new BindFunction(BITLESS, eventName,
          nameSpace, f, data, -1));
    }
  }

  private void liveBitEvent(int eventbits, String nameSpace, String cssSelector, Object data,
      Function... funcs) {
    for (int i = 0; i < 28; i++) {
      int event = (int) Math.pow(2, i);
      if ((eventbits & event) == event) {

        // is a LiveBindFunction already attached for this kind of event
        LiveBindFunction liveBindFunction = liveBindFunctionByEventType.get(event);
        if (liveBindFunction == null) {
          liveBindFunction = new LiveBindFunction(event, "live", data);
          sink(eventbits, null);
          elementEvents.add(liveBindFunction);
          liveBindFunctionByEventType.put(event, liveBindFunction);
        }

        for (Function f : funcs) {
          liveBindFunction.addBindFunctionForSelector(cssSelector, new BindFunction(event,
              null, nameSpace, f, data, -1));
        }
      }
    }
  }

  public void onBrowserEvent(Event event) {
    if (JsUtils.isDefaultPrevented(event)) {
      return;
    }
    double now = Duration.currentTimeMillis();
    // Workaround for Issue_20
    if (lastType.equals(event.getType()) && now - lastEvnt < 10
        && "body".equalsIgnoreCase(element.getTagName())) {
      return;
    }
    lastEvnt = now;
    lastType = event.getType();

    // Execute the original Gwt listener
    if (getOriginalEventListener() != null && getOriginalEventListener() != this) {
      getOriginalEventListener().onBrowserEvent(event);
    }

    dispatchEvent(event);
  }

  public void unbind(int eventbits) {
    unbind(eventbits, null, null, null);
  }

  public void unbind(int eventbits, String namespace, String eventName, Function f) {
    JsObjectArray<BindFunction> newList = JsObjectArray.createArray().cast();
    for (int i = 0; i < elementEvents.length(); i++) {
      BindFunction listener = elementEvents.get(i);
      boolean matchNS = isNullOrEmpty(namespace) || listener.nameSpace.equals(namespace);
      boolean matchEV = eventbits <= 0 || listener.hasEventType(eventbits);
      boolean matchEVN = matchEV || listener.isTypeOf(eventName);
      boolean matchFC = f == null || listener.isEquals(f);

      if (matchNS && matchEV && matchEVN && matchFC) {
        int currentEventbits = listener.unsink(eventbits);
        if (currentEventbits == 0) {
          // the BindFunction doesn't listen anymore on any events
          continue;
        }
      }
      newList.add(listener);
    }
    elementEvents =  newList;
  }

  /**
   * Return true if the element is listening for the
   * given eventBit or eventName.
   */
  public boolean hasHandlers(int eventBits, String eventName) {
    return hasHandlers(eventBits, eventName, null);
  }

  /**
   * Return true if the element is listening for the
   * given eventBit or eventName and the handler matches.
   */
  public boolean hasHandlers(int eventBits, String eventName, Function handler) {
    for (int i = 0, j = elementEvents.length(); i < j; i++) {
      BindFunction function = elementEvents.get(i);
      if ((function.hasEventType(eventBits) || function.isTypeOf(eventName))
         && (handler == null || function.isEquals(handler))) {
        return true;
      }
    }
    return false;
  }

  private static boolean isNullOrEmpty(String s) {
    return s == null || s.isEmpty();
  }

  public void unbind(String events, Function f) {
    for (EventName ev : EventName.split(events)) {
      SpecialEvent hook = special.get(ev.eventName);
      boolean unbind = hook == null || hook.tearDown(element) == false;
      if (unbind) {
        unbind(Event.getTypeInt(ev.eventName), ev.nameSpace, ev.eventName, f);
      }
      if (hook != null) {
        hook.remove(element, ev.eventName, ev.nameSpace, f);
      }
    }
  }

  public void clean() {
    cleanGQListeners(element);
    elementEvents = JsObjectArray.createArray().cast();
    liveBindFunctionByEventType = JsMap.create();
    eventBits = 0;
  }

  private void sink(int eventbits, String eventName) {
    // ensure that the gwtQuery's event listener is set as event listener of the element
    DOM.setEventListener((com.google.gwt.user.client.Element) element, this);

    if (eventbits != BITLESS) {
      eventBits |= eventbits;

      if ((eventBits | Event.FOCUSEVENTS) == Event.FOCUSEVENTS
          && JsUtils.isElement(element)
          && element.getAttribute("tabIndex").length() == 0) {
        element.setAttribute("tabIndex", "0");
      }
      DOM.sinkEvents((com.google.gwt.user.client.Element) element, eventBits
          | DOM.getEventsSunk((com.google.gwt.user.client.Element) element));
    } else {
      sinkBitlessEvent(element, eventName);
    }
  }

  public void cleanEventDelegation() {
    for (String k : liveBindFunctionByEventType.keys()) {
      LiveBindFunction function = liveBindFunctionByEventType.<JsCache> cast().get(k);
      function.clean();
    }
  }
}
