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

import com.google.gwt.dom.client.Element;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Test class for testing gwtquery-core api.
 */
public class GQueryEffectsTest extends GWTTestCase {

  static Element e = null;

  static HTML testPanel = null;

  public String getModuleName() {
    return "com.google.gwt.query.Query";
  }
  
  public void gwtSetUp() {
    if (e == null) {
      testPanel = new HTML();
      RootPanel.get().add(testPanel);
      e = testPanel.getElement();
      e.setId("effects-tst");
    } else {
      e.setInnerHTML("");
    }
  }

  public void testEffectsPlugin() {
    $(e).html(
        "<p id='id1'>Content 1</p><p id='id2'>Content 2</p><p id='id3'>Content 3</p>");

    final GQuery sectA = $("#id1");
    final GQuery sectB = $("#id2");
    final GQuery sectC = $("#id3");

    // hide()
    sectA.hide();
    assertEquals("none", sectA.css("display"));

    // show()
    sectB.show();
    assertEquals("", sectB.css("display"));

    // toggle()
    assertEquals("", sectC.css("display"));
    sectC.toggle();
    assertEquals("none", sectC.css("display"));
    sectC.toggle();
    assertEquals("", sectC.css("display"));

    // fadeIn() & fadeOut() are tested with delayed assertions
    sectA.fadeIn(2000);
    sectB.fadeOut(2000);

    // Configure the max duration for this test
    // If the test exceeds the timeout without calling finishTest() it will fail
    delayTestFinish(2500);

    // Delayed assertions at different intervals
    Timer timerShortTime = new Timer() {
      public void run() {
        double o = Double.valueOf(sectA.css("opacity"));
        assertTrue("'sectA' opacity must be in the interval 0-0.5 but is: " + o, o > 0 && o < 0.5);
        o = Double.valueOf(sectB.css("opacity"));
        assertTrue("'sectB' opacity must be in the interval 0.5-1 but is: " + o, o > 0.5 && o < 1);
      }
    };
    Timer timerMidTime = new Timer() {
      public void run() {
        assertEquals("", sectA.css("display"));
        assertEquals("", sectB.css("display"));
        double o = Double.valueOf(sectA.css("opacity"));
        assertTrue("'sectA' opacity must be in the interval 0.5-1 but is: " + o, o > 0.5 && o < 1);
        o = Double.valueOf(sectB.css("opacity"));
        assertTrue("'sectB' opacity must be in the interval 0-0.5 but is: " + o, o > 0 && o < 0.5);
      }
    };
    Timer timerLongTime = new Timer() {
      public void run() {
        assertEquals("", sectA.css("display"));
        assertEquals("none", sectB.css("display"));
        // Last delayed assertion has to stop the test to avoid a timeout failure
        finishTest();
      }
    };
    // schedule the delayed assertions
    timerShortTime.schedule(200);
    timerMidTime.schedule(1200);
    timerLongTime.schedule(2200);
  }

}
