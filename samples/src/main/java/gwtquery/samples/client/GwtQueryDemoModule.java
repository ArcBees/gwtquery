package gwtquery.samples.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Node;
import com.google.gwt.user.client.Event;

import static gwtquery.client.GQuery.$;
import static gwtquery.client.Effects.Effects;
import gwtquery.client.*;

/**
 * 
 */
public class GwtQueryDemoModule implements EntryPoint {
    // Compile-time Selectors!
    public interface Slide extends Selectors {
        // find all LI elements in DIV.slide elements
        @Selector("div.slide li")
        NodeList<Element> allSlideBullets();

        // find all LI elements rooted at ctx
        @Selector("li")
        NodeList<Element> slideBulletsCtx(Node ctx);

        // Find all DIV elements with class 'slide'
        @Selector("div.slide")
        NodeList<Element> allSlides();

    }

    public void onModuleLoad() {
        // Ask GWT compiler to generate our interface
        final Slide s = GWT.create(Slide.class);

        // Find all slides, set css to display: none
        // change first slide to display: block
        $(s.allSlides()).css("display", "none")
                .eq(0).css("display", "block");


        // we initially hide all bullets by setting opacity to 0
        $(s.allSlideBullets()).css("opacity", "0");

        // add onclick handler to body element
        $(Document.get().getBody()).click(new Function() {
            // two state variables to note current slide being shown
            // and current bullet
            int curSlide = 0;
            int curBullets = 0;

            // query and store all slides, and bullets of current slide
            GQuery slides = $(s.allSlides());
            GQuery bullets = $(s.slideBulletsCtx(slides.get(curSlide)));

            public boolean f(Event e) {
                // onclick, if not all bullets shown, show a bullet and increment
                if (curBullets < bullets.size()) {
                    bullets.eq(curBullets++).as(Effects).fadeIn();
                } else {
                    // all bullets shown, hide them and current slide
                    bullets.css("opacity","0");
                    slides.eq(curSlide).css("display", "none");
                    // move to next slide, checking for wrap around
                    curSlide++;
                    if(curSlide == slides.size()) {
                        curSlide = 0;
                    }
                    curBullets = 0;
                    // query for new set of bullets, and show next slide
                    // by changing opacity to 1 and display to block
                    bullets = $(s.slideBulletsCtx(slides.get(curSlide)));
                    slides.eq(curSlide).css("display", "block").as(Effects).fadeIn();
                }
                return true;
            }
        });

    }
}
