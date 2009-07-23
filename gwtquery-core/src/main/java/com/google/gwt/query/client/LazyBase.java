package com.google.gwt.query.client;

/**
 * All lazy interfaces must extend this baseclass. This ensures the done()
 * method exists and returns an executable function.
 */
public interface LazyBase<S> {

  Function done();
}