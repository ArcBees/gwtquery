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

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Element;
import com.google.gwt.query.client.Function;

import static com.google.gwt.query.client.plugins.Effects.Effects;
import static com.google.gwt.query.client.GQuery.$;

public class FadeEffectsSample implements EntryPoint {

  public void onModuleLoad() {

    // FadeIn sample
    $("#fadeIn div.foo").hide();
    $("#fadeIn > button").click(new Function() {
      @Override
      public void f(Element e) {
        $("#fadeIn div.foo").as(Effects).fadeIn(2000);
      }
    });

    $("#fadeIn > button.reset").click(new Function() {
      @Override
      public void f(Element e) {
        $("#fadeIn div.foo").hide();
      }
    });


    // FadeOut sample
    $("#fadeOut > button").click(new Function() {
      @Override
      public void f(Element e) {
        $("#fadeOut div.foo").as(Effects).fadeOut(2000);
      }
    });

    $("#fadeOut > button.reset").click(new Function() {
      @Override
      public void f(Element e) {
        $("#slideRight div.foo").hide();
      }
    });

 // FadeToogle sample
    $("#fadeToogle > button").click(new Function() {
      @Override
      public void f(Element e) {
        $("#fadeToogle div.foo").as(Effects).fadeToggle(2000);
      }
    });


//Toogle sample
  $("#toogle > button").click(new Function() {
    @Override
    public void f(Element e) {
      $("#toogle div.foo").as(Effects).toggle(2000);
    }
  });
  }

}
