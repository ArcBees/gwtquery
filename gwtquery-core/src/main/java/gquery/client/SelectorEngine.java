package gquery.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;

import gquery.client.impl.SelectorEngineImpl;

/**
 *
 */
public class SelectorEngine {
    private SelectorEngineImpl impl;


    public SelectorEngine() {
      impl = (SelectorEngineImpl) GWT
                .create(SelectorEngineImpl.class);  
    }

    public static native boolean eq(String s1, String s2) /*-{
       return s1 == s2;
    }-*/;

    public static native NodeList<Element> getElementsByClassName(String clazz,
                                                                  Node ctx) /*-{
        return ctx.getElementsByClassName(clazz);
    }-*/;

    public static native String or(String s1, String s2) /*-{
       return s1 || s2;
    }-*/;

    public static native NodeList<Element> querySelectorAll(String selector) /*-{
      return $doc.querySelectorAll(selector);
  }-*/;

    public static native NodeList<Element> querySelectorAll(String selector,
                                                            Node ctx) /*-{
      return ctx.querySelectorAll(selector);
  }-*/;

    public NodeList<Element> select(String selector, Node ctx) {
        return impl.select(selector, ctx);
    }
    public static boolean truth(String a) {
        return GWT.isScript() ? truth0(a) : a != null && !"".equals(a);
    }

    public static boolean truth(JavaScriptObject a) {
        return GWT.isScript() ? truth0(a) : a != null;
    }

    public static NodeList<Element> xpathEvaluate(String selector, Node ctx) {
        return xpathEvaluate(selector, ctx, JSArray.create());
    }

    public static native NodeList<Element> xpathEvaluate(String selector,
                                                         Node ctx, JSArray r) /*-{
      var node;
      var result = $doc.evaluate(selector, ctx, null, 0, null);
      while ((node = result.iterateNext())) {
          r.push(node);
      }
      return r;
  }-*/;

    private static native boolean truth0(String a) /*-{
       return a;
    }-*/;

    private static native boolean truth0(JavaScriptObject a) /*-{
         return a;
      }-*/;

    protected JSArray veryQuickId(Node context, String id) {
        JSArray r = JSArray.create();
        if (context.getNodeType() == Node.DOCUMENT_NODE) {
            r.addNode(((Document) context).getElementById(id));
            return r;
        } else {
            r.addNode(context.getOwnerDocument().getElementById(id));
            return r;
        }
    }

    public static native Node getNextSibling(Node n) /*-{
       return n.nextSibling || null; 
    }-*/;

    public static native Node getPreviousSibling(Node n) /*-{
       return n.previousSibling || null; 
    }-*/;
}
