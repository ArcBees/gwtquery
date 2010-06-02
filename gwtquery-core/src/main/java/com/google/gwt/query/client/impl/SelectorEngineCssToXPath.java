/*
 * Copyright 2009 Google Inc.
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

import java.util.ArrayList;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.query.client.JSArray;
import com.google.gwt.query.client.Regexp;
import com.google.gwt.query.client.SelectorEngine;

/**
 * Runtime selector engine implementation which translates selectors to XPath
 * and delegates to document.evaluate(). 
 * It is based on the regular expressions in Andrea Giammarchi's Css2Xpath 
 */
public class SelectorEngineCssToXPath extends SelectorEngineImpl {
  
  /**
   * Interface for callbacks in replaceAll operations.
   */
  public static interface ReplaceCallback {
    String foundMatch(ArrayList<String> s);
  }
  
  /**
   * Interface for replacer implementations (GWT and JVM).
   */
  public static interface Replacer {
    String replaceAll(String s, String expr, Object replacement);
  }
  
  private static SelectorEngineCssToXPath instance;
  
  private static ReplaceCallback rc_$Attr = new ReplaceCallback() {
    public String foundMatch(ArrayList<String> s) {
      return "[substring(@" + s.get(1) +  ",string-length(@" + s.get(1) + ")-" + (s.get(2).replaceAll("'", "").length() - 1) +  ")=" + s.get(2) + "]";
    }
  };
  
  private static ReplaceCallback rc_Not = new ReplaceCallback() {
    public String foundMatch(ArrayList<String> s) {
      return s.get(1) + "[not(" +  getInstance().css2Xpath(s.get(2)).replaceAll("^[^\\[]+\\[([^\\]]*)\\].*$", "$1" +  ")]");
    }
  };
  
  private static ReplaceCallback rc_Attr = new ReplaceCallback() {
    public String foundMatch(ArrayList<String> s) {
      if (s.get(1) == null || s.get(1).length() == 0) {
        s.set(1, "*");
      }
      if (s.get(3) == null || s.get(3).length() == 0) {
        s.set(3, "");
      }
      return s.get(1) + "[@" + s.get(2) + s.get(3) + "]";
    }
  };
  
  private static ReplaceCallback rc_nth_child = new ReplaceCallback() {
    public String foundMatch(ArrayList<String> s) {
      if (s.get(1) == null || s.get(1).length() == 0) {
        s.set(1, "0");
      }
      if ("n".equals(s.get(2))) {
        return s.get(1);
      }
      if ("even".equals(s.get(2))) {
        return "*[position() mod 2=0 and position()>=0]/self::" + s.get(1);
      }
      if ("odd".equals(s.get(2))) {
        return s.get(1) + "[(count(preceding-sibling::*) + 1) mod 2=1]";
      }
      String[] t = s.get(2).replaceAll("^([0-9]*)n.*?([0-9]*)?$", "$1+$2").split("\\+");
      String t0 = t[0];
      String t1 = t.length > 1 ? t[1] : "0";
      return "*[(position()-" + t1 +  ") mod " +  t0 + "=0 and position()>=" +  t1 + "]/self::" +  s.get(1);
    }
  };

