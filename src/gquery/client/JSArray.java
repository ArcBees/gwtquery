package gquery.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;

/**
 */
public class JSArray extends NodeList<Element> {

  public static JSArray create() {
    return (JSArray) JavaScriptObject.createArray();
  }

  public static native JSArray create(Node node) /*-{
    return [node];
  }-*/;

  public static native JSArray create(NodeList nl) /*-{
    var r = [], len=nl.length;
    for(var i=0; i<len; i++) {
      r.push(nl[i]);
    }
    return r;
  }-*/;

  protected JSArray() {
  }

  public final native void addInt(int i) /*-{
       this[this.length]=i;
  }-*/;

  public final native void addNode(Node n) /*-{
     this[this.length]=n;
  }-*/;

  public final native void addNode(Node ci, int i) /*-{
       this[i]=ci;
    }-*/;

  public final native void concat(JSArray ary) /*-{
     this.concat(ary);
  }-*/;

  public final native Element getElement(int i) /*-{
        return this[i];
    }-*/;

  public final native int getInt(int i) /*-{
    return this[i]  || 0;
  }-*/;

  public final native Node getNode(int i) /*-{
        return this[i];
  }-*/;

  public final native String getStr(int i) /*-{
     return this[i] || null;
  }-*/;

  public final void pushAll(JSArray prevElem) {
    for (int i = 0, ilen = prevElem.size(); i < ilen; i++) {
      addNode(prevElem.getNode(i));
    }
  }

  public final native int size() /*-{
     return this.length;
  }-*/;
}
