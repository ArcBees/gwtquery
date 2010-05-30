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

  public GQueryQueue(Element element) {
    super(element);
  }

  public GQueryQueue(JSArray elements) {
    super(elements);
  }

  public GQueryQueue(NodeList<Element> list) {
    super(list);
  }

  /**
   * Removes a queued function from the front of the FX queue and executes it.
   */
  public GQuery dequeue() {
    return dequeue("__FX");
  }

  /**
   * Removes a queued function from the front of the queue and executes it.
   */
  public GQueryQueue dequeue(String type) {
    for (Element e : elements()) {
      dequeue(e, type);
    }
    return this;
  }

  /**
   * Returns a reference to the FX queue.
   */
  public Queue<Function> queue() {
    return queue(elements.getItem(0), "__FX", null);
  }

  /**
   * Adds a new function, to be executed, onto the end of the queue of all
   * matched elements in the FX queue.
   */
  public GQueryQueue queue(Function data) {
    return queue("__FX", data);
  }

  /**
   * Returns a reference to the first element's queue (which is an array of
   * functions).
   */
  public Queue<Function> queue(String type) {
    return queue(elements.getItem(0), type, null);
  }

  /**
   * Adds a new function, to be executed, onto the end of the queue of all
   * matched elements.
   */
  public GQueryQueue queue(String type, Function data) {
    for (Element e : elements()) {
      queue(e, type, data);
    }
    return this;
  }

  /**
   * Replaces the current queue with the given queue on all matched elements.
   */
  public GQueryQueue queue(String type, Queue<?> data) {
    for (Element e : elements()) {
      replacequeue(e, type, data);
    }
    return this;
  }

  private void dequeue(Element elem, String type) {
    Queue<Function> q = queue(elem, type, null);

    if (q != null) {
      Function f = q.dequeue();

      if (SelectorEngine.eq(type, "__FX")) {
        f = q.peek(0);
      }
      if (f != null) {
        f.f(elem);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private Queue<Function> queue(Element elem, String type, Function data) {
    if (elem != null) {
      type = type + "queue";
      Queue<Function> qq = (Queue<Function>) data(elem, type, null);
      if (qq == null) {
        qq = (Queue<Function>) data(elem, type, Queue.newInstance());
      }
      if (data != null) {
        qq.enqueue(data);
      }
      if (SelectorEngine.eq(type, "__FXqueue") && qq.length() == 1) {
        if (data != null) {
          data.f(elem);
        }
      }
      return qq;
    }
    return null;
  }

  private void replacequeue(Element elem, String type, Queue<?> data) {
    if (elem != null) {
      type = type + "queue";
      data(elem, type, data);
    }
  }
}
