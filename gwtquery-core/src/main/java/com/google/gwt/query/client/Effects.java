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

import com.google.gwt.animation.client.Animation;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.core.client.Duration;
import com.google.gwt.user.client.Timer;

/**
 * Effects plugin for Gwt Query.
 */
public class Effects extends GQuery {

  /**
   * Used to register the plugin.
   */
  private static class EffectsPlugin implements Plugin<Effects> {

    public Effects init(GQuery gq) {
      return new Effects(gq.get());
    }
  }

  public static final Class<Effects> Effects = Effects.class;

  static {
    GQuery.registerPlugin(Effects.class, new EffectsPlugin());
  }

  public enum Easing {

    LINEAR {
      public double ease(double p, double n, double firstNum, double diff) {
        return firstNum + diff * p;
      }
    }, SWING {
      public double ease(double p, double n, double firstNum, double diff) {
        return ((-Math.cos(p * Math.PI) / 2) + 0.5) * diff + firstNum;
      }
    };

    public abstract double ease(double p, double n, double firstNum,
        double diff);
  }

  public enum Speed {

    SLOW(600), FAST(200), DEFAULT(400);

    public int getDuration() {
      return duration;
    }

    private final int duration;

    Speed(int dur) {
      this.duration = dur;
    }
  }

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
      double r = Double.parseDouble(GQuery.curCSS(elem, prop, force));
      return !Double.isNaN(r) && r > -10000 ? r
          : Double.parseDouble(GQuery.curCSS(elem, prop, false));
    }

    public void show() {
      opt.cache.put(prop, elem.getStyle().getProperty(prop));
      opt.show = true;
      custom("width".equals("width") || "height".equals(prop) ? 1 : 0,
          cur(false));
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
        update();
        return true;
      }
    }

    public void update() {
		(jQuery.fx.step[this.prop] || jQuery.fx.step._default)( this );

		// Set display property to block for height/width animations
		if ( ( this.prop == "height" || this.prop == "width" ) && this.elem.style )
			this.elem.style.display = "block";
    }

    public void hide() {
      opt.cache.put(prop, elem.getStyle().getProperty(prop));
      opt.hide = true;
      custom(cur(false), 0);
    }

    private void custom(double from, double to) {
      custom(from, to, "px");
    }

    private void custom(double from, double to, String unit) {
      startTime = Duration.currentTimeMillis();
      start = from;
      end = to;
      now = start;

      Timer t = new Timer() {
        @Override
        public void run() {
          step(false);
        }
      };
    }
  }

  private class SpeedOpts {

    private Properties properties;

    private int duration;

    private Effects.Easing easing;

    private Function complete;

    private boolean queue = true;

    public String display;

    public String overflow;

    public Properties curAnim;

    public boolean hide;

    private GQuery.DataCache cache = DataCache.createObject().cast();

    public boolean show;

    public boolean isQueue() {
      return queue;
    }

    public void setQueue(boolean queue) {
      this.queue = queue;
    }

    protected SpeedOpts(int speed, Easing easing, Function complete) {
      this.complete = complete;
      this.easing = easing;
      this.duration = speed;
    }

    public Function getComplete() {
      return complete;
    }

    public void setComplete(Function complete) {
      this.complete = complete;
    }

    public int getDuration() {
      return duration;
    }

    public void setDuration(int duration) {
      this.duration = duration;
    }

    public Easing getEasing() {
      return easing;
    }

    public void setEasing(Easing easing) {
      this.easing = easing;
    }

    public Properties getProperties() {
      return properties;
    }

    public void setProperties(Properties properties) {
      this.properties = properties;
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
  }

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
    if (properties.get("queue") != null) {
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
                double end = Double.parseDouble(parts.getStr(2));
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
                fx.custom(start, Double.parseDouble(val), "");
              }
            }
          }
        }
      });
    }
    return this;
  }

//			jQuery.each( prop, function(name, val){
//				var e = new jQuery.fx( self, opt, name );
//
//				if ( /toggle|show|hide/.test(val) )
//					e[ val == "toggle" ? hidden ? "show" : "hide" : val ]( prop );
//				else {
//					var parts = val.toString().match(/^([+-]=)?([\d+-.]+)(.*)$/),
//						start = e.cur(true) || 0;
//
//					if ( parts ) {
//						var end = parseFloat(parts[2]),
//							unit = parts[3] || "px";
//
//						// We need to compute starting value
//						if ( unit != "px" ) {
//							self.style[ name ] = (end || 1) + unit;
//							start = ((end || 1) / e.cur(true)) * start;
//							self.style[ name ] = start + unit;
//						}
//
//						// If a +=/-= token was provided, we're doing a relative animation
//						if ( parts[1] )
//							end = ((parts[1] == "-=" ? -1 : 1) * end) + start;
//
//						e.custom( start, end, unit );
//					} else
//						e.custom( start, val, "" );
//				}
//			});
//
//			// For JS strict compliance
//			return true;
//		});
//	},  
//  }

  public Effects fadeIn() {
    Animation a = new Animation() {

      public void onCancel() {
      }

      public void onComplete() {
      }

      public void onStart() {
      }

      public void onUpdate(double progress) {
        for (int i = 0; i < elements.getLength(); i++) {
          elements.getItem(i).getStyle()
              .setProperty("opacity", String.valueOf(progress));
        }
      }
    };
    a.run(1000);
    return this;
  }

  public Effects fadeOut() {
    Animation a = new Animation() {

      public void onCancel() {
      }

      public void onComplete() {
        for (int i = 0; i < elements.getLength(); i++) {
          elements.getItem(i).getStyle().setProperty("opacity", "0");
          elements.getItem(i).getStyle().setProperty("display", "none");
        }
      }

      public void onStart() {
      }

      public void onUpdate(double progress) {
        for (int i = 0; i < elements.getLength(); i++) {
          elements.getItem(i).getStyle()
              .setProperty("opacity", String.valueOf(1.0 - progress));
        }
      }
    };
    a.run(1000);
    return this;
  }

  public Effects hide() {
    for (Element e : elements()) {
      GQuery q = $(e);
      String old = (String) q.data("olddisplay");
      if (old != null && !"none".equals(old)) {
        q.data("olddisplay", GQuery.curCSS(e, "display", false));
        e.getStyle().setProperty("display", "none");
      }
    }
    return this;
  }

  private DataCache elemDisplay = DataCache.createObject().cast();

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
