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
package com.google.gwt.query.client;
import static com.google.gwt.query.client.plugins.Effects.Effects;
import static com.google.gwt.query.client.plugins.Events.Events;
import static com.google.gwt.query.client.plugins.SimpleNamedQueue.SimpleNamedQueue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.dom.client.BodyElement;
import com.google.gwt.dom.client.ButtonElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.OptionElement;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.HasCssName;
import com.google.gwt.dom.client.TextAreaElement;
import com.google.gwt.query.client.css.CSS;
import com.google.gwt.query.client.css.HasCssValue;
import com.google.gwt.query.client.css.TakesCssValue;
import com.google.gwt.query.client.css.TakesCssValue.CssSetter;
import com.google.gwt.query.client.impl.DocumentStyleImpl;
import com.google.gwt.query.client.impl.SelectorEngine;
import com.google.gwt.query.client.js.JsCache;
import com.google.gwt.query.client.js.JsMap;
import com.google.gwt.query.client.js.JsNamedArray;
import com.google.gwt.query.client.js.JsNodeArray;
import com.google.gwt.query.client.js.JsUtils;
import com.google.gwt.query.client.plugins.Effects;
import com.google.gwt.query.client.plugins.Plugin;
import com.google.gwt.query.client.plugins.effects.PropertiesAnimation.Easing;
import com.google.gwt.query.client.plugins.events.EventsListener;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.GqUi;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.query.client.LazyBase;

public interface LazyGQuery<T> extends LazyBase<T>{

  /**
   * Add elements to the set of matched elements if they are not included yet.
   * It also update the selector appending the new one.
   */
  LazyGQuery<T> add(GQuery previousObject);

  /**
   * Add elements to the set of matched elements if they are not included yet.
   */
  LazyGQuery<T> add(String selector);

  /**
   * Adds the specified classes to each matched element.
   */
  LazyGQuery<T> addClass(String... classes);

  /**
   * Insert content after each of the matched elements. The elements must
   * already be inserted into the document (you can't insert an element after
   * another if it's not in the page).
   */
  LazyGQuery<T> after(GQuery query);

  /**
   * Insert content after each of the matched elements. The elements must
   * already be inserted into the document (you can't insert an element after
   * another if it's not in the page).
   */
  LazyGQuery<T> after(Node n);

  /**
   * Insert content after each of the matched elements. The elements must
   * already be inserted into the document (you can't insert an element after
   * another if it's not in the page).
   */
  LazyGQuery<T> after(String html);

  /**
   * Add the previous selection to the current selection. Useful for traversing
   * elements, and then adding something that was matched before the last
   * traversal.
   */
  LazyGQuery<T> andSelf();

  /**
   * The animate() method allows you to create animation effects on any numeric
   * CSS property. All animated properties should be numeric, non-numeric cannot
   * be animated using basic functionality. (For example, width, height, or left
   * can be animated but background-color cannot be.) Property values are
   * treated as a number of pixels unless otherwise specified. The units em and
   * % can be specified where applicable.
   * 
   * Example:
   * 
   * <pre class="code">
   *  //move the element from its original position to the position top:500px and left:500px for 400ms.
   *  //use a swing easing function for the transition
   *  $("#foo").animate(Properties.create("{top:'500px',left:'500px'}"), 400, Easing.SWING);
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
   *  //move the element from its original position to 500px to the left and 5OOpx down for 400ms.
   *  //use a swing easing function for the transition
   *  $("#foo").animate(Properties.create("{top:'+=500px',left:'+=500px'}"), 400, Easing.SWING);
   * </pre>
   * 
   * @param p a {@link Properties} object containing css properties to animate.
   * @param funcs an array of {@link Function} called once the animation is
   *          complete
   * @param duration the duration in milliseconds of the animation
   * @param easing the easing function to use for the transition
   */
  LazyGQuery<T> animate(Properties p, int duration, Easing easing, Function... funcs);

  /**
   * The animate() method allows you to create animation effects on any numeric
   * CSS property. All animated properties should be numeric, non-numeric cannot
   * be animated using basic functionality. (For example, width, height, or left
   * can be animated but background-color cannot be.) Property values are
   * treated as a number of pixels unless otherwise specified. The units em and
   * % can be specified where applicable.
   * 
   * Example:
   * 
   * <pre class="code">
   *  //move the element from its original position to left:500px for 500ms
   *  $("#foo").animate("left:'500'");
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
   *  //move the element from its original position to 500px to the left for 500ms and
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
   * @param prop the property to animate : "cssName:'value'"
   * @param funcs an array of {@link Function} called once the animation is
   *          complete
   * @param duration the duration in milliseconds of the animation
   */
  LazyGQuery<T> animate(String prop, Function... funcs);

  /**
   * The animate() method allows you to create animation effects on any numeric
   * CSS property. All animated properties should be numeric, non-numeric cannot
   * be animated using basic functionality. (For example, width, height, or left
   * can be animated but background-color cannot be.) Property values are
   * treated as a number of pixels unless otherwise specified. The units em and
   * % can be specified where applicable.
   * 
   * 
   * Example:
   * 
   * <pre class="code">
   *  //move the element from its original position to left:500px for 2s
   *  $("#foo").animate("left:'500px'", 2000);
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
   *  //move the element from its original position to 500px to the left for 1000ms and
   *  // change the background color of the element at the end of the animation
   *  $("#foo").animate("left:'+=500'", 1000, new Function(){
   *                  
   *                 public void f(Element e){
   *                   $(e).css(CSS.BACKGROUND_COLOR.with(RGBColor.RED);
   *                 }
   *                 
   *              });
   * </pre>
   * 
   * 
   * @param prop the property to animate : "cssName:'value'"
   * @param funcs an array of {@link Function} called once the animation is
   *          complete
   * @param duration the duration in milliseconds of the animation
   */
  LazyGQuery<T> animate(String prop, int duration, Function... funcs);

  /**
   * Append content to the inside of every matched element. This operation is
   * similar to doing an appendChild to all the specified elements, adding them
   * into the document.
   */
  LazyGQuery<T> append(GQuery query);

  /**
   * Append content to the inside of every matched element. This operation is
   * similar to doing an appendChild to all the specified elements, adding them
   * into the document.
   */
  LazyGQuery<T> append(Node n);

  /**
   * Append content to the inside of every matched element. This operation is
   * similar to doing an appendChild to all the specified elements, adding them
   * into the document.
   */
  LazyGQuery<T> append(String html);

  /**
   * All of the matched set of elements will be inserted at the end of the
   * element(s) specified by the parameter other.
   * 
   * The operation $(A).appendTo(B) is, essentially, the reverse of doing a
   * regular $(A).append(B), instead of appending B to A, you're appending A to
   * B.
   */
  LazyGQuery<T> appendTo(GQuery other);

  /**
   * All of the matched set of elements will be inserted at the end of the
   * element(s) specified by the parameter other.
   * 
   * The operation $(A).appendTo(B) is, essentially, the reverse of doing a
   * regular $(A).append(B), instead of appending B to A, you're appending A to
   * B.
   */
  LazyGQuery<T> appendTo(Node n);

  /**
   * All of the matched set of elements will be inserted at the end of the
   * element(s) specified by the parameter other.
   * 
   * The operation $(A).appendTo(B) is, essentially, the reverse of doing a
   * regular $(A).append(B), instead of appending B to A, you're appending A to
   * B.
   */
  LazyGQuery<T> appendTo(String html);

  /**
   * Convert to Plugin interface provided by Class literal.
   */
  <T extends GQuery> T as(Class<T> plugin);

