/*
 * Copyright 2013, The gwtquery team.
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


import static com.google.gwt.query.client.GQuery.$;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.jsonp.client.TimeoutException;
import com.google.gwt.junit.DoNotRunWith;
import com.google.gwt.junit.Platform;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.query.client.builders.Name;
import com.google.gwt.query.client.builders.XmlBuilder;
import com.google.gwt.query.client.plugins.ajax.Ajax;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Test class for testing ajax stuff.
 */
public class GQueryAjaxTestGwt extends GWTTestCase {

  static Element e = null;

  static HTML testPanel = null;

  public String getModuleName() {
    return "com.google.gwt.query.QueryTest";
  }

  public void gwtTearDown() {
    $(e).remove();
    e = null;
  }

  public void gwtSetUp() {
    if (e == null) {
      testPanel = new HTML();
      RootPanel.get().add(testPanel);
      e = testPanel.getElement();
      e.setId("core-tst");
    } else {
      e.setInnerHTML("");
    }
  }

  interface XmlExample extends XmlBuilder {
    interface T extends XmlBuilder {
    }
    enum E {
      FOO, BAR
    }
    String getA();
    Boolean getB();
    @Name("c")
    int getNumber();

    XmlExample[] getX();
    @Name("x")
    XmlExample getFirstX();

    XmlExample setA(String s);
    @Name("c")
    XmlExample setNumber(int i);

    T getEnum();
    T getBool();
    T getNum();
  }

  public void testXmlBuilder() {
    String xml = "<a a='ra' b='true' c='-1.48'><x a='xa1'>  text</x><x a='xa2'/><enum>FOO</enum><bool>true</bool><num>333</num></a>";
    XmlExample x = GWT.create(XmlExample.class);
    x.parse(xml);
    assertTrue(x.getB());
    assertEquals("ra", x.getA());
    assertEquals(-1, x.getNumber());
    assertEquals("xa2", x.getX()[1].getA());
    assertEquals("xa1", x.getFirstX().getA());
    x.setA("X").setNumber(1234);
    assertEquals("X", x.getA());
    assertEquals(1234, x.getNumber());
    assertEquals("  text", x.getFirstX().getText());
    x.getX()[0].setText("pepe");
    assertEquals("pepe", x.getFirstX().getText());

    assertEquals(XmlExample.E.FOO, x.getEnum().getTextAsEnum(XmlExample.E.class));
    assertEquals(true, x.getBool().getTextAsBoolean());
    assertEquals(333d, x.getNum().getTextAsNumber());
  }

  interface Feed extends XmlBuilder {
    interface Tag extends XmlBuilder {
    }
    Tag getTitle();
    Tag getTagline();
    Tag getFullcount();
    Tag getModified();

    interface Link extends XmlBuilder {
      String getHref();
      String getType();
    }
    Link getLink();

    interface Entry extends XmlBuilder {
      interface Author extends XmlBuilder {
        Tag getEmail();
        Tag getName();
      }
      Tag getTitle();
      Tag getSummary();
      Link getLink();
      Tag getModified();
      Tag getIssued();
      Tag getId();
      Author getAuthor();
    }
    Entry[] getEntry();
  }

  // FIXME: gquery xml does not work well with htmlUnit, FF & Safari works
  // TODO: test in IE
  @DoNotRunWith({Platform.HtmlUnitLayout})
  @SuppressWarnings("deprecation")
  public void testXmlGmailExample() {
    String xml = "<?xml version='1.0' encoding='UTF-8'?>" +
        "<feed version='0.3' xmlns='http://purl.org/atom/ns#'>"
      + " <title>Gmail - Inbox for manolo@...</title>"
      + " <tagline>New messages in your Gmail Inbox</tagline>"
      + " <fullcount>1</fullcount>"
      + " <link rel='alternate' href='http://mail.google.com/mail' type='text/html' />"
      + " <modified>2012-11-07T10:32:52Z</modified>"
      + " <entry>"
      + "  <title>Trending Startups and Updates</title>"
      + "  <summary>AngelList Weekly Trending Startups Storenvy Tumblr for stores E-Commerce Platforms · San Francisco</summary>"
      + "  <link rel='alternate' href='http://mail.google.com/mail?account_id=manolo@....&amp;message_id=13ad2e227da1488b&amp;view=conv&amp;extsrc=atom' type='text/html' />"
      + "  <modified>2012-11-05T23:22:47Z</modified>"
      + "  <issued>2012-11-05T23:22:47Z</issued>"
      + "  <id>tag:gmail.google.com,2004:1417840183363061889</id>"
      + "  <author>"
      + "   <name>AName</name>"
      + "   <email>AnEmail</email>"
      + "  </author>"
      + " </entry>"
      + "</feed>";

    Feed f = GWT.create(Feed.class);
    f.parse(xml);
    assertEquals((int)f.getFullcount().getTextAsNumber(), f.getEntry().length);
    assertEquals(112, f.getModified().getTextAsDate().getYear());
    assertEquals("AName", f.getEntry()[0].getAuthor().getName().getText());
  }

  @DoNotRunWith({Platform.HtmlUnitLayout})
  public void testJsonNonCallbackResponse() {
    delayTestFinish(5000);
    String testJsonpUrl = "http://www.google.com";
    Ajax.getJSONP(testJsonpUrl, null, new Function(){
      public void f() {
        Object response = arguments(0);
        if (response != null) {
          assertTrue("Unknown response: " + response, response instanceof TimeoutException);
        }
        finishTest();
      }
    }, 500);
  }
}
