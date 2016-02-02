/*
 * Copyright 2014, The gwtquery team.
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
package com.google.gwt.query.vm;

import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.IsProperties;
import com.google.gwt.query.client.Properties;
import com.google.gwt.query.client.builders.JsonBuilder;
import com.google.gwt.query.client.builders.Name;
import com.google.gwt.query.rebind.JsonBuilderGenerator;
import com.google.gwt.query.vm.JsonFactoryJre.JreJsonFunction;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;

import elemental.json.Json;
import elemental.json.JsonArray;
import elemental.json.JsonBoolean;
import elemental.json.JsonNull;
import elemental.json.JsonNumber;
import elemental.json.JsonObject;
import elemental.json.JsonString;
import elemental.json.JsonValue;

/**
 * Reflection handler for JsonBuilder implementation in JVM. 
 */
public class JsonBuilderHandler implements InvocationHandler {

  static JsonFactoryJre jsonFactory = new JsonFactoryJre();

  private JsonObject jsonObject;

  public JsonBuilderHandler() {
    jsonObject = Json.createObject();
  }

  public JsonBuilderHandler(JsonObject j) {
    jsonObject = j;
  }

  public JsonBuilderHandler(String payload) throws Throwable {
    jsonObject = Json.parse(payload);
  }

  @SuppressWarnings("unchecked")
  private <T> Object jsonArrayToList(JsonArray j, Class<T> ctype, boolean isArray) {
    if (j == null) {
      return null;
    }
    
    List<T> l = new ArrayList<>();
    for (int i = 0; j != null && i < j.length(); i++) {
      l.add((T) getValue(j, i, null, null, ctype, null));
    }
    
    return l.isEmpty() ? Collections.emptyList() : isArray ? l.toArray((T[]) Array.newInstance(ctype, l.size())) : l;
  }

  private Double toDouble(String attr, JsonArray arr, int idx, JsonObject obj) {
    try {
      return obj != null ? obj.getNumber(attr) : arr.getNumber(idx);
    } catch (Exception e) {
      return 0d;
    }
  }

  private Object getValue(JsonArray arr, int idx, JsonObject obj, String attr, Class<?> clz,
      Method method) {
    if (clz.equals(Boolean.class) || clz == Boolean.TYPE) {
      try {
        return obj != null ? obj.getBoolean(attr) : arr.getBoolean(idx);
      } catch (Exception e) {
        return Boolean.FALSE;
      }
    } else if (clz.equals(Date.class)) {
      return new Date((long) (obj != null ? obj.getNumber(attr) : arr.getNumber(idx)));
    } else if (clz.equals(Byte.class) || clz == Byte.TYPE) {
      return toDouble(attr, arr, idx, obj).byteValue();
    } else if (clz.equals(Short.class) || clz == Short.TYPE) {
      return toDouble(attr, arr, idx, obj).shortValue();
    } else if (clz.equals(Integer.class) || clz == Integer.TYPE) {
      return toDouble(attr, arr, idx, obj).intValue();
    } else if (clz.equals(Double.class) || clz == Double.TYPE) {
      return toDouble(attr, arr, idx, obj);
    } else if (clz.equals(Float.class) || clz == Float.TYPE) {
      return toDouble(attr, arr, idx, obj).floatValue();
    } else if (clz.equals(Long.class) || clz == Long.TYPE) {
      return toDouble(attr, arr, idx, obj).longValue();
    }

    Object ret = obj != null ? obj.get(attr) : arr.get(idx);
    if (ret instanceof JreJsonFunction || clz.equals(Function.class)) {
      return ret != null && ret instanceof JreJsonFunction ? ((JreJsonFunction) ret).getFunction()
          : null;
    } else if (ret instanceof JsonNull) {
      return null;
    } else if (ret instanceof JsonString) {
      return ((JsonString) ret).asString();
    } else if (ret instanceof JsonBoolean) {
      return ((JsonBoolean) ret).asBoolean();
    } else if (ret instanceof JsonNumber) {
      return toDouble(attr, arr, idx, obj);
    } else if (ret instanceof JsonArray || clz.isArray() || clz.equals(List.class)) {
      Class<?> ctype = Object.class;
      if (clz.isArray()) {
        ctype = clz.getComponentType();
      } else {
        Type returnType = method.getGenericReturnType();
        if (returnType instanceof ParameterizedType) {
          ctype = (Class<?>) ((ParameterizedType) returnType).getActualTypeArguments()[0];
        }
      }
      return jsonArrayToList(obj.getArray(attr), ctype, clz.isArray());
    } else if (ret instanceof JsonObject) {
      if (clz == Object.class) {
        return jsonFactory.createBinder((JsonObject) ret);
      } else if (IsProperties.class.isAssignableFrom(clz) && !clz.isAssignableFrom(ret.getClass())) {
        return jsonFactory.create(clz, (JsonObject) ret);
      }
    }
    return ret;
  }

