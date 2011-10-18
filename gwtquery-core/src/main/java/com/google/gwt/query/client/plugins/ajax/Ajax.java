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

/**
 * Ajax class for GQuery.
 * 
 * The jQuery library has a full suite of AJAX capabilities, but GWT
 * is plenty of classes to get data from server side: RPC, XHR, RF, etc.
 * 
 * This class is not a substitute for the GWT utilities, but a complement
 * to get server data in a jquery way, specially when querying non java 
 * backends.
 * 
 * We do not pretend to clone all the jquery Ajax API inside gquery, just take 
 * its syntax and to implement the most popular usage of it. 
 * This implementation is almost thought to be used as an alternative to 
 * the GWT-XHR, GWT-XML and GWT-JSON modules.
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
  
  public static final Class<Ajax> Ajax = 
    registerPlugin(Ajax.class, new Plugin<Ajax>() {
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
    Method httpMethod = RequestBuilder.POST;
    String method = settings.getType();
    if ("get".equalsIgnoreCase(method)) {
      httpMethod = RequestBuilder.GET;
    }
    
    String url = settings.getUrl();

    String data = settings.getDataString();
    if (data == null) {
      data = settings.getData().toQueryString();
    }

    if (data != null && httpMethod == RequestBuilder.GET) {
      url += (url.contains("?") ? "&" : "?") + data;
    }
    RequestBuilder r = new RequestBuilder(httpMethod, url);
    if (data != null && httpMethod != RequestBuilder.GET) {
      r.setRequestData(data);
    }

    r.setTimeoutMillis(settings.getTimeout());

    String user = settings.getUsername();
    if (user != null) {
      r.setUser(user);
    }

    String password = settings.getPassword();
    if (password != null) {
      r.setPassword(password);
    }

    String ctype = settings.getContentType();
    r.setHeader("Content-type", ctype != null ? ctype
        : "application/x-www-form-urlencoded");

    final String dataType = settings.getDataType() != null
        ? settings.getDataType() : "text";
        
    Properties p = settings.getHeaders();
    for (String s : p.keys()) {
      r.setHeader(s, p.getStr(s));
    }
    
    final Function onSuccess = settings.getSuccess();
    if (onSuccess != null) {
      onSuccess.setElement(settings.getContext());
    }
    final Function onError = settings.getError();
    if (onError != null) {
      onError.setElement(settings.getContext());
    }

    r.setCallback(new RequestCallback() {
      public void onError(Request request, Throwable exception) {
        if (onError != null) {
          onError.f(null, exception.getMessage(), request, null, exception);
        }
      }

      public void onResponseReceived(Request request, Response response) {
        if (response.getStatusCode() > 202) {
          if (onError != null) {
            onError.f(response.getText(), "error", request, response);
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
            System.err.println("Error parsing '" + dataType
                + "' received data: " + e.getMessage());
            System.err.println("Server response was: \n" + response.getText());
          }
          onSuccess.f(retData, "success", request, response);
        }
      }
    });

    try {
      r.send();
    } catch (RequestException e) {
      if (onError != null) {
        onError.f(null, -1, null, null, e);
      }
    }
  }
  
  public static void ajax(String url, Function onSuccess, Function onError) {
    ajax(url, onSuccess, onError, (Settings)null);
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
        GQuery d = $(getData()[0].toString());
        Ajax.this.empty().append(filter.isEmpty() ? d : d.filter(filter));
        if (onSuccess != null) {
          onSuccess.setElement(Ajax.this.get(0));
          onSuccess.f();
        }
      }
    });
    ajax(s);
    return this;
  }
}
