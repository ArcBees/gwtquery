/*
 * Copyright 2011, The gwtquery team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.gwt.query.client;

import static com.google.gwt.query.client.GQuery.$;
import static com.google.gwt.query.client.GQuery.document;
import static com.google.gwt.query.client.GQuery.lazy;
import static com.google.gwt.user.client.Event.FOCUSEVENTS;
import static com.google.gwt.user.client.Event.KEYEVENTS;
import static com.google.gwt.user.client.Event.MOUSEEVENTS;
import static com.google.gwt.user.client.Event.ONBLUR;
import static com.google.gwt.user.client.Event.ONFOCUS;
import static com.google.gwt.user.client.Event.ONKEYDOWN;
import static com.google.gwt.user.client.Event.ONKEYPRESS;
import static com.google.gwt.user.client.Event.ONKEYUP;
import static com.google.gwt.user.client.Event.ONMOUSEDOWN;
import static com.google.gwt.user.client.Event.ONMOUSEMOVE;
import static com.google.gwt.user.client.Event.ONMOUSEOUT;
import static com.google.gwt.user.client.Event.ONMOUSEOVER;
import static com.google.gwt.user.client.Event.ONMOUSEUP;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.junit.DoNotRunWith;
import com.google.gwt.junit.Platform;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.query.client.css.CSS;
import com.google.gwt.query.client.css.Length;
import com.google.gwt.query.client.css.RGBColor;
import com.google.gwt.query.client.plugins.Events;
import com.google.gwt.query.client.plugins.events.EventsListener;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Test class for testing gwt events plugin api.
 */
public class GQueryEventsTestGwt extends GWTTestCase {

  private static class CounterFunction extends Function {
    private int invokationCounter;

    @Override
    public void f() {
      invokationCounter++;
    }
  }

  static Element e = null;

  static HTML testPanel = null;

  int testSubmitEventCont = 0;

  public String getModuleName() {
    return "com.google.gwt.query.QueryTest";
  }

  public void gwtSetUp() {
    if (e == null) {
      testPanel = new HTML();
      RootPanel.get().add(testPanel);
      e = testPanel.getElement();
      e.setId("evnt-tst");
    } else {
      EventsListener.clean(e);
      e.setInnerHTML("");
    }
  }

  public void gwtTearDown() {
    $(e).remove();
    e = null;
  }

  public void testBindingWithNameSpace() {
    String content = "<input type='text' id='test'></div>";
    $(e).html(content);

    $("#test", e)
        .bind(
            "focus.focusevents blur.focusevents keydown.keyevents keypress.keyevents keyup.keyevents "
                + "mousedown.mouseevents mouseup.mouseevents mousemove.mouseevents mouseover.mouseevents "
                + "mouseout.mouseevents", null, new Function() {
              @Override
              public void f() {
                $("#test", e).val("event fired");
              }
            });

    int allEventbits[] =
        new int[] {
            ONFOCUS, ONBLUR, ONKEYDOWN, ONKEYPRESS, ONKEYUP, ONMOUSEDOWN, ONMOUSEUP, ONMOUSEMOVE,
            ONMOUSEOVER, ONMOUSEOUT};

    for (int eventbits : allEventbits) {
      $("#test", e).trigger(eventbits, 'c');

      assertEquals("event fired", $("#test", e).val());
      $("#test", e).val("");

    }

    // test unbind without namespace
    $("#test", e).unbind("focus blur");

    for (int eventbits : allEventbits) {
      $("#test", e).trigger(eventbits, 'c');

      if (eventbits != ONFOCUS && eventbits != ONBLUR) {
        assertEquals("event fired", $("#test", e).val());
        $("#test", e).val("");
      } else {
        assertEquals("", $("#test", e).val());
      }

    }

    // test unbind event name + namespace
    $("#test", e).unbind("keydown.keyevents keypress.keyevents keyup.keyevents");

    for (int eventbits : allEventbits) {
      $("#test", e).trigger(eventbits, 'c');

      if ((eventbits & MOUSEEVENTS) == eventbits) {
        assertEquals("event fired", $("#test", e).val());
        $("#test", e).val("");
      } else {
        assertEquals("", $("#test", e).val());
      }
    }

    // test unbind only on namespace
    $("#test", e).unbind(".mouseevents");

    for (int eventbits : allEventbits) {
      $("#test", e).trigger(eventbits, 'c');

      assertEquals("", $("#test", e).val());

    }
  }

  public void testBindUnbindSubmitEvent() {
    // Add a form and an iframe to the dom. The form target is the iframe
    $(e).html(
        "<form action='whatever' target='miframe'><input type='text' value='Hello'><input type='submit' value='Go'></form><iframe name='miframe' id='miframe' src=\"javascript:''\">");
    testSubmitEventCont = 0;

    // Add an onsubmit function to the form returning false to cancel the action
    $("form").bind("submit", null, new Function() {
      public boolean f(Event e) {
        testSubmitEventCont++;
        return false;
      }
    });

    // Check that the onsubmit function is called and the iframe has not changed
    $("form").submit();
    assertEquals(1, testSubmitEventCont);

    // Remove the binding
    $("form").unbind("submit");

    // Check that on submit function is not called and the form has been
    // submitted
    $("form").submit();
    assertEquals(1, testSubmitEventCont);
  }

  public void testDelegate() {

    $(e).html(
        "<div class='mainDiv'><div class='subDiv'>Content 0<span>blop</span></div></div><div class='mainDiv'><div class='subDiv'>Content 0<span>blop</span></div></div>");

    $(".mainDiv", e).delegate(".subDiv", "click", new Function() {
      @Override
      public void f(Element e) {
        $(e).css(CSS.COLOR.with(RGBColor.RED));
      }
    });

    $(".mainDiv", e).delegate(".subDiv", Event.ONMOUSEOVER, new Function() {
      @Override
      public void f(Element e) {
        $(e).css(CSS.BACKGROUND_COLOR.with(RGBColor.YELLOW));
      }
    });

    for (Element mainDiv : $(".mainDiv", e).elements()) {
      for (int i = 0; i < 3; i++) {
        String html = "<div class='subDiv'>Content " + i + "<span>blop</span></div>";
        $(mainDiv).append(html);
      }
    }

    assertEquals(8, $(".subDiv", e).length());

    $("span", e).click().trigger(Event.ONMOUSEOVER);

    for (Element el : $(".subDiv", e).elements()) {
      assertEquals("red", $(el).css(CSS.COLOR, false));
      assertEquals("yellow", $(el).css(CSS.BACKGROUND_COLOR, false));
    }

  }

  public void testDie() {
    $(e).html("<div id='div1'>content</div>");
    $(".clickMe", e).live("click", new Function() {
      public void f(Element e) {
        $(e).css(CSS.COLOR.with(RGBColor.RED));
      }
    });

    $(".clickMe", e).live("dblclick", new Function() {
      public void f(Element e) {
        $(e).css(CSS.COLOR.with(RGBColor.BLUE));
      }
    });

    $("#div1", e).addClass("clickMe");

    $("#div1", e).click();
    assertEquals(RGBColor.RED.getCssName(), $("#div1", e).css(CSS.COLOR, false));

    $("#div1", e).dblclick();
    assertEquals(RGBColor.BLUE.getCssName(), $("#div1", e).css(CSS.COLOR, false));

    // reset
    $("#div1", e).css(CSS.COLOR.with(RGBColor.BLACK));

    $(".clickMe", e).die("click");
    $("#div1", e).click();
    assertEquals(RGBColor.BLACK.getCssName(), $("#div1", e).css(CSS.COLOR, false));

    $("#div1", e).dblclick();
    assertEquals(RGBColor.BLUE.getCssName(), $("#div1", e).css(CSS.COLOR, false));

    // reset
    $("#div1", e).css(CSS.COLOR.with(RGBColor.BLACK));

    $(".clickMe", e).die("dblclick");

    $("#div1", e).dblclick();
    assertEquals(RGBColor.BLACK.getCssName(), $("#div1", e).css(CSS.COLOR, false));

  }

