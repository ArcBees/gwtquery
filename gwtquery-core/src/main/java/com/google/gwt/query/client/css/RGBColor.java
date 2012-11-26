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

/**
 * Color type constructors.
 */
public class RGBColor implements HasCssName {

  public static RGBColor AQUA;
  public static RGBColor BLACK;
  public static RGBColor BLUE;
  public static RGBColor FUSCHIA;
  public static RGBColor GRAY;
  public static RGBColor GREY;
  public static RGBColor GREEN;
  public static RGBColor LIME;
  public static RGBColor MAROON;
  public static RGBColor NAVY;
  public static RGBColor OLIVE;
  public static RGBColor ORANGE;
  public static RGBColor PURPLE;
  public static RGBColor RED;
  public static RGBColor SILVER;
  public static RGBColor TEAL;
  public static RGBColor TRANSPARENT;
  public static RGBColor WHITE;
  public static RGBColor YELLOW;

  static {
    AQUA = rgb("aqua");
    BLACK = rgb("black");
    BLUE = rgb("blue");
    FUSCHIA = rgb("fuschia");
    GRAY = rgb("gray");
    GREY = rgb("grey");
    GREEN = rgb("green");
    LIME = rgb("lime");
    MAROON = rgb("maroon");
    NAVY = rgb("navy");
    OLIVE = rgb("olive");
    ORANGE = rgb("orange");
    PURPLE = rgb("purple");
    RED = rgb("red");
    SILVER = rgb("silver");
    TEAL = rgb("teal");
    WHITE = rgb("white");
    YELLOW = rgb("yellow");
    TRANSPARENT = rgb("transparent");
  }

  /**
   * RGB color as r,g,b triple.
   */
  public static RGBColor rgb(int r, int g, int b) {
    return new RGBColor(makeRgb(r, g, b));
  }

  /**
   * RGB color in hexidecimal.
   */
  public static RGBColor rgb(String hex) {
    return new RGBColor(hex);
  }

  private static String makeRgb(int r, int g, int b) {
    return "rgb(" + r + "," + g + "," + b + ")";
  }

  private String value;

  private RGBColor(String value) {
    this.value = value;
  }

  public String getCssName() {
    return value;
  }
}
