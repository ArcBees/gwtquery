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
import static com.google.gwt.query.client.plugins.Effects.Effects;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.css.CSS;
import com.google.gwt.query.client.css.Length;

public class HomePageSample implements EntryPoint {


  public void onModuleLoad() {
    //Hide the text and set the width and append an h1 element
    $("#text").hide().css(CSS.WIDTH.with(Length.px(400))).prepend("<h1>GwtQuery Rocks !</h1>");

    //add a click handler on the button
    $("button").click(new Function(){

      public void f() {
        //display the text with effects and animate its background color
        $("#text").as(Effects)
            .clipDown(1000)
            .animate("backgroundColor: 'yellow'", 1000)
            .delay(1000)
            .animate("backgroundColor: '#fff'", 1500);
      }

    });
  }

}
