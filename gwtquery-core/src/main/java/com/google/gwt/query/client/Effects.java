/*
 * Copyright 2009 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.gwt.query.client;

import com.google.gwt.core.client.Duration;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.user.client.Timer;

/**
 * Effects plugin for Gwt Query.
 */
public class Effects extends GQuery {

  /**
   * Built in easing functions.
   */
  public enum Easing {

    /**
     * Linear easing function.
     */
    LINEAR {
      public double ease(double p, double n, double firstNum, double diff) {
        return firstNum + diff * p;
      }
    },
    /**
     * Sinusoidal easing function.
     */
    SWING {
      public double ease(double p, double n, double firstNum, double diff) {
        return ((-Math.cos(p * Math.PI) / 2) + 0.5) * diff + firstNum;
      }
    };

    /**
     * Override to implement custom easing functions.
     */
    public abstract double ease(double p, double n, double firstNum,
        double diff);
  }

  /**
   * Build in speed constants.
   */
  public enum Speed {

    /**
     * 600 millisecond animation.
     */
    SLOW(600),
    /**
     * 200 millisecond animation.
     */
    FAST(200),
    /**
     * 400 millisecond animation.
     */
    DEFAULT(400);

    private final int duration;

    Speed(int dur) {
      this.duration = dur;
    }

    public int getDuration() {
      return duration;
    }
  }

  /**
   * Utility class.
   */
  protected class PropFx {

    public SpeedOpts opt;

    public Element elem;

    public String prop;

    private double startTime;

    private double start;

    private double end;

    private String unit;

    private double now;

    private double pos;

    private double state;

    public double cur(boolean force) {
      if (elem.getPropertyString(prop) != null && (elem.getStyle() == null
          || elem.getStyle().getProperty(prop) == null)) {
        return elem.getPropertyDouble(prop);
      }
      double r = parseDouble(GQuery.curCSS(elem, prop, force));
      r = !Double.isNaN(r) && r > -10000 ? r
          : parseDouble(GQuery.curCSS(elem, prop, false));
      if (Double.isNaN(r)) {
        r = 0;
      }
      return r;
    }

    public void hide() {
      opt.cache.put(prop, elem.getStyle().getProperty(prop));
      opt.hide = true;
      custom(cur(false), 0);
    }

    public Effects hide(Speed speed) {
      return hide(speed, null);
    }

    public Effects hide(Speed speed, Function callback) {
      return animate(genFx("hide", 3), speed, Easing.LINEAR, callback);
    }

    public Effects show(Speed speed) {
      return show(speed, null);
    }

    public Effects show(Speed speed, Function callback) {
      return animate(genFx("show", 3), speed, Easing.LINEAR, callback);
    }

    public void show() {
      opt.cache.put(prop, elem.getStyle().getProperty(prop));
      opt.show = true;
      custom("width".equals(prop) || "height".equals(prop) ? 1 : 0, cur(false));
      $(elem).show();
    }

    public Effects toggle(Speed speed) {
      return hide(speed, null);
    }

    public Effects toggle(Speed speed, Function callback) {
      return animate(genFx("toggle", 3), speed, Easing.LINEAR, callback);
    }

    public void update() {
      if ("opacity".equals(prop)) {
        GQuery.setStyleProperty(prop, "" + now, elem);
      } else {
        if (elem.getStyle() != null
            && elem.getStyle().getProperty(prop) != null) {
          elem.getStyle().setProperty(prop, now + unit);
        } else {
          elem.setPropertyString(prop, "" + now);
        }
      }
      if (("height".equals(prop) || "width".equals(prop))
          && elem.getStyle() != null) {
        elem.getStyle().setProperty("display", "block");
      }
    }

    private void custom(double from, double to) {
      custom(from, to, "px");
    }

    private void custom(double from, double to, String unit) {
      startTime = Duration.currentTimeMillis();
      start = from;
      end = to;
      now = start;
      this.unit = unit;
      Timer t = new Timer() {
        @Override
        public void run() {
          if (!step(false)) {
            cancel();
          }
        }
      };
      t.scheduleRepeating(13);
    }

