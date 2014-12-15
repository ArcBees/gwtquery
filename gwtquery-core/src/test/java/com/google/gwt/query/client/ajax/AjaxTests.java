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

import junit.framework.Assert;

import com.google.gwt.http.client.Response;
import com.google.gwt.junit.DoNotRunWith;
import com.google.gwt.junit.Platform;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.query.client.IsProperties;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQ;
import com.google.gwt.query.client.Promise;
import com.google.gwt.query.client.plugins.ajax.Ajax;
import com.google.gwt.query.client.plugins.ajax.Ajax.Settings;

/**
 * Common Tests for Data Binding and Ajax which can run either in JVM and GWT
 */
public abstract class AjaxTests extends GWTTestCase {

  protected String echoUrl, echoUrlCORS;
  protected IsProperties json, jsonGET;
  protected String servletPath = "test.json";

  private Function failFunction = new Function() {
    public void f() {
      fail();
    }
  };

  private Function finishFunction = new Function() {
    public void f() {
      finishTest();
    }
  };

  public AjaxTests() {
    jsonGET = GQ.create("data: {a: abc, d: def}", true);
    json = GQ.create("a: abc, d: def", true);
  }

  private Promise performAjaxJsonTest(Settings s) {
    delayTestFinish(5000);
    return Ajax.ajax(s)
      .done(new Function(){public void f() {
        IsProperties p = arguments(0);
        assertEquals("abc", p.get("a"));
        finishTest();
      }})
      .fail(failFunction);
  }

  private Promise performAjaxJsonTest_CORS(Settings s) {
    return performAjaxJsonTest(s)
      .done(new Function() {public void f() {
        Response r = arguments(3);
        assertNotNull(r.getHeader("Access-Control-Allow-Origin"));
      }});
  }

  public void testAjaxJsonPost() {
    delayTestFinish(5000);
    Settings s = Ajax.createSettings()
      .setUrl(echoUrl)
      .setData(json)
      .setDataType("json")
      .setUsername("testuser")
      .setPassword("testpassword");

    performAjaxJsonTest(s);
  }

  // This test needs htmlunit at least 2.11
  // https://groups.google.com/forum/#!msg/google-web-toolkit/dmyTt1Bh0pM/lBTIFiTyrpkJ
  //
  // It is necessary to patch RunStyleHtmlUnit because GWT default browser is FF3 since
  // minimun version in htmlunit-2.1x is FF3.6
  // It is necessary to patch BrowserChannel as well because convertFromJavaValue receives
  // non string objects under certain circumstances.
  @DoNotRunWith(Platform.HtmlUnitBug)
  public void testAjaxJsonPost_CORS() {
    delayTestFinish(5000);
    Settings s = Ajax.createSettings()
      .setUrl(echoUrlCORS)
      .setData(json)
      .setDataType("json");

    performAjaxJsonTest_CORS(s);
  }

  public void testAjaxJsonGet() {
    Settings s = Ajax.createSettings()
      .setType("get")
      .setUrl(echoUrl)
      .setData(jsonGET)
      .setDataType("json");

    performAjaxJsonTest(s);
  }

  @DoNotRunWith(Platform.HtmlUnitBug)
  public void testAjaxJsonGet_CORS() {
    Settings s = Ajax.createSettings()
      .setType("get")
      .setUrl(echoUrlCORS)
      .setData(jsonGET)
      .setDataType("json");

    performAjaxJsonTest_CORS(s)
      .done(new Function() {
          public void f() {
            Response r = arguments(3);
            Assert.assertNotNull(r.getHeader("Access-Control-Allow-Origin"));
            Assert.assertNull(r.getHeader("Access-Control-Allow-Credentials"));
          }
        });
  }

  @DoNotRunWith(Platform.HtmlUnitBug)
  public void testAjaxJsonGet_CORS_WithCredentials_Supported() {
    Settings s = Ajax.createSettings()
      .setType("get")
      // Enable credentials in servlet
      .setUrl(echoUrlCORS + "&credentials=true")
      .setData(jsonGET)
      .setDataType("json")
      .setWithCredentials(true);

    performAjaxJsonTest_CORS(s)
      .done(new Function() {
        public void f() {
          Response r = arguments(3);
          Assert.assertNotNull(r.getHeader("Access-Control-Allow-Origin"));
          Assert.assertNotNull(r.getHeader("Access-Control-Allow-Credentials"));
        }
      });
  }

