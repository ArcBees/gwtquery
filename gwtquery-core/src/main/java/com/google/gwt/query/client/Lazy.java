package com.google.gwt.query.client;

/**
 * A tagging interface which triggers the LazyGenerator for type T.
 * LazyGenerator creates an implementation of Type T for the class by forwarding
 * method calls to the class which implements the Lazy interface. Methods in the
 * generated class do not execute but instead queue up a deferred execution of
 * the method.
 */
public interface Lazy<S, T extends LazyBase> {

  /**
   * Create a lazy instance of the current class. Most implementing classes will
   * automate this by simply invoking GWT.create().
   */
  T createLazy();
}
