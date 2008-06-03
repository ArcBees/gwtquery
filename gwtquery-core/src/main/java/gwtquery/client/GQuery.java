package gwtquery.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ButtonElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.OptionElement;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.dom.client.TextAreaElement;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class GQuery {

  public static class Offset {

    public int top;

    public int left;

    Offset(int left, int top) {
      this.left = left;
      this.top = top;
    }
  }

  private static Map<Class<? extends GQuery>, Plugin<? extends GQuery>> plugins;

  /**
   * This function accepts a string containing a CSS selector which is then used
   * to match a set of elements, or it accepts raw HTML creating a GQuery
   * element containing those elements.
   */
  public static GQuery $(String selectorOrHtml) {
    if (selectorOrHtml.trim().charAt(0) == '<') {
      return innerHtml(selectorOrHtml);
    }
    return $(selectorOrHtml, Document.get());
  }

  public static <T extends GQuery> T $(T gq) {
    return gq;
  }

  /**
   * This function accepts a string containing a CSS selector which is then used
   * to match a set of elements, or it accepts raw HTML creating a GQuery
   * element containing those elements. The second parameter is is a class
   * reference to a plugin to be used.
   */
  public static <T extends GQuery> T $(String selector, Class<T> plugin) {
    try {
      if (plugins != null) {
        T gquery = (T) plugins.get(plugin).init($(selector, Document.get()));
        return gquery;
      }
      throw new RuntimeException("No plugin for class " + plugin);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * This function accepts a string containing a CSS selector which is then used
   * to match a set of elements, or it accepts raw HTML creating a GQuery
   * element containing those elements. The second parameter is the context to
   * use for the selector.
   */
  public static GQuery $(String selector, Node context) {
    return new GQuery(select(selector, context));
  }

  /**
   * This function accepts a string containing a CSS selector which is then used
   * to match a set of elements, or it accepts raw HTML creating a GQuery
   * element containing those elements. The second parameter is the context to
   * use for the selector. The third parameter is the class plugin to use.
   */
  public static <T extends GQuery> GQuery $(String selector, Node context,
      Class<T> plugin) {
    try {
      if (plugins != null) {
        T gquery = (T) plugins.get(plugin)
            .init(new GQuery(select(selector, context)));
        return gquery;
      }
      throw new RuntimeException("No plugin for class " + plugin);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Wrap a GQuery around  existing Elements.
   */
  public static GQuery $(NodeList<Element> elements) {
    return new GQuery(elements);
  }

  public static GQuery $(Element element) {
    JSArray a = JSArray.create();
    a.addNode(element);
    return new GQuery(a);
  }

  /**
   * Wrap a JSON object
   */
  public static Properties $$(String properties) {
    return Properties.create(properties);
  }

  public static <T extends Node> T[] asArray(NodeList<T> nl) {
    if (GWT.isScript()) {
      return reinterpretCast(nl);
    } else {
      Node[] elts = new Node[nl.getLength()];
      for (int i = 0; i < elts.length; i++) {
        elts[i] = nl.getItem(i);
      }
      return (T[]) elts;
    }
  }

  public static void registerPlugin(Class<? extends GQuery> plugin,
      Plugin<? extends GQuery> pluginFactory) {
    if (plugins == null) {
      plugins = new HashMap();
    }
    plugins.put(plugin, pluginFactory);
  }

  /**
   * Copied from UIObject *
   */
  protected static void setStyleName(Element elem, String style, boolean add) {

    style = style.trim();

    // Get the current style string.
    String oldStyle = elem.getClassName();
    int idx = oldStyle.indexOf(style);

    // Calculate matching index.
    while (idx != -1) {
      if (idx == 0 || oldStyle.charAt(idx - 1) == ' ') {
        int last = idx + style.length();
        int lastPos = oldStyle.length();
        if ((last == lastPos) || ((last < lastPos) && (oldStyle.charAt(last)
            == ' '))) {
          break;
        }
      }
      idx = oldStyle.indexOf(style, idx + 1);
    }

    if (add) {
      // Only add the style if it's not already present.
      if (idx == -1) {
        if (oldStyle.length() > 0) {
          oldStyle += " ";
        }
        DOM.setElementProperty(elem.<com.google.gwt.user.client.Element>cast(),
            "className", oldStyle + style);
      }
    } else {
      // Don't try to remove the style if it's not there.
      if (idx != -1) {
        // Get the leading and trailing parts, without the removed name.
        String begin = oldStyle.substring(0, idx).trim();
        String end = oldStyle.substring(idx + style.length()).trim();

        // Some contortions to make sure we don't leave extra spaces.
        String newClassName;
        if (begin.length() == 0) {
          newClassName = end;
        } else if (end.length() == 0) {
          newClassName = begin;
        } else {
          newClassName = begin + " " + end;
        }

        DOM.setElementProperty(elem.<com.google.gwt.user.client.Element>cast(),
            "className", newClassName);
      }
    }
  }

  private static boolean hasClass(Element e, String clz) {
    return e.getClassName().matches("\\s" + clz + "\\s");
  }

  private static GQuery innerHtml(String html) {
    Element div = DOM.createDiv();
    div.setInnerHTML(html);
    return new GQuery((NodeList<Element>) (NodeList<?>) div.getChildNodes());
  }

  private static native <T extends Node> T[] reinterpretCast(NodeList<T> nl) /*-{
        return nl;
    }-*/;

  private static NodeList select(String selector, Node context) {
    return new SelectorEngine().select(selector, context);
  }

  protected NodeList<Element> elements = null;

  public GQuery(NodeList<Element> list) {
    elements = list;
  }

  public GQuery(JSArray elements) {
    this.elements = elements;
  }

  public GQuery(Element element) {
    elements = JSArray.create(element);
  }

  /**
   * Adds the specified classes to each matched element.
   */
  public GQuery addClass(String... classes) {
    for (Element e : elements()) {
      for (String clz : classes) {
        setStyleName(e, clz, true);
      }
    }
    return this;
  }

  /**
   * Convert to Plugin interface provided by Class literal.
   */
  public <T extends GQuery> T as(Class<T> plugin) {
    if (plugins != null) {
      return (T) plugins.get(plugin).init(this);
    }
    throw new RuntimeException("No plugin registered for class " + plugin);
  }

  public String attr(String name) {
    return elements.getItem(0).getAttribute(name);
  }

  public GQuery attr(String key, String value) {
    for (Element e : elements()) {
      e.setAttribute(key, value);
    }
    return this;
  }

  public GQuery attr(Properties properties) {
    for (Element e : elements()) {
      for (String name : properties.keys()) {
        e.setAttribute(name, properties.get(name));
      }
    }
    return this;
  }

  public GQuery bind(int eventbits, final Object data, final Function f) {
    EventListener listener = new EventListener() {
      public void onBrowserEvent(Event event) {
        if (!f.f(event, data)) {
          event.cancelBubble(true);
          event.preventDefault();
        }
      }
    };
    for (Element e : elements()) {
      DOM.sinkEvents((com.google.gwt.user.client.Element) e, eventbits);
      DOM.setEventListener((com.google.gwt.user.client.Element) e, listener);
    }
    return this;
  }

  public GQuery blur(Function f) {
    return bind(Event.ONBLUR, null, f);
  }

  public GQuery change(Function f) {
    return bind(Event.ONCHANGE, null, f);
  }

  public GQuery click(final Function f) {
    return bind(Event.ONCLICK, null, f);
  }

  public String css(String name) {
    return elements.getItem(0).getStyle().getProperty(name);
  }

  public GQuery css(Properties properties) {
    for (String property : properties.keys()) {
      css(property, properties.get(property));
    }
    return this;
  }

  public GQuery css(String prop, String val) {
    for (Element e : elements()) {
      e.getStyle().setProperty(prop, val);
    }
    return this;
  }

  public GQuery dblclick(Function f) {
    return bind(Event.ONDBLCLICK, null, f);
  }

  /**
   * Run one or more Functions over each element of the GQuery.
   */
  public GQuery each(Function... f) {
    for (Function f1 : f) {
      for (Element e : elements()) {
        f1.f(e);
      }
    }
    return this;
  }

  /**
   * Returns the working set of nodes as a Java array. <b>Do NOT</b attempt to
   * modify this array, e.g. assign to its elements, or call Arrays.sort()
   */
  public Element[] elements() {
    return asArray(elements);
  }

  /**
   * Reduce GQuery to element in the specified position.
   */
  public GQuery eq(int pos) {
    return $(elements.getItem(pos));
  }

  public GQuery error(Function f) {
    return bind(Event.ONERROR, null, f);
  }

  public GQuery focus(Function f) {
    return bind(Event.ONFOCUS, null, f);
  }

  /**
   * Return all elements matched in the GQuery as a NodeList. @see #elements()
   * for a method which returns them as an immutable Java array.
   */
  public NodeList<Element> get() {
    return elements;
  }

  /**
   * Return the ith element matched.
   */
  public Element get(int i) {
    return elements.getItem(i);
  }

  /**
   * Returns true any of the specified classes are present on any of the matched
   * elements.
   */
  public boolean hasClass(String... classes) {
    for (Element e : elements()) {
      for (String clz : classes) {
        if (hasClass(e, clz)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Set the height of every element in the matched set.
   */
  public GQuery height(int height) {
    for (Element e : elements()) {
      e.getStyle().setPropertyPx("height", height);
    }
    return this;
  }

  /**
   * Get the innerHTML of the first matched element.
   */
  public String html() {
    return get(0).getInnerHTML();
  }

  /**
   * Set the innerHTML of every matched element.
   */
  public GQuery html(String html) {
    for (Element e : elements()) {
      e.setInnerHTML(html);
    }
    return this;
  }

  /**
   * Find the index of the specified Element
   */
  public int index(Element element) {
    for (int i = 0; i < elements.getLength(); i++) {
      if (elements.getItem(i) == element) {
        return i;
      }
    }
    return -1;
  }

  public GQuery keydown(Function f) {
    return bind(Event.ONKEYDOWN, null, f);
  }

  public GQuery keypressed(Function f) {
    return bind(Event.ONKEYPRESS, null, f);
  }

  public GQuery keyup(Function f) {
    return bind(Event.ONKEYUP, null, f);
  }

  public GQuery load(Function f) {
    return bind(Event.ONLOAD, null, f);
  }

  public GQuery mousedown(Function f) {
    return bind(Event.ONMOUSEDOWN, null, f);
  }

  public GQuery mousemove(Function f) {
    return bind(Event.ONMOUSEMOVE, null, f);
  }

  public GQuery mouseout(Function f) {
    return bind(Event.ONMOUSEOUT, null, f);
  }

  public GQuery mouseover(Function f) {
    return bind(Event.ONMOUSEOVER, null, f);
  }

  public GQuery mouseup(Function f) {
    return bind(Event.ONMOUSEUP, null, f);
  }

  public Offset offset() {
    return new Offset(get(0).getOffsetLeft(), get(0).getOffsetTop());
  }

  /**
   * Remove the named attribute from every element in the matched set.
   */
  public GQuery removeAttr(String key) {
    for (Element e : elements()) {
      e.removeAttribute(key);
    }
    return this;
  }

  /**
   * Removes the specified classes to each matched element.
   */
  public GQuery removeClass(String... classes) {
    for (Element e : elements()) {
      for (String clz : classes) {
        setStyleName(e, clz, false);
      }
    }
    return this;
  }

  public GQuery scroll(Function f) {
    return bind(Event.ONSCROLL, null, f);
  }

  /**
   * Return the number of elements in the matched set.
   */
  public int size() {
    return elements.getLength();
  }

  public GQuery slice(int start, int end) {
    JSArray slice = JSArray.create();
    if (end == -1 || end > elements.getLength()) {
      end = elements.getLength();
    }
    for (int i = start; i < elements.getLength(); i++) {
      slice.addNode(elements.getItem(i));
    }
    return new GQuery(slice);
  }

  /**
   * Return the text contained in the first matched element.
   */
  public String text() {
    return elements.getItem(0).getInnerText();
  }

  /**
   * Set the innerText of every matched element.
   */
  public GQuery text(String txt) {
    for (Element e : asArray(elements)) {
      e.setInnerText(txt);
    }
    return this;
  }

  /**
   * Adds or removes the specified classes to each matched element.
   */
  public GQuery toggleClass(String... classes) {
    for (Element e : elements()) {
      for (String clz : classes) {
        if (hasClass(e, clz)) {
          setStyleName(e, clz, false);
        } else {
          setStyleName(e, clz, true);
        }
      }
    }
    return this;
  }

  /**
   * Get the content of the value attribute of the first matched element,
   * returns more than one value if it is a multiple select.
   */
  public String[] val() {
    if (size() > 0) {
      Element e = get(0);
      if (e.getNodeName().equals("select")) {
        SelectElement se = SelectElement.as(e);
        if (se.getMultiple() != null) {
          NodeList<OptionElement> oel = se.getOptions();
          int count = 0;
          for (OptionElement oe : asArray(oel)) {
            if (oe.isSelected()) {
              count++;
            }
          }
          String result[] = new String[count];
          count = 0;
          for (OptionElement oe : asArray(oel)) {
            if (oe.isSelected()) {
              result[count++] = oe.getValue();
            }
          }

          return result;
        } else {
          int index = se.getSelectedIndex();
          if (index != -1) {
            return new String[]{se.getOptions().getItem(index).getValue()};
          }
        }
      } else if (e.getNodeName().equals("input")) {
        InputElement ie = InputElement.as(e);
        return new String[]{ie.getValue()};
      }
    }
    return new String[0];
  }

  public GQuery val(String... values) {
    for (Element e : elements()) {
      String name = e.getNodeName();
      if ("select".equals(name)) {

      } else if ("input".equals(name)) {
        InputElement ie = InputElement.as(e);
        String type = ie.getType();
        if ("radio".equals((type)) || "checkbox".equals(type)) {
          if ("checkbox".equals(type)) {
            for (String val : values) {
              if (ie.getValue().equals(val)) {
                ie.setChecked(true);
              } else if (ie.getValue().equals(val)) {
                ie.setChecked(true);
              }
            }
          }
        } else {
          ie.setValue(values[0]);
        }
      } else if ("textarea".equals(name)) {
        TextAreaElement.as(e).setValue(values[0]);
      } else if ("button".equals(name)) {
        ButtonElement.as(e).setValue(values[0]);
      }
    }
    return this;
  }

  /**
   * Set the width of every matched element.
   */
  public GQuery width(int width) {
    for (Element e : elements()) {
      e.getStyle().setPropertyPx("width", width);
    }
    return this;
  }

  private void init(GQuery gQuery) {
    this.elements = gQuery.elements;
  }
  
 
}