    private boolean step(boolean gotoEnd) {
      double t = Duration.currentTimeMillis();

      if (gotoEnd || t >= opt.duration + startTime) {
        now = end;
        pos = start = 1;
        update();
        opt.curAnim.set(prop, "true");
        boolean done = true;
        for (String key : opt.curAnim.keys()) {
          if (!"true".equals(opt.curAnim.get(key))) {
            done = false;
          }
        }
        if (done) {
          if (opt.display != null) {
            elem.getStyle().setProperty("overflow", opt.overflow);
            elem.getStyle().setProperty("display", opt.display);
            if ("none".equals(GQuery.curCSS(elem, "display", false))) {
              elem.getStyle().setProperty("display", "block");
            }
          }
          if (opt.hide) {
            $(elem).hide();
          }
          if (opt.hide || opt.show) {
            for (String key : opt.curAnim.keys()) {
              elem.getStyle().setProperty(key, opt.cache.getString(key));
            }
          }
          opt.complete(elem);
        }
        return false;
      } else {
        double n = t - startTime;
        state = n / opt.duration;
        pos = opt.easing.ease(this.state, n, 0, 1);
        now = start + ((this.end - this.start) * this.pos);

        update();
        return true;
      }
    }
  }

  /**
   * Used to register the plugin.
   */
  private static class EffectsPlugin implements Plugin<Effects> {

    public Effects init(GQuery gq) {
      return new Effects(gq.get());
    }
  }

  private class SpeedOpts {

    public String display;

    public String overflow;

    public Properties curAnim;

    public boolean hide;

    public boolean show;

    private Properties properties;

    private int duration;

    private Effects.Easing easing;

    private Function complete;

    private boolean queue = true;

    private GQuery.DataCache cache = DataCache.createObject().cast();

    protected SpeedOpts(int speed, Easing easing, Function complete) {
      this.complete = complete;
      this.easing = easing;
      this.duration = speed;
    }

    protected SpeedOpts(Speed speed, Easing easing, Function complete) {
      this.complete = complete;
      this.easing = easing;
      this.duration = speed.getDuration();
    }

    public void complete(Element elem) {
      if (queue) {
        $(elem).dequeue();
      }
      if (complete != null) {
        complete.f(elem);
      }
    }

    public Function getComplete() {
      return complete;
    }

    public int getDuration() {
      return duration;
    }

    public Easing getEasing() {
      return easing;
    }

    public Properties getProperties() {
      return properties;
    }

    public boolean isQueue() {
      return queue;
    }

    public void setComplete(Function complete) {
      this.complete = complete;
    }

    public void setDuration(int duration) {
      this.duration = duration;
    }

    public void setEasing(Easing easing) {
      this.easing = easing;
    }

    public void setProperties(Properties properties) {
      this.properties = properties;
    }

    public void setQueue(boolean queue) {
      this.queue = queue;
    }
  }

  public static final Class<Effects> Effects = Effects.class;

  private static String[][] fxAttrs = {
      {"height", "marginTop", "marginBottom", "paddingTop", "paddingBottom"},
      {"width", "marginLeft", "marginRight", "paddingLeft", "paddingRight"},
      {"opacity"}};

  static {
    GQuery.registerPlugin(Effects.class, new EffectsPlugin());
  }

  private static Properties genFx(String type, int num) {
    Properties prop = Properties.createObject().cast();
    for (int i = 0; i < num; i++) {
      for (int j = 0; j < fxAttrs[i].length; j++) {
        prop.set(fxAttrs[i][j], type);
      }
    }
    return prop;
  }

  // don't valid double after parsing
  private static native double parseDouble(String dstr) /*-{
    return parseFloat(dstr);
  }-*/;

  private DataCache elemDisplay = DataCache.createObject().cast();

  public Effects(Element element) {
    super(element);
  }

  public Effects(JSArray elements) {
    super(elements);
  }

  public Effects(NodeList list) {
    super(list);
  }

  public Effects animate(final Properties properties, final Speed speed,
      final Easing easing, final Function complete) {
    return animate(properties, speed.getDuration(), easing, complete);
  }

