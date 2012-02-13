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
package com.google.gwt.query.client.js;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayMixed;
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
    checkNull();
    JsCache c = prevElem.cast();
    for (int i = 0, ilen = c.length(); i < ilen; i++) {
      put(length(), c.get(i));
    }
  }
  
  public final native <T> void delete(T name) /*-{
    delete this[name];
  }-*/;

  public final native <T> void clear() /*-{
    if (this.length) {
      for (i = this.length - 1; i >= 0; i--) 
        delete this[i];
      this.length = 0;  
    }
  }-*/;

  public final native <T> boolean exists(T name) /*-{
    return !!this[name];
  }-*/;
  
  public final native <R, T> R get(T id) /*-{
    var r = this[id], t = typeof r;
    return r && t != 'number' && t != 'boolean' ? r : null;
  }-*/;

  public final <T> JsCache getCache(int id) {
    return (JsCache)get(id);
  }
  
  public final native <T> boolean getBoolean(T id) /*-{
    var r = this[id], t = typeof r;
    return 'boolean' == r ? r : 'true' == String(r);
  }-*/;
  
  public final <T> float getFloat(T id) {
    return (float)getDouble(id);
  }

  public final native <T> double getDouble(T id) /*-{
    // HtmlUnit prints an 'Unknown property name in get valueOf' 
    // error here, but it is ok.
    var r = this[id] ? Number(this[id]) : 0;
    return r ? r : 0;
  }-*/;

  public final <T> int getInt(T id) {
    return (int)getDouble(id);
  }

  public final native <T> String getString(T id) /*-{
    return this[id] == null ? null : String(this[id]);
  }-*/;
  
  public final native <T> JsArrayMixed getArray(T id) /*-{
    var r = this[id];
    if (r && Object.prototype.toString.call(r) == '[object Array]') {
      return r;
    }
    return null;
  }-*/;
  
  public final <T extends JavaScriptObject> T getJavaScriptObject(Object name) {
    Object o = get(name); 
    return (o != null && o instanceof JavaScriptObject) ? ((JavaScriptObject)o).<T>cast() : null;
  }

  public final native boolean isEmpty() /*-{
    for (k in this) return false;
    return true;
  }-*/;
  
  public final native int indexOf(Object o) /*-{
    return this.indexOf(o);
  }-*/;

  public final native <T, O> void put(T id, O obj) /*-{
    this[id] = obj;
  }-*/;

  public final native int length() /*-{
    if (typeof(this.length) == 'number') 
     return this.length;
     
    var key, ret = 0;
    // Chrome in DevMode injects a property to JS objects
    for (key in this) if (key != "__gwt_ObjectId") ret ++;
    return ret; 
  }-*/;
  
  public final int[] indexes() {
    checkNull();
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
    checkNull();
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
  
  private void checkNull() {
    // In dev-mode a null object casted to JavascriptObject does not throw a NPE
    if (!GWT.isProdMode() && this == null) {
      throw new NullPointerException();
    }
  }
  
  private final native JsArrayString keysImpl() /*-{
    var key, keys=[];
    // Chrome in DevMode injects a property to JS objects
    for(key in this) if (key != '__gwt_ObjectId') keys.push(String(key));
    return keys;
  }-*/;
}
