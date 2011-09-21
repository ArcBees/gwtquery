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
import com.google.gwt.query.client.Properties;
import com.google.gwt.query.client.builders.JsonBuilder;
import com.google.gwt.query.client.js.JsUtils;

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
 * This class is not a plugin because in jquery it does not extends the jquery
 * object, but we prefer this name-space in order to centralize the jquery core 
 * features in a common folder.
 * 
 */
public class Ajax {
  
  /**
   * Ajax Settings object 
   */
  public interface Settings extends JsonBuilder {
    String getType();
    Settings setType(String t);
    String getUrl();
    Settings setUrl(String u);
    Properties getData();
    Settings setData(Properties p);
    String getDataString();
    Settings setDataString(String d);
    String getDataType();
    Settings setDataType(String t);
    int getTimeout();
    Settings setTimeout(int t);
    String getUsername();
    Settings setUsername(String u);
    String getPassword();
    Settings setPassword(String p);
    String getContentType();
    Settings setContentType(String t);
    Properties getHeaders();
    Settings setHeaders(Properties p);
    Element getContext();
    Settings setContext(Element e);
    Function getSuccess();
    Settings setSuccess(Function f);
    Function getError();
    Settings setError(Function f);
  }
  
  public void ajax(String url, Function onSuccess, Function onError, Properties p) {
    Settings settings = GWT.create(Settings.class);
    settings.load(p);
    ajax(url, onSuccess, onError, settings);
  }
  
  public void ajax(String url, Function onSuccess, Function onError) {
    ajax(url, onSuccess, onError, (Settings)null);
  }
  
  public void ajax(String url, Function onSuccess, Function onError, Settings settings) {
    if (settings == null) {
      settings = GWT.create(Settings.class);
    }
    settings.setUrl(url).setSuccess(onSuccess).setError(onError);
    ajax(settings);
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
  public void ajax(Settings settings) {
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

      public void onError(Request request, Throwable exception) {
        if (onError != null) {
          onError.f(null, exception.getMessage(), request, null, exception);
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
}
