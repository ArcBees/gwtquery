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
package com.google.gwt.query.client.plugins;

import com.google.gwt.dom.client.Element;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 *  Widgets plugin for Gwt Query. 
 */
public class Widgets extends GQueryQueue<Widgets>  {
  
  public static final Class<Widgets> Widgets = Widgets.class;

  static {
    GQuery.registerPlugin(Widgets.class, new Plugin<Widgets>() {
      public Widgets init(GQuery gq) {
        return new Widgets(gq);
      }
    });
  }
  
  protected Widgets(GQuery gq) {
    super(gq);
  }

  @SuppressWarnings("unchecked")
  @Override
  // TODO: consider more widgets
  public  <W extends Widget> W widget() {
    Widget w = super.widget();
    if (w == null) {
      Element e = get(0);
      if ("div".equalsIgnoreCase(e.getTagName()) || "span".equalsIgnoreCase(e.getTagName())) {
        w = HTML.wrap(e); 
      } else  if ("button".equalsIgnoreCase(e.getTagName())) {
        w = Button.wrap(e);
      } else  if ("text".equalsIgnoreCase(e.getTagName())) {
        w = TextBox.wrap(e);
      } else {
        w = new HTML($(e).toString());
      }
    }
    return (W)w;
  }

}
