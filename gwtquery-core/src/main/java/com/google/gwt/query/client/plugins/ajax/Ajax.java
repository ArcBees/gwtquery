package com.google.gwt.query.client.plugins.ajax;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestBuilder.Method;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.Properties;
import com.google.gwt.query.client.builders.JsonBuilder;
import com.google.gwt.query.client.js.JsUtils;
import com.google.gwt.query.client.plugins.Plugin;
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

  public static void ajax(Properties p) {
    Settings s = createSettings();
    s.load(p);
    ajax(s);
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
  public static void ajax(Settings settings) {

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
    String url = resolveUrl(settings, httpMethod, data);
    final String dataType = settings.getDataType();
    
    if ("jsonp".equalsIgnoreCase(dataType)) {
      int timeout = settings.getTimeout();
      getJSONP(url, onSuccess, onError, timeout);
      return;
    }
    
    final RequestBuilder requestBuilder = createRequestBuilder(settings, httpMethod, url, data);
    requestBuilder.setCallback(new RequestCallback() {
      public void onError(Request request, Throwable exception) {
        if (onError != null) {
          onError.f(null, exception.getMessage(), request, null, exception);
        }
      }

      public void onResponseReceived(Request request, Response response) {
        if (response.getStatusCode() > 202) {
          if (onError != null) {
            onError.fe(response.getText(), "error", request, response);
          }
        } else if (onSuccess != null) {
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
          onSuccess.fe(retData, "success", request, response);
        }
      }
    });

    try {
      requestBuilder.send();
    } catch (RequestException e) {
      if (onError != null) {
        onError.f(null, -1, null, null, e);
      }
    }
  }

  private static RequestBuilder createRequestBuilder(Settings settings, Method httpMethod, String url, String data) {

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

    return requestBuilder;
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
          && httpMethod == RequestBuilder.POST
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
    }
    return RequestBuilder.POST;
  }

  public static void ajax(String url, Function onSuccess, Function onError) {
    ajax(url, onSuccess, onError, (Settings) null);
  }

  public static void ajax(String url, Function onSuccess, Function onError, Settings s) {
    if (s == null) {
      s = createSettings();
    }
    s.setUrl(url).setSuccess(onSuccess).setError(onError);
    ajax(s);
  }

  public static void ajax(String url, Properties p) {
    Settings s = createSettings();
    s.load(p);
    s.setUrl(url);
    ajax(s);
  }

  public static void ajax(String url, Settings settings) {
    ajax(settings.setUrl(url));
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

  public static void get(String url, Properties data, final Function onSuccess) {
    Settings s = createSettings();
    s.setUrl(url);
    s.setDataType("txt");
    s.setType("get");
    s.setData(data);
    s.setSuccess(onSuccess);
    ajax(s);
  }

  public static void getJSON(String url, Properties data, final Function onSuccess) {
    Settings s = createSettings();
    s.setUrl(url);
    s.setDataType("json");
    s.setType("post");
    s.setData(data);
    s.setSuccess(onSuccess);
    ajax(s);
  }
  
  public static void getJSONP(String url, Properties data, Function onSuccess) {
    Settings s = createSettings();
    s.setUrl(url);
    s.setDataType("jsonp");
    s.setType("get");
    s.setData(data);
    s.setSuccess(onSuccess);
    ajax(s);
  }
  
  public static void getJSONP(String url, Function success, Function error, int timeout) {
    if (!url.contains("=?") && !url.contains("callback=")) {
      url += (url.contains("?") ? "&" : "?") + "callback=?";
    }
    url += "&_=" + System.currentTimeMillis();
    Element e = $("head").get(0);
    if (e == null) {
      e = document.getDocumentElement();
    }
    getJsonpImpl(e, url, null, success, error == null ? success : error, timeout);
  }

  public static void post(String url, Properties data, final Function onSuccess) {
    Settings s = createSettings();
    s.setUrl(url);
    s.setDataType("txt");
    s.setType("post");
    s.setData(data);
    s.setSuccess(onSuccess);
    ajax(s);
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
          String s = getData()[0].toString().replaceAll("<![^>]+>\\s*", "")
            .replaceAll("(?si)</?html[^>]*>\\s*", "")
            .replaceFirst("(?si)<head[^>]*>.*</head>\\s*", "")
            .replaceFirst("(?si)<script[^>]*>.*</script>\\s*", "")
            .replaceAll("<?si></?body[^>]*>\\s*", "");
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
  
  private static int callBackCounter = 0;
  
  public static native void getJsonpImpl(Element elem, String url, String charset, Function success, Function error, int timeout) /*-{
    var fName = "__GQ_cb_" + @com.google.gwt.query.client.plugins.ajax.Ajax::callBackCounter ++;
    var done = false;
    $wnd[fName] = function(data) {
      if (!done) {
        done = true;
        $wnd[fName] = null;
        success.@com.google.gwt.query.client.Function::fe(Ljava/lang/Object;)(data);
      }
    }
    function err() {
      if (!done) {
        done = true;
        $wnd[fName] = null;
        var func = error ? error : success;
        func.@com.google.gwt.query.client.Function::fe(Ljava/lang/Object;)();
      }
    }
    if (timeout) {
      setTimeout(err, timeout);
    } 

    url = url.replace(/=\?/g,'=' + fName);
    var script = document.createElement("script" );
    script.async = "async";
    if (charset) script.charset = charset;
    script.src = url;
    script.onload = script.onreadystatechange = function(evt) {
      script.onload = script.onreadystatechange = null;
      elem.removeChild(script);
      err();
    };
    elem.insertBefore(script, elem.firstChild);
  }-*/;  
}
