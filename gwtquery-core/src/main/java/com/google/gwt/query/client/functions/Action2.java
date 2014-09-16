package com.google.gwt.query.client.functions;

import com.google.gwt.query.client.functions.Action;

public interface Action2<T1, T2> extends Action {
  public void call(T1 t1, T2 t2);
}
