package com.google.gwt.query.client.plugins;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.JSArray;
import com.google.gwt.query.client.Plugin;
import com.google.gwt.query.client.Properties;
import com.google.gwt.query.client.Regexp;
import com.google.gwt.query.client.SelectorEngine;
import com.google.gwt.user.client.Event;

/**
 * Star Rating plugin.
 */
public class Ratings extends GQuery {

  private static int calls;

  /**
   * Used to register the plugin.
   */
  private static class RatingsPlugin implements Plugin<Ratings> {

    public Ratings init(GQuery gq) {
      return new Ratings(gq.get());
    }
  }

  public static final Class<Ratings> Ratings = Ratings.class;

  static {
    GQuery.registerPlugin(Ratings.class, new RatingsPlugin());
  }

  public Ratings(Element element) {
    super(element);
  }

  public Ratings(JSArray elements) {
    super(elements);
  }

  public Ratings(NodeList<Element> list) {
    super(list);
  }

  public Ratings rating() {
    if (size() == 0) {
      return this;
    }
    calls++;
    not(".star-rating-applied").addClass("star-rating-applied");
    Control control = null;
    for (Element e : elements()) {
      GQuery input = $(e);
      String eid = SelectorEngine
          .or(e.getPropertyString("name"), "unnamed-rating")
          .replaceAll("\\[|\\]", "_").replaceAll("^\\_+|\\_$", "");
      GQuery context = $(getContext(e));
      Raters raters = (Raters) context.data("rating");
      if (raters == null || raters.getCalls() != calls) {
        raters = new Raters(0, calls);
      }
      GQuery rater = raters.get(eid);
      if (rater != null) {
        control = (Control) rater.data("rating");
      }
      if (rater != null && control != null) {
        control.bumpCount();
      } else {
        control = new Control();
        control.setSerial(raters.bumpCount());
        Properties metadata = getMetaData(input.get(0).getClassName());
        if (metadata != null && metadata.defined("split")) {
          control.setSplit(metadata.getInt("split"));
        }
        // create rating element
        rater = $("<span class=\"star-rating-control\"/>");
        input.before(rater);

        // Mark element for initialization (once all stars are ready)
        rater.addClass("rating-to-be-drawn");

        // Accept readOnly setting from 'disabled' property
        if (SelectorEngine.truth(input.attr("disabled"))) {
          control.setReadOnly(true);
        }

        // Create 'cancel' button
        GQuery query = $(
            "<div class=\"rating-cancel\"><a title=\"" + control.cancel + "\">"
                + control.cancelValue + "</a></div>").
            mouseover(new Function() {
              @Override
              public boolean f(Event e) {
                $(e).as(Ratings).drain();
                $(e).addClass("star-rating-hover");
                return true;
              }
            }).
            mouseout(new Function() {
              @Override
              public boolean f(Event e) {
                $(e).as(Ratings).draw();
                $(e).removeClass("star-rating-hover");
                return true;
              }
            }).
            click(new Function() {
              @Override
              public boolean f(Event e) {
                $(e).as(Ratings).selectStar();
                return true;
              }
            });
        control.cancelButton = query;
        query.data("rating", control);
        rater.append(query);
      }
      // insert rating star
      GQuery star = $("<div class=\"star-rating rater-" + control.getSerial()
          + "\"><a title=\""
          + (SelectorEngine.or(e.getTitle(), e.getPropertyString("value")))
          + "\">" + e.getPropertyString("value") + "</a></div>");
      rater.append(star);

      // inherit attributes from input element
      if (e.getId() != null) {
        star.attr("id", e.getId());
      }
      if (e.getClassName() != null) {
        star.addClass(e.getClassName());
      }

      // Half-stars?
      if (control.isHalf()) {
        control.setSplit(2);
      }

      // Prepare division control
      if (control.getSplit() > 0) {
        int stw = star.width();
        if (stw == 0) {
          stw = control.getStarWidth();
        }

        int spi = (control.getCount() % control.getSplit());
        int spw = (int) Math.floor(stw / control.getSplit());

        star.width(spw).find("a").css("margin-left", "-" + (spi * spw) + "px");
      }
      ;

      // readOnly?
      if (control.isReadOnly())//{ //save a byte!
      // Mark star as readOnly so user can customize display
      {
        star.addClass("star-rating-readonly");
      }
      //}  //save a byte!
      else//{ //save a byte!
      // Enable hover css effects
      {
        star.addClass("star-rating-live")
            // Attach mouse events
            .mouseover(new Function() {
              @Override
              public boolean f(Event e) {
                $(e).as(Ratings).fill();
                $(e).as(Ratings).focusStar();
                return true;
              }
            }).mouseout(new Function() {
          @Override
          public boolean f(Event e) {
            $(e).as(Ratings).draw();
            $(e).as(Ratings).blurStar();

            return true;
          }
        }).click(new Function() {
          @Override
          public boolean f(Event e) {
            $(e).as(Ratings).selectStar();
            return true;
          }
        });
      }
      // set current selection
      if (e.getPropertyBoolean("checked")) {
        control.setCurrent(star);
      }

      // hide input element
      input.hide();

      // backward compatibility, form element to plugin
      input.change(new Function() {
        @Override
        public boolean f(Event e) {
          $(e).as(Ratings).selectStar();
          return true;
        }
      });

      // attach reference to star to input element and vice-versa
      star.data("rating.input", input.data("rating.star", star));

      // store control information in form (or body when form not available)
      control.addStar(star.get(0));
      control.addInput(input.get(0));
      control.setRater(rater);
      raters.put(eid, rater);
      control.setContext(context);

      input.data("rating", control);
      rater.data("rating", control);
      star.data("rating", control);
      context.data("rating", raters);
    }
    // Initialize ratings (first draw)
    $(".rating-to-be-drawn").as(Ratings).draw()
        .removeClass("rating-to-be-drawn");

    return this;
  }

