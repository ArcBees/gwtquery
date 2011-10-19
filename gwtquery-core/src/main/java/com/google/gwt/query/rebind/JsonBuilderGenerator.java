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

import java.io.PrintWriter;

import com.google.gwt.core.client.JavaScriptObject;
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
import com.google.gwt.query.client.builders.JsonBuilder;
import com.google.gwt.query.client.builders.Name;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

/**
 */
public class JsonBuilderGenerator extends Generator {
  TypeOracle oracle;
  static JClassType jsonBuilderType;
  static JClassType stringType;
  static JClassType jsType;

  public String generate(TreeLogger treeLogger,
      GeneratorContext generatorContext, String requestedClass)
      throws UnableToCompleteException {
    oracle = generatorContext.getTypeOracle();
    JClassType clazz = oracle.findType(requestedClass);
    jsonBuilderType = oracle.findType(JsonBuilder.class.getName());
    stringType = oracle.findType(String.class.getName());
    jsType = oracle.findType(JavaScriptObject.class.getName());

    String t[] = generateClassName(clazz);

    SourceWriter sw = getSourceWriter(treeLogger, generatorContext, t[0],
        t[1], requestedClass);
    if (sw != null) {
      for (JMethod method : clazz.getMethods()) {
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
    ret[1] = c.getName().replace('.', '_') + "_JsonBuilder";
    ret[2] = ret[0] + "." + ret[1];
    return ret;
  }

  public boolean isTypeAssignableTo(JType t, JClassType o) {
    JClassType c = t.isClassOrInterface();
    return (c != null && c.isAssignableTo(o));
  }

  public void generateMethod(SourceWriter sw, JMethod method, TreeLogger logger)
      throws UnableToCompleteException {
    Name nameAnnotation = method.getAnnotation(Name.class);
    String name = nameAnnotation != null ? nameAnnotation.value()
        : method.getName().replaceFirst("^(get|set)", "").toLowerCase();

    String retType = method.getReturnType().getParameterizedQualifiedSourceName();

    sw.print("public final " + retType + " " + method.getName());
    JParameter[] params = method.getParameters();
    if (params.length == 0) {
      JArrayType arr = method.getReturnType().isArray();
      sw.println("() {");
      sw.indent();
      if (retType.matches("(java.lang.Boolean|boolean)")) {
        sw.println("return p.getBoolean(\"" + name + "\");");
      } else if (method.getReturnType().isPrimitive() != null) {
        sw.println("return (" + retType + ")p.getFloat(\"" + name + "\");");
      } else if (isTypeAssignableTo(method.getReturnType(), stringType)) {
        sw.println("return p.getStr(\"" + name + "\");");
      } else if (isTypeAssignableTo(method.getReturnType(), jsonBuilderType)) {
        String q = method.getReturnType().getQualifiedSourceName();
        sw.println("return " +
        		"((" + q + ")GWT.create(" + q + ".class))" +
        		".load(p.getJavaScriptObject(\"" + name + "\"));");
      } else if (retType.equals(Properties.class.getName())){
        sw.println("return getPropertiesBase(\"" + name + "\");");
      } else if (isTypeAssignableTo(method.getReturnType(), jsType)) {
        sw.println("return p.getJavaScriptObject(\"" + name + "\");");
      } else if (arr != null) {
        String t = arr.getComponentType().getQualifiedSourceName();
        sw.println("JsArrayMixed a = p.getArray(\"" + name + "\");");
        sw.println("int l = a == null ? 0 : a.length();");
        sw.println("return getArrayBase(\"" + name + "\", new " + t + "[l], "
            + t + ".class);");
      } else {
        sw.println("return null; // Unsupported return type: " + retType);
      }
      sw.outdent();
      sw.println("}");
    } else if (params.length == 1) {
      JType type = params[0].getType();
      JArrayType arr = type.isArray();
      sw.print("(" + type.getParameterizedQualifiedSourceName() + " a)");
      sw.println("{");
      sw.indent();
      if (arr != null) {
        sw.println("setArrayBase(\"" + name + "\", a);");
      } else {
        sw.println("p.set(\"" + name + "\", a);");
      }
      if (!"void".equals(retType)){
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
    composerFactory.setSuperclass("com.google.gwt.query.client.builders.JsonBuilderBase<"
        + packageName + "." + className + ">");
    composerFactory.addImport("com.google.gwt.query.client.js.*");
    composerFactory.addImport("com.google.gwt.query.client.*");
    composerFactory.addImport("com.google.gwt.core.client.*");
    composerFactory.addImport("com.google.gwt.dom.client.*");
    for (String interfaceName : interfaceNames) {
      composerFactory.addImplementedInterface(interfaceName);
    }
    return composerFactory.createSourceWriter(context, printWriter);
  }
}
