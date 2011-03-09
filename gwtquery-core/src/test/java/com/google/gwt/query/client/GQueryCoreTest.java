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
import static com.google.gwt.query.client.plugins.Widgets.Widgets;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.query.client.css.CSS;
import com.google.gwt.query.client.css.RGBColor;
import com.google.gwt.query.client.impl.SelectorEngineImpl;
import com.google.gwt.query.client.impl.SelectorEngineSizzle;
import com.google.gwt.query.client.js.JsNodeArray;
import com.google.gwt.query.client.js.JsUtils;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;

import junit.framework.Assert;

import java.util.List;

/**
 * Test class for testing gwtquery-core api.
 */
public class GQueryCoreTest extends GWTTestCase {

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
    $("p", e).css(
        Properties.create("color: 'red', 'font-weight': 'bold', background: 'blue'"));
    assertEquals("red", $("p", e).css("color"));
    assertEquals("bold", $("p", e).css("font-weight"));
    assertEquals("blue", $("p", e).css("background-color"));

    // css() camelize and uppercase
    $(e).html("<p>Test Paragraph.</p>");
    $("p", e).css(Properties.create("COLOR: 'red', 'FONT-WEIGHT': 'bold'"));
    assertEquals("red", $("p", e).css("color"));
    assertEquals("", $("p", e).css("background"));
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
    // gq.val("v1");
    // assertEquals(1, gq.vals().length);
    // assertEquals("v1", gq.val());

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
    assertEquals("white", $("#mid").css("color"));

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
    assertEquals("yellow", $("#mid").css("color"));

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

    $(g1).append(g2);
    assertEquals(1, g1.size());
    assertEquals(1, g2.size());
    assertEquals(expected, g2.toString());

    $(g1).prepend(g2);
    assertEquals(1, g1.size());
    assertEquals(1, g2.size());
    assertEquals(expected, g2.toString());

    $(g1).after(g2);
    assertEquals(1, g1.size());
    assertEquals(1, g2.size());
    assertEquals(expected, g2.toString());

    $(g1).before(g2);
    assertEquals(1, g1.size());
    assertEquals(1, g2.size());
    assertEquals(expected, g2.toString());
  }

  public void testOpacity() {
    $(e).html(
        "<p id='id1' style='opacity: 0.6; filter: alpha(opacity=60)'>Content 1</p>");
    GQuery g = $("#id1");
    assertEquals("0.6", g.css("opacity", false));
    assertEquals("0.6", g.css("opacity", true));
    g.css("opacity", "");
    assertEquals("", g.css("opacity", false));
    assertEquals("1.0", g.css("opacity", true));
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
    content = "<div><span>Hello</span></div><p class=\"selected\">Hello Again</p><p>And Again</p>";
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
    assertEquals("red", $("h3", e).css(CSS.COLOR));


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
    assertEquals("none", sectA.css("display"));
    sectB.hide();
    assertEquals("none", sectB.css("display"));

    // show()
    sectA.show();
    assertEquals("inline", sectA.css("display"));
    sectB.show();
    assertEquals("", sectB.css("display"));

    // toggle()
    assertEquals("", sectC.css("display"));
    sectC.toggle();
    assertEquals("none", sectC.css("display"));
    sectC.toggle();
    assertEquals("block", sectC.css("display"));
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

  public void testWidthHeight() {
    $(e).html(
        "<div style='border: 1px solid red; padding: 10px; margin:10px; width: 100px; height: 100px'>Content 1</div>");
    GQuery g = $("div", e);
    assertEquals(100, g.width());
    assertEquals(100, g.height());
    assertEquals(120, g.innerWidth());
    assertEquals(120, g.innerHeight());
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

    b2 = $("<button>Click-me</button>").appendTo(document).as(Widgets).button().widget();
    b2.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        $(b1).css("color", "red");
      }
    });

    (b2).click();
    assertEquals("red", $(b1).css("color"));

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
    
    Button b2 = new Button("b");
    $(e).append($(b2));
    assertTrue(b2.isAttached());
    
    $(e).empty();
    
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

  public void testNulls() {
    Assert.assertEquals(0, $((Node) null).size());
    Assert.assertEquals(0, $((Element) null).size());
    Assert.assertEquals(0, $((String) null).size());
    Assert.assertNull($((String) null).get(0));
    Assert.assertNull($((String) null).get(-1));
    Assert.assertEquals(0, $((String) null).eq(0).size());
  }

}
