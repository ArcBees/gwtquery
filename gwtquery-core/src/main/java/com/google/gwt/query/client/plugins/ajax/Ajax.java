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
package com.google.gwt.query.client.plugins.ajax;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.dom.client.Element;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.Response;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQ;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.IsProperties;
import com.google.gwt.query.client.Promise;
import com.google.gwt.query.client.builders.JsonBuilder;
import com.google.gwt.query.client.js.JsUtils;
import com.google.gwt.query.client.plugins.Plugin;
import com.google.gwt.query.client.plugins.deferred.PromiseFunction;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FormPanel;

/**
 * Ajax class for GQuery.
 *
 * The jQuery library has a full suite of AJAX capabilities, but GWT is plenty of classes to get
 * data from server side: RPC, XHR, RF, etc.
 *
 * This class is not a substitute for the GWT utilities, but a complement to get server data in a
 * jquery way, specially when querying non java backends.
 *
 * We do not pretend to clone all the jquery Ajax API inside gquery, just take its syntax and to
 * implement the most popular usage of it. This implementation is almost thought to be used as an
 * alternative to the GWT-XHR, GWT-XML and GWT-JSON modules.
 *
 */
public class Ajax extends GQuery {

  public static final String JSON_CONTENT_TYPE = "application/json";

  public static final String JSON_CONTENT_TYPE_UTF8 = JSON_CONTENT_TYPE + "; charset=utf-8";

  /**
   * Ajax Transport object.
   */
  public interface AjaxTransport {
    Promise getJsonP(Settings settings);

    Promise getLoadScript(Settings settings);

    Promise getXhr(Settings settings);
  }

  /**
   * Ajax Settings object.
   */
  public interface Settings extends JsonBuilder {
    String getContentType();

    Element getContext();

    IsProperties getData();

    String getDataString();

    String getDataType();

    Function getError();

    IsProperties getHeaders();

    String getPassword();

    Function getSuccess();

    int getTimeout();

    String getType();

    String getUrl();

    String getUsername();

    boolean getWithCredentials();

    Settings setContentType(String t);

    Settings setContext(Element e);

    Settings setData(Object p);

    Settings setDataString(String d);

    Settings setDataType(String t);

    Settings setError(Function f);

    Settings setHeaders(IsProperties p);

    Settings setPassword(String p);

    Settings setSuccess(Function f);

    Settings setTimeout(int t);

    Settings setType(String t);

    Settings setUrl(String u);

    Settings setUsername(String u);

    Settings setWithCredentials(boolean b);
  }

  public static final Class<Ajax> Ajax = registerPlugin(Ajax.class, new Plugin<Ajax>() {
    public Ajax init(GQuery gq) {
      return new Ajax(gq);
    }
  });

  public static Promise ajax(IsProperties p) {
    Settings s = createSettings();
    s.load(p);
    return ajax(s);
  }

  /**
   * Perform an ajax request to the server.
   *
   * Example:
   *
   * <pre>
    import static com.google.gwt.query.client.GQ.*
    ...
    Properties properties = $$("dataType: xml, type: post; data: {q: 'gwt'}, headers: {X-Powered-By: GQuery}");
    ajax("test.php", new Function() {
      public void f() {
        Element xmlElem = getData()[0];
        System.out.println($("message", xmlElem));
      }
    }, new Function(){
      public void f() {
        System.err.println("Ajax Error: " + getData()[1]);
      }
    }, properties);
   * </pre>
   *
   */
  public static Promise ajax(Settings settings) {
    resolveSettings(settings);

    final Function onSuccess = settings.getSuccess();
    if (onSuccess != null) {
      onSuccess.setElement(settings.getContext());
    }

    final Function onError = settings.getError();
    if (onError != null) {
      onError.setElement(settings.getContext());
    }

    final String dataType = settings.getDataType();

    Promise ret = null;

    if ("jsonp".equalsIgnoreCase(dataType)) {
      ret = GQ.getAjaxTransport().getJsonP(settings);
    } else if ("loadscript".equalsIgnoreCase(dataType)) {
      ret = GQ.getAjaxTransport().getLoadScript(settings);
    } else {
      ret = GQ.getAjaxTransport().getXhr(settings)
          .then(new Function() {
            public Object f(Object... args) {
              Response response = arguments(0);
              Request request = arguments(1);
              Object retData = response.getText();
              if (retData != null && !"".equals(retData)) {
                try {
                  if ("xml".equalsIgnoreCase(dataType)) {
                    retData = JsUtils.parseXML(response.getText());
                  } else if ("json".equalsIgnoreCase(dataType)) {
                    retData = GQ.create(response.getText());
                  } else {
                    retData = response.getText();
                    if ("script".equalsIgnoreCase(dataType)) {
                      ScriptInjector.fromString((String) retData).setWindow(window).inject();
                    }
                  }
                } catch (Exception e) {
                  if (GWT.isClient() && GWT.getUncaughtExceptionHandler() != null) {
                    GWT.getUncaughtExceptionHandler().onUncaughtException(e);
                  } else {
                    e.printStackTrace();
                  }
                }
              }
              return new Object[] {retData, "success", request, response};
            }
          }, new Function() {
            public Object f(Object... args) {
              Throwable exception = arguments(0);
              Request request = getArgument(1, Request.class);
              String msg = String.valueOf(exception);
              return new Object[] {null, msg, request, null, exception};
            }
          });
    }
    if (onSuccess != null) {
      ret.done(onSuccess);
    }
    if (onError != null) {
      ret.fail(onError);
    }
    return ret;
  }

