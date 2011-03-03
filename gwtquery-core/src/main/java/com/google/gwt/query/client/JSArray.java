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

import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.query.client.js.JsNodeArray;

/**
 * @deprecated use JsNodeArray
 */
@Deprecated
public final class JSArray extends JsNodeArray {
  
  protected JSArray() {
  }

  public static JSArray create() {
    return JsNodeArray.create().cast();
  }

  public static JSArray create(Node node) {
    return JsNodeArray.create(node).cast();
  }

  public static JSArray create(NodeList<?> nl) {
    JSArray ret = create();
    for(int i=0; i<nl.getLength(); i++) {
      ret.addNode(nl.getItem(i));
    }
    return ret;
  }

}
