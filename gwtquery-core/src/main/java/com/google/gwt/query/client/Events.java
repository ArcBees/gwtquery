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
import com.google.gwt.dom.client.NodeList;
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
   * Binds a handler to a particular Event for each matched element.
   *
   * The event handler is passed as a Function that you can use to prevent
   * default behaviour. To stop both default action and event bubbling, the
   * function event handler has to return false.
   *
   * You can pass an additional Object data to your Function as the second
   * parameter
   */
  public GQuery bind(int eventbits, final Object data, final Function f) {
    for (Element e : elements()) {
      EventsListener.getInstance(e).bind(eventbits, data, f);
    }
    return this;
  }

  /**
   * Fires an event on each matched element.
   *
   * Example: fire(Event.ONCLICK)
   */
  public GQuery fire(int eventbits, int... keys) {
    for (Element e : elements()) {
      EventsListener.getInstance(e).fire(eventbits, keys);
    }
    return this;
  }

  /**
   * Removes all handlers, that matches the events bits passed, from each
   * element.
   *
   * Example: unbind(Event.ONCLICK | Event.ONMOUSEOVER)
   */
  public GQuery unbind(int eventbits) {
    for (Element e : elements()) {
      EventsListener.getInstance(e).unbind(eventbits);
    }
    return this;
  }
}

/**
 * Just a class with static methods for firing element events on demand.
 */
class FireEvents {

  public static void fire(Element element, int eventbits, int... keys) {
    Event event = null;

    String type = getEventTypeStr(eventbits);

    if ((eventbits & Event.MOUSEEVENTS) != 0
        || (eventbits | Event.ONCLICK) == Event.ONCLICK) {
      event = createMouseEventImpl(type);
    } else if ((eventbits & Event.KEYEVENTS) != 0) {
      event = createKeyEventImpl(type, keys[0]);
    } else if ((eventbits & Event.FOCUSEVENTS) != 0) {
      event = createHtmlEventImpl(type);
    }

    dispatchEvent(element, event);
  }

  private static native Event createHtmlEventImpl(String type) /*-{
   var event = $doc.createEvent('HTMLEvents');
   event.initEvent( type, true, true);
   return event;
  }-*/;

  private static native Event createKeyEventImpl(String type, int keycode) /*-{
   var event;
     if( $wnd.KeyEvent ) {
        event = $doc.createEvent('KeyEvents');
       event.initKeyEvent( type, true, true, $wnd, false, false, false, false, keycode, 0 );
     } else {
        event = $doc.createEvent('UIEvents');
       event.initUIEvent( type, true, true, $wnd, 1 );
       event.keyCode = keycode;
     }
    return event;
  }-*/;

  private static native Event createMouseEventImpl(String type) /*-{
   var event = $doc.createEvent('MouseEvents');
    event.initEvent(type, true, true);
    return event;
  }-*/;

  private static native void dispatchEvent(Element elem, Event event) /*-{
    elem.dispatchEvent(event);
    if (event.type == 'focus' && elem.focus)
      elem.focus();
    else if (event.type == 'blur' && elem.focus)  
      elem.blur();
  }-*/;

  private static String getEventTypeStr(int type) {
    switch (type) {
      case Event.ONBLUR:
        return "blur";
      case Event.ONCHANGE:
        return "change";
      case Event.ONCLICK:
        return "click";
      case Event.ONDBLCLICK:
        return "dblclick";
      case Event.ONFOCUS:
        return "focus";
      case Event.ONKEYDOWN:
        return "keydown";
      case Event.ONKEYPRESS:
        return "keypress";
      case Event.ONKEYUP:
        return "keyup";
      case Event.ONLOSECAPTURE:
        return "losecapture";
      case Event.ONMOUSEDOWN:
        return "mousedown";
      case Event.ONMOUSEMOVE:
        return "mousemove";
      case Event.ONMOUSEOUT:
        return "mouseout";
      case Event.ONMOUSEOVER:
        return "mouseover";
      case Event.ONMOUSEUP:
        return "mouseup";
      case Event.ONSCROLL:
        return "scroll";
      case Event.ONERROR:
        return "error";
      case Event.ONMOUSEWHEEL:
        return "mousewheel";
      default:
        return "";
    }
  }
}