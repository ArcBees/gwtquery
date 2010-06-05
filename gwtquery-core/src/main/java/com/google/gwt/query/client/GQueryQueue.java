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

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;

/**
 * Class used in plugins which need a queue system.
 */
public abstract class GQueryQueue extends GQuery {

  private static final class Queue<T> extends JavaScriptObject {

    public static Queue<?> newInstance() {
      return createArray().cast();
    }

    @SuppressWarnings("unused")
    protected Queue() {
    }

    public native T dequeue() /*-{
      return this.shift();
    }-*/;

    public native void enqueue(T foo) /*-{
      this.push(foo);
    }-*/;

    public native int length() /*-{
      return this.length;
    }-*/;

    public native T peek(int i) /*-{
      return this[i];
    }-*/;
  }

  public GQueryQueue(GQuery gq) {
    super(gq);
  }

  public GQueryQueue(JSArray elements) {
    super(elements);
  }

  public GQueryQueue(NodeList<Element> list) {
    super(list);
  }

  public GQueryQueue(Element element) {
    super(element);
  }

  /**
   * Removes a queued function from the front of the queue and executes it.
   */
  public GQueryQueue dequeue() {
    for (Element e : elements()) {
      dequeue(e);
    }
    return this;
  }


  /**
   * Adds a new function, to be executed, onto the end of the queue of all
   * matched elements.
   */
  public GQueryQueue queue(Function func) {
    for (Element e : elements()) {
      queue(e, func);
    }
    return this;
  }

  /**
   * Replaces the current queue with the given queue on all matched elements.
   */
  public GQueryQueue queue(Queue<?> queue) {
    for (Element e : elements()) {
      replacequeue(e, queue);
    }
    return this;
  }

  private void dequeue(Element elem) {
    Queue<Function> q = queue(elem, null);
    if (q != null) {
      Function f = q.dequeue();
      f = q.peek(0);
      if (f != null) {
        f.f(elem);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private Queue<Function> queue(Element elem, Function func) {
    if (elem != null) {
      Queue<Function> q = (Queue<Function>) data(elem, getQueueType(), null);
      if (q == null) {
        q = (Queue<Function>) data(elem, getQueueType(), Queue.newInstance());
      }
      if (func != null) {
        q.enqueue(func);
      }
      if (q.length() == 1 && func != null) {
        func.f(elem);
      }
      return q;
    }
    return null;
  }

  private void replacequeue(Element elem, Queue<?> queue) {
    if (elem != null) {
      data(elem, getQueueType(), queue);
    }
  }
  
  protected String getQueueType() {
    return "GQueryQueue_" + this.getClass().getName(); 
  }
}
