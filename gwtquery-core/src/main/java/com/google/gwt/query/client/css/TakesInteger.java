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
package com.google.gwt.query.client.css;

import com.google.gwt.query.client.css.TakesCssValue.CssSetter;

/**
 * Interface to be implemented by properties which take a integer as defined in
 * css2 specification.
 *
 * @see http://www.w3.org/TR/CSS21/syndata.html#value-def-number
 */
public interface TakesInteger extends HasCssValue {

  CssSetter with(Integer value);
}
