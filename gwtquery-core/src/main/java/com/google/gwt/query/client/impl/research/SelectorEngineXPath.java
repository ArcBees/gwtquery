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
package com.google.gwt.query.client.impl.research;

import static com.google.gwt.query.client.js.JsUtils.eq;
import static com.google.gwt.query.client.js.JsUtils.truth;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.query.client.impl.SelectorEngine;
import com.google.gwt.query.client.impl.SelectorEngineImpl;
import com.google.gwt.query.client.impl.research.SelectorEngineJS.Sequence;
import com.google.gwt.query.client.impl.research.SelectorEngineJS.SplitRule;
import com.google.gwt.query.client.js.JsNodeArray;
import com.google.gwt.query.client.js.JsObjectArray;
import com.google.gwt.query.client.js.JsRegexp;
import com.google.gwt.query.client.js.JsUtils;

/**
 * Runtime selector engine implementation which translates selectors to XPath
 * and delegates to document.evaluate().
 */
public class SelectorEngineXPath extends SelectorEngineImpl {

  private static String attrToXPath(String match, String p1, String p2,
      String p3) {
    if (eq("^", p2)) {
      return "starts-with(@" + p1 + ", '" + p3 + "')";
    }
    if (eq("$", p2)) {
      return "substring(@" + p1 + ", (string-length(@" + p1 + ") - "
          + (p3.length() - 1) + "), " + p3.length() + ") = '" + p3 + "'";
    }
    if (eq("*", p2)) {
      return "contains(concat(' ', @" + p1 + ", ' '), '" + p3 + "')";
    }
    if (eq("|", p2)) {
      return "(@" + p1 + "='" + p3 + "' or starts-with(@" + p1 + ", '" + p3
          + "-'))";
    }
    if (eq("~", p2)) {
      return "contains(concat(' ', @" + p1 + ", ' '), ' " + p3 + " ')";
    }
    return "@" + p1 + (truth(p3) ? "='" + p3 + "'" : "");
  }

  private JsRegexp cssSelectorRegExp;

  private JsRegexp selectorSplitRegExp;

  private JsRegexp combinator;

  public SelectorEngineXPath() {
  }

