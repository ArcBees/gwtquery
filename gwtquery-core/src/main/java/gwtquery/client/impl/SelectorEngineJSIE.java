package gwtquery.client.impl;

import com.google.gwt.dom.client.Element;

/**
 * Fix some DOM ops for IE
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
