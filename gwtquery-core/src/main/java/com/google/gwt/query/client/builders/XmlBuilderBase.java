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

import static com.google.gwt.query.client.GQuery.$;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsDate;
import com.google.gwt.dom.client.Element;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.Properties;
import com.google.gwt.query.client.js.JsUtils;

import java.util.Date;

/**
 * Common code in XmlBuilder implementations.
 *
 * @param <J>
 */
public abstract class XmlBuilderBase<J extends XmlBuilderBase<?>> implements XmlBuilder {

  // TODO empty document
  protected GQuery g = $((Element) JsUtils.parseXML("<root/>"));

  public void append(String xml) {
    g.append(JsUtils.parseXML(xml));
  }

  public void append(XmlBuilder x) {
    g.append(x.getRootElement());
  }

  protected Boolean getBooleanBase(String n) {
    return "true".equalsIgnoreCase(getStrBase(n));
  }

  protected Element getElementBase(String n) {
    return g.children(n).get(0);
  }

  protected Element[] getElementsBase(String n) {
    return g.children(n).elements();
  }

  protected float getFloatBase(String s) {
    String n = getStrBase(s).replaceAll("[^\\d\\-\\.]", "");
    return n.isEmpty() ? 0 : Float.parseFloat(n);
  }

  protected Properties getPropertiesBase(String n) {
    // TODO:
    return null;
  }

  public Element getRootElement() {
    return g.get(0);
  }

  protected String getStrBase(String n) {
    return g.attr(n);
  }

  public String getText() {
    return g.text();
  }

  public double getTextAsNumber() {
    String t = g.text().replaceAll("[^\\d\\.\\-]", "");
    return t.isEmpty() ? 0 : Double.parseDouble(t);
  }

  public Date getTextAsDate() {
    String t = g.text().trim();
    if (t.matches("\\d+")) {
      return new Date(Long.parseLong(t));
    } else {
      return new Date((long) JsDate.parse(t));
    }
  }

  public boolean getTextAsBoolean() {
    String t = g.text().trim().toLowerCase();
    return !t.matches("^(|false|off|0)$");
  }

  public <T extends Enum<T>> T getTextAsEnum(Class<T> clazz) {
    String t = g.text().trim();
    return Enum.valueOf(clazz, t);
  }

  @SuppressWarnings("unchecked")
  public J load(Object o) {
    assert o == null || o instanceof JavaScriptObject && JsUtils.isElement((JavaScriptObject) o)
        || o instanceof String;
    if (o != null && o instanceof String) {
      return parse((String) o);
    }
    if (o != null) {
      g = $((Element) o);
    }
    return (J) this;
  }

  @SuppressWarnings("unchecked")
  public J parse(String xml) {
    return load(JsUtils.parseXML(xml));
  }

  protected <T> void setArrayBase(String n, T[] r) {
    String v = "";
    for (T t : r) {
      v += String.valueOf(t);
    }
    setBase(n, v);
  }

  protected void setBase(String n, Object v) {
    g.attr(n, v);
  }

  @SuppressWarnings("unchecked")
  public <T> T setText(String t) {
    g.text(t);
    return (T) this;
  }

  public String toString() {
    return g.toString();
  }
}
