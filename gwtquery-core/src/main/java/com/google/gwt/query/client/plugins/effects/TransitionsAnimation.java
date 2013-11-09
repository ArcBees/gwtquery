/*
 * Copyright 2013, The gwtquery team.
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
package com.google.gwt.query.client.plugins.effects;

import static com.google.gwt.query.client.GQuery.*;

import com.google.gwt.dom.client.Element;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.Properties;
import com.google.gwt.query.client.js.JsObjectArray;
import com.google.gwt.query.client.plugins.effects.Fx.TransitFx;
import com.google.gwt.regexp.shared.MatchResult;

/**
 * Animation effects on any numeric CSS3 property or transformation
 * using CSS3 transitions
 */
public class TransitionsAnimation extends PropertiesAnimation {
  
  public static Fx computeFxProp(Element e, String key, String val, boolean hidden) {
    Transitions g = $(e).as(Transitions.Transitions);
    String unit = "";
    if ("toggle".equals(val)) {
      val = hidden ? "show" : "hide";
    }

    if (("show".equals(val) && !hidden) || ("hide").equals(val) && hidden) {
      return null;
    }

    if (hidden) {
      g.show();
    }

    String cur = g.css(key, true);
    String trsStart = cur, trsEnd = trsStart;
    
    if ("show".equals(val)) {
      g.saveCssAttrs(key);
      trsStart = "0";
    } else if ("hide".equals(val)) {
      if (hidden) {
        return null;
      }
      g.saveCssAttrs(key);
      trsEnd = "0";
    } else {
      MatchResult parts = REGEX_SYMBOL_NUMBER_UNIT.exec(val);
      if (parts != null) {
        unit = REGEX_NON_PIXEL_ATTRS.test(key) || Transitions.transformRegex.test(key) ? "" : "px";
        
        String $1 = parts.getGroup(1);
        String $2 = parts.getGroup(2);
        String $3 = parts.getGroup(3);
        trsEnd = "" + Double.parseDouble($2);
        
        if (unit.isEmpty() && $3 != null) {
          unit = $3;
        }
        if (trsStart.isEmpty()) {
          trsStart = "0";
        }
        
        if (!trsStart.endsWith(unit)) {
          trsStart += unit;
        }

        if ($1 != null && !$1.isEmpty()) {
          double n = "-=".equals($1) ? -1 : 1;
          double st = Double.parseDouble(trsStart);
          double en = Double.parseDouble(trsEnd);
          trsEnd = "" + (st + (n*en));
        }
      } else {
        trsStart = trsEnd = val;
      }
    }
    return new TransitFx(key, val, trsStart, trsEnd, unit);
  }

  private Transitions g;

  public TransitionsAnimation(Easing easing, Element elem, Properties p, Function... funcs) {
    super(easing, elem, p, funcs);
    g = $(e).as(Transitions.Transitions);
  }

  private Properties getFxProperties(boolean isStart) {
    Properties p = $$();
    for (int i = 0; i < effects.length(); i++) {
      TransitFx fx = (TransitFx)effects.get(i);
      p.set(fx.cssprop, (isStart ? fx.transitStart : fx.transitEnd) + fx.unit);
    }
    return p;
  }

  @Override
  public void onStart() {
    effects = JsObjectArray.create();
    boolean resize = false;
    boolean move = false;
    boolean hidden = !g.isVisible();
    Fx fx;
    for (String key : prps.keys()) {
      String val = prps.getStr(key);
      if ((fx = computeFxProp(e, key, val, hidden)) != null) {
        effects.add(fx);
        resize = resize || "height".equals(key) || "width".equals(key);
        move = move || "top".equals(key) || "left".equals(key);
      }
      System.out.println(fx);
    }
    g.saveCssAttrs(ATTRS_TO_SAVE);
    if (resize) {
      g.css("overflow", "hidden");
    }
    if (move && !g.css("position", true).matches("absolute|relative|fixed")) {
      g.css("position", "relative");
    }
  }

  @Override
  public void run(int duration) {
    onStart();
    Properties p = getFxProperties(true);
    System.out.println(p.toJsonString());
    g.css(p);
    p = getFxProperties(false);
    System.out.println(p.toJsonString());
    g.transition(p, duration - 150, easing, 0, new Function(){public void f() {
      onComplete();
    }});
  }
}
