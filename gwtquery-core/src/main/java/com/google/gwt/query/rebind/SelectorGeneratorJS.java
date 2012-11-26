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

/**
 * An implementation which simply defers to the runtime selector engine.
 */
public class SelectorGeneratorJS extends SelectorGeneratorBase {

  protected String getImplSuffix() {
    return "JS" + super.getImplSuffix();
  }

  protected void generateMethodBody(SourceWriter sw, JMethod method,
      TreeLogger treeLogger, boolean hasContext)
      throws UnableToCompleteException {
    String selector = method.getAnnotation(Selector.class).value();
    sw.println("return "
        + wrap(method, "impl.select(\"" + selector + "\", root)") + ";");
  }
}
