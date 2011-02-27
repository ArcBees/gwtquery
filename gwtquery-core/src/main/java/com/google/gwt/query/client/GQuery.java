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
import java.util.Arrays;
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
import com.google.gwt.dom.client.Style.HasCssName;
import com.google.gwt.dom.client.TextAreaElement;
import com.google.gwt.query.client.css.CSS;
import com.google.gwt.query.client.css.CssProperty;
import com.google.gwt.query.client.css.TakeCssValue;
import com.google.gwt.query.client.css.TakeCssValue.CssSetter;
import com.google.gwt.query.client.impl.DocumentStyleImpl;
import com.google.gwt.query.client.plugins.EventsListener;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;

/**
 * GwtQuery is a GWT clone of the popular jQuery library.
 */
public class GQuery implements Lazy<GQuery, LazyGQuery> {

  /**
   * A POJO used to store the top/left CSS positioning values of an element.
   */
  public static class Offset {
    public int left;
    public int top;

    public Offset(int left, int top) {
      this.left = left;
      this.top = top;
    }

    public Offset add(int left, int top) {
      return new Offset(this.left + left, this.top + top);
    }

    public String toString() {
      return top + "+" + left;
    }
  }

  /**
   * A class to store data in an element.
   */
  protected static final class DataCache extends JavaScriptObject {

    protected DataCache() {
    }

    public native void delete(int name) /*-{
      delete this[name];
    }-*/;

    public native void delete(String name) /*-{
      delete this[name];
    }-*/;

    public native boolean exists(int id) /*-{
      return !!this[id];
    }-*/;

    public native JavaScriptObject get(int id) /*-{
      return this[id];
    }-*/;

    public native JavaScriptObject get(String id) /*-{
      return this[id];
    }-*/;

    public DataCache getCache(int id) {
      return get(id).cast();
    }

    public native double getDouble(int id) /*-{
      return this[id];
    }-*/;

    public native double getDouble(String id) /*-{
      return this[id];
    }-*/;

    public native int getInt(int id) /*-{
      return this[id];
    }-*/;

    public native int getInt(String id) /*-{
      return this[id];
    }-*/;

    public native Object getObject(String id) /*-{
          return this[id];
        }-*/;

    public native String getString(int id) /*-{
      return this[id];
    }-*/;

    public native String getString(String id) /*-{
      return this[id];
    }-*/;

    public native boolean isEmpty() /*-{
        var foo = "";
        for(foo in this) break;
        return !foo;
    }-*/;

    public native void put(int id, Object obj) /*-{
      this[id]=obj;
    }-*/;

    public native void put(String id, Object obj) /*-{
      this[id]=obj;
    }-*/;
  }

  /**
   * The body element in the current page.
   */
  public static final BodyElement body = Document.get().getBody();

  /**
   * The document element in the current page. 
   */
  public static final Document document = Document.get();

  /**
   * A static reference to the GQuery class.
   */
  public static Class<GQuery> GQUERY = GQuery.class;

  /**
   * The window object.
   */
  public static final Element window = window();

  private static DataCache dataCache = null;

  private static SelectorEngine engine;

  private static final int FUNC_PREPEND = 0, FUNC_APPEND = 1, FUNC_AFTER = 2,
      FUNC_BEFORE = 3;

  private static final String OLD_DATA_PREFIX = "old-";

  private static JsMap<Class<? extends GQuery>, Plugin<? extends GQuery>>
      plugins;;

  private static DocumentStyleImpl styleImpl = GWT.create(DocumentStyleImpl.class);

  private static Element windowData = null;

  /**
   * Create an empty GQuery object.
   */
  public static GQuery $() {
    return new GQuery(JSArray.create());
  }

  /**
   * Wrap a GQuery around a collection of existing widget.
   */
  public static GQuery $(Collection<Widget> widgetList){
    JSArray elements = JSArray.create();
    for (Widget w : widgetList){
      elements.addNode(w.getElement());
    }
    return $(elements);
  }

  /**
   * Wrap a GQuery around an existing element.
   */
  public static GQuery $(Element element) {
    return new GQuery(element);
  }

  /**
   * Wrap a GQuery around an event's target element.
   */
  public static GQuery $(Event event) {
    return event == null ? $() : $((Element) event.getCurrentEventTarget().cast());
  }

  /**
   * Wrap a GQuery around an existing node.
   */
  public static GQuery $(Node n) {
    return n == null ? $() : new GQuery(JSArray.create(n));
  }

  /**
   * Wrap a GQuery around  existing Elements.
   */
  public static GQuery $(NodeList<Element> elements) {
    return new GQuery(elements);
  }
  
  /**
   * Create a new GQuery given a list of objects. 
   * Only node objects will be added;
   */
  public static GQuery $(@SuppressWarnings("rawtypes") ArrayList a) {
    JSArray elements = JSArray.create();
    if (a != null) {
      for (Object o : a ) {
        if (o instanceof Node) {
          elements.addNode((Node)o);
        } else if (o instanceof Widget) {
          elements.addNode(((Widget)o).getElement());
        }
      }
    }
    return new GQuery(elements);
  }

  /**
   * This function accepts a string containing a CSS selector which is then used
   * to match a set of elements, or it accepts raw HTML creating a GQuery
   * element containing those elements.
   */
  public static GQuery $(String selectorOrHtml) {
    if (selectorOrHtml == null || selectorOrHtml.trim().length() == 0) {
      return $();
    }
    if (selectorOrHtml.trim().charAt(0) == '<') {
      return innerHtml(selectorOrHtml);
    }
    return $(selectorOrHtml, document);
  }

  /**
   * This function accepts a string containing a CSS selector which is then used
   * to match a set of elements, or it accepts raw HTML creating a GQuery
   * element containing those elements. The second parameter is is a class
   * reference to a plugin to be used.
   */
  public static <T extends GQuery> T $(String selector, Class<T> plugin) {
    return $(selector, (Node) null, plugin);
  }

  /**
   * This function accepts a string containing a CSS selector which is then used
   * to match a set of elements, or it accepts raw HTML creating a GQuery
   * element containing those elements.
   * The second parameter is the context to use for the selector, or
   * the document where the new elements will be created.
   */
  public static GQuery $(String selectorOrHtml, Node ctx) {
    if (selectorOrHtml == null || selectorOrHtml.trim().length() == 0) {
      return $();
    }
    if (selectorOrHtml.trim().charAt(0) == '<') {
      Document doc = ctx instanceof Document ? ctx.<Document>cast() : ctx.getOwnerDocument();
      return $(cleanHtmlString(selectorOrHtml, doc));
    }
    return new GQuery(select(selectorOrHtml, ctx));
  }

