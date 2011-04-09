package gwtquery.samples.client;

import static com.google.gwt.query.client.GQuery.$;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.query.client.js.JsCache;
import com.google.gwt.query.client.js.JsMap;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

import java.util.HashMap;

public class JsCollectionVsJavaCollection implements EntryPoint{
  
  public static final int MAX_ITEMS = 100000;
  
  
  public void onModuleLoad() {
    
    Button b = new Button("run test");
    b.addClickHandler(new ClickHandler() {
      
      public void onClick(ClickEvent event) {
        $(".gwt-label").remove();
        testJsMapVsHashMap();       
      }
    });
    
    RootPanel.get().add(b);
    
  }
  

  public void testJsMapVsHashMap() {
    log("Testing jsMap");
    
    log("init phase");
    for (int i = 0; i < MAX_ITEMS; i++){
      new String(""+i);
    }
    
    JsCache cache = JsCache.create();
    JsMap<String, Object> jsMap = cache.cast();
    
    log("Testing jsMap : put "+MAX_ITEMS+" items in the map :");

    long ellapsedTime = System.currentTimeMillis();
    for (int i = 0; i < MAX_ITEMS; i++){
      jsMap.put(new String(""+i), new Object());
    }
    ellapsedTime = System.currentTimeMillis() - ellapsedTime;
    log(" ellapsed Time : "+ellapsedTime);
    
    log("Testing jsMap : do "+MAX_ITEMS+" get in the map :");
    long totalTime = 0;
    
    for (int i = 0 ; i < MAX_ITEMS; i++){
      int random = Random.nextInt(MAX_ITEMS);
      ellapsedTime = System.currentTimeMillis();
      jsMap.get(new String(""+random));
      totalTime += (System.currentTimeMillis() - ellapsedTime);
    }
    log(" ellapsed Time : "+totalTime);
    
    log("Testing jsMap : do "+MAX_ITEMS+" exist (JsCache.exists() ) in the cache :");
    totalTime = 0;
    
    for (int i = 0 ; i < MAX_ITEMS; i++){
      int random = Random.nextInt(MAX_ITEMS);
      ellapsedTime = System.currentTimeMillis();
      cache.exists(new String(""+random));
      totalTime += (System.currentTimeMillis() - ellapsedTime);
    }
    log(" ellapsed Time : "+totalTime);
    
    log("Testing jsMap : get all keys :");
    
    ellapsedTime = System.currentTimeMillis();
    jsMap.keys();
    ellapsedTime = System.currentTimeMillis() - ellapsedTime;
    log(" ellapsed Time : "+ellapsedTime);
    
    log("Testing jsMap : get all values (JsCache.elements() ) :");
    
    ellapsedTime = System.currentTimeMillis();
    cache.elements();
    ellapsedTime = System.currentTimeMillis() - ellapsedTime;
    log(" ellapsed Time : "+ellapsedTime);
    
    log("-------------");
    log("");
    
    HashMap<String, Object> hashMap = new HashMap<String, Object>();
    
    log("Testing hashMap : put "+MAX_ITEMS+" items in the map :");
    ellapsedTime = System.currentTimeMillis();
    for (int i = 0; i < MAX_ITEMS; i++){
      hashMap.put(new String(""+i), new Object());
    }
    ellapsedTime = System.currentTimeMillis() - ellapsedTime;
    log(" ellapsed Time : "+ellapsedTime);
    
    log("Testing hashMap : do "+MAX_ITEMS+" get in the map :");
    totalTime = 0;
    
    for (int i = 0 ; i < MAX_ITEMS; i++){
      int random = Random.nextInt(MAX_ITEMS);
      ellapsedTime = System.currentTimeMillis();
      hashMap.get(new String(""+random));
      totalTime += (System.currentTimeMillis() - ellapsedTime);
    }
    log(" ellapsed Time : "+totalTime);
    
    log("Testing hashMap : do "+MAX_ITEMS+" containsKey() :");
    totalTime = 0;
    
    for (int i = 0 ; i < MAX_ITEMS; i++){
      int random = Random.nextInt(MAX_ITEMS);
      ellapsedTime = System.currentTimeMillis();
      hashMap.containsKey(new String(""+random));
      totalTime += (System.currentTimeMillis() - ellapsedTime);
    }
    log(" ellapsed Time : "+totalTime);
    
    log("Testing hashMap : get all keys :");
    
    ellapsedTime = System.currentTimeMillis();
    hashMap.keySet();
    ellapsedTime = System.currentTimeMillis() - ellapsedTime;
    log(" ellapsed Time : "+ellapsedTime);
    
    log("Testing hashMap : get all values :");
    
    ellapsedTime = System.currentTimeMillis();
    hashMap.values();
    ellapsedTime = System.currentTimeMillis() - ellapsedTime;
    log(" ellapsed Time : "+ellapsedTime);
    
    log("-------------");
    
  }




  public void log(String msg) {
    RootPanel.get().add(new Label(msg));
  }


}
