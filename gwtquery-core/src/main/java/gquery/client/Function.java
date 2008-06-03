package gquery.client;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Event;

/**
   * Extend this class to implement functions.
 */
public abstract class Function {

  public void f(Element e) {
  }

  public boolean f(Event e, Object data) {
    return f(e);
  }

  public boolean f(Event e) {
    f(e.getCurrentTarget());
    return true;
  }
}
