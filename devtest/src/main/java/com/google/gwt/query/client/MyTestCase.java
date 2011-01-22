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
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Just a simple class to emulate JUnit TestCase.
 */
public class MyTestCase {

  static Element e = null;
  static HTML testPanel = null;

  public static void assertEquals(Object a, Object b) {
    check(a.equals(b), "assertEquals: expected=" + a + " actual=" + b);
  }
  
  public static void assertFalse(boolean b) {
    check(!b, "assertTrue: actual should be false but is true");
  }

  public static void assertNotNull(Object a) {
    check(a != null, "assertNotNull: actual object is null");
  }

  public static void assertTrue(boolean b) {
    check(b, "assertTrue: actual should be true but is false");
  }
  
  public static void assertTrue(String msg, boolean b) {
    check(b, msg);
  }
  
  public static void check(boolean condition, String message) {
    if (!condition) {
      RuntimeException ex = new RuntimeException(message);
      ex.printStackTrace();
      throw ex;
    }
  }
  
  protected static void assertHtmlEquals(Object expected, Object actual) {
    assertEquals(iExplorerFixHtml(expected), iExplorerFixHtml(actual));
  }
  
  protected static String iExplorerFixHtml(Object s) {
    return s.toString().trim().toLowerCase().replaceAll(
        "[\r\n]", "").replaceAll(
        " ([\\w]+)=[\"']([^\"']+)[\"']", " $1=$2").replaceAll(
        "\\s+\\$h=\"[^\"]+\"", "").replaceAll(
        " added=[^ >]+", "");
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
  
  protected static void assertArrayContains(Object result, Object... array) {
    assertArrayContains("", result, array);
  }
  
  protected static void assertArrayContains(String message, Object result, Object... array) {
    String values = "";
    boolean done = false;
    for (Object o : array) {
      values += o.toString() + " ";
      if (result.equals(o)) {
        done = true;
      }
    }
    message = message + ", value (" + result + ") not found in: " + values;
    assertTrue(message, done);
  }  
  
  private boolean testRunning = false;
  
  protected void delayTestFinish(int millis) {
    testRunning = true;
    new Timer(){
      public void run() {
        assertFalse(testRunning);
      }
    }.schedule(millis);
  }
  
  protected void finishTest() {
    testRunning = false;
  }
  
  
}
