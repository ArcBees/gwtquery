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

/**
 * A tagging interface which triggers the LazyGenerator for type T.
 * LazyGenerator creates an implementation of Type T for the class by forwarding
 * method calls to the class which implements the Lazy interface. Methods in the
 * generated class do not execute but instead queue up a deferred execution of
 * the method.
 *
 * @param <S>
 * @param <T>
 */
public interface Lazy<S, T extends LazyBase> {

  /**
   * Create a lazy instance of the current class. Most implementing classes will
   * automate this by simply invoking GWT.create().
   */
  T createLazy();
}
