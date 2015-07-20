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
package com.google.gwt.query.client.js;

import static com.google.gwt.query.client.GQuery.browser;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayMixed;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.Properties;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;

/**
 * A bunch of utility methods for GQuery.
 */
public class JsUtils {

  /**
   * A Function which wraps a javascript function.
   */
  public static class JsFunction extends Function implements Command {
    private JavaScriptObject jso = null;

    public JsFunction(JavaScriptObject f) {
      if (JsUtils.isFunction(f)) {
        jso = f;
      }
    }

    public boolean equals(Object obj) {
      return jso.equals(obj);
    }

    public int hashCode() {
      return jso.hashCode();
    }

    private native Object exec(JavaScriptObject f, Object data) /*-{
      return @com.google.gwt.query.client.js.JsCache::gwtBox(*)([ f(data) ]);
    }-*/;

    public void f() {
      if (jso != null) {
        setArguments(exec(jso, arguments(0)));
      }
    }

    public void execute() {
      f();
    }
  }

  /**
   * Wraps a GQuery function into a native javascript one so as we can
   * export Java methods without using JSNI.
   */
  public static native JavaScriptObject wrapFunction(Function f) /*-{
    return function(r) {
      var o = @java.util.ArrayList::new()();
      for (i in arguments) {
        r = @com.google.gwt.query.client.js.JsCache::gwtBox(*)([arguments[i]]);
        o.@java.util.ArrayList::add(Ljava/lang/Object;)(r);
      }
      o = o.@java.util.ArrayList::toArray()();
      f.@com.google.gwt.query.client.Function::setArguments([Ljava/lang/Object;)(o);
      return f.@com.google.gwt.query.client.Function::fe([Ljava/lang/Object;)(o);
    }
  }-*/;

  /**
   * Default JsUtils implementation.
   */
  public static class JsUtilsImpl {
    public Properties parseJSON(String json) {
      return JsonUtils.safeEval(json);
    }

    public native String JSON2String(JavaScriptObject o) /*-{
      return $wnd.JSON.stringify(o);
    }-*/;

    public native Element parseXML(String xml) /*-{
      return new DOMParser().parseFromString(xml, "text/xml").documentElement;
    }-*/;

    public String text(Element e) {
      return e.getInnerText();
    }

    public JsArray<Element> unique(JsArray<Element> a) {
      JsArray<Element> ret = JavaScriptObject.createArray().cast();
      JsCache cache = JsCache.create();
      for (int i = 0; i < a.length(); i++) {
        Element e = a.get(i);
        int id = e.hashCode();
        if (!cache.exists(id)) {
          cache.putNumber(id, 1);
          ret.push(e);
        }
      }
      return ret;
    }

    public native String XML2String(JavaScriptObject o) /*-{
      return (new XMLSerializer()).serializeToString(o);
    }-*/;
  }

  /**
   * IE JsUtils implemetation.
   */
  public static class JsUtilsImplIE6 extends JsUtilsImpl {
    @Override
    public Properties parseJSON(String json) {
      return JsonUtils.unsafeEval(json);
    }

    @Override
    public String JSON2String(JavaScriptObject js) {
      // This is a very basic implementation for IE6/IE7 of JSON.stringify
      // If many people demand a better one we could consider to use json2.js
      // @see https://github.com/douglascrockford/JSON-js/blob/master/json2.js
      Properties prop = js.cast();
      String ret = "";
      for (String k : prop.keys()) {
        String ky = k.matches("\\d+") ? k : "\"" + k + "\"";
        JsCache o = prop.getArray(k).cast();
        if (o != null) {
          ret += ky + ":[";
          for (int i = 0, l = o.length(); i < l; i++) {
            Properties p = o.<JsCache> cast().getJavaScriptObject(i);
            if (p != null) {
              ret += p.toJsonString() + ",";
            } else {
              ret += "\"" + o.getString(i) + "\",";
            }
          }
          ret += "],";
        } else {
          Properties p = prop.getJavaScriptObject(k);
          if (p != null) {
            ret += ky + ":" + p.toJsonString() + ",";
          } else {
            ret += ky + ":\"" + prop.getStr(k) + "\",";
          }
        }
      }
      return "{" + ret.replaceAll(",\\s*([\\]}]|$)", "$1")
          .replaceAll("([:,\\[])\"(-?[\\d\\.]+|null|false|true)\"", "$1$2")
          + "}";
    }

