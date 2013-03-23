package com.google.gwt.query.client.plugins.ajax;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestBuilder.Method;
import com.google.gwt.http.client.Response;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.Promise;
import com.google.gwt.query.client.Properties;
import com.google.gwt.query.client.builders.JsonBuilder;
import com.google.gwt.query.client.js.JsUtils;
import com.google.gwt.query.client.plugins.Plugin;
import com.google.gwt.query.client.plugins.deferred.Deferred;
import com.google.gwt.query.client.plugins.deferred.PromiseReqBuilder;
import com.google.gwt.query.client.plugins.deferred.PromiseReqBuilderJSONP;
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
  
  /**
   * Ajax Settings object
   */
  public interface Settings extends JsonBuilder {
    String getContentType();
    Element getContext();
    Properties getData();
    String getDataString();
    String getDataType();
    Function getError();
    Properties getHeaders();
    String getPassword();
    Function getSuccess();
    int getTimeout();
    String getType();
    String getUrl();
    String getUsername();
    Settings setContentType(String t);
    Settings setContext(Element e);
    Settings setData(Properties p);
    Settings setDataString(String d);
    Settings setDataType(String t);
    Settings setError(Function f);
    Settings setHeaders(Properties p);
    Settings setPassword(String p);
    Settings setSuccess(Function f);
    Settings setTimeout(int t);
    Settings setType(String t);
    Settings setUrl(String u);
    Settings setUsername(String u);
  }

  public static final Class<Ajax> Ajax = registerPlugin(Ajax.class, new Plugin<Ajax>() {
    public Ajax init(GQuery gq) {
      return new Ajax(gq);
    }
  });

  public static Promise ajax(Properties p) {
    Settings s = createSettings();
    s.load(p);
    return ajax(s);
  }

  /**
   * Perform an ajax request to the server.
   *
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
   * @param url The url to connect
   * @param onSuccess a function to execute in the case of success
   * @param onError the function to execute on error
   * @param settings a Properties object with the configuration of the Ajax request.
   */
  public static Promise ajax(Settings settings) {
    final Deferred dfd = $().as(Deferred.Deferred);

    final Function onSuccess = settings.getSuccess();
    if (onSuccess != null) {
      onSuccess.setElement(settings.getContext());
    }

    final Function onError = settings.getError();
    if (onError != null) {
      onError.setElement(settings.getContext());
    }

    Method httpMethod = resolveHttpMethod(settings);
    String data = resolveData(settings, httpMethod);
    final String url = resolveUrl(settings, httpMethod, data);
    final String dataType = settings.getDataType();

    if ("jsonp".equalsIgnoreCase(dataType)) {
      return getJSONP(url, onSuccess, onError, settings.getTimeout());
    } else {
      return createPromiseRequestBuilder(settings, httpMethod, url, data)
        .then(new Function() {
          public Object f(Object...args) {
            Response response = (Response)args[0];
            Request request = (Request)args[1];
            Object retData = null;
            try {
              if ("xml".equalsIgnoreCase(dataType)) {
                retData = JsUtils.parseXML(response.getText());
              } else if ("json".equalsIgnoreCase(dataType)) {
                retData = JsUtils.parseJSON(response.getText());
              } else {
                retData = response.getText();
              }
            } catch (Exception e) {
              if (GWT.getUncaughtExceptionHandler() != null) {
                GWT.getUncaughtExceptionHandler().onUncaughtException(e);
              }
            }
            return new Object[]{retData, "success", request, response};
          }
        }, new Function() {
          public Object f(Object...args) {
            Throwable exception = (Throwable)args[0];
            Response request = (Response)args[1];
            return new Object[]{null, exception.getMessage(), request, null, exception};
          }
        })
        .done(onSuccess)
        .fail(onError);
    }
  }
  
  private static Promise createPromiseRequestBuilder(Settings settings, Method httpMethod, String url, String data) {

    RequestBuilder requestBuilder = new RequestBuilder(httpMethod, url);

    if (data != null && httpMethod != RequestBuilder.GET) {
      String ctype = settings.getContentType();
      if (ctype == null) {
        String type = settings.getDataType();
        if (type != null && type.toLowerCase().startsWith("json")) {
          ctype = "application/json; charset=utf-8";
        } else {
          ctype = FormPanel.ENCODING_URLENCODED;
        }
      }
      requestBuilder.setHeader("Content-Type", ctype);
      requestBuilder.setRequestData(data);
    }

    requestBuilder.setTimeoutMillis(settings.getTimeout());

    String user = settings.getUsername();
    if (user != null) {
      requestBuilder.setUser(user);
    }

    String password = settings.getPassword();
    if (password != null) {
      requestBuilder.setPassword(password);
    }

    Properties headers = settings.getHeaders();
    if (headers != null) {
      for (String headerKey : headers.keys()) {
        requestBuilder.setHeader(headerKey, headers.getStr(headerKey));
      }
    }

    return new PromiseReqBuilder(requestBuilder);
  }

  private static String resolveUrl(Settings settings, Method httpMethod, String data) {
    String url = settings.getUrl();
    assert url != null : "no url found in settings";
    if (httpMethod == RequestBuilder.GET && data != null) {
      url += (url.contains("?") ? "&" : "?") + data;
    }
    return url;
  }

  private static String resolveData(Settings settings, Method httpMethod) {
    String data = settings.getDataString();
    if (data == null && settings.getData() != null) {
      String type = settings.getDataType();
      if (type != null
          && (httpMethod == RequestBuilder.POST || httpMethod == RequestBuilder.PUT)
          && type.equalsIgnoreCase("json")) {
        data = settings.getData().toJsonString();
      } else {
        data = settings.getData().toQueryString();
      }
    }
    return data;
  }

  private static Method resolveHttpMethod(Settings settings) {
    String method = settings.getType();
    if ("get".equalsIgnoreCase(method)) {
      return RequestBuilder.GET;
    }else if("post".equalsIgnoreCase(method)){
    	return RequestBuilder.POST;
    }else if("put".equalsIgnoreCase(method)){
    	return RequestBuilder.PUT;
    }else if("delete".equalsIgnoreCase(method)){
    	return RequestBuilder.DELETE;
    }else if("head".equalsIgnoreCase(method)){
    	return RequestBuilder.HEAD;
    }

    GWT.log("unknow method type -> use POST as default method");
    return RequestBuilder.POST;
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

  public static Promise ajax(String url, Properties p) {
    Settings s = createSettings();
    s.load(p);
    s.setUrl(url);
    return ajax(s);
  }

  public static Promise ajax(String url, Settings settings) {
    return ajax(settings.setUrl(url));
  }

  public static Settings createSettings() {
    return createSettings($$(""));
  }

  public static Settings createSettings(String prop) {
    return createSettings($$(prop));
  }

  public static Settings createSettings(Properties p) {
    Settings s = GWT.create(Settings.class);
    s.load(p);
    return s;
  }

  public static Promise get(String url, Properties data, Function onSuccess) {
    Settings s = createSettings();
    s.setUrl(url);
    s.setDataType("txt");
    s.setType("get");
    s.setData(data);
    s.setSuccess(onSuccess);
    return ajax(s);
  }

  public static Promise getJSON(String url, Properties data, Function onSuccess) {
    Settings s = createSettings();
    s.setUrl(url);
    s.setDataType("json");
    s.setType("post");
    s.setData(data);
    s.setSuccess(onSuccess);
    return ajax(s);
  }
  
  public static Promise getJSONP(String url) {
    return getJSONP(url, null, null, 0);
  }
  
  public static Promise getJSONP(String url, Properties data, Function onSuccess) {
    Settings s = createSettings();
    s.setUrl(url);
    s.setDataType("jsonp");
    s.setType("get");
    s.setData(data);
    s.setSuccess(onSuccess);
    return ajax(s);
  }

  public static Promise getJSONP(String url, Function success, Function error, int timeout) {
    return new PromiseReqBuilderJSONP(url, null, timeout)
       .done(success)
       .fail(error);
  }

  public static Promise post(String url, Properties data, final Function onSuccess) {
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

  public Ajax load(String url, Properties data, final Function onSuccess) {
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
          String s = getArguments()[0].toString().replaceAll("<![^>]+>\\s*", "")
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
}
