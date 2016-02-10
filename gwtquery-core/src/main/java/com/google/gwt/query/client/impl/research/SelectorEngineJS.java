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

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.query.client.impl.SelectorEngine;
import com.google.gwt.query.client.impl.SelectorEngineImpl;
import com.google.gwt.query.client.js.JsNodeArray;
import com.google.gwt.query.client.js.JsObjectArray;
import com.google.gwt.query.client.js.JsRegexp;
import com.google.gwt.query.client.js.JsUtils;

/**
 * Runtime selector engine implementation with no-XPath/native support based on
 * DOMAssistant.
 */
public class SelectorEngineJS extends SelectorEngineImpl {

  /**
   * Internal class.
   */
  protected static class Sequence {

    public int start;

    public int max;

    public int add;

    public int modVal;
  }

  /**
   * Internal class.
   */
  protected static class SplitRule {

    public String tag;

    public String id;

    public String allClasses;

    public String allAttr;

    public String allPseudos;

    public String tagRelation;

    public SplitRule(String tag, String id, String allClasses, String allAttr,
        String allPseudos) {
      this.tag = tag;
      this.id = id;
      this.allClasses = allClasses;
      this.allAttr = allAttr;
      this.allPseudos = allPseudos;
    }

    public SplitRule(String tag, String id, String allClasses, String allAttr,
        String allPseudos, String tagRelation) {
      this.tag = tag;
      this.id = id;
      this.allClasses = allClasses;
      this.allAttr = allAttr;
      this.allPseudos = allPseudos;
      this.tagRelation = tagRelation;
    }
  }

  protected static Sequence getSequence(String expression) {
    int start = 0, add = 2, max = -1, modVal = -1;
    JsRegexp expressionRegExp =
        new JsRegexp(
            "^((odd|even)|([1-9]\\d*)|((([1-9]\\d*)?)n((\\+|\\-)(\\d+))?)|(\\-(([1-9]\\d*)?)n\\+(\\d+)))$");
    JsObjectArray<String> pseudoValue = expressionRegExp.exec(expression);
    if (!truth(pseudoValue)) {
      return null;
    } else {
      if (truth(pseudoValue.get(2))) { // odd or even
        start = eq(pseudoValue.get(2), "odd") ? 1 : 2;
        modVal = (start == 1) ? 1 : 0;
      } else if (JsUtils
          .truth(pseudoValue.get(3))) { // single digit
        start = Integer.parseInt(pseudoValue.get(3), 10);
        add = 0;
        max = start;
      } else if (truth(pseudoValue.get(4))) { // an+b
        add = truth(pseudoValue.get(6)) ? Integer
            .parseInt(pseudoValue.get(6), 10) : 1;
        start = truth(pseudoValue.get(7)) ? Integer.parseInt(
            (pseudoValue.get(8).charAt(0) == '+' ? ""
                : pseudoValue.get(8)) + pseudoValue.get(9), 10) : 0;
        while (start < 1) {
          start += add;
        }
        modVal = (start > add) ? (start - add) % add
            : ((start == add) ? 0 : start);
      } else if (truth(pseudoValue.get(10))) { // -an+b
        add = truth(pseudoValue.get(12)) ? Integer
            .parseInt(pseudoValue.get(12), 10) : 1;
        start = max = Integer.parseInt(pseudoValue.get(13), 10);
        while (start > add) {
          start -= add;
        }
        modVal = (max > add) ? (max - add) % add : ((max == add) ? 0 : max);
      }
    }
    Sequence s = new Sequence();
    s.start = start;
    s.add = add;
    s.max = max;
    s.modVal = modVal;
    return s;
  }

  public static void clearAdded(JsNodeArray a) {
    for (int i = 0, len = a.size(); i < len; i++) {
      clearAdded(a.getNode(i));
    }
  }

  public static native void clearAdded(Node node) /*-{
      node.added = null;
    }-*/;

