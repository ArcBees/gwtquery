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

import java.util.LinkedList;
import java.util.Queue;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.JSArray;

/**
 * Class used in plugins which need a queue system.
 */
public abstract class GQueryQueue extends GQuery {
  
  private static final String QUEUE_DATA_PREFIX = "GQueryQueue_";

  public GQueryQueue(Element element) {
    super(element);
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
  
  /**
   * Stop the function which is currently in execution, remove it
   * from the queue an start the next one.  
   */
  public GQueryQueue stop() {
    for (Element e : elements()) {
      stop(e);
    }
    return this;
  }

  protected String getQueueType() {
    return QUEUE_DATA_PREFIX + this.getClass().getName(); 
  }

  private void dequeue(Element elem) {
    Queue<?> q = queue(elem, null);
    if (q != null) {
      q.poll();
      Object f = q.peek();
      if (f != null) {
        if (f instanceof Function) {
          ((Function) f).f(elem);
        }
      }
    }
  }
  
  @SuppressWarnings("unchecked")
  private <S> Queue<S> queue(Element elem, S func) {
    if (elem != null) {
      Queue<S> q = (Queue<S>) data(elem, getQueueType(), null);
      if (q == null) {
        q = (Queue<S>) data(elem, getQueueType(), new LinkedList<S>());
      }
      if (func != null) {
        q.add(func);
      }
      if (q.size() == 1 && func != null) {
        if (func instanceof Function) {
          ((Function) func).f(elem);
        }
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
  
  private void stop(Element elem) {
    Queue<?> q = queue(elem, null);
    if (q != null) {
      Object f = q.peek();
      if (f != null) {
        if (f instanceof Function) {
          ((Function) f).cancel(elem);
        }
      }
      dequeue();
    }
  }
}
