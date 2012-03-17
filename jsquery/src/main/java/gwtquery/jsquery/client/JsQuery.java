package gwtquery.jsquery.client;

import gwtquery.jsquery.client.plugins.menu.JsMenu;

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
    OverlayGQuery.onLoad();

//    testJs();
  }
  
  /**
   * Useful to paste js code here and test in dev mode
   */
  private native static void testJs() /*-{
    var l = @gwtquery.jsquery.client.utils.JsQueryUtils::log(Ljava/lang/Object;);
    window = $wnd;
    document = $doc;
    $ = $wnd.$;
    
  }-*/;
}