  public static native NodeList<Element> getElementsByClassName(String clazz,
      Node ctx) /*-{
  return ctx.getElementsByClassName(clazz);
  }-*/;

  public static native boolean isAdded(Node prevRef) /*-{
      return prevRef.added || false;
    }-*/;

  public static native void setAdded(Node prevRef, boolean added) /*-{
      prevRef.added = added;
    }-*/;

  public static native void setSkipTag(JsNodeArray prevElem, boolean skip) /*-{
      prevElem.skipTag = skip;
    }-*/;

  private static String attrToRegExp(String attrVal, String op) {
    if (JsUtils.eq("^", op)) {
      return "^" + attrVal;
    }
    if (JsUtils.eq("$", op)) {
      return attrVal + "$";
    }
    if (JsUtils.eq("*", op)) {
      return attrVal;
    }
    if (JsUtils.eq("|", op)) {
      return "(^" + attrVal + "(\\-\\w+)*$)";
    }
    if (JsUtils.eq("~", op)) {
      return "\\b" + attrVal + "\\b";
    }
    return JsUtils.truth(attrVal) ? "^" + attrVal + "$" : null;
  }

  private static native boolean checked(Node previous) /*-{
    return previous.checked || false;
  }-*/;

  private static void clearChildElms(JsNodeArray prevParents) {
    for (int n = 0, nl = prevParents.size(); n < nl; n++) {
      setHasChildElms(prevParents.getNode(n), false);
    }
  }

  private static native boolean enabled(Node node) /*-{
    return !node.disabled;
  }-*/;

  private static void getDescendantNodes(JsNodeArray matchingElms,
      String nextTagStr, Node prevRef) {
    NodeList<Element> children = getElementsByTagName(nextTagStr, prevRef);
    for (int k = 0, klen = children.getLength(); k < klen; k++) {
      Node child = children.getItem(k);
      if (child.getParentNode() == prevRef) {
        matchingElms.addNode(child);
      }
    }
  }

  private static NodeList<Element> getElementsByTagName(String tag, Node ctx) {
    if (ctx == null) {
      return JavaScriptObject.createArray().cast();
    }
    return ((Element) ctx).getElementsByTagName(tag);
  }

  private static void getGeneralSiblingNodes(JsNodeArray matchingElms,
      JsObjectArray<String> nextTag, JsRegexp nextRegExp, Node prevRef) {
    while (JsUtils.truth(prevRef = SelectorEngine.getNextSibling(prevRef))
        && !isAdded(prevRef)) {
      if (!JsUtils.truth(nextTag) || nextRegExp
          .test(prevRef.getNodeName())) {
        setAdded(prevRef, true);
        matchingElms.addNode(prevRef);
      }
    }
  }

  private static void getSiblingNodes(JsNodeArray matchingElms, JsObjectArray<String> nextTag,
      JsRegexp nextRegExp, Node prevRef) {
    while (JsUtils.truth(prevRef = SelectorEngine.getNextSibling(prevRef))
        && prevRef.getNodeType() != Node.ELEMENT_NODE) {
    }
    if (JsUtils.truth(prevRef)) {
      if (!JsUtils.truth(nextTag) || nextRegExp
          .test(prevRef.getNodeName())) {
        matchingElms.addNode(prevRef);
      }
    }
  }

  private static native boolean hasChildElms(Node prevParent) /*-{
      return prevParent.childElms || false;
    }-*/;

  private static native boolean isSkipped(JsNodeArray prevElem) /*-{
       return prevElem.skipTag || false;
    }-*/;

  private static native void setHasChildElms(Node prevParent, boolean bool) /*-{
      prevParent.childElms = bool ? bool : null;
    }-*/;

  private static native JsNodeArray subtractArray(JsNodeArray previousMatch,
      JsNodeArray elementsByPseudo) /*-{
  for (var i=0, src1; (src1=arr1[i]); i++) {
    var found = false;
    for (var j=0, src2; (src2=arr2[j]); j++) {
      if (src2 === src1) {
    found = true;
        break;
      }
    }
    if (found) {
      arr1.splice(i--, 1);
    }
  }
  return arr;
  }-*/;

