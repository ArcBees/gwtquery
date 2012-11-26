package com.google.gwt.query.client.css;

import com.google.gwt.dom.client.Style.HasCssName;

public class MultipleValueCssSetter extends SimpleCssSetter {


  public MultipleValueCssSetter(String cssPropertyName, HasCssName... values) {
    super(cssPropertyName, computeValue(values));
  }

  protected static String computeValue(HasCssName... values){
    StringBuilder valueBuilder = new StringBuilder();

    for (HasCssName cssValue : values){
      valueBuilder.append(notNull(cssValue));
    }

    return valueBuilder.toString().trim();
  }


  private static String notNull(HasCssName value) {
    return value != null ? value.getCssName() + " " : "";
  }

}
