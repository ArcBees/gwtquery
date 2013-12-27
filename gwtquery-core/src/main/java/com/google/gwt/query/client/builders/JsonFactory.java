package com.google.gwt.query.client.builders;

public interface JsonFactory {
  <T extends JsonBuilder> T create(Class<T> clz);
}
