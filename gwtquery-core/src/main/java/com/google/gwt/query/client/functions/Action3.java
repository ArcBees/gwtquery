package com.google.gwt.query.client.functions;

import com.google.gwt.query.client.functions.Action;

public interface Action3<T1, T2, T3> extends Action {
  public void call(T1 t1, T2 t2, T3 t3);
}