  /**
   * Set a key/value object as properties to all matched elements.
   * 
   * Example: $("img").attr(new
   * Properties("src: 'test.jpg', alt: 'Test Image'"))
   */
  LazyGQuery<T> attr(Properties properties);

  /**
   * Access a property on the first matched element. This method makes it easy
   * to retrieve a property value from the first matched element. If the element
   * does not have an attribute with such a name, empty string is returned.
   * Attributes include title, alt, src, href, width, style, etc.
   */
  String attr(String name);

  /**
   * Set a single property to a computed value, on all matched elements.
   */
  LazyGQuery<T> attr(String key, Function closure);

  /**
   * Set a single property to a value, on all matched elements.
   */
  LazyGQuery<T> attr(String key, String value);

  /**
   * Insert content before each of the matched elements. The elements must
   * already be inserted into the document (you can't insert an element before
   * another if it's not in the page).
   */
  LazyGQuery<T> before(GQuery query);

  /**
   * Insert content before each of the matched elements. The elements must
   * already be inserted into the document (you can't insert an element before
   * another if it's not in the page).
   */
  LazyGQuery<T> before(Node n);

  /**
   * Insert content before each of the matched elements. The elements must
   * already be inserted into the document (you can't insert an element before
   * another if it's not in the page).
   */
  LazyGQuery<T> before(String html);

  /**
   * Binds a set of handlers to a particular Event for each matched element.
   * 
   * The event handlers are passed as Functions that you can use to prevent
   * default behavior. To stop both default action and event bubbling, the
   * function event handler has to return false.
   * 
   * You can pass an additional Object data to your Function as the second
   * parameter
   * 
   */
  LazyGQuery<T> bind(int eventbits, Object data, Function... funcs);

  /**
   * Binds a set of handlers to a particular Event for each matched element.
   * 
   * The event handlers are passed as Functions that you can use to prevent
   * default behavior. To stop both default action and event bubbling, the
   * function event handler has to return false.
   * 
   * You can pass an additional Object data to your Function as the second
   * parameter
   * 
   */
  LazyGQuery<T> bind(String eventType, Object data, Function... funcs);

  /**
   * Bind a set of functions to the blur event of each matched element. Or
   * trigger the event if no functions are provided.
   */
  LazyGQuery<T> blur(Function... f);

  /**
   * Bind a set of functions to the change event of each matched element. Or
   * trigger the event if no functions are provided.
   */
  LazyGQuery<T> change(Function... f);

  /**
   * Get a set of elements containing all of the unique immediate children of
   * each of the matched set of elements. Also note: while parents() will look
   * at all ancestors, children() will only consider immediate child elements.
   */
  LazyGQuery<T> children();

  /**
   * Get a set of elements containing all of the unique children of each of the
   * matched set of elements. This set is filtered with the expressions that
   * will cause only elements matching any of the selectors to be collected.
   */
  LazyGQuery<T> children(String... filters);

  /**
   * Remove from the Effects queue all {@link Function} that have not yet been
   * run.
   */
  LazyGQuery<T> clearQueue();

  /**
   * Remove from the queue all {@link Function} that have not yet been run.
   */
  LazyGQuery<T> clearQueue(String queueName);

  /**
   * Bind a set of functions to the click event of each matched element. Or
   * trigger the event if no functions are provided.
   */
  LazyGQuery<T> click(Function... f);

  /**
   * Clone matched DOM Elements and select the clones. This is useful for moving
   * copies of the elements to another location in the DOM.
   */
  LazyGQuery<T> clone();

  /**
   * Get the first ancestor element that matches the selector (for each matched
   * element), beginning at the current element and progressing up through the
   * DOM tree.
   * 
   * @param selector
   * @return
   */
  LazyGQuery<T> closest(String selector);

  /**
   * Returns a {@link Map} object as key a selector and as value the list of
   * ancestor elements matching this selectors, beginning at the first matched
   * element and progressing up through the DOM. This method allows retrieving
   * the list of ancestors matching many selectors by traversing the DOM only
   * one time.
   * 
   * @param selector
   * @return
   */
  JsNamedArray<NodeList<Element>> closest(String[] selectors);

  /**
   * Returns a {@link Map} object as key a selector and as value the list of
   * ancestor elements matching this selectors, beginning at the first matched
   * element and progressing up through the DOM until reach the
   * <code>context</code> node.. This method allows retrieving the list of
   * ancestors matching many selectors by traversing the DOM only one time.
   * 
   * @param selector
   * @return
   */
  JsNamedArray<NodeList<Element>> closest(String[] selectors, Node context);

  /**
   * Get the first ancestor element that matches the selector (for each matched
   * element), beginning at the current element and progressing up through the
   * DOM tree until reach the <code>context</code> node.
   * 
   * If no context is passed in then the context of the gQuery object will be
   * used instead.
   * 
   */
  LazyGQuery<T> closest(String selector, Node context);

  /**
   * Filter the set of elements to those that contain the specified text.
   */
  LazyGQuery<T> contains(String text);

  /**
   * Find all the child nodes inside the matched elements (including text
   * nodes), or the content document, if the element is an iframe.
   */
  LazyGQuery<T> contents();

  /**
   * Set CSS a single style property on every matched element using type-safe
   * enumerations.
   * 
   * The best way to use this method (i.e. to generate a CssSetter) is to take
   * the desired css property defined in {@link CSS} class and call the
   * {@link TakesCssValue#with(HasCssName)} method on it.
   * 
   * 
   * ex :
   * 
   * <pre class="code">
   * $("#myDiv").css(CSS.TOP.with(Length.cm(15)));
   * $("#myDiv").css(CSS.BACKGROUND.with(RGBColor.SILVER, ImageValue.url(""),
   *               BackgroundRepeat.NO_REPEAT, BackgroundAttachment.FIXED,
   *               BackgroundPosition.CENTER));
   * $("#myDiv").css(CSS.BACKGROUND_ATTACHMENT.with(BackgroundAttachment.FIXED));
   * 
   * </pre>
   * 
   */
  LazyGQuery<T> css(CssSetter... cssSetter);

  /**
   * Return a style property on the first matched element using type-safe
   * enumerations.
   * 
   * Ex : $("#myId").css(CSS.BACKGROUND_COLOR);
   */
  String css(HasCssValue property);

  /**
   * Return a style property on the first matched element using type-safe
   * enumerations.
   * 
   * The parameter force has a special meaning here: - When force is false,
   * returns the value of the css property defined in the style attribute of the
   * element. - Otherwise it returns the real computed value.
   * 
   * For instance if you define 'display=none' not in the element style but in
   * the css stylesheet, it returns an empty string unless you pass the
   * parameter force=true.
   * 
   * Ex : $("#myId").css(CSS.WIDTH, true);
   */
  String css(HasCssValue property, boolean force);

  /**
   * Set a key/value object as style properties to all matched elements. This
   * serves as the best way to set a large number of style properties on all
   * matched elements.
   * 
   * Example:
   * 
   * <pre class="code">
   *  $(".item").css(Properties.create("color: 'red', background:'blue'"))
   * </pre>
   */
  LazyGQuery<T> css(Properties properties);

  /**
   * Return a style property on the first matched element.
   */
  String css(String name);

  /**
   * Return a style property on the first matched element.
   * 
   * The parameter force has a special meaning here: - When force is false,
   * returns the value of the css property defined in the style attribute of the
   * element. - Otherwise it returns the real computed value.
   * 
   * For instance if you define 'display=none' not in the element style but in
   * the css stylesheet, it returns an empty string unless you pass the
   * parameter force=true.
   */
  String css(String name, boolean force);

