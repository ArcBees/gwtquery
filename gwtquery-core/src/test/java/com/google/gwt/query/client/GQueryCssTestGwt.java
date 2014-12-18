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
package com.google.gwt.query.client;

import static com.google.gwt.query.client.GQuery.$;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.FontStyle;
import com.google.gwt.dom.client.Style.ListStyleType;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.TextDecoration;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.junit.DoNotRunWith;
import com.google.gwt.junit.Platform;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.query.client.css.BackgroundAttachmentProperty.BackgroundAttachment;
import com.google.gwt.query.client.css.BackgroundPositionProperty.BackgroundPosition;
import com.google.gwt.query.client.css.BackgroundRepeatProperty.BackgroundRepeat;
import com.google.gwt.query.client.css.BorderCollapseProperty.BorderCollapse;
import com.google.gwt.query.client.css.BorderSpacingProperty.BorderSpacing;
import com.google.gwt.query.client.css.BorderStyleProperty.BorderStyle;
import com.google.gwt.query.client.css.BorderWidthProperty.BorderWidth;
import com.google.gwt.query.client.css.CSS;
import com.google.gwt.query.client.css.CaptionSideProperty.CaptionSide;
import com.google.gwt.query.client.css.ClearProperty.Clear;
import com.google.gwt.query.client.css.ClipProperty.Shape;
import com.google.gwt.query.client.css.EmptyCellsProperty;
import com.google.gwt.query.client.css.FontSizeProperty.FontSize;
import com.google.gwt.query.client.css.FontVariantProperty.FontVariant;
import com.google.gwt.query.client.css.Length;
import com.google.gwt.query.client.css.ListStylePositionProperty.ListStylePosition;
import com.google.gwt.query.client.css.RGBColor;
import com.google.gwt.query.client.css.TextAlignProperty.TextAlign;
import com.google.gwt.query.client.css.TextTransformProperty.TextTransform;
import com.google.gwt.query.client.css.UnicodeBidiProperty.UnicodeBidi;
import com.google.gwt.query.client.css.UriValue;
import com.google.gwt.dom.client.Style.Float;

import com.google.gwt.query.client.css.WhiteSpaceProperty.WhiteSpace;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Test class for testing css part.
 */
public class GQueryCssTestGwt extends GWTTestCase {

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

  public void testBackgroundAttachmentProperty() {

    $(e).html("<div id='test'>Content</div>");

    $("#test").css(
        CSS.BACKGROUND_ATTACHMENT.with(BackgroundAttachment.FIXED));

    assertEquals("fixed", $("#test").css("backgroundAttachment"));
    assertEquals("fixed", $("#test").css(CSS.BACKGROUND_ATTACHMENT));

    $("#test").css(
        CSS.BACKGROUND_ATTACHMENT.with(BackgroundAttachment.SCROLL));

    assertEquals("scroll", $("#test").css("backgroundAttachment"));
    assertEquals("scroll", $("#test").css(CSS.BACKGROUND_ATTACHMENT));

  }

  @DoNotRunWith({Platform.Prod})
  public void testBackgroundColorProperty() {

    $(e).html("<div id='test'>Content</div>");

    $("#test").css(CSS.BACKGROUND_COLOR.with(RGBColor.AQUA));

    assertEquals("aqua", $("#test").css("backgroundColor", false));
    assertEquals("aqua", $("#test").css(CSS.BACKGROUND_COLOR, false));

    $("#test").css(CSS.BACKGROUND_COLOR.with(RGBColor.rgb("#112233")));

    assertTrue($("#test").css("backgroundColor", false).matches("#112233|rgb\\(17,34,51\\)"));
    assertTrue($("#test").css(CSS.BACKGROUND_COLOR, false).matches("#112233|rgb\\(17,34,51\\)"));

    $("#test").css(CSS.BACKGROUND_COLOR.with(RGBColor.rgb(35, 45, 55)));
    assertEquals("rgb(35,45,55)", $("#test").css("backgroundColor", false));
    assertEquals("rgb(35,45,55)", $("#test").css(CSS.BACKGROUND_COLOR, false));

  }

  public void testBackgroundImageProperty() {

    $(e).html("<div id='test'>Content</div>");

    $("#test").css(CSS.BACKGROUND_IMAGE.with(UriValue.url("image.jpg")));

    assertTrue($("#test").css("backgroundImage").contains("image.jpg"));
    assertTrue($("#test").css(CSS.BACKGROUND_IMAGE).contains("image.jpg"));
  }

  void assertMatches(String regex, String test) {
    boolean b = test.matches("^(" + regex + ")$");
    assertTrue("assertMatches error, expected:" + regex + ", actual:" + test, b);
  }

  public void testBackgroundPositionProperty() {

    $(e).html("<div id='test'>Content</div>");

    $("#test").css(CSS.BACKGROUND_POSITION.with(BackgroundPosition.CENTER));
    assertMatches("center|center center|50% 50%", $("#test").css("backgroundPosition", false));
    assertMatches("center|center center|50% 50%", $("#test").css(CSS.BACKGROUND_POSITION, false));

    $("#test").css(
        CSS.BACKGROUND_POSITION.with(BackgroundPosition.CENTER_CENTER));
    assertMatches("center|center center|50% 50%", $("#test").css(CSS.BACKGROUND_POSITION, false));

    $("#test").css(
        CSS.BACKGROUND_POSITION.with(BackgroundPosition.CENTER_TOP));
    assertMatches("center top|50% 0%", $("#test").css(CSS.BACKGROUND_POSITION, false));

    $("#test").css(
        CSS.BACKGROUND_POSITION.with(BackgroundPosition.CENTER_BOTTOM));
    assertMatches("center bottom|50% 100%", $("#test").css(CSS.BACKGROUND_POSITION, false));

    $("#test").css(CSS.BACKGROUND_POSITION.with(BackgroundPosition.LEFT));
    assertMatches("left|left center|0% 50%", $("#test").css(CSS.BACKGROUND_POSITION, false));

    $("#test").css(CSS.BACKGROUND_POSITION.with(BackgroundPosition.LEFT_TOP));
    assertMatches("left top|0% 0%", $("#test").css(CSS.BACKGROUND_POSITION, false));

    $("#test").css(
        CSS.BACKGROUND_POSITION.with(BackgroundPosition.LEFT_CENTER));
    assertMatches("left center|0% 50%", $("#test").css(CSS.BACKGROUND_POSITION, false));

    $("#test").css(
        CSS.BACKGROUND_POSITION.with(BackgroundPosition.LEFT_BOTTOM));
    assertMatches("left bottom|0% 100%", $("#test").css(CSS.BACKGROUND_POSITION, false));

    $("#test").css(CSS.BACKGROUND_POSITION.with(BackgroundPosition.RIGHT));
    assertMatches("right|right center|100% 50%", $("#test").css(CSS.BACKGROUND_POSITION, false));

    $("#test").css(
        CSS.BACKGROUND_POSITION.with(BackgroundPosition.RIGHT_TOP));
    assertMatches("right top|100% 0%", $("#test").css(CSS.BACKGROUND_POSITION, false));

    $("#test").css(
        CSS.BACKGROUND_POSITION.with(BackgroundPosition.RIGHT_CENTER));
    assertMatches("right center|100% 50%", $("#test").css(CSS.BACKGROUND_POSITION, false));

    $("#test").css(
        CSS.BACKGROUND_POSITION.with(BackgroundPosition.RIGHT_BOTTOM));
    assertMatches("right bottom|100% 100%", $("#test").css(CSS.BACKGROUND_POSITION, false));

    $("#test").css(
        CSS.BACKGROUND_POSITION.with(BackgroundPosition.position(12, 12,
            Unit.PX)));
    assertEquals("12px 12px", $("#test").css(CSS.BACKGROUND_POSITION, false));

    $("#test").css(
        CSS.BACKGROUND_POSITION.with(BackgroundPosition.position(12, 12,
            Unit.PCT)));
    assertEquals("12% 12%", $("#test").css(CSS.BACKGROUND_POSITION, false));
  }

