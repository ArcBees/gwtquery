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
import com.google.gwt.user.client.ui.RichTextArea;

/**
 * Factory used to create a {@link RichTextArea} widget. 
 * A {@link Button} is created if the element is a <i>textarea</i>, <i>div></i> or <i>span</i>.
 * The content of the element will be copied to the rich text area.
 */
public class RichTextWidgetFactory implements WidgetFactory<RichTextArea> {
  
  
  public RichTextArea create(Element e) {
    String v = null;
    if ("textarea".equalsIgnoreCase(e.getTagName())) {
      v = $(e).val();
    } else if (WidgetsUtils.matchesTags(e, "div", "span")) {
      v = $(e).html();
    }
    if (v != null) {
      RichTextArea w = create(v);
      WidgetsUtils.replaceOrAppend(e, w);
      return w;
    }
    return null;
  }

  private RichTextArea create(String v) {
    RichTextArea b = new RichTextArea();
    b.setHTML(v);
    return b;
  }

}