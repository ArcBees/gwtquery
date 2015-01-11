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

import static com.google.gwt.query.client.GQuery.$;
import static com.google.gwt.query.client.GQuery.$$;
import static com.google.gwt.query.client.GQuery.Effects;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.junit.DoNotRunWith;
import com.google.gwt.junit.Platform;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.query.client.GQuery.Offset;
import com.google.gwt.query.client.plugins.Effects.GQAnimation;
import com.google.gwt.query.client.plugins.effects.Fx.ColorFx;
import com.google.gwt.query.client.plugins.effects.Fx.TransitFx;
import com.google.gwt.query.client.plugins.effects.PropertiesAnimation;
import com.google.gwt.query.client.plugins.effects.PropertiesAnimation.EasingCurve;
import com.google.gwt.query.client.plugins.effects.Transform;
import com.google.gwt.query.client.plugins.effects.Transitions;
import com.google.gwt.query.client.plugins.effects.TransitionsAnimation;
import com.google.gwt.query.client.plugins.effects.TransitionsAnimation.TransitionsClipAnimation;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.Arrays;
import java.util.List;

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
    assertEquals("cssprop=opacity value=show start=0 end=1 unit=",
        PropertiesAnimation.computeFxProp(g.get(0), "opacity", "toggle", true)
            .toString());
    assertEquals("cssprop=opacity value=hide start=1 end=0 unit=",
        PropertiesAnimation.computeFxProp(g.get(0), "opacity", "toggle", false)
            .toString());

    prop1 = GQuery.$$("marginTop: '-110px', marginLeft: '-110px', top: '50%', left: '50%', width: '174px', height: '174px', padding: '20px'");
    GQAnimation an = new PropertiesAnimation().setEasing(EasingCurve.swing).setElement(g.get(0)).setProperties(prop1);
    an.run(0);

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
    an = new PropertiesAnimation().setEasing(EasingCurve.swing).setElement(g.get(0)).setProperties(prop1);
    an.run(0);

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

  private void assertTransitFx(TransitFx fx, String prop, String val, String unit, String start, String end) {
    assertEquals(prop, fx.cssprop);
    assertEquals(val, fx.value);
    assertEquals(unit, fx.unit);
    if (!start.contains(".")) {
      // discard decimals
      assertEquals(start, fx.transitStart.replaceAll("\\.\\d+([a-z%]*)$", "$1"));
    } else {
      assertEquals(start, fx.transitStart);
    }
    if (!end.contains(".")) {
      // discard decimals
      assertEquals(end, fx.transitEnd.replaceAll("\\.\\d+([a-z%]*)$", "$1"));
    } else {
      assertEquals(end, fx.transitEnd);
    }
  }

  public void testTransitionsCss() {
    $(e).html("<div>");
    Transitions t = $("div", e).as(Transitions.Transitions);

    t.css("transform", "scale(1,2) rotateX(5deg) x(7) y(8)");
    assertEquals("1,2", t.css("scale"));
    assertEquals("7px", t.css("x"));
    assertEquals("7px", t.css("translateX"));
    t.css("y", "8");
    assertEquals("8px", t.css("y"));
    assertEquals("8px", t.css("translateY"));
  }

  public void testTransitionsAnimationComputeEffects() {
    $(e)
        .html(
            "<div id='parent' style='background-color: yellow; width: 100px; height: 200px; top:130px; position: absolute; left: 130px'>"
            + "<p id='child' style='opacity: 0.7; background-color: pink; width: 100px; height: 100px; position: absolute; padding: 5px; margin: 0px'>Content 1</p></div>");
    GQuery g = $("#child");
    TransitFx f;

    f = TransitionsAnimation.computeFxProp(g.get(0), "rotateY", "90deg", false);
    assertTransitFx(f, "rotateY", "90deg", "deg", "0deg", "90deg");

    f = TransitionsAnimation.computeFxProp(g.get(0), "marginTop", "-110px", false);
    assertTransitFx(f, "marginTop", "-110px", "px", "0px", "-110px");

    f = TransitionsAnimation.computeFxProp(g.get(0), "opacity", "toggle", false);
    assertTransitFx(f, "opacity", "hide", "", "0.7", "0");

    f = TransitionsAnimation.computeFxProp(g.get(0), "scaleX", "show", true);
    assertTransitFx(f, "scaleX", "show", "", "0", "1");

    f = TransitionsAnimation.computeFxProp(g.get(0), "width", "toggle", false);
    assertTransitFx(f, "width", "hide", "px", "100px", "0px");

    f = TransitionsAnimation.computeFxProp(g.get(0), "width", "+=45", false);
    assertTransitFx(f, "width", "+=45", "px", "100px", "145px");

    f = TransitionsAnimation.computeFxProp(g.get(0), "width", "100%", false);
    assertTransitFx(f, "width", "100%", "%", "100px", "100%");
  }

  public void testTransformParser() {
    Transform.has3d = true;
    Transform t = new Transform("scaleZ(0.5) rotateZ(90deg) skewX(4) rotateY(45) scale(1, 1) x(10) y(12) z(14) matrix(1, 2,3 ,4)");
    List<String> vals = Arrays.asList(t.toString().split(" "));
    // scale(1,1) matrix(1,2,3,4) rotateZ(90deg) translateZ(14px) rotateY(45deg) translateY(12px) skewX(4deg) translateX(10px) scaleZ(0.5)
    assertEquals(9, vals.size());
    assertTrue(vals.contains("scaleZ(0.5)"));
    assertTrue(vals.contains("rotateZ(90deg)"));
    assertTrue(vals.contains("rotateY(45deg)"));
    assertTrue(vals.contains("skewX(4deg)"));
    assertTrue(vals.contains("rotateY(45deg)"));
    assertTrue(vals.contains("scale(1,1)"));
    assertTrue(vals.contains("translateX(10px)"));
    assertTrue(vals.contains("translateY(12px)"));
    assertTrue(vals.contains("translateZ(14px)"));
    assertTrue(vals.contains("matrix(1,2,3,4)"));

    Transform.has3d = false;
    t = new Transform("scaleZ(0.5) rotateZ(90deg) skewX(4) rotateY(45) scale(1, 1) x(10) y(12) z(14) matrix(1, 2,3 ,4)");
    vals = Arrays.asList(t.toString().split(" "));
    // scale(1,1) matrix(1,2,3,4) translateY(12px) skewX(4deg) translateX(10px)
    assertEquals(5, vals.size());
    assertTrue(vals.contains("scale(1,1)"));
    assertTrue(vals.contains("skewX(4deg)"));
    assertTrue(vals.contains("translateX(10px)"));
    assertTrue(vals.contains("translateY(12px)"));
    assertTrue(vals.contains("matrix(1,2,3,4)"));
  }

  public void testTransitionsAnimation() {
    final GQuery m = $("<div style='top: 10px; width:50px'>foo</div>").appendTo(e);

    TransitionsClipAnimation a = new TransitionsClipAnimation();
    a.setElement(m.get(0));
    a.setProperties($$("clip-action: show, clip-origin: top-right, scaleZ: 0.5, delay: 30, left: 100, top: +=50, rotateZ: 90, rotateY: 45deg, easing: custom, duration: 400"));
    a.onStart();

    Properties from = a.getFxProperties(true);
    Properties to = a.getFxProperties(false);

    // HTMLUnit and chrome return different decimal part
    assertEquals("0px", from.getStr("left").replace(".0", ""));
    assertEquals("100px", to.getStr("left").replace(".0", ""));
    assertEquals("10px", from.getStr("top").replace(".0", ""));
    assertEquals("60px", to.getStr("top").replace(".0", ""));
    assertEquals("0", from.getStr("rotateZ").replace(".0", ""));
    assertEquals("90", to.getStr("rotateZ").replace(".0", ""));
    assertEquals("0deg", from.getStr("rotateY").replace(".0", ""));
    assertEquals("45deg", to.getStr("rotateY").replace(".0", ""));
    assertEquals("0 0", from.getStr("scale").replace(".0", ""));
    assertEquals("1 1", to.getStr("scale"));
    assertNull(to.get("delay"));
    assertNull(to.get("easing"));

    // HTMLUnit and chrome return different values
    assertTrue(m.attr("style").contains("rigin: 100% 0%") || m.attr("style").contains("rigin: right top"));
    assertTrue(m.attr("style").contains("top: 10px"));

    a.run(1);
    assertTrue(m.attr("style").contains("rigin: 100% 0%") || m.attr("style").contains("rigin: right top"));
    assertTrue(m.attr("style").contains("top: 60px"));
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
