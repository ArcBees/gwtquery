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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.Properties;
import com.google.gwt.query.client.js.JsUtils;
import com.google.gwt.query.client.plugins.Plugin;
import com.google.gwt.query.client.plugins.effects.PropertiesAnimation.Easing;
import com.google.gwt.query.client.plugins.effects.PropertiesAnimation.EasingCurve;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.DOM;

/**
 * Transitions and transformation plugin for gQuery.
 * 
 * It is inspired on jquery.transit (http://github.com/rstacruz/jquery.transit)
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
public class Transitions extends GQuery {
  
  /**
   * A dictionary class with all the properties of an element transform
   * which is able to return the correct syntax for setting css properties.
   */
  public static class Transform  {
    
    private static final RegExp transform3dRegex = RegExp.compile("^(rotate([XY]|3d)|perspective)$");
    
    private HashMap<String, List<String>> map = new HashMap<String, List<String>>();

    public Transform(String s) {
      parse(s);
    }
    
    public List<String> get(String prop) {
      return map.get(prop);
    }
    
    private void parse(String s) {
      if (s != null) {
        RegExp re = RegExp.compile("([a-zA-Z0-9]+)\\((.*?)\\)", "g");
        for (MatchResult r = re.exec(s); r != null; r = re.exec(s)) {
          setFromString(r.getGroup(1), r.getGroup(2));
        }
      }
    }
    
    public void set(String prop, String ...val) {
      setter(prop, val);
    }
    
    public void setFromString(String prop, String ...val) {
      if (val.length == 1 && val[0] instanceof String) {
        String[] vals = ((String)val[0]).split("[\\s*,\\s*]");
        set(prop, vals);
      } else {
        set(prop, val);
      }
    }
    
    private void setter(String prop, String ...val) {
      if (prop.matches("(rotate[XY]?|skew[XY])")) {
        map.put(prop, unit(val[0], "deg"));
      } else if ("scale".equals(prop)) {
        String x = val.length < 1 ? "1" : val[0];
        String y = val.length < 2 ? x : val[1];
        map.put(prop, Arrays.asList(x, y));
      } else if ("perspective".equals(prop)) {
        map.put(prop, unit(val[0], "px"));
      } else if ("x".equals(prop)) {
        setter("translate", val[0], null);
      } else if ("y".equals(prop)) {
        setter("translate", null, val[0]);
      } else if ("translate".equals(prop)) {
        if (map.get("translateX") == null) {
          map.put("translateX", unit("0", "px"));
        }
        if (val[0] != null) {
          map.put("translateX", unit(val[0], "px"));
        }
        if (map.get("translateY") == null) {
          map.put("translateY", unit("0", "px"));
        }
        if (val[1] != null) {
          map.put("translateY", unit(val[1], "px"));
        }
        map.put("translate", Arrays.asList(map.get("translateX").get(0), map.get("translateY").get(0)));
      }
    }
    
    /**
     * Converts the dictionary to a transition css string.
     */
    public String toString() {
      // purposely using string addition, since my last tests demonstrate
      // that string addition performs better than string builders in gwt-prod.
      String ret = "";
      for (Entry<String, List<String>> e: map.entrySet()) {
        if (has3d || !transform3dRegex.test(e.getKey())) {
          String v = "";
          for (String s : e.getValue()) {
            v += (v.isEmpty() ? "" : ",") + s;
          }
          ret += (ret.isEmpty() ? "" : " ") + e.getKey() + "(" + v + ")";
        }
      }
      return ret;
    }
    
    private List<String> unit(String val, String unit) {
      return Arrays.asList(val + (val.endsWith(unit) ? "" : unit));
    }
  }
  
  // Used to check supported properties in the browser
  private static Style divStyle = DOM.createDiv().getStyle();
  
  private static final String prefix = browser.msie ? "ms" : browser.opera ? "o" : browser.mozilla ? "moz" : browser.webkit ? "webkit" : "";
  private static final String transform = getVendorPropertyName("transform");
  private static final String TRANSFORM = "_t_";
  private static final String transformOrigin = getVendorPropertyName("transformOrigin");
  
  protected static final RegExp transformRegex = RegExp.compile("^(scale|translate|rotate([XY]|3d)?|perspective|skew[XY]|x|y)$");
  private static final String transition = getVendorPropertyName("transition");
  
  private static final String transitionDelay = getVendorPropertyName("transitionDelay");
  private static final String transitionEnd = browser.mozilla || browser.msie ? "transitionend" : (prefix + "transitionEnd");
  
  public static boolean has3d = supportsTransform3d();
  
  public static final Class<Transitions> Transitions = GQuery.registerPlugin(
      Transitions.class, new Plugin<Transitions>() {
        public Transitions init(GQuery gq) {
          return new Transitions(gq);
        }
      });
  
  private static String getVendorPropertyName(String prop) {
    // we prefer vendor specific names by default
    String vendorProp =  JsUtils.camelize("-" + prefix + "-" + prop);
    if (JsUtils.hasProperty(divStyle, vendorProp)) {
      return vendorProp;
    }
    if (JsUtils.hasProperty(divStyle, prop)) {
      return prop;
    }
    String camelProp = JsUtils.camelize(prop);
    if (JsUtils.hasProperty(divStyle, camelProp)) {
      return camelProp;
    }
    return null;
  }
  
  private static String property(String prop) {
    if (transformRegex.test(prop)) {
      return transform;
    }
    return prop.replaceFirst("^(margin|padding).+$", "$1");
  }
  
  private static boolean supportsTransform3d() {
    String rotate = "rotateY(1deg)";
    divStyle.setProperty(transform, rotate);
    rotate = divStyle.getProperty(transform);
    return rotate != null && !rotate.isEmpty();
  }
  
  protected Transitions(GQuery gq) {
    super(gq);
  }

  @Override 
  public String css(String prop, boolean force) {
    if ("transform".equals(prop)) {
      Transform t = data(TRANSFORM);
      return t == null ? "" : t.toString();
    } else if ("transformOrigin".equals(prop)) {
      return super.css(transformOrigin, force);
    } else if ("transition".equals(prop)) {
      return super.css(transition, force);
    } else {
      return super.css(prop, force);
    }
  }
  
  @Override 
  public Transitions css(String prop, String value) {
    if ("transform".equals(prop)) {
      for (Element e : elements()) {
        Transform t = getTransform(e, value);
        getStyleImpl().setStyleProperty(e, transform, t.toString());
      }
    } else if ("transformOrigin".equals(prop)) {
      super.css(transformOrigin, value);
    } else if ("transition".equals(prop)) {
      super.css(transition, value);
    } else if (transformRegex.test(prop)) {
      for (Element e : elements()) {
        Transform t = getTransform(e, null);
        t.setFromString(prop, value);
        getStyleImpl().setStyleProperty(e, transform, t.toString());
      }
    } else {
      super.css(prop, value);
    }
    return this;
  }
  
  private List<String> filterPropertyNames(Properties p) {
    List<String> ret = new ArrayList<String>();
    for (String s : p.keys()) {
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
  
  private Transform getTransform(Element e, String initial) {
    Transform t = data(e, TRANSFORM);
    if (t == null || initial != null && !initial.isEmpty() ) {
      t = new Transform(initial);
      data(e, TRANSFORM, t);
    }
    return t;
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
  public Transitions transition(Object stringOrProperties, int duration, Easing easing, int delay, final Function... funcs) {
    if (isEmpty()) {
      return this;
    }
    
    final Properties p = (stringOrProperties instanceof String) ? $$((String) stringOrProperties) : (Properties) stringOrProperties;

    final String oldTransitions = css(transition);

    if (easing == null) {
      easing = EasingCurve.ease;
    }
    
    String attribs = duration + "ms" + " "  + easing.toString() + " " + delay + "ms";
    List<String> props = filterPropertyNames(p);
    String value  = "";
    for (String s : props) {
      value += (value.isEmpty() ? "" : ", ") + s + " " + attribs;
    }
    
    final String transitionValue = value;

    // Use gQuery queue, so as we can chain transitions, animations etc.
    delay(0, new Function(){public void f() {
      // This is called once per element
      $(this)
        // Configure animation using transition property
        .css(transition, transitionValue)
        // Set all css properties for this transition using the css method in this class 
        .as(Transitions).css(p)
        // prevent memory leak
        .removeData(TRANSFORM);
    }});
    
    // restore oldTransitions in the element, and use the queue to prevent more effects being run.
    // TODO: Use transitionEnd events once GQuery supports non-bit events
    delay(duration + delay, new Function(){public void f() {
      // This is called once per element
      $(this).as(Transitions)
        .css(transition, oldTransitions)
        .each(funcs);
    }});
    
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

}
