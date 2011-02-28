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
import static com.google.gwt.query.client.plugins.widgets.Widgets.Widgets;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.css.CSS;
import com.google.gwt.query.client.css.UriValue;
import com.google.gwt.query.client.css.Length;
import com.google.gwt.query.client.css.RGBColor;
import com.google.gwt.query.client.css.BackgroundAttachmentProperty.BackgroundAttachment;
import com.google.gwt.query.client.css.BackgroundPositionProperty.BackgroundPosition;
import com.google.gwt.query.client.css.BackgroundRepeatProperty.BackgroundRepeat;
import com.google.gwt.query.client.plugins.widgets.widgetfactory.ButtonWidgetFactory.ButtonOptions;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;

public class GwtQueryWidgetModule implements EntryPoint {

/*  public void onModuleLoad() {
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
  }*/
  
  public void onModuleLoad() {
    
    
    // Let gquery syntax to help gwt developers. 
    GQuery buttons = $(".btn").as(Widgets).buttons().click(new Function() {
      public void f() {
        Label l = new Label("You click on a GWT Button !");
        PopupPanel panel = new PopupPanel(true, true);
        panel.setGlassEnabled(true);
        panel.add(l);
        panel.center();
      }
    });
    
    // The user use a widget in the traditional way
    buttons.eq(0).<Button>widget().addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        Window.alert("You clicked in the first button");
      }
    });
    
    $("#tabs").as(Widgets).tabPanel();
    
    // IDE suggestions are not available with this syntax. -> I agree
    // Also it implies that the css method in GQuery needs more overloads
    // $("#aaa").css(CSS.POSITION, Position.ABSOLUTE);
    // $("#aaa").css(CSS.TOP, Length.cm(15));
    // $("#aaa").css(CSS.BACKGROUND, RGBColor.RED, ImageValue.url(""), BackgroundRepeat.NO_REPEAT, BackgroundAttachment.FIXED, BackgroundPosition.CENTER);
    
    $("#aaa").css(CSS.TOP.with(Length.cm(15)));
    $("#aaa").css(CSS.BACKGROUND.with(RGBColor.SILVER, UriValue.url(""), BackgroundRepeat.NO_REPEAT, BackgroundAttachment.FIXED, BackgroundPosition.CENTER)); 
    $("#aaa").css(CSS.BACKGROUND_ATTACHMENT.with(BackgroundAttachment.FIXED));
  }
  
  private ButtonOptions createButtonOptions(){
    ButtonOptions options = new ButtonOptions();
    options.addClickHandler(new ClickHandler() {
      
      public void onClick(ClickEvent event) {
        Label l = new Label("You click on a GWT Button !");
        PopupPanel panel = new PopupPanel(true, true);
        panel.setGlassEnabled(true);
        panel.add(l);
        panel.center();
        
      }
    });
    
    return options;
  }
}
