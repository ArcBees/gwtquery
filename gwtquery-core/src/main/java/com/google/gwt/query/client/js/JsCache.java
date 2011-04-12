package com.google.gwt.query.client.js;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;

 /**
   * A Lightweight JSO class to store data.
   */
public class JsCache extends JavaScriptObject {

  protected JsCache() {
  }
  
  public static JsCache create() {
    return createObject().cast();
  }

  public final native void concat(Object ary) /*-{
    if (ary) this.concat(ary);
  }-*/;
  
  public final void pushAll(JavaScriptObject prevElem) {
    JsCache c = prevElem.cast();
    for (int i = 0, ilen = c.length(); i < ilen; i++) {
      put(length(), c.get(i));
    }
  }
  
  public final native void delete(int name) /*-{
    delete this[name];
  }-*/;

  public final native void delete(String name) /*-{
    delete this[name];
  }-*/;

  public final native boolean exists(String name) /*-{
    return !!this[name];
  }-*/;
  
  public final native boolean exists(int id) /*-{
    return !!this[id];
  }-*/;

  public final native <T> T get(int id) /*-{
    return this[id] || null;
  }-*/;

  public final native <T> T get(String id) /*-{
    return this[id] || null;
  }-*/;

  public final JsCache getCache(int id) {
    return (JsCache)get(id);
  }

  public final native double getDouble(int id) /*-{
    return this[id] || 0;
  }-*/;

  public final native double getDouble(String id) /*-{
    return this[id] || 0;
  }-*/;

  public final native int getInt(int id) /*-{
    return this[id] || 0;
  }-*/;

  public final native int getInt(String id) /*-{
    return this[id] || 0;
  }-*/;

  public final native String getString(int id) /*-{
    return this[id];
  }-*/;

  public final native String getString(String id) /*-{
    return this[id] || null;
  }-*/;

  public final native boolean isEmpty() /*-{
    var foo = "";
    for (foo in this) break;
    return !foo;
  }-*/;

  public final native void put(int id, Object obj) /*-{
    this[id] = obj;
  }-*/;

  public final native void put(String id, Object obj) /*-{
    this[id] = obj;
  }-*/;
  
  public final native int length() /*-{
    if (this.length) return this.length;
    var key, ret = 0; 
    for (key in this) ret ++;
    return ret; 
  }-*/;
  
  public final int[] indexes() {
    JsArrayString a = keysImpl();
    int[] ret = new int[a.length()];
    for (int i = 0; i < a.length(); i++) {
      try {
        ret[i] = Integer.valueOf(a.get(i));
      } catch (Exception e) {
        ret[i] = i;
      }
    }
    return ret;
  }
  
  public final String[] keys() {
    JsArrayString a = keysImpl();
    String[] ret = new String[a.length()];
    for (int i = 0; i < a.length(); i++) {
      ret[i] = a.get(i);
    }
    return ret;
  }
  
  public final Object[] elements() {
    String[] keys = keys();
    Object[] ret = new Object[keys.length];
    int i=0;
    for (String s: keys) {
      ret[i++] = get(s);
    }
    return ret;
  }
  
  public final String tostring() {
    String ret = getClass().getName() + "{ ";
    for (String k: keys()){
      ret += k + "=" + get(k) + " ";
    }
    return ret + "}";
  }
  
  private final native JsArrayString keysImpl() /*-{
    var key, keys=[];
    for(key in this) keys.push(String(key));
    return keys;
  }-*/;
}
