package com.google.gwt.query.client.builders;

import com.google.gwt.query.client.Binder;

public interface JsonFactory {
  <T extends JsonBuilder> T create(Class<T> clz);
  <T extends Binder> T create(String s);
}