  public Effects animate(final Properties properties, final int speed,
      final Easing easing, final Function complete) {
    if (!"false".equals(properties.get("queue"))) {
      queue(new Function() {
        final SpeedOpts optall = new SpeedOpts(speed, easing, complete);

        @Override
        public void f(Element e) {
          boolean hidden = !$(e).visible();
          for (String key : properties.keys()) {
            String prop = properties.get(key);
            if ("hide".equals(prop) && hidden
                || "show".equals(prop) && !hidden) {
              optall.complete(e);
              return;
            }
            if ("height".equals(key)
                || "width".equals(key) && e.getStyle() != null) {
              optall.display = $(e).css("display");
              optall.overflow = e.getStyle().getProperty("overflow");
            }
          }
          if (optall.overflow != null) {
            e.getStyle().setProperty("overflow", "hidden");
          }
          optall.curAnim = properties.cloneProps();
          for (String key : properties.keys()) {
            PropFx fx = new PropFx();
            String val = properties.get(key);
            fx.elem = e;
            fx.opt = optall;
            fx.prop = key;
            if ("toggle".equals(val)) {
              if (hidden) {
                fx.show();
              } else {
                fx.hide();
              }
            } else if ("show".equals(val)) {
              fx.show();
            } else if ("hide".equals(val)) {
              fx.hide();
            } else {
              JSArray parts = new Regexp("^([+-]=)?([0-9+-.]+)(.*)$")
                  .match(val);
              double start = fx.cur(true);

              if (parts != null) {
                double end = parseDouble(parts.getStr(2));
                String unit = parts.getStr(3);
                if (unit == null) {
                  unit = "px";
                }
                if (!"px".equals(unit)) {
                  e.getStyle().setProperty(key, (end != 0 ? end : 1) + unit);
                  start = (end != 0 ? end : 1) / fx.cur(true) * start;
                  e.getStyle().setProperty(key, start + unit);
                }
                if (parts.getStr(1) != null) {
                  end = (("-=".equals(parts.getStr(1)) ? -1 : 1) * end) + start;
                }
                fx.custom(start, end, unit);
              } else {
                fx.custom(start, parseDouble(val), "");
              }
            }
          }
        }
      });
    }
    return this;
  }

  /**
   * Fade in all matched elements by adjusting their opacity. Only the opacity
   * is adjusted for this animation, meaning that all of the matched elements
   * should already have some form of height and width associated with them.
   */
  public Effects fadeIn() {
    return fadeIn(Speed.DEFAULT);
  }

  /**
   * Fade in all matched elements by adjusting their opacity. Only the opacity
   * is adjusted for this animation, meaning that all of the matched elements
   * should already have some form of height and width associated with them.
   */
  public Effects fadeIn(Speed speed) {
    return fadeIn(speed, null);
  }

  /**
   * Fade in all matched elements by adjusting their opacity. Only the opacity
   * is adjusted for this animation, meaning that all of the matched elements
   * should already have some form of height and width associated with them.
   */
  public Effects fadeIn(int speed) {
    return fadeIn(speed, null);
  }

  /**
   * Fade in all matched elements by adjusting their opacity and firing an
   * optional callback after completion. Only the opacity is adjusted for this
   * animation, meaning that all of the matched elements should already have
   * some form of height and width associated with them.
   */
  public Effects fadeIn(Speed speed, Function callback) {
    return animate($$("opacity: \"show\""), speed, Easing.LINEAR, callback);
  }

  /**
   * Fade in all matched elements by adjusting their opacity and firing an
   * optional callback after completion. Only the opacity is adjusted for this
   * animation, meaning that all of the matched elements should already have
   * some form of height and width associated with them.
   */
  public Effects fadeIn(int speed, Function callback) {
    return animate($$("opacity: \"show\""), speed, Easing.LINEAR, callback);
  }

  /**
   * Fade out all matched elements by adjusting their opacity to 0, then setting
   * display to "none". Only the opacity is adjusted for this animation, meaning
   * that all of the matched elements should already have some form of height
   * and width associated with them.
   */
  public Effects fadeOut() {
    return fadeOut(Speed.DEFAULT);
  }

  /**
   * Fade out all matched elements by adjusting their opacity to 0, then setting
   * display to "none". Only the opacity is adjusted for this animation, meaning
   * that all of the matched elements should already have some form of height
   * and width associated with them.
   */
  public Effects fadeOut(Speed speed) {
    return fadeOut(speed, null);
  }

  /**
   * Fade out all matched elements by adjusting their opacity to 0, then setting
   * display to "none". Only the opacity is adjusted for this animation, meaning
   * that all of the matched elements should already have some form of height
   * and width associated with them.
   */
  public Effects fadeOut(int speed) {
    return fadeOut(speed, null);
  }

  /**
   * Fade out all matched elements by adjusting their opacity to 0, then setting
   * display to "none" and firing an optional callback after completion. Only
   * the opacity is adjusted for this animation, meaning that all of the matched
   * elements should already have some form of height and width associated with
   * them.
   */
  public Effects fadeOut(Speed speed, Function callback) {
    return animate($$("opacity: \"hide\""), speed, Easing.LINEAR, callback);
  }

  /**
   * Fade out all matched elements by adjusting their opacity to 0, then setting
   * display to "none" and firing an optional callback after completion. Only
   * the opacity is adjusted for this animation, meaning that all of the matched
   * elements should already have some form of height and width associated with
   * them.
   */
  public Effects fadeOut(int speed, Function callback) {
    return animate($$("opacity: \"hide\""), speed, Easing.LINEAR, callback);
  }

