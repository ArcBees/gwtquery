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
package com.google.gwt.query.client.plugins.effects;

import static com.google.gwt.query.client.plugins.Effects.prefix;
import static com.google.gwt.query.client.plugins.Effects.vendorPropNames;
import static com.google.gwt.query.client.plugins.Effects.vendorProperty;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.js.JsUtils;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

/**
 * A dictionary class with all the properties of an element transform
 * which is able to return the correct syntax for setting the CSS transform
 * property.
 */
public class Transform  {

  private static final String TRANSFORM = "_t_";

  // Used to check supported properties in the browser
  protected static final Style divStyle = Document.get().createDivElement().getStyle();

  // Compute browser specific constants, public so as they are usable in plugins

  static {
    for (String s: new String[]{"transition", "transitionDelay", "transform", "transformOrigin"}) {
      vendorPropNames.put(s, getVendorPropertyName(s));
    }
    // x,y,z are aliases
    for (String s: new String[]{"x", "y", "z"}) {
      vendorPropNames.put(s, "translate" + s.toUpperCase());
    }
  }

  public static final String transform = vendorProperty("transform");
  public static final String transformOrigin = vendorProperty("transformOrigin");

  // Non final for testing purposes.
  public static boolean has3d = supportsTransform3d();

  // Regular expressions based on http://www.w3schools.com/cssref/css3_pr_transform.asp
  protected static final RegExp transformRegex = RegExp.compile("^(matrix(3d)?|(translate|scale|rotate)([XYZ]|3d)?|skew([XY])?|perspective|x|y|z)$");
  private static final RegExp transform3dRegex = RegExp.compile("^(rotate[XY]|\\w+(Z|3d)|perspective)$");
  private static final RegExp transformParseRegex = RegExp.compile("(\\w+)\\((.*?)\\)", "g");
  private static final RegExp anglePropRegex = RegExp.compile("(rotate[XYZ]?|skew[XY]?)");
  private static final RegExp translatePropRegex = RegExp.compile("translate[XYZ]");

  private HashMap<String, List<String>> map = new HashMap<>();

  // Some browsers like HTMLUnit only support 2d transformations
  private static boolean supportsTransform3d() {
    if (transform == null) {
      return false;
    }
    String rotate = "rotateY(1deg)";
    divStyle.setProperty(transform, rotate);
    rotate = divStyle.getProperty(transform);
    return rotate != null && !rotate.isEmpty();
  }

  /**
   * Compute the correct CSS property name for a specific browser vendor.
   */
  public static String getVendorPropertyName(String prop) {
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


  /**
   * Return the Transform dictionary object of a element.
   */
  public static Transform getInstance(Element e) {
    return getInstance(e, null);
  }

  /**
   * Return true if the propName is a valid value of the css3 transform property.
   */
  public static boolean isTransform(String propName) {
    return transformRegex.test(propName);
  }

  /**
   * Return the Transform dictionary object of an element, but reseting
   * historical values and setting them to the initial value passed.
   */
  public static Transform getInstance(Element e, String initial) {
    Transform t = GQuery.data(e, TRANSFORM);
    if (t == null || initial != null) {
      if (initial == null) {
        initial = GQuery.getSelectorEngine().getDocumentStyleImpl().curCSS(e, transform, false);
      }
      t = new Transform(initial);
      GQuery.data(e, TRANSFORM, t);
    }
    return t;
  }

  /**
   * Create a new Transform dictionary setting initial values based on the
   * string passed.
   */
  public Transform(String s) {
    parse(s);
  }

  /**
   * Return the value of a transform property.
   */
  public String get(String prop) {
    return listToStr(map.get(prop), ",");
  }

  private String listToStr(List<String> l, String sep) {
    String v = "";
    if (l != null) {
      for (String s : l) {
        v += (v.isEmpty() ? "" : sep) + s;
      }
    }
    return v;
  }

  /**
   * Parse a transform value as string and fills the dictionary map.
   */
  private void parse(String s) {
    if (s != null) {
      for (MatchResult r = transformParseRegex.exec(s); r != null; r = transformParseRegex.exec(s)) {
        setFromString(vendorProperty(r.getGroup(1)), r.getGroup(2));
      }
    }
  }

  /**
   * Set a transform value or multi-value.
   */
  public void set(String prop, String ...val) {
    setter(prop, val);
  }

  /**
   * Set a transform multi-value giving either a set of strings or
   * just an string of values separated by comma.
   */
  public void setFromString(String prop, String ...val) {
    if (val.length == 1) {
      String[] vals = val[0].split("[\\s,]+");
      set(prop, vals);
    } else {
      set(prop, val);
    }
  }

  private void setter(String prop, String ...val) {
    if (anglePropRegex.test(prop)) {
      map.put(prop, unit(val[0], "deg"));
    } else if ("scale".equals(prop)) {
      String x = val.length < 1 ? "1" : val[0];
      String y = val.length < 2 ? x : val[1];
      map.put(prop, Arrays.asList(x, y));
    } else if ("perspective".equals(prop)) {
      map.put(prop, unit(val[0], "px"));
    } else if (translatePropRegex.test(prop)) {
      map.put(prop, unit(val[0], "px"));
    } else if ("translate".equals(prop)) {
      if (val[0] != null) {
        map.put("translateX", unit(val[0], "px"));
      }
      if (val.length > 1 && val[1] != null) {
        map.put("translateY", unit(val[1], "px"));
      }
      if (has3d && val.length > 2 && val[2] != null) {
        map.put("translateZ", unit(val[2], "px"));
      }
    } else {
      map.put(prop, Arrays.asList(val));
    }
  }

  /**
   * Converts the dictionary to a transition css string value but
   * excluding 3d properties if the browser only supports 2d.
   */
  public String toString() {
    // purposely using string addition, since my last tests demonstrate
    // that string addition performs better than string builders in gwt-prod.
    String ret = "";
    for (Entry<String, List<String>> e: map.entrySet()) {
      if (has3d || !transform3dRegex.test(e.getKey())) {
        String v = listToStr(e.getValue(), ",");
        ret += (ret.isEmpty() ? "" : " ") + e.getKey() + "(" + v + ")";
      }
    }
    return ret;
  }

  private List<String> unit(String val, String unit) {
    return Arrays.asList(val + (val.endsWith(unit) ? "" : unit));
  }
}
