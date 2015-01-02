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

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayMixed;
import com.google.gwt.query.client.builders.JsonBuilder;
import com.google.gwt.query.client.js.JsCache;
import com.google.gwt.query.client.js.JsUtils;

/**
 * JSO for accessing Javascript objective literals used by GwtQuery functions.
 */
public class Properties extends JavaScriptObject implements IsProperties {

  public static Properties create() {
    return JsCache.create().cast();
  }

  public static Properties create(String properties) {
    if (properties != null && !properties.isEmpty()) {
      String p = wrapPropertiesString(properties);
      try {
        return JsUtils.parseJSON(p);
      } catch (Exception e) {
        if (!GWT.isProdMode()) {
          System.err.println("Error creating Properties: \n> " + properties + "\n< " + p + "\n"
              + e.getMessage());
        }
      }
    }
    return create();
  }

  /**
   * Allows using a more relaxed syntax for creating json objects from strings.
   *
   * It is very useful in java, since we dont have to use escaped double quotes,
   * and we can pass directly css strings.
   *
   * Example:
   * $$("a: b; c: 'n'; d: null") is the same than $$("\"a\": \"b\", "\b\":\"n\"n, \"d\":null)")
   */
  public static String wrapPropertiesString(String s) {
    String ret = s //
        .replaceAll("\\s*/\\*[\\s\\S]*?\\*/\\s*", "") // Remove comments
        .replaceAll("([:\\)\\(,;}{'\"])\\s+", "$1") // Remove spaces
        .replaceAll("\\s+([:\\)\\(,;}{'\"])", "$1") // Remove spaces
        .replaceFirst("^[\\(]+(.*)[\\)]+$", "$1") // Remove ()
        .replaceAll("\\([\"']([^\\)]+)[\"']\\)", "($1)") // Remove quotes
        .replaceAll("[;,]+([\\w-\\$]+:|$)", ";$1") // Change comma by semicolon
        .replaceAll("([^,;])([\\]}])", "$1;$2") // Put control semicolon used below
        .replaceAll(":\\s*[\"']?([^;\\{\\}\\[\\]\"']*)[\"']?\\s*([;,]+|$)", ":\"$1\";") // put quotes to all values (even empty)
        .replaceAll("[;,]+([\\w-]+):", ";$1:") // Change semicolon by comma
        .replaceAll("(^|[^\\w-\\$'])([\\w-\\$]+):(['\"\\[{])", "$1\"$2\":$3") // quote keys
        .replaceAll("(^|[^\\w-\\$'])([\\w-\\$]+):(['\"\\[{])", "$1\"$2\":$3") // quote keys second pass
        .replaceAll("(|[\\[\\]\\{\\},\\(])'([^']*)'", "$1\"$2\"") // Replace single-quote by double-quote
        .replaceAll(";([^:]+):", ",$1:") // change semicolon
        .replaceAll(";([^:]+):", ",$1:") // change semicolon second pass
        .replaceAll(":\"(-?\\d[\\d\\.]*|null|false|true)\"[;,]", ":$1,") // numbers do not need quote
        .replaceAll("[;,]+([\\]\\}]|$)", "$1"); // remove endings
    ret = ret.matches("(^[\\[\\{].*[\\]\\}]$)") ? ret : "{" + ret + "}";
    return ret;
  }

  protected Properties() {
  }

  public final Properties $$(String key, String value) {
    set(key, value);
    return this;
  }

  private JsCache c() {
    return this.<JsCache> cast();
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

  @SuppressWarnings("unchecked")
  public final <T> T get(Object name) {
    // Casting because of issue_135
    return (T) c().get(String.valueOf(name));
  }

  public final boolean getBoolean(Object name) {
    return c().getBoolean(String.valueOf(name));
  }

  public final float getFloat(Object name) {
    return c().getFloat(String.valueOf(name));
  }

  public final Function getFunction(Object name) {
    final Object o = c().get(String.valueOf(name));
    if (o != null) {
      if (o instanceof Function) {
        return (Function) o;
      } else if (o instanceof JavaScriptObject) {
        Object f = ((JavaScriptObject) o).<Properties> cast().getObject("__f");
        if (f != null && f instanceof Function) {
          return (Function) f;
        }
        return new JsUtils.JsFunction((JavaScriptObject) o);
      }
    }
    return null;
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

  public final Properties getProperties(Object name) {
    return getJavaScriptObject(name);
  }

  @SuppressWarnings("unchecked")
  public final <T extends JavaScriptObject> T getJavaScriptObject(Object name) {
    // Casting because of issue_135
    return (T) c().getJavaScriptObject(String.valueOf(name));
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

  public final <T> void setNumber(T name, double val) {
    c().putNumber(name, val);
  }

  public final <T> void setBoolean(T name, boolean val) {
    c().putBoolean(name, val);
  }

  /**
   * Adds a new native js function to the properties object.
   * This native function will wrap the passed java Function.
   *
   * Its useful for exporting or importing to javascript.
   *
   */
  public final native <T> void setFunction(T name, Function f) /*-{
    if (!f) return;
    this[name] = function() {
      f.@com.google.gwt.query.client.Function::fe(Ljava/lang/Object;)(arguments);
    }
    // We store the original function reference
    this[name].__f = f;
  }-*/;

  @SuppressWarnings("unchecked")
  public final Properties set(Object name, Object val) {
    c().put(String.valueOf(name), val);
    return this;
  }

  public final String tostring() {
    return toJsonString();
  }

  public final String toJsonString() {
    return JsUtils.JSON2String(JsCache.checkNull(this));
  }

  public final String toQueryString() {
    return JsUtils.param(JsCache.checkNull(this));
  }

  public final boolean isEmpty() {
    return c().length() == 0;
  }

  public final <J extends IsProperties> J load(Object prp) {
    c().clear();
    if (prp instanceof JsCache) {
      c().copy((JsCache) prp);
    }
    return getDataImpl();
  }
  
  @SuppressWarnings("unchecked")
  public final <J extends IsProperties> J strip() {
    return (J) this;
  }

  public final <J extends IsProperties> J parse(String json) {
    return load(JsUtils.parseJSON(json));
  }

  public final String[] getFieldNames() {
    return c().keys();
  }

  public final String toJson() {
    return toJsonString();
  }

  public final String toJsonWithName() {
    return toJsonWithName(getJsonName());
  }

  public final String toJsonWithName(String name) {
    return "{\"" + name + "\":{" + toJson() + "}";
  }

  @SuppressWarnings("unchecked")
  public final <J> J getDataImpl() {
    return (J) this;
  }

  public final String getJsonName() {
    return "jso";
  }

  public final <T extends JsonBuilder> T as(Class<T> clz) {
    T ret = GQ.create(clz);
    ret.load(this);
    return ret;
  }
}
