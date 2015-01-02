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

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;

/**
 * Runtime selector engine implementation for IE with native
 * querySelectorAll support (IE8 standards mode).
 *
 * It will fall back to Sizzle engine when QuerySelector were unavailable
 * or in the case of selectors unsupported by the IE8 native QuerySelector.
 *
 */
public class SelectorEngineNativeIE8 extends SelectorEngineSizzleIE {

  public static String NATIVE_EXCEPTIONS_REGEXP =
      ".*(:contains|!=|:not|:nth-|:only-|:first|:last|:even|:odd).*";

  public NodeList<Element> select(String selector, Node ctx) {
    if (!SelectorEngine.hasQuerySelector || selector.matches(NATIVE_EXCEPTIONS_REGEXP)) {
      return super.select(selector, ctx);
    } else {
      try {
        return SelectorEngine.querySelectorAllImpl(selector, ctx);
      } catch (Exception e) {
        return super.select(selector, ctx);
      }
    }
  }
}
