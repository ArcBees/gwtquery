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
 * Lightweight JSO based array class that can store objects rather than just
 * other JSOs.
 */
public final class JsObjectArray<T> extends JavaScriptObject {

  protected JsObjectArray() {
  }

  public void add(T val) {
    set(length(), val);
  }

  public native T get(int i) /*-{
    return this[i];
  }-*/;

  public native int length() /*-{
    return this.length;
  }-*/;

  public native void set(int i, T val) /*-{
    this[i]=val;
  }-*/;
}