  private JsRegexp cssSelectorRegExp;

  private JsRegexp selectorSplitRegExp;

  private JsRegexp childOrSiblingRefRegExp;

  public SelectorEngineJS() {
    selectorSplitRegExp = new JsRegexp("[^\\s]+", "g");
    childOrSiblingRefRegExp = new JsRegexp("^(>|\\+|~)$");
    cssSelectorRegExp =
        new JsRegexp(
            "^(\\w+)?(#[\\w\\u00C0-\\uFFFF\\-\\_]+|(\\*))?((\\.[\\w\\u00C0-\\uFFFF\\-_]+)*)?((\\[\\w+(\\^|\\$|\\*|\\||~)?(=[\"']*[\\w\\u00C0-\\uFFFF\\s\\-\\_\\.]+[\"']*)?\\]+)*)?(((:\\w+[\\w\\-]*)(\\((odd|even|\\-?\\d*n?((\\+|\\-)\\d+)?|[\\w\\u00C0-\\uFFFF\\-_]+|((\\w*\\.[\\w\\u00C0-\\uFFFF\\-_]+)*)?|(\\[#?\\w+(\\^|\\$|\\*|\\||~)?=?[\\w\\u00C0-\\uFFFF\\s\\-\\_\\.]+\\]+)|(:\\w+[\\w\\-]*))\\))?)*)?");
  }

