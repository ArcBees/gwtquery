package com.google.gwt.query.client;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import static java.lang.annotation.ElementType.METHOD;

/**
 * Used to pass a CSS Selector to a generator at compile time
 */
@Target({METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Selector {
   String value();
}
