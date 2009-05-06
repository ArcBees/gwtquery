package gwtquery.samples.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.query.client.$;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.Effects;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.css.CSS;
import static com.google.gwt.query.client.GQuery.$;
import static com.google.gwt.query.client.GQuery.lazy;
import static com.google.gwt.query.client.css.CSS.*;
import static com.google.gwt.query.client.css.Length.*;
import static com.google.gwt.query.client.css.Percentage.*;
import static com.google.gwt.query.client.css.RGBColor.*;

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
    GQuery q = $(".note");
    q.setCss(CSS.BACKGROUND_COLOR, CSS.RED);
    q.setCss(TEXT_ALIGN, LEFT);
    q.setCss(VERTICAL_ALIGN, px(10));

    $("div > div").hover(lazy().
        css("color", "red").
        done(), lazy().
        css("color", "blue").
        done());

//    $(".note").dblclick(
//        lazy().as(Effects.Effects).
//            fadeOut().
//        done());
//     $("div.outer").dblclick(new Function() {
//       @Override
//       public boolean f(Event e, Object data) {
//         $(e.getCurrentTarget()).as(Effects.Effects).slideUp();
//         return true;
//       }
//     });
//    $("div").wrapAll("<table border=2><tr><td></td></tr></table>");
  }
}
