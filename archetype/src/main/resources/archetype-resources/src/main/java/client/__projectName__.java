#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.client;
import com.google.gwt.dom.client.Element;
import com.google.gwt.query.client.Function;
import static com.google.gwt.query.client.GQuery.*;


import com.google.gwt.core.client.EntryPoint;

/**
 * Example code for a GwtQuery application
 */
public class ${projectName} implements EntryPoint {

  public void onModuleLoad() {

    ${symbol_dollar}("div")
    .hover(new Function() {
      public void f(Element e) {
        ${symbol_dollar}(e).css("color", "blue").stop(true, true).animate("fontSize: '+=10px'");
      }
    }, new Function() {
      public void f(Element e) {
        ${symbol_dollar}(e).css("color", "").stop(true, true).animate("fontSize: '-=10px'");
      }
    });
  }

}
