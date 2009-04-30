package com.google.gwt.query.client;

/**
 * A GQuery plugin. All GQuery plugins must implement this interface.
 */
public interface Plugin<T extends GQuery> {

  /**
   * Called by the GQuery.as() method in order to pass the current matched
   * set. Typically a plugin will want to call a super class copy constructor
   * in order to copy the internal matched set of elements.
   * @param gQuery
   * @return
   */
     T init(GQuery gQuery);
}
