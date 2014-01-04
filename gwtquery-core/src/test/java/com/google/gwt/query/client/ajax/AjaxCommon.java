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

import static com.google.gwt.query.client.GQuery.*;

import org.mortbay.jetty.Server;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

import com.gargoylesoftware.htmlunit.javascript.host.xml.XMLHttpRequest;
import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.query.client.Binder;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQ;
import com.google.gwt.query.client.Properties;
import com.google.gwt.query.client.plugins.ajax.Ajax;
import com.google.gwt.query.client.plugins.ajax.Ajax.Settings;

/**
 * Tests for Deferred which can run either in JVM and GWT
 */
public abstract class AjaxCommon extends GWTTestCase {

  public String getModuleName() {
    return null;
  }
  
  protected String echoUrl, corsUrl;
  protected Binder json, jsonData;
  protected String servletPath = "test.json";
  
  private void performAjaxJsonTest(Settings s) {
    delayTestFinish(5000);
    Ajax.ajax(s).done(new Function(){public void f() {
      Binder p = arguments(0);
      assertEquals("abc", p.get("a"));
      finishTest();
    }}).fail(new Function(){public void f() {
      fail();
    }});
  }

  public void testAjaxJsonPost() {
    delayTestFinish(5000);
    Settings s = Ajax.createSettings()
      .setUrl(echoUrl)
      .setData(json)
      .setDataType("json")
      .setUsername("testuser")
      .setPassword("testpassword")
      ;
    performAjaxJsonTest(s);
  }
  
  public void testAjaxJsonPost_CORS() {
    delayTestFinish(5000);
    Settings s = Ajax.createSettings()
      .setUrl(corsUrl)
      .setData(json)
      .setDataType("json");
    
    performAjaxJsonTest(s);
  }
  
  public void testAjaxJsonGet() {
    Settings s = Ajax.createSettings()
      .setType("get")
      .setUrl(echoUrl)
      .setData(jsonData)
      .setDataType("json");

    performAjaxJsonTest(s);
  }
  
  public void testAjaxJsonGet_CORS() {
    Settings s = Ajax.createSettings()
      .setType("get")
      .setUrl(corsUrl)
      .setData(jsonData)
      .setDataType("json");

    performAjaxJsonTest(s);
  }
  
  public void testAjaxGetJsonP() {
    delayTestFinish(5000);
    Settings s = Ajax.createSettings()
      .setType("post")
      .setUrl(echoUrl)
      .setData(jsonData)
      .setDataType("jsonp");

    performAjaxJsonTest(s);
  }
  
  public void testAjaxGetJsonP_CORS() {
    delayTestFinish(5000);
    Settings s = Ajax.createSettings()
      .setType("post")
      .setUrl(corsUrl)
      .setData(jsonData)
      .setDataType("jsonp");

    performAjaxJsonTest(s);
  }
  
  
  
//  public void testAjaxJson() {
//    delayTestFinish(5000);
//    Settings s = Ajax.createSettings()
//      .setType("get")
//      .setUrl(GWT.getModuleBaseURL() + "test.json")
//      .setData($$("data: {a: abc, d: ddd}"))
//      .setDataType("json");
//
//    Ajax.ajax(s).done(new Function(){public void f() {
//      Binder p = arguments(0);
//      assertEquals("abc", p.get("a"));
//      finishTest();
//    }}).fail(new Function(){public void f() {
//      fail();
//    }});
//  }
  public void testJsonValidService() {
    delayTestFinish(5000);
    // Use a public json service
    String testJsonpUrl = "https://www.googleapis.com/blogger/v2/blogs/user_id/posts/post_id?callback=?&key=NO-KEY";
    Ajax.getJSONP(testJsonpUrl, new Function(){
      public void f() {
        Binder p = arguments(0);
        // It should return error since we do not use a valid key
        // {"error":{"errors":[{"domain":"usageLimits","reason":"keyInvalid","message":"Bad Request"}],"code":400,"message":"Bad Request"}}
        assertEquals(400, p.<Binder>get("error").get("code"));
        finishTest();
      }
    }, null, 0);
  }

}