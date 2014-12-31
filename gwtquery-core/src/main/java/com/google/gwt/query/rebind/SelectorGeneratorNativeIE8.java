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
import com.google.gwt.query.client.impl.SelectorEngineNativeIE8;
import com.google.gwt.user.rebind.SourceWriter;

/**
 * Compile time selector generator which delegates to native browser methods.
 */
public class SelectorGeneratorNativeIE8 extends SelectorGeneratorJS {

  @Override
  protected void generateMethodBody(SourceWriter sw, JMethod method,
      TreeLogger treeLogger, boolean hasContext)
      throws UnableToCompleteException {
    String selector = method.getAnnotation(Selector.class).value();
    if (selector.matches("#[\\w\\-]+")) {
      sw.println("return "
          + wrap(method, "veryQuickId(\"" + selector.substring(1) + "\", root)") + ";");
    } else if (selector.equals("*") || selector.matches("[\\w\\-]+")) {
      sw.println("return "
          + wrap(method, "elementsByTagName(\"" + selector + "\", root)") + ";");
    } else if (selector.matches("\\.[\\w\\-]+")) {
      sw.println("return "
          + wrap(method, "elementsByClassName(\"" + selector.substring(1) + "\", root)") + ";");
    } else if (selector.matches(SelectorEngineNativeIE8.NATIVE_EXCEPTIONS_REGEXP)) {
      super.generateMethodBody(sw, method, treeLogger, hasContext);
    } else {
      sw.println("return "
          + wrap(method, "querySelectorAll(\"" + selector + "\", root)") + ";");
    }
  }

  @Override
  protected String getImplSuffix() {
    return "IE8" + super.getImplSuffix();
  }

  @Override
  protected boolean hasGetElementsByClassName() {
    return false;
  }
}
