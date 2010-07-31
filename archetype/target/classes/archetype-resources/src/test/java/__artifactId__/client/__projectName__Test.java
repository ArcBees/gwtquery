#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${artifactId}.client;

import static com.google.gwt.query.client.GQuery.*;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.query.client.GQUtils;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.plugins.Effects.Speed;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
/**
 * Test class for ${projecName} entry-point
 */
public class ${projectName}Test extends GWTTestCase {

  public String getModuleName() {
    return "${package}.${artifactId}.${projectName}";
  }

  private double fontSize(GQuery g) {
    return GQUtils.cur(g.get(0), "fontSize", true);
  }

  public void testOnModuleLoad() {
    
    // execute the plugin method
    final GQuery g =  $("<div></div>").appendTo(document);

    // run onModuleLoad
    Art a = new Art();
    a.onModuleLoad();

    // delay the test
    delayTestFinish(Speed.DEFAULT * 3);

    // trigger mouse over event
    final double size1 = fontSize(g);
    g.trigger(Event.ONMOUSEOVER);
    new Timer() {
      public void run() {
        // assert that the font size increases
        assertTrue(fontSize(g) > size1);
        
        // trigger mouse out event
        final double size2 = fontSize(g);
        g.trigger(Event.ONMOUSEOUT);
        new Timer() {
          public void run() {
            // assert that the font size decreases
            assertTrue(fontSize(g) < size2);
            g.remove();
            
            // finish the test
            finishTest();
          }
        }.schedule(Speed.DEFAULT);
      }
    }.schedule(Speed.DEFAULT);
  }

}
