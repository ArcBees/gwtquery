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

import static java.lang.annotation.ElementType.METHOD;

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
 *   - Use pure javascript files so as we can use IDEs for editing, formating etc,
 *     instead of dealing with code in comment blocks.
 *   - Facilitate writing and testing javascript in the browser before compiling it.
 *   - Include third-party javascript libraries without modification of the original source.
 *   - Not need of adding javascript tags in the html page or module file to include
 *     third-party javascript.
 *   - GWT compiler will get rid of any jsni fragment if the application does not use it.
 *   - Included javascript will take advantage of GWT jsni validators, obfuscators
 *     and optimizers.
 *
 * In summary, this mechanism facilitates the creation of GWT libraries wrapping or using
 * external javascript code, hence the developer does not have to take care of tags,
 * and leaving gwt compiler the decission to include external code when it is actually
 * required.
 *
 * <pre>
    public interface HighCharts extends JsniBundle {
      @LibrarySource("highcharts.js")
      public void initialize();

      @MethodSource("createChart.js")
      public JavaScriptObject createChart(String elementId, JavaScriptObject config);
   }

   // Generate the Bundle implementation
   HighCharts library = GWT.create(HighCharts.class);

   // Initialize the third-party library
   library.initialize();

   // Create a new chart
   JavaScriptObject chart = hc.createChart("container", properties);

 * </pre>
 */
public interface JsniBundle {

  /**
   * Annotation to mark inclusion of third-party libraries.
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

    /**
     * Regular expression to run over the javascript code to import.
     *
     * You should add pairs of values with the same syntax than String.replaceAll.
     */
    String[] replace() default {};
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

    /**
     * Regular expression to run over the javascript code to import.
     *
     * You should add pairs of values with the same syntax than String.replaceAll.
     */
    String[] replace() default {};
  }
}
