package com.google.gwt.query.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GQAjaxTestServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;
  String name = this.getClass().getSimpleName() + " ";

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    System.out.println(name + req.getMethod() + " " + req.getContentType());
    Enumeration<String> en = req.getHeaderNames();
    while (en.hasMoreElements()) {
      String s = (String) en.nextElement();
      System.out.println(name + s + " " + req.getHeader(s));
    }
    
    
    String t = req.getParameter("timeout");
    if (t != null && t.matches("\\d+")) {
      try {
        int ms = Integer.parseInt(t);
        System.out.println("  Sleeping: " + ms);
        Thread.sleep(ms);
      } catch (Exception e) {
      }
    }

    String data = "";
    if (req.getMethod().equalsIgnoreCase("get")) {
      data = req.getParameter("data") != null ? req.getParameter("data") : "";
      if (req.getParameter("callback") != null) {
        data = req.getParameter("callback")  + "(" + data + ");";
      }
    } else if (req.getMethod().equalsIgnoreCase("post") 
        && req.getContentType().toLowerCase().startsWith("application/json")) {
      BufferedReader reader = req.getReader();
      String line;
      while ((line = reader.readLine()) != null)
        data += line;
    }
    
    String origin = req.getHeader("Origin");
    if ("true".equals(req.getParameter("cors")) && origin != null) {
      resp.addHeader("Access-Control-Allow-Origin", origin);
      resp.addHeader("Access-Control-Allow-Credentials", "true");
      String method = req.getHeader("Access-Control-Request-Method");
      if (method != null) {
        resp.addHeader("Access-Control-Allow-Methods", method);
        resp.setHeader("Allow", "GET, HEAD, POST, PUT, DELETE, TRACE, OPTIONS");
      }
      String headers = req.getHeader("Access-Control-Request-Headers");
      if (headers != null) {
        resp.addHeader("Access-Control-Allow-Headers", headers);
      }
    }
    
    PrintWriter p = resp.getWriter();
    p.print(data);
    p.flush();
    p.close();
    
    System.out.println(name + "returns: " + data);
  }
}
