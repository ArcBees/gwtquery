package com.google.gwt.query.client;

import junit.framework.Test;
import junit.framework.TestCase;

import com.google.gwt.junit.tools.GWTTestSuite;
import com.google.gwt.query.client.impl.SelectorEnginesTestGwt;

/**
 * Class to run all Gwt test in a jvm instance.
 * It speeds up maven testing phase.
 */
public class GQueryGwtSuiteTest extends TestCase
{
  public static Test suite()
  {
      GWTTestSuite suite = new GWTTestSuite( "GQuery Suite" );
      suite.addTestSuite(GQuerySelectorsTestGwt.class);
      suite.addTestSuite(GQueryCoreTestGwt.class);
      suite.addTestSuite(GQueryCssTestGwt.class);
      suite.addTestSuite(GQueryEventsTestGwt.class);
      suite.addTestSuite(GQueryEffectsTestGwt.class);
      suite.addTestSuite(GQueryJsTestGwt.class);
      suite.addTestSuite(GQueryWidgetsTestGwt.class);
      suite.addTestSuite(GQueryAjaxTestGwt.class);
      suite.addTestSuite(SelectorEnginesTestGwt.class);
      return suite;
  }


}
