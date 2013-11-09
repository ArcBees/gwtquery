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

  private static final String[] attrsToSave = new String[]{
      "position", "overflow", "visibility", "white-space", "top", "left", "width", "height"};

  Action action;
  Corner corner;
  Direction direction;
  int percent;
  private GQuery back = Effects.$();
  private Function[] funcs;
  private Effects g;

  public ClipAnimation(Element elem, Action a, Corner c, Direction d, Easing easing,  Properties p, final Function... funcs) {
    super(easing, elem, p, funcs);
    if (a == Action.TOGGLE) {
      a = GQuery.$(elem).isVisible() ? Action.HIDE : Action.SHOW;
    }
    this.action = a;
    this.corner = c;
    this.direction = d;
    this.funcs = funcs;
    e = elem;
    g = GQuery.$(e).as(Effects.Effects);
  }

  public ClipAnimation(Element elem, Action a, Corner c, Direction d, final Function... funcs) {
    this(elem, a, c, d, null, Properties.create(), funcs);
  }

  @Override
  public void onCancel() {
    Boolean jumpToEnd = Effects.$(e).data(Effects.JUMP_TO_END, Boolean.class);
    if (jumpToEnd != null && jumpToEnd){
      onComplete();
    } else {
      g.dequeue();
    }
  }

  @Override
  public void onComplete() {
    super.onComplete();
    if (action == Action.HIDE) {
      g.hide();
    }
    g.restoreCssAttrs(attrsToSave);
    back.remove();
    back = Effects.$();
    g.css("clip", "");
    g.each(funcs);
    g.dequeue();
  }

  @Override
  public void onStart() {
    super.onStart();
    g.show();
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
  }

  @Override
  public void onUpdate(double progress) {
    super.onUpdate(progress);

    if (action == Action.HIDE) {
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

    String rect = top + "px " + right + "px " + bottom + "px  " + left + "px";
    g.css("clip", "rect(" + rect + ")");
  }
}
