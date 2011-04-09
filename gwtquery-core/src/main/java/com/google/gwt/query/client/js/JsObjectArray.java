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

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Lightweight JSO based array class that can store objects.
 */
public final class JsObjectArray<T> extends JavaScriptObject {
  
  public static JsObjectArray<?> create() {
    return JavaScriptObject.createArray().cast();
  }
  
  protected JsObjectArray() {
  }
  
  private JsCache c() {
    return cast();
  }

  public void add(T...vals) {
    for (T t: vals) {
      c().put(length(), t);
    }
  }

  public void add(int i, T val) {
    c().put(i, val);
  }

  @SuppressWarnings("unchecked")
  public T get(int hashCode) {
    return (T)c().get(hashCode);
  }

  public int length() {
    return c().length();
  }

  public void set(int i, T val) {
    c().put(i, val);
  }
  
  public void concat(JsObjectArray<T> ary) {
    c().concat(ary);
  }

  public void pushAll(JavaScriptObject prevElem) {
    c().pushAll(prevElem);
  }

}
