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
// TODO: inject page styles to the iframe. Provide a Toolbar.
public class RichTextWidgetFactory implements WidgetFactory<RichTextArea> {
  
  
  public static class AttachableRichTextArea extends RichTextArea implements Attachable {
    public void attach(){
      onAttach();
      // This fails with richtextarea, 
      // RootPanel.detachOnWindowClose(this);
    }
  }

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
    RichTextArea b = new AttachableRichTextArea();
    b.setHTML(v);
    return b;
  }

}