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
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JArrayType;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JParameter;
import com.google.gwt.core.ext.typeinfo.JParameterizedType;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.Properties;
import com.google.gwt.query.client.builders.JsonBuilder;
import com.google.gwt.query.client.builders.Name;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

/**
 */
public class JsonBuilderGenerator extends Generator {
  static JClassType enumType;
  static JClassType functionType;
  static JClassType jsonBuilderType;
  static JClassType jsType;
  static JClassType listType;
  static JClassType stringType;
  public static String capitalize(String s) {
    if (s.length() == 0)
      return s;
    return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
  }

  public static String classNameToJsonName(String name) {
    return deCapitalize(name.replaceAll("^.*[\\.\\$_]", ""));
  }

  public static String deCapitalize(String s) {
    return s == null || s.isEmpty() ? s : 
           (s.substring(0, 1).toLowerCase() + (s.length() > 1 ? s.substring(1) : ""));
  }
  
  TypeOracle oracle;

  public String generate(TreeLogger treeLogger,
      GeneratorContext generatorContext, String requestedClass)
      throws UnableToCompleteException {
    oracle = generatorContext.getTypeOracle();
    JClassType clazz =  oracle.findType(requestedClass);
    jsonBuilderType = oracle.findType(JsonBuilder.class.getName());
    stringType = oracle.findType(String.class.getName());
    jsType = oracle.findType(JavaScriptObject.class.getName());
    listType = oracle.findType(List.class.getName());
    functionType = oracle.findType(Function.class.getName());
    enumType = oracle.findType(Enum.class.getName());

    String t[] = generateClassName(clazz);

    SourceWriter sw = getSourceWriter(treeLogger, generatorContext, t[0], t[1],
        requestedClass);
    if (sw != null) {
      Set<String> attrs = new HashSet<String>();
      for (JMethod method : clazz.getInheritableMethods()) {
        String methName = method.getName();
        //skip method from JsonBuilder
        if(jsonBuilderType.findMethod(method.getName(), method.getParameterTypes()) != null){
        	continue;
        }
        
        Name nameAnnotation = method.getAnnotation(Name.class);
        String name = nameAnnotation != null 
          ? nameAnnotation.value()
          : methName.replaceFirst("^(get|set)", "");
        name = name.substring(0, 1).toLowerCase() + name.substring(1);
        attrs.add(name);
        generateMethod(sw, method, name, treeLogger);
      }
      generateFieldNamesMethod(sw, attrs, treeLogger);
      generateToJsonMethod(sw, t[3], treeLogger);
      sw.commit(treeLogger);
    }
    return t[2];
  }

  public String[] generateClassName(JType t) {
    String[] ret = new String[4];
    JClassType c = t.isClassOrInterface();
    ret[0] = c.getPackage().getName();
    ret[1] = c.getName().replace('.', '_') + "_JsonBuilder";
    ret[2] = ret[0] + "." + ret[1];
    ret[3] = classNameToJsonName(c.getName());
    return ret;
  }
  
  public void generateFieldNamesMethod(SourceWriter sw, Collection<String> attrs, TreeLogger logger) {
    String ret = "";
    for (Iterator<String> it = attrs.iterator(); it.hasNext();) {
      ret += (ret.isEmpty() ? "" : ",") + "\"" + it.next() + "\"";
    }
    sw.println("public final String[] getFieldNames() {return new String[]{" + ret + "};}");
  }
  
