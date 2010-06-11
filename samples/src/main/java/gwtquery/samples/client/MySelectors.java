package gwtquery.samples.client;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.query.client.Selector;
import com.google.gwt.query.client.Selectors;

/**
 * Sample selectors from SlickSpeed benchmark.
 */
public interface MySelectors extends Selectors {

  @Selector("h1[id]:contains(Selectors)")
  NodeList<Element> h1IdContainsSelectors();

  @Selector("a[href][lang][class]")
  NodeList<Element> aHrefLangClass();

  @Selector("*:checked")
  NodeList<Element> allChecked();

  @Selector("body")
  NodeList<Element> body();

  @Selector("body div")
  NodeList<Element> bodyDiv();

  @Selector("div .example")
  NodeList<Element> divExample();

  @Selector("div > div")
  NodeList<Element> divGtP();

//    @Selector("*:first")
//    NodeList<Element> allFirst();

  @Selector("div:not(.example)")
  NodeList<Element> divNotExample();

  @Selector("div p")
  NodeList<Element> divP();

  @Selector("div p a")
  NodeList<Element> divPA();

  @Selector("div + p")
  NodeList<Element> divPlusP();

  @Selector("div[class^=exa][class$=mple]")
  NodeList<Element> divPrefixExaSuffixMple();

  @Selector("div #title")
  NodeList<Element> divSpaceTitle();

  @Selector("div ~ p")
  NodeList<Element> divTildeP();

  @Selector("div[class]")
  NodeList<Element> divWithClass();

  @Selector("div[class~=dialog]")
  NodeList<Element> divWithClassContainsDialog();

  @Selector("div[class*=e]")
  NodeList<Element> divWithClassContainsE();

  @Selector("div[class=example]")
  NodeList<Element> divWithClassExample();

  @Selector("div[class!=madeup]")
  NodeList<Element> divWithClassNotContainsMadeup();

  @Selector("div[class~=dialog]")
  NodeList<Element> divWithClassListContainsDialog();

  @Selector("div[class^=exa]")
  NodeList<Element> divWithClassPrefixExa();

  @Selector("div[class$=mple]")
  NodeList<Element> divWithClassSuffixMple();

  @Selector("p:first-child")
  NodeList<Element> firstChild();

  @Selector("h1#title")
  NodeList<Element> h1Title();

  @Selector("h1#title + div > p")
  NodeList<Element> h1TitlePlusDivGtP();

  @Selector("p:last-child")
  NodeList<Element> lastChild();

  @Selector("div, p a")
  NodeList<Element> divCommaPA();

  @Selector(".note")
  NodeList<Element> note();

  @Selector("p:nth-child(n)")
  NodeList<Element> nthChild();

  @Selector("p:nth-child(2n)")
  NodeList<Element> nThChild2n();

  @Selector("p:nth-child(2n+1)")
  NodeList<Element> nThChild2nPlus1();

  @Selector("p:contains(selectors)")
  NodeList<Element> pContainsSelectors();

  @Selector("p:nth-child(even)")
  NodeList<Element> nThChildEven();

  @Selector("p:nth-child(odd)")
  NodeList<Element> nThChildOdd();

  @Selector("p:only-child")
  NodeList<Element> onlyChild();

  @Selector("#title")
  NodeList<Element> title();

  @Selector("#title,h1#title")
  NodeList<Element> titleAndh1Title();

  @Selector("ul .tocline2")
  NodeList<Element> ulTocline2();

  @Selector("ui.toc li.tocline2")
  NodeList<Element> ulTocLiTocLine2();
}