  /**
   * Set a single style property to a value, on all matched elements.
   * 
   */
  LazyGQuery<T> css(String prop, String val);

  /**
   * Set CSS a single style property on every matched element using type-safe
   * enumerations. This method allows you to set manually the value or set
   * <i>inherit</i> value
   * 
   * ex :
   * 
   * <pre class="code">
   * $(#myId).css(CSS.TEXT_DECORATION, CSS.INHERIT);
   * </pre>
   */
  LazyGQuery<T> css(TakesCssValue<?> cssProperty, String value);

  /**
   * Returns the numeric value of a css property.
   */
  double cur(String prop);

  /**
   * Returns the numeric value of a css property.
   * 
   * The parameter force has a special meaning: - When force is false, returns
   * the value of the css property defined in the set of style attributes. -
   * When true returns the real computed value.
   */
  double cur(String prop, boolean force);

  /**
   * Returns value at named data store for the element, as set by data(name,
   * value).
   */
  Object data(String name);

  /**
   * Returns value at named data store for the element, as set by data(name,
   * value) with desired return type.
   * 
   * @param clz return type class literal
   */
  <T> T data(String name, Class<T> clz);

  /**
   * Stores the value in the named spot with desired return type.
   */
  LazyGQuery<T> data(String name, Object value);

  /**
   * Bind a set of functions to the dblclick event of each matched element. Or
   * trigger the event if no functions are provided.
   */
  LazyGQuery<T> dblclick(Function... f);

  /**
   * Insert a delay (in ms) in the Effects queue.
   * 
   * Example:
   * 
   * <pre class="code">
   * $('#foo').slideUp(300).delay(800).fadeIn(400); 
   * </pre>
   * 
   * When this statement is executed, the element slides up for 300 milliseconds
   * and then pauses for 800 milliseconds before fading in for 400 milliseconds.
   * 
   * Please note that this methods affects only the Effects queue. So the
   * following example is wrong:
   * 
   * <pre>
   * $('#foo').css(CSS.COLOR.with(RGBColor.RED)).delay(800).css(CSS.COLOR.with(RGBColor.BLACK)); 
   * </pre>
   * 
   * The code above will not insert a delay of 800 ms between the css() calls !
   * For this kind of behavior, please check {@link #delay(int, String)}
   */
  LazyGQuery<T> delay(int milliseconds);

  /**
   * Insert a delay (in ms) in the queue identified by the
   * <code>queueName</code> parameter. if <code>queueName</code> is null or
   * equats to 'fx', the delay will be inserted to the Effects queue.
   * 
   * Example :
   * 
   * <pre class="code">
   * $('#foo').queue("colorQueue", lazy().css(CSS.COLOR.with(RGBColor.RED)).done())
   *          .delay(800, "colorQueue")
   *          .queue("colorQueue", lazy().css(CSS.COLOR.with(RGBColor.BLACK)).done()); 
   * </pre>
   * 
   * When this statement is executed, the text color of the element changes to
   * red and then wait for 800 milliseconds before changes the text color to
   * black.
   * 
   */
  LazyGQuery<T> delay(int milliseconds, String queueName);

  /**
   * Attach <code>handlers</code> to one or more events for all elements that
   * match the <code>selector</code>, now or in the future, based on a specific
   * set of root elements.
   * 
   * Example:
   * 
   * <pre>
   * $("table").delegate("td", "click", new Function(){
   *  public void f(Element e){
   *  $(e).css(CSS.BACKGROUND_COLOR.with(RGBColor.RED));
   *  }
   * });
   * </pre>
   * 
   * This code above add an handler on click event on all cell (the existing
   * oneand the future cell) of all table. This code is equivalent to :
   * 
   * <pre>
   * $("table").each(new Function(){
   *  public void f(Element table){
   *   $("td", table).live("click", new Function(){
   *      public void f(Element e){
   *      $(e).css(CSS.BACKGROUND_COLOR.with(RGBColor.RED));
   *    }
   *  }
   * });
   *
   * </pre>
   * 
   * You can pass attach the handlers to many events by specifying a String with espaced event type.
   * ex:
   * <pre>
   *  $("div.main").delegate(".subMain", "click dblclick", new Function(){...});
   * </pre>
   * </pre>
   */
  LazyGQuery<T> delegate(String selector, String eventType, Function... handlers);

  /**
   * Attach <code>handlers</code> to one or more events for all elements that
   * match the <code>selector</code>, now or in the future, based on a specific
   * set of root elements.
   * 
   * Example:
   * 
   * <pre>
   * $("table").delegate("td", "click", new Function(){
   *  public void f(Element e){
   *  $(e).css(CSS.BACKGROUND_COLOR.with(RGBColor.RED));
   *  }
   * });
   * </pre>
   * 
   * This code above add an handler on click event on all cell (the existing
   * oneand the future cell) of all table. This code is equivalent to :
   * 
   * <pre>
   * $("table").each(new Function(){
   *  public void f(Element table){
   *   $("td", table).live("click", new Function(){
   *      public void f(Element e){
   *      $(e).css(CSS.BACKGROUND_COLOR.with(RGBColor.RED));
   *    }
   *  }
   * });
   *
   * You can pass attach the handlers to many events by specifying a String with espaced event type.
   * ex:
   * <pre>
   *  $("div.main").delegate(".subMain", "click dblclick", new Function(){...});
   * </pre>
   * </pre>
   */
  LazyGQuery<T> delegate(String selector, String eventType, Object data, Function... handlers);

  /**
   * Attach <code>handlers</code> to one or more events for all elements that
   * match the <code>selector</code>, now or in the future, based on a specific
   * set of root elements. 
   * 
   * Example:
   * 
   * <pre>
   * $("table").delegate("td", Event.ONCLICK, new Function(){
   *  public void f(Element e){
   *  $(e).css(CSS.BACKGROUND_COLOR.with(RGBColor.RED));
   *  }
   * });
   * </pre>
   * 
   * This code above add an handler on click event on all cell (the existing
   * oneand the future cell) of all table. This code is equivalent to :
   * 
   * <pre>
   * $("table").each(new Function(){
   *  public void f(Element table){
   *   $("td", table).live(Event.ONCLICK, new Function(){
   *      public void f(Element e){
   *      $(e).css(CSS.BACKGROUND_COLOR.with(RGBColor.RED));
   *    }
   *  }
   * });
   *
   * </pre>
   * 
   * You can attach the handlers to many events by using the '|' operator
   * ex:
   * <pre>
   *  $("div.main").delegate(".subMain", Event.ONCLICK | Event.ONDBLCLICK, new Function(){...});
   * </pre>
   */
  LazyGQuery<T> delegate(String selector, int eventbits, Function... handlers);

  /**
   * Attach <code>handlers</code> to one or more events for all elements that match the <code>selector</code>, 
   * now or in the future, based on a specific set of root elements.
   * The <code>data</code> parameter allows us
   * to pass data to the handler.
   *
   * Example:
   * <pre>
   * $("table").delegate("td", "click", new Function(){
   *  public void f(Element e){
   *  $(e).css(CSS.BACKGROUND_COLOR.with(RGBColor.RED));
   *  }
   * });
   * </pre>
   * This code above add an handler on click event on all cell (the existing oneand the future cell) of all table.
   * This code is equivalent to :
   * <pre>
   * $("table").each(new Function(){
   *  public void f(Element table){
   *   $("td", table).live("click", new Function(){
   *      public void f(Element e){
   *      $(e).css(CSS.BACKGROUND_COLOR.with(RGBColor.RED));
   *    }
   *  }
   * });
   *
   * </pre>
   * 
   * You can pass attach the handlers to many events by using the '|' operator
   * ex:
   * <pre>
   *  $("div.main").delegate(".subMain", Event.ONCLICK | Event.ONDBLCLICK, new Function(){...});
   * </pre>
   */
  LazyGQuery<T> delegate(String selector, int eventbits, Object data, Function... handlers);

