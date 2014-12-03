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

import com.google.gwt.core.shared.GWT;
import com.google.gwt.query.client.IsProperties;
import com.google.gwt.query.client.Properties;
import com.google.gwt.query.client.builders.JsonBuilder;
import com.google.gwt.query.client.builders.JsonFactory;
import com.google.gwt.query.client.plugins.ajax.AjaxTransportJs;
import com.google.gwt.query.client.plugins.ajax.Ajax.AjaxTransport;
import com.google.gwt.query.vm.AjaxTransportJre;
import com.google.gwt.query.vm.JsonFactoryJre;
import com.google.gwt.user.client.Window;

public class GQ {
  
  private static JsonFactory jsonFactory;
  private static AjaxTransport ajaxTransport;

  public static <T extends JsonBuilder> T create(Class<T> clz) {
    return getFactory().create(clz);
  }

  public static <T extends JsonBuilder> T create(Class<T> clz, String payload) {
    T ret = create(clz);
    ret.load(payload);
    return ret;
  }

  public static <T extends JsonBuilder> T create(Class<T> clz, IsProperties obj) {
    T ret = create(clz);
    ret.load(obj.getDataImpl());
    return ret;
  }
  
  public static IsProperties create(String s) {
    return getFactory().create(s);
  }

  public static IsProperties create(String s, boolean fix) {
    return getFactory().create(fix ? Properties.wrapPropertiesString(s) : s);
  }

  public static IsProperties create() {
    return getFactory().create();
  }

  public static AjaxTransport getAjaxTransport() {
    if (ajaxTransport == null) {
      ajaxTransport = new AjaxTransportJs();    
    }
    return ajaxTransport;
  }
  
  private static JsonFactory getFactory() {
    if (jsonFactory == null) {
      jsonFactory = GWT.<JsonFactory>create(JsonFactory.class);
    }
    return jsonFactory;
  }
}
