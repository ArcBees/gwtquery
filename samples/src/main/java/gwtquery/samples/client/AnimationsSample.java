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
import static com.google.gwt.query.client.GQuery.lazy;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Element;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.Properties;
import com.google.gwt.query.client.css.CSS;
import com.google.gwt.query.client.css.Length;
import com.google.gwt.query.client.css.RGBColor;
import com.google.gwt.query.client.plugins.effects.PropertiesAnimation.Easing;

public class AnimationsSample implements EntryPoint {


  public void onModuleLoad() {
    doMoveAnimation();
    doColorAnimation();

    $("#stopMove").click(new Function(){
      public void f() {
        $(".foo").clearQueue().stop();

      }
    });

    $("#stopColor").click(new Function(){
      public void f() {
        $(".foo").clearQueue("colorQueue");
      }
    });

    $("#startMove").click(new Function(){
      public void f() {
        $(".foo").css(CSS.LEFT.with(Length.px(0)));
        doMoveAnimation();
      }
    });

    $("#startColor").click(new Function(){
      public void f() {
        doColorAnimation();
      }
    });

  }

  private void doColorAnimation(){
    $(".foo")
      .queue("colorQueue", lazy().css(CSS.BACKGROUND_COLOR.with(RGBColor.RED)).dequeue("colorQueue").done())
      .delay(500, "colorQueue")
      .queue("colorQueue", lazy().css(CSS.BACKGROUND_COLOR.with(RGBColor.BLACK)).dequeue("colorQueue").done())
      .delay(500, "colorQueue")
      .queue("colorQueue", new Function() {
            @Override
            public void f() {
              doColorAnimation();
              $(".foo").dequeue("colorQueue");
            }

            @Override
            public void cancel(Element e) {
              $(".foo").clearQueue().stop();
            }
        });

  }

  private void doMoveAnimation(){
    $(".foo").animate(Properties.create("{left:'+=1000'}"), 2000, Easing.SWING)
        .delay(500)
        .animate("left:'-=1000'", 2000)
        .queue(new Function() {
          @Override
          public void f() {
            doMoveAnimation();
            $(".foo").dequeue();
          }
        });

  }
}
