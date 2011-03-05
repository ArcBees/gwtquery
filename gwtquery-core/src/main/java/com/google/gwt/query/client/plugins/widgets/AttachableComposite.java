package com.google.gwt.query.client.plugins.widgets;

import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import com.google.gwt.user.client.ui.Composite;

/**
 * Provide a way to subclass widget class that cannot be subclassed directly in
 * order to provide a way to call onAttach method on it
 * 
 * 
 * @param <W>
 */
public class AttachableComposite<W extends Widget> extends Composite implements
    Attachable {

  
  public AttachableComposite(W widget) {
    initWidget(widget);
  }
  
  
  public void attach() {
    onAttach();
    RootPanel.detachOnWindowClose(this);
    
  }

  @SuppressWarnings("unchecked")
  public W getOriginalWidget(){
    return (W) getWidget();
  }
  
}