  public void testDie2() {
    $(e).html("<div id='div1'>content</div>");
    $(".clickMe", e).live("click", new Function() {
      public void f(Element e) {
        $(e).css(CSS.COLOR.with(RGBColor.RED));
      }
    });

    $(".clickMe", e).live("dblclick", new Function() {
      public void f(Element e) {
        $(e).css(CSS.COLOR.with(RGBColor.BLUE));
      }
    });

    $("#div1", e).addClass("clickMe");

    $("#div1", e).click();
    assertEquals(RGBColor.RED.getCssName(), $("#div1", e).css(CSS.COLOR, false));

    $("#div1", e).dblclick();
    assertEquals(RGBColor.BLUE.getCssName(), $("#div1", e).css(CSS.COLOR, false));

    // reset
    $("#div1", e).css(CSS.COLOR.with(RGBColor.BLACK));

    $(".clickMe", e).die();

    $("#div1", e).click();
    assertEquals(RGBColor.BLACK.getCssName(), $("#div1", e).css(CSS.COLOR, false));

    $("#div1", e).dblclick();
    assertEquals(RGBColor.BLACK.getCssName(), $("#div1", e).css(CSS.COLOR, false));

  }

  /**
   * TODO: DblClick doesn't work with HtmlUnit, investigate and report.
   */
  @DoNotRunWith({Platform.HtmlUnitBug, Platform.HtmlUnitLayout, Platform.HtmlUnitUnknown})
  public void testEventsDblClick() {
    $(e).html("<p>Content</p>");
    $("p", e).css(CSS.COLOR.with(RGBColor.WHITE));
    $("p", e).dblclick(new Function() {
      public void f(Element elem) {
        $(elem).css(CSS.COLOR.with(RGBColor.YELLOW));
      }
    });
    $("p", e).dblclick();
    assertEquals("yellow", $("p", e).css("color", false));
  }

  public void testEventsPlugin() {
    $(e).html("<p>Content</p>");

    // click
    $("p", e).click(new Function() {
      public void f(Element elem) {
        $(elem).css(CSS.COLOR.with(RGBColor.RED));
      }
    }, new Function() {
      public void f(Element elem) {
        $(elem).css(CSS.BACKGROUND_COLOR.with(RGBColor.GREEN));
      }
    });
    $("p", e, Events.Events).trigger(Event.ONCLICK);
    assertEquals("red", $("p", e).css("color", false));
    assertEquals("green", $("p", e).css("background-color", false));

    // unbind
    $("p", e).css(CSS.COLOR.with(RGBColor.WHITE));
    $("p", e).unbind(Event.ONCLICK);
    $("p", e).click();
    assertEquals("white", $("p", e).css("color", false));

    Function f1 = new Function() {
      public void f() {
        $(this).css(CSS.COLOR.with(RGBColor.RED));
      }
    };
    Function f2 = new Function() {
      public void f() {
        $(this).css(CSS.COLOR.with(RGBColor.GREEN));
      }
    };
    $("p", e).click(f1, f2);
    $("p", e).css(CSS.COLOR.with(RGBColor.WHITE));
    $("p", e, Events.Events).trigger(Event.ONCLICK);
    assertEquals("green", $("p", e).css("color", false));
    $("p", e).unbind(Event.ONCLICK, f2);
    $("p", e, Events.Events).trigger(Event.ONCLICK);
    assertEquals("red", $("p", e).css("color", false));

    // toggle
    $("p", e).unbind(Event.ONCLICK);
    $("p", e).toggle(new Function() {
      public void f(Element elem) {
        $(elem).css(CSS.COLOR.with(RGBColor.RED));
      }
    }, new Function() {
      public void f(Element elem) {
        $(elem).css(CSS.COLOR.with(RGBColor.BLUE));
      }
    });
    $("p", e).click();
    assertEquals("red", $("p", e).css("color", false));
    $("p", e).click();
    assertEquals("blue", $("p", e).css("color", false));

    // one
    $("p", e).unbind(Event.ONCLICK);
    $("p", e).one(Event.ONCLICK, null, new Function() {
      public void f(Element elem) {
        $(elem).css(CSS.COLOR.with(RGBColor.RED));
      }
    });
    $("p", e).click();
    assertEquals("red", $("p", e).css("color", false));
    $("p", e).css(CSS.COLOR.with(RGBColor.WHITE));
    $("p", e).click();
    assertEquals("white", $("p", e).css("color", false));

    // hover (mouseenter, mouseleave)
    $("p", e).hover(new Function() {
      public void f(Element elem) {
        $(elem).css(CSS.BACKGROUND_COLOR.with(RGBColor.YELLOW));
      }
    }, new Function() {
      public void f(Element elem) {
        $(elem).css(CSS.BACKGROUND_COLOR.with(RGBColor.WHITE));
      }
    });
    $("p", e).trigger(Event.ONMOUSEOVER);
    assertEquals("yellow", $("p", e).css("background-color", false));
    $("p", e).trigger(Event.ONMOUSEOUT);
    assertEquals("white", $("p", e).css("background-color", false));

    $("p", e).css(CSS.COLOR.with(RGBColor.WHITE));
    $("p", e).hover(null, null);
    $("p", e).trigger(Event.ONMOUSEOVER);
    assertEquals("white", $("p", e).css("background-color", false));

    // key events
    $(e).html("<input type='text'/>");
    Function keyEventAction = new Function() {
      public boolean f(Event evnt) {
        GQuery gq = $(evnt);
        int c = evnt.getCharCode() > 0 ? evnt.getCharCode() : evnt.getKeyCode();
        gq.val(gq.val() + Character.toString((char) c));
        return false;
      }
    };
    $("input", e).keypress(keyEventAction);
    $("input", e).keydown(keyEventAction);
    $("input", e).keyup(keyEventAction);
    $("input", e).focus();
    $("input", e).keydown('A');
    $("input", e).keypress('B');
    $("input", e).keyup('C');
    $("input", e).keydown('a');
    $("input", e).keypress('b');
    $("input", e).keyup('c');
    assertEquals("ABCabc", $("input", e).val());
  }

  /**
   * TODO: focus/blur doesn't work with HtmlUnit, investigate and report.
   */
  @DoNotRunWith({Platform.HtmlUnitLayout})
  public void testFocusBlur() {
    $(e).html("<p>Content</p>");

    // focus
    // FIXME: Html 2.1.0 failing but FF do not
    $("p", e).focus(new Function() {
      public void f(Element elem) {
        $(elem).css("border", "1px dotted black");
      }
    });
    $("p", e).focus();
    assertEquals("black", $("p", e).css("border-top-color", false));
    assertEquals("dotted", $("p", e).css("border-top-style", false));
    assertEquals("1px", $("p", e).css("border-top-width", false));

    // blur
    $("p", e).blur(new Function() {
      public void f(Element elem) {
        $(elem).css("border", "");
      }
    });
    $("p", e).blur();
    assertEquals("", $("p", e).css("border", false));
  }

  public void testLazyMethods() {
    $(e).css(CSS.COLOR.with(RGBColor.WHITE));
    assertEquals("white", $(e).css("color", false));

    $(e).one(Event.ONCLICK, null, lazy().css(CSS.COLOR.with(RGBColor.RED)).done());
    $(e).click();
    assertEquals("red", $(e).css("color", false));

    $(e).click(lazy().css(CSS.COLOR.with(RGBColor.BLACK)).done());
    $(e).click();
    assertEquals("black", $(e).css("color", false));
  }

