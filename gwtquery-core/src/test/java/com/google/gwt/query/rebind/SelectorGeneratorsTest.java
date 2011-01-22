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
package com.google.gwt.query.rebind;

import java.util.ArrayList;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.query.client.impl.SelectorEngineCssToXPath.ReplaceCallback;

/**
 * Test class for selector generators.
 */
public class SelectorGeneratorsTest extends GWTTestCase {

  public String getModuleName() {
    return   null; 
  }

  public void testCss2Xpath() {
    SelectorGeneratorCssToXPath sel = new SelectorGeneratorCssToXPath();
    
    assertEquals(".//div[starts-with(@class,'exa') and (substring(@class,string-length(@class)-3)='mple')]", 
        sel.css2Xpath("div[class^=exa][class$=mple]"));
    assertEquals(".//div[not(contains(concat(' ',normalize-space(@class),' '),' example '))]",
        sel.css2Xpath("div:not(.example)"));
    
    assertEquals(".//p", 
        sel.css2Xpath("p:nth-child(n)"));
    assertEquals(".//p[(count(preceding-sibling::*) + 1) mod 2=1]", 
        sel.css2Xpath("p:nth-child(odd)"));
    assertEquals(".//*[(position()-0) mod 2=0 and position()>=0]/self::p", 
        sel.css2Xpath("p:nth-child(2n)"));
    
    assertEquals(".//div[substring(@class,string-length(@class)-3)='mple']", 
        sel.css2Xpath("div[class$=mple]"));
    assertEquals(".//div[substring(@class,string-length(@class)-5)='xample']", 
        sel.css2Xpath("div[class$=xample]"));
    
    assertEquals(".//div[not(contains(concat(' ',normalize-space(@class),' '),' example '))]", 
        sel.css2Xpath("div:not(.example)"));

    assertEquals(".//*", 
        sel.css2Xpath("*"));
    
    assertEquals(".//input[@checked='checked']", 
        sel.css2Xpath("input:checked"));

    assertEquals(".//*[@myAttr]", 
        sel.css2Xpath("[myAttr]"));

    assertEquals(".//tag[@myAttr='abcd']", 
        sel.css2Xpath("tag[myAttr=abcd]"));
    
    assertEquals(".//a[@href and (@lang) and (@class)]", 
        sel.css2Xpath("a[href][lang][class]"));
    
    assertEquals(".//*[@checked='checked']|*[not(@disabled)]|*[@disabled]", 
        sel.css2Xpath(":checked, :enabled, :disabled"));
    
  }

  public void testReplaceAll() {
    assertEquals("<img src=\"thumbs/01\"/> <img src=\"thumbs/03\"/>", SelectorGeneratorCssToXPath.replacer.replaceAll("/[thumb01]/ /[thumb03]/", "/\\[thumb(\\d+)\\]/", new ReplaceCallback() {
      public String foundMatch(ArrayList<String>s) {
        return "<img src=\"thumbs/" + s.get(1) + "\"/>";
      }
    }));
  }
}