  private <T> JsonArray listToJsonArray(Object... l) throws Throwable {
    JsonArray ret = Json.createArray();
    for (Object o : l) {
      setValue(ret, null, null, o);
    }
    return ret;
  }

  private Object setValue(JsonArray jsArr, JsonObject jsObj, String attr, Object val) {
    if (val == null) {
      return Json.createNull();
    }

    try {
      Class<?> valClaz = JsonValue.class;
      if (val instanceof Number) {
        val = ((Number) val).doubleValue();
        valClaz = Double.TYPE;
      } else if (val instanceof Boolean) {
        valClaz = Boolean.TYPE;
      } else if (val instanceof Date) {
        val = ((Date) val).getTime();
        valClaz = Double.TYPE;
      } else if (val instanceof String) {
        valClaz = String.class;
      } else if (val instanceof IsProperties) {
        val = ((IsProperties) val).getDataImpl();
      } else if (val.getClass().isArray() || val instanceof List) {
        val =
            listToJsonArray(val.getClass().isArray() ? (Object[]) val : ((List<?>) val).toArray());
      } else if (val instanceof Function) {
        val = new JreJsonFunction((Function) val);
      }

      if (jsObj != null) {
        Method mth = jsObj.getClass().getMethod("put", String.class, valClaz);
        mth.invoke(jsObj, new Object[] {attr, val});
        return jsObj;
      } else {
        Method mth = jsArr.getClass().getMethod("set", Integer.TYPE, valClaz);
        mth.invoke(jsArr, new Object[] {new Integer(jsArr.length()), val});
        return jsArr;
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
    String attr = name != null ? name.value() : methodName2AttrName(mname);

    if ("getFieldNames".equals(mname)) {
      return jsonObject.keys();
    } else if ("as".equals(mname)) {
      @SuppressWarnings("unchecked")
      Class<? extends JsonBuilder> clz = (Class<? extends JsonBuilder>) args[0];
      return jsonFactory.create(clz, jsonObject);
    } else if ("getJsonName".equals(mname)) {
      return JsonBuilderGenerator.classNameToJsonName(getDataBindingClassName(proxy.getClass()));
    } else if (mname.matches("getProperties|getDataImpl")) {
      return jsonObject;
    } else if (largs > 0 && ("parse".equals(mname) || "load".equals(mname))) {
      String json = String.valueOf(args[0]);
      if (largs > 1 && Boolean.TRUE.equals(args[1])) {
        json = Properties.wrapPropertiesString(json);
      }
      jsonObject = Json.parse(json);
    } else if ("strip".equals(mname)) {
      stripProxy((JsonBuilder) proxy);
    } else if (mname.matches("toString")) {
      return jsonObject.toString();
    } else if (mname.matches("toJsonWithName")) {
      String jsonName =
          JsonBuilderGenerator.classNameToJsonName(getDataBindingClassName(proxy.getClass()));
      return "{\"" + jsonName + "\":" + jsonObject.toString() + "}";
    } else if (mname.matches("toJson")) {
      return jsonObject.toString();
    } else if ("toQueryString".equals(mname)) {
      return param(jsonObject);
    } else if (largs == 1 && mname.equals("get")) {
      Class<?> ret = method.getReturnType();
      attr = String.valueOf(args[0]);
      return getValue(null, 0, jsonObject, attr, ret, method);
    } else if (largs == 0 || mname.startsWith("get")) {
      Class<?> ret = method.getReturnType();
      return getValue(null, 0, jsonObject, attr, ret, method);
    } else if (largs == 2 && mname.equals("set")) {
      setValue(null, jsonObject, String.valueOf(args[0]), args[1]);
      return proxy;
    } else if (largs == 1 || mname.startsWith("set")) {
      setValue(null, jsonObject, attr, args[0]);
      return proxy;
    }
    return null;
  }

  public String methodName2AttrName(String s) {
    return deCapitalize(s.replaceFirst("^[gs]et", ""));
  }

  private String deCapitalize(String s) {
    return s != null && s.length() > 0 ? s.substring(0, 1).toLowerCase() + s.substring(1) : s;
  }

  /*
   * This method removes all the json which is not mapped into a 
   * method inside the JsonBuilder Object.
   * Also if the proxy contains another JsonBuilder in their methods
   * the method strip() is called.
   */
  private void stripProxy(JsonBuilder proxy) throws Throwable {
    Class<?> type = proxy.getClass().getInterfaces()[0];

    HashSet<String> validAttrs = getAttributeNames(type.getMethods());
    Hashtable<String, Method> ispropertyGetters = getJsonBuilders(type.getMethods());

    for (String key : jsonObject.keys()) {
      String name = methodName2AttrName(key);
      if (!validAttrs.contains(name)) {
        jsonObject.remove(key);
        continue;
      }
      Method ispropertyGetter = ispropertyGetters.get(name);
      if (ispropertyGetter != null) {
        ((IsProperties) invoke(proxy, ispropertyGetter, new Object[] {})).strip();
      }
    }
  }

  private String getDataBindingClassName(Class<?> type) {
    for (Class<?> c : type.getInterfaces()) {
      if (c.equals(JsonBuilder.class)) {
        return type.getName();
      } else {
        return getDataBindingClassName(c);
      }
    }
    return null;
  }

  private String param(JsonObject o) {
    String ret = "";
    for (String k : o.keys()) {
      ret += ret.isEmpty() ? "" : "&";
      JsonValue v = o.get(k);
      if (v instanceof JsonArray) {
        for (int i = 0, l = ((JsonArray) v).length(); i < l; i++) {
          ret += i > 0 ? "&" : "";
          JsonValue e = ((JsonArray) v).get(i);
          ret += k + "[]=" + e.toJson();
        }
      } else {
        if (v != null && !(v instanceof JsonNull)) {
          ret += k + "=" + v.toJson();
        }
      }
    }
    return ret;
  }

  private HashSet<String> getAttributeNames(Method[] methods) {
    HashSet<String> valid = new HashSet<>();

    if (methods == null || methods.length == 0) {
      return valid;
    }

    for (Method m : methods) {
      String attr = methodName2AttrName(m.getName());
      Name annotation = m.getAnnotation(Name.class);

      if (annotation != null) {
        attr = annotation.value();
      }
      valid.add(attr);
    }
    return valid;
  }

  private Hashtable<String, Method> getJsonBuilders(Method[] methods) {
    Hashtable<String, Method> ispropertyGetters = new Hashtable<>();

    if (methods == null || methods.length == 0) {
      return ispropertyGetters;
    }

    for (Method m : methods) {
      Class<?>[] classes = m.getParameterTypes();
      boolean isJsonBuilder =
          classes.length == 0 && IsProperties.class.isAssignableFrom(m.getReturnType());
      if (isJsonBuilder) {
        String attr = methodName2AttrName(m.getName());
        ispropertyGetters.put(attr, m);
      }
    }

    return ispropertyGetters;
  }
}
