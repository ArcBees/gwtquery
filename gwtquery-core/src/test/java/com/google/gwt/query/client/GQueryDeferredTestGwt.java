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
import com.google.gwt.query.client.plugins.ajax.Ajax;
import com.google.gwt.query.client.plugins.deferred.Callbacks;
import com.google.gwt.query.client.plugins.deferred.Callbacks.Callback;
import com.google.gwt.query.client.plugins.deferred.PromiseFunction;
import com.google.gwt.user.client.Timer;

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
      public Object f(Object...arguments) {
        String s = " f1:";
        for (Object o: arguments){
          s += " " + o;
        }
        result += s;
        return false;
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
        result += " f3_fail: " + reason;
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
    callbacks.remove(fn2);
    callbacks.fire( "foobar" );
    assertEquals(" f1: foo f2: foo f1: bar f2: bar f1: foobar", result);

    result = "";
    callbacks = new Callbacks("stopOnFalse");
    callbacks.add( fn1 );
    callbacks.add( fn2 );
    callbacks.fire( "bar" );
    assertEquals(" f1: bar", result);
    
    result = "";
    callbacks.disable();
    callbacks.fire( "bar" );
    assertEquals("", result);

    result = "";
    callbacks = new Callbacks("memory once unique");
    callbacks.add( fn1 );
    callbacks.add( fn1 );
    callbacks.fire( "bar" );
    assertEquals(" f1: bar", result);
    callbacks.fire( "foo" );
    assertEquals(" f1: bar", result);
    callbacks.add( fn2 );
    callbacks.add( fn2 );
    assertEquals(" f1: bar f2: bar f2: bar", result);
    callbacks.remove( fn1 );
    callbacks.add( fn1 );
    assertEquals(" f1: bar f2: bar f2: bar f1: bar", result);
    callbacks.remove( fn1 );
    callbacks.disable();
    callbacks.add( fn1 );
    assertEquals(" f1: bar f2: bar f2: bar f1: bar", result);
  }
  
      public void testDeferredAjaxWhenDone() {
        String url = "https://www.googleapis.com/blogger/v2/blogs/user_id/posts/post_id?callback=?&key=NO-KEY";
        
        delayTestFinish(5000);
        GQuery.when(Ajax.getJSONP(url, null, null, 1000))
          .done(new Function() {
            public void f() {
              Properties p = getArgument(0, 0);
              assertEquals(400, p.getProperties("error").getInt("code"));
              finishTest();
            }
          });
      }
    
      public void testDeferredAjaxWhenFail() {
        String url1 = "https://www.googleapis.com/blogger/v2/blogs/user_id/posts/post_id?callback=?&key=NO-KEY";
        String url2 = "https://localhost:4569/foo";
        
        delayTestFinish(5000);
        GQuery.when(
            Ajax.getJSONP(url1), 
            Ajax.getJSONP(url2, null, null, 1000))
          .done(new Function() {
            public void f() {
              fail();
            }
          })
          .fail(new Function(){
            public void f() {
              finishTest();
            }
          });
      }
      
      int progress = 0;
      public void testPromiseFunction() {
        delayTestFinish(3000);
        final Promise doSomething = new PromiseFunction() {
          @Override
          public void f(final Deferred dfd) {
            new Timer() {
              int count = 0;
              public void run() {
                dfd.notify(count ++);
                if (count > 3) {
                  cancel();
                  dfd.resolve("done");
                }
              }
            }.scheduleRepeating(50);
          }
        };
        
        doSomething.progress(new Function() {
          public void f() {
            progress = getArgument(0);
          }
        }).done(new Function() {
          public void f() {
            assertEquals(3, progress);
            assertEquals(Promise.RESOLVED, doSomething.state());
            finishTest();
          }
        });
      }
      
      public void testNestedPromiseFunction() {
        progress = 0;
        delayTestFinish(3000);
        
        Promise doingFoo = new PromiseFunction() {
          public void f(final Deferred dfd) {
            new Timer() {
              int count = 0;
              public void run() {
                dfd.notify(count ++);
                if (count > 3) {
                  cancel();
                  dfd.resolve("done");
                }
              }
            }.scheduleRepeating(50);
          }
        };
        
        Promise doingBar = new PromiseFunction() {
          public void f(final Deferred dfd) {
            new Timer() {
              int count = 0;
              public void run() {
                dfd.notify(count ++);
                if (count > 3) {
                  cancel();
                  dfd.resolve("done");
                }
              }
            }.scheduleRepeating(50);
          }
        };
        
        GQuery.when(doingFoo, doingBar).progress(new Function() {
          public void f() {
            int c = getArgument(0);
            progress += c;
          }
        }).done(new Function() {
          public void f() {
            assertEquals(12, progress);
            finishTest();
          }
        });
      }

      public void testWhen() {
        new PromiseFunction() {
          public void f(final Deferred dfd) {
            dfd.resolve(5);
          }
        }.done(new Function() {
          public void f() {
            assertEquals(5d, arguments(0));
          }
        }).then(new Function() {
          public Object f(Object... args) {
            return (Double)args[0] * 2;
          }
        }).done(new Function() {
          public void f() {
            assertEquals(10d, arguments(0));
          }
        });
      }

}
