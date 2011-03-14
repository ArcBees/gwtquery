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
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Label;

public class DisclosurePanelWidgetFactory implements
    WidgetFactory<DisclosurePanel> {

  /**
   * Options used to initialize new {@link Button}
   * 
   */
  public static class DisclosurePanelOptions {

    private String headerSelector;
    //private String headerTitle;

    public DisclosurePanelOptions() {
      initDefault();
    }
    
    public DisclosurePanelOptions(String headerSelector) {
      this.headerSelector = headerSelector;
     
    }

    public String getHeaderSelector() {
      return headerSelector;
    }

   /* public String getHeaderTitle() {
      return headerTitle;
    }*/
    
    public void setHeaderSelector(String headerSelector) {
      this.headerSelector = headerSelector;
    }

   /* public void setHeaderTitle(String headerTitle) {
      this.headerTitle = headerTitle;
    }*/

    private void initDefault() {
      //headerTitle = null;
      headerSelector = "h3";
    }
  }

  private DisclosurePanelOptions options;

  public DisclosurePanelWidgetFactory(DisclosurePanelOptions options) {
    this.options = options;
  }

  public DisclosurePanel create(Element e) {
    
    String headerValue = "";
    /*if (options.getHeaderTitle() != null){
      headerValue = options.getHeaderTitle();
    }else{*/
      headerValue = $(options.getHeaderSelector(), e).first().remove().text();
    //}
    
    DisclosurePanel disclosurePanel = new DisclosurePanel();
    disclosurePanel.setHeader(new Label(headerValue));
    
    WidgetsUtils.replaceOrAppend(e, disclosurePanel);
    
    disclosurePanel.add(new WidgetsHtmlPanel(e));
    

    return disclosurePanel;
  }

}
