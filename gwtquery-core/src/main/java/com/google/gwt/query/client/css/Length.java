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
package com.google.gwt.query.client.css;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;

/**
 * Length type constructors.
 */
public class Length extends JavaScriptObject {

  protected Length() {
  }

  /**
   * Size in pixels.
   */
  public static Length px(int amt) {
    return GWT.isScript() ? createWeb(amt + "px") : createHosted(amt + "px");
  }

  /**
   * Size as multiple of the 'font-size' of the relevant font.
   */
  public static Length em(int amt) {
    return GWT.isScript() ? createWeb(amt + "em") : createHosted(amt + "em");
  }

  /**
   * Size as multiple of the 'x-height' of the relevant font.
   */
  public static Length ex(int amt) {
    return GWT.isScript() ? createWeb(amt + "ex") : createHosted(amt + "ex");
  }

  /**
   * Size in picas.
   */
  public static Length pc(int amt) {
    return GWT.isScript() ? createWeb(amt + "pc") : createHosted(amt + "pc");
  }

  /**
   * Size in millimeters.
   */
  public static Length mm(int amt) {
    return GWT.isScript() ? createWeb(amt + "mm") : createHosted(amt + "mm");
  }

  /**
   * Size in centimeters.
   */
  public static Length cm(int amt) {
    return GWT.isScript() ? createWeb(amt + "cm") : createHosted(amt + "cm");
  }

  /**
   * Size in inches.
   */
  public static Length in(int amt) {
    return GWT.isScript() ? createWeb(amt + "in") : createHosted(amt + "in");
  }

  private static native Length createWeb(String pct) /*-{
    return pct;
  }-*/;

  private static native Length createHosted(String pct) /*-{
    return [pct];
  }-*/;

  final public String value() {
    return GWT.isScript() ? valueWeb() : valueHosted();
  }

  private native String valueWeb() /*-{
    return this;
  }-*/;

  private native String valueHosted() /*-{
    return this[0];
  }-*/;
}
