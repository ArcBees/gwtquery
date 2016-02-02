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

import static com.google.gwt.query.client.GQuery.console;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.query.client.js.JsNamedArray;
import com.google.gwt.query.client.js.JsNodeArray;
import com.google.gwt.query.client.js.JsUtils;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;

import java.util.ArrayList;

/**
 * Runtime selector engine implementation which translates selectors to XPath
 * and delegates to document.evaluate().
 * It is based on the regular expressions taken from Andrea Giammarchi's Css2Xpath
 */
public class SelectorEngineCssToXPath extends SelectorEngineImpl {

  static JsNamedArray<String> cache;

  /**
   * Interface for callbacks in replaceAll operations.
   */
  public interface ReplaceCallback {
    String foundMatch(ArrayList<String> s);
  }

  /**
   * Interface for replacer implementations (GWT and JVM).
   */
  public interface Replacer {
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
      return "[substring(@" + s.get(1) + ",string-length(@" + s.get(1) + ")-"
          + (s.get(2).replaceAll("'", "").length() - 1) + ")=" + s.get(2) + "]";
    }
  };

  private static ReplaceCallback rc_Not = new ReplaceCallback() {
    public String foundMatch(ArrayList<String> s) {
      return s.get(1) + "[not("
          + getInstance().css2Xpath(s.get(2)).replaceAll("^[^\\[]+\\[([^\\]]*)\\].*$", "$1" + ")]");
    }
  };

  private static ReplaceCallback rc_nth_child = new ReplaceCallback() {
    public String foundMatch(ArrayList<String> s) {
      String s1 = s.get(1);
      String s2 = s.get(2);

      boolean afterAttr = "]".equals(s1);
      String prefix = afterAttr ? s1 : "*";
      boolean noPrefix = afterAttr || s1 == null || s1.length() == 0;

      if ("n".equals(s2)) {
        return s1;
      }
      if ("even".equals(s2)) {
        return prefix + "[position() mod 2=0 and position()>=0]" + (noPrefix ? "" : "/self::" + s1);
      }
      if ("odd".equals(s2)) {
        prefix = afterAttr ? prefix : noPrefix ? "" : s1;
        return prefix + "[(count(preceding-sibling::*) + 1) mod 2=1]";
      }

      if (!s2.contains("n")) {
        return prefix + "[position() = " + s2 + "]" + (noPrefix ? "" : "/self::" + s1);
      }

      String[] t = s2.replaceAll("^([0-9]*)n.*?([0-9]*)?$", "$1+$2").split("\\+");
      String t0 = t[0];
      String t1 = t.length > 1 ? t[1] : "0";
      return prefix + "[(position()-" + t1 + ") mod " + t0 + "=0 and position()>=" + t1 + "]"
          + (noPrefix ? "" : "/self::" + s1);
    }
  };

  public static Object[] regs = new Object[] {
      // scape some dots and spaces
      "(['\\[])([^'\\]]*)([\\s\\.#])([^'\\]]*)(['\\]])", rc_scp,
      // add @ for attrib
      "\\[([^@\\]~\\$\\*\\^\\|\\!]+)(=[^\\]]+)?\\]", "[@$1$2]",
      // multiple queries
      "\\s*,\\s*", "|.//",
      // , + ~ >
      "\\s*(\\+|~|>)\\s*", "$1",
      // * ~ + >
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
      // :odd :even, this is intentional since sizzle behaves so
      ":odd", ":nth-child(even)",
      ":even", ":nth-child(odd)",
      // :not
      "(.+):not\\(([^\\)]*)\\)", rc_Not,
      // :nth-child
      "([a-zA-Z0-9\\_\\-\\*]*|\\]):nth-child\\(([^\\)]*)\\)", rc_nth_child,
      // :contains(selectors)
      ":contains\\(([^\\)]*)\\)", "[contains(string(.),'$1')]",
      // |= attrib
      "\\[([\\w\\-]+)\\|=([^\\]]+)\\]", "[@$1=$2 or starts-with(@$1,concat($2,'-'))]",
      // *= attrib
      "\\[([\\w\\-]+)\\*=([^\\]]+)\\]", "[contains(@$1,$2)]",
      // ~= attrib
      "\\[([\\w\\-]+)~=([^\\]]+)\\]",
      "[contains(concat(' ',normalize-space(@$1),' '),concat(' ',$2,' '))]",
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
      ":(first)", "[1]",
      ":(last)", "[last()]",
      // put '*' when tag is omitted
      "(^|\\|[\\./]*)(\\[)", "$1*$2",
      // Replace escaped dots and spaces
      "%S%", " ",
      "%P%", ".",
      "%H%", "#",
      // Duplicated quotes
      "'+", "'",
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
      RegExp p = RegExp.compile(r);
      if (o instanceof ReplaceCallback) {
        ReplaceCallback callback = (ReplaceCallback) o;
        while (p.test(s)) {
          MatchResult a = p.exec(s);
          ArrayList<String> args = new ArrayList<>();
          for (int i = 0; a != null && i < a.getGroupCount(); i++) {
            args.add(a.getGroup(i));
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

  /**
   * A replacer which works in both sides. Right now gquery JsRegexp is faster
   * than gwt shared RegExp and does not uses HashSet
   */
  public static final Replacer replacerGwt = new Replacer() {
    public String replaceAll(String s, String r, Object o) {
      RegExp p = RegExp.compile(r, "g");
      if (o instanceof ReplaceCallback) {
        ReplaceCallback callback = (ReplaceCallback) o;
        com.google.gwt.regexp.shared.MatchResult a = null;
        while ((a = p.exec(s)) != null) {
          ArrayList<String> args = new ArrayList<>();
          for (int i = 0; i < a.getGroupCount(); i++) {
            args.add(a.getGroup(i));
          }
          String f = callback.foundMatch(args);
          s = s.replace(a.getGroup(0), f);
          p = RegExp.compile(r, "g");
        }
        return s;
      } else {
        return p.replace(s, o.toString());
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
      xsel = sel.startsWith("./") || sel.startsWith("/") ? sel : css2Xpath(sel);
      cache.put(sel, xsel);
    }

    JsNodeArray elm = JsNodeArray.create();
    try {
      SelectorEngine.xpathEvaluate(xsel, ctx, elm);
      return JsUtils.unique(elm.<JsArray<Element>> cast()).cast();
    } catch (Exception e) {
      if (!GWT.isScript()) {
        if (!SelectorEngine.hasXpathEvaluate()) {
          throw new RuntimeException("This Browser does not support Xpath selectors.", e);
        }
        console.error("ERROR: xpathEvaluate invalid xpath expression: "
            + xsel + " css-selector: " + sel + " " + e.getMessage() + "\n");
      }
      return elm;
    }
  }
}
