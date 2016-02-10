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
import com.google.gwt.core.ext.typeinfo.JArrayType;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JParameter;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.query.client.Properties;
import com.google.gwt.query.client.builders.Name;
import com.google.gwt.query.client.builders.XmlBuilder;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

import java.io.PrintWriter;

/**
 */
public class XmlBuilderGenerator extends Generator {
  TypeOracle oracle;
  static JClassType xmlBuilderType;
  static JClassType stringType;

  public String generate(TreeLogger treeLogger,
      GeneratorContext generatorContext, String requestedClass)
      throws UnableToCompleteException {
    oracle = generatorContext.getTypeOracle();
    JClassType clazz = oracle.findType(requestedClass);
    xmlBuilderType = oracle.findType(XmlBuilder.class.getName());
    stringType = oracle.findType(String.class.getName());

    String t[] = generateClassName(clazz);

    SourceWriter sw = getSourceWriter(treeLogger, generatorContext, t[0],
        t[1], requestedClass);
    if (sw != null) {
      for (JMethod method : clazz.getInheritableMethods()) {
        // skip method from JsonBuilder
        if (xmlBuilderType.findMethod(method.getName(), method.getParameterTypes()) != null) {
          continue;
        }
        generateMethod(sw, method, treeLogger);
      }
      sw.commit(treeLogger);
    }
    return t[2];
  }

  public String[] generateClassName(JType t) {
    String[] ret = new String[3];
    JClassType c = t.isClassOrInterface();
    ret[0] = c.getPackage().getName();
    ret[1] = c.getName().replace('.', '_') + "_XmlBuilder";
    ret[2] = ret[0] + "." + ret[1];
    return ret;
  }

  public boolean isTypeAssignableTo(JType t, JClassType o) {
    JClassType c = t.isClassOrInterface();
    return c != null && c.isAssignableTo(o);
  }

  public void generateMethod(SourceWriter sw, JMethod method, TreeLogger logger)
      throws UnableToCompleteException {
    Name nameAnnotation = method.getAnnotation(Name.class);
    String name = nameAnnotation != null ? nameAnnotation.value()
        : method.getName().replaceFirst("^(get|set)", "");

    if (nameAnnotation == null) {
      name = name.substring(0, 1).toLowerCase() + name.substring(1);
    }

    String retType = method.getReturnType().getParameterizedQualifiedSourceName();

    sw.print("public final " + retType + " " + method.getName());
    JParameter[] params = method.getParameters();
    if (params.length == 0) {
      JArrayType arr = method.getReturnType().isArray();
      sw.println("() {");
      sw.indent();
      if (retType.matches("(java.lang.Boolean|boolean)")) {
        sw.println("return getBooleanBase(\"" + name + "\");");
      } else if (method.getReturnType().isPrimitive() != null) {
        sw.println("return (" + retType + ")getFloatBase(\"" + name + "\");");
      } else if (isTypeAssignableTo(method.getReturnType(), stringType)) {
        sw.println("return getStrBase(\"" + name + "\");");
      } else if (isTypeAssignableTo(method.getReturnType(), xmlBuilderType)) {
        String q = method.getReturnType().getQualifiedSourceName();
        sw.println("Element e = getElementBase(\"" + name + "\");");
        sw.println("return e == null ? null : (" + q + ")((" + q + ")GWT.create(" + q
            + ".class)).load(e);");
      } else if (retType.equals(Properties.class.getName())) {
        sw.println("return getPropertiesBase(\"" + name + "\");");
      } else if (arr != null) {
        String q = arr.getComponentType().getQualifiedSourceName();
        sw.println("ArrayList<" + q + "> l = new ArrayList<" + q + ">();");
        sw.println("for (Element e: getElementsBase(\"" + name + "\")) {");
        sw.println("  " + q + " c = GWT.create(" + q + ".class);");
        sw.println("  c.load(e);");
        sw.println("  l.add(c);");
        sw.println("}");
        sw.println("return l.toArray(new " + q + "[0]);");
      } else {
        sw.println("return null; // Unsupported return type: " + retType);
      }
      sw.outdent();
      sw.println("}");
    } else if (params.length == 1) {
      JType type = params[0].getType();
      JArrayType arr = type.isArray();
      String qname = type.getParameterizedQualifiedSourceName();
      sw.print("(" + qname + " a)");
      sw.println("{");
      sw.indent();
      if (arr != null) {
        sw.println("setArrayBase(\"" + name + "\", a);");
      } else {
        sw.println("setBase(\"" + name + "\", a);");
      }
      if (!"void".equals(retType)) {
        if (isTypeAssignableTo(method.getReturnType(), method.getEnclosingType())) {
          sw.println("return this;");
        } else {
          sw.println("return null;");
        }
      }
      sw.outdent();
      sw.println("}");
    }
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
    composerFactory.setSuperclass("com.google.gwt.query.client.builders.XmlBuilderBase<"
        + packageName + "." + className + ">");
    composerFactory.addImport("com.google.gwt.query.client.js.*");
    composerFactory.addImport("com.google.gwt.query.client.*");
    composerFactory.addImport("com.google.gwt.core.client.*");
    composerFactory.addImport("com.google.gwt.dom.client.*");
    composerFactory.addImport("java.util.*");
    for (String interfaceName : interfaceNames) {
      composerFactory.addImplementedInterface(interfaceName);
    }
    return composerFactory.createSourceWriter(context, printWriter);
  }
}
