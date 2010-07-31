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
package com.google.gwt.query.client.plugins;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.JSArray;
import com.google.gwt.query.client.Plugin;
import com.google.gwt.user.client.Event;

/**
 * GQuery Plugin for handling and queuing browser events.
 */
public class Events extends GQuery {

  public static final Class<Events> Events = Events.class;

  static {
    GQuery.registerPlugin(Events.class, new Plugin<Events>() {
      public Events init(GQuery gq) {
        return new Events(gq.get());
      }
    });
  }

  public Events(Element element) {
    super(element);
  }

  public Events(JSArray elements) {
    super(elements);
  }

  public Events(NodeList<Element> list) {
    super(list);
  }
  
  /**
   * Binds a set of handlers to a particular Event for each matched element.
   * 
   * The event handlers are passed as Functions that you can use to prevent
   * default behavior. To stop both default action and event bubbling, the
   * function event handler has to return false.
   * 
   * You can pass an additional Object data to your Function as the second
   * parameter
   * 
   */
  public Events bind(int eventbits, Object data, Function...funcs) {
    for (Element e : elements()) {
      EventsListener.getInstance(e).bind(eventbits, data, funcs);
    }
    return this;
  }

  /**
   * Binds a set of handlers to a particular Event for each matched element.
   * 
   * The namespace is a way to group events of the same type, making easier unbind
   * specific handlers.
   * 
   * The event handlers are passed as Functions that you can use to prevent
   * default behavior. To stop both default action and event bubbling, the
   * function event handler has to return false.
   * 
   * You can pass an additional Object data to your Function
   * 
   */  
  public Events bind(int eventbits, String namespace, Object data, Function...funcs) {
    for (Element e : elements()) {
      EventsListener.getInstance(e).bind(eventbits, namespace, data, funcs);
    }
    return this;
  }

  /**
   * Binds a set of handlers to a particular Event for each matched element.
   * 
   * The name could contain a namespace which is a way to group events of the same type, 
   * making easier unbind specific handlers.
   * 
   * The event handlers are passed as Functions that you can use to prevent
   * default behavior. To stop both default action and event bubbling, the
   * function event handler has to return false.
   * 
   * You can pass an additional Object data to your Function
   * 
   */  
  public Events bind(String event, Object data, Function...funcs) {
    for (Element e : elements()) {
      EventsListener.getInstance(e).bind(event, data, funcs);
    }
    return this;
  }
  
  /**
   * Binds a handler to a particular Event (like Event.ONCLICK) for each matched
   * element. The handler is executed only once for each element.
   *
   * The event handler is passed as a Function that you can use to prevent
   * default behavior. To stop both default action and event bubbling, the
   * function event handler has to return false.
   *
   * You can pass an additional Object data to your Function as the second
   * parameter
   */  
  public Events one(int eventbits, final Object data, final Function f) {
    for (Element e : elements()) {
      EventsListener.getInstance(e).bind(eventbits, data, f, 1);
    }
    return this;
  }
  
