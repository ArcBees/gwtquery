package com.google.gwt.query.client;

import static com.google.gwt.query.client.GQuery.$;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.Properties;
import com.google.gwt.query.client.Function;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Test class for testing gwtquery-core api.
 */
public class GwtQueryCoreTest extends GWTTestCase {

    public String getModuleName() {
        return "com.google.gwt.query.Query";
    }

    static HTML testPanel = null;
    static Element e = null;

    public void gwtSetUp() {
        if (e == null) {
            testPanel = new HTML();
            RootPanel.get().add(testPanel);
            e = testPanel.getElement();
            e.setId("tst");
        } else {
            e.setInnerHTML("");
        }
    }

    public void testBrowserStartUp() {
        // just a test for seeing in eclipse that GWTTestCase internal browser is starting
        assertTrue(true);
    }

    public void testInnerMethods() {
        String txt = "<p>I would like to say: </p>";

        // Check that setHTML and getHTML works as GQuery html()
        testPanel.setHTML(txt);
        assertEquals(txt, testPanel.getHTML());
        assertEquals(txt, $(e).html());
        assertEquals(txt, $("#tst").html());
        $(e).html("");
        assertEquals("", $(e).html());
        $(e).html(txt);
        assertEquals(txt, $(e).html());

        // toString()
        assertEquals(txt, $("p", e).toString());

        // remove()
        $("p", e).remove();
        assertEquals("", $(e).html());

        // text()
        String expected = "I would like to say: I would like to say: ";
        $(e).html(txt + txt);
        assertEquals(expected, $("p", e).text());

        // empty()
        expected = "<p></p><p></p>";
        $("p", e).empty();
        assertEquals(expected, $(e).html());
    }

    public void testAttributeMethods() {

        $(e).html("<p class=\"a1\">Content</p>");
        GQuery gq = $("p", e);
        
        // attr()
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

        // css()
        String content = "<p style='color:red;'>Test Paragraph.</p>";
        $(e).html(content);
        assertEquals("red", $("p", e).css("color"));
        $("p", e).css("font-weight", "bold");
        assertEquals("bold", $("p", e).css("font-weight"));

        // css() properties
        $(e).html("<p>Test Paragraph.</p>");
        $("p", e).css(Properties.create("color: 'red', 'font-weight': 'bold', background: 'blue'"));
        assertEquals("red", $("p", e).css("color"));
        assertEquals("bold", $("p", e).css("font-weight"));
        assertEquals("blue", $("p", e).css("background-color"));

        // css() camelize and uppercase
        $(e).html("<p>Test Paragraph.</p>");
        $("p", e).css(Properties.create("COLOR: 'red', 'FONT-WEIGHT': 'bold'"));
        assertEquals("red", $("p", e).css("color"));
        assertEquals("", $("p", e).css("background"));

    }

    public void testSliceMethods() {
        String content = "<p>This is just a test.</p><p>So is this</p>";
        $(e).html(content);

        String expected = "<p>So is this</p>";
        assertEquals(1, $("p", e).eq(1).size());
        assertEquals(expected, $("p", e).eq(1).toString());

        expected = "<p>This is just a test.</p>";
        assertEquals(1, $("p", e).lt(1).size());
        assertEquals(expected, $("p", e).lt(1).toString());

        expected = "<p>So is this</p>";
        assertEquals(1, $("p", e).gt(0).size());
        assertEquals(expected, $("p", e).gt(0).toString());

        assertEquals(2, $("p", e).slice(0, 2).size());
        assertEquals(2, $("p", e).slice(0, -1).size());
        assertEquals(0, $("p", e).slice(3, 2).size());
    }