  public NodeList<Element> select(String sel, Node ctx) {
    init();
    String selectors[] = sel.replaceAll("\\s*(,)\\s*", "$1").split(",");
    boolean identical = false;
    JsNodeArray elm = JsNodeArray.create();
    for (int a = 0, len = selectors.length; a < len; a++) {
      if (a > 0) {
        identical = false;
        for (int b = 0, bl = a; b < bl; b++) {
          if (eq(selectors[a], selectors[b])) {
            identical = true;
            break;
          }
        }
        if (identical) {
          continue;
        }
      }
      String currentRule = selectors[a];
      JsObjectArray<String> cssSelectors = selectorSplitRegExp.match(currentRule);
      String xPathExpression = ".";
      for (int i = 0, slen = cssSelectors.length(); i < slen; i++) {
        String rule = cssSelectors.get(i);
        JsObjectArray<String> cssSelector = cssSelectorRegExp.exec(rule);
        SplitRule splitRule = new SplitRule(
            !truth(cssSelector.get(1)) || eq(cssSelector.get(3), "*")
                ? "*" : cssSelector.get(1),
            !eq(cssSelector.get(3), "*") ? cssSelector.get(2) : null,
            cssSelector.get(4), cssSelector.get(6),
            cssSelector.get(10), cssSelector.get(22));
        if (truth(splitRule.tagRelation)) {
          if (eq(">", splitRule.tagRelation)) {
            xPathExpression += "/child::";
          } else if (eq("+", splitRule.tagRelation)) {
            xPathExpression += "/following-sibling::*[1]/self::";
          } else if (eq("~", splitRule.tagRelation)) {
            xPathExpression += "/following-sibling::";
          }
        } else {
          xPathExpression +=
              (i > 0 && combinator.test(cssSelectors.get(i - 1)))
                  ? splitRule.tag : ("/descendant::" + splitRule.tag);
        }
        if (truth(splitRule.id)) {
          xPathExpression += "[@id = '" + splitRule.id.replaceAll("^#", "")
              + "']";
        }
        if (truth(splitRule.allClasses)) {
          xPathExpression += splitRule.allClasses
              .replaceAll("\\.([\\w\\u00C0-\\uFFFF\\-_]+)",
                  "[contains(concat(' ', @class, ' '), ' $1 ')]");
        }
        if (truth(splitRule.allAttr)) {
          xPathExpression += replaceAttr(
              JsUtils.or(splitRule.allAttr, ""));
        }
        if (truth(splitRule.allPseudos)) {
          JsRegexp pseudoSplitRegExp = new JsRegexp(
              ":(\\w[\\w\\-]*)(\\(([^\\)]+)\\))?");
          JsRegexp pseudoMatchRegExp = new JsRegexp(
              "(:\\w+[\\w\\-]*)(\\([^\\)]+\\))?", "g");
          JsObjectArray<String> allPseudos = pseudoMatchRegExp.match(splitRule.allPseudos);
          for (int k = 0, kl = allPseudos.length(); k < kl; k++) {
            JsObjectArray<String> pseudo = pseudoSplitRegExp.match(allPseudos.get(k));
            String pseudoClass = truth(pseudo.get(1)) ? pseudo.get(1)
                .toLowerCase() : null;
            String pseudoValue = truth(pseudo.get(3)) ? pseudo.get(3)
                : null;
            String xpath = pseudoToXPath(splitRule.tag, pseudoClass,
                pseudoValue);
            if (xpath.length() > 0) {
              xPathExpression += "[" + xpath + "]";
            }
          }
        }
      }
      SelectorEngine.xpathEvaluate(xPathExpression, ctx, elm);
    }
    return JsUtils.unique(elm.<JsArray<Element>> cast()).cast();
  }

  private void init() {
    if (cssSelectorRegExp == null) {
      cssSelectorRegExp =
          new JsRegexp(
              "^(\\w+)?(#[\\w\\u00C0-\\uFFFF\\-\\_]+|(\\*))?((\\.[\\w\\u00C0-\\uFFFF\\-_]+)*)?((\\[\\w+(\\^|\\$|\\*|\\||~)?(=[\"']*[\\w\\u00C0-\\uFFFF\\s\\-\\_\\.]+[\"']*)?\\]+)*)?(((:\\w+[\\w\\-]*)(\\((odd|even|\\-?\\d*n?((\\+|\\-)\\d+)?|[\\w\\u00C0-\\uFFFF\\-_]+|((\\w*\\.[\\w\\u00C0-\\uFFFF\\-_]+)*)?|(\\[#?\\w+(\\^|\\$|\\*|\\||~)?=?[\\w\\u00C0-\\uFFFF\\s\\-\\_\\.]+\\]+)|(:\\w+[\\w\\-]*))\\))?)*)?(>|\\+|~)?");
      selectorSplitRegExp = new JsRegexp("[^\\s]+", "g");
      combinator = new JsRegexp("(>|\\+|~)");
    }
  }

