package com.google.gwt.query.client;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * A wrapper around Javascript Regexps.
 */
public class Regexp {

  private final JavaScriptObject regexp;

  public Regexp(String pattern) {
    this.regexp = compile(pattern);
  }

  public Regexp(String pat, String flags) {
    this.regexp = compileFlags(pat, flags);
  }

  public static  native JavaScriptObject compile(String pat) /*-{
     return new RegExp(pat);
  }-*/;
  
   public static  native JavaScriptObject compileFlags(String pat, String flags) /*-{
     return new RegExp(pat, flags);
  }-*/;
  
   public JSArray exec(String str) {
     return exec0(regexp, str);
   }

  
  private static native JSArray exec0(JavaScriptObject regexp, String str) /*-{
    return regexp.exec(str);
  }-*/;

  public JSArray match(String currentRule) {
    return match0(regexp, currentRule);
  }

  private native JSArray match0(JavaScriptObject regexp, String currentRule)/*-{
    return currentRule.match(regexp);
  }-*/;

  public boolean test(String rule) {
    return test0(regexp, rule);
  }

  private native boolean test0(JavaScriptObject regexp, String rule) /*-{
    return regexp.test(rule);
  }-*/;

  public static JSArray match(String regexp, String flags, String string) {
    return new Regexp(regexp, flags).match(string);
  }
}
