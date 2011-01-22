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
package com.google.gwt.query.client.plugins;

import com.google.gwt.core.client.Duration;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.JSArray;
import com.google.gwt.query.client.plugins.Events;

/**
 * Base class for all plug-in that need to handle some mouse interactions.
 * 
 */
public abstract class MousePlugin extends GQueryUi {

  private Event mouseDownEvent;
  private boolean mouseStarted = false;
  private Duration mouseUpDuration;
  private MouseOptions options;
  private boolean preventClickEvent = false;

  public MousePlugin(Element element) {
    super(element);
  }

  public MousePlugin(GQuery gq) {
    super(gq);
  }

  public MousePlugin(JSArray elements) {
    super(elements);
  }

  public MousePlugin(NodeList<Element> list) {
    super(list);
  }

  protected void destroyMouseHandler() {
    as(Events.Events)
        .unbind(Event.ONMOUSEDOWN | Event.ONCLICK, getPluginName());
  }

  /**
   * Return a String identifying the plugin. This string is used as namespace
   * when we bind handlers.
   * 
   */
  protected abstract String getPluginName();

  /**
   * This method initialize all needed handlers
   * 
   */
  protected void initMouseHandler(MouseOptions options) {
    this.options = options;

    for (final Element e : elements()) {

      $(e).as(Events.Events).bind(Event.ONMOUSEDOWN, getPluginName(),
          (Object) null, new Function() {
            @Override
            public boolean f(com.google.gwt.user.client.Event event) {
              return mouseDown(e, Event.create(event));

            }
          }).bind(Event.ONCLICK, getPluginName(), (Object) null,
          new Function() {
            @Override
            public boolean f(com.google.gwt.user.client.Event event) {
              preventClickEvent |= !mouseClick(e, Event.create(event));

              if (preventClickEvent) {

                preventClickEvent = false;
                event.stopPropagation();
                event.preventDefault();
                return false;
              }

              return true;
            }
          });
    }

  }

  /**
   * Test if the mouse down event must be handled by the plugin or not.
   * 
   */
  protected boolean mouseCapture(Element draggable, Event event) {
    return true;
  }

  /**
   * Method called when mouse click
   * 
   */
  protected boolean mouseClick(Element element, Event event) {
    return true;
  }

  /**
   * Method called when mouse down occur on the element.
   * 
   * You should not override this method. Instead, override
   * {@link #mouseStart(Element, Event)} method
   * 
   */
  protected boolean mouseDown(Element element, Event event) {

    // test if an other plugin handle the mouseStart
    if (isEventAlreadyHandled(event)) {
      return false;
    }
    if (mouseStarted) { // case where we missed a mouseup
      mouseUp(element, event);
    }

    // calculate all interesting variables
    reset(event);

    if (notHandleMouseDown(element, event)) {
      return true;
    }

    if (delayConditionMet() && distanceConditionMet(event)) {
      mouseStarted = mouseStart(element, event);
      if (!mouseStarted) {
        event.getOriginalEvent().preventDefault();
        return true;
      }
    }

    bindOtherMouseEvent(element);

    event.getOriginalEvent().preventDefault();

    markEventAsHandled(event);

    return true;
  }

  /**
   * Method called when the mouse is dragging
   * 
   */
  protected abstract boolean mouseDrag(Element element, Event event);

  /**
   * Method called on MouseMove event.
   * 
   * You should not override this method. Instead, override
   * {@link #mouseMove(Element, Event)} method
   * 
   */
  protected boolean mouseMove(Element element, Event event) {
    if (mouseStarted) {
      event.getOriginalEvent().preventDefault();
      return mouseDrag(element, event);
    }

    if (delayConditionMet() && distanceConditionMet(event)) {
      mouseStarted = mouseStart(element, mouseDownEvent);
      if (mouseStarted) {
        mouseDrag(element, event);
      } else {
        mouseUp(element, event);
      }
    }

    return !mouseStarted;
  }

  /**
   * Method called when the mouse is clicked and all conditions for starting the
   * plugin are met.
   * 
   */
  protected abstract boolean mouseStart(Element element, Event event);

  /**
   * Method called when the mouse button is released
   * 
   */
  protected abstract boolean mouseStop(Element element, Event event);

  /**
   * Method called when mouse is released..
   * 
   * You should not override this method. Instead, override
   * {@link #mouseStop(Element, Event)} method
   * 
   */
  protected boolean mouseUp(Element element, Event event) {
    unbindOtherMouseEvent();
    if (mouseStarted) {
      mouseStarted = false;
      preventClickEvent = (event.getCurrentEventTarget() == mouseDownEvent
          .getCurrentEventTarget());
      mouseStop(element, event);
    }
    return false;

  }

  private void bindOtherMouseEvent(final Element element) {

    $(document).as(Events.Events).bind(Event.ONMOUSEMOVE, getPluginName(),
        (Object) null, new Function() {
          @Override
          public boolean f(com.google.gwt.user.client.Event e) {
            mouseMove(element, Event.create(e));
            return false;
          }
        }).bind(Event.ONMOUSEUP, getPluginName(), (Object) null,
        new Function() {
          @Override
          public boolean f(com.google.gwt.user.client.Event e) {
            mouseUp(element, Event.create(e));
            return false;
          }
        });
  }

  private boolean delayConditionMet() {

    if (mouseUpDuration == null) {
      return false;
    }

    return options.getDelay() <= mouseUpDuration.elapsedMillis();
  }

  private boolean distanceConditionMet(Event event) {
    int neededDistance = options.getDistance();
    int mouseDownX = mouseDownEvent.getClientX();
    int mouseDownY = mouseDownEvent.getClientY();
    int xMouseDistance = Math.abs(mouseDownX - event.getClientX());
    int yMouseDistance = Math.abs(mouseDownY - event.getClientY());
    // in jQuery-ui we take the greater distance between x and y... not really
    // good !
    // int mouseDistance = Math.max(xMouseDistance, yMouseDistance);
    // use Pythagor theorem !!
    int mouseDistance = (int) Math.sqrt(xMouseDistance * xMouseDistance
        + yMouseDistance * yMouseDistance);
    return mouseDistance >= neededDistance;
  }

  private native boolean isEventAlreadyHandled(Event event)/*-{
    var result = event.mouseHandled ? event.mouseHandled : false;
    return result;
  }-*/;

  private native void markEventAsHandled(Event event)/*-{
    event.mouseHandled = true;
  }-*/;

  private boolean notHandleMouseDown(Element element, Event mouseDownEvent) {
    boolean isNotBoutonLeft = mouseDownEvent.getButton() != NativeEvent.BUTTON_LEFT;
    Element eventTarget = mouseDownEvent.getEventTarget().cast();

    boolean isElementCancel = false;
    if (options.getCancel() != null) {
      isElementCancel = $(eventTarget).parents().add($(eventTarget)).filter(
          options.getCancel()).length() > 0;
    }

    return isNotBoutonLeft || isElementCancel
        || !mouseCapture(element, mouseDownEvent);

  }

  private void reset(Event mouseDownEvent) {
    this.mouseDownEvent = mouseDownEvent;
    this.mouseUpDuration = new Duration();
  }

  private void unbindOtherMouseEvent() {
    $(document).as(Events.Events).unbind((Event.ONMOUSEUP | Event.ONMOUSEMOVE),
        getPluginName());
  }

}
