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
import static com.google.gwt.query.client.GQuery.document;
import static com.google.gwt.query.client.GQuery.lazy;

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
      EventsListener.clean(e);
      e.setInnerHTML("");
    }
  } 
  
 
  
  public void testDie() {
    $(e).html("<div id='div1'>content</div>");
    $(".clickMe", e).live("click", new Function(){
      public void f(Element e) {
        $(e).css(CSS.COLOR.with(RGBColor.RED));
      }
    });
    
    $(".clickMe", e).live("dblclick", new Function(){
      public void f(Element e) {
        $(e).css(CSS.COLOR.with(RGBColor.BLUE));
      }
    });
    
    $("#div1",e).addClass("clickMe");
    
    $("#div1",e).click();
    assertEquals(RGBColor.RED.getCssName(), $("#div1", e).css(CSS.COLOR));
    
    $("#div1",e).dblclick();
    assertEquals(RGBColor.BLUE.getCssName(), $("#div1", e).css(CSS.COLOR));
    
    //reset
    $("#div1",e).css(CSS.COLOR.with(RGBColor.BLACK));
    
    $(".clickMe", e).die("click");
    $("#div1",e).click();
    assertEquals(RGBColor.BLACK.getCssName(), $("#div1", e).css(CSS.COLOR));
    
    $("#div1",e).dblclick();
    assertEquals(RGBColor.BLUE.getCssName(), $("#div1", e).css(CSS.COLOR));
    
    //reset
    $("#div1",e).css(CSS.COLOR.with(RGBColor.BLACK));
    
    $(".clickMe", e).die("dblclick");
    
    $("#div1",e).dblclick();
    assertEquals(RGBColor.BLACK.getCssName(), $("#div1", e).css(CSS.COLOR));
    
  }
  
  public void testDie2() {
    $(e).html("<div id='div1'>content</div>");
    $(".clickMe", e).live("click", new Function(){
      public void f(Element e) {
        $(e).css(CSS.COLOR.with(RGBColor.RED));
      }
    });
    
    $(".clickMe", e).live("dblclick", new Function(){
      public void f(Element e) {
        $(e).css(CSS.COLOR.with(RGBColor.BLUE));
      }
    });
    
    $("#div1",e).addClass("clickMe");
    
    $("#div1",e).click();
    assertEquals(RGBColor.RED.getCssName(), $("#div1", e).css(CSS.COLOR));
    
    $("#div1",e).dblclick();
    assertEquals(RGBColor.BLUE.getCssName(), $("#div1", e).css(CSS.COLOR));
    
    //reset
    $("#div1",e).css(CSS.COLOR.with(RGBColor.BLACK));
    
    $(".clickMe", e).die();

    $("#div1",e).click();
    assertEquals(RGBColor.BLACK.getCssName(), $("#div1", e).css(CSS.COLOR));

    $("#div1",e).dblclick();
    assertEquals(RGBColor.BLACK.getCssName(), $("#div1", e).css(CSS.COLOR));
    
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
    assertEquals("yellow", $("p", e).css("color"));
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
    assertEquals("red", $("p", e).css("color"));
    assertEquals("green", $("p", e).css("background-color"));

    // unbind
    $("p", e).css(CSS.COLOR.with(RGBColor.WHITE));
    $("p", e).unbind(Event.ONCLICK);
    $("p", e).click();
    assertEquals("white", $("p", e).css("color"));

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
    assertEquals("red", $("p", e).css("color"));
    $("p", e).click();
    assertEquals("blue", $("p", e).css("color"));

    // one
    $("p", e).unbind(Event.ONCLICK);
    $("p", e).one(Event.ONCLICK, null, new Function() {
      public void f(Element elem) {
        $(elem).css(CSS.COLOR.with(RGBColor.RED));
      }
    });
    $("p", e).click();
    assertEquals("red", $("p", e).css("color"));
    $("p", e).css(CSS.COLOR.with(RGBColor.WHITE));
    $("p", e).click();
    assertEquals("white", $("p", e).css("color"));

    // hover (mouseover, mouseout)
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
    assertEquals("yellow", $("p", e).css("background-color"));
    $("p", e).trigger(Event.ONMOUSEOUT);
    assertEquals("white", $("p", e).css("background-color"));

//    // focus
//    FIXME: Html 2.1.0 failing but FF do not
//    $("p", e).focus(new Function() {
//      public void f(Element elem) {
//        $(elem).css("border", "1px dotted black");
//      }
//    });
//    $("p", e).focus();
//    assertEquals("black", $("p", e).css("border-top-color"));
//    assertEquals("dotted", $("p", e).css("border-top-style"));
//    assertEquals("1px", $("p", e).css("border-top-width"));
//
//    // blur
//    $("p", e).blur(new Function() {
//      public void f(Element elem) {
//        $(elem).css("border", "");
//      }
//    });
//    $("p", e).blur();
//    assertEquals("", $("p", e).css("border"));

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
    $(e).css(CSS.COLOR.with(RGBColor.WHITE));
    assertEquals("white", $(e).css("color"));

    $(e).one(Event.ONCLICK, null, lazy().css(CSS.COLOR.with(RGBColor.RED)).done());
    $(e).click();
    assertEquals("red", $(e).css("color"));

    $(e).click(lazy().css(CSS.COLOR.with(RGBColor.BLACK)).done());
    $(e).click();
    assertEquals("black", $(e).css("color"));
  }

  public void testLive() {
    $(e).html("<div id='div1' class='clickMe'><div id='div2'>Content 1<span id='span1'> blop</span></div></div>");
    $(".clickMe", e).live("click", new Function(){
      public void f(Element el) {
        $(el).css("color", "red");
      }
    });
    
    $(e).append("<div id='div3' class='clickMe'>Content 2 <div id='div4'><span id='span2'>blop</span></div></div>");

    $(".clickMe", e).click();
    assertEquals("red", $("#div1", e).css("color"));
    assertEquals("red", $("#div3", e).css("color"));
    
    //reset
    $("*", e).css(CSS.COLOR.with(RGBColor.BLACK));
    assertEquals("black", $("div", e).css("color"));
    assertEquals("black", $("span", e).css("color"));

    $("#span1", e).click();
    assertEquals("red", $("#div1", e).css("color"));
    assertEquals("black", $("#div3", e).css("color"));
    
     //reset
    $("*", e).css(CSS.COLOR.with(RGBColor.BLACK));
    
    $("#span2", e).click();
    assertEquals("black", $("#div1", e).css("color"));
    assertEquals("red", $("#div3", e).css("color"));
    
    //reset
    $("*", e).css(CSS.COLOR.with(RGBColor.BLACK));
    
    $("#div2, #div4", e).addClass("clickMe");
    
    $("#span1", e).click();
    assertEquals("red", $("#div1", e).css("color"));
    assertEquals("red", $("#div2", e).css("color"));
    assertEquals("black", $("#div3", e).css("color"));
    assertEquals("black", $("#div4", e).css("color"));

  //reset
    $("*", e).css(CSS.COLOR.with(RGBColor.BLACK));
          
    $("#span2", e).click();
    assertEquals("black", $("#div1", e).css("color"));
    assertEquals("black", $("#div2", e).css("color"));
    assertEquals("red", $("#div3", e).css("color"));
    assertEquals("red", $("#div4", e).css("color"));

  }

  public void testLive2() {
    
    $(e).html("<div id='div1'><div id='div2'>Content 1<span id='span1'> blop</span></div></div>");
    
    $(".clickable", e).live("click", new Function(){
      public void f(Element e) {
        $(e).css(CSS.COLOR.with(RGBColor.RED));
      }
    });
    
    $(".clickable2", e).live("click", new Function(){
      public void f(Element e) {
        $(e).css(CSS.COLOR.with(RGBColor.BLUE));
      }
    });
    
    $(".hover", e).live("mouseover", new Function(){
      public void f(Element e) {
        $(e).css(CSS.BACKGROUND_COLOR.with(RGBColor.YELLOW));
      }
    });
    
    $("#div1", e).addClass("clickable");
    $("#div2", e).addClass("clickable2", "hover");
    
    $("#span1", e).click();
    
    assertEquals("red", $("#div1", e).css(CSS.COLOR));
    assertEquals("blue", $("#div2", e).css(CSS.COLOR));
    assertNotSame("yellow", $("#div2", e).css(CSS.BACKGROUND_COLOR));
    
    
  }
  
