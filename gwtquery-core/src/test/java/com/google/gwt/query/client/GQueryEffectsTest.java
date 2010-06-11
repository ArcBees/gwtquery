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
import static com.google.gwt.query.client.GQuery.$$;

import com.google.gwt.dom.client.Element;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.query.client.GQuery.Offset;
import com.google.gwt.query.client.plugins.Effects;
import com.google.gwt.query.client.plugins.PropertiesAnimation.Easing;
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

  public void testFade() {
    $(e)
    .html(
        "<p id='id1' style='display: inline'>Content 1</p><p id='id2'>Content 2</p><p id='id3'>Content 3</p>");

    final GQuery sectA = $("#id1");
    final GQuery sectB = $("#id2");
    
    // fadeIn() & fadeOut() are tested with delayed assertions
    sectA.hide();
    sectA.fadeIn(2000);
    sectB.fadeOut(2000);

    // Configure the max duration for this test
    // If the test exceeds the timeout without calling finishTest() it will fail
    delayTestFinish(2500);

    // Delayed assertions at different intervals
    Timer timerShortTime = new Timer() {
      public void run() {
        double o = Double.valueOf(sectA.css("opacity"));
        assertTrue(
            "'sectA' opacity must be in the interval 0-0.5 but is: " + o, o > 0
                && o < 0.5);
        o = Double.valueOf(sectB.css("opacity"));
        assertTrue(
            "'sectB' opacity must be in the interval 0.5-1 but is: " + o,
            o > 0.5 && o < 1);
      }
    };
    Timer timerMidTime = new Timer() {
      public void run() {
        assertEquals("inline", sectA.css("display"));
        assertEquals("", sectB.css("display"));
        double o = Double.valueOf(sectA.css("opacity"));
        assertTrue(
            "'sectA' opacity must be in the interval 0.5-1 but is: " + o,
            o > 0.5 && o < 1);
        o = Double.valueOf(sectB.css("opacity"));
        assertTrue(
            "'sectB' opacity must be in the interval 0-0.5 but is: " + o, o > 0
                && o < 0.5);
      }
    };
    Timer timerLongTime = new Timer() {
      public void run() {
        assertEquals("inline", sectA.css("display"));
        assertEquals("none", sectB.css("display"));
        // Last delayed assertion has to stop the test to avoid a timeout
        // failure
        finishTest();
      }
    };

    // schedule the delayed assertions
    timerShortTime.schedule(200);
    timerMidTime.schedule(1200);
    timerLongTime.schedule(2200);    
  }  

  public void testEffectsShouldBeQueued() {
    $(e).html("<p id='idtest'>Content 1</p></p>");

    final GQuery g = $("#idtest").css("position", "absolute");
    final Offset o = g.offset();
    g.as(Effects.Effects).
        animate($$("left: '+=100'"), 400, Easing.LINEAR).
        animate($$("top: '+=100'"), 400, Easing.LINEAR).
        animate($$("left: '-=100'"), 400, Easing.LINEAR).
        animate($$("top: '-=100'"), 400, Easing.LINEAR);
    
    // Configure the max duration for this test
    delayTestFinish(400 * 4);

    // each timer calls the next one
    final Timer timer1 = new Timer() {
      public void run() {
        assertPosition(g, o.add(0, 99), o.add(0, 1));
        // Last timer should finish the test
        finishTest();
      }
    };
    final Timer timer2 = new Timer() {
      public void run() {
        assertPosition(g, o.add(99, 100), o.add(1, 100));
        timer1.schedule(400);
      }
    };
    final Timer timer3 = new Timer() {
      public void run() {
        assertPosition(g, o.add(100, 1), o.add(100, 99));
        timer2.schedule(400);
      }
    };
    final Timer timer4 = new Timer() {
      public void run() {
        assertPosition(g, o.add(1, 0), o.add(99, 0));
        timer3.schedule(400);
      }
    };

    // Starts the first timer
    timer4.schedule(200);
  }

  private void assertPosition(GQuery g, Offset min, Offset max) {
    int a = Math.min(min.top, max.top);
    int b = Math.max(min.top, max.top);
    int v = g.offset().top;
    boolean c = a <= v && v <= b;
    String msg = "Top has the value " + v + ", but should be in the range: "
        + a + " - " + b;
    assertTrue(msg, c);

    a = Math.min(min.left, max.left);
    b = Math.max(min.left, max.left);
    v = g.offset().left;
    c = a <= v && v <= b;
    msg = "Left has the value " + v + ", but should be in the range: " + a
        + " - " + b;
    assertTrue(msg, c);
  }

}
