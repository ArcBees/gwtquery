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
package com.google.gwt.query.client.css;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Style;

/**
 * This property specifies whether a box should float to the left, right, or not
 * at all. It may be set for any element, but only applies to elements that
 * generate boxes that are not absolutely positioned.
 */
public class Float implements CssProperty<Float.FloatValue> {

  public static void init() {
    CSS.FLOAT = new Float();
    CSS.FLOAT_LEFT = FloatValue.create("left");
    CSS.FLOAT_RIGHT = FloatValue.create("right");
    CSS.FLOAT_NONE = FloatValue.create("none");
  }

  public void set(Style s, FloatValue value) {
    s.setProperty("float", value.value());
  }

  public String get(Style s) {
    return s.getProperty("float");
  }

  final static public class FloatValue extends JavaScriptObject {

    protected FloatValue() {
    }

    protected static FloatValue create(String val) {
      return GWT.isScript() ? createWeb(val) : createHosted(val);
    }

    public String value() {
      return GWT.isScript() ? valueWeb() : valueHosted();
    }

    private static native FloatValue createWeb(String val) /*-{
      return val;
    }-*/;

    private static native FloatValue createHosted(String val) /*-{
      return [val];
    }-*/;

    private native String valueWeb() /*-{
       return this;
    }-*/;

    private native String valueHosted() /*-{
       return this[0];
    }-*/;
  }
}
