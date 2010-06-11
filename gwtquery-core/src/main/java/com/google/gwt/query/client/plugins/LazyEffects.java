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
import com.google.gwt.query.client.LazyBase;

public interface LazyEffects<T> extends LazyBase<T>{

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
  LazyEffects<T> animate(Properties p, int duration, Easing easing, Function... funcs);

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
  LazyEffects<T> animate(String prop, Function... funcs);

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
  LazyEffects<T> animate(String prop, int duration, Function... funcs);

  /**
   * Animate the set of matched elements using the clip property.
   * It is possible to show or hide a set of elements, 
   * specify the direction of the animation and the start corner of the effect.
   * Finally it executes the set of functions passed as arguments.
   */
  LazyEffects<T> clip(ClipAnimation.Action a, ClipAnimation.Corner c, ClipAnimation.Direction d, Function... f);

  /**
   * Animate the set of matched elements using the clip property.
   * It is possible to show or hide a set of elements, 
   * specify the direction of the animation and the start corner of the effect.
   * Finally it executes the set of functions passed as arguments.
   */
  LazyEffects<T> clip(ClipAnimation.Action a, ClipAnimation.Corner c, ClipAnimation.Direction d, int duration, Function... f);

  /**
   * Animate the set of matched elements using the clip property.
   * It is possible to show or hide a set of elements, 
   * specify the direction of the animation and the start corner of the effect.
   * Finally it executes the set of functions passed as arguments.
   */
  LazyEffects<T> clip(ClipAnimation.Action a, ClipAnimation.Corner c, Function... f);

  /**
   * Reveal all matched elements by adjusting the clip property firing an
   * optional callback after completion.
   * The effect goes from the center to the perimeter.
   */
  LazyEffects<T> clipAppear(Function... f);

  /**
   * Reveal all matched elements by adjusting the clip property firing an
   * optional callback after completion.
   * The effect goes from the center to the perimeter.
   */
  LazyEffects<T> clipAppear(int millisecs, Function... f);

  /**
   * Hide all matched elements by adjusting the clip property firing an
   * optional callback after completion.
   * The effect goes from the perimeter to the center.
   */
  LazyEffects<T> clipDisappear(Function... f);

  /**
   * Hide all matched elements by adjusting the clip property firing an
   * optional callback after completion.
   * The effect goes from the perimeter to the center.
   */
  LazyEffects<T> clipDisappear(int millisecs, Function... f);

  /**
   * Reveal all matched elements by adjusting the clip property firing an
   * optional callback after completion.
   * The effect goes from the top to the bottom.
   */
  LazyEffects<T> clipDown(Function... f);

  /**
   * Reveal all matched elements by adjusting the clip property firing an
   * optional callback after completion.
   * The effect goes from the top to the bottom.
   */
  LazyEffects<T> clipDown(int millisecs, Function... f);

  /**
   * Hide all matched elements by adjusting the clip property firing an
   * optional callback after completion.
   * The effect goes from the bottom to the top.
   */
  LazyEffects<T> clipUp(Function... f);

  /**
   * Hide all matched elements by adjusting the clip property firing an
   * optional callback after completion.
   * The effect goes from the bottom to the top.
   */
  LazyEffects<T> clipUp(int millisecs, Function... f);

  /**
   * Fade in all matched elements by adjusting their opacity and firing an
   * optional callback after completion. Only the opacity is adjusted for this
   * animation, meaning that all of the matched elements should already have
   * some form of height and width associated with them.
   */  
  LazyEffects<T> fadeIn(Function... f);

  /**
   * Fade in all matched elements by adjusting their opacity and firing an
   * optional callback after completion. Only the opacity is adjusted for this
   * animation, meaning that all of the matched elements should already have
   * some form of height and width associated with them.
   */
  LazyEffects<T> fadeIn(int millisecs, Function... f);

  /**
   * Fade out all matched elements by adjusting their opacity to 0, then setting
   * display to "none" and firing an optional callback after completion. Only
   * the opacity is adjusted for this animation, meaning that all of the matched
   * elements should already have some form of height and width associated with
   * them.
   */  
  LazyEffects<T> fadeOut(Function... f);

  /**
   * Fade out all matched elements by adjusting their opacity to 0, then setting
   * display to "none" and firing an optional callback after completion. Only
   * the opacity is adjusted for this animation, meaning that all of the matched
   * elements should already have some form of height and width associated with
   * them.
   */  
  LazyEffects<T> fadeOut(int millisecs, Function... f);

  /**
   * Fade the opacity of all matched elements to a specified opacity and firing
   * an optional callback after completion. Only the opacity is adjusted for
   * this animation, meaning that all of the matched elements should already
   * have some form of height and width associated with them.
   */
  LazyEffects<T> fadeTo(int millisecs, double opacity, Function... f);

  /**
   * Reveal all matched elements by adjusting their height and firing an
   * optional callback after completion.
   */
  LazyEffects<T> slideDown(Function... f);

  /**
   * Reveal all matched elements by adjusting their height and firing an
   * optional callback after completion.
   */
  LazyEffects<T> slideDown(int millisecs, Function... f);

  /**
   * Hide all matched elements by adjusting their width and firing an optional
   * callback after completion.
   */
  LazyEffects<T> slideLeft(Function... f);

  /**
   * Hide all matched elements by adjusting their width and firing an optional
   * callback after completion.
   */
  LazyEffects<T> slideLeft(int millisecs, Function... f);

  /**
   * Reveal all matched elements by adjusting their width and firing an
   * optional callback after completion.
   */
  LazyEffects<T> slideRight(Function... f);

  /**
   * Reveal all matched elements by adjusting their width and firing an
   * optional callback after completion.
   */
  LazyEffects<T> slideRight(int millisecs, Function... f);

  /**
   * Toggle the visibility of all matched elements by adjusting their height and
   * firing an optional callback after completion. Only the height is adjusted
   * for this animation, causing all matched elements to be hidden or shown in a
   * "sliding" manner
   */  
  LazyEffects<T> slideToggle(int millisecs, Function... f);

  /**
   * Hide all matched elements by adjusting their height and firing an optional
   * callback after completion.
   */
  LazyEffects<T> slideUp(Function... f);

  /**
   * Hide all matched elements by adjusting their height and firing an optional
   * callback after completion.
   */
  LazyEffects<T> slideUp(int millisecs, Function... f);

  /**
   * Toggle displaying each of the set of matched elements.
   */
  LazyEffects<T> toggle();

  /**
   * Toggle displaying each of the set of matched elements.
   */
  LazyEffects<T> toggle(int millisecs, Function... f);

}
