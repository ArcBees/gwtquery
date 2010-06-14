/*
 * Copyright 2010 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.gwt.query.client;

import java.util.Hashtable;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.Name;
import javax.naming.NameClassPair;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;

import com.google.gwt.junit.client.GWTTestCase;

/**
 * Test class for testing gwtquery-core api in JRE.
 */
public class JreQueryCoreTest extends GWTTestCase {

  public String getModuleName() {
    return null;
  }

  public void testAssertHtmlEquals() {
    GQueryCoreTest.assertHtmlEquals("<span>hello</span>",
        "<span $h=\"5\">hello</span>");
    GQueryCoreTest.assertHtmlEquals("<p class=\"abc\">whatever</p> ",
        " <p class=abc added=\"null\">Whatever</p>");
  }

  public void testWrapPropertiesString() {
    assertEquals("({border:'1px solid black'})", Properties
        .wrapPropertiesString("border:'1px solid black'"));
    assertEquals("({border:'1px solid black'})", Properties
        .wrapPropertiesString("(border:'1px solid black')"));
    assertEquals("({border:'1px solid black'})", Properties
        .wrapPropertiesString("{border:'1px solid black'}"));
    assertEquals("({border:'1px solid black'})", Properties
        .wrapPropertiesString("{border:'1px solid black'}"));
    assertEquals("({border:'1px solid black'})", Properties
        .wrapPropertiesString("(border:'1px solid black')"));
    assertEquals("({border:'1px solid black'})", Properties
        .wrapPropertiesString("{(border:'1px solid black')}"));
    assertEquals("({border:'1px solid black'})", Properties
        .wrapPropertiesString("({border:'1px solid black'})"));
  }
  public void test() throws NamingException {
    System.out.println( MockInitialContextFactory.class.getName());
    System.setProperty("java.naming.factory.initial", MockInitialContextFactory.class.getName());
    Context mockCtx = new MockContext();
//    UserTransaction mockTrans = createMock(UserTransaction.class);
//    expect(mockCtx.lookup("UserTransaction")).andReturn(mockTrans);
//    replay(mockCtx);
    MockInitialContextFactory.setMockContext(mockCtx);
    
    Context ctx = new InitialContext();
    ctx.lookup("whatever");
    
  }
  public static class MockInitialContextFactory implements
      InitialContextFactory {
    private static Context mockCtx = null;

    public static void setMockContext(Context ctx) {
      mockCtx = ctx;
    }

    public Context getInitialContext(java.util.Hashtable<?, ?> environment)
        throws NamingException {
      if (mockCtx == null) {
        throw new IllegalStateException("mock context was not set.");
      }
      return mockCtx;
    }
  }
  public static class MockContext implements Context {

    public Object addToEnvironment(String propName, Object propVal)
        throws NamingException {
      // TODO Auto-generated method stub
      return null;
    }

    public void bind(Name name, Object obj) throws NamingException {
      // TODO Auto-generated method stub
      
    }

    public void bind(String name, Object obj) throws NamingException {
      // TODO Auto-generated method stub
      
    }

    public void close() throws NamingException {
      // TODO Auto-generated method stub
      
    }

    public Name composeName(Name name, Name prefix) throws NamingException {
      // TODO Auto-generated method stub
      return null;
    }

    public String composeName(String name, String prefix)
        throws NamingException {
      // TODO Auto-generated method stub
      return null;
    }

    public Context createSubcontext(Name name) throws NamingException {
      // TODO Auto-generated method stub
      return null;
    }

    public Context createSubcontext(String name) throws NamingException {
      // TODO Auto-generated method stub
      return null;
    }

    public void destroySubcontext(Name name) throws NamingException {
      // TODO Auto-generated method stub
      
    }

    public void destroySubcontext(String name) throws NamingException {
      // TODO Auto-generated method stub
      
    }

    public Hashtable<?, ?> getEnvironment() throws NamingException {
      // TODO Auto-generated method stub
      return null;
    }

    public String getNameInNamespace() throws NamingException {
      // TODO Auto-generated method stub
      return null;
    }

    public NameParser getNameParser(Name name) throws NamingException {
      // TODO Auto-generated method stub
      return null;
    }

    public NameParser getNameParser(String name) throws NamingException {
      // TODO Auto-generated method stub
      return null;
    }

    public NamingEnumeration<NameClassPair> list(Name name)
        throws NamingException {
      // TODO Auto-generated method stub
      return null;
    }

    public NamingEnumeration<NameClassPair> list(String name)
        throws NamingException {
      // TODO Auto-generated method stub
      return null;
    }

    public NamingEnumeration<Binding> listBindings(Name name)
        throws NamingException {
      // TODO Auto-generated method stub
      return null;
    }

    public NamingEnumeration<Binding> listBindings(String name)
        throws NamingException {
      // TODO Auto-generated method stub
      return null;
    }

    public Object lookup(Name name) throws NamingException {
      // TODO Auto-generated method stub
      return null;
    }

    public Object lookup(String name) throws NamingException {
      System.out.println("lookup " + name);
      return null;
    }

    public Object lookupLink(Name name) throws NamingException {
      // TODO Auto-generated method stub
      return null;
    }

    public Object lookupLink(String name) throws NamingException {
      // TODO Auto-generated method stub
      return null;
    }

    public void rebind(Name name, Object obj) throws NamingException {
      // TODO Auto-generated method stub
      
    }

    public void rebind(String name, Object obj) throws NamingException {
      // TODO Auto-generated method stub
      
    }

    public Object removeFromEnvironment(String propName) throws NamingException {
      // TODO Auto-generated method stub
      return null;
    }

    public void rename(Name oldName, Name newName) throws NamingException {
      // TODO Auto-generated method stub
      
    }

    public void rename(String oldName, String newName) throws NamingException {
      // TODO Auto-generated method stub
      
    }

    public void unbind(Name name) throws NamingException {
      // TODO Auto-generated method stub
      
    }

    public void unbind(String name) throws NamingException {
      // TODO Auto-generated method stub
      
    }
    
  }



}
