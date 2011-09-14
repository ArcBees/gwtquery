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
import com.google.gwt.core.client.JsArrayMixed;
import com.google.gwt.query.client.js.JsCache;

/**
 * JSO for accessing Javascript objective literals used by GwtQuery functions.
 */
public class Properties extends JavaScriptObject {
  
  public static Properties create() {
    return (Properties) createImpl("({})");
  }
  
  public static Properties create(String properties) {
    if (properties != null && !properties.isEmpty()) {
      String p = camelizePropertiesKeys(wrapPropertiesString(properties));
      System.out.println(p);
      try {
        return (Properties) createImpl(p);
      } catch (Exception e) {
        System.err.println("Error creating Properties: \n> " + properties  + "\n< " + p + "\n" + e.getMessage());
      }
    }
    return create();
  }

  public static final native JavaScriptObject createImpl(String properties) /*-{
    return eval(properties);
  }-*/;

  public static String wrapPropertiesString(String s) {
    String ret = "({" + s //
        .replaceAll("\\s*/\\*[\\s\\S]*?\\*/\\s*", "") // Remove comments
        .replaceAll("([:\\)\\(,;}{'\"])\\s+" , "$1") // Remove spaces
        .replaceAll("\\s+([:\\)\\(,;}{'\"])" , "$1") // Remove spaces
        .replaceFirst("^[{\\(]+(|.*[^}\\)])[}\\)]+$", "$1") // Remove ({})
        .replaceAll("\\('([^\\)]+)'\\)" , "($1)") // Remove quotes
        .replaceAll(",+([\\w-]+:+)" , ";$1") // put semicolon
        .replaceAll(":\\s*[\"']?([^'\\]};]*)[\"']?\\s*(;+|$)", ":'$1',") // put quotes
        .replaceAll(":'(-?[\\d\\.]+|null|false|true)',", ":$1,") // numbers do not need quote
        .replaceFirst("[;,]$", "") // remove endings 
        + "})";
    return ret;
  }
  
  public static native String camelizePropertiesKeys(String s)/*-{
    return s.replace(/(\w+)\-(\w)(\w*:)/g, function(all, g1, letter, g2) {
      return g1 + letter.toUpperCase() + g2;
    });
  }-*/;

  protected Properties() {
  }

  public final Properties $$(String key, String value) {
    set(key, value);
    return this;
  }

  private JsCache c() {
    return this.<JsCache>cast();
  }

  public final native Properties cloneProps() /*-{
    var props = {};
    for(p in this) {
      props[p] =  this[p];
    }
    return props;
  }-*/;

  public final boolean defined(Object name) {
    return c().exists(String.valueOf(name));
  }

  public final <T> T get(Object name) {
    return c().get(String.valueOf(name));
  }

  public final boolean getBoolean(Object name) {
    return c().getBoolean(String.valueOf(name));
  }

  public final float getFloat(Object name) {
    return c().getFloat(String.valueOf(name));
  }

  public final int getInt(Object name) {
    return c().getInt(String.valueOf(name));
  }
  
  public final String getStr(Object name) {
    return c().getString(String.valueOf(name));
  }
  
  public final Object getObject(Object name) {
    return c().get(String.valueOf(name));
  }
  
  public final <T extends JavaScriptObject> T getJavaScriptObject(Object name) {
    return c().getJavaScriptObject(String.valueOf(name));
  }

  public final JsArrayMixed getArray(Object name) {
    return c().getArray(String.valueOf(name));
  }
  
  public final String[] keys() {
    return c().keys();
  }
  
  public final <T> void remove(T name) {
    c().delete(String.valueOf(name));
  }
  
  public final <T> void set(T name, Object val) {
    c().put(String.valueOf(name), val);
  }
  
  public final String tostring() {
    return "(" + toJsonString() + ")";
  }
  
  public final String toJsonString() {
    String ret = "";
    
    for (String k : keys()){
      JsArrayMixed o = getArray(k);
      if (o != null) {
        ret += k + ":[";
        for (int i = 0, l = o.length(); i < l ; i++) {
          ret += "'" + o.getString(i) + "',";
        }
        ret += "],";
      } else {
        Properties p = getJavaScriptObject(k);
        if (p != null) {
          ret += k + ":" + p.toJsonString();
        } else {
          ret += k + ":'" + getStr(k) + "',";
        }
      }
    }
    return "{" + ret.replaceAll(",\\s*([\\]}]|$)","").replaceAll("([:,\\[])'(-?[\\d\\.]+|null|false|true)'", "$1$2") + "}";
  }
  
  public final String toQueryString() {
    String ret = "";
    for (String k : keys()) {
      ret += ret.isEmpty() ? "" : "&";
      JsArrayMixed o = getArray(k);
      if (o != null) {
        for (int i = 0, l = o.length(); i < l ; i++) {
          ret += (i > 0 ? "&" : "") + k + "[]=" + o.getString(i) ;
        }
      } else {
        Properties p = getJavaScriptObject(k);
        if (p != null) {
          ret += p.toQueryString();
        } else {
          ret += k + "=" + getStr(k);
        }
      }
    }
    return ret;
  }
}
