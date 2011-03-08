package com.google.gwt.query.client.plugins.widgets;

import static com.google.gwt.query.client.GQuery.$;

import com.google.gwt.dom.client.Element;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;
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
  
  private static class ExtendedTabPanel extends TabPanel implements Attachable{

    public void attach() {
      onAttach();
      RootPanel.detachOnWindowClose(this);
    }
  }

  private TabPanelOptions options;

  public TabPanelWidgetFactory(TabPanelOptions o) {
    this.options = o;
  }

  public TabPanel create(Element e) {
    ExtendedTabPanel tabPanel = new ExtendedTabPanel();

    initialize(tabPanel, options, e);

    return tabPanel;
  }


  protected void initialize(ExtendedTabPanel tabPanel, TabPanelOptions options,
      Element e) {

    GQuery tabs = $(options.getTabSelector(), e);
    GQuery titles = $(options.getTitleSelector(), e);

    for (int i = 0; i < tabs.length(); i++) {
      Element tab = tabs.get(i);
      Element title = titles.get(i);

      Widget tabWidget = $(tab).widget();
      if (tabWidget == null){
        tabWidget = new HTMLPanel(tab.getString());
      }
      
      tabPanel.add(tabWidget, title != null
          ? title.getInnerText() : "Tab " + (i + 1));

    }

    if (tabs.length() > 0) {
      tabPanel.selectTab(0);
    }

    WidgetsUtils.replaceOrAppend(e, tabPanel);

  }
}
