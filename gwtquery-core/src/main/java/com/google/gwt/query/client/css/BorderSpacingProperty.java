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
 * This property describes the color of a border.
 */
public class BorderSpacingProperty extends
    AbstractCssProperty<BorderSpacingProperty.BorderSpacing> {

  public static class BorderSpacing implements HasCssName {
    
    private Length verticalSpacing;
    private Length horizontalSpacing;
    
    public BorderSpacing(Length spacing) {
      this(spacing, spacing);
    }
    
    public BorderSpacing(Length horizontalSpacing, Length verticalSpacing) {
      assert horizontalSpacing != null : "horizontal spacing cannot be null";
      assert verticalSpacing != null : "vertical spacing cannot be null";
      
      this.verticalSpacing = verticalSpacing;
      this.horizontalSpacing = horizontalSpacing;
    }
    
    
    public String getCssName() {
      return horizontalSpacing.getCssName()+" "+verticalSpacing.getCssName();
    }
  }

  private static final String CSS_PROPERTY = "borderSpacing";

  public static void init() {
    CSS.BORDER_SPACING = new BorderSpacingProperty();

  }

  private BorderSpacingProperty() {
    super(CSS_PROPERTY);
  }
}
