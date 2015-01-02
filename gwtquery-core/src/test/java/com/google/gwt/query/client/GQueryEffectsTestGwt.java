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

import static com.google.gwt.query.client.GQuery.*;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.junit.DoNotRunWith;
import com.google.gwt.junit.Platform;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.query.client.GQuery.Offset;
import com.google.gwt.query.client.plugins.effects.Fx;
import com.google.gwt.query.client.plugins.effects.Fx.ColorFx;
import com.google.gwt.query.client.plugins.effects.Fx.TransitFx;
import com.google.gwt.query.client.plugins.effects.PropertiesAnimation;
import com.google.gwt.query.client.plugins.effects.PropertiesAnimation.EasingCurve;
import com.google.gwt.query.client.plugins.effects.TransitionsAnimation;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Test class for testing gwtquery effects plugin api.
 */
public class GQueryEffectsTestGwt extends GWTTestCase {

  static Element e = null;

  static HTML testPanel = null;

  public String getModuleName() {
    return "com.google.gwt.query.QueryTest";
  }

  public void gwtTearDown() {
    $(e).remove();
    e = null;
  }

  public void gwtSetUp() {
    Fx.css3 = false;
    if (e == null) {
      testPanel = new HTML();
      RootPanel.get().add(testPanel);
      e = testPanel.getElement();
      e.setId("effects-tst");
    } else {
      e.setInnerHTML("");
    }
  }

  // Fails in TC windows, pass in Jenkins Linux
  @DoNotRunWith(Platform.HtmlUnitLayout)
  public void testClipAnimation() {
    $(e).html("<p id='idtest'>Content 1</p></p>");

    final GQuery g = $("#idtest");
    final int duration = 1000;

    // Clip effect places a relative div in the position of the original element
    // So check that there is not any div.
    GQuery back = $("div", e);
    assertEquals(0, back.size());

    // Configure the max duration for this test
    delayTestFinish(duration * 3);

    // each timer calls the next one
    final Timer endTimer = new Timer() {
      public void run() {
        // Check that the back div has been removed
        GQuery back = $("div", e);
        assertEquals(0, back.size());
        // Check that the attribute clip has been removed
        assertTrue(g.css("clip").matches("(|auto)"));
        finishTest();
      }
    };

    Scheduler.get().scheduleFixedPeriod(new RepeatingCommand() {
      int c = 0;
      // Run until we detect the rect has been changed or timeout
      public boolean execute() {
        String re = "rect\\(\\d+px[, ]+\\d+px[, ]+\\d+px[, ]+\\d+px\\)";
        if (g.css("clip").matches(re)) {
          endTimer.schedule(duration);
          return false;
        }
        if (duration < (c += 10))  {
          fail(g.css("clip") + " does not matched " + re);
          return false;
        }
        return true;
      }
    }, 100);

    g.as(Effects).clipDisappear(duration);

    // Check that the back div has been created
    back = $("div", e);
    assertEquals(1, back.size());
  }

