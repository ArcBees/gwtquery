/*
 * Copyright 2011, The gwtquery team.
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
package com.google.gwt.query.client.js;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * A wrapper around Javascript Regexps.
 * @deprecated use RegExp instead whose jsni implementation is the same
 */
@Deprecated
public class JsRegexp {

  public static native JavaScriptObject compile(String pat) /*-{
     return new RegExp(pat);
  }-*/;

  public static native JavaScriptObject compileFlags(String pat, String flags) /*-{
     return new RegExp(pat, flags);
  }-*/;

  public static JsObjectArray<String> match(String regexp, String flags, String string) {
    return new JsRegexp(regexp, flags).match(string);
  }

  private static native JsObjectArray<String> exec0(JavaScriptObject regexp, String str) /*-{
    return regexp.exec(str);
  }-*/;

  private final JavaScriptObject regexp;

  /**
   * @deprecated use RegExp.compile(pattern) instead
   */
  @Deprecated
  public JsRegexp(String pattern) {
    this.regexp = compile(pattern);
  }

  /**
   * @deprecated use RegExp.compile(pattern, flags) instead
   */
  @Deprecated
  public JsRegexp(String pat, String flags) {
    this.regexp = compileFlags(pat, flags);
  }

  public JsObjectArray<String> exec(String str) {
    return exec0(regexp, str);
  }

  public JsObjectArray<String> match(String currentRule) {
    return match0(regexp, currentRule);
  }

  public boolean test(String rule) {
    return test0(regexp, rule);
  }

  private native JsObjectArray<String> match0(JavaScriptObject regexp, String currentRule) /*-{
    return currentRule.match(regexp);
  }-*/;

  private native boolean test0(JavaScriptObject regexp, String rule) /*-{
    return regexp.test(rule);
  }-*/;

  public String getPattern() {
    return regexp.toString();
  };
}
