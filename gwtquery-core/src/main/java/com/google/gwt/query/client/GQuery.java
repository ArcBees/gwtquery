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
package com.google.gwt.query.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.dom.client.BodyElement;
import com.google.gwt.dom.client.ButtonElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.IFrameElement;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.OptionElement;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.TextAreaElement;
import static com.google.gwt.query.client.Effects.Effects;
import static com.google.gwt.query.client.Events.Events;
import com.google.gwt.query.client.css.CssProperty;
import com.google.gwt.query.client.css.Length;
import com.google.gwt.query.client.css.Percentage;
import com.google.gwt.query.client.css.TakesLength;
import com.google.gwt.query.client.css.TakesPercentage;
import com.google.gwt.query.client.impl.DocumentStyleImpl;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;

/**
 * Gwt Query is a GWT clone of the popular jQuery library.
 */
public class GQuery implements Lazy<GQuery, LazyGQuery> {

  /**
   * A POJO used to store the top/left CSS positioning values of an element.
   */
  public static class Offset {

    public int top;

    public int left;

    Offset(int left, int top) {
      this.left = left;
      this.top = top;
    }
  }

  protected static final class DataCache extends JavaScriptObject {

    protected DataCache() {
    }

    public native void delete(String name) /*-{
      delete this[name];
    }-*/;

    public native void delete(int name) /*-{
      delete this[name];
    }-*/;

    public native boolean exists(int id) /*-{
      return !!this[id];
    }-*/;

    public native JavaScriptObject get(String id) /*-{
      return this[id];
    }-*/;

    public native JavaScriptObject get(int id) /*-{
      return this[id];
    }-*/;

    public DataCache getCache(int id) {
      return get(id).cast();
    }

    public native double getDouble(String id) /*-{
      return this[id];
    }-*/;

    public native double getDouble(int id) /*-{
      return this[id];
    }-*/;

    public native int getInt(String id) /*-{
      return this[id];
    }-*/;

    public native int getInt(int id) /*-{
      return this[id];
    }-*/;

    public native Object getObject(String id) /*-{
          return this[id];
        }-*/;

    public native String getString(String id) /*-{
      return this[id];
    }-*/;

    public native String getString(int id) /*-{
      return this[id];
    }-*/;

    public native boolean isEmpty() /*-{
        var foo = "";
        for(foo in this) break;
        return !foo;
    }-*/;

    public native void put(String id, Object obj) /*-{
      this[id]=obj;
    }-*/;

    public native void put(int id, Object obj) /*-{
      this[id]=obj;
    }-*/;
  }

  private static final class FastSet extends JavaScriptObject {

    public static FastSet create() {
      return JavaScriptObject.createObject().cast();
    }

    protected FastSet() {
    }

    public void add(Object o) {
      add0(o.hashCode());
    }

    public boolean contains(Object o) {
      return contains0(o.hashCode());
    }

    public void remove(Object o) {
      remove0(o.hashCode());
    }

    private native void add0(int hc) /*-{
      this[hc]=true;
    }-*/;

    private native boolean contains0(int hc) /*-{
      return this[hc] || false;
    }-*/;

    private native void remove0(int hc) /*-{
      delete this[hc];
    }-*/;
  }

  private static final class Queue<T> extends JavaScriptObject {

    public static Queue newInstance() {
      return createArray().cast();
    }

    protected Queue() {
    }

    public native T dequeue() /*-{
       return this.shift();
    }-*/;

    public native void enqueue(T foo) /*-{
       this.push(foo);
     }-*/;

    public native int length() /*-{
       return this.length;
    }-*/;

    public native T peek(int i) /*-{
      return this[i];
    }-*/;
  }

  public static boolean fxOff = false;

  public static Class<GQuery> GQUERY = GQuery.class;

  private static JsMap<Class<? extends GQuery>, Plugin<? extends GQuery>>
      plugins;

  private static Element windowData = null;

  private static DataCache dataCache = null;

  private static DocumentStyleImpl styleImpl;

  private static final int FUNC_PREPEND = 0, FUNC_APPEND = 1, FUNC_AFTER = 2,
      FUNC_BEFORE = 3;

  /**
   * Create an empty GQuery object.
   */
  public static GQuery $() {
    return new GQuery(JSArray.create());
  }

  /**
   * This function accepts a string containing a CSS selector which is then used
   * to match a set of elements, or it accepts raw HTML creating a GQuery
   * element containing those elements.
   */
  public static GQuery $(String selectorOrHtml) {
    if (selectorOrHtml.trim().charAt(0) == '<') {
      return innerHtml(selectorOrHtml);
    }
    return $(selectorOrHtml, Document.get());
  }

  public static <T extends GQuery> T $(T gq) {
    return gq;
  }

