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
package com.google.gwt.query.client.impl;

import static com.google.gwt.query.client.GQuery.console;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;

/**
 * Runtime selector engine implementation for IE with native querySelectorAll
 * support (IE8 standards mode).
 *
 * In the case of QuerySelector were unavailable or unsupported selectors, it
 * will display an error message instead of falling back to js.
 */
public class SelectorEngineNativeMinIE8 extends SelectorEngineImpl {

  public NodeList<Element> select(String selector, Node ctx) {
    try {
      return SelectorEngine.querySelectorAllImpl(selector, ctx);
    } catch (Exception e) {
      console.error("GwtQuery: Selector '" + selector
          + "' is unsupported in this SelectorEngineNativeMinIE8 engine,"
          + " check that you are in 'standards mode' or configure your module to use JS fallback. "
          + e.getMessage());
      return null;
    }
  }
}
