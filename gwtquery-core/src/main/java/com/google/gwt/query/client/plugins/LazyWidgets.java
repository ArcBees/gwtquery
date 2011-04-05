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
import java.util.ArrayList;
import java.util.List;
import com.google.gwt.dom.client.Element;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.plugins.widgets.WidgetFactory;
import com.google.gwt.query.client.plugins.widgets.WidgetInitializer;
import com.google.gwt.query.client.plugins.widgets.WidgetsUtils;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.query.client.LazyBase;

public interface LazyWidgets<T> extends LazyBase<T>{

  /**
   * Try to create a widget using the given factory and the given options for
   * each element of the query. Returns a new gquery set of elements with the
   * new widgets created.
   */
  <W extends Widget> LazyWidgets<T> widgets(WidgetFactory<W> factory, WidgetInitializer<W> initializers);

}
