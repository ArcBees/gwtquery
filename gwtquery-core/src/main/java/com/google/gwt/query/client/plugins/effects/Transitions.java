/*
 * Copyright 2014, The gwtquery team.
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
/*
 *
 * Copyright 2013, The gwtquery team.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.gwt.query.client.plugins.effects;

import static com.google.gwt.query.client.plugins.effects.Transform.getInstance;
import static com.google.gwt.query.client.plugins.effects.Transform.isTransform;
import static com.google.gwt.query.client.plugins.effects.Transform.transform;

import com.google.gwt.dom.client.Element;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.Properties;
import com.google.gwt.query.client.js.JsUtils;
import com.google.gwt.query.client.plugins.Effects;
import com.google.gwt.query.client.plugins.Plugin;
import com.google.gwt.query.client.plugins.effects.PropertiesAnimation.Easing;
import com.google.gwt.query.client.plugins.effects.PropertiesAnimation.EasingCurve;
import com.google.gwt.query.client.plugins.effects.TransitionsAnimation.TransitionsClipAnimation;
import com.google.gwt.regexp.shared.RegExp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Transitions and transformation plugin for gQuery.
 *
 * It is inspired on jquery.transit (http://github.com/rstacruz/jquery.transit).
 *
 * Usage examples:
 * <pre>

    $("#foo")
     .transition("{ opacity: 0.1, scale: 2, x: 50, y: 50 }", 5000, "easeOutBack", 0)
     .promise().done(new Function(){public void f() {
        g1.transition("{x: +100}", 2000, "linear", 0);
     }});

    $("#bar")
     .transition("{ opacity: 0.1, scale: 2, x: 50, y: 50 }", 5000, EasingCurve.easeInBack)
     .transition("{x: +100, width: +40px}", 2000, EasingCurve.easeOut);

 * </pre>
 */
public class Transitions extends Effects {

  // passing an invalid transition property in chrome, makes disable all transitions in the element
  public static final RegExp invalidTransitionNamesRegex = RegExp.compile("^(.*transform.*|duration|function|easing|delay|clip-.*)$");

  protected static final String transitionEnd = browser.mozilla || browser.msie ? "transitionend" : (prefix + "TransitionEnd");

  protected static final String transition = vendorProperty("transition");

  public static final Class<Transitions> Transitions = GQuery.registerPlugin(
      Transitions.class, new Plugin<Transitions>() {
        public Transitions init(GQuery gq) {
          return new Transitions(gq);
        }
      });

  private static String property(String prop) {
    if (isTransform(prop)) {
      return transform;
    }
    return prop.replaceFirst("^(margin|padding).+$", "$1");
  }

  protected Transitions(GQuery gq) {
    super(gq);
  }

  @Override
  public String css(String prop, boolean force) {
    prop = vendorProperty(prop);
    if (transform.equals(prop)) {
      return isEmpty() ? "" : getInstance(get(0), null).toString();
    } else if (isTransform(prop)) {
      return isEmpty() ? "" : getInstance(get(0), null).get(prop);
    } else {
      return super.css(prop, force);
    }
  }

  @Override
  public Transitions css(String prop, String value) {
    prop = vendorProperty(prop);
    if (transform.equals(prop)) {
      for (Element e : elements()) {
        Transform t = getInstance(e, value != null ? value : "");
        getStyleImpl().setStyleProperty(e, transform, t.toString());
      }
    } else if (isTransform(prop)) {
      for (Element e : elements()) {
        Transform t = getInstance(e, null);
        t.setFromString(prop, value);
        getStyleImpl().setStyleProperty(e, transform, t.toString());
      }
    } else {
      super.css(prop, value);
    }
    return this;
  }

  public static List<String> filterTransitionPropertyNames(Properties p) {
    List<String> ret = new ArrayList<>();
    for (String s : p.keys()) {
      if (invalidTransitionNamesRegex.test(s)) {
        continue;
      }
      String c = JsUtils.camelize(s);
      // marginLeft, marginRight ...  -> margin
      String m = property(c);
      if (m != null) {
        c = m;
      }
      // chrome needs transition:-webkit-transform instead of transition:transform
      c = JsUtils.hyphenize(c);
      if (!ret.contains(c)) {
        ret.add(c);
      }
    }
    return ret;
  }

  /**
   * The transition() method allows you to create animation effects on any numeric HTML Attribute,
   * CSS property, or color using CSS3 transformations and transitions.
   *
   * It works similar to animate(), supports chainning and queueing and an extra parameter for
   * delaying the animation.
   *
   * Example:
   * <pre>
     $("#foo").transition("{ opacity: 0.1, scale: 2, x: 50, y: 50 }", 5000, EasingCurve.easeInBack);

     $("#bar")
       .transition("{ opacity: 0.1, scale: 2, x: 50, y: 50 }", 5000, EasingCurve.easeInBack)
       .transition("{x: +100, width: +40px}", 2000, EasingCurve.easeOut);
   * </pre>
   */
  public Transitions transition(Object stringOrProperties, final int duration, final Easing easing,
      final int delay, final Function... funcs) {
    if (!isEmpty()) {
      Properties p = (stringOrProperties instanceof String)
          ? $$((String) stringOrProperties)
          : (Properties) stringOrProperties;
      for (Element e : elements()) {
        queueAnimation(new TransitionsClipAnimation().setEasing(easing).setProperties(p)
            .setElement(e).setCallback(funcs), duration);
      }
    }
    return this;
  }

  /**
   * The transition() method allows you to create animation effects on any numeric HTML Attribute,
   * CSS property, or color using CSS3 transformations and transitions.
   *
   * It works similar to animate() but has an extra parameter for delaying the animation.
   *
   * Example animate an element within 2 seconds:
   * $("#foo")
   *   .transition("{ opacity: 0.1, scale: 2, x: 50, y: 50 }", 5000, EasingCurve.easeInBack, 2000);
   *
   */
  public Transitions transition(Object stringOrProperties, int duration, String easing, int delay) {
    return transition(stringOrProperties, duration, EasingCurve.valueOf(easing), delay);
  }

  @Override
  protected GQAnimation createAnimation() {
    return new TransitionsAnimation();
  }

  @Override
  public boolean isVisible() {
    for (String s : Arrays.asList("opacity", "scale", "scaleX", "scaleY", "scale3d", "width", "height")) {
      String[] parts = css(s).split("\\s*,\\s*");
      for (String p : parts) {
        if (p.matches("^0[a-z%]*")) {
          return false;
        }
      }
    }
    return super.isVisible();
  }
}
