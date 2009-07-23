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

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.query.client.JSArray;
import com.google.gwt.query.client.Regexp;
import com.google.gwt.query.client.SelectorEngine;

/**
 * Base/Utility class for runtime selector engine implementations.
 */
public abstract class SelectorEngineImpl {

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
    Regexp expressionRegExp = new Regexp(
        "^((odd|even)|([1-9]\\d*)|((([1-9]\\d*)?)n((\\+|\\-)(\\d+))?)|(\\-(([1-9]\\d*)?)n\\+(\\d+)))$");
    JSArray pseudoValue = expressionRegExp.exec(expression);
    if (!SelectorEngine.truth(pseudoValue)) {
      return null;
    } else {
      if (SelectorEngine.truth(pseudoValue.getStr(2))) {        // odd or even
        start = (SelectorEngine.eq(pseudoValue.getStr(2), "odd")) ? 1 : 2;
        modVal = (start == 1) ? 1 : 0;
      } else if (SelectorEngine
          .truth(pseudoValue.getStr(3))) {        // single digit
        start = Integer.parseInt(pseudoValue.getStr(3), 10);
        add = 0;
        max = start;
      } else if (SelectorEngine.truth(pseudoValue.getStr(4))) {        // an+b
        add = SelectorEngine.truth(pseudoValue.getStr(6)) ? Integer
            .parseInt(pseudoValue.getStr(6), 10) : 1;
        start = SelectorEngine.truth(pseudoValue.getStr(7)) ? Integer.parseInt(
            (pseudoValue.getStr(8).charAt(0) == '+' ? ""
                : pseudoValue.getStr(8)) + pseudoValue.getStr(9), 10) : 0;
        while (start < 1) {
          start += add;
        }
        modVal = (start > add) ? (start - add) % add
            : ((start == add) ? 0 : start);
      } else if (SelectorEngine.truth(pseudoValue.getStr(10))) {        // -an+b
        add = SelectorEngine.truth(pseudoValue.getStr(12)) ? Integer
            .parseInt(pseudoValue.getStr(12), 10) : 1;
        start = max = Integer.parseInt(pseudoValue.getStr(13), 10);
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

  /**
   * Parse and execute a given selector expression given a context.
   *
   * @param selector the CSS selector expression
   * @param ctx      the DOM node to use as a context
   * @return a list of matched nodes
   */
  public abstract NodeList<Element> select(String selector, Node ctx);
}
