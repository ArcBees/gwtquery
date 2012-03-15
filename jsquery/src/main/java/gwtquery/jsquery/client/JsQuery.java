package gwtquery.jsquery.client;

import java.util.logging.Logger;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

public class JsQuery implements EntryPoint {
  
  public void onModuleLoad() {
    GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
      Logger l = Logger.getLogger("jsQuery");
      public void onUncaughtException(Throwable e) {
        String r = "";
        for (StackTraceElement s :e.getStackTrace()) {
          r += s + "\n";
        }
        l.info(r);
      }
    });    
    
    OverlayGQuery.export();
    JsMenu.loadPlugin();
    onJsQueryLoad();
//    testJs();
  }
  
  private native static void testJs() /*-{
    var l = @gwtquery.jsquery.client.utils.JsQueryUtils::log(Ljava/lang/Object;);
    l($.each);
    $.each(["a","b"], function(a, b){
      l("kk " + " " + a + " " + b);
    });
  }-*/;
  
  private native static void onJsQueryLoad() /*-{
    $ = $wnd.$;
    window = $wnd;
    document = $doc;
    
    $wnd.onJsQueryLoad && $wnd.onJsQueryLoad();
    $wnd.JsQuery && $wnd.JsQuery.onLoad && $wnd.JsQuery.onLoad();
  }-*/;
}
