package com.google.gwt.query.rebind;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.query.client.Selector;
import com.google.gwt.user.rebind.SourceWriter;

/**
 * An implementaton of pure-JS compile time selectors. This implementation
 * simply defers to the runtime selector engine.
 */
public class SelectorGeneratorJS extends SelectorGeneratorBase {

  protected String getImplSuffix() {
    return "JS" + super.getImplSuffix();
  }

  protected void generateMethodBody(SourceWriter sw, JMethod method,
      TreeLogger treeLogger, boolean hasContext)
      throws UnableToCompleteException {

    String selector = method.getAnnotation(Selector.class).value();
    if (!hasContext) {
      sw.println("Node root = Document.get();");
    }

    sw.println("return " + wrap(method,
        "new SelectorEngine().select(\"" + selector + "\", root)") + ";");
  }
}