  // Fails in TC windows, pass in Jenkins Linux
  // FIXME: Also timer 3 fails in real browsers (chrome).
  @DoNotRunWith(Platform.HtmlUnitLayout)
  public void testEffectsShouldBeQueued() {
    $(e).html("<p id='idtest'>Content 1</p></p>");

    final GQuery g = $("#idtest").css("position", "absolute");
    final Offset o = g.offset();

    final int duration = 1000;
    g.as(Effects).
        animate($$("left: '+=100'"), duration, EasingCurve.linear).
        animate($$("top: '+=100'"), duration, EasingCurve.linear).
        animate($$("left: '-=100'"), duration, EasingCurve.linear).
        animate($$("top: '-=100'"), duration, EasingCurve.linear);

    // Configure the max duration for this test
    delayTestFinish(duration * 4);

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
        timer1.schedule(duration);
      }
    };
    final Timer timer3 = new Timer() {
      public void run() {
        assertPosition(g, o.add(100, 1), o.add(100, 99));
        timer2.schedule(duration);
      }
    };
    final Timer timer4 = new Timer() {
      public void run() {
        assertPosition(g, o.add(1, 0), o.add(99, 0));
        timer3.schedule(duration);
      }
    };
    // Start the first timer
    timer4.schedule(duration/2);
  }

  // Fails in TC windows, pass in Jenkins Linux
  @DoNotRunWith(Platform.HtmlUnitLayout)
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
        double o = Double.valueOf(sectA.css("opacity", false));
        assertTrue(
            "'sectA' opacity must be in the interval 0-0.5 but is: " + o, o > 0
                && o < 0.5);
        o = Double.valueOf(sectB.css("opacity", false));
        assertTrue(
            "'sectB' opacity must be in the interval 0.5-1 but is: " + o,
            o > 0.5 && o < 1);
      }
    };
    Timer timerMidTime = new Timer() {
      public void run() {
        assertEquals("inline", sectA.css("display", false));
        assertEquals("", sectB.css("display", false));
        double o = Double.valueOf(sectA.css("opacity", false));
        assertTrue(
            "'sectA' opacity must be in the interval 0.5-1 but is: " + o,
            o > 0.5 && o < 1);
        o = Double.valueOf(sectB.css("opacity", false));
        assertTrue(
            "'sectB' opacity must be in the interval 0-0.5 but is: " + o, o > 0
                && o < 0.5);
      }
    };
    Timer timerLongTime = new Timer() {
      public void run() {
        assertEquals("inline", sectA.css("display", false));
        assertEquals("none", sectB.css("display", false));
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

  public void testPropertiesAnimationComputeEffects() {
    $(e)
        .html(
            "<div id='parent' style='background-color: yellow; width: 100px; height: 200px; top:130px; position: absolute; left: 130px'><p id='child' style='background-color: pink; width: 100px; height: 100px; position: absolute; padding: 5px; margin: 0px'>Content 1</p></div>");
    GQuery g = $("#child");
    Properties prop1;

    assertEquals("cssprop=marginTop value=-110px start=0 end=-110 unit=px",
        PropertiesAnimation.computeFxProp(g.get(0), "marginTop", "-110px",
            false).toString());
    assertEquals("cssprop=marginLeft value=-110px start=0 end=-110 unit=px",
        PropertiesAnimation.computeFxProp(g.get(0), "marginLeft", "-110px",
            false).toString());
    assertEquals("cssprop=top value=50% start=0 end=50 unit=%",
        PropertiesAnimation.computeFxProp(g.get(0), "top", "50%", false)
            .toString());
    assertEquals("cssprop=left value=50% start=0 end=50 unit=%",
        PropertiesAnimation.computeFxProp(g.get(0), "left", "50%", false)
            .toString());
    assertEquals("cssprop=width value=174px start=100 end=174 unit=px",
        PropertiesAnimation.computeFxProp(g.get(0), "width", "174px", false)
            .toString());
    assertEquals("cssprop=height value=174px start=100 end=174 unit=px",
        PropertiesAnimation.computeFxProp(g.get(0), "height", "174px", false)
            .toString());
    assertEquals("cssprop=padding value=20px start=5 end=20 unit=px",
        PropertiesAnimation.computeFxProp(g.get(0), "padding", "20px", false)
            .toString());

    prop1 = GQuery.$$("marginTop: '-110px', marginLeft: '-110px', top: '50%', left: '50%', width: '174px', height: '174px', padding: '20px'");
    PropertiesAnimation an = new PropertiesAnimation(EasingCurve.swing, g.get(0), prop1);
    an.onStart();
    an.onComplete();

    assertEquals("cssprop=marginTop value=0 start=-110 end=0 unit=px",
        PropertiesAnimation.computeFxProp(g.get(0), "marginTop", "0", false)
            .toString());
    assertEquals("cssprop=marginLeft value=0 start=-110 end=0 unit=px",
        PropertiesAnimation.computeFxProp(g.get(0), "marginLeft", "0", false)
            .toString());
    assertEquals("cssprop=top value=0% start=50 end=0 unit=%", PropertiesAnimation
        .computeFxProp(g.get(0), "top", "0%", false).toString());
    assertEquals("cssprop=left value=0% start=50 end=0 unit=%",
        PropertiesAnimation.computeFxProp(g.get(0), "left", "0%", false)
            .toString());
    assertEquals("cssprop=width value=100px start=174 end=100 unit=px",
        PropertiesAnimation.computeFxProp(g.get(0), "width", "100px", false)
            .toString());
    assertEquals("cssprop=height value=100px start=174 end=100 unit=px",
        PropertiesAnimation.computeFxProp(g.get(0), "height", "100px", false)
            .toString());
    assertEquals("cssprop=padding value=5px start=20 end=5 unit=px",
        PropertiesAnimation.computeFxProp(g.get(0), "padding", "5px", false)
            .toString());

    prop1 = GQuery.$$("marginTop: '0', marginLeft: '0', top: '0%', left: '0%', width: '100px', height: '100px', padding: '5px'");
    an = new PropertiesAnimation(EasingCurve.swing, g.get(0), prop1);
    an.onStart();
    an.onComplete();

    assertEquals("cssprop=marginTop value=-110px start=0 end=-110 unit=px",
        PropertiesAnimation.computeFxProp(g.get(0), "marginTop", "-110px",
            false).toString());
    assertEquals("cssprop=marginLeft value=-110px start=0 end=-110 unit=px",
        PropertiesAnimation.computeFxProp(g.get(0), "marginLeft", "-110px",
            false).toString());
    assertEquals("cssprop=top value=50% start=0 end=50 unit=%",
        PropertiesAnimation.computeFxProp(g.get(0), "top", "50%", false)
            .toString());
    assertEquals("cssprop=left value=50% start=0 end=50 unit=%",
        PropertiesAnimation.computeFxProp(g.get(0), "left", "50%", false)
            .toString());
    assertEquals("cssprop=width value=174px start=100 end=174 unit=px",
        PropertiesAnimation.computeFxProp(g.get(0), "width", "174px", false)
            .toString());
    assertEquals("cssprop=height value=174px start=100 end=174 unit=px",
        PropertiesAnimation.computeFxProp(g.get(0), "height", "174px", false)
            .toString());
    assertEquals("cssprop=padding value=20px start=5 end=20 unit=px",
        PropertiesAnimation.computeFxProp(g.get(0), "padding", "20px", false)
            .toString());
  }

  public void testColorEffectParsing(){
    String html = "<div id='test' style='color: #112233'>Test</div>";
    $(e).html(html);

    ColorFx effect = (ColorFx) PropertiesAnimation.computeFxProp($("#test",e).get(0), "color", "#ffffff", false);
    assertEquals(17, effect.getStartColor()[0]); //#11
    assertEquals(34, effect.getStartColor()[1]); //#22
    assertEquals(51, effect.getStartColor()[2]); //#33

    assertEquals(255, effect.getEndColor()[0]);
    assertEquals(255, effect.getEndColor()[1]);
    assertEquals(255, effect.getEndColor()[2]);

    effect = (ColorFx) PropertiesAnimation.computeFxProp(e, "color", "rgb(255,255,255)", false);
    assertEquals(255, effect.getEndColor()[0]);
    assertEquals(255, effect.getEndColor()[1]);
    assertEquals(255, effect.getEndColor()[2]);

    effect = (ColorFx) PropertiesAnimation.computeFxProp(e, "color", "rgb(100%, 100%, 100%)", false);
    assertEquals(255, effect.getEndColor()[0]);
    assertEquals(255, effect.getEndColor()[1]);
    assertEquals(255, effect.getEndColor()[2]);

    effect = (ColorFx) PropertiesAnimation.computeFxProp(e, "color", "white", false);
    assertEquals(255, effect.getEndColor()[0]);
    assertEquals(255, effect.getEndColor()[1]);
    assertEquals(255, effect.getEndColor()[2]);
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

  // Fails in TC windows, pass in Jenkins Linux
  @DoNotRunWith(Platform.HtmlUnitLayout)
  public void testAttrEffect() {
    $(e).html("<table border=1 id=idtest width=440><tr><td width=50%>A</td><td width=50%>B</td></tr></table>");

    final GQuery g = $("#idtest").css("position", "absolute");
    final int duration = 500;

    assertEquals("cssprop=$width attr=width value=+=100 start=440 end=540 unit=",
        PropertiesAnimation.computeFxProp(g.get(0), "$width", "+=100", false).toString());

    delayTestFinish(duration * 3);

    g.as(Effects).
        animate($$("$width: +=100; $border: +=4"), duration, EasingCurve.linear);

    final Timer timer = new Timer() {
      public void run() {
        assertEquals(540.0, Double.parseDouble(g.attr("width")));
        assertEquals(5.0, Double.parseDouble(g.attr("border")));
        finishTest();
      }
    };
    timer.schedule(duration * 2);
  }

  public void testComputeFxPropTransitions() {
    $(e).html("<div id='idtest' style='width: 200px; height 200px; border: solid 1px; position: absolute' ></div>");
    final GQuery g = $("#idtest", e);

    TransitFx fx = (TransitFx)TransitionsAnimation.computeFxProp(g.get(0), "width", "+=100", false);
    assertEquals("200", fx.transitStart.replace(".0",""));
    assertEquals("300", fx.transitEnd.replace(".0",""));
    assertEquals("px", fx.unit);
  }

  public void testStop() {
    $(e)
    .html(
        "<p id='id1' style='display: inline'>Content 1</p><p id='id2'>Content 2</p><p id='id3'>Content 3</p>");

    final GQuery sectA = $("#id1");
    final GQuery sectB = $("#id2");
    final GQuery sectC = $("#id2");

    sectA.hide();
    sectA.fadeIn(2000);
    sectB.fadeOut(2000).delay(500).fadeIn(1000);
    sectC.fadeOut(2000).delay(500).fadeIn(1000);

    // Call stop
    Timer timerMidTime = new Timer() {
      public void run() {
        sectA.stop();
        sectB.stop(true, true);
        sectC.stop(false, false);

        Double o  = Double.valueOf(sectA.css("opacity"));
        sectA.data("opacityA", o);
        assertTrue(
            "'sectA' opacity must be in the interval 0.5-1 but is: " + o,
            o > 0.5 && o < 1);

        //animation should jump to the end
        assertEquals("none", sectB.css("display"));

        o = Double.valueOf(sectC.css("opacity"));
        sectC.data("opacityC", o);
        assertTrue(
            "'sectC' opacity must be in the interval 0-0.5 but is: " + o,
            o > 0 && o < 0.5);
      }
    };


    Timer timerLongTime = new Timer() {
      public void run() {
        Double midAOpacity = sectA.data("opacityA", Double.class);
        //animation was stopped, opacity should not change
        assertEquals(midAOpacity, Double.valueOf(sectA.css("opacity")));
        //animation was stopped and jumped to the end, the queue was cleared so no change too.
        assertEquals("none", sectB.css("display"));

        //fadeOut was stopped but fadeIn should continue
        Double midCOpacity = sectC.data("opacityC", Double.class);
        Double laterCOpacity = Double.valueOf(sectC.css("opacity"));
        assertTrue(laterCOpacity > midCOpacity);
        // Last delayed assertion has to stop the test to avoid a timeout
        // failure
        finishTest();
      }
    };
    // schedule timer
    timerMidTime.schedule(1200);
    // schedule timer
    timerLongTime.schedule(2200);

  }

  // This test is used to demonstrate the issue, dont run it normally to avoid
  // problems during the testing phase
  int animationRunCounter = 0;
  public void ignore_testQueuesAndDataLeaks_issue132() {

    final Widget w = new Label("some animation");
    w.setVisible(false);
    RootPanel.get().add(w);
    w.getElement().setId("e");
    GQuery g = $(w);

    int test_duration = 1000;
    int fx_duration = 200;
    final int loops = test_duration / fx_duration;

    // Queue a set of effects which will use the data cache
    for (int i = 0; i < loops ; i++) {
      final char[] bulk = new char[5*1024*1024]; // let's leak 5MBs
      g.fadeToggle(fx_duration, new Function() {
        public void f() {
          animationRunCounter ++;
          bulk[0] = 0; // we keep it in handler
        }
      });
    }

    // Testing delay as well
    g.delay(fx_duration, new Function(){
      public void f() {
        animationRunCounter ++;
      }
    });

    // We do the assertions after all effects have been run
    g.queue(new Function() {
      public void f() {
        // after running queue method it is mandatory to call dequeue,
        // otherwise the queue get stuck
        $(this).dequeue();
        // Check that all animations and the delayed function has been run
        assertEquals(loops + 1, animationRunCounter);

        // Check that nothings is left in the dataCache object
        assertEquals(0, GQuery.dataCache.length());

        // Check that getting queue size does not initialize the data
        // object for this object
        assertEquals(0, $(this).queue());
        assertEquals(0, GQuery.dataCache.length());

        // Mark the test as success and stop delay timer
        finishTest();
      };
    });

    // delay the test enough to run all animations
    delayTestFinish(test_duration * 2);
  }
}
