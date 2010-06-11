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

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.JSArray;
import com.google.gwt.query.client.Plugin;
import com.google.gwt.query.client.Properties;
import com.google.gwt.query.client.plugins.ClipAnimation.Action;
import com.google.gwt.query.client.plugins.ClipAnimation.Direction;
import com.google.gwt.query.client.plugins.PropertiesAnimation.Easing;

/**
 *  Effects plugin for Gwt Query. 
 */
public class Effects extends GQueryQueue  {
  
  public static int DEFAULT_SPEED = 400;

  public static Class<Effects> Effects = Effects.class;

  public static int FAST = 200;

  public static int SLOW = 600;
  
  static {
    GQuery.registerPlugin(Effects.class, new Plugin<Effects>() {
      public Effects init(GQuery gq) {
        return new Effects(gq);
      }
    });
  }

  public Effects(final Element element) {
    super(element);
  }

  public Effects(GQuery gq) {
    super(gq);
  }

  public Effects(final JSArray elements) {
    super(elements);
  }

  public Effects(final NodeList<Element> list) {
    super(list);
  }

  /**
   * The animate() method allows us to create animation effects on any numeric CSS property. 
   * The only required parameter is a map of CSS properties. 
   * This map is similar to the one that can be sent to the .css() method, 
   * except that the range of properties is more restrictive.
   * All animated properties should be numeric, non-numeric cannot be animated using basic functionality. 
   * (For example, width, height, or left can be animated but background-color cannot be.) 
   * Property values are treated as a number of pixels unless otherwise specified.
   *  The units em and % can be specified where applicable.
   *  
   *  In addition to numeric values, each property can take the strings 'show', 'hide', and 'toggle'. 
   *  These shortcuts allow for custom hiding and showing animations that take into account the display type of the element.
   *  Animated properties can also be relative. If a value is supplied with a leading += or -= 
   *  sequence of characters, then the target value is computed by adding or subtracting the given number 
   *  from the current value of the property.
   */  
  public Effects animate(final Properties p, final int duration,
      final Easing easing, final Function... funcs) {
    queue(new Function() {
      public void f(Element e) {
        new PropertiesAnimation(easing, e, p, funcs).run(duration);
      }
    });
    return this;
  }

  /**
   * The animate() method allows us to create animation effects on any numeric CSS property. 
   * The only required parameter is a map of CSS properties. 
   * This map is similar to the one that can be sent to the .css() method, 
   * except that the range of properties is more restrictive.
   * All animated properties should be numeric, non-numeric cannot be animated using basic functionality. 
   * (For example, width, height, or left can be animated but background-color cannot be.) 
   * Property values are treated as a number of pixels unless otherwise specified.
   *  The units em and % can be specified where applicable.
   *  
   *  In addition to numeric values, each property can take the strings 'show', 'hide', and 'toggle'. 
   *  These shortcuts allow for custom hiding and showing animations that take into account the display type of the element.
   *  Animated properties can also be relative. If a value is supplied with a leading += or -= 
   *  sequence of characters, then the target value is computed by adding or subtracting the given number 
   *  from the current value of the property.
   */
  public Effects animate(String prop, Function... funcs) {
    return animate(prop, 500, funcs);
  }

  /**
   * The animate() method allows us to create animation effects on any numeric CSS property. 
   * The only required parameter is a map of CSS properties. 
   * This map is similar to the one that can be sent to the .css() method, 
   * except that the range of properties is more restrictive.
   * All animated properties should be numeric, non-numeric cannot be animated using basic functionality. 
   * (For example, width, height, or left can be animated but background-color cannot be.) 
   * Property values are treated as a number of pixels unless otherwise specified.
   *  The units em and % can be specified where applicable.
   *  
   *  In addition to numeric values, each property can take the strings 'show', 'hide', and 'toggle'. 
   *  These shortcuts allow for custom hiding and showing animations that take into account the display type of the element.
   *  Animated properties can also be relative. If a value is supplied with a leading += or -= 
   *  sequence of characters, then the target value is computed by adding or subtracting the given number 
   *  from the current value of the property.
   */  
  public Effects animate(String prop, int duration, Function... funcs) {
    return animate($$(prop), duration, Easing.SWING, funcs);
  }