  public void testLive() {
    $(e).html(
        "<div id='div1' class='clickMe'><div id='div2'>Content 1<span id='span1'> blop</span></div></div>");
    $(".clickMe", e).live("click", new Function() {
      public void f() {
        $(this).css("color", "red");
      }
    });

    $(e).append(
        "<div id='div3' class='clickMe'>Content 2 <div id='div4'><span id='span2'>blop</span></div></div>");

    $(".clickMe", e).click();
    assertEquals("red", $("#div1", e).css("color", false));
    assertEquals("red", $("#div3", e).css("color", false));

    // reset
    $("*", e).css(CSS.COLOR.with(RGBColor.BLACK));
    assertEquals("black", $("div", e).css("color", false));
    assertEquals("black", $("span", e).css("color", false));

    $("#span1", e).click();
    assertEquals("red", $("#div1", e).css("color", false));
    assertEquals("black", $("#div3", e).css("color", false));

    // reset
    $("*", e).css(CSS.COLOR.with(RGBColor.BLACK));

    $("#span2", e).click();
    assertEquals("black", $("#div1", e).css("color", false));
    assertEquals("red", $("#div3", e).css("color", false));

    // reset
    $("*", e).removeClass("clickMe").css(CSS.COLOR.with(RGBColor.BLACK));

    $("#div2, #div4", e).addClass("clickMe");

    $("#span1", e).click();
    assertEquals("black", $("#div1", e).css("color", false));
    assertEquals("red", $("#div2", e).css("color", false));
    assertEquals("black", $("#div3", e).css("color", false));
    assertEquals("black", $("#div4", e).css("color", false));

    // reset
    $("*", e).css(CSS.COLOR.with(RGBColor.BLACK));

    $("#span2", e).click();
    assertEquals("black", $("#div1", e).css("color", false));
    assertEquals("black", $("#div2", e).css("color", false));
    assertEquals("black", $("#div3", e).css("color", false));
    assertEquals("red", $("#div4", e).css("color", false));
  }

  public void testLive2() {

    $(e).html("<div id='div1'><div id='div2'>Content 1<span id='span1'> blop</span></div></div>");

    $(".clickable", e).live("click", new Function() {
      public void f(Element e) {
        $(e).css(CSS.COLOR.with(RGBColor.RED));
      }
    });

    $(".clickable2", e).live("click", new Function() {
      public void f(Element e) {
        $(e).css(CSS.COLOR.with(RGBColor.BLUE));
      }
    });

    $(".hover", e).live("mouseover", new Function() {
      public void f(Element e) {
        $(e).css(CSS.BACKGROUND_COLOR.with(RGBColor.YELLOW));
      }
    });

    $("#div1", e).addClass("clickable");
    $("#div2", e).addClass("clickable2", "hover");

    $("#span1", e).click();

    assertEquals("red", $("#div1", e).css(CSS.COLOR, false));
    assertEquals("blue", $("#div2", e).css(CSS.COLOR, false));
    // ensure taht handler related to mouseover event was not called
    assertNotSame("yellow", $("#div2", e).css(CSS.BACKGROUND_COLOR, false));

  }

  public void testLiveWithEventBit() {
    $(e).html("<div id='div1'><div id='div2'>Content 1<span id='span1'> blop</span></div></div>");

    $(".clickable", e).live(Event.ONCLICK, new Function() {
      public void f(Element e) {
        $(e).css(CSS.COLOR.with(RGBColor.RED));
      }
    });

    $("#div1", e).addClass("clickable");
    $("#span1", e).click();

    assertEquals("red", $("#div1", e).css(CSS.COLOR, false));

  }

  public void testLiveWithSpecial() {
    $(e).html("<div id='div1'><div id='div2'>Content 1<span id='span1'> blop</span></div></div>");

    $(".clickable", e).live("mouseenter", new Function() {
      public void f(Element e) {
        $(e).css(CSS.COLOR.with(RGBColor.RED));
      }
    });

    $("#div1", e).addClass("clickable");
    $("#span1", e).mouseenter();
    assertEquals("red", $("#div1", e).css(CSS.COLOR, false));

    $(".clickable", e).die("mouseenter");
    $("*", e).css(CSS.COLOR.with(RGBColor.BLACK));

    $("#span1", e).mouseenter();
    assertEquals("black", $("#div1", e).css(CSS.COLOR, false));
  }

  public void testOnOffWithSpecial() {
    $(e).html("<div id='div1'><div id='div2'>Content 1<span id='span1'> blop</span></div></div>");

    $(e).on("mouseenter", ".clickable", new Function() {
      public void f(Element e) {
        $(e).css(CSS.COLOR.with(RGBColor.RED));
      }
    });

    $("#div1", e).addClass("clickable");
    $("#span1", e).mouseenter();
    assertEquals("red", $("#div1", e).css(CSS.COLOR, false));

    $(e).off("mouseenter", ".clickable");
    $("*", e).css(CSS.COLOR.with(RGBColor.BLACK));

    $("#span1", e).mouseenter();
    assertEquals("black", $("#div1", e).css(CSS.COLOR, false));
  }

  public void testLiveWithMultipleEvent() {

    $(e).html("<div id='div1'><div id='div2'>Content 1<span id='span1'> blop</span></div></div>");

    $(".myClass", e).live("click mouseover", new Function() {
      public void f(Element e) {
        $(e).css(CSS.COLOR.with(RGBColor.RED));
      }
    });

    $("#div1", e).addClass("myClass");

    $("#div1", e).click();

    assertEquals("red", $("#div1", e).css(CSS.COLOR, false));

    $("#div1", e).css(CSS.COLOR.with(RGBColor.BLACK));

    $("#div1", e).trigger(Event.ONMOUSEOVER);
    assertEquals("red", $("#div1", e).css(CSS.COLOR, false));

    $(".myClass2", e).live(Event.ONCLICK | Event.ONMOUSEDOWN, new Function() {
      public void f(Element e) {
        $(e).css(CSS.COLOR.with(RGBColor.YELLOW));
      }
    });

    $("#div2", e).addClass("myClass2");

    $("#div2", e).click();

    assertEquals("yellow", $("#div2", e).css(CSS.COLOR, false));

    $("#div2", e).css(CSS.COLOR.with(RGBColor.BLACK));

    $("#div2", e).trigger(Event.ONMOUSEDOWN);
    assertEquals("yellow", $("#div2", e).css(CSS.COLOR, false));

  }

  public void testLiveWithMultipleFunction() {
    $(e).html("<div id='div1'><div id='div2'>Content 1<span id='span1'> blop</span></div></div>");

    $(".clickable", e).live("click", new Function() {
      public void f(Element e) {
        $(e).css(CSS.COLOR.with(RGBColor.RED));
      }
    }, new Function() {
      public void f(Element e) {
        $(e).css(CSS.BACKGROUND_COLOR.with(RGBColor.YELLOW));
      }
    });

    $("#div1", e).addClass("clickable");
    $("#span1", e).click();

    assertEquals("red", $("#div1", e).css(CSS.COLOR, false));
    assertEquals("yellow", $("#div1", e).css(CSS.BACKGROUND_COLOR, false));
  }

