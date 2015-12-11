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

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JParameter;
import com.google.gwt.core.ext.typeinfo.JTypeParameter;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

import java.io.PrintWriter;

/**
 * Generate lazy implementations for interface delegated to parameterized type.
 * The originating type implements Lazy<T> where T extends LazyBase<OriginType>.
 * The generator generates an implementation of T where each method returns type
 * T and queues up a closure which delegates execution of the method to the
 * OriginType.
 */
public class LazyGenerator extends Generator {

  private JClassType lazyType = null;

  private JClassType lazyBaseType = null;

  public String generate(TreeLogger treeLogger,
      GeneratorContext generatorContext, String requestedClass)
      throws UnableToCompleteException {
    TypeOracle oracle = generatorContext.getTypeOracle();
    lazyType = oracle.findType("com.google.gwt.query.client.Lazy");
    lazyBaseType = oracle.findType("com.google.gwt.query.client.LazyBase");

    assert lazyType != null : "Can't find Lazy interface type";
    assert lazyBaseType != null : "Can't find LazyBase interface type";

    JClassType requestedType = oracle.findType(requestedClass);
    JClassType targetType = null;
    JClassType nonLazyType = null;

    for (JClassType inf : requestedType.getImplementedInterfaces()) {
      if (inf.isAssignableTo(lazyType)) {
        nonLazyType = inf.isParameterized().getTypeArgs()[0];
        targetType = inf.isParameterized().getTypeArgs()[1];
        break;
      }
    }

    if (targetType == null)
      return null;

    assert targetType != null : "Parameter of Lazy<T> not found";
    String genClass = targetType.getPackage().getName() + "."
        + targetType.getSimpleSourceName() + getImplSuffix();

    SourceWriter sw = getSourceWriter(treeLogger, generatorContext,
        requestedType.getPackage().getName(),
        targetType.getSimpleSourceName() + getImplSuffix(),
        targetType.getQualifiedSourceName());
    if (sw != null) {
      generatePreamble(sw, nonLazyType.getQualifiedSourceName(), treeLogger);
      sw.indent();
      for (JMethod method : targetType.getMethods()) {
        generateMethod(sw, method, nonLazyType.getQualifiedSourceName(),
            genClass, treeLogger);
      }
      sw.outdent();
      generateDoneMethod(sw, nonLazyType, treeLogger);
      sw.commit(treeLogger);
    }

    return genClass;
  }

  private void generatePreamble(SourceWriter sw, String nonLazyClass,
      TreeLogger treeLogger) {
    sw.indent();
    sw.println(
        "private JsArray<JsClosure> closures = JsArray.createArray().cast();");
    sw.println("private " + nonLazyClass + "  ctx;");
    sw.outdent();
  }

  public String getJSNIParams(JMethod method) {

    String reference = "(";
    JParameter[] params = method.getParameters();
    for (int i = 0; i < params.length; i++) {
      reference += params[i].getType().getJNISignature();
    }
    reference += ")";
    return reference;
  }

  public void generateMethod(SourceWriter sw, JMethod method,
      String nonLazyClass, String genClass, TreeLogger logger)
      throws UnableToCompleteException {

    JParameter[] params = method.getParameters();
    JTypeParameter gType = method.getReturnType().isTypeParameter();

    String retType = method.getReturnType()
        .getParameterizedQualifiedSourceName();
    if (gType != null) {
      retType = "<" + gType.getParameterizedQualifiedSourceName() + " extends "
          + gType.getFirstBound().getQualifiedSourceName() + "> " + retType;
    }
    sw.print("public final native " + retType + " " + method.getName());
    sw.print("(");
    int argNum = 0;
    for (JParameter param : params) {
      sw.print((argNum == 0 ? "" : ", ")
          + param.getType().getParameterizedQualifiedSourceName() + " arg"
          + argNum);
      argNum++;
    }
    sw.println(") /*-{");

    sw.indent();
    sw.println("var self=this;");
    sw.println("this.@" + genClass + "::closures.push(");
    sw.indent();
    sw.println("function() {");
    sw.indent();
    sw.print("self.@" + genClass + "::ctx=self.@" + genClass + "::ctx.@"
        + nonLazyClass + "::" + method.getName());
    sw.print(getJSNIParams(method));
    sw.print("(");
    for (int i = 0; i < argNum; i++) {
      sw.print("arg" + i + (i < argNum - 1 ? "," : ""));
    }

    sw.print(")");
    // special case, as() needs to invoke createLazy()
    if ("as".equals(method.getName())) {
      sw.print(".createLazy()");
    }
    sw.println(";");
    sw.outdent();
    sw.println("}");
    sw.outdent();
    sw.println(");");
    sw.println("return this;");
    sw.outdent();
    sw.println("}-*/;");
  }

  protected String getImplSuffix() {
    return "Impl";
  }

  protected SourceWriter getSourceWriter(TreeLogger logger,
      GeneratorContext context, String packageName, String className,
      String... interfaceNames) {
    PrintWriter printWriter = context.tryCreate(logger, packageName, className);
    if (printWriter == null) {
      return null;
    }
    ClassSourceFileComposerFactory composerFactory =
        new ClassSourceFileComposerFactory(packageName, className);
    composerFactory.addImport("com.google.gwt.core.client.*");
    composerFactory.addImport("com.google.gwt.query.client.*");
    composerFactory.addImport("com.google.gwt.dom.client.Element");
    composerFactory.addImport("com.google.gwt.user.client.Event");
    composerFactory.addImport("com.google.gwt.query.client.Function");
    composerFactory.addImport("com.google.gwt.query.client.js.JsClosure");

    for (String interfaceName : interfaceNames) {
      composerFactory.addImplementedInterface(interfaceName);
    }

    return composerFactory.createSourceWriter(context, printWriter);
  }

  // used by benchmark harness
  private void generateDoneMethod(SourceWriter sw, JClassType nonLazyType,
      TreeLogger treeLogger) {
    sw.indent();
    sw.println("public Function done() {");
    sw.indent();
    sw.println("return new Function() {");
    sw.indent();

    sw.println("public void f() {");
    sw.indent();
    String classID = nonLazyType.getSimpleSourceName();
    if ("GQuery".equals(classID)) {
      classID = "GQUERY";
    }

    sw.println(
        "ctx = GQuery.$((Element) getElement()).as(" + nonLazyType.getQualifiedSourceName() + "."
            + classID + ");");
    sw.println("for (int i = 0; i < closures.length(); i++) {");
    sw.indent();
    sw.println("closures.get(i).invoke();");
    sw.outdent();
    sw.println("}");
    sw.outdent();
    sw.println("}");
    sw.outdent();
    sw.println("};");
    sw.outdent();
    sw.println("}");
  }
}
