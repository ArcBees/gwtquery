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
import com.google.gwt.query.client.js.JsUtils;
import com.google.gwt.query.client.plugins.Effects;

/**
 * Animation which uses the css clip property to show/hide an element.
 */
public class ClipAnimation extends PropertiesAnimation {

  /**
   * Type of the effect action.
   */
  public static enum Action {
    HIDE, SHOW, TOGGLE
  }

  /**
   * Corner from which the effect starts.
   */
  public static enum Corner {
    BOTTOM_LEFT, BOTTOM_RIGHT, CENTER, TOP_LEFT, TOP_RIGHT
  }

  /**
   * Direction of the effect.
   */
  public static enum Direction {
    BIDIRECTIONAL, HORIZONTAL, VERTICAL
  }

  private static final String[] attrsToSave = new String[] {
      "position", "overflow", "visibility", "white-space", "top", "left", "width", "height"};

  private Action action;
  private Corner corner;
  private Direction direction;
  private GQuery back = Effects.$();
  private Effects g;
  private Action currentAction;

  public ClipAnimation(Element elem, Properties p, Function... funcs) {
    this(null, elem, p, funcs);
  }

  public ClipAnimation(Easing easing, Element elem, Properties p, Function... funcs) {
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
    g = GQuery.$(e).as(Effects.Effects);
  }

  public static String getNormalizedValue(String value, Properties p) {
    return JsUtils.hyphenize(p.getStr(value)).replace("-", "_").toUpperCase();
  }

  public ClipAnimation(Element elem, Action a, Corner c, Direction d, Easing easing,
      Properties p, final Function... funcs) {
    super(easing, elem, p, funcs);
    this.action = a;
    this.corner = c;
    this.direction = d;
    g = GQuery.$(e).as(Effects.Effects);
  }

  public ClipAnimation(Element elem, Action a, Corner c, Direction d, final Function... funcs) {
    this(elem, a, c, d, null, Properties.create(), funcs);
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
    g.restoreCssAttrs(attrsToSave);
    back.remove();
    back = Effects.$();
    g.css("clip", "");
  }

  @Override
  public void onStart() {
    boolean hidden = !g.isVisible();
    super.onStart();
    if (action == null) {
      return;
    }
    currentAction = action != Action.TOGGLE ? action : hidden ? Action.SHOW : Action.HIDE;

    g.saveCssAttrs(attrsToSave);

    // CSS clip only works with absolute/fixed positioning
    if (!g.css("position", true).matches("absolute|fixed")) {
      // Add a temporary element to replace the original one, so nothing is moved when
      // setting the absolute  position to our element
      back = back.add(g.before("<div></div>")).prev();
      back.height(g.height());
      back.width(g.width());
      // change the position property, but keeping its original position and sizes
      g.css("top", g.offset().top + "px");
      g.css("left", g.offset().left + "px");
      g.css("width", g.width() + "px");
      g.css("height", g.height() + "px");
      g.css("position", "absolute");
    }
    g.css("overflow", "hidden");
    g.css("visivility", "visible");

    // Set the initial clip viewport before showing the element
    onUpdate(0);
    g.show();
  }

  @Override
  public void onUpdate(double progress) {
    super.onUpdate(progress);
    if (action == null) {
      return;
    }
    if (currentAction == Action.HIDE) {
      progress = (1 - progress);
    }
    int w = g.outerWidth();
    int h = g.outerHeight();
    int top = 0;
    int left = 0;
    int right = w;
    int bottom = h;

    if (direction == Direction.VERTICAL || direction == Direction.BIDIRECTIONAL) {
      bottom = (int) (h * progress);
    }
    if (direction == Direction.HORIZONTAL || direction == Direction.BIDIRECTIONAL) {
      right = (int) (w * progress);
    }
    if (corner == Corner.CENTER) {
      top = (h - bottom) / 2;
      left = (w - right) / 2;
    }
    if (corner == Corner.TOP_RIGHT || corner == Corner.BOTTOM_RIGHT) {
      left = (w - right);
      right = w;
    }
    if (corner == Corner.BOTTOM_LEFT || corner == Corner.BOTTOM_RIGHT) {
      top = (h - bottom);
      bottom = h;
    }

    g.css("clip", "rect(" + top + "px " + right + "px " + bottom + "px  " + left + "px)");
  }
}
