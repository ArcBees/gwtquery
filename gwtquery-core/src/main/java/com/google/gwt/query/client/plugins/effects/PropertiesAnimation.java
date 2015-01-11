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
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.js.JsObjectArray;
import com.google.gwt.query.client.plugins.Effects;
import com.google.gwt.query.client.plugins.Effects.GQAnimation;
import com.google.gwt.query.client.plugins.effects.Fx.ColorFx;
import com.google.gwt.query.client.plugins.effects.Fx.ColorFx.BorderColorFx;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;

/**
 * Animation effects on any numeric CSS property.
 */
public class PropertiesAnimation extends GQAnimation {

  /**
   * Easing method to use.
   */
  public interface Easing {
    double interpolate(double progress);

    /**
     * @deprecated use EasingCurve.linear instead
     */
    Easing LINEAR = EasingCurve.linear;

    /**
     * @deprecated use EasingCurve.swing instead
     */
    Easing SWING = EasingCurve.swing;
  }

  /**
   * This is a collection of most popular curves used in web animations
   * implemented using the Bezier Curve instead of a different algorithm per
   * animation.
   *
   * The toString() method returns the string parameter which can be used
   * for CSS3 transition-timing-function properties, example:
   * <pre>

   transition-timing-function: ease;
   transition-timing-function: cubic-bezier(0, 0, 1, 1);

   * </pre>
   *
   * This enum can be used with customized transitions in this way:
   * <pre>

    $("#foo").animate($$("{top:'500px',left:'500px'}"), 400, EasingCurve.custom.with(.02,.01,.47,1));

   * </pre>
   *
   */
  public static enum EasingCurve implements Easing {
    linear(0, 0, 1, 1) {
      public double interpolate(double p) {
        return p;
      }

      public String toString() {
        return "linear";
      }
    },
    ease(0.25, 0.1, 0.25, 1) {
      public String toString() {
        return "ease";
      }
    },
    easeIn(0.42, 0, 1, 1) {
      public String toString() {
        return "ease-in";
      }
    },
    easeOut(0, 0, 0.58, 1) {
      public String toString() {
        return "ease-out";
      }
    },
    easeInOut(0.42, 0, 0.58, 1) {
      public String toString() {
        return "ease-in-out";
      }
    },
    snap(0, 1, .5, 1),
    swing(.02, .01, .47, 1),
    easeInCubic(.550, .055, .675, .190),
    easeOutCubic(.215, .61, .355, 1),
    easeInOutCubic(.645, .045, .355, 1),
    easeInCirc(.6, .04, .98, .335),
    easeOutCirc(.075, .82, .165, 1),
    easeInOutCirc(.785, .135, .15, .86),
    easeInExpo(.95, .05, .795, .035),
    easeOutExpo(.19, 1, .22, 1),
    easeInOutExpo(1, 0, 0, 1),
    easeInQuad(.55, .085, .68, .53),
    easeOutQuad(.25, .46, .45, .94),
    easeInOutQuad(.455, .03, .515, .955),
    easeInQuart(.895, .03, .685, .22),
    easeOutQuart(.165, .84, .44, 1),
    easeInOutQuart(.77, 0, .175, 1),
    easeInQuint(.755, .05, .855, .06),
    easeOutQuint(.23, 1, .32, 1),
    easeInOutQuint(.86, 0, .07, 1),
    easeInSine(.47, 0, .745, .715),
    easeOutSine(.39, .575, .565, 1),
    easeInOutSine(.445, .05, .55, .95),
    easeInBack(.6, -.28, .735, .045),
    easeOutBack(.175, .885, .32, 1.275),
    easeInOutBack(.68, -.55, .265, 1.55),
    custom(0, 0, 1, 1);

    private Bezier c = new Bezier(0, 0, 1, 1);

    EasingCurve(double x1, double y1, double x2, double y2) {
      with(x1, y1, x2, y2);
    }

    public Easing with(double x1, double y1, double x2, double y2) {
      c = new Bezier(x1, y1, x2, y2);
      return this;
    }

    public double interpolate(double progress) {
      return c.f(progress);
    }

    public String toString() {
      return "cubic-bezier(" + c + ")";
    }
  }

  protected static final String[] ATTRS_TO_SAVE = new String[] {"overflow"};