    @Override
    public native Element parseXML(String xml) /*-{
      var d = new ActiveXObject("Microsoft.XmlDom");
      d.loadXML(xml);
      return d.documentElement;
    }-*/;

    @Override
    public String text(Element e) {
      return isXML(e) ? xmlText(e) : super.text(e);
    }

    @Override
    public JsArray<Element> unique(JsArray<Element> a) {
      // in IE6 XML elements does not support adding hashId to the object
      if (browser.ie6 && isXML(a.get(0))) {
        return a;
      }
      return super.unique(a);
    }

    @Override
    public native String XML2String(JavaScriptObject o) /*-{
      return o.xml;
    }-*/;

    private native String xmlText(Element e) /*-{
      return e.text;
    }-*/;
  }

  private static JsUtilsImpl utilsImpl = GWT.create(JsUtilsImpl.class);

  /**
   * Returns a property present in a javascript object.
   */
  public static <T> T prop(JavaScriptObject o, Object id, Class<? extends T> type) {
    return o == null ? null : o.<JsCache> cast().get(id, type);
  }

  /**
   * Returns a property present in a javascript object.
   */
  public static <T> T prop(JavaScriptObject o, Object id) {
    return o == null ? null : o.<JsCache> cast().<T> get(id);
  }

  /**
   * Set a property to a javascript object.
   */
  public static void prop(JavaScriptObject o, Object id, Object val) {
    if (o != null) {
      o.<JsCache> cast().put(id, val);
    }
  }

  /**
   * Execute a native javascript function.
   */
  public static <T> T exec(JavaScriptObject jsFunction, Object... args) {
    assert isFunction(jsFunction);
    return jsni(jsFunction, "call", jsFunction, args);
  }

  /**
   * Assign a function to a property of the window object.
   */
  public static void export(String name, Function f) {
    export(GQuery.window, name, f);
  }

  /**
   * Export a function as a property of a javascript object.
   */
  public static void export(JavaScriptObject o, String name, Function f) {
    prop(o, name, (Object)(f != null ? wrapFunction(f) : null));
  }

  /**
   * Camelize style property names.
   * for instance: font-name -> fontName
   */
  public static native String camelize(String s) /*-{
    return s.replace(/\-(\w)/g, function(all, letter) {
      return letter.toUpperCase();
    });
  }-*/;

  /**
   * Merge the oldNodes list into the newNodes one. If oldNodes is null, a new
   * list will be created and returned. If oldNodes is not null, a new list will
   * be created depending on the create flag.
   */
  public static NodeList<Element> copyNodeList(NodeList<Element> oldNodes,
      NodeList<Element> newNodes, boolean create) {
    NodeList<Element> ret = oldNodes == null || create ? JsNodeArray.create()
        : oldNodes;
    JsCache idlist = JsCache.create();
    for (int i = 0; oldNodes != null && i < oldNodes.getLength(); i++) {
      Element e = oldNodes.getItem(i);
      idlist.put(e.hashCode(), 1);
      if (create) {
        ret.<JsNodeArray> cast().addNode(e, i);
      }
    }
    for (int i = 0, l = newNodes.getLength(), j = ret.getLength(); i < l; i++) {
      Element e = newNodes.getItem(i);
      if (!idlist.exists(e.hashCode())) {
        ret.<JsNodeArray> cast().addNode(newNodes.getItem(i), j++);
      }
    }
    return ret;
  }

  /**
   * Use the method in the gquery class.
   *  $(elem).cur(prop, force);
   */
  @Deprecated
  public static double cur(Element elem, String prop, boolean force) {
    return GQuery.$(elem).cur(prop, force);
  }

