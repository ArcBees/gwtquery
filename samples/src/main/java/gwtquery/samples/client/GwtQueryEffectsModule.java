package gwtquery.samples.client;

import com.google.gwt.core.client.EntryPoint;
import static com.google.gwt.query.client.Effects.Easing.LINEAR;
import static com.google.gwt.query.client.Effects.Effects;
import com.google.gwt.query.client.Function;
import static com.google.gwt.query.client.GQuery.$;
import static com.google.gwt.query.client.GQuery.$$;
import static com.google.gwt.query.client.GQuery.lazy;
import com.google.gwt.user.client.Event;

public class GwtQueryEffectsModule implements EntryPoint {

  public void onModuleLoad() {

    $("div > div").
        css("color", "blue").
        hover(lazy().
            css("color", "red").
            done(), lazy().
            css("color", "blue").
            done());

    $("div.outer > div").css("position", "relative").dblclick(new Function() {
      public boolean f(Event e) {
        $("div.outer > div").as(Effects).
            animate($$("left: '+=100'"), 400, LINEAR, null).
            animate($$("top: '+=100'"), 400, LINEAR, null).
            animate($$("left: '-=100'"), 400, LINEAR, null).
            animate($$("top: '-=100'"), 400, LINEAR, null);

        return true;
      }
    });
    $(".note").click(lazy().fadeOut().done());
    $(".note").append(" Hello");
  }
}