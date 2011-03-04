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
import static com.google.gwt.query.client.plugins.Widgets.Widgets;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.query.client.Function;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

public class GwtQueryWidgetModule implements EntryPoint {

  public void onModuleLoad() {

    $(".inputText").as(Widgets).textBox();
    
    $(".btn").as(Widgets).button(new Function() {
      
      public void f(Widget w) {
        
        Button button = (Button) w;
        button.addClickHandler(new ClickHandler() {

          public void onClick(ClickEvent event) {
            Label l = new Label("You click on a GWT Button !");
            PopupPanel panel = new PopupPanel(true, true);
            panel.setGlassEnabled(true);
            panel.add(l);
            panel.center();

          }
        });

      }
      
    });

    $("#tabs").as(Widgets).tabPanel();

    DateBox b = $(".date").as(Widgets).datebox().widget();
    System.out.println(b);
    
    RichTextArea a = $(".editable").as(Widgets).richtext().widget(0);
    System.out.println(a);
    
  }

}
