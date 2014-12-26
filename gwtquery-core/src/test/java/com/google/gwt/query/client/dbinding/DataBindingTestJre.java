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

import com.google.gwt.core.shared.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQ;
import com.google.gwt.query.client.IsProperties;
import com.google.gwt.query.client.builders.JsonBuilder;
import com.google.gwt.query.client.builders.Name;

import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * Tests for Deferred which can run either in JVM and GWT
 */
public class DataBindingTestJre extends GWTTestCase {

  public String getModuleName() {
    return null;
  }

  public void testPropertiesCreate() {
    IsProperties p1 = GQ.create();
    p1.set("a", "1");
    p1.set("b", 1);
    p1.set("c", "null");
    p1.set("d", null);
    p1.set("e", true);

    assertEquals("1", p1.get("a"));
    assertEquals(Double.valueOf(1), p1.get("b"));
    assertEquals("null", p1.get("c"));
    assertNull(p1.get("d"));
    assertTrue((Boolean)p1.get("e"));

    p1 = GQ.create(p1.toJson());

    assertEquals("1", p1.get("a"));
    assertEquals(Double.valueOf(1), p1.get("b"));
    assertEquals("null", p1.get("c"));
    assertNull(p1.get("d"));
  }

  public interface Item extends JsonBuilder {
    public static enum Type {BIG, SMALL}

    Date getDate();
    void setDate(Date d);
    Type getType();
    void setType(Type t);
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
    Item getI();
    void setI(Item i);
    String y();
    void y(String s);
    Function getF();
    void setF(Function f);
    String getN();
  }

  boolean functionRun = false;
  public void testJsonBuilder() {
    String json = "{n: null, M:0, a:1, b:{a:2,b:{a:3}},u:url, d:'2','t':['hola','adios'], 'z': true, 'items':[{'date':100}]}";

    JsonExample c = GQ.create(JsonExample.class);
    assertEquals(0, c.getA());
    c.parse(json, true);

    assertNull(c.getN());
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
    i1.setDate(new Date(2000));
    c.setI(i1);
    assertEquals(2000l, c.getI().getDate().getTime());

    Item i2 = GQ.create(Item.class);
    i2.setDate(new Date(3000));
    Item[] items = new Item[]{i1, i2};
    c.setItems(Arrays.asList(items));
    assertEquals(2000l, c.getItems().get(0).getDate().getTime());
    assertEquals(3000l, c.getItems().get(1).getDate().getTime());


    assertFalse(c.toJson().startsWith("{\"jsonExample\":"));
    assertTrue(c.toJsonWithName().startsWith("{\"jsonExample\":"));
    assertTrue(c.toJson().contains("\"items\":[{\"date\":"));
    assertTrue(c.toQueryString().replace("\"bar\"", "bar").contains("t[]=bar"));
    assertTrue(c.toQueryString().contains("a=1"));
    assertTrue(c.toQueryString().contains("\"a\":2"));

    assertEquals(1, c.<Number>get("a").intValue());
  }

  public interface GAddress extends JsonBuilder {
    String street();
    String city();
  }

  public interface GUser extends JsonBuilder {
    @Name("_id")
    String getId(); 
    
    int getAge();
    String getName();
    GAddress address();
  }

  public static final String JSON_USER_EXAMPLE = " { "
                                                 + "   '_id': 'aaabbbccc', "
                                                 + "   'email': 'foo@bar.com', "
                                                 + "   'age': 27, "
                                                 + "   'name': 'Foo Bar', "
                                                 + "   'address': {"
                                                 + "      'street': 'Street Foo N6', "
                                                 + "      'phone': '670'"
                                                 + "   }"
                                                 + "}";

  public void
  test_parse_json() {
    GUser entity = GQ.create(GUser.class);
    entity.parse(JSON_USER_EXAMPLE, true);

    assertNotNull(entity.get("email"));
    assertEquals("aaabbbccc", entity.getId());
    assertEquals(27, entity.getAge());
    assertEquals("Foo Bar", entity.getName());
    assertNotNull(entity.address());
    assertEquals("Street Foo N6", entity.address().street());
    assertNotNull(entity.address().get("phone"));
  }

  public void
  test_parse_strict_json() {
    GUser entity = GQ.create(GUser.class);
    entity.parse(JSON_USER_EXAMPLE, true);
    entity.strip();

    assertEquals("aaabbbccc", entity.getId());
    assertNull(entity.get("email"));
    assertEquals(27, entity.getAge());
    assertEquals("Foo Bar", entity.getName());
    assertNotNull(entity.address());
    assertEquals("Street Foo N6", entity.address().street());

    // Recursion not implemented in client side
    if (GWT.isScript()) {
      assertNull(entity.address().get("phone"));
    }
  }
}