  /**
   * Execute all handlers and behaviors attached to the matched elements for the given event types.
   * 
   * Different event types can be passed joining these using the or bit wise operator.
   * 
   * For keyboard events you can pass a second parameter which represents 
   * the key-code of the pushed key. 
   * 
   * Example: fire(Event.ONCLICK | Event.ONFOCUS)
   * Example: fire(Event.ONKEYDOWN. 'a');
   */
  public Events trigger(int eventbits, int... keys) {
    if ((eventbits | Event.ONBLUR) == Event.ONBLUR)
      dispatchEvent(document.createBlurEvent());
    if ((eventbits | Event.ONCHANGE) == Event.ONCHANGE)
      dispatchEvent(document.createChangeEvent());
    if ((eventbits | Event.ONCLICK) == Event.ONCLICK)
      dispatchEvent(document.createClickEvent(0, 0, 0, 0, 0, false, false, false, false));
    if ((eventbits | Event.ONDBLCLICK) == Event.ONDBLCLICK)
      dispatchEvent(document.createDblClickEvent(0, 0, 0, 0, 0, false, false, false, false));
    if ((eventbits | Event.ONFOCUS) == Event.ONFOCUS)
      dispatchEvent(document.createFocusEvent());
    if ((eventbits | Event.ONKEYDOWN) == Event.ONKEYDOWN)
      dispatchEvent(document.createKeyDownEvent(false, false, false, false, keys[0], 0));
    if ((eventbits | Event.ONKEYPRESS) == Event.ONKEYPRESS)
      dispatchEvent(document.createKeyPressEvent(false, false, false, false, keys[0], 0));
    if ((eventbits | Event.ONKEYUP) == Event.ONKEYUP)
      dispatchEvent(document.createKeyUpEvent(false, false, false, false, keys[0], 0));
    if ((eventbits | Event.ONLOSECAPTURE) == Event.ONLOSECAPTURE)
      triggerHtmlEvent("losecapture");
    if ((eventbits | Event.ONMOUSEDOWN) == Event.ONMOUSEDOWN)
      dispatchEvent(document.createMouseDownEvent(0, 0, 0, 0, 0, false, false, false, false, NativeEvent.BUTTON_LEFT));
    if ((eventbits | Event.ONMOUSEMOVE) == Event.ONMOUSEMOVE)
      dispatchEvent(document.createMouseMoveEvent(0, 0, 0, 0, 0, false, false, false, false, NativeEvent.BUTTON_LEFT));
    if ((eventbits | Event.ONMOUSEOUT) == Event.ONMOUSEOUT)
      dispatchEvent(document.createMouseOutEvent(0, 0, 0, 0, 0, false, false, false, false, NativeEvent.BUTTON_LEFT, null));
    if ((eventbits | Event.ONMOUSEOVER) == Event.ONMOUSEOVER)
      dispatchEvent(document.createMouseOverEvent(0, 0, 0, 0, 0, false, false, false, false, NativeEvent.BUTTON_LEFT, null));
    if ((eventbits | Event.ONMOUSEUP) == Event.ONMOUSEUP)
      dispatchEvent(document.createMouseUpEvent(0, 0, 0, 0, 0, false, false, false, false, NativeEvent.BUTTON_LEFT));
    if ((eventbits | Event.ONSCROLL) == Event.ONSCROLL)
      dispatchEvent(document.createScrollEvent());
    if ((eventbits | Event.ONERROR) == Event.ONERROR)
      dispatchEvent(document.createErrorEvent());
    if ((eventbits | Event.ONMOUSEWHEEL) == Event.ONMOUSEWHEEL)
      dispatchEvent(document.createMouseEvent("mousewheel", true, true, 0, 0, 0, 0, 0, false, false, false, false, NativeEvent.BUTTON_LEFT, null));
    if (eventbits == EventsListener.ONSUBMIT)
      triggerHtmlEvent("submit");
    return this;
  }
  

  /**
   * Trigger a html event in all matched elements.
   * 
   * @param htmlEvent
   *    An string representing the html event desired 
   */
  public Events triggerHtmlEvent(String htmlEvent) {
    dispatchEvent(document.createHtmlEvent(htmlEvent, true, true));
    return this;
  }
  
  /**
   * Removes all handlers, that matches the events bits passed, from each
   * element.
   * 
   * Example: unbind(Event.ONCLICK | Event.ONMOUSEOVER)
   */
  public Events unbind(int eventbits) {
    for (Element e : elements()) {
      EventsListener.getInstance(e).unbind(eventbits);
    }
    return this;
  }
  
  /**
   * Removes all handlers, that matches the events bits and the namespace
   * passed, from each element.
   * 
   * Example: unbind(Event.ONCLICK | Event.ONMOUSEOVER, "my.namespace")
   */
  public Events unbind(int eventbits, String name) {
    for (Element e : elements()) {
      EventsListener.getInstance(e).unbind(eventbits, name);
    }
    return this;
  }
  
  /**
   * Removes all handlers, that matches event name passed. This name
   * could contain a namespace.
   * 
   * Example: unbind("click.my.namespace")
   */
  public Events unbind(String name) {
    for (Element e : elements()) {
      EventsListener.getInstance(e).unbind(name);
    }
    return this;
  }
  
  private void dispatchEvent(NativeEvent evt) {
    for (Element e : elements()) {
      e.dispatchEvent(evt);
    }
  }
}
