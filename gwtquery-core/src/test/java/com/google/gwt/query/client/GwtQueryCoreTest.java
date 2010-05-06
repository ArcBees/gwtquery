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
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Test class for testing gwtquery-core api.
 */
public class GwtQueryCoreTest extends GWTTestCase {

  static Element e = null;

  static HTML testPanel = null;

  protected static void assertHtmlEquals(Object expected, Object actual) {
    assertEquals(iExplorerFixHtml(expected), iExplorerFixHtml(actual));
  }

  protected static String iExplorerFixHtml(Object s) {
    // IE shows all tags upper-case
    // IE adds \r \n 
    // IE does not put quotes to some attributes
    // Investigate: IE in method find puts the attribute $h="4" 
    // Investigate: IE in method filter adds the attrib added="null"
    return s.toString().trim().toLowerCase().
        replaceAll("[\r\n]", "").
        replaceAll(" ([\\w]+)=[\"]([^\"]+)[\"]", " $1=$2").
        replaceAll("\\s+\\$h=\"[^\"]+\"", "").
        replaceAll(" added=[^ >]+", "");
  }

  public String getModuleName() {
    return "com.google.gwt.query.Query";
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

  public void testAttributeMethods() {

    $(e).html("<p class=\"a1\">Content</p>");
    GQuery gq = $("p", e);
    
    // attr()
    gq.attr($$("attr1: 'a', attr2: 'b'"));
    assertEquals("a", gq.attr("attr1"));
    assertEquals("b", gq.attr("attr2"));
    
    gq.attr("attr3", new Function() {
      public String f(Element e, int i) {
        return e.getInnerText();
      }
    });
    assertEquals("Content", gq.attr("attr3"));

    assertEquals("a1", gq.attr("class"));
    gq.attr("class", "b1 b2");

    // hasClass()
    assertTrue(gq.hasClass("b1"));
    assertTrue(gq.hasClass("b2"));

    // addClass()
    gq.addClass("c1", "c2");
    assertTrue(gq.hasClass("b1"));
    assertTrue(gq.hasClass("b2"));
    assertTrue(gq.hasClass("c1"));
    assertTrue(gq.hasClass("c2"));

    // removeClass()
    gq.removeClass("c2", "c1");
    assertTrue(gq.hasClass("b1"));
    assertTrue(gq.hasClass("b2"));
    assertFalse(gq.hasClass("c1"));
    assertFalse(gq.hasClass("c2"));
    
    // toggleClass()
    gq.toggleClass("b2");
    assertTrue(gq.hasClass("b1"));
    assertFalse(gq.hasClass("b2"));
    gq.toggleClass("b2");
    assertTrue(gq.hasClass("b1"));
    assertTrue(gq.hasClass("b2"));
    gq.toggleClass("b2", true);
    assertTrue(gq.hasClass("b2"));
    gq.toggleClass("b2", false);
    assertFalse(gq.hasClass("b2"));
    
    // css()
    String content = "<p style='color:red;'>Test Paragraph.</p>";
    $(e).html(content);
    assertEquals("red", $("p", e).css("color"));
    $("p", e).css("font-weight", "bold");
    assertEquals("bold", $("p", e).css("font-weight"));

    // css() properties
    $(e).html("<p>Test Paragraph.</p>");
    $("p", e).css(Properties.create(
        "color: 'red', 'font-weight': 'bold', background: 'blue'"));
    assertEquals("red", $("p", e).css("color"));
    assertEquals("bold", $("p", e).css("font-weight"));
    assertEquals("blue", $("p", e).css("background-color"));

    // css() camelize and uppercase
    $(e).html("<p>Test Paragraph.</p>");
    $("p", e).css(Properties.create("COLOR: 'red', 'FONT-WEIGHT': 'bold'"));
    assertEquals("red", $("p", e).css("color"));
    assertEquals("", $("p", e).css("background"));
  }
  
  public void testEach() {
    $(e).html("<p>Content 1</p><p>Content 2</p><p>Content 3</p>");
    $("p", e).each(new Function() {
      public void f(Element e) {
        $(e).text(".");
      }      
    });
    assertHtmlEquals("<p>.</p><p>.</p><p>.</p>", $("p", e));
    $("p", e).each(new Function() {
      public String f(Element e, int i) {
        $(e).text("" + i);
        return "";
      }      
    });
    assertHtmlEquals("<p>0</p><p>1</p><p>2</p>", $("p", e));
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

  public void testInnerMethods() {
    String txt = "<p>I would like to say: </p>";

    // Check that setHTML and getHTML work as GQuery html()
    testPanel.setHTML(txt);
    assertHtmlEquals(txt, testPanel.getHTML());
    assertHtmlEquals(txt, $(e).html());
    assertHtmlEquals(txt, $("#core-tst").html());
    $(e).html("");
    assertHtmlEquals("", $(e).html());
    $(e).html(txt);
    assertHtmlEquals(txt, $(e).html());

    // toString()
    assertHtmlEquals(txt, $("p", e));

    // remove()
    $("p", e).remove();
    assertHtmlEquals("", $(e).html());

    // text()
    String expected = "I would like to say: I would like to say:";
    $(e).html(txt + txt);
    assertHtmlEquals(expected, $("p", e).text());

    // empty()
    expected = "<p></p><p></p>";
    $("p", e).empty();
    assertHtmlEquals(expected, $(e).html());
  }

  public void testInputValueMethods() {
    // imput text
    $(e).html("<input type='text'/>");
    GQuery gq = $("input", e);
    assertEquals("", gq.val());
    gq.val("some value");
    assertEquals("some value", gq.val());

    // select
    $(e).html(
        "<select name='n'><option value='v1'>1</option><option value='v2' selected='selected'>2</option></select>");
    gq = $("select", e);
    assertEquals("v2", gq.val());
    gq.val("v1");
    assertEquals("v1", gq.val());

    // select multiple
    $(e).html(
        "<select name='n' multiple='multiple'><option value='v1'>1</option><option value='v2'>2</option><option value='v3'>3</option></select>");
    gq = $("select", e);
    assertEquals(0, gq.vals().length);
    assertEquals("", gq.val());
    
    $(e).html(
        "<select name='n' multiple='multiple'><option value='v1'>1</option><option value='v2' selected='selected'>2</option><option value='v3'>3</option></select>");
    gq = $("select", e);
    assertEquals(1, gq.vals().length);
    assertEquals("v2", gq.val());
    gq.val("v1", "v3", "invalid");
    assertEquals(2, gq.vals().length);
    assertEquals("v1", gq.vals()[0]);
    assertEquals("v3", gq.vals()[1]);
    // FIXME: fix in IE
//    gq.val("v1");
//    assertEquals(1, gq.vals().length);
//    assertEquals("v1", gq.val());

    // input radio
    $(e).html(
        "<input type='radio' name='n' value='v1'>1</input><input type='radio' name='n' value='v2' checked='checked'>2</input>");
    gq = $("input", e);
    assertEquals("v2", gq.val());
    gq.val("v1");
    assertEquals("v1", gq.val());
    gq.val("v2");
    assertEquals("v2", gq.val());

    // input checkbox
    $(e).html(
        "<input type='checkbox' name='n1' value='v1'>1</input><input type='checkbox' name='n2' value='v2' checked='checked'>2</input>");
    gq = $("input", e);
    assertEquals("", gq.val());
    gq.val("v1");
    assertEquals("v1", gq.val());
  }
  
  public void testModifyMethods() {
    String pTxt = "<p>I would like to say: </p>";
    String bTxt = "<b>Hello</b>";

    // append()
    String expected = "<p>I would like to say: <b>Hello</b></p>";
    $(e).html(pTxt);
    $("p", e).append(bTxt);
    assertHtmlEquals(expected, $(e).html());
    
    $(e).html(pTxt);
    $("p", e).append($(bTxt).get(0));
    assertHtmlEquals(expected, $(e).html());

    // appendTo()
    expected = "<p>I would like to say: <b>Hello</b></p>";
    $(e).html(bTxt + pTxt);
    $("b", e).appendTo($("p", e));
    assertHtmlEquals(expected, $(e).html());

    // prepend()
    expected = "<p><b>Hello</b>I would like to say: </p>";
    $(e).html(pTxt);
    $("p", e).prepend(bTxt);
    assertHtmlEquals(expected, $(e).html());

    // prependTo()
    expected = "<p><b>Hello</b>I would like to say: </p>";
    $(e).html(bTxt + pTxt);
    $("b", e).prependTo($("p", e));
    assertHtmlEquals(expected, $(e).html());

    // prependTo()
    expected = "<b>Hello</b><p><b>Hello</b>I would like to say: </p>";
    $(e).html(bTxt + pTxt);
    $("b", e).clone().prependTo($("p", e));
    assertHtmlEquals(expected, $(e).html());

    // before()
    expected = "<b>Hello</b><p>I would like to say: </p>";
    $(e).html(pTxt);
    $("p", e).before(bTxt);
    assertHtmlEquals(expected, $(e).html());

    // before()
    expected = "<b>Hello</b><p>I would like to say: </p>";
    $(e).html(pTxt + bTxt);
    $("p", e).before($("b", e));
    assertHtmlEquals(expected, $(e).html());

    // before()
    expected = "<b>Hello</b><p>I would like to say: </p><b>Hello</b>";
    $(e).html(pTxt + bTxt);
    $("p", e).before($("b", e).clone());
    assertHtmlEquals(expected, $(e).html());

    // insertBefore()
    expected = "<b>Hello</b><p>I would like to say: </p>";
    $(e).html(pTxt + bTxt);
    $("b", e).insertBefore($("p", e));
    assertHtmlEquals(expected, $(e).html());

    // insertBefore()
    expected = "<b>Hello</b><p>I would like to say: </p><b>Hello</b>";
    $(e).html(pTxt + bTxt);
    $("b", e).clone().insertBefore($("p", e));
    assertHtmlEquals(expected, $(e).html());

    // after()
    expected = "<p>I would like to say: </p><b>Hello</b>";
    $(e).html(pTxt);
    $("p", e).after(bTxt);
    assertHtmlEquals(expected, testPanel.getHTML());

    // after()
    expected = "<p>I would like to say: </p><b>Hello</b>";
    $(e).html(bTxt + pTxt);
    $("p", e).after($("b", e));
    assertHtmlEquals(expected, $(e).html());

    // after()
    expected = "<b>Hello</b><p>I would like to say: </p><b>Hello</b>";
    $(e).html(bTxt + pTxt);
    $("p", e).after($("b", e).clone().get(0));
    assertHtmlEquals(expected, $(e).html());
  }
  
  public void testProperties() {
    Properties p = $$("border:'1px solid black'");
    assertEquals(1, p.keys().length);
    assertNotNull(p.get("border"));
    
    p = $$("({border:'1px solid black'})");
    assertEquals(1, p.keys().length);
    assertNotNull(p.get("border"));
  }
  


  public void testRelativeMethods() {
    String content = "<p><span>Hello</span>, how are you?</p>";
    String expected = "<span>Hello</span>";

    // find()
    $(e).html(content);
    assertHtmlEquals(expected, $("p", e).find("span"));

    // filter()
    content = "<p>First</p><p class=\"selected\">Hello</p><p>How are you?</p>";
    $(e).html(content);
    expected = "<p class=\"selected\">Hello</p>";
    assertHtmlEquals(expected, $("p", e).filter(".selected"));

    // filter()
    // Commented because GQuery doesn't support this syntax yet
    // expected = "<p class=\"selected\">Hello</p>";
    // assertHtmlEquals(expected, $("p", e).filter(".selected, :first").toString());

    // not()
    expected = "<p>First</p><p>How are you?</p>";
    assertEquals(2, $("p", e).not(".selected").size());
    assertHtmlEquals(expected, $("p", e).not(".selected"));
    assertEquals(2, $("p", e).not($(".selected")).size());
    assertHtmlEquals(expected, $("p", e).not($(".selected")));
    assertEquals(2, $("p", e).not($(".selected").get(0)).size());
    assertHtmlEquals(expected, $("p", e).not($(".selected").get(0)));

    // add()
    String added = "<p>Last</p>";
    expected = content + added;
    assertEquals(4, $("p", e).add(added).size());
    assertHtmlEquals(expected, $("p", e).add(added));

    // parent()
    expected = content = "<div><p>Hello</p><p>Hello</p></div>";
    $(e).html(content);
    assertHtmlEquals(expected, $("p", e).parent());

    // parent()
    content
        = "<div><p>Hello</p></div><div class=\"selected\"><p>Hello Again</p></div>";
    expected = "<div class=\"selected\"><p>Hello Again</p></div>";
    $(e).html(content);
    assertHtmlEquals(expected, $("p", e).parent(".selected"));

    // parents()
    content = "<div><p><span>Hello</span></p><span>Hello Again</span></div>";
    $(e).html(content);
    assertEquals(2, $("span", e).size());
    assertTrue(3 < $("span", e).parents().size());
    assertEquals(1, $("span", e).parents().filter("body").size());
    $("span", e).parents().filter("body").toString().trim().toLowerCase().contains(content.toLowerCase());

    // is()
    content = "<form><input type=\"checkbox\"></form>";
    $(e).html(content);
    assertEquals(true, $("input[type=\"checkbox\"]", e).parent().is("form"));

    // is()
    content = "<form><p><input type=\"checkbox\"></p></form>";
    $(e).html(content);
    assertEquals(false, $("input[type='checkbox']", e).parent().is("form"));

    // next()
    content = "<p>Hello</p><p>Hello Again</p><div><span>And Again</span></div>";
    String next1 = "<p>Hello Again</p>";
    String next2 = "<div><span>And Again</span></div>";
    $(e).html(content);
    assertEquals(2, $("p", e).next().size());
    assertHtmlEquals(next1, $("p", e).next().get(0).getString());
    assertHtmlEquals(next2, $("p", e).next().get(1).getString());

    // next()
    content
        = "<p>Hello</p><p class=\"selected\">Hello Again</p><div><span>And Again</span></div>";
    expected = "<p class=\"selected\">Hello Again</p>";
    $(e).html(content);
    assertEquals(1, $("p", e).next(".selected").size());
    assertHtmlEquals(expected, $("p", e).next(".selected").get(0).getString());
    
    // nextAll()
    content = "<ul><li>i1</li><li>i2</li><li class='third-item'>i3</li><li>i4</li><li>i5</li></ul>";
    expected = "<li>i4</li><li>i5</li>";
    $(e).html(content);
    assertEquals(2, $("li.third-item", e).nextAll().size());
    assertHtmlEquals(expected, $("li.third-item", e).nextAll());
    
    // andSelf()
    content = "<ul><li>i1</li><li>i2</li><li class=\"third-item\">i3</li><li>i4</li><li>i5</li></ul>";
    expected = "<li>i4</li><li>i5</li><li class=\"third-item\">i3</li>";
    $(e).html(content);
    assertEquals(3, $("li.third-item", e).nextAll().andSelf().size());
    assertHtmlEquals(expected, $("li.third-item", e).nextAll().andSelf());

    // prev()
    content = "<p>Hello</p><div><span>Hello Again</span></div><p>And Again</p>";
    expected = "<div><span>Hello Again</span></div>";
    $(e).html(content);
    assertEquals(1, $("p", e).prev().size());
    assertHtmlEquals(expected, $("p", e).prev().get(0).getString());

    // prev()
    content
        = "<div><span>Hello</span></div><p class=\"selected\">Hello Again</p><p>And Again</p>";
    expected = "<p class=\"selected\">Hello Again</p>";
    $(e).html(content);
    assertEquals(2, $("p", e).prev().size());
    assertEquals(1, $("p", e).prev(".selected").size());
    assertHtmlEquals(expected, $("p", e).prev(".selected").get(0).getString());

    // siblings()
    content = "<p>Hello</p><div id='mdiv'><span>Hello Again</span></div><p>And Again</p>";
    next1 = "<p>Hello</p>";
    next2 = "<p>And Again</p>";
    $(e).html(content);
    assertEquals(2, $("#mdiv", e).siblings().size());
    assertHtmlEquals(next1, $("#mdiv", e).siblings().get(0).getString());
    assertHtmlEquals(next2, $("#mdiv", e).siblings().get(1).getString());

    // siblings()
    content
        = "<div><span>Hello</span></div><p class=\"selected\">Hello Again</p><p>And Again</p>";
    expected = "<p class=\"selected\">Hello Again</p>";
    $(e).html(content);
    assertEquals(1, $("p", e).siblings(".selected").size());
    assertHtmlEquals(expected, $("p", e).siblings(".selected").get(0).getString());

    // children()
    content = "<p>Hello</p><div id='mdiv'><span>Hello Again</span></div><p>And Again</p>";
    expected = "<span>Hello Again</span>";
    $(e).html(content);
    assertHtmlEquals(expected, $("#mdiv", e).children());

    // children()
    content
        = "<div id='mdiv'><span>Hello</span><p class=\"selected\">Hello Again</p><p>And Again</p></div>";
    expected = "<p class=\"selected\">Hello Again</p>";
    $(e).html(content);
    assertHtmlEquals(expected, $("#mdiv", e).children(".selected"));

    // contains()
    content = "<p>This is just a test.</p><p>So is this</p>";
    expected = "<p>This is just a test.</p>";
    $(e).html(content);
    assertHtmlEquals(expected, $("p", e).contains("test"));
  }

  public void testSliceMethods() {
    String content = "<p>This is just a test.</p><p>So is this</p>";
    $(e).html(content);

    String expected = "<p>So is this</p>";
    assertEquals(1, $("p", e).eq(1).size());
    assertHtmlEquals(expected, $("p", e).eq(1));

    expected = "<p>This is just a test.</p>";
    assertEquals(1, $("p", e).lt(1).size());
    assertHtmlEquals(expected, $("p", e).lt(1));

    expected = "<p>So is this</p>";
    assertEquals(1, $("p", e).gt(0).size());
    assertHtmlEquals(expected, $("p", e).gt(0));

    assertEquals(2, $("p", e).slice(0, 2).size());
    assertEquals(2, $("p", e).slice(0, -1).size());
    assertEquals(0, $("p", e).slice(3, 2).size());
  }

  public void testWrapMethod() {
    String content = "<p>Test Paragraph.</p>";
    String wrapper = "<div id=\"content\">Content</div>";

    String expected = "<div id=\"content\">Content<p>Test Paragraph.</p></div>";
    $(e).html(content);

    $("p", e).wrap(wrapper);
    assertHtmlEquals(expected, $(e).html());

    $(e).html(content + wrapper);
    expected
        = "<b><p>Test Paragraph.</p></b><b><div id=\"content\">Content</div></b>";
    $("*", e).wrap("<b></b>");
    assertHtmlEquals(expected, $(e).html());
  }
}
