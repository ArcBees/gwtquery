package gwtquery.samples.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.query.client.Effects;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import static com.google.gwt.query.client.GQuery.$;
import static com.google.gwt.query.client.GQuery.$$;
import static com.google.gwt.query.client.GQuery.lazy;
import com.google.gwt.user.client.Event;


public class GwtQuerySampleModule implements EntryPoint {
//    public interface Sample extends Selectors {
//      @Selector(".note")
//      GQuery allNotes();

  //    }

  public void onModuleLoad() {
    GQuery q = $(".note");

    $("div > div").
        css("color", "blue").
        hover(
            lazy().
              css("color", "red").
            done(),
            lazy().
              css("color", "blue").
            done());
    $("div.outer > div").css("position", "relative").dblclick(new Function() {
      public boolean f(Event e) {
        $("div.outer > div").as(Effects.Effects).
            animate($$("left: '+=100'"), 400, Effects.Easing.LINEAR, null).
            animate($$("top: '+=100'"), 400, Effects.Easing.LINEAR, null).
            animate($$("left: '-=100'"), 400, Effects.Easing.LINEAR, null).
            animate($$("top: '-=100'"), 400, Effects.Easing.LINEAR, null);

        return true;
      }
    });
    $(".note").click(lazy().fadeOut().done());
    $(".note").append(" Hello");


  }
}
