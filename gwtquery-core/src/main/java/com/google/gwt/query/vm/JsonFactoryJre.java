package com.google.gwt.query.vm;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gwt.query.client.builders.JsonBuilder;
import com.google.gwt.query.client.builders.JsonFactory;
import com.google.gwt.query.client.builders.Name;
import com.google.gwt.query.rebind.JsonBuilderGenerator;

public class JsonFactoryJre implements JsonFactory  {

  static JsonFactoryJre jsonFactory = new JsonFactoryJre();

  public static class JsonBuilderHandler implements InvocationHandler {
    private JSONObject jsonObject;

    public JsonBuilderHandler() {
      jsonObject = new JSONObject();
    }

    public JsonBuilderHandler(JSONObject j) {
       jsonObject = j;
    }

    public JsonBuilderHandler(String payload) throws Throwable {
      jsonObject = new JSONObject(payload);
    }

    @SuppressWarnings("unchecked")
    private <T> Object jsonArrayToList(JSONArray j, Class<T> ctype, boolean isArray) throws Throwable {
      List<T> l = new ArrayList<T>();
      for (int i = 0; j != null && i < j.length() ; i++) {
        l.add((T)getValue(j, i, null, null, ctype, null));
      }
      return l.isEmpty() ? null : isArray ? l.toArray((T[])Array.newInstance(ctype, l.size())) : l;
    }

    private Object getValue(JSONArray arr, int idx, JSONObject obj, String attr, Class<?> clz, Method method) {
      Object ret = null;
      try {
        if (clz.isArray() || clz.equals(List.class)) {
          Class<?> ctype = Object.class;
          if (clz.isArray()) {
            ctype = clz.getComponentType();
          } else {
            Type returnType = method.getGenericReturnType();
            if (returnType instanceof ParameterizedType) {
              ctype = (Class<?>)((ParameterizedType) returnType).getActualTypeArguments()[0];
            }
          }
          ret = jsonArrayToList(obj.getJSONArray(attr), ctype, clz.isArray());
        } else if (clz.equals(Date.class)) {
          ret = new Date(obj != null ? obj.getLong(attr): arr.getLong(idx));
        } else if (clz.equals(String.class)) {
          ret = obj != null ? obj.getString(attr) : arr.getString(idx);
        } else if (clz.equals(Boolean.class) || clz.isPrimitive() && clz == Boolean.TYPE) {
          try {
            ret = obj != null ? obj.getBoolean(attr): arr.getBoolean(idx);
          } catch (Exception e) {
            return Boolean.FALSE;
          }
        } else if (clz.equals(Byte.class) || clz.equals(Short.class) || clz.equals(Integer.class)
            || clz.isPrimitive() && (clz == Byte.TYPE || clz == Short.TYPE || clz == Integer.TYPE)) {
          try {
            ret = obj != null ? obj.getInt(attr): arr.getInt(idx);
          } catch (Exception e) {
            return 0;
          }
        } else if (clz.equals(Double.class) || clz.equals(Float.class)
            || clz.isPrimitive() && (clz == Double.TYPE || clz == Float.TYPE)) {
          try {
            ret = obj != null ? obj.getDouble(attr): arr.getDouble(idx);
          } catch (Exception e) {
            return .0;
          }
        } else if (clz.equals(Long.class)
            || clz.isPrimitive() && clz == Long.TYPE) {
          try {
            ret = obj != null ? obj.getLong(attr): arr.getLong(idx);
          } catch (Exception e) {
            return 0l;
          }
        } else {
          ret = obj != null ? obj.get(attr): arr.get(idx);
          if (ret instanceof JSONObject && JsonBuilder.class.isAssignableFrom(clz) && !clz.isAssignableFrom(ret.getClass())) {
            ret = jsonFactory.create(clz, (JSONObject)ret);
          }
        }
      } catch (Throwable e) {
         System.out.println(this.getClass().getSimpleName() + " ERROR getting attr=" + attr + " idx=" + idx + " Exception=" + e.getMessage());
      }
      return ret;
    }

