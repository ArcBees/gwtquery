/*
 * Copyright 2009 Google Inc.
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
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;

/**
 */
public class JSArray extends NodeList<Element> {

  public static JSArray create() {
    return (JSArray) JavaScriptObject.createArray();
  }

  public static native JSArray create(Node node) /*-{
    return [node];
  }-*/;

  public static native JSArray create(NodeList nl) /*-{
    var r = [], len=nl.length;
    for(var i=0; i<len; i++) {
      r.push(nl[i]);
    }
    return r;
  }-*/;

  protected JSArray() {
  }

  public final native void addInt(int i) /*-{
       this[this.length]=i;
  }-*/;

  public final native void addNode(Node n) /*-{
     this[this.length]=n;
  }-*/;

  public final native void addNode(Node ci, int i) /*-{
       this[i]=ci;
    }-*/;

  public final native void concat(JSArray ary) /*-{
     this.concat(ary);
  }-*/;

  public final native Element getElement(int i) /*-{
        return this[i];
    }-*/;

  public final native int getInt(int i) /*-{
    return this[i]  || 0;
  }-*/;

  public final native Node getNode(int i) /*-{
        return this[i];
  }-*/;

  public final native String getStr(int i) /*-{
     return this[i] || null;
  }-*/;

  public final void pushAll(JSArray prevElem) {
    for (int i = 0, ilen = prevElem.size(); i < ilen; i++) {
      addNode(prevElem.getNode(i));
    }
  }

  public final native int size() /*-{
     return this.length;
  }-*/;
}
