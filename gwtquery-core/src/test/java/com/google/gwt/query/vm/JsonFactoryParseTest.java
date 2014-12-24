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
  }
  
  public static final String JSON_USER_EXAMPLE = " { " +
                                                 "   'email': 'foo@bar.com', " +
                                                 "   'age': 27, " +
                                                 "   'name': 'Foo Bar' " +
                                                 " }";
      
  @Test public void
  test_parse_json() {
    GUser entity = GQ.create(GUser.class);
    entity.parse(JSON_USER_EXAMPLE);
    System.out.println(entity.toJson());    
   
    Assert.assertEquals(27, entity.getAge());
    Assert.assertEquals("Foo Bar", entity.getName());
    Assert.assertTrue(entity.toJson().contains("email"));
  }
  
  @Test public void
  test_parse_strict_json() {
    GUser entity = GQ.create(GUser.class);
    entity.parseStrict(JSON_USER_EXAMPLE, GUser.class);
    
    Assert.assertEquals(27, entity.getAge());
    Assert.assertEquals("Foo Bar", entity.getName());
    Assert.assertFalse(entity.toJson().contains("email"));
  }
  
}