  private static Object[] regs = new Object[]{
    // tag[attrib=value]
    "([a-zA-Z0-9_\\-\\*\\[\\]])?\\[([^\\]@~\\$\\*\\^\\|\\!]+)(=[^\\]]+)?\\]", rc_Attr,
    // multiple queries
    "\\s*,\\s*", "|",
    // , + ~ >
    "\\s*(\\+|~|>)\\s*", "$1",
    //* ~ + >
    "([a-zA-Z0-9_\\-\\*])~([a-zA-Z0-9_\\-\\*])", "$1/following-sibling::$2",
    "([a-zA-Z0-9_\\-\\*])\\+([a-zA-Z0-9_\\-\\*])", "$1/following-sibling::*[1]/self::$2",
    "([a-zA-Z0-9_\\-\\*])>([a-zA-Z0-9_\\-\\*])", "$1/$2",
    // all unescaped stuff escaped
    "\\[([^=]+)=([^'|\"][^\\]]*)\\]", "[$1='$2']",
    // all descendant or self to 
    "(^|[^a-zA-Z0-9_\\-\\*])(#|\\.)([a-zA-Z0-9_\\-]+)", "$1*$2$3",
    "([\\>\\+\\|\\~\\,\\s])([a-zA-Z\\*]+)", "$1//$2",
    "\\s+//", "//",
    // :first-child
    "([a-zA-Z0-9_\\-\\*]+):first-child", "$1[not(preceding-sibling::*)]",
    // :last-child
    "([a-zA-Z0-9_\\-\\*]+):last-child", "$1[not(following-sibling::*)]",
    // :only-child
    "([a-zA-Z0-9_\\-\\*]+):only-child", "*[last()=1]/self::$1",
    // :empty
    "([a-zA-Z0-9_\\-\\*]+):empty", "$1[not(*) and not(normalize-space())]",
    "(.+):not\\(([^\\)]*)\\)", rc_Not,
    "([a-zA-Z0-9\\_\\-\\*]+):nth-child\\(([^\\)]*)\\)", rc_nth_child,
    // :contains(selectors)
    ":contains\\(([^\\)]*)\\)", "[contains(string(.),'$1')]",
    // |= attrib
    "\\[([a-zA-Z0-9_\\-]+)\\|=([^\\]]+)\\]", "[@$1=$2 or starts-with(@$1,concat($2,'-'))]",
    // *= attrib
    "\\[([a-zA-Z0-9_\\-]+)\\*=([^\\]]+)\\]", "[contains(@$1,$2)]",
    // ~= attrib
    "\\[([a-zA-Z0-9_\\-]+)~=([^\\]]+)\\]", "[contains(concat(' ',normalize-space(@$1),' '),concat(' ',$2,' '))]",
    // ^= attrib
    "\\[([a-zA-Z0-9_\\-]+)\\^=([^\\]]+)\\]", "[starts-with(@$1,$2)]",
    // $= attrib
    "\\[([a-zA-Z0-9_\\-]+)\\$=([^\\]]+)\\]", rc_$Attr,
    // != attrib
    "\\[([a-zA-Z0-9_\\-]+)\\!=([^\\]]+)\\]", "[not(@$1) or @$1!=$2]",
    // ids and classes
    "#([a-zA-Z0-9_\\-]+)", "[@id='$1']",
    "\\.([a-zA-Z0-9_\\-]+)", "[contains(concat(' ',normalize-space(@class),' '),' $1 ')]",
    // normalize multiple filters
    "\\]\\[([^\\]]+)", " and ($1)",
    // tag:attrib
    "([a-zA-Z0-9_\\-\\*]+):([a-zA-Z0-9_\\-]+)", "$1[@$2='$2']"
    };
  
  public static SelectorEngineCssToXPath getInstance() {
    if (instance == null) {
      instance = new SelectorEngineCssToXPath();
    }
    return instance;
  }

  // This replacer only works in browser, it must be replaced
  // when using this engine in generators and tests for the JVM
  private Replacer replacer = new Replacer() {
    public String replaceAll(String s, String r, Object o) {
      Regexp p = new Regexp(r);
      if (o instanceof ReplaceCallback) {
        ReplaceCallback callback = (ReplaceCallback) o;
        while (p.test(s)) {
          JSArray a = p.match(s);
          ArrayList<String> args = new ArrayList<String>();
          for (int i = 0; i < a.getLength(); i++) {
            args.add(a.getStr(i));
          }
          String f = callback.foundMatch(args);
          s = s.replaceFirst(r, f);
        }
        return s;
      } else {
        return s.replaceAll(r, o.toString()); 
      }
    }
  };

  public SelectorEngineCssToXPath() {
    instance = this;
  }
  
  public SelectorEngineCssToXPath(Replacer r) {
    replacer = r;
    instance = this;
  }
  
  public String css2Xpath(String selector) {
    String ret = selector;
    for (int i = 0; i < regs.length;) {
      ret = replacer.replaceAll(ret, regs[i++].toString(), regs[i++]);
    }
    return ".//" + ret;
  }
  
  public NodeList<Element> select(String sel, Node ctx) {
    JSArray elm = JSArray.create();
    if (!sel.startsWith("./") && !sel.startsWith("/")) {
      sel = css2Xpath(sel);
    }
    SelectorEngine.xpathEvaluate(sel, ctx, elm);
    return unique(elm.<JsArray<Element>> cast()).cast();
  }
  
}