  public void testLiveWithNameSpace() {
    String content = "<input type='text' id='test'></div>";
    $(e).html(content);

    $(".live", e)
        .live(
            "keydown.keyevents keypress.keyevents keyup.keyevents "
                + "mousedown.mouseevents mouseup.mouseevents mousemove.mouseevents mouseover.mouseevents "
                + "mouseout.mouseevents", new Function() {
          @Override
          public void f() {
            $("#test", e).val("event fired");
          }
        });

    $("#test", e).addClass("live");


    int allEventbits[] =
        new int[]{
            ONKEYDOWN, ONKEYPRESS, ONKEYUP, ONMOUSEDOWN, ONMOUSEUP, ONMOUSEMOVE,
            ONMOUSEOVER, ONMOUSEOUT};

    for (int eventbits : allEventbits) {
      $("#test", e).trigger(eventbits, 'c');

      assertEquals("event fired", $("#test", e).val());
      $("#test", e).val("");


    }

    // test die without namespace
    $(".live", e).die("keydown");

    for (int eventbits : allEventbits) {
      $("#test", e).trigger(eventbits, 'c');

      if (eventbits != ONKEYDOWN) {
        assertEquals("event fired", $("#test", e).val());
        $("#test", e).val("");
      } else {
        assertEquals("", $("#test", e).val());
      }

    }

    // test die event name + namespace
    $(".live", e).die("keypress.keyevents keyup.keyevents");

    for (int eventbits : allEventbits) {
      $("#test", e).trigger(eventbits, 'c');
      if ((eventbits & MOUSEEVENTS) == eventbits) {
        assertEquals("event fired", $("#test", e).val());
        $("#test", e).val("");
      } else {
        assertEquals("", $("#test", e).val());
      }
    }

    // test die only on namespace
    $(".live", e).die(".mouseevents");

    for (int eventbits : allEventbits) {
      $("#test", e).trigger(eventbits, 'c');

      assertEquals("", $("#test", e).val());

    }

  }

  public void testMouseenterEvent() {
    String content = "<div id='test'>blop</div>";
    $(e).html(content);

    $("#test", e).mouseenter(new Function() {
      @Override
      public void f(Element e) {
        e.setInnerText("test succeed");
      }
    }).mouseover(new Function() {
      @Override
      public void f() {
        fail("mouseover handler should not be fired");
      }
    });

    $("#test", e).mouseenter();

    assertEquals("test succeed", $("#test", e).text());

    // unbind the mouseover event should not remove the mouseenter
    $("#test", e).text("blop").unbind("mouseover");

    $("#test", e).mouseenter();

    assertEquals("test succeed", $("#test", e).text());

    $("#test", e).text("blop").unbind("mouseenter");

    $("#test", e).mouseenter();

    assertEquals("blop", $("#test", e).text());

    // try the bind function
    $("#test", e).bind("mouseenter", new Function() {
      @Override
      public void f(Element e) {
        e.setInnerText("test succeed");
      }
    }).mouseover(new Function() {
      @Override
      public void f() {
        fail("mouseover handler should not be fired");
      }
    });

    $("#test", e).mouseenter();

    assertEquals("test succeed", $("#test", e).text());

    $("#test", e).text("blop").unbind("mouseenter");

    $("#test", e).mouseenter();

    assertEquals("blop", $("#test", e).text());

  }

  public void testMouseleaveEvent() {
    String content = "<div id='test'>blop</div>";
    $(e).html(content);

    $("#test", e).mouseleave(new Function() {
      @Override
      public void f(Element e) {
        e.setInnerText("test succeed");
      }
    }).mouseout(new Function() {
      @Override
      public void f() {
        fail("mouseout handler should not be fired");
      }
    });

    $("#test", e).mouseleave();

    assertEquals("test succeed", $("#test", e).text());

    // unbind the mouseout event should not remove the mouseleave
    $("#test", e).text("blop").unbind("mouseout");

    $("#test", e).mouseleave();

    assertEquals("test succeed", $("#test", e).text());

    $("#test", e).text("blop").unbind("mouseleave");

    $("#test", e).mouseleave();

    assertEquals("blop", $("#test", e).text());

    // try the bind method directly
    $("#test", e).bind("mouseleave", new Function() {
      @Override
      public void f(Element e) {
        e.setInnerText("test succeed");
      }
    }).mouseout(new Function() {
      @Override
      public void f() {
        fail("mouseout handler should not be fired");
      }
    });

    $("#test", e).mouseleave();

    assertEquals("test succeed", $("#test", e).text());

    $("#test", e).text("blop").unbind("mouseleave");

    $("#test", e).mouseleave();

    assertEquals("blop", $("#test", e).text());

  }

  public void testMultipleEvents() {
    String content = "<input type='text' id='test'></div>";
    $(e).html(content);

    $("#test", e).bind(FOCUSEVENTS | KEYEVENTS | MOUSEEVENTS, null, new Function() {
      @Override
      public void f() {
        $("#test", e).val("event fired");
      }
    });

    int allEventbits[] =
        new int[] {
            ONFOCUS, ONBLUR, ONKEYDOWN, ONKEYPRESS, ONKEYUP, ONMOUSEDOWN, ONMOUSEUP, ONMOUSEMOVE,
            ONMOUSEOVER, ONMOUSEOUT};

    for (int eventbits : allEventbits) {
      $("#test", e).trigger(eventbits, 'c');

      assertEquals("event fired", $("#test", e).val());
      $("#test", e).val("");

    }

    // unbind focus event
    $("#test", e).unbind(FOCUSEVENTS);

    for (int eventbits : allEventbits) {
      $("#test", e).trigger(eventbits, 'c');

      if (eventbits == ONBLUR || eventbits == ONFOCUS) {
        assertEquals("", $("#test", e).val());
      } else {
        assertEquals("event fired", $("#test", e).val());
        $("#test", e).val("");
      }

    }

    // unbind focus event
    $("#test", e).unbind(KEYEVENTS);

    for (int eventbits : allEventbits) {
      $("#test", e).trigger(eventbits, 'c');

      if ((eventbits & MOUSEEVENTS) == eventbits) {
        assertEquals("event fired", $("#test", e).val());
        $("#test", e).val("");
      } else {
        assertEquals("", $("#test", e).val());
      }

    }

    // unbind some mouse events
    $("#test", e).unbind(ONMOUSEDOWN | ONMOUSEUP | ONMOUSEMOVE | ONMOUSEOVER);

    for (int eventbits : allEventbits) {
      $("#test", e).trigger(eventbits, 'c');

      if (eventbits == ONMOUSEOUT) {
        assertEquals("event fired", $("#test", e).val());
        $("#test", e).val("");
      } else {
        assertEquals("", $("#test", e).val());
      }

    }

    // unbind one event
    $("#test", e).unbind(ONMOUSEOUT);

    for (int eventbits : allEventbits) {
      $("#test", e).trigger(eventbits, 'c');
      assertEquals("", $("#test", e).val());
    }

  }

  public void testMultipleEventsString() {
    String content = "<input type='text' id='test'></div>";
    $(e).html(content);

    int allEventbits[] =
        new int[] {
            ONFOCUS, ONBLUR, ONKEYDOWN, ONKEYPRESS, ONKEYUP, ONMOUSEDOWN, ONMOUSEUP, ONMOUSEMOVE,
            ONMOUSEOVER, ONMOUSEOUT};

    $("#test", e).bind(
        "focus blur keydown keypress keyup mousedown mouseup mousemove mouseover mouseout", null,
        new Function() {
          @Override
          public void f() {
            $("#test", e).val("event fired");
          }
        });

    for (int eventbits : allEventbits) {
      $("#test", e).trigger(eventbits, 'c');

      assertEquals("event fired", $("#test", e).val());
      $("#test", e).val("");

    }

    $("#test", e).unbind("focus blur keydown keypress keyup");

    for (int eventbits : allEventbits) {
      $("#test", e).trigger(eventbits, 'c');

      if ((eventbits & MOUSEEVENTS) == eventbits) {
        assertEquals("event fired", $("#test", e).val());
        $("#test", e).val("");
      } else {
        assertEquals("", $("#test", e).val());
      }
    }

    $("#test", e).unbind("mousedown mouseup mousemove mouseover").unbind("mouseout");

    for (int eventbits : allEventbits) {
      $("#test", e).trigger(eventbits, 'c');

      assertEquals("", $("#test", e).val());

    }

  }