  private Properties getMetaData(String className) {
    if (className.indexOf("{") == -1) {
      return Properties.createObject().cast();
    }
    Regexp re = new Regexp("{(.*)}");
    JSArray match = re.exec(className);
    if (match != null && match.size() > 1) {
      return Properties.create(match.getStr(1));
    }
    return Properties.createObject().cast();
  }

  public Ratings blurStar() {
    return this;
  }

  public Ratings focusStar() {
    Control control = (Control) this.data("rating");
    if (control == null) {
      return this;
    }
//   GQuery input = data("rating.input");
    return this;
  }

  public Ratings fill() {
    for (Element e : elements()) {
      GQuery self = $(e);
      Control control = nullControlIfShouldSkipStar(self);
      if (control == null) {
        continue;
      }

      // Reset all stars and highlight them up to this element
      self.as(Ratings).drain();
      self.prevAll().andSelf().filter(".rater-" + control.getSerial())
          .addClass("star-rating-hover");

    }

    return this;
  }

  public Ratings draw() {
    for (Element e : elements()) {
      GQuery self = $(e);
      Control control = nullControlIfShouldSkipStar(self);
      if (control == null) {
        continue;
      }

      // Clear all stars
      self.as(Ratings).drain();
      // Set control value
      if (control.getCurrent() != null) {
        ((GQuery) control.current.data("rating.input"))
            .attr("checked", "checked");
        control.current.prevAll().andSelf().filter(".rater-" + control.serial)
            .addClass("star-rating-on");
      } else {
        $(control.getInputs()).removeAttr("checked");
      }

      // Show/hide 'cancel' button
//			control.cancel[control.readOnly || control.required?'hide':'show']();
      // Add/remove read-only classes to remove hand pointer
      self.siblings().toggleClass("star-rating-readonly", control.isReadOnly());
    }

    return this;
  }

  public Ratings selectStar(int value) {
    for (Element e : elements()) {
      GQuery self = $(e);
      Control control = nullControlIfShouldSkipStar(self);
      if (control == null) {
        continue;
      }

      control.setCurrent(null);
      $(control.getStar(value)).as(Ratings).selectStar();
    }
    return this;
  }

