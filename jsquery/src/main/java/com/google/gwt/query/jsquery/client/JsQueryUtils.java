package com.google.gwt.query.jsquery.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.js.JsCache;
import com.google.gwt.query.client.js.JsNodeArray;
import com.google.gwt.query.client.js.JsUtils;

/**
 * These are a set of utility methods needed in jsquery because
 * either they are not in the GQuery core yet, or they are already
 * there but we need to modify their behavior.
 * Most of them should be moved to the GQuery core api.
 *
 */
public abstract class JsQueryUtils {

  private native static String dumpObject(JavaScriptObject o) /*-{
		var s = "";
		for (k in o)
			s += " " + k;
		return s;
  }-*/;

  public static GQuery dollar(String s, Element ctx) {
    return GQuery.$(s, ctx);
  }

  public static void ready(Function f) {
    f.f();
  }

  public static int inArray(Object object, Object array) {
    if (array instanceof List) {
       return ((List<?>)array).indexOf(object);
    } else if (object instanceof JavaScriptObject
        && JsUtils.isElement((JavaScriptObject) object)) {
      return GQuery.$(array).index((Element) object);
    } else if (array instanceof JavaScriptObject
        && JsUtils.isArray((JavaScriptObject) array)) {
      return ((JsCache) array).indexOf(object);
    }
    return -1;
  }

  public static JavaScriptObject extend(Object... objs) {
    int i = 0, l = objs.length;
    boolean deep = false;
    JavaScriptObject ctx = null;
    Object target = objs[i];
    if (target instanceof Boolean) {
      deep = (Boolean) target;
      if (l == 1)
        return ctx;
      target = objs[i++];
    }
    if (l - i == 1) {
      i--;
    } else {
      ctx = (JavaScriptObject) target;
    }

    for (++i; i < l; i++) {
      if (objs[i] != null) {
        ctx = extendImpl(deep, ctx, objs[i]);
      }
    }
    return ctx;
  }

  private static native JavaScriptObject getDefaultPrototype() /*-{
		return $wnd.JsQuery && $wnd.JsQuery.fn
		    ? $wnd.JsQuery.fn.prototype
				: null;
  }-*/;

  private static native JavaScriptObject extendImpl(boolean deep,
      JavaScriptObject ctx, Object s) /*-{
		var d = ctx ? ctx : $wnd.JsQuery.fn.prototype || {};
		for (k in s) {
			d[k] = s[k];
			if (!ctx)
				$wnd.$[k] = s[k];
		}
		return d;
  }-*/;

  public static JavaScriptObject[] each(JavaScriptObject[] objs, Function f) {
    ArrayList<Object> ret = new ArrayList<Object>();
    for (Object o : objs) {
      f.setDataObject(o);
      if (f.fe(null, o)) {
        ret.add(o);
      }
    }
    return ret.toArray(new JavaScriptObject[0]);
  }

  public static void log(Object l) {
    System.out.println(l);
  }

}