  public void testBackgroundProperty() {

    $(e).html("<div id='test'>Content</div>");

    $("#test").css(
        CSS.BACKGROUND.with(RGBColor.TRANSPARENT, UriValue.url("back.jpg"),
            BackgroundRepeat.NO_REPEAT, BackgroundAttachment.SCROLL,
            BackgroundPosition.CENTER));

    assertMatches(".*back.jpg.*no-repeat scroll (center|center center|50% 50%).*", $("#test").css("background"));
  }

  public void testBackgroundRepeatProperty() {

    $(e).html("<div id='test'>Content</div>");

    $("#test").css(CSS.BACKGROUND_REPEAT.with(BackgroundRepeat.REPEAT_X));

    assertEquals("repeat-x", $("#test").css("backgroundRepeat"));
    assertEquals("repeat-x", $("#test").css(CSS.BACKGROUND_REPEAT));
  }

  public void testBorderCollapseProperty() {

    $(e).html("<table id='test'><tr><td>Content<td></tr></table>");

    $("#test").css(CSS.BORDER_COLLAPSE.with(BorderCollapse.COLLAPSE));

    assertEquals("collapse", $("#test").css("borderCollapse"));
    assertEquals("collapse", $("#test").css(CSS.BORDER_COLLAPSE));

    $("#test").css(CSS.BORDER_COLLAPSE.with(BorderCollapse.SEPARATE));

    assertEquals("separate", $("#test").css("borderCollapse"));
    assertEquals("separate", $("#test").css(CSS.BORDER_COLLAPSE));
  }

  public void testCaptionSideProperty() {

    $(e).html("<table id='test'><tr><td>Content<td></tr></table>");

    $("#test").css(CSS.CAPTION_SIDE.with(CaptionSide.BOTTOM));
    assertEquals("bottom", $("#test").css("captionSide"));
    assertEquals("bottom", $("#test").css(CSS.CAPTION_SIDE));

  }

  public void testEmptyCellsProperty() {

    $(e).html("<table id='test'><tr><td>Content<td></tr></table>");

    $("#test").css(CSS.EMPTY_CELLS.with(EmptyCellsProperty.EmptyCells.HIDE));
    assertEquals("hide", $("#test").css("emptyCells"));
    assertEquals("hide", $("#test").css(CSS.EMPTY_CELLS));

  }

  // In chrome it returns 14.39999px instead of 15px
  @DoNotRunWith(Platform.Prod)
  public void testBorderSpacingProperty() {

    $(e).html("<table id='test'><tr><td>Content<td></tr></table>");

    $("#test").css(CSS.BORDER_SPACING.with(new BorderSpacing(Length.px(15))));

    assertEquals("15px 15px", $("#test").css("borderSpacing"));
    assertEquals("15px 15px", $("#test").css(CSS.BORDER_SPACING));

    $("#test").css(
        CSS.BORDER_SPACING.with(new BorderSpacing(Length.px(10), Length.em(20))));

    assertEquals("10px 20em", $("#test").css("borderSpacing", false));
    assertEquals("10px 20em", $("#test").css(CSS.BORDER_SPACING, false));
  }

  public void testBorderColorProperty() {
    $(e).html("<div id='test'>Content</div>");

    $("#test").css(CSS.BORDER_COLOR.with(RGBColor.AQUA));
    assertEquals("aqua", $("#test").css("borderColor", false));
    assertEquals("aqua", $("#test").css(CSS.BORDER_COLOR, false));

    $("#test").css(CSS.BORDER_BOTTOM_COLOR.with(RGBColor.BLACK));
    assertEquals("black", $("#test").css("borderBottomColor", false));
    assertEquals("black", $("#test").css(CSS.BORDER_BOTTOM_COLOR, false));

    $("#test").css(CSS.BORDER_TOP_COLOR.with(RGBColor.RED));
    assertEquals("red", $("#test").css("borderTopColor", false));
    assertEquals("red", $("#test").css(CSS.BORDER_TOP_COLOR, false));

    $("#test").css(CSS.BORDER_LEFT_COLOR.with(RGBColor.GRAY));
    assertEquals("gray", $("#test").css("borderLeftColor", false));
    assertEquals("gray", $("#test").css(CSS.BORDER_LEFT_COLOR, false));

    $("#test").css(CSS.BORDER_RIGHT_COLOR.with(RGBColor.WHITE));
    assertEquals("white", $("#test").css("borderRightColor", false));
    assertEquals("white", $("#test").css(CSS.BORDER_RIGHT_COLOR, false));
  }