  public NodeList<Element> select(String sel, Node ctx) {
    String selectors[] = sel.replace("\\s*(,)\\s*", "$1").split(",");
    boolean identical = false;
    JsNodeArray elm = JsNodeArray.create();
    for (int a = 0, len = selectors.length; a < len; a++) {
      if (a > 0) {
        identical = false;
        for (int b = 0, bl = a; b < bl; b++) {
          if (JsUtils.eq(selectors[a], selectors[b])) {
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
      JsNodeArray prevElem = JsNodeArray.create(ctx);
      for (int i = 0, slen = cssSelectors.length(); i < slen; i++) {
        JsNodeArray matchingElms = JsNodeArray.create();
        String rule = cssSelectors.get(i);
        if (i > 0 && childOrSiblingRefRegExp.test(rule)) {
          JsObjectArray<String> childOrSiblingRef = childOrSiblingRefRegExp.exec(rule);
          if (JsUtils.truth(childOrSiblingRef)) {
            JsObjectArray<String> nextTag = new JsRegexp("^\\w+")
                .exec(cssSelectors.get(i + 1));
            JsRegexp nextRegExp = null;
            String nextTagStr = null;
            if (JsUtils.truth(nextTag)) {
              nextTagStr = nextTag.get(0);
              nextRegExp = new JsRegexp("(^|\\s)" + nextTagStr + "(\\s|$)", "i");
            }
            for (int j = 0, jlen = prevElem.size(); j < jlen; j++) {
              Node prevRef = prevElem.getNode(j);
              String ref = childOrSiblingRef.get(0);
              if (JsUtils.eq(">", ref)) {
                getDescendantNodes(matchingElms, nextTagStr, prevRef);
              } else if (JsUtils.eq("+", ref)) {
                getSiblingNodes(matchingElms, nextTag, nextRegExp, prevRef);
              } else if (JsUtils.eq("~", ref)) {
                getGeneralSiblingNodes(matchingElms, nextTag, nextRegExp,
                    prevRef);
              }
            }
            prevElem = matchingElms;
            clearAdded(prevElem);
            rule = cssSelectors.get(++i);
            if (new JsRegexp("^\\w+$").test(rule)) {
              continue;
            }
            setSkipTag(prevElem, true);
          }
        }
        JsObjectArray<String> cssSelector = cssSelectorRegExp.exec(rule);
        SplitRule splitRule = new SplitRule(
            !JsUtils.truth(cssSelector.get(1)) || JsUtils
                .eq(cssSelector.get(3), "*") ? "*" : cssSelector.get(1),
            !JsUtils.eq(cssSelector.get(3), "*") ? cssSelector
                .get(2) : null, cssSelector.get(4), cssSelector.get(6),
            cssSelector.get(10));
        if (JsUtils.truth(splitRule.id)) {
          Element domelem = Document.get()
              .getElementById(splitRule.id.substring(1));
          if (JsUtils.truth(domelem)) {
            matchingElms = JsNodeArray.create(domelem);
          }
          prevElem = matchingElms;
        } else if (JsUtils.truth(splitRule.tag) && !isSkipped(
            prevElem)) {
          if (i == 0 && matchingElms.size() == 0 && prevElem.size() == 1) {
            prevElem = matchingElms = JsNodeArray.create(
                getElementsByTagName(splitRule.tag, prevElem.getNode(0)));
          } else {
            NodeList<Element> tagCollectionMatches;
            for (int l = 0, ll = prevElem.size(); l < ll; l++) {
              tagCollectionMatches = getElementsByTagName(splitRule.tag,
                  prevElem.getNode(l));
              for (int m = 0, mlen = tagCollectionMatches.getLength(); m < mlen; m++) {
                Node tagMatch = tagCollectionMatches.getItem(m);

                if (!isAdded(tagMatch)) {
                  setAdded(tagMatch, true);
                  matchingElms.addNode(tagMatch);
                }
              }
            }
            prevElem = matchingElms;
            clearAdded(prevElem);
          }
          if (matchingElms.size() == 0) {
            break;
          }
          setSkipTag(prevElem, false);
          if (JsUtils.truth(splitRule.allClasses)) {
            String[] allClasses = splitRule.allClasses.replaceFirst("^\\.", "")
                .split("\\.");
            JsRegexp[] regExpClassNames = new JsRegexp[allClasses.length];
            for (int n = 0, nl = allClasses.length; n < nl; n++) {
              regExpClassNames[n] = new JsRegexp(
                  "(^|\\s)" + allClasses[n] + "(\\s|$)");
            }
            JsNodeArray matchingClassElms = JsNodeArray.create();
            for (int o = 0, olen = prevElem.size(); o < olen; o++) {
              Element current = prevElem.getElement(o);
              String elmClass = current.getClassName();
              boolean addElm = false;
              if (JsUtils.truth(elmClass) && !isAdded(current)) {
                for (int p = 0, pl = regExpClassNames.length; p < pl; p++) {
                  addElm = regExpClassNames[p].test(elmClass);
                  if (!addElm) {
                    break;
                  }
                }
                if (addElm) {
                  setAdded(current, true);
                  matchingClassElms.addNode(current);
                }
              }
            }
            clearAdded(prevElem);
            prevElem = matchingElms = matchingClassElms;
          }
          if (JsUtils.truth(splitRule.allAttr)) {
            JsObjectArray<String> allAttr = JsRegexp
                .match("\\[[^\\]]+\\]", "g", splitRule.allAttr);
            JsRegexp[] regExpAttributes = new JsRegexp[allAttr.length()];
            String[] regExpAttributesStr = new String[allAttr.length()];
            JsRegexp attributeMatchRegExp = new JsRegexp(
                "(\\w+)(\\^|\\$|\\*|\\||~)?=?[\"']?([\\w\u00C0-\uFFFF\\s\\-_\\.]+)?");
            for (int q = 0, ql = allAttr.length(); q < ql; q++) {
              JsObjectArray<String> attributeMatch = attributeMatchRegExp
                  .exec(allAttr.get(q));
              String attributeValue =
                  JsUtils.truth(attributeMatch.get(3))
                      ? attributeMatch.get(3).replaceAll("\\.", "\\.")
                      : null;
              String attrVal = attrToRegExp(attributeValue,
                  JsUtils.or(attributeMatch.get(2), null));
              regExpAttributes[q] = JsUtils.truth(attrVal) ? new JsRegexp(
                  attrVal) : null;
              regExpAttributesStr[q] = attributeMatch.get(1);
            }
            JsNodeArray matchingAttributeElms = JsNodeArray.create();

            for (int r = 0, rlen = matchingElms.size(); r < rlen; r++) {
              Element current = matchingElms.getElement(r);
              boolean addElm = false;
              for (int s = 0, sl = regExpAttributes.length; s < sl; s++) {
                addElm = false;
                JsRegexp attributeRegexp = regExpAttributes[s];
                String currentAttr = getAttr(current, regExpAttributesStr[s]);
                if (JsUtils.truth(currentAttr)
                    && currentAttr.length() != 0) {
                  if (attributeRegexp == null || attributeRegexp
                      .test(currentAttr)) {
                    addElm = true;
                  }
                }
                if (!addElm) {
                  break;
                }
              }
              if (addElm) {
                matchingAttributeElms.addNode(current);
              }
            }
            prevElem = matchingElms = matchingAttributeElms;
          }
          if (JsUtils.truth(splitRule.allPseudos)) {
            JsRegexp pseudoSplitRegExp = new JsRegexp(
                ":(\\w[\\w\\-]*)(\\(([^\\)]+)\\))?");

            JsObjectArray<String> allPseudos = JsRegexp
                .match("(:\\w+[\\w\\-]*)(\\([^\\)]+\\))?", "g",
                    splitRule.allPseudos);
            for (int t = 0, tl = allPseudos.length(); t < tl; t++) {
              JsObjectArray<String> pseudo = pseudoSplitRegExp.match(allPseudos.get(t));
              String pseudoClass = JsUtils.truth(pseudo.get(1))
                  ? pseudo.get(1).toLowerCase() : null;
              String pseudoValue = JsUtils.truth(pseudo.get(3))
                  ? pseudo.get(3) : null;
              matchingElms = getElementsByPseudo(matchingElms, pseudoClass,
                  pseudoValue);
              clearAdded(matchingElms);
            }
            prevElem = matchingElms;
          }
        }
      }
      elm.pushAll(prevElem);
    }

    return JsUtils.unique(elm.<JsArray<Element>> cast()).cast();
  }

  protected String getAttr(Element current, String name) {
    return current.getAttribute(name);
  }

  private void getCheckedPseudo(JsNodeArray previousMatch, JsNodeArray matchingElms) {
    Node previous;
    for (int q = 0, qlen = previousMatch.size(); q < qlen; q++) {
      previous = previousMatch.getNode(q);
      if (checked(previous)) {
        matchingElms.addNode(previous);
      }
    }
  }

  private void getContainsPseudo(JsNodeArray previousMatch, String pseudoValue,
      JsNodeArray matchingElms) {
    Node previous;
    for (int q = 0, qlen = previousMatch.size(); q < qlen; q++) {
      previous = previousMatch.getNode(q);
      if (!isAdded(previous)) {
        if (((Element) previous).getInnerText().contains(pseudoValue)) {
          setAdded(previous, true);
          matchingElms.addNode(previous);
        }
      }
    }
  }

  private void getDefaultPseudo(JsNodeArray previousMatch, String pseudoClass,
      String pseudoValue, JsNodeArray matchingElms) {
    Node previous;
    for (int w = 0, wlen = previousMatch.size(); w < wlen; w++) {
      previous = previousMatch.getElement(w);
      if (JsUtils
          .eq(((Element) previous).getAttribute(pseudoClass), pseudoValue)) {
        matchingElms.addNode(previous);
      }
    }
  }

  private void getDisabledPseudo(JsNodeArray previousMatch, JsNodeArray matchingElms) {
    Node previous;
    for (int q = 0, qlen = previousMatch.size(); q < qlen; q++) {
      previous = previousMatch.getNode(q);
      if (!enabled(previous)) {
        matchingElms.addNode(previous);
      }
    }
  }

  private JsNodeArray getElementsByPseudo(JsNodeArray previousMatch, String pseudoClass,
      String pseudoValue) {
    JsNodeArray prevParents = JsNodeArray.create();
    boolean previousDir = pseudoClass.startsWith("first") ? true : false;
    JsNodeArray matchingElms = JsNodeArray.create();
    if (JsUtils.eq("first-child", pseudoClass) || JsUtils
        .eq("last-child", pseudoClass)) {
      getFirstChildPseudo(previousMatch, previousDir, matchingElms);
    } else if (JsUtils.eq("only-child", pseudoClass)) {
      getOnlyChildPseudo(previousMatch, matchingElms);
    } else if (JsUtils.eq("nth-child", pseudoClass)) {
      matchingElms = getNthChildPseudo(previousMatch, pseudoValue, prevParents,
          matchingElms);
    } else if (JsUtils.eq("first-of-type", pseudoClass) || JsUtils
        .eq("last-of-type", pseudoClass)) {
      getFirstOfTypePseudo(previousMatch, previousDir, matchingElms);
    } else if (JsUtils.eq("only-of-type", pseudoClass)) {
      getOnlyOfTypePseudo(previousMatch, matchingElms);
    } else if (JsUtils.eq("nth-of-type", pseudoClass)) {
      matchingElms = getNthOfTypePseudo(previousMatch, pseudoValue, prevParents,
          matchingElms);
    } else if (JsUtils.eq("empty", pseudoClass)) {
      getEmptyPseudo(previousMatch, matchingElms);
    } else if (JsUtils.eq("enabled", pseudoClass)) {
      getEnabledPseudo(previousMatch, matchingElms);
    } else if (JsUtils.eq("disabled", pseudoClass)) {
      getDisabledPseudo(previousMatch, matchingElms);
    } else if (JsUtils.eq("checked", pseudoClass)) {
      getCheckedPseudo(previousMatch, matchingElms);
    } else if (JsUtils.eq("contains", pseudoClass)) {
      getContainsPseudo(previousMatch, pseudoValue, matchingElms);
    } else if (JsUtils.eq("not", pseudoClass)) {
      matchingElms = getNotPseudo(previousMatch, pseudoValue, matchingElms);
    } else {
      getDefaultPseudo(previousMatch, pseudoClass, pseudoValue, matchingElms);
    }
    return matchingElms;
  }

  private void getEmptyPseudo(JsNodeArray previousMatch, JsNodeArray matchingElms) {
    Node previous;
    for (int q = 0, qlen = previousMatch.size(); q < qlen; q++) {
      previous = previousMatch.getNode(q);
      if (!previous.hasChildNodes()) {
        matchingElms.addNode(previous);
      }
    }
  }

  private void getEnabledPseudo(JsNodeArray previousMatch, JsNodeArray matchingElms) {
    Node previous;
    for (int q = 0, qlen = previousMatch.size(); q < qlen; q++) {
      previous = previousMatch.getNode(q);
      if (enabled(previous)) {
        matchingElms.addNode(previous);
      }
    }
  }

  private void getFirstChildPseudo(JsNodeArray previousMatch, boolean previousDir,
      JsNodeArray matchingElms) {
    Node next;
    Node previous;
    for (int j = 0, jlen = previousMatch.size(); j < jlen; j++) {
      previous = next = previousMatch.getElement(j);
      if (previousDir) {
        while (JsUtils
            .truth(next = SelectorEngine.getPreviousSibling(next))
            && next.getNodeType() != Node.ELEMENT_NODE) {
        }
      } else {
        while (JsUtils.truth(next = SelectorEngine.getNextSibling(next))
            && next.getNodeType() != Node.ELEMENT_NODE) {
        }
      }
      if (!JsUtils.truth(next)) {
        matchingElms.addNode(previous);
      }
    }
  }

  private void getFirstOfTypePseudo(JsNodeArray previousMatch, boolean previousDir,
      JsNodeArray matchingElms) {
    Node previous;
    Node next;
    for (int n = 0, nlen = previousMatch.size(); n < nlen; n++) {
      next = previous = previousMatch.getNode(n);

      if (previousDir) {
        while (JsUtils.truth(next = SelectorEngine.getPreviousSibling(next))
            && !JsUtils
                .eq(next.getNodeName(), previous.getNodeName())) {
        }
      } else {
        while (JsUtils.truth(next = SelectorEngine.getNextSibling(next))
            && !JsUtils.eq(next.getNodeName(), previous.getNodeName())) {
        }
      }

      if (!JsUtils.truth(next)) {
        matchingElms.addNode(previous);
      }
    }
  }

  private JsNodeArray getNotPseudo(JsNodeArray previousMatch, String pseudoValue,
      JsNodeArray matchingElms) {
    if (new JsRegexp("(:\\w+[\\w\\-]*)$").test(pseudoValue)) {
      matchingElms = subtractArray(previousMatch,
          getElementsByPseudo(previousMatch, pseudoValue.substring(1), ""));
    } else {
      pseudoValue = pseudoValue
          .replace("^\\[#([\\w\\u00C0-\\uFFFF\\-\\_]+)\\]$", "[id=$1]");
      JsObjectArray<String> notTag = new JsRegexp("^(\\w+)").exec(pseudoValue);
      JsObjectArray<String> notClass = new JsRegexp("^\\.([\\w\u00C0-\uFFFF\\-_]+)")
          .exec(pseudoValue);
      JsObjectArray<String> notAttr = new JsRegexp(
          "\\[(\\w+)(\\^|\\$|\\*|\\||~)?=?([\\w\\u00C0-\\uFFFF\\s\\-_\\.]+)?\\]")
          .exec(pseudoValue);
      JsRegexp notRegExp = new JsRegexp("(^|\\s)"
          + (JsUtils.truth(notTag) ? notTag.get(1)
              : JsUtils.truth(notClass) ? notClass.get(1) : "")
          + "(\\s|$)", "i");
      if (JsUtils.truth(notAttr)) {
        String notAttribute = JsUtils.truth(notAttr.get(3)) ? notAttr
            .get(3).replace("\\.", "\\.") : null;
        String notMatchingAttrVal = attrToRegExp(notAttribute,
            notAttr.get(2));
        notRegExp = new JsRegexp(notMatchingAttrVal, "i");
      }
      for (int v = 0, vlen = previousMatch.size(); v < vlen; v++) {
        Element notElm = previousMatch.getElement(v);
        Element addElm = null;
        if (JsUtils.truth(notTag) && !notRegExp
            .test(notElm.getNodeName())) {
          addElm = notElm;
        } else if (JsUtils.truth(notClass) && !notRegExp
            .test(notElm.getClassName())) {
          addElm = notElm;
        } else if (JsUtils.truth(notAttr)) {
          String att = getAttr(notElm, notAttr.get(1));
          if (!JsUtils.truth(att) || !notRegExp.test(att)) {
            addElm = notElm;
          }
        }
        if (JsUtils.truth(addElm) && !isAdded(addElm)) {
          setAdded(addElm, true);
          matchingElms.addNode(addElm);
        }
      }
    }
    return matchingElms;
  }

  private JsNodeArray getNthChildPseudo(JsNodeArray previousMatch, String pseudoValue,
      JsNodeArray prevParents, JsNodeArray matchingElms) {
    Node previous;
    if (JsUtils.eq(pseudoValue, "n")) {
      matchingElms = previousMatch;
    } else {
      Sequence sequence = getSequence(pseudoValue);
      if (sequence != null) {
        for (int l = 0, llen = previousMatch.size(); l < llen; l++) {
          previous = previousMatch.getNode(l);
          Node prevParent = previous.getParentNode();
          if (!hasChildElms(prevParent)) {
            int iteratorNext = sequence.start;
            int childCount = 0;
            Node childElm = prevParent.getFirstChild();
            while (childElm != null && (sequence.max < 0
                || iteratorNext <= sequence.max)) {
              if (childElm.getNodeType() == Node.ELEMENT_NODE) {
                if (++childCount == iteratorNext) {
                  if (JsUtils
                      .eq(childElm.getNodeName(), previous.getNodeName())) {
                    matchingElms.addNode(childElm);
                  }
                  iteratorNext += sequence.add;
                }
              }
              childElm = SelectorEngine.getNextSibling(childElm);
            }
            setHasChildElms(prevParent, true);
            prevParents.addNode(prevParent);
          }
        }
        clearChildElms(prevParents);
      }
    }
    return matchingElms;
  }

  private JsNodeArray getNthOfTypePseudo(JsNodeArray previousMatch, String pseudoValue,
      JsNodeArray prevParents, JsNodeArray matchingElms) {
    Node previous;
    if (pseudoValue.startsWith("n")) {
      matchingElms = previousMatch;
    } else {
      Sequence sequence = getSequence(pseudoValue);
      if (sequence != null) {
        for (int p = 0, plen = previousMatch.size(); p < plen; p++) {
          previous = previousMatch.getNode(p);
          Node prevParent = previous.getParentNode();
          if (!hasChildElms(prevParent)) {
            int iteratorNext = sequence.start;
            int childCount = 0;
            Node childElm = prevParent.getFirstChild();
            while (JsUtils.truth(childElm) && (sequence.max < 0
                || iteratorNext <= sequence.max)) {
              if (JsUtils
                  .eq(childElm.getNodeName(), previous.getNodeName())) {
                if (++childCount == iteratorNext) {
                  matchingElms.addNode(childElm);
                  iteratorNext += sequence.add;
                }
              }
              childElm = SelectorEngine.getNextSibling(childElm);
            }
            setHasChildElms(prevParent, true);
            prevParents.addNode(prevParent);
          }
        }
        clearChildElms(prevParents);
      }
    }
    return matchingElms;
  }

  private void getOnlyChildPseudo(JsNodeArray previousMatch, JsNodeArray matchingElms) {
    Node previous;
    Node next;
    Node prev;
    Node kParent = null;
    for (int k = 0, klen = previousMatch.size(); k < klen; k++) {
      prev = next = previous = previousMatch.getNode(k);
      Node prevParent = previous.getParentNode();
      if (prevParent != kParent) {
        while (JsUtils.truth(prev = SelectorEngine.getPreviousSibling(prev))
            && prev.getNodeType() != Node.ELEMENT_NODE) {
        }
        while (JsUtils.truth(next = SelectorEngine.getNextSibling(next))
            && next.getNodeType() != Node.ELEMENT_NODE) {
        }
        if (!JsUtils.truth(prev) && !JsUtils.truth(next)) {
          matchingElms.addNode(previous);
        }
        kParent = prevParent;
      }
    }
  }

  private void getOnlyOfTypePseudo(JsNodeArray previousMatch,
      JsNodeArray matchingElms) {
    Node previous;
    Node next;
    Node prev;
    Node oParent = null;
    for (int o = 0, olen = previousMatch.size(); o < olen; o++) {
      prev = next = previous = previousMatch.getNode(o);
      Node prevParent = previous.getParentNode();
      if (prevParent != oParent) {
        while (JsUtils.truth(prev = SelectorEngine.getPreviousSibling(prev))
            && !JsUtils
                .eq(prev.getNodeName(), previous.getNodeName())) {
        }
        while (JsUtils.truth(next = SelectorEngine.getNextSibling(next))
            && !JsUtils.eq(next.getNodeName(), previous.getNodeName())) {
        }
        if (!JsUtils.truth(prev) && !JsUtils.truth(next)) {
          matchingElms.addNode(previous);
        }
        oParent = prevParent;
      }
    }
  }
}
