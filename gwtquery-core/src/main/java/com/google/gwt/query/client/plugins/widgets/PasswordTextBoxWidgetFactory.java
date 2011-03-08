package com.google.gwt.query.client.plugins.widgets;

import static com.google.gwt.query.client.GQuery.$;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.user.client.ui.PasswordTextBox;

/**
 * Factory used to create a {@link PasswordTextBox} widget. A
 * {@link PasswordTextBox} is created if the element is a <i>input</i> with type
 * <i>password</i>, a <i>div</i> or a<i>span</i> element.
 * 
 */
public class PasswordTextBoxWidgetFactory implements
    WidgetFactory<PasswordTextBox> {

  public PasswordTextBox create(Element e) {

    GQuery input = $(e).filter("input[type='password']");

    if (input.get(0) != null) {
      return PasswordTextBox.wrap(e);
    }

    InputElement inputElement = Document.get().createPasswordInputElement();
    inputElement.setValue(e.getInnerText());

    WidgetsUtils.replaceOrAppend(e, inputElement);

    PasswordTextBox textBox = PasswordTextBox.wrap(inputElement);

    return textBox;

  }
}