  public void testBorderProperty() {
    $(e).html("<div id='test'>Content</div>");

    $("#test").css(
        CSS.BORDER.with(BorderWidth.THICK, BorderStyle.DASHED, RGBColor.BLACK));
    assertEquals("thick dashed black", $("#test").css("border", false));
    assertEquals("thick dashed black", $("#test").css(CSS.BORDER, false));

    $("#test").css(
        CSS.BORDER.with(Length.px(15), BorderStyle.SOLID, RGBColor.rgb("#000000")));
    assertMatches("15px solid (#000000|rgb\\(0, 0, 0\\))", $("#test").css("border", false));
    assertMatches("15px solid (#000000|rgb\\(0, 0, 0\\))", $("#test").css(CSS.BORDER, false));

    $("#test").css(
        CSS.BORDER_TOP.with(BorderWidth.MEDIUM, BorderStyle.SOLID, RGBColor.GRAY));
    assertEquals("medium solid gray", $("#test").css("borderTop", false));
    assertEquals("medium solid gray", $("#test").css(CSS.BORDER_TOP, false));

    $("#test").css(
        CSS.BORDER_BOTTOM.with(BorderWidth.THIN, BorderStyle.DOUBLE,
            RGBColor.BLUE));
    assertEquals("thin double blue", $("#test").css("borderBottom", false));
    assertEquals("thin double blue", $("#test").css(CSS.BORDER_BOTTOM, false));

    $("#test").css(
        CSS.BORDER_LEFT.with(BorderWidth.THIN, BorderStyle.SOLID, RGBColor.BLACK));
    assertEquals("thin solid black", $("#test").css("borderLeft", false));
    assertEquals("thin solid black", $("#test").css(CSS.BORDER_LEFT, false));

    $("#test").css(
        CSS.BORDER_RIGHT.with(BorderWidth.MEDIUM, BorderStyle.DASHED, RGBColor.GRAY));
    assertEquals("medium dashed gray", $("#test").css("borderRight", false));
    assertEquals("medium dashed gray", $("#test").css(CSS.BORDER_RIGHT, false));
  }

  public void testBorderStyleProperty() {

    $(e).html("<div id='test'>Content</div>");

    $("#test").css(CSS.BORDER_STYLE.with(BorderStyle.DOTTED));
    assertEquals("dotted", $("#test").css("borderStyle"));
    assertEquals("dotted", $("#test").css(CSS.BORDER_STYLE));

    $("#test").css(CSS.BORDER_BOTTOM_STYLE.with(BorderStyle.DASHED));
    assertEquals("dashed", $("#test").css("borderBottomStyle"));
    assertEquals("dashed", $("#test").css(CSS.BORDER_BOTTOM_STYLE));

    $("#test").css(CSS.BORDER_TOP_STYLE.with(BorderStyle.DOUBLE));
    assertEquals("double", $("#test").css("borderTopStyle"));
    assertEquals("double", $("#test").css(CSS.BORDER_TOP_STYLE));

    $("#test").css(CSS.BORDER_LEFT_STYLE.with(BorderStyle.HIDDEN));
    assertEquals("hidden", $("#test").css("borderLeftStyle"));
    assertEquals("hidden", $("#test").css(CSS.BORDER_LEFT_STYLE));

    $("#test").css(CSS.BORDER_RIGHT_STYLE.with(BorderStyle.SOLID));
    assertEquals("solid", $("#test").css("borderRightStyle"));
    assertEquals("solid", $("#test").css(CSS.BORDER_RIGHT_STYLE));

  }

  public void testBorderWidthProperty() {

    $(e).html("<div id='test'>Content</div>");

    $("#test").css(CSS.BORDER_WIDTH.with(BorderWidth.MEDIUM));
    assertEquals("medium", $("#test").css("borderWidth", false));
    assertEquals("medium", $("#test").css(CSS.BORDER_WIDTH, false));

    $("#test").css(CSS.BORDER_WIDTH.with(Length.px(15)));
    assertEquals("15px", $("#test").css(CSS.BORDER_WIDTH, false));

    $("#test").css(CSS.BORDER_WIDTH.with(Length.px(20)));
    assertEquals("20px", $("#test").css(CSS.BORDER_WIDTH, false));

    $("#test").css(CSS.BORDER_WIDTH.with(Length.mm(20)));
    assertEquals("20mm", $("#test").css(CSS.BORDER_WIDTH, false));

    $("#test").css(CSS.BORDER_BOTTOM_WIDTH.with(BorderWidth.THICK));
    assertEquals("thick", $("#test").css("borderBottomWidth", false));
    assertEquals("thick", $("#test").css(CSS.BORDER_BOTTOM_WIDTH, false));

    $("#test").css(CSS.BORDER_TOP_WIDTH.with(BorderWidth.THIN));
    assertEquals("thin", $("#test").css("borderTopWidth", false));
    assertEquals("thin", $("#test").css(CSS.BORDER_TOP_WIDTH, false));

    $("#test").css(CSS.BORDER_LEFT_WIDTH.with(BorderWidth.THIN));
    assertEquals("thin", $("#test").css("borderLeftWidth", false));
    assertEquals("thin", $("#test").css(CSS.BORDER_LEFT_WIDTH, false));

    $("#test").css(CSS.BORDER_RIGHT_WIDTH.with(BorderWidth.THICK));
    assertEquals("thick", $("#test").css("borderRightWidth", false));
    assertEquals("thick", $("#test").css(CSS.BORDER_RIGHT_WIDTH, false));

  }

  public void testClearProperty() {

    $(e).html("<div id='test'>Content</div>");

    $("#test").css(CSS.CLEAR.with(Clear.BOTH));
    assertEquals("both", $("#test").css("clear"));
    assertEquals("both", $("#test").css(CSS.CLEAR));

    $("#test").css(CSS.CLEAR.with(Clear.LEFT));
    assertEquals("left", $("#test").css(CSS.CLEAR));

    $("#test").css(CSS.CLEAR.with(Clear.RIGHT));
    assertEquals("right", $("#test").css(CSS.CLEAR));

    $("#test").css(CSS.CLEAR.with(Clear.NONE));
    assertEquals("none", $("#test").css(CSS.CLEAR));

  }

  public void testClipProperty() {

    $(e).html("<div id='test'>Content</div>");

    $("#test").css(CSS.CLIP.with(Shape.rect(0, 10, 10, 0)));
    assertMatches("rect\\(0px.10px.10px.0px\\)", $("#test").css("clip"));
    assertMatches("rect\\(0px.10px.10px.0px\\)", $("#test").css(CSS.CLIP));
  }

