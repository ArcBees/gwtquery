package gwtquery.samples.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.query.client.Function;
import static com.google.gwt.query.client.GQuery.$;
import com.google.gwt.query.client.plugins.Ratings;
import com.google.gwt.query.client.plugins.Widgets;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;


public class GwtQueryPluginModule implements EntryPoint {

  public void onModuleLoad() {

    $(".outer").eq(0).after("<button id='enhance'>Enhance</button>");
    $("#enhance").one(Event.ONCLICK, null, new Function() {
      @Override
      public boolean f(Event e) {
        $(e).attr("disabled", "true");
        $("input").as(Ratings.Ratings).rating();
        return true;
      }
    });
    $(".btn:nth-child(odd)").as(Widgets.Widgets).button()
        .addClickHandler(new ClickHandler() {
          public void onClick(ClickEvent clickEvent) {
            Window.alert("You Clicked the Button");
          }
        }).end();

  }
}