  /**
   * Animate the set of matched elements using the clip property.
   * It is possible to show or hide a set of elements, 
   * specify the direction of the animation and the start corner of the effect.
   * Finally it executes the set of functions passed as arguments.
   */
  public Effects clip(ClipAnimation.Action a, ClipAnimation.Corner c,
      ClipAnimation.Direction d, Function... f) {
    return clip(a, c, d, DEFAULT_SPEED, f);
  }
  
  /**
   * Animate the set of matched elements using the clip property.
   * It is possible to show or hide a set of elements, 
   * specify the direction of the animation and the start corner of the effect.
   * Finally it executes the set of functions passed as arguments.
   */
  public Effects clip(final ClipAnimation.Action a, final ClipAnimation.Corner c, 
      final ClipAnimation.Direction d, final int duration, final Function... f) {
    queue(new Function() {
      public void f(Element e) {
        new ClipAnimation(e, a, c, d, f).run(duration);
      }
    });
    return this;
  }

  /**
   * Animate the set of matched elements using the clip property.
   * It is possible to show or hide a set of elements, 
   * specify the direction of the animation and the start corner of the effect.
   * Finally it executes the set of functions passed as arguments.
   */
  public Effects clip(ClipAnimation.Action a, ClipAnimation.Corner c,
      Function... f) {
    return clip(a, c, Direction.BIDIRECTIONAL, DEFAULT_SPEED, f);
  }

  /**
   * Reveal all matched elements by adjusting the clip property firing an
   * optional callback after completion.
   * The effect goes from the center to the perimeter.
   */
  public Effects clipAppear(Function... f) {
    return clipAppear(DEFAULT_SPEED, f);
  }
  
  /**
   * Reveal all matched elements by adjusting the clip property firing an
   * optional callback after completion.
   * The effect goes from the center to the perimeter.
   */
  public Effects clipAppear(int millisecs, Function... f) {
    return clip(ClipAnimation.Action.SHOW, ClipAnimation.Corner.CENTER,
        ClipAnimation.Direction.BIDIRECTIONAL, millisecs, f);
  }

  /**
   * Hide all matched elements by adjusting the clip property firing an
   * optional callback after completion.
   * The effect goes from the perimeter to the center.
   */
  public Effects clipDisappear(Function... f) {
    return clipDisappear(DEFAULT_SPEED, f);
  }
  
  /**
   * Hide all matched elements by adjusting the clip property firing an
   * optional callback after completion.
   * The effect goes from the perimeter to the center.
   */
  public Effects clipDisappear(int millisecs, Function... f) {
    return clip(ClipAnimation.Action.HIDE, ClipAnimation.Corner.CENTER,
        ClipAnimation.Direction.BIDIRECTIONAL, millisecs, f);
  }

  /**
   * Reveal all matched elements by adjusting the clip property firing an
   * optional callback after completion.
   * The effect goes from the top to the bottom.
   */
  public Effects clipDown(Function... f) {
    return clipDown(DEFAULT_SPEED, f);
  }

  /**
   * Reveal all matched elements by adjusting the clip property firing an
   * optional callback after completion.
   * The effect goes from the top to the bottom.
   */
  public Effects clipDown(int millisecs, Function... f) {
    return clip(Action.SHOW, ClipAnimation.Corner.TOP_LEFT,
        ClipAnimation.Direction.BIDIRECTIONAL, millisecs, f);
  }

  /**
   * Hide all matched elements by adjusting the clip property firing an
   * optional callback after completion.
   * The effect goes from the bottom to the top.
   */
  public Effects clipUp(Function... f) {
    return clipUp(DEFAULT_SPEED, f);
  }

  /**
   * Hide all matched elements by adjusting the clip property firing an
   * optional callback after completion.
   * The effect goes from the bottom to the top.
   */
  public Effects clipUp(int millisecs, Function... f) {
    return clip(Action.HIDE, ClipAnimation.Corner.TOP_LEFT,
        ClipAnimation.Direction.BIDIRECTIONAL, millisecs, f);
  }

