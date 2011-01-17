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