  public void testNamedBinding() {
    $(e).html("<p>Content</p>");

    $("p", e, Events.Events).bind("click.first.namespace", new Function() {
      public void f(Element elem) {
        $(elem).css(CSS.COLOR.with(RGBColor.RED));
      }
    });
    $("p", e, Events.Events).bind("click.second.namespace", new Function() {
      public void f(Element elem) {
        $(elem).css(CSS.BACKGROUND_COLOR.with(RGBColor.GREEN));
      }
    });
    $("p", e, Events.Events).bind("click", "red",new Function() {
      public void f(Element elem) {
        $(elem).css(CSS.FONT_SIZE.with(Length.px(24)));

        assertEquals("red", getArgument(0));
      }
    });
    $("p", e, Events.Events).trigger(Event.ONCLICK);
    assertEquals("red", $("p", e).css("color", false));
    assertEquals("green", $("p", e).css("background-color", false));
    assertEquals(24.0d, $("p", e).cur("fontSize", true));

    $("p", e).css(CSS.COLOR.with(null)).css(CSS.BACKGROUND_COLOR, "").css(
        CSS.FONT_SIZE.with(Length.px(12)));
    assertFalse("red".equalsIgnoreCase($("p", e).css("color", false)));
    assertFalse("green".equalsIgnoreCase($("p", e).css("background-color", false)));
    assertEquals(12.0d, $("p", e).cur("fontSize", true));

    $("p", e, Events.Events).unbind("click.first.namespace");
    $("p", e, Events.Events).trigger(Event.ONCLICK);
    assertFalse("red".equalsIgnoreCase($("p", e).css("color", false)));
    assertEquals("green", $("p", e).css("background-color", false));
    assertEquals(24.0d, $("p", e).cur("fontSize", true));

    $("p", e).css(CSS.COLOR.with(null)).css(CSS.BACKGROUND_COLOR, "").css(
        CSS.FONT_SIZE.with(Length.px(12)));
    assertFalse("red".equalsIgnoreCase($("p", e).css("color", false)));
    assertFalse("green".equalsIgnoreCase($("p", e).css("background-color", false)));
    assertEquals(12.0d, $("p", e).cur("fontSize", true));

    $("p", e, Events.Events).unbind("click");
    $("p", e, Events.Events).trigger(Event.ONCLICK);
    assertFalse("red".equalsIgnoreCase($("p", e).css("color", false)));
    assertFalse("green".equalsIgnoreCase($("p", e).css("background-color", false)));
    assertEquals(12.0d, $("p", e).cur("fontSize", true));
  }

  public void testRebind() {
    final GQuery b = $("<p>content</p>");
    assertEquals(1, b.size());
    b.click(new Function() {
      public void f(Element e) {
        b.css(CSS.COLOR.with(RGBColor.RED));
      }
    });
    $(e).append(b);
    assertEquals(1, b.size());
    b.click();
    assertEquals("red", b.css("color", false));
  }

  public void testResizeEvent() {
    $(e).html("<div id=ra></div>");
    GQuery g = $("#ra", e);

    delayTestFinish(100);
    g.bind("resize", null, new Function() {
      public void f(Element e) {
        finishTest();
      }
    });

    g.width(400);
    g.resize();
  }

  @DoNotRunWith({Platform.HtmlUnitLayout, Platform.Prod})
  // FIXME: Window.resizeTo does not resize the window, maybe we need a different approach.
  public void testResizeWindowEvent() {
    GQuery w = $(GQuery.window);


    delayTestFinish(100);
    w.bind("resize", null, new Function() {
      public void f(Element e) {
        finishTest();
      }
    });

    w.trigger("resize");

  }

  /**
   * TODO: submit doesn't work with HtmlUnit, investigate and report. The problem is that
   * preventDefault does not set the flag e.defaultPrevented || e.returnValue in HtmlUnit native
   * event.
   */
  @DoNotRunWith({Platform.HtmlUnitLayout})
  public void testSubmitEvent() {
    // Add a form and an iframe to the dom. The form target is the iframe
    $(e).html(
        "<form action='whatever' target='miframe'><input type='text' value='Hello'><input type='submit' value='Go'></form><iframe name='miframe' id='miframe' src=\"javascript:''\">");
    testSubmitEventCont = 0;

    // Add an onsubmit function to the form returning false to cancel the action
    $("form").bind("submit", null, new Function() {
      public boolean f(Event e) {
        testSubmitEventCont++;
        return false;
      }
    });

    // Check that the onsubmit function is called and the iframe has not changed
    $("form").submit();
    assertEquals(1, testSubmitEventCont);
    assertFalse($("#miframe").contents().find("body").text().contains("ERROR"));

    // Remove the binding
    $("form").unbind("submit");

    // Check that on submit function is not called and the form has been
    // submitted
    $("form").submit();
    assertEquals(1, testSubmitEventCont);

    delayTestFinish(1000);
    new Timer() {
      public void run() {
        // Check that the server returns an error since the action does not
        // exist
        assertTrue($("#miframe").contents().find("body").text().contains("ERROR"));
        finishTest();
      }
    }.schedule(500);
  }

  /**
   * Test for issue 62 http://code.google.com/p/gwtquery/issues/detail?id=62
   */
  public void testTabInbexInFocusEventBinding() {
    String content = "<div id='mtest'>test content</div>";
    $(e).html(content);
    $("#mtest").focus(new Function() {
    });

    assertEquals($("#mtest").attr("tabIndex"), "0");

    content = "<div id='mtest' tabIndex='2'>test content</div>";
    $(e).html(content);
    $("#mtest").focus(new Function() {
    });

    assertEquals($("#mtest").attr("tabIndex"), "2");
  }

  public void testUnbindMultipleEvents() {
    String content = "<p>content</p>";
    $(e).html(content);
    $(document).bind(Event.ONMOUSEMOVE, null, new Function() {
      public void f(Element e) {
        $("p").css(CSS.COLOR.with(RGBColor.RED));
      }
    });
    $(document).bind(Event.ONMOUSEUP, null, new Function() {
      public void f(Element e) {
        $("p").css(CSS.COLOR.with(RGBColor.YELLOW));
      }
    });
    $(document).trigger(Event.ONMOUSEMOVE);
    assertEquals("red", $("p").css("color", false));
    $(document).trigger(Event.ONMOUSEUP);
    assertEquals("yellow", $("p").css("color", false));
    $("p").css(CSS.COLOR.with(RGBColor.BLACK));
    $(document).unbind(Event.ONMOUSEUP | Event.ONMOUSEMOVE);
    $(document).trigger(Event.ONMOUSEMOVE);
    assertEquals("black", $("p").css("color", false));
    $(document).trigger(Event.ONMOUSEUP);
    assertEquals("black", $("p").css("color", false));
  }

