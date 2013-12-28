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

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQ;
import com.google.gwt.query.client.Properties;
import com.google.gwt.query.client.builders.JsonBuilder;
import com.google.gwt.query.client.builders.Name;
import com.google.gwt.query.client.plugins.ajax.Ajax;

/**
 * Tests for Deferred which can run either in JVM and GWT
 */
public class AjaxTest extends GWTTestCase {

  public String getModuleName() {
    return null;
  }
  
  public void testJsonValidService() {
    delayTestFinish(5000);
    // Use a public json service
    String testJsonpUrl = "https://www.googleapis.com/blogger/v2/blogs/user_id/posts/post_id?callback=?&key=NO-KEY";
    Ajax.getJSONP(testJsonpUrl, new Function(){
      public void f() {
        Properties p = getDataProperties();
        // It should return error since we do not use a valid key
        // {"error":{"errors":[{"domain":"usageLimits","reason":"keyInvalid","message":"Bad Request"}],"code":400,"message":"Bad Request"}}
        assertEquals(400, p.getJavaScriptObject("error").<Properties>cast().getInt("code"));
        finishTest();
      }
    }, null, 0);
  }

}