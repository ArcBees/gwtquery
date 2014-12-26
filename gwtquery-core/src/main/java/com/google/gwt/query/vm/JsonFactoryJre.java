package com.google.gwt.query.vm;

import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.IsProperties;
import com.google.gwt.query.client.builders.JsonBuilder;
import com.google.gwt.query.client.builders.JsonFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import elemental.json.JsonObject;
import elemental.json.impl.JreJsonNull;

/**
 * Factory class to create JsonBuilders in the JVM.
 *
 * It uses java.util.reflect.Proxy to implement JsonBuilders
 * and elemental light weight json to handle json data.
 */
public class JsonFactoryJre implements JsonFactory  {

 

  /**
   * Although functions cannot be serialized to json we use JsonBuilders
   * or IsProperties objects which can be used as settings in Ajax.
   * Since Ajax and Promises are server side compatible, we need to handle
   * Functions in JVM.
   */
  static class JreJsonFunction extends JreJsonNull {
    final private Function function;
    public JreJsonFunction(Function f) {
      function = f;
    }
    @Override
    public String toJson() {
      return function.toString();
    }
    public Function getFunction() {
      return function;
    }
  }

  @SuppressWarnings("unchecked")
  public <T> T create(Class<T> clz, JsonObject jso) {
    InvocationHandler handler = new JsonBuilderHandler(jso);
    return (T) Proxy.newProxyInstance(clz.getClassLoader(), new Class[] {clz}, handler);
  }

  @SuppressWarnings("unchecked")
  public <T extends JsonBuilder> T create(Class<T> clz) {
    InvocationHandler handler = new JsonBuilderHandler();
    return (T) Proxy.newProxyInstance(clz.getClassLoader(), new Class[] {clz}, handler);
  }

  public IsProperties createBinder() {
    InvocationHandler handler = new JsonBuilderHandler();
    return (IsProperties)Proxy.newProxyInstance(IsProperties.class.getClassLoader(), new Class[] {IsProperties.class}, handler);
  }

  public IsProperties createBinder(JsonObject jso) {
    InvocationHandler handler = new JsonBuilderHandler(jso);
    return (IsProperties)Proxy.newProxyInstance(IsProperties.class.getClassLoader(), new Class[] {IsProperties.class}, handler);
  }

  @Override
  public IsProperties create(String s) {
    IsProperties ret = createBinder();
    ret.parse(s);
    return ret;
  }

  @Override
  public IsProperties create() {
    return createBinder();
  }
}
