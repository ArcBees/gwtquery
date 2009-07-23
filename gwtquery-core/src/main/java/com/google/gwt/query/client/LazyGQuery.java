package com.google.gwt.query.client;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
//import com.google.gwt.query.client.css.CssProperty;

/**
 * Created by IntelliJ IDEA. User: ray Date: May 2, 2009 Time: 10:48:07 PM To
 * change this template use File | Settings | File Templates.
 */
public interface LazyGQuery<T> extends LazyBase<T> {

  /**
   * Add elements to the set of matched elements if they are not included yet.
   */

  LazyGQuery add(String selector);

  /**
   * Adds the specified classes to each matched element. Add elements to the set
   * of matched elements if they are not included yet.
   */
  LazyGQuery add(GQuery previousObject);

  /**
   * Adds the specified classes to each matched element.
   */
  LazyGQuery addClass(String... classes);

  /**
   * Insert content after each of the matched elements. The elements must
   * already be inserted into the document (you can't insert an element after
   * another if it's not in the page).
   */
  LazyGQuery after(Node n);

  /**
   * Insert content after each of the matched elements. The elements must
   * already be inserted into the document (you can't insert an element after
   * another if it's not in the page).
   */
  LazyGQuery after(String html);

  /**
   * Insert content after each of the matched elements. The elements must
   * already be inserted into the document (you can't insert an element after
   * another if it's not in the page).
   */
  LazyGQuery after(GQuery query);

  /**
   * Add the previous selection to the current selection. Useful for traversing
   * elements, and then adding something that was matched before the last
   * traversal.
   */
  LazyGQuery andSelf();

  /**
   * Append content to the inside of every matched element. This operation is
   * similar to doing an appendChild to all the specified elements, adding them
   * into the document.
   */
  LazyGQuery append(String html);

  /**
   * Append content to the inside of every matched element. This operation is
   * similar to doing an appendChild to all the specified elements, adding them
   * into the document.
   */
  LazyGQuery append(Node n);

  /**
   * Append content to the inside of every matched element. This operation is
   * similar to doing an appendChild to all the specified elements, adding them
   * into the document.
   */
  LazyGQuery append(GQuery query);

  /**
   * Append all of the matched elements to another, specified, set of elements.
   * This operation is, essentially, the reverse of doing a regular
   * $(A).append(B), in that instead of appending B to A, you're appending A to
   * B.
   */
  LazyGQuery appendTo(GQuery other);

  /**
   * Convert to Plugin interface provided by Class literal.
   */
  <T extends LazyGQuery> T as(Class<T> plugin);

  /**
   * Set a single property to a value, on all matched elements.
   */
  LazyGQuery attr(String key, String value);

  /**
   * Set a key/value object as properties to all matched elements.
   *
   * Example: $("img").attr(new Properties("src: 'test.jpg', alt: 'Test
   * Image'"))
   */
  LazyGQuery attr(Properties properties);

  /**
   * Set a single property to a computed value, on all matched elements.
   */
  LazyGQuery attr(String key, Function closure);

  /**
   * Insert content before each of the matched elements. The elements must
   * already be inserted into the document (you can't insert an element before
   * another if it's not in the page).
   */
  LazyGQuery before(Node n);

  /**
   * Insert content before each of the matched elements. The elements must
   * already be inserted into the document (you can't insert an element before
   * another if it's not in the page).
   */
  LazyGQuery before(GQuery query);

  /**
   * Insert content before each of the matched elements. The elements must
   * already be inserted into the document (you can't insert an element before
   * another if it's not in the page).
   */
  LazyGQuery before(String html);

  /**
   * Binds a handler to a particular Event (like Event.ONCLICK) for each matched
   * element.
   *
   * The event handler is passed as a Function that you can use to prevent
   * default behaviour. To stop both default action and event bubbling, the
   * function event handler has to return false.
   *
   * You can pass an additional Object data to your Function as the second
   * parameter
   */
  LazyGQuery bind(int eventbits, Object data, Function f);

  /**
   * Bind a function to the blur event of each matched element.
   */
  LazyGQuery blur(Function f);

  /**
   * Trigger a blur event.
   */
  LazyGQuery blur();

  /**
   * Bind a function to the change event of each matched element.
   */
  LazyGQuery change(Function f);

  /**
   * Trigger a change event.
   */
  LazyGQuery change();

  /**
   * Get a set of elements containing all of the unique children of each of the
   * matched set of elements. This set is filtered with the expressions that
   * will cause only elements matching any of the selectors to be collected.
   */
  LazyGQuery children(String... filters);

