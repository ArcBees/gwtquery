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
package gwtquery.samples.client;

import static com.google.gwt.query.client.GQuery.$;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Element;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.plugins.Effects;

/**
 * @author Manolo Carrasco
 */
public class GwtQueryImageZoom implements EntryPoint {

  public void onModuleLoad() {
    // Fancy Thumbnail Hover Effect w/ jQuery - by Soh Tanaka
    // http://www.sohtanaka.com/web-design/examples/image-zoom/
    $("ul.thumb li").hover(new Function() {
      public void f(Element e) {
      $(e).css("z-index", "10").find("img").addClass("hover")
        .as(Effects.Effects).stop()
        .animate("marginTop: '-110px', marginLeft: '-110px', top: '50%', left: '50%', width: '174px', height: '174px', padding: '20px'", 200);
      }} , new Function() {
        public void f(Element e) {
      $(e).css("z-index", "0").find("img").removeClass("hover")
        .as(Effects.Effects).stop()
        .animate("marginTop: '0', marginLeft: '0', top: '0%', left: '0%', width: '100px', height: '100px', padding: '5px'", 600);
    }});

  }
}
