/*
 *  Copyright 2009 Google Inc.
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

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

public class GQuerySelectorsTest extends GWTTestCase {

  public String getModuleName() {
    return "com.google.gwt.query.Query";
  }

  static Element e = null;
  static HTML testPanel = null;

  public void gwtSetUp() {
    if (e == null) {
      testPanel = new HTML();
      RootPanel.get().add(testPanel);
      e = testPanel.getElement();
      e.setId("select-tst");
    } else {
      e.setInnerHTML("");
    }
  }

  public interface TestSelectors extends Selectors {
    @Selector(".target")
    public GQuery target();

    @Selector(".branchA")
    public GQuery branchA();

    @Selector(".branchB")
    public GQuery branchB();
    
    @Selector(".target")
    public GQuery target(Node n);

    @Selector(".branchA")
    public GQuery branchA(Node n);

    @Selector(".branchB")
    public GQuery branchB(Node n);    
    
    @Selector("*:checked")
    public GQuery allChecked();
    
    @Selector("*:checked")
    public GQuery allChecked(Node n);
  }
  
  public void testSelectorsGeneratorNative() {
    $(e).html( "<input type='radio' name='n' value='v1'>1</input>"
             + "<input type='radio' name='n' value='v2' checked='checked'>2</input>");
    
    TestSelectors selectors = GWT.create(TestSelectors.class);
    assertEquals(1, selectors.allChecked().size());
  }

  public void testSelectorsWithContext() {
    $(e).append("<div class='branchA'><div class='target'>branchA target</div></div>"
        + "<div class='branchB'><div class='target'>branchB target</div></div>");

    TestSelectors selectors = GWT.create(TestSelectors.class);
    
    assertEquals(2, selectors.target().length());
    Element branchA = selectors.branchA().get(0);
    Element branchB = selectors.branchB().get(0);
    assertNotNull(selectors.branchA().get(0));
    assertNotNull(selectors.branchB().get(0));
    
    assertEquals(2, selectors.target(RootPanel.getBodyElement()).length());
    branchA = selectors.branchA(RootPanel.getBodyElement()).get(0);
    branchB = selectors.branchB(RootPanel.getBodyElement()).get(0);
    assertNotNull(branchA);
    assertNotNull(branchB);
    assertEquals("branchA target", selectors.target(branchA).text());
    assertEquals("branchB target", selectors.target(branchB).text());

    selectors.setRoot(branchA);
    assertEquals(1, selectors.target().length());
    assertEquals("branchA target", selectors.target().text());

    selectors.setRoot(branchB);
    assertEquals(1, selectors.target().length());
    assertEquals("branchB target", selectors.target().text());
  }
  
}
