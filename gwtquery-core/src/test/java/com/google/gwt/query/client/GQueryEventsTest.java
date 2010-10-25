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
import static com.google.gwt.query.client.GQuery.lazy;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.junit.DoNotRunWith;
import com.google.gwt.junit.Platform;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.query.client.css.CSS;
import com.google.gwt.query.client.plugins.Events;
import com.google.gwt.query.client.plugins.EventsListener;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Test class for testing events plugin.
 */
public class GQueryEventsTest extends GWTTestCase {

  static Element e = null;

  static HTML testPanel = null;

  int testSubmitEventCont = 0;

  public String getModuleName() {
    return "com.google.gwt.query.Query";
  }

  public void gwtSetUp() {
    if (e == null) {
      testPanel = new HTML();
      RootPanel.get().add(testPanel);
      e = testPanel.getElement();
      e.setId("evnt-tst");
    } else {
      e.setInnerHTML("");
    }
  } 
  
  /**
   * TODO: DblClick doesn't work with HtmlUnit, investigate and report.
   */
  @DoNotRunWith(Platform.HtmlUnit)
  public void testEventsDblClick() {
    $(e).html("<p>Content</p>");
    $("p", e).css("color", "white");
    $("p", e).dblclick(new Function() {
      public void f(Element elem) {
        $(elem).css("color", "yellow");
      }
    });
    $("p", e).dblclick();
    assertEquals("yellow", $("p", e).css("color"));
  }

  public void testEventsPlugin() {
    $(e).html("<p>Content</p>");

    // click
    $("p", e).click(new Function() {
      public void f(Element elem) {
        $(elem).css("color", "red");
      }
    }, new Function() {
      public void f(Element elem) {
        $(elem).css("background", "green");
      }
    });
    $("p", e, Events.Events).trigger(Event.ONCLICK);
    assertEquals("red", $("p", e).css("color"));
    assertEquals("green", $("p", e).css("background-color"));

    // unbind
    $("p", e).css("color", "white");
    $("p", e).unbind(Event.ONCLICK);
    $("p", e).click();
    assertEquals("white", $("p", e).css("color"));

    // toggle
    $("p", e).unbind(Event.ONCLICK);
    $("p", e).toggle(new Function() {
      public void f(Element elem) {
        $(elem).css("color", "red");
      }
    }, new Function() {
      public void f(Element elem) {
        $(elem).css("color", "blue");
      }
    });
    $("p", e).click();
    assertEquals("red", $("p", e).css("color"));
    $("p", e).click();
    assertEquals("blue", $("p", e).css("color"));

    // one
    $("p", e).unbind(Event.ONCLICK);
    $("p", e).one(Event.ONCLICK, null, new Function() {
      public void f(Element elem) {
        $(elem).css("color", "red");
      }
    });
    $("p", e).click();
    assertEquals("red", $("p", e).css("color"));
    $("p", e).css("color", "white");
    $("p", e).click();
    assertEquals("white", $("p", e).css("color"));

    // hover (mouseover, mouseout)
    $("p", e).hover(new Function() {
      public void f(Element elem) {
        $(elem).css("background-color", "yellow");
      }
    }, new Function() {
      public void f(Element elem) {
        $(elem).css("background-color", "white");
      }
    });
    $("p", e).trigger(Event.ONMOUSEOVER);
    assertEquals("yellow", $("p", e).css("background-color"));
    $("p", e).trigger(Event.ONMOUSEOUT);
    assertEquals("white", $("p", e).css("background-color"));

    // focus
    $("p", e).focus(new Function() {
      public void f(Element elem) {
        $(elem).css("border", "1px dotted black");
      }
    });
    $("p", e).focus();
    assertEquals("black", $("p", e).css("border-top-color"));
    assertEquals("dotted", $("p", e).css("border-top-style"));
    assertEquals("1px", $("p", e).css("border-top-width"));

    // blur
    $("p", e).blur(new Function() {
      public void f(Element elem) {
        $(elem).css("border", "");
      }
    });
    $("p", e).blur();
    assertEquals("", $("p", e).css("border"));

    // key events
    $(e).html("<input type='text'/>");
    Function keyEventAction = new Function() {
      public boolean f(Event evnt) {
        GQuery gq = $(evnt);
        gq.val(gq.val() + Character.toString((char) evnt.getKeyCode()));
        return false;
      }
    };
    $("input", e).keypress(keyEventAction);
    $("input", e).keydown(keyEventAction);
    $("input", e).keyup(keyEventAction);
    $("input", e).focus();
    $("input", e).keydown('a');
    $("input", e).keypress('b');
    $("input", e).keyup('c');
    assertEquals("abc", $("input", e).val());
  }

