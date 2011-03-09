package com.google.gwt.query.client.plugins.widgets;

import static com.google.gwt.query.client.GQuery.$;

import com.google.gwt.dom.client.Element;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.WidgetsUtils;

/**
 * Factory used to create a {@link TextBox} widget. A {@link TextBox} is created
 * if the element is a <i>input</i> with type text, a <i>div</i> or a<i>span</i>
 * element.
 * 
 */
public class TextBoxWidgetFactory implements WidgetFactory<TextBox> {

  public TextBox create(Element e) {

    GQuery input = $(e).filter("input[type='text']");

    if (input.get(0) != null) {
      return TextBox.wrap(e);
    }

    TextBox textBox = new TextBox();
    textBox.setValue(e.getInnerText());
    WidgetsUtils.replaceOrAppend(e, textBox);

    return textBox;
  }
}