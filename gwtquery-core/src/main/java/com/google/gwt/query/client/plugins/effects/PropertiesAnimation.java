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

import com.google.gwt.dom.client.Element;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.Properties;
import com.google.gwt.query.client.js.JsObjectArray;
import com.google.gwt.query.client.js.JsRegexp;
import com.google.gwt.query.client.plugins.Effects;
import com.google.gwt.query.client.plugins.Effects.GQAnimation;
import com.google.gwt.query.client.plugins.effects.Fx.ColorFx;
import com.google.gwt.query.client.plugins.effects.Fx.ColorFx.BorderColorFx;

/**
 * Animation effects on any numeric CSS property.
 */
public class PropertiesAnimation extends GQAnimation {

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

  private static final String[] ATTRS_TO_SAVE = new String[]{
      "overflow"};

  private static final JsRegexp REGEX_NUMBER_UNIT = new JsRegexp(
      "^([0-9+-.]+)(.*)?$");

  private static final JsRegexp REGEX_SYMBOL_NUMBER_UNIT = new JsRegexp(
      "^([+-]=)?([0-9+-.]+)(.*)?$");

  private static final JsRegexp REGEX_NON_PIXEL_ATTRS = new JsRegexp(
      "z-?index|font-?weight|opacity|zoom|line-?height|^\\$", "i");

  private static final JsRegexp REGEX_COLOR_ATTR = new JsRegexp(".*color$", "i");

  private static final JsRegexp REGEX_BORDERCOLOR = new JsRegexp("^bordercolor$", "i");

  private static final JsRegexp REGEX_BACKGROUNDCOLOR = new JsRegexp("^backgroundcolor$", "i");
  
  

  public static Fx computeFxProp(Element e, String key, String val,
      boolean hidden) {

    if (REGEX_COLOR_ATTR.test(key)) {
      return computeFxColorProp(e, key, val);
    }

    return computeFxNumericProp(e, key, val, hidden);
  }

  private static Fx computeFxColorProp(Element e, String key, String val) {

    if (REGEX_BORDERCOLOR.test(key)) {
      return new BorderColorFx(e, val);
    }

    String initialColor = null;
    if (REGEX_BACKGROUNDCOLOR.test(key)) {
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

    return new ColorFx(key, initialColor, val);
  }

  public static Fx computeFxNumericProp(Element e, String key, String val,
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

    // If key starts with $ we animate node attributes, otherwise css properties
    double cur;
    String rkey = null;
    if (key.startsWith("$")) {
      rkey = key.substring(1).toLowerCase();
      String attr = g.attr(rkey);
      JsObjectArray<String> parts = REGEX_NUMBER_UNIT.match(attr);
      if (parts != null) {
        String $1 = parts.get(1);
        String $2 = parts.get(2);
        cur = Double.parseDouble($1);
        unit = $2 == null ? "" : $2;
      } else {
        cur = g.cur(key, true);
        key = rkey;
      }
    } else {
      cur = g.cur(key, true);
    }

    double start = cur, end = start;

    if ("show".equals(val)) {
      g.saveCssAttrs(key);
      start = 0;
      unit = REGEX_NON_PIXEL_ATTRS.test(key) ? "" : "px";
    } else if ("hide".equals(val)) {
      if (hidden) {
        return null;
      }
      g.saveCssAttrs(key);
      end = 0;
      unit = REGEX_NON_PIXEL_ATTRS.test(key) ? "" : "px";
    } else {
      JsObjectArray<String> parts = REGEX_SYMBOL_NUMBER_UNIT.match(val);

      if (parts != null) {
        String $1 = parts.get(1);
        String $2 = parts.get(2);
        String $3 = parts.get(3);
        end = Double.parseDouble($2);
        
        if (rkey == null) {
          unit = REGEX_NON_PIXEL_ATTRS.test(key) ? "" : //
            $3 == null || $3.isEmpty() ? "px" : $3;
          if (!"px".equals(unit)) {
            double to = end == 0 ? 1 : end;
            g.css(key, to + unit);
            start = to * start / g.cur(key, true);
            g.css(key, start + unit);
          }
        } else if ($3 != null && !$3.isEmpty()) {
          unit = $3;
        }

        if ($1 != null && !$1.isEmpty()) {
          end = (("-=".equals($1) ? -1 : 1) * end) + start;
        }
      }
    }
    
    return new Fx(key, val, start, end, unit, rkey);
  }

  private Element e;
  private Easing easing = Easing.SWING;
  private JsObjectArray<Fx> effects = JsObjectArray.create();
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
    Boolean jumpToEnd = Effects.$(e).data(Effects.JUMP_TO_END, Boolean.class);
    if (jumpToEnd != null && jumpToEnd){
      onComplete();
    } else {
      g.dequeue();
      g.restoreCssAttrs(ATTRS_TO_SAVE);
    }
  }

  @Override
  public void onComplete() {
    super.onComplete();
    for (int i = 0; i < effects.length(); i++) {
      Fx fx = effects.get(i);
      if ("hide".equals(fx.value)) {
        g.hide();
        g.restoreCssAttrs(fx.cssprop);
      } else if ("show".equals(fx.value)) {
        g.show();
        g.restoreCssAttrs(fx.cssprop);
      }
    }
    g.restoreCssAttrs(ATTRS_TO_SAVE);
    g.each(funcs);
    g.dequeue();
  }

  @Override
  public void onStart() {
    boolean resize = false;
    boolean move = false;
    boolean hidden = !g.isVisible();
    Fx fx;
    // g.show();
    for (String key : prps.keys()) {
      String val = prps.getStr(key);
      if ((fx = computeFxProp(e, key, val, hidden)) != null) {
        effects.add(fx);
        resize = resize || "height".equals(key) || "width".equals(key);
        move = move || "top".equals(key) || "left".equals(key);
      }
    }
    g.saveCssAttrs(ATTRS_TO_SAVE);
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
    for (int i = 0; i < effects.length(); i++) {
      effects.get(i).applyValue(g, progress);
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
