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

/**
 * Lightweight JSO backed implemented of a Map, using Object.hashCode() as key.
 */
final public class JsMap<S, T> extends JavaScriptObject {

  protected JsMap() {
  }

  public T get(S key) {
    return get(key.hashCode());
  }

  public native T get(int hashCode) /*-{
    return this[hashCode] || null;
  }-*/;

  public void put(S key, T val) {
    put(key.hashCode(), val);
  }

  public native void put(int hashCode, T val) /*-{
    this[hashCode]=val;
  }-*/;
}
