package com.google.gwt.query.client.impl;

import com.google.gwt.dom.client.Element;

/**
 * Runtime implementaton of non-XPath/native for IE that fixes some DOM
 * operation incompatibilities.
 */
public class SelectorEngineJSIE extends SelectorEngineJS {

  public native String getAttr(Element elm, String attr) /*-{
     	switch (attr) {
	 case "id":
	   return elm.id;
	 case "for":
	   return elm.htmlFor;
	 case "class":
	   return elm.className;
	}
	return elm.getAttribute(attr, 2);
   }-*/;
}