  /**
   * Get a set of elements containing all of the unique immediate children of
   * each of the matched set of elements. Also note: while parents() will look
   * at all ancestors, children() will only consider immediate child elements.
   */
  LazyGQuery children();

  /**
   * Trigger a click event.
   */
  LazyGQuery click();

  /**
   * Triggers the click event of each matched element. Causes all of the
   * functions that have been bound to that click event to be executed.
   */
  LazyGQuery click(Function f);

  /**
   * Clone matched DOM Elements and select the clones. This is useful for moving
   * copies of the elements to another location in the DOM.
   */
  LazyGQuery clone();

  /**
   * Filter the set of elements to those that contain the specified text.
   */
  LazyGQuery contains(String text);

  /**
   * Find all the child nodes inside the matched elements (including text
   * nodes), or the content document, if the element is an iframe.
   */
  LazyGQuery contents();

  /**
   * Set a single style property to a value, on all matched elements using
   * type-safe overhead-free enumerations.
   *
   * @param property a CSS property type
   * @param value a legal value from the type T
   * @param <T> inferred from the CSS property type
   * @return
   */
//  <T> LazyGQuery css(CssProperty<T> property, T value);

  /**
   * Set a key/value object as style properties to all matched elements. This
   * serves as the best way to set a large number of style properties on all
   * matched elements.
   *
   * Example: $(".item").css(Properties.create("color: 'red', background:
   * 'blue'"))
   */
  LazyGQuery css(Properties properties);

  /**
   * Set a single style property to a value, on all matched elements.
   */
  LazyGQuery css(String prop, String val);

  /**
   * Trigger a double click event.
   */
  LazyGQuery dblclick();

  /**
   * Bind a function to the dblclick event of each matched element.
   */
  LazyGQuery dblclick(Function f);

  /**
   * Removes a queued function from the front of the queue and executes it.
   */
  LazyGQuery dequeue(String type);

  /**
   * Removes a queued function from the front of the FX queue and executes it.
   */
  LazyGQuery dequeue();

  /**
   * Run one or more Functions over each element of the GQuery.
   */
  LazyGQuery each(Function... f);

  /**
   * Remove all child nodes from the set of matched elements.
   */
  LazyGQuery empty();

  /**
   * Revert the most recent 'destructive' operation, changing the set of matched
   * elements to its previous state (right before the destructive operation).
   */
  LazyGQuery end();

  /**
   * Reduce GQuery to element in the specified position.
   */
  LazyGQuery eq(int pos);

  /**
   * Trigger an error event.
   */
  LazyGQuery error();

  /**
   * Bind a function to the error event of each matched element.
   */
  LazyGQuery error(Function f);

  /**
   * Fade in all matched elements by adjusting their opacity.
   */
  LazyGQuery fadeIn(int millisecs);

  /**
   * Fade in all matched elements by adjusting their opacity. The effect will
   * take 1000 milliseconds to complete
   */
  LazyGQuery fadeIn();

  /**
   * Fade out all matched elements by adjusting their opacity.
   */
  LazyGQuery fadeOut(int millisecs);

  /**
   * Fade out all matched elements by adjusting their opacity. The effect will
   * take 1000 milliseconds to complete
   */
  LazyGQuery fadeOut();

  /**
   * Removes all elements from the set of matched elements that do not match the
   * specified function. The function is called with a context equal to the
   * current element. If the function returns false, then the element is removed
   * - anything else and the element is kept.
   */
  LazyGQuery filter(Predicate filterFn);

  /**
   * Removes all elements from the set of matched elements that do not pass the
   * specified css expression. This method is used to narrow down the results of
   * a search. Provide a comma-separated list of expressions to apply multiple
   * filters at once.
   */
  LazyGQuery filter(String... filters);

  /**
   * Searches for all elements that match the specified css expression. This
   * method is a good way to find additional descendant elements with which to
   * process.
   *
   * Provide a comma-separated list of expressions to apply multiple filters at
   * once.
   */
  LazyGQuery find(String... filters);

  /**
   * Trigger a focus event.
   */
  LazyGQuery focus();

  /**
   * Bind a function to the focus event of each matched element.
   */

  LazyGQuery focus(Function f);

  /**
   * Return the previous set of matched elements prior to the last destructive
   * operation (e.g. query)
   */
  LazyGQuery getPreviousObject();

  /**
   * Returns true any of the specified classes are present on any of the matched
   * Reduce the set of matched elements to all elements after a given position.
   * The position of the element in the set of matched elements starts at 0 and
   * goes to length - 1.
   */
  LazyGQuery gt(int pos);

  /**
   * Set the height of every element in the matched set.
   */
  LazyGQuery height(int height);

