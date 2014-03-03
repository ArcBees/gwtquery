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

import com.google.gwt.core.client.GWT;
import com.google.gwt.query.client.builders.JsonBuilder;
import com.google.gwt.query.client.builders.JsonFactory;
import com.google.gwt.query.client.plugins.ajax.Ajax.AjaxTransport;
import com.google.gwt.query.client.plugins.ajax.AjaxTransportJs;
import com.google.gwt.query.vm.AjaxTransportJre;
import com.google.gwt.query.vm.JsonFactoryJre;

/**
 * A set of useful methods for gQuery which can be run in browser or JVM.
 */
public abstract class GQ {
  
  private static AjaxTransport ajaxTransport;
  
  private static JsonFactory jsonFactory;

  public static IsProperties create() {
    return getFactory().create();
  }

  /**
   * Create an instance of a JsonBuilder object whose type is <T>
   */
  public static <T extends JsonBuilder> T create(Class<T> clz) {
    return getFactory().create(clz);
  }

  /**
   * Create an instance of a JsonBuilder object whose type is <T>
   * and set the the underlying properties object.
   */
  public static <T extends JsonBuilder> T create(Class<T> clz, IsProperties obj) {
    T ret = create(clz);
    ret.load(obj.getDataImpl());
    return ret;
  }

  /**
   * Create an instance of a JsonBuilder object whose type is <T>
   * and load all its properties from a json string.
   */
  public static <T extends JsonBuilder> T create(Class<T> clz, String payload) {
    T ret = create(clz);
    ret.load(payload);
    return ret;
  }
  
  /**
   * Create an instance of IsProperties, a Properties JavaScriptObject in the client
   * side and a proxy object in the JVM.
   */
  public static IsProperties create(String s) {
    return getFactory().create(s);
  }

  /**
   * Create an instance of IsProperties, a Properties JavaScriptObject in the client
   * side and a proxy object in the JVM.
   *
   * If fixJson is set, we correct certain errors in the Json string. It is useful
   * for generating Properties using java strings, so as we can use a more relaxed 
   * syntax.
   */
  public static IsProperties create(String s, boolean fixJson) {
    return getFactory().create(fixJson ? Properties.wrapPropertiesString(s) : s);
  }

  /**
   * Return the appropriate transport implementation depending on the runtime
   * environment: browser or JVM 
   */
  public static AjaxTransport getAjaxTransport() {
    if (ajaxTransport == null) {
      ajaxTransport = GWT.isClient() ?
          new AjaxTransportJs() :
            new AjaxTransportJre();    
     }
    return ajaxTransport;
  }

  private static JsonFactory getFactory() {
    if (jsonFactory == null) {
      jsonFactory = GWT.isClient() ?
          GWT.<JsonFactory>create(JsonFactory.class) :
          new JsonFactoryJre();
    }
    return jsonFactory;
  }
  
  /**
   * Change the default Ajax transport by a customized one, useful for 
   * testing purposes.
   */
  public static void setAjaxTransport(AjaxTransport transport) {
    ajaxTransport = transport;
  }
  
}
