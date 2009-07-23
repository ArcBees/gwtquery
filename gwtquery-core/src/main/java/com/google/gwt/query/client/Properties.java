/*
 * Copyright 2009 Google Inc.
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
package com.google.gwt.query.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;

/**
 * JSO for accessing Javascript objective literals used by GwtQuery functions.
 */
public class Properties extends JavaScriptObject {

  public static Properties create(String properties) {
    String s = properties.replaceFirst("^[({]*(.*)[})]*$", "({$1})");
    return (Properties) createImpl(s);
  }

  public final static native JavaScriptObject createImpl(String properties) /*-{
      return eval(properties);
    }-*/;

  protected Properties() {
  }

  public final Properties $$(String key, String value) {
    set(key, value);
    return this;
  }

  public final native Properties cloneProps() /*-{
    var props = {};
    for(p in this) {
      props[p] =  this[p];
    }
    return props;
  }-*/;

  public final native boolean defined(String name) /*-{
    return this[name] != undefined;  
  }-*/;

  public final native String get(String name) /*-{
    return this[name];
  }-*/;

  public final native float getFloat(String name) /*-{
    return this[name];
  }-*/;

  public final native int getInt(String name) /*-{
    return this[name];
  }-*/;

  public final String[] keys() {
    JsArrayString a = keysImpl();
    String[] ret = new String[a.length()];
    for (int i = 0; i < a.length(); i++) {
      ret[i] = "" + a.get(i);
    }
    return ret;
  }

  public final native JsArrayString keysImpl() /*-{
    var key, keys=[];
    for(key in this) {
      keys.push("" + key); 
    }
    return keys;
  }-*/;

  public final native void set(String key, String val) /*-{
    this[key]=val;
  }-*/;
}