  /**
   * This function accepts a string containing a CSS selector which is then used
   * to match a set of elements, or it accepts raw HTML creating a GQuery
   * element containing those elements. The second parameter is is a class
   * reference to a plugin to be used.
   */
  public static <T extends GQuery> T $(String selector, Class<T> plugin) {
    try {
      if (plugins != null) {
        T gquery = (T) plugins.get(plugin).init($(selector, Document.get()));
        return gquery;
      }
      throw new RuntimeException("No plugin for class " + plugin);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * This function accepts a string containing a CSS selector which is then used
   * to match a set of elements, or it accepts raw HTML creating a GQuery
   * element containing those elements. The second parameter is the context to
   * use for the selector.
   */
  public static GQuery $(String selector, Node context) {
    return new GQuery(select(selector, context));
  }

  /**
   * This function accepts a string containing a CSS selector which is then used
   * to match a set of elements, or it accepts raw HTML creating a GQuery
   * element containing those elements. The second parameter is the context to
   * use for the selector. The third parameter is the class plugin to use.
   */
  public static <T extends GQuery> GQuery $(String selector, Node context,
      Class<T> plugin) {
    try {
      if (plugins != null) {
        T gquery = (T) plugins.get(plugin).
            init(new GQuery(select(selector, context)));
        return gquery;
      }
      throw new RuntimeException("No plugin for class " + plugin);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Wrap a GQuery around  existing Elements.
   */
  public static GQuery $(NodeList<Element> elements) {
    return new GQuery(elements);
  }

  /**
   * Wrap a GQuery around an existing element.
   */
  public static GQuery $(Element element) {
    JSArray a = JSArray.create();
    a.addNode(element);
    return new GQuery(a);
  }

  /**
   * Wrap a GQuery around an event's target element.
   */
  public static GQuery $(Event event) {
    return $(event.getCurrentTarget());
  }

  /**
   * Wrap a JSON object.
   */
  public static Properties $$(String properties) {
    return Properties.create(properties);
  }

  public static <T extends Node> T[] asArray(NodeList<T> nl) {
    if (GWT.isScript()) {
      return reinterpretCast(nl);
    } else {
      Node[] elts = new Node[nl.getLength()];
      for (int i = 0; i < elts.length; i++) {
        elts[i] = nl.getItem(i);
      }
      return (T[]) elts;
    }
  }

  public static native String camelize(String s)/*-{
     return s.replace(/\-(\w)/g, function(all, letter){
				return letter.toUpperCase();
			});
   }-*/;

  public static String curCSS(Element elem, String name, boolean force) {
    Style s = elem.getStyle();
    ensureStyleImpl();
    if (!force) {
      name = styleImpl.getPropertyName(name);

      if (SelectorEngine.truth(s.getProperty(name))) {
        return s.getProperty(name);
      }
      return name.equals("opacity") ? "1" : "";
    } else {
      return styleImpl.getCurrentStyle(elem, name);
    }
  }

  /**
   * Return a lazy version of the GQuery interface. Lazy function calls are
   * simply queued up and not executed immediately.
   */
  public static LazyGQuery lazy() {
    return GQuery.$().createLazy();
  }

  public static void registerPlugin(Class<? extends GQuery> plugin,
      Plugin<? extends GQuery> pluginFactory) {
    if (plugins == null) {
      plugins = JsMap.createObject().cast();
    }
    plugins.put(plugin, pluginFactory);
  }

  protected static String[] jsArrayToString(JsArrayString array) {
    if (GWT.isScript()) {
      return jsArrayToString0(array);
    } else {
      String result[] = new String[array.length()];
      for (int i = 0; i < result.length; i++) {
        result[i] = array.get(i);
      }
      return result;
    }
  }

  /**
   * Copied from UIObject.
   */
  protected static void setStyleName(Element elem, String style, boolean add) {

    style = style.trim();

    // Get the current style string.
    String oldStyle = elem.getClassName();
    int idx = oldStyle.indexOf(style);

    // Calculate matching index.
    while (idx != -1) {
      if (idx == 0 || oldStyle.charAt(idx - 1) == ' ') {
        int last = idx + style.length();
        int lastPos = oldStyle.length();
        if ((last == lastPos) || ((last < lastPos) && (oldStyle.charAt(last)
            == ' '))) {
          break;
        }
      }
      idx = oldStyle.indexOf(style, idx + 1);
    }

    if (add) {
      // Only add the style if it's not already present.
      if (idx == -1) {
        if (oldStyle.length() > 0) {
          oldStyle += " ";
        }
        DOM.setElementProperty(elem.<com.google.gwt.user.client.Element>cast(),
            "className", oldStyle + style);
      }
    } else {
      // Don't try to remove the style if it's not there.
      if (idx != -1) {
        // Get the leading and trailing parts, without the removed name.
        String begin = oldStyle.substring(0, idx).trim();
        String end = oldStyle.substring(idx + style.length()).trim();

        // Some contortions to make sure we don't leave extra spaces.
        String newClassName;
        if (begin.length() == 0) {
          newClassName = end;
        } else if (end.length() == 0) {
          newClassName = begin;
        } else {
          newClassName = begin + " " + end;
        }

        DOM.setElementProperty(elem.<com.google.gwt.user.client.Element>cast(),
            "className", newClassName);
      }
    }
  }

  protected static void setStyleProperty(String prop, String val, Element e) {
    String property = camelize(prop);
    e.getStyle().setProperty(property, val);
    if ("opacity".equals(property)) {
      e.getStyle().setProperty("zoom", "1");
      e.getStyle().setProperty("filter",
          "alpha(opacity=" + (int) (Double.valueOf(val) * 100) + ")");
    }
  }

  private static JSArray copyNodeList(NodeList n) {
    JSArray res = JSArray.create();
    for (int i = 0; i < n.getLength(); i++) {
      res.addNode(n.getItem(i));
    }
    return res;
  }

  private static String curCSS(Element elem, String name) {
    return curCSS(elem, name, false);
  }

  private static void ensureStyleImpl() {
    if (styleImpl == null) {
      styleImpl = GWT.create(DocumentStyleImpl.class);
    }
  }

  private static boolean hasClass(Element e, String clz) {
    return ((" " + e.getClassName() + " ").matches(".*\\s" + clz + "\\s.*"));
  }

  private static GQuery innerHtml(String html) {
    Element div = DOM.createDiv();
    div.setInnerHTML(html);
    return new GQuery(
        copyNodeList((NodeList<Element>) (NodeList<?>) div.getChildNodes()));
  }

  private static native String[] jsArrayToString0(JsArrayString array) /*-{
    return array;
  }-*/;

  private static native <T extends Node> T[] reinterpretCast(NodeList<T> nl) /*-{
        return nl;
    }-*/;

  private static NodeList select(String selector, Node context) {
    NodeList n = new SelectorEngine().select(selector, context);
    JSArray res = copyNodeList(n);
    return res;
  }

  protected NodeList<Element> elements = null;

  private String currentSelector;

  private GQuery previousObject;

  public GQuery() {
    elements = JavaScriptObject.createArray().cast();
  }

  public GQuery(NodeList<Element> list) {
    elements = list;
  }

  public GQuery(JSArray elements) {
    this.elements = elements;
  }

  public GQuery(Element element) {
    elements = JSArray.create(element);
  }

  /**
   * Add elements to the set of matched elements if they are not included yet.
   */

  public GQuery add(String selector) {
    return add($(selector));
  }

  /**
   * Adds the specified classes to each matched element. Add elements to the set
   * of matched elements if they are not included yet.
   */
  public GQuery add(GQuery previousObject) {
    return pushStack(unique(merge(elements, previousObject.elements)), "add",
        getSelector() + "," + previousObject.getSelector());
  }

  /**
   * Adds the specified classes to each matched element.
   */
  public GQuery addClass(String... classes) {
    for (Element e : elements()) {
      for (String clz : classes) {
        setStyleName(e, clz, true);
      }
    }
    return this;
  }

  /**
   * Insert content after each of the matched elements. The elements must
   * already be inserted into the document (you can't insert an element after
   * another if it's not in the page).
   */
  public GQuery after(Node n) {
    return domManip(JSArray.create(n), FUNC_AFTER);
  }

  /**
   * Insert content after each of the matched elements. The elements must
   * already be inserted into the document (you can't insert an element after
   * another if it's not in the page).
   */
  public GQuery after(String html) {
    return domManip(html, FUNC_AFTER);
  }

  /**
   * Insert content after each of the matched elements. The elements must
   * already be inserted into the document (you can't insert an element after
   * another if it's not in the page).
   */
  public GQuery after(GQuery query) {
    return domManip(query.elements, FUNC_AFTER);
  }

  /**
   * Add the previous selection to the current selection. Useful for traversing
   * elements, and then adding something that was matched before the last
   * traversal.
   */
  public GQuery andSelf() {
    return add(previousObject);
  }

  /**
   * Append content to the inside of every matched element. This operation is
   * similar to doing an appendChild to all the specified elements, adding them
   * into the document.
   */
  public GQuery append(String html) {
    return domManip(html, FUNC_APPEND);
  }

  /**
   * Append content to the inside of every matched element. This operation is
   * similar to doing an appendChild to all the specified elements, adding them
   * into the document.
   */
  public GQuery append(Node n) {
    return domManip(JSArray.create(n), FUNC_APPEND);
  }

  /**
   * Append content to the inside of every matched element. This operation is
   * similar to doing an appendChild to all the specified elements, adding them
   * into the document.
   */
  public GQuery append(GQuery query) {
    return domManip(query.elements, FUNC_APPEND);
  }

  /**
   * Append all of the matched elements to another, specified, set of elements.
   * This operation is, essentially, the reverse of doing a regular
   * $(A).append(B), in that instead of appending B to A, you're appending A to
   * B.
   */
  public GQuery appendTo(GQuery other) {
    return other.append(this);
  }

  /**
   * Convert to Plugin interface provided by Class literal.
   */
  public <T extends GQuery> T as(Class<T> plugin) {
    // GQuery is not a plugin for itself
    if (plugin == GQUERY) {
      return (T) $(this);
    } else if (plugins != null) {
      return (T) plugins.get(plugin).init(this);
    }
    throw new RuntimeException("No plugin registered for class " + plugin);
  }

  /**
   * Access a property on the first matched element. This method makes it easy
   * to retrieve a property value from the first matched element. If the element
   * does not have an attribute with such a name, undefined is returned.
   * Attributes include title, alt, src, href, width, style, etc.
   */
  public String attr(String name) {
    return elements.getItem(0).getAttribute(fixAttributeName(name));
  }

  /**
   * Set a single property to a value, on all matched elements.
   */
  public GQuery attr(String key, String value) {
    key = fixAttributeName(key);
    for (Element e : elements()) {
      e.setAttribute(key, value);
    }
    return this;
  }

  /**
   * Set a key/value object as properties to all matched elements.
   *
   * Example: $("img").attr(new Properties("src: 'test.jpg', alt: 'Test
   * Image'"))
   */
  public GQuery attr(Properties properties) {
    for (Element e : elements()) {
      for (String name : properties.keys()) {
        e.setAttribute(fixAttributeName(name), properties.get(name));
      }
    }
    return this;
  }

  /**
   * Set a single property to a computed value, on all matched elements.
   */
  public GQuery attr(String key, Function closure) {
    for (int i = 0; i < elements.getLength(); i++) {
      Element e = elements.getItem(i);
      e.setAttribute(fixAttributeName(key), closure.f(e, i));
    }
    return this;
  }

  /**
   * Insert content before each of the matched elements. The elements must
   * already be inserted into the document (you can't insert an element before
   * another if it's not in the page).
   */
  public GQuery before(Node n) {
    return domManip(JSArray.create(n), FUNC_BEFORE);
  }

  /**
   * Insert content before each of the matched elements. The elements must
   * already be inserted into the document (you can't insert an element before
   * another if it's not in the page).
   */
  public GQuery before(GQuery query) {
    return domManip(query.elements, FUNC_BEFORE);
  }

  /**
   * Insert content before each of the matched elements. The elements must
   * already be inserted into the document (you can't insert an element before
   * another if it's not in the page).
   */
  public GQuery before(String html) {
    return domManip(html, FUNC_BEFORE);
  }

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
  public GQuery bind(int eventbits, final Object data, final Function f) {
    return as(Events).bind(eventbits, data, f);
  }

  /**
   * Bind a function to the blur event of each matched element.
   */
  public GQuery blur(Function f) {
    return bind(Event.ONBLUR, null, f);
  }

  /**
   * Trigger a blur event.
   */
  public GQuery blur() {
    return trigger(Document.get().createBlurEvent(), null);
  }

  /**
   * Bind a function to the change event of each matched element.
   */
  public GQuery change(Function f) {
    return bind(Event.ONCHANGE, null, f);
  }

  /**
   * Trigger a change event.
   */
  public GQuery change() {
    return trigger(Document.get().createChangeEvent(), null);
  }

  /**
   * Get a set of elements containing all of the unique children of each of the
   * matched set of elements. This set is filtered with the expressions that
   * will cause only elements matching any of the selectors to be collected.
   */
  public GQuery children(String... filters) {
    return find(filters);
  }

  /**
   * Get a set of elements containing all of the unique immediate children of
   * each of the matched set of elements. Also note: while parents() will look
   * at all ancestors, children() will only consider immediate child elements.
   */
  public GQuery children() {
    JSArray result = JSArray.create();
    for (Element e : elements()) {
      allNextSiblingElements(e.getFirstChildElement(), result, null);
    }
    return new GQuery(unique(result));
  }

  /**
   * Trigger a click event.
   */
  public GQuery click() {
    return trigger(
        Document.get().createClickEvent(0, 0, 0, 0, 0, false, false, false,
            false), null);
  }

  /**
   * Triggers the click event of each matched element. Causes all of the
   * functions that have been bound to that click event to be executed.
   */
  public GQuery click(final Function f) {
    return bind(Event.ONCLICK, null, f);
  }

  /**
   * Clone matched DOM Elements and select the clones. This is useful for moving
   * copies of the elements to another location in the DOM.
   */
  public GQuery clone() {
    JSArray result = JSArray.create();
    for (Element e : elements()) {
      result.addNode(e.cloneNode(true));
    }
    return new GQuery(result);
  }

  /**
   * Filter the set of elements to those that contain the specified text.
   */
  public GQuery contains(String text) {
    JSArray array = JSArray.create();
    for (Element e : elements()) {
      if ($(e).text().contains(text)) {
        array.addNode(e);
      }
    }
    return $(array);
  }

  /**
   * Find all the child nodes inside the matched elements (including text
   * nodes), or the content document, if the element is an iframe.
   */
  public GQuery contents() {
    JSArray result = JSArray.create();
    for (Element e : elements()) {
      NodeList children = e.getChildNodes();
      for (int i = 0; i < children.getLength(); i++) {
        Node n = children.getItem(i);
        if (IFrameElement.is(n)) {
          result.addNode(getContentDocument(n));
        } else {
          result.addNode(n);
        }
      }
    }
    return new GQuery(unique(result));
  }

  public LazyGQuery createLazy() {
    return GWT.create(GQuery.class);
  }

  /**
   * Set CSS property on every matched element using type-safe enumerations.
   */
  public <S, T extends CssProperty<S>> GQuery css(T cssProperty, S value) {
    for (Element e : elements()) {
      cssProperty.set(e.getStyle(), value);
    }
    return this;
  }

  /**
   * Set CSS property on every matched element using type-safe enumerations.
   */
  public GQuery css(TakesLength cssProperty, Length value) {
    for (Element e : elements()) {
      cssProperty.setLength(e.getStyle(), value);
    }
    return this;
  }

  /**
   * Set CSS property on every matched element using type-safe enumerations.
   */
  public GQuery css(TakesPercentage cssProperty, Percentage value) {
    for (Element e : elements()) {
      cssProperty.setPercentage(e.getStyle(), value);
    }
    return this;
  }

  /**
   * Return a style property on the first matched element.
   */
  public String css(String name) {
    return curCSS(get(0), name);
  }

  /**
   * Set a key/value object as style properties to all matched elements. This
   * serves as the best way to set a large number of style properties on all
   * matched elements.
   *
   * Example: $(".item").css(Properties.create("color: 'red', background:
   * 'blue'"))
   */
  public GQuery css(Properties properties) {
    for (String property : properties.keys()) {
      css(property, properties.get(property));
    }
    return this;
  }

  /**
   * Set a single style property to a value, on all matched elements.
   */
  public GQuery css(String prop, String val) {
    for (Element e : elements()) {
      setStyleProperty(prop, val, e);
    }
    return this;
  }

  /**
   * Returns value at named data store for the element, as set by data(name,
   * value).
   */
  public Object data(String name) {
    return data(elements.getItem(0), name, null);
  }

  /**
   * Returns value at named data store for the element, as set by data(name,
   * value) with desired return type.
   *
   * @param clz return type class literal
   */
  public <T> T data(String name, Class<T> clz) {
    return (T) data(elements.getItem(0), name, null);
  }

  /**
   * Stores the value in the named spot with desired return type.
   */
  public Object data(String name, Object value) {
    for (Element e : elements()) {
      data(e, name, value);
    }
    return this;
  }

  /**
   * Trigger a double click event.
   */
  public GQuery dblclick() {
    return trigger(
        Document.get().createDblClickEvent(0, 0, 0, 0, 0, false, false, false,
            false), null);
  }

  /**
   * Bind a function to the dblclick event of each matched element.
   */
  public GQuery dblclick(Function f) {
    return bind(Event.ONDBLCLICK, null, f);
  }

  /**
   * Removes a queued function from the front of the queue and executes it.
   */
  public GQuery dequeue(String type) {
    for (Element e : elements()) {
      dequeue(e, type);
    }
    return this;
  }

  /**
   * Removes a queued function from the front of the FX queue and executes it.
   */
  public GQuery dequeue() {
    return dequeue("__FX");
  }

  /**
   * Run one or more Functions over each element of the GQuery.
   */
  public GQuery each(Function... f) {
    for (Function f1 : f) {
      for (Element e : elements()) {
        f1.f(e);
      }
    }
    return this;
  }

  /**
   * Returns the working set of nodes as a Java array. <b>Do NOT</b attempt to
   * modify this array, e.g. assign to its elements, or call Arrays.sort()
   */
  public Element[] elements() {
    return asArray(elements);
  }

  /**
   * Remove all child nodes from the set of matched elements.
   */
  public GQuery empty() {
    // TODO: add memory leak cleanup, remove event handlers, and 
    // data caches
    for (Element e : elements()) {
      while (e.getFirstChild() != null) {
        e.removeChild(e.getFirstChild());
      }
    }

    return this;
  }

  /**
   * Revert the most recent 'destructive' operation, changing the set of matched
   * elements to its previous state (right before the destructive operation).
   */
  public GQuery end() {
    return previousObject != null ? previousObject : new GQuery();
  }

  /**
   * Reduce GQuery to element in the specified position.
   */
  public GQuery eq(int pos) {
    return $(elements.getItem(pos));
  }

  /**
   * Trigger an error event.
   */
  public GQuery error() {
    return trigger(Document.get().createErrorEvent(), null);
  }

  /**
   * Bind a function to the error event of each matched element.
   */
  public GQuery error(Function f) {
    return bind(Event.ONERROR, null, f);
  }

  /**
   * Fade in all matched elements by adjusting their opacity.
   */
  public GQuery fadeIn(int millisecs) {
    return $(as(Effects).fadeIn(millisecs));
  }

  /**
   * Fade in all matched elements by adjusting their opacity. The effect will
   * take 1000 milliseconds to complete
   */
  public GQuery fadeIn() {
    return $(as(Effects).fadeIn());
  }

  /**
   * Fade out all matched elements by adjusting their opacity.
   */
  public GQuery fadeOut(int millisecs) {
    return as(Effects).fadeOut(millisecs);
  }

  /**
   * Fade out all matched elements by adjusting their opacity. The effect will
   * take 1000 milliseconds to complete
   */
  public GQuery fadeOut() {
    return $(as(Effects).fadeOut());
  }

  /**
   * Removes all elements from the set of matched elements that do not match the
   * specified function. The function is called with a context equal to the
   * current element. If the function returns false, then the element is removed
   * - anything else and the element is kept.
   */
  public GQuery filter(Predicate filterFn) {
    JSArray result = JSArray.create();
    for (int i = 0; i < elements.getLength(); i++) {
      Element e = elements.getItem(i);
      if (filterFn.f(e, i)) {
        result.addNode(e);
      }
    }
    return pushStack(result, "filter", currentSelector);
  }

  /**
   * Removes all elements from the set of matched elements that do not pass the
   * specified css expression. This method is used to narrow down the results of
   * a search. Provide a comma-separated list of expressions to apply multiple
   * filters at once.
   */
  public GQuery filter(String... filters) {
    JSArray array = JSArray.create();
    for (String f : filters) {
      for (Element e : elements()) {
        for (Element c : $(f, e.getParentNode()).elements()) {
          if (c == e) {
            array.addNode(c);
            break;
          }
        }
      }
    }
    return pushStack(unique(array), "filter", filters[0]);
  }

  /**
   * Searches for all elements that match the specified css expression. This
   * method is a good way to find additional descendant elements with which to
   * process.
   *
   * Provide a comma-separated list of expressions to apply multiple filters at
   * once.
   */
  public GQuery find(String... filters) {
    JSArray array = JSArray.create();
    for (String selector : filters) {
      for (Element e : elements()) {
        for (Element c : $(selector, e).elements()) {
          array.addNode(c);
        }
      }
    }
    return pushStack(unique(array), "find", filters[0]);
  }

  /**
   * Trigger a focus event.
   */
  public GQuery focus() {
    return trigger(Document.get().createFocusEvent(), null);
  }

  /**
   * Bind a function to the focus event of each matched element.
   */

  public GQuery focus(Function f) {
    return bind(Event.ONFOCUS, null, f);
  }

  /**
   * Return all elements matched in the GQuery as a NodeList. @see #elements()
   * for a method which returns them as an immutable Java array.
   */
  public NodeList<Element> get() {
    return elements;
  }

  /**
   * Return the ith element matched.
   */
  public Element get(int i) {
    return elements.getItem(i);
  }

  /**
   * Return the previous set of matched elements prior to the last destructive
   * operation (e.g. query)
   */
  public GQuery getPreviousObject() {
    return previousObject;
  }

  /**
   * Return the selector representing the current set of matched elements.
   */
  public String getSelector() {
    return currentSelector;
  }

  /**
   * Returns true any of the specified classes are present on any of the matched
   * Reduce the set of matched elements to all elements after a given position.
   * The position of the element in the set of matched elements starts at 0 and
   * goes to length - 1.
   */
  public GQuery gt(int pos) {
    return $(slice(pos + 1, -1));
  }

  /**
   * Returns true any of the specified classes are present on any of the matched
   * elements.
   */
  public boolean hasClass(String... classes) {
    for (Element e : elements()) {
      for (String clz : classes) {
        if (hasClass(e, clz)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Set the height of every element in the matched set.
   */
  public GQuery height(int height) {
    for (Element e : elements()) {
      e.getStyle().setPropertyPx("height", height);
    }
    return this;
  }

  /**
   * Set the height style property of every matched element. It's useful for
   * using 'percent' or 'em' units Example: $(".a").width("100%")
   */
  public GQuery height(String height) {
    return css("height", height);
  }

  /**
   * Get the current computed, pixel, height of the first matched element.
   */
  public int height() {
    return DOM.
        getElementPropertyInt((com.google.gwt.user.client.Element) get(0),
            "offsetHeight");
  }

  /**
   * Make invisible all matched elements.
   */
  public GQuery hide() {
    return $(as(Effects).hide());
  }

  /**
   * Bind a function to the mouseover event of each matched element. A method
   * for simulating hovering (moving the mouse on, and off, an object). This is
   * a custom method which provides an 'in' to a frequent task. Whenever the
   * mouse cursor is moved over a matched element, the first specified function
   * is fired. Whenever the mouse moves off of the element, the second specified
   * function fires.
   */
  public GQuery hover(Function fover, Function fout) {
    return bind(Event.ONMOUSEOVER, null, fover).
        bind(Event.ONMOUSEOUT, null, fout);
  }

  /**
   * Get the innerHTML of the first matched element.
   */
  public String html() {
    return get(0).getInnerHTML();
  }

  /**
   * Set the innerHTML of every matched element.
   */
  public GQuery html(String html) {
    for (Element e : elements()) {
      e.setInnerHTML(html);
    }
    return this;
  }

  /**
   * Find the index of the specified Element.
   */
  public int index(Element element) {
    for (int i = 0; i < elements.getLength(); i++) {
      if (elements.getItem(i) == element) {
        return i;
      }
    }
    return -1;
  }

  /**
   * Insert all of the matched elements after another, specified, set of
   * elements.
   */
  public GQuery insertAfter(String selector) {
    return insertAfter($(selector));
  }

  /**
   * Insert all of the matched elements after another, specified, set of
   * elements.
   */
  public GQuery insertAfter(Element elem) {
    return insertAfter($(elem));
  }

  /**
   * Insert all of the matched elements after another, specified, set of
   * elements.
   */
  public GQuery insertAfter(GQuery query) {
    for (Element e : elements()) {
      query.after(e);
    }
    return this;
  }

  /**
   * Insert all of the matched elements before another, specified, set of
   * elements.
   *
   * The elements must already be inserted into the document (you can't insert
   * an element after another if it's not in the page).
   */
  public GQuery insertBefore(Element item) {
    return insertBefore($(item));
  }

  /**
   * Insert all of the matched elements before another, specified, set of
   * elements.
   *
   * The elements must already be inserted into the document (you can't insert
   * an element after another if it's not in the page).
   */
  public GQuery insertBefore(GQuery query) {
    for (Element e : elements()) {
      query.before(e);
    }
    return this;
  }

  /**
   * Insert all of the matched elements before another, specified, set of
   * elements.
   *
   * The elements must already be inserted into the document (you can't insert
   * an element after another if it's not in the page).
   */
  public GQuery insertBefore(String selector) {
    return insertBefore($(selector));
  }

  /**
   * Checks the current selection against an expression and returns true, if at
   * least one element of the selection fits the given expression. Does return
   * false, if no element fits or the expression is not valid.
   */
  public boolean is(String... filters) {
    return filter(filters).size() > 0;
  }

  /**
   * Trigger a keydown event.
   */
  public GQuery keydown() {
    return trigger(
        Document.get().createKeyDownEvent(false, false, false, false, 0, 0),
        null);
  }

  /**
   * Bind a function to the keydown event of each matched element.
   */
  public GQuery keydown(Function f) {
    return bind(Event.ONKEYDOWN, null, f);
  }

  /**
   * Trigger a keypress event.
   */
  public GQuery keypress() {
    return trigger(
        Document.get().createKeyPressEvent(false, false, false, false, 0, 0),
        null);
  }

  /**
   * Bind a function to the keypress event of each matched element.
   */
  public GQuery keypressed(Function f) {
    return bind(Event.ONKEYPRESS, null, f);
  }

  /**
   * Trigger a keyup event.
   */
  public GQuery keyup() {
    return trigger(
        Document.get().createKeyUpEvent(false, false, false, false, 0, 0),
        null);
  }

  /**
   * Bind a function to the keyup event of each matched element.
   */
  public GQuery keyup(Function f) {
    return bind(Event.ONKEYUP, null, f);
  }

  /**
   * Returns the number of elements currently matched. The size function will
   * return the same value.
   */
  public int length() {
    return size();
  }

  /**
   * Bind a function to the load event of each matched element.
   */
  public GQuery load(Function f) {
    return bind(Event.ONLOAD, null, f);
  }

  /**
   * Reduce the set of matched elements to all elements before a given position.
   * The position of the element in the set of matched elements starts at 0 and
   * goes to length - 1.
   */
  public GQuery lt(int pos) {
    return $(slice(0, pos));
  }

  /**
   * Bind a function to the mousedown event of each matched element.
   */
  public GQuery mousedown(Function f) {
    return bind(Event.ONMOUSEDOWN, null, f);
  }

  /**
   * Bind a function to the mousemove event of each matched element.
   */
  public GQuery mousemove(Function f) {
    return bind(Event.ONMOUSEMOVE, null, f);
  }

  /**
   * Bind a function to the mouseout event of each matched element.
   */
  public GQuery mouseout(Function f) {
    return bind(Event.ONMOUSEOUT, null, f);
  }

  /**
   * Bind a function to the mouseover event of each matched element.
   */
  public GQuery mouseover(Function f) {
    return bind(Event.ONMOUSEOVER, null, f);
  }

  /**
   * Bind a function to the mouseup event of each matched element.
   */
  public GQuery mouseup(Function f) {
    return bind(Event.ONMOUSEUP, null, f);
  }

  /**
   * Get a set of elements containing the unique next siblings of each of the
   * given set of elements. next only returns the very next sibling for each
   * element, not all next siblings see {#nextAll}.
   */
  public GQuery next() {
    JSArray result = JSArray.create();
    for (Element e : elements()) {
      Element next = e.getNextSiblingElement();
      if (next != null) {
        result.addNode(next);
      }
    }
    return pushStack(unique(result), "next", getSelector());
  }

  /**
   * Get a set of elements containing the unique next siblings of each of the
   * given set of elements filtered by 1 or more selectors. next only returns
   * the very next sibling for each element, not all next siblings see
   * {#nextAll}.
   */
  public GQuery next(String... selectors) {
    JSArray result = JSArray.create();
    for (Element e : elements()) {
      Element next = e.getNextSiblingElement();
      if (next != null) {
        result.addNode(next);
      }
    }
    return pushStack(result, "next", selectors[0]).filter(selectors);
  }

  /**
   * Find all sibling elements after the current element.
   */
  public GQuery nextAll() {
    JSArray result = JSArray.create();
    for (Element e : elements()) {
      allNextSiblingElements(e.getNextSiblingElement(), result, null);
    }
    return pushStack(unique(result), "nextAll", getSelector());
  }

  /**
   * Removes the specified Element from the set of matched elements. This method
   * is used to remove a single Element from a jQuery object.
   */
  public GQuery not(Element elem) {
    JSArray array = JSArray.create();
    for (Element e : elements()) {
      if (e != elem) {
        array.addNode(e);
      }
    }
    return $(array);
  }

  /**
   * Removes any elements inside the passed set of elements from the set of
   * matched elements.
   */
  public GQuery not(GQuery gq) {
    GQuery ret = this;
    for (Element e : gq.elements()) {
      ret = ret.not(e);
    }
    return ret;
  }

  /**
   * Removes elements matching the specified expression from the set of matched
   * elements.
   */
  public GQuery not(String... filters) {
    GQuery ret = this;
    for (String f : filters) {
      ret = ret.not($(f));
    }
    return ret;
  }

  /**
   * Get the current offset of the first matched element, in pixels, relative to
   * the document. The returned object contains two Float properties, top and
   * left. Browsers usually round these values to the nearest integer pixel for
   * actual positioning. The method works only with visible elements.
   */
  public Offset offset() {
    if (length() == 0) {
      return new Offset(0, 0);
    }
    int boxtop = getClientBoundingRectTop(get(0));
    int boxleft = getClientBoundingRectLeft(get(0));
    Element docElem = Document.get().getDocumentElement();
    Element body = Document.get().getBody();
    int clientTop = docElem.getPropertyInt("clientTop");
    if (clientTop == 0) {
      clientTop = body.getPropertyInt("clientTop");
    }
    int clientLeft = docElem.getPropertyInt("clientLeft");
    if (clientLeft == 0) {
      clientLeft = body.getPropertyInt("clientLeft");
    }
    int wleft = Window.getScrollLeft();
    int wtop = Window.getScrollTop();
    int top = boxtop + (wtop == 0 ? body.getScrollTop() : wtop) - clientTop;
    int left = boxleft + (wleft == 0 ? body.getScrollLeft() : wleft)
        - clientLeft;
    return new Offset(top, left);
  }

  /**
   * Returns a GQuery collection with the positioned parent of the first matched
   * element. This is the first parent of the element that has position (as in
   * relative or absolute). This method only works with visible elements.
   */
  public GQuery offsetParent() {
    Element offParent = SelectorEngine.
        or(elements.getItem(0).getOffsetParent(), Document.get().getBody());
    while (offParent != null && !"body".equalsIgnoreCase(offParent.getTagName())
        && !"html".equalsIgnoreCase(offParent.getTagName()) && "static".
        equals(curCSS(offParent, "position"))) {
      offParent = offParent.getOffsetParent();
    }
    return new GQuery(offParent);
  }

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
  public GQuery one(int eventbits, final Object data, final Function f) {
    for (Element e : elements()) {
      EventsListener.getInstance(e).bind(eventbits, data, f, 1);
    }
    return this;
  }

  /**
   * Get a set of elements containing the unique parents of the matched set of
   * elements.
   */
  public GQuery parent() {
    JSArray result = JSArray.create();
    for (Element e : elements()) {
      result.addNode(e.getParentElement());
    }
    return new GQuery(unique(result));
  }

  /**
   * Get a set of elements containing the unique parents of the matched set of
   * elements. You may use an optional expressions to filter the set of parent
   * elements that will match one of them.
   */
  public GQuery parent(String... filters) {
    return parent().filter(filters);
  }

  /**
   * Get a set of elements containing the unique ancestors of the matched set of
   * elements (except for the root element). The matched elements are filtered,
   * returning those that match any of the filters.
   */
  public GQuery parents(String... filters) {
    return parents().filter(filters);
  }

  /**
   * Get a set of elements containing the unique ancestors of the matched set of
   * elements (except for the root element).
   */
  public GQuery parents() {
    JSArray result = JSArray.create();
    for (Element e : elements()) {
      Node par = e.getParentNode();
      while (par != null && par != Document.get()) {
        result.addNode(par);
        par = par.getParentNode();
      }
    }
    return new GQuery(unique(result));
  }

  /**
   * Gets the top and left position of an element relative to its offset parent.
   * The returned object contains two Integer properties, top and left. For
   * accurate calculations make sure to use pixel values for margins, borders
   * and padding. This method only works with visible elements.
   */
  public Offset position() {

    if (size() > 0) {
      GQuery offsetParent = offsetParent();
      Offset offset = offset();
      Element e = offsetParent.get(0);

      Offset parentOffset = BodyElement.is(e) || "html".equals(e.getTagName())
          ? new Offset(0, 0) : offsetParent.offset();
      offset.top -= num(this, "marginTop");
      offset.left -= num(this, "marginLeft");
      parentOffset.top += num(offsetParent, "borderTopWidth");
      parentOffset.left += num(offsetParent, "borderLeftWidth");
      return new Offset(offset.top - parentOffset.top,
          offset.left - parentOffset.left);
    }
    return null;
  }

  /**
   * Prepend content to the inside of every matched element. This operation is
   * the best way to insert elements inside, at the beginning, of all matched
   * elements.
   */
  public GQuery prepend(String html) {
    return domManip(html, FUNC_PREPEND);
  }

  /**
   * Prepend content to the inside of every matched element. This operation is
   * the best way to insert elements inside, at the beginning, of all matched
   * elements.
   */
  public GQuery prepend(GQuery query) {
    return domManip(query.elements, FUNC_PREPEND);
  }

  /**
   * Prepend content to the inside of every matched element. This operation is
   * the best way to insert elements inside, at the beginning, of all matched
   * elements.
   */
  public GQuery prepend(Node n) {
    return domManip(JSArray.create(n), FUNC_PREPEND);
  }

  /**
   * Prepend all of the matched elements to another, specified, set of elements.
   * This operation is, essentially, the reverse of doing a regular
   * $(A).prepend(B), in that instead of prepending B to A, you're prepending A
   * to B.
   */
  public GQuery prependTo(GQuery elms) {
    return elms.prepend(this);
  }

  /**
   * Get a set of elements containing the unique previous siblings of each of
   * the matched set of elements. Only the immediately previous sibling is
   * returned, not all previous siblings.
   */
  public GQuery prev() {
    JSArray result = JSArray.create();
    for (Element e : elements()) {
      Element next = getPreviousSiblingElement(e);
      if (next != null) {
        result.addNode(next);
      }
    }
    return new GQuery(unique(result));
  }

  /**
   * Get a set of elements containing the unique previous siblings of each of
   * the matched set of elements filtered by selector. Only the immediately
   * previous sibling is returned, not all previous siblings.
   */
  public GQuery prev(String... selectors) {
    JSArray result = JSArray.create();
    for (Element e : elements()) {
      Element next = getPreviousSiblingElement(e);
      if (next != null) {
        result.addNode(next);
      }
    }
    return new GQuery(unique(result)).filter(selectors);
  }

  /**
   * Find all sibling elements in front of the current element.
   */
  public GQuery prevAll() {
    JSArray result = JSArray.create();
    for (Element e : elements()) {
      allPreviousSiblingElements(getPreviousSiblingElement(e), result);
    }
    return pushStack(unique(result), "prevAll", getSelector());
  }

  /**
   * Returns a reference to the first element's queue (which is an array of
   * functions).
   */
  public Queue<Function> queue(String type) {
    return queue(elements.getItem(0), type, null);
  }

  /**
   * Returns a reference to the FX queue.
   */
  public Queue<Function> queue() {
    return queue(elements.getItem(0), "__FX", null);
  }

  /**
   * Adds a new function, to be executed, onto the end of the queue of all
   * matched elements.
   */
  public GQuery queue(String type, Function data) {
    for (Element e : elements()) {
      queue(e, type, data);
    }
    return this;
  }

  /**
   * Replaces the current queue with the given queue on all matched elements.
   */
  public GQuery queue(String type, Queue data) {
    for (Element e : elements()) {
      replacequeue(e, type, data);
    }
    return this;
  }

  /**
   * Adds a new function, to be executed, onto the end of the queue of all
   * matched elements in the FX queue.
   */
  public GQuery queue(Function data) {
    return queue("__FX", data);
  }

  /**
   * Removes all matched elements from the DOM.
   */
  public GQuery remove() {
    for (Element e : elements()) {
      // TODO: cleanup event bindings
      removeData(e, null);
      if (e.getParentNode() != null) {
        e.getParentNode().removeChild(e);
      }
    }
    return this;
  }

  /**
   * Remove the named attribute from every element in the matched set.
   */
  public GQuery removeAttr(String key) {
    for (Element e : elements()) {
      e.removeAttribute(key);
    }
    return this;
  }

  /**
   * Removes the specified classes to each matched element.
   */
  public GQuery removeClass(String... classes) {
    for (Element e : elements()) {
      for (String clz : classes) {
        setStyleName(e, clz, false);
      }
    }
    return this;
  }

  /**
   * Removes named data store from an element.
   */
  public GQuery removeData(String name) {
    for (Element e : elements()) {
      removeData(e, name);
    }
    return this;
  }

  /**
   * Replaces the elements matched by the specified selector with the matched
   * elements. This function is the complement to replaceWith() which does the
   * same task with the parameters reversed.
   */
  public GQuery replaceAll(GQuery query) {
    for (Element e : elements()) {
      $(e).replaceWith(query);
    }
    return this;
  }

  /**
   * Replaces the elements matched by the specified selector with the matched
   * elements. This function is the complement to replaceWith() which does the
   * same task with the parameters reversed.
   */
  public GQuery replaceAll(String html) {
    return replaceAll($(html));
  }

  /**
   * Replaces the elements matched by the specified selector with the matched
   * elements. This function is the complement to replaceWith() which does the
   * same task with the parameters reversed.
   */
  public GQuery replaceAll(Element elem) {
    return replaceAll($(elem));
  }

  /**
   * Replaces all matched elements with the specified HTML or DOM elements. This
   * returns the GQuery element that was just replaced, which has been removed
   * from the DOM.
   */
  public GQuery replaceWith(GQuery query) {
    return after(query).remove();
  }

  /**
   * Replaces all matched elements with the specified HTML or DOM elements. This
   * returns the GQuery element that was just replaced, which has been removed
   * from the DOM.
   */
  public GQuery replaceWith(String html) {
    return replaceWith($(html));
  }

  /**
   * Replaces all matched elements with the specified HTML or DOM elements. This
   * returns the GQuery element that was just replaced, which has been removed
   * from the DOM.
   */
  public GQuery replaceWith(Element elem) {
    return replaceWith($(elem));
  }

  /**
   * Bind a function to the scroll event of each matched element.
   */
  public GQuery scroll(Function f) {
    return bind(Event.ONSCROLL, null, f);
  }

  /**
   * When a value is passed in, the scroll left offset is set to that value on
   * all matched elements. This method works for both visible and hidden
   * elements.
   */
  public GQuery scrollLeft(int left) {
    for (Element e : elements()) {
      if (e == window() || e == (Node) Document.get()) {
        Window.scrollTo(left, $(e).scrollTop());
      } else {
        e.setPropertyInt("scrollLeft", left);
      }
    }
    return this;
  }

  /**
   * Gets the scroll left offset of the first matched element. This method works
   * for both visible and hidden elements.
   */
  public int scrollLeft() {
    Element e = get(0);
    if (e == window()) {
      return Window.getScrollLeft();
    } else if (e == (Node) Document.get()) {
      return Document.get().getScrollLeft();
    } else {
      return e.getScrollLeft();
    }
  }

  /**
   * When a value is passed in, the scroll top offset is set to that value on
   * all matched elements. This method works for both visible and hidden
   * elements.
   */
  public GQuery scrollTop(int top) {
    for (Element e : elements()) {
      if (e == window() || e == (Node) Document.get()) {
        Window.scrollTo($(e).scrollLeft(), top);
      } else {
        e.setPropertyInt("scrollTop", top);
      }
    }
    return this;
  }

  /**
   * Gets the scroll top offset of the first matched element. This method works
   * for both visible and hidden elements.
   */
  public int scrollTop() {
    Element e = get(0);
    if (e == window()) {
      return Window.getScrollTop();
    } else if (e == (Node) Document.get()) {
      return Document.get().getScrollTop();
    } else {
      return e.getScrollTop();
    }
  }

  public GQuery select() {
    return trigger(Document.get().createHtmlEvent("select", false, false),
        null);
  }

  /**
   * Set CSS property on the first element.
   */
  public <S, T extends CssProperty<S>> GQuery setCss(T cssProperty, S value) {
    cssProperty.set(elements.getItem(0).getStyle(), value);
    return this;
  }

  /**
   * Set CSS property on first matched element using type-safe enumerations.
   */
  public GQuery setCss(TakesLength cssProperty, Length value) {
    cssProperty.setLength(elements.getItem(0).getStyle(), value);
    return this;
  }

  /**
   * Set CSS property on first matched element using type-safe enumerations.
   */
  public GQuery setCss(TakesPercentage cssProperty, Percentage value) {
    cssProperty.setPercentage(elements.getItem(0).getStyle(), value);
    return this;
  }

  public void setPreviousObject(GQuery previousObject) {
    this.previousObject = previousObject;
  }

  public void setSelector(String selector) {
    this.currentSelector = selector;
  }

  /**
   * Return the number of elements in the matched set. Make visible all mached
   * elements
   */
  public GQuery show() {
    return $(as(Effects).show());
  }

  /**
   * Get a set of elements containing all of the unique siblings of each of the
   * matched set of elements.
   */
  public GQuery siblings() {
    JSArray result = JSArray.create();
    for (Element e : elements()) {
      allNextSiblingElements(e.getParentElement().getFirstChildElement(),
          result, e);
    }
    return new GQuery(unique(result));
  }

  /**
   * Get a set of elements containing all of the unique siblings of each of the
   * matched set of elements filtered by the provided set of selectors.
   */
  public GQuery siblings(String... selectors) {
    return siblings().filter(selectors);
  }

  /**
   * Return the number of elements in the matched set.
   */
  public int size() {
    return elements.getLength();
  }

  /**
   * Selects a subset of the matched elements.
   */
  public GQuery slice(int start, int end) {
    JSArray slice = JSArray.create();
    if (end == -1 || end > elements.getLength()) {
      end = elements.getLength();
    }
    for (int i = start; i < end; i++) {
      slice.addNode(elements.getItem(i));
    }
    return new GQuery(slice);
  }

  public GQuery submit() {
    return trigger(Document.get().createHtmlEvent("submit", false, false),
        null);
  }

  /**
   * Return the text contained in the first matched element.
   */
  public String text() {
    String result = "";
    for (Element e : elements()) {
      result += e.getInnerText();
    }
    return result;
  }

  /**
   * Set the innerText of every matched element.
   */
  public GQuery text(String txt) {
    for (Element e : asArray(elements)) {
      e.setInnerText(txt);
    }
    return this;
  }

  /**
   * Toggle visibility of elements.
   */
  public GQuery toggle() {
    return as(Effects).toggle();
  }

  /**
   * Toggle among two or more function calls every other click.
   */
  public GQuery toggle(final Function... fn) {
    return click(new Function() {
      int click = 0;

      @Override
      public boolean f(Event e) {
        return fn[(click++ % fn.length)].f(e);
      }
    });
  }

  /**
   * Adds or removes the specified classes to each matched element.
   */
  public GQuery toggleClass(String... classes) {
    for (Element e : elements()) {
      for (String clz : classes) {
        if (hasClass(e, clz)) {
          setStyleName(e, clz, false);
        } else {
          setStyleName(e, clz, true);
        }
      }
    }
    return this;
  }

  /**
   * Adds or removes the specified classes to each matched element.
   */
  public GQuery toggleClass(String clz, boolean sw) {
    for (Element e : elements()) {
      setStyleName(e, clz, sw);
    }
    return this;
  }

  /**
   * Produces a string representation of the matched elements.
   */
  public String toString() {
    return toString(false);
  }

  /**
   * Produces a string representation of the matched elements.
   */
  public String toString(boolean pretty) {
    String r = "";
    for (Element e : elements()) {
      r += (pretty && r.length() > 0 ? "\n " : "") + e.getString();
    }
    return r;
  }

  /**
   * Trigger an event of type eventbits on every matched element.
   */
  public GQuery trigger(int eventbits, int... keys) {
    return as(Events).fire(eventbits, keys);
  }

  /**
   * Removes all events that match the eventbits.
   */
  public GQuery unbind(int eventbits) {
    return as(Events).unbind(eventbits);
  }

  /**
   * Remove all duplicate elements from an array of elements. Note that this
   * only works on arrays of DOM elements, not strings or numbers.
   */
  public JSArray unique(JSArray result) {
    FastSet f = FastSet.create();
    JSArray ret = JSArray.create();
    for (int i = 0; i < result.getLength(); i++) {
      Element e = result.getElement(i);
      if (!f.contains(e)) {
        f.add(e);
        ret.addNode(e);
      }
    }
    return ret;
  }

  /**
   * Gets the content of the value attribute of the first matched element,
   * returns only the first value even if it is a multivalued element. To get an
   * array of all values in multivalues elements use vals()
   *
   * When the first element is a radio-button and is not checked, then it looks
   * for a the first checked radio-button that has the same name in the list of
   * matched elements.
   */
  public String val() {
    String[] v = vals();
    return (v != null && v.length > 0) ? v[0] : "";
  }

  /**
   * Sets the value attribute of every matched element In the case of multivalue
   * elements, all values are setted for other elements, only the first value is
   * considered.
   */
  public GQuery val(String... values) {
    for (Element e : elements()) {
      String name = e.getNodeName();
      if ("select".equalsIgnoreCase(name)) {
        SelectElement s = SelectElement.as(e);
        if (values.length > 1 && s.isMultiple()) {
          s.setSelectedIndex(-1);
          for (String v : values) {
            for (int i = 0; i < s.getOptions().getLength(); i++) {
              if (v.equals(s.getOptions().getItem(i).getValue())) {
                s.getOptions().getItem(i).setSelected(true);
              }
            }
          }
        } else {
          s.setValue(values[0]);
        }
      } else if ("input".equalsIgnoreCase(name)) {
        InputElement ie = InputElement.as(e);
        String type = ie.getType();
        if ("radio".equalsIgnoreCase((type)) || "checkbox".
            equalsIgnoreCase(type)) {
          if ("checkbox".equalsIgnoreCase(type)) {
            for (String val : values) {
              if (ie.getValue().equals(val)) {
                ie.setChecked(true);
              } else {
                ie.setChecked(false);
              }
            }
          } else if (values[0].equals(ie.getValue())) {
            ie.setChecked(true);
          }
        } else {
          ie.setValue(values[0]);
        }
      } else if ("textarea".equalsIgnoreCase(name)) {
        TextAreaElement.as(e).setValue(values[0]);
      } else if ("button".equalsIgnoreCase(name)) {
        ButtonElement.as(e).setValue(values[0]);
      }
    }
    return this;
  }

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
  public String[] vals() {
    if (size() > 0) {
      Element e = get(0);
      if (e.getNodeName().equalsIgnoreCase("select")) {
        SelectElement se = SelectElement.as(e);
        if (se.isMultiple()) {
          JsArrayString result = JsArrayString.createArray().cast();
          for (OptionElement oe : asArray(se.getOptions())) {
            if (oe.isSelected()) {
              result.set(result.length(), oe.getValue());
            }
          }
          return jsArrayToString(result);
        } else if (se.getSelectedIndex() >= 0) {
          return new String[]{
              se.getOptions().getItem(se.getSelectedIndex()).getValue()};
        }
      } else if (e.getNodeName().equalsIgnoreCase("input")) {
        InputElement ie = InputElement.as(e);
        if ("radio".equalsIgnoreCase(ie.getType())) {
          for (Element e2 : elements()) {
            if ("input".equalsIgnoreCase(e2.getNodeName())) {
              InputElement ie2 = InputElement.as(e2);
              if ("radio".equalsIgnoreCase(ie2.getType()) && ie2.isChecked()
                  && ie.getName().equals(ie2.getName())) {
                return new String[]{ie2.getValue()};
              }
            }
          }
        } else if ("checkbox".equalsIgnoreCase(ie.getType())) {
          if (ie.isChecked()) {
            return new String[]{ie.getValue()};
          }
        } else {
          return new String[]{ie.getValue()};
        }
      } else if (e.getNodeName().equalsIgnoreCase("textarea")) {
        return new String[]{TextAreaElement.as(e).getValue()};
      } else if (e.getNodeName().equalsIgnoreCase("button")) {
        return new String[]{ButtonElement.as(e).getValue()};
      }
    }
    return new String[0];
  }

  /**
   * Return true if the first element is visible.
   */
  public boolean visible() {
    return as(Effects).visible();
  }

  /**
   * Set the width of every matched element.
   */
  public GQuery width(int width) {
    for (Element e : elements()) {
      e.getStyle().setPropertyPx("width", width);
    }
    return this;
  }

  /**
   * Get the current computed, pixel, width of the first matched element.
   */
  public int width() {
    return DOM.
        getElementPropertyInt((com.google.gwt.user.client.Element) get(0),
            "offsetWidth");
  }

  /**
   * Wrap each matched element with the specified HTML content. This wrapping
   * process is most useful for injecting additional structure into a document,
   * without ruining the original semantic qualities of a document. This works
   * by going through the first element provided (which is generated, on the
   * fly, from the provided HTML) and finds the deepest descendant element
   * within its structure -- it is that element that will enwrap everything
   * else.
   */
  public GQuery wrap(GQuery query) {
    for (Element e : elements()) {
      $(e).wrapAll(query);
    }
    return this;
  }

  /**
   * Wrap each matched element with the specified HTML content. This wrapping
   * process is most useful for injecting additional structure into a document,
   * without ruining the original semantic qualities of a document. This works
   * by going through the first element provided (which is generated, on the
   * fly, from the provided HTML) and finds the deepest descendant element
   * within its structure -- it is that element that will enwrap everything
   * else.
   */
  public GQuery wrap(Element elem) {
    return wrap($(elem));
  }

  /**
   * Wrap each matched element with the specified HTML content. This wrapping
   * process is most useful for injecting additional structure into a document,
   * without ruining the original semantic qualities of a document. This works
   * by going through the first element provided (which is generated, on the
   * fly, from the provided HTML) and finds the deepest descendant element
   * within its structure -- it is that element that will enwrap everything
   * else.
   */
  public GQuery wrap(String html) {
    return wrap($(html));
  }

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
  public GQuery wrapAll(String html) {
    return wrapAll($(html));
  }

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
  public GQuery wrapAll(Element elem) {
    return wrapAll($(elem));
  }

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
  public GQuery wrapAll(GQuery query) {
    GQuery wrap = query.clone();
    if (elements.getItem(0).getParentNode() != null) {
      wrap.insertBefore(elements.getItem(0));
    }
    for (Element e : wrap.elements()) {
      Node n = e;
      while (n.getFirstChild() != null
          && n.getFirstChild().getNodeType() == Node.ELEMENT_NODE) {
        n = n.getFirstChild();
      }
      $((Element) n).append(this);
    }
    return this;
  }

  /**
   * Wrap the inner child contents of each matched element (including text
   * nodes) with an HTML structure. This wrapping process is most useful for
   * injecting additional structure into a document, without ruining the
   * original semantic qualities of a document. This works by going through the
   * first element provided (which is generated, on the fly, from the provided
   * HTML) and finds the deepest ancestor element within its structure -- it is
   * that element that will enwrap everything else.
   */
  public GQuery wrapInner(GQuery query) {
    for (Element e : elements()) {
      $(e).contents().wrapAll(query);
    }
    return this;
  }

  /**
   * Wrap the inner child contents of each matched element (including text
   * nodes) with an HTML structure. This wrapping process is most useful for
   * injecting additional structure into a document, without ruining the
   * original semantic qualities of a document. This works by going through the
   * first element provided (which is generated, on the fly, from the provided
   * HTML) and finds the deepest ancestor element within its structure -- it is
   * that element that will enwrap everything else.
   */
  public GQuery wrapInner(String html) {
    return wrapInner($(html));
  }

  /**
   * Wrap the inner child contents of each matched element (including text
   * nodes) with an HTML structure. This wrapping process is most useful for
   * injecting additional structure into a document, without ruining the
   * original semantic qualities of a document. This works by going through the
   * first element provided (which is generated, on the fly, from the provided
   * HTML) and finds the deepest ancestor element within its structure -- it is
   * that element that will enwrap everything else.
   */
  public GQuery wrapInner(Element elem) {
    return wrapInner($(elem));
  }

  protected GQuery pushStack(JSArray elts, String name, String selector) {
    GQuery g = new GQuery(elts);
    g.setPreviousObject(this);
    g.setSelector(selector);
    return g;
  }

  private void allNextSiblingElements(Element firstChildElement, JSArray result,
      Element elem) {
    while (firstChildElement != null) {
      if (firstChildElement != elem) {
        result.addNode(firstChildElement);
      }
      firstChildElement = firstChildElement.getNextSiblingElement();
    }
  }

  private void allPreviousSiblingElements(Element firstChildElement,
      JSArray result) {
    while (firstChildElement != null) {
      result.addNode(firstChildElement);
      firstChildElement = getPreviousSiblingElement(firstChildElement);
    }
  }

  private JSArray clean(String elem) {
    String tags = elem.trim().toLowerCase();
    String preWrap = "", postWrap = "";
    int wrapPos = 0;
    if (tags.contains("<opt")) {
      wrapPos = 1;
      preWrap = "<select multiple=\"multiple\">";
      postWrap = "</select>";
    } else if (tags.contains("<legend")) {
      wrapPos = 1;
      preWrap = "<fieldset>";
      postWrap = "</fieldset>";
    } else if (tags.matches("^<(thead|tbody|tfoot|colg|cap)")) {
      wrapPos = 1;
      preWrap = "<table>";
      postWrap = "</table>";
    } else if (tags.contains("<tr")) {
      wrapPos = 2;
      preWrap = "<table><tbody>";
      postWrap = "</tbody></table>";
    } else if (tags.contains("<td") || tags.contains("<th")) {
      wrapPos = 3;
      preWrap = "<table><tbody><tr>";
      postWrap = "</tr></tbody></table>";
    } else if (tags.contains("<col")) {
      wrapPos = 2;
      preWrap = "<table><tbody></tbody><colgroup>";
      postWrap = "</colgroup></table>";
    }
    // TODO: fix IE link tag serialization
    Element div = Document.get().createDivElement();
    div.setInnerHTML(preWrap + elem + postWrap);
    Node n = div;
    while (wrapPos-- != 0) {
      n = n.getLastChild();
    }
    // TODO: add fixes for IE TBODY issue
    return n.getChildNodes().cast();
  }

  private <S> Object data(Element item, String name, S value) {
    if (dataCache == null) {
      windowData = JavaScriptObject.createObject().cast();
      dataCache = JavaScriptObject.createObject().cast();
    }
    item = item == window() ? windowData : item;
    if (item == null) {
      return value;
    }
    int id = item.hashCode();
    if (name != null && !dataCache.exists(id)) {
      dataCache.put(id, DataCache.createObject().cast());
    }

    DataCache d = dataCache.get(id).cast();
    if (name != null && value != null) {
      d.put(name, value);
    }
    return name != null ? d.getObject(name) : id;
  }

  private void dequeue(Element elem, String type) {
    Queue<Function> q = queue(elem, type, null);

    if (q != null) {
      Function f = q.dequeue();

      if (SelectorEngine.eq(type, "__FX")) {
        f = q.peek(0);
      }
      if (f != null) {
        f.f(elem);
      }
    }
  }

  private GQuery domManip(String html, int func) {
    return domManip(clean(html), func);
  }

  private GQuery domManip(NodeList nodes, int func) {
    for (Element e : elements()) {
      for (int i = 0; i < nodes.getLength(); i++) {
        Node n = nodes.getItem(i);
        if (size() > 1) {
          n = n.cloneNode(true);
        }

        switch (func) {
          case FUNC_PREPEND:
            e.insertBefore(n, e.getFirstChild());
            break;
          case FUNC_APPEND:
            e.appendChild(n);
            break;
          case FUNC_AFTER:
            e.getParentNode().insertBefore(n, e.getNextSibling());
            break;
          case FUNC_BEFORE:
            e.getParentNode().insertBefore(n, e);
            break;
        }
      }
    }
    return this;
  }

  private String fixAttributeName(String key) {
    return key;
  }

  private native int getClientBoundingRectLeft(Element element) /*-{
    return element.getClientBoundingRect().left;
  }-*/;

  private native int getClientBoundingRectTop(Element element) /*-{
    return element.getClientBoundingRect().top;
  }-*/;

  private native Document getContentDocument(Node n) /*-{
    return n.contentDocument || n.contentWindow.document;
  }-*/;

  private native Element getPreviousSiblingElement(Element elem)  /*-{
    var sib = elem.previousSibling;
    while (sib && sib.nodeType != 1)
      sib = sib.previousSibling;
    return sib;
  }-*/;

  private void init(GQuery gQuery) {
    this.elements = gQuery.elements;
  }

  private JSArray merge(NodeList<Element> first, NodeList<Element> second) {
    JSArray res = copyNodeList(first);
    for (int i = 0; i < second.getLength(); i++) {
      res.addNode(second.getItem(i));
    }
    return res;
  }

  private int num(GQuery gQuery, String val) {
    Element elem = gQuery.get(0);
    try {
      if (elem != null) {
        String v = GQuery.curCSS(elem, val);
        return Integer.parseInt(v);
      }
    } catch (NumberFormatException e) {
    }
    return 0;
  }

  private Queue<Function> queue(Element elem, String type, Function data) {
    if (elem != null) {
      type = type + "queue";
      Object q = (Queue) data(elem, type, null);
      if (q == null) {
        q = data(elem, type, Queue.newInstance());
      }
      Queue<Function> qq = (Queue<Function>) q;
      if (data != null) {
        qq.enqueue(data);
      }
      if (SelectorEngine.eq(type, "__FXqueue") && qq.length() == 1) {
        if (data != null) {
          data.f(elem);
        }
      }
      return qq;
    }
    return null;
  }

  private void removeData(Element item, String name) {
    if (dataCache == null) {
      windowData = JavaScriptObject.createObject().cast();
      dataCache = JavaScriptObject.createObject().cast();
    }
    item = item == window() ? windowData : item;
    int id = item.hashCode();
    if (name != null) {
      if (!dataCache.exists(id)) {
        dataCache.getCache(id).delete(name);
      }
      if (dataCache.getCache(id).isEmpty()) {
        removeData(item, null);
      }
    } else {
      dataCache.delete(id);
    }
  }

  private void replacequeue(Element elem, String type, Queue data) {
    if (elem != null) {
      type = type + "queue";
      data(elem, type, data);
    }
  }

  private GQuery trigger(NativeEvent event, Object o) {
    for (Element e : elements()) {
      e.dispatchEvent(event);
    }
    return this;
  }

  private native Element window() /*-{
    return $wnd;
  }-*/;
}
