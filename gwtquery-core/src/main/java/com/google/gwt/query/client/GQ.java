package com.google.gwt.query.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.JsArrayMixed;
import com.google.gwt.dom.client.Element;
import com.google.gwt.query.client.plugins.ajax.Ajax;
import com.google.gwt.query.client.plugins.ajax.Ajax.Settings;

/**
 * Class to implement the GQuery API static methods.
 * 
 * NOTE: This class should be named $ but gwtc does not support the Dollar char
 * in class names. If gwtc fixed this someday, this class would be renamed to $
 * but we would maintain a copy of this in order not to break apps.
 * 
 * This class is abstract because is thought to contain only static methods and
 * it extends GQuery so as static methods and attributes defined in GQuery are
 * available here when statically importing this class.
 * 
 * Code example:
 *  <pre>
  GQ.$("div").css("background", "red");
 * </pre>
 * <pre>
  import static com.google.gwt.query.client.GQ.*;
  ...
  $("div").css("background", "red");
 * </pre>
 * 
 */
public abstract class GQ extends GQuery {

  /**
   * Non instantiable class.
   * 
   * TODO: move all public static GQuery methods here.
   */
  private GQ(GQuery gq) {
    super(gq);
  }

  public static void ajax(Properties p) {
    ajax(p);
  }

  public static void ajax(String url, Settings settings) {
    Ajax.ajax(url, settings);
  }
  
  public GQuery load(String url, Properties data, final Function onSuccess) {
    return as(Ajax.Ajax).load(url, data, onSuccess);
  }

  public GQuery load(String url) {
    return as(Ajax.Ajax).load(url, null, null);
  }
  
  public static void get(String url, Properties data, final Function onSuccess) {
    Ajax.get(url, data, onSuccess);
  }
  
  public static void post(String url, Properties data, final Function onSuccess) {
    Ajax.post(url, data, onSuccess);
  }
  
  public static void getJSON(String url, Properties data, final Function onSuccess) {
    Ajax.getJSON(url, data, onSuccess);
  }

  public static boolean contains(Element a, Element b) {
    return engine.contains(a, b);
  }

  public static Object data(Element e, String key) {
    return GQuery.data(e, key, null);
  }

  public static Object data(Element e, String key, String value) {
    return GQuery.data(e, key, value);
  }

  public static <T> void each(List<T> objects, Function f) {
    for (int i = 0, l = objects.size(); i < l; i++) {
      f.f(i, objects.get(i));
    }
  }

  public static <T> void each(T[] objects, Function f) {
    for (int i = 0, l = objects.length; i < l; i++) {
      f.f(i, objects[i]);
    }
  }

  public static void each(JsArrayMixed objects, Function f) {
    for (int i = 0, l = objects.length(); i < l; i++) {
      f.f(i, objects.getObject(i));
    }
  }

  @SuppressWarnings("unchecked")
  public static <T> T[] grep(T[] objects, Predicate f) {
    ArrayList<Object> ret = new ArrayList<Object>();
    for (int i = 0, l = objects.length; i < l; i++) {
      if (f.f(objects[i], i)) {
        ret.add(objects[i]);
      }
    }
    return (T[]) ret.toArray(new Object[0]);
  }
}