  public void testUnDelegate() {

    $(e).html(
        "<div class='mainDiv'><div class='subDiv'>Content 0<span>blop</span></div></div><div class='mainDiv'><div class='subDiv'>Content 0<span>blop</span></div></div>");

    $(".mainDiv", e).delegate(".subDiv", "click", new Function() {
      @Override
      public void f(Element e) {
        $(e).css(CSS.COLOR.with(RGBColor.RED));
      }
    });

    $(".mainDiv", e).delegate(".subDiv", Event.ONMOUSEOVER, new Function() {
      @Override
      public void f(Element e) {
        $(e).css(CSS.BACKGROUND_COLOR.with(RGBColor.YELLOW));
      }
    });

    for (Element mainDiv : $(".mainDiv", e).elements()) {
      for (int i = 0; i < 3; i++) {
        String html = "<div class='subDiv'>Content " + i + "<span>blop</span></div>";
        $(mainDiv).append(html);
      }
    }

    assertEquals(8, $(".subDiv", e).length());

    $("span", e).click().trigger(Event.ONMOUSEOVER);

    for (Element el : $(".subDiv", e).elements()) {
      assertEquals("red", $(el).css(CSS.COLOR, false));
      assertEquals("yellow", $(el).css(CSS.BACKGROUND_COLOR, false));
      // reset
      $(el).css(CSS.COLOR.with(RGBColor.BLACK), CSS.BACKGROUND_COLOR.with(RGBColor.WHITE));
    }

    $(".mainDiv", e).undelegate(".subDiv", Event.ONCLICK);

    $("span", e).click().trigger(Event.ONMOUSEOVER);

    for (Element el : $(".subDiv", e).elements()) {
      assertEquals("black", $(el).css(CSS.COLOR, false));
      assertEquals("yellow", $(el).css(CSS.BACKGROUND_COLOR, false));
      // reset
      $(el).css(CSS.COLOR.with(RGBColor.BLACK), CSS.BACKGROUND_COLOR.with(RGBColor.WHITE));
    }

    $(".mainDiv", e).undelegate(".subDiv", "mouseover");

    $("span", e).click().trigger(Event.ONMOUSEOVER);

    for (Element el : $(".subDiv", e).elements()) {
      assertEquals("black", $(el).css(CSS.COLOR, false));
      assertEquals("white", $(el).css(CSS.BACKGROUND_COLOR, false));
    }
  }

  public void testOnOff() {
    $(e).html("<div class='mainDiv'><div class='subDiv'>Content</div></div>");

    $(".mainDiv", e).on("click", new Function() {
      @Override
      public void f(Element e) {
        $(e).css("color", "red");
      }
    });

    $(".mainDiv", e).click();

    assertEquals("red", $(".mainDiv", e).css("color", false));

    // reset
    $(".mainDiv", e).css("color", "black");

    $(".mainDiv", e).off("click");

    $(".mainDiv", e).click();

    assertEquals("black", $(".mainDiv", e).css("color", false));

    // try with other signatures by passing null to extra parameters
    $(".mainDiv", e).on("click", (String) null, new Function() {
      @Override
      public void f(Element e) {
        $(e).css("color", "red");
      }
    });

    $(".mainDiv", e).click();

    assertEquals("red", $(".mainDiv", e).css("color", false));

    // reset
    $(".mainDiv", e).css("color", "black");

    $(".mainDiv", e).off("click");

    // try with other signatures by passing null to extra parameters
    $(".mainDiv", e).on("click", null,  new Object(), new Function() {
      @Override
      public void f(Element e) {
        $(e).css("color", "red");
      }
    });

    $(".mainDiv", e).click();

    assertEquals("red", $(".mainDiv", e).css("color", false));
  }


  public void testOff() {
    $(e).html("<div class='mainDiv'><div class='subDiv'>Content</div></div>");

    $(".mainDiv", e).on("click", new Function() {
      @Override
      public void f(Element e) {
        $(e).css("color", "red");
      }
    });

    $(".mainDiv", e).on("mouseover", new Function() {
      @Override
      public void f(Element e) {
        $(e).css("background-color", "yellow");
      }
    });

    $(".mainDiv", e).click().trigger(Event.ONMOUSEOVER);

    assertEquals("red", $(".mainDiv", e).css("color", false));
    assertEquals("yellow", $(".mainDiv", e).css("background-color", false));

    // reset
    $(".mainDiv", e).css("color", "black");
    $(".mainDiv", e).css("background-color", "white");

    $(".mainDiv", e).off();

    $(".mainDiv", e).click().trigger(Event.ONMOUSEOVER);

    assertEquals("black", $(".mainDiv", e).css("color", false));
    assertEquals("white", $(".mainDiv", e).css("background-color", false));
  }


  public void testOnOffWithSelector() {
    $(e).html("<div class='mainDiv'><div class='subDiv'>Content " +
        "0<span>blop</span></div></div><div class='mainDiv'><div class='subDiv'>" +
        "Content 0<span>blop</span></div></div>");

    $(".mainDiv", e).on("click", ".subDiv", new Function() {
      @Override
      public void f(Element e) {
        $(e).css("color", "red");
      }
    });


    for (Element mainDiv : $(".mainDiv", e).elements()) {
      for (int i = 0; i < 3; i++) {
        String html = "<div class='subDiv'>Content " + i + "<span>blop</span></div>";
        $(mainDiv).append(html);
      }
    }

    assertEquals(8, $(".subDiv", e).length());

    $("span", e).click();

    for (Element el : $(".subDiv", e).elements()) {
      assertEquals("red", $(el).css("color", false));
      // reset
      $(el).css("color", "black");
    }

    $(".mainDiv", e).off("click", ".subDiv");

    $("span", e).click();

    for (Element el : $(".subDiv", e).elements()) {
      assertEquals("black", $(el).css(CSS.COLOR, false));
    }
  }

  public void testUnDelegateAll() {

    $(e).html(
        "<div class='mainDiv'><div class='subDiv'>Content 0<span>blop</span></div></div><div class='mainDiv'><div class='subDiv'>Content 0<span>blop</span></div></div>");

    $(".mainDiv", e).delegate(".subDiv", "click", new Function() {
      @Override
      public void f(Element e) {
        $(e).css(CSS.COLOR.with(RGBColor.RED));
      }
    });

    $(".mainDiv", e).delegate(".subDiv", Event.ONMOUSEOVER, new Function() {
      @Override
      public void f(Element e) {
        $(e).css(CSS.BACKGROUND_COLOR.with(RGBColor.YELLOW));
      }
    });

    for (Element mainDiv : $(".mainDiv", e).elements()) {
      for (int i = 0; i < 3; i++) {
        String html = "<div class='subDiv'>Content " + i + "<span>blop</span></div>";
        $(mainDiv).append(html);
      }
    }

    assertEquals(8, $(".subDiv", e).length());

    $("span", e).click().trigger(Event.ONMOUSEOVER);

    for (Element el : $(".subDiv", e).elements()) {
      assertEquals("red", $(el).css(CSS.COLOR, false));
      assertEquals("yellow", $(el).css(CSS.BACKGROUND_COLOR, false));
      // reset
      $(el).css(CSS.COLOR.with(RGBColor.BLACK), CSS.BACKGROUND_COLOR.with(RGBColor.WHITE));
    }

    $(".mainDiv", e).undelegate(".subDiv");

    $("span", e).click().trigger(Event.ONMOUSEOVER);

    for (Element el : $(".subDiv", e).elements()) {
      assertEquals("black", $(el).css(CSS.COLOR, false));
      assertEquals("white", $(el).css(CSS.BACKGROUND_COLOR, false));
    }
  }

  public void testUnDelegateAll2() {

    $(e).html(
        "<div class='mainDiv'><div class='subDiv'>Content 0<span>blop</span></div></div><div class='mainDiv'><div class='subDiv'>Content 0<span>blop</span></div></div>");

    $(".mainDiv", e).delegate(".subDiv", "click", new Function() {
      @Override
      public void f(Element e) {
        $(e).css(CSS.COLOR.with(RGBColor.RED));
      }
    });

    $(".mainDiv", e).delegate(".subDiv", Event.ONMOUSEOVER, new Function() {
      @Override
      public void f(Element e) {
        $(e).css(CSS.BACKGROUND_COLOR.with(RGBColor.YELLOW));
      }
    });

    for (Element mainDiv : $(".mainDiv", e).elements()) {
      for (int i = 0; i < 3; i++) {
        String html = "<div class='subDiv'>Content " + i + "<span>blop</span></div>";
        $(mainDiv).append(html);
      }
    }

    assertEquals(8, $(".subDiv", e).length());

    $("span", e).click().trigger(Event.ONMOUSEOVER);

    for (Element el : $(".subDiv", e).elements()) {
      assertEquals("red", $(el).css(CSS.COLOR, false));
      assertEquals("yellow", $(el).css(CSS.BACKGROUND_COLOR, false));
      // reset
      $(el).css(CSS.COLOR.with(RGBColor.BLACK), CSS.BACKGROUND_COLOR.with(RGBColor.WHITE));
    }

    $(".mainDiv", e).undelegate();

    $("span", e).click().trigger(Event.ONMOUSEOVER);

    for (Element el : $(".subDiv", e).elements()) {
      assertEquals("black", $(el).css(CSS.COLOR, false));
      assertEquals("white", $(el).css(CSS.BACKGROUND_COLOR, false));
    }
  }

