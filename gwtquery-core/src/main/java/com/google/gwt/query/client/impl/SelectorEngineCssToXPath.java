/*
 * Copyright 2011, The gwtquery team.
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
import com.google.gwt.query.client.js.JsNamedArray;
import com.google.gwt.query.client.js.JsNodeArray;
import com.google.gwt.query.client.js.JsObjectArray;
import com.google.gwt.query.client.js.JsRegexp;
import com.google.gwt.query.client.js.JsUtils;

/**
 * Runtime selector engine implementation which translates selectors to XPath
 * and delegates to document.evaluate(). 
 * It is based on the regular expressions taken from Andrea Giammarchi's Css2Xpath 
 */
public class SelectorEngineCssToXPath extends SelectorEngineImpl {
  
  JsNamedArray<String> cache;
  
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
  
  private static ReplaceCallback rc_scp = new ReplaceCallback() {
    public String foundMatch(ArrayList<String> s) {
      return s.get(1) + s.get(2) + 
        (s.get(3).startsWith(" ") ? "%S%" : s.get(3).startsWith("#") ? "%H%" : "%P%") +
        s.get(4) + s.get(5);
    }
  };
  
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

  public static Object[] regs = new Object[]{
    // scape some dots and spaces
    "(['\\[])([^'\\]]+)([\\s\\.#])([^'\\]]+)(['\\]])", rc_scp,
    // add @ for attrib
    "\\[([^@\\]~\\$\\*\\^\\|\\!]+)(=[^\\]]+)?\\]", "[@$1$2]",
    // multiple queries
    "\\s*,\\s*", "|",
    // , + ~ >
    "\\s*(\\+|~|>)\\s*", "$1",
    //* ~ + >
    "([\\w\\-\\*])~([\\w\\-\\*])", "$1/following-sibling::$2",
    "([\\w\\-\\*])\\+([\\w\\-\\*])", "$1/following-sibling::*[1]/self::$2",
    "([\\w\\-\\*])>([\\w\\-\\*])", "$1/$2",
    // all unescaped stuff escaped
    "\\[([^=]+)=([^'|\"][^\\]]*)\\]", "[$1='$2']",
    // all descendant or self to 
    "(^|[^\\w\\-\\*])(#|\\.)([\\w\\-]+)", "$1*$2$3",
    "([\\>\\+\\|\\~\\,\\s])([a-zA-Z\\*]+)", "$1//$2",
    "\\s+//", "//",
    // :first-child
    "([\\w\\-\\*]+):first-child", "*[1]/self::$1",
    // :last-child
    "([\\w\\-\\*]+):last-child", "$1[not(following-sibling::*)]",
    // :only-child
    "([\\w\\-\\*]+):only-child", "*[last()=1]/self::$1",
    // :empty
    "([\\w\\-\\*]+):empty", "$1[not(*) and not(normalize-space())]",
    "(.+):not\\(([^\\)]*)\\)", rc_Not,
    "([a-zA-Z0-9\\_\\-\\*]+):nth-child\\(([^\\)]*)\\)", rc_nth_child,
    // :contains(selectors)
    ":contains\\(([^\\)]*)\\)", "[contains(string(.),'$1')]",
    // |= attrib
    "\\[([\\w\\-]+)\\|=([^\\]]+)\\]", "[@$1=$2 or starts-with(@$1,concat($2,'-'))]",
    // *= attrib
    "\\[([\\w\\-]+)\\*=([^\\]]+)\\]", "[contains(@$1,$2)]",
    // ~= attrib
    "\\[([\\w\\-]+)~=([^\\]]+)\\]", "[contains(concat(' ',normalize-space(@$1),' '),concat(' ',$2,' '))]",
    // ^= attrib
    "\\[([\\w\\-]+)\\^=([^\\]]+)\\]", "[starts-with(@$1,$2)]",
    // $= attrib
    "\\[([\\w\\-]+)\\$=([^\\]]+)\\]", rc_$Attr,
    // != attrib
    "\\[([\\w\\-]+)\\!=([^\\]]+)\\]", "[not(@$1) or @$1!=$2]",
    // ids and classes
    "#([\\w\\-]+)", "[@id='$1']",
    "\\.([\\w\\-]+)", "[contains(concat(' ',normalize-space(@class),' '),' $1 ')]",
    // normalize multiple filters
    "\\]\\[([^\\]]+)", " and ($1)",
    // tag:pseudo
    ":(enabled)", "[not(@disabled)]",
    ":(checked)", "[@$1='$1']",
    ":(disabled)", "[@$1]",
    // put '*' when tag is omitted
    "(^|\\|)(\\[)", "$1*$2",
    // Replace escaped dots and spaces
    "%S%"," ",
    "%P%",".",
    "%H%","#",
    // Duplicated quotes
    "'+","'",
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
      JsRegexp p = new JsRegexp(r);
      if (o instanceof ReplaceCallback) {
        ReplaceCallback callback = (ReplaceCallback) o;
        while (p.test(s)) {
          JsObjectArray<String> a = p.match(s);
          ArrayList<String> args = new ArrayList<String>();
          for (int i = 0; i < a.length(); i++) {
            args.add(a.get(i));
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
    if (cache == null) {
      cache = JsNamedArray.create();
    }
    String xsel = cache.get(sel);
    if (xsel == null) {
      xsel =  sel.startsWith("./") || sel.startsWith("/") ? sel : css2Xpath(sel);
      cache.put(sel, xsel);
    }
    
    JsNodeArray elm = JsNodeArray.create();
    try {
      SelectorEngine.xpathEvaluate(xsel, ctx, elm);
      return JsUtils.unique(elm.<JsArray<Element>> cast()).cast();    
    } catch (Exception e) {
      System.err.println("ERROR: xpathEvaluate invalid xpath expression:" + xsel + " css-selector:" + sel + "\n\n" + e.getMessage());
      return elm;
    }
  }
}
