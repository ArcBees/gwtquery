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
 * If a background image has been specified, this property specifies its initial
 * position.
 */
public class BackgroundPositionProperty extends
    CssProperty<BackgroundPositionProperty.BackgroundPosition> {

  /**
   * Value for <i>background-position</i> property.
   */
  public static class BackgroundPosition implements HasCssName {

    /**
     * Equivalent to {@link BackgroundPosition#CENTER_BOTTOM}.
     */
    public static BackgroundPosition BOTTOM;

    /**
     * Equivalent to {@link BackgroundPosition#CENTER_CENTER}.
     */
    public static BackgroundPosition CENTER;

    /**
     * Equivalent to '50%' for the horizontal position and '100%' for the
     * vertical position.
     */
    public static BackgroundPosition CENTER_BOTTOM;

    /**
     * Equivalent to '50%' for the horizontal position and '50%' for the
     * vertical position.
     */
    public static BackgroundPosition CENTER_CENTER;

    /**
     * Equivalent to '50%' for the horizontal position and '0%' for the vertical
     * position.
     */
    public static BackgroundPosition CENTER_TOP;

    /**
     * Equivalent to {@link BackgroundPosition#LEFT_CENTER}.
     */
    public static BackgroundPosition LEFT;

    /**
     * Equivalent to '0%' for the horizontal position and '100%' for the
     * vertical position.
     */
    public static BackgroundPosition LEFT_BOTTOM;

    /**
     * Equivalent to '0%' for the horizontal position and '50%' for the vertical
     * position.
     */
    public static BackgroundPosition LEFT_CENTER;

    /**
     * Equivalent to '0%' for the horizontal position and '0%' for the vertical
     * position.
     */
    public static BackgroundPosition LEFT_TOP;

    /**
     * Equivalent to {@link BackgroundPosition#RIGHT_CENTER}.
     */
    public static BackgroundPosition RIGHT;

    /**
     * Equivalent to '100%' for the horizontal position and '100%' for the
     * vertical position.
     */
    public static BackgroundPosition RIGHT_BOTTOM;

    /**
     * Equivalent to '100%' for the horizontal position and '50%' for the
     * vertical position.
     */
    public static BackgroundPosition RIGHT_CENTER;

    /**
     * Equivalent to '100%' for the horizontal position and '0%' for the
     * vertical position.
     */
    public static BackgroundPosition RIGHT_TOP;

    /**
     * Equivalent to {@link BackgroundPosition#CENTER_TOP}.
     */
    public static BackgroundPosition TOP;

    private static final String BOTTOM_VALUE = "bottom";
    private static final String CENTER_VALUE = "center";
    private static final String LEFT_VALUE = "left";
    private static final String RIGHT_VALUE = "right";
    private static final String TOP_VALUE = "top";

    static {

      CENTER_BOTTOM = new BackgroundPosition(CENTER_VALUE + " " + BOTTOM_VALUE);
      CENTER_CENTER = new BackgroundPosition(CENTER_VALUE + " " + CENTER_VALUE);
      CENTER = new BackgroundPosition(CENTER_VALUE);
      CENTER_TOP = new BackgroundPosition(CENTER_VALUE + " " + TOP_VALUE);
      LEFT_BOTTOM = new BackgroundPosition(LEFT_VALUE + " " + BOTTOM_VALUE);
      LEFT_CENTER = new BackgroundPosition(LEFT_VALUE + " " + CENTER_VALUE);
      LEFT = new BackgroundPosition(LEFT_VALUE);
      LEFT_TOP = new BackgroundPosition(LEFT_VALUE + " " + TOP_VALUE);
      RIGHT_BOTTOM = new BackgroundPosition(RIGHT_VALUE + " " + BOTTOM_VALUE);
      RIGHT_CENTER = new BackgroundPosition(RIGHT_VALUE + " " + CENTER_VALUE);
      RIGHT = new BackgroundPosition(RIGHT_VALUE);
      RIGHT_TOP = new BackgroundPosition(RIGHT_VALUE + " " + TOP_VALUE);
      TOP = new BackgroundPosition(TOP_VALUE);
      BOTTOM = new BackgroundPosition(BOTTOM_VALUE);
    }

    /**
     * Return a {@link BackgroundPosition} by specifying the horizontal and
     * vertical position. Pixel will be used as Unit.
     */
    public static BackgroundPosition position(int xpos, int ypos) {
      return position(xpos, ypos, Unit.PX);
    }

    /**
     * Return a {@link BackgroundPosition} by specifying the horizontal and
     * vertical position.
     */
    public static BackgroundPosition position(int xpos, int ypos, Unit unit) {
      if (unit == null) {
        unit = Unit.PX;
      }
      return new BackgroundPosition("" + xpos + unit.getType() + " " + ypos
          + unit.getType());
    }

    private String cssValue;

    private BackgroundPosition(String cssValue) {
      this.cssValue = cssValue;
    }

    public String getCssName() {
      return cssValue;
    }
  }

  private static final String CSS_PROPERTY = "backgroundPosition";

  public static void init() {
    CSS.BACKGROUND_POSITION = new BackgroundPositionProperty();
  }

  private BackgroundPositionProperty() {
    super(CSS_PROPERTY);
  }
}
