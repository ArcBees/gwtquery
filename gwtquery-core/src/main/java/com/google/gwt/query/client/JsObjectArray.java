package com.google.gwt.query.client;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Lightweight JSO based array class that can store objects rather than just
 * other JSOs.
 */
public final class JsObjectArray<T> extends JavaScriptObject {

  protected JsObjectArray() {
  }

  public void add(T val) {
    set(length(), val);
  }

  public native T get(int i) /*-{
    return this[i];
  }-*/;

  public native int length() /*-{
    return this.length;
  }-*/;

  public native void set(int i, T val) /*-{
    this[i]=val;
  }-*/;
}
