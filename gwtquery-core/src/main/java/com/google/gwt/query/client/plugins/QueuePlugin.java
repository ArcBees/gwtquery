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
import com.google.gwt.query.client.Promise;
import com.google.gwt.query.client.plugins.deferred.Callbacks;
import com.google.gwt.user.client.Timer;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Class used in plugins which need a queue system.
 *
 * @param <T>
 */
public class QueuePlugin<T extends QueuePlugin<?>> extends GQuery {

  @SuppressWarnings("rawtypes")
  public static final Class<QueuePlugin> Queue = GQuery.registerPlugin(
      QueuePlugin.class, new Plugin<QueuePlugin>() {
        public QueuePlugin init(GQuery gq) {
          return new QueuePlugin(gq);
        }
      });

  /**
   * Function used to delay the execution of a set of functions in
   * a queue.
   */
  protected class DelayFunction extends Function {
    private class SimpleTimer extends Timer {
      public void run() {
        for (Function f : funcs) {
          f.fe(elem);
        }
        dequeueIfNotDoneYet(elem, name, DelayFunction.this);
      }
    }

    private int delay;
    Function[] funcs;
    Element elem;
    String name;

    public DelayFunction(Element elem, String name, int delay, Function... f) {
      this.elem = elem;
      this.delay = delay;
      this.funcs = f;
      this.name = name;
    }

    @Override
    public void f() {
      new SimpleTimer().schedule(delay);
    }
  }

  public static final String JUMP_TO_END = QueuePlugin.class.getName() + ".StopData";
  protected static final String QUEUE_DATA_PREFIX = QueuePlugin.class.getName() + ".Queue-";
  protected static String DEFAULT_NAME = QUEUE_DATA_PREFIX + "fx";
  private static final String EMPTY_HOOKS = ".Empty";

  protected QueuePlugin(GQuery gq) {
    super(gq);
  }

  /**
   * remove all queued functions from the effects queue.
   */
  public T clearQueue() {
    return clearQueue(DEFAULT_NAME);
  }

  /**
   * remove all queued function from the named queue.
   */
  @SuppressWarnings("unchecked")
  public T clearQueue(String name) {
    for (Element e : elements()) {
      queue(e, name, null).clear();
    }
    return (T) this;
  }

  /**
   * Add a delay in the effects queue.
   */
  public T delay(int milliseconds, Function... f) {
    return delay(milliseconds, DEFAULT_NAME, f);
  }

  /**
   * Add a delay in the named queue.
   */
  @SuppressWarnings("unchecked")
  public T delay(int milliseconds, String name, Function... funcs) {
    for (Element e : elements()) {
      queue(e, name, new DelayFunction(e, name, milliseconds, funcs));
    }
    return (T) this;
  }

  /**
   * Removes a queued function from the front of the effects queue and executes it.
   */
  public T dequeue() {
    return dequeue(DEFAULT_NAME);
  }

  /**
   * Removes a queued function from the front of the named queue and executes it.
   */
  @SuppressWarnings("unchecked")
  public T dequeue(String name) {
    for (Element e : elements()) {
      dequeueCurrentAndRunNext(e, name);
    }
    return (T) this;
  }

  /**
   * Returns a dynamically generated Promise that is resolved once all actions
   * in the queue have ended.
   */
  public Promise promise() {
    return promise(DEFAULT_NAME);
  }

  /**
   * Returns a dynamically generated Promise that is resolved once all actions
   * in the named queue have ended.
   */
  public Promise promise(final String name) {
    final Promise.Deferred dfd = Deferred();

    // This is the unique instance of the resolve function which will be added to each element.
    final Function resolve = new Function() {
      // Because it is an inner function, the counter cannot final outside the function
      int count = 1;
      // Inner functions don't have constructors, we use a block to initialize it
      {
        for (Element elem : elements()) {
          // Add this resolve function only to those elements with active queue
          if (queue(elem, name, null) != null) {
            emptyHooks(elem, name).add(this);
            count++;
          }
        }
      }

      public void f() {
        if (--count == 0) {
          dfd.resolve(QueuePlugin.this);
        }
      }
    };

    // Run the function and resolve it in case there are not elements with active queue
    resolve.f(this, name);

    return dfd.promise();
  }

  /**
   * Show the number of functions to be executed on the first matched element
   * in the effects queue.
   */
  public int queue() {
    return queue(DEFAULT_NAME);
  }

  /**
   * Show the number of functions to be executed on the first matched element
   * in the named queue.
   */
  public int queue(String name) {
    Queue<?> q = isEmpty() ? null : queue(get(0), name, null);
    return q == null ? 0 : q.size();
  }

  /**
   * Adds new functions, to be executed, onto the end of the effects
   * queue of all matched elements.
   */
  @SuppressWarnings("unchecked")
  public T queue(Function... funcs) {
    for (Element e : elements()) {
      for (Function f : funcs) {
        queue(e, DEFAULT_NAME, f);
      }
    }
    return (T) this;
  }

  /**
   * Adds new functions, to be executed, onto the end of the named
   * queue of all matched elements.
   */
  @SuppressWarnings("unchecked")
  public T queue(final String name, Function... funcs) {
    for (final Function f : funcs) {
      for (Element e : elements()) {
        queue(e, name, f);
      }
    }
    return (T) this;
  }