  private static void resolveSettings(Settings settings) {
    String url = settings.getUrl();
    assert settings != null && settings.getUrl() != null : "no url found in settings";

    String type = "POST";
    if (settings.getType() != null) {
      type = settings.getType().toUpperCase();
    }
    if ("jsonp".equalsIgnoreCase(settings.getDataType())) {
      type = "GET";
    }
    settings.setType(type);

    IsProperties data = settings.getData();
    if (data != null) {
      String dataString = null, contentType = null;
      if (data.getDataImpl() instanceof JavaScriptObject
          && JsUtils.isFormData(data.<JavaScriptObject> getDataImpl())) {
        dataString = null;
        contentType = FormPanel.ENCODING_URLENCODED;
      } else if (settings.getType().matches("(POST|PUT)")
          && "json".equalsIgnoreCase(settings.getDataType())) {
        dataString = data.toJson();
        contentType = JSON_CONTENT_TYPE_UTF8;
      } else {
        dataString = data.toQueryString();
        contentType = FormPanel.ENCODING_URLENCODED;
      }
      settings.setDataString(dataString);
      settings.setContentType(contentType);
    }

    if ("GET".equals(settings.getType()) && settings.getDataString() != null) {
      url += (url.contains("?") ? "&" : "?") + settings.getDataString();
      settings.setUrl(url);
    }
  }

  public static Promise ajax(String url, Function onSuccess, Function onError) {
    return ajax(url, onSuccess, onError, (Settings) null);
  }

  public static Promise ajax(String url, Function onSuccess, Function onError, Settings s) {
    if (s == null) {
      s = createSettings();
    }
    s.setUrl(url).setSuccess(onSuccess).setError(onError);
    return ajax(s);
  }

  public static Promise ajax(String url, IsProperties p) {
    Settings s = createSettings();
    s.load(p);
    s.setUrl(url);
    return ajax(s);
  }

  public static Promise ajax(String url, Settings settings) {
    return ajax(settings.setUrl(url));
  }

  public static Settings createSettings() {
    return createSettings("");
  }

  public static Settings createSettings(String prop) {
    Settings s = GQ.create(Settings.class);
    if (prop != null && !prop.isEmpty())
      s.parse(prop);
    return s;
  }

  public static Settings createSettings(IsProperties p) {
    Settings s = GQ.create(Settings.class);
    s.load(p.getDataImpl());
    return s;
  }

  public static Promise get(String url) {
    return get(url, null);
  }

  public static Promise get(String url, IsProperties data) {
    return get(url, (IsProperties) data, null);
  }

  /**
   * @deprecated Use promises instead
   */
  public static Promise get(String url, IsProperties data, Function onSuccess) {
    Settings s = createSettings();
    s.setUrl(url);
    s.setDataType("txt");
    s.setType("get");
    s.setData(data);
    s.setSuccess(onSuccess);
    return ajax(s);
  }

  public static Promise getJSON(String url, IsProperties data) {
    return getJSON(url, data, null);
  }

  public static Promise getJSON(String url, IsProperties data, Function onSuccess) {
    Settings s = createSettings();
    s.setUrl(url);
    s.setDataType("json");
    s.setType("post");
    s.setData(data);
    s.setSuccess(onSuccess);
    return ajax(s);
  }

  public static Promise getJSONP(String url) {
    return getJSONP(url, null);
  }

  public static Promise getJSONP(String url, IsProperties data) {
    return getJSONP(url, (IsProperties) data, null);
  }