  @DoNotRunWith(Platform.HtmlUnitBug)
  public void testAjaxJsonGet_CORS_WithCredentials_Unsupported() {
    Settings s = Ajax.createSettings()
      .setType("get")
      // Disable credentials in servlet
      .setUrl(echoUrlCORS)
      .setData(jsonGET)
      .setDataType("json")
      .setWithCredentials(true);

    Ajax.ajax(s)
      .fail(finishFunction)
      .done(failFunction);
  }

  public void testAjaxGetJsonP() {
    delayTestFinish(5000);
    Settings s = Ajax.createSettings()
      .setType("post")
      .setUrl(echoUrlCORS)
      .setData(jsonGET)
      .setDataType("jsonp");

    performAjaxJsonTest(s);
  }

  public void testJsonValidService() {
    delayTestFinish(5000);
    // Use a public json service supporting callback parameter
    Ajax.getJSONP("https://www.googleapis.com/blogger/v2/blogs/user_id/posts/post_id?callback=?&key=NO-KEY")
      .done(new Function(){
        public void f() {
          IsProperties p = arguments(0);
          // It should return error since we do not use a valid key
          // {"error":{"errors":[{"domain":"usageLimits","reason":"keyInvalid","message":"Bad Request"}],"code":400,"message":"Bad Request"}}
          assertEquals(400, p.<IsProperties>get("error").<Number>get("code").intValue());
          finishTest();
        }
      })
      .fail(failFunction);
  }

  public void testInvalidOrigin() {
    delayTestFinish(5000);
    Settings s = Ajax.createSettings()
      // Use a public json service non CORS enabled
      .setUrl("https://www.googleapis.com/blogger/v2/blogs/user_id/posts/post_id?key=NO-KEY")
      .setDataType("json")
      .setTimeout(1000);

    Ajax.ajax(s)
      .done(failFunction)
      .fail(finishFunction);
  }

  public void testJsonInvalidService() {
    delayTestFinish(5000);
    Settings s = Ajax.createSettings()
      // Use a valid javascript which does not wrap content in a callback
      .setUrl("http://ajax.googleapis.com/ajax/libs/jquery/1.8.1/jquery.min.js")
      .setDataType("jsonp")
      .setTimeout(1000);

    Ajax.ajax(s)
      .done(failFunction)
      .fail(finishFunction);
  }

  // For some reason htmlunit 2.16 does not raises a timeout, 2.9 does though,
  // when server sleeps or connection lasts a while. Tested with htmlunit 2.16
  @DoNotRunWith(Platform.HtmlUnitBug)
  public void testAjaxTimeout() {
    delayTestFinish(5000);
    Settings s = Ajax.createSettings()
      .setTimeout(100)
      .setType("get")
      // Connecting to private networks out of our LAN raises a timeout because
      // there is no route for them in public networks.
      .setUrl("http://10.32.45.67:7654");

    Ajax.ajax(s)
      .done(failFunction)
      .fail(finishFunction);
  }

  public void testJsonpTimeout() {
    delayTestFinish(5000);
    Settings s = Ajax.createSettings()
      .setTimeout(1000)
      .setDataType("jsonp")
      .setUrl(echoUrl + "?timeout=2000");

    Ajax.ajax(s)
      .done(failFunction)
      .fail(finishFunction);
  }

  public void testAjaxError() {
    delayTestFinish(5000);
    String url = "http://127.0.0.1/nopage";

    Ajax.ajax(Ajax.createSettings().setTimeout(1000).setUrl(url))
      .done(new Function(){
        public void f() {
          fail();
        }
      }).fail(new Function(){
        public void f() {
          finishTest();
        }
      });
  }

  public void testLoadScript() {
    delayTestFinish(5000);
    String url = "http://code.jquery.com/jquery-2.0.3.min.js";
    Ajax.loadScript(url)
      .done(new Function(){
        public void f() {
          finishTest();
        }
      }).fail(new Function(){
        public void f() {
          fail();
        }
      });
  }

  public void testGetScriptFail() {
    delayTestFinish(5000);
    String url = "http://127.0.0.1/nopage";
    Ajax.getScript(url)
      .done(new Function(){
        public void f() {
          fail();
        }
      }).fail(new Function(){
        public void f() {
          finishTest();
        }
      });
  }
}