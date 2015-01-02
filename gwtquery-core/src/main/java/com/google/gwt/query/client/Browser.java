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
package com.google.gwt.query.client;

/**
 * This class is the equivalent to the jQuery.browser object in gQuery.
 * 
 * The implementation is performed by the {@link BrowserGenerator}
 * 
 * It can be used as a way of deferred-binding without modifying .gwt.xml files,
 * taking advantage of compiler optimizations which will or will not include the
 * code in a 'if' statement checking these conditions.
 * 
 * Example:
 * <pre>
      if (GQuery.browser.ie6) {
        // this code will be removed on non-ie6 permutations
        Window.alert("IE6");
      } else if (!browser.webkit) {
        // this code will be only in the webkit permutation
        Window.alert("NOT WEBKIT");
      }
 * </pre>
 *
 */
public abstract class Browser {

  /**
   * @return true if ie6
   */
  public final boolean ie6 = isIe6();
  /**
   * @return true if ie8
   */
  public final boolean ie8 = isIe8();
  /**
   * @return true if ie9
   */
  public final boolean ie9 = isIe9();
  /**
   * @return true if Firefox
   */
  public final boolean mozilla = isMozilla();
  /**
   * @return true if any IE
   */
  public final boolean msie = isMsie();
  /**
   * @return true if opera
   */
  public final boolean opera = isOpera();
  /**
   * Maintained for jQuery compatibility.
   * @return true if webkit
   * @deprecated use webkit() instead
   */
  @Deprecated
  public final boolean safari = isWebkit();
  /**
   * @return true if webkit
   */
  public final boolean webkit = isWebkit();

  protected abstract boolean isIe6();

  protected abstract boolean isIe8();

  protected abstract boolean isIe9();

  protected abstract boolean isIe10();

  protected abstract boolean isIe11();

  protected abstract boolean isMozilla();

  protected abstract boolean isMsie();

  protected abstract boolean isOpera();

  protected abstract boolean isWebkit();
}
