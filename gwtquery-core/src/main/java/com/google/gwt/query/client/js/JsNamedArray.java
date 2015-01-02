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
 * Lightweight JSO backed implemented of a named array.
 *
 * @param <T>
 */
public final class JsNamedArray<T> extends JavaScriptObject {

  protected JsNamedArray() {
  }

  private JsCache c() {
    return cast();
  }

  @SuppressWarnings("unchecked")
  public T get(String key) {
    return (T) c().get(key);
  }

  public void put(String key, T val) {
    c().put(key, val);
  }

  public String[] keys() {
    return c().keys();
  }

  public int length() {
    return c().length();
  }

  public Object[] values() {
    return c().elements();
  }

  public boolean exists(String key) {
    return c().exists(key);
  }

  public void delete(String key) {
    c().delete(key);
  }

  public static <T> JsNamedArray<T> create() {
    return createObject().cast();
  }
}