  public void testColorProperty() {
    $(e).html("<div id='test'>Content</div>");

    $("#test").css(CSS.COLOR.with(RGBColor.AQUA));
    assertEquals("aqua", $("#test").css("color", false));
    assertEquals("aqua", $("#test").css(CSS.COLOR, false));

    $("#test").css(CSS.COLOR.with(RGBColor.rgb("#112233")));
    assertMatches("#112233|rgb\\(17, 34, 51\\)", $("#test").css("color", false));
    assertMatches("#112233|rgb\\(17, 34, 51\\)", $("#test").css(CSS.COLOR, false));

    $("#test").css(CSS.COLOR.with(RGBColor.rgb(35, 45, 55)));
    assertMatches("rgb\\(35, *45, *55\\)", $("#test").css("color", false));
    assertMatches("rgb\\(35, *45, *55\\)", $("#test").css(CSS.COLOR, false));
  }

  public void testColorValue() {
    $(e).html("<div id='test'>Content</div>");

    $("#test").css(CSS.COLOR.with(RGBColor.AQUA));
    assertEquals("aqua", $("#test").css(CSS.COLOR, false));

    $("#test").css(CSS.COLOR.with(RGBColor.BLACK));
    assertEquals("black", $("#test").css(CSS.COLOR, false));

    $("#test").css(CSS.COLOR.with(RGBColor.BLUE));
    assertEquals("blue", $("#test").css(CSS.COLOR, false));

//    $("#test").css(CSS.COLOR.with(RGBColor.FUSCHIA));
//    assertEquals("fuschia", $("#test").css(CSS.COLOR, false));

    $("#test").css(CSS.COLOR.with(RGBColor.GRAY));
    assertEquals("gray", $("#test").css(CSS.COLOR, false));

    $("#test").css(CSS.COLOR.with(RGBColor.GREY));
    assertMatches("grey|rgb\\(128, *128, *128\\)", $("#test").css(CSS.COLOR, false));

    $("#test").css(CSS.COLOR.with(RGBColor.GREEN));
    assertEquals("green", $("#test").css(CSS.COLOR, false));

    $("#test").css(CSS.COLOR.with(RGBColor.LIME));
    assertEquals("lime", $("#test").css(CSS.COLOR, false));

    $("#test").css(CSS.COLOR.with(RGBColor.MAROON));
    assertEquals("maroon", $("#test").css(CSS.COLOR, false));

    $("#test").css(CSS.COLOR.with(RGBColor.NAVY));
    assertEquals("navy", $("#test").css(CSS.COLOR, false));

    $("#test").css(CSS.COLOR.with(RGBColor.OLIVE));
    assertEquals("olive", $("#test").css(CSS.COLOR, false));

    $("#test").css(CSS.COLOR.with(RGBColor.ORANGE));
    assertEquals("orange", $("#test").css(CSS.COLOR, false));

    $("#test").css(CSS.COLOR.with(RGBColor.PURPLE));
    assertEquals("purple", $("#test").css(CSS.COLOR, false));

    $("#test").css(CSS.COLOR.with(RGBColor.RED));
    assertEquals("red", $("#test").css(CSS.COLOR, false));

    $("#test").css(CSS.COLOR.with(RGBColor.SILVER));
    assertEquals("silver", $("#test").css(CSS.COLOR, false));

    $("#test").css(CSS.COLOR.with(RGBColor.TEAL));
    assertEquals("teal", $("#test").css(CSS.COLOR, false));

    $("#test").css(CSS.COLOR.with(RGBColor.TRANSPARENT));
    assertEquals("transparent", $("#test").css(CSS.COLOR, false));

    $("#test").css(CSS.COLOR.with(RGBColor.WHITE));
    assertEquals("white", $("#test").css(CSS.COLOR, false));

    $("#test").css(CSS.COLOR.with(RGBColor.YELLOW));
    assertEquals("yellow", $("#test").css(CSS.COLOR, false));

    $("#test").css(CSS.COLOR.with(RGBColor.rgb("#112233")));
    assertMatches("#112233|rgb\\(17, *34, *51\\)", $("#test").css(CSS.COLOR, false));

    $("#test").css(CSS.COLOR.with(RGBColor.rgb(35, 45, 55)));
    assertMatches("rgb\\(35, *45, *55\\)", $("#test").css(CSS.COLOR, false));
  }

  public void testCursorProperty() {
    $(e).html("<div id='test'>Content</div>");

    $("#test").css(CSS.CURSOR.with(Cursor.WAIT));

    assertEquals("wait", $("#test").css("cursor"));
    assertEquals("wait", $("#test").css(CSS.CURSOR));
  }

  public void testDisplayProperty() {
    $(e).html("<div id='test'>Content</div>");

    $("#test").css(CSS.DISPLAY.with(Display.INLINE));

    assertEquals("inline", $("#test").css("display"));
    assertEquals("inline", $("#test").css(CSS.DISPLAY));
  }

  public void testEdgePositionProperty() {
    $(e).html("<div id='test'>Content</div><");

    // Chrome needs position fixed, otherwise returns auto
    $("#test").css(CSS.POSITION.with(Position.FIXED))
              .css(CSS.LEFT.with(Length.px(10)));
    assertEquals("10px", $("#test").css("left"));
    assertEquals("10px", $("#test").css(CSS.LEFT));

    $("#test").css(CSS.TOP.with(Length.px(15)));
    assertEquals("15px", $("#test").css("top"));
    assertEquals("15px", $("#test").css(CSS.TOP));

    $("#test").css(CSS.RIGHT.with(Length.px(0)));
    assertEquals("0px", $("#test").css("right"));
    assertEquals("0px", $("#test").css(CSS.RIGHT));

    $("#test").css(CSS.BOTTOM.with(Length.px(20)));
    assertEquals("20px", $("#test").css("bottom"));
    assertEquals("20px", $("#test").css(CSS.BOTTOM));
  }

  @DoNotRunWith(Platform.Prod)
  // FIXME: do not run in FF not chrome
  public void testFloatProperty() {
    $(e).html("<div><div id='test'>Content</div></div>");
    $("#test").css(CSS.FLOAT.with(Float.LEFT));

    assertEquals("left", $("#test").css("float"));
    assertEquals("left", $("#test").css(CSS.FLOAT));
  }

