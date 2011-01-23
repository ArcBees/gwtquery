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
import static com.google.gwt.query.client.plugins.Widgets.Widgets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.dom.client.BodyElement;
import com.google.gwt.dom.client.ButtonElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.IFrameElement;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.OptionElement;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.TextAreaElement;
import com.google.gwt.query.client.css.CssProperty;
import com.google.gwt.query.client.css.Length;
import com.google.gwt.query.client.css.Percentage;
import com.google.gwt.query.client.css.TakesLength;
import com.google.gwt.query.client.css.TakesPercentage;
import com.google.gwt.query.client.impl.DocumentStyleImpl;
import com.google.gwt.query.client.plugins.EventsListener;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.Window;
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
   * All of the matched set of elements will be inserted at the end
   * of the element(s) specified by the parameter other.
   *
   * The operation $(A).appendTo(B) is, essentially, the reverse of doing a regular
   * $(A).append(B), instead of appending B to A, you're appending A to B.
   */
  LazyGQuery<T> appendTo(GQuery other);

  /**
   * All of the matched set of elements will be inserted at the end
   * of the element(s) specified by the parameter other.
   *
   * The operation $(A).appendTo(B) is, essentially, the reverse of doing a regular
   * $(A).append(B), instead of appending B to A, you're appending A to B.
   */
  LazyGQuery<T> appendTo(Node n);

  /**
   * All of the matched set of elements will be inserted at the end
   * of the element(s) specified by the parameter other.
   *
   * The operation $(A).appendTo(B) is, essentially, the reverse of doing a regular
   * $(A).append(B), instead of appending B to A, you're appending A to B.
   */
  LazyGQuery<T> appendTo(String html);

  /**
   * Convert to Plugin interface provided by Class literal.
   */
  <T extends GQuery> T as(Class<T> plugin);

  /**
   * Return a GWT Widget containing the first matched element.
   * 
   * If the element is already associated to a widget it returns the original widget, 
   * otherwise a new GWT widget will be created depending on the tagName.
   * 
   */
  Widget asWidget();

  /**
   * Set a key/value object as properties to all matched elements.
   *
   * Example: $("img").attr(new Properties("src: 'test.jpg', alt: 'Test Image'"))
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
  LazyGQuery<T> bind(int eventbits, Object data, Function...funcs);

  /**
   * Bind a set of functions to the blur event of each matched element.
   * Or trigger the event if no functions are provided.
   */
  LazyGQuery<T> blur(Function...f);

  /**
   * Bind a set of functions to the change event of each matched element.
   * Or trigger the event if no functions are provided.
   */
  LazyGQuery<T> change(Function...f);

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
   * Bind a set of functions to the click event of each matched element.
   * Or trigger the event if no functions are provided.
   */
  LazyGQuery<T> click(Function...f);

  /**
   * @deprecated use innerHeight()
   */
  int clientHeight();

  /**
   * @deprecated use innerWidth()
   */
  int clientWidth();

  /**
   * Clone matched DOM Elements and select the clones. This is useful for moving
   * copies of the elements to another location in the DOM.
   */
  LazyGQuery<T> clone();

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
   * Set a key/value object as style properties to all matched elements. This
   * serves as the best way to set a large number of style properties on all
   * matched elements.
   *
   * Example: $(".item").css(Properties.create("color: 'red', background:
   * 'blue'"))
   */
  LazyGQuery<T> css(Properties properties);

  /**
   * Return a style property on the first matched element.
   */
  String css(String name);

  /**
   * Return a style property on the first matched element.
   *
   * The parameter force has a special meaning here:
   * - When force is false, returns the value of the css property defined
   *   in the style attribute of the element.
   * - Otherwise it returns the real computed value.
   *
   * For instance if you define 'display=none' not in the element style
   * but in the css stylesheet, it returns an empty string unless you
   * pass the parameter force=true.
   */
  String css(String name, boolean force);

  /**
   * Set a single style property to a value, on all matched elements.
   */
  LazyGQuery<T> css(String prop, String val);

  /**
   * Set CSS property on every matched element using type-safe enumerations.
   */
  <S, T extends CssProperty<S>> LazyGQuery<T> css(T cssProperty, S value);

  /**
   * Set CSS property on every matched element using type-safe enumerations.
   */
  LazyGQuery<T> css(TakesLength cssProperty, Length value);

  /**
   * Set CSS property on every matched element using type-safe enumerations.
   */
  LazyGQuery<T> css(TakesPercentage cssProperty, Percentage value);

  /**
   * Returns the numeric value of a css property.
   */
  double cur(String prop);

  /**
   * Returns the numeric value of a css property.
   *  
   * The parameter force has a special meaning:
   * - When force is false, returns the value of the css property defined
   *   in the set of style attributes. 
   * - When true returns the real computed value.   
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
   * Bind a set of functions to the dblclick event of each matched element.
   * Or trigger the event if no functions are provided.
   */
  LazyGQuery<T> dblclick(Function...f);

  /**
   * Run one or more Functions over each element of the GQuery.
   * You have to override one of these funcions:
   *    public void f(Element e)
   *    public String f(Element e, int i)
   */
  LazyGQuery<T> each(Function... f);

  /**
   * Returns the working set of nodes as a Java array. <b>Do NOT</b> attempt to
   * modify this array, e.g. assign to its elements, or call Arrays.sort()
   */
  Element[] elements();

  /**
   * Remove all child nodes from the set of matched elements.
   * In the case of a document element, it removes all the content
   * You should call this method whenever you create a new iframe and you
   * want to add dynamic content to it.
   */
  LazyGQuery<T> empty();

  /**
   * Revert the most recent 'destructive' operation, changing the set of matched
   * elements to its previous state (right before the destructive operation).
   */
  LazyGQuery<T> end();

  /**
   * Reduce GQuery to element in the specified position.
   */
  LazyGQuery<T> eq(int pos);

  /**
   * Bind a set of functions to the error event of each matched element.
   * Or trigger the event if no functions are provided.
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
   * Bind a set of functions to the focus event of each matched element.
   * Or trigger the event if no functions are provided.
   */
  LazyGQuery<T> focus(Function...f);

  /**
   * Return all elements matched in the GQuery as a NodeList. @see #elements()
   * for a method which returns them as an immutable Java array.
   */
  NodeList<Element> get();

  /**
   * Return the ith element matched.
   */
  Element get(int i);

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
   * Get the current computed, pixel, height of the first matched element.
   * It does not include margin, padding nor border.
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
   * Returns the inner width of the first matched element, including padding
   * but not the vertical scrollbar width, border, or margin.
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
   * Bind a set of functions to the keydown event of each matched element.
   * Or trigger the event if no functions are provided.
   */
  LazyGQuery<T> keydown(Function...f);

  /**
   * Trigger a keydown event passing the key pushed.
   */
  LazyGQuery<T> keydown(int key);

  /**
   * Bind a set of functions to the keypress event of each matched element.
   * Or trigger the event if no functions are provided.
   */
  LazyGQuery<T> keypress(Function...f);

  /**
   * Trigger a keypress event passing the key pushed.
   */
  LazyGQuery<T> keypress(int key);

  /**
   * Bind a set of functions to the keyup event of each matched element.
   * Or trigger the event if no functions are provided.
   */
  LazyGQuery<T> keyup(Function...f);

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
   * Pass each element in the current matched set through a function, 
   * producing a new array containing the return values.
   */
  <W> ArrayList<W> map(Function f);

  /**
   * Bind a set of functions to the mousedown event of each matched element.
   * Or trigger the event if no functions are provided.
   */
  LazyGQuery<T> mousedown(Function...f);

  /**
   * Bind a set of functions to the mousemove event of each matched element.
   * Or trigger the event if no functions are provided.
   */
  LazyGQuery<T> mousemove(Function...f);

  /**
   * Bind a set of functions to the mouseout event of each matched element.
   * Or trigger the event if no functions are provided.
   */
  LazyGQuery<T> mouseout(Function...f);

  /**
   * Bind a set of functions to the mouseover event of each matched element.
   * Or trigger the event if no functions are provided.
   */
  LazyGQuery<T> mouseover(Function...f);

  /**
   * Bind a set of functions to the mouseup event of each matched element.
   * Or trigger the event if no functions are provided.
   */
  LazyGQuery<T> mouseup(Function...f);

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
   * Get the current computed height for the first element in the set of matched elements, 
   * including padding, border, but not the margin.
   */
  int outerHeight();

  /**
   * Get the current computed height for the first element in the set of matched elements, 
   * including padding, border, and optionally margin.
   */
  int outerHeight(boolean includeMargin);

  /**
   * Get the current computed width for the first element in the set of matched elements, 
   * including padding, border, but not the margin.
   */
  int outerWidth();

  /**
   * Get the current computed width for the first element in the set of matched elements, 
   * including padding and border and optionally margin.
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
   * All of the matched set of elements will be inserted at the beginning
   * of the element(s) specified by the parameter other.
   *
   * The operation $(A).prependTo(B) is, essentially, the reverse of doing a regular
   * $(A).prepend(B), instead of prepending B to A, you're prepending A to B.
   */
  LazyGQuery<T> prependTo(GQuery other);

  /**
   * All of the matched set of elements will be inserted at the beginning
   * of the element(s) specified by the parameter other.
   *
   * The operation $(A).prependTo(B) is, essentially, the reverse of doing a regular
   * $(A).prepend(B), instead of prepending B to A, you're prepending A to B.
   */
  LazyGQuery<T> prependTo(Node n);

  /**
   * All of the matched set of elements will be inserted at the beginning
   * of the element(s) specified by the parameter other.
   *
   * The operation $(A).prependTo(B) is, essentially, the reverse of doing a regular
   * $(A).prepend(B), instead of prepending B to A, you're prepending A to B.
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
   * Removes all matched elements from the DOM.
   */
  LazyGQuery<T> remove();

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
   * Replaces the elements matched by the specified selector with the matched
   * elements. This function is the complement to replaceWith() which does the
   * same task with the parameters reversed.
   */
  LazyGQuery<T> replaceAll(Element elem);

  /**
   * Replaces the elements matched by the specified selector with the matched
   * elements. This function is the complement to replaceWith() which does the
   * same task with the parameters reversed.
   */
  LazyGQuery<T> replaceAll(GQuery query);

  /**
   * Replaces the elements matched by the specified selector with the matched
   * elements. This function is the complement to replaceWith() which does the
   * same task with the parameters reversed.
   */
  LazyGQuery<T> replaceAll(String html);

  /**
   * Replaces all matched elements with the specified HTML or DOM elements. This
   * returns the GQuery element that was just replaced, which has been removed
   * from the DOM.
   */
  LazyGQuery<T> replaceWith(Element elem);

  /**
   * Replaces all matched elements with the specified HTML or DOM elements. This
   * returns the GQuery element that was just replaced, which has been removed
   * from the DOM.
   */
  LazyGQuery<T> replaceWith(GQuery query);

  /**
   * Replaces all matched elements with the specified HTML or DOM elements. This
   * returns the GQuery element that was just replaced, which has been removed
   * from the DOM.
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
   * Bind a set of functions to the scroll event of each matched element.
   * Or trigger the event if no functions are provided.
   */
  LazyGQuery<T> scroll(Function...f);

  /**
   * Gets the scroll left offset of the first matched element. This method works
   * for both visible and hidden elements.
   */
  int scrollLeft();

  /**
   * When a value is passed in, the scroll left offset is set to that value on
   * all matched elements. This method works for both visible and hidden
   * elements.
   */
  LazyGQuery<T> scrollLeft(int left);

  /**
   * Gets the scroll top offset of the first matched element. This method works
   * for both visible and hidden elements.
   */
  int scrollTop();

  /**
   * When a value is passed in, the scroll top offset is set to that value on
   * all matched elements. This method works for both visible and hidden
   * elements.
   */
  LazyGQuery<T> scrollTop(int top);

  LazyGQuery<T> select();

  /**
   * Force the current matched set of elements to become
   * the specified array of elements.
   */
  LazyGQuery<T> setArray(NodeList<Element> nodes);

  /**
   * Set CSS property on the first element.
   */
  <S, T extends CssProperty<S>> LazyGQuery<T> setCss(T cssProperty, S value);

  /**
   * Set CSS property on first matched element using type-safe enumerations.
   */
  LazyGQuery<T> setCss(TakesLength cssProperty, Length value);

  /**
   * Set CSS property on first matched element using type-safe enumerations.
   */
  LazyGQuery<T> setCss(TakesPercentage cssProperty, Percentage value);

  void setPreviousObject(GQuery previousObject);

  void setSelector(String selector);

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
   * Adds or removes the specified classes to each matched element
   * depending on the class's presence.
   */
  LazyGQuery<T> toggleClass(String... classes);

  /**
   * Adds or removes the specified classes to each matched element
   * depending on the value of the switch argument.
   *
   * if addOrRemove is true, the class is added and in the case of
   * false it is removed.
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
   * For keyboard events you can pass a second parameter which represents
   * the key-code of the pushed key.
   *
   * Example: fire(Event.ONCLICK | Event.ONFOCUS)
   * Example: fire(Event.ONKEYDOWN. 'a');
   */
  LazyGQuery<T> trigger(int eventbits, int... keys);

  /**
   * Removes all events that match the eventbits.
   */
  LazyGQuery<T> unbind(int eventbits);

  /**
   * Remove all duplicate elements from an array of elements. Note that this
   * only works on arrays of DOM elements, not strings or numbers.
   */
  JSArray unique(JSArray result);

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
   * Return the first non null attached widget from the matched elements
   * or null if there isn't any.
   */
  <W extends Widget> W widget();

  /**
   * return the list of attached widgets matching the query
   */
  List<Widget> widgets();

  /**
   * Get the current computed, pixel, width of the first matched element.
   * It does not include margin, padding nor border.
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
