/*
 * Copyright 2009 Google Inc.
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
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.SelectorEngine;

/**
 * A helper class to get computed CSS styles for elements.
 */
public class DocumentStyleImpl {

  public String getCurrentStyle(Element elem, String name) {
    name = hyphenize(name);
    String propVal = getComputedStyle(elem, name, null);
    if ("opacity".equals(name)) {
      propVal = SelectorEngine.or(propVal, "1");
    }
    return propVal;
  }

  public String getPropertyName(String name) {
    if ("float".equals(name)) {
      return "cssFloat";
    } else if ("class".equals(name)) {
      return "className";
    } else if ("for".equals(name)) {
      return "htmlFor";
    }
    return GQuery.camelize(name);
  }

  protected native String hyphenize(String name) /*-{
      return name.replace( /([A-Z])/g, "-$1" ).toLowerCase();
  }-*/;

  private native String getComputedStyle(Element elem, String name,
      String pseudo) /*-{
      var cStyle = $doc.defaultView.getComputedStyle( elem, pseudo );
      return cStyle ? cStyle.getPropertyValue( name ) : null;
  }-*/;
}
