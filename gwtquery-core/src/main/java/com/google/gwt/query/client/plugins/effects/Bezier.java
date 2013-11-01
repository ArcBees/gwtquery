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
package com.google.gwt.query.client.plugins.effects;


/**
 * Bezier curve for transition easing functions.
 * 
 * Inspired in KeySpline.js from Gaetan Renaudeau [1] which
 * is based in nsSMILKeySpline.cpp from mozilla.
 * 
 * [1] https://gist.github.com/gre/1926947
 * [2] http://dxr.mozilla.org/mozilla-central/source/content/smil/nsSMILKeySpline.cpp
 */
public class Bezier {
                  
  private double x1, y1 , x2, y2;
  
  public Bezier(double x1, double y1, double x2, double y2) {
    this.x1 = x1; this.y1 = y1; this.x2 = x2; this.y2 = y2;
  }
  
  private double A(double a1, double a2) {
    return 1.0 - 3.0 * a2 + 3.0 * a1;
  }
  private double B(double a1, double a2) {
    return 3.0 * a2 - 6.0 * a1;
  }
  private double C(double a1){
    return 3.0 * a1;
  }
  
  private double calcBezier(double t, double a1, double a2) {
    return ((A(a1, a2)*t + B(a1, a2))*t + C(a1))*t;
  }
  
  private double calcSlope(double t, double a1, double a2) {
    return 3.0 * A(a1, a2)*t*t + 2.0 * B(a1, a2) * t + C(a1);
  }
  
  private double getTForX(double x) {
    double t = x;
    for (double i = 0; i < 4; ++i) {
      double currentSlope = calcSlope(t, x1, x2);
      if (currentSlope == 0.0) return t;
      double currentX = calcBezier(t, x1, x2) - x;
      t -= currentX / currentSlope;
    }
    return t;
  }
  
  public double f (double x) {
    return calcBezier(getTForX(x), y1, y2);
  }
  
  public String toString() {
    return x1 + "," + y1 + "," + x2 + "," + y2;
  }

}
