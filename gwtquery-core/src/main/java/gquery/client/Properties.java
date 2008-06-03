package gquery.client;

import com.google.gwt.core.client.JavaScriptObject;

/**
 *
 */
public class Properties extends JavaScriptObject {

  protected Properties() { }
  public native static Properties create(String properties) /*-{
    return eval(properties);
  }-*/;
  
  public final native String get(String name) /*-{
    return this[name];
  }-*/;
  
  public final native int getInt(String name) /*-{
    return this[name];
  }-*/;
  
  public final native float getFloat(String name) /*-{
    return this[name];
  }-*/;
  
  public final native String[] keys() /*-{
    var key, keys=[];
    
    for(key in this) {
      keys.push(key); 
    }
    return keys;
  }-*/;
}
