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

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.query.client.plugins.Widgets;
import com.google.gwt.query.client.plugins.widgets.WidgetFactory;
import com.google.gwt.query.client.plugins.widgets.WidgetsUtils;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Test class for testing gwtquery widgets plugin api.
 */
public class GQueryWidgetsTestGwt extends GWTTestCase {

  static Element e = null;

  static HTML testPanel = null;

  int testSubmitEventCont = 0;

  public String getModuleName() {
    return "com.google.gwt.query.QueryTest";
  }

  public void testGWTQueryCoreWidgets() {
    final FlowPanel p = new FlowPanel();
    Button b = new Button("test");
    RootPanel.get().add(b);
    RootPanel.get().add(p);

    int nitems = 4;
    final String label1 = "I'm the label ";
    final String label2 = "Finally I'm just a simple label";

    for (int i = 0; i < nitems; i++) {
      Label l = new Label(label1 + i);
      p.add(l);
    }
    $("<div>whatever</div").appendTo($(p));

    b.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        $(".gwt-Label", p).each(new Function() {
          @Override
          public void f(Widget w) {
            Label l = (Label) w;
            l.setText(label2);
          }
        });
      }
    });

    $(".gwt-Label", p).each(new Function() {
      @Override
      public Object f(Widget w, int i) {
        assertEquals(label1 + i, ((Label)w).getText());
        return null;
      }
    });

    $(b).click();

    $(".gwt-Label", p).each(new Function() {
      public void f(Element e) {
        assertEquals(label2, $(e).text());
      }
    });

    $("div", p).each(new Function() {
      public void f(Element e) {
        // Just to avoid the exception when non-widget elements match
      }
      public void f(Widget w) {
        if (w instanceof Label) {
          assertEquals(label2, $(w).text());
        }
      }
    });

    p.removeFromParent();
    b.removeFromParent();
  }

  class TestButtonWidgetFactory implements WidgetFactory<Button> {
    public Button create(Element e) {
      Button button = new Button();
      button.getElement().setInnerText(e.getInnerText());
      WidgetsUtils.replaceOrAppend(e, button);
      return button;
    }
  }

  public void testGQueryWidgets() {
    final Button b1 = new Button("click-me");
    RootPanel.get().add(b1);
    GQuery g = $(b1);
    Button b2 = (Button) g.as(Widgets.Widgets).widget();
    assertEquals(b1, b2);

    b2 = $("<button>Click-me</button>").appendTo(document)
       .as(Widgets.Widgets).widgets(new TestButtonWidgetFactory(), null).widget();
    b2.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        $(b1).css("color", "red");
      }
    });

    b2.click();
    assertEquals("red", $(b1).css("color", false));
  }
  
  public void testSelectorWidget() {
    final Button b1 = new Button("Button click");
    RootPanel.get().add(b1);
    GQuery g = $(b1);
    assertTrue(g.isVisible());
    g.hide();
    assertFalse(g.isVisible());
    b1.removeFromParent();
  }

}
