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
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.junit.DoNotRunWith;
import com.google.gwt.junit.Platform;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Test class for testing events plugin.
 */
public class GwtEventsTest extends GWTTestCase {

  static Element e = null;

  static HTML testPanel = null;

  public String getModuleName() {
    return "com.google.gwt.query.Test";
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
  
  public void testWidgetEvents() {
    final Button b = new Button("click-me");
    b.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        b.getElement().getStyle().setBackgroundColor("black");
      }
    });
    RootPanel.get().add(b);
    $("button").click(new Function(){
      public boolean f(Event e) {
        $(e).css("color", "red");
        return true;
      }
    });
    $("button").click();
    assertEquals("red", $("button").css("color"));    
    assertEquals("black", $("button").css("background-color"));    
    RootPanel.get().remove(b);
  }
}
