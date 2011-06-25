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
import com.google.gwt.query.client.plugins.Effects;

/**
 * Animation wich uses the css clip property to show/hide an element.
 */
public class ClipAnimation extends Animation {

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
      "position", "overflow", "visibility", "white-space", "top", "left"};

  Action action;
  Corner corner;
  Direction direction;
  Element e;
  int percent;
  private GQuery back = Effects.$();
  private Function[] funcs;
  private Effects g;

  public ClipAnimation(Element elem, Action a, Corner c, Direction d,
      final Function... funcs) {
    if (a == Action.TOGGLE) {
      a = GQuery.$(elem).visible() ? Action.HIDE : Action.SHOW;
    }
    this.action = a;
    this.corner = c;
    this.direction = d;
    this.funcs = funcs;
    e = elem;
    g = GQuery.$(e).as(Effects.Effects);
  }

  @Override
  public void onCancel() {
    Boolean jumpToEnd = Effects.$(e).data(Effects.STOP_DATA_KEY, Boolean.class);
    
    if (jumpToEnd != null && jumpToEnd) {
      onCompleteImpl();
    }
  }

  @Override
  public void onComplete() {
    onCompleteImpl();
    g.each(funcs);
    g.dequeue();
  }

  @Override
  public void onStart() {
    g.show();
    g.saveCssAttrs(attrsToSave);
    if (!"absolute".equalsIgnoreCase(g.css("position", true))) {
      g.css("position", "absolute");
      g.css("top", g.offset().top + "px");
      g.css("left", g.offset().left + "px");
      back = back.add(g.before("<div></div>")).prev();
      back.height(g.height());
      back.width(g.width());
    }
    g.css("overflow", "hidden");
    g.css("visivility", "visible");
    super.onStart();
  }

  @Override
  public void onUpdate(double progress) {
    if (action == Action.HIDE) {
      progress = (1 - progress);
    }
    int top = 0;
    int left = 0;
    int right = g.width();
    int bottom = g.height();

    if (direction == Direction.VERTICAL || direction == Direction.BIDIRECTIONAL) {
      bottom = (int) (g.height() * progress);
    }
    if (direction == Direction.HORIZONTAL
        || direction == Direction.BIDIRECTIONAL) {
      right = (int) (g.width() * progress);
    }
    if (corner == Corner.CENTER) {
      top = (g.height() - bottom) / 2;
      left = (g.width() - right) / 2;
    } else if (corner == Corner.BOTTOM_LEFT) {
      top = (g.height() - bottom);
    } else if (corner == Corner.TOP_RIGHT) {
      left = (g.width() - right);
    } else if (corner == Corner.BOTTOM_RIGHT) {
      left = (g.width() - right);
      top = (g.height() - bottom);
    }
    String rect = top + "px " + right + "px " + bottom + "px  " + left + "px";
    g.css("clip", "rect(" + rect + ")");
  }
  
  private void onCompleteImpl(){
    
    super.onComplete();
    if (action == Action.HIDE) {
      g.hide();
    }
    g.restoreCssAttrs(attrsToSave);
    back.remove();
    back = Effects.$();
    g.css("clip", "");
  }
}
