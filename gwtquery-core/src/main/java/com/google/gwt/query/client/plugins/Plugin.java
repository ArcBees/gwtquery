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

import com.google.gwt.query.client.GQuery;

/**
 * A GQuery plugin. All GQuery plugins must implement this interface.
 *
 * @param <T> the plugin class
 */
public interface Plugin<T extends GQuery> {

  /**
   * Called by the GQuery.as() method in order to pass the current matched set.
   * Typically a plugin will want to call a super class copy constructor in
   * order to copy the internal matched set of elements.
   */
  T init(GQuery gQuery);
}