  public void testFontSizeProperty() {

    $(e).html("<div id='test'>Content</div>");

    $("#test").css(CSS.FONT_SIZE.with(FontSize.LARGER));
    assertEquals("larger", $("#test").css("fontSize", false));
    assertEquals("larger", $("#test").css(CSS.FONT_SIZE, false));

    $("#test").css(CSS.FONT_SIZE.with(FontSize.LARGE));
    assertEquals("large", $("#test").css(CSS.FONT_SIZE, false));

    $("#test").css(CSS.FONT_SIZE.with(FontSize.MEDIUM));
    assertEquals("medium", $("#test").css(CSS.FONT_SIZE, false));

    $("#test").css(CSS.FONT_SIZE.with(FontSize.SMALL));
    assertEquals("small", $("#test").css(CSS.FONT_SIZE, false));

    $("#test").css(CSS.FONT_SIZE.with(FontSize.SMALLER));
    assertEquals("smaller", $("#test").css(CSS.FONT_SIZE, false));

    $("#test").css(CSS.FONT_SIZE.with(FontSize.X_LARGE));
    assertEquals("x-large", $("#test").css(CSS.FONT_SIZE, false));

    $("#test").css(CSS.FONT_SIZE.with(FontSize.X_SMALL));
    assertEquals("x-small", $("#test").css(CSS.FONT_SIZE, false));

    $("#test").css(CSS.FONT_SIZE.with(FontSize.XX_LARGE));
    assertEquals("xx-large", $("#test").css(CSS.FONT_SIZE, false));

    $("#test").css(CSS.FONT_SIZE.with(FontSize.XX_SMALL));
    assertEquals("xx-small", $("#test").css(CSS.FONT_SIZE, false));

    $("#test").css(CSS.FONT_SIZE.with(Length.px(16)));
    assertEquals("16px", $("#test").css(CSS.FONT_SIZE, false));
  }

  public void testFontStyleProperty() {

    $(e).html("<div id='test'>Content</div>");

    $("#test").css(CSS.FONT_STYLE.with(FontStyle.ITALIC));
    assertEquals("italic", $("#test").css("fontStyle"));
    assertEquals("italic", $("#test").css(CSS.FONT_STYLE));

  }

  public void testFontVariantProperty() {

    $(e).html("<div id='test'>Content</div>");

    $("#test").css(CSS.FONT_VARIANT.with(FontVariant.SMALL_CAPS));
    assertEquals("small-caps", $("#test").css("fontVariant"));
    assertEquals("small-caps", $("#test").css(CSS.FONT_VARIANT));

  }

  public void testHeightProperties() {

    $(e).html("<div id='test'>Content</div>");

    $("#test").css(CSS.HEIGHT.with(Length.px(10)));

    assertEquals("10px", $("#test").css("height"));
    assertEquals("10px", $("#test").css(CSS.HEIGHT));

    $("#test").css(CSS.MAX_HEIGHT.with(Length.px(15)));

    assertEquals("15px", $("#test").css("maxHeight"));
    assertEquals("15px", $("#test").css(CSS.MAX_HEIGHT));

    $("#test").css(CSS.MIN_HEIGHT.with(Length.px(5)));

    assertEquals("5px", $("#test").css("minHeight"));
    assertEquals("5px", $("#test").css(CSS.MIN_HEIGHT));
  }

  public void testLengthValue() {

    $(e).html("<div id='test'>Content</div>");

    $("#test").css(CSS.HEIGHT.with(Length.px(10)));
    assertEquals("10px", $("#test").css(CSS.HEIGHT));

    $("#test").css(CSS.HEIGHT.with(Length.cm(10)));
    assertEquals("10cm", $("#test").css(CSS.HEIGHT, false));

    $("#test").css(CSS.HEIGHT.with(Length.em(10)));
    assertEquals("10em", $("#test").css(CSS.HEIGHT, false));

    $("#test").css(CSS.HEIGHT.with(Length.ex(10)));
    assertEquals("10ex", $("#test").css(CSS.HEIGHT, false));

    $("#test").css(CSS.HEIGHT.with(Length.in(10)));
    assertEquals("10in", $("#test").css(CSS.HEIGHT, false));

    $("#test").css(CSS.HEIGHT.with(Length.mm(10)));
    assertEquals("10mm", $("#test").css(CSS.HEIGHT, false));

    $("#test").css(CSS.HEIGHT.with(Length.pc(10)));
    assertEquals("10pc", $("#test").css(CSS.HEIGHT, false));

    $("#test").css(CSS.HEIGHT.with(Length.pct(10)));
    assertEquals("10%", $("#test").css(CSS.HEIGHT, false));

    $("#test").css(CSS.HEIGHT.with(Length.pt(10)));
    assertEquals("10pt", $("#test").css(CSS.HEIGHT, false));

    $("#test").css(CSS.HEIGHT.with(Length.px(10.1)));
    assertEquals("10.1px", $("#test").css(CSS.HEIGHT, false));

    $("#test").css(CSS.HEIGHT.with(Length.cm(10.1)));
    assertEquals("10.1cm", $("#test").css(CSS.HEIGHT, false));

    $("#test").css(CSS.HEIGHT.with(Length.em(10.1)));
    assertEquals("10.1em", $("#test").css(CSS.HEIGHT, false));

    $("#test").css(CSS.HEIGHT.with(Length.ex(10.1)));
    assertEquals("10.1ex", $("#test").css(CSS.HEIGHT, false));

    $("#test").css(CSS.HEIGHT.with(Length.in(10.1)));
    assertEquals("10.1in", $("#test").css(CSS.HEIGHT, false));

    $("#test").css(CSS.HEIGHT.with(Length.mm(10.1)));
    assertEquals("10.1mm", $("#test").css(CSS.HEIGHT, false));

    $("#test").css(CSS.HEIGHT.with(Length.pc(10.1)));
    assertEquals("10.1pc", $("#test").css(CSS.HEIGHT, false));

    $("#test").css(CSS.HEIGHT.with(Length.pct(10.1)));
    assertEquals("10.1%", $("#test").css(CSS.HEIGHT, false));

    $("#test").css(CSS.HEIGHT.with(Length.pt(10.1)));
    assertEquals("10.1pt", $("#test").css(CSS.HEIGHT, false));

  }

  public void testLetterSpacingProperty() {

    $(e).html("<div id='test'>Content</div>");

    $("#test").css(CSS.LETTER_SPACING.with(Length.px(15)));
    assertEquals("15px", $("#test").css("letterSpacing"));
    assertEquals("15px", $("#test").css(CSS.LETTER_SPACING));

  }

  // Modern HtmlUnit for (2.6.0) seems not supporting line-height
  @DoNotRunWith(Platform.HtmlUnitBug)
  public void testLineHeightProperty() {
    $(e).html("<div id='test'>Content</div>");

    $("#test").css(CSS.LINE_HEIGHT.with(Length.px(15)));
    assertEquals("15px", $("#test").css("lineHeight"));
    assertEquals("15px", $("#test").css(CSS.LINE_HEIGHT));

    $("#test").css(CSS.LINE_HEIGHT.with(2));
    assertEquals("2", $("#test").css("lineHeight", false));
    assertEquals("2", $("#test").css(CSS.LINE_HEIGHT, false));

    $("#test").css(CSS.LINE_HEIGHT.with(2.5));
    assertEquals("2.5", $("#test").css("lineHeight", false));
    assertEquals("2.5", $("#test").css(CSS.LINE_HEIGHT, false));
  }

