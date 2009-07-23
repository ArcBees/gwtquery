package com.google.gwt.query.client.css;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Style;

/**
 * This property specifies whether a box should float to the left, right, or not
 * at all. It may be set for any element, but only applies to elements that
 * generate boxes that are not absolutely positioned.
 */
public class Cursor implements CssProperty<Cursor.CursorValue> {

  final static public class CursorValue extends JavaScriptObject {

    protected static CursorValue create(String val) {
      return GWT.isScript() ? createWeb(val) : createHosted(val);
    }

    private static native CursorValue createHosted(String val) /*-{
      return [val];
    }-*/;

    private static native CursorValue createWeb(String val) /*-{
      return val;
    }-*/;

    protected CursorValue() {
    }

    public String value() {
      return GWT.isScript() ? valueWeb() : valueHosted();
    }

    private native String valueHosted() /*-{
       return this[0];
    }-*/;

    private native String valueWeb() /*-{
       return this;
    }-*/;
  }

  /**
   * Returns a cursor located at the given uri.
   */
  public static CursorValue cursor(String uri) {
    return CursorValue.create("uri(" + uri + ")");
  }

  public static void init() {
    CSS.CURSOR = new Cursor();
    CSS.CURSOR_AUTO = CursorValue.create("auto");
    CSS.CURSOR_CROSSHAIR = CursorValue.create("crosshair");
    CSS.CURSOR_DEFAULT = CursorValue.create("default");
    CSS.CURSOR_POINTER = CursorValue.create("pointer");
    CSS.CURSOR_MOVE = CursorValue.create("move");
    CSS.CURSOR_TEXT = CursorValue.create("text");
    CSS.CURSOR_WAIT = CursorValue.create("wait");
    CSS.CURSOR_PROGRESS = CursorValue.create("progress");
    CSS.CURSOR_HELP = CursorValue.create("help");
    CSS.CURSOR_E_RESIZE = CursorValue.create("e-resize");
    CSS.CURSOR_NE_RESIZE = CursorValue.create("ne-resize");
    CSS.CURSOR_NW_RESIZE = CursorValue.create("nw-resize");
    CSS.CURSOR_N_RESIZE = CursorValue.create("n-resize");
    CSS.CURSOR_SE_RESIZE = CursorValue.create("se-resize");
    CSS.CURSOR_SW_RESIZE = CursorValue.create("sw-resize");
    CSS.CURSOR_S_RESIZE = CursorValue.create("s-resize");
    CSS.CURSOR_W_RESIZE = CursorValue.create("w-resize");
  }

  public String get(Style s) {
    return s.getProperty("float");
  }

  public void set(Style s, CursorValue value) {
    s.setProperty("float", value.value());
  }
}