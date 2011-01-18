package gwtquery.samples.client.effects;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Element;
import com.google.gwt.query.client.Function;

import static com.google.gwt.query.client.plugins.Effects.Effects;
import static com.google.gwt.query.client.GQuery.$;

public class SlideEffectsSample implements EntryPoint {

  public void onModuleLoad() {

    // SlideUp sample
    $("#slideUp > button").click(new Function() {
      @Override
      public void f(Element e) {
        $("#slideUp div.foo").as(Effects).slideUp();
      }
    });
    
    $("#slideUp > button.reset").click(new Function() {
      @Override
      public void f(Element e) {
        $("#slideUp div.foo").show();
      }
    });

    // SlideDown sample
    $("#slideDown div.foo").hide();
    $("#slideDown > button").click(new Function() {
      @Override
      public void f(Element e) {
        $("#slideDown div.foo").as(Effects).slideDown();
      }
    });
    
    $("#slideDown > button.reset").click(new Function() {
      @Override
      public void f(Element e) {
        $("#slideDown div.foo").hide();
      }
    });

    // SlideToogle sample
    $("#slideToogle > button").click(new Function() {
      @Override
      public void f(Element e) {
        $("#slideToogle div.foo").as(Effects).slideToggle(400);
      }
    });
    
    // SlideLeft sample
    $("#slideLeft > button").click(new Function() {
      @Override
      public void f(Element e) {
        $("#slideLeft div.foo").as(Effects).slideLeft();
      }
    });
    
    $("#slideLeft > button.reset").click(new Function() {
      @Override
      public void f(Element e) {
        $("#slideLeft div.foo").show();
      }
    });
    
    
    // SlideRight sample
    $("#slideRight div.foo").hide();
    $("#slideRight > button").click(new Function() {
      @Override
      public void f(Element e) {
        $("#slideRight div.foo").as(Effects).slideRight();
      }
    });
    
    $("#slideRight > button.reset").click(new Function() {
      @Override
      public void f(Element e) {
        $("#slideRight div.foo").hide();
      }
    });
    
  }

}
