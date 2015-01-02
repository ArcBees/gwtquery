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
package com.google.gwt.query.client.impl;

import com.google.gwt.dom.client.Element;

/**
 * Helper class for setting and getting attribute on an element.
 */
public class AttributeTridentImpl extends AttributeImpl {

  /**
   * Use {@link AttrNodeSetter} for button element.
   */
  protected static class IEValueAttrSetter extends ValueAttrSetter {
    private static AttributeSetter INSTANCE;

    public static AttributeSetter getInstance() {
      if (INSTANCE == null) {
        INSTANCE = new IEValueAttrSetter();
      }
      return INSTANCE;
    }

    public void setAttribute(Element e, String key, Object value) {
      if ("button".equals(e.getTagName())) {
        AttrNodeSetter.getInstance().setAttribute(e, key, value);
        return;
      }
      super.setAttribute(e, key, value);
    }
  }
  private static class AttrNodeSetter extends DefaultSetter {
    private static AttributeSetter INSTANCE;

    public static AttributeSetter getInstance() {
      if (INSTANCE == null) {
        INSTANCE = new AttrNodeSetter();
      }
      return INSTANCE;
    }

    public void setAttribute(Element e, String key, Object value) {
      if (!setAttributeNode(e, key, value)) {
        super.setAttribute(e, key, value);
      }
    }
  }

  private static native String getAttributeNode(Element e, String name) /*-{
    var attrNode = e.getAttributeNode(name);
    if (attrNode && attrNode.nodeValue !== "") {
      return attrNode.nodeValue;
    }
    return null;
  }-*/;

  private static native boolean setAttributeNode(Element e, String name,
      Object value) /*-{
  var attrNode = e.getAttributeNode(name);
  if (attrNode) {
  attrNode.nodeValue = value;
  return true;
  }
  return false;
  }-*/;

  @Override
  protected Object fixValue(String key, Object value) {
    if (("width".equalsIgnoreCase(key) || "height".equalsIgnoreCase(key))
        && (key == null || "".equals(key))) {
      return "auto";
    }
    return value;
  }

  @Override
  protected AttributeSetter getAttributeSetter(String key) {
    if (!"className".equals(key)
        && ("name".equals(key) || "title".equals(key) || key.contains(":") || key.startsWith("on"))) {
      return AttrNodeSetter.getInstance();
    } else if ("value".equals(key)) {
      return IEValueAttrSetter.getInstance();
    }
    return super.getAttributeSetter(key);
  }
}
