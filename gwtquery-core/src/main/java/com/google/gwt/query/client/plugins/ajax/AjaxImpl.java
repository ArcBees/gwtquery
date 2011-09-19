package com.google.gwt.query.client.plugins.ajax;

import static com.google.gwt.query.client.GQuery.$$;

import com.google.gwt.dom.client.Element;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.RequestBuilder.Method;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQ;
import com.google.gwt.query.client.Properties;
import com.google.gwt.query.client.js.JsUtils;

/**
 * Ajax class for GQuery.
 * 
 * It is not actually a plugin but we prefer this name-space in order
 * to centralize jquery core features in a common folder.
 *
 */
public class AjaxImpl {
  
  /**
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
  public void ajax(String url, Function onSuccess, Function onError,
      Properties settings) {
    Method httpMethod = RequestBuilder.POST;
    String method = settings.getStr("type");
    if ("get".equalsIgnoreCase(method)) {
      httpMethod = RequestBuilder.GET;
    }

    if (settings.getStr("url") != null) {
      url = settings.getStr("url");
    }

    String data = null;
    Properties props = settings.getJavaScriptObject("data");
    if (props != null) {
      data = props.toQueryString();
    } else {
      data = settings.getStr("data");
    }

    if (data != null && httpMethod == RequestBuilder.GET) {
      url += (url.contains("?") ? "&" : "?") + data;
    }
    RequestBuilder r = new RequestBuilder(httpMethod, url);
    if (data != null && httpMethod != RequestBuilder.GET) {
      r.setRequestData(data);
    }

    int timeout = settings.getInt("timeout");
    r.setTimeoutMillis(timeout);

    String user = settings.getStr("username");
    if (user != null) {
      r.setUser(user);
    }

    String password = settings.getStr("password");
    if (password != null) {
      r.setPassword(password);
    }

    String ctype = settings.getStr("contentType");
    r.setHeader("Content-type", ctype != null ? ctype
        : "application/x-www-form-urlencoded");

    final String dataType = settings.defined("dataType")
        ? settings.getStr("dataType") : "text";
        
    Properties p = settings.getJavaScriptObject("headers");
    if (p != null) {
      for (String s : p.keys()) {
        r.setHeader(s, p.getStr(s));
      }
    }

    Element ctx = settings.getJavaScriptObject("context");
    if (ctx != null) {
      if (onSuccess != null) {
        onSuccess.setElement(ctx);
      }
      if (onError != null) {
        onError.setElement(ctx);
      }
    }

    Object o = settings.getObject("success");
    final Function success = (o != null && (o instanceof Function))
        ? (Function) o : onSuccess;

    o = settings.getObject("error");
    final Function error = (o != null && (o instanceof Function))
        ? (Function) o : onError;

    r.setCallback(new RequestCallback() {
      public void onResponseReceived(Request request, Response response) {
        if (response.getStatusCode() > 202) {
          if (error != null) {
            error.f(response.getText(), "error", request, response);
          }
        } else if (success != null) {
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
          success.f(retData, "success", request, response);
        }
      }

      public void onError(Request request, Throwable exception) {
        if (error != null) {
          error.f(null, exception.getMessage(), request, null, exception);
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
