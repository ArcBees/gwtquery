package gwtquery.samples.client;

import static com.google.gwt.query.client.GQuery.$;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Element;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.plugins.Effects;

/**
 * @author Manolo Carrasco
 */
public class GwtQueryImageZoom implements EntryPoint {

  public void onModuleLoad() {
    // Fancy Thumbnail Hover Effect w/ jQuery - by Soh Tanaka
    // http://www.sohtanaka.com/web-design/examples/image-zoom/
    $("ul.thumb li").hover(new Function() {
      public void f(Element e) {
      $(e).css("z-index", "10").find("img").addClass("hover")
        .as(Effects.Effects).stop()
        .animate("marginTop: '-110px', marginLeft: '-110px', top: '50%', left: '50%', width: '174px', height: '174px', padding: '20px'", 200);
      }} , new Function() {
        public void f(Element e) {
      $(e).css("z-index", "0").find("img").removeClass("hover")
        .as(Effects.Effects).stop()
        .animate("marginTop: '0', marginLeft: '0', top: '0%', left: '0%', width: '100px', height: '100px', padding: '5px'", 600);
    }});
    
  }
}