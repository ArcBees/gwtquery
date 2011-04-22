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
package com.google.gwt.query.client.plugins.effects;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.dom.client.Element;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.Properties;
import com.google.gwt.query.client.js.JsObjectArray;
import com.google.gwt.query.client.js.JsRegexp;
import com.google.gwt.query.client.plugins.Effects;
import com.google.gwt.query.client.plugins.effects.ColorEffect.BorderColorEffect;

import java.util.ArrayList;

/**
 * Animation effects on any numeric CSS property.
 */
public class PropertiesAnimation extends Animation {

  /**
   * Easing method to use.
   */
  public static interface Easing {
    public double interpolate(double progress);

    public Easing LINEAR = new Easing() {
      public double interpolate(double progress) {
        return progress;
      }
    };

    public Easing SWING = new Easing() {
      public double interpolate(double progress) {
        return (1 + Math.cos(Math.PI + progress * Math.PI)) / 2;
      }
    };
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

    Effect() {
      end = start = -1;
    }

    Effect(String attr, String value, double start, double end, String unit) {
      this.attr = attr;
      this.value = value;
      this.start = start;
      this.end = end;
      this.unit = unit;
    }

    public void applyValue(GQuery g, double progress) {

      double ret = (start + ((end - start) * progress));
      String value = ("px".equals(unit) ? ((int) ret) : ret) + unit;

      g.css(attr, value);
    }

    public String toString() {
      return ("attr=" + attr + " value=" + value + " start=" + start + " end="
          + end + " unit=" + unit).replaceAll("\\.0([^\\d])", "$1");
    }
  }

  private static final String[] attrsToSave = new String[]{
      "overflow", "visibility"};

  private static JsRegexp nonPxRegExp = new JsRegexp(
      "z-?index|font-?weight|opacity|zoom|line-?height", "i");

  private static JsRegexp colorRegExp = new JsRegexp(".*color$", "i");
  public static JsRegexp RGB_COLOR_PATTERN = new JsRegexp(
      "rgb\\(\\s*([0-9]{1,3}%?)\\s*,\\s*([0-9]{1,3}%?)\\s*,\\s*([0-9]{1,3}%?)\\s*\\)$");
  public static JsRegexp HEXADECIMAL_COLOR_PATTERN = new JsRegexp(
      "^#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$");

  public static Effect computeFxProp(Element e, String key, String val,
      boolean hidden) {

    if (colorRegExp.test(key)) {
      return computeFxColorProp(e, key, val);
    }

    return computeFxNumericProp(e, key, val, hidden);
  }

  private static Effect computeFxColorProp(Element e, String key, String val) {

    if ("BORDERCOLOR".equals(key.toUpperCase())) {
      return new BorderColorEffect(e, val);
    }

    String initialColor = null;

    if ("BACKGROUNDCOLOR".equals(key.toUpperCase())) {
      // find the first parent having a background-color value (other than
      // transparent)
      Element current = e;

      while ((initialColor == null || initialColor.length() == 0 || initialColor.equals("transparent"))
          && current != null) {
        initialColor = GQuery.$(current).css(key);
        current = !"body".equalsIgnoreCase(current.getTagName())
            ? current.getParentElement() : null;
      }
      if (initialColor == null || initialColor.length() == 0
          || initialColor.equals("transparent")) {
        initialColor = "white";
      }
    } else {
      initialColor = GQuery.$(e).css(key, true);
    }

    return new ColorEffect(key, initialColor, val);
  }

  public static Effect computeFxNumericProp(Element e, String key, String val,
      boolean hidden) {

    GQuery g = Effects.$(e);
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
      JsObjectArray<String> parts = new JsRegexp("^([+-]=)?([0-9+-.]+)(.*)?$").match(val);

      if (parts != null) {
        String $1 = parts.get(1);
        String $2 = parts.get(2);
        String $3 = parts.get(3);
        end = Double.parseDouble($2);
        unit = nonPxRegExp.test(key) ? "" : $3 == null || $3.isEmpty() ? "px"
            : $3;
        if (!"px".equals(unit)) {
          double to = end == 0 ? 1 : end;
          g.css(key, to + unit);
          start = to * start / g.cur(key, true);
          g.css(key, start + unit);
        }
        if ($1 != null && !$1.isEmpty()) {
          end = (("-=".equals($1) ? -1 : 1) * end) + start;
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
    // g.show();
    for (String key : prps.keys()) {
      String val = prps.getStr(key);
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
      fx.applyValue(g, progress);

    }
  }

  @Override
  protected double interpolate(double progress) {
    if (easing != null) {
      return easing.interpolate(progress);
    }
    // maybe return super.interpolate() instead ?
    return progress;
  }

}