  /**
   * Set the height style property of every matched element. It's useful for
   * using 'percent' or 'em' units Example: $(".a").width("100%")
   */
  LazyGQuery height(String height);

  /**
   * Make invisible all matched elements.
   */
  LazyGQuery hide();

  /**
   * Bind a function to the mouseover event of each matched element. A method
   * for simulating hovering (moving the mouse on, and off, an object). This is
   * a custom method which provides an 'in' to a frequent task. Whenever the
   * mouse cursor is moved over a matched element, the first specified function
   * is fired. Whenever the mouse moves off of the element, the second specified
   * function fires.
   */
  LazyGQuery hover(Function fover, Function fout);

  /**
   * Set the innerHTML of every matched element.
   */
  LazyGQuery html(String html);

  /**
   * Insert all of the matched elements after another, specified, set of
   * elements.
   */
  LazyGQuery insertAfter(String selector);

  /**
   * Insert all of the matched elements after another, specified, set of
   * elements.
   */
  LazyGQuery insertAfter(Element elem);

  /**
   * Insert all of the matched elements after another, specified, set of
   * elements.
   */
  LazyGQuery insertAfter(GQuery query);

  /**
   * Insert all of the matched elements before another, specified, set of
   * elements.
   *
   * The elements must already be inserted into the document (you can't insert
   * an element after another if it's not in the page).
   */
  LazyGQuery insertBefore(Element item);

  /**
   * Insert all of the matched elements before another, specified, set of
   * elements.
   *
   * The elements must already be inserted into the document (you can't insert
   * an element after another if it's not in the page).
   */
  LazyGQuery insertBefore(GQuery query);

  /**
   * Insert all of the matched elements before another, specified, set of
   * elements.
   *
   * The elements must already be inserted into the document (you can't insert
   * an element after another if it's not in the page).
   */
  LazyGQuery insertBefore(String selector);

  /**
   * Trigger a keydown event.
   */
  LazyGQuery keydown();

  /**
   * Trigger a keypress event.
   */
  LazyGQuery keypress();

  /**
   * Bind a function to the keypress event of each matched element.
   */
  LazyGQuery keypressed(Function f);

  /**
   * Trigger a keyup event.
   */
  LazyGQuery keyup();

  /**
   * Bind a function to the keyup event of each matched element.
   */
  LazyGQuery keyup(Function f);

  /**
   * Bind a function to the load event of each matched element.
   */
  LazyGQuery load(Function f);

  /**
   * Reduce the set of matched elements to all elements before a given position.
   * The position of the element in the set of matched elements starts at 0 and
   * goes to length - 1.
   */
  LazyGQuery lt(int pos);

  /**
   * Bind a function to the mousedown event of each matched element.
   */
  LazyGQuery mousedown(Function f);

  /**
   * Bind a function to the mousemove event of each matched element.
   */
  LazyGQuery mousemove(Function f);

  /**
   * Bind a function to the mouseout event of each matched element.
   */
  LazyGQuery mouseout(Function f);

  /**
   * Bind a function to the mouseover event of each matched element.
   */
  LazyGQuery mouseover(Function f);

  /**
   * Bind a function to the mouseup event of each matched element.
   */
  LazyGQuery mouseup(Function f);

  /**
   * Get a set of elements containing the unique next siblings of each of the
   * given set of elements. next only returns the very next sibling for each
   * element, not all next siblings see {#nextAll}.
   */
  LazyGQuery next();

  /**
   * Get a set of elements containing the unique next siblings of each of the
   * given set of elements filtered by 1 or more selectors. next only returns
   * the very next sibling for each element, not all next siblings see
   * {#nextAll}.
   */
  LazyGQuery next(String... selectors);

  /**
   * Find all sibling elements after the current element.
   */
  LazyGQuery nextAll();

  /**
   * Removes the specified Element from the set of matched elements. This method
   * is used to remove a single Element from a jQuery object.
   */
  LazyGQuery not(Element elem);

  /**
   * Removes any elements inside the passed set of elements from the set of
   * matched elements.
   */
  LazyGQuery not(GQuery gq);

  /**
   * Removes elements matching the specified expression from the set of matched
   * elements.
   */
  LazyGQuery not(String... filters);

  /**
   * Returns a GQuery collection with the positioned parent of the first matched
   * element. This is the first parent of the element that has position (as in
   * relative or absolute). This method only works with visible elements.
   */
  LazyGQuery offsetParent();

  /**
   * Binds a handler to a particular Event (like Event.ONCLICK) for each matched
   * element. The handler is executed only once for each element.
   *
   * The event handler is passed as a Function that you can use to prevent
   * default behaviour. To stop both default action and event bubbling, the
   * function event handler has to return false.
   *
   * You can pass an additional Object data to your Function as the second
   * parameter
   */
  LazyGQuery one(int eventbits, Object data, Function f);

