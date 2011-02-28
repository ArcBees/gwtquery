/*
 * Copyright 2011, The gwtquery team.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package gwtquery.samples.client;

import static com.google.gwt.query.client.GQuery.$;
import static com.google.gwt.query.client.GQuery.document;
import static com.google.gwt.query.client.GQuery.lazy;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.Selector;
import com.google.gwt.query.client.Selectors;
import com.google.gwt.query.client.css.CSS;
import com.google.gwt.query.client.css.RGBColor;
import com.google.gwt.user.client.Window;

public class GwtQuerySampleModule implements EntryPoint {

  public interface Sample extends Selectors {
    @Selector(".note")
    GQuery allNotes();
  }

  public void onModuleLoad() {
    // Use compiled selectors
    Sample s = GWT.create(Sample.class);
    
    // Just a simple usage of dom manipulation, events, and lazy usage
    s.allNotes().text("Hello Google I/O").
      css(CSS.CURSOR.with(Cursor.POINTER)).
      toggle(
        lazy().css(CSS.COLOR.with(RGBColor.RED)).done(),
        lazy().css(CSS.COLOR.with(null)).done()
      );
    
    // Cascade effects
    $("<div id='id' style='font-size: 150%;'>Cascade Efects</div>").appendTo(document).hide().fadeIn(5000).fadeOut(3000);
    
    int gqw = $(".outer").width();
    String jqw = runJsCommand("$wnd.$('.outer').width()");
    
    int gqh = $(".outer").height();
    String jqh = runJsCommand("$wnd.$('.outer').height()");
    
    String msg = ".outer size: GQuery: " + gqw + "x" + gqh + " jQuery: " + jqw + "x" + jqh;
    Window.alert(msg);
  }
  
  private native String runJsCommand(String js) /*-{
    try {
      return "" + eval(js);
    } catch (e) {
      return "" + e;
    }
  }-*/;
}
