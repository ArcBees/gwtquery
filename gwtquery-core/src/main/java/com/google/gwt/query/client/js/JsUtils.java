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

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
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

    private native void exec(JavaScriptObject f, Object data) /*-{
			f(data);
    }-*/;
    
    public void f() {
      if (jso != null) {
        exec(jso, getDataObject());
      }
    }

    public void execute() {
      f();
    }
  }

  public static class JsUtilsImpl {
    public native Properties parseJSON(String json) /*-{
			return $wnd.JSON.parse(json);
    }-*/;
    
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

  public static class JsUtilsImplIE6 extends JsUtilsImpl {
    public static final native Properties evalImpl(String properties) /*-{
			return eval(properties);
    }-*/;

    @Override
    public Properties parseJSON(String json) {
      // No checks to the passed string so json should be
      // a well-formed json string.
      return evalImpl("(" + json + ")");
    }
    
    @Override
    public String JSON2String(JavaScriptObject js) {
      // This is a very basic implementation for IE6/IE7 of JSON.stringify
      // If many people demand a better one we could consider to use json2.js
      // @see https://github.com/douglascrockford/JSON-js/blob/master/json2.js
      Properties prop = js.cast();
      String ret = "";
      for (String k : prop.keys()){
        String ky = k.matches("\\d+") ? k : "\"" + k + "\"";
        JsCache o = prop.getArray(k).cast();
        if (o != null) {
          ret += ky + ":[";
          for (int i = 0, l = o.length(); i < l ; i++) {
            Properties p = o.<JsCache>cast().getJavaScriptObject(i);
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
      return "{" + ret.replaceAll(",\\s*([\\]}]|$)","$1")
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
      if (a.length() > 0 && isXML(a.get(0))) {
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
   * Camelize style property names. for instance: font-name -> fontName
   */
  public static native String camelize(String s)/*-{
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
   * Use the method in the gquery class $(elem).cur(prop, force);
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
    return n == null ? null : n.getNodeType() == Node.DOCUMENT_NODE
        ? n.<Document> cast() : n.getOwnerDocument();
  }

  /**
   * Check if an object have already a property with name <code>name</code>
   * defined.
   */
  public static native boolean hasProperty(JavaScriptObject o, String name)/*-{
		return name in o;
  }-*/;

  /**
   * Hyphenize style property names. for instance: fontName -> font-name
   */
  public static native String hyphenize(String name) /*-{
		return name.replace(/([A-Z])/g, "-$1").toLowerCase();
  }-*/;

  /**
   * Check is a javascript object can be used as an array
   */
  public static native boolean isArray(JavaScriptObject o) /*-{
		return Object.prototype.toString.call(o) == '[object Array]'
				|| typeof o.length == 'number';
  }-*/;

  /**
   * Return whether the event was prevented
   */
  public static native boolean isDefaultPrevented(JavaScriptObject e)/*-{
		return e.defaultPrevented || e.returnValue === false || e.getPreventDefault
				&& e.getPreventDefault() ? true : false;
  }-*/;
  
  /**
   * Return whether a node is detached to the dom
   * Be careful : This method works only on node that should be inserted within the body node. 
   */
  public static boolean isDetached(Node n) {
	assert n != null;
	
	if ("html".equalsIgnoreCase(n.getNodeName())){
		return true;
	}
	
    return !getOwnerDocument(n).getBody().isOrHasChild(n);
  }

  /**
   * Check is a javascript object can be cast to an Element
   */
  public static boolean isElement(JavaScriptObject o) {
    return hasProperty(o, "nodeName");
  }

  /**
   * Check is a javascript object can be cast to an Event
   */
  public static boolean isEvent(JavaScriptObject o) {
    return hasProperty(o, "currentTarget");
  }

  /**
   * Check is a javascript object is a function
   */
  public static native boolean isFunction(JavaScriptObject o) /*-{
		return !!o && typeof o == 'function'
  }-*/;

  /**
   * Check is a javascript can be cast to a node list
   */
  public static native boolean isNodeList(JavaScriptObject o) /*-{
		var r = Object.prototype.toString.call(o);
		return r == '[object HTMLCollection]' || r == '[object NodeList]'
				|| (typeof o == 'object' && o.length && o[0].tagName) ? true : false;
  }-*/;

  /**
   * Check is a javascript object is a Window
   */
  public static boolean isWindow(JavaScriptObject o) {
    return hasProperty(o, "alert");
  }

  /**
   * Check if an element is a DOM or a XML node
   */
  public static boolean isXML(Node o) {
    return o == null
        ? false
        : !"HTML".equals(getOwnerDocument(o).getDocumentElement().getNodeName());
  }
  
  /**
   * Load an external javascript library. The inserted script replaces the
   * element with the given id in the document.
   */
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
      System.err.println("Error while parsing json: " + e.getMessage() + ".\n"
          + json);
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
   * Remove duplicates from an elements array
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
   * TODO: jquery implementation accepts a second parameter (traditional) 
   */
  public static String param(JavaScriptObject js) {
    Properties prop = js.cast();
    String ret = "";
    for (String k : prop.keys()) {
      ret += ret.isEmpty() ? "" : "&";
      JsCache o = prop.getArray(k).cast();
      if (o != null) {
        for (int i = 0, l = o.length(); i < l ; i++) {
          ret += i > 0 ? "&" : "";
          Properties p = o.<JsCache>cast().getJavaScriptObject(i);
          if (p != null) {
            ret += k + "[]=" + p.toJsonString();
          } else {
            ret += k + "[]=" + o.getString(i) ;
          }
        }
      } else {
        Properties p = prop.getJavaScriptObject(k);
        if (p != null) {
          ret += p.toQueryString();
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
