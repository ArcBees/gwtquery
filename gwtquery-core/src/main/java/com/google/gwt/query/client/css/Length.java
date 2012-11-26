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

import com.google.gwt.dom.client.Style.HasCssName;
import com.google.gwt.dom.client.Style.Unit;

/**
 * Length type constructors.
 */
public class Length implements HasCssName {

  /**
   * Size in centimeters.
   */
  public static Length cm(double amt) {
    return new Length(amt + Unit.CM.getType());
  }

  /**
   * Size in centimeters.
   */
  public static Length cm(int amt) {
    px(1.2);
    return new Length(amt + Unit.CM.getType());
  }

  /**
   * Size as multiple of the 'font-size' of the relevant font.
   */
  public static Length em(double amt) {
    return new Length(amt + Unit.EM.getType());
  }

  /**
   * Size as multiple of the 'font-size' of the relevant font.
   */
  public static Length em(int amt) {
    return new Length(amt + Unit.EM.getType());
  }

  /**
   * Size as multiple of the 'x-height' of the relevant font.
   */
  public static Length ex(double amt) {
    return new Length(amt + Unit.EX.getType());
  }

  /**
   * Size as multiple of the 'x-height' of the relevant font.
   */
  public static Length ex(int amt) {
    return new Length(amt + Unit.EX.getType());
  }

  /**
   * Size in inches.
   */
  public static Length in(double amt) {
    return new Length(amt + Unit.IN.getType());
  }

  /**
   * Size in inches.
   */
  public static Length in(int amt) {
    return new Length(amt + Unit.IN.getType());
  }

  /**
   * Size in millimeters.
   */
  public static Length mm(double amt) {
    return new Length(amt + Unit.MM.getType());
  }

  /**
   * Size in millimeters.
   */
  public static Length mm(int amt) {
    return new Length(amt + Unit.MM.getType());
  }

  /**
   * Size in picas.
   */
  public static Length pc(double amt) {
    return new Length(amt + Unit.PC.getType());
  }

  /**
   * Size in picas.
   */
  public static Length pc(int amt) {
    return new Length(amt + Unit.PC.getType());
  }

  /**
   * Size in percentage units.
   */
  public static Length pct(double amt) {
    return new Length(amt + Unit.PCT.getType());
  }

  /**
   * Size in percentage units.
   */
  public static Length pct(int amt) {
    return new Length(amt + Unit.PCT.getType());
  }

  /**
   * Size in point.
   */
  public static Length pt(double amt) {
    return new Length(amt + Unit.PT.getType());
  }

  /**
   * Size in point.
   */
  public static Length pt(int amt) {
    return new Length(amt + Unit.PT.getType());
  }

  /**
   * Size in pixels.
   */
  public static Length px(double amt) {
    return new Length(amt + Unit.PX.getType());
  }

  /**
   * Size in pixels.
   */
  public static Length px(int amt) {
    return new Length(amt + Unit.PX.getType());
  }

  private String value;

  protected Length(String value) {
    this.value = value;
  }

  public String getCssName() {
    return value;
  }

  /**
   * @deprecated use {@link Length#getCssName()} instead
   */
  @Deprecated
  public String value() {
    return getCssName();
  }
}
