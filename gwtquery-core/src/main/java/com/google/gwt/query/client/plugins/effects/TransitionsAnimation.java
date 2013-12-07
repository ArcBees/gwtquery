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

import static com.google.gwt.query.client.GQuery.$;
import static com.google.gwt.query.client.GQuery.$$;
import static com.google.gwt.query.client.plugins.effects.ClipAnimation.getNormalizedValue;

import com.google.gwt.dom.client.Element;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.Properties;
import com.google.gwt.query.client.js.JsObjectArray;
import com.google.gwt.query.client.plugins.effects.ClipAnimation.Action;
import com.google.gwt.query.client.plugins.effects.ClipAnimation.Corner;
import com.google.gwt.query.client.plugins.effects.ClipAnimation.Direction;
import com.google.gwt.query.client.plugins.effects.Fx.TransitFx;
import com.google.gwt.regexp.shared.MatchResult;

/**
 * Animation effects on any numeric CSS3 property or transformation
 * using CSS3 transitions
 */
public class TransitionsAnimation extends PropertiesAnimation {

  public static class TransitionsClipAnimation extends TransitionsAnimation {

    private Action action;
    private Corner corner;
    private Direction direction;
    private Action currentAction;

    public TransitionsClipAnimation(Element elem, Properties p, Function... funcs) {
      this(null, elem, p, funcs);
    }

    public TransitionsClipAnimation(Easing easing, Element elem, Properties p, Function... funcs) {
      super(easing, elem, p, funcs);
      corner = Corner.CENTER;
      try {
        corner = Corner.valueOf(getNormalizedValue("clip-origin", p));
      } catch (Exception e) {
      }
      direction = Direction.BIDIRECTIONAL;
      try {
        direction = Direction.valueOf(getNormalizedValue("clip-direction", p));
      } catch (Exception e) {
      }
      try {
        action = Action.valueOf(getNormalizedValue("clip-action", p));
      } catch (Exception e) {
      }
    }

    public TransitionsClipAnimation(Element elem, Action a, Corner c, Direction d, Easing easing,
        Properties p, final Function... funcs) {
      super(easing, elem, p, funcs);
      this.action = a;
      this.corner = c;
      this.direction = d;
    }

    public void onStart() {
      boolean hidden = !g.isVisible();

      super.onStart();
      if (action == null) {
        return;
      }
      currentAction = action != Action.TOGGLE ? action :  hidden ? Action.SHOW : Action.HIDE;
      int bit = currentAction == Action.HIDE ? 1 : 0;

      String originX = "left", originY = "top";
      int scaleXini = 0^bit, scaleYini = scaleXini;
      int scaleXend = 1^bit, scaleYend = scaleXend;

      if (direction == Direction.VERTICAL) {
        scaleXini = scaleXend = 1;
      }
      if (direction == Direction.HORIZONTAL) {
        scaleYini = scaleYend = 1;
      }
      if (corner == Corner.CENTER) {
        originX = originY = "center";
      }
      if (corner == Corner.TOP_RIGHT || corner == Corner.BOTTOM_RIGHT) {
        originX = "right";
      }
      if (corner == Corner.BOTTOM_LEFT || corner == Corner.BOTTOM_RIGHT) {
        originY = "bottom";
      }

      g.show().css("transformOrigin", originX + " " + originY);

      effects.add(new TransitFx("scale", "", scaleXini + " " + scaleYini, scaleXend + " " + scaleYend, ""));
    }

    @Override
    public void onComplete() {
      super.onComplete();
      if (action == null) {
        return;
      }
      if (currentAction == Action.HIDE) {
        g.hide();
      }
      g.css("transformOrigin", "");
      g.css("transform", "scale(1 1)");
    }
  }

  public static Fx computeFxProp(Element e, String key, String val, boolean hidden) {
    Transitions g = $(e).as(Transitions.Transitions);
    String unit = "";
    if ("toggle".equals(val)) {
      val = hidden ? "show" : "hide";
    }

    if ("show".equals(val) && !hidden || "hide".equals(val) && hidden) {
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
      g.saveCssAttrs(key);
      trsEnd = "0";
    } else {
      MatchResult parts = REGEX_SYMBOL_NUMBER_UNIT.exec(val);
      if (parts != null) {
        unit = REGEX_NON_PIXEL_ATTRS.test(key) || Transitions.transformRegex.test(key) ? "" : "px";

        String part1 = parts.getGroup(1);
        String part2 = parts.getGroup(2);
        String part3 = parts.getGroup(3);
        trsEnd = "" + Double.parseDouble(part2);

        if (unit.isEmpty() && part3 != null) {
          unit = part3;
        }
        if (trsStart.isEmpty()) {
          trsStart = key.matches("scale") ? "1" : "0";
        }

        if (part1 != null && !part1.isEmpty()) {
          double n = "-=".equals(part1) ? -1 : 1;
          double st = Double.parseDouble(trsStart);
          double en = Double.parseDouble(trsEnd);
          trsEnd = "" + (st + (n*en));
        }
      } else {
        trsStart = "";
        trsEnd = val;
      }
    }
    return new TransitFx(key, val, trsStart, trsEnd, unit);
  }

  protected Transitions g;

  public TransitionsAnimation(Element elem, Properties p, Function... funcs) {
    this(null, elem, p, funcs);
  }

  public TransitionsAnimation(Easing easing, Element elem, Properties p, Function... funcs) {
    super(easing, elem, p, funcs);
    g = $(e).as(Transitions.Transitions);
  }

  private Properties getFxProperties(boolean isStart) {
    Properties p = $$();
    for (int i = 0; i < effects.length(); i++) {
      TransitFx fx = (TransitFx)effects.get(i);
      String val = isStart ? fx.transitStart : fx.transitEnd;
      if (!val.isEmpty()) {
        p.set(fx.cssprop, val + fx.unit);
      }
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
  public void onUpdate(double progress) {
  }

  @Override
  public void run(int duration) {
    onStart();
    Properties p = getFxProperties(true);
    g.css(p);
    // TODO: Reflow, it seems it is not needed in chrome and FF, check other browsers
    // g.css("offsetHeight");
    p = getFxProperties(false);
    g.transition(p, duration, easing, 0, new Function(){public void f() {
      onComplete();
    }});
  }
}
