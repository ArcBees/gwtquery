package gwtquery.samples.client.effects;

import static com.google.gwt.query.client.GQuery.$;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.Properties;
import com.google.gwt.query.client.css.CSS;
import com.google.gwt.query.client.plugins.effects.PropertiesAnimation.Easing;

public class ColorEffectsSample implements EntryPoint {

  public void onModuleLoad() {

    $("#shoot").click(new Function() {
      
      public void f() {
        $("body").animate("backgroundColor: 'red'", 400)
          .delay(1000)
          .animate("backgroundColor: 'white'", 2000);
      }
      
    });
    
    $("#startAnim2").click(new Function(){
    
      public void f() {
        $(".bar").animate("backgroundColor: 'yellow'", 1000)
          .delay(200)
          .animate("borderColor: '#ff0000'",1000)
          .delay(200)
          .animate(Properties.create().$$("color","rgb(255, 255, 255)"), 1000, Easing.SWING);
      }
    
    });
    
    $("#resetAnim2").click(new Function(){
      
      public void f() {
        $(".bar").css(CSS.BACKGROUND_COLOR.with(null), CSS.BORDER_COLOR.with(null), CSS.COLOR.with(null));
      }
    
    });
  }

}
