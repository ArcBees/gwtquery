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
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.js.JsUtils;
import com.google.gwt.regexp.shared.RegExp;

/**
 * Helper class for setting and getting attribute on an element.
 */
public class AttributeImpl {

  /**
   * Interface used for attribute setter implementations.
   */
  public interface AttributeSetter {
    boolean isRemoval(Object value);

    void setAttribute(Element e, String name, Object value);
  }

  /**
   * Default setter using <code>e.setAttribute()</code> method.
   */
  protected static class DefaultSetter implements AttributeSetter {
    private static AttributeSetter INSTANCE;

    public static AttributeSetter getInstance() {
      if (INSTANCE == null) {
        INSTANCE = new DefaultSetter();
      }
      return INSTANCE;
    }

    public boolean isRemoval(Object value) {
      return value == null;
    }

    public void setAttribute(Element e, String key, Object value) {
      e.setAttribute(key, String.valueOf(value));
    }
  }

  /**
   * value must be set on element directly.
   */
  protected static class ValueAttrSetter extends DefaultSetter {
    private static AttributeSetter INSTANCE;

    public static AttributeSetter getInstance() {
      if (INSTANCE == null) {
        INSTANCE = new ValueAttrSetter();
      }
      return INSTANCE;
    }

    public void setAttribute(Element e, String key, Object value) {
      e.setPropertyObject("value", String.valueOf(value));
      super.setAttribute(e, key, value);
    }
  }

  /**
   * Boolean attribute.
   * - Ensure to set a boolean value to the element's property with the same
   * name if this last exists
   * - Ensure to set an attribute having the value equals to the name (i.e
   * checked="checked")
   */
  private static class BooleanAttrSetter extends DefaultSetter {
    private static AttributeSetter INSTANCE;

    public static AttributeSetter getInstance() {
      if (INSTANCE == null) {
        INSTANCE = new BooleanAttrSetter();
      }
      return INSTANCE;
    }

    public boolean isRemoval(Object value) {
      return super.isRemoval(value) || Boolean.FALSE.equals(value);
    }

    public void setAttribute(Element e, String key, Object value) {
      if (JsUtils.hasProperty(e, key)) {
        e.setPropertyBoolean(key, true);
      }
      super.setAttribute(e, key, key.toLowerCase());
    }
  }

  /**
   * Id attribute.
   */
  private static class IdAttrSetter extends DefaultSetter {
    private static AttributeSetter INSTANCE;

    public static AttributeSetter getInstance() {
      if (INSTANCE == null) {
        INSTANCE = new IdAttrSetter();
      }
      return INSTANCE;
    }

    public void setAttribute(Element e, String key, Object value) {
      e.setId(value == null ? null : value.toString());
      super.setAttribute(e, key, value);
    }
  }

  /**
   * For button and inputs, the type cannot be changed once the element is
   * attached to the DOM.
   */
  private static class TypeAttrSetter extends DefaultSetter {
    private static AttributeSetter INSTANCE;
    private static RegExp NOT_AUTHORIZED_NODE = RegExp.compile(
        "^(?:button|input)$", "i");

    public static AttributeSetter getInstance() {
      if (INSTANCE == null) {
        INSTANCE = new TypeAttrSetter();
      }
      return INSTANCE;
    }

    protected TypeAttrSetter() {
    }

    public void setAttribute(Element e, String name, Object value) {
      String tag = e.getNodeName();
      if (NOT_AUTHORIZED_NODE.test(tag)
          && GQuery.$(e).parents("body").length() > 0) {
        //  TODO maybe it's better to simply do nothing...
        throw new RuntimeException(
            "You cannot change type of button or input element if the element is already attached to the dom");
      }
      if ("input".equals(tag.toLowerCase()) && "radio".equals(value)) {
        // some browser (IE6-9) resets value property of the input when you change the type to radio button
        InputElement ie = InputElement.as(e);
        String keepValue = ie.getValue();
        super.setAttribute(ie, "type", value);
        ie.setValue(keepValue);
      } else {
        super.setAttribute(e, name, value);
      }
    }
  }

  private static final RegExp BOOLEAN_ATTR_REGEX = RegExp.compile(
      "^(?:autofocus|autoplay|async|checked|controls|defer|disabled|hidden|loop|multiple|open|readonly|required|scoped|selected)$",
      "i");

  public void removeAttribute(GQuery gQuery, String key) {
    for (Element e : gQuery.elements()) {
      if (e.getNodeType() != 1) {
        continue;
      }
      if (JsUtils.hasProperty(e, key)) {
        if (BOOLEAN_ATTR_REGEX.test(key)) {
          e.setPropertyBoolean(key, false);
        } else {
          e.setPropertyObject(key, null);
        }
      }
      e.removeAttribute(key);
    }
  }

  public void setAttribute(GQuery gQuery, String key, Object value) {
    AttributeSetter setter = getAttributeSetter(key);
    if (setter.isRemoval(value)) {
      gQuery.removeAttr(key);
      return;
    }
    value = fixValue(key, value);
    for (Element e : gQuery.elements()) {
      int nodeType = e.getNodeType();
      // don't set attribute on text, comment and attributes nodes
      if (nodeType == 3 || nodeType == 8 || nodeType == 2) {
        continue;
      }
      setter.setAttribute(e, key, value);
    }
  }

  protected Object fixValue(@SuppressWarnings("unused") String key, Object value) {
    return value;
  }

  protected AttributeSetter getAttributeSetter(String key) {
    if ("type".equalsIgnoreCase(key)) {
      return TypeAttrSetter.getInstance();
    } else if ("id".equals(key)) {
      return IdAttrSetter.getInstance();
    } else if ("value".equals(key)) {
      return ValueAttrSetter.getInstance();
    } else if (BOOLEAN_ATTR_REGEX.test(key)) {
      return BooleanAttrSetter.getInstance();
    }
    return DefaultSetter.getInstance();
  }
}
