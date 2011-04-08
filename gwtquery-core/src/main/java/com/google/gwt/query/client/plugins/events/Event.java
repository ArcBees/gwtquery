package com.google.gwt.query.client.plugins.events;

import com.google.gwt.dom.client.Element;
import com.google.gwt.query.client.GQuery;

/**
 * This object allows you to have a full copy of the original Event and
 * implements some useful method of the jQuery event model.
 * 
 * This is also useful in Internet Explorer because it use the same javascript
 * object to fire MouseDownEvent, MouseMoveEvent or MouseStopEvent on the same
 * element. So, we cannot keep a copy of the MouseDownEvent during a dragging
 * for example.
 * 
 * 
 * 
 * TOBEFIXED : the method preventDefault() must be called directly on the
 * original event
 * 
 * 
 */
public class Event extends com.google.gwt.user.client.Event {

  
  /**
   * Create a new {@link Event} by copying the <code>originalEvent</code>.
   */
  public static Event create(com.google.gwt.user.client.Event originalEvent) {
    Event gQueryEvent = createObject().cast();
    copy(originalEvent, gQueryEvent);
    return gQueryEvent;
  }

  private static native void copy(
      com.google.gwt.user.client.Event originalEvent, Event gQueryEvent) /*-{
		for ( var field in originalEvent) {
			gQueryEvent[field] = originalEvent[field];
		}
		gQueryEvent.originalEvent = originalEvent;
  }-*/;

  protected Event() {
  }

  /**
   * Return the original event (the one created by the browser)
   */
  public final native com.google.gwt.user.client.Event getOriginalEvent()/*-{
		return this.originalEvent;
  }-*/;
  
  public final native void setCurrentElementTarget(Element e)/*-{
    
    this.currentTarget = e;
    
    //ie don't have a currentEventTarget field on event
    try{
      @com.google.gwt.dom.client.DOMImplTrident::currentEventTarget = e;
    }catch(e){}
  }-*/;

  /**
   * Tell whether ctrl or cmd key is pressed
   * 
   */
  public final boolean isMetaKeyPressed() {
    return getMetaKey() || getCtrlKey();
  }

  /**
   * The mouse position relative to the left edge of the document
   * 
   */
  public final int pageX() {
    return getClientX() + GQuery.document.getScrollLeft();
  }

  /**
   * The mouse position relative to the top edge of the document.
   * 
   */
  public final int pageY() {
    return getClientY() + GQuery.document.getScrollTop();
  }
 
  

}
