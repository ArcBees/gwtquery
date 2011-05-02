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
package com.google.gwt.query.linker;

import com.google.gwt.core.ext.LinkerContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.linker.IFrameLinker;
import com.google.gwt.core.ext.linker.LinkerOrder;

/**
 * Adds doctype to the iframe used to load the application.
 * Without this code, IE8 does not enable document.querySelectorAll feature.
 */
@LinkerOrder(LinkerOrder.Order.PRIMARY)
public class IFrameWithDocTypeLinker extends IFrameLinker {

  private static final String DOCTYPE = "<!doctype html>\n";

  protected String getModulePrefix(TreeLogger logger, LinkerContext context,
      String strongName) {
    return DOCTYPE + super.getModulePrefix(logger, context, strongName);
  }

  @Override
  protected String getModulePrefix(TreeLogger logger, LinkerContext context,
      String strongName, int numFragments) {
    return DOCTYPE
        + super.getModulePrefix(logger, context, strongName, numFragments);
  }
}

