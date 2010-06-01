/*
 * Copyright 2010 Google Inc.
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
package com.google.gwt.query.client.impl;

import com.google.gwt.junit.client.GWTTestCase;

/**
 * Test for selector engine implementations
 */
public class SelectorEnginesTest extends GWTTestCase {
  
  public String getModuleName() {
    return "com.google.gwt.query.Query";
  }
  
  public void testCssToXpath() {
    SelectorEngineCssToXPath sel = new SelectorEngineCssToXPath();
    
    assertEquals("//div[starts-with(@class,'exa') and (substring(@class,string-length(@class)-3)='mple')]", 
        sel.css2Xpath("div[class^=exa][class$=mple]"));
    assertEquals("//div[not(contains(concat(' ',normalize-space(@class),' '),' example '))]",
        sel.css2Xpath("div:not(.example)"));
    
    assertEquals("//p", 
        sel.css2Xpath("p:nth-child(n)"));
    assertEquals("//p[(count(preceding-sibling::*) + 1) mod 2=1]", 
        sel.css2Xpath("p:nth-child(odd)"));
    assertEquals("//*[(position()-0) mod 2=0 and position()>=0]/self::p", 
        sel.css2Xpath("p:nth-child(2n)"));
    
    assertEquals("//div[substring(@class,string-length(@class)-3)='mple']", 
        sel.css2Xpath("div[class$=mple]"));
    assertEquals("//div[substring(@class,string-length(@class)-5)='xample']", 
        sel.css2Xpath("div[class$=xample]"));
    
    assertEquals("//div[not(contains(concat(' ',normalize-space(@class),' '),' example '))]", 
        sel.css2Xpath("div:not(.example)"));
    
  }


}