  public Ratings selectStar() {
    for (Element e : elements()) {
      GQuery self = $(e);
      Control control = nullControlIfShouldSkipStar(self);
      if (control == null) {
        continue;
      }

      control.current = self.get(0).getTagName().equalsIgnoreCase("INPUT")
          ? (GQuery) self.data("rating.star")
          : self.is(".rater-" + control.getSerial()) ? this : null;

      // Update rating control state
      self.data("rating", control);
      // Update display
      self.as(Ratings).draw();
      // find data for event
      GQuery input = $(control.current != null ? (GQuery) control.current
          .data("rating.input") : null);
      // click callback, as requested here: http://plugins.jquery.com/node/1655
//			if(control.callback) control.callback.apply(input[0], [input.val(), $('a', control.current)[0]]);// callback event
    }

    return this;
  }

  private Control nullControlIfShouldSkipStar(GQuery q) {
    Control control = (Control) q.data("rating");
    if (control == null) {
      return null;
    }
    // do not execute when control is in read-only mode
    if (control.isReadOnly()) {
      return null;
    }
    return control;
  }

  public Ratings selectStar(String value) {
    for (Element e : elements()) {
      GQuery self = $(e);
      Control control = nullControlIfShouldSkipStar(self);
      if (control == null) {
        continue;
      }
      control.setCurrent(null);
      NodeList<Element> stars = control.getStars();
      for (int i = 0; i < stars.getLength(); i++) {
        String val = ((GQuery) $(stars.getItem(i)).data("rating.input")).val();
        if (val != null && val.equals(value)) {
          $(stars.getItem(i)).as(Ratings).selectStar();
        }
      }
    }
    return this;
  }

  public Ratings drain() {
    for (Element e : elements()) {
      GQuery self = $(e);
      Control control = nullControlIfShouldSkipStar(self);
      if (control == null) {
        continue;
      }
      control.rater.children().filter(".rater-" + control.getSerial())
          .removeClass("star-rating-on").removeClass("star-rating-hover");
    }
    return this;
  }

  public static class Control {

    private int count;

    private String cancel = "Cancel Rating";

    private String cancelValue = "";

    private int split = 0;

    private int starWidth = 16;

    private int serial;

    private boolean readOnly;

    private boolean half;

    private GQuery current;

    private GQuery context;

    private JsArray<Element> stars = JsArray.createArray().cast();

    private JsArray<Element> inputs = JsArray.createArray().cast();

    private GQuery rater;

    public GQuery cancelButton;

    public int bumpCount() {
      return count++;
    }

    public void setSerial(int serial) {
      this.serial = serial;
    }

    public void setReadOnly(boolean readOnly) {
      this.readOnly = readOnly;
    }

    public int getSerial() {
      return serial;
    }

    public boolean isHalf() {
      return half;
    }

    public void setSplit(int split) {
      this.split = split;
    }

    public int getSplit() {
      return split;
    }

    public int getStarWidth() {
      return starWidth;
    }

    public int getCount() {
      return count;
    }

    public boolean isReadOnly() {
      return readOnly;
    }

    public void setCurrent(GQuery current) {
      this.current = current;
    }

    public void setContext(GQuery context) {
      this.context = context;
    }

    public void addStar(Element element) {
      stars.set(stars.length(), element);
    }

    public void addInput(Element element) {
      inputs.set(inputs.length(), element);
    }

    public void setRater(GQuery rater) {
      this.rater = rater;
    }

    public Object getCurrent() {
      return current;
    }

    public NodeList<Element> getInputs() {
      return inputs.cast();
    }

    public Element getStar(int value) {
      return stars.get(value);
    }

    public NodeList<Element> getStars() {
      return stars.cast();
    }
  }

  public static class Raters {

    private int calls;

    private int count;

    private GQuery.DataCache cache = GQuery.DataCache.createObject().cast();

    public Raters(int count, int calls) {
      this.count = count;
      this.calls = calls;
    }

    public int getCalls() {
      return calls;
    }

    public GQuery get(String eid) {
      return (GQuery) cache.getObject(eid);
    }

    public void put(String eid, GQuery q) {
      cache.put(eid, q);
    }

    public int bumpCount() {
      return count++;
    }
  }

  private static native Element getContext(Element e) /*-{
    return this.form || $doc.body;
  }-*/;
}
