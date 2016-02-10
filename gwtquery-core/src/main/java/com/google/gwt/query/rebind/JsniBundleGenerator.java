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

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.query.client.builders.JsniBundle.LibrarySource;
import com.google.gwt.query.client.builders.JsniBundle.MethodSource;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

import org.apache.commons.io.output.ByteArrayOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

/**
 * Generates an implementation of a user-defined interface <code>T</code> that
 * extends {@link JsniBundle}.
 *
 * The generated implementation includes hand-written external js-files into
 * jsni methods so as those files can take advantage of gwt compiler optimizations.
 *
 */
public class JsniBundleGenerator extends Generator {

  public String generate(TreeLogger logger, GeneratorContext context, String requestedClass)
      throws UnableToCompleteException {

    TypeOracle oracle = context.getTypeOracle();
    JClassType clazz = oracle.findType(requestedClass);

    String packageName = clazz.getPackage().getName();
    String className = clazz.getName().replace('.', '_') + "_Impl";
    String fullName = packageName + "." + className;

    PrintWriter pw = context.tryCreate(logger, packageName, className);

    if (pw != null) {
      ClassSourceFileComposerFactory fact =
          new ClassSourceFileComposerFactory(packageName, className);
      if (clazz.isInterface() != null) {
        fact.addImplementedInterface(requestedClass);
      } else {
        fact.setSuperclass(requestedClass);
      }

      SourceWriter sw = fact.createSourceWriter(context, pw);

      if (sw != null) {
        for (JMethod method : clazz.getMethods()) {
          LibrarySource librarySource = method.getAnnotation(LibrarySource.class);
          String value, prepend, postpend;
          String replace[];
          if (librarySource != null) {
            value = librarySource.value();
            prepend = librarySource.prepend();
            postpend = librarySource.postpend();
            replace = librarySource.replace();
          } else {
            MethodSource methodSource = method.getAnnotation(MethodSource.class);
            if (methodSource != null) {
              value = methodSource.value();
              prepend = methodSource.prepend();
              postpend = methodSource.postpend();
              replace = methodSource.replace();
            } else {
              continue;
            }
          }
          try {
            // Read the javascript content
            String content = getContent(logger, packageName.replace(".", "/"), value);

            // Adjust javascript so as we can introduce it in a JSNI comment block without
            // breaking java syntax.
            String jsni = parseJavascriptSource(content);

            for (int i = 0; i < replace.length - 1; i += 2) {
              jsni = jsni.replaceAll(replace[i], replace[i + 1]);
            }

            pw.println(method.toString().replace("abstract", "native") + "/*-{");
            pw.println(prepend);
            pw.println(jsni);
            pw.println(postpend);
            pw.println("}-*/;");
          } catch (Exception e) {
            logger.log(TreeLogger.ERROR, "Error parsing javascript source: " + value + " "
                + e.getMessage());
            throw new UnableToCompleteException();
          }
        }
      }
      sw.commit(logger);
    }

    return fullName;
  }

  /**
   * Get the content of a javascript source. It supports remote sources hosted in CDN's.
   */
  private String getContent(TreeLogger logger, String path, String src)
      throws UnableToCompleteException {
    HttpURLConnection connection = null;
    InputStream in = null;
    try {
      if (!src.matches("(?i)https?://.*")) {
        String file = path + "/" + src;
        in = this.getClass().getClassLoader().getResourceAsStream(file);
        if (in == null) {
          // If we didn't find the resource relative to the package, assume it is absolute.
          file = src;
          in = this.getClass().getClassLoader().getResourceAsStream(file); 
        }
        if (in != null) {
          logger.log(TreeLogger.INFO, getClass().getSimpleName()
              + " - importing external javascript: " + file);
        } else {
          logger.log(TreeLogger.ERROR, "Unable to read javascript file: " + file);
        }
      } else {
        logger.log(TreeLogger.INFO, getClass().getSimpleName()
            + " - downloading external javascript: " + src);
        URL url = new URL(src);
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Accept-Encoding", "gzip, deflate");
        connection.setRequestProperty("Host", url.getHost());
        connection.setConnectTimeout(3000);
        connection.setReadTimeout(3000);

        int status = connection.getResponseCode();
        if (status != HttpURLConnection.HTTP_OK) {
          logger.log(TreeLogger.ERROR, "Server Error: " + status + " "
              + connection.getResponseMessage());
          throw new UnableToCompleteException();
        }

        String encoding = connection.getContentEncoding();
        in = connection.getInputStream();
        if ("gzip".equalsIgnoreCase(encoding)) {
          in = new GZIPInputStream(in);
        } else if ("deflate".equalsIgnoreCase(encoding)) {
          in = new InflaterInputStream(in);
        }
      }

      return inputStreamToString(in);
    } catch (IOException e) {
      logger.log(TreeLogger.ERROR, "Error: " + e.getMessage());
      throw new UnableToCompleteException();
    } finally {
      if (connection != null) {
        connection.disconnect();
      }
    }
  }

