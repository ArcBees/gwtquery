package gwtquery.samples.client.effects;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Element;
import com.google.gwt.query.client.Function;

import static com.google.gwt.query.client.plugins.Effects.Effects;
import static com.google.gwt.query.client.GQuery.$;

public class FadeEffectsSample implements EntryPoint {

  public void onModuleLoad() {
    
    // FadeIn sample
    $("#fadeIn div.foo").hide();
    $("#fadeIn > button").click(new Function() {
      @Override
      public void f(Element e) {
        $("#fadeIn div.foo").as(Effects).fadeIn(2000);
      }
    });
    
    $("#fadeIn > button.reset").click(new Function() {
      @Override
      public void f(Element e) {
        $("#fadeIn div.foo").hide();
      }
    });
    
    
    // FadeOut sample
    $("#fadeOut > button").click(new Function() {
      @Override
      public void f(Element e) {
        $("#fadeOut div.foo").as(Effects).fadeOut(2000);
      }
    });
    
    $("#fadeOut > button.reset").click(new Function() {
      @Override
      public void f(Element e) {
        $("#slideRight div.foo").hide();
      }
    });

 // FadeToogle sample
    $("#fadeToogle > button").click(new Function() {
      @Override
      public void f(Element e) {
        $("#fadeToogle div.foo").as(Effects).fadeToggle(2000);
      }
    });
  

//Toogle sample
  $("#toogle > button").click(new Function() {
    @Override
    public void f(Element e) {
      $("#toogle div.foo").as(Effects).toggle(2000);
    }
  });
  }

}
