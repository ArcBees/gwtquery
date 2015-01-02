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
package com.google.gwt.query.client;

/**
 * This interface represents the Window.console object.
 */
public interface Console {

  /**
   * Clears the console.
   */
  void clear();

  /**
   * Displays an interactive listing of the properties of a specified JavaScript object. This
   * listing lets you use disclosure triangles to examine the contents of child objects.
   */
  void dir(Object arg);

  /**
   * Outputs an error message. You may use string substitution and additional arguments with this method.
   */
  void error(Object arg);

  /**
   * Outputs an error message. You may use string substitution and additional arguments with this method.
   */
  void error(Object... args);

  /**
   * Creates a new inline group, indenting all following output by another level. To move back out a
   * level, call groupEnd().
   */
  void group(Object arg);

  /**
   * Creates a new inline group, indenting all following output by another level; unlike group(),
   * this starts with the inline group collapsed, requiring the use of a disclosure button to expand
   * it. To move back out a level, call groupEnd().
   */
  void groupCollapsed(Object arg);

  /**
   * Exits the current inline group.
   */
  void groupEnd();

  /**
   * Informative logging information. You may use string substitution and additional arguments with
   * this method.
   */
  void info(Object arg);

  /**
   * Informative logging information. You may use string substitution and additional arguments with
   * this method.
   */
  void info(Object... args);

  /**
   * For general output of logging information. You may use string substitution and additional
   * arguments with this method.
   */
  void log(Object arg);

  /**
   * For general output of logging information. You may use string substitution and additional
   * arguments with this method.
   */
  void log(Object... args);

  /**
   * Starts a JavaScript CPU profile with a name. To complete the profile, call console.profileEnd().
   */
  void profile(String title);

  /**
   * Stops the current JavaScript CPU profiling session, if one is in progress, and prints the report.
   */
  void profileEnd(String title);

  /**
   * Starts a timer with a name specified as an input parameter. Up to 10,000 simultaneous timers
   * can run on a given page.
   */
  void time(String title);

  /**
   * Stops the specified timer and logs the elapsed time in seconds since its start.
   */
  void timeEnd(String title);

  /**
   * Outputs a timestamp.
   */
  void timeStamp(Object arg);

  /**
   * Outputs a warning message. You may use string substitution and additional arguments with this
   * method.
   */
  void warn(Object arg);

  /**
   * Outputs a warning message. You may use string substitution and additional arguments with this
   * method.
   */
  void warn(Object... args);
}
