package com.google.gwt.query.client.builders;

import com.google.gwt.query.client.IsProperties;

public interface JsonFactory {
  <T extends JsonBuilder> T create(Class<T> clz);
  IsProperties create(String s);
  IsProperties create();
}
