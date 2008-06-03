package gwtquery.samples.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.IncrementalCommand;
import gwtquery.client.*;
import static gwtquery.client.GQuery.$;
   
public class GwtQueryBenchModule implements EntryPoint {

    private StringBuffer log = new StringBuffer();

    private static final int MIN_TIME = 200;

    private static final String GCOMPILED = "gcompiled";

    private static final String DOMASSISTANT = "dresult";

    private static final String GDYNAMIC = "gresult";

    public void onModuleLoad() {
        final MySelectors m = GWT.create(MySelectors.class);

        final DeferredGQuery dg[] = m.getAllSelectors();
        initResultsTable(dg, "Compiled GQuery", GCOMPILED, "DOMAssistant-2.7" /*"DOMAssistant 2.7" */,
                DOMASSISTANT, "Dynamic GQuery", GDYNAMIC);
        runBenchmarks(dg, new GQueryCompiledBenchmark(),
                new DomAssistantBenchmark(), new GQueryDynamicBenchmark());
    }

    public interface Benchmark {

        public int runSelector(DeferredGQuery dq, String selector);

        String getId();
    }


    public void runBenchmarks(final DeferredGQuery[] dg,
                              final Benchmark... benchmark) {
        DeferredCommand.addCommand(new IncrementalCommand() {
            int selectorNumber = 0;
            int numCalls = 0;
            int benchMarkNumber = 0;
            double totalTimes[] = new double[benchmark.length];
            long cumTime = 0;

            int numRuns = 0;
            int winner = -1;
            double winTime = Double.MAX_VALUE;
            public boolean execute() {
                if (benchMarkNumber >= benchmark.length) {
                    benchMarkNumber = 0;
                    numCalls = 0;
                    cumTime = 0;
                    numRuns = 0;
                    setResultClass(benchmark[winner].getId(), selectorNumber, "win");
                    for (int i = 0; i < benchmark.length; i++) {
                        if (i != winner)
                            setResultClass(benchmark[i].getId(), selectorNumber, "lose");
                    }
                    selectorNumber++;
                    winner = -1;
                    winTime = Double.MAX_VALUE;
                    if (selectorNumber >= dg.length) {
                        double min = Double.MAX_VALUE;
                        for (int i = 0; i < totalTimes.length; i++) {
                            if (totalTimes[i] < min) min = totalTimes[i];
                        }
                        for (int i = 0; i < totalTimes.length; i++) {
                            d(benchmark[i].getId(), dg.length, (((int) (totalTimes[i] * 100)) / 100.0) + " ms");
                            setResultClass(benchmark[i].getId(),
                                    dg.length, totalTimes[i] <= min ? "win" : "lose");
                        }
                        return false;
                    }
                }
                DeferredGQuery d = dg[selectorNumber];
                long start = System.currentTimeMillis();
                int num = 0;
                long end = start;
                Benchmark m = benchmark[benchMarkNumber];
                String selector = d.getSelector();

                do {
                    num += m.runSelector(d, selector);
                    end = System.currentTimeMillis();
                    numCalls++;
                } while (end - start < MIN_TIME);
                double runtime = (double) (end - start) / numCalls;
                if (runtime < winTime) {
                    winTime = runtime;
                    winner = benchMarkNumber;
                }
                d(m.getId(), selectorNumber, runtime, (num / numCalls));
                totalTimes[benchMarkNumber] += runtime;
                numCalls = 0;
                benchMarkNumber++;
                return true;
            }
        });
    }

    private void setResultClass(String id, int i, String clz) {
        Element td = Document.get().getElementById(id + i);
        td.setClassName(clz);
    }

    private void d(String type, int i, String text) {
        Element td = Document.get().getElementById(type + i);
        td.setInnerHTML(text);
    }

    private void d(String type, int i, double v, int i1) {

        Element td = Document.get().getElementById(type + i);
        td.setInnerHTML(
                "" + (((int) (v * 100)) / 100.0) + " ms, found " + i1 + " nodes");
    }

    private void initResultsTable(DeferredGQuery[] dg, String... options) {
        int numRows = dg.length;
        Document doc = Document.get();
        Element table = doc.getElementById("resultstable");
        Element thead = doc.createTHeadElement();
        table.appendChild(thead);
        Element selectorHeader = doc.createTHElement();
        Element theadtr = doc.createTRElement();
        selectorHeader.setInnerHTML("Selector");
        theadtr.appendChild(selectorHeader);
        thead.appendChild(theadtr);

        Element tbody = doc.createTBodyElement();
        table.appendChild(tbody);

        for (int i = 0; i < options.length; i += 2) {
            Element th = doc.createTHElement();
            th.setInnerHTML(options[i]);
            theadtr.appendChild(th);
        }
        for (int i = 0; i < numRows; i++) {
            Element tr = doc.createTRElement();
            Element lab = doc.createTHElement();
            lab.setInnerHTML(dg[i].getSelector());
            tr.appendChild(lab);
            for (int j = 0; j < options.length; j += 2) {
                Element placeholder = doc.createTDElement();
                placeholder.setInnerHTML("Not Tested");
                placeholder.setId(options[j + 1] + i);
                tr.appendChild(placeholder);
            }
            tbody.appendChild(tr);
        }
        Element totalRow = doc.createTRElement();
        Element totalLab = doc.createTHElement();
        totalLab.setInnerHTML("Total");
        totalRow.appendChild(totalLab);
        for (int j = 0; j < options.length; j += 2) {
            Element placeholder = doc.createTDElement();
            placeholder.setInnerHTML("0");
            placeholder.setId(options[j + 1] + numRows);
            totalRow.appendChild(placeholder);
        }
        tbody.appendChild(totalRow);
    }

    private void d(String s) {
        log.append(s + "<br>");
    }
    
    private static class GQueryCompiledBenchmark implements Benchmark {

        public int runSelector(DeferredGQuery dq, String selector) {
            return dq.array(null).getLength();
        }

        public String getId() {
            return GCOMPILED;
        }
    }

    private static class DomAssistantBenchmark implements Benchmark {

        public native int runSelector(DeferredGQuery dq, String selector) /*-{
         return $wnd.$(selector).length;
    }-*/;

        public String getId() {
            return DOMASSISTANT;
        }
    }

    private static class GQueryDynamicBenchmark implements Benchmark {
        private SelectorEngine engine;

        private GQueryDynamicBenchmark() {
            engine = new SelectorEngine();
        }

        public int runSelector(DeferredGQuery dq, String selector) {
            return engine.select(selector, Document.get()).getLength();
        }

        public String getId() {
            return GDYNAMIC;
        }
    }
}
