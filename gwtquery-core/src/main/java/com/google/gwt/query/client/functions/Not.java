package com.google.gwt.query.client.functions;

/**
 * Implements the negation of a predicate.
 *
 * @param <T>
 *            The type of the single input parameter.
 */
public class Not<T> implements Func1<T, Boolean> {
  private final Func1<? super T, Boolean> predicate;

  /**
   * Constructs a predicate that returns true for each input that the source
   * predicate returns false for and vice versa.
   *
   * @param predicate
   *            The source predicate to negate.
   */
  public Not(Func1<? super T, Boolean> predicate) {
    this.predicate = predicate;
  }

  @Override
  public Boolean call(T param) {
    return !predicate.call(param);
  }
}
