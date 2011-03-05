package com.google.gwt.query.client.plugins.widgets;

import static com.google.gwt.query.client.GQuery.$;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.user.client.ui.ListBox;

/**
 * Factory used to create a {@link ListBox} widget. A {@link ListBox} is created
 * if the element is a <i>select</i>, a <i>ul</i>, a <i>ol</i>, a <i>div</i> or
 * a<i>span</i> element. In case of <i>ul</i>, <i>ol</i>, <i>div</i> and
 * <i>span</i>, inner text of direct children will be composed the elements of
 * the list
 * 
 */
public class ListBoxWidgetFactory implements WidgetFactory<ListBox> {

  /**
   * Options used to create a {@link ListBox}
   * 
   */
  public static class ListBoxOptions implements WidgetOptions {

    private String optionsSelector;
    private boolean multipleSelect;

    public ListBoxOptions() {
      initDefault();
    }

    public ListBoxOptions(String optionsSelector, boolean multipleSelect) {
      this.optionsSelector = optionsSelector;
      this.multipleSelect = multipleSelect;
    }

    public String getOptionsSelector() {
      return optionsSelector;
    }

    public boolean isMultipleSelect() {
      return multipleSelect;
    }

    /**
     * Set the css selector to use for selecting elements playing roles of the
     * list items. The value of the items will be the inner texts of the
     * selected elements
     * 
     * If this options is null, the inner text of the direct children of the
     * element will be used as items list.
     * 
     * This options is used if the element is not a <i>select</i> element.
     * 
     * @param optionsSelector
     */
    public void setOptionsSelector(String optionsSelector) {
      this.optionsSelector = optionsSelector;
    }

    public void setMultipleSelect(boolean multipleSelect) {
      this.multipleSelect = multipleSelect;
    }

    private void initDefault() {
      optionsSelector = null;
      multipleSelect = false;
    }
  }

  private ListBoxOptions options;

  public ListBoxWidgetFactory(ListBoxOptions options) {
    this.options = options;
  }

  public ListBox create(Element e) {

    if (WidgetsUtils.matchesTags(e, "select")) {

      SelectElement selectElement = e.cast();

      if (selectElement.isMultiple() != options.isMultipleSelect()) {
        selectElement.setMultiple(options.isMultipleSelect());
      }

      return ListBox.wrap(e);
    }

    if (WidgetsUtils.matchesTags(e, "ul", "ol", "div", "span")) {
      SelectElement selectElement = Document.get().createSelectElement(
          options.isMultipleSelect());

      WidgetsUtils.replace(e, selectElement);

      ListBox listBox = ListBox.wrap(selectElement);

      GQuery itemsList = getItemsList(e);

      for (Element item : itemsList.elements()) {
        listBox.addItem(item.getInnerText());
      }

      return listBox;
    }

    return null;
  }

  private GQuery getItemsList(Element e) {
    if (options.getOptionsSelector() != null) {
      return $(options.getOptionsSelector(), e);
    }

    return $(e).children();
  }
}