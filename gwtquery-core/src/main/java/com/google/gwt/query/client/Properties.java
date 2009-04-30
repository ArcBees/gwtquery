package com.google.gwt.query.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;

/**
 * JSO for accessing Javascript objective literals used by GwtQuery functions.
 */
public class Properties extends JavaScriptObject {

  protected Properties() {
  }

  public static Properties create(String properties) {
    String s = properties.replaceFirst("^[({]*(.*)[})]*$", "({$1})");
    return (Properties) createImpl(s);
  }

  public final native static JavaScriptObject createImpl(String properties) /*-{
      return eval(properties);
    }-*/;

  public final native String get(String name) /*-{
    return this[name];
  }-*/;

  public final native int getInt(String name) /*-{
    return this[name];
  }-*/;

  public final native float getFloat(String name) /*-{
    return this[name];
  }-*/;

  public final String[] keys() {
    JsArrayString a = keysImpl();
    String[] ret = new String[a.length()];
    for (int i = 0; i < a.length(); i++) {
      ret[i] = "" + a.get(i);
    }
    return ret;
  }

  public final native JsArrayString keysImpl() /*-{
    var key, keys=[];
    for(key in this) {
      keys.push("" + key); 
    }
    return keys;
  }-*/;
}
