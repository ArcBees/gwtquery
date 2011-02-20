package com.google.gwt.query.client.plugins.widgets.widgetfactory;

import com.google.gwt.dom.client.ButtonElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory used to create a {@link Button} widget. A {@link Button} is created
 * if the element is a <i>button</i>, <i>div></i>, <i>span</i> or <i>a</i>
 * element (should be extends to other element).
 */
public class ButtonWidgetFactory extends
    AbstractWidgetFactory<Button, ButtonWidgetFactory.ButtonOptions> {

  /**
   * Options used to initialize new {@link Button}
   * 
   */
  public static class ButtonOptions implements WidgetOptions {

    private List<ClickHandler> clickHandlers;

    public ButtonOptions() {
      clickHandlers = new ArrayList<ClickHandler>();
    }

    public void addClickHandler(ClickHandler clickHandler) {
      clickHandlers.add(clickHandler);
    }

    public List<ClickHandler> getClickHandlers() {
      return clickHandlers;
    }
  }

  protected void initialize(Button button, ButtonOptions options, Element e) {
    if (button == null || options == null) {
      return;
    }

    for (ClickHandler handler : options.getClickHandlers()) {
      button.addClickHandler(handler);
    }

  }

  protected Button createWidget(Element e) {

    if ("button".equalsIgnoreCase(e.getTagName())) {
      return Button.wrap(e);
    }

    if (matchesTags(e, "div", "span", "a")) {
      ButtonElement buttonElement = Document.get().createPushButtonElement();
      e.getParentElement().insertAfter(buttonElement, e);
      // detach the original element (can be maybe hidden instead of detach
      // it?)
      e.removeFromParent();

      Button b = Button.wrap(buttonElement);
      b.setHTML(e.getInnerHTML()); // maybe use setText and getInnerText

      return b;
    }

    return null;
  }
}