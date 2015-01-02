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
package com.google.gwt.query.client.plugins;

/**
 * Object use to configure a Plugin extending {@link MousePlugin}.
 *
 */
public class MouseOptions {

  private String[] cancel;

  private int delay;

  private int distance;

  public MouseOptions() {
    initDefault();
  }

  /**
   * Return an array of css selectors. The plugin will not start when the mouse
   * interacts with the elements selected by the selectors.
   *
   */
  public String[] getCancel() {
    return cancel;
  }

  /**
   * Return the tolerance, in pixels, for when plugin should start. If
   * specified, the plugin will not start until after mouse is dragged beyond
   * distance.
   *
   */
  public int getDelay() {
    return delay;
  }

  /**
   * return the time in milliseconds that define when the plugin should start.
   */
  public int getDistance() {
    return distance;
  }

  /**
   * Prevents starting of the plugin on specified elements.
   *
   * @param cancel
   *          array of css selectors
   */
  public void setCancel(String... cancel) {
    this.cancel = cancel;
  }

  /**
   * Time in milliseconds to define when the plugin should start. It helps
   * preventing unwanted selections when clicking on an element.
   *
   */
  public void setDelay(int delay) {
    this.delay = delay;
  }

  /**
   * Tolerance, in pixels, for when plugin should start. If specified, the
   * plugin will not start until after mouse is dragged beyond distance. Default
   * : 1
   *
   */
  public void setDistance(int distance) {
    this.distance = distance;
  }

  protected void initDefault() {
    delay = 0;
    distance = 1; // by default, the mouse have to move one pixel !
    cancel = new String[] {"input", "option"};
  }
}
