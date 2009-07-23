package com.google.gwt.query.client;

/**
 *
 */
public interface LazyEffects extends LazyGQuery {

  LazyEffects animate(Properties properties, Effects.Speed speed,
      Effects.Easing easing, Function complete);

  LazyEffects animate(Properties properties, int speed, Effects.Easing easing,
      Function complete);

  LazyEffects fadeIn();

  LazyEffects fadeIn(Effects.Speed speed);

  LazyEffects fadeIn(int speed);

  LazyEffects fadeIn(Effects.Speed speed, Function callback);

  LazyEffects fadeIn(int speed, Function callback);

  LazyEffects fadeOut();

  LazyEffects fadeOut(Effects.Speed speed);

  LazyEffects fadeOut(int speed);

  LazyEffects fadeOut(Effects.Speed speed, Function callback);

  LazyEffects fadeOut(int speed, Function callback);

  LazyEffects fadeTo(Effects.Speed speed, double opacity);

  LazyEffects fadeTo(Effects.Speed speed, double opacity, Function callback);

  LazyEffects hide();

  LazyEffects show();

  LazyEffects slideDown();

  LazyEffects slideDown(Effects.Speed speed);

  LazyEffects slideDown(Effects.Speed speed, Function callback);

  LazyEffects slideToggle();

  LazyEffects slideToggle(Effects.Speed speed);

  LazyEffects slideToggle(Effects.Speed speed, Function callback);

  LazyEffects slideUp();

  LazyEffects slideUp(Effects.Speed speed);

  LazyEffects slideUp(Effects.Speed speed, Function callback);

  LazyEffects toggle();
}
