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
package com.google.gwt.query.client.builders;

import com.google.gwt.query.client.IsProperties;

/**
 * Tagging interface used to generate JsonBuilder classes.
 */
public interface JsonBuilder extends IsProperties {

  /**
   * parses a json string and loads the resulting properties object,
   * if the param 'fix' is true, the syntax of the json string will be
   * checked previously and fixed when possible.
   */
  <J> J parse(String json, boolean fix);

  /**
   * Returns the wrapped object, normally a Properties jso in client
   * but can be used to return the underlying Json implementation in JVM.
   * 
   * @deprecated use asObject() instead.
   */
  <J> J getProperties();

  /**
   * return the short name of this class, to use in json structures.
   */
  String getJsonName();
}
