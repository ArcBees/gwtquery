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

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;

/**
 * Runtime selector engine implementation for browsers with native
 * querySelectorAll support.
 */
public class SelectorEngineNative extends SelectorEngineImpl {
  
  public static String NATIVE_EXCEPTIONS_REGEXP = ".*(:contains|!=).*";
  
  private static HasSelector impl;
  
  NodeList<Element> result = null;
  
  public SelectorEngineNative() {
  }
  
  RunAsyncCallback callBack = new RunAsyncCallback() {
    public void onSuccess() {
      if (impl == null) {
        impl=GWT.create(HasSelector.class);
      }
    }
    public void onFailure(Throwable reason) {
    }
  };
  
  private NodeList<Element> jsFallbackSelect (String selector, Node ctx) {
    if (impl == null) {
      GWT.runAsync(callBack);
      while (impl == null);
    } 
    return impl.select(selector, ctx);
  }
  
  public NodeList<Element> select(String selector, Node ctx) {
    if (!SelectorEngine.hasQuerySelector || selector.matches(NATIVE_EXCEPTIONS_REGEXP)) {
      return jsFallbackSelect(selector, ctx);
    } else {
      try {
        return SelectorEngine.querySelectorAllImpl(selector, ctx);
      } catch (Exception e) {
        return jsFallbackSelect(selector, ctx);
      }
    }
  }
  
}