    public void testRelativeMethods() {
        String content = "<p><span>Hello</span>, how are you?</p>";
        String expected = "<span>Hello</span>";

        // find()
        $(e).html(content);
        assertEquals(expected, $("p", e).find("span").toString());

        // filter()
        content = "<p>First</p><p class=\"selected\">Hello</p><p>How are you?</p>";
        $(e).html(content);
        expected = "<p class=\"selected\">Hello</p>";
        assertEquals(expected, $("p", e).filter(".selected").toString());

        // filter()
        // Commented because GQuery doesn't support this syntax yet
        // expected = "<p class=\"selected\">Hello</p>";
        // assertEquals(expected, $("p", e).filter(".selected, :first").toString());

        // not()
        expected = "<p>First</p><p>How are you?</p>";
        assertEquals(2, $("p", e).not(".selected").size());
        assertEquals(expected, $("p", e).not(".selected").toString());
        assertEquals(2, $("p", e).not($(".selected")).size());
        assertEquals(expected, $("p", e).not($(".selected")).toString());
        assertEquals(2, $("p", e).not($(".selected").get(0)).size());
        assertEquals(expected, $("p", e).not($(".selected").get(0)).toString());

        // add()
        String added = "<p>Last</p>";
        expected = content + added;
        assertEquals(4, $("p", e).add(added).size());
        assertEquals(expected, $("p", e).add(added).toString());

        // parent()
        expected = content = "<div><p>Hello</p><p>Hello</p></div>";
        $(e).html(content);
        assertEquals(expected, $("p", e).parent().toString());

        // parent()
        content = "<div><p>Hello</p></div><div class=\"selected\"><p>Hello Again</p></div>";
        expected = "<div class=\"selected\"><p>Hello Again</p></div>";
        $(e).html(content);
        assertEquals(expected, $("p", e).parent(".selected").toString());

        // parents()
        content = "<div><p><span>Hello</span></p><span>Hello Again</span></div>";
        $(e).html(content);
        assertEquals(2, $("span", e).size());
        assertTrue(3 < $("span", e).parents().size());
        assertEquals(1, $("span", e).parents().filter("body").size());
        $("span", e).parents().filter("body").toString().contains(content);

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
        assertEquals(next1, $("p", e).next().get(0).getString());
        assertEquals(next2, $("p", e).next().get(1).getString());

        // next()
        content = "<p>Hello</p><p class=\"selected\">Hello Again</p><div><span>And Again</span></div>";
        expected = "<p class=\"selected\">Hello Again</p>";
        $(e).html(content);
        assertEquals(1, $("p", e).next(".selected").size());
        assertEquals(expected, $("p", e).next(".selected").get(0).getString());

        // prev()
        content = "<p>Hello</p><div><span>Hello Again</span></div><p>And Again</p>";
        expected = "<div><span>Hello Again</span></div>";
        $(e).html(content);
        assertEquals(1, $("p", e).prev().size());
        assertEquals(expected, $("p", e).prev().get(0).getString());

        // prev()
        content = "<div><span>Hello</span></div><p class=\"selected\">Hello Again</p><p>And Again</p>";
        expected = "<p class=\"selected\">Hello Again</p>";
        $(e).html(content);
        assertEquals(2, $("p", e).prev().size());
        assertEquals(1, $("p", e).prev(".selected").size());
        assertEquals(expected, $("p", e).prev(".selected").get(0).getString());

        // siblings()
        content = "<p>Hello</p><div><span>Hello Again</span></div><p>And Again</p>";
        next1 = "<p>Hello</p>";
        next2 = "<p>And Again</p>";
        $(e).html(content);
        assertEquals(2, $("div", e).siblings().size());
        assertEquals(next1, $("div", e).siblings().get(0).getString());
        assertEquals(next2, $("div", e).siblings().get(1).getString());

        // siblings()
        content = "<div><span>Hello</span></div><p class=\"selected\">Hello Again</p><p>And Again</p>";
        expected = "<p class=\"selected\">Hello Again</p>";
        $(e).html(content);
        assertEquals(1, $("p", e).siblings(".selected").size());
        assertEquals(expected, $("p", e).siblings(".selected").get(0).getString());

        // children()
        content = "<p>Hello</p><div><span>Hello Again</span></div><p>And Again</p>";
        expected = "<span>Hello Again</span>";
        $(e).html(content);
        assertEquals(expected, $("div", e).children().toString());

        // children()
        content = "<div><span>Hello</span><p class=\"selected\">Hello Again</p><p>And Again</p></div>";
        expected = "<p class=\"selected\">Hello Again</p>";
        $(e).html(content);
        assertEquals(expected, $("div", e).children(".selected").toString());

        // contains()
        content = "<p>This is just a test.</p><p>So is this</p>";
        expected = "<p>This is just a test.</p>";
        $(e).html(content);
        assertEquals(expected, $("p", e).contains("test").toString());
    }

