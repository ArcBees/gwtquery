package gquery.client;

import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.LOCAL_VARIABLE;

/**
 * Used to pass a CSS Selector to a generator at compile time
 */
@Target({METHOD})
@Retention(RetentionPolicy.SOURCE)
public @interface Selector {
   String value();
}
