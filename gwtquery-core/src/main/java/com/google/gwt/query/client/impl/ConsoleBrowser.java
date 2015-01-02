/*
 * Copyright 2013, The gwtquery team.
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
package com.google.gwt.query.client.impl;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.query.client.Console;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.js.JsObjectArray;

/**
 * Implementation of the Console interface based on the
 * browser console.
 */
public class ConsoleBrowser implements Console {

  /**
   * http://whattheheadsaid.com/2011/04/internet-explorer-9s-problematic-console-object.
   */
  private static class ConsoleIe8 extends ConsoleIe9 {
    @Override
    protected native void init() /*-{
      try {
        Function.prototype.call.call($wnd.console.log, $wnd.console, Array.prototype.slice.call(arguments));
        this.@com.google.gwt.query.client.impl.ConsoleBrowser.ConsoleIe9::initialized = true;
      } catch(e) {
      }
    }-*/;
  }

  /**
   * See: http://whattheheadsaid.com/2011/04/internet-explorer-9s-problematic-console-object.
   */
  private static class ConsoleIe9 extends ConsoleImpl {

    private boolean initialized = false;

    public ConsoleIe9() {
      init();
    }

    protected native void init() /*-{
      try {
        [ "log", "info", "warn", "error", "dir", "clear", "profile", "profileEnd" ]
          .forEach(function(method) {
            $wnd.console[method] = this.call($wnd.console[method], $wnd.console);
          }, Function.prototype.bind);
        this.@com.google.gwt.query.client.impl.ConsoleBrowser.ConsoleIe9::initialized = true;
      } catch(e) {
      }
    }-*/;

    @Override
    public void clear() {
      if (initialized) {
        super.clear();
      }
    }

    @Override
    public void dir(Object arg) {
      if (initialized) {
        super.dir(arg);
      }
    }

    @Override
    public void error(JavaScriptObject arg) {
      if (initialized) {
        super.error(arg);
      }
    }

    @Override
    public void info(JavaScriptObject arg) {
      if (initialized) {
        super.info(arg);
      }
    }

    @Override
    public void profile(String title) {
      if (initialized) {
        super.profile(title);
      }
    }

    @Override
    public void profileEnd(String title) {
      if (initialized) {
        super.profileEnd(title);
      }
    }

    @Override
    public void warn(JavaScriptObject arg) {
      if (initialized) {
        super.warn(arg);
      }
    }

    @Override
    public void group(Object arg) {
    }

    @Override
    public void groupCollapsed(Object arg) {
    }

    @Override
    public void groupEnd() {
    }

    @Override
    public void time(String title) {
    }

    @Override
    public void timeStamp(Object arg) {
    }

    @Override
    public void timeEnd(String title) {
    }
  }

  /**
   * Default implementation: webkit, opera, FF, IE10.
   */
  private static class ConsoleImpl {
    public native void clear() /*-{
      $wnd.console.clear();
    }-*/;

    public native void dir(Object arg) /*-{
      $wnd.console.dir(arg);
    }-*/;

    public native void error(JavaScriptObject arg) /*-{
      $wnd.console.error.apply($wnd.console, arg);
    }-*/;

    public native void group(Object arg) /*-{
      $wnd.console.group(arg);
    }-*/;

    public native void groupCollapsed(Object arg) /*-{
      $wnd.console.groupCollapsed(arg);
    }-*/;

    public native void groupEnd() /*-{
      $wnd.console.groupEnd();
    }-*/;

    public native void info(JavaScriptObject arg) /*-{
      $wnd.console.info.apply($wnd.console, arg);
    }-*/;

    public native void log(JavaScriptObject arg) /*-{
      $wnd.console.log.apply($wnd.console, arg);
    }-*/;

    public native void profile(String title) /*-{
      $wnd.console.profile(title);
    }-*/;

    public native void profileEnd(String title) /*-{
      $wnd.console.profileEnd(title);
    }-*/;

    public native void time(String title) /*-{
      $wnd.console.time(title);
    }-*/;

    public native void timeEnd(String title) /*-{
      $wnd.console.timeEnd(title);
    }-*/;

    public native void timeStamp(Object arg) /*-{
      $wnd.console.timeStamp(arg);
    }-*/;

    public native void warn(JavaScriptObject arg) /*-{
      $wnd.console.warn.apply($wnd.console, arg);
    }-*/;
  }

  private ConsoleImpl impl;

  public ConsoleBrowser() {
    impl =
        GQuery.browser.ie8 ? new ConsoleIe8() : GQuery.browser.ie9 ? new ConsoleIe9()
            : new ConsoleImpl();
  }

  @Override
  public void clear() {
    impl.clear();
  }

  @Override
  public void dir(Object arg) {
    impl.dir(toJs(arg));
  }

  @Override
  public void error(Object arg) {
    impl.error(toJs(arg));
  }

  @Override
  public void error(Object... args) {
    impl.error(toJs(args));
  }

  @Override
  public void group(Object arg) {
    impl.group(toJs(arg));
  }

  @Override
  public void groupCollapsed(Object arg) {
    impl.groupCollapsed(toJs(arg));
  }

  @Override
  public void groupEnd() {
    impl.groupEnd();
  }

  @Override
  public void info(Object arg) {
    impl.info(toJs(arg));
  }

  @Override
  public void info(Object... args) {
    impl.info(toJs(args));
  }

  @Override
  public void log(Object arg) {
    impl.log(toJs(arg));
  }

  @Override
  public void log(Object... args) {
    impl.log(toJs(args));
  }

  @Override
  public void profile(String title) {
    impl.profile(title);
  }

  @Override
  public void profileEnd(String title) {
    impl.profileEnd(title);
  }

  @Override
  public void time(String title) {
    impl.time(title);
  }

  @Override
  public void timeEnd(String title) {
    impl.timeEnd(title);
  }

  @Override
  public void timeStamp(Object arg) {
    impl.timeStamp(toJs(arg));
  }

  @Override
  public void warn(Object arg) {
    impl.warn(toJs(arg));
  }

  @Override
  public void warn(Object... arg) {
    impl.warn(toJs(arg));
  }

  private JsObjectArray<?> toJs(Object... arg) {
    JsObjectArray<Object> ret = JsObjectArray.createArray().cast();
    for (Object o : arg) {
      ret.add(o);
    }
    return ret;
  }
}
