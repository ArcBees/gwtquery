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

import com.google.gwt.dom.client.Element;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

/**
 * A dictionary class with all the properties of an element transform
 * which is able to return the correct syntax for setting the css transform
 * property.
 */
public class Transform  {

  private static final String TRANSFORM = "_t_";

  protected static final RegExp transformRegex = RegExp.compile("^(scale([XYZ]|)|translate([XYZ]|3d)|rotate([XYZ]|3d)?|perspective|skew[XYZ]|x|y)$");
  private static final RegExp transform3dRegex = RegExp.compile("^(rotate([XYZ]|3d)|perspective)$");

  private HashMap<String, List<String>> map = new HashMap<String, List<String>>();

  /**
   * Return the Transform dictionary object of a element.
   */
  public static Transform getInstance(Element e) {
    return getInstance(e, null);
  }

  public static boolean isTransform(String propName) {
    return transformRegex.test(propName);
  }

  /**
   * Return the Transform dictionary object of an element, but reseting
   * historical values and setting them to the initial value passed.
   */
  public static Transform getInstance(Element e, String initial) {
    Transform t = GQuery.data(e, TRANSFORM);
    if (t == null || initial != null && !initial.isEmpty()) {
      t = new Transform(initial);
      GQuery.data(e, TRANSFORM, t);
    }
    return t;
  }

  public Transform(String s) {
    parse(s);
  }

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
    if (val.length == 1) {
      String[] vals = val[0].split("[\\s*,\\s*]");
      set(prop, vals);
    } else {
      set(prop, val);
    }
  }

  private void setter(String prop, String ...val) {
    if (prop.matches("(rotate[XYZ]?|skew[XYZ])")) {
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
    } else if (prop.matches("(translate[XYZ])")) {
      map.put(prop, unit(val[0], "px"));
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
      if (val.length > 1 && val[1] != null) {
        map.put("translateY", unit(val[1], "px"));
      }
      if (map.get("translateZ") == null) {
        map.put("translateZ", unit("0", "px"));
      }
      if (val.length > 2 && val[2] != null) {
        map.put("translateZ", unit(val[0], "px"));
      }
      map.put("translate", Arrays.asList(map.get("translateX").get(0), map.get("translateY").get(0), map.get("translateY").get(0)));
    } else {
      map.put(prop, unit(val[0], ""));
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
      if (Transitions.has3d || !transform3dRegex.test(e.getKey())) {
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