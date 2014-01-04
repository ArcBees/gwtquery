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
import com.google.gwt.query.client.Promise;
import com.google.gwt.query.client.plugins.ajax.Ajax.AjaxTransport;
import com.google.gwt.query.client.plugins.ajax.Ajax.Settings;
import com.google.gwt.query.client.plugins.deferred.PromiseFunction;
import com.google.gwt.user.server.Base64Utils;

/**
 * 
 */
public class AjaxTransportJre implements AjaxTransport {
  
  private final String USER_AGENT = "Mozilla/5.0";

  public Promise getJsonP(final Settings settings) {
    String url = settings.getUrl().replaceFirst("callback=[^&]*", "");
    url += (url.contains("?") ? "&" : "?") + "callback=jre_callback";
    settings.setUrl(url);
    
    return getXhr(settings)
      .then(new Function() {
        public Object f(Object... args) {
          ResponseJre response = arguments(0);
          return GQ.create(response.getText().replaceFirst("jre_callback\\((.*)\\)", "$1"));
        }
      });
  }

  public Promise getLoadScript(Settings settings) {
    return getXhr(settings);
  }

  public Promise getXhr(final Settings settings) {
    return new PromiseFunction() {
      public void f(Deferred dfd) {
        try {
          Response response = httpClient(settings);
          int status = response.getStatusCode();
          if (status <= 0 || status >= 400) {
            String statusText = status <= 0 ? "Bad CORS" : response.getStatusText();
            dfd.reject(null, new RequestException("HTTP ERROR: " + status + " " + statusText + "\n" + response.getText()));
          } else {
            dfd.resolve(response, null);
          }
        } catch (Exception e) {
          dfd.reject(e, null);
        }
      }
    };
  }

  private Response httpClient(Settings s) throws Exception {

    URL u = new URL(s.getUrl());

    HttpURLConnection c = (HttpURLConnection) u.openConnection();
    c.setRequestMethod(s.getType());
    c.setRequestProperty("User-Agent", USER_AGENT);
    if (s.getUsername() != null && s.getPassword() != null) {
      c.setRequestProperty ("Authorization", "Basic " + Base64Utils.toBase64((s.getUsername() + ":" + s.getPassword()).getBytes()));
    }
    
    Binder headers = s.getHeaders();
    if (headers != null) {
      for (String h : headers.getFieldNames()) {
        c.setRequestProperty(h, "" + headers.get(h));
      }
    }
    
    if (!s.getType().equals("GET")) {
      String ctype = s.getDataType();
      if (s.getDataType().toLowerCase().startsWith("json")) {
        ctype = "application/json; charset=utf-8";
      } 
      c.setRequestProperty("Content-Type", ctype);
      
      c.setDoOutput(true);
      DataOutputStream wr = new DataOutputStream(c.getOutputStream());
      wr.writeBytes(s.getDataString());
      wr.flush();
      wr.close();
    }

    int code = c.getResponseCode();
    BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));
    String inputLine;
    StringBuffer response = new StringBuffer();
    while ((inputLine = in.readLine()) != null) {
      response.append(inputLine);
    }
    in.close();
    
    return new ResponseJre(code, c.getResponseMessage(), response.toString(), c.getHeaderFields());
  }
}
