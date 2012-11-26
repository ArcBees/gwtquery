package gwtquery.jsplugins.menu.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.query.jsquery.client.JsQuery;

public class JsQueryMenu implements EntryPoint {

  public void onModuleLoad() {
    JsMenu.loadPlugin();
    JsQuery.onLoad();
  }
}