  /**
   * Fade in all matched elements by adjusting their opacity and firing an
   * optional callback after completion. Only the opacity is adjusted for this
   * animation, meaning that all of the matched elements should already have
   * some form of height and width associated with them.
   */  
  public Effects fadeIn(Function... f) {
    return fadeIn(DEFAULT_SPEED, f);
  }

  /**
   * Fade in all matched elements by adjusting their opacity and firing an
   * optional callback after completion. Only the opacity is adjusted for this
   * animation, meaning that all of the matched elements should already have
   * some form of height and width associated with them.
   */
  public Effects fadeIn(int millisecs, Function... f) {
    return animate("opacity: 'show'", millisecs, f);
  }

  /**
   * Fade out all matched elements by adjusting their opacity to 0, then setting
   * display to "none" and firing an optional callback after completion. Only
   * the opacity is adjusted for this animation, meaning that all of the matched
   * elements should already have some form of height and width associated with
   * them.
   */  
  public Effects fadeOut(Function... f) {
    return fadeOut(DEFAULT_SPEED, f);
  }

  /**
   * Fade out all matched elements by adjusting their opacity to 0, then setting
   * display to "none" and firing an optional callback after completion. Only
   * the opacity is adjusted for this animation, meaning that all of the matched
   * elements should already have some form of height and width associated with
   * them.
   */  
  public Effects fadeOut(int millisecs, Function... f) {
    return animate("opacity: 'hide'", millisecs, f);
  };
  
  /**
   * Fade the opacity of all matched elements to a specified opacity and firing
   * an optional callback after completion. Only the opacity is adjusted for
   * this animation, meaning that all of the matched elements should already
   * have some form of height and width associated with them.
   */
  public Effects fadeTo(int millisecs, double opacity, Function... f) {
    return animate("opacity: " + opacity, millisecs, f);
  }
  
  /**
   * Reveal all matched elements by adjusting their height and firing an
   * optional callback after completion.
   */
  public Effects slideDown(Function... f) {
    return slideDown(DEFAULT_SPEED, f);
  }

  /**
   * Reveal all matched elements by adjusting their height and firing an
   * optional callback after completion.
   */
  public Effects slideDown(int millisecs, Function... f) {
    return animate("height: 'show'", millisecs, f);
  }

  /**
   * Hide all matched elements by adjusting their width and firing an optional
   * callback after completion.
   */
  public Effects slideLeft(Function... f) {
    return slideLeft(DEFAULT_SPEED, f);
  }

  /**
   * Hide all matched elements by adjusting their width and firing an optional
   * callback after completion.
   */
  public Effects slideLeft(int millisecs, Function... f) {
    return animate("width: 'hide'", millisecs, f);
  }

  /**
   * Reveal all matched elements by adjusting their width and firing an
   * optional callback after completion.
   */
  public Effects slideRight(Function... f) {
    return slideRight(DEFAULT_SPEED, f);
  }

  /**
   * Reveal all matched elements by adjusting their width and firing an
   * optional callback after completion.
   */
  public Effects slideRight(final int millisecs, Function... f) {
    return animate("width: 'show'", millisecs, f);
  }

  /**
   * Toggle the visibility of all matched elements by adjusting their height and
   * firing an optional callback after completion. Only the height is adjusted
   * for this animation, causing all matched elements to be hidden or shown in a
   * "sliding" manner
   */  
  public Effects slideToggle(int millisecs, Function... f) {
    return animate("height: 'toggle'", millisecs, f);
  }

  /**
   * Hide all matched elements by adjusting their height and firing an optional
   * callback after completion.
   */
  public Effects slideUp(Function... f) {
    return slideUp(DEFAULT_SPEED, f);
  }

  /**
   * Hide all matched elements by adjusting their height and firing an optional
   * callback after completion.
   */
  public Effects slideUp(int millisecs, Function... f) {
    return animate("height: 'hide'", millisecs, f);
  }

  /**
   * Toggle displaying each of the set of matched elements.
   */
  @Override
  public Effects toggle() {
    return toggle(DEFAULT_SPEED);
  }

  /**
   * Toggle displaying each of the set of matched elements.
   */
  public Effects toggle(int millisecs, Function... f) {
    return animate("opacity: 'toggle'", millisecs, f);
  }
}
