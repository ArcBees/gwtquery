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
package gwtquery.samples.client;

import static com.google.gwt.query.client.GQuery.$;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.plugins.Widgets;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;

public class GwtQueryWidgetModule implements EntryPoint {

  public void onModuleLoad() {
    $("<button>Enhance</button>").appendTo(".outer").one(Event.ONCLICK, null, new Function() {
      public boolean f(Event e) {
        $(".btn:nth-child(odd)").each(new Function(){
          public void f(Element el) {
            // Replace odd labels by a button
            GQuery g = $("<button/>");
            $(el).hide().after(g);
            
            // Use the Widgets plugin to convert the button element to a button.
            Button b = g.as(Widgets.Widgets).widget();
            b.setText("Foo");
            b.addClickHandler(new ClickHandler() {
              public void onClick(ClickEvent clickEvent) {
                Window.alert("You Clicked the Button");
              }
            });
          }
        });
        return true;
      }
    });
  }
}