  public void testListStyleImageProperty() {
    $(e).html("<ul id='test'><li>Content</li></ul>");

    $("#test").css(CSS.LIST_STYLE_IMAGE.with(UriValue.url("arrow.jpg")));
    assertMatches("url\\(.*arrow.jpg.*\\)", $("#test").css("listStyleImage"));
    assertMatches("url\\(.*arrow.jpg.*\\)", $("#test").css(CSS.LIST_STYLE_IMAGE));
  }

  public void testListStylePositionProperty() {

    $(e).html("<ul id='test'><li>Content</li></ul>");

    $("#test").css(CSS.LIST_STYLE_POSITION.with(ListStylePosition.INSIDE));
    assertEquals("inside", $("#test").css("listStylePosition"));
    assertEquals("inside", $("#test").css(CSS.LIST_STYLE_POSITION));

    $("#test").css(CSS.LIST_STYLE_POSITION.with(ListStylePosition.OUTSIDE));
    assertEquals("outside", $("#test").css("listStylePosition"));
    assertEquals("outside", $("#test").css(CSS.LIST_STYLE_POSITION));

  }

  public void testListStyleProperty() {

    $(e).html("<ul id='test'><li>Content</li></ul>");

    assertMatches("(disc |)(outside |)(none|)", $("#test").css("listStyle"));

    $("#test").css(
        CSS.LIST_STYLE.with(ListStyleType.DISC, ListStylePosition.OUTSIDE,
            null));
    assertMatches("disc outside( none|)", $("#test").css("listStyle"));
    assertMatches("disc outside( none|)", $("#test").css(CSS.LIST_STYLE));

    $("#test").css(
        CSS.LIST_STYLE.with(ListStyleType.DISC, null, UriValue.url("square.jpg")));
    assertMatches("disc (outside |)url\\(.*square.jpg.*\\)", $("#test").css("listStyle"));
    assertMatches("disc (outside |)url\\(.*square.jpg.*\\)", $("#test").css(CSS.LIST_STYLE));

    $("#test").css(
        CSS.LIST_STYLE.with(null, ListStylePosition.OUTSIDE, UriValue.url("square.jpg")));
    assertMatches("(disc |)outside url\\(.*square.jpg.*\\)", $("#test").css("listStyle"));
    assertMatches("(disc |)outside url\\(.*square.jpg.*\\)", $("#test").css(CSS.LIST_STYLE));
  }

  public void testListStyleTypeProperty() {

    $(e).html("<ul id='test'><li>Content</li></ul>");

    $("#test").css(CSS.LIST_STYLE_TYPE.with(ListStyleType.DISC));
    assertEquals("disc", $("#test").css("listStyleType"));
    assertEquals("disc", $("#test").css(CSS.LIST_STYLE_TYPE));

  }

  public void testMarginProperty() {

    $(e).html("<div id='test'>Content</div>");

    $("#test").css(CSS.MARGIN.with(Length.px(10), Length.px(20)));

    assertEquals("10px 20px", $("#test").css("margin"));
    assertEquals("10px 20px", $("#test").css(CSS.MARGIN));

    $("#test").css(CSS.MARGIN.with(Length.px(10), Length.px(20), Length.px(30)));

    assertEquals("10px 20px 30px", $("#test").css("margin"));
    assertEquals("10px 20px 30px", $("#test").css(CSS.MARGIN));

    $("#test").css(CSS.MARGIN.with(Length.px(10)));

    assertEquals("10px", $("#test").css("margin"));
    assertEquals("10px", $("#test").css(CSS.MARGIN));

    $("#test").css(
        CSS.MARGIN.with(Length.px(10), Length.px(20), Length.px(30),
            Length.px(40)));

    assertEquals("10px 20px 30px 40px", $("#test").css("margin"));
    assertEquals("10px 20px 30px 40px", $("#test").css(CSS.MARGIN));

    $("#test").css(CSS.MARGIN_TOP.with(Length.px(20)));

    assertEquals("20px", $("#test").css("marginTop"));
    assertEquals("20px", $("#test").css(CSS.MARGIN_TOP));

    $("#test").css(CSS.MARGIN_BOTTOM.with(Length.px(30)));

    assertEquals("30px", $("#test").css("marginBottom"));
    assertEquals("30px", $("#test").css(CSS.MARGIN_BOTTOM));

    $("#test").css(CSS.MARGIN_LEFT.with(Length.px(40)));

    assertEquals("40px", $("#test").css("marginLeft"));
    assertEquals("40px", $("#test").css(CSS.MARGIN_LEFT));

    $("#test").css(CSS.MARGIN_RIGHT.with(Length.px(50)));

    assertEquals("50px", $("#test").css("marginRight"));
    assertEquals("50px", $("#test").css(CSS.MARGIN_RIGHT));
  }

  @DoNotRunWith(Platform.Prod)
  // FIXME: do not run in FF not chrome
  public void testOutlineProperty() {
    $(e).html("<div id='test'>Content</div>");

    $("#test").css(CSS.OUTLINE_WIDTH.with(Length.px(10)));
    assertEquals("10px", $("#test").css("outlineWidth"));
    assertEquals("10px", $("#test").css(CSS.OUTLINE_WIDTH));

    $("#test").css(CSS.OUTLINE_WIDTH.with(BorderWidth.MEDIUM));
    assertEquals("medium", $("#test").css("outlineWidth", false));
    assertEquals("medium", $("#test").css(CSS.OUTLINE_WIDTH, false));

    $("#test").css(CSS.OUTLINE_COLOR.with(RGBColor.GRAY));
    assertEquals("gray", $("#test").css("outlineColor", false));
    assertEquals("gray", $("#test").css(CSS.OUTLINE_COLOR, false));

    $("#test").css(CSS.OUTLINE_STYLE.with(BorderStyle.DOTTED));
    assertEquals("dotted", $("#test").css("outlineStyle"));
    assertEquals("dotted", $("#test").css(CSS.OUTLINE_STYLE));

    $("#test").css(
        CSS.OUTLINE.with(RGBColor.BLACK, BorderStyle.DASHED, BorderWidth.MEDIUM));
    assertEquals("black dashed medium", $("#test").css("outline", false));
    assertEquals("black dashed medium", $("#test").css(CSS.OUTLINE, false));

    $("#test").css(
        CSS.OUTLINE.with(RGBColor.AQUA, BorderStyle.DOUBLE, Length.px(15)));
    assertEquals("aqua double 15px", $("#test").css("outline", false));
    assertEquals("aqua double 15px", $("#test").css(CSS.OUTLINE, false));

  }