  /**
   * Replaces the current effects queue with the given queue on all matched elements.
   */
  public T queue(Queue<?> queue) {
    return queue(DEFAULT_NAME, queue);
  }

  /**
   * Replaces the current named queue with the given queue on all matched elements.
   */
  @SuppressWarnings("unchecked")
  public T queue(String name, Queue<?> queue) {
    for (Element e : elements()) {
      replacequeue(e, name, queue);
    }
    return (T) this;
  }

  /**
   * Stop the function which is currently in execution, remove it from the
   * effects queue and start the next one.
   */
  public T stop() {
    return stop(false);
  }

  /**
   * Stop the function which is currently in execution, remove it from the
   * named queue and start the next one.
   */
  public T stop(String name) {
    return stop(name, false);
  }

  /**
   * Stop the function which is currently in execution and depending on the
   * value of the parameter:
   * - remove it from the effects queue and start the next one.
   * - or remove all functions in the effects queue.
   */
  public T stop(boolean clearQueue) {
    return stop(DEFAULT_NAME, clearQueue, false);
  }

  /**
   * Stop the function which is currently in execution and depending on the
   * value of the parameter:
   * - remove it from the effects queue and start the next one.
   * - or remove all functions in the effects queue.
   *
   * If the parameter jump is true, the current stopped effect will set
   * the final css properties like if the effect would be completely executed.
   */
  public T stop(boolean clearQueue, boolean jumpToEnd) {
    return stop(DEFAULT_NAME, clearQueue, jumpToEnd);
  }

  /**
   * Stop the function which is currently in execution and depending on the
   * value of the parameter:
   * - remove it from the named queue and start the next one.
   * - or remove all functions in the named queue.
   */
  public T stop(String name, boolean clearQueue) {
    return stop(name, clearQueue, false);
  }

  /**
   * Stop the function which is currently in execution and depending on the
   * value of the clear parameter:
   * - remove it from the named queue and start the next one.
   * - or remove all functions in the queue.
   *
   * If the parameter jump is true, the current stopped effect will set
   * the final css properties like if the effect would be completely executed.
   *
   */
  @SuppressWarnings("unchecked")
  public T stop(String name, boolean clearQueue, boolean jumpToEnd) {
    for (Element e : elements()) {
      stop(e, name, clearQueue, jumpToEnd);
    }
    return (T) this;
  }

  private void dequeueCurrentAndRunNext(Element elem, String name) {
    Queue<? extends Function> q = queue(elem, name, null);
    if (q != null) {
      // Remove current function
      q.poll();
      // Run the next in the queue
      runNext(elem, name, q);
    }
  }

  private void runNext(Element elem, String name, Queue<? extends Function> q) {
    assert q != null;
    Function f = q.peek();
    if (f != null) {
      f.fe(elem);
    } else {
      // Run final hooks when emptying the queue, used in promises
      emptyHooks(elem, name).fire();
      // It is the last function, remove the queue to avoid leaks (issue 132)
      removeData(elem, name);
      removeData(elem, name + EMPTY_HOOKS);
    }
  }

  @SuppressWarnings("unchecked")
  protected <S extends Function> Queue<S> queue(Element elem, String name, S func) {
    if (elem != null) {
      Queue<S> q = (Queue<S>) data(elem, name, null);
      if (func != null) {
        if (q == null) {
          q = (Queue<S>) data(elem, name, new LinkedList<S>());
        }
        q.add(func);
        if (q.size() == 1) {
          runNext(elem, name, q);
        }
      }
      return q;
    }
    return null;
  }

  /**
   * Dequeue the object and run the next if it is the first
   * in the queue.
   */
  public void dequeueIfNotDoneYet(Element elem, String name, Object object) {
    @SuppressWarnings("rawtypes")
    Queue queue = queue(elem, name, null);
    if (queue != null && object.equals(queue.peek())) {
      dequeueCurrentAndRunNext(elem, name);
    }
  }

  protected void replacequeue(Element elem, String name, Queue<?> queue) {
    if (elem != null) {
      data(elem, name, queue);
    }
  }

  private Callbacks emptyHooks(Element elem, String name) {
    String key = name + EMPTY_HOOKS;
    Callbacks c = (Callbacks) data(elem, key, null);
    if (c == null) {
      c = (Callbacks) data(elem, key, new Callbacks("once memory"));
    }
    return c;
  }

  private void stop(Element elem, String name, boolean clear, boolean jumpToEnd) {
    @SuppressWarnings("rawtypes")
    Queue q = queue(elem, name, null);
    if (q != null) {
      Object f = q.peek();
      if (clear) {
        q.clear();
      }
      if (f != null) {
        if (f instanceof Function) {
          // pass jumpToEnd to Animation.onCancel() via the element's data object
          $(elem).data(JUMP_TO_END, jumpToEnd);
          ((Function) f).cancel(elem);
          $(elem).removeData(JUMP_TO_END);
        }
        dequeueIfNotDoneYet(elem, name, f);
      }
    }
  }
}
