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

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;

/**
 * A compiled selector that can be lazily turned into a GQuery.
 */
public interface DeferredGQuery {

  /**
   * Evaluate the compiled selector with the given DOM node as a context.
   * Returns a NodeList as a result.
   */
  NodeList<Element> array(Node ctx);

  /**
   * Evaluate the compiled selector with the given DOM node as a context.
   * Returns the result as a GQuery object.
   */
  GQuery eval(Node ctx);

  /**
   * The selector which was compiled.
   */
  String getSelector();
}