  /**
   * Execute the next function on the Effects queue for the matched elements.
   * This method is usefull to tell when a function you add in the Effects queue
   * is ended and so the next function in the queue can start.
   */
  LazyGQuery<T> dequeue();

  /**
   * Execute the next function on the queue for the matched elements. This
   * method is usefull to tell when a function you add in the Effects queue is
   * ended and so the next function in the queue can start.
   */
  LazyGQuery<T> dequeue(String queueName);

  /**
   * Detach all matched elements from the DOM. This method is the same than
   * {@link #remove()} method except all data and event handlers are not remove
   * from the element. This method is useful when removed elements are to be
   * reinserted into the DOM at a later time.
   */
  LazyGQuery<T> detach();

  /**
   * Detach from the DOM all matched elements filtered by the
   * <code>filter</code>.. This method is the same than {@link #remove(String)}
   * method except all data and event handlers are not remove from the element.
   * This method is useful when removed elements are to be reinserted into the
   * DOM at a later time.
   */
  LazyGQuery<T> detach(String filter);

  /**
   * Remove all event handlers previously attached using
   * {@link #live(String, Function)}. In order for this method to function
   * correctly, the selector used with it must match exactly the selector
   * initially used with {@link #live(String, Function)}
   */
  LazyGQuery<T> die();

  /**
   * Remove an event handlers previously attached using
   * {@link #live(String, Function)} In order for this method to function
   * correctly, the selector used with it must match exactly the selector
   * initially used with {@link #live(String, Function)}
   */
  LazyGQuery<T> die(String eventName);

  /**
   * Remove an event handlers previously attached using
   * {@link #live(int, Function)} In order for this method to function
   * correctly, the selector used with it must match exactly the selector
   * initially used with {@link #live(int, Function)}
   */
  LazyGQuery<T> die(int eventbits);

  /**
   * Run one or more Functions over each element of the GQuery. You have to
   * override one of these funcions: public void f(Element e) public String
   * f(Element e, int i)
   */
  LazyGQuery<T> each(Function... f);

  /**
   * Returns the working set of nodes as a Java array. <b>Do NOT</b> attempt to
   * modify this array, e.g. assign to its elements, or call Arrays.sort()
   */
  Element[] elements();

  /**
   * Remove all child nodes from the set of matched elements. In the case of a
   * document element, it removes all the content You should call this method
   * whenever you create a new iframe and you want to add dynamic content to it.
   */
  LazyGQuery<T> empty();

  /**
   * Revert the most recent 'destructive' operation, changing the set of matched
   * elements to its previous state (right before the destructive operation).
   */
  LazyGQuery<T> end();

  /**
   * Reduce GQuery to element in the specified position. This method accept
   * negative index. A negative index is counted from the end of the matched
   * set:
   * 
   * Example:
   * 
   * <pre>
   *  $("div").eq(0) will reduce the matched set to the first matched div
   *  $("div").eq(1) will reduce the matched set to the second matched div
   *  
   *  $("div").eq(-1) will reduce the matched set to the last matched div
   *  $("div").eq(-2) will reduce the matched set to the second-to-last matched div
   *  ...
   * </pre>
   */
  LazyGQuery<T> eq(int pos);

  /**
   * Bind a set of functions to the error event of each matched element. Or
   * trigger the event if no functions are provided.
   */
  LazyGQuery<T> error(Function... f);

  /**
   * Fade in all matched elements by adjusting their opacity. The effect will
   * take 1000 milliseconds to complete
   */
  LazyGQuery<T> fadeIn(Function... f);

  /**
   * Fade in all matched elements by adjusting their opacity.
   */
  LazyGQuery<T> fadeIn(int millisecs, Function... f);

  /**
   * Fade out all matched elements by adjusting their opacity. The effect will
   * take 1000 milliseconds to complete
   */
  LazyGQuery<T> fadeOut(Function... f);

  /**
   * Fade out all matched elements by adjusting their opacity.
   */
  LazyGQuery<T> fadeOut(int millisecs, Function... f);

  /**
   * Toggle the visibility of all matched elements by adjusting their opacity and
   * firing an optional callback after completion. Only the opacity is adjusted for
   * this animation, meaning that all of the matched elements should already
   * have some form of height and width associated with them.
   */
  Effects fadeToggle(int millisecs, Function... f);

  /**
   * Removes all elements from the set of matched elements that do not match the
   * specified function. The function is called with a context equal to the
   * current element. If the function returns false, then the element is removed
   * - anything else and the element is kept.
   */
  LazyGQuery<T> filter(Predicate filterFn);

  /**
   * Removes all elements from the set of matched elements that do not pass the
   * specified css expression. This method is used to narrow down the results of
   * a search. Provide a comma-separated list of expressions to apply multiple
   * filters at once.
   */
  LazyGQuery<T> filter(String... filters);

  /**
   * Searches for all elements that match the specified css expression. This
   * method is a good way to find additional descendant elements with which to
   * process.
   * 
   * Provide a comma-separated list of expressions to apply multiple filters at
   * once.
   */
  LazyGQuery<T> find(String... filters);

  /**
   * Reduce the set of matched elements to the first in the set.
   */
  LazyGQuery<T> first();

  /**
   * Bind a set of functions to the focus event of each matched element. Or
   * trigger the event if no functions are provided.
   */
  LazyGQuery<T> focus(Function... f);

  /**
   * Return all elements matched in the GQuery as a NodeList. @see #elements()
   * for a method which returns them as an immutable Java array.
   */
  NodeList<Element> get();

  /**
   * Return the ith element matched. This method accept negative index. A
   * negative index is counted from the end of the matched set.
   * 
   * Example:
   * 
   * <pre>
   *  $("div").get(0) will return the first matched div
   *  $("div").get(1) will return the second matched div
   *  
   *  $("div").get(-1) will return the last matched div
   *  $("div").get(-2) will return the secont-to-last matched div
   *  ...
   * </pre>
   */
  Element get(int i);

  Node getContext();

  /**
   * Return the previous set of matched elements prior to the last destructive
   * operation (e.g. query)
   */
  LazyGQuery<T> getPreviousObject();

  /**
   * Return the selector representing the current set of matched elements.
   */
  String getSelector();

  /**
   * Returns true any of the specified classes are present on any of the matched
   * Reduce the set of matched elements to all elements after a given position.
   * The position of the element in the set of matched elements starts at 0 and
   * goes to length - 1.
   */
  LazyGQuery<T> gt(int pos);

  /**
   * Returns true any of the specified classes are present on any of the matched
   * elements.
   */
  boolean hasClass(String... classes);

  /**
   * Get the current computed, pixel, height of the first matched element. It
   * does not include margin, padding nor border.
   */
  int height();

  /**
   * Set the height of every element in the matched set.
   */
  LazyGQuery<T> height(int height);

  /**
   * Set the height style property of every matched element. It's useful for
   * using 'percent' or 'em' units Example: $(".a").width("100%")
   */
  LazyGQuery<T> height(String height);

  /**
   * Make invisible all matched elements.
   */
  LazyGQuery<T> hide();

