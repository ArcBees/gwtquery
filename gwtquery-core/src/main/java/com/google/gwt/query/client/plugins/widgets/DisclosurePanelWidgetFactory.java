package com.google.gwt.query.client.plugins.widgets;

import static com.google.gwt.query.client.GQuery.$;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;

public class DisclosurePanelWidgetFactory implements
    WidgetFactory<DisclosurePanel> {

  /**
   * Options used to initialize new {@link Button}
   * 
   */
  public static class DisclosurePanelOptions implements WidgetOptions {

    private String headerSelector;
    private String headerTitle;

    public DisclosurePanelOptions() {
      initDefault();
    }

    public String getHeaderSelector() {
      return headerSelector;
    }

    public String getHeaderTitle() {
      return headerTitle;
    }
    
    public void setHeaderSelector(String headerSelector) {
      this.headerSelector = headerSelector;
    }

    public void setHeaderTitle(String headerTitle) {
      this.headerTitle = headerTitle;
    }

    private void initDefault() {
      headerTitle = null;
      headerSelector = "h3";
    }
  }

  private DisclosurePanelOptions options;

  public DisclosurePanelWidgetFactory(DisclosurePanelOptions options) {
    this.options = options;
  }

  public DisclosurePanel create(Element e) {
    AttachableComposite<DisclosurePanel> attachableDisclosurePanel = new AttachableComposite<DisclosurePanel>(
        new DisclosurePanel());

    initialize(attachableDisclosurePanel, e);

    return attachableDisclosurePanel.getOriginalWidget();
  }

  protected void initialize(AttachableComposite<DisclosurePanel> widget,
      Element e) {
    
    String headerValue = "";
    if (options.getHeaderTitle() != null){
      headerValue = options.getHeaderTitle();
    }else{
      headerValue = $(options.getHeaderSelector(), e).first().remove().text();
    }
    
    DisclosurePanel disclosurePanel = widget.getOriginalWidget();
   
    disclosurePanel.setHeader(new Label(headerValue));
    disclosurePanel.add(new HTMLPanel(e.getString()));
    WidgetsUtils.replaceOrAppend(e, widget);

  }

}
