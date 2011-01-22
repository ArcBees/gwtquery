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

import java.util.Hashtable;

import com.google.gwt.junit.client.GWTTestCase;

/**
 * Test class for testing gwtquery-core api in JRE.
 */
public class JreQueryCoreTest extends GWTTestCase {

  public String getModuleName() {
    return null;
  }

  public void testAssertHtmlEquals() {
    GQueryCoreTest.assertHtmlEquals("<span>hello</span>",
        "<span $h=\"5\">hello</span>");
    GQueryCoreTest.assertHtmlEquals("<p class=\"abc\">whatever</p> ",
        " <p class=abc added=\"null\">Whatever</p>");
  }

  public void testWrapPropertiesString() {
    assertEquals("({border:'1px solid black'})", Properties
        .wrapPropertiesString("border:'1px solid black'"));
    assertEquals("({border:'1px solid black'})", Properties
        .wrapPropertiesString("(border:'1px solid black')"));
    assertEquals("({border:'1px solid black'})", Properties
        .wrapPropertiesString("{border:'1px solid black'}"));
    assertEquals("({border:'1px solid black'})", Properties
        .wrapPropertiesString("{border:'1px solid black'}"));
    assertEquals("({border:'1px solid black'})", Properties
        .wrapPropertiesString("(border:'1px solid black')"));
    assertEquals("({border:'1px solid black'})", Properties
        .wrapPropertiesString("{(border:'1px solid black')}"));
    assertEquals("({border:'1px solid black'})", Properties
        .wrapPropertiesString("({border:'1px solid black'})"));
  }

}
