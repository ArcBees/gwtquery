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
import com.google.gwt.query.client.IsProperties;
import com.google.gwt.query.client.Properties;
import com.google.gwt.query.client.js.JsObjectArray;
import com.google.gwt.query.client.js.JsUtils;

public abstract class JsonBuilderBase<J extends JsonBuilderBase<?>> implements JsonBuilder {

  protected Properties p = Properties.create();

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
  public J load(Object prp) {
    assert prp == null || prp instanceof JavaScriptObject || prp instanceof String;
    if (prp != null && prp instanceof String) {
      return parse((String)prp);
    }
    if (prp != null) {
      p = (Properties)prp;
    }
    return (J)this;
  }

  protected <T> void setArrayBase(String n, T[] r) {
    if (r.length > 0 && r[0] instanceof JsonBuilder) {
      JsArray<JavaScriptObject> a = JavaScriptObject.createArray().cast();
      for (T o : r) {
        a.push(((JsonBuilder)o).<Properties>getDataImpl());
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
    for (int i = 0 ; i < l ; i++) {
      Object w = a.get(i);
      Class<?> c = w.getClass();
      do {
        if (c.equals(clazz)) {
          r[i] = (T)w;
          break;
        }
        c = c.getSuperclass();
      } while (c != null);
    }
    return r;
  }

  protected Properties getPropertiesBase(String n) {
    Properties r = p.getJavaScriptObject(n);
    return r != null ? r : Properties.create();
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
      p.set(key, ((IsProperties)val).getDataImpl());
    } else {
      p.set(key, val);
    }
    return (T)this;
  }
  
  public <T extends JsonBuilder> T as(Class<T> clz) {
    return p.as(clz);
  }
}
