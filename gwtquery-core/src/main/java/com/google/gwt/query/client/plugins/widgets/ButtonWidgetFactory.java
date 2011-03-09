package com.google.gwt.query.client.plugins.widgets;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.WidgetsUtils;

/**
 * Factory used to create a {@link Button} widget. A {@link Button} is created
 * if the element is a <i>button</i>, <i>div></i>, <i>span</i> or <i>a</i>
 * element (should be extends to other element).
 */
public class ButtonWidgetFactory implements WidgetFactory<Button> {

  public Button create(Element e) {

    if ("button".equalsIgnoreCase(e.getTagName())) {
      return Button.wrap(e);
    }

    
    Button button = new Button();
    button.getElement().setInnerText(e.getInnerText());
    
    WidgetsUtils.replaceOrAppend(e, button);
    return button;
  }
}