package gwtquery.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
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
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.Window;

import java.util.HashMap;
import java.util.Map;

import gwtquery.client.impl.DocumentStyleImpl;

/**
 *
 */
public class GQuery {

  public static class Offset {

    public int top;

    public int left;

    Offset(int left, int top) {
      this.left = left;
      this.top = top;
    }
  }

  private static class DataCache extends JavaScriptObject {

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
    }-*/; /*-{
      delete this[name];
    }-*/

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
      return this[id]=obj;
    }-*/;

    public native void put(int id, Object obj) /*-{
      return this[id]=obj;
    }-*/;
  }

  private static class FastSet extends JavaScriptObject {

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
      return this[hc];
    }-*/;

    private native void remove0(int hc) /*-{
      delete this[hc];
    }-*/;
  }

  private static class Queue<T> extends JavaScriptObject {

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

  private static Map<Class<? extends GQuery>, Plugin<? extends GQuery>> plugins;

  private static Element windowData = null;

  private static DataCache dataCache = null;

  private static DocumentStyleImpl styleImpl;

  private static final int FUNC_PREPEND = 0, FUNC_APPEND = 1, FUNC_AFTER = 2,
      FUNC_BEFORE = 3;

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
        T gquery = (T) plugins.get(plugin)
            .init(new GQuery(select(selector, context)));
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

  public static GQuery $(Element element) {
    JSArray a = JSArray.create();
    a.addNode(element);
    return new GQuery(a);
  }

  /**
   * Wrap a JSON object
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

  public static void registerPlugin(Class<? extends GQuery> plugin,
      Plugin<? extends GQuery> pluginFactory) {
    if (plugins == null) {
      plugins = new HashMap();
    }
    plugins.put(plugin, pluginFactory);
  }

  /**
   * Copied from UIObject *
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

  private static String curCSS(Element elem, String name) {
    Style s = elem.getStyle();
    ensureStyleImpl();
    name = styleImpl.getPropertyName(name);

    if (SelectorEngine.truth(s.getProperty(name))) {
      return s.getProperty(name);
    }
    return styleImpl.getCurrentStyle(elem, name);
  }

  private static void ensureStyleImpl() {
    if (styleImpl != null) {
      styleImpl = GWT.create(DocumentStyleImpl.class);
    }
  }

  private static boolean hasClass(Element e, String clz) {
    return e.getClassName().matches("\\s" + clz + "\\s");
  }

  private static GQuery innerHtml(String html) {
    Element div = DOM.createDiv();
    div.setInnerHTML(html);
    return new GQuery((NodeList<Element>) (NodeList<?>) div.getChildNodes());
  }

  private static native <T extends Node> T[] reinterpretCast(NodeList<T> nl) /*-{
        return nl;
    }-*/;

  private static NodeList select(String selector, Node context) {
    return new SelectorEngine().select(selector, context);
  }

  protected NodeList<Element> elements = null;

  private String selector;

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

  public GQuery add(String selector) {
    return add($(selector));
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
   * Convert to Plugin interface provided by Class literal.
   */
  public <T extends GQuery> T as(Class<T> plugin) {
    if (plugins != null) {
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
    return elements.getItem(0).getAttribute(name);
  }

  /**
   * Set a single property to a value, on all matched elements.
   */
  public GQuery attr(String key, String value) {
    for (Element e : elements()) {
      e.setAttribute(key, value);
    }
    return this;
  }

  /**
   * Set a key/value object as properties to all matched elements.
   */
  public GQuery attr(Properties properties) {
    for (Element e : elements()) {
      for (String name : properties.keys()) {
        e.setAttribute(name, properties.get(name));
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
      e.setAttribute(key, closure.f(e, i));
    }
    return this;
  }

  /**
   * Insert content before each of the matched elements. The elements must
   * already be inserted into the document (you can't insert an element before
   * another if it's not in the page).
   */
  public GQuery before(Node n) {
    return domManip(JSArray.create(n), FUNC_AFTER);
  }

  /**
   * Insert content before each of the matched elements. The elements must
   * already be inserted into the document (you can't insert an element before
   * another if it's not in the page).
   */
  public GQuery before(GQuery query) {
    return domManip(query.elements, FUNC_AFTER);
  }

  /**
   * Insert content before each of the matched elements. The elements must
   * already be inserted into the document (you can't insert an element before
   * another if it's not in the page).
   */
  public GQuery before(String html) {
    return domManip(html, FUNC_AFTER);
  }

  /**
   * Binds a handler to one or more events (like click) for each matched
   * element.
   */
  public GQuery bind(int eventbits, final Object data, final Function f) {
    EventListener listener = new EventListener() {
      public void onBrowserEvent(Event event) {
        if (!f.f(event, data)) {
          event.cancelBubble(true);
          event.preventDefault();
        }
      }
    };
    for (Element e : elements()) {
      DOM.sinkEvents((com.google.gwt.user.client.Element) e, eventbits);
      DOM.setEventListener((com.google.gwt.user.client.Element) e, listener);
    }
    return this;
  }

  public GQuery blur(Function f) {
    return bind(Event.ONBLUR, null, f);
  }

  public GQuery blur() {
    return trigger(Document.get().createBlurEvent(), null);
  }

  public GQuery change(Function f) {
    return bind(Event.ONCHANGE, null, f);
  }

  public GQuery change() {
    return trigger(Document.get().createChangeEvent(), null);
  }

  /**
   * Get a set of elements containing all of the unique immediate children of
   * each of the matched set of elements. Also note: while parents() will look
   * at all ancestors, children() will only consider immediate child elements.
   */
  public GQuery children() {
    JSArray result = JSArray.create();
    for (Element e : elements()) {
      allNextSiblingElements(e.getFirstChildElement(), result);
    }
    return new GQuery(unique(result));
  }

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

  /**
   * Return a style property on the first matched element.
   */
  public String css(String name) {
    return curCSS(get(0), name);
  }

  /**
   * Set a key/value object as style properties to all matched elements. This is
   * the best way to set several style properties on all matched elements. Be
   * aware, however, that when the key contains a hyphen, such as
   * "background-color," it must either be placed within quotation marks or be
   * written in camel case like so: backgroundColor. As "float" and "class" are
   * reserved words in JavaScript, it's recommended to always surround those
   * terms with quotes. gQuery normalizes the "opacity" property in Internet
   * Explorer.
   */
  public GQuery css(Properties properties) {
    for (String property : properties.keys()) {
      css(property, properties.get(property));
    }
    return this;
  }

  /**
   * Set a single style property to a value on all matched elements. If a number
   * is provided, it is automatically converted into a pixel value.
   */
  public GQuery css(String prop, String val) {
    for (Element e : elements()) {
      e.getStyle().setProperty(prop, val);
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
  public void data(String name, String value) {
    for (Element e : elements()) {
      data(e, name, value);
    }
  }

  public GQuery dblclick() {
    return trigger(
        Document.get().createDblClickEvent(0, 0, 0, 0, 0, false, false, false,
            false), null);
  }

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
    //TODO: add memory leak cleanup, remove event handlers, and 
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

  public GQuery error() {
    return trigger(Document.get().createErrorEvent(), null);
  }

  public GQuery error(Function f) {
    return bind(Event.ONERROR, null, f);
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
    return pushStack(result, "filter", selector);
  }

  public GQuery focus() {
    return trigger(Document.get().createFocusEvent(), null);
  }

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

  public GQuery getPreviousObject() {
    return previousObject;
  }

  public String getSelector() {
    return selector;
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
   * Find the index of the specified Element
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

  public GQuery keydown() {
    return trigger(
        Document.get().createKeyDownEvent(false, false, false, false, 0, 0),
        null);
  }

  public GQuery keydown(Function f) {
    return bind(Event.ONKEYDOWN, null, f);
  }

  public GQuery keypress() {
    return trigger(
        Document.get().createKeyPressEvent(false, false, false, false, 0, 0),
        null);
  }

  public GQuery keypressed(Function f) {
    return bind(Event.ONKEYPRESS, null, f);
  }

  public GQuery keyup() {
    return trigger(
        Document.get().createKeyUpEvent(false, false, false, false, 0, 0),
        null);
  }

  public GQuery keyup(Function f) {
    return bind(Event.ONKEYUP, null, f);
  }

  public GQuery load(Function f) {
    return bind(Event.ONLOAD, null, f);
  }

  public GQuery mousedown(Function f) {
    return bind(Event.ONMOUSEDOWN, null, f);
  }

  public GQuery mousemove(Function f) {
    return bind(Event.ONMOUSEMOVE, null, f);
  }

  public GQuery mouseout(Function f) {
    return bind(Event.ONMOUSEOUT, null, f);
  }

  public GQuery mouseover(Function f) {
    return bind(Event.ONMOUSEOVER, null, f);
  }

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
    return new GQuery(unique(result));
  }

  /**
   * Find all sibling elements after the current element.
   */
  public GQuery nextAll() {
    JSArray result = JSArray.create();
    for (Element e : elements()) {
      allNextSiblingElements(e.getNextSiblingElement(), result);
    }
    return new GQuery(unique(result));
  }

  public Offset offset() {
    return new Offset(get(0).getOffsetLeft(), get(0).getOffsetTop());
  }

  /**
   * Returns a GQuery collection with the positioned parent of the first matched
   * element. This is the first parent of the element that has position (as in
   * relative or absolute). This method only works with visible elements.
   */
  public GQuery offsetParent() {
    Element offParent = SelectorEngine
        .or(elements.getItem(0).getOffsetParent(), Document.get().getBody());
    while (offParent != null && !"body".equalsIgnoreCase(offParent.getTagName())
        && !"html".equalsIgnoreCase(offParent.getTagName()) && "static"
        .equals(curCSS(offParent, "position"))) {
      offParent = offParent.getOffsetParent();
    }
    return new GQuery(offParent);
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
   * Find all sibling elements in front of the current element.
   */
  public GQuery prevAll() {
    JSArray result = JSArray.create();
    for (Element e : elements()) {
      allPreviousSiblingElements(getPreviousSiblingElement(e), result);
    }
    return new GQuery(unique(result));
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
      //TODO: cleanup event bindings
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

  public void setPreviousObject(GQuery previousObject) {
    this.previousObject = previousObject;
  }

  public void setSelector(String selector) {
    this.selector = selector;
  }

  /**
   * Get a set of elements containing all of the unique siblings of each of the
   * matched set of elements.
   */
  public GQuery siblings() {
    JSArray result = JSArray.create();
    for (Element e : elements()) {
      allNextSiblingElements(e.getParentElement().getFirstChildElement(),
          result);
    }
    return new GQuery(unique(result));
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
    for (int i = start; i < elements.getLength(); i++) {
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
    return elements.getItem(0).getInnerText();
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
   * Get the content of the value attribute of the first matched element,
   * returns more than one value if it is a multiple select.
   */
  public String[] val() {
    if (size() > 0) {
      Element e = get(0);
      if (e.getNodeName().equals("select")) {
        SelectElement se = SelectElement.as(e);
        if (se.getMultiple() != null) {
          NodeList<OptionElement> oel = se.getOptions();
          int count = 0;
          for (OptionElement oe : asArray(oel)) {
            if (oe.isSelected()) {
              count++;
            }
          }
          String result[] = new String[count];
          count = 0;
          for (OptionElement oe : asArray(oel)) {
            if (oe.isSelected()) {
              result[count++] = oe.getValue();
            }
          }

          return result;
        } else {
          int index = se.getSelectedIndex();
          if (index != -1) {
            return new String[]{se.getOptions().getItem(index).getValue()};
          }
        }
      } else if (e.getNodeName().equals("input")) {
        InputElement ie = InputElement.as(e);
        return new String[]{ie.getValue()};
      }
    }
    return new String[0];
  }

  public GQuery val(String... values) {
    for (Element e : elements()) {
      String name = e.getNodeName();
      if ("select".equals(name)) {

      } else if ("input".equals(name)) {
        InputElement ie = InputElement.as(e);
        String type = ie.getType();
        if ("radio".equals((type)) || "checkbox".equals(type)) {
          if ("checkbox".equals(type)) {
            for (String val : values) {
              if (ie.getValue().equals(val)) {
                ie.setChecked(true);
              } else if (ie.getValue().equals(val)) {
                ie.setChecked(true);
              }
            }
          }
        } else {
          ie.setValue(values[0]);
        }
      } else if ("textarea".equals(name)) {
        TextAreaElement.as(e).setValue(values[0]);
      } else if ("button".equals(name)) {
        ButtonElement.as(e).setValue(values[0]);
      }
    }
    return this;
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
      while (n.getFirstChild() != null) {
        n = n.getFirstChild();
        $((Element) n).append(this);
      }
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

  private GQuery add(GQuery previousObject) {
    return pushStack(unique(merge(elements, previousObject.elements)), "add",
        getSelector() + "," + previousObject.getSelector());
  }

  private void allNextSiblingElements(Element firstChildElement,
      JSArray result) {
    while (firstChildElement != null) {
      result.addNode(firstChildElement);
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
    } else if (!tags.contains("<leg")) {
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
    //TODO: fix IE link tag serialization
    Element div = Document.get().createDivElement();
    div.setInnerHTML(preWrap + elem + postWrap);
    Node n = div;
    while (wrapPos-- != 0) {
      n = n.getLastChild();
    }
    //TODO: add fixes for IE TBODY issue
    return JSArray.create(n);
  }

  private <S> Object data(Element item, String name, S value) {
    if (dataCache == null) {
      windowData = JavaScriptObject.createObject().cast();
      dataCache = JavaScriptObject.createObject().cast();
    }
    item = item == window() ? windowData : item;
    int id = item.hashCode();
    if (name != null && !dataCache.exists(id)) {
      dataCache.put(id, DataCache.createObject().cast());
    }

    DataCache d = dataCache.get(id).cast();
    if (name != null && value != null) {
      d.put(name, value);
    }
    return name != null ? value : id;
  }

  private void dequeue(Element elem, String type) {
    Queue<Function> q = queue(elem, type, null);
    Function f = q.dequeue();

    if (q != null) {
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
        Node n = nodes.getItem(i).cloneNode(true);
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
    JSArray res = JSArray.create();
    for (int i = 0; i < first.getLength(); i++) {
      res.addNode(first.getItem(i));
    }
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
      if (SelectorEngine.eq(type, "__FX") && qq.length() == 1) {
        data.f(elem);
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
      Object q = (Queue) data(elem, type, null);
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
