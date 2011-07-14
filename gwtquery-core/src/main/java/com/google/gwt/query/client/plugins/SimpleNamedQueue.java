package com.google.gwt.query.client.plugins;

import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.user.client.Element;

public class SimpleNamedQueue extends QueuePlugin<SimpleNamedQueue>{

  public static final Class<SimpleNamedQueue> SimpleNamedQueue = SimpleNamedQueue.class;
  protected SimpleNamedQueue(GQuery gq) {
    super(gq);
  }
  
  static {
    GQuery.registerPlugin(SimpleNamedQueue.class, new Plugin<SimpleNamedQueue>() {
      public SimpleNamedQueue init(GQuery gq) {
        return new SimpleNamedQueue(gq);
      }
    });
  }
  
  private String queueName;
  
  @Override
  public SimpleNamedQueue delay(int milliseconds, String queueName, Function... f) {
    this.queueName = queueName;
    return delay(milliseconds, f);
  }
  
  @Override
  public SimpleNamedQueue queue(final String queueName, final Function func) {
    this.queueName = queueName;
    return queue(new Function(){
      @Override
      public void f(Element e) {
        func.f(e.<com.google.gwt.dom.client.Element>cast());
        dequeueIfNotDoneYet(e, this);
      }
    });
  }
  
  @Override
  public GQuery dequeue(String queueName) {
    this.queueName = queueName;
    return dequeue();
  }
  
  @Override
  public GQuery clearQueue(String queueName) {
    this.queueName = queueName;
    return clearQueue();
  }
  
  @Override
  protected String getQueueType() {
    return super.getQueueType() + (queueName != null ? queueName : "");
  }

}
