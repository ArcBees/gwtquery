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

import com.google.gwt.query.client.Properties;

/**
 * Tagging interface used to generate JsonBuilder classes. 
 */
public interface JsonBuilder {
  
  /**
   * load a properties object.
   */
  <J> J load(Object prp);
  
  /**
   * parses a json string and loads the resulting properties object.
   */
  <J> J parse(String json);
  
  /**
   * parses a json string and loads the resulting properties object,
   * if the param 'fix' is true, the syntax of the json string will be
   * checked previously and fixed when possible.
   */
  <J> J parse(String json, boolean fix);
  
  /**
   * Returns the javascript properties object.
   */
  Properties getProperties();
  
  /**
   * return a list of field names.
   */
  String[] getFieldNames();
  
  /**
   * return a string which represents the object with an alias for the className 
   */
  String toJson();
  
  /**
   * return the Json name for this class 
   */
  String getJsonName();
}