  /**
   * Compare two numbers using javascript equality.
   */
  public static native boolean eq(double s1, double s2) /*-{
    return s1 == s2;
  }-*/;

  /**
   * Compare two objects using javascript equality.
   */
  public static native boolean eq(Object s1, Object s2) /*-{
    return s1 == s2;
  }-*/;

  /**
   * Returns the owner document element of an element.
   */
  public static Document getOwnerDocument(Node n) {
    return n == null || !isElement(n) ? null : n.getNodeType() == Node.DOCUMENT_NODE
        ? n.<Document> cast() : n.getOwnerDocument();
  }

  /**
   * Check if an object has a property with <code>name</code> defined.
   * It supports dots in the name meaning checking nested properties.
   *
   * Example:
   * <pre>
   *  // Check whether a browser supports touch events
   *  hasProperty(window, "ontouchstart");
   * </pre>
   */
  public static native boolean hasProperty(JavaScriptObject o, String name)/*-{
    var p = name.split('.');
    for (var i in p) {
      if (!(o && p[i] in o)) return false;
      o = o[p[i]];
    }
    return true;
  }-*/;

  /**
   * Check whether an element has an attribute, this is here since GWT Element.getAttribute
   * implementation returns an empty string instead of null when the attribute is not
   * present.
   */
  public static native boolean hasAttribute(Element o, String name) /*-{
    return !!(o && o.getAttribute(name) !== null);
  }-*/;

  /**
   * Hyphenize style property names.
   *  for instance: fontName -> font-name
   */
  public static native String hyphenize(String name) /*-{
    return name.replace(/([A-Z])/g, "-$1").toLowerCase();
  }-*/;

  /**
   * Check is a javascript object can be used as an array.
   */
  public static native boolean isArray(JavaScriptObject o) /*-{
    return Object.prototype.toString.call(o) == '[object Array]'
        || typeof o.length == 'number';
  }-*/;

  /**
   * Check is a javascript object is a FormData.
   */
  public static native boolean isFormData(JavaScriptObject o) /*-{
    return Object.prototype.toString.call(o) == '[object FormData]';
  }-*/;

  /**
   * Return whether the event was prevented.
   */
  public static native boolean isDefaultPrevented(JavaScriptObject e) /*-{
    return e.defaultPrevented || e.returnValue === false || e.getPreventDefault
        && e.getPreventDefault() ? true : false;
  }-*/;

  /**
   * Return whether a node is detached to the DOM.
   *
   * Be careful : This method works only on node that should be inserted within the body node.
   */
  public static boolean isDetached(Node n) {
    assert n != null;

    if ("html".equalsIgnoreCase(n.getNodeName())) {
      return false;
    }

    return !getOwnerDocument(n).getBody().isOrHasChild(n);
  }

  /**
   * Check is a javascript object can be cast to an Element.
   */
  public static native boolean isElement(Object o) /*-{
    return !!o && 'nodeType' in o && 'nodeName' in o;
  }-*/;

  /**
   * Check is a javascript object can be cast to an Event.
   */
  public static boolean isEvent(JavaScriptObject o) {
    return hasProperty(o, "currentTarget");
  }

  /**
   * Check is a javascript object is a function.
   */
  public static native boolean isFunction(JavaScriptObject o) /*-{
    return Object.prototype.toString.call(o) == '[object Function]';
  }-*/;

  /**
   * Check is a javascript can be cast to a node list.
   */
  public static native boolean isNodeList(JavaScriptObject o) /*-{
    var r = Object.prototype.toString.call(o);
    return r == '[object HTMLCollection]' || r == '[object NodeList]'
        || (typeof o == 'object' && o.length && o[0] && o[0].tagName) ? true : false;
  }-*/;

  /**
   * Check is a javascript object is a Window.
   */
  public static boolean isWindow(JavaScriptObject o) {
    return hasProperty(o, "alert");
  }

