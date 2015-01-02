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
package com.google.gwt.query.vm;

import com.google.gwt.http.client.Header;
import com.google.gwt.http.client.Response;

import java.util.List;
import java.util.Map;

/**
 * Implementation of `Response` in the JVM.
 */
public class ResponseJre extends Response {
  private int status;
  private Map<String, List<String>> headers;
  private String responseText;
  private String statusText;

  public ResponseJre(int status, String statusText, String text, Map<String, List<String>> headers) {
    this.status = status;
    this.headers = headers;
    this.responseText = text;
    this.statusText = statusText;
  }

  public String getHeader(String header) {
    List<String> l = headers.get(header);
    if (l != null && !l.isEmpty()) {
      String ret = "";
      for (String s : l) {
        ret += ret.isEmpty() ? s : (", " + s);
      }
      return ret;
    }
    return null;
  }

  public Header[] getHeaders() {
    Header[] ret = new Header[headers.size()];
    int i = 0;
    for (final String s : headers.keySet()) {
      final String v = getHeader(s);
      ret[i] = new Header() {
        public String getValue() {
          return v;
        }

        public String getName() {
          return s;
        }
      };
      i++;
    }
    return ret;
  }

  public String getHeadersAsString() {
    String ret = "";
    for (Header h : getHeaders()) {
      ret += h.getName() + ":" + h.getValue() + "\n";
    }
    return ret;
  }

  public int getStatusCode() {
    return status;
  }

  public String getStatusText() {
    return statusText;
  }

  public String getText() {
    return responseText;
  }
}
