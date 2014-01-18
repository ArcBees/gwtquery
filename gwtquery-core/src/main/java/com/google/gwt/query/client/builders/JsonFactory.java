package com.google.gwt.query.client.builders;

import com.google.gwt.query.client.Binder;

public interface JsonFactory {
  <T extends JsonBuilder> T create(Class<T> clz);
  Binder create(String s);
  Binder create();
}
