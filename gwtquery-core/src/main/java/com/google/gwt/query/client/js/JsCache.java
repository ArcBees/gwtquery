package com.google.gwt.query.client.js;

import com.google.gwt.core.client.JavaScriptObject;

 /**
   * A Lightweight JSO class to store data.
   */
public class JsCache extends JavaScriptObject {

  protected JsCache() {
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

  public final native boolean exists(int id) /*-{
		return !!this[id];
  }-*/;

  public final native Object get(int id) /*-{
		return this[id] || null;
  }-*/;

  public final native Object get(String id) /*-{
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
		for (foo in this)
			break;
		return !foo;
  }-*/;

  public final native void put(int id, Object obj) /*-{
		if (obj && id >=0 ) this[id] = obj;
  }-*/;

  public final native void put(String id, Object obj) /*-{
		if (obj && id) this[id] = obj;
  }-*/;
  
  public final native int length() /*-{
    return this.length;
  }-*/;
}
