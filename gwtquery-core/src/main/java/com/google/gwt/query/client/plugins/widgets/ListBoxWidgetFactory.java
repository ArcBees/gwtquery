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
package com.google.gwt.query.client.plugins.widgets;

import static com.google.gwt.query.client.GQuery.$;

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
    
    ListBox listBox = new ListBox(options.isMultipleSelect());
    if (WidgetsUtils.matchesTags(e, "select")) {
      copyAttributes((SelectElement)e.cast(),(SelectElement)listBox.getElement().cast());
    }

    GQuery itemsList = getItemsList(e);
    for (Element item : itemsList.elements()) {
      listBox.addItem(item.getInnerText());
    }

    WidgetsUtils.replaceOrAppend(e, listBox);
    return listBox;
  }

  protected void copyAttributes(SelectElement source, SelectElement destination) {
    destination.setDisabled(source.isDisabled());
    destination.setName(source.getName());
    destination.setSelectedIndex(source.getSelectedIndex());
    destination.setSize(source.getSize());
    
  }

  protected GQuery getItemsList(Element e) {
    if ("select".equalsIgnoreCase(e.getTagName())){
      return $("option", e);
    }
    if (options.getOptionsSelector() != null) {
      return $(options.getOptionsSelector(), e);
    }
    return $(e).children();
  }
}