package gwtquery.samples.client;

import static com.google.gwt.query.client.GQuery.$;
import static com.google.gwt.query.client.GQuery.document;
import static com.google.gwt.query.client.GQuery.lazy;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.Selector;
import com.google.gwt.query.client.Selectors;

public class GwtQuerySampleModule implements EntryPoint {

  public interface Sample extends Selectors {
    @Selector(".note")
    GQuery allNotes();
  }

  public void onModuleLoad() {
    // Use compiled selectors
    Sample s = GWT.create(Sample.class);
    
    // Just a simple usage of dom manipulation, events, and lazy usage
    s.allNotes().text("Hello Google I/O").
      css("cursor", "pointer").
      toggle(
        lazy().css("color", "red").done(),
        lazy().css("color", "").done()
      );
    
    // Cascade effects
    $("<div id='id1'>content</div>").appendTo(document).hide().fadeIn(5000).fadeOut(3000);
  }
  
  
}