public void testLiveWithMultipleFunction() {
    
    $(e).html("<div id='div1'><div id='div2'>Content 1<span id='span1'> blop</span></div></div>");
    
    $(".clickable", e).live("click", new Function(){
      public void f(Element e) {
        $(e).css(CSS.COLOR.with(RGBColor.RED));
      }
    }, new Function(){
      public void f(Element e) {
        $(e).css(CSS.BACKGROUND_COLOR.with(RGBColor.YELLOW));
      }
    });
    
    $("#div1", e).addClass("clickable");
    
    $("#span1", e).click();
    
    assertEquals("red", $("#div1", e).css(CSS.COLOR));
    assertNotSame("yellow", $("#div1", e).css(CSS.BACKGROUND_COLOR));
    
    
  }

public void testLiveWithMultipleEvent() {
  
  $(e).html("<div id='div1'><div id='div2'>Content 1<span id='span1'> blop</span></div></div>");
  
  $(".clickable", e).live(Event.ONCLICK | Event.ONMOUSEMOVE, new Function(){
    public void f(Element e) {
      $(e).css(CSS.COLOR.with(RGBColor.RED));
    }
  });
  
  $("#div1", e).addClass("clickable");
  
  $("#span1", e).click();
  
  assertEquals("red", $("#div1", e).css(CSS.COLOR));
 
  //reset
  $("#div1", e).css(CSS.COLOR.with(RGBColor.BLACK));
  
  
  
}


  public void testNamedBinding() {
    $(e).html("<p>Content</p>");

    $("p", e, Events.Events).bind("click.first.namespace", null, new Function() {; 
      public void f(Element elem) {
        $(elem).css(CSS.COLOR.with(RGBColor.RED));
      }
    });
    $("p", e, Events.Events).bind("click.second.namespace", null, new Function() {; 
      public void f(Element elem) {
        $(elem).css(CSS.BACKGROUND_COLOR.with(RGBColor.GREEN));
      }
    });
    $("p", e, Events.Events).bind("click", null, new Function() {; 
      public void f(Element elem) {
        $(elem).css(CSS.FONT_SIZE.with(Length.px(24)));
      }
    });
    $("p", e, Events.Events).trigger(Event.ONCLICK);
    assertEquals("red", $("p", e).css("color"));
    assertEquals("green", $("p", e).css("background-color"));
    assertEquals(24.0d, $("p", e).cur("fontSize", true));
    
    $("p", e).css(CSS.COLOR.with(null)).css(CSS.BACKGROUND_COLOR,"").css(CSS.FONT_SIZE.with(Length.px(12)));
    assertFalse("red".equalsIgnoreCase($("p", e).css("color")));
    assertFalse("green".equalsIgnoreCase($("p", e).css("background-color")));
    assertEquals(12.0d, $("p", e).cur("fontSize", true));

    $("p", e, Events.Events).unbind("click.first.namespace");
    $("p", e, Events.Events).trigger(Event.ONCLICK);
    assertFalse("red".equalsIgnoreCase($("p", e).css("color")));
    assertEquals("green", $("p", e).css("background-color"));
    assertEquals(24.0d, $("p", e).cur("fontSize", true));
    
    
    $("p", e).css(CSS.COLOR.with(null)).css(CSS.BACKGROUND_COLOR,"").css(CSS.FONT_SIZE.with(Length.px(12)));
    assertFalse("red".equalsIgnoreCase($("p", e).css("color")));
    assertFalse("green".equalsIgnoreCase($("p", e).css("background-color")));
    assertEquals(12.0d, $("p", e).cur("fontSize", true));

    $("p", e, Events.Events).unbind("click");
    $("p", e, Events.Events).trigger(Event.ONCLICK);
    assertFalse("red".equalsIgnoreCase($("p", e).css("color")));
    assertFalse("green".equalsIgnoreCase($("p", e).css("background-color")));
    assertEquals(12.0d, $("p", e).cur("fontSize", true));
  }

  public void testRebind() {
    final GQuery b = $("<p>content</p>");
    b.click(new Function() {
      public void f(Element e){
        b.css(CSS.COLOR.with(RGBColor.RED));
      }
    });
    $(e).append(b);
    b.click();
    assertEquals("red", $(b).css("color"));
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
  
  /**
   * Test for issue 62
   * http://code.google.com/p/gwtquery/issues/detail?id=62
   */
  public void testTabInbexInFocusEventBinding(){
    String content="<div id='test'>test content</div>";
    $(e).html(content);
    $("#test").focus(new Function(){});
    
    assertEquals($("#test").attr("tabIndex"), "0");
    
    content="<div id='test' tabIndex='2'>test content</div>";
    $(e).html(content);
    $("#test").focus(new Function(){});
    
    assertEquals($("#test").attr("tabIndex"), "2");
  }
  
  

  public void testWidgetEvents() {
    final Button b = new Button("click-me");
    b.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        b.getElement().getStyle().setBackgroundColor("black");
      }
    });
    RootPanel.get().add(b);
    $(b).click(lazy().css(CSS.COLOR.with(RGBColor.RED)).done());

    $(b).click();
    assertEquals("red", $("button").css("color"));
    assertEquals("black", $("button").css("background-color"));
    RootPanel.get().remove(b);
    
    $(e).append($(b));
    $(b).css(CSS.COLOR.with(RGBColor.YELLOW), CSS.BACKGROUND_COLOR.with(RGBColor.BLUE));
    $(b).click();
    assertEquals("red", $("button").css("color"));
    assertEquals("black", $("button").css("background-color"));
  }

}
