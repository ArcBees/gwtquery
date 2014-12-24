package com.google.gwt.query.vm;

import com.google.gwt.query.client.GQ;
import com.google.gwt.query.client.builders.JsonBuilder;

import org.junit.Assert;
import org.junit.Test;

public class JsonFactoryParseTest {

  public interface GUser extends JsonBuilder{
    int getAge();
    void setAge(int age);

    String getName();
    void setName(String name);
    
    GUser address(String address);
    String address();
  }
  
  public static final String JSON_USER_EXAMPLE = " { " +
                                                 "   'email': 'foo@bar.com', " +
                                                 "   'age': 27, " +
                                                 "   'name': 'Foo Bar', " +
                                                 "   'address': 'Street Foo N6' " +
                                                 " }";
      
  @Test public void
  test_parse_json() {
    GUser entity = GQ.create(GUser.class);
    entity.parse(JSON_USER_EXAMPLE);
      
    Assert.assertEquals(27, entity.getAge());
    Assert.assertEquals("Foo Bar", entity.getName());
    Assert.assertEquals("Street Foo N6", entity.address());
    Assert.assertTrue(entity.toJson().contains("email"));
  }
  
  @Test public void
  test_parse_strict_json() {
    GUser entity = GQ.create(GUser.class);
    entity.parse(JSON_USER_EXAMPLE);
    entity.strip();
    System.out.println(entity.toJson());
    Assert.assertEquals(27, entity.getAge());
    Assert.assertEquals("Foo Bar", entity.getName());
    Assert.assertEquals("Street Foo N6", entity.address());
    Assert.assertFalse(entity.toJson().contains("email"));
  }
  
}