  /**
   * Bind a function to the mouseover event of each matched element. A method
   * for simulating hovering (moving the mouse on, and off, an object). This is
   * a custom method which provides an 'in' to a frequent task. Whenever the
   * mouse cursor is moved over a matched element, the first specified function
   * is fired. Whenever the mouse moves off of the element, the second specified
   * function fires.
   */
  LazyGQuery<T> hover(Function fover, Function fout);

  /**
   * Get the innerHTML of the first matched element.
   */
  String html();

  /**
   * Set the innerHTML of every matched element.
   */
  LazyGQuery<T> html(String html);

  /**
   * Find the index of the specified Element.
   */
  int index(Element element);

  /**
   * Returns the inner height of the first matched element, including padding
   * but not the vertical scrollbar height, border, or margin.
   */
  int innerHeight();

  /**
   * Returns the inner width of the first matched element, including padding but
   * not the vertical scrollbar width, border, or margin.
   */
  int innerWidth();

  /**
   * Insert all of the matched elements after another, specified, set of
   * elements.
   */
  LazyGQuery<T> insertAfter(Element elem);

  /**
   * Insert all of the matched elements after another, specified, set of
   * elements.
   */
  LazyGQuery<T> insertAfter(GQuery query);

  /**
   * Insert all of the matched elements after another, specified, set of
   * elements.
   */
  LazyGQuery<T> insertAfter(String selector);

  /**
   * Insert all of the matched elements before another, specified, set of
   * elements.
   * 
   * The elements must already be inserted into the document (you can't insert
   * an element after another if it's not in the page).
   */
  LazyGQuery<T> insertBefore(Element item);

  /**
   * Insert all of the matched elements before another, specified, set of
   * elements.
   * 
   * The elements must already be inserted into the document (you can't insert
   * an element after another if it's not in the page).
   */
  LazyGQuery<T> insertBefore(GQuery query);

  /**
   * Insert all of the matched elements before another, specified, set of
   * elements.
   * 
   * The elements must already be inserted into the document (you can't insert
   * an element after another if it's not in the page).
   */
  LazyGQuery<T> insertBefore(String selector);

  /**
   * Checks the current selection against an expression and returns true, if at
   * least one element of the selection fits the given expression. Does return
   * false, if no element fits or the expression is not valid.
   */
  boolean is(String... filters);

  /**
   * Bind a set of functions to the keydown event of each matched element. Or
   * trigger the event if no functions are provided.
   */
  LazyGQuery<T> keydown(Function... f);

  /**
   * Trigger a keydown event passing the key pushed.
   */
  LazyGQuery<T> keydown(int key);

  /**
   * Bind a set of functions to the keypress event of each matched element. Or
   * trigger the event if no functions are provided.
   */
  LazyGQuery<T> keypress(Function... f);

  /**
   * Trigger a keypress event passing the key pushed.
   */
  LazyGQuery<T> keypress(int key);

  /**
   * Bind a set of functions to the keyup event of each matched element. Or
   * trigger the event if no functions are provided.
   */
  LazyGQuery<T> keyup(Function... f);

  /**
   * Trigger a keyup event passing the key pushed.
   */
  LazyGQuery<T> keyup(int key);

  /**
   * Reduce the set of matched elements to the final one in the set.
   */
  LazyGQuery<T> last();

  /**
   * Returns the computed left position of the first element matched.
   */
  int left();

  /**
   * Returns the number of elements currently matched. The size function will
   * return the same value.
   */
  int length();

  /**
   * <p>
   * Attach a handler for this event to all elements which match the current
   * selector, now and in the future.
   * <p>
   * <p>
   * Ex :
   * 
   * <pre>
   * $(".clickable").live("click", new Function(){
   *  public void f(Element e){
   *    $(e).css(CSS.COLOR.with(RGBColor.RED));
   *  }
   * });
   *  </pre>
   * 
   * With this code, all elements with class "clickable" present in the DOM or
   * added to the DOM in the future will be clickable. The text color will be
   * changed to red when they will be clicked. So if after in the code, you add
   * another element :
   * 
   * <pre>
   * $("body").append("<div class='clickable'>Click me and I will be red</div>");
   * </pre>
   * 
   * The click on this new element will also trigger the handler.
   * </p>
   * <p>
   * In the same way, if you add "clickable" class on some existing element,
   * these elements will be clickable also.
   * </p>
   * <p>
   * <h3>important remarks</h3>
   * <ul>
   * <li>
   * The live method should be always called after a selector</li>
   * <li>
   * Live events are bound to the context of the {@link GQuery} object :
   * 
   * <pre>
   * $(".clickable", myElement).live("click", new Function(){
   *  public void f(Element e){
   *    $(e).css(CSS.COLOR.with(RGBColor.RED));
   *  }
   * });
   * </pre>
   * The {@link Function} will be called only on elements having the class
   * "clickable" and being descendant of myElement.</li>
   * </ul>
   * </p>
   */
  LazyGQuery<T> live(String eventName, Function... funcs);

  /**
   * Attach a handler for this event to all elements which match the current
   * selector, now and in the future.
   */
  LazyGQuery<T> live(int eventbits, Function... funcs);

  /**
   * Attach a handler for this event to all elements which match the current
   * selector, now and in the future.
   */
  LazyGQuery<T> live(int eventbits, Object data, Function... funcs);

  /**
   * <p>
   * Attach a handler for this event to all elements which match the current
   * selector, now and in the future. The <code>data</code> parameter allows us
   * to pass data to the handler.
   * <p>
   * <p>
   * Ex :
   * 
   * <pre>
   * $(".clickable").live("click", new Function(){
   *  public void f(Element e){
   *    $(e).css(CSS.COLOR.with(RGBColor.RED));
   *  }
   * });
   *  </pre>
   * 
   * With this code, all elements with class "clickable" present in the DOM or
   * added to the DOM in the future will be clickable. The text color will be
   * changed to red when they will be clicked. So if after in the code, you add
   * another element :
   * 
   * <pre>
   * $("body").append("<div class='clickable'>Click me and I will be red</div>");
   * </pre>
   * 
   * The click on this new element will also trigger the handler.
   * </p>
   * <p>
   * In the same way, if you add "clickable" class on some existing element,
   * these elements will be clickable also.
   * </p>
   * <p>
   * <h3>important remarks</h3>
   * <ul>
   * <li>
   * The live method should be always called after a selector</li>
   * <li>
   * Live events are bound to the context of the {@link GQuery} object :
   * 
   * <pre>
   * $(".clickable", myElement).live("click", new Function(){
   *  public void f(Element e){
   *    $(e).css(CSS.COLOR.with(RGBColor.RED));
   *  }
   * });
   * </pre>
   * The {@link Function} will be called only on elements having the class
   * "clickable" and being descendant of myElement.</li>
   * </ul>
   * </p>
   */
  LazyGQuery<T> live(String eventName, Object data, Function... funcs);

  /**
   * Bind a function to the load event of each matched element.
   */
  LazyGQuery<T> load(Function f);

  /**
   * Reduce the set of matched elements to all elements before a given position.
   * The position of the element in the set of matched elements starts at 0 and
   * goes to length - 1.
   */
  LazyGQuery<T> lt(int pos);

  /**
   * Pass each element in the current matched set through a function, producing
   * a new array containing the return values.
   */
  <W> List<W> map(Function f);

  /**
   * Bind a set of functions to the mousedown event of each matched element. Or
   * trigger the event if no functions are provided.
   */
  LazyGQuery<T> mousedown(Function... f);

  /**
   * Bind a set of functions to the mousemove event of each matched element. Or
   * trigger the event if no functions are provided.
   */
  LazyGQuery<T> mousemove(Function... f);

