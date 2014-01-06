/*
 * Copyright 2013, The gwtquery team.
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
package com.google.gwt.query.client.dbinding;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQ;
import com.google.gwt.query.client.builders.JsonBuilder;
import com.google.gwt.query.client.builders.Name;

/**
 * Tests for Deferred which can run either in JVM and GWT
 */
public class DataBindingTestJre extends GWTTestCase {

  public String getModuleName() {
    return null;
  }

  public interface Item extends JsonBuilder {
    Date getDate();
    void setDate(Date d);
  }

  public interface JsonExample extends JsonBuilder {
    int getA();
    JsonExample getB();
    @Name("M")
    int getM();
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
    String y();
    void y(String s);
    Function getF();
    void setF(Function f);
  }

  boolean functionRun = false;
  public void testJsonBuilder() {
    String json = "{M:0, a:1, b:{a:2,b:{a:3}},u:url, d:'2','t':['hola','adios'], 'z': true, 'items':[{'date':100}]}";

    JsonExample c = GQ.create(JsonExample.class);
    assertEquals(0, c.getA());
    c.parse(json, true);

    assertEquals(0, c.getM());
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
    c.y("y");
    assertEquals("y", c.y());
    assertEquals(1, c.getItems().size());

    c.setF(new Function() {
      public void f() {
        functionRun = true;
      }
    });
    assertFalse(functionRun);
    c.getF().f();
    assertTrue(functionRun);

    Item i1 = GQ.create(Item.class);
    Item i2 = GQ.create(Item.class);
    i1.setDate(new Date(2000));
    i2.setDate(new Date(3000));
    Item[] items = new Item[]{i1, i2};
    c.setItems(Arrays.asList(items));
    assertEquals(2000l, c.getItems().get(0).getDate().getTime());
    assertEquals(3000l, c.getItems().get(1).getDate().getTime());

    assertFalse(c.toJson().startsWith("{\"jsonExample\":"));
    assertTrue(c.toJsonWithName().startsWith("{\"jsonExample\":"));
    assertTrue(c.toJson().contains("\"items\":[{\"date\":"));
    assertTrue(c.toQueryString().contains("t[]=bar"));
    assertTrue(c.toQueryString().contains("a=1"));
    assertTrue(c.toQueryString().contains("\"a\":2"));
    
    assertEquals(1, c.<Number>get("a").intValue());
  }
}