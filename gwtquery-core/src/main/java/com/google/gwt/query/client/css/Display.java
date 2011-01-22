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
 * This property specifies the mechanism by which elements are rendered.
 */
public class Display implements CssProperty<Display.DisplayValue> {

  final static public class DisplayValue extends JavaScriptObject {

    protected static DisplayValue create(String val) {
      return GWT.isScript() ? createWeb(val) : createHosted(val);
    }

    private static native DisplayValue createHosted(String val) /*-{
      return [val];
    }-*/;

    private static native DisplayValue createWeb(String val) /*-{
      return val;
    }-*/;

    protected DisplayValue() {
    }

    public String value() {
      return GWT.isScript() ? valueWeb() : valueHosted();
    }

    private native String valueHosted() /*-{
       return this[0];
    }-*/;

    private native String valueWeb() /*-{
       return this;
    }-*/;
  }

  public static void init() {
    CSS.DISPLAY = new Display();
    CSS.BLOCK = DisplayValue.create("block");
    CSS.INLINE = DisplayValue.create("inline");
    CSS.INLINE_BLOCK = DisplayValue.create("inline-block");
    CSS.LIST_ITEM = DisplayValue.create("list-item");
    CSS.NONE = DisplayValue.create("none");
    CSS.RUN_IN = DisplayValue.create("run-in");
    CSS.TABLE = DisplayValue.create("table");
    CSS.INLINE_TABLE = DisplayValue.create("inline-table");
    CSS.TABLE_ROW_GROUP = DisplayValue.create("table-row-group");
    CSS.TABLE_COLUMN = DisplayValue.create("table-column");
    CSS.TABLE_COLUMN_GROUP = DisplayValue.create("table-column-group");
    CSS.TABLE_HEADER_GROUP = DisplayValue.create("table-header-group");
    CSS.TABLE_FOOTER_GROUP = DisplayValue.create("table-footer-group");
    CSS.TABLE_ROW = DisplayValue.create("table-row");
    CSS.TABLE_CELL = DisplayValue.create("table-cell");
    CSS.TABLE_CAPTION = DisplayValue.create("table-caption");
  }

  public String get(Style s) {
    return s.getProperty("display");
  }

  public void set(Style s, DisplayValue value) {
    s.setProperty("display", value.value());
  }
}
