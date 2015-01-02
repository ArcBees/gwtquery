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
package com.google.gwt.query.client.plugins;

import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.LazyBase;
import com.google.gwt.query.client.plugins.effects.PropertiesAnimation.Easing;

/**
 * LazyEffects.
 * @param <T>
 */
public interface LazyEffects<T> extends LazyBase<T> {

  /**
   * The animate() method allows you to create animation effects on any numeric
   * Attribute, CSS property, or color CSS property.
   *
   * Concerning to numeric properties, values are treated as a number of pixels
   * unless otherwise specified. The units em and % can be specified where
   * applicable.
   *
   * By default animate considers css properties, if you wanted to animate element
   * attributes you should to prepend the symbol dollar to the attribute name.
   *
   * Example:
   *
   * <pre class="code">
   *  // move the element from its original position to the position top:500px and left:500px for 400ms.
   *  // use a swing easing function for the transition
   *  $("#foo").animate(Properties.create("{top:'500px',left:'500px'}"), 400, EasingCurve.swing);
   *  // Change the width and border attributes of a table
   *  $("table").animate(Properties.create("{$width: '500', $border: '10'}"), 400, EasingCurve.linear);
   * </pre>
   *
   * In addition to numeric values, each property can take the strings 'show',
   * 'hide', and 'toggle'. These shortcuts allow for custom hiding and showing
   * animations that take into account the display type of the element. Animated
   * properties can also be relative. If a value is supplied with a leading +=
   * or -= sequence of characters, then the target value is computed by adding
   * or subtracting the given number from the current value of the property.
   *
   * Example:
   *
   * <pre class="code">
   *  // move the element from its original position to 500px to the left and 5OOpx down for 400ms.
   *  // use a swing easing function for the transition
   *  $("#foo").animate(Properties.create("{top:'+=500px',left:'+=500px'}"), 400, Easing.SWING);
   * </pre>
   *
   * For color css properties, values can be specified via hexadecimal or rgb or
   * literal values.
   *
   * Example:
   *
   * <pre class="code">
   *  $("#foo").animate("backgroundColor:'red', color:'#ffffff', borderColor:'rgb(129, 0, 70)'", 400, EasingCurve.swing);
   *  $("#foo").animate($$("{backgroundColor:'red', color:'#ffffff', borderColor:'rgb(129, 0, 70)'}"), 400, EasingCurve.swing);
   * </pre>
   *
   * @param stringOrProperties {@link Properties} object containing css properties to animate.
   * @param funcs an array of {@link Function} called once the animation is
   *          complete
   * @param duration the duration in milliseconds of the animation
   * @param easing the easing function to use for the transition
   */
  LazyEffects<T> animate(Object stringOrProperties, int duration, Easing easing, Function... funcs);

  /**
   *
   * The animate() method allows you to create animation effects on any numeric
   * Attribute, CSS property, or color CSS property.
   *
   * Concerning to numeric properties, values are treated as a number of pixels
   * unless otherwise specified. The units em and % can be specified where
   * applicable.
   *
   * By default animate considers css properties, if you wanted to animate element
   * attributes you should to prepend the symbol dollar to the attribute name.
   *
   * Example:
   *
   * <pre class="code">
   *  // move the element from its original position to left:500px for 500ms
   *  $("#foo").animate("left:'500'");
   *  // Change the width attribute of a table
   *  $("table").animate("$width:'500'"), 400, EasingCurve.swing);
   * </pre>
   *
   * In addition to numeric values, each property can take the strings 'show',
   * 'hide', and 'toggle'. These shortcuts allow for custom hiding and showing
   * animations that take into account the display type of the element. Animated
   * properties can also be relative. If a value is supplied with a leading +=
   * or -= sequence of characters, then the target value is computed by adding
   * or subtracting the given number from the current value of the property.
   *
   * Example:
   *
   * <pre class="code">
   *  // move the element from its original position to 500px to the left for 500ms and
   *  // change the background color of the element at the end of the animation
   *  $("#foo").animate("left:'+=500'", new Function(){
   *
   *                 public void f(Element e){
   *                   $(e).css(CSS.BACKGROUND_COLOR.with(RGBColor.RED);
   *                 }
   *
   *              });
   * </pre>
   *
   * The duration of the animation is 500ms.
   *
   * For color css properties, values can be specified via hexadecimal or rgb or
   * literal values.
   *
   * Example:
   *
   * <pre class="code">
   *  $("#foo").animate("backgroundColor:'red', color:'#ffffff', borderColor:'rgb(129, 0, 70)'");
   * </pre>
   *
   * @param stringOrProperties the property to animate : "cssName:'value'"
   * @param funcs an array of {@link Function} called once the animation is
   *          complete
   */
  LazyEffects<T> animate(Object stringOrProperties, Function... funcs);