  public void generateMethod(SourceWriter sw, JMethod method, String name, TreeLogger logger)
      throws UnableToCompleteException {
    String ifaceName = method.getEnclosingType().getQualifiedSourceName();

    String retType = method.getReturnType().getParameterizedQualifiedSourceName();
    sw.print("public final " + retType + " " + method.getName());
    JParameter[] params = method.getParameters();
    if (params.length == 0) {
      JArrayType arr = method.getReturnType().isArray();
      JParameterizedType list = method.getReturnType().isParameterized();

      sw.println("() {");
      sw.indent();
      if (retType.matches("(java.lang.Boolean|boolean)")) {
        sw.println("return p.getBoolean(\"" + name + "\");");
      } else if (retType.matches("java.util.Date")) {
        sw.println("return new Date(java.lang.Long.parseLong(p.getStr(\"" + name + "\")));");
      } else if (method.getReturnType().isPrimitive() != null) {
        sw.println("return (" + retType + ")p.getFloat(\"" + name + "\");");
      } else if (retType.equals("java.lang.Character")) {
        sw.println("return (char) p.getFloat(\"" + name + "\");");
      } else if (retType.equals("java.lang.Byte")) {
        sw.println("return (byte) p.getFloat(\"" + name + "\");");
      } else if (retType.equals("java.lang.Integer")) {
    	  sw.println("return (int) p.getFloat(\"" + name + "\");");
      } else if (retType.equals("java.lang.Float")) {
    	  sw.println("return p.getFloat(\"" + name + "\");");
      } else if (retType.equals("java.lang.Double")) {
    	  sw.println("return (double) p.getFloat(\"" + name + "\");");
      } else if (retType.equals("java.lang.Long")) {
    	  sw.println("return (long) p.getFloat(\"" + name + "\");");
      } else if (retType.equals("java.lang.Byte")) {
    	  sw.println("return (byte) p.getFloat(\"" + name + "\");");
      } else if (isTypeAssignableTo(method.getReturnType(), stringType)) {
        sw.println("return p.getStr(\"" + name + "\");");
      } else if (isTypeAssignableTo(method.getReturnType(), jsonBuilderType)) {
        String q = method.getReturnType().getQualifiedSourceName();
        sw.println("return " + "((" + q + ")GWT.create(" + q + ".class))"
            + ".load(p.getJavaScriptObject(\"" + name + "\"));");
      } else if (retType.equals(Properties.class.getName())) {
        sw.println("return getPropertiesBase(\"" + name + "\");");
      } else if (isTypeAssignableTo(method.getReturnType(), jsType)) {
        sw.println("return p.getJavaScriptObject(\"" + name + "\");");
      } else if (isTypeAssignableTo(method.getReturnType(), functionType)) {
        sw.println("return getFunctionBase(\"" + name + "\");");
      } else if (arr != null || list != null) {
        JType type = arr != null ? arr.getComponentType()
            : list.getTypeArgs()[0];
        boolean buildType = isTypeAssignableTo(type, jsonBuilderType);
        String t = type.getQualifiedSourceName();
        sw.println("JsArrayMixed a = p.getArray(\"" + name + "\");");
        sw.println("int l = a == null ? 0 : a.length();");
        String ret;
        if (buildType) {
          sw.println(t + "[] r = new " + t + "[l];");
          sw.println("JsObjectArray<?> a1 = p.getArray(\"" + name
              + "\").cast();");
          sw.println("int l1 = r.length;");
          sw.println("for (int i = 0 ; i < l1 ; i++) {");
          sw.println("  Object w = a1.get(i);");
          sw.println("  " + t + " instance = GWT.create(" + t + ".class);");
          sw.println("  r[i] = instance.load(w);");
          sw.println("}");
          ret = "r";
        } else {
          ret = "getArrayBase(\"" + name + "\", new " + t + "[l], " + t + ".class)";
        }
        if (arr != null) {
          sw.println("return " + ret + ";");
        } else {
          sw.println("return Arrays.asList(" + ret + ");");
        }
      } else if (isTypeAssignableTo(method.getReturnType(), enumType)){  
    	 sw.println("return "+method.getReturnType().getQualifiedSourceName()+".valueOf(p.getStr(\"" + name + "\"));");
      }else {
        sw.println("System.err.println(\"JsonBuilderGenerator WARN: unknown return type " 
            + retType + " " + ifaceName + "." + name + "()\"); ");
        // We return the object because probably the user knows how to handle it
        sw.println("return p.get(\"" + name + "\");");
      }
      sw.outdent();
      sw.println("}");
    } else if (params.length == 1) {
      JType type = params[0].getType();
      JArrayType arr = type.isArray();
      JParameterizedType list = type.isParameterized();
      sw.print("(" + type.getParameterizedQualifiedSourceName() + " a)");
      sw.println("{");
      sw.indent();
      if (arr != null || list != null) {
        String a = "a";
        if (list != null) {
          a = "a.toArray(new " + list.getTypeArgs()[0].getQualifiedSourceName()
              + "[0])";
        }
        sw.println("setArrayBase(\"" + name + "\", " + a + ");");
      } else if (type.getParameterizedQualifiedSourceName().matches("java.util.Date")) {
        sw.println("p.setNumber(\"" + name + "\", a.getTime());");
      } else if (type.getParameterizedQualifiedSourceName().matches("(java.lang.(Character|Long|Double|Integer|Float|Byte)|(char|long|double|int|float|byte))")) {
        sw.println("p.setNumber(\"" + name + "\", a);");
      } else if (type.getParameterizedQualifiedSourceName().matches("(java.lang.Boolean|boolean)")) {
        sw.println("p.setBoolean(\"" + name + "\", a);");        
      } else {
        sw.println("p.set(\"" + name + "\", a);");
      }
      if (!"void".equals(retType)) {
        if (isTypeAssignableTo(method.getReturnType(),
            method.getEnclosingType())) {
          sw.println("return this;");
        } else {
          sw.println("return null;");
        }
      }
      sw.outdent();
      sw.println("}");
    }
  }
  
  public void generateToJsonMethod(SourceWriter sw, String name, TreeLogger logger) {
    sw.println("public final String getJsonName() {return \"" + name + "\";}");
    sw.println("public final String toJson() {return \"{\\\"\" + getJsonName() + \"\\\":\" + toString() + \"}\";}");
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
    composerFactory.addImport("java.util.*");

    for (String interfaceName : interfaceNames) {
      composerFactory.addImplementedInterface(interfaceName);
    }
    return composerFactory.createSourceWriter(context, printWriter);
  }

  public boolean isTypeAssignableTo(JType t, JClassType o) {
    JClassType c = t.isClassOrInterface();
    return (c != null && c.isAssignableTo(o));
  }
}
