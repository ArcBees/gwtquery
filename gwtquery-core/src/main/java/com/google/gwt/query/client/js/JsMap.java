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
 * Lightweight JSO backed implemented of a Map, using Object.hashCode() as key.
 *
 * @param <S>
 * @param <T>
 */
public final class JsMap<S, T> extends JavaScriptObject {

  protected JsMap() {
  }

  private JsCache c() {
    return cast();
  }

  @SuppressWarnings("unchecked")
  public T get(int hashCode) {
    return (T) c().get(hashCode);
  }

  public T get(S key) {
    return get(key.hashCode());
  }

  public void put(S key, T val) {
    c().put(key.hashCode(), val);
  }

  public T remove(S key) {
    T old = get(key);
    c().delete(key.hashCode());
    return old;
  }

  public T remove(int key) {
    T old = get(key);
    c().delete(key);
    return old;
  }

  public String[] keys() {
    return c().keys();
  }

  public static <S, T> JsMap<S, T> create() {
    return createObject().cast();
  }
}