  /**
   * Adapt a java-script block which could produce a syntax error when
   * embedding it in a JSNI block.
   *
   * The objective is to replace any 'c' comment-ending occurrence to avoid closing
   * JSNI comment blocks prematurely.
   *
   * A Regexp based parser is not reliable, this approach is better and faster.
   */
  // Note: this comment is intentionally using c++ style to allow writing the '*/' sequence.
  //
  // - Remove C comments: /* ... */
  // - Remove C++ comments: // ...
  // - Escape certain strings:  '...*/...' to '...*' + '/...'
  // - Rewrite inline regex:  /...*/igm to new RegExp('...*' + 'igm');
  private String parseJavascriptSource(String js) throws Exception {

    boolean isJS = true;
    boolean isSingQuot = false;
    boolean isDblQuot = false;
    boolean isSlash = false;
    boolean isCComment = false;
    boolean isCPPComment = false;
    boolean isRegex = false;
    boolean isOper = false;

    StringBuilder ret = new StringBuilder();
    String tmp = "";
    Character last = 0;
    Character prev = 0;

    for (int i = 0, l = js.length(); i < l; i++) {
      Character c = js.charAt(i);
      String out = c.toString();

      if (isJS) {
        isDblQuot = c == '"';
        isSingQuot = c == '\'';
        isSlash = c == '/';
        isJS = !isDblQuot && !isSingQuot && !isSlash;
        if (!isJS) {
          out = tmp = "";
          isCPPComment = isCComment = isRegex = false;
        }
      } else if (isSingQuot) {
        isJS = !(isSingQuot = last == '\\' || c != '\'');
        if (isJS)
          out = escapeQuotedString(tmp, c);
        else
          tmp += c;
      } else if (isDblQuot) {
        isJS = !(isDblQuot = last == '\\' || c != '"');
        if (isJS)
          out = escapeQuotedString(tmp, c);
        else
          tmp += c;
      } else if (isSlash) {
        if (!isCPPComment && !isCComment && !isRegex && !isOper) {
          isCPPComment = c == '/';
          isCComment = c == '*';
          isOper = !isCPPComment && !isCComment && !"=(&|?:;},".contains("" + prev);
          isRegex = !isCPPComment && !isCComment && !isOper;
        }
        if (isOper) {
          isJS = !(isSlash = isOper = false);
          out = "" + last + c;
        } else if (isCPPComment) {
          isJS = !(isSlash = isCPPComment = c != '\n');
          if (isJS)
            out = "\n";
        } else if (isCComment) {
          isSlash = isCComment = !(isJS = last == '*' && c == '/');
          if (isJS)
            out = "";
        } else if (isRegex) {
          isJS = !(isSlash = isRegex = last == '\\' || c != '/');
          if (isJS) {
            String mod = "";
            while (++i < l) {
              c = js.charAt(i);
              if ("igm".contains("" + c))
                mod += c;
              else
                break;
            }
            out = escapeInlineRegex(tmp, mod) + c;
          } else {
            tmp += c;
          }
        } else {
          isJS = true;
        }
      }

      if (isJS) {
        ret.append(out);
      }
      if (last != ' ') {
        prev = last;
      }
      last = prev == '\\' && c == '\\' ? 0 : c;
    }
    return ret.toString();
  }

  private String escapeQuotedString(String s, Character quote) {
    return quote + s.replace("*/", "*" + quote + " + " + quote + "/") + quote;
  }

  private String escapeInlineRegex(String s, String mod) {
    if (s.endsWith("*")) {
      return "new RegExp('" + s.replace("\\", "\\\\") + "','" + mod + "')";
    } else {
      return '/' + s + '/' + mod;
    }
  }

  private String inputStreamToString(InputStream in) throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    byte[] buffer = new byte[4096];
    int read = in.read(buffer);
    while (read != -1) {
      bytes.write(buffer, 0, read);
      read = in.read(buffer);
    }
    in.close();
    return bytes.toString();
  }
}
