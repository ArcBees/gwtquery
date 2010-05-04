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
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Test class for testing events plugin.
 */
public class GwtEventsTest extends GWTTestCase {

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
      e.setId("tst");
    } else {
      e.setInnerHTML("");
    }
  }

  // FIXME: this test is broken in IE, and in chrome ONKEYPRESS does not work
  public void testEventsPlugin() {
    $(e).html("<p>Content</p>");

    // click
    $("p", e).click(new Function() {
      public void f(Element elem) {
        $(elem).css("color", "red");
      }
    });
    $("p", e, Events.Events).fire(Event.ONCLICK);
    assertEquals("red", $("p", e).css("color"));

    // unbind
    $("p", e).css("color", "white");
    $("p", e).unbind(Event.ONCLICK);
    $("p", e).trigger(Event.ONCLICK);
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
    $("p", e, Events.Events).fire(Event.ONCLICK);
    assertEquals("red", $("p", e).css("color"));
    $("p", e, Events.Events).fire(Event.ONCLICK);
    assertEquals("blue", $("p", e).css("color"));

    // one
    $("p", e).unbind(Event.ONCLICK);
    $("p", e).one(Event.ONCLICK, null, new Function() {
      public void f(Element elem) {
        $(elem).css("color", "red");
      }
    });
    $("p", e).trigger(Event.ONCLICK);
    assertEquals("red", $("p", e).css("color"));
    $("p", e).css("color", "white");
    $("p", e).trigger(Event.ONCLICK);
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
        Element elem = evnt.getCurrentEventTarget().cast();
        InputElement input = InputElement.as(elem);
        input.setValue(
            input.getValue() + Character.toString((char) evnt.getKeyCode()));
        return false;
      }
    });
    $("input", e).trigger(Event.ONFOCUS);
    $("input", e).trigger(Event.ONKEYPRESS, 'a');
    assertEquals("a", InputElement.as($("input", e).get(0)).getValue());
  }

}
