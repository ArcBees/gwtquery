package gwtquery.jsquery.client;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportClosure;
import org.timepedia.exporter.client.ExportInstanceMethod;
import org.timepedia.exporter.client.ExportOverlay;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.GQuery.Offset;
import com.google.gwt.query.client.Predicate;
import com.google.gwt.query.client.Properties;
import com.google.gwt.query.client.plugins.Effects;
import com.google.gwt.query.client.plugins.effects.PropertiesAnimation;
import com.google.gwt.query.client.plugins.effects.PropertiesAnimation.Easing;
import com.google.gwt.user.client.Event;


@ExportPackage("")
@Export(value="jQuery", all=false)
public class JQ implements ExportOverlay<GQuery> {
  
  @ExportPackage("")
  @Export("jFunction")
  @ExportClosure()
  public interface JFunction extends ExportOverlay<Function>  {
    public void f();
// Element and Event are JavascriptObjects so we export one.    
//    public void f(Element e);
    public boolean f(Event e);
    public Object f(Element e, int i);
  }

  @ExportPackage("")
  @Export("jPredicate")
  @ExportClosure()
  public interface JPredicate extends ExportOverlay<Predicate>  {
    public boolean f(Element e, int i);
  }
  
  @ExportPackage("")
  @Export("jOffset")
  public static class JOffset implements ExportOverlay<Offset>{
    public int left;
    public int top;
    public JOffset(int left, int top) {
    }
  }
  
  @ExportPackage("")
  @Export("jEasing")
  @ExportClosure()
  public interface JEasing extends ExportOverlay<Easing>  {
    public double interpolate(double progress);
  }
  
  // Used to export $ method 
  public static class Dollar implements Exportable {
    public native static String dumpObject(JavaScriptObject o) /*-{
      var s = "" ; for (k in o) s += " " + k; return s;
    }-*/;
    public native static boolean isEvent(JavaScriptObject o) /*-{
      return o.preventDefault ? true : false;
    }-*/;
    public native static boolean isElement(JavaScriptObject o) /*-{
      return o.getElementById ? true : false;
    }-*/;
    @Export("$wnd.$")
    public static GQuery StaticDollar(Object o) {
      if (o instanceof String) {
        return GQuery.$((String)o);
      } else if (isEvent((JavaScriptObject)o)){
        return GQuery.$((Event)o);
      } else if (isElement((JavaScriptObject)o)){
        return GQuery.$((Element)o);
      }
      return GQuery.$();
    }
  }
  
  // We have to stub all the method we want to export here.
  private JQ(){}
  public String toString() {return null;}
  public GQuery add(GQuery previousObject) {return null;}
  public GQuery add(String selector) {return null;}
  public GQuery addClass(String... classes) {return null;}
  public GQuery after(GQuery query) {return null;}
  public GQuery after(Node n) {return null;}
  public GQuery after(String html) {return null;}
  public GQuery andSelf() {return null;}
  
