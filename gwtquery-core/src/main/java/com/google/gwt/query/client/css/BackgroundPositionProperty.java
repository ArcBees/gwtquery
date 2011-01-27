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

import static com.google.gwt.query.client.css.CSS.INHERIT_VALUE;

import com.google.gwt.dom.client.Style.HasCssName;
import com.google.gwt.dom.client.Style.Unit;

/**
 * The {@link BackgroundPositionProperty} define the position of a
 * background-image.
 */
public class BackgroundPositionProperty extends
    AbstractCssProperty<BackgroundPositionProperty.BackgroundPosition> {

  public static class BackgroundPosition implements HasCssName {
    public static BackgroundPosition CENTER;
    public static BackgroundPosition CENTER_BOTTOM;
    public static BackgroundPosition CENTER_CENTER;
    public static BackgroundPosition CENTER_TOP;
    public static BackgroundPosition INHERIT;
    public static BackgroundPosition LEFT;
    public static BackgroundPosition LEFT_BOTTOM;
    public static BackgroundPosition LEFT_CENTER;
    public static BackgroundPosition LEFT_TOP;
    public static BackgroundPosition RIGHT;
    public static BackgroundPosition RIGHT_BOTTOM;
    public static BackgroundPosition RIGHT_CENTER;
    public static BackgroundPosition RIGHT_TOP;

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
      INHERIT = new BackgroundPosition(INHERIT_VALUE);
    }

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
  }

  public String getCssName() {
    return CSS_PROPERTY;
  }
}
