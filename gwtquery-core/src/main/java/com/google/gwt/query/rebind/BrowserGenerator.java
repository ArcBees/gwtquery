/*
 * Copyright 2013, The gwtquery team.
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

import com.google.gwt.core.ext.BadPropertyValueException;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.PropertyOracle;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

import java.io.PrintWriter;

/**
 * Creates an implementation for {@link Browser}.
 */
public class BrowserGenerator extends Generator {
  @Override
  public String generate(TreeLogger logger, GeneratorContext context, String typeName)
      throws UnableToCompleteException {
    TypeOracle oracle = context.getTypeOracle();
    PropertyOracle propOracle = context.getPropertyOracle();

    String ua = null;
    try {
      ua = propOracle.getSelectionProperty(logger, "user.agent").getCurrentValue();
    } catch (BadPropertyValueException e) {
      logger.log(TreeLogger.ERROR, "Can not resolve user.agent property", e);
      throw new UnableToCompleteException();
    }

    JClassType clz = oracle.findType(typeName);
    String pName = clz.getPackage().getName();
    String cName = clz.getName() + "_" + ua;

    PrintWriter pWriter = context.tryCreate(logger, pName, cName);

    if (pWriter != null) {
      ClassSourceFileComposerFactory cFact = new ClassSourceFileComposerFactory(pName, cName);
      cFact.setSuperclass(pName + "." + clz.getName());

      SourceWriter writer = cFact.createSourceWriter(context, pWriter);

      writer.println("protected boolean isWebkit() {return " + "safari".equals(ua) + ";}");
      writer.println("protected boolean isSafari() {return " + "safari".equals(ua) + ";}");
      writer.println("protected boolean isOpera() {return " + "opera".equals(ua) + ";}");
      writer.println("protected boolean isMozilla() {return " + ua.contains("gecko") + ";}");
      writer.println("protected boolean isMsie() {return " + ua.contains("ie") + ";}");
      writer.println("protected boolean isIe6() {return " + "ie6".equals(ua) + ";}");
      writer.println("protected boolean isIe8() {return " + "ie8".equals(ua) + ";}");
      writer.println("protected boolean isIe9() {return " + "ie9".equals(ua) + ";}");
      writer.println("protected boolean isIe10() {return " + "ie10".equals(ua) + ";}");
      writer.println("protected boolean isIe11() {return " + "gecko1_8".equals(ua) + ";}");
      writer.println("public String toString() {return \"Browser:\"" +
          " + \" webkit=\" + webkit" +
          " + \" mozilla=\" + mozilla" +
          " + \" opera=\" + opera" +
          " + \" msie=\" + msie" +
          " + \" ie6=\" + ie6" +
          " + \" ie8=\" + ie8" +
          " + \" ie9=\" + ie9" +
          ";}");
      writer.commit(logger);
    }

    return pName + "." + cName;
  }
}
