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
    OverlayGQuery.onLoad();

//    testJs();
  }
  
  private native static void testJs() /*-{
    var l = @gwtquery.jsquery.client.utils.JsQueryUtils::log(Ljava/lang/Object;);
    
  var options = {minWidth: 120, arrowSrc: 'arrow_right.gif', copyClassAttr: true, onClick: function(e, menuItem){
    alert('you clicked item "' + $(this).text() + '"');
  }};
  $('#menuone').menu(options);
  
    var items = [ {src: 'test', url:'http://www.jquery.com'},
          {src: ''}, // separator
          {src: 'test2', subMenu: [ {src: 'sub 1'},
                        {src: 'sub 2', url: 'http://p.sohei.org', target: '_blank'},
                        {src: 'sub 3'}]}];
    $('#menutwo').menu(options, items);
  
  
  }-*/;
  
}