  public void testOverflowProperty() {
    $(e).html("<div id='test'>Content</div>");

    $("#test").css(CSS.OVERFLOW.with(Overflow.HIDDEN));
    assertEquals("hidden", $("#test").css("overflow"));
    assertEquals("hidden", $("#test").css(CSS.OVERFLOW));

    $("#test").css(CSS.OVERFLOW.with(Overflow.SCROLL));
    assertEquals("scroll", $("#test").css("overflow"));
    assertEquals("scroll", $("#test").css(CSS.OVERFLOW));

    $("#test").css(CSS.OVERFLOW.with(Overflow.VISIBLE));
    assertEquals("visible", $("#test").css("overflow"));
    assertEquals("visible", $("#test").css(CSS.OVERFLOW));

  }

  public void testPaddingProperty() {

    $(e).html("<div id='test'>Content</div>");

    $("#test").css(CSS.PADDING.with(Length.px(10)));

    assertEquals("10px", $("#test").css("padding"));
    assertEquals("10px", $("#test").css(CSS.PADDING));

    $("#test").css(
        CSS.PADDING.with(Length.px(10), Length.px(20)));

    assertEquals("10px 20px", $("#test").css("padding"));
    assertEquals("10px 20px", $("#test").css(CSS.PADDING));

    $("#test").css(
        CSS.PADDING.with(Length.px(10), Length.px(20), Length.px(30)));

    assertEquals("10px 20px 30px", $("#test").css("padding"));
    assertEquals("10px 20px 30px", $("#test").css(CSS.PADDING));

    $("#test").css(
        CSS.PADDING.with(Length.px(10), Length.px(20), Length.px(30),
            Length.px(40)));

    assertEquals("10px 20px 30px 40px", $("#test").css("padding"));
    assertEquals("10px 20px 30px 40px", $("#test").css(CSS.PADDING));

    $("#test").css(CSS.PADDING_TOP.with(Length.px(20)));

    assertEquals("20px", $("#test").css("paddingTop"));
    assertEquals("20px", $("#test").css(CSS.PADDING_TOP));

    $("#test").css(CSS.PADDING_BOTTOM.with(Length.px(30)));

    assertEquals("30px", $("#test").css("paddingBottom"));
    assertEquals("30px", $("#test").css(CSS.PADDING_BOTTOM));

    $("#test").css(CSS.PADDING_LEFT.with(Length.px(40)));

    assertEquals("40px", $("#test").css("paddingLeft"));
    assertEquals("40px", $("#test").css(CSS.PADDING_LEFT));

    $("#test").css(CSS.PADDING_RIGHT.with(Length.px(50)));

    assertEquals("50px", $("#test").css("paddingRight"));
    assertEquals("50px", $("#test").css(CSS.PADDING_RIGHT));
  }

  public void testPositionProperty() {

    $(e).html("<div id='test'>Content</div>");

    $("#test").css(CSS.POSITION.with(Position.ABSOLUTE));
    assertEquals("absolute", $("#test").css("position"));
    assertEquals("absolute", $("#test").css(CSS.POSITION));

    $("#test").css(CSS.POSITION.with(Position.FIXED));
    assertEquals("fixed", $("#test").css("position"));
    assertEquals("fixed", $("#test").css(CSS.POSITION));

    $("#test").css(CSS.POSITION.with(Position.RELATIVE));
    assertEquals("relative", $("#test").css("position"));
    assertEquals("relative", $("#test").css(CSS.POSITION));

    $("#test").css(CSS.POSITION.with(Position.STATIC));
    assertEquals("static", $("#test").css("position"));
    assertEquals("static", $("#test").css(CSS.POSITION));

  }

  public void testTextAlignProperty() {

    $(e).html("<div id='test'>Content</div>");

    $("#test").css(CSS.TEXT_ALIGN.with(TextAlign.CENTER));
    assertEquals("center", $("#test").css("textAlign"));
    assertEquals("center", $("#test").css(CSS.TEXT_ALIGN));

    $("#test").css(CSS.TEXT_ALIGN.with(TextAlign.JUSTIFY));
    assertEquals("justify", $("#test").css("textAlign"));
    assertEquals("justify", $("#test").css(CSS.TEXT_ALIGN));

    $("#test").css(CSS.TEXT_ALIGN.with(TextAlign.LEFT));
    assertEquals("left", $("#test").css("textAlign"));
    assertEquals("left", $("#test").css(CSS.TEXT_ALIGN));

    $("#test").css(CSS.TEXT_ALIGN.with(TextAlign.RIGHT));
    assertEquals("right", $("#test").css("textAlign"));
    assertEquals("right", $("#test").css(CSS.TEXT_ALIGN));

  }

  public void testTextDecorationProperty() {

    $(e).html("<div id='test'>Content</div>");

    $("#test").css(CSS.TEXT_DECORATION.with(TextDecoration.LINE_THROUGH));
    assertEquals("line-through", $("#test").css("textDecoration"));
    assertEquals("line-through", $("#test").css(CSS.TEXT_DECORATION));

  }

  public void testTextIdentProperty() {

    $(e).html("<div id='test'>Content</div>");

    $("#test").css(CSS.TEXT_IDENT.with(Length.px(15)));
    assertEquals("15px", $("#test").css("textIdent", false));
    assertEquals("15px", $("#test").css(CSS.TEXT_IDENT, false));

  }

  public void testTextTransformProperty() {

    $(e).html("<div id='test'>Content</div>");

    $("#test").css(CSS.TEXT_TRANSFORM.with(TextTransform.UPPERCASE));
    assertEquals("uppercase", $("#test").css("textTransform", false));
    assertEquals("uppercase", $("#test").css(CSS.TEXT_TRANSFORM, false));

    $("#test").css(CSS.TEXT_TRANSFORM.with(TextTransform.LOWERCASE));
    assertEquals("lowercase", $("#test").css("textTransform", false));

    $("#test").css(CSS.TEXT_TRANSFORM.with(TextTransform.CAPITALIZE));
    assertEquals("capitalize", $("#test").css("textTransform", false));
  }