  public void testWidgetEvents() {
    final Button b = new Button("click-me");
    b.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        b.getElement().getStyle().setBackgroundColor("black");
        b.setWidth("100px");
      }
    });
    RootPanel.get().add(b);
    $(b).click(lazy().css(CSS.COLOR.with(RGBColor.RED)).done());

    $(b).click();
    assertEquals("red", $("button").css("color", false));
    assertEquals("black", $("button").css("background-color", false));
    assertEquals("100px", $("button").css("width", false));

    $(b).bind("custom", lazy().css("width", "200px").done());
    RootPanel.get().remove(b);

    $(e).append($(b));
    $(b).css(CSS.COLOR.with(RGBColor.YELLOW), CSS.BACKGROUND_COLOR.with(RGBColor.BLUE));
    $(b).click();
    assertEquals("red", $("button").css("color", false));
    assertEquals("black", $("button").css("background-color", false));
    assertEquals("100px", $("button").css("width", false));

    $(b).trigger("custom");
    assertEquals("200px", $("button").css("width", false));
  }

  public void testIssue152() {
    $(e).html("<div class='mdiv'>");
    final GQuery div = $(".mdiv", e);
    final int[] count = { 0 };
    div.one(Event.ONCLICK, null, new Function() {
      public void f() {
        count[0]++;
        div.one(Event.ONCLICK, null, new Function() {
          public void f() {
            fail();
          }
        });
      };
    });

    div.click();
    assertEquals(1, count[0]);
  }

  public void testCustomEvent() {
    $(e).html("<div id='target'>");
    GQuery target = $("#target", e);

    CounterFunction handler = new CounterFunction();

    // test  custom event binding
    target.bind("mycustomevent", handler);

    target.trigger("mycustomevent");

    assertEquals(1, handler.invokationCounter);

    //  test  custom event unbinding
    target.unbind("mycustomevent");

    handler.invokationCounter = 0;

    target.trigger("mycustomevent");

    assertEquals(0, handler.invokationCounter);
  }

  public void testCustomEventWithEventData() {
    $(e).html("<div id='target'>");
    GQuery target = $("#target", e);

    CounterFunction handler = new CounterFunction();

    // test custom event binding with event data
    target.bind("mycustomevent", "handlerdata0", handler);

    target.trigger("mycustomevent");

    assertEquals(1, handler.invokationCounter);
    assertEquals("handlerdata0", handler.getArgument(0));

    // unbind
    target.unbind("mycustomevent");
    handler.invokationCounter = 0;
    target.trigger("mycustomevent");
    assertEquals(0, handler.invokationCounter);

    // test custom event binding with event data as array
    target.bind("mycustomevent", new String[]{"handlerdata0", "handlerdata1"}, handler);

    target.trigger("mycustomevent");

    assertEquals(1, handler.invokationCounter);
    assertEquals("handlerdata0", handler.getArgument(0));
    assertEquals("handlerdata1", handler.getArgument(1));
  }

  public void testCustomEventWithHandlerData() {
    $(e).html("<div id='target'>");
    GQuery target = $("#target", e);

    CounterFunction handler = new CounterFunction();

    // test custom event binding
    target.bind("mycustomevent", handler);

    target.trigger("mycustomevent", "eventdata0", "eventdata1");

    assertEquals(1, handler.invokationCounter);
    assertEquals("eventdata0", handler.getArgument(0));
    assertEquals("eventdata1", handler.getArgument(1));

    // unbind
    target.unbind("mycustomevent");
    handler.invokationCounter = 0;
    target.trigger("mycustomevent");
    assertEquals(0, handler.invokationCounter);
  }


  public void testCustomEventWithEventDataAndHandlerData() {
    $(e).html("<div id='target'>");
    GQuery target = $("#target", e);

    CounterFunction handler = new CounterFunction();

    // test custom event binding with event data
    target.bind("mycustomevent", "handlerdata0", handler);

    target.trigger("mycustomevent", "eventdata0", "eventdata1");

    assertEquals(1, handler.invokationCounter);
    assertEquals("handlerdata0", handler.getArgument(0));
    assertEquals("eventdata0", handler.getArgument(1));
    assertEquals("eventdata1", handler.getArgument(2));

    // unbind
    target.unbind("mycustomevent");
    handler.invokationCounter = 0;
    target.trigger("mycustomevent");
    assertEquals(0, handler.invokationCounter);

    // test custom event binding with event data as array
    target.bind("mycustomevent", new String[]{"handlerdata0", "handlerdata1"}, handler);

    target.trigger("mycustomevent", "eventdata0", "eventdata1");

    assertEquals(1, handler.invokationCounter);
    assertEquals("handlerdata0", handler.getArgument(0));
    assertEquals("handlerdata1", handler.getArgument(1));
    assertEquals("eventdata0", handler.getArgument(2));
    assertEquals("eventdata1", handler.getArgument(3));
  }

  public void testBitlessEventTriggersWithEventName() {
    $(e).html("<div id='target'>");
    GQuery target = $("#target", e);

    CounterFunction handler = new CounterFunction();

    target.bind(Event.ONCLICK, handler);

    target.trigger("click");

    assertEquals(1, handler.invokationCounter);
  }

  public void testDelegationEventWithCustomEvent() {
    $(e).html("<div id='container'></div>");
    GQuery container = $("#container", e);

    CounterFunction handler = new CounterFunction();

    // test  custom event binding
    $(".custom", e).live("mycustomevent", handler);

    container.html("<div class='custom'></div><div class='other'></div><div" +
        " class='custom'>custom2</div>");

    $(".custom").trigger("mycustomevent");

    assertEquals(2, handler.invokationCounter);
    handler.invokationCounter = 0;

    $(".other").trigger("mycustomevent");
    assertEquals(0, handler.invokationCounter);

    //  test  custom event unbinding
    $(".custom", e).die("mycustomevent");

    handler.invokationCounter = 0;

    $(".custom").trigger("mycustomevent");

    assertEquals(0, handler.invokationCounter);
  }


  public void testDelegationEventWithCustomEventAndData() {
    $(e).html("<div id='container'></div>");
    GQuery container = $("#container", e);

    CounterFunction handler = new CounterFunction();

    // test  custom event binding
    $(".custom", e).live("mycustomevent", new String[]{"handlerdata0", "handlerdata1"}, handler);

    container.html("<div class='custom'></div><div class='other'></div>");

    $(".custom").trigger("mycustomevent", "eventdata0", "eventdata1");

    assertEquals(1, handler.invokationCounter);
    handler.invokationCounter = 0;
    assertEquals("handlerdata0", handler.getArgument(0));
    assertEquals("handlerdata1", handler.getArgument(1));
    assertEquals("eventdata0", handler.getArgument(2));
    assertEquals("eventdata1", handler.getArgument(3));

    $(".other").trigger("mycustomevent");
    assertEquals(0, handler.invokationCounter);

    //  test  custom event unbinding
    $(".custom", e).die("mycustomevent");

    handler.invokationCounter = 0;

    $(".custom").trigger("mycustomevent");

    assertEquals(0, handler.invokationCounter);
  }

  public void testDelegationEventWithDelegateAndWithCustomEvent() {
    $(e).html("<div id='container'></div>");
    GQuery container = $("#container", e);

    CounterFunction handler = new CounterFunction();

    // test  custom event binding
    $(e).delegate(".custom", "mycustomevent", handler);

    container.html("<div class='custom'></div><div class='other'></div>");

    $(".custom").trigger("mycustomevent");

    assertEquals(1, handler.invokationCounter);
    handler.invokationCounter = 0;

    $(".other").trigger("mycustomevent");
    assertEquals(0, handler.invokationCounter);

    //  test  custom event unbinding
    $(e).undelegate(".custom", "mycustomevent");

    handler.invokationCounter = 0;

    $(".custom").trigger("mycustomevent");

    assertEquals(0, handler.invokationCounter);
  }


  public void testDelegationEventWithDelegateAndWithCustomEventAndData() {
    $(e).html("<div id='container'></div>");
    GQuery container = $("#container", e);

    CounterFunction handler = new CounterFunction();

    // test  custom event binding
    $(e).delegate(".custom", "mycustomevent", new String[]{"handlerdata0", "handlerdata1"}, handler);

    container.html("<div class='custom'></div><div class='other'></div><div class='custom'>custom2</div>");

    $(".custom").trigger("mycustomevent", "eventdata0", "eventdata1");

    assertEquals(2, handler.invokationCounter);
    handler.invokationCounter = 0;
    assertEquals("handlerdata0", handler.getArgument(0));
    assertEquals("handlerdata1", handler.getArgument(1));
    assertEquals("eventdata0", handler.getArgument(2));
    assertEquals("eventdata1", handler.getArgument(3));

    $(".other").trigger("mycustomevent");
    assertEquals(0, handler.invokationCounter);

    //  test  custom event unbinding
    $(e).undelegate(".custom", "mycustomevent");

    handler.invokationCounter = 0;

    $(".custom").trigger("mycustomevent");

    assertEquals(0, handler.invokationCounter);
  }

  public void testBitlessEvent() {
    $(e).html("<div id='target'>");
    GQuery target = $("#target", e);

    CounterFunction handler = new CounterFunction();

    // test  custom event binding
    target.bind("drag", handler);

    target.trigger("drag");

    assertEquals(1, handler.invokationCounter);
  }

  public void testIssue226() {
    $(e).html("<div id='target'>");
    GQuery target = $("#target", e);

    // this should not throw a NPE
    target.undelegate("li", "click");
  }

  // issue 25 : https://github.com/gwtquery/gwtquery/issues/25
  public void testDelegateAfterUndelegateWithoutParameter() {
    $(e).html(
        "<div class='mainDiv'><div class='subDiv'>Content 0<span>blop</span></div></div><div class='mainDiv'><div class='subDiv'>Content 0<span>blop</span></div></div>");

    CounterFunction clickFunction = new CounterFunction();
    CounterFunction mouseOverFunction = new CounterFunction();

    $(".mainDiv", e).delegate(".subDiv", "click", clickFunction);

    $(".mainDiv", e).delegate(".subDiv", Event.ONMOUSEOVER, mouseOverFunction);

    for (Element mainDiv : $(".mainDiv", e).elements()) {
      for (int i = 0; i < 3; i++) {
        String html = "<div class='subDiv'>Content " + i + "<span>blop</span></div>";
        $(mainDiv).append(html);
      }
    }

    assertEquals(8, $(".subDiv", e).length());

    $("span", e).click().trigger(Event.ONMOUSEOVER);

    assertEquals(8, clickFunction.invokationCounter);
    assertEquals(8, mouseOverFunction.invokationCounter);

    clickFunction.invokationCounter = 0;
    mouseOverFunction.invokationCounter = 0;

    $(".mainDiv", e).undelegate();

    $("span", e).click().trigger(Event.ONMOUSEOVER);
    assertEquals(0, clickFunction.invokationCounter);
    assertEquals(0, mouseOverFunction.invokationCounter);

    //reattach the event

    $(".mainDiv", e).delegate(".subDiv", "click", clickFunction);
    $(".mainDiv", e).delegate(".subDiv", Event.ONMOUSEOVER, mouseOverFunction);

    $("span", e).click().trigger(Event.ONMOUSEOVER);

    assertEquals(8, clickFunction.invokationCounter);
    assertEquals(8, mouseOverFunction.invokationCounter);

  }

  public void testDelegateAfterUndelegateWithSelectorWithDifferentEvent() {
    $(e).html(
        "<div class='mainDiv'><div class='subDiv'>Content 0<span>blop</span></div></div><div class='mainDiv'><div class='subDiv'>Content 0<span>blop</span></div></div>");

    CounterFunction clickFunction = new CounterFunction();
    CounterFunction mouseOverFunction = new CounterFunction();
    CounterFunction customEventFunction = new CounterFunction();

    $(".mainDiv", e).delegate(".subDiv", "click", clickFunction)
        .delegate(".subDiv", Event.ONMOUSEOVER, mouseOverFunction)
        .delegate(".subDiv", "custom", customEventFunction);

    for (Element mainDiv : $(".mainDiv", e).elements()) {
      for (int i = 0; i < 3; i++) {
        String html = "<div class='subDiv'>Content " + i + "<span>blop</span></div>";
        $(mainDiv).append(html);
      }
    }

    assertEquals(8, $(".subDiv", e).length());

    $("span", e).click().trigger(Event.ONMOUSEOVER).trigger("custom");

    assertEquals(8, clickFunction.invokationCounter);
    assertEquals(8, mouseOverFunction.invokationCounter);
    assertEquals(8, customEventFunction.invokationCounter);

    $(".mainDiv", e).undelegate(".subDiv");

    clickFunction.invokationCounter = 0;
    mouseOverFunction.invokationCounter = 0;
    customEventFunction.invokationCounter = 0;

    $("span", e).click().trigger(Event.ONMOUSEOVER).trigger("custom");

    assertEquals(0, clickFunction.invokationCounter);
    assertEquals(0, mouseOverFunction.invokationCounter);
    assertEquals(0, customEventFunction.invokationCounter);
  }

  public void testUnbindSpecialEventWithNameSpace() {
    $(e).html("<div id='mainDiv'>blop</div>");

    CounterFunction mouseEnterFunction = new CounterFunction();

    $("#mainDiv", e).on("mouseenter.mynamespace", mouseEnterFunction);
    $("#mainDiv", e).mouseenter();
    assertEquals(1, mouseEnterFunction.invokationCounter);

    $("#mainDiv", e).off(".mynamespace");
    $("#mainDiv", e).mouseenter();
    assertEquals(1, mouseEnterFunction.invokationCounter);

    $("#mainDiv", e).on("mouseenter.mynamespace", mouseEnterFunction);
    $("#mainDiv", e).mouseenter();
    assertEquals(2, mouseEnterFunction.invokationCounter);

    $("#mainDiv", e).off("mouseenter.mynamespace");
    $("#mainDiv", e).mouseenter();
    assertEquals(2, mouseEnterFunction.invokationCounter);
  }

  public void testBindAndTriggerWithNameSpace() {
    $(e).html("<div id='mainDiv'>blop</div>");
    CounterFunction counter = new CounterFunction();
    $("#mainDiv", e).on("click.mynamespace;foo", counter);
    $("#mainDiv").trigger("click");
    assertEquals(1, counter.invokationCounter);
    $("#mainDiv").trigger("click.mynamespace;bar");
    assertEquals(1, counter.invokationCounter);
    $("#mainDiv").trigger("click.mynamespace;foo");
    assertEquals(2, counter.invokationCounter);
  }

}