  private String pseudoToXPath(String tag, String pseudoClass,
      String pseudoValue) {
    String xpath = "";
    if (eq("first-child", pseudoClass)) {
      xpath = "not(preceding-sibling::*)";
    } else if (eq("first-of-type", pseudoClass)) {
      xpath = "not(preceding-sibling::" + tag + ")";
    } else if (eq("last-child", pseudoClass)) {
      xpath = "not(following-sibling::*)";
    } else if (eq("last-of-type", pseudoClass)) {
      xpath = "not(following-sibling::" + tag + ")";
    } else if (eq("only-child", pseudoClass)) {
      xpath = "not(preceding-sibling::* or following-sibling::*)";
    } else if (eq("only-of-type", pseudoClass)) {
      xpath = "not(preceding-sibling::" + tag + " or following-sibling::" + tag
          + ")";
    } else if (eq("nth-child", pseudoClass)) {
      if (!eq("n", pseudoClass)) {
        Sequence sequence = SelectorEngineJS.getSequence(pseudoValue);
        if (sequence != null) {
          if (sequence.start == sequence.max) {
            xpath = "count(preceding-sibling::*) = " + (sequence.start - 1);
          } else {
            xpath = "(count(preceding-sibling::*) + 1) mod " + sequence.add
                + " = " + sequence.modVal
                + ((sequence.start > 1) ? " and count(preceding-sibling::*) >= "
                    + (sequence.start - 1) : "") + ((sequence.max > 0) ?
                    " and count(preceding-sibling::*) <= " + (sequence.max - 1)
                    : "");
          }
        }
      }
    } else if (eq("nth-of-type", pseudoClass)) {
      if (!pseudoValue.startsWith("n")) {
        Sequence sequence = SelectorEngineJS.getSequence(pseudoValue);
        if (sequence != null) {
          if (sequence.start == sequence.max) {
            xpath = pseudoValue;
          } else {
            xpath = "position() mod " + sequence.add + " = " + sequence.modVal
                + ((sequence.start > 1) ? " and position() >= " + sequence.start
                    : "") + ((sequence.max > 0) ? " and position() <= "
                    + sequence.max : "");
          }
        }
      }
    } else if (eq("empty", pseudoClass)) {
      xpath = "count(child::*) = 0 and string-length(text()) = 0";
    } else if (eq("contains", pseudoClass)) {
      xpath = "contains(., '" + pseudoValue + "')";
    } else if (eq("enabled", pseudoClass)) {
      xpath = "not(@disabled)";
    } else if (eq("disabled", pseudoClass)) {
      xpath = "@disabled";
    } else if (eq("checked", pseudoClass)) {
      xpath = "@checked='checked'"; // Doesn't work in Opera 9.24
    } else if (eq("not", pseudoClass)) {
      if (new JsRegexp("^(:\\w+[\\w\\-]*)$").test(pseudoValue)) {
        xpath = "not(" + pseudoToXPath(tag, pseudoValue.substring(1), "") + ")";
      } else {

        pseudoValue = pseudoValue
            .replaceFirst("^\\[#([\\w\\u00C0-\\uFFFF\\-\\_]+)\\]$", "[id=$1]");
        String notSelector = pseudoValue.replaceFirst("^(\\w+)", "self::$1");
        notSelector = notSelector.replaceAll("^\\.([\\w\\u00C0-\\uFFFF\\-_]+)",
            "contains(concat(' ', @class, ' '), ' $1 ')");
        notSelector = replaceAttr2(notSelector);
        xpath = "not(" + notSelector + ")";
      }
    } else {
      xpath = "@" + pseudoClass + "='" + pseudoValue + "'";
    }

    return xpath;
  }

  private native String replaceAttr(String allAttr) /*-{
        if(!allAttr) return "";
        return allAttr.replace(/["']+/g,'').replace(/(\w+)(\^|\$|\*|\||~)?=?([\w\u00C0-\uFFFF\s\-_\.]+)?/g,
            function(a,b,c,d) {
              return @com.google.gwt.query.client.impl.research.SelectorEngineXPath::attrToXPath(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)(a,b || "",c || "",d || "");
            });
    }-*/;

  private native String replaceAttr2(String allAttr) /*-{
        if(!allAttr) return "";
        return allAttr.replace(/\[(\w+)(\^|\$|\*|\||~)?=?([\w\u00C0-\uFFFF\s\-_\.]+)?\]/g, @com.google.gwt.query.client.impl.research.SelectorEngineXPath::attrToXPath(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;));
    }-*/;
}
