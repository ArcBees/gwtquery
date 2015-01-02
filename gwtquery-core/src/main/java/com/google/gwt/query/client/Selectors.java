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

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;

/**
 * Tagging interface used to generate compile time selectors.
 */
public interface Selectors {

  /**
   * A compiled selector that can be lazily turned into a GQuery.
   */
  public interface DeferredSelector {

    /**
     * Evaluate the compiled selector with the given DOM node as a context.
     *
     * Returns a NodeList as a result which you could transform into a GQuery
     * object passing it as argument to the $() function.
     */
    NodeList<Element> runSelector(Node ctx);

    /**
     * The selector which was compiled.
     */
    String getSelector();
  }

  /**
   * Return all the selectors defined for this interface, so as
   * we can get the css representation of each one and lazily evaluate it
   * in runtime.
   */
  DeferredSelector[] getAllSelectors();

  /**
   * Set the context for all the selectors.
   * By default they are evaluated in all the document.
   */
  void setRoot(Node node);

  /**
   * Get the configured root context.
   */
  Node getRoot();

  /**
   * Used for benchmarking purposes, it returns true if the selector engine
   * for this browser is using a pure javascript implementation or a native
   * one.
   *
   * It is useful to check if IE8 native selectors are being used.
   */
  boolean isDegradated();
}
