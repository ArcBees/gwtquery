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

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.junit.DoNotRunWith;
import com.google.gwt.junit.Platform;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.query.client.builders.JsonBuilder;
import com.google.gwt.query.client.builders.Name;
import com.google.gwt.query.client.builders.XmlBuilder;
import com.google.gwt.query.client.plugins.ajax.Ajax;
import com.google.gwt.query.client.plugins.ajax.Ajax.Settings;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Test class for testing ajax stuff.
 */
public class GQueryAjaxTestGwt extends GWTTestCase {

  static Element e = null;

  static HTML testPanel = null;

  public String getModuleName() {
    return "com.google.gwt.query.Query";
  }

  public void gwtTearDown() {
    $(e).remove();
    e = null;
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
  
  interface Item extends JsonBuilder {
    Date getDate();
    void setDate(Date d);
  }
  
  interface JsonExample extends JsonBuilder {
    int getA();
    JsonExample getB();
    @Name("u")
    String getUrl();
    long getD();
    Boolean getZ();
    String[] getT();
    JsonExample setT(String[] strings);
    JsonExample setZ(Boolean b);
    JsonExample setD(long l);
    List<Item> getItems();
    void setItems(List<Item> a);
  }
  
  public void testJsonBuilder() {
    String json = "{a:1, b:{a:2,b:{a:3}},u:url, d:'2','t':['hola','adios'], 'z': true}";
    JsonExample c = GWT.create(JsonExample.class);
    assertEquals(0, c.getA());
    c.parse(json, true);
    assertEquals(1, c.getA());
    assertNotNull(c.getB());
    assertEquals(2, c.getB().getA());
    assertEquals(3, c.getB().getB().getA());
    assertTrue(c.getZ());
    assertEquals("hola", c.getT()[0]);
    assertEquals("adios", c.getT()[1]);
    assertEquals("url", c.getUrl());
    c.setT(new String[]{"foo", "bar"})
     .setZ(false).setD(1234);
    assertFalse(c.getZ());
    assertEquals("foo", c.getT()[0]);
    assertEquals("bar", c.getT()[1]);
    assertEquals(1234l, c.getD());
    
    Item i1 = GWT.create(Item.class);
    Item i2 = GWT.create(Item.class);
    i1.setDate(new Date(2000));
    i2.setDate(new Date(3000));
    Item[] items = new Item[]{i1, i2};
    c.setItems(Arrays.asList(items));
    assertEquals(2000l, c.getItems().get(0).getDate().getTime());
    assertEquals(3000l, c.getItems().get(1).getDate().getTime());
    String s = "{\"a\":1,\"b\":{\"a\":2,\"b\":{\"a\":3}},\"u\":\"url\",\"d\":1234,\"t\":[\"foo\",\"bar\"],\"z\":false,\"items\":[{\"date\":2000},{\"date\":3000}]}";
    assertEquals(s, c.toString());
  }
  
  interface XmlExample extends XmlBuilder {
    String getA();
    Boolean getB();
    @Name("c")
    int getNumber();
    
    XmlExample[] getX();
    @Name("x")
    XmlExample getFirstX();
    
    XmlExample setA(String s);
    @Name("c")
    XmlExample setNumber(int i);
  }
  
  public void testXmlBuilder() {
    String xml = "<a a='ra' b='true' c='-1.48'><x a='xa1'/> <x a='xa2'/> text</a>";
    XmlExample x = GWT.create(XmlExample.class);
    x.parse(xml);
    assertTrue(x.getB());
    assertEquals("ra", x.getA());
    assertEquals(-1, x.getNumber());
    assertEquals("xa2", x.getX()[1].getA());
    assertEquals("xa1", x.getFirstX().getA());
    x.setA("X").setNumber(1234);
    assertEquals("X", x.getA());
    assertEquals(1234, x.getNumber());
  }
  
  public void testJsonValidService() {
    delayTestFinish(5000);
    // Use a public json service
    String testJsonpUrl = "https://www.googleapis.com/blogger/v2/blogs/user_id/posts/post_id?callback=?&key=NO-KEY";
    Ajax.getJSONP(testJsonpUrl, new Function(){
      public void f() {
        Properties p = getDataProperties();
        // It should return error since we do not use a valid key
        // {"error":{"errors":[{"domain":"usageLimits","reason":"keyInvalid","message":"Bad Request"}],"code":400,"message":"Bad Request"}}
        assertEquals(400, p.getJavaScriptObject("error").<Properties>cast().getInt("code"));
        finishTest();
      }
    }, null, 0);
  }

  @DoNotRunWith({Platform.HtmlUnitLayout})
  public void testJsonNonCallbackResponse() {
    delayTestFinish(5000);
    String testJsonpUrl = "http://www.google.com";
    Ajax.getJSONP(testJsonpUrl, null, new Function(){
      public void f() {
        Properties p = getDataProperties();
        assertNull(p);
        finishTest();
      }
    }, 500);
  }
  
  public void testJsonTimeout() {
    delayTestFinish(5000);
    String nonJsonpUrl = "http://www.google.com/nopage";
    
    Settings s = Ajax.createSettings();
    s.setTimeout(400);
    s.setSuccess(new Function(){
      public void f() {
        fail();
      }
    });
    s.setError(new Function(){
      public void f() {
        finishTest();
      }
    });
    s.setDataType("jsonp");
    s.setUrl(nonJsonpUrl);
    
    Ajax.ajax(s);
  }
    
}