  /**
   * Check if an element is a DOM or a XML node.
   */
  public static boolean isXML(Node o) {
    return o == null ? false
        : !"HTML".equals(getOwnerDocument(o).getDocumentElement().getNodeName());
  }

  /**
   * Load an external javascript library. The inserted script replaces the
   * element with the given id in the document.
   *
   * @deprecated use {@link com.google.gwt.query.client.plugins.ajax.Ajax#loadScript(String)}
   */
  @Deprecated
  public static void loadScript(String url, String id) {
    GQuery gs = GQuery.$(DOM.createElement("script"));
    GQuery gp = GQuery.$("#" + id).parent();
    if (gp.size() != 1) {
      gp = GQuery.$(GQuery.document.getBody());
    }
    GQuery.$("#" + id).remove();
    gp.append(gs.attr("src", url).attr("type", "text/javascript").attr("id", id));
  }

  /**
   * Return the element which is truth in the double scope.
   */
  public static native double or(double s1, double s2) /*-{
    return s1 || s2;
  }-*/;

  /**
   * Return the element which is truth in the javascript scope.
   */
  public static native <T> T or(T s1, T s2) /*-{
    return s1 || s2;
  }-*/;

  /**
   * Parses a json string returning a Object with useful method to get the
   * content.
   */
  public static Properties parseJSON(String json) {
    try {
      return utilsImpl.parseJSON(json);
    } catch (Exception e) {
      if (!GWT.isProdMode()) {
        System.err.println("Error while parsing json: " + e.getMessage() + ".\n" + json);
      }
      return Properties.create();
    }
  }

  /**
   * Parses a xml string and return the xml document element which can then be
   * passed to GQuery to create a typical GQuery object that can be traversed
   * and manipulated.
   */
  public static Element parseXML(String xml) {
    return utilsImpl.parseXML(xml);
  }

  public static String text(Element e) {
    return utilsImpl.text(e);
  }

  /**
   * Utility method to cast objects in production.
   * Useful for casting native implementations to interfaces like JsInterop
   */
  public static native <T> T cast(Object o) /*-{
    return o;
  }-*/;

  /**
   * Utility method to cast objects to array of string in production.
   */
  public static native String[] castArrayString(Object a) /*-{
    return a
  }-*/;

  /**
   * Call any arbitrary function present in a Javascript object.
   * It checks the existence of the function and object hierarchy before executing it.
   * It's very useful in order to avoid writing jsni blocks for very simple snippets.
   *
   * Note that GWT 3.0 jsinterop will come with a method similar, so we might deprecate
   * this in the future.
   *
   * Example
   * <pre>
   *  // Create a svg node in our document.
   *  Element svg = jsni(document, "createElementNS", "http://www.w3.org/2000/svg", "svg");
   *  // Append it to the dom
   *  $(svg).appendTo(document);
   *  // show the svg element in the debug console
   *  jsni("console.log", svg);
   * </pre>
   *
   * @param jso the object containing the method to execute
   * @param meth the literal name of the function to call, dot separators are allowed.
   * @param args an array with the arguments to pass to the function.
   * @return the java ready boxed object returned by the jsni method or null, if the
   * call return a number we will get a Double, if it returns a boolean we get a java
   * Boolean, strings comes as java String, otherwise we get the javascript object.
   */
  public static <T> T jsni(JavaScriptObject jso, String meth, Object... args) {
    return runJavascriptFunction(jso, meth, args);
  }

  /**
   * Run any arbitrary function in javascript scope using the window as the base object.
   * It checks the existence of the function and object hierarchy before executing it.
   * It's very useful in order to avoid writing jsni blocks for very simple snippets.
   *
   * Note that GWT 3.0 jsinterop will come with a method similar, so we might deprecate
   * this in the future.
   *
   * Example
   * <pre>
   *  // Create a svg node in our document.
   *  Element svg = jsni("document.createElementNS", "http://www.w3.org/2000/svg", "svg");
   *  // Append it to the dom
   *  $(svg).appendTo(document);
   *  // show the svg element in the debug console
   *  jsni("console.log", svg);
   * </pre>
   *
   * @param meth the literal name of the function to call, dot separators are allowed.
   * @param args an array with the arguments to pass to the function.
   * @return the java ready boxed object returned by the jsni method or null, if the
   * call return a number we will get a Double, if it returns a boolean we get a java
   * Boolean, strings comes as java String, otherwise we get the javascript object.
   */
  public static <T> T jsni(String meth, Object... args) {
    return runJavascriptFunction(null, meth, args);
  }

