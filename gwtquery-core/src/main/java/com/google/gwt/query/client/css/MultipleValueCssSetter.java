package com.google.gwt.query.client.css;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.HasCssName;
import com.google.gwt.query.client.css.TakeCssValue.CssSetter;

public class MultipleValueCssSetter implements CssSetter {

  private String cssValue;
  private String cssPropertyName;
  
  public MultipleValueCssSetter(String cssPropertyName, HasCssName... values) {
    computeValue(values);
    this.cssPropertyName = cssPropertyName;
  }
  
  protected void computeValue(HasCssName... values){
    StringBuilder valueBuilder = new StringBuilder();
    
    for (HasCssName cssValue : values){
      valueBuilder.append(notNull(cssValue));
    }

    cssValue = valueBuilder.toString().trim();
  }

  public void applyCss(Element e) {
    assert e != null : "Impossible to apply css to a null object";
    e.getStyle().setProperty(cssPropertyName, cssValue);
    
  }
  

  private String notNull(HasCssName value) {
    return value != null ? value.getCssName() + " " : "";
  }
  
}
