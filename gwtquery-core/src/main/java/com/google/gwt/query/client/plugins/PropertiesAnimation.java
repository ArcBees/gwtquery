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
package com.google.gwt.query.client.plugins;

import java.util.ArrayList;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.dom.client.Element;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.JSArray;
import com.google.gwt.query.client.Properties;
import com.google.gwt.query.client.JSRegexp;

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
  
  /**
   * A pojo to store effect values.
   */
  public static class Effect {

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
      this.unit = unit;
    }

    public String getVal(double progress) {
      double ret = (start + ((end - start) * progress));
      return ("px".equals(unit) ? ((int) ret) : ret) + unit;
    }
    
    public String toString() {
      return ("attr=" + attr + " value=" + value + " start=" + start + " end=" + end + " unit=" + unit).replaceAll("\\.0([^\\d])", "$1");
    }
  }
  
  private static final String[] attrsToSave = new String[] { "overflow",
      "visibility" };

  private static JSRegexp nonPxRegExp = new JSRegexp(
      "z-?index|font-?weight|opacity|zoom|line-?height", "i");
  
  
  public static Effect computeFxProp(Element e, String key, String val,
      boolean hidden) {
    GQuery g = Effects.$(e);
    String unit = "";
    if ("toggle".equals(val)) {
      val = hidden ? "show" : "hide";
    }
    
    if (("show".equals(val) && !hidden) || ("hide").equals(val) && hidden){
      return null;
    }
    
    if (hidden){
      g.show();
    }
    double start = g.cur(key, true), end = start;
    
    if ("show".equals(val)) {
      g.saveCssAttrs(key);
      start = 0;
      unit = nonPxRegExp.test(key) ? "" : "px";
    } else if ("hide".equals(val)) {
      if (hidden) {
        return null;
      }
      g.saveCssAttrs(key);
      end = 0;
      unit = nonPxRegExp.test(key) ? "" : "px";
    } else {
      JSArray parts = new JSRegexp("^([+-]=)?([0-9+-.]+)(.*)?$").match(val);

      if (parts != null) {
        unit = nonPxRegExp.test(key) ? "" : parts.getStr(3) == null ? "px"
            : parts.getStr(3);
        end = Double.parseDouble(parts.getStr(2));
        if (!"px".equals(unit)) {
          double to = end == 0 ? 1 : end;
          g.css(key, to + unit);
          start = to * start / g.cur(key, true);
          g.css(key, start + unit);
        }
        if (parts.getStr(1) != null) {
          end = (("-=".equals(parts.getStr(1)) ? -1 : 1) * end) + start;
        }
      }
    }
    
    Effect fx = new Effect(key, val, start, end, unit);
    return fx;
  }
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
    boolean resize = false;
    boolean move = false;
    boolean hidden = !g.visible();
    Effect fx;
    //g.show();
    for (String key : prps.keys()) {
      String val = prps.get(key);
      if ((fx = computeFxProp(e, key, val, hidden)) != null) {
        effects.add(fx);
        resize = resize || "height".equals(key) || "width".equals(key);
        move = move || "top".equals(key) || "left".equals(key);
      }
    }
    g.saveCssAttrs(attrsToSave);
    if (resize) {
      g.css("overflow", "hidden");
    }
    if (move && !g.css("position", true).matches("absolute|relative")) {
      g.css("position", "relative");    
    }
    g.css("visibility", "visible");
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
