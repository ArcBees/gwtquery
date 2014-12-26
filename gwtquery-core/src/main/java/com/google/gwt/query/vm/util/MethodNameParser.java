package com.google.gwt.query.vm.util;

public class MethodNameParser {
  
  public static String methodName2AttrName(String s) {
    return deCapitalize(s.replaceFirst("^[gs]et", ""));
  }

  private static String deCapitalize(String s) {
    return s != null && s.length() > 0 ? s.substring(0, 1).toLowerCase() + s.substring(1) : s;
  }
}
