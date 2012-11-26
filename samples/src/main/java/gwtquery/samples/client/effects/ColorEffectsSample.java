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
package gwtquery.samples.client.effects;

import static com.google.gwt.query.client.GQuery.$;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.css.CSS;

public class ColorEffectsSample implements EntryPoint {

  public void onModuleLoad() {

    $("#shoot").click(new Function() {

      public void f() {
        $("body").animate("backgroundColor: 'red'", 400)
          .delay(1000)
          .animate("backgroundColor: 'white'", 2000);
      }

    });

    $("#startAnim2").click(new Function(){

      public void f() {
        $(".bar").animate("backgroundColor: 'yellow'", 1000)
          .delay(200)
          .animate("borderColor: '#ff0000'",1000)
          .delay(200)
          .animate("color:'rgb(255, 255, 255)'", 1000);
      }

    });

    $("#resetAnim2").click(new Function(){

      public void f() {
        $(".bar").css(CSS.BACKGROUND_COLOR.with(null), CSS.BORDER_COLOR.with(null), CSS.COLOR.with(null));
      }

    });
  }

}
