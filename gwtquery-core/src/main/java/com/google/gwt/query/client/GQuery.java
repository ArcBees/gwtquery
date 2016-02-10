/*
 * Copyright 2011, The gwtquery team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.gwt.query.client;

import static com.google.gwt.query.client.plugins.QueuePlugin.Queue;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayMixed;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.dom.client.BodyElement;
import com.google.gwt.dom.client.ButtonElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.OptionElement;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.TextAreaElement;
import com.google.gwt.query.client.builders.JsonBuilder;
import com.google.gwt.query.client.css.HasCssValue;
import com.google.gwt.query.client.css.TakesCssValue;
import com.google.gwt.query.client.css.TakesCssValue.CssSetter;
import com.google.gwt.query.client.impl.AttributeImpl;
import com.google.gwt.query.client.impl.DocumentStyleImpl;
import com.google.gwt.query.client.impl.SelectorEngine;
import com.google.gwt.query.client.js.JsCache;
import com.google.gwt.query.client.js.JsMap;
import com.google.gwt.query.client.js.JsNamedArray;
import com.google.gwt.query.client.js.JsNodeArray;
import com.google.gwt.query.client.js.JsObjectArray;
import com.google.gwt.query.client.js.JsUtils;
import com.google.gwt.query.client.plugins.Effects;
import com.google.gwt.query.client.plugins.Events;
import com.google.gwt.query.client.plugins.Plugin;
import com.google.gwt.query.client.plugins.Widgets;
import com.google.gwt.query.client.plugins.ajax.Ajax;
import com.google.gwt.query.client.plugins.ajax.Ajax.Settings;
import com.google.gwt.query.client.plugins.deferred.Deferred;
import com.google.gwt.query.client.plugins.deferred.PromiseFunction;
import com.google.gwt.query.client.plugins.effects.PropertiesAnimation.Easing;
import com.google.gwt.query.client.plugins.events.EventsListener;
import com.google.gwt.query.client.plugins.widgets.WidgetsUtils;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * GwtQuery is a GWT clone of the popular jQuery library.
 */
public class GQuery implements Lazy<GQuery, LazyGQuery> {

  private enum DomMan {
    AFTER, APPEND, BEFORE, PREPEND;
  }

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
   * Class used internally to create DOM element from html snippet.
   */
  private static class TagWrapper {
    public static final TagWrapper DEFAULT = new TagWrapper(0, "", "");
    private String postWrap;
    private String preWrap;
    private int wrapDepth;

    public TagWrapper(int wrapDepth, String preWrap, String postWrap) {
      this.wrapDepth = wrapDepth;
      this.postWrap = postWrap;
      this.preWrap = preWrap;
    }
  }

  /**
   * Implementation class to modify attributes.
   */
  protected static AttributeImpl attributeImpl;

  /**
   * The body element in the current page.
   */
  public static final BodyElement body = GWT.isClient() ? Document.get().getBody() : null;

  /**
   * A Browser object with a set of browser-specific flags.
   */
  public static final Browser browser = GWT.isClient() ? GWT.<Browser>create(Browser.class) : null;

  /**
   * An object with the same methods than window.console.
   */
  public static final Console console = GWT.isClient() ? GWT.<Console>create(Console.class) : null;

  /**
   * Object to store element data (public so as we can access to it from tests).
   */
  public static JsCache dataCache = null;

  /**
   * The document element in the current page.
   */
  public static final Document document = GWT.isClient() ? Document.get() : null;

  /**
   * Static reference Effects plugin.
   */
  public static Class<Effects> Effects = com.google.gwt.query.client.plugins.Effects.Effects;

  /**
   * Implementation engine used for CSS selectors.
   */
  protected static SelectorEngine engine;

  /**
   * Static reference Events plugin.
   */
  public static Class<Events> Events = com.google.gwt.query.client.plugins.Events.Events;

  /**
   * A static reference to the GQuery class.
   */
  public static Class<GQuery> GQUERY = GQuery.class;

  private static final String OLD_DATA_PREFIX = "old-";

  private static final String OLD_DISPLAY = OLD_DATA_PREFIX + "display";

  private static JsMap<Class<? extends GQuery>, Plugin<? extends GQuery>> plugins;

  // Sizzle POS regex : usefull in some methods
  // TODO: Share this static with SelectorEngineSizzle
  private static RegExp posRegex = RegExp
      .compile("^:(nth|eq|gt|lt|first|last|even|odd)(?:\\((\\d*)\\))?(?=[^\\-]|$)$");

  /**
   * Implementation class used for style manipulations.
   */
  private static DocumentStyleImpl styleImpl;

  private static RegExp tagNameRegex = RegExp.compile("<([\\w:-]+)");

  /**
   * Static reference to the Widgets plugin.
   */
  public static Class<Widgets> Widgets = com.google.gwt.query.client.plugins.Widgets.Widgets;

  /**
   * The window object.
   */
  public static final Element window = GWT.isClient() ? ScriptInjector.TOP_WINDOW.<Element> cast()
      : null;

  private static Element windowData = null;

  private static JsNamedArray<TagWrapper> wrapperMap;

  /**
   * Create an empty GQuery object.
   */
  public static GQuery $() {
    return new GQuery(JsNodeArray.create());
  }

  /**
   * Wrap a GQuery around any object, supported objects are:
   *   String, GQuery, Function, Widget, JavaScriptObject.
   *
   * In the case of string, we accept a CSS selector which is then used to match a set of
   * elements, or a raw HTML to create a GQuery element containing those elements. Xpath
   * selector is supported in browsers with native xpath engine.
   *
   * In the case of a JavaScriptObject we handle:
   *   Element, Event, Node, Nodelist, Function, and native functions or arrays.
   *
   * If the case of a native function, we execute it and return empty.
   */
  public static GQuery $(Object o) {
    if (o != null) {
      if (o instanceof String) {
        return $((String) o);
      }
      if (o instanceof SafeHtml) {
        return $(((SafeHtml) o).asString());
      }
      if (o instanceof GQuery) {
        return (GQuery) o;
      }
      if (o instanceof Function) {
        return new GQuery(((Function) o).getElement());
      }
      if (JsUtils.isElement(o)) {
        return new GQuery(JsUtils.<Element> cast(o));
      }
      if (o instanceof JsonBuilder) {
        return new GQuery(((JsonBuilder) o).<Element>getDataImpl());
      }
      if (o instanceof IsWidget) {
        return $(Arrays.asList(o));
      }
      if (o instanceof JavaScriptObject) {
        JavaScriptObject jso = (JavaScriptObject) o;
        // Execute a native javascript function like jquery does
        if (JsUtils.isFunction(jso)) {
          new JsUtils.JsFunction(jso).fe();
          return $();
        }
        // Wraps a native array like jquery does
        if (!JsUtils.isWindow(jso) && !JsUtils.isElement(jso) && JsUtils.isArray(jso)) {
          JsArrayMixed c = jso.cast();
          JsNodeArray elms = JsNodeArray.create();
          for (int i = 0; i < c.length(); i++) {
            Object obj = c.getObject(i);
            if (obj instanceof Node) {
              elms.addNode((Node) obj);
            }
          }
          return new GQuery(elms);
        }
        // Wraps a native NodeList
        if (JsUtils.isNodeList(jso)) {
          return new GQuery(jso.<NodeList<Element>> cast());
        }
        // Wraps an event
        if (JsUtils.isEvent(jso)) {
          jso = jso.<Event>cast().getCurrentEventTarget();
        }
        // Otherwise we wrap it as an element
        return new GQuery(jso.<Element> cast());
      }
      throw new RuntimeException("Error: GQuery.$(Object o) could not wrap the type : " + o.getClass().getName() + " " + o);
    }
    return $();
  }

  /**
   * Create a new GQuery given a list of {@link com.google.gwt.dom.client.Node} or
   * {@link com.google.gwt.user.client.ui.IsWidget}.
   */
  public static GQuery $(List<?> nodesOrWidgets) {
    JsNodeArray elms = JsNodeArray.create();
    if (nodesOrWidgets != null) {
      for (Object o : nodesOrWidgets) {
        if (o instanceof Node) {
          elms.addNode((Node) o);
        } else if (o instanceof IsWidget) {
          elms.addNode(((IsWidget) o).asWidget().getElement());
        }
      }
    }
    return new GQuery(elms);
  }

  /**
   * This function accepts a string containing a CSS selector which is then used to match a set of
   * elements, or it accepts raw HTML creating a GQuery element containing those elements. Xpath
   * selector is supported in browsers with native xpath engine.
   */
  public static GQuery $(String selectorOrHtml) {
    return $(selectorOrHtml, document);
  }
  
  /**
   * This function accepts a string containing a CSS selector which is then used to match a set of
   * elements, or it accepts raw HTML creating a GQuery element containing those elements. The
   * second parameter is is a class reference to a plugin to be used.
   *
   * Xpath selector is supported in browsers with native xpath engine.
   */
  public static <T extends GQuery> T $(String selector, Class<T> plugin) {
    return $(selector, document, plugin);
  }

  /**
   * This function accepts a string containing a CSS selector which is then used to match a set of
   * elements, or it accepts raw HTML creating a GQuery element containing those elements. The
   * second parameter is the context to use for the selector, or the document where the new elements
   * will be created.
   *
   * Xpath selector is supported in browsers with native xpath engine.
   */
  public static GQuery $(String selectorOrHtml, Node ctx) {
    String selector = null;
    if (selectorOrHtml == null || (selector = selectorOrHtml.trim()).length() == 0) {
      return $();
    }
    if (selector.startsWith("<")) {
      return innerHtml(selectorOrHtml, JsUtils.getOwnerDocument(ctx));
    }
    return new GQuery().select(selectorOrHtml, ctx);
  }

