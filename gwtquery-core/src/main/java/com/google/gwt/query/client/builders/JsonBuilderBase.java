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
package com.google.gwt.query.client.builders;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayMixed;
import com.google.gwt.query.client.GQ;
import com.google.gwt.query.client.IsProperties;
import com.google.gwt.query.client.Properties;
import com.google.gwt.query.client.js.JsCache;
import com.google.gwt.query.client.js.JsObjectArray;
import com.google.gwt.query.client.js.JsUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Common class for all JsonBuilder implementations.
 *
 * @param <J>
 */
public abstract class JsonBuilderBase<J extends JsonBuilderBase<?>> implements JsonBuilder {

  protected Properties p = Properties.create();
  protected String[] fieldNames = new String[] {};

  @SuppressWarnings("unchecked")
  @Override
  public J parse(String json) {
    return load(JsUtils.parseJSON(json));
  }

  @SuppressWarnings("unchecked")
  @Override
  public J parse(String json, boolean fix) {
    return fix ? parse(Properties.wrapPropertiesString(json)) : parse(json);
  }

  @SuppressWarnings("unchecked")
  @Override
  public J strip() {
    List<String> names = Arrays.asList(getFieldNames());
    for (String jsonName : p.getFieldNames()) {
      // TODO: figure out a way so as we can generate some marks in generated class in
      // order to call getters to return JsonBuilder object given an an attribute name
      if (!names.contains(jsonName)) {
        p.<JsCache>cast().delete(jsonName);
      }
    }
    return (J) this;
  }

  @SuppressWarnings("unchecked")
  @Override
  public J load(Object prp) {
    assert prp == null || prp instanceof JavaScriptObject || prp instanceof String;
    if (prp != null && prp instanceof String) {
      return parse((String) prp);
    }
    if (prp != null) {
      p = (Properties) prp;
    }
    return (J) this;
  }

  protected <T> void setArrayBase(String n, T[] r) {
    if (r.length > 0 && r[0] instanceof JsonBuilder) {
      JsArray<JavaScriptObject> a = JavaScriptObject.createArray().cast();
      for (T o : r) {
        a.push(((JsonBuilder) o).<Properties> getDataImpl());
      }
      p.set(n, a);
    } else {
      JsObjectArray<Object> a = JsObjectArray.create();
      a.add(r);
      p.set(n, a);
    }
  }

  @SuppressWarnings("unchecked")
  protected <T> T[] getArrayBase(String n, T[] r, Class<T> clazz) {
    JsObjectArray<?> a = p.getArray(n).cast();
    int l = r.length;
    for (int i = 0; i < l; i++) {
      Object w = a.get(i);
      Class<?> c = w.getClass();
      do {
        if (c.equals(clazz)) {
          r[i] = (T) w;
          break;
        }
        c = c.getSuperclass();
      } while (c != null);
    }
    return r;
  }

  protected final <T extends JsonBuilder> T[] getIsPropertiesArrayBase(JsArrayMixed js, T[] r, Class<T> clazz) {
    JsObjectArray<?> a1 = js.cast();
    for (int i = 0; i < r.length; i++) {
      r[i] = getIsPropertiesBase(a1.get(i), clazz);
    }
    return r;
  }

  protected final <T extends JsonBuilder> T getIsPropertiesBase(Object o, Class<T> clazz) {
    return GQ.create(clazz).load(o);
  }

  protected Properties getPropertiesBase(String n) {
    if (p.getJavaScriptObject(n) == null) {
      p.set(n, Properties.create());
    }
    return p.getJavaScriptObject(n);
  }

  public String toString() {
    return p.tostring();
  }

  @Override
  public String toJson() {
    return p.tostring();
  }

  public String toJsonWithName() {
    return "{\"" + getJsonName() + "\":" + p.tostring() + "}";
  }

  @SuppressWarnings("unchecked")
  @Override
  public Properties getProperties() {
    return p;
  }

  @Override
  public String toQueryString() {
    return p.toQueryString();
  }

  @SuppressWarnings("unchecked")
  @Override
  public Properties getDataImpl() {
    return p;
  }

  public <T> T get(Object key) {
    return p.get(key);
  }

  @SuppressWarnings("unchecked")
  public <T extends IsProperties> T set(Object key, Object val) {
    if (val instanceof IsProperties) {
      p.set(key, ((IsProperties) val).getDataImpl());
    } else if (val instanceof Object[]) {
      setArrayBase(String.valueOf(key), (Object[]) val);
    } else if (val instanceof Collection) {
      Collection collection = (Collection) val;
      setArrayBase(String.valueOf(key), collection.toArray(new Object[collection.size()]));
    } else {
      p.set(key, val);
    }
    return (T) this;
  }

  public <T extends JsonBuilder> T as(Class<T> clz) {
    return p.as(clz);
  }

  public final String[] getFieldNames() {
    return fieldNames;
  }
}