  // HtmlUnit in 2.6.0 returns '-moz-isolate' always when asking css(unicode-bidi)
  @DoNotRunWith(Platform.HtmlUnitBug)
  public void testUnicodeBidiProperty() {
    $(e).html("<div id='test'>Content</div>");

    $("#test").css(CSS.UNICODE_BIDI.with(UnicodeBidi.BIDI_OVERRIDE));
    assertEquals("bidi-override", $("#test").css("unicode-bidi"));
    assertEquals("bidi-override", $("#test").css(CSS.UNICODE_BIDI));

    $("#test").css(CSS.UNICODE_BIDI.with(UnicodeBidi.EMBED));
    assertEquals("embed", $("#test").css(CSS.UNICODE_BIDI));

    $("#test").css(CSS.UNICODE_BIDI.with(UnicodeBidi.NORMAL));
    assertEquals("normal", $("#test").css(CSS.UNICODE_BIDI));
  }

  public void testVerticalAlignProperty() {

    $(e).html("<div id='test'>Content</div>");

    $("#test").css(CSS.VERTICAL_ALIGN.with(Length.px(120)));
    assertEquals("120px", $("#test").css("verticalAlign"));
    assertEquals("120px", $("#test").css(CSS.VERTICAL_ALIGN));

    $("#test").css(CSS.VERTICAL_ALIGN.with(VerticalAlign.BASELINE));
    assertEquals("baseline", $("#test").css("verticalAlign"));
    assertEquals("baseline", $("#test").css(CSS.VERTICAL_ALIGN));

    $("#test").css(CSS.VERTICAL_ALIGN.with(VerticalAlign.BOTTOM));
    assertEquals("bottom", $("#test").css("verticalAlign"));
    assertEquals("bottom", $("#test").css(CSS.VERTICAL_ALIGN));

    $("#test").css(CSS.VERTICAL_ALIGN.with(VerticalAlign.MIDDLE));
    assertEquals("middle", $("#test").css("verticalAlign"));
    assertEquals("middle", $("#test").css(CSS.VERTICAL_ALIGN));

    $("#test").css(CSS.VERTICAL_ALIGN.with(VerticalAlign.SUB));
    assertEquals("sub", $("#test").css("verticalAlign"));
    assertEquals("sub", $("#test").css(CSS.VERTICAL_ALIGN));

    $("#test").css(CSS.VERTICAL_ALIGN.with(VerticalAlign.SUPER));
    assertEquals("super", $("#test").css("verticalAlign"));
    assertEquals("super", $("#test").css(CSS.VERTICAL_ALIGN));

    $("#test").css(CSS.VERTICAL_ALIGN.with(VerticalAlign.TEXT_BOTTOM));
    assertEquals("text-bottom", $("#test").css("verticalAlign"));
    assertEquals("text-bottom", $("#test").css(CSS.VERTICAL_ALIGN));

    $("#test").css(CSS.VERTICAL_ALIGN.with(VerticalAlign.TEXT_TOP));
    assertEquals("text-top", $("#test").css("verticalAlign"));
    assertEquals("text-top", $("#test").css(CSS.VERTICAL_ALIGN));

    $("#test").css(CSS.VERTICAL_ALIGN.with(VerticalAlign.TOP));
    assertEquals("top", $("#test").css("verticalAlign"));
    assertEquals("top", $("#test").css(CSS.VERTICAL_ALIGN));

  }

  public void testVisibilityProperty() {

    $(e).html("<div id='test'>Content</div>");

    $("#test").css(CSS.VISIBILITY.with(Visibility.HIDDEN));
    assertEquals("hidden", $("#test").css("visibility"));
    assertEquals("hidden", $("#test").css(CSS.VISIBILITY));

    $("#test").css(CSS.VISIBILITY.with(Visibility.VISIBLE));
    assertEquals("visible", $("#test").css("visibility"));
    assertEquals("visible", $("#test").css(CSS.VISIBILITY));

  }

  public void testWhiteSpaceProperty() {

    $(e).html("<div id='test'>Content</div>");

    $("#test").css(CSS.WHITE_SPACE.with(WhiteSpace.NORMAL));
    assertEquals("normal", $("#test").css("whiteSpace"));
    assertEquals("normal", $("#test").css(CSS.WHITE_SPACE));

    $("#test").css(CSS.WHITE_SPACE.with(WhiteSpace.NOWRAP));
    assertEquals("nowrap", $("#test").css(CSS.WHITE_SPACE));

    $("#test").css(CSS.WHITE_SPACE.with(WhiteSpace.PRE));
    assertEquals("pre", $("#test").css(CSS.WHITE_SPACE));

    $("#test").css(CSS.WHITE_SPACE.with(WhiteSpace.PRE_LINE));
    assertEquals("pre-line", $("#test").css(CSS.WHITE_SPACE));

    $("#test").css(CSS.WHITE_SPACE.with(WhiteSpace.PRE_WRAP));
    assertEquals("pre-wrap", $("#test").css(CSS.WHITE_SPACE));

  }

  public void testWidthProperty() {

    $(e).html("<div id='test'>Content</div>");

    $("#test").css(CSS.WIDTH.with(Length.px(20)));
    assertEquals("20px", $("#test").css("width"));
    assertEquals("20px", $("#test").css(CSS.WIDTH));

    $("#test").css(CSS.MIN_WIDTH.with(Length.px(10)));
    assertEquals("10px", $("#test").css("minWidth"));
    assertEquals("10px", $("#test").css(CSS.MIN_WIDTH));

    $("#test").css(CSS.MAX_WIDTH.with(Length.px(30)));
    assertEquals("30px", $("#test").css("maxWidth"));
    assertEquals("30px", $("#test").css(CSS.MAX_WIDTH));

  }

  public void testWordSpacingProperty() {
    $(e).html("<div id='test'>Content</div>");

    $("#test").css(CSS.WORD_SPACING.with(Length.pt(2)));
    assertEquals("2pt", $("#test").css("wordSpacing", false));
    assertEquals("2pt", $("#test").css(CSS.WORD_SPACING, false));
  }

  @DoNotRunWith(Platform.Prod)
  public void testZIndexProperty() {
    $(e).html("<div id='test'>Content</div>");

    $("#test").css(CSS.ZINDEX.with(1000));
    assertEquals("1000", $("#test").css("zIndex", true));
    assertEquals("1000", $("#test").css(CSS.ZINDEX, true));

    $("#test").css(CSS.ZINDEX.with(null));
    assertMatches("0|auto", $("#test").css("zIndex", true));
    assertMatches("0|auto", $("#test").css(CSS.ZINDEX, true));
  }
}