    private <T> JSONArray listToJsonArray(Object...l) throws Throwable {
      JSONArray ret = new JSONArray();
      for (Object o: l) {
        setValue(ret, null, null, o, null);
      }
      return ret;
    }

    private Object setValue(JSONArray arr, JSONObject obj, String attr, Object o, Method method) {
      try {
        if (o == null) {
          return o;
        }
        if (o instanceof String) {
          return obj != null ? obj.put(attr, o) : arr.put(o);
        } else if (o instanceof Boolean) {
          return obj != null ? obj.put(attr, o) : arr.put(o);
        } else if (o instanceof Number) {
          return obj != null ? obj.put(attr, o) : arr.put(o);
        } else if (o instanceof Date) {
          return obj != null ? obj.put(attr, ((Date) o).getTime()) : arr.put(((Date) o).getTime());
        } else if (o instanceof JsonBuilder) {
          return obj != null ? obj.put(attr, ((JsonBuilder) o).getProperties()) : arr.put(((JsonBuilder) o).getProperties());
        } else if (o.getClass().isArray() || o instanceof List) {
          Object[] arg;
          if (o.getClass().isArray()) {
            arg = (Object[])o;
          } else {
            arg = ((List<?>)o).toArray();
          }
          JSONArray a = listToJsonArray(arg);
          return obj != null ? obj.put(attr, a) : arr.put(a);
        } else {
          System.out.println("Unkown setter object " + attr + " " + o.getClass().getName() + " " + o);
          return obj != null ? obj.put(attr, o) : arr.put(o);
        }
      } catch (Throwable e) {
        e.printStackTrace();
      }
      return null;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      String mname = method.getName();
      Class<?>[] classes = method.getParameterTypes();
      int largs = classes.length;

      Name name = method.getAnnotation(Name.class);
      String attr = name != null ? name.value() : deCapitalize(mname.replaceFirst("^[gs]et", ""));

      if ("getFieldNames".equals(mname)) {
        return JSONObject.getNames(jsonObject);
      } else if ("getProperties".equals(mname)) {
        return jsonObject;
      } else if (largs > 0 && ("parse".equals(mname) || "load".equals(mname))) {
        jsonObject = new JSONObject(String.valueOf(args[0]));
      } else if ("toString".equals(mname) || "toJson".equals(mname)) {
        String jsonName = JsonBuilderGenerator.classNameToJsonName(getDataBindingClassName(proxy.getClass()));
        return "{\"" + jsonName + "\":"+ jsonObject.toString();
      } else if (largs == 0 || mname.startsWith("get")) {
        Class<?> ret = method.getReturnType();
        return getValue(null, 0, jsonObject, attr, ret, method);
      } else if (largs == 1 || mname.startsWith("set")) {
        setValue(null, jsonObject, attr, args[0], method);
        return proxy;
      }
      return null;
    }
  }

  @SuppressWarnings("unchecked")
  public <T> T create(Class<T> clz, JSONObject jso) {
    InvocationHandler handler = new JsonBuilderHandler(jso);
    return (T) Proxy.newProxyInstance(clz.getClassLoader(), new Class[] {clz}, handler);
  }

  @SuppressWarnings("unchecked")
  public <T extends JsonBuilder> T create(Class<T> clz) {
    InvocationHandler handler = new JsonBuilderHandler();
    return (T) Proxy.newProxyInstance(clz.getClassLoader(), new Class[] {clz}, handler);
  }

  private static String deCapitalize(String s) {
    return s != null && s.length() > 0 ? s.substring(0, 1).toLowerCase() + s.substring(1) : s;
  }

  private static String getDataBindingClassName(Class<?> type) {
    for (Class<?> c : type.getInterfaces()) {
      if (c.equals(JsonBuilder.class)) {
        return type.getName();
      } else {
        return getDataBindingClassName(c);
      }
    }
    return null;
  }
}