  /**
   * Bind a set of functions to the mouseout event of each matched element. Or
   * trigger the event if no functions are provided.
   */
  LazyGQuery<T> mouseout(Function... f);

  /**
   * Bind a set of functions to the mouseover event of each matched element. Or
   * trigger the event if no functions are provided.
   */
  LazyGQuery<T> mouseover(Function... f);

  /**
   * Bind a set of functions to the mouseup event of each matched element. Or
   * trigger the event if no functions are provided.
   */
  LazyGQuery<T> mouseup(Function... f);

  /**
   * Get a set of elements containing the unique next siblings of each of the
   * given set of elements. next only returns the very next sibling for each
   * element, not all next siblings see {#nextAll}.
   */
  LazyGQuery<T> next();

  /**
   * Get a set of elements containing the unique next siblings of each of the
   * given set of elements filtered by 1 or more selectors. next only returns
   * the very next sibling for each element, not all next siblings see
   * {#nextAll}.
   */
  LazyGQuery<T> next(String... selectors);

  /**
   * Find all sibling elements after the current element.
   */
  LazyGQuery<T> nextAll();

  /**
   * Removes the specified Element from the set of matched elements. This method
   * is used to remove a single Element from a jQuery object.
   */
  LazyGQuery<T> not(Element elem);

  /**
   * Removes any elements inside the passed set of elements from the set of
   * matched elements.
   */
  LazyGQuery<T> not(GQuery gq);

  /**
   * Removes elements matching the specified expression from the set of matched
   * elements.
   */
  LazyGQuery<T> not(String... filters);

  /**
   * Get the current offset of the first matched element, in pixels, relative to
   * the document. The returned object contains two integer properties, top and
   * left. The method works only with visible elements.
   */
  com.google.gwt.query.client.GQuery.Offset offset();

  /**
   * Returns a GQuery collection with the positioned parent of the first matched
   * element. This is the first parent of the element that has position (as in
   * relative or absolute). This method only works with visible elements.
   */
  LazyGQuery<T> offsetParent();

  /**
   * Binds a handler to a particular Event (like Event.ONCLICK) for each matched
   * element. The handler is executed only once for each element.
   * 
   * The event handler is passed as a Function that you can use to prevent
   * default behavior. To stop both default action and event bubbling, the
   * function event handler has to return false.
   * 
   * You can pass an additional Object data to your Function as the second
   * parameter
   */
  LazyGQuery<T> one(int eventbits, Object data, Function f);

  /**
   * Get the current computed height for the first element in the set of matched
   * elements, including padding, border, but not the margin.
   */
  int outerHeight();

  /**
   * Get the current computed height for the first element in the set of matched
   * elements, including padding, border, and optionally margin.
   */
  int outerHeight(boolean includeMargin);

  /**
   * Get the current computed width for the first element in the set of matched
   * elements, including padding, border, but not the margin.
   */
  int outerWidth();

  /**
   * Get the current computed width for the first element in the set of matched
   * elements, including padding and border and optionally margin.
   */
  int outerWidth(boolean includeMargin);

  /**
   * Get a set of elements containing the unique parents of the matched set of
   * elements.
   */
  LazyGQuery<T> parent();

  /**
   * Get a set of elements containing the unique parents of the matched set of
   * elements. You may use an optional expressions to filter the set of parent
   * elements that will match one of them.
   */
  LazyGQuery<T> parent(String... filters);

  /**
   * Get a set of elements containing the unique ancestors of the matched set of
   * elements (except for the root element).
   */
  LazyGQuery<T> parents();

  /**
   * Get a set of elements containing the unique ancestors of the matched set of
   * elements (except for the root element). The matched elements are filtered,
   * returning those that match any of the filters.
   */
  LazyGQuery<T> parents(String... filters);

  /**
   * Gets the top and left position of an element relative to its offset parent.
   * The returned object contains two Integer properties, top and left. For
   * accurate calculations make sure to use pixel values for margins, borders
   * and padding. This method only works with visible elements.
   */
  com.google.gwt.query.client.GQuery.Offset position();

  /**
   * Prepend content to the inside of every matched element. This operation is
   * the best way to insert elements inside, at the beginning, of all matched
   * elements.
   */
  LazyGQuery<T> prepend(GQuery query);

  /**
   * Prepend content to the inside of every matched element. This operation is
   * the best way to insert elements inside, at the beginning, of all matched
   * elements.
   */
  LazyGQuery<T> prepend(Node n);

  /**
   * Prepend content to the inside of every matched element. This operation is
   * the best way to insert elements inside, at the beginning, of all matched
   * elements.
   */
  LazyGQuery<T> prepend(String html);

  /**
   * All of the matched set of elements will be inserted at the beginning of the
   * element(s) specified by the parameter other.
   * 
   * The operation $(A).prependTo(B) is, essentially, the reverse of doing a
   * regular $(A).prepend(B), instead of prepending B to A, you're prepending A
   * to B.
   */
  LazyGQuery<T> prependTo(GQuery other);

  /**
   * All of the matched set of elements will be inserted at the beginning of the
   * element(s) specified by the parameter other.
   * 
   * The operation $(A).prependTo(B) is, essentially, the reverse of doing a
   * regular $(A).prepend(B), instead of prepending B to A, you're prepending A
   * to B.
   */
  LazyGQuery<T> prependTo(Node n);

  /**
   * All of the matched set of elements will be inserted at the beginning of the
   * element(s) specified by the parameter other.
   * 
   * The operation $(A).prependTo(B) is, essentially, the reverse of doing a
   * regular $(A).prepend(B), instead of prepending B to A, you're prepending A
   * to B.
   */
  LazyGQuery<T> prependTo(String html);

  /**
   * Get a set of elements containing the unique previous siblings of each of
   * the matched set of elements. Only the immediately previous sibling is
   * returned, not all previous siblings.
   */
  LazyGQuery<T> prev();

  /**
   * Get a set of elements containing the unique previous siblings of each of
   * the matched set of elements filtered by selector. Only the immediately
   * previous sibling is returned, not all previous siblings.
   */
  LazyGQuery<T> prev(String... selectors);

  /**
   * Find all sibling elements in front of the current element.
   */
  LazyGQuery<T> prevAll();

  /**
   * Put a {@link Function} at the end of the Effects queue.
   * 
   * Example:
   * 
   * <pre class="code">
   * $("#foo").animate("left:'+=500'", 400)
   *      .queue(new Function(){
   *          public void f(Element e){
   *             $(e).css(CSS.BACKGROUNG_COLOR.with(RGBColor.RED));
   *             $(e).dequeue();     
   *          }
   *        }).animate("left:'-=500'", 400)
   * </pre>
   * 
   * When this statement is executed, the element move to 500 px to left for 400
   * ms, then its background color is changed to red and then move to 500px to
   * right for 400ms.
   * 
   * Please note that {@link #dequeue()} function is needed at the end of your
   * function to start the next function in the queue. {@see #dequeue()}
   */
  LazyGQuery<T> queue(Function f);

  /**
   * Put a {@link Function} at the end of a queue.
   * 
   * Example:
   * 
   * <pre class="code">
   * $("#foo").queue("myQueue", new Function(){
   *          public void f(Element e){
   *             $(e).css(CSS.BACKGROUNG_COLOR.with(RGBColor.RED));
   *             $(e).dequeue();     
   *          }
   *        }).delay(500, "myQueue")
   *        .queue("myQueue", new Function(){
   *          public void f(Element e){
   *             $(e).css(CSS.COLOR.with(RGBColor.YELLOW));
   *             $(e).dequeue();     
   *          }
   *         });
   * </pre>
   * 
   * When this statement is executed, the background color of the element is set
   * to red, then wait 500ms before to set the text color of the element to
   * yellow. right for 400ms.
   * 
   * Please note that {@link #dequeue()} function is needed at the end of your
   * function to start the next function in the queue. {@see #dequeue()}
   */
  LazyGQuery<T> queue(String queueName, Function f);