  /**
   * This function accepts a string containing a CSS selector which is then used
   * to match a set of elements, or it accepts raw HTML creating a GQuery
   * element containing those elements. The second parameter is the context to
   * use for the selector. The third parameter is the class plugin to use.
   */
  @SuppressWarnings("unchecked")
  public static <T extends GQuery> T $(String selector, Node context,
      Class<T> plugin) {
    try {
      if (plugins != null) {
        T gquery = (T) plugins.get(plugin).init(new GQuery(select(selector, context)));
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
   * element containing those elements.
   * The second parameter is the context to use for the selector, or
   * the document where the new elements will be created.
   */
  public static GQuery $(String selectorOrHtml, Widget context) {
    return $(selectorOrHtml, context.getElement());
  }

  /**
   * This function accepts a string containing a CSS selector which is then used
   * to match a set of elements, or it accepts raw HTML creating a GQuery
   * element containing those elements. The second parameter is the context to
   * use for the selector. The third parameter is the class plugin to use.
   */
  public static <T extends GQuery> T $(String selector, Widget context,
      Class<T> plugin) {
    return $(selector, context.getElement(), plugin);
  }

  public static <T extends GQuery> T $(T gq) {
    return gq;
  }

  /**
   * Wrap a GQuery around an existing widget.
   */
  public static GQuery $(Widget w){
    return w == null ? $() : $(w.getElement());
  }

  /**
   * Wrap a GQuery around a array of existing widget.
   */
  public static <T extends Widget> GQuery $(T... widgets){
    return $(Arrays.asList(widgets));
  }

  /**
   * Wrap a GQuery around a List of existing widget.
   */
  public static <T extends Widget> GQuery $(List<T> widgets){
    JSArray elements = JSArray.create();
    for (Widget w : widgets){
      elements.addNode(w.getElement());
    }
    return $(elements);
  }

  /**
   * Wrap a JSON object.
   */
  public static Properties $$(String properties) {
    return Properties.create(properties);
  }

  @SuppressWarnings("unchecked")
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

  /**
   * Return a lazy version of the GQuery interface. Lazy function calls are
   * simply queued up and not executed immediately.
   */
  public static LazyGQuery<?> lazy() {
    return $().createLazy();
  }

  public static void registerPlugin(Class<? extends GQuery> plugin,
      Plugin<? extends GQuery> pluginFactory) {
    if (plugins == null) {
      plugins = JsMap.createObject().cast();
    }
    plugins.put(plugin, pluginFactory);
  }

  @SuppressWarnings("unchecked")
  protected static GQuery cleanHtmlString(String elem, Document doc) {
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
    // TODO: fix IE <script> tag
    Element div = doc.createDivElement();
    div.setInnerHTML(preWrap + elem + postWrap);
    Node n = div;
    while (wrapPos-- != 0) {
      n = n.getLastChild();
    }
    // TODO: add fixes for IE TBODY issue
    return $((NodeList<Element>) n.getChildNodes().cast());
  }

  protected static <S> Object data(Element item, String name, S value) {
    if (dataCache == null) {
      windowData = JavaScriptObject.createObject().cast();
      dataCache = JavaScriptObject.createObject().cast();
    }
    item = item == window || item.getNodeName() == null? windowData : item;
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
   * We will use the fact as GWT use the widget itself as EventListener !
   * If no Widget associated with the element, this method returns null.
   */
   protected static Widget getAssociatedWidget(Element e){
    EventListener listener = DOM.getEventListener((com.google.gwt.user.client.Element) e);
    
    //No listener attached to the element, so no widget exist for this element
    if (listener == null){
      return null;
    }
    if (listener instanceof Widget){
      //GWT uses the widget as event listener
      return (Widget)  listener;
    }else if (listener instanceof EventsListener){
      EventsListener gQueryListener = (EventsListener)listener;
      if (gQueryListener.getOriginalEventListener() != null && gQueryListener.getOriginalEventListener() instanceof Widget){
        return (Widget) gQueryListener.getOriginalEventListener();
      }
    }
    // I think it's not a good idea to generate ourself a new widget wrapping the element...
    // To be discussed
    return null;
  }

  private static JSArray copyNodeList(NodeList<? extends Node> n) {
    JSArray res = JSArray.create();
    for (int i = 0; i < n.getLength(); i++) {
      res.addNode(n.getItem(i));
    }
    return res;
  }

  private static native void emptyDocument(Document d) /*-{
    d.open(); d.write("<head/><body/>"); d.close();
  }-*/;

  private native static Document getContentDocument(Node n) /*-{
    var d =  n.contentDocument || n.contentWindow.document;
    if (!d.body) @com.google.gwt.query.client.GQuery::emptyDocument(Lcom/google/gwt/dom/client/Document;)(d);
    return d;
  }-*/;

  private static boolean hasClass(Element e, String clz) {
    return e.getClassName().matches("(^|.*\\s)" + clz + "(\\s.*|$)");
  }

  private static GQuery innerHtml(String html) {
    return $(cleanHtmlString(html, document));
  }

  private static native String[] jsArrayToString0(JsArrayString array) /*-{
    return array;
  }-*/;
  
  private static native void scrollIntoViewImpl(Node n) /*-{
    if (n) n.scrollIntoView()
  }-*/;

  private static native <T extends Node> T[] reinterpretCast(NodeList<T> nl) /*-{
    return nl;
  }-*/;

  private static NodeList<Element> select(String selector, Node context) {
    if (engine == null) {
      engine = new SelectorEngine();
    }
    NodeList<Element> n = engine.select(selector, context);
    JSArray res = copyNodeList(n);
    return res;
  }

  private static native Element window() /*-{
    return $wnd;
  }-*/;

  protected NodeList<Element> elements = JavaScriptObject.createArray().cast();

  private String currentSelector;

  private GQuery previousObject;

  public GQuery() {
  }

  public GQuery(Element element) {
    if (element != null) {
      elements =  JSArray.create(element);
    }
  }

  public GQuery(GQuery gq) {
    this(gq == null ? null : gq.get());
  }

  public GQuery(JSArray elements) {
    if (elements != null) {
      this.elements = elements;
    }
  }

  public GQuery(NodeList<Element> list) {
    if (list != null) {
      elements = list;
    }
  }

  /**
   * Add elements to the set of matched elements if they are not included yet.
   * It also update the selector appending the new one.
   */
  public GQuery add(GQuery previousObject) {
    return pushStack(unique(merge(elements, previousObject.elements)), "add",
        getSelector() + "," + previousObject.getSelector());
  }

  /**
   * Add elements to the set of matched elements if they are not included yet.
   */

  public GQuery add(String selector) {
    return add($(selector));
  }

  /**
   * Adds the specified classes to each matched element.
   */
  public GQuery addClass(String... classes) {
    for (Element e : elements()) {
      for (String clz : classes) {
        e.addClassName(clz);
      }
    }
    return this;
  }

  /**
   * Insert content after each of the matched elements. The elements must
   * already be inserted into the document (you can't insert an element after
   * another if it's not in the page).
   */
  public GQuery after(GQuery query) {
    return domManip(query, FUNC_AFTER);
  }

  /**
   * Insert content after each of the matched elements. The elements must
   * already be inserted into the document (you can't insert an element after
   * another if it's not in the page).
   */
  public GQuery after(Node n) {
    return domManip($(n), FUNC_AFTER);
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
  public GQuery append(GQuery query) {
    return domManip(query, FUNC_APPEND);
  }
  
  /**
   * Append content to the inside of every matched element. This operation is
   * similar to doing an appendChild to all the specified elements, adding them
   * into the document.
   */
  public GQuery append(Node n) {
    return domManip($(n), FUNC_APPEND);
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
   * All of the matched set of elements will be inserted at the end
   * of the element(s) specified by the parameter other.
   *
   * The operation $(A).appendTo(B) is, essentially, the reverse of doing a regular
   * $(A).append(B), instead of appending B to A, you're appending A to B.
   */
  public GQuery appendTo(GQuery other) {
    other.append(this);
    return this;
  }

  /**
   * All of the matched set of elements will be inserted at the end
   * of the element(s) specified by the parameter other.
   *
   * The operation $(A).appendTo(B) is, essentially, the reverse of doing a regular
   * $(A).append(B), instead of appending B to A, you're appending A to B.
   */
  public GQuery appendTo(Node n) {
    $(n).append(this);
    return this;
  }

  /**
   * All of the matched set of elements will be inserted at the end
   * of the element(s) specified by the parameter other.
   *
   * The operation $(A).appendTo(B) is, essentially, the reverse of doing a regular
   * $(A).append(B), instead of appending B to A, you're appending A to B.
   */
  public GQuery appendTo(String html) {
    $(html).append(this);
    return this;
  }

  /**
   * Convert to Plugin interface provided by Class literal.
   */
  @SuppressWarnings("unchecked")
  public <T extends GQuery> T as(Class<T> plugin) {
    // GQuery is not a plugin for itself
    if (plugin == GQUERY) {
      return (T) $(this);
    } else if (plugins != null) {
      Plugin<?> p = plugins.get(plugin);
      if (p != null) {
        return (T) p.init(this);
      }
    }
    throw new RuntimeException("No plugin registered for class " + plugin.getName());
  }

  /**
   * Return a GWT Widget containing the first matched element.
   * 
   * If the element is already associated to a widget it returns the original widget, 
   * otherwise a new GWT widget will be created depending on the tagName.
   * 
   */
  public Widget asWidget() {
    return as(Widgets).widget();
  }

  /**
   * Set a key/value object as properties to all matched elements.
   *
   * Example: $("img").attr(new Properties("src: 'test.jpg', alt: 'Test Image'"))
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
   * Access a property on the first matched element. This method makes it easy
   * to retrieve a property value from the first matched element. If the element
   * does not have an attribute with such a name, empty string is returned.
   * Attributes include title, alt, src, href, width, style, etc.
   */
  public String attr(String name) {
    return elements.getItem(0).getAttribute(name);
  }

  /**
   * Set a single property to a computed value, on all matched elements.
   */
  public GQuery attr(String key, Function closure) {
    for (int i = 0; i < elements.getLength(); i++) {
      Element e = elements.getItem(i);
      e.setAttribute(key, String.valueOf(closure.f(e, i)));
    }
    return this;
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
   * Insert content before each of the matched elements. The elements must
   * already be inserted into the document (you can't insert an element before
   * another if it's not in the page).
   */
  public GQuery before(GQuery query) {
    return domManip(query, FUNC_BEFORE);
  }

  /**
   * Insert content before each of the matched elements. The elements must
   * already be inserted into the document (you can't insert an element before
   * another if it's not in the page).
   */
  public GQuery before(Node n) {
    return domManip($(n), FUNC_BEFORE);
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
  public GQuery bind(int eventbits, final Object data, final Function...funcs) {
    return as(Events).bind(eventbits, data, funcs);
  }

  /**
   * Bind a set of functions to the blur event of each matched element.
   * Or trigger the event if no functions are provided.
   */
  public GQuery blur(Function...f) {
    return bindOrFire(Event.ONBLUR, null, f);
  }

  /**
   * Bind a set of functions to the change event of each matched element.
   * Or trigger the event if no functions are provided.
   */
  public GQuery change(Function...f) {
    return bindOrFire(Event.ONCHANGE, null, f);
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
   * Get a set of elements containing all of the unique children of each of the
   * matched set of elements. This set is filtered with the expressions that
   * will cause only elements matching any of the selectors to be collected.
   */
  public GQuery children(String... filters) {
    return find(filters);
  }

  /**
   * Bind a set of functions to the click event of each matched element.
   * Or trigger the event if no functions are provided.
   */
  public GQuery click(Function...f) {
    return bindOrFire(Event.ONCLICK, null, f);
  }

  /**
   * @deprecated use innerHeight()
   */
  @Deprecated
  public int clientHeight() {
    return innerHeight();
  }

  /**
   * @deprecated use innerWidth()
   */
  @Deprecated
  public int clientWidth() {
    return innerWidth();
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
      if (IFrameElement.is(e)){
        result.addNode(getContentDocument(e));
      } else {
        NodeList<Node> children = e.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
          result.addNode(children.getItem(i));
        }
      }
    }
    return new GQuery(unique(result));
  }

  public LazyGQuery<?> createLazy() {
    return GWT.create(GQuery.class);
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
   * Return a style property on the first matched element.
   */
  public String css(String name) {
    return styleImpl.curCSS(get(0), name, false);
  }
  
  /**
   * Return a style property on the first matched element using type-safe
   * enumerations.
   * 
   * Ex : $("#myId").css(CSS.BACKGROUND_COLOR);
   */
  public String css(CssProperty property) {
    return css(property, false);
  }

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
  public String css(CssProperty property, boolean force) {
    return css(property.getCssName(), force);
  }

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
  public String css(String name, boolean force) {
    return styleImpl.curCSS(get(0), name, force);
  }

  /**
   * Set a single style property to a value, on all matched elements.
   */
  public GQuery css(String prop, String val) {
    for (Element e : elements()) {
      styleImpl.setStyleProperty(e, prop, val);
    }
    return this;
  }

  /**
   * Set CSS a single style property on every matched element using type-safe
   * enumerations.
   * 
   * @deprecated use css(TakeCssValue.with(...)) instead
   */
  @Deprecated
  public <S extends HasCssName, T extends TakeCssValue<S>> GQuery css(T cssProperty, S value) {
    return setCss(cssProperty.with(value));
  }
  
  /**
   * Set CSS a single style property on every matched element using type-safe
   * enumerations. This method allows you to set manually the value or set
   * <i>inherit</i> value
   * 
   * ex :
   * <pre class="code">
   * $(#myId).css(CSS.TEXT_DECORATION, CSS.INHERIT);
   * </pre>
   */
  public GQuery css(TakeCssValue<?> cssProperty, String value) {
    return css(cssProperty.getCssName(), value);
  }

//  /**
//   * Set CSS a single style property on every matched element using type-safe
//   * enumerations.
//   * 
//   */
//  public GQuery css(TakesLength cssProperty, Length value) {
//    for (Element e : elements()) {
//      cssProperty.set(e.getStyle(), value);
//    }
//    return this;
//  }
//
//  /**
//   * Set a shorthand style property taking 3 values on every matched element using type-safe
//   * enumerations. ex : $("#id").css(CSS.BORDER, BorderWidth.thick(),
//   * BorderStyle.DASHED, RGBColor.BLACK);
//   */
//  public <X, Y, Z, T extends CssShorthandProperty3<X, Y, Z>> GQuery css(
//      T cssProperty, X value1, Y value2, Z value3) {
//    for (Element e : elements()) {
//      cssProperty.set(e.getStyle(), value1, value2, value3);
//    }
//    return this;
//  }
//  
//  /**
//   * Set a shorthand style property taking 3 values on every matched element using type-safe
//   * enumerations. ex : $("#id").css(CSS.BORDER, BorderWidth.thick(),
//   * BorderStyle.DASHED, RGBColor.BLACK);
//   */
//  public <W, X, Y, Z, T extends CssShorthandProperty4<W, X, Y, Z>> GQuery css(
//      T cssProperty, W value0, X value1, Y value2, Z value3) {
//    for (Element e : elements()) {
//      cssProperty.set(e.getStyle(), value0, value1, value2, value3);
//    }
//    return this;
//  }
//
//  /**
//   * Set a shorthand style property taking 5 values on every matched element using type-safe
//   * enumerations. ex : $("#id").css(CSS.BACKGROUND, RGBColor.TRANSPARENT,
//   * BackgroundImage.url("back.jpg"), BackgroundRepeat.NO_REPEAT,
//   * BackgroundAttachment.SCROLL, BackgroundPosition.CENTER);
//   */
//  public <V, W, X, Y, Z, T extends CssShorthandProperty5<V, W, X, Y, Z>> GQuery css(
//      T cssProperty, V value1, W value2, X value3, Y value4, Z value5) {
//    for (Element e : elements()) {
//      cssProperty.set(e.getStyle(), value1, value2, value3, value4, value5);
//    }
//    return this;
//  }

  /**
   * Returns the numeric value of a css property.
   */
  public double cur(String prop) {
    return cur(prop, false);
  }
  
  /**
   * Returns the numeric value of a css property.
   *  
   * The parameter force has a special meaning:
   * - When force is false, returns the value of the css property defined
   *   in the set of style attributes. 
   * - When true returns the real computed value.   
   */
  public double cur(String prop, boolean force) {
    return size() == 0 ? 0 : styleImpl.cur(get(0), prop, force);
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
  @SuppressWarnings("unchecked")
  public <T> T data(String name, Class<T> clz) {
    return (T) data(elements.getItem(0), name, null);
  }

  /**
   * Stores the value in the named spot with desired return type.
   */
  public GQuery data(String name, Object value) {
    for (Element e : elements()) {
      data(e, name, value);
    }
    return this;
  }

  /**
   * Bind a set of functions to the dblclick event of each matched element.
   * Or trigger the event if no functions are provided.
   */
  public GQuery dblclick(Function...f) {
    return bindOrFire(Event.ONDBLCLICK, null, f);
  }

  /**
   * Run one or more Functions over each element of the GQuery.
   * You have to override one of these funcions:
   *    public void f(Element e)
   *    public String f(Element e, int i)
   */
  public GQuery each(Function... f) {
    if (f != null) {
      for (Function f1 : f) {
        for (int i = 0; i < elements.getLength(); i++) {
          f1.f(elements.getItem(i), i);
        }
      }
    }
    return this;
  }

  /**
   * Returns the working set of nodes as a Java array. <b>Do NOT</b> attempt to
   * modify this array, e.g. assign to its elements, or call Arrays.sort()
   */
  public Element[] elements() {
    return asArray(elements);
  }

  /**
   * Remove all child nodes from the set of matched elements.
   * In the case of a document element, it removes all the content
   * You should call this method whenever you create a new iframe and you
   * want to add dynamic content to it.
   */
  public GQuery empty() {
    // TODO: add memory leak cleanup, remove event handlers, and
    // data caches
    for (Element e : elements()) {
      if (e.getNodeType() == Element.DOCUMENT_NODE) {
        emptyDocument(e.<Document>cast());
      } else {
        while (e.getFirstChild() != null) {
          e.removeChild(e.getFirstChild());
        }
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
    return $(get(pos));
  }

  /**
   * Bind a set of functions to the error event of each matched element.
   * Or trigger the event if no functions are provided.
   */
  public GQuery error(Function... f) {
    return bindOrFire(Event.ONERROR, null, f);
  }

  /**
   * Fade in all matched elements by adjusting their opacity. The effect will
   * take 1000 milliseconds to complete
   */
  public GQuery fadeIn(Function... f) {
    return $(as(Effects).fadeIn(f));
  }

  /**
   * Fade in all matched elements by adjusting their opacity.
   */
  public GQuery fadeIn(int millisecs, Function... f) {
    return $(as(Effects).fadeIn(millisecs, f));
  }

  /**
   * Fade out all matched elements by adjusting their opacity. The effect will
   * take 1000 milliseconds to complete
   */
  public GQuery fadeOut(Function... f) {
    return $(as(Effects).fadeOut(f));
  }

  /**
   * Fade out all matched elements by adjusting their opacity.
   */
  public GQuery fadeOut(int millisecs, Function... f) {
    return as(Effects).fadeOut(millisecs, f);
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
   * Reduce the set of matched elements to the first in the set.
   */
  public GQuery first() {
    return eq(0);
  }

  /**
   * Bind a set of functions to the focus event of each matched element.
   * Or trigger the event if no functions are provided.
   */
  public GQuery focus(Function...f) {
    return bindOrFire(Event.ONFOCUS, null, f);
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
    int l = elements.getLength();
    return i >= 0 && i < l? elements.getItem(i) : null;
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
   * Get the current computed, pixel, height of the first matched element.
   * It does not include margin, padding nor border.
   */
  public int height() {
    return (int)cur("height", true);
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
   * Make invisible all matched elements.
   */
  public GQuery hide() {
    for (Element e : elements()) {
      Object old = data(e, "oldDisplay", null);
      if (old == null) {
        data(e, "oldDisplay", styleImpl.curCSS(e, "display", false));
      }
      e.getStyle().setDisplay(Display.NONE);
    }
    return this;
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
    return bind(Event.ONMOUSEOVER, null, fover).bind(Event.ONMOUSEOUT, null, fout);
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
      if (e.getNodeType() == Node.DOCUMENT_NODE) {
        e = e.<Document>cast().getBody();
      }
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
   * Returns the inner height of the first matched element, including padding
   * but not the vertical scrollbar height, border, or margin.
   */
  public int innerHeight() {
    return get(0).getClientHeight();
  }

  /**
   * Returns the inner width of the first matched element, including padding
   * but not the vertical scrollbar width, border, or margin.
   */
  public int innerWidth() {
    return get(0).getClientWidth();
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
   * Insert all of the matched elements after another, specified, set of
   * elements.
   */
  public GQuery insertAfter(String selector) {
    return insertAfter($(selector));
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
   * Bind a set of functions to the keydown event of each matched element.
   * Or trigger the event if no functions are provided.
   */
  public GQuery keydown(Function...f) {
    return bindOrFire(Event.ONKEYDOWN, null, f);
  }

  /**
   * Trigger a keydown event passing the key pushed.
   */
  public GQuery keydown(int key) {
    return trigger(Event.ONKEYDOWN, key);
  }

  /**
   * Bind a set of functions to the keypress event of each matched element.
   * Or trigger the event if no functions are provided.
   */
  public GQuery keypress(Function...f) {
    return bindOrFire(Event.ONKEYPRESS, null, f);
  }

  /**
   * Trigger a keypress event passing the key pushed.
   */
  public GQuery keypress(int key) {
    return trigger(Event.ONKEYPRESS, key);
  }

  /**
   * Bind a set of functions to the keyup event of each matched element.
   * Or trigger the event if no functions are provided.
   */
  public GQuery keyup(Function...f) {
    return bindOrFire(Event.ONKEYUP, null, f);
  }

  /**
   * Trigger a keyup event passing the key pushed.
   */
  public GQuery keyup(int key) {
    return trigger(Event.ONKEYUP, key);
  }
  
  /**
   * Reduce the set of matched elements to the final one in the set.
   */
  public GQuery last() {
    return eq(size() - 1);
  }

  /**
   * Returns the computed left position of the first element matched.
   */
  public int left() {
    return (int)cur("left", true);
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
   * Pass each element in the current matched set through a function, 
   * producing a new array containing the return values.
   */
  @SuppressWarnings("unchecked")
  public <W> ArrayList<W> map(Function f) {
    @SuppressWarnings("rawtypes")
    ArrayList ret = new ArrayList();
    for (int i = 0; i < elements().length; i++) {
      Object o = f.f(elements()[i], i);
      if (o != null) {
        ret.add(o);
      }
    }
    return ret;
  }
  
  /**
   * Bind a set of functions to the mousedown event of each matched element.
   * Or trigger the event if no functions are provided.
   */
  public GQuery mousedown(Function...f) {
    return bindOrFire(Event.ONMOUSEDOWN, null, f);
  }

  /**
   * Bind a set of functions to the mousemove event of each matched element.
   * Or trigger the event if no functions are provided.
   */
  public GQuery mousemove(Function...f) {
    return bindOrFire(Event.ONMOUSEMOVE, null, f);
  }

  /**
   * Bind a set of functions to the mouseout event of each matched element.
   * Or trigger the event if no functions are provided.
   */
  public GQuery mouseout(Function...f) {
    return bindOrFire(Event.ONMOUSEOUT, null, f);
  }

  /**
   * Bind a set of functions to the mouseover event of each matched element.
   * Or trigger the event if no functions are provided.
   */
  public GQuery mouseover(Function...f) {
    return bindOrFire(Event.ONMOUSEOVER, null, f);
  }

  /**
   * Bind a set of functions to the mouseup event of each matched element.
   * Or trigger the event if no functions are provided.
   */
  public GQuery mouseup(Function...f) {
    return bindOrFire(Event.ONMOUSEUP, null, f);
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
   * the document. The returned object contains two integer properties, top and
   * left. The method works only with visible elements.
   */
  public com.google.gwt.query.client.GQuery.Offset offset() {
    return new Offset(get(0).getAbsoluteLeft(), get(0).getAbsoluteTop());
  } 

  /**
   * Returns a GQuery collection with the positioned parent of the first matched
   * element. This is the first parent of the element that has position (as in
   * relative or absolute). This method only works with visible elements.
   */
  public GQuery offsetParent() {
    Element offParent = GQUtils.or(elements.getItem(0).getOffsetParent(), body);
    while (offParent != null
        && !"body".equalsIgnoreCase(offParent.getTagName())
        && !"html".equalsIgnoreCase(offParent.getTagName())
        && "static".equals(styleImpl.curCSS(offParent, "position", true))) {
      offParent = offParent.getOffsetParent();
    }
    return new GQuery(offParent);
  }

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
  public GQuery one(int eventbits, final Object data, final Function f) {
    return as(Events).one(eventbits, data, f);
  }

  /**
   * Get the current computed height for the first element in the set of matched elements, 
   * including padding, border, but not the margin.
   */
  public int outerHeight(){
    return outerHeight(false);
  }

  /**
   * Get the current computed height for the first element in the set of matched elements, 
   * including padding, border, and optionally margin.
   */
  public int outerHeight(boolean includeMargin){
    int outerHeight  = get(0).getOffsetHeight(); //height including padding and border
    if (includeMargin){
      outerHeight += cur("marginTop", true) + cur("marginBottom", true);
    }
    return  outerHeight;
  }

  /**
   * Get the current computed width for the first element in the set of matched elements, 
   * including padding, border, but not the margin.
   */
  public int outerWidth(){
    return outerWidth(false);
  }

  /**
   * Get the current computed width for the first element in the set of matched elements, 
   * including padding and border and optionally margin.
   */
  public int outerWidth(boolean includeMargin){
    int outerWidth  = get(0).getOffsetWidth(); //width including padding and border
    if (includeMargin){
      outerWidth += cur("marginRight", true) + cur("marginLeft", true);
    }
    return  outerWidth;
  }

  /**
   * Get a set of elements containing the unique parents of the matched set of
   * elements.
   */
  public GQuery parent() {
    JSArray result = JSArray.create();
    for (Element e : elements()) {
      Element p = e.getParentElement();
      if (p != null) {
        result.addNode(p);
      }
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
   * elements (except for the root element).
   */
  public GQuery parents() {
    JSArray result = JSArray.create();
    for (Element e : elements()) {
      Node par = e.getParentNode();
      while (par != null && par != document) {
        result.addNode(par);
        par = par.getParentNode();
      }
    }
    return new GQuery(unique(result));
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
   * Gets the top and left position of an element relative to its offset parent.
   * The returned object contains two Integer properties, top and left. For
   * accurate calculations make sure to use pixel values for margins, borders
   * and padding. This method only works with visible elements.
   */
  public com.google.gwt.query.client.GQuery.Offset position() {
    Element element = get(0);
    if (element == null) {
      return null;
    }
    // Get *real* offsetParent
    Element offsetParent = element.getOffsetParent();
    // Get correct offsets
    Offset offset = offset();
    Offset parentOffset = null;
    if (offsetParent == body || offsetParent == (Node)document) {
      parentOffset = new Offset(0, 0);
    } else {
      parentOffset = $(offsetParent).offset();
    }

    // Subtract element margins
    int topMargin = (int)styleImpl.cur(element, "marginTop", true);
    // TODO: move this check to styleImpl
    // When margin-left = auto, Safari and chrome return a value while IE and
    // Firefox return 0
    // force the margin-left to 0 if margin-left = auto.
    int leftMargin = 0;
    if (!"auto".equals(element.getStyle().getMarginLeft())) {
      leftMargin = (int)styleImpl.cur(element, "marginLeft", true);
    }

    offset = offset.add(-leftMargin, -topMargin);

    // Add offsetParent borders
    int parentOffsetBorderTop = (int)styleImpl.cur(offsetParent,
        "borderTopWidth", true);
    int parentOffsetBorderLeft = (int)styleImpl.cur(offsetParent,
        "borderLeftWidth", true);
    parentOffset = parentOffset.add(parentOffsetBorderLeft,
        parentOffsetBorderTop);
        
    // Subtract the two offsets
    return offset.add(-parentOffset.left, -parentOffset.top);
  }

  /**
   * Prepend content to the inside of every matched element. This operation is
   * the best way to insert elements inside, at the beginning, of all matched
   * elements.
   */
  public GQuery prepend(GQuery query) {
    return domManip(query, FUNC_PREPEND);
  }

  /**
   * Prepend content to the inside of every matched element. This operation is
   * the best way to insert elements inside, at the beginning, of all matched
   * elements.
   */
  public GQuery prepend(Node n) {
    return domManip($(n), FUNC_PREPEND);
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
   * All of the matched set of elements will be inserted at the beginning
   * of the element(s) specified by the parameter other.
   *
   * The operation $(A).prependTo(B) is, essentially, the reverse of doing a regular
   * $(A).prepend(B), instead of prepending B to A, you're prepending A to B.
   */
  public GQuery prependTo(GQuery other) {
    other.prepend(this);
    return this;
  }

  /**
   * All of the matched set of elements will be inserted at the beginning
   * of the element(s) specified by the parameter other.
   *
   * The operation $(A).prependTo(B) is, essentially, the reverse of doing a regular
   * $(A).prepend(B), instead of prepending B to A, you're prepending A to B.
   */
  public GQuery prependTo(Node n) {
    $(n).prepend(this);
    return this;
  }

  /**
   * All of the matched set of elements will be inserted at the beginning
   * of the element(s) specified by the parameter other.
   *
   * The operation $(A).prependTo(B) is, essentially, the reverse of doing a regular
   * $(A).prepend(B), instead of prepending B to A, you're prepending A to B.
   */
  public GQuery prependTo(String html) {
    $(html).prepend(this);
    return this;
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
        e.removeClassName(clz);
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
  public GQuery replaceAll(Element elem) {
    return replaceAll($(elem));
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
   * Replaces all matched elements with the specified HTML or DOM elements. This
   * returns the GQuery element that was just replaced, which has been removed
   * from the DOM.
   */
  public GQuery replaceWith(Element elem) {
    return replaceWith($(elem));
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
   * Save a set of Css properties of every matched element.
   */
  public void restoreCssAttrs(String... cssProps) {
    for (Element e : elements()) {
      for (String a : cssProps) {
        styleImpl.setStyleProperty(e, a, (String) data(e, OLD_DATA_PREFIX + a, null));
      }
    }
  }

  /**
   * Restore a set of previously saved Css properties in every matched element.
   */
  public void saveCssAttrs(String... cssProps) {
    for (Element e : elements()) {
      for (String a : cssProps) {
        data(OLD_DATA_PREFIX + a, styleImpl.curCSS(e, a, false));
      }
    }
  }

  /**
   * Bind a set of functions to the scroll event of each matched element.
   * Or trigger the event if no functions are provided.
   */
  public GQuery scroll(Function...f) {
    return bindOrFire(Event.ONSCROLL, null, f);
  }
  
  /**
   * Scrolls the first matched element into view.
   */
  public GQuery scrollIntoView() {
    scrollIntoViewImpl(get(0));
    return this;
  }

  /**
   * Scrolls the first matched element into view.
   * 
   * If ensure == true, it crawls up the DOM hierarchy, adjusting the scrollLeft and
   * scrollTop properties of each scroll-able element to ensure that the
   * specified element is completely in view. It adjusts each scroll position by
   * the minimum amount necessary. 
   */
  public GQuery scrollIntoView(boolean ensure) {
    if (ensure) {
      DOM.scrollIntoView((com.google.gwt.user.client.Element)get(0));
    } else {
      scrollIntoView();
    }
    return this;
  }

  /**
   * Gets the scroll left offset of the first matched element. This method works
   * for both visible and hidden elements.
   */
  public int scrollLeft() {
    Element e = get(0);
    if (e == window || e.getNodeName() == null) {
      return Window.getScrollLeft();
    } else if (e == (Node) document) {
      return document.getScrollLeft();
    } else {
      return e.getScrollLeft();
    }
  }

  /**
   * The scroll left offset is set to the passed value on
   * all matched elements. This method works for both visible and hidden
   * elements.
   */
  public GQuery scrollLeft(int left) {
    for (Element e : elements()) {
      if (e == window || e.getNodeName() == null || e == (Node) document) {
        Window.scrollTo(left, $(e).scrollTop());
      } else {
        e.setPropertyInt("scrollLeft", left);
      }
    }
    return this;
  }
  
  /**
   * 
   * Scrolls the contents of all matched elements to the specified co-ordinate 
   * becoming the top left corner of the viewable area. 
   * 
   * This method is only useful where there are areas of the document not viewable within 
   * the current viewable area of the window and the visible property 
   * of the window's scrollbar must be set to true. 
   * 
   */
  public GQuery scrollTo(int left, int top) {
    scrollLeft(left).scrollTop(top);
    return this;
  }

  /**
   * Gets the scroll top offset of the first matched element. This method works
   * for both visible and hidden elements.
   */
  public int scrollTop() {
    Element e = get(0);
    if (e == window || e.getNodeName() == null) {
      return Window.getScrollTop();
    } else if (e == (Node) document) {
      return document.getScrollTop();
    } else {
      return e.getScrollTop();
    }
  }

  /**
   * The scroll top offset is set to the passed value on
   * all matched elements. This method works for both visible and hidden
   * elements.
   */
  public GQuery scrollTop(int top) {
    for (Element e : elements()) {
      if (e == window || e.getNodeName() == null || e == (Node) document) {
        Window.scrollTo($(e).scrollLeft(), top);
      } else {
        e.setPropertyInt("scrollTop", top);
      }
    }
    return this;
  }

  public GQuery select() {
    return as(Events).triggerHtmlEvent("select");
  }

  /**
   * Force the current matched set of elements to become
   * the specified array of elements.
   */
  public GQuery setArray(NodeList<Element> nodes) {
    this.elements = nodes;
    return this;
  }

  /**
   * Set CSS a single style property on every matched element using type-safe
   * enumerations.
   * 
   * The best way to use this method (i.e. to generate a CssSetter) is to take
   * the desired css property defined in {@link CSS} class and call the
   * {@link TakeCssValue#with(HasCssName)} method on it.
   * 
   *  ex :
   *  
   *  $("#myDiv").css(CSS.TOP.with(Length.cm(15)));
   *  $("#myDiv").css(CSS.BACKGROUND.with(RGBColor.SILVER, ImageValue.url(""), BackgroundRepeat.NO_REPEAT, BackgroundAttachment.FIXED, BackgroundPosition.CENTER)); 
   *  $("#myDiv").css(CSS.BACKGROUND_ATTACHMENT.with(BackgroundAttachment.FIXED));
   * 
   */
  public GQuery setCss(CssSetter cssSetter) {
    for (Element e : elements()) {
      cssSetter.applyCss(e);
    }
    return this;
  }
//  public <S, T extends TakeCssValue<S>> GQuery setCss(T cssProperty, S value) {
//    cssProperty.set(elements.getItem(0).getStyle(), value);
//    return this;
//  }
//
//  /**
//   * Set CSS property on first matched element using type-safe enumerations.
//   */
//  public GQuery setCss(TakesLength cssProperty, Length value) {
//    cssProperty.set(elements.getItem(0).getStyle(), value);
//    return this;
//  }
//
//  /**
//   * Set a multiple style property on first matched element using type-safe
//   * enumerations.
//   * 
//   * ex : $("#id").css(CSS.BORDER, BorderWidth.thick(), BorderStyle.DASHED,
//   * RGBColor.BLACK);
//   */
//  public <X, Y, Z, T extends CssShorthandProperty3<X, Y, Z>> GQuery setCss(
//      T cssProperty, X value1, Y value2, Z value3) {
//    cssProperty.set(get(0).getStyle(), value1, value2, value3);
//
//    return this;
//  }
//
//  /**
//   * Set a multiple style property on first matched element using type-safe
//   * enumerations.
//   * 
//   * ex : $("#id").css(CSS.BACKGROUND, RGBColor.TRANSPARENT,
//   * BackgroundImage.url("back.jpg"), BackgroundRepeat.NO_REPEAT,
//   * BackgroundAttachment.SCROLL, BackgroundPosition.CENTER);
//   */
//  public <V, W, X, Y, Z, T extends CssShorthandProperty5<V, W, X, Y, Z>> GQuery setCss(
//      T cssProperty, V value1, W value2, X value3, Y value4, Z value5) {
//    cssProperty.set(get(0).getStyle(), value1, value2, value3, value4, value5);
//
//    return this;
//  }

  public void setPreviousObject(GQuery previousObject) {
    this.previousObject = previousObject;
  }

  public void setSelector(String selector) {
    this.currentSelector = selector;
  }

  /**
   * Make all matched elements visible
   */
  public GQuery show() {
    for (Element e : elements()) {
      styleImpl.setStyleProperty(e, "display",
          GQUtils.or((String) data(e, "oldDisplay", null), ""));
      // When the display=none is in the stylesheet.
      if (!styleImpl.isVisible(e)) {
        styleImpl.setStyleProperty(e, "display", "block");
      }
    }
    return this;
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
    return as(Events).trigger(EventsListener.ONSUBMIT);
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
    for (Element e : elements()) {
      if ($(e).visible()) {
        $(e).hide();
      } else {
        $(e).show();
        e.getStyle().setDisplay(Display.BLOCK);
      }
    }
    return this;
  }

  /**
   * Toggle among two or more function calls every other click.
   */
  public GQuery toggle(final Function... fn) {
    for(Element e: elements()) {
      $(e).click(new Function(){
        int click = 0;
        public boolean f(Event e) {
          int n = fn.length == 1 ? 0 : (click++ % fn.length);
          return fn[n].f(e);
        }
      });
    }
    return this;
  }

  /**
   * Adds or removes the specified classes to each matched element
   * depending on the class's presence.
   */
  public GQuery toggleClass(String... classes) {
    for (Element e : elements()) {
      for (String clz : classes) {
        if (hasClass(e, clz)) {
          e.removeClassName(clz);
        } else {
          e.addClassName(clz);
        }
      }
    }
    return this;
  }

  /**
   * Adds or removes the specified classes to each matched element
   * depending on the value of the switch argument.
   *
   * if addOrRemove is true, the class is added and in the case of
   * false it is removed.
   */
  public GQuery toggleClass(String clz, boolean addOrRemove) {
    if (addOrRemove) {
      addClass(clz);
    } else {
      removeClass(clz);
    }
    return this;
  }

  /**
   * Returns the computed left position of the first element matched.
   */
  public int top() {
    return (int)cur("top", true);
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
      if (window.equals(e)) {
        continue;
      }
      r += (pretty && r.length() > 0 ? "\n " : "") + e.getString();
    }
    return r;
  }

  /**
   * Trigger a set of events on each matched element.
   *
   * For keyboard events you can pass a second parameter which represents
   * the key-code of the pushed key.
   *
   * Example: fire(Event.ONCLICK | Event.ONFOCUS)
   * Example: fire(Event.ONKEYDOWN. 'a');
   */
  public GQuery trigger(int eventbits, int... keys) {
    return as(Events).trigger(eventbits, keys);
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
    return GQUtils.unique(result.<JsArray<Element>>cast()).cast();
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
        s.setSelectedIndex(-1);
        if (values.length > 1 && s.isMultiple()) {
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
        if ("radio".equalsIgnoreCase((type))
            || "checkbox".equalsIgnoreCase(type)) {
          if ("checkbox".equalsIgnoreCase(type)) {
            for (String val : values) {
              if (ie.getValue().equals(val)) {
                ie.setChecked(true);
              } else {
                ie.setChecked(false);
              }
            }
          } else if (ie.getValue().equals(values[0])) {
            ie.setChecked(true);
          } else {
            ie.setChecked(false);
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
    return styleImpl.isVisible(get(0));
  }

  /**
   * Return the first non null attached widget from the matched elements
   * or null if there isn't any.
   */
  public <W extends Widget> W widget(){
    for (Element e : elements()){
      @SuppressWarnings("unchecked")
      W w = (W) getAssociatedWidget(e);
      if (w != null){
        return w;
      }
    }
    return null;
  }

  /**
   * return the list of attached widgets matching the query
   */
  public List<Widget> widgets(){
    List<Widget> widgets = new ArrayList<Widget>();
    for (Element e : elements()){
      Widget w = getAssociatedWidget(e);
      if (w != null){
        widgets.add(w);
      }
    }
   return widgets;
  }

  /**
   * Return the list of attached widgets instance of the provided class matching the query.
   * 
   * This method is very useful for decoupled views, so as we can access widgets from other
   * views without maintaining methods which export them.
   *  
   */
  @SuppressWarnings("unchecked")
  public <W extends Widget> List<W> widgets(Class<W> clazz) {
    List<W> ret = new ArrayList<W>();
    for (Widget w: widgets()) {
      // isAssignableFrom does not work in gwt.
      Class<?> c = w.getClass();
      do {
        if (c.equals(clazz)) {
          ret.add((W)w);
          break;
        }
        c = c.getSuperclass();
      } while (c != null);
    }
   return ret;
  }

  /**
   * Get the current computed, pixel, width of the first matched element.
   * It does not include margin, padding nor border.
   */
  public int width() {
    return (int)cur("width", true);
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
  
  /**
   * Bind Handlers or fire Events for each matched element.
   */
  private GQuery bindOrFire(int eventbits, final Object data, final Function...funcs) {
    if (funcs.length == 0) {
      return trigger(eventbits);
    } else {
      return bind(eventbits, data, funcs);
    }
  }
  
  private GQuery domManip(GQuery g, int func, Element...elms) {
    JSArray newNodes = JSArray.create();
    if (elms.length == 0) {
      elms = elements();
    }
    for (Element e: elms) {
      e.getOwnerDocument();
      if (e.getNodeType() == Node.DOCUMENT_NODE) {
        e = e.<Document>cast().getBody();
      }
      for (int j = 0; j < g.size(); j++) {
        Node n = g.get(j);
        if (g.size() > 1) {
          n = n.cloneNode(true);
        }
        switch (func) {
          case FUNC_PREPEND:
            newNodes.addNode(e.insertBefore(n, e.getFirstChild()));
            break;
          case FUNC_APPEND:
            newNodes.addNode(e.appendChild(n));
            break;
          case FUNC_AFTER:
            newNodes.addNode(e.getParentNode().insertBefore(n, e.getNextSibling()));
            break;
          case FUNC_BEFORE:
            newNodes.addNode(e.getParentNode().insertBefore(n, e));
            break;
        }
      }
    }
    if (newNodes.size() > g.size()) {
      g.setArray(newNodes);
    }
    return this;
  }
  
  private GQuery domManip(String htmlString, int func) {
    HashMap<Document, GQuery> cache = new HashMap<Document, GQuery>();
    for (Element e: elements()) {
      Document d = e.getNodeType() == Node.DOCUMENT_NODE ? e.<Document>cast() : e.getOwnerDocument();
      GQuery g = cache.get(d);
      if (g == null) {
        g = cleanHtmlString(htmlString, d);
        cache.put(d, g);
      }
      domManip(g.clone(), func, e);
    }
    return this;
  }
  
  private native Element getPreviousSiblingElement(Element elem)  /*-{
    var sib = elem.previousSibling;
    while (sib && sib.nodeType != 1)
      sib = sib.previousSibling;
    return sib;
  }-*/;
  
  private JSArray merge(NodeList<Element> first, NodeList<Element> second) {
    JSArray res = copyNodeList(first);
    for (int i = 0; i < second.getLength(); i++) {
      res.addNode(second.getItem(i));
    }
    return res;
  }
  
  private void removeData(Element item, String name) {
    if (dataCache == null) {
      windowData = JavaScriptObject.createObject().cast();
      dataCache = JavaScriptObject.createObject().cast();
    }
    item = item == window || item.getNodeName() == null? windowData : item;
    int id = item.hashCode();
    if (name != null) {
      if (dataCache.exists(id)) {
        dataCache.getCache(id).delete(name);
      }
      if (dataCache.getCache(id).isEmpty()) {
        removeData(item, null);
      }
    } else {
      dataCache.delete(id);
    }
  }
}
