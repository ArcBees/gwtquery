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
import static com.google.gwt.query.client.GQuery.document;
import static com.google.gwt.query.client.GQuery.window;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import junit.framework.Assert;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.junit.DoNotRunWith;
import com.google.gwt.junit.Platform;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.query.client.GQuery.Offset;
import com.google.gwt.query.client.css.CSS;
import com.google.gwt.query.client.css.RGBColor;
import com.google.gwt.query.client.impl.SelectorEngineCssToXPath;
import com.google.gwt.query.client.impl.SelectorEngineImpl;
import com.google.gwt.query.client.impl.SelectorEngineSizzle;
import com.google.gwt.query.client.js.JsNamedArray;
import com.google.gwt.query.client.js.JsNodeArray;
import com.google.gwt.query.client.js.JsUtils;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

/**
 * Test class for testing gwtquery-core api.
 */
public class GQueryCoreTestGwt extends GWTTestCase {

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
    return s.toString().trim().toLowerCase().replaceAll("[\r\n]", "").replaceAll(
        " ([\\w]+)=[\"']([^\"']+)[\"']", " $1=$2").replaceAll(
        "\\s+\\$h=\"[^\"]+\"", "").replaceAll(" added=[^ >]+", "");
  }

  int done = 0;

  public String getModuleName() {
    return "com.google.gwt.query.QueryTest";
  }

  public void gwtTearDown() {
    $(e).remove();
    e = null;
  }

  public void gwtSetUp() {
    if (e == null || DOM.getElementById("core-tst") == null) {
      testPanel = new HTML();
      RootPanel.get().add(testPanel);
      e = testPanel.getElement();
      e.setId("core-tst");
    } else {
      e.setInnerHTML("");
    }
  }

  @DoNotRunWith({Platform.Prod})
  public void testBrowser() {
    assertTrue(GQuery.browser.mozilla);
    assertFalse(GQuery.browser.webkit);
    assertFalse(GQuery.browser.opera);
    assertFalse(GQuery.browser.msie);
    assertFalse(GQuery.browser.ie6);
    assertFalse(GQuery.browser.ie8);
    assertFalse(GQuery.browser.ie9);
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
    assertEquals("red", $("p", e).css("color", false));
    $("p", e).css("font-weight", "bold");
    assertEquals("bold", $("p", e).css("font-weight", false));

    // css() properties
    $(e).html("<p>Test Paragraph.</p>");
    $("p", e).css(
        Properties.create("color: 'red', 'font-weight': 'bold', background: 'blue'"));
    assertEquals("red", $("p", e).css("color", false));
    assertEquals("bold", $("p", e).css("font-weight", false));
    assertEquals("blue", $("p", e).css("background-color", false));

    // css() camelize and uppercase
    $(e).html("<p>Test Paragraph.</p>");
    $("p", e).css(Properties.create("COLOR: 'red', 'FONT-WEIGHT': 'bold'"));
    assertEquals("red", $("p", e).css("color", false));
    assertEquals("", $("p", e).css("background", false));
  }

  public void testCapitalLetters() {
    $(e).html("<div id='testImageDisplay' class='whatEver'>Content</div>");
    assertEquals(1, $("#testImageDisplay").size());
    // Commented because IE is case insensitive
    // assertEquals(0, $("#testimagedisplay").size());
    assertEquals(1, $(".whatEver").size());
    assertEquals(0, $(".whatever").size());
  }

  public void testCleanMethod() {
    $(e).html("").append($("<tr/>"));
    assertHtmlEquals("<tr></tr>", $(e).html());

    $(e).html("").append($("<td/>"));
    assertHtmlEquals("<td></td>", $(e).html());

    $(e).html("").append($("<th/>"));
    assertHtmlEquals("<th></th>", $(e).html());
  }

  public void testDomManip() {
    String content = "<span class='branchA'><span class='target'>branchA target</span></span>"
        + "<span class='branchB'><span class='target'>branchB target</span></span>";

    $(e).html("");
    $(e).append(content);
    assertEquals(4, $("span", e).size());
    assertEquals(2, $("span.target", e).size());
    assertHtmlEquals(content, $(e).html());

    $(e).html("<span>a</span><span>b</span>");
    $("span").append("<div>c</div>");
    assertHtmlEquals("<span>a<div>c</div></span><span>b<div>c</div></span>", $(
        e).html());
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

  public void testIFrameManipulation() {
    $(e).html("<iframe name='miframe' id='miframe' src=\"javascript:''\">");
    // FF has to call empty to open and close the document before
    // accessing the recently created iframe content
    Document d = $("#miframe").contents().empty().get(0).cast();
    assertNotNull(d);
    assertNotNull(d.getBody());
    assertEquals(1, $("#miframe").contents().size());
    assertEquals(1, $("#miframe").contents().find("body").size());
    assertEquals(0, $("#miframe").contents().find("body > h1").size());
    $("#miframe").contents().find("body").append("<h1>Test</h1>");
    assertEquals(1, $("#miframe").contents().find("body > h1").size());
    assertEquals(1, $(d).find("h1").size());
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
    assertNull(gq.vals());
    assertNull(gq.val());

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
    // gq.val("v1");
    // assertEquals(1, gq.vals().length);
    // assertEquals("v1", gq.val());

    // input radio
    $(e).html(
        "<input type='radio' name='n' value='v1'>1</input><input type='radio' name='n' value='v2' checked='checked'>2</input>");
    gq = $("input", e);
    assertEquals("v1", gq.val());
    assertEquals("v2", $("input:checked", e).val());
    gq.val(new String[]{"v1"});
    assertEquals("v1", $("input:checked", e).val());
    gq.val(new String[]{"v2"});
    assertEquals("v2", $("input:checked", e).val());

    // input checkbox
    $(e).html(
        "<input type='checkbox' name='n1' value='v1'>1</input><input type='checkbox' name='n2' value='v2' checked='checked'>2</input>");
    gq = $("input", e);
    assertEquals("v1", gq.val());
    assertEquals("v2", $("input:checked", e).val());
    gq.val(new String[]{"v1"});
    assertEquals("v1", $("input:checked", e).val());
  }

   public void testIssue23() {
    $(e).html(
        "<table><tr><td><input type='radio' name='n' value='v1'>1</input><input type='radio' name='n' value='v2' checked='checked'>2</input></td><td><button>Click</button></tr><td></table>");
    $("button").click(new Function() {
      public boolean f(Event ev) {
        done = 0;
        $("table > tbody > tr > td > input:checked", e).each(new Function() {
          public void f(Element e) {
            done++;
          }
        });
        assertEquals(1, done);
        return true;
      }
    });
    $("button").click();
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
    GQuery g = $("b", e).appendTo($("p", e));
    assertHtmlEquals(expected, $(e).html());
    assertHtmlEquals("<b>Hello</b>", g.toString());
    // document is a valid node, actually it is substituted by body
    g.appendTo(document);
    expected = "<p>I would like to say: </p>";
    assertHtmlEquals(expected, $(e).html());
    g.remove();
    // Check that the new elements are returned and can be modified
    $("<div id='mid'>Hello</div>").appendTo(e).css("color", "white");
    assertEquals("white", $("#mid").css("color", false));

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
    // Check that the new elements are returned and can be modified
    $("<div id='mid'>Hello</div>").prependTo(e).css("color", "yellow");
    assertEquals("yellow", $("#mid").css("color", false));

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

    // The set of elements should be the same after the manipulation
    String content = "<span>s</span>";
    expected = "<p>p</p>";
    GQuery g1 = $(content);
    GQuery g2 = $(expected);
    $(e).html("").append(g1);
    assertEquals(1, g1.size());
    assertEquals(content, g1.toString());

    g1.append(g2);
    assertEquals(1, g1.size());
    assertEquals(1, g2.size());
    assertEquals(expected, g2.toString());

    g1.prepend(g2);
    assertEquals(1, g1.size());
    assertEquals(1, g2.size());
    assertEquals(expected, g2.toString());

    g1.after(g2);
    assertEquals(1, g1.size());
    assertEquals(1, g2.size());
    assertEquals(expected, g2.toString());

    g1.before(g2);
    assertEquals(1, g1.size());
    assertEquals(1, g2.size());
    assertEquals(expected, g2.toString());
  }

  public void test_issue128() {
    GQuery g = $(e).html("<span>a</span><span>b</span><span>c</span>");
    assertEquals(g.text(), "abc");
    $("span", e).after(" ");
    assertEquals(g.text(), "a b c ");
    $("span", e).after("-");
    assertEquals(g.text(), "a- b- c- ");
  }

  public void testAppendTo() {
    String txt = "<h2>Greetings</h2><div class='container'><div class='inner'>Hello</div><div class='inner'>Goodbye</div></div>";
    String expected = "<h2>Greetings</h2><div class='container'><div class='inner'>Hello<p>Test</p></div><div class='inner'>Goodbye<p>Test</p></div></div>";
    $(e).html(txt);
    $("<p>Test</p>").appendTo(".inner");
    assertHtmlEquals(expected, $(e).html());

    expected = "<div class='container'><div class='inner'>Hello</div><div class='inner'>Goodbye</div><h2>Greetings</h2></div>";
    $(e).html(txt);
    $("h2", e).appendTo($(".container"));
    assertHtmlEquals(expected, $(e).html());

    expected = "<div class='container'><div class='inner'>Hello<h2>Greetings</h2></div><div class='inner'>Goodbye<h2>Greetings</h2></div><h2>Greetings</h2></div><h2>Greetings</h2>";
    $(e).html(txt);
    $("h2", e).appendTo($("div"));
    assertHtmlEquals(expected, $(e).html());
  }

  public void testOffset(){
    $(e).html(
        "<div id='id1' style='padding-left:10px; padding-top:20px;'><div id='id2'>Content 1</div></div>");

    Offset parentOffset = $("#id1", e).offset();

    GQuery g = $("#id2", e);
    Offset initialOffset = g.offset();

    assertEquals(10 + parentOffset.left, initialOffset.left);
    assertEquals(20 + parentOffset.top, initialOffset.top);

    g.offset(10, 0);

    Offset offset = g.offset();
    assertEquals(0, offset.left);
    assertEquals(10, offset.top);

    //css control
    String top = g.css("top", true);
    String left = g.css("left", true);

    int expectedTop = 10 - initialOffset.top;
    int expectedLeft = 0 - initialOffset.left;

    assertEquals(""+expectedTop+"px", top);
    assertEquals(""+expectedLeft+"px", left);

  }

  public void testOpacity() {
    $(e).html(
        "<p id='id1' style='opacity: 0.6; filter: alpha(opacity=60)'>Content 1</p>");
    GQuery g = $("#id1");
    assertEquals("0.6", g.css("opacity", false));
    assertEquals("0.6", g.css("opacity", true));
    g.css("opacity", "");
    assertEquals("", g.css("opacity", false));
    assertEquals("1", g.css("opacity", true).replaceFirst("\\.0$", ""));
    g.css("opacity", "0.4");
    assertEquals("0.4", g.css("opacity", false));
    assertEquals("0.4", g.css("opacity", true));
  }

  public void testPosition() {
    $(e).html(
        "<div style='top:25px; left:25px; padding:20px; position:relative;'><div id='child' style='margin:30px'>test</div></div> ");
    GQuery g = $("#child");
    assertEquals(20, g.position().left);
    assertEquals(20, g.position().top);

    $(e).html(
        "<div style='top:25px; left:25px; position:relative;'><div id='child' style='position:relative; top:15px; left:35px;'>test</div></div> ");
    g = $("#child");
    assertEquals(35, g.position().left);
    assertEquals(15, g.position().top);

  }

  public void testPropMethod(){
    $(e).html("<input id=\"checkBox1\" type=\"checkbox\" checked=\"checked\" /> <input id=\"checkBox2\" type=\"checkbox\" />");

    assertEquals(Boolean.TRUE, $("#checkBox1",e).prop("checked"));
    assertEquals(Boolean.FALSE, $("#checkBox2",e).prop("checked"));

    $("#checkBox1",e).prop("checked", false);
    $("#checkBox2",e).prop("checked", new Function() {
      @Override
      public Object f(Element e, int i) {
        return Boolean.TRUE;
      }
    });
    assertEquals(Boolean.TRUE, $("#checkBox2",e).prop("checked"));
    assertEquals(Boolean.FALSE, $("#checkBox1",e).prop("checked"));

    $(window).prop("foo", 234);
    assertEquals(234d, $(window).prop("foo"));
    assertEquals(234l, (long)$(window).prop("foo", Long.class));
  }

  @DoNotRunWith(Platform.Prod)
  public void testProperties() {
    Properties p = $$("border:'1px solid black'");
    assertEquals(1, p.keys().length);
    assertNotNull(p.getStr("border"));

    p = $$("({border:'1px solid black'})");
    assertEquals(1, p.keys().length);
    assertNotNull(p.getStr("border"));

    try {
      // DevMode null casting returns an object
      // @see:
      //  http://code.google.com/p/gwtquery/issues/detail?id=122
      //  http://code.google.com/p/google-web-toolkit/issues/detail?id=6625
      ((Properties)null).toJsonString();
      fail("Executing methods of a null object should throw a NullPointerException");
    } catch (NullPointerException e) {
    }
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
    content = "<div><p>Hello</p></div><div class=\"selected\"><p>Hello Again</p></div>";
    expected = "<div class=\"selected\"><p>Hello Again</p></div>";
    $(e).html(content);
    assertHtmlEquals(expected, $("p", e).parent(".selected"));

    // parents()
    content = "<div><p><span>Hello</span></p><span>Hello Again</span></div>";
    $(e).html(content);
    assertEquals(2, $("span", e).size());
    assertTrue(3 < $("span", e).parents().size());
    assertEquals(1, $("span", e).parents().filter("body").size());
    $("span", e).parents().filter("body").toString().trim().toLowerCase().contains(
        content.toLowerCase());

    //parentsUntil()
    content = "<div id='mainDiv'><div id='subDiv1' class='subDiv'><div id='subSubDiv1'><p id='p1'>child1</p></div></div><div id='subDiv2' class='subDiv'><div id='subSubDiv2'><p id='p2'>child2</p></div></div></div>";
    $(e).html(content);
    $("p", e).parentsUntil("#mainDiv").css(CSS.COLOR.with(RGBColor.RED));
    assertEquals("red", $("#subDiv1", e).css(CSS.COLOR, false));
    assertEquals("red", $("#subSubDiv1", e).css(CSS.COLOR, false));
    assertEquals("red", $("#subDiv2", e).css(CSS.COLOR, false));
    assertEquals("red", $("#subSubDiv2", e).css(CSS.COLOR, false));
    assertEquals("", $("#mainDiv", e).css(CSS.COLOR, false));
    assertEquals("", $("#p1", e).css(CSS.COLOR, false));
    assertEquals("", $("#p2", e).css(CSS.COLOR, false));

    $("#p1",e).parentsUntil(".subDiv").css(CSS.COLOR.with(RGBColor.YELLOW));
    assertEquals("red", $("#subDiv1", e).css(CSS.COLOR, false));
    assertEquals("yellow", $("#subSubDiv1", e).css(CSS.COLOR, false));

    //parentsUntil()
    content = "<div id='mainDiv'><div id='subDiv1' class='subDiv'><div id='subSubDiv1'><p id='p1'>child1</p></div></div><div id='subDiv2' class='subDiv'><div id='subSubDiv2'><p id='p2'>child2</p></div></div></div>";
    $(e).html(content);
    Element node = $("#mainDiv", e).get(0);
    $("p", e).parentsUntil(node).css(CSS.COLOR.with(RGBColor.RED));
    assertEquals("red", $("#subDiv1", e).css(CSS.COLOR, false));
    assertEquals("red", $("#subSubDiv1", e).css(CSS.COLOR, false));
    assertEquals("red", $("#subDiv2", e).css(CSS.COLOR, false));
    assertEquals("red", $("#subSubDiv2", e).css(CSS.COLOR, false));
    assertEquals("", $("#mainDiv", e).css(CSS.COLOR, false));
    assertEquals("", $("#p1", e).css(CSS.COLOR, false));
    assertEquals("", $("#p2", e).css(CSS.COLOR, false));

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
    content = "<p>Hello</p><p class=\"selected\">Hello Again</p><div><span>And Again</span></div>";
    expected = "<p class=\"selected\">Hello Again</p>";
    $(e).html(content);
    assertEquals(1, $("p", e).next(".selected").size());
    assertHtmlEquals(expected, $("p", e).next(".selected").get(0).getString());

    // nextAll()
    content = "<ul><li>i1</li><li>i2</li><li class='third-item'>i3</li><li>i4</li><li class=\"last-item\">i5</li></ul>";
    expected = "<li>i4</li><li class=\"last-item\">i5</li>";
    $(e).html(content);
    assertEquals(2, $("li.third-item", e).nextAll().size());
    assertHtmlEquals(expected, $("li.third-item", e).nextAll());

    expected = "<li class=\"last-item\">i5</li>";
    assertEquals(1, $("li.third-item", e).nextAll(".last-item").size());
    assertHtmlEquals(expected, $("li.third-item", e).nextAll(".last-item"));

    // nextUntil()
    content = "<ul><li class='first-item'>i1</li><li>i2</li><li class='third-item'>i3</li><li>i4</li><li class='five-item'>i5</li></ul>";
    expected = "<li>i4</li>";
    $(e).html(content);
    assertEquals(1, $("li.third-item", e).nextUntil(".five-item").size());
    assertHtmlEquals(expected, $("li.third-item", e).nextUntil(".five-item"));

    GQuery nextUntil = $("li.five-item");
    assertEquals(1, $("li.third-item", e).nextUntil(nextUntil).size());
    assertHtmlEquals(expected, $("li.third-item", e).nextUntil(nextUntil));

    Element nextUntilElement = nextUntil.get(0);
    assertEquals(1, $("li.third-item", e).nextUntil(nextUntilElement).size());
    assertHtmlEquals(expected, $("li.third-item", e).nextUntil(nextUntilElement));


    expected = "<li class='third-item'>i3</li>";
    $(e).html(content);
    assertEquals(1, $("li.first-item", e).nextUntil(".five-item", "li.third-item").size());
    assertHtmlEquals(expected, $("li.first-item", e).nextUntil(".five-item", "li.third-item"));

    assertEquals(1, $("li.first-item", e).nextUntil(nextUntil, "li.third-item").size());
    assertHtmlEquals(expected, $("li.first-item", e).nextUntil(nextUntil, "li.third-item"));

    assertEquals(1, $("li.first-item", e).nextUntil(nextUntilElement, "li.third-item").size());
    assertHtmlEquals(expected, $("li.first-item", e).nextUntil(nextUntilElement, "li.third-item"));

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
    content = "<div><span>Hello</span></div><p class=\"selected\">Hello Again</p><p>And Again</p>";
    expected = "<p class=\"selected\">Hello Again</p>";
    $(e).html(content);
    assertEquals(2, $("p", e).prev().size());
    assertEquals(1, $("p", e).prev(".selected").size());
    assertHtmlEquals(expected, $("p", e).prev(".selected").get(0).getString());

    // prevAll()
    content = "<ul><li>i1</li><li>i2</li><li class='third-item'>i3</li><li>i4</li><li class='five-item'>i5</li></ul>";
    expected = "<li>i4</li><li class='third-item'>i3</li><li>i2</li><li>i1</li>";
    $(e).html(content);
    assertEquals(4, $("li.five-item", e).prevAll().size());
    assertHtmlEquals(expected, $("li.five-item", e).prevAll());

    expected = "<li class='third-item'>i3</li>";
    assertEquals(1, $("li.five-item", e).prevAll(".third-item").size());
    assertHtmlEquals(expected, $("li.five-item", e).prevAll(".third-item"));

    // prevUntil()
    content = "<ul><li class='item'>i1</li><li class='second-item'>i2</li><li class='third-item'>i3</li><li class='item'>i4</li><li class='five-item'>i5</li></ul>";
    expected = "<li class='item'>i4</li>";
    $(e).html(content);
    assertEquals(1, $("li.five-item", e).prevUntil(".third-item").size());
    assertHtmlEquals(expected, $("li.five-item", e).prevUntil(".third-item"));

    assertEquals(1, $("li.five-item", e).prevUntil($(".third-item")).size());
    assertHtmlEquals(expected, $("li.five-item", e).prevUntil($(".third-item")));

    Element until = $(".third-item").get(0);
    assertEquals(1, $("li.five-item", e).prevUntil(until).size());
    assertHtmlEquals(expected, $("li.five-item", e).prevUntil(until));


    assertEquals(0, $("li.five-item", e).prevUntil(".third-item", ".fake-class").size());
    assertEquals(1, $("li.five-item", e).prevUntil(".second-item", ".item").size());
    assertHtmlEquals(expected, $("li.five-item", e).prevUntil(".second-item", ".item"));

    assertEquals(0, $("li.five-item", e).prevUntil($(".third-item"), ".fake-class").size());
    assertEquals(1, $("li.five-item", e).prevUntil($(".second-item"), ".item").size());
    assertHtmlEquals(expected, $("li.five-item", e).prevUntil($(".second-item"), ".item"));

    assertEquals(0, $("li.five-item", e).prevUntil(until, ".fake-class").size());

    until = $(".second-item").get(0);
    assertEquals(1, $("li.five-item", e).prevUntil(until, ".item").size());
    assertHtmlEquals(expected, $("li.five-item", e).prevUntil(until, ".item"));

    // siblings()
    content = "<p>Hello</p><div id='mdiv'><span>Hello Again</span></div><p>And Again</p>";
    next1 = "<p>Hello</p>";
    next2 = "<p>And Again</p>";
    $(e).html(content);
    assertEquals(2, $("#mdiv", e).siblings().size());
    assertHtmlEquals(next1, $("#mdiv", e).siblings().get(0).getString());
    assertHtmlEquals(next2, $("#mdiv", e).siblings().get(1).getString());

    // siblings()
    content = "<div><span>Hello</span></div><p class=\"selected\">Hello Again</p><p>And Again</p>";
    expected = "<p class=\"selected\">Hello Again</p>";
    $(e).html(content);
    assertEquals(1, $("p", e).siblings(".selected").size());
    assertHtmlEquals(expected,
        $("p", e).siblings(".selected").get(0).getString());

    // children()
    content = "<p>Hello</p><div id='mdiv'><span>Hello Again</span></div><p>And Again</p>";
    expected = "<span>Hello Again</span>";
    $(e).html(content);
    assertHtmlEquals(expected, $("#mdiv", e).children());

    // children()
    content = "<div id='mdiv'><span>Hello</span><p class=\"selected\">Hello Again</p><p>And Again</p></div>";
    expected = "<p class=\"selected\">Hello Again</p>";
    $(e).html(content);
    assertHtmlEquals(expected, $("#mdiv", e).children(".selected"));

    // contains()
    content = "<p>This is just a test.</p><p>So is this</p>";
    expected = "<p>This is just a test.</p>";
    $(e).html(content);
    assertHtmlEquals(expected, $("p", e).contains("test"));
  }

  public void testReplaceMethods() {
    String content = "<div><div class='inner first'>Hello</div><div class='inner second'>And</div><div class='inner third'>Goodbye</div></div>";

    $(e).html(content);
    GQuery $inner = $(".inner").replaceWith("<h3>blop</h3>");
    String expectedHtml = "<div><h3>blop</h3><h3>blop</h3><h3>blop</h3></div>";
    assertEquals(expectedHtml, $(e).html());

    // the returned gquery object should be the original with the div elements
    assertEquals(3, $inner.filter("div.inner").length());

    $(e).html(content);
    // the css part below allows us to check if the objects returned by the
    // replaceAll method are the new inserted elements
    $("<h3>blop</h3>").replaceAll($(".inner")).css(CSS.COLOR.with(RGBColor.RED));
    // $(e) must content 3 h3
    assertEquals(3, $("h3", e).length());
    // the objects returned by the replaceAll method should be the 3 inserted h3
    assertEquals("red", $("h3", e).css(CSS.COLOR, false));


    $(e).html(content);
    $(".third").replaceWith($(".first"));
    expectedHtml = "<div><div class=\"inner second\">And</div><div class=\"inner first\">Hello</div></div>";
    assertEquals($(e).html(), expectedHtml);

    $(e).html(content);
    $(".first").replaceAll(".third");
    assertEquals($(e).html(), expectedHtml);

  }

  public void testShowHide() {
    $(e).html(
        "<p id='id1' style='display: inline'>Content 1</p><p id='id2'>Content 2</p><p id='id3'>Content 3</p>");
    final GQuery sectA = $("#id1");
    final GQuery sectB = $("#id2");
    final GQuery sectC = $("#id3");

    // hide()
    sectA.hide();
    assertEquals("none", sectA.css("display", false));
    sectB.hide();
    assertEquals("none", sectB.css("display", false));

    // show()
    sectA.show();
    assertEquals("inline", sectA.css("display", false));
    sectB.show();
    assertEquals("", sectB.css("display", false));

    // toggle()
    assertEquals("", sectC.css("display", false));
    sectC.toggle();
    assertEquals("none", sectC.css("display", false));
    sectC.toggle();
    assertEquals("block", sectC.css("display", false));
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

  public void testGetEqLastFirstMethods(){
    String content = "<div id='1'>blop1</div><div id='2'>blop2</div><div id='3'>blop3</div><div id='4'>blop4</div>";
    $(e).html(content);

    GQuery divs =$("div",e);
    assertEquals(4, divs.size());
    assertEquals("1", divs.get(0).getId());
    assertEquals("2", divs.get(1).getId());
    assertEquals("3", divs.get(2).getId());
    assertEquals("4", divs.get(3).getId());
    assertEquals("1", divs.get(-4).getId());
    assertEquals("2", divs.get(-3).getId());
    assertEquals("3", divs.get(-2).getId());
    assertEquals("4", divs.get(-1).getId());

    assertEquals(1, divs.first().size());
    assertEquals("1", divs.first().get(0).getId());

    assertEquals(1, divs.last().size());
    assertEquals("4", divs.last().get(0).getId());

    assertEquals(1, divs.eq(0).size());
    assertEquals("1", divs.eq(0).get(0).getId());
    assertEquals(1, divs.eq(1).size());
    assertEquals("2", divs.eq(1).get(0).getId());
    assertEquals(1, divs.eq(2).size());
    assertEquals("3", divs.eq(2).get(0).getId());
    assertEquals(1, divs.eq(3).size());
    assertEquals("4", divs.eq(3).get(0).getId());

    assertEquals(1, divs.eq(-4).size());
    assertEquals("1", divs.eq(-4).get(0).getId());
    assertEquals(1, divs.eq(-3).size());
    assertEquals("2", divs.eq(-3).get(0).getId());
    assertEquals(1, divs.eq(-2).size());
    assertEquals("3", divs.eq(-2).get(0).getId());
    assertEquals(1, divs.eq(-1).size());
    assertEquals("4", divs.eq(-1).get(0).getId());


  }

  public void testUnique() {
    SelectorEngineImpl selSizz = new SelectorEngineSizzle();
    GQuery g = $(e).html("<div><p></p><p></p><span></span><p></p>");
    JsNodeArray a;
    a = selSizz.select("p", e).cast();
    assertEquals(3, a.getLength());
    a.addNode(a.getNode(0));
    a.addNode(a.getNode(3));
    assertEquals(5, a.getLength());
    a = g.unique(a);
    assertEquals(3, a.getLength());
  }

  public void testUtilsEq() {
    assertTrue(JsUtils.eq("a", "a"));
    assertTrue(JsUtils.eq(true, true));
    assertTrue(JsUtils.eq(45, 45));
    assertTrue(JsUtils.eq(45d, 45f));
    assertTrue(JsUtils.eq("", ""));
    assertTrue(JsUtils.eq(0.45, 0.45));
    assertTrue(JsUtils.eq(0.45d, 0.45d));
    assertTrue(JsUtils.eq(0.45f, 0.45f));

    assertFalse(JsUtils.eq("a", ""));
    assertFalse(JsUtils.eq(true, false));
    assertFalse(JsUtils.eq(45, 42));
    assertFalse(JsUtils.eq("", null));
    assertFalse(JsUtils.eq(0.45, 0.451));

    // assertEquals("a", GQUtils.or("a", ""));
  }

  public void testUtilsTruth() {
    assertTrue(JsUtils.truth("a"));
    assertTrue(JsUtils.truth(this));
    assertTrue(JsUtils.truth(45));
    assertTrue(JsUtils.truth(0.33));
    assertTrue(JsUtils.truth(45l));
    assertTrue(JsUtils.truth(45d));
    assertTrue(JsUtils.truth(45f));
    assertTrue(JsUtils.truth(0.33f));

    assertFalse(JsUtils.truth(0));
    assertFalse(JsUtils.truth(0l));
    assertFalse(JsUtils.truth(0d));
    assertFalse(JsUtils.truth(00.00d));
    assertFalse(JsUtils.truth(00.00f));
    assertFalse(JsUtils.truth(null));
    assertFalse(JsUtils.truth(""));
  }

  public void testUtilsCallFunc() {
    assertTrue(JsUtils.hasProperty(window, "document.body.style.background"));
    assertTrue(JsUtils.hasProperty(window, "document.createElement"));
    assertFalse(JsUtils.hasProperty(window, "document.body.style.foo"));

    Element e = JsUtils.runJavascriptFunction(document, "createElement", "div");
    assertNotNull(e);
    assertEquals(e.getTagName().toLowerCase(), "div");

    e = JsUtils.jsni("document.createElement", "span");
    assertNotNull(e);
    assertEquals(e.getTagName().toLowerCase(), "span");

    e = JsUtils.jsni("document.foo.bar");
    assertNull(e);
  }

  public void testUtilsJsniExport() {
    JsUtils.export("foo", new Function() {
      public Object f(Object... args) {
        assertEquals("bar", (String)args[0]);
        assertEquals(3.5, (Double)args[1]);
        return "OK";
      }
    });
    assertTrue(JsUtils.hasProperty(window, "foo"));
    assertEquals("OK", JsUtils.jsni("foo", "bar", 3.5));
  }

  public void testVal_issue98() {
    $(e).html(""
      +"<input type='text' id='inputText' name='inputText' value='original' />"
      +"<textarea id='textArea' name='textArea'>original</textarea>"
      +"<button id='button' name='button'value='original'>Click</button>"

      +"<select id='selectSingle' name='selectSingle'>"
      +"<option value='v0'>Single0</option>"
      +"<option value='v1'>Single1</option>"
      +"<option value='v2'>Single2</option>"
      +"</select>"

      +"<select id='selectMultiple' name='selectMultiple' multiple='multiple'>"
      +"<option value='v0'>Multiple0</option>"
      +"<option value='v1'>Multiple1</option>"
      +"<option value='v2'>Multiple2</option>"
      +"</select><br/>"

      +"<input type='checkbox' name='c' value='v0'/> check0"
      +"<input type='checkbox' name='c' value='v1'/> check1"
      +"<input type='checkbox' name='c' value='v2'/> check2"

      +"<input type='radio'  name='r' value='v0'/> radio0"
      +"<input type='radio'  name='r' value='v1'/> radio1"
      +"<input type='radio'  name='r' value='v2'/> radio2"
    );

    assertNull($().val());
    assertEquals(0, $().vals().length);

    assertEquals("original", $("#inputText", e).val());
    assertEquals("original", $("#textArea", e).val());
    assertEquals("original", $("#button", e).val());
    $("#inputText, #textArea, #button", e).val("newval");
    assertEquals("newval", $("#inputText", e).val());
    assertEquals("newval", $("#textArea", e).val());
    assertEquals("newval", $("#button", e).val());

    assertEquals("v0", $("#selectSingle", e).val());
    assertNull($("#selectMultiple", e).val());
    $("#selectSingle, #selectMultiple", e).val("v2");
    assertEquals("v2", $("#selectSingle", e).val());
    assertEquals("v2", $("#selectMultiple", e).val());

    assertEquals("v0", $("input[type='checkbox']", e).val());
    assertNull($("input[type='checkbox']:checked", e).val());
    // val(String) changes the value attribute, but not set it as checked
    $("input[type='checkbox']", e).eq(0).val("n0");
    assertEquals("n0", $("input[type='checkbox']", e).val());
    assertNull($("input[type='checkbox']:checked", e).val());
    // val(array) set the checked property to true if the value name matches
    $("input[type='checkbox']", e).val(new String[]{"n0"});
    assertEquals("n0", $("input[type='checkbox']", e).val());
    assertNotNull($("input[type='checkbox']:checked", e).val());

    assertEquals("v0", $("input[type='radio']", e).val());
    assertNull($("input[type='radio']:checked", e).val());
    $("input[type='radio']").eq(0).val("n0");
    assertEquals("n0", $("input[type='radio']", e).val());
    assertNull($("input[type='radio']:checked", e).val());

//    evalJQuery("$('input, select, textarea, button').val(['whatever', 'v1', 'v2'])");
    $("input, select, textarea, button").val("whatever", "v1", "v2");

    String joinVals = "whatever,v1,v2";
    assertEquals(joinVals, $("#inputText", e).val());
    assertEquals(joinVals, $("#textArea", e).val());
    assertEquals(joinVals, $("#button", e).val());
    assertEquals("v2", $("#selectSingle", e).val());
    assertEquals("v1", $("#selectMultiple", e).vals()[0]);
    assertEquals("v2", $("#selectMultiple", e).vals()[1]);
    assertEquals(2, $("input[type='checkbox']:checked", e).size());
    assertEquals("v1", $("input[type='checkbox']:checked", e).eq(0).val());
    assertEquals("v2", $("input[type='checkbox']:checked", e).eq(1).val());
    assertEquals(1, $("input[type='radio']:checked", e).size());
    assertEquals("v2", $("input[type='radio']:checked", e).val());
  }


  public void testAttr_Issue97() {
    $(e).html("<input type='checkbox' id='cb' name='cb' value='1' />");
    assertNull($("#cb:checked", e).val());

    $("#cb", e).attr("checked", "checked");
    assertEquals(1, $("#cb:checked", e).length());
    assertEquals(true,  InputElement.as($("#cb", e).get(0)).isChecked());
    assertEquals("checked",  $("#cb", e).get(0).getAttribute("checked"));
    assertEquals(true,  $("#cb", e).get(0).getPropertyBoolean("checked"));

    $("#cb", e).removeAttr("checked");
    assertEquals(0, $("#cb:checked", e).length());
    assertEquals(false,  InputElement.as($("#cb", e).get(0)).isChecked());
    assertEquals("",  $("#cb", e).get(0).getAttribute("checked"));
    assertEquals(false,  $("#cb", e).get(0).getPropertyBoolean("checked"));

    $("#cb", e).attr("checked", true);
    assertEquals(1, $("#cb:checked", e).length());
    assertEquals(true,  InputElement.as($("#cb", e).get(0)).isChecked());
    assertEquals("checked",  $("#cb", e).get(0).getAttribute("checked"));
    assertEquals(true,  $("#cb", e).get(0).getPropertyBoolean("checked"));

    $("#cb", e).attr("checked", false);
    assertEquals(0, $("#cb:checked", e).length());
    assertEquals(false,  InputElement.as($("#cb", e).get(0)).isChecked());
    assertEquals("",  $("#cb", e).get(0).getAttribute("checked"));
    assertEquals(false,  $("#cb", e).get(0).getPropertyBoolean("checked"));

    $("#cb", e).attr("checked", "");
    assertEquals(1, $("#cb:checked", e).length());
    assertEquals(true,  InputElement.as($("#cb", e).get(0)).isChecked());
    assertEquals("checked",  $("#cb", e).get(0).getAttribute("checked"));
    assertEquals(true,  $("#cb", e).get(0).getPropertyBoolean("checked"));

    GQuery gq = $("<div></div>test<!-- a comment-->");
    gq.attr("class", "test1");

    assertEquals("test1", gq.get(0).getClassName());
    assertEquals("test1", gq.attr("class"));
    assertNull(gq.get(0).getPropertyString("class"));

    gq.removeAttr("class");
    assertEquals("", gq.get(0).getClassName());
    assertEquals("", gq.attr("class"));

    //test on value
    $("#cb", e).attr("value", "mail");
    assertEquals("mail", InputElement.as($("#cb", e).get(0)).getValue());
    assertEquals("mail", $("#cb", e).get(0).getAttribute("value"));

    $("#cb", e).removeAttr("value");
    String val = InputElement.as($("#cb", e).get(0)).getValue();
    assertTrue(String.valueOf(val).matches("^(|null|on)$"));

    val = $("#cb", e).get(0).getAttribute("value");
    assertTrue(String.valueOf(val).matches("^(|null|on)$"));

    try{
      $("#cb", e).attr("type", "hidden");
      fail("Cannnot change a type of an element already attached to the dom");
    }catch (Exception e){}

    gq = $("<input type='text' value='blop'></input>");
    gq.attr("type", "radio");
    assertEquals("radio", InputElement.as(gq.get(0)).getType());
    assertEquals("blop", InputElement.as(gq.get(0)).getValue());

    gq.attr(Properties.create("{class:'test2', disabled:true}"));
    InputElement ie = InputElement.as(gq.get(0));

    assertEquals("test2", ie.getClassName());
    assertEquals(true, ie.isDisabled());
    assertEquals("disabled", ie.getAttribute("disabled"));

  }

  public void testAttr_Issue165() {
    $(e).html("<a href='#' title='a title'>anchor</a>");
    Element a = $("a", e).get(0);

    assertEquals("a title", a.getAttribute("title"));
    assertTrue(JsUtils.hasAttribute(a, "title"));

    $(a).removeAttr("title");
    assertEquals("", a.getAttribute("title"));
    assertFalse(JsUtils.hasAttribute(a, "title"));
  }

  public void testWidthHeight() {
    $(e).html(
        "<div style='border: 1px solid red; padding: 10px; margin:10px; width: 100px; height: 100px'>Content 1</div>");
    GQuery g = $("div", e);
    assertEquals(100, g.width());
    assertEquals(100, g.height());
    assertEquals("e1", 120, g.innerWidth());
    assertEquals("e2", 120, g.innerHeight());
    assertEquals(100d, g.cur("width", false));
    assertEquals(100d, g.cur("height", false));
    assertEquals(100d, g.cur("width", true));
    assertEquals(100d, g.cur("height", true));
    assertEquals("100px", g.css("width"));
    assertEquals("100px", g.css("height"));
    assertEquals("100px", g.get(0).getStyle().getProperty("width"));
    assertEquals("100px", g.get(0).getStyle().getProperty("height"));
    assertEquals(122, g.outerHeight());
    assertEquals(122, g.outerWidth());
    assertEquals(142, g.outerHeight(true));
    assertEquals(142, g.outerWidth(true));
  }

  public void testWidthHeightInlineElement() {
    $(e).html(
        "<span style='border: 1px solid red; padding: 10px; margin:10px'>Content 1</span>");
    GQuery g = $("span", e);
    assertTrue(g.width() > 0);
    assertTrue(g.height() > 0);
  }

  public void testWrapMethod() {
    String content = "<p>Test Paragraph.</p>";
    String wrapper = "<div id=\"content\">Content</div>";

    String expected = "<div id=\"content\">Content<p>Test Paragraph.</p></div>";
    $(e).html(content);

    $("p", e).wrap(wrapper);
    assertHtmlEquals(expected, $(e).html());

    $(e).html(content + wrapper);
    expected = "<b><p>Test Paragraph.</p></b><b><div id=\"content\">Content</div></b>";
    $("*", e).wrap("<b></b>");
    assertHtmlEquals(expected, $(e).html());
  }


  public void testFilterBody() {
    GQuery myNewElement = $("<div>my new div</div>");
    boolean isAttachedToTheDOM = myNewElement.parents().filter("body").size() > 0;
    assertEquals(false, isAttachedToTheDOM);

    myNewElement.appendTo(document);
    isAttachedToTheDOM = myNewElement.parents().filter("body").size() > 0;
    assertEquals(true, isAttachedToTheDOM);
  }

  public void testFilterMethod(){
    // first test filter on element attached to the DOM
    String content = "<div class='outer'><div class='inner first'>Hello <span>blop</span></div><div class='inner second'>And</div><div class='inner third'>Goodbye</div></div>";

    $(e).html(content);

    assertEquals(5, $("*", e).length());
    assertEquals(4, $("*", e).filter("div").length());
    assertEquals(1, $("*", e).filter("div.outer").length());
    assertEquals(3, $("*", e).filter("div.inner").length());
    assertEquals(1, $("*", e).filter("span").length());

    GQuery $html = $("<div>div1</div><div>div2</div><div>div3</div><span>span1</span>");
    assertEquals(3, $html.filter("div").length());
    assertEquals(1, $html.filter("span").length());

    JsNodeArray array = JsNodeArray.create();
    for (int i = 0 ; i < 3; i++){
          array.addNode(DOM.createDiv());
     }
     assertEquals(3, $(array).filter("div").length());


     String content2 = "<div><div class='inner first'>Hello</div><div class='inner second'>And</div><div class='inner third'>Goodbye</div></div>";
     $(e).html(content2);
     //the inner object contains the 3 div that are detached from the dom
     GQuery $inner = $(".inner").replaceWith("<h3>blop</h3>");
     assertEquals(3, $inner.filter("div").length());
  }

  public void testGQueryWidgets() {
    final Button b1 = new Button("click-me");
    RootPanel.get().add(b1);

    GQuery g = $(b1);
    Button b2 = g.widget();
    assertEquals(b1, b2);

    b2 = new Button("click-me");
    RootPanel.get().add(b2);
    b2.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        $(b1).css("color", "red");
      }
    });

    (b2).click();
    assertEquals("red", $(b1).css("color", false));

    $("<button>Click-me</button>").appendTo(document);
    assertEquals(3, $("button").size());
    assertEquals(2, $("button").widgets(Button.class).size());
    assertEquals(2, $($("button").widgets(Button.class)).size());

    assertEquals(2, $(new Label(""), new TextArea()).size());
  }

  public void testGQueryWidgetManipulation() {

    String content = "<div class='outer'></div>";
    $(e).html(content);
    Button b = new Button("b");
    RootPanel.get().add(b);
    assertTrue(b.isAttached());
    $("button").remove();
    assertFalse(b.isAttached());

    RootPanel.get().add(b);
    assertTrue(b.isAttached());
    $(e).append($(b));
    assertTrue(b.isAttached());
    $(e).empty();
    // FIXME: assertFalse(b.isAttached());

  }

  public void testGQueryMap() {
    String content = "<p id='1'/><p/><p id='2'/><p id='4'/>";
    $(e).html(content);

    List<String> s = $("p", e).map(new Function() {
      public Object f(Element e, int i) {
        return null;
      }
    });
    assertNotNull(s);
    assertEquals(0, s.size());

    s = $("p", e).map(new Function() {
      public Object f(Element e, int i) {
        String id = $(e).attr("id");
        return id.isEmpty() ? null : id;
      }
    });
    assertEquals(3, s.size());
    assertEquals("1", s.get(0));
    assertEquals("2", s.get(1));
    assertEquals("4", s.get(2));

    List<Element> a = $("p", e).map(new Function() {
      public Object f(Element e, int i) {
        String id = $(e).attr("id");
        return id.isEmpty() ? null : e;
      }
    });
    assertEquals(3, a.size());
    assertEquals(3, $(a).size());

  }

  public void testHtmlSnippet(){

    GQuery gq = $("<div>blop</div><p>test</p><span>test</span>");
    assertEquals(3, gq.size());
    assertEquals("DIV", gq.get(0).getTagName().toUpperCase());
    assertEquals("P", gq.get(1).getTagName().toUpperCase());
    assertEquals("SPAN", gq.get(2).getTagName().toUpperCase());

    //xhtml tag
    gq = $("<div/>");
    assertEquals(1, gq.size());
    assertEquals("DIV", gq.get(0).getTagName().toUpperCase());

    gq = $("<a/>");
    assertEquals(1, gq.size());
    assertEquals("A", gq.get(0).getTagName().toUpperCase());

    gq = $("<div>");
    assertEquals(1, gq.size());
    assertEquals("DIV", gq.get(0).getTagName().toUpperCase());

    //issue 81 : trailing spaces
    gq = $(" <div>blop</div><p>test</p><span>test</span> ");
    assertEquals(3, gq.size());
    assertEquals("DIV", gq.get(0).getTagName().toUpperCase());
    assertEquals("P", gq.get(1).getTagName().toUpperCase());
    assertEquals("SPAN", gq.get(2).getTagName().toUpperCase());

    //wrapping
    gq = $("<tr>blop</tr><tr>test</tr>");
    assertEquals(2, gq.size());
    assertEquals("TR", gq.get(0).getTagName().toUpperCase());
    assertEquals("TR", gq.get(1).getTagName().toUpperCase());
    //same in uppercase
    gq = $("<TR>blop</TR><tr>test</tr>");
    assertEquals(2, gq.size());
    assertEquals("TR", gq.get(0).getTagName().toUpperCase());
    assertEquals("TR", gq.get(1).getTagName().toUpperCase());

    gq = $("<td>blop</td><td>test</td>");
    assertEquals(2, gq.size());
    assertEquals("TD", gq.get(0).getTagName().toUpperCase());
    assertEquals("TD", gq.get(1).getTagName().toUpperCase());

    gq = $("<th>blop</th><th>test</th>");
    assertEquals(2, gq.size());
    assertEquals("TH", gq.get(0).getTagName().toUpperCase());
    assertEquals("TH", gq.get(1).getTagName().toUpperCase());

    gq = $("<col/><col/>");
    assertEquals(2, gq.size());
    assertEquals("COL", gq.get(0).getTagName().toUpperCase());
    assertEquals("COL", gq.get(1).getTagName().toUpperCase());

    gq = $("<area/><area/>");
    assertEquals(2, gq.size());
    assertEquals("AREA", gq.get(0).getTagName().toUpperCase());
    assertEquals("AREA", gq.get(1).getTagName().toUpperCase());

    gq = $("<option>blop</option><option>test</option>");
    assertEquals(2, gq.size());
    assertEquals("OPTION", gq.get(0).getTagName().toUpperCase());
    assertEquals("OPTION", gq.get(1).getTagName().toUpperCase());

    gq = $("<legend>blop</legend><legend>test</legend>");
    assertEquals(2, gq.size());
    assertEquals("LEGEND", gq.get(0).getTagName().toUpperCase());
    assertEquals("LEGEND", gq.get(1).getTagName().toUpperCase());

    gq = $("<thead>blop</thead><thead>test</thead>");
    assertEquals(2, gq.size());
    assertEquals("THEAD", gq.get(0).getTagName().toUpperCase());
    assertEquals("THEAD", gq.get(1).getTagName().toUpperCase());

    //issue
    gq = $("<select name=\"modificator\"><option value=\"work\" selected=\"selected"
        +"\">Work</option><option value=\"work_fax\" >Work fax</option><option "
        +"value=\"home\" >Home</option><option value=\"other\" >Other</"
        +"option><option value=\"home_fax\" >Home fax</option><option value=\"main\" >Main</option></select>");

    assertEquals(1, gq.size());

    assertEquals("SELECT", gq.get(0).getTagName().toUpperCase());
    assertEquals(6, gq.get(0).getChildCount());


  }


  public void testNulls() {
    Assert.assertEquals(0, $((Node) null).size());
    Assert.assertEquals(0, $((Element) null).size());
    Assert.assertEquals(0, $((String) null).size());
    Assert.assertNull($((String) null).get(0));
    Assert.assertNull($((String) null).get(-1));
    Assert.assertEquals(0, $((String) null).eq(0).size());
  }

  public void testRemoveMethod(){
    String html = "<div id='parent'>parent<div id='child'>child</div></div>";
    $(e).html(html);

    Function failCallback = new Function(){
      @Override
      public void f() {
        fail("Event binding not removed");
      }
    };

    Element parent = $("#parent", e).get(0);
    Element child = $("#child", e).get(0);

    $("#child", e).data("key", "child");
    $("#child", e).click(failCallback);
    $("#parent", e).data("key", "parent");
    $("#parent", e).click(failCallback);

    $("#parent", e).remove();

    //child and the parent was removed
    assertEquals(0,$("#child", e).length());
    assertEquals(0,$("#parent", e).length());

    assertNull($(child).data("key"));
    assertNull($(parent).data("key"));
    //if failCallback is always binded, test fails...
    $(child).click();
    $(parent).click();



  }

  public void testRemoveMethodWithFilter(){
    String html = "<div id='parent'>parent<div id='child'>child</div></div>";
    $(e).html(html);

    Function failCallback = new Function(){
      @Override
      public void f() {
        fail("Event binding not removed");
      }
    };

    Function noFailCallback = new Function(){
      @Override
      public void f(Element e) {
        $(e).css(CSS.BACKGROUND_COLOR.with(RGBColor.RED));
      }
    };

    Element parent = $("#parent", e).get(0);
    Element child = $("#child", e).get(0);

    $("#child", e).data("key", "child");
    $("#child", e).click(failCallback);
    $("#parent", e).data("key", "parent");
    $("#parent", e).click(noFailCallback);

    $("div", e).remove("#child");

    //child was removed but not the parent
    assertEquals(0,$("#child", e).length());
    assertEquals(1,$("#parent", e).length());

    assertNull($(child).data("key"));
    assertEquals("parent",$(parent).data("key"));

    //if failCallback is always binded, test fails...
    $(child).click();


    $(parent).click();
    assertEquals("red", $(parent).css(CSS.BACKGROUND_COLOR, false));
  }

  public void testDataString() {
    // put something in the cache for the element
    $(e).data("initCache", "initCache");

    assertNotNull($(e).data("initCache", String.class));

    // returned the string "null" before the patch
    assertNull($(e).data("nonExistingKey", String.class));
  }

  public void testData() {

    assertEquals(null, $().data("whatever"));

    Object o = $$();

    GQuery g = $(e)
               .data("number", 3.5)
               .data("bool", true)
               .data("string", "foo")
               .data("object", o);

    Double d = g.data("number");
    assertEquals(3.5d, d);
    int i = g.data("number", Integer.class);
    assertEquals(3, i);
    long l = g.data("number", Long.class);
    assertEquals(3l, l);

    Boolean b = g.data("bool");
    assertTrue(b);

    String s = g.data("string");
    assertEquals("foo", s);
    s = g.data("bool", String.class);
    assertEquals("true", s);

    Element n = GQuery.data(e, "object");
    assertEquals(o, n);
  }

  public void testDetachMethod(){
    String html = "<div id='parent'>parent<div id='child'>child</div></div>";
    $(e).html(html);

    Function noFailCallback = new Function(){
      @Override
      public void f(Element e) {
        $(e).css(CSS.BACKGROUND_COLOR.with(RGBColor.RED));
      }
    };

    Element parent = $("#parent", e).get(0);
    Element child = $("#child", e).get(0);

    $("#child", e).data("key", "child");
    $("#child", e).click(noFailCallback);
    $("#parent", e).data("key", "parent");
    $("#parent", e).click(noFailCallback);

    GQuery $parent = $("#parent", e).detach();

    //test parent an child well detached
    assertEquals(0,$("#parent", e).length());
    assertEquals(0,$("#child", e).length());
    //test that data was not cleaned
    assertEquals("child",$(child).data("key"));
    assertEquals("parent",$(parent).data("key"));

    $(e).append($parent);

    assertEquals(1,$("#parent", e).length());
    assertEquals(1,$("#child", e).length());
    assertEquals("child",$("#child", e).data("key"));
    assertEquals("parent",$("#parent", e).data("key"));

    $("#child", e).click();
    assertEquals("red", $(child).css(CSS.BACKGROUND_COLOR, false));
    $("#parent", e).click();
    assertEquals("red", $(parent).css(CSS.BACKGROUND_COLOR, false));
  }

  public void testDetachMethodWithFilter(){
    String html = "<div id='parent'>parent<div id='child'>child</div></div>";
    $(e).html(html);

    Function noFailCallback = new Function(){
      @Override
      public void f(Element e) {
        $(e).css(CSS.BACKGROUND_COLOR.with(RGBColor.RED));
      }
    };

    Element parent = $("#parent", e).get(0);
    Element child = $("#child", e).get(0);

    $("#child", e).data("key", "child");
    $("#child", e).click(noFailCallback);
    $("#parent", e).data("key", "parent");
    $("#parent", e).click(noFailCallback);

    $("div", e).detach("#child");

    //child was removed but not the parent
    assertEquals(0,$("#child", e).length());
    assertEquals(1,$("#parent", e).length());

    //data must always exist
    assertEquals("child", $(child).data("key"));
    assertEquals("parent",$(parent).data("key"));

    $(e).append($(child));

    assertEquals(1,$("#child", e).length());
    assertEquals(1,$("#parent", e).length());

    $(child).click();
    assertEquals("red", $("#child", e).css(CSS.BACKGROUND_COLOR, false));

    $(parent).click();
    assertEquals("red", $("#parent", e).css(CSS.BACKGROUND_COLOR, false));
  }

  public void testUnwrapMethod(){
    String html = "<div class='parent'><div class='child'>child1</div><span>other child</span></div><div class='parent'><div class='child'>child2</div></div><div class='parent'><div class='child'>child3</div></div>";
    $(e).html(html);

    assertEquals(3, $(".parent", e).length());
    assertEquals(3, $(".child", e).length());

    $(".child",e).unwrap();

    assertEquals(0, $(".parent",e).length());
    assertEquals(3, $(".child",e).length());

    String expectedHtml = "<div class=\"child\">child1</div><span>other child</span><div class=\"child\">child2</div><div class=\"child\">child3</div>";

    assertEquals(expectedHtml, $(e).html());

  }

  public void testClosestMethod(){
    String html = "<div><p><div id='firstDiv'><p id='firstP'><span><input id='firstInput' type='text'></input></span></p></div></p></div>";
    $(e).html(html);

    GQuery closeP = $("input", e).closest("p,div");

    assertEquals(1, closeP.length());
    assertEquals("firstP", closeP.get(0).getId());

    GQuery closeDiv = $("input", e).closest("div");

    assertEquals(1, closeDiv.length());
    assertEquals("firstDiv", closeDiv.get(0).getId());

    GQuery closeInput = $("input", e).closest("input");

    assertEquals(1, closeInput.length());
    assertEquals("firstInput", closeInput.get(0).getId());

    GQuery closeUnknown = $("input", e).closest("h1");

    assertEquals(0, closeUnknown.length());

    GQuery closePWithContext = $("input", e).closest("p,div",$("#firstDiv",e).get(0));

    assertEquals(1, closePWithContext.length());
    assertEquals("firstP", closePWithContext.get(0).getId());

    GQuery closeDivWithContext = $("input", e).closest("div",$("#firstP",e).get(0));

    assertEquals(0, closeDivWithContext.length());

  }

  public void testClosestMethodWithArrayOfString(){

    String html = "<div id='mainDiv'><div id='subDiv' class='test'><div id='subSubDiv'><p id='mainP'><span id='testSpan' class='test'><input id='firstInput' type='text'></input></span></p></div></div></div>";
    $(e).html(html);

    JsNamedArray<NodeList<Element>> close = $("input", e).closest(new String[]{"p","div", ".test", "#unknown"});

    assertEquals(3, close.length());

    assertNotNull(close.get("p"));
    assertEquals(1,close.get("p").getLength());
    assertEquals("mainP", close.get("p").getItem(0).getId());

    assertNotNull(close.get("div"));
    assertEquals(3,close.get("div").getLength());
    assertEquals("subSubDiv", close.get("div").getItem(0).getId());
    assertEquals("subDiv", close.get("div").getItem(1).getId());
    assertEquals("mainDiv", close.get("div").getItem(2).getId());

    assertNotNull(close.get(".test"));
    assertEquals(2,close.get(".test").getLength());
    assertEquals("testSpan", close.get(".test").getItem(0).getId());
    assertEquals("subDiv", close.get(".test").getItem(1).getId());

    assertNull(close.get("#unknown"));

  }

  public void testMap() {
    String html = "<div class='d' id='6'></div>" +
    		"<span class='s' id='5'></span>" +
    		"<p class='p' id='4'></p>" +
    		"<em class='d' id='3'></em>" +
    		"<b class='s' id='2'></b>" +
    		"<i class='p' id='1'></i>" +
    		"<strong></strong>";
    $(e).html(html);

    GQuery c = $(e).children();
    assertEquals(7, c.size());

    // A list of lists containing tag,class,id, remove elements without id
    List<List<String>> m = $(e).children().map(new Function() {
      public List<String> f(Element e, int i) {
        // map does not add to the list null elements
        if ($(e).attr("id").isEmpty() || $(e).attr("class").isEmpty()) {
          return null;
        }
        List<String> attrs = new ArrayList<>();
        attrs.add(e.getTagName());
        attrs.add($(e).attr("class"));
        attrs.add($(e).attr("id"));
        return attrs;
      }
    });
    assertEquals(6, m.size());

    // Sort the list by id
    assertEquals("div", m.get(0).get(0).toLowerCase());
    assertEquals("i", m.get(5).get(0).toLowerCase());
    Collections.sort(m, new Comparator<List<String>>() {
      public int compare(List<String> o1, List<String> o2) {
        return o1.get(2).compareTo(o2.get(2));
      }
    });
    assertEquals("div", m.get(5).get(0).toLowerCase());
    assertEquals("i", m.get(0).get(0).toLowerCase());
  }

  public void testWindowSize() {
    GQuery w = $(GQuery.window);
    assertTrue(w.width() > 0);
    assertTrue(w.height() > 0);
  }

  public void testFunction() {
    $(e).html("<div id=fid>0</div>");
    GQuery g = $("#fid", e);
    assertEquals("0", g.text());

    // EACH
    g.each(new Function() {
      @Override
      public void f(com.google.gwt.user.client.Element e) {
        $(e).text("U");
      }
    });
    assertEquals("U", g.text());
    g.each(new Function() {
      @Override
      public void f(com.google.gwt.dom.client.Element e) {
        $(e).text("D");
      }
    });
    assertEquals("D", g.text());
    g.each(new Function() {
      @Override
      public Object f(com.google.gwt.user.client.Element e, int idx) {
        $(e).text("U" + idx);
        return "";
      }
    });
    assertEquals("U0", g.text());
    g.each(new Function() {
      @Override
      public Object f(com.google.gwt.user.client.Element e, int idx) {
        $(e).text("D" + idx);
        return "";
      }
    });
    assertEquals("D0", g.text());

    // EVENTS
    g.unbind(Event.ONCLICK).click(new Function(){
      @Override
      public void f(com.google.gwt.user.client.Element e) {
        $(e).text("U");
      }
    }).click();
    assertEquals("U", g.text());
    g.unbind(Event.ONCLICK).click(new Function(){
      @Override
      public void f(com.google.gwt.dom.client.Element e) {
        $(this).text("D");
      }
    }).click();
    assertEquals("D", g.text());
    g.unbind(Event.ONCLICK).click(new Function(){
      @Override
      public boolean f(Event e) {
        $(this).text("E");
        return false;
      }
    }).click();
    assertEquals("E", g.text());
    g.unbind(Event.ONCLICK).bind(Event.ONCLICK, "D", new Function(){
      @Override
      public boolean f(Event e, Object... o) {
        $(e).text("E" + o[0]);
        return false;
      }
    }).click();
    assertEquals("ED", g.text());

    // ELEMENTS AND WIDGETS
    Label label = new Label("1");
    RootPanel.get().add(label);
    g = $("#fid, .gwt-Label");
    assertEquals(2, g.size());

    g.each(new Function() {
      @Override
      public void f(com.google.gwt.user.client.Element e) {
        $(e).text("U");
      }
    });
    assertEquals("UU", g.text());
    g.each(new Function() {
      @Override
      public void f(com.google.gwt.dom.client.Element e) {
        $(e).text("D");
      }
    });
    assertEquals("DD", g.text());

    g.each(new Function() {
      @Override
      public void f(com.google.gwt.user.client.Element e) {
        $(e).text("U");
      }
      @Override
      public void f(Widget w) {
        $(w).text("W");
      }
    });
    assertEquals("UW", g.text());
    g.each(new Function() {
      @Override
      public void f(com.google.gwt.dom.client.Element e) {
        $(e).text("D");
      }
      @Override
      public void f(Widget w) {
        $(w).text("W");
      }
    });
    assertEquals("DW", g.text());

    g.each(new Function() {
      @Override
      public Object f(com.google.gwt.user.client.Element e, int idx) {
        $(e).text("U" + idx);
        return "";
      }
    });
    assertEquals("U0U1", g.text());
    g.each(new Function() {
      @Override
      public Object f(com.google.gwt.user.client.Element e, int idx) {
        $(e).text("D" + idx);
        return "";
      }
    });
    assertEquals("D0D1", g.text());

    g.each(new Function() {
      @Override
      public Object f(com.google.gwt.user.client.Element e, int idx) {
        $(e).text("U" + idx);
        return "";
      }
      @Override
      public Object f(Widget w, int idx) {
        $(w).text("W" + idx);
        return "";
      }
    });
    assertEquals("U0U1", g.text());
    g.each(new Function() {
      @Override
      public Object f(com.google.gwt.dom.client.Element e, int idx) {
        $(e).text("D" + idx);
        return "";
      }
      @Override
      public Object f(Widget w, int idx) {
        $(w).text("W" + idx);
        return "";
      }
    });
    assertEquals("D0D1", g.text());

    g.each(new Function() {
      @Override
      public void f(com.google.gwt.user.client.Element e) {
        $(e).text("U");
      }
      @Override
      public Object f(Widget w, int idx) {
        $(w).text("W" + idx);
        return "";
      }
    });
    assertEquals("UW1", g.text());
    g.each(new Function() {
      @Override
      public void f(com.google.gwt.dom.client.Element e) {
        $(e).text("D");
      }
      @Override
      public Object f(Widget w, int idx) {
        $(w).text("W" + idx);
        return "";
      }
    });
    assertEquals("DW1", g.text());

    label.removeFromParent();
  }

  public void testXpathSelector() {
    $(e).html("<table border=1 id=idtest width=440><tr><td width=50%>A Text</td><td width=50%>B</td></tr></table>");
    SelectorEngineCssToXPath s = new SelectorEngineCssToXPath();
    for (String selector : Arrays.asList("td[width]", "table > tr > td", "*[width!='440']")) {
      String xselector = s.css2Xpath(selector);
      assertEquals($(selector).size(), $(xselector).size());
    }
  }

  public void testIssue81(){
    GQuery test = $("     <div>blop</div><!-- comment --> <p>test2</p>    ");
    test.addClass("test");
    test.removeClass("test");
  }

  public void testHas() {
    $(e).html("<ul>"
              +"<li>list item 1</li>"
              +"<li id='l2'>list item 2"
              +" <ul>"
              +"  <li>list item 2-a</li>"
              +"  <li>list item 2-b</li>"
              +" </ul>"
              +"</li>"
              +"<li id='l3'>list item 3 <span>span</span>"
              +"</li>"
              +"<li>list item 4</li>"
              +"</ul>");
    assertEquals("", $("#l2").css("background-color", false));
    $("li", e).has("ul").css("background-color", "red");
    assertEquals("red", $("#l2").css("background-color", false));

    Element span = $("span", e).get(0);
    assertEquals("l3", $("li", e).has(span).id());
  }

  public void testDetachedElement(){
    GQuery view = $("<div id='view' style='width: 300px;'><div style='width: 50%'></div></div>");

    int viewWidth = view.width();

    assertEquals(300, viewWidth);

    int innerViewWidth = view.children().width();

    assertEquals(150, innerViewWidth);
  }

  public void testParentAndSelf() {
    $(e).html("<ul><li><span>test</span></li></ul>");

    Element span = $("span", e).get(0);
    Element li = $("li", e).get(0);
    int ulParentsNumber = $("ul", e).parents().size();

    assertNotNull(span);
    assertNotNull(li);

    GQuery result = $(span).parents().andSelf();

    assertEquals(ulParentsNumber + 3, result.size());

    result = $(li).parents().andSelf();

    assertEquals(ulParentsNumber + 2, result.size());
  }

  // issue #216 : https://github.com/gwtquery/gwtquery/issues/216
  public void testDataAsInteger() {
    $(e).html("<div id='target'></div>");

    GQuery target = $("#target", e);

    $("#target", e).data("intValue", 1);

    Integer value = $("#target", e).data("intValue", Integer.class);

    assertEquals(1, value.intValue());
  }
}
