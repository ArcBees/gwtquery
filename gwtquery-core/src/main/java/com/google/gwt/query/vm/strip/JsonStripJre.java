package com.google.gwt.query.vm.strip;

import com.google.gwt.query.client.IsProperties;
import com.google.gwt.query.client.builders.Name;
import com.google.gwt.query.vm.util.MethodNameParser;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Hashtable;

public class JsonStripJre {

  public static HashSet<String> getValidMethodsFrom(Method[] methods) {
    HashSet<String> valid = new HashSet<String>();
    
    if (!isValidArray(methods)) {
      return valid;
    }
    
    for (Method m : methods) {
      String attr = MethodNameParser.methodName2AttrName(m.getName());
      Name annotation = m.getAnnotation(Name.class);
      
      if (annotation != null) {
        attr = annotation.value();
      }
      valid.add(attr);
    }
    return valid;
  }

 

  public static Hashtable<String, Method> getJsonBuildersFrom(Method[] methods) {
    Hashtable<String, Method> recursiveBuilders = new Hashtable<String, Method>();
    
    if (!isValidArray(methods)) {
      return recursiveBuilders;
    }
    
    for (Method m : methods) {
      if(isRecursiveJsonBuilder(m)) {
        String attr = MethodNameParser.methodName2AttrName(m.getName());
        recursiveBuilders.put(attr, m);
      }
    }
    
    return recursiveBuilders;
  }
  
  private static boolean isRecursiveJsonBuilder(Method m) {
    Class<?>[] classes = m.getParameterTypes();
    return classes.length == 0 && IsProperties.class.isAssignableFrom(m.getReturnType());
  }

  
  private static boolean isValidArray(Object[] array) {
    return array != null && array.length > 0;
  }

}
