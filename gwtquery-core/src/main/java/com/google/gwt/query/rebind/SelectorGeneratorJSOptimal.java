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

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.query.client.Selector;
import com.google.gwt.user.rebind.SourceWriter;

import java.util.regex.Pattern;

/**
 *
 */
public class SelectorGeneratorJSOptimal extends SelectorGeneratorBase {

  static class RuleMatcher {

    public Pattern re;

    public String fnTemplate;

    RuleMatcher(String pat, String fnT) {
      this.re = Pattern.compile(pat);
      this.fnTemplate = fnT;
    }
  }

  protected static Pattern nonSpace = Pattern.compile("\\S/");

  protected static final String trimReStr = "^\\s+|\\s+$";

  protected static Pattern trimRe = Pattern.compile(trimReStr);

  protected static Pattern tplRe = Pattern.compile("\\{(\\d+)\\}");

  protected static Pattern modeRe = Pattern
      .compile("^(\\s?[\\/>+~]\\s?|\\s|$)");

  protected static Pattern tagTokenRe = Pattern
      .compile("^(#)?([a-zA-Z_0-9-\\*]+)");

  protected static Pattern nthRe = Pattern.compile("(\\d*)n\\+?(\\d*)");

  protected static Pattern nthRe2 = Pattern.compile("\\D");

  protected static RuleMatcher[] matchers = new RuleMatcher[] {
      new RuleMatcher("^\\.([a-zA-Z_0-9-]+)",
          "n = byClassName(n, null, \"{0}\");"),
      new RuleMatcher("^\\:([a-zA-Z_0-9-]+)(?:\\(((?:[^ >]*|.*?))\\))?",
          "n = byPseudo(n, \"{0}\", \"{1}\");"), new RuleMatcher(
          "^(?:([\\[\\{])(?:@)?([a-zA-Z_0-9-]+)\\s?(?:(=|.=)\\s?['\"]?(.*?)[\"']?)?[\\]\\}])",
          "n = byAttribute(n, \"{1}\", \"{3}\", \"{2}\", \"{0}\");"),
      new RuleMatcher("^#([a-zA-Z_0-9-]+)", "n = byId(n, null, \"{0}\");")};

  protected void generateMethodBody(SourceWriter sw, JMethod method,
      TreeLogger treeLogger, boolean hasContext)
      throws UnableToCompleteException {

    String selector = method.getAnnotation(Selector.class).value();

    sw.println("return " + wrap(method,
        "impl.select(\"" + selector + "\", root)") + ";");
    //    sw.println("JSArray n = JSArray.create();");
    //    if(!hasContext) {
    //      sw.println("Node root = Document.get();");
    //    }
    //
    //    // add root node as context.
    //    // TODO: support any context
    //    sw.println("n.addNode(root);");
    //    String q = selector, lq = null;
    //    Matcher lmode = modeRe.matcher(q);
    //    Matcher mm = null;
    //    String mode = "";
    //    if (lmode.lookingAt() && notNull(lmode.group(1))) {
    //      mode = lmode.group(1).replaceAll(trimReStr, "").trim();
    //      q = q.replaceFirst("\\Q" + lmode.group(1) + "\\E", "");
    //    }
    //
    //    while (notNull(q) && !q.equals(lq)) {
    //      debug("Doing q=" + q);
    //
    //      lq = q;
    //      Matcher tm = tagTokenRe.matcher(q);
    //      if (tm.lookingAt()) {
    //        if ("#".equals(tm.group(1))) {
    //          sw.println("n = quickId(n, \"" + mode + "\", root, \"" + tm.group(2)
    //              + "\");");
    //        } else {
    //          String tagName = tm.group(2);
    //          tagName = "".equals(tagName) ? "*" : tagName;
    //     //     sw.println("if (n.size() == 0) { n=JSArray.create(); }");
    //          String func = "";
    //          if ("".equals(mode)) {
    //            func = "getDescendentNodes";
    //          } else if (">".equals(mode)) {
    //            func = "getChildNodes";
    //          } else if ("+".equals(mode)) {
    //            func = "getSiblingNodes";
    //          } else if ("~".equals(mode)) {
    //            func = "getGeneralSiblingNodes";
    //          } else {
    //            treeLogger.log(TreeLogger.ERROR, "Error parsing selector, combiner "
    //                + mode + " not recognized in " + selector, null);
    //            throw new UnableToCompleteException();
    //          }
    //          sw.println("n = " + func + "(n, \"" + tagName + "\");");
    //        }
    //        debug("replacing in q, the value " + tm.group(0));
    //        q = q.replaceFirst("\\Q" + tm.group(0) + "\\E", "");
    //      } else {
    //        String func = "";
    //        String tagName = "*";
    //        if ("".equals(mode)) {
    //          func = "getDescendentNodes";
    //        } else if (">".equals(mode)) {
    //          func = "getChildNodes";
    //        } else if ("+".equals(mode)) {
    //          func = "getSiblingNodes";
    //        } else if ("~".equals(mode)) {
    //          func = "getGeneralSiblingNodes";
    //        } else {
    //          treeLogger.log(TreeLogger.ERROR, "Error parsing selector, combiner "
    //              + mode + " not recognized in " + selector, null);
    //          throw new UnableToCompleteException();
    //        }
    //        sw.println("n = " + func + "(n, \"" + tagName + "\");");
    //      }
    //
    //      while (!(mm = modeRe.matcher(q)).lookingAt()) {
    //        debug("Looking at " + q);
    //        boolean matched = false;
    //        for (RuleMatcher rm : matchers) {
    //          Matcher rmm = rm.re.matcher(q);
    //          if (rmm.lookingAt()) {
    //            String res[] = new String[rmm.groupCount()];
    //            for (int i = 1; i <= rmm.groupCount(); i++) {
    //              res[i - 1] = rmm.group(i);
    //              debug("added param " + res[i - 1]);
    //            }
    //            Object[] r = res;
    //            // inline enum, perhaps type-tightening will allow inlined eval()
    //            // call
    //            if (rm.fnTemplate.indexOf("byPseudo") != -1) {
    //              sw.println("n = Pseudo."+res[0].toUpperCase().replace("-", "_") +
    //                  ".eval(n, \""+res[1]+"\");");
    //            } else {
    //              sw.println(MessageFormat.format(rm.fnTemplate, r));
    //            }
    //            q = q.replaceFirst("\\Q" + rmm.group(0) + "\\E", "");
    //            matched = true;
    //            break;
    //          }
    //        }
    //        if (!matched) {
    //          treeLogger
    //              .log(TreeLogger.ERROR, "Error parsing selector at " + q, null);
    //          throw new UnableToCompleteException();
    //        }
    //      }
    //
    //      if (notNull(mm.group(1))) {
    //        mode = mm.group(1).replaceAll(trimReStr, "");
    //        debug("replacing q=" + q + " this part: " + mm.group(1));
    //        q = q.replaceFirst("\\Q" + mm.group(1) + "\\E", "");
    //      }
    //    }
    //    sw.println("return "+wrap(method, "nodup(n)")+";");
  }

  protected String getImplSuffix() {
    return "JS" + super.getImplSuffix();
  }
}
