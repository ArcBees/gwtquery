package gwtquery.client;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.animation.client.Animation;

public class Effects extends GQuery {

  static {
    GQuery.registerPlugin(Effects.class, new EffectsPlugin());
  }

  public static final Class<Effects> Effects = Effects.class;

  public Effects(Element element) {
    super(element);
  }

  public Effects(JSArray elements) {
    super(elements);
  }

  public Effects(NodeList list) {
    super(list);
  }

  /**
   * function( prop, speed, easing, callback ) {
		var optall = jQuery.speed(speed, easing, callback);

		return this[ optall.queue === false ? "each" : "queue" ](function(){
		
			var opt = jQuery.extend({}, optall), p,
				hidden = this.nodeType == 1 && jQuery(this).is(":hidden"),
				self = this;
	
			for ( p in prop ) {
				if ( prop[p] == "hide" && hidden || prop[p] == "show" && !hidden )
					return opt.complete.call(this);

				if ( ( p == "height" || p == "width" ) && this.style ) {
					// Store display property
					opt.display = jQuery.css(this, "display");

					// Make sure that nothing sneaks out
					opt.overflow = this.style.overflow;
				}
			}

			if ( opt.overflow != null )
				this.style.overflow = "hidden";

			opt.curAnim = jQuery.extend({}, prop);

			jQuery.each( prop, function(name, val){
				var e = new jQuery.fx( self, opt, name );

				if ( /toggle|show|hide/.test(val) )
					e[ val == "toggle" ? hidden ? "show" : "hide" : val ]( prop );
				else {
					var parts = val.toString().match(/^([+-]=)?([\d+-.]+)(.*)$/),
						start = e.cur(true) || 0;

					if ( parts ) {
						var end = parseFloat(parts[2]),
							unit = parts[3] || "px";

						// We need to compute starting value
						if ( unit != "px" ) {
							self.style[ name ] = (end || 1) + unit;
							start = ((end || 1) / e.cur(true)) * start;
							self.style[ name ] = start + unit;
						}

						// If a +=/-= token was provided, we're doing a relative animation
						if ( parts[1] )
							end = ((parts[1] == "-=" ? -1 : 1) * end) + start;

						e.custom( start, end, unit );
					} else
						e.custom( start, val, "" );
				}
			});

			// For JS strict compliance
			return true;
		});
	},
   * @return
   */
  public Effects animate(Properties props, String speed, String easing,
      Function callback) {
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

  public static class EffectsPlugin implements Plugin<Effects> {

    public Effects init(GQuery gq) {
      return new Effects(gq.get());
    }
  }
}
