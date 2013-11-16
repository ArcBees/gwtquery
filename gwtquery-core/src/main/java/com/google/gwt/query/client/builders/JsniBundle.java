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
package com.google.gwt.query.client.builders;

import static java.lang.annotation.ElementType.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A tag interface that is used in the generation of jsni bundles. 
 * 
 * A jsni-bundle is a class with jsni methods whose content is taken from
 * external handwritten javascript files.
 * 
 * The goals of using this technique are:
 *   - Use pure javascript files so as we can use IDEs for editing, formating etc.
 *   - Ability to test js files directly in the browser without compiling.
 *   - Include 3party libraries without modification of the original sources.
 *   - Not need of including javascript tags to include 3party js.
 *   - GWT compiler will get rid of these jsni fragments if the application
 *     does not use any method.
 *   - Included javascript will take advantage of GWT jsni validators, obfuscators
 *     and optimizers.
 * <pre>
    public interface HighCharts extends JsniBundle {
      @LibrarySource("highcharts.js")
      public void initialize();

      @MethodSource("createChart.js")
      public JavaScriptObject createChart(String elementId, JavaScriptObject config);
   }

   // Generate the Bundle implementation
   HighCharts library = GWT.create(HighCharts.class);

   // Initialize the 3party library
   library.initialize();

   // Create a new chart
   JavaScriptObject chart = hc.createChart("container", properties);

 * </pre>
 */
public interface JsniBundle {
  
  /**
   * Annotation to mark inclusion of 3 party libraries.
   *  
   * The content is wrapped with an inner javascript function to set
   * the context to the document window instead of the iframe where GWT
   * code is run. 
   */
  @Target({METHOD})
  @Retention(RetentionPolicy.RUNTIME)
  @Documented
  public @interface LibrarySource {
    /**
     * The external file to be included
     */
    String value();

    /**
     * Fragment of code to include before the external javascript is
     * written.
     */
    String prepend() default "(function(window, document, console){\n";

    /**
     * Fragment of code to include after the external javascript has been
     * written.
     */
    String postpend() default "\n}.apply($wnd, [$wnd, $doc, $wnd.console]));"; 
  }
  
  /**
   * Annotation to mark inclusion of jsni code writen in external js files.
   *  
   * The content is not wrapped by default, so the developer has the responsibility
   * to return the suitable value and to handle correctly parameters.
   */
  @Target({METHOD})
  @Retention(RetentionPolicy.RUNTIME)
  @Documented
  public @interface MethodSource {
    /**
     * The external file to be included
     */
    String value();

    /**
     * Fragment of code to include before the external javascript is
     * written.
     */
    String prepend() default "";

    /**
     * Fragment of code to include after the external javascript has been
     * written.
     */
    String postpend() default ""; 
  }
}
