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

import com.google.gwt.dom.client.Element;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.query.client.js.JsCache;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Test class for testing js classes.
 */
public class GQueryJsTest extends GWTTestCase {

  static Element e = null;

  static HTML testPanel = null;

  public String getModuleName() {
    return "com.google.gwt.query.Query";
  }

  public void gwtSetUp() {
    if (e == null) {
      testPanel = new HTML();
      RootPanel.get().add(testPanel);
      e = testPanel.getElement();
      e.setId("core-tst");
    } else {
      e.setInnerHTML("");
    }
  }

  public void testJsCache() {
    String[] slist = new String[]{"A", "B", "C"};
    
    JsCache c = JsCache.create();
    assertTrue(c.isEmpty());
    for (int i=0; i < slist.length; i++) {
      c.put(i, slist[i]);
    }
    assertFalse(c.isEmpty());
    assertEquals(3, c.length());
    assertEquals(slist[1], c.get(1));
    for (int i=0; i < slist.length; i++) {
      c.put(slist[i], slist[i]);
    }
    assertEquals(6, c.length());
    assertEquals(slist[1], c.get(1));
    assertEquals(slist[1], c.get(slist[1]));
    c.put(1, null);
    c.put("X", "X");
    assertNull(c.get(1));
    assertEquals(slist[2], c.get(2));
    assertEquals(7, c.length());
    assertEquals(7, c.keys().length);
    assertEquals(7, c.elements().length);
    
    assertTrue(c.exists(2));
    assertFalse(c.exists(3));
    assertTrue(c.exists("X"));
    assertFalse(c.exists("V"));
    
    c.delete(2);
    c.delete("C");
    assertEquals(5, c.length());
    
    c.put(-1, "N");
    assertEquals(6, c.length());
    assertEquals("N", c.get(-1));
  }
}
