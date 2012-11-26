package com.google.gwt.query.jsquery.client;

import java.util.logging.Logger;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.query.jsquery.client.GQueryOverlay.FunctionOverlay;
import com.google.gwt.query.jsquery.client.GQueryOverlay.PredicateOverlay;

public class JsQuery implements EntryPoint {

  public void onModuleLoad() {
    GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
      Logger l = Logger.getLogger("JsQuery");
      public void onUncaughtException(Throwable e) {
        String r = "";
        for (StackTraceElement s :e.getStackTrace()) {
          r += s + "\n";
        }
        l.info(r);
      }
    });
    // We just export the api, but we do not call GQueryOverlay.onLoad() so as
    // apps and plugins could load their stuff before calling it
    export();

    // Un comment for testing stuff below
    // testJs();
  }

  public static void export() {
    GWT.create(FunctionOverlay.class);
    GWT.create(PredicateOverlay.class);
    GWT.create(GQueryOverlay.class);
  }

  public static native void onLoad() /*-{
    $wnd.onJsQueryLoad && $wnd.onJsQueryLoad();
    $wnd.JsQuery && $wnd.JsQuery.onLoad && $wnd.JsQuery.onLoad();
  }-*/;

  /**
   * Used to paste jquery code and test it in dev mode
   */
  private native static void testJs() /*-{
    var l = @com.google.gwt.query.jsquery.client.JsQueryUtils::log(Ljava/lang/Object;);
    window = $wnd;
    document = $doc;
    $ = $wnd.$;

    // Paste jquery code here

  }-*/;
}