  /**
   * Fade the opacity of all matched elements to a specified opacity. Only the
   * opacity is adjusted for this animation, meaning that all of the matched
   * elements should already have some form of height and width associated with
   * them.
   */
  public Effects fadeTo(Speed speed, double opacity) {
    return fadeTo(speed, opacity, null);
  }

  /**
   * Fade the opacity of all matched elements to a specified opacity and firing
   * an optional callback after completion. Only the opacity is adjusted for
   * this animation, meaning that all of the matched elements should already
   * have some form of height and width associated with them.
   */
  public Effects fadeTo(Speed speed, double opacity, Function callback) {
    return animate($$("opacity: " + opacity), speed, Easing.LINEAR, callback);
  }

  /**
   * Hides each of the set of matched elements if they are shown.
   */
  public Effects hide() {
    for (Element e : elements()) {
      GQuery q = $(e);
      String old = (String) q.data("olddisplay");
      if (old != null && !"none".equals(old)) {
        q.data("olddisplay", GQuery.curCSS(e, "display", false));
      }
      e.getStyle().setProperty("display", "none");
    }
    return this;
  }

  /**
   * Displays each of the set of matched elements if they are hidden.
   */
  public Effects show() {
    for (Element e : elements()) {
      GQuery q = $(e);
      String old = (String) q.data("olddisplay");
      e.getStyle().setProperty("display", SelectorEngine.or(old, ""));
      if ("none".equals(GQuery.curCSS(e, "display", false))) {
        String tagName = e.getTagName();
        String display = "";
        if (elemDisplay.getString(tagName) != null) {
          display = elemDisplay.getString(tagName);
        } else {
          Element elem = $("<" + tagName + ">").appendTo($("body")).get(0);
          display = GQuery.curCSS(elem, "display", false);
          if ("none".equals(display)) {
            display = "block";
          }
        }
        e.getStyle().setProperty("display", display);
        q.data("olddisplay", display);
      }
    }
    return this;
  }

  /**
   * Reveal all matched elements by adjusting their height .
   */
  public Effects slideDown() {
    return slideDown(Speed.DEFAULT, null);
  }

  /**
   * Reveal all matched elements by adjusting their height .
   */
  public Effects slideDown(Speed speed) {
    return slideDown(speed, null);
  }

  /**
   * Reveal all matched elements by adjusting their height and firing an
   * optional callback after completion.
   */
  public Effects slideDown(Speed speed, Function callback) {
    return animate(genFx("show", 1), speed, Easing.LINEAR, callback);
  }

  /**
   * Toggle the visibility of all matched elements by adjusting their height.
   * Only the height is adjusted for this animation, causing all matched
   * elements to be hidden or shown in a "sliding" manner
   */
  public Effects slideToggle() {
    return slideToggle(Speed.DEFAULT, null);
  }

  /**
   * Toggle the visibility of all matched elements by adjusting their height.
   * Only the height is adjusted for this animation, causing all matched
   * elements to be hidden or shown in a "sliding" manner
   */
  public Effects slideToggle(Speed speed) {
    return slideToggle(speed, null);
  }

  /**
   * Toggle the visibility of all matched elements by adjusting their height and
   * firing an optional callback after completion. Only the height is adjusted
   * for this animation, causing all matched elements to be hidden or shown in a
   * "sliding" manner
   */
  public Effects slideToggle(Speed speed, Function callback) {
    return animate(genFx("toggle", 1), speed, Easing.LINEAR, callback);
  }

  /**
   * Hide all matched elements by adjusting their height .
   */
  public Effects slideUp() {
    return slideUp(Speed.DEFAULT, null);
  }

  /**
   * Hide all matched elements by adjusting their height.
   */
  public Effects slideUp(Speed speed) {
    return slideUp(speed, null);
  }

  /**
   * Hide all matched elements by adjusting their height and firing an optional
   * callback after completion.
   */
  public Effects slideUp(Speed speed, Function callback) {
    return animate(genFx("hide", 1), speed, Easing.LINEAR, callback);
  }

  /**
   * Toggle displaying each of the set of matched elements.
   */
  public Effects toggle() {
    for (Element e : elements()) {
      Effects ef = new Effects(e);
      if (ef.visible()) {
        ef.hide();
      } else {
        ef.show();
      }
    }
    return this;
  }

  public boolean visible() {
    return !"none".equalsIgnoreCase(this.css("display"));
  }
}
