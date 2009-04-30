package com.google.gwt.query.rebind;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.user.rebind.SourceWriter;
import com.google.gwt.query.client.Selector;

/**
 * Compile time selector generator which delegates to native browser
 * methods.
 */
public class SelectorGeneratorNative extends SelectorGeneratorBase {

  protected String getImplSuffix() {
    return "Native" + super.getImplSuffix();
  }

  protected void generateMethodBody(SourceWriter sw, JMethod method,
      TreeLogger treeLogger, boolean hasContext)
      throws UnableToCompleteException {
    String selector = method.getAnnotation(Selector.class).value();
    if (!hasContext) {
      sw.println("return "
          + wrap(method, "querySelectorAll(\"" + selector + "\"") + ");");
    } else {
      sw.println("return "
          + wrap(method, "querySelectorAll(\"" + selector + "\", root)")
          + ");");
    }
  }
}