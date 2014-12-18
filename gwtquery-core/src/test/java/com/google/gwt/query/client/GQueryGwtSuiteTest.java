package com.google.gwt.query.client;

import junit.framework.Test;

import com.google.gwt.junit.tools.GWTTestSuite;
import com.google.gwt.query.client.ajax.AjaxTestJre;
import com.google.gwt.query.client.ajax.AjaxTestGwt;
import com.google.gwt.query.client.dbinding.DataBindingTestJre;
import com.google.gwt.query.client.dbinding.DataBindingTestGwt;
import com.google.gwt.query.client.deferred.DeferredTestGwt;
import com.google.gwt.query.client.impl.SelectorEnginesTestGwt;

/**
 * Class to run all Gwt test in a jvm instance.
 * It speeds up maven testing phase.
 */
public class GQueryGwtSuiteTest extends GWTTestSuite {
  public static Test suite() {
      GWTTestSuite suite = new GWTTestSuite( "GQuery Suite" );
      suite.addTestSuite(AjaxTestJre.class);
      suite.addTestSuite(GQueryJsInteropTestGwt.class);
      suite.addTestSuite(DataBindingTestJre.class);
      suite.addTestSuite(DataBindingTestGwt.class);
      suite.addTestSuite(GQueryAjaxTestGwt.class);
      suite.addTestSuite(AjaxTestGwt.class);
      suite.addTestSuite(GQueryDeferredTestGwt.class);
      suite.addTestSuite(DeferredTestGwt.class);
      suite.addTestSuite(GQuerySelectorsTestGwt.class);
      suite.addTestSuite(GQueryCoreTestGwt.class);
      suite.addTestSuite(GQueryCssTestGwt.class);
      suite.addTestSuite(GQueryEventsTestGwt.class);
      suite.addTestSuite(GQueryEffectsTestGwt.class);
      suite.addTestSuite(GQueryJsTestGwt.class);
      suite.addTestSuite(GQueryWidgetsTestGwt.class);
      suite.addTestSuite(SelectorEnginesTestGwt.class);
      return suite;
  }
}
