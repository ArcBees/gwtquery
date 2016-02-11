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

import com.google.gwt.core.client.Duration;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.plugins.events.GqEvent;
import com.google.gwt.user.client.Event;

/**
 * Base class for all plug-in that need to handle some mouse interactions.
 *
 */
public abstract class MousePlugin extends UiPlugin {

  private GqEvent startEvent;
  private boolean started = false;
  private Duration mouseUpDuration;
  private MouseOptions options;
  private boolean preventClickEvent = false;
  private boolean touchSupported = false;
  private int startX = -1;
  private int startY = -1;

  protected MousePlugin(GQuery gq) {
    super(gq);
  }

  protected void destroyMouseHandler() {
    as(Events)
        .unbind(Event.ONMOUSEDOWN | Event.ONCLICK | Event.ONTOUCHSTART, getPluginName(), null);
  }

  /**
   * Return a String identifying the plugin. This string is used as namespace when we bind handlers.
   *
   */
  protected abstract String getPluginName();

  /**
   * This method initialize all needed handlers.
   *
   */
  protected void initMouseHandler(MouseOptions options) {
    this.options = options;

    for (final Element e : elements()) {

      $(e).as(Events).bind(Event.ONMOUSEDOWN, getPluginName(), (Object) null, new Function() {
        @Override
        public boolean f(com.google.gwt.user.client.Event event) {
          if (touchSupported) {
            return true;
          }
          return mouseDown(e, GqEvent.create(event));
        }
      }).bind(Event.ONTOUCHSTART, getPluginName(), (Object) null, new Function() {
        public boolean f(com.google.gwt.user.client.Event event) {
          if (event.getTouches().length() > 1) {
            return true;
          }

          touchSupported = true;
          return mouseDown(e, GqEvent.create(event));
        }
      }).bind(Event.ONCLICK, getPluginName(), (Object) null, new Function() {
        @Override
        public boolean f(com.google.gwt.user.client.Event event) {
          preventClickEvent |= !mouseClick(e, GqEvent.create(event));

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
   */
  protected boolean mouseCapture(Element draggable, GqEvent event) {
    return true;
  }

  /**
   * Method called when mouse click.
   */
  protected boolean mouseClick(Element element, GqEvent event) {
    return true;
  }

  /**
   * Method called when mouse down occur on the element.
   *
   * You should not override this method. Instead, override {@link #mouseStart(Element, GqEvent)}
   * method.
   */
  protected boolean mouseDown(Element element, GqEvent event) {

    // test if an other plugin handle the mouseStart
    if (isEventAlreadyHandled(event)) {
      return false;
    }

    if (started) { // case where we missed a mouseup
      mouseUp(element, event);
    }

    // calculate all interesting variables
    reset(event);

    if (notHandleMouseDown(element, event)) {
      return true;
    }

    if (delayConditionMet() && distanceConditionMet(event)) {
      started = mouseStart(element, event);
      if (!started) {
        event.getOriginalEvent().preventDefault();
        return true;
      }
    }

    bindOtherEvents(element);

    if (!touchSupported) { // click event are not triggered if we call preventDefault on touchstart event.
      event.getOriginalEvent().preventDefault();
    }

    markEventAsHandled(event);

    return true;
  }

  /**
   * Method called when the mouse is dragging.
   */
  protected abstract boolean mouseDrag(Element element, GqEvent event);

  /**
   * Method called on MouseMove event.
   *
   * You should not override this method. Instead, override {@link #mouseMove(Element, GqEvent)}
   * method
   *
   */
  protected boolean mouseMove(Element element, GqEvent event) {

    if (started) {
      event.getOriginalEvent().preventDefault();
      return mouseDrag(element, event);
    }

    if (delayConditionMet() && distanceConditionMet(event)) {
      started = mouseStart(element, startEvent);
      if (started) {
        mouseDrag(element, event);
      } else {
        mouseUp(element, event);
      }
    }

    return !started;
  }

  /**
   * Method called when the mouse is clicked and all conditions for starting the plugin are met.
   *
   */
  protected abstract boolean mouseStart(Element element, GqEvent event);

  /**
   * Method called when the mouse button is released.
   */
  protected abstract boolean mouseStop(Element element, GqEvent event);

  /**
   * Method called when mouse is released..
   *
   * You should not override this method. Instead, override {@link #mouseStop(Element, GqEvent)}
   * method.
   */
  protected boolean mouseUp(Element element, GqEvent event) {

    unbindOtherEvents();
    if (started) {
      started = false;
      preventClickEvent = event.getCurrentEventTarget() == startEvent.getCurrentEventTarget();
      mouseStop(element, event);
    }

    return true;
  }

  private void bindOtherEvents(final Element element) {

    int moveEvent = touchSupported ? Event.ONTOUCHMOVE : Event.ONMOUSEMOVE;

    int endEvents = touchSupported ? Event.ONTOUCHEND : Event.ONMOUSEUP;

    $(document).as(Events).bind(moveEvent, getPluginName(), (Object) null, new Function() {
      @Override
      public boolean f(com.google.gwt.user.client.Event e) {
        mouseMove(element, (GqEvent) GqEvent.create(e));
        return false;
      }
    }).bind(endEvents, getPluginName(), (Object) null, new Function() {
      @Override
      public boolean f(com.google.gwt.user.client.Event e) {
        mouseUp(element, (GqEvent) GqEvent.create(e));
        return false;
      }
    });

    // TODO Event.ONTOUCHEND | Event.ONTOUCHCANCEL don't work -> investigate
    if (touchSupported) {
      $(document).as(Events).bind(Event.ONTOUCHCANCEL, getPluginName(), (Object) null,
          new Function() {
            @Override
            public boolean f(com.google.gwt.user.client.Event e) {
              mouseUp(element, (GqEvent) GqEvent.create(e));
              return false;
            }
          });
    }
  }

  private boolean delayConditionMet() {

    if (mouseUpDuration == null) {
      return false;
    }

    return options.getDelay() <= mouseUpDuration.elapsedMillis();
  }

  private boolean distanceConditionMet(GqEvent event) {
    int neededDistance = options.getDistance();
    int xDistance = Math.abs(startX - getClientX(event));
    int yDistance = Math.abs(startY - getClientY(event));
    // in jQuery-ui we take the greater distance between x and y... not really
    // good !
    // int mouseDistance = Math.max(xMouseDistance, yMouseDistance);
    // use Pythagor theorem !!

    int mouseDistance = (int) Math.sqrt(xDistance * xDistance + yDistance * yDistance);
    return mouseDistance >= neededDistance;
  }

  private native boolean isEventAlreadyHandled(GqEvent event) /*-{
    var result = event.mouseHandled ? event.mouseHandled : false;
    return result;
  }-*/;

  private native void markEventAsHandled(GqEvent event) /*-{
    event.mouseHandled = true;
  }-*/;

  private boolean notHandleMouseDown(Element element, GqEvent mouseDownEvent) {
    boolean isNotBoutonLeft = mouseDownEvent.getButton() != NativeEvent.BUTTON_LEFT;
    Element eventTarget = mouseDownEvent.getEventTarget().cast();

    boolean isElementCancel = false;
    if (options.getCancel() != null) {
      isElementCancel =
          $(eventTarget).parents().add($(eventTarget)).filter(options.getCancel()).length() > 0;
    }

    return isNotBoutonLeft || isElementCancel || !mouseCapture(element, mouseDownEvent);
  }

  private void reset(GqEvent nativeEvent) {
    this.startEvent = nativeEvent;
    this.startX = getClientX(nativeEvent);
    this.startY = getClientY(nativeEvent);
    this.mouseUpDuration = new Duration();
  }

  private void unbindOtherEvents() {
    int events =
        touchSupported ? Event.ONTOUCHCANCEL | Event.ONTOUCHEND | Event.ONTOUCHMOVE
            : Event.ONMOUSEUP | Event.ONMOUSEMOVE;

    $(document).as(Events).unbind(events, getPluginName(), null);
  }

  protected int getClientX(Event e) {
    if (touchSupported) {
      return e.getTouches().get(0).getClientX();
    } else {
      return e.getClientX();
    }
  }

  protected int getClientY(Event e) {
    if (touchSupported) {
      return e.getTouches().get(0).getClientY();
    } else {
      return e.getClientY();
    }
  }
}
