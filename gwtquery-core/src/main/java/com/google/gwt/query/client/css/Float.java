package com.google.gwt.query.client.css;

import com.google.gwt.dom.client.Style.HasCssName;

/**
 * Enum for the float property.
 */
public enum Float implements HasCssName {
  LEFT {
    @Override
    public String getCssName() {
      return "left";
    }
  },
  RIGHT {
    @Override
    public String getCssName() {
      return "right";
    }
  },
  NONE {
    @Override
    public String getCssName() {
      return "none";
    }
  };
  public abstract String getCssName();
}