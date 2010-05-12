/*
 * Copyright 2009 Google Inc.
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

import com.google.gwt.core.client.EntryPoint;

/**
 * This module is thought to emulate a test environment similar to
 * GWTTestCase, but running it in development mode.
 * 
 * The main goal of it is to execute tests in a faster way, because you only need
 * to push reload in your browser.
 * 
 * @author manolo
 * 
 */
public class DevTestRunner extends MyTestCase implements EntryPoint {

  public void onModuleLoad() {
    gwtSetUp();
    testDomManip();
  }

  public void testDomManip() {
    String content = "<span class='branchA'><span class='target'>branchA target</span></span>"
      + "<span class='branchB'><span class='target'>branchB target</span></span>";
    
    $(e).html("");
    $(e).append(content);
    assertEquals(4, $("span", e).size());
    assertEquals(2, $("span.target", e).size());
    assertHtmlEquals(content, $(e).html());
  }
  
}
