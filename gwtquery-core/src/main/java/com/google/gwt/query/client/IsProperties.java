/*
 * Copyright 2013, The gwtquery team.
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

import com.google.gwt.query.client.builders.JsonBuilder;

/**
 * Interface using for Data Binders valid for JVM and JS.
 */
public interface IsProperties {
  /**
   * load a properties object.
   */
  <T extends IsProperties> T load(Object prp);

  /**
   * parses a json string and loads the resulting properties object.
   */
  <T extends IsProperties> T parse(String json);
  
  /**
   * Removes the extra JSON and leaves only the setters/getters described 
   * in the JsonBuilder interface.
   * If the object contains another IsProperties attributes the method strip()
   * is called on them.
   */
  <T extends IsProperties> T strip();

  /**
   * Returns the underlying object, normally a Properties jso in client
   * and a Json implementation in the JVM.
   */
  <T> T getDataImpl();

  /**
   * Return the Object with the given key.
   */
  <T> T get(Object key);

  /**
   * Set an Object with the given key.
   */
  <T extends IsProperties> T set(Object key, Object val);

  /**
   * return a list of field names.
   */
  String[] getFieldNames();

  /**
   * return a json string which represents the object.
   * Example {"name":"manolo","surname":"carrasco"}
   */
  String toJson();

  /**
   * return a string which represents the object with an alias for the
   * className useful for serialization.
   * 
   * Example {"user":{"name":"manolo","surname":"carrasco"}}
   */
  String toJsonWithName();

  /**
   * return a string which represents the object in a queryString format.
   */
  String toQueryString();

  /**
   * return the name for this type.
   */
  String getJsonName();

  /**
   * converts a JsonBuilder instance into another JsonBuilder type but
   * preserving the underlying data object.
   */
  <T extends JsonBuilder> T as(Class<T> clz);
}
