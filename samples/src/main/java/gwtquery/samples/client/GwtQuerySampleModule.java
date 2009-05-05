package gwtquery.samples.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.query.client.$;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.Widgets;
import com.google.gwt.query.client.Function;
import static com.google.gwt.query.client.Widgets.*;
import static com.google.gwt.query.client.$.$;
import com.google.gwt.user.client.Event;

/**
 * Copyright 2007 Timepedia.org Licensed under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 * @author Ray Cromwell <ray@timepedia.log>
 */
public class GwtQuerySampleModule implements EntryPoint {
//    public interface Sample extends Selectors {
//      @Selector(".note")
//      GQuery allNotes();

  //    }

  public void onModuleLoad() {
    $("div").hover(
        $.lazy().                   
          css("color", "red").
        done(),                                
        $.lazy().
          css("color", "blue").
        done());
    
    $(".note").dblclick(
        $.lazy().
            fadeOut().
        done());
    $("div").wrapAll("<table border=2><tr><td></td></tr></table>").fadeOut(10000);
  }


}