  /**
   * Get a set of elements containing the unique parents of the matched set of
   * elements.
   */
  LazyGQuery parent();

  /**
   * Get a set of elements containing the unique parents of the matched set of
   * elements. You may use an optional expressions to filter the set of parent
   * elements that will match one of them.
   */
  LazyGQuery parent(String... filters);

  /**
   * Get a set of elements containing the unique ancestors of the matched set of
   * elements (except for the root element). The matched elements are filtered,
   * returning those that match any of the filters.
   */
  LazyGQuery parents(String... filters);

  /**
   * Get a set of elements containing the unique ancestors of the matched set of
   * elements (except for the root element).
   */
  LazyGQuery parents();

  /**
   * Prepend content to the inside of every matched element. This operation is
   * the best way to insert elements inside, at the beginning, of all matched
   * elements.
   */
  LazyGQuery prepend(String html);

  /**
   * Prepend content to the inside of every matched element. This operation is
   * the best way to insert elements inside, at the beginning, of all matched
   * elements.
   */
  LazyGQuery prepend(GQuery query);

  /**
   * Prepend content to the inside of every matched element. This operation is
   * the best way to insert elements inside, at the beginning, of all matched
   * elements.
   */
  LazyGQuery prepend(Node n);

  /**
   * Prepend all of the matched elements to another, specified, set of elements.
   * This operation is, essentially, the reverse of doing a regular
   * $(A).prepend(B), in that instead of prepending B to A, you're prepending A
   * to B.
   */
  LazyGQuery prependTo(GQuery elms);

  /**
   * Get a set of elements containing the unique previous siblings of each of
   * the matched set of elements. Only the immediately previous sibling is
   * returned, not all previous siblings.
   */
  LazyGQuery prev();

  /**
   * Get a set of elements containing the unique previous siblings of each of
   * the matched set of elements filtered by selector. Only the immediately
   * previous sibling is returned, not all previous siblings.
   */
  LazyGQuery prev(String... selectors);

  /**
   * Find all sibling elements in front of the current element.
   */
  LazyGQuery prevAll();

  /**
   * Adds a new function, to be executed, onto the end of the queue of all
   * matched elements.
   */
  LazyGQuery queue(String type, Function data);

  /**
   * Adds a new function, to be executed, onto the end of the queue of all
   * matched elements in the FX queue.
   */
  LazyGQuery queue(Function data);

  /**
   * Removes all matched elements from the DOM.
   */
  LazyGQuery remove();

  /**
   * Remove the named attribute from every element in the matched set.
   */
  LazyGQuery removeAttr(String key);

  /**
   * Removes the specified classes to each matched element.
   */
  LazyGQuery removeClass(String... classes);

  /**
   * Removes named data store from an element.
   */
  LazyGQuery removeData(String name);

  /**
   * Replaces the elements matched by the specified selector with the matched
   * elements. This function is the complement to replaceWith() which does the
   * same task with the parameters reversed.
   */
  LazyGQuery replaceAll(GQuery query);

  /**
   * Replaces the elements matched by the specified selector with the matched
   * elements. This function is the complement to replaceWith() which does the
   * same task with the parameters reversed.
   */
  LazyGQuery replaceAll(String html);

  /**
   * Replaces the elements matched by the specified selector with the matched
   * elements. This function is the complement to replaceWith() which does the
   * same task with the parameters reversed.
   */
  LazyGQuery replaceAll(Element elem);

  /**
   * Replaces all matched elements with the specified HTML or DOM elements. This
   * returns the GQuery element that was just replaced, which has been removed
   * from the DOM.
   */
  LazyGQuery replaceWith(GQuery query);

  /**
   * Replaces all matched elements with the specified HTML or DOM elements. This
   * returns the GQuery element that was just replaced, which has been removed
   * from the DOM.
   */
  LazyGQuery replaceWith(String html);

  /**
   * Replaces all matched elements with the specified HTML or DOM elements. This
   * returns the GQuery element that was just replaced, which has been removed
   * from the DOM.
   */
  LazyGQuery replaceWith(Element elem);

  /**
   * Bind a function to the scroll event of each matched element.
   */
  LazyGQuery scroll(Function f);

  /**
   * When a value is passed in, the scroll left offset is set to that value on
   * all matched elements. This method works for both visible and hidden
   * elements.
   */
  LazyGQuery scrollLeft(int left);

  /**
   * Gets the scroll left offset of the first matched element. This method works
   * for both visible and hidden elements.
   */
  int scrollLeft();

