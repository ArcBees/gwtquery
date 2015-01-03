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
import com.google.gwt.query.client.Properties;
import com.google.gwt.query.client.plugins.Effects.GQAnimation;
import com.google.gwt.query.client.plugins.effects.ClipAnimation.Action;
import com.google.gwt.query.client.plugins.effects.ClipAnimation.Corner;
import com.google.gwt.query.client.plugins.effects.ClipAnimation.Direction;
import com.google.gwt.query.client.plugins.effects.Fx.TransitFx;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.user.client.Timer;

import java.util.List;

/**
 * Animation effects on any numeric CSS3 property or transformation
 * using CSS3 transitions.
 */
public class TransitionsAnimation extends PropertiesAnimation {

  /**
   * TransitionAnimation with Clip capabilities.
   */
  public static class TransitionsClipAnimation extends TransitionsAnimation {

    private Action action;
    private Corner corner;
    private Direction direction;
    private Action currentAction;

    @Override
    public GQAnimation setProperties(Properties p) {
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
      return super.setProperties(p);
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
      int scaleXini = 0 ^ bit, scaleYini = scaleXini;
      int scaleXend = 1 ^ bit, scaleYend = scaleXend;

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
      g.css("transform", "");
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
    String trsStart = cur.matches("auto|initial") ? "" : cur, trsEnd = trsStart;

    if ("show".equals(val)) {
      g.saveCssAttrs(key);
      trsStart = "0";
    } else if ("hide".equals(val)) {
      g.saveCssAttrs(key);
      trsEnd = "0";
    } else {
      MatchResult parts = REGEX_SYMBOL_NUMBER_UNIT.exec(val);
      if (parts != null) {

        String part1 = parts.getGroup(1);
        String part2 = parts.getGroup(2);
        String part3 = parts.getGroup(3);
        trsEnd = "" + Double.parseDouble(part2);

        unit = REGEX_NON_PIXEL_ATTRS.test(key) ? "" :  part3 == null || part3.isEmpty() ? "px" : part3;

        if (trsStart.isEmpty()) {
          trsStart = key.matches("scale") ? "1" : "0";
        }

        if (part1 != null && !part1.isEmpty()) {
          double n = "-=".equals(part1) ? -1 : 1;

          double st = 0;
          MatchResult sparts = REGEX_SYMBOL_NUMBER_UNIT.exec(trsStart);
          if (sparts != null) {
            st = Double.parseDouble(sparts.getGroup(2));
            unit = sparts.getGroup(3) == null || sparts.getGroup(3).isEmpty() ? unit : sparts.getGroup(3);
          }
          trsStart = "" + st;

          double en = Double.parseDouble(trsEnd);
          trsEnd = "" + (st + n * en);
        }

        // Deal with non px units like "%"
        if (!unit.isEmpty() && !"px".equals(unit) && trsStart.matches("[-+]?[\\d.]+")) {
          double start = Double.parseDouble(trsStart);
          double to = Double.parseDouble(trsEnd);
          g.css(key, to + unit);
          start = to * start / g.cur(key, true);
          trsStart = "" + start;
          g.css(key, start + unit);
        }
      } else {
        trsStart = "";
        trsEnd = val;
      }
    }
    return new TransitFx(key, val, trsStart, trsEnd, unit);
  }

  protected Transitions g;
  protected int delay = 0;
  private String oldTransitionValue;

  @Override
  public GQAnimation setProperties(Properties p) {
    delay = p.getInt("delay");
    return super.setProperties(p);
  }

  @Override
  public GQAnimation setElement(Element elem) {
    g = $(elem).as(Transitions.Transitions);
    return super.setElement(elem);
  }

  public TransitionsAnimation setDelay(int delay) {
    this.delay = delay;
    return this;
  }

  public Properties getFxProperties(boolean isStart) {
    Properties p = $$();
    for (int i = 0; i < effects.length(); i++) {
      TransitFx fx = (TransitFx) effects.get(i);
      String val = isStart ? fx.transitStart : fx.transitEnd;
      if (!val.isEmpty()) {
        p.set(fx.cssprop, val + fx.unit);
      }
    }
    return p;
  }

  @Override
  protected Fx getFx(Element e, String key, String val, boolean hidden) {
    return Transitions.invalidTransitionNamesRegex.test(key) ? null : computeFxProp(e, key, val, hidden);
  }

  @Override
  public void onUpdate(double progress) {
  }

  @Override
  public void onComplete() {
    g.css(Transitions.transition, oldTransitionValue);
    super.onComplete();
  }

  public void run(int duration) {
    // Calculate all Fx values for this animation
    onStart();
    // Compute initial properties
    Properties p = getFxProperties(true);
    g.css(p);
    // Some browsers need re-flow after setting initial properties (FF 24.4.0).
    g.offset();

    // Compute final properties
    p = getFxProperties(false);

    // Save old transition value
    oldTransitionValue = g.css(Transitions.transition);

    // Set new transition value
    String newTransitionValue  = "";
    List<String> transProps = Transitions.filterTransitionPropertyNames(p);
    String attribs = duration + "ms" + " "  + easing + " " + delay + "ms";
    for (String s : transProps) {
      newTransitionValue += (newTransitionValue.isEmpty() ? "" : ", ") + s + " " + attribs;
    }
    g.css(Transitions.transition, newTransitionValue);

    // Set new css properties so as the element is animated
    g.css(p);

    // Wait until transition has finished to run finish animation and dequeue
    new Timer() {
      public void run() {
        onComplete();
      }
    }.schedule(delay + duration);
  }
}
