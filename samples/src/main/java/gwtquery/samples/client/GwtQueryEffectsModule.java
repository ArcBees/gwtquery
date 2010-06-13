package gwtquery.samples.client;

import static com.google.gwt.query.client.GQuery.$;
import static com.google.gwt.query.client.GQuery.$$;
import static com.google.gwt.query.client.GQuery.lazy;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Element;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.plugins.Effects;
import com.google.gwt.query.client.plugins.PropertiesAnimation.Easing;
import com.google.gwt.user.client.Event;

public class GwtQueryEffectsModule implements EntryPoint {

  public void onModuleLoad() {
    $("div > div").css("color", "blue")
    .hover(lazy().css("color", "red").done(),
           lazy().css("color", "blue").done());

    $("div.outer > div").css("position", "relative").dblclick(new Function() {
      public boolean f(Event e) {
         $("div.outer > div").as(Effects.Effects).
         animate($$("left: '+=100'"), 400, Easing.LINEAR).
         animate($$("top: '+=100'"), 400, Easing.LINEAR).
         animate($$("left: '-=100'"), 400, Easing.LINEAR).
         animate($$("top: '-=100'"), 400, Easing.LINEAR);
        return true;
      }
    });
    $(".note").click(lazy().fadeOut().done());
    $(".note").append(" Hello");
    
    final Effects a = $(".a, .b > div:nth-child(2)").as(Effects.Effects);
    
    $("#b0").width(150).css("font-size", "10px").toggle(new Function() {
      public void f(Element e) {
        $("#b0").as(Effects.Effects).animate(" width: '400', opacity: '0.4', marginLeft: '0.6in', fontSize: '24px'");
      }
    }, new Function() {
      public void f(Element e) {
        $("#b0").as(Effects.Effects).animate(" width: '150', opacity: '1', marginLeft: '0', fontSize: '10px'");
      }
    });
    
    $("#b1").toggle(new Function() {
      public void f(Element e) {
        $(".a").toggle();
      }
    }, new Function() {
      public void f(Element e) {
        a.fadeOut();
      }
    }, new Function() {
      public void f(Element e) {
        a.fadeIn();
      }
    }, new Function() {
      public void f(Element e) {
        a.slideUp();
      }
    }, new Function() {
      public void f(Element e) {
        a.slideDown();
      }
    }, new Function() {
      public void f(Element e) {
        a.slideLeft();
      }
    }, new Function() {
      public void f(Element e) {
        a.slideRight();
      }
    }, new Function() {
      public void f(Element e) {
        a.animate("left: '+=300', width: 'hide'");
      }
    }, new Function() {
      public void f(Element e) {
        a.animate("left: '-=300', width: 'show'");
      }
    });
    
    $("#b2").toggle(new Function() {
      public void f(Element e) {
        $(".a").toggle();
      }
    }, new Function() {
      public void f(Element e) {
        a.as(Effects.Effects).clipUp();
      }
    }, new Function() {
      public void f(Element e) {
        a.as(Effects.Effects).clipDown();
      }
    }, new Function() {
      public void f(Element e) {
        a.as(Effects.Effects).clipDisappear();
      }
    }, new Function() {
      public void f(Element e) {
        a.as(Effects.Effects).clipAppear();
      }
    });
    
  }
}