  /**
   * Removes all matched elements from the DOM.
   */
  LazyGQuery<T> remove();

  /**
   * Removes from the DOM all matched elements filtered by the
   * <code>filter</code>.
   */
  LazyGQuery<T> remove(String filter);

  /**
   * Remove the named attribute from every element in the matched set.
   */
  LazyGQuery<T> removeAttr(String key);

  /**
   * Removes the specified classes to each matched element.
   */
  LazyGQuery<T> removeClass(String... classes);

  /**
   * Removes named data store from an element.
   */
  LazyGQuery<T> removeData(String name);

  /**
   * Replaces the element <code>elem</code> by the specified selector with the
   * matched elements. This function is the complement to replaceWith() which
   * does the same task with the parameters reversed.
   * 
   * @return a {@link GQuery} object containing the new elements.
   */
  LazyGQuery<T> replaceAll(Element elem);

  /**
   * Replaces the elements matched by the target with the selected elements.
   * This function is the complement to replaceWith() which does the same task
   * with the parameters reversed.
   * 
   * @return a {@link GQuery} object containing the new elements.
   */
  LazyGQuery<T> replaceAll(GQuery target);

  /**
   * Replaces the elements matched by the specified selector with the matched
   * elements. This function is the complement to replaceWith() which does the
   * same task with the parameters reversed.
   * 
   * @return a {@link GQuery} object containing the new elements.
   */
  LazyGQuery<T> replaceAll(String selector);

  /**
   * Replaces all matched elements with the specified element.
   * 
   * @return the GQuery element that was just replaced, which has been removed
   *         from the DOM and not the new element that has replaced it.
   */
  LazyGQuery<T> replaceWith(Element elem);

  /**
   * Replaces all matched elements with elements selected by <code>target</code>
   * .
   * 
   * @return the GQuery element that was just replaced, which has been removed
   *         from the DOM and not the new element that has replaced it.
   */
  LazyGQuery<T> replaceWith(GQuery target);

  /**
   * Replaces all matched elements with the specified HTML.
   * 
   * @return the GQuery element that was just replaced, which has been removed
   *         from the DOM and not the new element that has replaced it.
   */
  LazyGQuery<T> replaceWith(String html);

  /**
   * Save a set of Css properties of every matched element.
   */
  void restoreCssAttrs(String... cssProps);

  /**
   * Restore a set of previously saved Css properties in every matched element.
   */
  void saveCssAttrs(String... cssProps);

  /**
   * Bind a set of functions to the scroll event of each matched element. Or
   * trigger the event if no functions are provided.
   */
  LazyGQuery<T> scroll(Function... f);

  /**
   * Scrolls the first matched element into view.
   */
  LazyGQuery<T> scrollIntoView();

  /**
   * Scrolls the first matched element into view.
   * 
   * If ensure == true, it crawls up the DOM hierarchy, adjusting the scrollLeft
   * and scrollTop properties of each scroll-able element to ensure that the
   * specified element is completely in view. It adjusts each scroll position by
   * the minimum amount necessary.
   */
  LazyGQuery<T> scrollIntoView(boolean ensure);

  /**
   * Gets the scroll left offset of the first matched element. This method works
   * for both visible and hidden elements.
   */
  int scrollLeft();

  /**
   * The scroll left offset is set to the passed value on all matched elements.
   * This method works for both visible and hidden elements.
   */
  LazyGQuery<T> scrollLeft(int left);

  /**
   * 
   * Scrolls the contents of all matched elements to the specified co-ordinate
   * becoming the top left corner of the viewable area.
   * 
   * This method is only useful where there are areas of the document not
   * viewable within the current viewable area of the window and the visible
   * property of the window's scrollbar must be set to true.
   * 
   */
  LazyGQuery<T> scrollTo(int left, int top);

  /**
   * Gets the scroll top offset of the first matched element. This method works
   * for both visible and hidden elements.
   */
  int scrollTop();

  /**
   * The scroll top offset is set to the passed value on all matched elements.
   * This method works for both visible and hidden elements.
   */
  LazyGQuery<T> scrollTop(int top);

  LazyGQuery<T> select();

  /**
   * Force the current matched set of elements to become the specified array of
   * elements.
   */
  LazyGQuery<T> setArray(NodeList<Element> nodes);

  void setPreviousObject(GQuery previousObject);

  LazyGQuery<T> setSelector(String selector);

  /**
   * Make all matched elements visible
   */
  LazyGQuery<T> show();

  /**
   * Get a set of elements containing all of the unique siblings of each of the
   * matched set of elements.
   */
  LazyGQuery<T> siblings();

  /**
   * Get a set of elements containing all of the unique siblings of each of the
   * matched set of elements filtered by the provided set of selectors.
   */
  LazyGQuery<T> siblings(String... selectors);

  /**
   * Return the number of elements in the matched set.
   */
  int size();

  /**
   * Selects a subset of the matched elements.
   */
  LazyGQuery<T> slice(int start, int end);

  /**
   * Reveal all matched elements by adjusting their height and firing an
   * optional callback after completion.
   */
  Effects slideDown(Function... f);

  /**
   * Reveal all matched elements by adjusting their height and firing an
   * optional callback after completion.
   */
  Effects slideDown(int millisecs, Function... f);

  /**
   * Toggle the visibility of all matched elements by adjusting their height and
   * firing an optional callback after completion. Only the height is adjusted
   * for this animation, causing all matched elements to be hidden or shown in a
   * "sliding" manner
   */
  Effects slideToggle(int millisecs, Function... f);

  /**
   * Hide all matched elements by adjusting their height and firing an optional
   * callback after completion.
   */
  Effects slideUp(Function... f);

  /**
   * Hide all matched elements by adjusting their height and firing an optional
   * callback after completion.
   */
  Effects slideUp(int millisecs, Function... f);

  /**
   * Stop the animation currently running.
   */
  LazyGQuery<T> stop();

  /**
   * Stop the animation currently running.
   */
  LazyGQuery<T> stop(boolean clearQueue);

  LazyGQuery<T> submit();

  /**
   * Return the text contained in the first matched element.
   */
  String text();

  /**
   * Set the innerText of every matched element.
   */
  LazyGQuery<T> text(String txt);

  /**
   * Toggle visibility of elements.
   */
  LazyGQuery<T> toggle();

  /**
   * Toggle among two or more function calls every other click.
   */
  LazyGQuery<T> toggle(Function... fn);

  /**
   * Adds or removes the specified classes to each matched element depending on
   * the class's presence.
   */
  LazyGQuery<T> toggleClass(String... classes);

  /**
   * Adds or removes the specified classes to each matched element depending on
   * the value of the switch argument.
   * 
   * if addOrRemove is true, the class is added and in the case of false it is
   * removed.
   */
  LazyGQuery<T> toggleClass(String clz, boolean addOrRemove);

  /**
   * Returns the computed left position of the first element matched.
   */
  int top();

  /**
   * Produces a string representation of the matched elements.
   */
  String toString();

  /**
   * Produces a string representation of the matched elements.
   */
  String toString(boolean pretty);