  public void testLazyMethods() {
    $(e).css("color", "white");
    assertEquals("white", $(e).css("color"));

    $(e).one(Event.ONCLICK, null, lazy().css("color", "red").done());
    $(e).click();
    assertEquals("red", $(e).css("color"));

    $(e).click(lazy().css(CSS.COLOR, CSS.BLACK).done());
    $(e).click();
    assertEquals("black", $(e).css("color"));
  }

  public void testNamedBinding() {
    $(e).html("<p>Content</p>");

    $("p", e, Events.Events).bind("click.first.namespace", null, new Function() {; 
      public void f(Element elem) {
        $(elem).css("color", "red");
      }
    });
    $("p", e, Events.Events).bind("click.second.namespace", null, new Function() {; 
      public void f(Element elem) {
        $(elem).css("background", "green");
      }
    });
    $("p", e, Events.Events).bind("click", null, new Function() {; 
      public void f(Element elem) {
        $(elem).css("fontSize", "24px");
      }
    });
    $("p", e, Events.Events).trigger(Event.ONCLICK);
    assertEquals("red", $("p", e).css("color"));
    assertEquals("green", $("p", e).css("background-color"));
    assertEquals(24.0d, GQUtils.cur($("p", e).get(0), "fontSize", true));
    
    $("p", e).css("color","").css("background","").css("fontSize", "12px");
    assertFalse("red".equalsIgnoreCase($("p", e).css("color")));
    assertFalse("green".equalsIgnoreCase($("p", e).css("background-color")));
    assertEquals(12.0d, GQUtils.cur($("p", e).get(0), "fontSize", true));

    $("p", e, Events.Events).unbind("click.first.namespace");
    $("p", e, Events.Events).trigger(Event.ONCLICK);
    assertFalse("red".equalsIgnoreCase($("p", e).css("color")));
    assertEquals("green", $("p", e).css("background-color"));
    assertEquals(24.0d, GQUtils.cur($("p", e).get(0), "fontSize", true));
    
    
    $("p", e).css("color","").css("background","").css("fontSize", "12px");
    assertFalse("red".equalsIgnoreCase($("p", e).css("color")));
    assertFalse("green".equalsIgnoreCase($("p", e).css("background-color")));
    assertEquals(12.0d, GQUtils.cur($("p", e).get(0), "fontSize", true));

    $("p", e, Events.Events).unbind("click");
    $("p", e, Events.Events).trigger(Event.ONCLICK);
    assertFalse("red".equalsIgnoreCase($("p", e).css("color")));
    assertFalse("green".equalsIgnoreCase($("p", e).css("background-color")));
    assertEquals(12.0d, GQUtils.cur($("p", e).get(0), "fontSize", true));
  }

  public void testSubmitEvent() {
    // Add a form and an iframe to the dom. The form target is the iframe
    $(e).html("<form action='whatever' target='miframe'><input type='text' value='Hello'><input type='submit' value='Go'></form><iframe name='miframe' id='miframe' src=\"javascript:''\">");
    testSubmitEventCont = 0;

    // Add an onsubmit function to the form returning false to cancel the action
    $("form").bind(EventsListener.ONSUBMIT, null, new Function() {
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
    $("form").unbind(EventsListener.ONSUBMIT);

    // Check that on submit function is not called and the form has been
    // submitted
    $("form").submit();
    assertEquals(1, testSubmitEventCont);
    new Timer() {
      public void run() {
        // Check that the server returns an error since the action does not
        // exist
        assertTrue($("#miframe").contents().find("body").text().contains("ERROR"));
      }
    }.schedule(500);
  }

  public void testWidgetEvents() {
    final Button b = new Button("click-me");
    b.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        b.getElement().getStyle().setBackgroundColor("black");
      }
    });
    RootPanel.get().add(b);
    $("button").click(lazy().css("color", "red").done());

    $("button").click();
    assertEquals("red", $("button").css("color"));
    assertEquals("black", $("button").css("background-color"));
    RootPanel.get().remove(b);
  }
}
