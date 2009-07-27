package gwtquery.samples.client;

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
    Sample s = GWT.create(Sample.class);
    s.allNotes().text("Hello Google I/O");
  }
}
