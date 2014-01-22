package com.google.gwt.query.vm;

// import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.query.client.Binder;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQ;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.Promise;
import com.google.gwt.query.client.plugins.ajax.Ajax.AjaxTransport;
import com.google.gwt.query.client.plugins.ajax.Ajax.Settings;
import com.google.gwt.query.client.plugins.deferred.PromiseFunction;
import com.google.gwt.user.server.Base64Utils;

/**
 * 
 */
public class AjaxTransportJre implements AjaxTransport {
  
  private static String localDomain = null;
  
  private static CookieManager cookieManager = CookieManager.getInstance();

  private static boolean debugOutput = false;
  
  private static boolean followRedirections = true;
  
  public static void enableCORS(String domain) {
    localDomain = domain;
    System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
  }
  
  public static void enableDebug(boolean b) {
    debugOutput = b;
  }
  
  public static void enableCookies(boolean b) {
    cookieManager = b ? CookieManager.getInstance() : null;
  }
  
  public static void enableRedirections(boolean b) {
    followRedirections = b;
  }
  
  private final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:26.0) Gecko/20100101 Firefox/26.0";
  private final String jsonpCbRexp = "(?ms)^.*jre_callback\\((.*)\\).*$";

  public Promise getJsonP(final Settings settings) {
    String url = settings.getUrl().replaceFirst("callback=[^&]*", "");
    url += (url.contains("?") ? "&" : "?") + "callback=jre_callback";
    settings.setUrl(url);
    
    if (settings.getTimeout() < 1) {
      settings.setTimeout(10000);
    }
    
    return getXhr(settings, false)
      .then(new Function() {
        public Object f(Object... args) {
          Response response = arguments(0);
          if (response.getText().matches(jsonpCbRexp)) {
            return GQ.create(response.getText().replaceFirst(jsonpCbRexp, "$1"));
          } else {
            return GQuery.Deferred().reject().promise();
          }
        }
      });
  }

  public Promise getLoadScript(Settings settings) {
    return getXhr(settings, false);
  }
  
  public Promise getXhr(final Settings settings) {
    return getXhr(settings, true);
  }

  private Promise getXhr(final Settings settings, final boolean cors) {
    return new PromiseFunction() {
      public void f(Deferred dfd) {
        try {
          Response response = httpClient(settings, cors);
          int status = response.getStatusCode();
          if (status <= 0 || status >= 400) {
            String statusText = status <= 0 ? "Bad CORS" : response.getStatusText();
            dfd.reject(null, new RequestException("HTTP ERROR: " + status + " " + statusText + "\n" + response.getText()));
          } else {
            dfd.resolve(response, null);
          }
        } catch (Exception e) {
          e.printStackTrace();
          dfd.reject(e, null);
        }
      }
    };
  }

  private Response httpClient(Settings s, boolean cors) throws Exception {
    String url = s.getUrl();
    assert url.toLowerCase().startsWith("http");
    
    URL u = new URL(url);
    HttpURLConnection c = (HttpURLConnection) u.openConnection();
    
    c.setInstanceFollowRedirects(followRedirections);
    
    c.setRequestMethod(s.getType());
    c.setRequestProperty("User-Agent", USER_AGENT);
    if (s.getUsername() != null && s.getPassword() != null) {
      c.setRequestProperty ("Authorization", "Basic " + Base64Utils.toBase64((s.getUsername() + ":" + s.getPassword()).getBytes()));
    }
    if (cookieManager != null) {
      cookieManager.setCookies(c);
    }
    
    boolean isCORS = cors && localDomain != null && !s.getUrl().contains(localDomain);
    if (isCORS) {
      // TODO: fetch options previously to the request
      // >> OPTIONS
      // Origin: http://127.0.0.1:8888
      //   Access-Control-Allow-Origin: http://127.0.0.1:8888
      //   Access-Control-Allow-Credentials: true
      // Access-Control-Request-Headers: content-type
      //   Access-Control-Allow-Headers
      // Access-Control-Request-Method
      //   Access-Control-Allow-Methods: POST, GET
      //   Allow: GET, HEAD, POST, PUT, DELETE, TRACE, OPTIONS

      // >> POST/GET
      // Origin: http://127.0.0.1:8888
      //   Access-Control-Allow-Origin: http://127.0.0.1:8888
      //   Access-Control-Allow-Credentials: true
      c.setRequestProperty("Origin", localDomain);
    }
    
    if (s.getTimeout() > 0) {
      c.setConnectTimeout(s.getTimeout());
      c.setReadTimeout(s.getTimeout());
    }
    
    Binder headers = s.getHeaders();
    if (headers != null) {
      for (String h : headers.getFieldNames()) {
        c.setRequestProperty(h, "" + headers.get(h));
      }
    }
    
    if (s.getType().matches("POST|PUT")) {
      c.setRequestProperty("Content-Type", s.getContentType());
      
      debugRequest(c, s.getDataString());
      
      c.setDoOutput(true);
      DataOutputStream wr = new DataOutputStream(c.getOutputStream());
      wr.writeBytes(s.getDataString());
      wr.flush();
      wr.close();
    } else {
      debugRequest(c, null);
    }
    
    int code = c.getResponseCode();
    if (isCORS && !localDomain.equals(c.getHeaderField("Access-Control-Allow-Origin"))) {
      code = 0;
    }
    
    BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));
    String inputLine;
    StringBuffer response = new StringBuffer();
    while ((inputLine = in.readLine()) != null) {
      response.append(inputLine + "\n");
    }
    in.close();

    if (cookieManager != null) {
      cookieManager.storeCookies(c);
    }

    return new ResponseJre(code, c.getResponseMessage(), response.toString(), c.getHeaderFields());
  }
  
  private void debugRequest(HttpURLConnection c, String payload) {
    if (debugOutput) {
      System.out.println(c.getRequestMethod() + " " + c.getURL().getPath());
      for (String s : c.getRequestProperties().keySet()) {
        String v = c.getRequestProperties().get(s).get(0);
        if ("Cookie".equals(s)) {
          System.out.println(s + ":");
          for (String y : v.split("; ")) {
            System.out.println("        " + y);
          }
        } else {
          System.out.println(s + ": " + v);
        }
      }
      if (payload != null) {
        for (String y : payload.split("&")) {
          System.out.println(" " + y);
        }
      }
      System.out.println("");
    }
  }
}