  @ExportInstanceMethod
  public static GQuery animate(GQuery g, Object stringOrProperties, int duration, String easing, Function... funcs) {
    return g.animate(stringOrProperties, duration, "linear".equalsIgnoreCase(easing)? PropertiesAnimation.Easing.SWING : PropertiesAnimation.Easing.SWING, funcs);
  }
//  public GQuery animate(Object stringOrProperties, int duration, Easing easing, Function... funcs) {return null;}
  public GQuery animate(Object stringOrProperties, Function... funcs) {return null;}
  public GQuery animate(Object stringOrProperties, int duration, Function... funcs) {return null;}
  public GQuery append(GQuery query) {return null;}
  public GQuery append(Node n) {return null;}
  public GQuery append(String html) {return null;}
  public GQuery appendTo(GQuery other) {return null;}
  public GQuery appendTo(Node n) {return null;}
  public GQuery appendTo(String html) {return null;}
  public <T extends GQuery> T as(Class<T> plugin) {return null;}
//  public GQuery attr(Properties properties) {return null;}
  public String attr(String name) {return null;}
  public GQuery attr(String key, Function closure) {return null;}
  public GQuery attr(String key, String value) {return null;}
  public GQuery before(GQuery query) {return null;}
  public GQuery before(Node n) {return null;}
  public GQuery before(String html) {return null;}
//  public GQuery bind(int eventbits, Object data, Function... funcs) {return null;}
  public GQuery bind(String eventType, Object data, Function... funcs) {return null;}
  public GQuery blur(Function... f) {return null;}
  public GQuery change(Function... f) {return null;}
  public GQuery children() {return null;}
  public GQuery children(String... filters) {return null;}
  public GQuery clearQueue() {return null;}  
  public GQuery clone() {return null;}
  public GQuery clearQueue(String queueName) {return null;}
  public GQuery click(Function... f) {return null;}
  public GQuery closest(String selector) {return null;}
//  public JsNamedArray<NodeList<Element>> closest(String[] selectors) {return null;}
//  public JsNamedArray<NodeList<Element>> closest(String[] selectors, Node context) {return null;}
  public GQuery closest(String selector, Node context) {return null;}
  public GQuery contains(String text) {return null;}
  public GQuery contents() {return null;}
  public GQuery css(Properties properties) {return null;}
  public String css(String name) {return null;}
  public String css(String name, boolean force) {return null;}
  public GQuery css(String prop, String val) {return null;}
  public double cur(String prop) {return 0;}
  public double cur(String prop, boolean force) {return 0;}
  public Object data(String name) {return null;}
//  public <T> T data(String name, Class<T> clz) {return null;}
  public GQuery data(String name, Object value) {return null;}
  public GQuery dblclick(Function... f) {return null;}
  public GQuery delay(int milliseconds, Function... f) {return null;}
  public GQuery delay(int milliseconds, String queueName, Function... f) {return null;}
  public GQuery delegate(String selector, String eventType, Function... handlers) {return null;}
  public GQuery delegate(String selector, String eventType, Object data, Function... handlers) {return null;}
  public GQuery delegate(String selector, int eventbits, Function... handlers) {return null;}
  public GQuery delegate(String selector, int eventbits, Object data, Function... handlers) {return null;}
  public GQuery dequeue() {return null;}
  public GQuery dequeue(String queueName) {return null;}
  public GQuery detach() {return null;}
  public GQuery detach(String filter) {return null;}
  public GQuery die() {return null;}
  public GQuery die(String eventName) {return null;}
//  public GQuery die(int eventbits) {return null;}
  public GQuery each(Function... f) {return null;}
  public Element[] elements() {return null;}
  public GQuery empty() {return null;}
//  public GQuery end() {return null;}
  public GQuery eq(int pos) {return null;}
  public GQuery error(Function... f) {return null;}
  public GQuery fadeIn(Function... f) {return null;}
  public GQuery fadeIn(int millisecs, Function... f) {return null;}
  public GQuery fadeOut(Function... f) {return null;}
  public GQuery fadeOut(int millisecs, Function... f) {return null;}
  public Effects fadeToggle(int millisecs, Function... f) {return null;}
  public GQuery filter(Predicate filterFn) {return null;}
  public GQuery filter(String... filters) {return null;}
  public GQuery find(String... filters) {return null;}
  public GQuery first() {return null;}
  public GQuery focus(Function... f) {return null;}
  public NodeList<Element> get() {return null;}
  public Element get(int i) {return null;}
  public Node getContext() {return null;}
  public GQuery getPreviousObject() {return null;}
  public String getSelector() {return null;}
  public GQuery gt(int pos) {return null;}
  public boolean hasClass(String... classes) {return false;}
  public int height() {return 0;}
  public GQuery height(int height) {return null;}
  public GQuery height(String height) {return null;}
  public GQuery hide() {return null;}
  public GQuery hover(Function fover, Function fout) {return null;}
  public String html() {return null;}
  public GQuery html(String html) {return null;}
  public String id() {return null;}
  public GQuery id(String id) {return null;}
  public int index(Element element) {return 0;}
  public int innerHeight() {return 0;}
  public int innerWidth() {return 0;}
  public GQuery insertAfter(Element elem) {return null;}
  public GQuery insertAfter(GQuery query) {return null;}
  public GQuery insertAfter(String selector) {return null;}
  public GQuery insertBefore(Element item) {return null;}
  public GQuery insertBefore(GQuery query) {return null;}
  public GQuery insertBefore(String selector) {return null;}
  public boolean is(String... filters) {return false;}
  public boolean isEmpty() {return false;}
  public GQuery keydown(Function... f) {return null;}
  public GQuery keydown(int key) {return null;}
  public GQuery keypress(Function... f) {return null;}
  public GQuery keypress(int key) {return null;}
  public GQuery keyup(Function... f) {return null;}
  public GQuery keyup(int key) {return null;}
  public GQuery last() {return null;}
  public int left() {return 0;}
  public int length() {return 0;}
  public GQuery live(String eventName, Function... funcs) {return null;}
//  public GQuery live(int eventbits, Function... funcs) {return null;}
//  public GQuery live(int eventbits, Object data, Function... funcs) {return null;}
  public GQuery live(String eventName, Object data, Function... funcs) {return null;}
  public GQuery load(Function f) {return null;}
  public GQuery lt(int pos) {return null;}
//  public <W> List<W> map(Function f) {return null;}
  public GQuery mousedown(Function... f) {return null;}
  public GQuery mousemove(Function... f) {return null;}
  public GQuery mouseout(Function... f) {return null;}
  public GQuery mouseover(Function... f) {return null;}
  public GQuery mouseup(Function... f) {return null;}
  public GQuery next() {return null;}
  public GQuery next(String... selectors) {return null;}
  public GQuery nextAll() {return null;}
  public GQuery nextUntil(String selector) {return null;}
  public GQuery not(Element elem) {return null;}
  public GQuery not(GQuery gq) {return null;}
  public GQuery not(String... filters) {return null;}
//  public Offset offset() {return null;}
  public GQuery offsetParent() {return null;}
  public GQuery one(int eventbits, Object data, Function f) {return null;}
  public int outerHeight() {return 0;}
  public int outerHeight(boolean includeMargin) {return 0;}
  public int outerWidth() {return 0;}
  public int outerWidth(boolean includeMargin) {return 0;}
  public GQuery parent() {return null;}
  public GQuery parent(String... filters) {return null;}
  public GQuery parents() {return null;}
  public GQuery parents(String... filters) {return null;}
  public GQuery parentsUntil(String selector) {return null;}
//  public Offset position() {return null;}
  public GQuery prepend(GQuery query) {return null;}
  public GQuery prepend(Node n) {return null;}
  public GQuery prepend(String html) {return null;}
  public GQuery prependTo(GQuery other) {return null;}
  public GQuery prependTo(Node n) {return null;}
  public GQuery prependTo(String html) {return null;}
  public GQuery prev() {return null;}
  public GQuery prev(String... selectors) {return null;}
  public GQuery prevAll() {return null;}
  public GQuery prevUntil(String selector) {return null;}
  public boolean prop(String key) {return false;}
  public GQuery prop(String key, boolean value) {return null;}
  public GQuery prop(String key, Function closure) {return null;}
  public int queue() {return 0;}
  public int queue(String queueName) {return 0;}
  public GQuery queue(Function... f) {return null;}
  public GQuery queue(String queueName, Function... f) {return null;}
  public GQuery remove() {return null;}
  public GQuery remove(String filter) {return null;}
  public GQuery removeAttr(String key) {return null;}
  public GQuery removeClass(String... classes) {return null;}
  public GQuery removeData(String name) {return null;}
  public GQuery replaceAll(Element elem) {return null;}
  public GQuery replaceAll(GQuery target) {return null;}
  public GQuery replaceAll(String selector) {return null;}
  public GQuery replaceWith(Element elem) {return null;}
  public GQuery replaceWith(GQuery target) {return null;}
  public GQuery replaceWith(String html) {return null;}
  public GQuery resize(Function... f) {return null;}
  public void restoreCssAttrs(String... cssProps) {}
  public void resize(Function f) {}
  public void saveCssAttrs(String... cssProps) {}
  public GQuery scroll(Function... f) {return null;}
  public GQuery scrollIntoView() {return null;}
  public GQuery scrollIntoView(boolean ensure) {return null;}
  public int scrollLeft() {return 0;}
  public GQuery scrollLeft(int left) {return null;}
  public GQuery scrollTo(int left, int top) {return null;}
  public int scrollTop() {return 0;}
  public GQuery scrollTop(int top) {return null;}
  public GQuery select() {return null;}
  public GQuery setArray(NodeList<Element> list) {return null;}
  public void setPreviousObject(GQuery previousObject) {}
  public GQuery setSelector(String selector) {return null;}
  public GQuery show() {return null;}
  public GQuery siblings() {return null;}
  public GQuery siblings(String... selectors) {return null;}
  public int size() {return 0;}
  public GQuery slice(int start, int end) {return null;}
  public Effects slideDown(Function... f) {return null;}
  public Effects slideDown(int millisecs, Function... f) {return null;}
  public Effects slideToggle(int millisecs, Function... f) {return null;}
  public Effects slideUp(Function... f) {return null;}
  public Effects slideUp(int millisecs, Function... f) {return null;}
  public GQuery stop() {return null;}
  public GQuery stop(boolean clearQueue) {return null;}
  public GQuery stop(boolean clearQueue, boolean jumpToEnd) {return null;}
  public GQuery submit(Function... funcs) {return null;}
  public String text() {return null;}
  public GQuery text(String txt) {return null;}
  public GQuery toggle() {return null;}
  public GQuery toggle(Function... fn) {return null;}
  public GQuery toggleClass(String... classes) {return null;}
  public GQuery toggleClass(String clz, boolean addOrRemove) {return null;}
  public int top() {return 0;}
  public String toString(boolean pretty) {return null;}
//  public GQuery trigger(int eventbits, int... keys) {return null;}
//  public GQuery unbind(int eventbits) {return null;}
  public GQuery undelegate() {return null;}
  public GQuery undelegate(String selector) {return null;}
  public GQuery undelegate(String selector, String eventName) {return null;}
  public GQuery undelegate(String selector, int eventBit) {return null;}
//  public JsNodeArray unique(NodeList<Element> result) {return null;}
  public GQuery unwrap() {return null;}
  public String val() {return null;}
  public GQuery val(String... values) {return null;}
  public String[] vals() {return null;}
  public boolean isVisible() {return false;}
  public int width() {return 0;}
  public GQuery width(int width) {return null;}
  public GQuery wrap(Element elem) {return null;}
  public GQuery wrap(GQuery query) {return null;}
  public GQuery wrap(String html) {return null;}
  public GQuery wrapAll(Element elem) {return null;}
  public GQuery wrapAll(GQuery query) {return null;}
  public GQuery wrapAll(String html) {return null;}
  public GQuery wrapInner(Element elem) {return null;}
  public GQuery wrapInner(GQuery query) {return null;}
  public GQuery wrapInner(String html) {return null;}
}