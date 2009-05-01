/*
 * Copyright 2009 Google Inc.
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

import com.google.gwt.animation.client.Animation;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;

/**
 * Effects plugin for Gwt Query.
 */
public class Effects extends GQuery {

  /**
   * Used to register the plugin.
   */
  private static class EffectsPlugin implements Plugin<Effects> {

    public Effects init(GQuery gq) {
      return new Effects(gq.get());
    }
  }

  public static final Class<Effects> Effects = Effects.class;

  static {
    GQuery.registerPlugin(Effects.class, new EffectsPlugin());
  }

  public Effects(Element element) {
    super(element);
  }

  public Effects(JSArray elements) {
    super(elements);
  }

  public Effects(NodeList list) {
    super(list);
  }

  public Effects fadeIn() {
    Animation a = new Animation() {

      public void onCancel() {
      }

      public void onComplete() {
      }

      public void onStart() {
      }

      public void onUpdate(double progress) {
        for (int i = 0; i < elements.getLength(); i++) {
          elements.getItem(i).getStyle()
              .setProperty("opacity", String.valueOf(progress));
        }
      }
    };
    a.run(1000);
    return this;
  }

  public Effects fadeOut() {
    Animation a = new Animation() {

      public void onCancel() {
      }

      public void onComplete() {
        for (int i = 0; i < elements.getLength(); i++) {
          elements.getItem(i).getStyle().setProperty("opacity", "0");
          elements.getItem(i).getStyle().setProperty("display", "none");
        }
      }

      public void onStart() {
      }

      public void onUpdate(double progress) {
        for (int i = 0; i < elements.getLength(); i++) {
          elements.getItem(i).getStyle()
              .setProperty("opacity", String.valueOf(1.0 - progress));
        }
      }
    };
    a.run(1000);
    return this;
  }

  public Effects hide() {
    this.css("display", "none");
    return this;
  }

  public Effects show() {
    this.css("display", "");
    return this;
  }

  public Effects toggle() {
    for (Element e : elements()) {
      Effects ef = new Effects(e);
      if (ef.visible()) {
        ef.hide();
      } else {
        ef.show();
      }
    }
    return this;
  }

  public boolean visible() {
    return !"none".equalsIgnoreCase(this.css("display"));
  }
}