    public void testModifyMethods() {
        String p_txt = "<p>I would like to say: </p>";
        String b_txt = "<b>Hello</b>";

        // append()
        String expected = "<p>I would like to say: <b>Hello</b></p>";
        $(e).html(p_txt);
        $("p", e).append(b_txt);
        assertEquals(expected, $(e).html());

        // appendTo()
        expected = "<p>I would like to say: <b>Hello</b></p>";
        $(e).html(b_txt + p_txt);
        $("b", e).appendTo($("p", e));
        assertEquals(expected, $(e).html());

        // prepend()
        expected = "<p><b>Hello</b>I would like to say: </p>";
        $(e).html(p_txt);
        $("p", e).prepend(b_txt);
        assertEquals(expected, $(e).html());

        // prependTo()
        expected = "<p><b>Hello</b>I would like to say: </p>";
        $(e).html(b_txt + p_txt);
        $("b", e).prependTo($("p", e));
        assertEquals(expected, $(e).html());

        // prependTo()
        expected = "<b>Hello</b><p><b>Hello</b>I would like to say: </p>";
        $(e).html(b_txt + p_txt);
        $("b", e).clone().prependTo($("p", e));
        assertEquals(expected, $(e).html());

        // before()
        expected = "<b>Hello</b><p>I would like to say: </p>";
        $(e).html(p_txt);
        $("p", e).before(b_txt);
        assertEquals(expected, $(e).html());

        // before()
        expected = "<b>Hello</b><p>I would like to say: </p>";
        $(e).html(p_txt + b_txt);
        $("p", e).before($("b", e));
        assertEquals(expected, $(e).html());

        // before()
        expected = "<b>Hello</b><p>I would like to say: </p><b>Hello</b>";
        $(e).html(p_txt + b_txt);
        $("p", e).before($("b", e).clone());
        assertEquals(expected, $(e).html());

        // insertBefore()
        expected = "<b>Hello</b><p>I would like to say: </p>";
        $(e).html(p_txt + b_txt);
        $("b", e).insertBefore($("p", e));
        assertEquals(expected, $(e).html());

        // insertBefore()
        expected = "<b>Hello</b><p>I would like to say: </p><b>Hello</b>";
        $(e).html(p_txt + b_txt);
        $("b", e).clone().insertBefore($("p", e));
        assertEquals(expected, $(e).html());

        // after()
        expected = "<p>I would like to say: </p><b>Hello</b>";
        $(e).html(p_txt);
        $("p", e).after(b_txt);
        assertEquals(expected, testPanel.getHTML());

        // after()
        expected = "<p>I would like to say: </p><b>Hello</b>";
        $(e).html(b_txt + p_txt);
        $("p", e).after($("b", e));
        assertEquals(expected, $(e).html());

        // after()
        expected = "<b>Hello</b><p>I would like to say: </p><b>Hello</b>";
        $(e).html(b_txt + p_txt);
        $("p", e).after($("b", e).clone());
        assertEquals(expected, $(e).html());
    }

    public void testWrapMethod() {
        String content = "<p>Test Paragraph.</p><div id=\"content\">Content</div>";

        String expected = "<div id=\"content\">Content<p>Test Paragraph.</p></div>";
        $(e).html(content);

        $("p", e).wrap($("div", e).get(0));
        assertEquals(expected, $(e).html());

        expected = "<b><p>Test Paragraph.</p></b><b><div id=\"content\">Content</div></b>";
        $(e).html(content);
        $("*", e).wrap("<b></b>");
        assertEquals(expected, $(e).html());
    }

    public void testInputValueMethods() {
        // imput text
        $(e).html("<input type='text'/>");
        GQuery gq = $("input", e);
        assertEquals("", gq.val());
        gq.val("some value");
        assertEquals("some value", gq.val());
        
        // select
        $(e).html("<select name='n'><option value='v1'>1</option><option value='v2' selected='selected'>2</option></select>");
        gq = $("select", e);
        assertEquals("v2", gq.val());
        gq.val("v1");
        assertEquals("v1", gq.val());
        
        // select multiple
        $(e).html("<select name='n' multiple='multiple'><option value='v1'>1</option><option value='v2' selected='selected'>2</option><option value='v3'>3</option></select>");
        gq = $("select", e);
        gq.val("v1","v3","invalid");
        assertEquals(2, gq.vals().length);
        assertEquals("v1", gq.vals()[0]);
        assertEquals("v3", gq.vals()[1]);
        gq.val("v1");
        assertEquals(1, gq.vals().length);
        assertEquals("v1", gq.val());

        // input radio
        $(e).html("<input type='radio' name='n' value='v1'>1</input><input type='radio' name='n' value='v2' checked='checked'>2</input>");
        gq = $("input", e);
        assertEquals("v2", gq.val());
        gq.val("v1");
        assertEquals("v1", gq.val());
        gq.val("v2");
        assertEquals("v2", gq.val());
        
        // input checkbox
        $(e).html("<input type='checkbox' name='n1' value='v1'>1</input><input type='checkbox' name='n2' value='v2' checked='checked'>2</input>");
        gq = $("input", e);
        assertEquals("", gq.val());
        gq.val("v1");
        assertEquals("v1", gq.val());
    }