  protected static final String NUMBER = "[\\d+-.]+";
  protected static final String UNIT = "[a-z%]+";

  private static final RegExp REGEX_NUMBER_UNIT = RegExp.compile("^(" + NUMBER + ")(.*)?$");

  protected static final RegExp REGEX_SYMBOL_NUMBER_UNIT = RegExp.compile("^([+-]=)?(" + NUMBER + ")(" + UNIT + ")?$");

  protected static final RegExp REGEX_SCALE_ATTRS = RegExp.compile("scale|opacity");

  protected static final RegExp REGEX_NON_PIXEL_ATTRS = RegExp.compile("scale|opacity"
      + "|z-?index|font-?weight|zoom|line-?height|rotat|skew|perspect|^\\$", "i");

  private static final RegExp REGEX_COLOR_ATTR = RegExp.compile(".*color$", "i");

  private static final RegExp REGEX_BORDERCOLOR = RegExp.compile("^bordercolor$", "i");

  private static final RegExp REGEX_BACKGROUNDCOLOR = RegExp.compile("^backgroundcolor$", "i");

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

      while ((initialColor == null || initialColor.length() == 0 || initialColor
          .equals("transparent"))
          && current != null) {
        initialColor = GQuery.$(current).css(key, false);
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
      MatchResult parts = REGEX_NUMBER_UNIT.exec(attr);
      if (parts != null) {
        String p1 = parts.getGroup(1);
        String p2 = parts.getGroup(2);
        cur = Double.parseDouble(p1);
        unit = p2 == null ? "" : p2;
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
      g.saveCssAttrs(key);
      end = 0;
      unit = REGEX_NON_PIXEL_ATTRS.test(key) ? "" : "px";
    } else {
      MatchResult parts = REGEX_SYMBOL_NUMBER_UNIT.exec(val);

      if (parts != null) {
        String p1 = parts.getGroup(1);
        String p2 = parts.getGroup(2);
        String p3 = parts.getGroup(3);
        end = Double.parseDouble(p2);

        if (rkey == null) {
          unit = REGEX_NON_PIXEL_ATTRS.test(key) ? "" : //
              p3 == null || p3.isEmpty() ? "px" : p3;
          if (!"px".equals(unit)) {
            double to = end == 0 ? 1 : end;
            g.css(key, to + unit);
            start = to * start / g.cur(key, true);
            g.css(key, start + unit);
          }
        } else if (p3 != null && !p3.isEmpty()) {
          unit = p3;
        }

        if (p1 != null && !p1.isEmpty()) {
          end = (("-=".equals(p1) ? -1 : 1) * end) + start;
        }
      }
    }

    return new Fx(key, val, start, end, unit, rkey);
  }

  protected JsObjectArray<Fx> effects;

  @Override
  public void onCancel() {
    Boolean jumpToEnd = Effects.$(e).data(Effects.JUMP_TO_END, Boolean.class);
    if (jumpToEnd != null && jumpToEnd) {
      onComplete();
    } else {
      g.dequeue();
      g.restoreCssAttrs(ATTRS_TO_SAVE);
    }
  }

  @Override
  public void onComplete() {
    super.onComplete();
    for (int i = 0; effects != null && i < effects.length(); i++) {
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
    effects = JsObjectArray.create();
    boolean resize = false;
    boolean move = false;
    boolean hidden = !g.isVisible();
    Fx fx;
    // g.show();
    for (String key : prps.keys()) {
      String val = prps.getStr(key);
      if ((fx = getFx(e, key, val, hidden)) != null) {
        effects.add(fx);
        resize = resize || "height".equals(key) || "width".equals(key);
        move = move || "top".equals(key) || "left".equals(key);
      }
    }
    g.saveCssAttrs(ATTRS_TO_SAVE);
    if (resize) {
      g.css("overflow", "hidden");
    }
    if (move && !g.css("position", true).matches("absolute|relative|fixed")) {
      g.css("position", "relative");
    }
    super.onStart();
  }

  protected Fx getFx(Element e, String key, String val, boolean hidden) {
    return computeFxProp(e, key, val, hidden);
  }

  @Override
  public void onUpdate(double progress) {
    for (int i = 0; i < effects.length(); i++) {
      effects.get(i).applyValue(g, progress);
    }
  }

  @Override
  protected double interpolate(double progress) {
    return easing.interpolate(progress);
  }
}
