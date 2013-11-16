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

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.query.client.builders.JsniBundle;
import com.google.gwt.query.client.builders.JsniBundle.LibrarySource;
import com.google.gwt.query.client.builders.JsniBundle.MethodSource;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

/**
 * Generates an implementation of a user-defined interface <code>T</code> that
 * extends {@link JsniBundle}.
 * 
 * The generated implementation includes hand-written external js-files into
 * jsni methods so as those files can take advantage of gwt compiler optimizations.
 * 
 */
public class JsniBundleGenerator extends Generator {

  public String generate(TreeLogger logger,
      GeneratorContext context, String requestedClass)
      throws UnableToCompleteException {
    
    TypeOracle oracle = context.getTypeOracle();
    JClassType clazz = oracle.findType(requestedClass);

    String packageName = clazz.getPackage().getName();
    String className = clazz.getName().replace('.', '_') + "_Impl" ;
    String fullName = packageName + "." + className;
    
    PrintWriter pw = context.tryCreate(logger, packageName, className);
    
    if (pw != null) {

      ClassSourceFileComposerFactory fact = new ClassSourceFileComposerFactory(packageName, className);
      fact.addImplementedInterface(requestedClass);
      SourceWriter sw = fact.createSourceWriter(context, pw);
      
      if (sw != null) {
        for (JMethod method : clazz.getMethods()) {
          LibrarySource librarySource = method.getAnnotation(LibrarySource.class);
          String value, prepend, postpend;
          if (librarySource != null) {
            value = librarySource.value();
            prepend = librarySource.prepend();
            postpend = librarySource.postpend();
          } else {
            MethodSource methodSource = method.getAnnotation(MethodSource.class);
            if (methodSource != null) {
              value = methodSource.value();
              prepend = methodSource.prepend();
              postpend = methodSource.postpend();
            } else {
              return null;
            }
          }
          String file = packageName.replace(".", "/") + "/" + value;
          try {
            InputStream is = this.getClass().getClassLoader().getResourceAsStream(file);
            OutputStream os = new ByteArrayOutputStream();
            IOUtils.copy(is, os);
            
            String jsni = os.toString()
                // remove MS <CR>
                .replace("\r", "")
                // remove 'c' (/* */) style comments blocks 
                .replaceAll("(\\s*/\\*[\\s\\S]*?\\*/\\s*)", "\n")
                // remove 'c++' (//) style comment lines
                .replaceAll("(?m)^\\s*//.*$", "")
                // remove 'c++' (//) style comments at the end of a code line
                .replaceAll("(?m)^(.*)//[^'\"]*?$", "$1")
                // remove empty lines
                .replaceAll("\n+", "\n");
                ;
            
            // Using pw instead of sw in order to avoid stack errors because sw.print is a recursive function
            // and it fails with very long javascript files.
                
            // JMethod.toString() prints the java signature of the method, so we just have to replace abstract by native.    
            pw.println(method.toString().replace("abstract", "native") + "/*-{");
            pw.println(prepend);
            pw.println(jsni);
            pw.println(postpend);
            pw.println("}-*/;");
          } catch (Exception e) {
            e.printStackTrace();
            throw new UnableToCompleteException();
          }
        }
        
        sw.commit(logger);
      }
    }

    return fullName;
  }
}