  public static Promise getJSONP(String url, IsProperties data, Function onSuccess) {
    Settings s = createSettings();
    s.setUrl(url);
    s.setDataType("jsonp");
    s.setType("get");
    s.setData(data);
    s.setSuccess(onSuccess);
    return ajax(s);
  }

  public static Promise getJSONP(String url, Function success, Function error, int timeout) {
    return ajax(createSettings()
        .setUrl(url)
        .setDataType("jsonp")
        .setType("get")
        .setTimeout(timeout)
        .setSuccess(success)
        .setError(error));
  }

  /**
   * Get a JavaScript file from the server using a GET HTTP request, then execute it.
   */
  public static Promise getScript(String url) {
    return getScript(url, null);
  }

  public static Promise getScript(final String url, Function success) {
    return ajax(createSettings()
        .setUrl(url)
        .setType("get")
        .setDataType("script")
        .setSuccess(success));
  }

  /**
   * Load a JavaScript file from any url using the script tag mechanism.
   */
  public static Promise loadScript(String url) {
    return loadScript(url, null);
  }

  public static Promise loadScript(final String url, Function success) {
    if (!GWT.isClient() || $("script[src^='" + url + "']").isEmpty()) {
      return ajax(createSettings()
          .setUrl(url)
          .setType("get")
          .setDataType("loadscript")
          .setSuccess(success));
    } else {
      return Deferred().resolve().promise();
    }
  }

  public static Promise post(String url, IsProperties data) {
    return post(url, (IsProperties) data, null);
  }

  public static Promise post(String url, IsProperties data, final Function onSuccess) {
    Settings s = createSettings();
    s.setUrl(url);
    s.setDataType("txt");
    s.setType("post");
    s.setData(data);
    s.setSuccess(onSuccess);
    return ajax(s);
  }

  protected Ajax(GQuery gq) {
    super(gq);
  }

  public Ajax load(String url, IsProperties data) {
    return load(url, data);
  }

  public Ajax load(String url, IsProperties data, final Function onSuccess) {
    Settings s = createSettings();
    final String filter = url.contains(" ") ? url.replaceFirst("^[^\\s]+\\s+", "") : "";
    s.setUrl(url.replaceAll("\\s+.+$", ""));
    s.setDataType("html");
    s.setType("get");
    s.setData(data);
    s.setSuccess(new Function() {
      public void f() {
        try {
          // We clean up the returned string to smoothly append it to our document
          // Note: using '\s\S' instead of '.' because gwt String emulation does
          // not support java embedded flag expressions (?s) and javascript does
          // not have multidot flag.
          String s = arguments(0).toString().replaceAll("<![^>]+>\\s*", "")
              .replaceAll("</?html[\\s\\S]*?>\\s*", "")
              .replaceAll("<head[\\s\\S]*?</head>\\s*", "")
              .replaceAll("<script[\\s\\S]*?</script>\\s*", "")
              .replaceAll("</?body[\\s\\S]*?>\\s*", "");
          // We wrap the results in a div
          s = "<div>" + s + "</div>";

          Ajax.this.empty().append(filter.isEmpty() ? $(s) : $(s).find(filter));
          if (onSuccess != null) {
            onSuccess.setElement(Ajax.this.get(0));
            onSuccess.f();
          }
        } catch (Exception e) {
          if (GWT.getUncaughtExceptionHandler() != null) {
            GWT.getUncaughtExceptionHandler().onUncaughtException(e);
          }
        }
      }
    });
    ajax(s);
    return this;
  }

  /**
   * Load an external resource using the link tag element.
   * It is appended to the head of the document.
   *
   * @param rel Specifies the relationship between the current document and the linked document.
   * @param url Specifies the location of the linked document
   * @return a Promise which will be resolved when the external resource has been loaded.
   */
  public static Promise loadLink(final String rel, final String url) {
    GQuery link = $("link[rel='" + rel + "'][href^='" + url + "']");
    if (link.isEmpty()) {
      return new PromiseFunction() {
        public void f(final Deferred dfd) {
          GQuery link = $("<link rel='" + rel + "' href='" + url + "'/>");
          link.on("load", new Function() {
            public void f() {
              // load event is fired before the imported stuff has actually
              // being ready, we delay it to be sure it is ready.
              new Timer() {
                public void run() {
                  dfd.resolve();
                }
              }.schedule(100);
            }
          });
          $(document.getHead()).append(link);
        }
      };
    } else {
      return Deferred().resolve().promise();
    }
  }

  /**
   * Load an external html resource using the link tag element, it sets
   * the relationship between the current document as 'import'.
   * It is very useful to import web-components.
   */
  public static Promise importHtml(String url) {
    return loadLink("import", url);
  }
}
