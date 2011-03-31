package com.google.gwt.query.client.plugins;

import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;

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
  public SimpleNamedQueue delay(int milliseconds, String queueName) {
    this.queueName = queueName;
    return delay(milliseconds);
  }
  
  @Override
  public SimpleNamedQueue queue(String queueName, Function func) {
    this.queueName = queueName;
    return queue(func);
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