    public void testEventsPlugin() {
        $(e).html("<p>Content</p>");

        // click
        $("p", e).click(new Function() {
            public void f(Element elem) {
                $(elem).css("color", "red");
            }
        });
        $("p", e).trigger(Event.ONCLICK);
        assertEquals("red", $("p", e).css("color"));

        // unbind
        $("p", e).css("color", "");
        $("p", e).unbind(Event.ONCLICK);
        $("p", e).trigger(Event.ONCLICK);
        assertEquals("rgb(0, 0, 0)", $("p", e).css("color"));

        // one
        $("p", e).one(Event.ONCLICK, null, new Function() {
            public void f(Element elem) {
                $(elem).css("color", "red");
            }
        });
        $("p", e).trigger(Event.ONCLICK);
        assertEquals("red", $("p", e).css("color"));
        $("p", e).css("color", "");
        $("p", e).trigger(Event.ONCLICK);
        assertEquals("rgb(0, 0, 0)", $("p", e).css("color"));

        // hover (mouseover, mouseout)
        $("p", e).hover(new Function() {
            public void f(Element elem) {
                $(elem).css("background-color", "yellow");
            }
        }, new Function() {
            public void f(Element elem) {
                $(elem).css("background-color", "");
            }
        });
        $("p", e).trigger(Event.ONMOUSEOVER);
        assertEquals("yellow", $("p", e).css("background-color"));
        $("p", e).trigger(Event.ONMOUSEOUT);
        assertEquals("rgba(0, 0, 0, 0)", $("p", e).css("background-color"));

        // focus
        $("p", e).focus(new Function() {
            public void f(Element elem) {
                $(elem).css("border", "1px dotted black");
            }
        });
        $("p", e).trigger(Event.ONFOCUS);
        assertEquals("1px dotted black", $("p", e).css("border"));

        // blur
        $("p", e).blur(new Function() {
            public void f(Element elem) {
                $(elem).css("border", "");
            }
        });
        $("p", e).trigger(Event.ONBLUR);
        assertEquals("", $("p", e).css("border"));

        // keypressed
        $(e).html("<input type='text'/>");
        $("input", e).keypressed(new Function() {
            public boolean f(Event evnt) {
                Element elem = evnt.getCurrentTarget();
                InputElement input = InputElement.as(elem);
                input.setValue(input.getValue() + Character.toString((char) evnt.getKeyCode()));
                return false;
            }
        });
        $("input", e).trigger(Event.ONFOCUS);
        $("input", e).trigger(Event.ONKEYPRESS, 'a');
        assertEquals("a", InputElement.as($("input", e).get(0)).getValue());

    }

    public void testEffectsPlugin() {
        $(e).html("<p id='id1'>Content 1</p><p id='id2'>Content 2</p><p id='id3'>Content 3</p>");

        final GQuery sect_a = $("#id1");
        final GQuery sect_b = $("#id2");
        final GQuery sect_c = $("#id3");
        
         // hide()
        sect_a.hide();
        assertEquals("none", sect_a.css("display"));

        // show()
        sect_b.show();
        assertEquals("", sect_b.css("display"));

        // toggle()
        assertEquals("", sect_c.css("display"));
        sect_c.toggle();
        assertEquals("none", sect_c.css("display"));
        sect_c.toggle();
        assertEquals("", sect_c.css("display"));

        // fadeIn() & fadeOut() are tested with delayed assertions
        sect_a.fadeIn(2000);
        sect_b.fadeOut(2000);
        
        // Configure the max duration for this test
        // If the test exceeds the timeout without calling finishTest() it will fail
        delayTestFinish(2500);
        
        // Delayed assertions at different intervals
        Timer timer_shortTime = new Timer() {
            public void run() {
                double o = Double.valueOf(sect_a.css("opacity"));
                assertTrue(o > 0 && o < 0.5);
                o = Double.valueOf(sect_b.css("opacity"));
                assertTrue(o > 0.5 && o < 1);
            }
        };
        Timer timer_midTime = new Timer() {
            public void run() {
                assertEquals("", sect_a.css("display"));
                assertEquals("", sect_b.css("display"));
                double o = Double.valueOf(sect_a.css("opacity"));
                assertTrue(o > 0.5 && o < 1);
                o = Double.valueOf(sect_b.css("opacity"));
                assertTrue(o > 0 && o < 0.5);
            }
        };
        Timer timer_longTime = new Timer() {
            public void run() {
                assertEquals("", sect_a.css("display"));
                assertEquals("none", sect_b.css("display"));
                // Last delayed assertion has to stop the test to avoid a timeout failure
                finishTest();
            }
        };
        // schedule the delayed assertions
        timer_shortTime.schedule(200);
        timer_midTime.schedule(1200);
        timer_longTime.schedule(2200);

   }

}
