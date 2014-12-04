package com.google.gwt.query.client;

import com.google.gwt.dom.client.Element;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;

public class Filters {

  private static RegExp simpleAttrFilter = RegExp.compile("\\[([\\w-]+)(\\^|\\$|\\*|\\||~|!)?=?[\"']?([\\w\\u00C0-\\uFFFF\\s\\-_\\.]+)?[\"']?\\]");

  /**
   * Try to parse the selector and return an equivalent {@link Predicate}.
   *
   * @return the {@link Predicate} filter or null if the selector is not supported
   */
  public static Predicate asPredicateFilter(String selector) {
    final MatchResult simpleAttrMatch = simpleAttrFilter.exec(selector);
    if (simpleAttrMatch == null) return null; // non simple attr filter
    final String attrName = simpleAttrMatch.getGroup(1);
    final String matchOp = simpleAttrMatch.getGroup(2);
    final String matchVal = simpleAttrMatch.getGroup(3);
    final char op = matchOp == null || matchOp.length() == 0 ? '0' : matchOp.charAt(0);
    if ("0=^$*|~!".indexOf(op) == -1) return null; // unsupported or illegal operator
    return new Predicate() {
      @Override
      public boolean f(Element e, int index) {
        switch (op) {
          case '0': return e.hasAttribute(attrName);
          case '=': return e.getAttribute(attrName).equals(matchVal);
          case '^': return e.getAttribute(attrName).startsWith(matchVal);
          case '$': return e.getAttribute(attrName).endsWith(matchVal);
          case '*': return e.getAttribute(attrName).contains(matchVal);
          case '|': return (e.getAttribute(attrName) + "-").startsWith(matchVal + "-");
          case '~': return (" " + e.getAttribute(attrName) + " ").contains(" " + matchVal + " ");
          case '!': return !e.getAttribute(attrName).equals(matchVal);
          default: return false;
        }
      }
    };
  }

}
