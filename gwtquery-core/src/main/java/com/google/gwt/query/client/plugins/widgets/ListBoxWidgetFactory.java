package com.google.gwt.query.client.plugins.widgets;

import static com.google.gwt.query.client.GQuery.$;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.user.client.ui.ListBox;

/**
 * Factory used to create a {@link ListBox} widget.
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

    SelectElement selectElement = Document.get().createSelectElement(
        options.isMultipleSelect());
    
    GQuery itemsList = getItemsList(e);

    WidgetsUtils.replaceOrAppend(e, selectElement);

    ListBox listBox = ListBox.wrap(selectElement);

    for (Element item : itemsList.elements()) {
      listBox.addItem(item.getInnerText());
    }

    return listBox;

  }

  private GQuery getItemsList(Element e) {
    if (options.getOptionsSelector() != null) {
      return $(options.getOptionsSelector(), e);
    }

    return $(e).children();
  }
}