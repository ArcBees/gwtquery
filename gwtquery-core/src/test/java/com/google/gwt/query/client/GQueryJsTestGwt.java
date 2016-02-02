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

import static com.google.gwt.query.client.GQuery.*;

import com.google.gwt.dom.client.Element;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.query.client.js.JsCache;
import com.google.gwt.query.client.js.JsNodeArray;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Test class for testing js classes.
 */
public class GQueryJsTestGwt extends GWTTestCase {

  static Element e = null;

  static HTML testPanel = null;

  public String getModuleName() {
    return "com.google.gwt.query.QueryTest";
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

  public void gwtTearDown() {
    $(e).remove();
    e = null;
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
  
  public void testGetMethod() {
    JsCache j = GQuery.$$("bt: true, bf: false, dz: 0, dp: 1.2, dn: -2.3, st: 'foo', nl: null").cast();
    Boolean bt = j.get("bt");
    assertEquals(Boolean.TRUE, bt);
    Boolean bf = j.get("bf");
    assertEquals(Boolean.FALSE, bf);
    Double  dz = j.get("dz");
    assertEquals(0d, dz);
    Double  dp = j.get("dp");
    assertEquals(1.2, dp);
    Double  dn = j.get("dn");
    assertEquals(-2.3, dn);
    String st = j.get("st");
    assertEquals("foo", st);
    Object o = j.get("nl");
    assertNull(o);

    Integer i = j.get("dp", Integer.class);
    assertEquals(new Integer(1), i);
    Short s = j.get("dp", Short.class);
    assertEquals((short)1, (short)s);
    Long l = j.get("dp", Long.class);
    assertEquals(1l, (long)l);
    Byte b = j.get("dp", Byte.class);
    assertEquals((byte)1, (byte)b);
    
    j.put("me", j);
    JsCache r = j.get("me");
    assertEquals(j, r);
  }

  public void testChrome__gwt_ObjectId() {
    JsCache a = JsCache.create();
    assertEquals(0, a.length());
    assertEquals(0, a.keys().length);
    assertEquals(0, a.elements().length);

    a.put("obj", 21L);
    assertEquals(1, a.length());
    assertEquals(1, a.keys().length);
    assertEquals(1, a.elements().length);

    JsNodeArray n = JsNodeArray.create();
    assertEquals(0, n.getLength());
    assertEquals(0, n.<JsCache>cast().keys().length);
    assertEquals(0, n.elements().length);

    n.addNode($("<hr/>").get(0));
    assertEquals(1, n.getLength());
    assertEquals(1, n.<JsCache>cast().keys().length);
    assertEquals(1, n.elements().length);
  }

  public void testProperties() {
    Properties p = $$("b: 'a'; c: 1, /*gg: aadf*/d: url('https://test.com');");
    assertEquals(3, p.keys().length);
    assertEquals("url(https://test.com)", p.getStr("d"));

    p = $$("color: 'rgb(0, 0,139)', background: red");
    assertEquals(2, p.keys().length);
    assertEquals("rgb(0,0,139)", p.getStr("color"));

    p = $$("a: 1, b: 0.5, c: null, d: whatever, e: true, f: false");
    assertEquals(1, p.getInt("a"));
    assertEquals(0.5f, p.getFloat("b"));
    assertEquals("whatever", p.getStr("d"));
    assertNull(p.getStr("c"));
    assertNull(p.getStr("ccc"));
    assertTrue(p.getBoolean("e"));
    assertFalse(p.getBoolean("d"));
    assertFalse(p.getBoolean("f"));
    assertFalse(p.getBoolean("c"));
    assertTrue(p.defined("d"));
    p.remove("d");
    assertFalse(p.defined("d"));
  }
}
