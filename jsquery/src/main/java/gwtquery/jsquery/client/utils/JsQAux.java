package gwtquery.jsquery.client.utils;

import java.util.ArrayList;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.js.JsCache;
import com.google.gwt.query.client.js.JsNodeArray;
import com.google.gwt.query.client.js.JsUtils;

public abstract class JsQAux {

  private native static String dumpObject(JavaScriptObject o) /*-{
		var s = "";
		for (k in o)
			s += " " + k;
		return s;
  }-*/;

  private static native void runJsFunction(JavaScriptObject f) /*-{
		f();
  }-*/;

  public static GQuery dollar(Object o) {
    if (o instanceof String) {
      return GQuery.$((String) o);
    } else if (o instanceof JavaScriptObject) {
      JavaScriptObject jso = (JavaScriptObject) o;
      if (JsUtils.isFunction(jso)) {
        runJsFunction(jso);
      } else {
        GQuery r = GQuery.$(jso);
        if (JsUtils.isArray(jso)) {
          JsCache c = jso.cast();
          JsNodeArray elms = JsNodeArray.create();
          for (int i = 0; i < c.length(); i++) {
            Object obj = c.get(i);
            if (obj instanceof Node) {
              elms.addNode((Node) obj);
            }
          }
          r = GQuery.$(elms);
        }
        return r;
      }
    }
    return GQuery.$();
  }

  public static GQuery dollar(String s, Element ctx) {
    return GQuery.$(s, ctx);
  }

  public static void ready(Function f) {
    f.f();
  }

  public static int inArray(Object o, Object a) {
    if (o instanceof JavaScriptObject) {
      JavaScriptObject jso = (JavaScriptObject) o;
      if (JsUtils.isElement(jso)) {
        return dollar(a).index((Element) o);
      } else if (JsUtils.isArray(jso)) {
        return ((JsCache) a).indexOf(o);
      }
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
		return $wnd.jsQuery && $wnd.jsQuery.fn ? $wnd.jsQuery.fn.prototype
				: null;
  }-*/;

  private static native JavaScriptObject extendImpl(boolean deep,
      JavaScriptObject ctx, Object s) /*-{
		var d = ctx ? ctx : $wnd.jsQuery.fn.prototype || {};
		for (k in s) {
			d[k] = s[k];
			if (!ctx)
				$wnd.$[k] = s[k];
		}
		return d;
  }-*/;
  
  public static Object[] each(Object[] objs, Function f) {
    ArrayList<Object> ret = new ArrayList<Object>();
    for (Object o : objs) {
      f.setDataObject(o);
      if (f.fe(null, o)) {
        ret.add(o);
      }
    }
    return ret.toArray(new Object[0]);
  }

  public static void log(Object l) {
    System.out.println(l);
  }

}
