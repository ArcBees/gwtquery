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


import static com.google.gwt.query.client.GQuery.$;
import static com.google.gwt.query.client.GQuery.$$;
import static com.google.gwt.query.client.GQuery.document;

import com.google.gwt.core.client.Duration;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.query.client.plugins.ajax.Ajax;
import com.google.gwt.query.client.plugins.deferred.PromiseFunction;
import com.google.gwt.query.client.plugins.effects.Fx;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Test class for testing deferred and callbacks stuff.
 */
public class GQueryDeferredTestGwt extends GWTTestCase {

  // FIXME: Investigate why changing this with dom.Element makes
  // testDeferredEffectEach fail.
  static Element e = null;

  static HTML testPanel = null;

  public String getModuleName() {
    return "com.google.gwt.query.QueryTest";
  }

  public void gwtTearDown() {
    $(e).remove();
    e = null;
  }

  public void gwtSetUp() {
    if (e == null) {
      testPanel = new HTML();
      RootPanel.get().add(testPanel);
      e = testPanel.getElement();
      e.setId("core-tst");
    } else {
      e.setInnerHTML("");
    }
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
        progress = this.<Integer>getArgument(0);
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
        int c = this.<Integer>getArgument(0);
        progress += c;
      }
    }).done(new Function() {
      public void f() {
        assertEquals(12, progress);
        finishTest();
      }
    });
  }

  public void testDeferredAjaxThenDone() {
    final String url = "https://www.googleapis.com/blogger/v2/blogs/user_id/posts/post_id?callback=?&key=NO-KEY";

    delayTestFinish(5000);
    GQuery
      .when(Ajax.getJSONP(url))
      .then(new Function() {
        public Object f(Object... args) {
          Properties p = arguments(0, 0);
          assertEquals(400, p.getProperties("error").getInt("code"));
          return Ajax.getJSONP(url);
        }
      })
      .done(new Function() {
        public void f() {
          Properties p = arguments(0, 0);
          assertEquals(400, p.getProperties("error").getInt("code"));
          finishTest();
        }
      });
  }

  public void testDeferredQueueDelay() {
    final int delay = 300;
    final double init = Duration.currentTimeMillis();

    delayTestFinish(delay * 2);

    Function doneFc = new Function() {
      public void f() {
        finishTest();

        double ellapsed = Duration.currentTimeMillis() - init;
        assertTrue(ellapsed >= delay);
      }
    };

    $(document).delay(delay).promise().done(doneFc);
  }

  int deferredRun = 0;
  public void testDeferredQueueMultipleDelay() {
    final int delay = 300;
    final double init = Duration.currentTimeMillis();
    deferredRun = 0;

    delayTestFinish(delay * 3);

    $("<div>a1</div><div>a2</div>")
      .delay(delay, new Function() {
        public void f() {
          double ellapsed = Duration.currentTimeMillis() - init;
          assertTrue(ellapsed >= delay);
          deferredRun ++;
        }
      })
      .delay(delay, new Function() {
        public void f() {
          double ellapsed = Duration.currentTimeMillis() - init;
          assertTrue(ellapsed >= (delay * 2));
          deferredRun ++;
        }
      })
      .promise().done(new Function() {
        public void f() {
          finishTest();
          // Functions are run 4 times (2 functions * 2 elements)
          assertEquals(4, deferredRun);
        }
      });
  }

  /**
   * Example taken from the gquery.promise() documentation
   */
  public void testDeferredEffect() {
    $(e).html("<button>click</button><p>Ready...</p><br/><div></div>");
    $("div", e).css($$("height: 50px; width: 50px;float: left; margin-right: 10px;display: none; background-color: #090;"));

    final Function effect = new Function() {public Object f(Object... args) {
        return $("div", e).fadeIn(800).delay(1200).fadeOut();
    }};

    final double init = Duration.currentTimeMillis();

    delayTestFinish(10000);

    $("button", e)
      .click(new Function(){public void f()  {
        $("p", e).append(" Started... ");
        GQuery.when( effect ).done(new Function(){public void f()  {
          $("p", e).append(" Finished! ");
          assertEquals("Ready... Started...  Finished! ", $("p", e).text());

          double ellapsed = Duration.currentTimeMillis() - init;
          assertTrue(ellapsed >= (800 + 1200 + 400));

          finishTest();
        }});
      }})
    .click();
  }

  /**
   * Example taken from the gquery.promise() documentation
   */
  public void testDeferredEffectEach() {
    $(e).html("<button>click</button><p>Ready...</p><br/><div></div><div></div><div></div><div></div>");
    $("div", e).css($$("height: 50px; width: 50px;float: left; margin-right: 10px;display: none; background-color: #090;"));

    final double init = Duration.currentTimeMillis();

    delayTestFinish(10000);

    $("button", e)
      .bind("click", new Function(){public void f()  {
        $("p", e).append(" Started... ");

        $("div",e).each(new Function(){public Object f(Element e, int i) {
          return $( this ).fadeIn().fadeOut( 1000 * (i+1) );
        }});

        $("div", e).promise().done(new Function(){ public void f() {
          $("p", e).append( " Finished! " );

          assertEquals("Ready... Started...  Finished! ", $("p", e).text());
          double ellapsed = Duration.currentTimeMillis() - init;
          assertTrue(ellapsed >= (1000 * 4));

          finishTest();
        }});
      }})
     .click();
  }

  public void testWhenArgumentsWhithAnyObject() {
    $(e).html("<div>a1</div><div>a2</div>");

    final GQuery g = $("div", e);
    assertEquals(2, g.length());

    // We can pass to when any object.
    GQuery.when(g, g.delay(100).delay(100), "Foo", $$("{bar: 'foo'}"))
          .done(new Function(){public void f() {
              GQuery g1 = arguments(1, 0);
              GQuery g2 = arguments(1, 0);
              String foo = arguments(2, 0);
              Properties p = arguments(3, 0);

              // We dont compare g and g1/g2 because they are different
              // objects (GQuery vs QueuePlugin) but we can compare its content
              assertEquals(g.toString(), g1.toString());
              assertEquals(g.toString(), g2.toString());

              assertEquals("Foo", foo);
              assertEquals("foo", p.get("bar"));
          }});
  }
}
