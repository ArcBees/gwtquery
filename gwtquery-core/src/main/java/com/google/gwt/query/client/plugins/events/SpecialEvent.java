/*
 * Copyright 2014, The gwtquery team.
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

import com.google.gwt.dom.client.Element;
import com.google.gwt.query.client.Function;
import com.google.gwt.user.client.Event;

/**
 * Interface used to register special events.
 *
 * Use EventsListeners.special.add(
 */
public interface SpecialEvent {

  /**
   * Default implementation of SpecialEvents for simple cases like
   * creating event aliases. Extend this for creating more complex
   * cases.
   */
  public static class DefaultSpecialEvent implements SpecialEvent {
    protected final String delegateType;
    protected final String type;

    protected Function handler = new Function() {
      public boolean f(Event e, Object... arg) {
        setEvent(e);
        EventsListener.getInstance(getElement()).dispatchEvent(e, type);
        return true;
      };
    };

    public DefaultSpecialEvent(String type, String delegateType) {
      this.type = type;
      this.delegateType = delegateType;
    }

    protected EventsListener listener(Element e) {
      return EventsListener.getInstance(e);
    }

    @Override
    public void add(Element e, String eventType, String nameSpace, Object data, Function f) {
      // Nothing to do, let gQuery use default events mechanism
    }

    @Override
    public boolean hasHandlers(Element e) {
      return listener(e).hasHandlers(Event.getTypeInt(type), type, handler);
    }

    @Override
    public void remove(Element e, String eventType, String nameSpace, Function f) {
      // Nothing to do, let gQuery use default events mechanism
    }

    @Override
    public boolean setup(Element e) {
      if (!hasHandlers(e)) {
        listener(e).bind(delegateType, null, handler);
      }
      return false;
    }

    @Override
    public boolean tearDown(Element e) {
      if (!hasHandlers(e)) {
        listener(e).unbind(delegateType, handler);
      }
      return false;
    }
  }

  /**
   * For each bind call the add function is called.
   */
  void add(Element e, String eventType, String nameSpace, Object data, Function f);

  /**
   * Return true if there are handlers bound to this special event.
   */
  boolean hasHandlers(Element e);

  /**
   * For each unbind call the remove function is called.
   */
  void remove(Element e, String eventType, String nameSpace, Function f);

  /**
   * When the first event handler is bound for an EventsListener gQuery executes the setup function.
   *
   * If the method returns false means that gQuery has to run the default bind for the event before
   * calling add.
   */
  boolean setup(Element e);

  /**
   * The last unbind call triggers the tearDown method.
   *
   * If the method returns false means that gQuery has to run the default unbind for the event
   * before calling remove.
   */
  boolean tearDown(Element e);
}
