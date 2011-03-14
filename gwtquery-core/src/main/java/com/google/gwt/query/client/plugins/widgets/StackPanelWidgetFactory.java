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
import com.google.gwt.user.client.ui.DecoratedStackPanel;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.Widget;

public class StackPanelWidgetFactory implements WidgetFactory<StackPanel> {

  /**
   * Options used to initialize new {@link Button}
   * 
   */
  public static class StackPanelOptions implements WidgetOptions {

    private String headerSelector;
    private String contentSelector;
    private boolean decorated;

    public StackPanelOptions() {
      initDefault();
    }
    
    public StackPanelOptions(String headerSelector, String contentSelector, boolean decorated){
      this.headerSelector = headerSelector;
      this.contentSelector = contentSelector;
      this.decorated = decorated;
    }

    public String getHeaderSelector() {
      return headerSelector;
    }

    public String getContentSelector() {
      return contentSelector;
    }

    public boolean isDecorated() {
      return decorated;
    }
    
    public void setHeaderSelector(String headerSelector) {
      this.headerSelector = headerSelector;
    }

    public void setContentSelector(String contentSelector) {
      this.contentSelector = contentSelector;
    }
    
    public void setDecorated(boolean decorated) {
      this.decorated = decorated;
    }

    private void initDefault() {
      contentSelector = "div";
      headerSelector = "h3";
      decorated = true;
    }
  }
  
  private StackPanelOptions options;

  public StackPanelWidgetFactory(StackPanelOptions o) {
    this.options = o;
  }

  public StackPanel create(Element e) {
    StackPanel stackPanel = options.isDecorated() ? new DecoratedStackPanel() : new StackPanel();
    
    WidgetsUtils.replaceOrAppend(e, stackPanel);
    
    GQuery contents = $(options.getContentSelector(), e);
    GQuery headers = $(options.getHeaderSelector(), e);

    for (int i = 0; i < contents.length(); i++) {
      Element content = contents.get(i);
      Element header = headers.get(i);

      Widget contentWidget = $(content).widget();
      if (contentWidget == null){
        contentWidget = new WidgetsHtmlPanel(content);
      }
      
      stackPanel.add(contentWidget, header != null ? header.getInnerText() : "Undefined" );

    }
    

    
    return stackPanel;
  }

}
