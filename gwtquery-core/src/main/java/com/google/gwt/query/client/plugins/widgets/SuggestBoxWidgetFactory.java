package com.google.gwt.query.client.plugins.widgets;

import static com.google.gwt.query.client.GQuery.$;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;

/**
 * Factory used to create a {@link SuggestBox} widget.
 * 
 */
public class SuggestBoxWidgetFactory implements WidgetFactory<SuggestBox> {

  /**
   * Options used to create a {@link SuggestBox}
   * 
   */
  public static class SuggestBoxOptions implements WidgetOptions {

    private String suggestionsSelector;
    private SuggestOracle suggestOracle;

    public SuggestBoxOptions() {
      initDefault();
    }

    public SuggestBoxOptions(String suggestionsSelector) {
      this.suggestionsSelector = suggestionsSelector;
    }

    public SuggestBoxOptions(SuggestOracle suggestOracle) {
      this.suggestOracle = suggestOracle;
    }

    public SuggestOracle getSuggestOracle() {
      return suggestOracle;
    }

    public String getSuggestionsSelector() {
      return suggestionsSelector;
    }

    /**
     * Set the css selector to use for selecting elements playing roles of the
     * list items. The value of the items will be the inner texts of the
     * selected elements
     * 
     * If this options is null, the inner text of the direct children of the
     * element will be used as suggestions list.
     * 
     * This options is used if the element is not a <i>select</i> element.
     * 
     * @param optionsSelector
     */
    public void setSuggestionsSelector(String suggestionsSelector) {
      this.suggestionsSelector = suggestionsSelector;
    }

    public void setSuggestOracle(SuggestOracle suggestOracle) {
      this.suggestOracle = suggestOracle;
    }

    private void initDefault() {
      suggestionsSelector = null;
      suggestOracle = null;
    }
  }

  private SuggestBoxOptions options;

  public SuggestBoxWidgetFactory(SuggestBoxOptions options) {
    this.options = options;
  }

  public SuggestBox create(Element e) {

    SuggestOracle suggestOracle = createOracle(e);
 
    if ($(e).filter("input[type='text']").length() > 0) {
      return SuggestBox.wrap(suggestOracle, e);
    }

    InputElement inputElement = Document.get().createTextInputElement();

    WidgetsUtils.replaceOrAppend(e, inputElement);

    return SuggestBox.wrap(suggestOracle, inputElement);

  }

  private SuggestOracle createOracle(Element e) {
    if (options.getSuggestOracle() != null) {
      return options.getSuggestOracle();
    }

    MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();

    if (options.getSuggestionsSelector() != null) {
      GQuery suggestions = $(options.getSuggestionsSelector(), e);
      for (Element suggestion : suggestions.elements()) {
        oracle.add(suggestion.getInnerText());
      }
    }

    return oracle;

  }

}