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
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.query.client.plugins.widgets.WidgetInitializer;
import com.google.gwt.query.client.plugins.widgets.ListBoxWidgetFactory.ListBoxOptions;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class GwtQueryWidgetModule implements EntryPoint {

  public void onModuleLoad() {

    $(".inputText").as(Widgets).textBox();
    $(".inputPsw").as(Widgets).passwordBox();

    $(".btn").as(Widgets).button(new WidgetInitializer() {

      public void initialize(Widget w, Element e) {
        final String tag = e.getTagName();
        Button button = (Button) w;
        button.addClickHandler(new ClickHandler() {

          public void onClick(ClickEvent event) {
            Label l = new Label("You click on a GWT Button create from a "+tag);
            PopupPanel panel = new PopupPanel(true, true);
            panel.setGlassEnabled(true);
            panel.add(l);
            panel.center();

          }
        });

      }

    });

    $("#tabs").as(Widgets).tabPanel();
    $(".date").as(Widgets).datebox();
    $(".editable").as(Widgets).richtext();  
    $("#disclosure").as(Widgets).disclosurePanel();
    $(".list").as(Widgets).listBox();
    $("#multiSelect").as(Widgets).listBox(new ListBoxOptions(".item", true));
  }

}
