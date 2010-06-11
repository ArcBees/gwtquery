package gwtquery.samples.client;

import static com.google.gwt.query.client.GQuery.$;
import static com.google.gwt.query.client.plugins.Effects.Effects;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.Selector;
import com.google.gwt.query.client.Selectors;
import com.google.gwt.query.client.plugins.Effects.Speed;
import com.google.gwt.user.client.Event;

/**
 *
 */
public class GwtQueryDemoModule implements EntryPoint {

  // Compile-time Selectors!
  public interface Slide extends Selectors {

    // find all LI elements in DIV.slide elements
    @Selector("div.slide li")
    NodeList<Element> allSlideBullets();

    // Find all DIV elements with class 'slide'
    @Selector("div.slide")
    NodeList<Element> allSlides();

    // find all LI elements rooted at ctx
    @Selector("li")
    NodeList<Element> slideBulletsCtx(Node ctx);
  }

  public void onModuleLoad() {
    // Ask GWT compiler to generate our interface
    final Slide s = GWT.create(Slide.class);
    final GQuery slides = $(s.allSlides());

    // we initially hide all slides and bullets
    slides.hide().eq(0).as(Effects).clipAppear();
    $(s.allSlideBullets()).hide();


    // add onclick handler to body element
    $(slides).click(new Function() {
      // two state variables to note current slide being shown
      // and current bullet
      int curSlide = 0;
      int curBullets = 0;

      // query and store all bullets of current slide
      GQuery bullets = $(s.slideBulletsCtx(slides.get(curSlide)));
      
      public boolean f(Event e) {
        // onclick, if not all bullets shown, show a bullet and increment
        if (curBullets < bullets.size()) {
          bullets.eq(curBullets++).as(Effects).fadeIn(Speed.SLOW);
        } else {
          // all bullets shown, hide them and current slide
          bullets.hide();
          
          // move to next slide, checking for wrap around
          int lastSlide = curSlide++;
          if (curSlide == slides.size()) {
            curSlide = 0;
          }
          
          // query for new set of bullets, and show next slide
          curBullets = 0;
          bullets = $(s.slideBulletsCtx(slides.get(curSlide)));
          
          // Hide the last slide and show the next when the effects finishes
          slides.eq(lastSlide).as(Effects).fadeOut(new Function() {
            public void f(Element e) {
              slides.eq(curSlide).as(Effects).clipAppear();
            }
          });
        }
        return true;
      }
    });
  }
}
