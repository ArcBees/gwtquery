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
package com.google.gwt.query.client;


import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.query.client.plugins.callbacks.Callbacks;
import com.google.gwt.query.client.plugins.callbacks.Callbacks.Callback;

/**
 * Test class for testing deferred and callbacks stuff.
 */
public class GQueryDeferredTestGwt extends GWTTestCase {

  public String getModuleName() {
    return "com.google.gwt.query.Query";
  }

  private String result = "";
  
  public void testCallbacks() {
    Function fn1 = new Function() {
      public void f() {
        String s = " f1:";
        for (Object o: getData()){
          s += " " + o;
        }
        result += s;
      }
    };
    
    Callback fn2 = new Callback() {
      public boolean f(Object... objects) {
        String s = " f2:";
        for (Object o: objects){
          s += " " + o;
        }
        result += s;
        return false;
      }
    };
    
    com.google.gwt.core.client.Callback<Object, Object> fn3 = new com.google.gwt.core.client.Callback<Object, Object>() {
      public void onFailure(Object reason) {
        String s = " f3_fail: " + reason;
        System.out.println(s);
      }
      public void onSuccess(Object objects) {
        String s = " f3_success:";
        for (Object o: (Object[])objects){
          s += " " + o;
        }
        result += s;
      }
    };
    
    
    
    result = "";
    Callbacks callbacks = new Callbacks();
    callbacks.add( fn1 );
    callbacks.fire( "foo" );
    assertEquals(" f1: foo", result);
    
    result = "";
    callbacks.add( fn2 );
    callbacks.fire( "bar" );
    assertEquals(" f1: bar f2: bar", result);

    result = "";
    callbacks.remove( fn2 );
    callbacks.fire( "foobar" );
    assertEquals(" f1: foobar", result);

    result = "";
    callbacks.add( fn1 );
    callbacks.fire( "foo" );
    assertEquals(" f1: foo f1: foo", result);

    result = "";
    callbacks = new Callbacks("unique");
    callbacks.add( fn1 );
    callbacks.add( fn1 );
    callbacks.fire( "foo" );
    assertEquals(" f1: foo", result);

    result = "";
    callbacks.add( fn3 );
    callbacks.fire( "bar" );
    assertEquals(" f1: bar f3_success: bar", result);
    
    result = "";
    callbacks = new Callbacks("memory");
    callbacks.add( fn1 );
    callbacks.fire( "foo" );
    callbacks.add( fn2 );
    callbacks.fire( "bar" );
    assertEquals(" f1: foo f1: foo bar f2: foo bar", result);

    result = "";
    callbacks = new Callbacks("stopOnFalse");
    callbacks.add( fn2 );
    callbacks.add( fn1 );
    callbacks.fire( "bar" );
    assertEquals(" f2: bar", result);
    
    result = "";
    callbacks.disable();
    callbacks.fire( "bar" );
    assertEquals("", result);

    result = "";
    callbacks = new Callbacks("once");
    callbacks.add( fn1 );
    callbacks.fire( "bar" );
    assertEquals(" f1: bar", result);
    callbacks.fire( "foo" );
    assertEquals(" f1: bar", result);
  }

}
