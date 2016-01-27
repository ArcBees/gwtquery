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
package com.google.gwt.query.client;

import static com.google.gwt.query.client.GQuery.$;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.dom.client.Element;
import com.google.gwt.junit.DoNotRunWith;
import com.google.gwt.junit.Platform;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.query.client.js.JsUtils;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

/**
 * Test class for testing jsinterop
 */
public class GQueryJsInteropTestGwt extends GWTTestCase {

  static Element e = null;
  static HTML testPanel = null;

  public String getModuleName() {
    return "com.google.gwt.query.QueryTest";
  }

  public void gwtTearDown() {
    $(e).remove();
    e = null;
  }

  public void gwtSetUp() {
    if (e == null || DOM.getElementById("core-tst") == null) {
      testPanel = new HTML();
      RootPanel.get().add(testPanel);
      e = testPanel.getElement();
      e.setId("core-tst");
    } else {
      e.setInnerHTML("");
    }
  }

  @JsType(name = "Window")
  public interface HTMLWindow {
      @JsProperty
      String getName();
      @JsProperty void setName(String name);
  }

  @JsType(name = "Window")
  public interface HWindow {
      @JsProperty(name = "document") HDocument document();
  }

  @JsType(name = "HTMLDocument")
  public interface HDocument {
      HElement createElement(String tag);
  }
  
  @JsType(name = "HTMLElement")
  public interface HElement {
      @JsProperty(name = "id") void id(String s);
  }
  
  // jsInterop only works in prod mode.
  @DoNotRunWith({Platform.HtmlUnitUnknown, Platform.Devel})
  public void testJsType() {
    JavaScriptObject jsw = ScriptInjector.TOP_WINDOW;

    // FIXME: this might not be necessary when GWT issue #9059 is fixed 
    HWindow w = JsUtils.cast(jsw);
    assertNotNull(w);

    HDocument d = w.document();
    assertNotNull(d);
    
    HElement h = d.createElement("div");
    h.id("foo");
    
    // GQuery must support native elements created through jsInterop interfaces
    $(h).appendTo(e);
    
    GQuery f = $("#foo", e);
    assertEquals(1, f.size());
  }

}
