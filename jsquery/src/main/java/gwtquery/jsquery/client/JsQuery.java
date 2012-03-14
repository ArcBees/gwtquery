package gwtquery.jsquery.client;

import gwtquery.jsquery.client.JQ.Dollar;
import gwtquery.jsquery.client.JQ.JEasing;
import gwtquery.jsquery.client.JQ.JFunction;
import gwtquery.jsquery.client.JQ.JPredicate;

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
    
    GWT.create(JFunction.class);
    GWT.create(JPredicate.class);
    GWT.create(JEasing.class);
    GWT.create(JQ.class);
    GWT.create(Dollar.class);
    JsMenu.loadPlugin();
    onJsQueryLoad();
  }
  
  private native static void onJsQueryLoad() /*-{
    $wnd.onJsQueryLoad && $wnd.onJsQueryLoad();
    $wnd.JsQuery && $wnd.JsQuery.onLoad && $wnd.JsQuery.onLoad();
  }-*/;
}