  /**
   * Trigger a set of events on each matched element.
   * 
   * For keyboard events you can pass a second parameter which represents the
   * key-code of the pushed key.
   * 
   * Example: fire(Event.ONCLICK | Event.ONFOCUS) Example: fire(Event.ONKEYDOWN.
   * 'a');
   */
  LazyGQuery<T> trigger(int eventbits, int... keys);

  /**
   * Removes all events that match the eventbits.
   */
  LazyGQuery<T> unbind(int eventbits);

  /**
   * Remove all event delegation that have been bound using
   * {@link #delegate(String, int, Function...)} {@link #live(int, Function...)} methods
   */
  LazyGQuery<T> undelegate();

  /**
   * Undelegate is a way of removing event handlers that have been bound using
   * {@link #delegate(String, int, Function...)} method
   */
  LazyGQuery<T> undelegate(String selector);

  /**
   * Undelegate is a way of removing event handlers that have been bound using
   * {@link #delegate(String, int, Function...)} method
   */
  LazyGQuery<T> undelegate(String selector, String eventName);

  /**
   * Undelegate is a way of removing event handlers that have been bound using
   * {@link #delegate(String, int, Function...)} method
   */
  LazyGQuery<T> undelegate(String selector, int eventBit);

  /**
   * Remove all duplicate elements from an array of elements. Note that this
   * only works on arrays of DOM elements, not strings or numbers.
   */
  JsNodeArray unique(JsNodeArray result);

  /**
   * This method removes the element's parent. The matched elements replaces
   * their parents within the DOM structure. It is the inverse of
   * {@link GQuery#wrap(GQuery)} method
   * 
   * @return
   */
  LazyGQuery<T> unwrap();

  /**
   * Gets the content of the value attribute of the first matched element,
   * returns only the first value even if it is a multivalued element. To get an
   * array of all values in multivalues elements use vals()
   * 
   * When the first element is a radio-button and is not checked, then it looks
   * for a the first checked radio-button that has the same name in the list of
   * matched elements.
   */
  String val();

  /**
   * Sets the value attribute of every matched element In the case of multivalue
   * elements, all values are setted for other elements, only the first value is
   * considered.
   */
  LazyGQuery<T> val(String... values);

  /**
   * Gets the content of the value attribute of the first matched element,
   * returns more than one value if it is a multiple select.
   * 
   * When the first element is a radio-button and is not checked, then it looks
   * for a the first checked radio-button that has the same name in the list of
   * matched elements.
   * 
   * This method always returns an array. If no valid value can be determined
   * the array will be empty, otherwise it will contain one or more values.
   */
  String[] vals();

  /**
   * Return true if the first element is visible.
   */
  boolean visible();

  /**
   * Return the first non null attached widget from the matched elements or null
   * if there isn't any.
   */
  <W extends Widget> W widget();

  /**
   * Return the nth non null attached widget from the matched elements or null
   * if there isn't any.
   */
  <W extends Widget> W widget(int n);

  /**
   * return the list of attached widgets matching the query
   */
  List<Widget> widgets();

  /**
   * Return the list of attached widgets instance of the provided class matching
   * the query.
   * 
   * This method is very useful for decoupled views, so as we can access widgets
   * from other views without maintaining methods which export them.
   * 
   */
  <W extends Widget> List<W> widgets(Class<W> clazz);

  /**
   * Get the current computed, pixel, width of the first matched element. It
   * does not include margin, padding nor border.
   */
  int width();

  /**
   * Set the width of every matched element.
   */
  LazyGQuery<T> width(int width);

  /**
   * Wrap each matched element with the specified HTML content. This wrapping
   * process is most useful for injecting additional structure into a document,
   * without ruining the original semantic qualities of a document. This works
   * by going through the first element provided (which is generated, on the
   * fly, from the provided HTML) and finds the deepest descendant element
   * within its structure -- it is that element that will enwrap everything
   * else.
   */
  LazyGQuery<T> wrap(Element elem);

  /**
   * Wrap each matched element with the specified HTML content. This wrapping
   * process is most useful for injecting additional structure into a document,
   * without ruining the original semantic qualities of a document. This works
   * by going through the first element provided (which is generated, on the
   * fly, from the provided HTML) and finds the deepest descendant element
   * within its structure -- it is that element that will enwrap everything
   * else.
   */
  LazyGQuery<T> wrap(GQuery query);

  /**
   * Wrap each matched element with the specified HTML content. This wrapping
   * process is most useful for injecting additional structure into a document,
   * without ruining the original semantic qualities of a document. This works
   * by going through the first element provided (which is generated, on the
   * fly, from the provided HTML) and finds the deepest descendant element
   * within its structure -- it is that element that will enwrap everything
   * else.
   */
  LazyGQuery<T> wrap(String html);

  /**
   * Wrap all the elements in the matched set into a single wrapper element.
   * This is different from .wrap() where each element in the matched set would
   * get wrapped with an element. This wrapping process is most useful for
   * injecting additional structure into a document, without ruining the
   * original semantic qualities of a document.
   * 
   * This works by going through the first element provided (which is generated,
   * on the fly, from the provided HTML) and finds the deepest descendant
   * element within its structure -- it is that element that will enwrap
   * everything else.
   */
  LazyGQuery<T> wrapAll(Element elem);

  /**
   * Wrap all the elements in the matched set into a single wrapper element.
   * This is different from .wrap() where each element in the matched set would
   * get wrapped with an element. This wrapping process is most useful for
   * injecting additional structure into a document, without ruining the
   * original semantic qualities of a document.
   * 
   * This works by going through the first element provided (which is generated,
   * on the fly, from the provided HTML) and finds the deepest descendant
   * element within its structure -- it is that element that will enwrap
   * everything else.
   */
  LazyGQuery<T> wrapAll(GQuery query);

  /**
   * Wrap all the elements in the matched set into a single wrapper element.
   * This is different from .wrap() where each element in the matched set would
   * get wrapped with an element. This wrapping process is most useful for
   * injecting additional structure into a document, without ruining the
   * original semantic qualities of a document.
   * 
   * This works by going through the first element provided (which is generated,
   * on the fly, from the provided HTML) and finds the deepest descendant
   * element within its structure -- it is that element that will enwrap
   * everything else.
   */
  LazyGQuery<T> wrapAll(String html);

  /**
   * Wrap the inner child contents of each matched element (including text
   * nodes) with an HTML structure. This wrapping process is most useful for
   * injecting additional structure into a document, without ruining the
   * original semantic qualities of a document. This works by going through the
   * first element provided (which is generated, on the fly, from the provided
   * HTML) and finds the deepest ancestor element within its structure -- it is
   * that element that will enwrap everything else.
   */
  LazyGQuery<T> wrapInner(Element elem);

  /**
   * Wrap the inner child contents of each matched element (including text
   * nodes) with an HTML structure. This wrapping process is most useful for
   * injecting additional structure into a document, without ruining the
   * original semantic qualities of a document. This works by going through the
   * first element provided (which is generated, on the fly, from the provided
   * HTML) and finds the deepest ancestor element within its structure -- it is
   * that element that will enwrap everything else.
   */
  LazyGQuery<T> wrapInner(GQuery query);

  /**
   * Wrap the inner child contents of each matched element (including text
   * nodes) with an HTML structure. This wrapping process is most useful for
   * injecting additional structure into a document, without ruining the
   * original semantic qualities of a document. This works by going through the
   * first element provided (which is generated, on the fly, from the provided
   * HTML) and finds the deepest ancestor element within its structure -- it is
   * that element that will enwrap everything else.
   */
  LazyGQuery<T> wrapInner(String html);

}
