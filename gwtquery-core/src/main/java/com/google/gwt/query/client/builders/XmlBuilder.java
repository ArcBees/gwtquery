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

import com.google.gwt.dom.client.Element;

/**
 * Tagging interface used to generate XmlBuilder classes. 
 */
public interface XmlBuilder {
  /**
   * load a string or a documentElement.
   */
  <J> J load(Object o);
  
  /**
   * parses a xml string and loads the resulting documentElement.
   */
  <J> J parse(String xml);
  
  /**
   * Returns the documentElement.
   */
  Element getRootElement();
  
  /**
   * Appends a node
   */
  void append(XmlBuilder x);

  /**
   * Appends xml content
   */
  void append(String x);
}
