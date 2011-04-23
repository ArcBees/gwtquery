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
package com.google.gwt.query.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.query.client.js.JsCache;

/**
 * JSO for accessing Javascript objective literals used by GwtQuery functions.
 */
public class Properties extends JavaScriptObject {
  
  public static Properties create(String properties) {
    String p = wrapPropertiesString(properties);
    try {
      return (Properties) createImpl(p);
    } catch (Exception e) {
      System.err.println("Error creating Properties: \n" + properties  + "\n" + p + "\n" + e.getMessage());
      return (Properties) createImpl("({})");
    }
  }
  
  public static Properties create() {
    return (Properties) createImpl("({})");
  }

  public static final native JavaScriptObject createImpl(String properties) /*-{
     return eval(properties);
  }-*/;

  public static String wrapPropertiesString(String s) {
    String ret = "({" + s //
        .replaceAll("\\s*/\\*[\\s\\S]*?\\*/\\s*", "") //
        .replaceAll("([:\\)\\(,;}{'\"])\\s+" , "$1") //
        .replaceAll("\\s+([:\\)\\(,;}{'\"])" , "$1") //
        .replaceFirst("^[{\\(]+(.+[^}\\)])[}\\)]+$", "$1") //
        .replaceAll("\\('([^\\)]+)'\\)" , "($1)") //
        .replaceAll(",([\\w-]+:+)" , ";$1") //
        .replaceAll(":\\s*[\"']?([^;]+)([;]|$)[\"']?\\s*", ":'$1',") //
        .replaceFirst("[;,]$", "") //
        .replaceAll("\\s*[']+\\s*", "'") //
        + "})";
    return ret;
  }

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

  public final native <T> T get(String name) /*-{
    return this[name];
  }-*/;

  public final native String getStr(String name) /*-{
    return String(this[name]);
  }-*/;

  public final native float getFloat(String name) /*-{
    return this[name];
  }-*/;

  public final native int getInt(String name) /*-{
    return this[name];
  }-*/;

  public final String[] keys() {
    return this.<JsCache>cast().keys();
  }

  public final native void set(String key, Object val) /*-{
    this[key]=val;
  }-*/;
  
  public final String tostring() {
    String ret = "";
    for (String k : keys()){
      ret += k + ": '" + getStr(k) + "', ";
    }
    return "({" + ret.replaceAll("[, ]+$","") + "})";
  }
}
