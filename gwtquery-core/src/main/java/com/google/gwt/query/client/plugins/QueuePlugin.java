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

import com.google.gwt.dom.client.Element;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.user.client.Timer;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Class used in plugins which need a queue system.
 */
public abstract class QueuePlugin<T extends QueuePlugin<?>> extends GQuery {

  protected class DelayFunction extends Function {

    private class SimpleTimer extends Timer {
      @Override
      public void run() {
        dequeue();
      }
    }

    private int delay;

    public DelayFunction(int delayInMilliseconds) {
      this.delay = delayInMilliseconds;
    }

    @Override
    public void f() {
      new SimpleTimer().schedule(delay);

    }
  }

  private static final String QUEUE_DATA_PREFIX = "GQueryQueue_";

  protected QueuePlugin(GQuery gq) {
    super(gq);
  }
  
  /**
   * 
   */
  @SuppressWarnings("unchecked")
  public T clearQueue() {
    for (Element e : elements()) {
      queue(e, null).clear();
    }
    return (T) this;
  }
  
  
  /**
   * Add a delay in the queue
   */
  @SuppressWarnings("unchecked")
  public T delay(int milliseconds) {
    queue(new DelayFunction(milliseconds));
    return (T) this;
  }

  /**
   * Removes a queued function from the front of the queue and executes it.
   */
  @SuppressWarnings("unchecked")
  public T dequeue() {
    for (Element e : elements()) {
      dequeueCurrentAndRunNext(e);
    }
    return (T) this;
  }

  /**
   * Adds a new function, to be executed, onto the end of the queue of all
   * matched elements.
   */
  @SuppressWarnings("unchecked")
  public T queue(Function func) {
    for (Element e : elements()) {
      queue(e, func);
    }
    return (T) this;
  }

  /**
   * Replaces the current queue with the given queue on all matched elements.
   */
  @SuppressWarnings("unchecked")
  public T queue(Queue<?> queue) {
    for (Element e : elements()) {
      replacequeue(e, queue);
    }
    return (T) this;
  }

  /**
   * Stop the function which is currently in execution, remove it from the queue
   * and start the next one.
   */
  public T stop() {
    return stop(false);
  }

  /**
   * Stop the function which is currently in execution and depending on the
   * value of the parameter: - remove it from the queue and start the next one.
   * - or remove all functions in the queue.
   */
  @SuppressWarnings("unchecked")
  public T stop(boolean clearQueue) {
    for (Element e : elements()) {
      stop(e, clearQueue);
    }
    return (T) this;
  }

  protected String getQueueType() {
    return QUEUE_DATA_PREFIX + this.getClass().getName();
  }

  private void dequeueCurrentAndRunNext(Element elem) {
    Queue<?> q = queue(elem, null);
    if (q != null) {
      // Remove current function
      q.poll();
      // Run the next in the queue
      Object f = q.peek();
      if (f != null) {
        if (f instanceof Function) {
          ((Function) f).f(elem.<com.google.gwt.dom.client.Element>cast());
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
          ((Function) func).f(elem.<com.google.gwt.dom.client.Element>cast());
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

  private void stop(Element elem, boolean clear) {
    Queue<?> q = queue(elem, null);
    if (q != null) {
      Object f = q.peek();
      if (f != null) {
        if (f instanceof Function) {
          ((Function) f).cancel(elem);
        }
      }
      if (clear) {
        q.clear();
      } else {
        dequeueCurrentAndRunNext(elem);
      }
    }
  }
}