  /**
   * This function accepts a string containing a CSS selector which is then used to match a set of
   * elements, or it accepts raw HTML creating a GQuery element containing those elements. The
   * second parameter is the context to use for the selector. The third parameter is the class
   * plugin to use.
   *
   * Xpath selector is supported in browsers with native xpath engine.
   */
  @SuppressWarnings("unchecked")
  public static <T extends GQuery> T $(String selector, Node context, Class<T> plugin) {
    try {
      if (plugins != null) {
        T gquery = (T) plugins.get(plugin).init(new GQuery().select(selector, context));
        return gquery;
      }
      throw new RuntimeException("No plugin for class " + plugin);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * This function accepts a string containing a CSS selector which is then used to match a set of
   * elements, or it accepts raw HTML creating a GQuery element containing those elements. The
   * second parameter is the context to use for the selector, or the document where the new elements
   * will be created.
   *
   * Xpath selector is supported in browsers with native xpath engine.
   */
  public static GQuery $(String selectorOrHtml, Widget context) {
    return $(selectorOrHtml, context.getElement());
  }

  /**
   * This function accepts a string containing a CSS selector which is then used to match a set of
   * elements, or it accepts raw HTML creating a GQuery element containing those elements. The
   * second parameter is the context to use for the selector. The third parameter is the class
   * plugin to use.
   *
   * Xpath selector is supported in browsers with native xpath engine.
   */
  public static <T extends GQuery> T $(String selector, Widget context, Class<T> plugin) {
    return $(selector, context.getElement(), plugin);
  }

  /**
   * Wrap a GQuery around one widget or an array of existing ones.
   */
  public static GQuery $(Widget... widgets) {
    return $(Arrays.asList(widgets));
  }

  /**
   * Wrap a GQuery around one node or an array of existing ones.
   */
  public static GQuery $(Node... nodes) {
    return $(Arrays.asList(nodes));
  }

  /**
   * Create an empty JSON object.
   */
  public static Properties $$() {
    return $$(null);
  }

  /**
   * Wrap a JSON object.
   */
  public static Properties $$(String properties) {
    return Properties.create(properties);
  }

  /**
   * Perform an ajax request to the server.
   */
  public static Promise ajax(Properties p) {
    return Ajax.ajax(p);
  }

  /**
   * Perform an ajax request to the server.
   */
  public static Promise ajax(Settings settings) {
    return Ajax.ajax(settings);
  }

  /**
   * Perform an ajax request to the server.
   */
  public static Promise ajax(String url, Settings settings) {
    return Ajax.ajax(url, settings);
  }

  protected static GQuery cleanHtmlString(String elem, Document doc) {
    MatchResult mResult = tagNameRegex.exec(elem);
    if (mResult == null) {
      return $(doc.createTextNode(elem));
    }
    String tag = mResult.getGroup(1);

    if (wrapperMap == null) {
      initWrapperMap();
    }

    TagWrapper wrapper = wrapperMap.get(tag.toLowerCase());

    if (wrapper == null) {
      wrapper = TagWrapper.DEFAULT;
    }

    // TODO: fix IE link tag serialization
    // TODO: fix IE <script> tag
    // TODO: add fixes for IE TBODY issue

    // We use a temporary element to wrap the elements
    Element div = doc.createDivElement();
    div.setInnerHTML(wrapper.preWrap + elem.trim() + wrapper.postWrap);
    Node n = div;
    int depth = wrapper.wrapDepth;
    while (depth-- != 0) {
      n = n.getLastChild();
    }

    return
    // return all nodes added to the wrapper
    $(n.getChildNodes())
        // detach nodes from their temporary parent
        .remove(null, false);
  }

  /**
   * Return true if the element b is contained in a.
   */
  public static boolean contains(Element a, Element b) {
    return getSelectorEngine().contains(a, b);
  }

  /**
   * Get the element data matching the key.
   */
  public static <T> T data(Element e, String key) {
    return data(e, key, null);
  }

  /**
   * Store arbitrary data associated with the specified element.
   *
   * We store this data in a global js object having the structure:
   *  datacache [element.hashCode()] [key] = value
   *
   * @return the value stored in the element with the given name
   */
  public static <T> T data(Element element, String key, T value) {
    return data(element, key, value, null);
  }

  private static <T> T data(Element element, String key, T value, Class<? extends T> clz) {
    if (dataCache == null) {
      windowData = JavaScriptObject.createObject().cast();
      dataCache = JavaScriptObject.createObject().cast();
    }
    element = element == window || element.getNodeName() == null ? windowData : element;
    if (element != null && key != null) {
      int id = element.hashCode();

      if (value == null) {
        return dataCache.exists(id) ? dataCache.getCache(id).get(key, clz) : null;
      }

      if (!dataCache.exists(id)) {
        dataCache.put(id, JsCache.createObject().cast());
      }
      dataCache.getCache(id).put(key, value);
    }
    return value;
  }

  /**
   * Execute a function around each object.
   */
  public static void each(JsArrayMixed objects, Function f) {
    for (int i = 0, l = objects.length(); i < l; i++) {
      f.f(i, objects.getObject(i));
    }
  }

  /**
   * Execute a function around each object.
   */
  public static <T> void each(List<T> objects, Function f) {
    for (int i = 0, l = objects.size(); i < l; i++) {
      f.f(i, objects.get(i));
    }
  }

  /**
   * Execute a function around each object.
   */
  public static <T> void each(T[] objects, Function f) {
    for (int i = 0, l = objects.length; i < l; i++) {
      f.f(i, objects[i]);
    }
  }

  /**
   * Perform an ajax request to the server using GET.
   */
  public static Promise get(String url, Properties data, final Function onSuccess) {
    return Ajax.get(url, data, onSuccess);
  }

  /**
   * We will use the fact as GWT use the widget itself as EventListener ! If no Widget associated
   * with the element, this method returns null.
   */
  protected static Widget getAssociatedWidget(Element e) {
    try {
      EventListener listener = DOM.getEventListener((com.google.gwt.user.client.Element) e);
      // No listener attached to the element, so no widget exist for this element
      if (listener == null) {
        return null;
      }
      if (listener instanceof Widget) {
        // GWT uses the widget as event listener
        return (Widget) listener;
      } else if (listener instanceof EventsListener) {
        // GQuery replaces the gwt event listener and save it
        EventsListener gQueryListener = (EventsListener) listener;
        if (gQueryListener.getOriginalEventListener() != null
            && gQueryListener.getOriginalEventListener() instanceof Widget) {
          return (Widget) gQueryListener.getOriginalEventListener();
        }
      }
    } catch (Exception e2) {
      // Some times this code could raise an exception.
      // We do not want GQuery to fail, but in dev-mode we log the error.
      e2.printStackTrace();
    }
    return null;
  }

  private static AttributeImpl getAttributeImpl() {
    if (attributeImpl == null) {
      attributeImpl = GWT.create(AttributeImpl.class);
    }
    return attributeImpl;
  }

  /**
   * Perform an ajax request to the server using POST and parsing the json response.
   */
  public static Promise getJSON(String url, Properties data, final Function onSuccess) {
    return Ajax.getJSON(url, data, onSuccess);
  }

  /**
   * Perform an ajax request to the server using scripts tags and parsing the json response. The
   * request is not subject to the same origin policy restrictions.
   *
   * Server side should accept a parameter to specify the callback funcion name, and it must return
   * a valid json object wrapped this callback function.
   *
   * Example:
   *
   * <pre>
    Client code:
    getJSONP("http://server.exampe.com/getData.php",$$("myCallback:'?', otherParameter='whatever'"),
      new Function(){ public void f() {
        Properties p = getDataProperties();
        alert(p.getStr("k1");
    }});

    Server response:
    myCallback({"k1":"v1", "k2":"v2"});
   </pre>
   *
   */
  public static Promise getJSONP(String url, Properties data, final Function onSuccess) {
    return Ajax.getJSONP(url, data, onSuccess);
  }

  protected static DocumentStyleImpl getStyleImpl() {
    if (styleImpl == null) {
      styleImpl = getSelectorEngine().getDocumentStyleImpl();
    }
    return styleImpl;
  }

  /**
   * Return only the set of objects with match the predicate.
   */
  @SuppressWarnings("unchecked")
  public static <T> T[] grep(T[] objects, Predicate f) {
    ArrayList<Object> ret = new ArrayList<>();
    for (int i = 0, l = objects.length; i < l; i++) {
      if (f.f(objects[i], i)) {
        ret.add(objects[i]);
      }
    }
    return (T[]) ret.toArray(new Object[0]);
  }

  private static boolean hasClass(Element e, String clz) {
    return e.getClassName().matches("(^|.*\\s)" + clz + "(\\s.*|$)");
  }

  private static void initWrapperMap() {

    TagWrapper tableWrapper = new TagWrapper(1, "<table>", "</table>");
    TagWrapper selectWrapper = new TagWrapper(1, "<select multiple=\"multiple\">", "</select>");
    TagWrapper trWrapper = new TagWrapper(3, "<table><tbody><tr>", "</tr></tbody></table>");

    wrapperMap = JsNamedArray.create();
    wrapperMap.put("option", selectWrapper);
    wrapperMap.put("optgroup", selectWrapper);
    wrapperMap.put("legend", new TagWrapper(1, "<fieldset>", "</fieldset>"));
    wrapperMap.put("thead", tableWrapper);
    wrapperMap.put("tbody", tableWrapper);
    wrapperMap.put("tfoot", tableWrapper);
    wrapperMap.put("colgroup", tableWrapper);
    wrapperMap.put("caption", tableWrapper);
    wrapperMap.put("tr", new TagWrapper(2, "<table><tbody>", "</tbody></table>"));
    wrapperMap.put("td", trWrapper);
    wrapperMap.put("th", trWrapper);
    wrapperMap.put("col", new TagWrapper(2, "<table><tbody></tbody><colgroup>",
        "</colgroup></table>"));
    wrapperMap.put("area", new TagWrapper(1, "<map>", "</map>"));
  }

  private static GQuery innerHtml(String html, Document doc) {
    return cleanHtmlString(html, doc);
  }

  protected static String[] jsArrayToString(JsArrayString array) {
    if (GWT.isScript()) {
      return JsUtils.castArrayString(array);
    } else {
      String result[] = new String[array.length()];
      for (int i = 0, l = result.length; i < l; i++) {
        result[i] = array.get(i);
      }
      return result;
    }
  }

  /**
   * Return a lazy version of the GQuery interface. Lazy function calls are simply queued up and not
   * executed immediately.
   */
  public static LazyGQuery<?> lazy() {
    return $().createLazy();
  }

  /**
   * Perform an ajax request to the server using POST.
   */
  public static Promise post(String url, Properties data, final Function onSuccess) {
    return Ajax.post(url, data, onSuccess);
  }

  public static <T extends GQuery> Class<T> registerPlugin(Class<T> plugin, Plugin<T> pluginFactory) {
    // TODO: decide whether change plugins type to java.util.list
    // Right now we only test static methods in gquery, so this is only needed when initializing
    // plugins shortcuts in gquery.
    if (GWT.isClient()) {
      if (plugins == null) {
        plugins = JsMap.createObject().cast();
      }
      plugins.put(plugin, pluginFactory);
    }
    return plugin;
  }

  /**
   * Provides a way to execute callback Functions based on one or more objects
   * that represent asynchronous events.
   *
   * Arguments can be of any Object, but normally you would pass Promises.
   * In the case you provide a GQuery object it will call the promise() method to return
   * a Promise which will be executed when the queue is resolved.
   * In the case you provide a normal Object, it will return a promise which will be immediately
   * resolved with the object as argument.
   * In the case you provide a Function it will executed and if the f(Object...) method returns
   * a new promise it will be used, otherwise we will use the returned object like in the last case.
   *
   * It Returns a new promise which will be finalized when all of its subordinates finish.
   * In the case of all subordinates are resolved correctly the promise will be resolved
   * otherwise it will be rejected.
   */
  public static Promise when(Object... subordinates) {
    return Deferred.when(subordinates);
  }

  /**
   * A constructor function that returns a chainable utility object with methods to register
   * multiple callbacks into callback queues, invoke callback queues, and relay the success
   * or failure state of any synchronous or asynchronous function.
   */
  public static Promise.Deferred Deferred() {
    return new Deferred();
  }

  public static SelectorEngine getSelectorEngine() {
    if (engine == null) {
      engine = GWT.create(SelectorEngine.class);
    }
    return engine;
  }

  private static native void scrollIntoViewImpl(Node n) /*-{
    if (n)
      n.scrollIntoView()
  }-*/;

  private static native void setElementValue(Element e, String value) /*-{
    e.value = value;
  }-*/;

  protected Node currentContext;

  protected String currentSelector;
  /**
   * Immutable array of matched elements, modify this using setArray.
   */
  private Element[] elements = new Element[0];

  /**
   * The nodeList of matched elements, modify this using setArray.
   */
  // TODO: remove this and use elements, change return type of get()
  private NodeList<Element> nodeList = JavaScriptObject.createArray().cast();

  private GQuery previousObject;

  private GQuery() {
  }

  private GQuery(Element element) {
    this(JsNodeArray.create(element));
  }

  protected GQuery(GQuery gq) {
    if (gq != null) {
      elements = gq.elements;
      nodeList = gq.nodeList;
      currentSelector = gq.currentSelector;
      currentContext = gq.currentContext;
    }
  }

  private GQuery(JsNodeArray nodes) {
    this(nodes.<NodeList<Element>> cast());
  }

  private GQuery(NodeList<Element> list) {
    setArray(list);
  }

  /**
   * Add elements to the set of matched elements if they are not included yet.
   *
   * It construct a new GQuery object and does not modify the original ones.
   *
   * It also update the selector appending the new one.
   */
  public GQuery add(GQuery elementsToAdd) {
    return pushStack(JsUtils.copyNodeList(nodeList, elementsToAdd.nodeList, true)
        .<JsNodeArray> cast(), "add", join(",", getSelector(), elementsToAdd.getSelector()));
  }

  /**
   * Add elements to the set of matched elements if they are not included yet.
   */
  public GQuery add(String selector) {
    return add($(selector));
  }

  /**
   * Add the previous selection to the current selection. Useful for traversing elements, and then
   * adding something that was matched before the last traversal.
   */
  public GQuery addBack() {
    return previousObject != null ? add(previousObject) : this;
  }

  /**
   * Adds the specified classes to each matched element.
   */
  public GQuery addClass(String... classes) {
    for (Element e : elements) {
      // issue 81 : ensure that the element is an Element node.
      if (Element.is(e)) {
        for (String clz : classes) {
          e.addClassName(clz);
        }
      }
    }
    return this;
  }

  /**
   * Insert content after each of the matched elements. The elements must already be inserted into
   * the document (you can't insert an element after another if it's not in the page).
   */
  public GQuery after(GQuery query) {
    return domManip(query, DomMan.AFTER);
  }

  /**
   * Insert content after each of the matched elements. The elements must already be inserted into
   * the document (you can't insert an element after another if it's not in the page).
   */
  public GQuery after(Node n) {
    return domManip($(n), DomMan.AFTER);
  }

  /**
   * Insert content after each of the matched elements. The elements must already be inserted into
   * the document (you can't insert an element after another if it's not in the page).
   */
  public GQuery after(String html) {
    return domManip(html, DomMan.AFTER);
  }
  
  /**
   * Insert content after each of the matched elements. The elements must already be inserted into
   * the document (you can't insert an element after another if it's not in the page).
   */
  public GQuery after(SafeHtml safeHtml) {
    return after(safeHtml.asString());
  }

  private void allNextSiblingElements(Element firstChildElement, JsNodeArray result, Element elem,
      GQuery until, String filterSelector) {

    while (firstChildElement != null) {

      if (until != null && until.index(firstChildElement) != -1) {
        return;
      }

      if (firstChildElement != elem
          && (filterSelector == null || $(firstChildElement).is(filterSelector))) {
        result.addNode(firstChildElement);
      }
      firstChildElement = firstChildElement.getNextSiblingElement();
    }
  }

  private void allPreviousSiblingElements(Element firstChildElement, JsNodeArray result,
      GQuery until, String filterSelector) {
    while (firstChildElement != null) {
      if (until != null && until.index(firstChildElement) != -1) {
        return;
      }

      if (filterSelector == null || $(firstChildElement).is(filterSelector)) {
        result.addNode(firstChildElement);
      }

      firstChildElement = getPreviousSiblingElement(firstChildElement);
    }
  }

  /**
   * Add the previous selection to the current selection. Useful for traversing elements, and then
   * adding something that was matched before the last traversal.
   * @deprecated use addBack() instead
   */
  @Deprecated
  public GQuery andSelf() {
    return addBack();
  }

  /**
   *
   * The animate() method allows you to create animation effects on any numeric HTML Attribute,
   * CSS property, or color CSS property.
   *
   * Concerning to numeric properties, values are treated as a number of pixels unless otherwise
   * specified. The units em and % can be specified where applicable.
   *
   * By default animate considers css properties, if you wanted to animate element attributes you
   * should to prepend the symbol dollar to the attribute name. It's useful to animate svg elements.
   *
   * NOTE: The ability of animating attribute values is only available in gquery but not jquery
   *
   * Example:
   *
   * <pre class="code">
   *  // move the element from its original position to left:500px
   *  $("#foo").animate("left:'500'");
   *
   *  // Change the width html attribute of a table, note the symbol '$' to
   *  // tell gquery which it is an html-attribute instead of a css-property.
   *  $("table").animate("$width:'500'");
   * </pre>
   *
   * In addition to numeric values, each property can take the strings 'show', 'hide', and 'toggle'.
   * These shortcuts allow for custom hiding and showing animations that take into account the
   * display type of the element. Animated properties can also be relative. If a value is supplied
   * with a leading += or -= sequence of characters, then the target value is computed by adding or
   * subtracting the given number from the current value of the property.
   *
   * Example:
   *
   * <pre class="code">
   *  // move the element from its original position to 500px to the left for 500ms and
   *  // change the background color of the element at the end of the animation
   *
   *  $("#foo").animate("left:'+=500'", new Function(){
   *                 public void f(Element e){
   *                   $(e).css(CSS.BACKGROUND_COLOR.with(RGBColor.RED);
   *                 }
   *              });
   * </pre>
   *
   * The default duration of the animation is 500ms.
   *
   * For color css properties, values can be specified via hexadecimal or rgb or literal values.
   *
   * Example:
   *
   * <pre class="code">
   *  $("#foo").animate("backgroundColor:'red', color:'#ffffff', borderColor:'rgb(129, 0, 70)'");
   * </pre>
   *
   * @param stringOrProperties the property to animate : "cssName:'value'"
   * @param funcs an array of {@link Function} called once the animation is complete
   */
  public GQuery animate(Object stringOrProperties, Function... funcs) {
    return as(Effects).animate(stringOrProperties, funcs);
  }

  /**
   *
   * The animate() method allows you to create animation effects on any numeric HTML Attribute,
   * CSS property, or color CSS property.
   *
   * Concerning to numeric properties, values are treated as a number of pixels unless otherwise
   * specified. The units em and % can be specified where applicable.
   *
   * By default animate considers css properties, if you wanted to animate element attributes you
   * should to prepend the symbol dollar to the attribute name. It's useful to animate svg elements.
   *
   * NOTE: The ability of animating attribute values is only available in gquery but not jquery
   *
   * Example:
   *
   * <pre class="code">
   *  // move the element from its original position to left:500px for 500ms using a swing easing
   *  $("#foo").animate("left:'500'", 500, Easing.SWING);
   *
   *  // Change the width html attribute of a table, note the symbol '$' to
   *  // tell gquery which it is an html-attribute instead of a css-property.
   *  // the animation will last 400ms, and we use the LINEAR easing algorithm
   *  $("table").animate(Properties.create("{$width: '500', $border: '10'}"), 400, Easing.LINEAR);
   * </pre>
   *
   * In addition to numeric values, each property can take the strings 'show', 'hide', and 'toggle'.
   * These shortcuts allow for custom hiding and showing animations that take into account the
   * display type of the element. Animated properties can also be relative. If a value is supplied
   * with a leading += or -= sequence of characters, then the target value is computed by adding or
   * subtracting the given number from the current value of the property.
   *
   * Example:
   *
   * <pre class="code">
   *  // move the element from its original position to 500px to the left and 5OOpx down for 400ms.
   *  // use a swing easing function for the transition
   *  $("#foo").animate(Properties.create("{top:'+=500px',left:'+=500px'}"), 400, Easing.SWING);
   * </pre>
   *
   * For color css properties, values can be specified via hexadecimal or rgb or literal values.
   *
   * Example:
   *
   * <pre class="code">
   *  $("#foo").animate("backgroundColor:'red', color:'#ffffff', borderColor:'rgb(129, 0, 70)'"), 400, Easing.SWING);
   * </pre>
   *
   * @param stringOrProperties a String or a {@link Properties} object containing css properties to
   *          animate.
   * @param funcs an array of {@link Function} called once the animation is complete
   * @param duration the duration in milliseconds of the animation
   * @param easing the easing function to use for the transition
   */
  public GQuery animate(Object stringOrProperties, int duration, Easing easing, Function... funcs) {
    return as(Effects).animate(stringOrProperties, duration, easing, funcs);
  }

  /**
   * The animate() method allows you to create animation effects on any numeric HTML Attribute,
   * CSS property, or color CSS property.
   *
   * Concerning to numeric properties, values are treated as a number of pixels unless otherwise
   * specified. The units em and % can be specified where applicable.
   *
   * By default animate considers css properties, if you wanted to animate element attributes you
   * should to prepend the symbol dollar to the attribute name. It's useful to animate svg elements.
   *
   * NOTE: The ability of animating attribute values is only available in gquery but not jquery
   *
   * Example:
   *
   * <pre class="code">
   *  // move the element from its original position to left:500px for 500ms
   *  $("#foo").animate("left:'500'", 500);
   *
   *  // Change the width html attribute of a table, note the symbol '$' to
   *  // tell gquery which it is an html-attribute instead of a css-property.
   *  // the animation will last 400ms
   *  $("table").animate("$width:'500'"), 400);
   * </pre>
   *
   * In addition to numeric values, each property can take the strings 'show', 'hide', and 'toggle'.
   * These shortcuts allow for custom hiding and showing animations that take into account the
   * display type of the element. Animated properties can also be relative. If a value is supplied
   * with a leading += or -= sequence of characters, then the target value is computed by adding or
   * subtracting the given number from the current value of the property.
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
   * For color css properties, values can be specified via hexadecimal or rgb or literal values.
   *
   * Example:
   *
   * <pre class="code">
   *  $("#foo").animate("backgroundColor:'red', color:'#ffffff', borderColor:'rgb(129, 0, 70)', 1000");
   * </pre>
   *
   * @param stringOrProperties the set of properties to animate : "cssName:'value'"
   * @param funcs an array of {@link Function} called once the animation is complete.
   */
  public GQuery animate(Object stringOrProperties, int duration, Function... funcs) {
    return as(Effects).animate(stringOrProperties, duration, funcs);
  }

  /**
   * Append content to the inside of every matched element. This operation is similar to doing an
   * appendChild to all the specified elements, adding them into the document.
   */
  public GQuery append(GQuery query) {
    return domManip(query, DomMan.APPEND);
  }

  /**
   * Append content to the inside of every matched element. This operation is similar to doing an
   * appendChild to all the specified elements, adding them into the document.
   */
  public GQuery append(Node n) {
    return domManip($(n), DomMan.APPEND);
  }

  /**
   * Append content to the inside of every matched element. This operation is similar to doing an
   * appendChild to all the specified elements, adding them into the document.
   */
  public GQuery append(String html) {
    return domManip(html, DomMan.APPEND);
  }
  
  /**
   * Append content to the inside of every matched element. This operation is similar to doing an
   * appendChild to all the specified elements, adding them into the document.
   */
  public GQuery append(SafeHtml safeHtml) {
    return append(safeHtml.asString());
  }

  /**
   * All of the matched set of elements will be inserted at the end of the element(s) specified by
   * the parameter other.
   *
   * The operation $(A).appendTo(B) is, essentially, the reverse of doing a regular $(A).append(B),
   * instead of appending B to A, you're appending A to B.
   */
  public GQuery appendTo(GQuery other) {
    other.append(this);
    return this;
  }

  /**
   * All of the matched set of elements will be inserted at the end of the element(s) specified by
   * the parameter other.
   *
   * The operation $(A).appendTo(B) is, essentially, the reverse of doing a regular $(A).append(B),
   * instead of appending B to A, you're appending A to B.
   */
  public GQuery appendTo(Node n) {
    GQuery a = $(n);
    GQuery b = this;
    a.append(b);
    return this;
  }

  /**
   * All of the matched set of elements will be inserted at the end of the element(s) specified by
   * the parameter other.
   *
   * The operation $(A).appendTo(B) is, essentially, the reverse of doing a regular $(A).append(B),
   * instead of appending B to A, you're appending A to B.
   */
  public GQuery appendTo(String html) {
    $(html).append(this);
    return this;
  }
  
  /**
   * All of the matched set of elements will be inserted at the end of the element(s) specified by
   * the parameter other.
   *
   * The operation $(A).appendTo(B) is, essentially, the reverse of doing a regular $(A).append(B),
   * instead of appending B to A, you're appending A to B.
   */
  public GQuery appendTo(SafeHtml safeHtml) {
    return appendTo(safeHtml.asString());
  }

  /**
   * Convert to Plugin interface provided by Class literal.
   */
  @SuppressWarnings("unchecked")
  public <T extends GQuery> T as(Class<T> plugin) {
    // GQuery is not a plugin for itself
    if (plugin == GQUERY) {
      return (T) this;
    } else if (plugins != null) {
      Plugin<?> p = plugins.get(plugin);
      if (p != null) {
        return (T) p.init(this);
      }
    }
    throw new RuntimeException("No plugin registered for class " + plugin.getName());
  }

  /**
   * Set a key/value object as properties to all matched elements.
   *
   * Example: $("img").attr(new Properties("src: 'test.jpg', alt: 'Test Image'"))
   */
  public GQuery attr(Properties properties) {
    for (String name : properties.keys()) {
      attr(name, properties.getStr(name));
    }
    return this;
  }

  /**
   * Access a property on the first matched element. This method makes it easy to retrieve a
   * property value from the first matched element. If the element does not have an attribute with
   * such a name, empty string is returned. Attributes include title, alt, src, href, width, style,
   * etc.
   */
  public String attr(String name) {
    return isEmpty() ? "" : get(0).getAttribute(name);
  }

  /**
   * Set a single property to a computed value, on all matched elements.
   */
  public GQuery attr(String key, Function closure) {
    int i = 0;
    for (Element e : elements) {
      Object val = closure.f(e.<com.google.gwt.dom.client.Element> cast(), i++);
      $(e).attr(key, val);
    }
    return this;
  }

  /**
   * Set a single property to a value, on all matched elements.
   */
  public GQuery attr(String key, Object value) {
    assert key != null : "key cannot be null";
    assert !"$H".equalsIgnoreCase(key) : "$H is a GWT reserved attribute. Changing its value will break your application.";

    getAttributeImpl().setAttribute(this, key, value);

    return this;
  }

  /**
   * Insert content before each of the matched elements. The elements must already be inserted into
   * the document (you can't insert an element before another if it's not in the page).
   */
  public GQuery before(GQuery query) {
    return domManip(query, DomMan.BEFORE);
  }

  /**
   * Insert content before each of the matched elements. The elements must already be inserted into
   * the document (you can't insert an element before another if it's not in the page).
   */
  public GQuery before(Node n) {
    return domManip($(n), DomMan.BEFORE);
  }

  /**
   * Insert content before each of the matched elements. The elements must already be inserted into
   * the document (you can't insert an element before another if it's not in the page).
   */
  public GQuery before(String html) {
    return domManip(html, DomMan.BEFORE);
  }

  /**
   * Binds a set of handlers to a particular Event for each matched element.
   *
   * The event handlers are passed as Functions that you can use to prevent default behavior. To
   * stop both default action and event bubbling, the function event handler has to return false.
   *
   * You can pass an additional Object data to your Function as the second parameter
   *
   */
  public GQuery bind(int eventbits, final Object data, final Function... funcs) {
    return as(Events).bind(eventbits, data, funcs);
  }

  /**
   * Binds a set of handlers to a particular Event for each matched element.
   *
   * The event handlers are passed as Functions that you can use to prevent default behavior. To
   * stop both default action and event bubbling, the function event handler has to return false.
   *
   */
  public GQuery bind(int eventbits, final Function... funcs) {
    return as(Events).bind(eventbits, null, funcs);
  }

  /**
   * Binds a set of handlers to a particular Event for each matched element.
   *
   * The event handlers are passed as Functions that you can use to prevent default behavior. To
   * stop both default action and event bubbling, the function event handler has to return false.
   *
   * You can pass an additional Object data to your Function as the second parameter
   *
   */
  public GQuery bind(String eventType, final Object data, final Function... funcs) {
    return as(Events).bind(eventType, data, funcs);
  }

  /**
   * Binds a set of handlers to a particular Event for each matched element.
   *
   * The event handlers are passed as Functions that you can use to prevent default behavior. To
   * stop both default action and event bubbling, the function event handler has to return false.
   *
   */
  public GQuery bind(String eventType, final Function... funcs) {
    return as(Events).bind(eventType, null, funcs);
  }

  /**
   * Bind Handlers or fire Events for each matched element.
   */
  private GQuery bindOrFire(int eventbits, final Object data, final Function... funcs) {
    if (funcs.length == 0) {
      return trigger(eventbits);
    } else {
      return bind(eventbits, data, funcs);
    }
  }

  /**
   * Bind Handlers or fire Events for each matched element.
   */
  private GQuery bindOrFire(String eventname, final Object data, final Function... funcs) {
    if (funcs.length == 0) {
      return as(Events).triggerHtmlEvent(eventname);
    } else {
      return bind(eventname, data, funcs);
    }
  }

  /**
   * Bind a set of functions to the blur event of each matched element. Or trigger the blur event if
   * no functions are provided.
   */
  public GQuery blur(Function... f) {
    bindOrFire(Event.ONBLUR, null, f);
    if (!isEmpty() && f.length == 0) {
      get(0).blur();
    }
    return this;
  }

  /**
   * Bind a set of functions to the change event of each matched element. Or trigger the event if no
   * functions are provided.
   */
  public GQuery change(Function... f) {
    return bindOrFire(Event.ONCHANGE, null, f);
  }

  /**
   * Get a set of elements containing all of the unique immediate children of each of the matched
   * set of elements. Also note: while parents() will look at all ancestors, children() will only
   * consider immediate child elements.
   */
  public GQuery children() {
    JsNodeArray result = JsNodeArray.create();
    for (Element e : elements) {
      allNextSiblingElements(e.getFirstChildElement(), result, null, null, null);
    }
    return new GQuery(unique(result));
  }

  /**
   * Get a set of elements containing all of the unique children of each of the matched set of
   * elements. This set is filtered with the expressions that will cause only elements matching any
   * of the selectors to be collected.
   */
  public GQuery children(String... filters) {
    return children().filter(filters);
  }

  private void cleanGQData(Element... elements) {
    for (Element el : elements) {
      try {
        EventsListener.clean(el);
        removeData(el, null);
      } catch (Exception e) {
        // If for some reason event/data removal fails, do not break the app,
        // just log the error in dev-mode
        // e.g.: this happens when removing iframes which are no fully loaded.
        e.printStackTrace();
      }
    }
  }

  /**
   * Remove from the Effects queue all {@link Function} that have not yet been run.
   */
  public GQuery clearQueue() {
    return as(Queue).clearQueue();
  }

  /**
   * Remove from the queue all {@link Function} that have not yet been run.
   */
  public GQuery clearQueue(String queueName) {
    return as(Queue).clearQueue(queueName);
  }

  /**
   * Bind a set of functions to the click event of each matched element. Or trigger the event if no
   * functions are provided.
   */
  public GQuery click(Function... f) {
    return bindOrFire(Event.ONCLICK, null, f);
  }

  /**
   * Clone matched DOM Elements and select the clones. This is useful for moving copies of the
   * elements to another location in the DOM.
   */
  public GQuery clone() {
    JsNodeArray result = JsNodeArray.create();
    for (Element e : elements) {
      result.addNode(e.cloneNode(true));
    }
    GQuery ret = new GQuery(result);
    ret.currentContext = currentContext;
    ret.currentSelector = currentSelector;
    return ret;
  }

  /**
   * Get the first ancestor element that matches the selector (for each matched element), beginning
   * at the current element and progressing up through the DOM tree.
   *
   * @param selector
   * @return
   */
  public GQuery closest(String selector) {
    return closest(selector, null);
  }

  /**
   * Get the first ancestor element that matches the selector (for each matched element), beginning
   * at the current element and progressing up through the DOM tree until reach the
   * <code>context</code> node.
   *
   * If no context is passed in then the context of the gQuery object will be used instead.
   *
   */
  public GQuery closest(String selector, Node context) {
    assert selector != null;

    if (context == null) {
      context = currentContext;
    }

    GQuery pos = posRegex.test(selector) ? $(selector, context) : null;
    JsNodeArray result = JsNodeArray.create();

    for (Element e : elements) {
      Element current = e;
      while (current != null && current.getOwnerDocument() != null && current != context) {
        boolean match = pos != null ? pos.index(current) > -1 : $(current).is(selector);
        if (match) {
          result.addNode(current);
          break;
        } else {
          current = current.getParentElement();
        }
      }
    }

    return $(unique(result));
  }

  /**
   * Returns a {@link Map} object as key a selector and as value the list of ancestor elements
   * matching this selectors, beginning at the first matched element and progressing up through the
   * DOM. This method allows retrieving the list of ancestors matching many selectors by traversing
   * the DOM only one time.
   *
   * @return
   */
  public JsNamedArray<NodeList<Element>> closest(String[] selectors) {
    return closest(selectors, null);
  }

  /**
   * Returns a {@link Map} object as key a selector and as value the list of ancestor elements
   * matching this selectors, beginning at the first matched element and progressing up through the
   * DOM until reach the <code>context</code> node.. This method allows retrieving the list of
   * ancestors matching many selectors by traversing the DOM only one time.
   *
   * @return
   */
  public JsNamedArray<NodeList<Element>> closest(String[] selectors, Node context) {
    JsNamedArray<NodeList<Element>> results = JsNamedArray.create();

    if (context == null) {
      context = currentContext;
    }

    Element first = get(0);
    if (first != null && selectors != null && selectors.length > 0) {
      JsNamedArray<GQuery> matches = JsNamedArray.create();
      for (String selector : selectors) {
        if (!matches.exists(selector)) {
          matches.put(selector, posRegex.test(selector) ? $(selector, context) : null);
        }
      }

      Element current = first;
      while (current != null && current.getOwnerDocument() != null && current != context) {
        // for each selector, check if the current element match it.
        for (String selector : matches.keys()) {

          GQuery pos = matches.get(selector);
          boolean match = pos != null ? pos.index(current) > -1 : $(current).is(selector);

          if (match) {
            JsNodeArray elementsMatchingSelector = results.get(selector).cast();
            if (elementsMatchingSelector == null) {
              elementsMatchingSelector = JsNodeArray.create();
              results.put(selector, elementsMatchingSelector);
            }
            elementsMatchingSelector.addNode(current);
          }
        }

        current = current.getParentElement();
      }
    }
    return results;
  }

  /**
   * Filter the set of elements to those that contain the specified text.
   */
  public GQuery contains(String text) {
    JsNodeArray array = JsNodeArray.create();
    for (Element e : elements) {
      if ($(e).text().contains(text)) {
        array.addNode(e);
      }
    }
    return $(array);
  }

  /**
   * Find all the child nodes inside the matched elements (including text nodes), or the content
   * document, if the element is an iframe.
   */
  public GQuery contents() {
    JsNodeArray result = JsNodeArray.create();
    for (Element e : elements) {
      if (JsUtils.isWindow(e) || "iframe".equalsIgnoreCase(e.getTagName())) {
        result.addNode(getStyleImpl().getContentDocument(e));
      } else {
        NodeList<Node> children = e.getChildNodes();
        for (int i = 0, l = children.getLength(); i < l; i++) {
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
   * Set CSS a single style property on every matched element using type-safe enumerations.
   *
   * The best way to use this method (i.e. to generate a CssSetter) is to take the desired css
   * property defined in {@link CSS} class and call the {@link TakesCssValue#with(HasCssName)}
   * method on it.
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
  public GQuery css(CssSetter... cssSetter) {
    for (Element e : elements) {
      for (CssSetter s : cssSetter) {
        s.applyCss(e);
      }
    }
    return this;
  }

  /**
   * Return a style property on the first matched element using type-safe enumerations.
   *
   * Ex : $("#myId").css(CSS.BACKGROUND_COLOR);
   */
  public String css(HasCssValue property) {
    return css(property, true);
  }

  /**
   * Return a style property on the first matched element using type-safe enumerations.
   *
   * The parameter force has a special meaning here: - When force is false, returns the value of the
   * css property defined in the style attribute of the element. - Otherwise it returns the real
   * computed value.
   *
   * For instance if you define 'display=none' not in the element style but in the css stylesheet,
   * it returns an empty string unless you pass the parameter force=true.
   *
   * Ex : $("#myId").css(CSS.WIDTH, true);
   */
  public String css(HasCssValue property, boolean force) {
    return css(property.getCssName(), force);
  }

  /**
   * Set a key/value object as style properties to all matched elements. This serves as the best way
   * to set a large number of style properties on all matched elements. You can use either js maps
   * or pure css syntax.
   *
   * Example:
   *
   * <pre class="code">
   *  $(".item").css(Properties.create("color: 'red', background:'blue'"))
   *  $(".item").css(Properties.create("color: red; background: blue;"))
   * </pre>
   */
  public GQuery css(Properties properties) {
    for (String property : properties.keys()) {
      css(property, properties.getStr(property));
    }
    return this;
  }

  /**
   * Return a style property on the first matched element.
   */
  public String css(String name) {
    return css(name, true);
  }

  /**
   * Return a style property on the first matched element.
   *
   * The parameter force has a special meaning here:
   * <ul>
   * <li>When force is false, returns the value of the css property defined in the style attribute
   * of the element.
   * <li>Otherwise it returns the real computed value.
   * </ul>
   *
   * For instance if you don't define 'display=none'in the element style but in the css stylesheet,
   * it returns an empty string unless you pass the parameter force=true.
   */
  public String css(String name, boolean force) {
    return isEmpty() ? "" : getStyleImpl().curCSS(get(0), name, force);
  }

  /**
   * Set a single style property to a value, on all matched elements.
   *
   */
  public GQuery css(String prop, String val) {
    for (Element e : elements) {
      getStyleImpl().setStyleProperty(e, prop, val);
    }
    return this;
  }

  /**
   * Set CSS a single style property on every matched element using type-safe enumerations. This
   * method allows you to set manually the value or set <i>inherit</i> value
   *
   * ex :
   *
   * <pre class="code">
   * $(#myId).css(CSS.TEXT_DECORATION, CSS.INHERIT);
   * </pre>
   */
  public GQuery css(TakesCssValue<?> cssProperty, String value) {
    return css(cssProperty.getCssName(), value);
  }

  /**
   * Returns the numeric value of a css property.
   */
  public double cur(String prop) {
    return cur(prop, false);
  }

  /**
   * Returns the numeric value of a css property.
   *
   * The parameter force has a special meaning: - When force is false, returns the value of the css
   * property defined in the set of style attributes. - When true returns the real computed value.
   */
  public double cur(String prop, boolean force) {
    return isEmpty() ? 0 : getStyleImpl().cur(get(0), prop, force);
  }

  /**
   * Return the value at the named data store for the first element in the set of matched
   * elements.
   */
  @SuppressWarnings("unchecked")
  public <T> T data(String name) {
    return isEmpty() ? null : (T) data(get(0), name, null);
  }

  /**
   * Return the value at the named data store for the first element in the set of matched
   * elements, as set by data(name, value), with desired return type.
   *
   * @param clz return type class literal
   */
  public <T> T data(String name, Class<? extends T> clz) {
    return isEmpty() ? null : data(get(0), name, null, clz);
  }

  /**
   * Store arbitrary data associated with the matched elements in the named data store.
   */
  public GQuery data(String name, Object value) {
    for (Element e : elements()) {
      data(e, name, value);
    }
    return this;
  }

  /**
   * Bind a set of functions to the dblclick event of each matched element. Or trigger the event if
   * no functions are provided.
   */
  public GQuery dblclick(Function... f) {
    return bindOrFire(Event.ONDBLCLICK, null, f);
  }

  /**
   * Insert a delay (in ms) in the GQuery queue, and optionally execute one o more functions if
   * provided when the delay finishes. It uses the effects queue namespace, so you can stack any of
   * the methods in the effects plugin.
   *
   * Example:
   *
   * <pre class="code">
   * $("#foo").slideUp(300)
   *          .delay(800)
   *          .fadeIn(400);
   * </pre>
   *
   * When this statement is executed, the element slides up for 300 milliseconds and then pauses for
   * 800 milliseconds before fading in for 400 milliseconds. Aditionally after those 800
   * milliseconds the element color is set to red.
   *
   * NOTE that this methods affects only methods which uses the queue like effects. So the following
   * example is wrong:
   *
   * <pre>
   * $("#foo").css(CSS.COLOR.with(RGBColor.RED)).delay(800).css(CSS.COLOR.with(RGBColor.BLACK));
   * </pre>
   *
   * The code above will not insert a delay of 800 ms between the css() calls ! For this kind of
   * behavior, you should execute these methods puting them in inline functions passed as argument
   * to the delay() method, or adding them to the queue.
   *
   * <pre>
   * $("#foo").css(CSS.COLOR.with(RGBColor.RED)).delay(800, lazy().css(CSS.COLOR.with(RGBColor.BLACK)).done());
   * $("#foo").css(CSS.COLOR.with(RGBColor.RED)).delay(800).queue(lazy().css(CSS.COLOR.with(RGBColor.BLACK)).dequeue().done());
   * </pre>
   */
  public GQuery delay(int milliseconds, Function... f) {
    return as(Queue).delay(milliseconds, f);
  }

  /**
   * Insert a delay (in ms) in the queue identified by the <code>queueName</code> parameter, and
   * optionally execute one o more functions if provided when the delay finishes.
   *
   * If <code>queueName</code> is null or equats to 'fx', the delay will be inserted to the Effects
   * queue.
   *
   * Example :
   *
   * <pre class="code">
   * $("#foo").queue("colorQueue", lazy().css(CSS.COLOR.with(RGBColor.RED)).dequeue("colorQueue").done())
   *          .delay(800, "colorQueue")
   *          .queue("colorQueue", lazy().css(CSS.COLOR.with(RGBColor.BLACK)).dequeue("colorQueue").done());
   * </pre>
   *
   * When this statement is executed, the text color of the element changes to red and then wait for
   * 800 milliseconds before changes the text color to black.
   *
   */
  public GQuery delay(int milliseconds, String queueName, Function... f) {
    return as(Queue).delay(milliseconds, queueName, f);
  }

  /**
   * Attach <code>handlers</code> to one or more events for all elements that match the
   * <code>selector</code>, now or in the future, based on a specific set of root elements.
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
   * This code above add an handler on click event on all cell (the existing oneand the future cell)
   * of all table. This code is equivalent to :
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
   * You can attach the handlers to many events by using the '|' operator ex:
   *
   * <pre>
   *  $("div.main").delegate(".subMain", Event.ONCLICK | Event.ONDBLCLICK, new Function(){...});
   * </pre>
   * @deprecated use {@link #on(String, String, Function...)}
   */
  public GQuery delegate(String selector, int eventbits, Function... handlers) {
    return delegate(selector, eventbits, null, handlers);
  }

  /**
   * Attach <code>handlers</code> to one or more events for all elements that match the
   * <code>selector</code>, now or in the future, based on a specific set of root elements. The
   * <code>data</code> parameter allows us to pass data to the handler.
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
   * This code above add an handler on click event on all cell (the existing oneand the future cell)
   * of all table. This code is equivalent to :
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
   * You can pass attach the handlers to many events by using the '|' operator ex:
   *
   * <pre>
   *  $("div.main").delegate(".subMain", Event.ONCLICK | Event.ONDBLCLICK, new Function(){...});
   * </pre>
   * @deprecated use {@link #on(String, String, Object, Function...)}
   */
  public GQuery delegate(String selector, int eventbits, Object data, Function... handlers) {

    for (Element e : elements) {
      $(selector, e).live(eventbits, data, handlers);
    }

    return this;
  }

  /**
   * Attach <code>handlers</code> to one or more events for all elements that match the
   * <code>selector</code>, now or in the future, based on a specific set of root elements.
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
   * This code above add an handler on click event on all cell (the existing oneand the future cell)
   * of all table. This code is equivalent to :
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
   *
   * <pre>
   *  $("div.main").delegate(".subMain", "click dblclick", new Function(){...});
   * </pre>
   *
   * </pre>
   * @deprecated use {@link #on(String, String, Function...)}
   */
  public GQuery delegate(String selector, String eventType, Function... handlers) {
    return delegate(selector, eventType, null, handlers);
  }

  /**
   * Attach <code>handlers</code> to one or more events for all elements that match the
   * <code>selector</code>, now or in the future, based on a specific set of root elements.
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
   * This code above add an handler on click event on all cell (the existing oneand the future cell)
   * of all table. This code is equivalent to :
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
   *
   * <pre>
   *  $("div.main").delegate(".subMain", "click dblclick", new Function(){...});
   * </pre>
   *
   * </pre>
   * @deprecated use {@link #on(String, String, Object, Function...)}
   */
  public GQuery delegate(String selector, String eventType, Object data, Function... handlers) {
    for (Element e : elements) {
      $(selector, e).live(eventType, data, handlers);
    }

    return this;
  }

  /**
   * Execute the next function on the Effects queue for the matched elements. This method is usefull
   * to tell when a function you add in the Effects queue is ended and so the next function in the
   * queue can start.
   *
   * Note: you should be sure to call dequeue() in all functions of a queue chain, otherwise the
   * queue execution will be stopped.
   */
  public GQuery dequeue() {
    return as(Queue).dequeue();
  }

  /**
   * Execute the next function on the queue named as queueName for the matched elements. This method
   * is usefull to tell when a function you add in the Effects queue is ended and so the next
   * function in the queue can start.
   */
  public GQuery dequeue(String queueName) {
    return as(Queue).dequeue(queueName);
  }

  /**
   * Detach all matched elements from the DOM. This method is the same than {@link #remove()} method
   * except all data and event handlers are not remove from the element. This method is useful when
   * removed elements are to be reinserted into the DOM at a later time.
   */
  public GQuery detach() {
    return remove(null, false);
  }

  /**
   * Detach from the DOM all matched elements filtered by the <code>filter</code>.. This method is
   * the same than {@link #remove(String)} method except all data and event handlers are not remove
   * from the element. This method is useful when removed elements are to be reinserted into the DOM
   * at a later time.
   */
  public GQuery detach(String filter) {
    return remove(filter, false);
  }

  /**
   * Remove all event handlers previously attached using {@link #live(String, Function)}. In order
   * for this method to function correctly, the selector used with it must match exactly the
   * selector initially used with {@link #live(String, Function)}.
   * 
   * @deprecated use {@link #off(String, String)} instead
   */
  public GQuery die() {
    return die(0);
  }

  /**
   * Remove an event handlers previously attached using {@link #live(int, Function)} In order for
   * this method to function correctly, the selector used with it must match exactly the selector
   * initially used with {@link #live(int, Function)}.
   * @deprecated use {@link #off(String)}
   */
  public GQuery die(int eventbits) {
    return as(Events).die(eventbits);
  }

  /**
   * Remove an event handlers previously attached using {@link #live(String, Function)} In order for
   * this method to function correctly, the selector used with it must match exactly the selector
   * initially used with {@link #live(String, Function)}.
   */
  public GQuery die(String eventName) {
    return as(Events).die(eventName);
  }

  private GQuery domManip(GQuery g, DomMan type, Element... elms) {
    JsNodeArray newNodes = JsNodeArray.create();
    if (elms.length == 0) {
      elms = elements;
    }
    for (int i = 0, l = elms.length; i < l; i++) {
      Element e = elms[i];
      if (e.getNodeType() == Node.DOCUMENT_NODE) {
        e = e.<Document> cast().getBody();
      }
      for (int j = 0, size = g.size(); j < size; j++) {
        // Widget w = getAssociatedWidget(g.get(j));
        // GqUi.detachWidget(w);

        Node n = g.get(j);
        // If an element selected is inserted elsewhere, it will be moved into the target (not
        // cloned).
        // If there is more than one target element, however, cloned copies of the inserted element
        // will be created for each target after the first
        if (i > 0) {
          n = n.cloneNode(true);
        }
        switch (type) {
          case PREPEND:
            newNodes.addNode(e.insertBefore(n, e.getFirstChild()));
            break;
          case APPEND:
            newNodes.addNode(e.appendChild(n));
            break;
          case AFTER:
            newNodes.addNode(e.getParentNode().insertBefore(n, e.getNextSibling()));
            break;
          case BEFORE:
            newNodes.addNode(e.getParentNode().insertBefore(n, e));
            break;
        }
        EventsListener.rebind(n.<Element> cast());
        // GqUi.attachWidget(w);
      }
    }
    // TODO: newNodes.size() > g.size() makes testRebind fail
    if (newNodes.size() >= g.size()) {
      g.setArray(newNodes);
    }
    return this;
  }

  // TODO: this should be handled by the other domManip method
  private GQuery domManip(String htmlString, DomMan type) {
    JsMap<Document, GQuery> cache = JsMap.createObject().cast();
    for (Element e : elements) {
      Document d = JsUtils.getOwnerDocument(e);
      GQuery g = cache.get(d);
      if (g == null) {
        g = cleanHtmlString(htmlString, d);
        cache.put(d, g);
      }
      domManip(g.clone(), type, e);
    }
    return this;
  }

  /**
   * Run one or more Functions over each element of the GQuery. You have to override one of these
   * funcions: public void f(Element e) public String f(Element e, int i)
   */
  public GQuery each(Function... f) {
    if (f != null) {
      for (Function f1 : f) {
        if (f1 != null) {
          int i = 0;
          for (Element e : elements) {
            f1.f(e.<com.google.gwt.dom.client.Element> cast(), i++);
          }
        }
      }
    }
    return this;
  }

  /**
   * Returns the working set of nodes as a Java array. <b>Do NOT</b> attempt to modify this array,
   * e.g. assign to its elements, or call Arrays.sort()
   */
  public Element[] elements() {
    return elements;
  }

  /**
   * Remove all child nodes from the set of matched elements. In the case of a document element, it
   * removes all the content You should call this method whenever you create a new iframe and you
   * want to add dynamic content to it.
   */
  public GQuery empty() {
    for (Element e : elements) {
      if (e.getNodeType() == Element.DOCUMENT_NODE) {
        getStyleImpl().emptyDocument(e.<Document> cast());
      } else {
        Node c = e.getFirstChild();
        while (c != null) {
          removeData(c.<Element> cast(), null);
          WidgetsUtils.detachWidget(getAssociatedWidget(e));
          EventsListener.clean(c.<Element> cast());
          e.removeChild(c);
          c = e.getFirstChild();
        }
      }
    }
    return this;
  }

  /**
   * Revert the most recent 'destructive' operation, changing the set of matched elements to its
   * previous state (right before the destructive operation).
   */
  public GQuery end() {
    return previousObject != null ? previousObject : new GQuery();
  }

  /**
   * Reduce GQuery to element in the specified position. This method accept negative index. A
   * negative index is counted from the end of the matched set:
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
  public GQuery eq(int pos) {
    return $(get(pos));
  }

  /**
   * Bind a set of functions to the error event of each matched element. Or trigger the event if no
   * functions are provided.
   */
  public GQuery error(Function... f) {
    return bindOrFire(Event.ONERROR, null, f);
  }

  /**
   * Fade in all matched elements by adjusting their opacity. The effect will take 1000 milliseconds
   * to complete
   */
  public GQuery fadeIn(Function... f) {
    return as(Effects).fadeIn(f);
  }

  /**
   * Fade in all matched elements by adjusting their opacity.
   */
  public GQuery fadeIn(int millisecs, Function... f) {
    return as(Effects).fadeIn(millisecs, f);
  }

  /**
   * Fade the opacity of all matched elements to a specified opacity and firing
   * an optional callback after completion. Only the opacity is adjusted for
   * this animation, meaning that all of the matched elements should already
   * have some form of height and width associated with them.
   */
  public GQuery fadeTo(int millisecs, double opacity, Function... f) {
    return as(Effects).fadeTo(millisecs, opacity, f);
  }

  /**
   * Fade the opacity of all matched elements to a specified opacity and firing
   * an optional callback after completion. Only the opacity is adjusted for
   * this animation, meaning that all of the matched elements should already
   * have some form of height and width associated with them.
   */
  public GQuery fadeTo(double opacity, Function... f) {
    return as(Effects).fadeTo(opacity, f);
  }

  /**
   * Fade out all matched elements by adjusting their opacity. The effect will take 1000
   * milliseconds to complete
   */
  public GQuery fadeOut(Function... f) {
    return as(Effects).fadeOut(f);
  }

  /**
   * Fade out all matched elements by adjusting their opacity.
   */
  public GQuery fadeOut(int millisecs, Function... f) {
    return as(Effects).fadeOut(millisecs, f);
  }

  /**
   * Toggle the visibility of all matched elements by adjusting their opacity and firing an optional
   * callback after completion. Only the opacity is adjusted for this animation, meaning that all of
   * the matched elements should already have some form of height and width associated with them.
   */
  public Effects fadeToggle(int millisecs, Function... f) {
    return as(Effects).fadeToggle(millisecs, f);
  }

  /**
   * Removes all elements from the set of matched elements that do not match the specified function.
   * The function is called with a context equal to the current element. If the function returns
   * false, then the element is removed - anything else and the element is kept.
   */
  public GQuery filter(Predicate filterFn) {
    JsNodeArray result = getSelectorEngine().filter(nodeList, filterFn).cast();
    return pushStack(result, "filter", currentSelector);
  }

  /**
   * Removes all elements from the set of matched elements that do not pass the specified css
   * expression. This method is used to narrow down the results of a search.
   * By default it works for either detached and attached elements unless
   * {@link SelectorEngine#filterDetached} is set to false.
   */
  public GQuery filter(String... filters) {
    String selector = join(", ", filters);
    JsNodeArray result = getSelectorEngine().filter(nodeList, selector).cast();
    return pushStack(result, "filter", selector);
  }

  /**
   * Removes all elements from the set of matched elements that do not pass the specified css
   * expression. This method is used to narrow down the results of a search.
   * Setting filterDetached parameter to true, means that we should consider detached elements
   * as well which implies some performance penalty.
   */
  public GQuery filter(boolean filterDetached, String... filters) {
    String selector = join(", ", filters);
    JsNodeArray result = getSelectorEngine().filter(nodeList, selector, filterDetached).cast();
    return pushStack(result, "filter", selector);
  }

  /**
   * Removes all elements from the set of matched elements that do not pass the specified css
   * expression. This method is used to narrow down the results of a search.
   * Setting considerDetached parameter to true, means that we should consider detached elements
   * as well which implies some performance penalties.
   */
  public GQuery filter(boolean filterDetached, String selector) {
    if (selector.isEmpty()) {
      return this;
    }
    JsNodeArray result = getSelectorEngine().filter(nodeList, selector, filterDetached).cast();
    return pushStack(result, "filter", selector);
  }

  /**
   * Searches for all elements that match the specified css expression. This method is a good way to
   * find additional descendant elements with which to process.
   *
   * Provide a comma-separated list of expressions to apply multiple filters at once.
   */
  public GQuery find(String... filters) {
    JsNodeArray array = JsNodeArray.create();
    for (String selector : filters) {
      for (Element e : elements) {
        for (Element c : $(selector, e).elements) {
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
   * Bind a set of functions to the focus event of each matched element. Or trigger the event and
   * move the input focus to the first element if no functions are provided.
   */
  public GQuery focus(Function... f) {
    bindOrFire(Event.ONFOCUS, null, f);
    if (!isEmpty() && f.length == 0) {
      get(0).focus();
    }
    return this;
  }

  /**
   * Return all elements matched in the GQuery as a NodeList. @see #elements() for a method which
   * returns them as an immutable Java array.
   */
  public NodeList<Element> get() {
    return nodeList;
  }

  /**
   * Return the ith element matched. This method accept negative index. A negative index is counted
   * from the end of the matched set.
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
  public Element get(int i) {
    int l = elements.length;
    if (i >= 0 && i < l) {
      return elements[i];
    }
    if (i < 0 && l + i >= 0) {
      return elements[l + i];
    }
    return null;
  }

  public Node getContext() {
    return currentContext;
  }

  /**
   * Return the previous set of matched elements prior to the last destructive operation (e.g.
   * query)
   */
  public GQuery getPreviousObject() {
    return end();
  }

  private native Element getPreviousSiblingElement(Element elem) /*-{
    var sib = elem.previousSibling;
    while (sib && sib.nodeType != 1)
      sib = sib.previousSibling;
    return sib;
  }-*/;

  /**
   * Return the selector representing the current set of matched elements.
   */
  public String getSelector() {
    return currentSelector;
  }

  /**
   * Returns true any of the specified classes are present on any of the matched Reduce the set of
   * matched elements to all elements after a given position. The position of the element in the set
   * of matched elements starts at 0 and goes to length - 1.
   */
  public GQuery gt(int pos) {
    return slice(pos + 1, -1);
  }

  /**
   * Reduce the set of matched elements to those that have a descendant that matches the Element.
   */
  public GQuery has(final Element elem) {
    return filter(new Predicate() {
      public boolean f(Element e, int index) {
        return contains(e, elem);
      }
    });
  }

  /**
   * Reduce the set of matched elements to those that have a descendant that matches the selector.
   */
  public GQuery has(final String selector) {
    return filter(new Predicate() {
      public boolean f(Element e, int index) {
        return !$(selector, e).isEmpty();
      }
    });
  }

  /**
   * Returns true any of the specified classes are present on any of the matched elements.
   */
  public boolean hasClass(String... classes) {
    for (Element e : elements) {
      for (String clz : classes) {
        if (hasClass(e, clz)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Get the current computed, pixel, height of the first matched element. It does not include
   * margin, padding nor border.
   */
  public int height() {
    return (int) cur("height", true);
  }

  /**
   * Set the height of every element in the matched set.
   */
  public GQuery height(int height) {
    for (Element e : elements) {
      e.getStyle().setPropertyPx("height", height);
    }
    return this;
  }

  /**
   * Set the height style property of every matched element. It's useful for using 'percent' or 'em'
   * units Example: $(".a").height("100%")
   */
  public GQuery height(String height) {
    return css("height", height);
  }

  /**
   * Make invisible all matched elements.
   */
  public GQuery hide() {
    for (Element e : elements) {
      String currentDisplay = getStyleImpl().curCSS(e, "display", false);
      Object old = data(e, OLD_DISPLAY, null);
      if (old == null && currentDisplay.length() != 0 && !"none".equals(currentDisplay)) {
        data(e, OLD_DISPLAY, currentDisplay);
      }
    }

    // Set the display value in a separate for loop to avoid constant reflow
    // Reflows is very bad in performance point of view
    for (Element e : elements) {
      e.getStyle().setDisplay(Display.NONE);
    }

    return this;
  }

  /**
   * Bind a function to the mouseover event of each matched element. A method for simulating
   * hovering (moving the mouse on, and off, an object). This is a custom method which provides an
   * 'in' to a frequent task. Whenever the mouse cursor is moved over a matched element, the first
   * specified function is fired. Whenever the mouse moves off of the element, the second specified
   * function fires.
   *
   * Since GQuery 1.4.0, this method binds handlers for both mouseenter and mouseleave events.
   */
  public GQuery hover(Function fover, Function fout) {
    return bind("mouseenter", fover).bind("mouseleave", fout);
  }

  /**
   * Get the innerHTML of the first matched element.
   */
  public String html() {
    return isEmpty() ? "" : get(0).getInnerHTML();
  }

  /**
   * Set the innerHTML of every matched element.
   */
  public GQuery html(String html) {
    for (Element e : elements) {
      if (e.getNodeType() == Node.DOCUMENT_NODE) {
        e = e.<Document> cast().getBody();
      }
      e.setInnerHTML(html);
    }
    return this;
  }
  
  /**
   * Set the innerHTML of every matched element.
   */
  public GQuery html(SafeHtml safeHtml) {
    return html(safeHtml.asString());
  }

  /**
   * Get the id of the first matched element.
   */
  public String id() {
    return attr("id");
  }

  /**
   * Set the id of the first matched element.
   */
  public GQuery id(String id) {
    return eq(0).attr("id", id);
  }

  /**
   * Find the index of the specified Element.
   */
  public int index(Element element) {
    int i = 0;
    for (Element e : elements) {
      if (e == element) {
        return i;
      }
      i++;
    }
    return -1;
  }

  /**
   * Return the position of the first matched element in relation with its sibblings.
   */
  public int index() {
    return prevAll().size();
  }

  /**
   * Returns the inner height of the first matched element, including padding but not the vertical
   * scrollbar height, border, or margin.
   */
  public int innerHeight() {
    return (int) cur("clientHeight", true);
  }

  /**
   * Returns the inner width of the first matched element, including padding but not the vertical
   * scrollbar width, border, or margin.
   */
  public int innerWidth() {
    return (int) cur("clientWidth", true);
  }

  /**
   * Insert all of the matched elements after another, specified, set of elements.
   */
  public GQuery insertAfter(Element elem) {
    return insertAfter($(elem));
  }

  /**
   * Insert all of the matched elements after another, specified, set of elements.
   */
  public GQuery insertAfter(GQuery query) {
    for (Element e : elements) {
      query.after(e);
    }
    return this;
  }

  /**
   * Insert all of the matched elements after another, specified, set of elements.
   */
  public GQuery insertAfter(String selector) {
    return insertAfter($(selector));
  }

  /**
   * Insert all of the matched elements before another, specified, set of elements.
   *
   * The elements must already be inserted into the document (you can't insert an element after
   * another if it's not in the page).
   */
  public GQuery insertBefore(Element item) {
    return insertBefore($(item));
  }

  /**
   * Insert all of the matched elements before another, specified, set of elements.
   *
   * The elements must already be inserted into the document (you can't insert an element after
   * another if it's not in the page).
   */
  public GQuery insertBefore(GQuery query) {
    for (Element e : elements) {
      query.before(e);
    }
    return this;
  }

  /**
   * Insert all of the matched elements before another, specified, set of elements.
   *
   * The elements must already be inserted into the document (you can't insert an element after
   * another if it's not in the page).
   */
  public GQuery insertBefore(String selector) {
    return insertBefore($(selector));
  }

  /**
   * Checks the current selection against an expression and returns true, if at least one element of
   * the selection fits the given expression. Does return false, if no element fits or the
   * expression is not valid.
   */
  public boolean is(String... filters) {
    return !filter(filters).isEmpty();
  }

  /**
   * Returns true if the number of matched elements is 0.
   */
  public boolean isEmpty() {
    return size() == 0;
  }

  /**
   * Return true if the first element is visible.isVisible.
   */
  public boolean isVisible() {
    return isEmpty() ? false : getStyleImpl().isVisible(get(0));
  }

  /**
   * Bind a set of functions to the keydown event of each matched element. Or trigger the event if
   * no functions are provided.
   */
  public GQuery keydown(Function... f) {
    return bindOrFire(Event.ONKEYDOWN, null, f);
  }

  /**
   * Trigger a keydown event passing the key pushed.
   */
  public GQuery keydown(int key) {
    return trigger(Event.ONKEYDOWN, key);
  }

  /**
   * Bind a set of functions to the keypress event of each matched element. Or trigger the event if
   * no functions are provided.
   */
  public GQuery keypress(Function... f) {
    return bindOrFire(Event.ONKEYPRESS, null, f);
  }

  /**
   * Trigger a keypress event passing the key pushed.
   */
  public GQuery keypress(int key) {
    return trigger(Event.ONKEYPRESS, key);
  }

  /**
   * Bind a set of functions to the keyup event of each matched element. Or trigger the event if no
   * functions are provided.
   */
  public GQuery keyup(Function... f) {
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
    return (int) cur("left", true);
  }

  /**
   * Returns the number of elements currently matched. The size function will return the same value.
   */
  public int length() {
    return size();
  }

  /**
   * Attach a handler for this event to all elements which match the current selector, now and in
   * the future.
   * @deprecated use {@link #on(String, Function...)}
   */
  public GQuery live(int eventbits, Function... funcs) {
    return as(Events).live(eventbits, null, funcs);
  }

  /**
   * Attach a handler for this event to all elements which match the current selector, now and in
   * the future.
   * @deprecated use {@link #on(String, Object, Function...)}
   */
  public GQuery live(int eventbits, Object data, Function... funcs) {
    return as(Events).live(eventbits, data, funcs);
  }

  /**
   * <p>
   * Attach a handler for this event to all elements which match the current selector, now and in
   * the future.
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
   * With this code, all elements with class "clickable" present in the DOM or added to the DOM in
   * the future will be clickable. The text color will be changed to red when they will be clicked.
   * So if after in the code, you add another element :
   *
   * <pre>
   * $("body").append("<div class='clickable'>Click me and I will be red</div>");
   * </pre>
   *
   * The click on this new element will also trigger the handler.
   * </p>
   * <p>
   * In the same way, if you add "clickable" class on some existing element, these elements will be
   * clickable also.
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
   * The {@link Function} will be called only on elements having the class "clickable" and being
   * descendant of myElement.</li>
   * </ul>
   * </p>
   * @deprecated use {@link #on(String, Function...)}
   */
  public GQuery live(String eventName, Function... funcs) {
    return as(Events).live(eventName, null, funcs);
  }

  /**
   * <p>
   * Attach a handler for this event to all elements which match the current selector, now and in
   * the future. The <code>data</code> parameter allows us to pass data to the handler.
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
   * With this code, all elements with class "clickable" present in the DOM or added to the DOM in
   * the future will be clickable. The text color will be changed to red when they will be clicked.
   * So if after in the code, you add another element :
   *
   * <pre>
   * $("body").append("<div class='clickable'>Click me and I will be red</div>");
   * </pre>
   *
   * The click on this new element will also trigger the handler.
   * </p>
   * <p>
   * In the same way, if you add "clickable" class on some existing element, these elements will be
   * clickable also.
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
   * The {@link Function} will be called only on elements having the class "clickable" and being
   * descendant of myElement.</li>
   * </ul>
   * </p>
   * @deprecated use {@link #on(String, Object, Function...)}
   */
  public GQuery live(String eventName, Object data, Function... funcs) {
    return as(Events).live(eventName, data, funcs);
  }

  /**
   * Bind a function to the load event of each matched element.
   */
  @Deprecated
  public GQuery load(Function f) {
    return bind(Event.ONLOAD, f);
  }

  /**
   * Load data from the server and place the returned HTML into the matched element.
   *
   * The url allows us to specify a portion of the remote document to be inserted. This is achieved
   * with a special syntax for the url parameter. If one or more space characters are included in
   * the string, the portion of the string following the first space is assumed to be a GQuery
   * selector that determines the content to be loaded.
   *
   */
  public GQuery load(String url) {
    return load(url, null, null);
  }

  /**
   * Load data from the server and place the returned HTML into the matched element.
   *
   * The url allows us to specify a portion of the remote document to be inserted. This is achieved
   * with a special syntax for the url parameter. If one or more space characters are included in
   * the string, the portion of the string following the first space is assumed to be a GQuery
   * selector that determines the content to be loaded.
   *
   */
  public GQuery load(String url, IsProperties data, final Function onSuccess) {
    return as(Ajax.Ajax).load(url, data, onSuccess);
  }

  /**
   * Reduce the set of matched elements to all elements before a given position. The position of the
   * element in the set of matched elements starts at 0 and goes to length - 1.
   */
  public GQuery lt(int pos) {
    return slice(0, pos);
  }

  /**
   * Pass each element in the current matched set through a function, producing a new array
   * containing the return values. When the call to the function returns a null it is not added to
   * the array.
   */
  public <W> List<W> map(Function f) {
    ArrayList<W> ret = new ArrayList<>();
    int i = 0;
    for (Element e : elements) {
      @SuppressWarnings("unchecked")
      W o = (W) f.f(e.<com.google.gwt.dom.client.Element> cast(), i++);
      if (o != null) {
        ret.add(o);
      }
    }
    return ret;
  }

  /**
   * Bind a set of functions to the mousedown event of each matched element. Or trigger the event if
   * no functions are provided.
   */
  public GQuery mousedown(Function... f) {
    return bindOrFire(Event.ONMOUSEDOWN, null, f);
  }

  /**
   * Bind an event handler to be fired when the mouse enter an element, or trigger that handler on
   * an element if no functions are provided.
   *
   * The mouseenter event differs from mouseover in the way it handles event bubbling. When
   * mouseover is used on an element having inner element(s), then when the mouse pointer moves hover
   * of the Inner element, the handler would be triggered. This is usually undesirable behavior. The
   * mouseenter event, on the other hand, only triggers its handler when the mouse enters the
   * element it is bound to, not a descendant.
   */
  public GQuery mouseenter(Function... f) {
    return as(Events).mouseenter(f);
  }

  /**
   * Bind an event handler to be fired when the mouse leaves an element, or trigger that handler on
   * an element if no functions are provided.
   *
   * The mouseleave event differs from mouseout in the way it handles event bubbling. When
   * mouseout is used on an element having inner element(s), then when the mouse pointer moves out
   * of the Inner element, the handler would be triggered. This is usually undesirable behavior. The
   * mouseleave event, on the other hand, only triggers its handler when the mouse leaves the
   * element it is bound to, not a descendant.
   */
  public GQuery mouseleave(Function... f) {
    return as(Events).mouseleave(f);
  }

  /**
   * Bind a set of functions to the mousemove event of each matched element. Or trigger the event if
   * no functions are provided.
   */
  public GQuery mousemove(Function... f) {
    return bindOrFire(Event.ONMOUSEMOVE, null, f);
  }

  /**
   * Bind a set of functions to the mouseout event of each matched element. Or trigger the event if
   * no functions are provided.
   */
  public GQuery mouseout(Function... f) {
    return bindOrFire(Event.ONMOUSEOUT, null, f);
  }

  /**
   * Bind a set of functions to the mouseover event of each matched element. Or trigger the event if
   * no functions are provided.
   */
  public GQuery mouseover(Function... f) {
    return bindOrFire(Event.ONMOUSEOVER, null, f);
  }

  /**
   * Bind a set of functions to the mouseup event of each matched element. Or trigger the event if
   * no functions are provided.
   */
  public GQuery mouseup(Function... f) {
    return bindOrFire(Event.ONMOUSEUP, null, f);
  }

  /**
   * Get a set of elements containing the unique next siblings of each of the given set of elements.
   * next only returns the very next sibling for each element, not all next siblings see {#nextAll}.
   */
  public GQuery next() {
    JsNodeArray result = JsNodeArray.create();
    for (Element e : elements) {
      Element next = e.getNextSiblingElement();
      if (next != null) {
        result.addNode(next);
      }
    }
    return pushStack(unique(result), "next", getSelector());
  }

  /**
   * Get a set of elements containing the unique next siblings of each of the given set of elements
   * filtered by 1 or more selectors. next only returns the very next sibling for each element, not
   * all next siblings see {#nextAll}.
   */
  public GQuery next(String... selectors) {
    JsNodeArray result = JsNodeArray.create();
    for (Element e : elements) {
      Element next = e.getNextSiblingElement();
      if (next != null) {
        result.addNode(next);
      }
    }
    return pushStack(result, "next", selectors[0]).filter(selectors);
  }

  /**
   * Get all following siblings of each element in the set of matched elements.
   */
  public GQuery nextAll() {
    return nextAll(null);
  }

  /**
   * Get all following siblings of each element in the set of matched elements, filtered by a
   * selector.
   */
  public GQuery nextAll(String filter) {
    JsNodeArray result = JsNodeArray.create();
    for (Element e : elements) {
      allNextSiblingElements(e.getNextSiblingElement(), result, null, null, filter);
    }

    return pushStack(unique(result), "nextAll", getSelector());
  }

  /**
   * Get all following siblings of each element up to but not including the element matched by the
   * selector.
   *
   * @param selector
   * @return
   */
  public GQuery nextUntil(String selector) {
    return nextUntil($(selector), null);
  }

  /**
   * Get all following siblings of each element up to but not including the element matched by the
   * selector, filtered by a selector.
   *
   * @param selector
   * @return
   */
  public GQuery nextUntil(String selector, String filter) {
    return nextUntil($(selector), filter);
  }

  /**
   * Get all following siblings of each element up to but not including the element matched by the
   * DOM node.
   *
   * @param selector
   * @return
   */
  public GQuery nextUntil(Element until) {
    return nextUntil($(until), null);
  }

  /**
   * Get all following siblings of each element up to but not including the element matched by the
   * DOM node, filtered by a selector.
   *
   * @return
   */
  public GQuery nextUntil(Element until, String filter) {
    return nextUntil($(until), filter);
  }

  /**
   * Get all following siblings of each element up to but not including the element matched by the
   * GQuery object.
   *
   * @return
   */
  public GQuery nextUntil(GQuery until) {
    return nextUntil(until, null);
  }

  /**
   * Get all following siblings of each element up to but not including the element matched by the
   * GQuery object, filtered by a selector.
   *
   * @return
   */
  public GQuery nextUntil(GQuery until, String filter) {
    JsNodeArray result = JsNodeArray.create();
    for (Element e : elements) {
      allNextSiblingElements(e.getNextSiblingElement(), result, null, until, filter);
    }
    return pushStack(unique(result), "nextUntil", getSelector());
  }

  /**
   * Removes the specified Element from the set of matched elements. This method is used to remove a
   * single Element from a jQuery object.
   */
  public GQuery not(Element elem) {
    JsNodeArray array = JsNodeArray.create();
    for (Element e : elements) {
      if (e != elem) {
        array.addNode(e);
      }
    }
    return $(array);
  }

  /**
   * Removes any elements inside the passed set of elements from the set of matched elements.
   */
  public GQuery not(GQuery gq) {
    GQuery ret = this;
    for (Element e : gq.elements) {
      ret = ret.not(e);
    }
    return ret;
  }

  /**
   * Removes elements matching the specified expression from the set of matched elements.
   */
  public GQuery not(String... filters) {
    GQuery ret = this;
    for (String f : filters) {
      ret = ret.not($(f));
    }
    return ret;
  }

  /**
   * Get the current offset of the first matched element, in pixels, relative to the document. The
   * returned object contains two integer properties, top and left. The method works only with
   * visible elements.
   */
  public Offset offset() {
    Element e = get(0);
    return e == null ? new Offset(0, 0) : new Offset(e.getAbsoluteLeft(), e.getAbsoluteTop());
  }

  /**
   * Set the current coordinates of every element in the set of matched elements, relative to the document.
   */
  public GQuery offset(Offset offset) {
    assert offset() != null : "offset cannot be null";
    return offset(offset.top, offset.left);
  }

  /**
   * Set the current coordinates of every element in the set of matched elements, relative to the document.
   */
  public GQuery offset(int top, int left) {
    for (Element element : elements()) {
      GQuery g = $(element);

      String position = g.css("position", true);
      if ("static".equals(position)) {
        css("position", "relative");
      }

      Offset curOffset = g.offset();
      String curCSSTop = g.css("top", true);
      String curCSSLeft = g.css("left", true);
      long curTop = 0;
      long curLeft = 0;

      if (("absolute".equals(position) || "fixed".equals(position))
          && ("auto".equals(curCSSTop) || "auto".equals(curCSSLeft))) {
        Offset curPosition = g.position();
        curTop = curPosition.top;
        curLeft = curPosition.left;
      } else {
        try {
          curTop = Long.parseLong(curCSSTop);
        } catch (NumberFormatException e) {
          curTop = 0;
        }

        try {
          curLeft = Long.parseLong(curCSSLeft);
        } catch (NumberFormatException e) {
          curLeft = 0;
        }
      }

      long newTop = top - curOffset.top + curTop;
      long newLeft = left - curOffset.left + curLeft;

      g.css("top", "" + newTop).css("left", "" + newLeft);
    }

    return this;
  }

  /**
   * Returns a GQuery collection with the positioned parent of the first matched element. This is
   * the first parent of the element that has position (as in relative or absolute). This method
   * only works with visible elements.
   */
  public GQuery offsetParent() {
    if (isEmpty()) {
      return $();
    }
    Element offParent = JsUtils.or(get(0).getOffsetParent(), body);
    while (offParent != null && !"body".equalsIgnoreCase(offParent.getTagName())
        && !"html".equalsIgnoreCase(offParent.getTagName())
        && "static".equals(getStyleImpl().curCSS(offParent, "position", true))) {
      offParent = offParent.getOffsetParent();
    }
    return new GQuery(offParent);
  }

  /**
   * Attach an event handler function for one or more events to the selected elements.
   */
  public GQuery on(String eventName, Function... funcs) {
    return bind(eventName, funcs);
  }

  /**
   * Attach an event handler function for one or more events to the selected elements.
   */
  public GQuery on(String eventName, Object data, Function... funcs) {
    return bind(eventName, data, funcs);
  }

  /**
   * Attach an event handler function for one or more events to the selected elements.
   */
  public GQuery on(String eventName, String selector, Function... funcs) {
    if (selector == null || selector.isEmpty()) {
      return on(eventName, funcs);
    }

    return delegate(selector, eventName, funcs);
  }

  /**
   * Attach an event handler function for one or more events to the selected elements.
   */
  public GQuery on(String eventName, String selector, Object data, Function... funcs) {
    if (selector == null || selector.isEmpty()) {
      return on(eventName, data, funcs);
    }

    return delegate(selector, eventName, data, funcs);
  }

  /**
   * Remove all event handlers.
   */
  public GQuery off() {
    return as(Events).off();
  }

  /**
   * Remove an event handler.
   */
  public GQuery off(String eventName) {
    return unbind(eventName, null);
  }

  /**
   * Remove an event handler.
   */
  public GQuery off(String eventName, Function f) {
    return unbind(eventName, f);
  }

  /**
   * Remove an event handler.
   */
  public GQuery off(String eventName, String selector) {
    if (selector == null || selector.isEmpty()) {
      return off(eventName);
    }
    return undelegate(selector, eventName);
  }

  /**
   * Binds a handler to a particular Event (like Event.ONCLICK) for each matched element. The
   * handler is executed only once for each element.
   *
   * The event handler is passed as a Function that you can use to prevent default behavior. To stop
   * both default action and event bubbling, the function event handler has to return false.
   *
   * You can pass an additional Object data to your Function as the second parameter
   */
  public GQuery one(int eventbits, final Object data, final Function f) {
    return as(Events).one(eventbits, data, f);
  }

  /**
   * Get the current computed height for the first element in the set of matched elements, including
   * padding, border, but not the margin.
   */
  public int outerHeight() {
    return outerHeight(false);
  }

  /**
   * Get the current computed height for the first element in the set of matched elements, including
   * padding, border, and optionally margin.
   */
  public int outerHeight(boolean includeMargin) {
    if (isEmpty()) {
      return 0;
    }
    // height including padding and border
    int outerHeight = (int) cur("offsetHeight", true);
    if (includeMargin) {
      outerHeight += cur("marginTop", true) + cur("marginBottom", true);
    }
    return outerHeight;
  }

  /**
   * Get the current computed width for the first element in the set of matched elements, including
   * padding, border, but not the margin.
   */
  public int outerWidth() {
    return outerWidth(false);
  }

  /**
   * Get the current computed width for the first element in the set of matched elements, including
   * padding and border and optionally margin.
   */
  public int outerWidth(boolean includeMargin) {
    if (isEmpty()) {
      return 0;
    }
    // width including padding and border
    int outerWidth = (int) cur("offsetWidth", true);
    if (includeMargin) {
      outerWidth += cur("marginRight", true) + cur("marginLeft", true);
    }
    return outerWidth;
  }

  /**
   * Get a set of elements containing the unique parents of the matched set of elements.
   */
  public GQuery parent() {
    JsNodeArray result = JsNodeArray.create();
    for (Element e : elements) {
      Element p = e.getParentElement();
      if (p != null) {
        result.addNode(p);
      }
    }
    return new GQuery(unique(result));
  }

  /**
   * Get a set of elements containing the unique parents of the matched set of elements. You may use
   * an optional expressions to filter the set of parent elements that will match one of them.
   */
  public GQuery parent(String... filters) {
    return parent().filter(filters);
  }

  /**
   * Get a set of elements containing the unique ancestors of the matched set of elements (except
   * for the root element).
   */
  public GQuery parents() {
    return parentsUntil((String) null);
  }

  /**
   * Get a set of elements containing the unique ancestors of the matched set of elements (except
   * for the root element). The matched elements are filtered, returning those that match any of the
   * filters.
   */
  public GQuery parents(String... filters) {
    return parents().filter(filters);
  }

  /**
   * Get the ancestors of each element in the current set of matched elements, up to but not
   * including the element matched by the selector.
   */
  public GQuery parentsUntil(final String selector) {
    return parentsUntil(new Predicate() {
      @Override
      public <T> boolean f(T e, int index) {
        return selector != null && $(e).is(selector);
      }
    });
  }

  /**
   * Get the ancestors of each element in the current set of matched elements, up to but not
   * including the node.
   */
  public GQuery parentsUntil(final Node node) {
    return parentsUntil(new Predicate() {
      @Override
      public <T> boolean f(T e, int index) {
        return node != null && e == node;
      }
    });
  }

  private GQuery parentsUntil(Predicate predicate) {
    JsNodeArray result = JsNodeArray.create();
    for (Element e : elements) {
      int i = 0;
      Node par = e.getParentNode();
      while (par != null && par != document) {
        if (predicate.f(par, i)) {
          break;
        }
        result.addNode(par);
        par = par.getParentNode();
        i++;
      }
    }
    return new GQuery(unique(result)).setPreviousObject(this);
  }

  /**
   * Gets the top and left position of an element relative to its offset parent. The returned object
   * contains two Integer properties, top and left. For accurate calculations make sure to use pixel
   * values for margins, borders and padding. This method only works with visible elements.
   */
  public Offset position() {
    if (isEmpty()) {
      return new Offset(0, 0);
    }
    Element element = get(0);
    // Get *real* offsetParent
    Element offsetParent = get(0).getOffsetParent();
    // Get correct offsets
    Offset offset = offset();
    Offset parentOffset = null;
    if (offsetParent == body || offsetParent == (Node) document) {
      parentOffset = new Offset(0, 0);
    } else {
      parentOffset = $(offsetParent).offset();
    }

    // Subtract element margins
    int topMargin = (int) getStyleImpl().cur(element, "marginTop", true);
    // TODO: move this check to getStyleImpl()
    // When margin-left = auto, Safari and chrome return a value while IE and
    // Firefox return 0
    // force the margin-left to 0 if margin-left = auto.
    int leftMargin = 0;
    if (!"auto".equals(element.getStyle().getMarginLeft())) {
      leftMargin = (int) getStyleImpl().cur(element, "marginLeft", true);
    }

    offset = offset.add(-leftMargin, -topMargin);

    // Add offsetParent borders
    int parentOffsetBorderTop = (int) getStyleImpl().cur(offsetParent, "borderTopWidth", true);
    int parentOffsetBorderLeft = (int) getStyleImpl().cur(offsetParent, "borderLeftWidth", true);
    parentOffset = parentOffset.add(parentOffsetBorderLeft, parentOffsetBorderTop);

    // Subtract the two offsets
    return offset.add(-parentOffset.left, -parentOffset.top);
  }

  /**
   * Prepend content to the inside of every matched element. This operation is the best way to
   * insert elements inside, at the beginning, of all matched elements.
   */
  public GQuery prepend(GQuery query) {
    return domManip(query, DomMan.PREPEND);
  }

  /**
   * Prepend content to the inside of every matched element. This operation is the best way to
   * insert elements inside, at the beginning, of all matched elements.
   */
  public GQuery prepend(Node n) {
    return domManip($(n), DomMan.PREPEND);
  }

  /**
   * Prepend content to the inside of every matched element. This operation is the best way to
   * insert elements inside, at the beginning, of all matched elements.
   */
  public GQuery prepend(String html) {
    return domManip(html, DomMan.PREPEND);
  }

  /**
   * All of the matched set of elements will be inserted at the beginning of the element(s)
   * specified by the parameter other.
   *
   * The operation $(A).prependTo(B) is, essentially, the reverse of doing a regular
   * $(A).prepend(B), instead of prepending B to A, you're prepending A to B.
   */
  public GQuery prependTo(GQuery other) {
    other.prepend(this);
    return this;
  }

  /**
   * All of the matched set of elements will be inserted at the beginning of the element(s)
   * specified by the parameter other.
   *
   * The operation $(A).prependTo(B) is, essentially, the reverse of doing a regular
   * $(A).prepend(B), instead of prepending B to A, you're prepending A to B.
   */
  public GQuery prependTo(Node n) {
    $(n).prepend(this);
    return this;
  }

  /**
   * All of the matched set of elements will be inserted at the beginning of the element(s)
   * specified by the parameter other.
   *
   * The operation $(A).prependTo(B) is, essentially, the reverse of doing a regular
   * $(A).prepend(B), instead of prepending B to A, you're prepending A to B.
   */
  public GQuery prependTo(String html) {
    $(html).prepend(this);
    return this;
  }

  /**
   * Get a set of elements containing the unique previous siblings of each of the matched set of
   * elements. Only the immediately previous sibling is returned, not all previous siblings.
   */
  public GQuery prev() {
    JsNodeArray result = JsNodeArray.create();
    for (Element e : elements) {
      Element next = getPreviousSiblingElement(e);
      if (next != null) {
        result.addNode(next);
      }
    }
    return new GQuery(unique(result));
  }

  /**
   * Get a set of elements containing the unique previous siblings of each of the matched set of
   * elements filtered by selector. Only the immediately previous sibling is returned, not all
   * previous siblings.
   */
  public GQuery prev(String... selectors) {
    JsNodeArray result = JsNodeArray.create();
    for (Element e : elements) {
      Element next = getPreviousSiblingElement(e);
      if (next != null) {
        result.addNode(next);
      }
    }
    return new GQuery(unique(result)).filter(selectors);
  }

  /**
   * Get all preceding siblings of each element in the set of matched elements.
   */
  public GQuery prevAll() {
    return prevAll(null);
  }

  /**
   * Get all preceding siblings of each element in the set of matched elements filtered by a
   * selector.
   */
  public GQuery prevAll(String selector) {
    JsNodeArray result = JsNodeArray.create();
    for (Element e : elements) {
      allPreviousSiblingElements(getPreviousSiblingElement(e), result, null, selector);
    }
    return pushStack(unique(result), "prevAll", getSelector());
  }

  /**
   * Get all preceding siblings of each element up to but not including the element matched by the
   * <code>selector</code>.
   *
   * The elements are returned in order from the closest sibling to the farthest.
   */
  public GQuery prevUntil(String selector) {
    return prevUntil($(selector), null);
  }

  /**
   * Get all preceding siblings of each element up to but not including the <code>until</code>
   * element.
   *
   * The elements are returned in order from the closest sibling to the farthest.
   */
  public GQuery prevUntil(Element until) {
    return prevUntil($(until), null);
  }

  /**
   * Get all preceding siblings of each element up to but not including the <code>until</code>
   * element.
   *
   * The elements are returned in order from the closest sibling to the farthest.
   */
  public GQuery prevUntil(GQuery until) {
    return prevUntil(until, null);
  }

  /**
   * Get all preceding siblings of each element matching the <code>filter</code> up to but not
   * including the element matched by the <code>selector</code>.
   *
   * The elements are returned in order from the closest sibling to the farthest.
   */
  public GQuery prevUntil(String selector, String filter) {
    return prevUntil($(selector), filter);
  }

  /**
   * Get all preceding siblings of each element matching the <code>filter</code> up to but not
   * including the <code>until</code> element.
   *
   */
  public GQuery prevUntil(Element until, String filter) {
    return prevUntil($(until), filter);
  }

  /**
   * Get all preceding siblings of each element matching the <code>filter</code> up to but not
   * including the element matched by the <code>until</code> element.
   *
   */
  public GQuery prevUntil(GQuery until, String filter) {
    JsNodeArray result = JsNodeArray.create();
    for (Element e : elements) {
      allPreviousSiblingElements(getPreviousSiblingElement(e), result, until, filter);
    }
    return pushStack(unique(result), "prevUntil", getSelector());
  }

  /**
   * Returns a dynamically generated Promise that is resolved once all actions
   * in the queue have ended.
   */
  public Promise promise() {
    return as(Queue).promise();
  }

  /**
   * Returns a dynamically generated Promise that is resolved once all actions
   * in the named queue have ended.
   */
  public Promise promise(String name) {
    return as(Queue).promise(name);
  }

  /**
   * Get the value of a property for the first element in the set of matched elements.
   *
   * @param key the name of the property to be accessed
   * @return the value of the property, in the case the property is a 'boolean' it
   *        returns a Boolean object, and a Double if is a 'number', so be prepared
   *        if you cast to other numeric objects. In the case of the property is undefined
   *        it returns null.
   */
  public <T> T prop(String key) {
    assert key != null : "Key is null";
    return isEmpty() ? null : JsUtils.<T> prop(get(0), key);
  }

  /**
   * Get the value of a property for the first element in the set of matched elements.
   *
   * @param key the name of the property to be accessed
   * @param clz the class of the type to return
   *
   * @return the value of the property, it safely check the type passed as parameter
   *        and preform the aproproate transformations for numbers and booleans.
   *        In the case of the property is undefined it returns null.
   */
  public <T> T prop(String key, Class<? extends T> clz) {
    assert key != null : "Key is null";
    return isEmpty() ? null : JsUtils.<T> prop(get(0), key, clz);
  }

  /**
   * Sets a property to a value on all matched elements.
   *
   * @param key the name of the boolean property to be set
   * @param value the value specified. In the case the value is a Number, it is set
   *        as a 'number' in the javascript object and the same with Boolean.
   *
   * @return this <code>GQuery</code> object
   *
   */
  public GQuery prop(String key, Object value) {
    assert key != null : "Key is null";

    for (Element e : elements) {
      JsUtils.prop(e, key, value);
    }

    return this;
  }

  /**
   * Sets a boolean property to a computed value on all matched elements.
   *
   * @param key the name of the boolean property to be set
   * @param closure the closure to be used to compute the value the specified boolean property
   *          should be set to; the <code>closure</code> is
   *          {@linkplain Function#f(com.google.gwt.dom.client.Element, int) passed} the target
   *          element and its index as arguments and is expected to return either a
   *          <code>Boolean</code> value or an object whose textual representation is converted to a
   *          <code>Boolean</code> value; <code>null</code> return values are ignored
   *
   * @return this <code>GQuery</code> object
   *
   */
  public GQuery prop(String key, Function closure) {
    assert key != null : "Key is null";
    assert closure != null : "Closure is null";

    int i = 0;
    for (Element e : elements) {
      Object value = closure.f(e, i++);
      JsUtils.prop(e, key, value);
    }

    return this;
  }

  protected GQuery pushStack(JsNodeArray elts, String name, String selector) {
    GQuery g = new GQuery(elts);
    g.setPreviousObject(this);
    g.setSelector(selector);
    g.currentContext = currentContext;
    return g;
  }

  /**
   * Show the number of functions in the efects queue to be executed on the first matched element.
   */
  public int queue() {
    return as(Queue).queue();
  }

  /**
   * Put a set of {@link Function} at the end of the Effects queue.
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
   *        })
   *       .animate("left:'-=500'", 400)
   *       .queue(lazy().css("color", "yellow");
   *
   * </pre>
   *
   * When this statement is executed, the element move to 500 px to left for 400 ms, then its
   * background color is changed to red and then move to 500px to right for 400ms, and finally its
   * color is set to yellow.
   *
   * Please note that {@link #dequeue()} function is needed at the end of your function to start the
   * next function in the queue. In lazy() methods you should call dequeue() just before the done()
   * call. {@see #dequeue()}
   */
  public GQuery queue(Function... f) {
    return as(Queue).queue(f);
  }

  /**
   * Show the number of functions in the queued named as queueName to be executed on the first
   * matched element.
   */
  public int queue(String queueName) {
    return as(Queue).queue(queueName);
  }

  /**
   * Put a set of {@link Function} at the end of a queue.
   *
   * Example:
   *
   * <pre class="code">
   * $("#foo").queue("myQueue", new Function(){
   *          public void f(Element e){
   *             $(e).css(CSS.BACKGROUNG_COLOR.with(RGBColor.RED));
   *             dequeue("myQueue");
   *          }
   *        })
   *        .delay(500, "myQueue")
   *        .queue("myQueue", lazy().css(CSS.COLOR.with(RGBColor.YELLOW)).dequeue("myQueue").done());
   * </pre>
   *
   * When this statement is executed, the background color of the element is set to red, then wait
   * 500ms before to set the text color of the element to yellow. right for 400ms.
   *
   * Please note that {@link #dequeue()} function is needed at the end of your function to start the
   * next function in the queue. In lazy() methods you should call dequeue() just before the done()
   * call. {@see #dequeue()}
   */
  public GQuery queue(String queueName, Function... f) {
    return as(Queue).queue(queueName, f);
  }

  /**
   * Specify a function to execute when the DOM is fully loaded.
   *
   * While JavaScript provides the load event for executing code when a page is rendered, this event
   * is not seen if we attach an event listener after the document has been loaded.
   * This guarantees that our gwt code will be executed either it's executed synchronously before the
   * DOM has been rendered (ie: single script linker in header) or asynchronously.
   */
  public Promise ready(Function... fncs) {
    return new PromiseFunction() {
      public void f(final com.google.gwt.query.client.Promise.Deferred dfd) {
        if ("complete" == $(document).prop("readyState")) {
          dfd.resolve();
        } else {
          $(document).on("load", new Function() {
            public void f() {
              dfd.resolve();
            }
          });
        }
      }
    }.done(fncs);
  }

  /**
   * Removes all matched elements from the DOM.
   */
  public GQuery remove() {
    return remove(null, true);
  }

  /**
   * Removes from the DOM all matched elements filtered by the <code>filter</code>.
   */
  public GQuery remove(String filter) {
    return remove(filter, true);
  }

  /**
   * Removes all matched elements from the DOM and cleans their data and bound events if the value
   * of <code>clean</code> parameter is set to true. The <code> filter</code> parameter allows to
   * filter the matched set to remove.
   */
  protected GQuery remove(String filter, boolean clean) {
    for (Element e : elements) {
      if (filter == null || $(e).filter(filter).length() == 1) {
        if (clean) {
          // clean data linked to the children
          // TODO: "*" fails in queryselectorall (webkit mobile)
          cleanGQData($("*", e).elements());
          // clean data linked to the element itself
          cleanGQData(e);
        }
        Widget w = getAssociatedWidget(e);
        if (w != null) {
          w.removeFromParent();
        } else {
          e.removeFromParent();
        }
      }
    }
    return this;
  }

  /**
   * Remove the named attribute from every element in the matched set.
   */
  public GQuery removeAttr(String key) {
    getAttributeImpl().removeAttribute(this, key);
    return this;
  }

  /**
   * Removes the specified classes to each matched element.
   *
   * If no arguments are provided, it removes all classes like jquery does.
   */
  public GQuery removeClass(String... classes) {
    for (Element e : elements) {
      if (Element.is(e)) {
        if (classes.length == 0) {
          e.setClassName(null);
        } else {
          for (String clz : classes) {
            e.removeClassName(clz);
          }
        }
      }
    }
    return this;
  }

  protected void removeData(Element item, String name) {
    if (dataCache == null) {
      windowData = JavaScriptObject.createObject().cast();
      dataCache = JavaScriptObject.createObject().cast();
    }
    item = item == window || item.getNodeName() == null ? windowData : item;
    int id = item.hashCode();
    if (name != null) {
      if (dataCache.exists(id)) {
        dataCache.getCache(id).delete(name);
        if (dataCache.getCache(id).isEmpty()) {
          // Save memory
          removeData(item, null);
        }
      }
    } else {
      // when the element cache is empty we remove its entry to save memory (issue 132)
      dataCache.delete(id);
    }
  }

  /**
   * Removes named data store from an element.
   */
  public GQuery removeData(String name) {
    for (Element e : elements) {
      removeData(e, name);
    }
    return this;
  }

  /**
   * Remove a property for the set of matched elements.
   */
  public GQuery removeProp(String name) {
    for (Element e : elements) {
      e.<JsCache> cast().delete(name);
    }
    return this;
  }

  /**
   * Replaces the element <code>elem</code> by the specified selector with the matched elements.
   * This function is the complement to replaceWith() which does the same task with the parameters
   * reversed.
   *
   * @return a {@link GQuery} object containing the new elements.
   */
  public GQuery replaceAll(Element elem) {
    return replaceAll($(elem));
  }

  /**
   * Replaces the elements matched by the target with the selected elements. This function is the
   * complement to replaceWith() which does the same task with the parameters reversed.
   *
   * @return a {@link GQuery} object containing the new elements.
   */
  public GQuery replaceAll(GQuery target) {
    // if there is only one element and it is not attached to the dom, we have
    // to clone it to be reused on each element of target (if target contains
    // more than one element)
    boolean mustBeCloned = length() == 1 && parents().filter("body").length() == 0;

    List<Element> newElements = new ArrayList<>();
    for (int i = 0, l = target.size(); i < l; i++) {
      GQuery that = (i > 0 && mustBeCloned) ? this.clone() : this;
      $(target.get(i)).replaceWith(that);
      newElements.addAll(Arrays.asList(that.elements));
    }
    return $(newElements);
  }

  /**
   * Replaces the elements matched by the specified selector with the matched elements. This
   * function is the complement to replaceWith() which does the same task with the parameters
   * reversed.
   *
   * @return a {@link GQuery} object containing the new elements.
   */
  public GQuery replaceAll(String selector) {
    return replaceAll($(selector));
  }

  /**
   * Replaces all matched elements with the specified element.
   *
   * @return the GQuery element that was just replaced, which has been removed from the DOM and not
   *         the new element that has replaced it.
   */
  public GQuery replaceWith(Element elem) {
    return replaceWith($(elem));
  }

  /**
   * Replaces all matched elements with elements selected by <code>target</code> .
   *
   * @return the GQuery element that was just replaced, which has been removed from the DOM and not
   *         the new element that has replaced it.
   */
  public GQuery replaceWith(GQuery target) {
    for (Element el : elements) {
      Element nextSibling = el.getNextSiblingElement();

      if (nextSibling != null) {
        $(nextSibling).before(target);
      } else {
        Element parent = el.getParentElement();
        $(parent).append(target);
      }
      $(el).remove();
    }
    return this;
  }

  /**
   * Replaces all matched elements with the specified HTML.
   *
   * @return the GQuery element that was just replaced, which has been removed from the DOM and not
   *         the new element that has replaced it.
   */
  public GQuery replaceWith(String html) {
    for (Element el : elements) {
      Element nextSibling = el.getNextSiblingElement();

      if (nextSibling != null) {
        $(nextSibling).before(html);
      } else {
        Element parent = el.getParentElement();
        $(parent).append(html);
      }
      $(el).remove();
    }
    return this;
  }

  /**
   * Bind a set of functions to the resize event of each matched element, or tigger the resize event
   * if no functions are provided.
   *
   * Note that although all elements can be configured to handle resize events, by default only
   * window will trigger it when it is resized, for an arbitrary element you have to trigger the
   * event after resizing the object.
   *
   */
  public GQuery resize(Function... f) {
    return bindOrFire("resize", null, f);
  }

  /**
   * Bind an event handler to the "resize" JavaScript event, or trigger that event on an element.
   */
  public GQuery resize(final Function f) {
    return bindOrFire("resize", null, f);
  }

  /**
   * Save a set of Css properties of every matched element.
   */
  public void restoreCssAttrs(String... cssProps) {
    for (Element e : elements) {
      for (String a : cssProps) {
        String datakey = OLD_DATA_PREFIX + a;
        getStyleImpl().setStyleProperty(e, a, (String) data(e, datakey, null));
        removeData(e, datakey);
      }
    }
  }

  /**
   * Restore a set of previously saved Css properties in every matched element.
   */
  public void saveCssAttrs(String... cssProps) {
    for (Element e : elements) {
      for (String a : cssProps) {
        data(OLD_DATA_PREFIX + a, getStyleImpl().curCSS(e, a, false));
      }
    }
  }

  /**
   * Bind a set of functions to the scroll event of each matched element. Or trigger the event if no
   * functions are provided.
   */
  public GQuery scroll(Function... f) {
    return bindOrFire(Event.ONSCROLL, null, f);
  }

  /**
   * Scrolls the first matched element into view.
   */
  public GQuery scrollIntoView() {
    if (!isEmpty()) {
      scrollIntoViewImpl(get(0));
    }
    return this;
  }

  /**
   * Scrolls the first matched element into view.
   *
   * If ensure == true, it crawls up the DOM hierarchy, adjusting the scrollLeft and scrollTop
   * properties of each scroll-able element to ensure that the specified element is completely in
   * view. It adjusts each scroll position by the minimum amount necessary.
   */
  public GQuery scrollIntoView(boolean ensure) {
    if (!isEmpty() && ensure) {
      DOM.scrollIntoView((com.google.gwt.user.client.Element) get(0));
    } else {
      scrollIntoView();
    }
    return this;
  }

  /**
   * Gets the scroll left offset of the first matched element. This method works for both visible
   * and hidden elements.
   */
  public int scrollLeft() {
    Element e = get(0);
    if (e == null) {
      return 0;
    }
    if (e == window || e.getNodeName() == null) {
      return Window.getScrollLeft();
    } else if (e == (Node) document) {
      return document.getScrollLeft();
    } else {
      return e.getScrollLeft();
    }
  }

  /**
   * The scroll left offset is set to the passed value on all matched elements. This method works
   * for both visible and hidden elements.
   */
  public GQuery scrollLeft(int left) {
    for (Element e : elements) {
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
   * Scrolls the contents of all matched elements to the specified co-ordinate becoming the top left
   * corner of the viewable area.
   *
   * This method is only useful where there are areas of the document not viewable within the
   * current viewable area of the window and the visible property of the window's scrollbar must be
   * set to true.
   *
   */
  public GQuery scrollTo(int left, int top) {
    scrollLeft(left).scrollTop(top);
    return this;
  }

  /**
   * Gets the scroll top offset of the first matched element. This method works for both visible and
   * hidden elements.
   */
  public int scrollTop() {
    Element e = get(0);
    if (e == null) {
      return 0;
    }
    if (e == window || e.getNodeName() == null) {
      return Window.getScrollTop();
    } else if (e == (Node) document) {
      return document.getScrollTop();
    } else {
      return e.getScrollTop();
    }
  }

  /**
   * The scroll top offset is set to the passed value on all matched elements. This method works for
   * both visible and hidden elements.
   */
  public GQuery scrollTop(int top) {
    for (Element e : elements) {
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

  private GQuery select(String selector, Node context) {
    NodeList<Element> n = getSelectorEngine().select(selector, context == null ? document :
        context);
    currentSelector = selector;
    currentContext = context != null ? context : document;
    return setArray(n);
  }

  /**
   * Force the current matched set of elements to become the specified array of elements.
   */
  public GQuery setArray(NodeList<Element> list) {
    if (list != null) {
      nodeList.<JsCache> cast().clear();
      int l = list.getLength();
      elements = new Element[l];
      for (int i = 0; i < l; i++) {
        elements[i] = list.getItem(i);
        nodeList.<JsObjectArray<Element>> cast().add(list.getItem(i));
      }
    }
    return this;
  }

  public GQuery setPreviousObject(GQuery previousObject) {
    this.previousObject = previousObject;
    return this;
  }

  public GQuery setSelector(String selector) {
    this.currentSelector = selector;
    return this;
  }

  /**
   * Make all matched elements visible.
   */
  public GQuery show() {
    for (Element e : elements) {
      String currentDisplay = e.getStyle().getDisplay();
      String oldDisplay = (String) data(e, OLD_DISPLAY, null);

      // reset the display
      if (oldDisplay == null && "none".equals(currentDisplay)) {
        getStyleImpl().setStyleProperty(e, "display", "");
        currentDisplay = "";
      }

      // check if the stylesheet impose display: none. If it is the case, determine
      // the default display for the tag and store it at the element level
      if ("".equals(currentDisplay) && !getStyleImpl().isVisible(e)) {
        data(e, OLD_DISPLAY, getStyleImpl().defaultDisplay(e.getNodeName()));
      }
    }

    // set the display value in a separate for loop to avoid constant reflow
    // because broswer reflow is triggered each time we gonna set and after get (in
    // isVisibleProperty() method)
    // the diplay property. Reflows is very bad in performance point of view
    for (Element e : elements) {
      String currentDisplay = e.getStyle().getDisplay();
      if ("".equals(currentDisplay) || "none".equals(currentDisplay)) {
        getStyleImpl().setStyleProperty(e, "display",
            JsUtils.or((String) data(e, OLD_DISPLAY, null), ""));
      }
    }
    removeData(OLD_DISPLAY);
    return this;
  }

  /**
   * Get a set of elements containing all of the unique siblings of each of the matched set of
   * elements.
   */
  public GQuery siblings() {
    JsNodeArray result = JsNodeArray.create();
    for (Element e : elements) {
      allNextSiblingElements(e.getParentElement().getFirstChildElement(), result, e, null, null);
    }
    return new GQuery(unique(result));
  }

  /**
   * Get a set of elements containing all of the unique siblings of each of the matched set of
   * elements filtered by the provided set of selectors.
   */
  public GQuery siblings(String... selectors) {
    return siblings().filter(selectors);
  }

  /**
   * Return the number of elements in the matched set.
   */
  public int size() {
    return elements.length;
  }

  /**
   * Selects a subset of the matched elements.
   */
  public GQuery slice(int start, int end) {
    JsNodeArray slice = JsNodeArray.create();
    int l = size();
    if (end == -1 || end > l) {
      end = l;
    }
    for (int i = start; i < end; i++) {
      slice.addNode(get(i));
    }
    return new GQuery(slice);
  }

  /**
   * Reveal all matched elements by adjusting their height and firing an optional callback after
   * completion.
   */
  public Effects slideDown(Function... f) {
    return as(Effects).slideDown(f);
  }

  /**
   * Reveal all matched elements by adjusting their height and firing an optional callback after
   * completion.
   */
  public Effects slideDown(int millisecs, Function... f) {
    return as(Effects).slideDown(millisecs, f);
  }

  /**
   * Toggle the visibility of all matched elements by adjusting their height and firing an optional
   * callback after completion. Only the height is adjusted for this animation, causing all matched
   * elements to be hidden or shown in a "sliding" manner
   */
  public Effects slideToggle(Function... f) {
    return as(Effects).slideToggle(f);
  }

  /**
   * Toggle the visibility of all matched elements by adjusting their height and firing an optional
   * callback after completion. Only the height is adjusted for this animation, causing all matched
   * elements to be hidden or shown in a "sliding" manner
   */
  public Effects slideToggle(int millisecs, Function... f) {
    return as(Effects).slideToggle(millisecs, f);
  }

  /**
   * Hide all matched elements by adjusting their height and firing an optional callback after
   * completion.
   */
  public Effects slideUp(Function... f) {
    return as(Effects).slideUp(f);
  }

  /**
   * Hide all matched elements by adjusting their height and firing an optional callback after
   * completion.
   */
  public Effects slideUp(int millisecs, Function... f) {
    return as(Effects).slideUp(millisecs, f);
  }

  /**
   * When .stop() is called on an element, the currently-running animation (if any) is immediately
   * stopped. If, for instance, an element is being hidden with .slideUp() when .stop() is called,
   * the element will now still be displayed, but will be a fraction of its previous height.
   * Callback functions are not called but the next animation in the queue begins immediately.
   */
  public GQuery stop() {
    return stop(false);
  }

  /**
   * When .stop() is called on an element, the currently-running animation (if any) is immediately
   * stopped. If, for instance, an element is being hidden with .slideUp() when .stop() is called,
   * the element will now still be displayed, but will be a fraction of its previous height.
   * Callback functions are not called but the next animation in the queue begins immediately.
   *
   * If the clearQueue parameter is provided with a value of true, then the rest of the animations
   * in the queue are removed and never run.
   */
  public GQuery stop(boolean clearQueue) {
    return stop(clearQueue, false);
  }

  /**
   * When .stop() is called on an element, the currently-running animation (if any) is immediately
   * stopped. If, for instance, an element is being hidden with .slideUp() when .stop() is called,
   * the element will now still be displayed, but will be a fraction of its previous height.
   * Callback functions are not called but the next animation in the queue begins immediately.
   *
   * If the clearQueue parameter is provided with a value of true, then the rest of the animations
   * in the queue are removed and never run.
   *
   * If the jumpToEnd property is provided with a value of true, the current animation stops, but
   * the element is immediately given its target values for each CSS property. The callback
   * functions are then immediately called, if provided.
   */
  public GQuery stop(boolean clearQueue, boolean jumpToEnd) {
    return as(Queue).stop(clearQueue, jumpToEnd);
  }

  /**
   * Bind a set of functions to the submit event of each matched element. Or submit a form if no
   * functions are provided.
   */
  public GQuery submit(Function... funcs) {
    return bindOrFire("submit", null, funcs);
  }

  /**
   * Return the concatened text contained in the matched elements.
   */
  public String text() {
    String result = "";
    for (Element e : elements) {
      result += JsUtils.text(e);
    }
    return result;
  }

  /**
   * Set the innerText of every matched element.
   */
  public GQuery text(String txt) {
    for (Element e : elements) {
      e.setInnerText(txt);
    }
    return this;
  }

  /**
   * Toggle visibility of elements.
   */
  public GQuery toggle() {
    for (Element e : elements) {
      if (getStyleImpl().isVisible(e)) {
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
    for (Element e : elements) {
      $(e).click(new Function() {
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
   * Adds or removes the specified classes to each matched element depending on the class's
   * presence.
   */
  public GQuery toggleClass(String... classes) {
    for (Element e : elements) {
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
   * Adds or removes the specified classes to each matched element depending on the value of the
   * switch argument.
   *
   * if addOrRemove is true, the class is added and in the case of false it is removed.
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
   * Returns the computed top position of the first element matched.
   */
  public int top() {
    return (int) cur("top", true);
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
    for (Element e : elements) {
      if (window.equals(e)) {
        continue;
      }
      String elStr;
      try {
        elStr = JsUtils.isXML(e) ? JsUtils.XML2String(e) : e.getString();
      } catch (Exception e2) {
        elStr =
            "< " + (e == null ? "null" : e.getNodeName())
                + "(gquery, error getting the element string representation: " + e2.getMessage()
                + ")/>";
      }
      r += (pretty && r.length() > 0 ? "\n " : "") + elStr;
    }
    return r;
  }

  /**
   * Trigger a browser native event on each matched element.
   */
  public GQuery trigger(NativeEvent event) {
    return as(Events).trigger(event, new Function[] {});
  }

  /**
   * Trigger a set of events on each matched element.
   *
   * For keyboard events you can pass a second parameter which represents the key-code of the pushed
   * key.
   *
   * Example: fire(Event.ONCLICK | Event.ONFOCUS) Example: fire(Event.ONKEYDOWN. 'a');
   */
  public GQuery trigger(int eventbits, int... keys) {
    return as(Events).trigger(eventbits, keys);
  }

  /**
   * Trigger a event in all matched elements.
   *
   * @param eventName An string representing the type of the event desired
   * @param datas Additional parameters to pass along to the event handlers.
   */
  public GQuery trigger(String eventName, Object... datas) {
    return as(Events).triggerHtmlEvent(eventName, datas);
  }

  /**
   * Removes all events that match the eventbits.
   */
  public GQuery unbind(int eventbits) {
    return as(Events).unbind(eventbits);
  }

  /**
   * Removes the function passed from the set of events which match the eventbits.
   */
  public GQuery unbind(int eventbits, Function f) {
    return as(Events).unbind(eventbits, null, f);
  }

  /**
   * Removes all events that match the eventList.
   */
  public GQuery unbind(String eventList) {
    return unbind(eventList, null);
  }

  /**
   * Removes all events that match the eventList.
   */
  public GQuery unbind(String eventList, Function f) {
    return as(Events).unbind(eventList, f);
  }

  /**
   * Remove all event delegation that have been bound using
   * {@link #delegate(String, int, Function...)} {@link #live(int, Function...)} methods.
   * 
   * @deprecated use {@link #off()}
   */
  public GQuery undelegate() {
    return as(Events).undelegate();
  }

  /**
   * Undelegate is a way of removing event handlers that have been bound using
   * {@link #delegate(String, int, Function...)} method.
   * 
   * @deprecated use {@link #off(String)}
   */
  public GQuery undelegate(String selector) {
    for (Element e : elements) {
      $(selector, e).die();
    }

    return this;
  }

  /**
   * Undelegate is a way of removing event handlers that have been bound using
   * {@link #delegate(String, int, Function...)} method.
   * 
   * @deprecated use {@link #off(String)}
   */
  public GQuery undelegate(String selector, int eventBit) {
    for (Element e : elements) {
      $(selector, e).die(eventBit);
    }

    return this;
  }

  /**
   * Undelegate is a way of removing event handlers that have been bound using
   * {@link #delegate(String, int, Function...)} method.
   * 
   * @deprecated use {@link #off(String, String)}
   */
  public GQuery undelegate(String selector, String eventName) {
    for (Element e : elements) {
      $(selector, e).die(eventName);
    }

    return this;
  }

  /**
   * Remove all duplicate elements from an array of elements. Note that this only works on arrays of
   * DOM elements, not strings or numbers.
   */
  public JsNodeArray unique(NodeList<Element> result) {
    return JsUtils.unique(result.<JsArray<Element>> cast()).cast();
  }

  /**
   * This method removes the element's parent. The matched elements replaces their parents within
   * the DOM structure. It is the inverse of {@link GQuery#wrap(GQuery)} method.
   *
   * @return
   */
  public GQuery unwrap() {

    for (Element parent : parent().elements) {
      if (!"body".equalsIgnoreCase(parent.getTagName())) {
        GQuery g = $(parent);
        g.replaceWith(g.children());
      }
    }
    return this;
  }

  /**
   * Gets the content of the value attribute of the first matched element, returns only the first
   * value even if it is a multivalued element. To get an array of all values in multivalues
   * elements use vals().
   *
   * When the first element is a radio-button and is not checked, then it looks for the first
   * checked radio-button that has the same name in the list of matched elements.
   *
   * When there are not matched elements it returns null.
   */
  public String val() {
    if (isEmpty()) {
      return null;
    }
    String[] v = vals();
    return v == null ? null : v.length > 0 ? v[0] : "";
  }

  /**
   * Sets the value attribute of every matched element based in the return value of the function
   * evaluated for this element.
   *
   * NOTE: in jquery the function receives the arguments in different way, first index and them the
   * actual value, but we use the normal way in gquery Function, first the element and second the
   * index.
   */
  public GQuery val(Function f) {
    for (int i = 0; i < size(); i++) {
      eq(i).val(f.f(get(i), i).toString());
    }
    return this;
  }

  /**
   * Sets the 'value' attribute of every matched element, but does not set the checked flag to
   * checkboxes or radiobuttons.
   *
   * If you wanted to set values in collections of checkboxes o radiobuttons use val(String[])
   * instead
   */
  public GQuery val(String value) {
    for (Element e : elements) {
      setElementValue(e, value);
    }
    return this;
  }

  /**
   * Sets the value of every matched element.
   *
   * There is a different behaviour depending on the element type:
   * <ul>
   * <li>select multiple: options whose value match any of the passed values will be set.
   * <li>select single: the last option whose value matches any of the passed values will be set.
   * <li>input radio: the last input whose value matches any of the passed values will be set.
   * <li>input checkbox: inputs whose value match any of the passed values will be set.
   * <li>textarea, button, and other input: value will set to a string result of joining with coma,
   * all passed values
   * </ul>
   *
   * NOTE: if you wanted call this function with just one parameter, you have to pass an array
   * signature to avoid call the overloaded val(String) method:
   *
   * $(...).val(new String[]{"value"});
   */
  public GQuery val(String... values) {
    String value = join(",", values);
    for (Element e : elements) {
      String name = e.getNodeName();
      if ("select".equalsIgnoreCase(name)) {
        SelectElement s = SelectElement.as(e);
        s.setSelectedIndex(-1);
        for (String v : values) {
          if (s.isMultiple()) {
            for (int i = 0, l = s.getOptions().getLength(); i < l; i++) {
              if (v.equals(s.getOptions().getItem(i).getValue())) {
                s.getOptions().getItem(i).setSelected(true);
              }
            }
          } else {
            s.setValue(v);
          }
        }
      } else if ("input".equalsIgnoreCase(name)) {
        InputElement ie = InputElement.as(e);
        String type = ie.getType();
        if ("radio".equalsIgnoreCase(type) || "checkbox".equalsIgnoreCase(type)) {
          ie.setChecked(false);
          for (String v : values) {
            if (ie.getValue().equals(v)) {
              ie.setChecked(true);
              break;
            }
          }
        } else {
          ie.setValue(value);
        }
      } else {
        setElementValue(e, value);
      }
    }
    return this;
  }

  /**
   * Gets the content of the value attribute of the first matched element, returns more than one
   * value if it is a multiple select.
   *
   * When the first element is a radio-button and is not checked, then it looks for a the first
   * checked radio-button that has the same name in the list of matched elements.
   *
   * This method always returns an array. If no valid value can be determined the array will be
   * empty, otherwise it will contain one or more values.
   */
  public String[] vals() {
    if (!isEmpty()) {
      Element e = get(0);
      if (e.getNodeName().equalsIgnoreCase("select")) {
        SelectElement se = SelectElement.as(e);
        if (se.isMultiple()) {
          JsArrayString result = JsArrayString.createArray().cast();
          for (int i = 0, l = se.getOptions().getLength(); i < l; i++) {
            OptionElement oe = se.getOptions().getItem(i);
            if (oe.isSelected()) {
              result.set(result.length(), oe.getValue());
            }
          }
          return result.length() > 0 ? jsArrayToString(result) : null;
        } else if (se.getSelectedIndex() >= 0) {
          return new String[] {se.getOptions().getItem(se.getSelectedIndex()).getValue()};
        }
      } else if (e.getNodeName().equalsIgnoreCase("input")) {
        InputElement ie = InputElement.as(e);
        return new String[] {ie.getValue()};
      } else if (e.getNodeName().equalsIgnoreCase("textarea")) {
        return new String[] {TextAreaElement.as(e).getValue()};
      } else if (e.getNodeName().equalsIgnoreCase("button")) {
        return new String[] {ButtonElement.as(e).getValue()};
      }
    }
    return new String[0];
  }

  @Deprecated
  public boolean visible() {
    return isVisible();
  }

  /**
   * Return the first non null attached widget from the matched elements or null if there isn't any.
   */
  @SuppressWarnings("unchecked")
  public <W extends Widget> W widget() {
    return (W) widget(0);
  }

  /**
   * Return the nth non null attached widget from the matched elements or null if there isn't any.
   */
  public <W extends Widget> W widget(int n) {
    for (Element e : elements) {
      @SuppressWarnings("unchecked")
      W w = (W) getAssociatedWidget(e);
      if (w != null) {
        if (n == 0) {
          return w;
        }
        n--;
      }
    }
    return null;
  }

  /**
   * return the list of attached widgets matching the query.
   */
  public List<Widget> widgets() {
    List<Widget> widgets = new ArrayList<>();
    for (Element e : elements) {
      Widget w = getAssociatedWidget(e);
      if (w != null) {
        widgets.add(w);
      }
    }
    return widgets;
  }

  /**
   * Return the list of attached widgets instance of the provided class matching the query.
   *
   * This method is very useful for decoupled views, so as we can access widgets from other views
   * without maintaining methods which export them.
   *
   */
  @SuppressWarnings("unchecked")
  public <W extends Widget> List<W> widgets(Class<W> clazz) {
    List<W> ret = new ArrayList<>();
    for (Widget w : widgets()) {
      // isAssignableFrom does not work in gwt.
      Class<?> c = w.getClass();
      do {
        if (c.equals(clazz)) {
          ret.add((W) w);
          break;
        }
        c = c.getSuperclass();
      } while (c != null);
    }
    return ret;
  }

  /**
   * Get the current computed, pixel, width of the first matched element. It does not include
   * margin, padding nor border.
   */
  public int width() {
    return (int) cur("width", true);
  }

  /**
   * Set the width of every matched element.
   */
  public GQuery width(int width) {
    for (Element e : elements) {
      e.getStyle().setPropertyPx("width", width);
    }
    return this;
  }

  /**
   * Set the width style property of every matched element. It's useful for using 'percent' or 'em'
   * units Example: $(".a").width("100%")
   */
  public GQuery width(String width) {
    return css("width", width);
  }

  /**
   * Wrap each matched element with the specified HTML content. This wrapping process is most useful
   * for injecting additional structure into a document, without ruining the original semantic
   * qualities of a document. This works by going through the first element provided (which is
   * generated, on the fly, from the provided HTML) and finds the deepest descendant element within
   * its structure -- it is that element that will enwrap everything else.
   */
  public GQuery wrap(Element elem) {
    return wrap($(elem));
  }

  /**
   * Wrap each matched element with the specified HTML content. This wrapping process is most useful
   * for injecting additional structure into a document, without ruining the original semantic
   * qualities of a document. This works by going through the first element provided (which is
   * generated, on the fly, from the provided HTML) and finds the deepest descendant element within
   * its structure -- it is that element that will enwrap everything else.
   */
  public GQuery wrap(GQuery query) {
    for (Element e : elements) {
      $(e).wrapAll(query);
    }
    return this;
  }

  /**
   * Wrap each matched element with the specified HTML content. This wrapping process is most useful
   * for injecting additional structure into a document, without ruining the original semantic
   * qualities of a document. This works by going through the first element provided (which is
   * generated, on the fly, from the provided HTML) and finds the deepest descendant element within
   * its structure -- it is that element that will enwrap everything else.
   */
  public GQuery wrap(String html) {
    return wrap($(html));
  }
  
  /**
   * Wrap each matched element with the specified SafeHtml content. This wrapping process is most useful
   * for injecting additional structure into a document, without ruining the original semantic
   * qualities of a document. This works by going through the first element provided (which is
   * generated, on the fly, from the provided SafeHtml) and finds the deepest descendant element within
   * its structure -- it is that element that will enwrap everything else.
   */
  public GQuery wrap(SafeHtml safeHtml) {
    return wrap($(safeHtml));
  }

  /**
   * Wrap all the elements in the matched set into a single wrapper element. This is different from
   * .wrap() where each element in the matched set would get wrapped with an element. This wrapping
   * process is most useful for injecting additional structure into a document, without ruining the
   * original semantic qualities of a document.
   *
   * This works by going through the first element provided (which is generated, on the fly, from
   * the provided HTML) and finds the deepest descendant element within its structure -- it is that
   * element that will enwrap everything else.
   */
  public GQuery wrapAll(Element elem) {
    return wrapAll($(elem));
  }

  /**
   * Wrap all the elements in the matched set into a single wrapper element. This is different from
   * .wrap() where each element in the matched set would get wrapped with an element. This wrapping
   * process is most useful for injecting additional structure into a document, without ruining the
   * original semantic qualities of a document.
   *
   * This works by going through the first element provided (which is generated, on the fly, from
   * the provided HTML) and finds the deepest descendant element within its structure -- it is that
   * element that will enwrap everything else.
   */
  public GQuery wrapAll(GQuery query) {
    if (!isEmpty()) {
      GQuery wrap = query.clone();
      if (get(0).getParentNode() != null) {
        wrap.insertBefore(get(0));
      }
      for (Element e : wrap.elements) {
        Node n = e;
        while (n.getFirstChild() != null && n.getFirstChild().getNodeType() == Node.ELEMENT_NODE) {
          n = n.getFirstChild();
        }
        $((Element) n).append(this);
      }
    }
    return this;
  }

  /**
   * Wrap all the elements in the matched set into a single wrapper element. This is different from
   * .wrap() where each element in the matched set would get wrapped with an element. This wrapping
   * process is most useful for injecting additional structure into a document, without ruining the
   * original semantic qualities of a document.
   *
   * This works by going through the first element provided (which is generated, on the fly, from
   * the provided HTML) and finds the deepest descendant element within its structure -- it is that
   * element that will enwrap everything else.
   */
  public GQuery wrapAll(String html) {
    return wrapAll($(html));
  }
  
  /**
   * Wrap all the elements in the matched set into a single wrapper element. This is different from
   * .wrap() where each element in the matched set would get wrapped with an element. This wrapping
   * process is most useful for injecting additional structure into a document, without ruining the
   * original semantic qualities of a document.
   *
   * This works by going through the first element provided (which is generated, on the fly, from
   * the provided SafeHtml) and finds the deepest descendant element within its structure -- it is that
   * element that will enwrap everything else.
   */
  public GQuery wrapAll(SafeHtml safeHtml) {
    return wrapAll(safeHtml.asString());
  }

  /**
   * Wrap the inner child contents of each matched element (including text nodes) with an HTML
   * structure. This wrapping process is most useful for injecting additional structure into a
   * document, without ruining the original semantic qualities of a document. This works by going
   * through the first element provided (which is generated, on the fly, from the provided HTML) and
   * finds the deepest ancestor element within its structure -- it is that element that will enwrap
   * everything else.
   */
  public GQuery wrapInner(Element elem) {
    return wrapInner($(elem));
  }

  /**
   * Wrap the inner child contents of each matched element (including text nodes) with an HTML
   * structure. This wrapping process is most useful for injecting additional structure into a
   * document, without ruining the original semantic qualities of a document. This works by going
   * through the first element provided (which is generated, on the fly, from the provided HTML) and
   * finds the deepest ancestor element within its structure -- it is that element that will enwrap
   * everything else.
   */
  public GQuery wrapInner(GQuery query) {
    for (Element e : elements) {
      $(e).contents().wrapAll(query);
    }
    return this;
  }

  /**
   * Wrap the inner child contents of each matched element (including text nodes) with an HTML
   * structure. This wrapping process is most useful for injecting additional structure into a
   * document, without ruining the original semantic qualities of a document. This works by going
   * through the first element provided (which is generated, on the fly, from the provided HTML) and
   * finds the deepest ancestor element within its structure -- it is that element that will enwrap
   * everything else.
   */
  public GQuery wrapInner(String html) {
    return wrapInner($(html));
  }
  
  /**
   * Wrap the inner child contents of each matched element (including text nodes) with an SafeHtml
   * structure. This wrapping process is most useful for injecting additional structure into a
   * document, without ruining the original semantic qualities of a document. This works by going
   * through the first element provided (which is generated, on the fly, from the provided SafeHtml) and
   * finds the deepest ancestor element within its structure -- it is that element that will enwrap
   * everything else.
   */
  public GQuery wrapInner(SafeHtml safeHtml) {
    return wrapInner(safeHtml.asString());
  }

  private String join(String chr, String... values) {
    String value = "";
    for (int i = 0; i < values.length; i++) {
      value += i > 0 ? chr + values[i] : values[i];
    }
    return value;
  }
}
