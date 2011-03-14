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
import com.google.gwt.query.client.GQuery;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Factory used to create a {@link Button} widget. A {@link Button} is created
 * if the element is a <i>button</i>, <i>div></i>, <i>span</i> or <i>a</i>
 * element (should be extends to other element).
 */
public class TabPanelWidgetFactory implements WidgetFactory<TabPanel> {


  /**
   * Options used to initialize new {@link Button}
   * 
   */
  public static class TabPanelOptions implements WidgetOptions {

    private String tabSelector;
    private String titleSelector;

    public TabPanelOptions() {
      initDefault();
    }
    
    public TabPanelOptions(String tabSelector, String titleSelector) {
      this.tabSelector = tabSelector;
      this.titleSelector = titleSelector;
      
    }

    public String getTabSelector() {
      return tabSelector;
    }

    public String getTitleSelector() {
      return titleSelector;
    }

    public void setTabSelector(String contentSelector) {
      this.tabSelector = contentSelector;
    }

    public void setTitleSelector(String titleSelector) {
      this.titleSelector = titleSelector;
    }

    private void initDefault() {
      tabSelector = "div";
      titleSelector = "h3";
    }
  }
  
  private TabPanelOptions options;

  public TabPanelWidgetFactory(TabPanelOptions o) {
    this.options = o;
  }

  public TabPanel create(Element e) {
    TabPanel tabPanel = new TabPanel();

    GQuery tabs = $(options.getTabSelector(), e);
    GQuery titles = $(options.getTitleSelector(), e);

    for (int i = 0; i < tabs.length(); i++) {
      GQuery tab = tabs.eq(i);
      GQuery title = titles.eq(i);

      Widget tabWidget = tab.widget();
      if (tabWidget == null){
        tabWidget = new WidgetsHtmlPanel(tab.get(0));
      }
      
      tabPanel.add(tabWidget, title.get(0) != null
          ? title.text() : "Tab " + (i + 1));

    }
    if (tabs.length() > 0) {
      tabPanel.selectTab(0);
    }

    WidgetsUtils.replaceOrAppend(e, tabPanel);
    return tabPanel;
  }
}
