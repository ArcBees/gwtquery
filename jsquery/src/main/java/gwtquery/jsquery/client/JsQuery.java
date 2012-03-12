package gwtquery.jsquery.client;

import gwtquery.jsquery.client.JQ.Dollar;
import gwtquery.jsquery.client.JQ.JEasing;
import gwtquery.jsquery.client.JQ.JFunction;
import gwtquery.jsquery.client.JQ.JOffset;
import gwtquery.jsquery.client.JQ.JPredicate;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Timer;
import static com.google.gwt.user.client.Window.*;

public class JsQuery implements EntryPoint {
  
  public void onModuleLoad() {
//    GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
//      public void onUncaughtException(Throwable e) {
//        String r = "";
//        for (StackTraceElement s :e.getStackTrace()) {
//          r += s + "\n";
//        }
//        Window.alert(r);
//      }
//    });    
    
    GWT.create(JFunction.class);
    GWT.create(JPredicate.class);
    GWT.create(JOffset.class);
    GWT.create(JEasing.class);
    GWT.create(JQ.class);
//    new JQExporterImpl();
    GWT.create(Dollar.class);
//    new DollarExporterImpl();
//    System.out.println(testExtend("kaka"));
    JsMenu.loadPlugin();
    myJs();
//    onJsQueryLoad();
  }

  private native static void myJs() /*-{
  try {
    $ = $wnd.$;
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
      $('#menuthree').menu(options);
      $('#menufive>img').menu(options, '#menufivelist');
      
    //  creating a menu without items
      var menu = new $.Menu('#menufour', null, options);
    //  adding items to the menu
      menu.addItems([
        new $.MenuItem({src: 'test', url:'http://www.jquery.com'}, options),
        new $.MenuItem({src: ''}) // separator
      ]);
      var itemWithSubmenu = new $.MenuItem({src: 'test2'}, options);
    //  creating a menu with items (as child of itemWithSubmenu)
      new $.Menu(itemWithSubmenu, [
        new $.MenuItem({src: 'sub 1'}, options),
        new $.MenuItem({src: 'sub 2', url: 'http://p.sohei.org', target: '_blank'}, options),
        new $.MenuItem({src: 'sub 3'}, options)
      ], options);
    //  adding the submenu to the main menu
      menu.addItem(itemWithSubmenu);
    

  } catch(e){alert(e);}
  }-*/;
  
  private native static String testExtend(String s) /*-{
    $wnd.$.extend({hola: function(){return s;}});
    return $wnd.$('*').hola();
  }-*/;

  
  private native static void onJsQueryLoad() /*-{
    $wnd.onJsQueryLoad && $wnd.onJsQueryLoad();
    $wnd.JsQuery && $wnd.JsQuery.onLoad && $wnd.JsQuery.onLoad();
  }-*/;
}