  /**
   * The animate() method allows you to create animation effects on any numeric
   * Attribute, CSS properties, or color CSS property.
   *
   * Concerning to numeric property, values are treated as a number of pixels
   * unless otherwise specified. The units em and % can be specified where
   * applicable.
   *
   * By default animate considers css properties, if you wanted to animate element
   * attributes you should to prepend the symbol dollar to the attribute name.
   *
   * Example:
   *
   * <pre class="code">
   *  // move the element from its original position to left:500px for 2s
   *  $("#foo").animate("left:'500px'", 2000);
   *  // Change the width attribute of a table
   *  $("table").animate("$width:'500'"), 400);
   * </pre>
   *
   * In addition to numeric values, each property can take the strings 'show',
   * 'hide', and 'toggle'. These shortcuts allow for custom hiding and showing
   * animations that take into account the display type of the element. Animated
   * properties can also be relative. If a value is supplied with a leading +=
   * or -= sequence of characters, then the target value is computed by adding
   * or subtracting the given number from the current value of the property.
   *
   * Example:
   *
   * <pre class="code">
   *  // move the element from its original position to 500px to the left for 1000ms and
   *  // change the background color of the element at the end of the animation
   *  $("#foo").animate("left:'+=500'", 1000, new Function(){
   *     public void f(Element e){
   *       $(e).css(CSS.BACKGROUND_COLOR.with(RGBColor.RED);
   *     }
   *  });
   * </pre>
   *
   * For color css properties, values can be specified via hexadecimal or rgb or
   * literal values.
   *
   * Example:
   *
   * <pre class="code">
   *  $("#foo").animate("backgroundColor:'red', color:'#ffffff', borderColor:'rgb(129, 0, 70)', 1000");
   * </pre>
   *
   * @param stringOrProperties the property to animate : "cssName:'value'"
   * @param funcs an array of {@link Function} called once the animation is
   *          complete
   * @param duration the duration in milliseconds of the animation
   */
  LazyEffects<T> animate(Object stringOrProperties, int duration, Function... funcs);

  /**
   * Reveal all matched elements by adjusting the clip or scale property firing an optional callback
   * after completion. The effect goes from the center to the perimeter.
   */
  LazyEffects<T> clipAppear(Function... f);

  /**
   * Reveal all matched elements by adjusting the clip or scale property firing an optional callback
   * after completion. The effect goes from the center to the perimeter.
   */
  LazyEffects<T> clipAppear(int millisecs, Function... f);

  /**
   * Hide all matched elements by adjusting the clip or scale property firing an optional callback
   * after completion. The effect goes from the perimeter to the center.
   */
  LazyEffects<T> clipDisappear(Function... f);

  /**
   * Hide all matched elements by adjusting the clip or scale property firing an optional callback
   * after completion. The effect goes from the perimeter to the center.
   */
  LazyEffects<T> clipDisappear(int millisecs, Function... f);

  /**
   * Reveal all matched elements by adjusting the clip or scale property firing an optional callback
   * after completion. The effect goes from the top to the bottom.
   */
  LazyEffects<T> clipDown(Function... f);

  /**
   * Reveal all matched elements by adjusting the clip or scale property firing an optional callback
   * after completion. The effect goes from the top to the bottom.
   */
  LazyEffects<T> clipDown(int millisecs, Function... f);

  /**
   * Toggle the visibility of all matched elements by adjusting the clip or scale property and
   * firing an optional callback after completion. The effect goes from the bottom to the top.
   */
  LazyEffects<T> clipToggle(Function... f);

  /**
   * Toggle the visibility of all matched elements by adjusting the clip or scale or scale property
   * and firing an optional callback after completion. The effect goes from the bottom to the top.
   */
  LazyEffects<T> clipToggle(int millisecs, Function... f);

  /**
   * Hide all matched elements by adjusting the clip or scale property firing an optional callback
   * after completion. The effect goes from the bottom to the top.
   */
  LazyEffects<T> clipUp(Function... f);

  /**
   * Hide all matched elements by adjusting the clip or scale property firing an optional callback
   * after completion. The effect goes from the bottom to the top.
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
  LazyEffects<T> fadeTo(double opacity, Function... f);

  /**
   * Fade the opacity of all matched elements to a specified opacity and firing
   * an optional callback after completion. Only the opacity is adjusted for
   * this animation, meaning that all of the matched elements should already
   * have some form of height and width associated with them.
   */
  LazyEffects<T> fadeTo(int millisecs, double opacity, Function... f);

  /**
   * Display or hide the matched elements by animating their opacity. and firing
   * an optional callback after completion. Only the opacity is adjusted for
   * this animation, meaning that all of the matched elements should already
   * have some form of height and width associated with them.
   */
  LazyEffects<T> fadeToggle(Function... f);

  /**
   * Display or hide the matched elements by animating their opacity. and firing
   * an optional callback after completion. Only the opacity is adjusted for
   * this animation, meaning that all of the matched elements should already
   * have some form of height and width associated with them.
   */
  LazyEffects<T> fadeToggle(int millisecs, Function... f);

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
   * Reveal all matched elements by adjusting their width and firing an optional
   * callback after completion.
   */
  LazyEffects<T> slideRight(Function... f);

  /**
   * Reveal all matched elements by adjusting their width and firing an optional
   * callback after completion.
   */
  LazyEffects<T> slideRight(int millisecs, Function... f);

  /**
   * Toggle the visibility of all matched elements by adjusting their height and firing an optional
   * callback after completion. Only the height is adjusted for this animation, causing all matched
   * elements to be hidden or shown in a "sliding" manner
   */
  LazyEffects<T> slideToggle(Function... f);

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
   * Toggle displaying each of the set of matched elements by animating the
   * width, height, and opacity of the matched elements simultaneously. When
   * these properties reach 0 after a hiding animation, the display style
   * property is set to none to ensure that the element no longer affects the
   * layout of the page.
   */
  LazyEffects<T> toggle(int millisecs, Function... f);

}
