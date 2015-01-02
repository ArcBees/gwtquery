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
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.query.client.Selector;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

import java.io.PrintWriter;

/**
 * Base class for compile time selector generators.
 */
public abstract class SelectorGeneratorBase extends Generator {

  protected JClassType nodeType = null;

  private TreeLogger treeLogger;

  public String generate(TreeLogger treeLogger,
      GeneratorContext generatorContext, String requestedClass)
      throws UnableToCompleteException {
    this.treeLogger = treeLogger;
    TypeOracle oracle = generatorContext.getTypeOracle();
    nodeType = oracle.findType("com.google.gwt.dom.client.Node");

    JClassType selectorType = oracle.findType(requestedClass);

    String generatedPkgName = selectorType.getPackage().getName();
    String generatedClassName = selectorType.getName().replace('.', '_') + "_"
        + getImplSuffix();

    SourceWriter sw = getSourceWriter(treeLogger, generatorContext,
        generatedPkgName, generatedClassName, requestedClass);
    if (sw != null) {
      for (JMethod method : selectorType.getInheritableMethods()) {
        generateMethod(sw, method, treeLogger);
      }
      genGetAllMethod(sw, selectorType.getInheritableMethods(), treeLogger);
      sw.commit(treeLogger);
    }

    return generatedPkgName + "." + generatedClassName;
  }

  public void generateMethod(SourceWriter sw, JMethod method, TreeLogger logger)
      throws UnableToCompleteException {
    Selector selectorAnnotation = method.getAnnotation(Selector.class);
    if (selectorAnnotation == null) {
      return;
    }

    JParameter[] params = method.getParameters();

    String retType = method.getReturnType()
        .getParameterizedQualifiedSourceName();
    sw.print("public final " + retType + " " + method.getName());
    boolean hasContext = false;
    if (params.length == 0) {
      sw.print("()");
    } else if (params.length == 1) {
      JClassType type = params[0].getType().isClassOrInterface();
      if (type != null && type.isAssignableTo(nodeType)) {
        sw.print("(Node root)");
        hasContext = true;
      }
    }
    sw.println(" {");
    sw.indent();
    Selector sel = method.getAnnotation(Selector.class);

    if (sel != null && sel.value().matches("^#\\w+$")) {
      // short circuit #foo
      sw.println("return "
          + wrap(method, "JsNodeArray.create(((Document)root).getElementById(\""
              + sel.value().substring(1) + "\"))") + ";");
    } else if (sel != null && sel.value().matches("^\\w+$")) {
      // short circuit FOO
      sw.println("return "
          + wrap(method,
              "JsNodeArray.create(((Element)root).getElementsByTagName(\""
                  + sel.value() + "\"))") + ";");
    } else if (sel != null && sel.value().matches("^\\.\\w+$")
        && hasGetElementsByClassName()) {
      // short circuit .foo for browsers with native getElementsByClassName
      sw.println("return "
          + wrap(method, "JsNodeArray.create(getElementsByClassName(\""
              + sel.value().substring(1) + "\", root))") + ";");
    } else {
      generateMethodBody(sw, method, logger, hasContext);
    }
    sw.outdent();
    sw.println("}");
  }

  protected void debug(String s) {
    // System.err.println(s);
    treeLogger.log(TreeLogger.DEBUG, s, null);
  }

  protected abstract void generateMethodBody(SourceWriter sw, JMethod method,
      TreeLogger logger, boolean hasContext) throws UnableToCompleteException;

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
    ClassSourceFileComposerFactory composerFactory = new ClassSourceFileComposerFactory(
        packageName, className);
    composerFactory.setSuperclass("com.google.gwt.query.client.impl.SelectorEngine");
    composerFactory.addImport("com.google.gwt.query.client.impl.*");
    composerFactory.addImport("com.google.gwt.query.client.js.*");
    composerFactory.addImport("com.google.gwt.query.client.Selectors.*");
    composerFactory.addImport("com.google.gwt.query.client.*");
    composerFactory.addImport("com.google.gwt.core.client.*");
    composerFactory.addImport("com.google.gwt.dom.client.*");
    for (String interfaceName : interfaceNames) {
      composerFactory.addImplementedInterface(interfaceName);
    }

    return composerFactory.createSourceWriter(context, printWriter);
  }

  protected boolean hasGetElementsByClassName() {
    return false;
  }

  protected boolean notNull(String s) {
    return s != null && !"".equals(s);
  }

  protected String wrap(JMethod method, String expr) {
    if ("NodeList".equals(method.getReturnType().getSimpleSourceName())) {
      return expr;
    } else {
      return "GQuery.$(" + expr + ")";
    }
  }

  protected String wrapJS(JMethod method, String expr) {
    if ("GQuery".equals(method.getReturnType().getSimpleSourceName())) {
      return expr;
    } else {
      return "GQuery.$(" + expr + ")";
    }
  }

  // used by benchmark harness
  private void genGetAllMethod(SourceWriter sw, JMethod[] methods,
      TreeLogger treeLogger) {
    sw.println("public DeferredSelector[] getAllSelectors() {return ds;}");
    sw.println("private final DeferredSelector[] ds = new DeferredSelector[] {");
    sw.indent();
    for (JMethod m : methods) {
      Selector selectorAnnotation = m.getAnnotation(Selector.class);
      if (selectorAnnotation == null) {
        continue;
      }
      String selector = selectorAnnotation.value();

      sw.println("new DeferredSelector() {");
      sw.indent();
      sw
          .println("public String getSelector() { return \"" + selector
              + "\"; }");
      sw
          .println("public NodeList<Element> runSelector(Node ctx) { return " +
              (m.getName() + (m.getParameters().length == 0 ? "()" : "(ctx)")) +
              ("NodeList".equals(m.getReturnType().getSimpleSourceName()) ? "" : ".get()") + ";}"
          );
      sw.outdent();
      sw.println("},");
    }
    sw.outdent();
    sw.println("};");
  }
}