  /**
   * Call via jsni any arbitrary function present in a Javascript object.
   *
   * It's thought for avoiding to create jsni methods to call external functions and
   * facilitate the writing of js wrappers.
   *
   * Example
   * <pre>
   *  // Create a svg node in our document.
   *  Element svg = runJavascriptFunction(document, "createElementNS", "http://www.w3.org/2000/svg", "svg");
   *  // Append it to the dom
   *  $(svg).appendTo(document);
   * </pre>
   *
   * @param o  the javascript object where the function is, it it is null we use window.
   * @param meth the literal name of the function to call, dot separators are allowed.
   * @param args an array with the arguments to pass to the function.
   * @return the java ready boxed object returned by the jsni method or null, if the
   * call return a number we will get a Double, if it returns a boolean we get a java
   * Boolean, strings comes as java String, otherwise we get the javascript object.
   *
   * @deprecated use jsni instead.
   */
  public static <T> T runJavascriptFunction(JavaScriptObject o, String meth, Object... args) {
    return runJavascriptFunctionImpl(o, meth, JsObjectArray.create().add(args).<JsArrayMixed>cast());
  }

  private static native <T> T runJavascriptFunctionImpl(JavaScriptObject o, String meth, JsArrayMixed args) /*-{
    var f = o || $wnd, p = meth.split('.');
    for (var i in p) {
      o = f;
      f = f[p[i]];
      if (!f) return null;
    }
    return @com.google.gwt.query.client.js.JsUtils::isFunction(*)(f)
        && @com.google.gwt.query.client.js.JsCache::gwtBox(*)([f.apply(o, args)]);
  }-*/;

  /**
   * Check if a number is true in the javascript scope.
   */
  public static native boolean truth(double a) /*-{
    return a ? true : false;
  }-*/;

  /**
   * Check if an object is true in the javascript scope.
   */
  public static native boolean truth(Object a) /*-{
    return a ? true : false;
  }-*/;

  /**
   * Remove duplicates from an elements array.
   */
  public static JsArray<Element> unique(JsArray<Element> a) {
    return utilsImpl.unique(a);
  }

  public static String XML2String(JavaScriptObject js) {
    return utilsImpl.XML2String(js);
  }

  public static String JSON2String(JavaScriptObject js) {
    return utilsImpl.JSON2String(js);
  }

  /**
   * Returns a QueryString representation of a JavascriptObject.
   *
   * TODO: jquery implementation accepts a second parameter (traditional)
   */
  public static String param(JavaScriptObject js) {
    Properties prop = js.cast();
    String ret = "";
    for (String k : prop.keys()) {
      ret += ret.isEmpty() ? "" : "&";
      JsCache o = prop.getArray(k).cast();
      if (o != null) {
        for (int i = 0, l = o.length(); i < l; i++) {
          ret += i > 0 ? "&" : "";
          Properties p = o.<JsCache> cast().getJavaScriptObject(i);
          if (p != null) {
            ret += k + "[]=" + p.toJsonString();
          } else {
            ret += k + "[]=" + o.getString(i);
          }
        }
      } else {
        Properties p = prop.getJavaScriptObject(k);
        if (p != null) {
          ret += k + "=" + p.tostring();
        } else {
          String v = prop.getStr(k);
          if (v != null && !v.isEmpty() && !"null".equalsIgnoreCase(v)) {
            ret += k + "=" + v;
          }
        }
      }
    }
    return ret;
  }
}
