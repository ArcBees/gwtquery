package com.google.gwt.query.client;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Lightweight JSO backed implemented of a Map, using Object.hashCode() as key.
 */
final public class JsMap<S, T> extends JavaScriptObject {

  protected JsMap() {
  }

  public T get(S key) {
    return get(key.hashCode());
  }

  public native T get(int hashCode) /*-{
    return this[hashCode] || null;
  }-*/;

  public void put(S key, T val) {
    put(key.hashCode(), val);
  }

  public native void put(int hashCode, T val) /*-{
    this[hashCode]=val;
  }-*/;
}
