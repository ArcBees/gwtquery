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
package com.google.gwt.query.client.ajax;

import com.google.gwt.query.servlet.GQAjaxTestServlet;
import com.google.gwt.query.vm.AjaxTransportJre;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerWrapper;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.webapp.WebAppClassLoader;
import org.eclipse.jetty.webapp.WebAppContext;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.Servlet;

/**
 * Tests for Data Binders and Ajax run in the JVM
 */
public class AjaxTestJre extends AjaxTests {

  static Server server;
  static int port = new Random().nextInt(1000) + 2000;

  public String getModuleName() {
    return null;
  }

  public AjaxTestJre() throws Exception {
    // Disable stderr, so as stack traces of expected failures do not
    // mess the output.
    System.setErr(new PrintStream(new ByteArrayOutputStream()));

    String localDomain = "http://127.0.0.1:" + port;
    AjaxTransportJre.enableCORS(localDomain);
    String corsDomain = "http://localhost:" + port;

    echoUrl = localDomain + "/" + servletPath;
    echoUrlCORS = corsDomain + "/" + servletPath + "?cors=true";

    startWebServer(port);
  }

  protected void startWebServer(int port) throws Exception {
    if (server == null) {
      final Map<String, Class<? extends Servlet>> servlets = new HashMap<>();
      servlets.put("/" + servletPath, GQAjaxTestServlet.class);
      server = createWebServer(port, ".", null, servlets, null);
    }
  }

  public static Server createWebServer(final int port, final String resourceBase, final String[] classpath,
      final Map<String, Class<? extends Servlet>> servlets, final HandlerWrapper handler) throws Exception {

    final Server server = new Server(port);

    final WebAppContext context = new WebAppContext();
    context.setContextPath("/");
    context.setResourceBase(resourceBase);

    if (servlets != null) {
      for (final Map.Entry<String, Class<? extends Servlet>> entry : servlets.entrySet()) {
        final String pathSpec = entry.getKey();
        final Class<? extends Servlet> servlet = entry.getValue();
        context.addServlet(servlet, pathSpec);

        // disable defaults if someone likes to register his own root servlet
        if ("/".equals(pathSpec)) {
          context.setDefaultsDescriptor(null);
          context.addServlet(DefaultServlet.class, "/favicon.ico");
        }
      }
    }

    final WebAppClassLoader loader = new WebAppClassLoader(context);
    if (classpath != null) {
      for (final String path : classpath) {
        loader.addClassPath(path);
      }
    }
    context.setClassLoader(loader);
    if (handler != null) {
      handler.setHandler(context);
      server.setHandler(handler);
    } else {
      server.setHandler(context);
    }
    server.start();
    return server;
  }
}
