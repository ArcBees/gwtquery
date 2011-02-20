package com.google.gwt.query.client.plugins.widgets.widgetfactory;

import static com.google.gwt.query.client.GQuery.$;

import com.google.gwt.dom.client.Element;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabPanel;


/**
 * Factory used to create a {@link Button} widget. A {@link Button} is created
 * if the element is a <i>button</i>, <i>div></i>, <i>span</i> or <i>a</i>
 * element (should be extends to other element).
 */
public class TabPanelWidgetFactory extends
    AbstractWidgetFactory<TabPanel, TabPanelWidgetFactory.TabPanelOptions> {
  
  public static class ExtendedTabPanel extends TabPanel{
    
    
    void attach(){
      onAttach();
      RootPanel.detachOnWindowClose(this);
    }
  }

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

  protected void initialize(TabPanel tabPanel, TabPanelOptions options,
      Element e) {

    GQuery tabs = $(options.getTabSelector(), e);
    GQuery titles = $(options.getTitleSelector(), e);

    for (int i = 0; i < tabs.length(); i++) {
      Element tab = tabs.get(i);
      Element title = titles.get(i);

      tabPanel.add(new HTMLPanel(tab.getString()), title != null
          ? title.getInnerText() : "Tab " + (i + 1));
      
      

    }
    
    if (tabs.length() > 0){
      tabPanel.selectTab(0);
    }

    // the tab panel is initialized, attach it to the dom ;
    e.getParentElement().insertBefore(tabPanel.getElement(), e);
    ((ExtendedTabPanel)tabPanel).attach();

    // detach the element as it is replaced by the tab panel !
    e.removeFromParent();
  }

  protected TabPanel createWidget(Element e) {
    return new ExtendedTabPanel();
  }
}