  /**
   * When a value is passed in, the scroll top offset is set to that value on
   * all matched elements. This method works for both visible and hidden
   * elements.
   */
  LazyGQuery scrollTop(int top);

  /**
   * Gets the scroll top offset of the first matched element. This method works
   * for both visible and hidden elements.
   */
  int scrollTop();

  LazyGQuery select();

  /**
   * Return the number of elements in the matched set. Make visible all mached
   * elements
   */
  LazyGQuery show();

  /**
   * Get a set of elements containing all of the unique siblings of each of the
   * matched set of elements.
   */
  LazyGQuery siblings();

  /**
   * Get a set of elements containing all of the unique siblings of each of the
   * matched set of elements filtered by the provided set of selectors.
   */
  LazyGQuery siblings(String... selectors);

  /**
   * Selects a subset of the matched elements.
   */
  LazyGQuery slice(int start, int end);

  LazyGQuery submit();

  /**
   * Set the innerText of every matched element.
   */
  LazyGQuery text(String txt);

  /**
   * Toggle among two or more function calls every other click.
   */
  LazyGQuery toggle(Function... fn);

  /**
   * Adds or removes the specified classes to each matched element.
   */
  LazyGQuery toggleClass(String... classes);

  /**
   * Adds or removes the specified classes to each matched element.
   */
  LazyGQuery toggleClass(String clz, boolean sw);

  /**
   * Trigger an event of type eventbits on every matched element.
   */
  LazyGQuery trigger(int eventbits, int... keys);

  /**
   * Removes all events that match the eventbits.
   */
  LazyGQuery unbind(int eventbits);

  /**
   * Sets the value attribute of every matched element In the case of multivalue
   * elements, all values are setted for other elements, only the first value is
   * considered.
   */
  LazyGQuery val(String... values);

  /**
   * Set the width of every matched element.
   */
  LazyGQuery width(int width);

  /**
   * Wrap each matched element with the specified HTML content. This wrapping
   * process is most useful for injecting additional structure into a document,
   * without ruining the original semantic qualities of a document. This works
   * by going through the first element provided (which is generated, on the
   * fly, from the provided HTML) and finds the deepest descendant element
   * within its structure -- it is that element that will enwrap everything
   * else.
   */
  LazyGQuery wrap(GQuery query);

  /**
   * Wrap each matched element with the specified HTML content. This wrapping
   * process is most useful for injecting additional structure into a document,
   * without ruining the original semantic qualities of a document. This works
   * by going through the first element provided (which is generated, on the
   * fly, from the provided HTML) and finds the deepest descendant element
   * within its structure -- it is that element that will enwrap everything
   * else.
   */
  LazyGQuery wrap(Element elem);

  /**
   * Wrap each matched element with the specified HTML content. This wrapping
   * process is most useful for injecting additional structure into a document,
   * without ruining the original semantic qualities of a document. This works
   * by going through the first element provided (which is generated, on the
   * fly, from the provided HTML) and finds the deepest descendant element
   * within its structure -- it is that element that will enwrap everything
   * else.
   */
  LazyGQuery wrap(String html);

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
  LazyGQuery wrapAll(String html);

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
  LazyGQuery wrapAll(Element elem);

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
  LazyGQuery wrapAll(GQuery query);

  /**
   * Wrap the inner child contents of each matched element (including text
   * nodes) with an HTML structure. This wrapping process is most useful for
   * injecting additional structure into a document, without ruining the
   * original semantic qualities of a document. This works by going through the
   * first element provided (which is generated, on the fly, from the provided
   * HTML) and finds the deepest ancestor element within its structure -- it is
   * that element that will enwrap everything else.
   */
  LazyGQuery wrapInner(GQuery query);

  /**
   * Wrap the inner child contents of each matched element (including text
   * nodes) with an HTML structure. This wrapping process is most useful for
   * injecting additional structure into a document, without ruining the
   * original semantic qualities of a document. This works by going through the
   * first element provided (which is generated, on the fly, from the provided
   * HTML) and finds the deepest ancestor element within its structure -- it is
   * that element that will enwrap everything else.
   */
  LazyGQuery wrapInner(String html);

  /**
   * Wrap the inner child contents of each matched element (including text
   * nodes) with an HTML structure. This wrapping process is most useful for
   * injecting additional structure into a document, without ruining the
   * original semantic qualities of a document. This works by going through the
   * first element provided (which is generated, on the fly, from the provided
   * HTML) and finds the deepest ancestor element within its structure -- it is
   * that element that will enwrap everything else.
   */
  LazyGQuery wrapInner(Element elem);
}
