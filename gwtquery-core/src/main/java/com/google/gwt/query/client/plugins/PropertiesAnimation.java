/*
 * Copyright 2009 Google Inc.
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
package com.google.gwt.query.client.plugins;

import java.util.ArrayList;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.dom.client.Element;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.JSArray;
import com.google.gwt.query.client.Properties;
import com.google.gwt.query.client.Regexp;

/**
 *  Animation effects on any numeric CSS property. 
 */
public class PropertiesAnimation extends Animation {

  /**
   * Easing method to use.
   */
  public enum Easing {
    LINEAR, SWING
  }
  
  private static class Effect {
    private static Regexp nonPx = new Regexp(
        "z-?index|font-?weight|opacity|zoom|line-?height", "i");

    public String attr;
    public double end;
    public double start;
    public String unit;
    public String value;

    Effect(String attr, String value, double start, double end,
        String unit) {
      this.attr = attr;
      this.value = value;
      this.start = start;
      this.end = end;
      this.unit = nonPx.test(attr) ? "" : unit == null ? "px" : unit;
    }

    public String getVal(double progress) {
      double ret = (start + ((end - start) * progress));
      return ("px".equals(unit) ? ((int) ret) : ret) + unit;
    }
  }

  private static final String[] attrsToSave = new String[] { "overflow",
      "visibility", "white-space" };
  private Element e;
  private Easing easing = Easing.SWING;
  private ArrayList<Effect> effects = new ArrayList<Effect>();
  private Function[] funcs;
  private Effects g;

  private Properties prps;

  public PropertiesAnimation(Easing easing, Element elem, Properties p,
      Function... funcs) {
    this.easing = easing;
    this.e = elem;
    this.funcs = funcs;
    this.prps = p;
    g = Effects.$(e).as(Effects.Effects);
  }

  @Override
  public void onCancel() {
    onComplete();
  }

  @Override
  public void onComplete() {
    super.onComplete();
    for (Effect l : effects) {
      if ("hide".equals(l.value)) {
        g.hide();
        g.restoreCssAttrs(l.attr);
      } else if ("show".equals(l.value)) {
        g.show();
        g.restoreCssAttrs(l.attr);
      }
    }
    g.restoreCssAttrs(attrsToSave);
    g.each(funcs);
    g.dequeue();
  }

  @Override
  public void onStart() {
    boolean hidden = !g.visible();
    boolean resize = false;
    g.show();
    for (String key : prps.keys()) {
      String val = prps.get(key);
      if ("toggle".equals(val)) {
        val = hidden ? "show" : "hide";
      }
      if (("top".equals(key) || "left".equals(key))
          && !"absolute".equalsIgnoreCase(g.css("position", true))) {
        g.css("position", "relative");
      }

      JSArray parts = new Regexp("^([+-]=)?([0-9+-.]+)(.*)?$").match(val);
      String unit = parts != null ? parts.getStr(3) : "";
      double start = GQuery.cur(e, key);
      double end = start;
      if (parts != null) {
        end = Double.parseDouble(parts.getStr(2));
        if (parts.getStr(1) != null) {
          end = (("-=".equals(parts.getStr(1)) ? -1 : 1) * end) + start;
        }
      } else if ("show".equals(val)) {
        if (!hidden) {
          return;
        }
        g.saveCssAttrs(key);
        start = 0;
      } else if ("hide".equals(val)) {
        if (hidden) {
          return;
        }
        g.saveCssAttrs(key);
        end = 0;
      }
      resize = resize || "height".equals(key) || "width".equals(key);
      effects.add(new Effect(key, val, start, end, unit));
    }
    g.saveCssAttrs(attrsToSave);
    if (resize) {
      g.css("overflow", "hidden");
    }
    g.css("visibility", "visible");
    g.css("white-space", "nowrap");
    super.onStart();
  }

  @Override
  public void onUpdate(double progress) {
    for (Effect fx : effects) {
      g.css(fx.attr, fx.getVal(progress));
    }
  }

  @Override
  protected double interpolate(double progress) {
    if (easing == Easing.SWING) {
      return super.interpolate(progress);
    } else {
      return progress;
    }
  }

}
