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
package com.google.gwt.query.client.plugins;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.LazyBase;

/**
 * LazyEvents.
 * @param <T>
 */
public interface LazyEvents<T> extends LazyBase<T> {

  /**
   * Binds a set of handlers to a particular Event for each matched element.
   *
   * The event handlers are passed as Functions that you can use to prevent default behavior. To
   * stop both default action and event bubbling, the function event handler has to return false.
   *
   * You can pass an additional Object data to your Function as the second parameter
   *
   */
  LazyEvents<T> bind(int eventbits, Object data, Function... funcs);

  /**
   * Binds a set of handlers to a particular Event for each matched element.
   *
   * The namespace is a way to group events of the same type, making easier unbind specific
   * handlers.
   *
   * The event handlers are passed as Functions that you can use to prevent default behavior. To
   * stop both default action and event bubbling, the function event handler has to return false.
   *
   * You can pass an additional Object data to your Function
   *
   */
  LazyEvents<T> bind(int eventbits, String namespace, Object data, Function... funcs);

  /**
   * Binds a set of handlers to a particular Event for each matched element.
   *
   * The name could contain a namespace which is a way to group events of the same type, making
   * easier unbind specific handlers.
   *
   * The event handlers are passed as Functions that you can use to prevent default behavior. To
   * stop both default action and event bubbling, the function event handler has to return false.
   *
   * You can pass an additional Object data to your Function
   *
   */
  LazyEvents<T> bind(String event, Object data, Function... funcs);

  GQuery die(int eventbits, String nameSpace);

  GQuery die(int eventbits);

  /**
   * Remove an event handlers previously attached using live() The selector used with it must match
   * exactly the selector initially used with live(). if <code>eventName</code> is null, all event
   * handlers corresponding of the GQuery selector will be removed
   */
  GQuery die(String eventName);

  GQuery live(int eventbits, Object data, Function... funcs);

  GQuery live(int eventbits, String nameSpace, Object data, Function... funcs);

  GQuery live(String eventName, Object data, Function... funcs);

  /**
   * Bind an event handler to be fired when the mouse enter an element, or trigger that handler on
   * an element if no functions are provided.
   *
   * The mouseenter event differs from mouseover in the way it handles event bubbling. When
   * mouseover is used on an element having inner element(s), then when the mouse pointer moves
   * hover of the Inner element, the handler would be triggered. This is usually undesirable
   * behavior. The mouseenter event, on the other hand, only triggers its handler when the mouse
   * enters the element it is bound to, not a descendant.
   */
  GQuery mouseenter(Function... f);

  /**
   * Bind an event handler to be fired when the mouse leaves an element, or trigger that handler on
   * an element if no functions are provided.
   *
   * The mouseleave event differs from mouseout in the way it handles event bubbling. When mouseout
   * is used on an element having inner element(s), then when the mouse pointer moves out of the
   * Inner element, the handler would be triggered. This is usually undesirable behavior. The
   * mouseleave event, on the other hand, only triggers its handler when the mouse leaves the
   * element it is bound to, not a descendant.
   */
  GQuery mouseleave(Function... fs);

  /**
   * Binds a handler to a particular Event (like Event.ONCLICK) for each matched element. The
   * handler is executed only once for each element.
   *
   * The event handler is passed as a Function that you can use to prevent default behavior. To stop
   * both default action and event bubbling, the function event handler has to return false.
   *
   * You can pass an additional Object data to your Function as the second parameter
   */
  LazyEvents<T> one(int eventbits, Object data, Function f);

  /**
   * Execute all handlers and behaviors attached to the matched elements for the given event types.
   *
   * Different event types can be passed joining these using the or bit wise operator.
   *
   * For keyboard events you can pass a second parameter which represents the key-code of the pushed
   * key.
   *
   * Example: fire(Event.ONCLICK | Event.ONFOCUS) Example: fire(Event.ONKEYDOWN. 'a');
   */
  LazyEvents<T> trigger(int eventbits, int... keys);

  /**
   * Trigger a native event in all matched elements.
   *
   * @param nativeEvent the browser native event.
   * @functions a set of function to run if the event is not canceled.
   */
  LazyEvents<T> trigger(NativeEvent nativeEvent, Function... functions);

  /**
   * Trigger a html event in all matched elements.
   *
   * @param htmlEvent A string representing the desired html event.
   * @functions a set of function to run if the event is not canceled.
   */
  LazyEvents<T> triggerHtmlEvent(String htmlEvent, Function... functions);

  /**
   * Trigger a html event in all matched elements.
   *
   * @param htmlEvent An string representing the desired html event.
   * @functions a set of function to run if the event is not canceled.
   */
  LazyEvents<T> triggerHtmlEvent(String htmlEvent, Object[] datas, Function... functions);

  /**
   * Removes all handlers, that matches the events bits passed, from each element.
   *
   * Example: unbind(Event.ONCLICK | Event.ONMOUSEOVER)
   */
  LazyEvents<T> unbind(int eventbits);

  LazyEvents<T> off();

  /**
   * Removes all handlers, that matches the events bits and the namespace passed, from each element.
   *
   * Example: unbind(Event.ONCLICK | Event.ONMOUSEOVER, "my.namespace")
   */
  LazyEvents<T> unbind(int eventbits, String name, Function f);

  /**
   * Removes all handlers, that matches the event name passed.
   *
   * This name could contain a namespace.
   *
   * Example: unbind("click.my.namespace")
   */
  LazyEvents<T> unbind(String name);

  /**
   * Removes the function passed as parameter from the event list matching the event name passed.
   * This name could contain a namespace.
   *
   * Example: unbind("click.my.namespace", myFunction)
   */
  LazyEvents<T> unbind(String name, Function f);

  LazyEvents<T> undelegate();

}
