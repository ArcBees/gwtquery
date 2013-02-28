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

import com.google.gwt.junit.client.GWTTestCase;

/**
 * Test class for testing gwtquery-core api in JRE.
 */
public class JreQueryCoreTest extends GWTTestCase {

  public String getModuleName() {
    return null; //"com.google.gwt.query.Query";
  }

  public void testAssertHtmlEquals() {
    GQueryCoreTestGwt.assertHtmlEquals("<span>hello</span>",
        "<span $h=\"5\">hello</span>");
    GQueryCoreTestGwt.assertHtmlEquals("<p class=\"abc\">whatever</p> ",
        " <p class=abc added=\"null\">Whatever</p>");
  }

  public void testWrapPropertiesString() {
    assertEquals("{}", Properties
        .wrapPropertiesString(""));
    assertEquals("{}", Properties
        .wrapPropertiesString("({})"));
    assertEquals("{\"border\":\"1px solid black\"}", Properties
        .wrapPropertiesString("border:'1px solid black'"));
    assertEquals("{\"b\":\"a\",\"c\":1,\"d\":\"url(https://test.com)\",\"e\":null,\"f\":false}", Properties
        .wrapPropertiesString("b: 'a'; c: 1, /*gg: aadf*/d: url('https://test.com');,e:null,f:false"));
    assertEquals("{\"color\":\"rgb(0,0,139)\",\"background\":\"red\"}", Properties
        .wrapPropertiesString("color: 'rgb(0, 0,139)', background: red"));
    assertEquals("{\"width\":\"\",\"top\":\"\"}", Properties
        .wrapPropertiesString("width: '' ; top:'' ;"));
    assertEquals("{\"border-left\":\"solid\"}", Properties
        .wrapPropertiesString("border-left: solid,"));
    assertEquals("[{\"a\":1,\"b\":{\"a\":2,\"b\":{\"a\":3}},\"u\":\"url\",\"d\":2,\"t\":[\"hola\",\"adios\"],\"z\":true}]", Properties
        .wrapPropertiesString("[{a:1, b:{a:2,b:{a:3}},u:url, d:'2','t':['hola','adios'], 'z': true}]"));
    assertEquals("{\"$x\":22.60,\"$y\":\".0\",\"h\":\"#y\"}", Properties
        .wrapPropertiesString("$x:22.60,$y:.0,h:#y"));
  }

}
