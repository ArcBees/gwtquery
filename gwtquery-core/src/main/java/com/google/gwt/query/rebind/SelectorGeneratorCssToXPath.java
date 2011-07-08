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
package com.google.gwt.query.rebind;

import java.util.ArrayList;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.query.client.Selector;
import com.google.gwt.query.client.impl.SelectorEngineCssToXPath;
import com.google.gwt.query.client.impl.SelectorEngineCssToXPath.ReplaceCallback;
import com.google.gwt.query.client.impl.SelectorEngineCssToXPath.Replacer;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.rebind.SourceWriter;

/**
 * Compile time selector generator which translates selector into XPath at
 * compile time. It Uses the SelectorEngineCssToXpath to produce the xpath
 * selectors
 */
public class SelectorGeneratorCssToXPath extends SelectorGeneratorBase {

  /**
   * The replacer implementation for the JVM.
   */
  public static final Replacer replacerJvm = new Replacer() {
    public String replaceAll(String s, String r, Object o) {
      Pattern p = Pattern.compile(r);
      if (o instanceof ReplaceCallback) {
        final Matcher matcher = p.matcher(s);
        ReplaceCallback callback = (ReplaceCallback) o;
        while (matcher.find()) {
          final MatchResult matchResult = matcher.toMatchResult();
          ArrayList<String> argss = new ArrayList<String>();
          for (int i = 0; i < matchResult.groupCount() + 1; i++) {
            argss.add(matchResult.group(i));
          }
          final String replacement = callback.foundMatch(argss);
          s = s.substring(0, matchResult.start()) + replacement
              + s.substring(matchResult.end());
          matcher.reset(s);
        }
        return s;
      } else {
        return p.matcher(s).replaceAll(o.toString());
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
          ArrayList<String> args = new ArrayList<String>();
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
  
  public static final Replacer replacer = replacerGwt;

  private SelectorEngineCssToXPath engine = new SelectorEngineCssToXPath(
      replacer);

  protected String css2Xpath(String s) {
    return engine.css2Xpath(s);
  }

  private XPathFactory factory = XPathFactory.newInstance();
  private XPath xpath = factory.newXPath();

  protected void generateMethodBody(SourceWriter sw, JMethod method,
      TreeLogger treeLogger, boolean hasContext)
      throws UnableToCompleteException {

    String selector = method.getAnnotation(Selector.class).value();
    String xselector = css2Xpath(selector);

    // Validate the generated xpath selector.
    try {
      validateXpath(xselector);
    } catch (XPathExpressionException e1) {
      System.err.println("Invalid XPath generated selector, please revise it: " + xselector);
      if (!selector.equals(xselector)) {
        System.err.println("If your css2 selector syntax is correct, open an issue in the gwtquery project. cssselector:"
            + selector + " xpath:" + xselector);
      }
      throw new UnableToCompleteException();
    }

    sw.println("return "
        + wrap(method, "SelectorEngine.xpathEvaluate(\"" + xselector
            + "\", root)") + ";");
  }
  
  public void validateXpath(String xselector) throws XPathExpressionException {
    xpath.compile(xselector);
  }

  protected String getImplSuffix() {
    return "CssToXPath" + super.getImplSuffix();
  }
}
