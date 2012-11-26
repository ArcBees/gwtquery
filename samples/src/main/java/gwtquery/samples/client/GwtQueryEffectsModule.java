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
import static com.google.gwt.query.client.GQuery.$$;
import static com.google.gwt.query.client.GQuery.lazy;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.css.CSS;
import com.google.gwt.query.client.css.Length;
import com.google.gwt.query.client.css.RGBColor;
import com.google.gwt.query.client.plugins.Effects;
import com.google.gwt.query.client.plugins.effects.PropertiesAnimation.Easing;
import com.google.gwt.user.client.Event;

public class GwtQueryEffectsModule implements EntryPoint {

  public void onModuleLoad() {
    $("div > div").css(CSS.COLOR.with(RGBColor.BLUE))
    .hover(lazy().css(CSS.COLOR.with(RGBColor.RED)).done(),
           lazy().css(CSS.COLOR.with(RGBColor.BLUE)).done());

    $("div.outer > div").css(CSS.POSITION.with(Position.RELATIVE)).dblclick(new Function() {
      public boolean f(Event e) {
         $("div.outer > div").as(Effects.Effects).
         animate($$("left: '+=100'"), 400, Easing.LINEAR).
         animate($$("top: '+=100'"), 400, Easing.LINEAR).
         animate($$("left: '-=100'"), 400, Easing.LINEAR).
         animate($$("top: '-=100'"), 400, Easing.LINEAR);
        return true;
      }
    });
    $(".note").click(lazy().fadeOut().done());
    $(".note").append(" Hello");

    final Effects a = $(".a, .b > div:nth-child(2)").as(Effects.Effects);
    final Effects b = $(".b > div:nth-child(odd)").as(Effects.Effects);

    $("#b0").width(150).css(CSS.FONT_SIZE.with(Length.px(10))).toggle(new Function() {
      public void f(Element e) {
        $("#b0").as(Effects.Effects).animate(" width: '400', opacity: '0.4', marginLeft: '0.6in', fontSize: '24px'");
      }
    }, new Function() {
      public void f(Element e) {
        $("#b0").as(Effects.Effects).animate(" width: '150', opacity: '1', marginLeft: '0', fontSize: '10px'");
      }
    });

    $("#b1").toggle(new Function() {
      public void f(Element e) {
        $(".a").toggle();
      }
    }, new Function() {
      public void f(Element e) {
        a.fadeOut();
      }
    }, new Function() {
      public void f(Element e) {
        a.fadeIn();
      }
    }, new Function() {
      public void f(Element e) {
        a.slideUp();
      }
    }, new Function() {
      public void f(Element e) {
        a.slideDown();
      }
    }, new Function() {
      public void f(Element e) {
        a.slideLeft();
      }
    }, new Function() {
      public void f(Element e) {
        a.slideRight();
      }
    }, new Function() {
      public void f(Element e) {
        a.animate("left: '+=300', width: 'hide'");
      }
    }, new Function() {
      public void f(Element e) {
        a.animate("left: '-=300', width: 'show'");
      }
    });

    $("#b2").toggle(new Function() {
      public void f(Element e) {
        b.as(Effects.Effects).clipUp();
      }
    }, new Function() {
      public void f(Element e) {
        b.as(Effects.Effects).clipDown();
      }
    }, new Function() {
      public void f(Element e) {
        b.as(Effects.Effects).clipDisappear();
      }
    }, new Function() {
      public void f(Element e) {
        b.as(Effects.Effects).clipAppear();
      }
    });

  }
}
