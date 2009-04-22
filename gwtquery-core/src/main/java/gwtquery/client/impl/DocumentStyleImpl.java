package gwtquery.client.impl;

import com.google.gwt.dom.client.Element;

import gwtquery.client.SelectorEngine;

/**
 *
 */
public class DocumentStyleImpl {

  public String getPropertyName(String name) {
    if ("float".equals(name)) {
      return "cssFloat";
    } else if ("class".equals(name)) {
      return "className";
    } else if ("for".equals(name)) {
      return "htmlFor";
    }
    return name;
  }

  public String getCurrentStyle(Element elem, String name) {
    name = hyphenize(name);
    String propVal = getComputedStyle(elem, name, null);
    if ("opacity".equals(name)) {
      propVal = SelectorEngine.or(propVal, "1");
    }
    return propVal;
  }

  protected native String hyphenize(String name) /*-{
      return name.replace( /([A-Z])/g, "-$1" ).toLowerCase();
  }-*/;

  private native String getComputedStyle(Element elem, String name,
      String pseudo) /*-{
    	var cStyle = $doc.defaultView.getComputedStyle( elem, pseudo );
        return cStyle ? cStyle.getPropertyValue( name ) : null;
  }-*/;
}
