/*
 * Copyright 2011, The gwtquery team.
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
package gwtquery.samples.client;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.query.client.Selector;
import com.google.gwt.query.client.Selectors;

/**
 * Sample selectors from SlickSpeed benchmark.
 */
public interface MySelectors extends Selectors {

  @Selector("h1[id]:contains(Selectors)")
  NodeList<Element> h1IdContainsSelectors(Node n);

  @Selector("a[href][lang][class]")
  NodeList<Element> aHrefLangClass(Node n);

//  @Selector("*:checked")
//  NodeList<Element> allChecked(Node n);

  @Selector("body")
  NodeList<Element> body(Node n);

  @Selector("body div")
  NodeList<Element> bodyDiv(Node n);

  @Selector("div .example")
  NodeList<Element> divExample(Node n);

  @Selector("div > div")
  NodeList<Element> divGtP(Node n);

//    @Selector("*:first")
//    NodeList<Element> allFirst(Node n);

  @Selector("div:not(.example)")
  NodeList<Element> divNotExample(Node n);

  @Selector("div p")
  NodeList<Element> divP(Node n);

  @Selector("div p a")
  NodeList<Element> divPA(Node n);

  @Selector("div + p")
  NodeList<Element> divPlusP(Node n);

  @Selector("div[class^=exa][class$=mple]")
  NodeList<Element> divPrefixExaSuffixMple(Node n);

  @Selector("div #title")
  NodeList<Element> divSpaceTitle(Node n);

  @Selector("div ~ p")
  NodeList<Element> divTildeP(Node n);

  @Selector("div[class]")
  NodeList<Element> divWithClass(Node n);

  @Selector("div[class~=dialog]")
  NodeList<Element> divWithClassContainsDialog(Node n);

  @Selector("div[class*=e]")
  NodeList<Element> divWithClassContainsE(Node n);

  @Selector("div[class=example]")
  NodeList<Element> divWithClassExample(Node n);

  @Selector("div[class!=madeup]")
  NodeList<Element> divWithClassNotContainsMadeup(Node n);

  @Selector("div[class~=dialog]")
  NodeList<Element> divWithClassListContainsDialog(Node n);

  @Selector("div[class^=exa]")
  NodeList<Element> divWithClassPrefixExa(Node n);

  @Selector("div[class$=mple]")
  NodeList<Element> divWithClassSuffixMple(Node n);

  @Selector("p:first-child")
  NodeList<Element> firstChild(Node n);

  @Selector("h1#title")
  NodeList<Element> h1Title(Node n);

  @Selector("h1#title + div > p")
  NodeList<Element> h1TitlePlusDivGtP(Node n);

  @Selector("p:last-child")
  NodeList<Element> lastChild(Node n);

  @Selector("div, p a")
  NodeList<Element> divCommaPA(Node n);

  @Selector(".note")
  NodeList<Element> note(Node n);

  @Selector("p:nth-child(n)")
  NodeList<Element> nthChild(Node n);

  @Selector("p:nth-child(2n)")
  NodeList<Element> nThChild2n(Node n);

  @Selector("p:nth-child(2n+1)")
  NodeList<Element> nThChild2nPlus1(Node n);

//  @Selector("p:contains(selectors)")
//  NodeList<Element> pContainsSelectors(Node n);

  @Selector("p:nth-child(even)")
  NodeList<Element> nThChildEven(Node n);

  @Selector("p:nth-child(odd)")
  NodeList<Element> nThChildOdd(Node n);

  @Selector("p:only-child")
  NodeList<Element> onlyChild(Node n);

  @Selector("#title")
  NodeList<Element> title(Node n);

  @Selector("#title,h1#title")
  NodeList<Element> titleAndh1Title(Node n);

  @Selector("ul .tocline2")
  NodeList<Element> ulTocline2(Node n);

  @Selector("ui.toc li.tocline2")
  NodeList<Element> ulTocLiTocLine2(Node n);
}
