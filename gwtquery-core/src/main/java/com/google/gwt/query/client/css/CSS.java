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
package com.google.gwt.query.client.css;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.FontStyle;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.ListStyleType;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.TextDecoration;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.query.client.css.BackgroundAttachmentProperty.BackgroundAttachment;
import com.google.gwt.query.client.css.BackgroundPositionProperty.BackgroundPosition;
import com.google.gwt.query.client.css.BackgroundRepeatProperty.BackgroundRepeat;
import com.google.gwt.query.client.css.BorderCollapseProperty.BorderCollapse;
import com.google.gwt.query.client.css.BorderSpacingProperty.BorderSpacing;
import com.google.gwt.query.client.css.BorderStyleProperty.LineStyle;
import com.google.gwt.query.client.css.BorderWidthProperty.LineWidth;
import com.google.gwt.query.client.css.CaptionSideProperty.CaptionSide;
import com.google.gwt.query.client.css.ClearProperty.Clear;
import com.google.gwt.query.client.css.ClipProperty.Shape;
import com.google.gwt.query.client.css.DirectionProperty.Direction;
import com.google.gwt.query.client.css.EmptyCellsProperty.EmptyCells;
import com.google.gwt.query.client.css.FontSizeProperty.FontSize;
import com.google.gwt.query.client.css.FontVariantProperty.FontVariant;
import com.google.gwt.query.client.css.ListStylePositionProperty.ListStylePosition;
import com.google.gwt.query.client.css.MarginProperty.ShorthandMarginProperty;
import com.google.gwt.query.client.css.PaddingProperty.ShorthandPaddingProperty;
import com.google.gwt.query.client.css.TextAlignProperty.TextAlign;
import com.google.gwt.query.client.css.TextTransformProperty.TextTransform;
import com.google.gwt.query.client.css.UnicodeBidiProperty.UnicodeBidi;
import com.google.gwt.query.client.css.WhiteSpaceProperty.WhiteSpace;

/**
 * This class lists all CSS properties.
 */
public class CSS {

  /**
   * <p>
   * The <i>'background'</i> property is a shorthand property for setting the
   * individual background properties (i.e., <i>'background-color'</i>,
   * <i>'background-image'</i>, <i>'background-repeat'</i>,
   * <i>'background-attachment'</i> and <i>'background-position'</i>) at the
   * same place in the style sheet.
   * </p>
   * <p>
   * The <i>'background'</i> property first sets all the individual background
   * properties to their initial values, then assigns explicit values given in
   * the declaration.
   * </p>
   * <h3>Example :</h3>
   * 
   * <pre class="code">$("#myId").css(CSS.BACKGROUND, RGBColor.TRANSPARENT,
   *     ImageValue.url("back.jpg"), BackgroundRepeat.NO_REPEAT,
   *     BackgroundAttachment.SCROLL, BackgroundPosition.CENTER);</code>
   *
   * </pre>
   */
  public static BackgroundProperty BACKGROUND;

  /**
   * <p>
   * If a background image is specified, this property specifies whether it is
   * fixed with regard to the viewport (<i>'fixed'</i>) or scrolls along with
   * the containing block (<i>'scroll'</i>).
   * </p>
   * 
   * <p>
   * This property can only take a {@link BackgroundAttachment} object as value.
   * </p>
   * <h3>Example:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.BACKGROUND_ATTACHMENT, BackgroundAttachment.FIXED);
   * </pre>
   */
  public static BackgroundAttachmentProperty BACKGROUND_ATTACHMENT;

  /**
   * <p>
   * This property sets the background color of an element, either a color value
   * or the keyword 'transparent', to make the underlying colors shine through.
   * </p>
   * 
   * <p>
   * This property can only take a {@link RGBColor} object as value.
   * </p>
   * <h3>Examples:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.BACKGROUND_COLOR, RGBColor.AQUA);
   * $("#myId2").css(CSS.BACKGROUND_COLOR, RGBColor.rgb("#112233"));
   * $("#myId3").css(CSS.BACKGROUND_COLOR, RGBColor.rgb((35, 45, 55));
   * </pre>
   * 
   */
  public static BackgroundColorProperty BACKGROUND_COLOR;

  /**
   * <p>
   * This property sets the background image of an element. When setting a
   * background image, authors should also specify a background color that will
   * be used when the image is unavailable. When the image is available, it is
   * rendered on top of the background color. (Thus, the color is visible in the
   * transparent parts of the image).
   * </p>
   * 
   * <p>
   * This property takes a {@link ImageValue} object as value.
   * </p>
   * 
   * <h3>Examples:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.BACKGROUND_IMAGE, ImageValue.url("image.jpg"));
   * $("#myId2").css(CSS.BACKGROUND_IMAGE, ImageValue.NONE);
   * </pre>
   * 
   */
  public static BackgroundImageProperty BACKGROUND_IMAGE;

  /**
   * <p>
   * If a background image has been specified, this property specifies its
   * initial position.
   * </p>
   * 
   * <p>
   * This property takes a {@link BackgroundPosition} object as value.
   * </p>
   * 
   * <h3>Examples:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.BACKGROUND_POSITION, BackgroundPosition.CENTER_TOP);
   * $("#myId2").css(CSS.BACKGROUND_POSITION,BackgroundPosition.position(25, 25, Unit.PCT));
   * </pre>
   * 
   */
  public static BackgroundPositionProperty BACKGROUND_POSITION;

  /**
   * <p>
   * If a background image is specified, this property specifies whether the
   * image is repeated (tiled), and how. All tiling covers the content and
   * padding areas of a box
   * </p>
   * 
   * <p>
   * This property takes a {@link BackgroundRepeat} object as value.
   * </p>
   * 
   * <h3>Example:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.BACKGROUND_REPEAT, BackgroundRepeat.REPEAT_X);
   * </pre>
   */
  public static BackgroundRepeatProperty BACKGROUND_REPEAT;

  /**
   * <p>
   * The <i>border</i> property is a shorthand property for setting the same
   * width, color, and style for all four borders of a box. Unlike the shorthand
   * <i>margin</i> and <i>padding</i> properties, the <i>border</i> property
   * cannot set different values on the four borders. To do so, one or more of
   * the other border properties must be used.
   * </p>
   * 
   * <h3>Example:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.BORDER, LineWidth.THICK, LineStyle.DASHED,RGBColor.BLACK);
   * $("#myId2").css(CSS.BORDER, LineWidth.length(3, Unit.PX),LineStyle.SOLID, RGBColor.rgb("#000000"));
   * </pre>
   */
  public static BorderProperty BORDER;

  /**
   * <p>
   * The <i>border-bottom</i> property is a shorthand property for setting the
   * width, style, and color of the bottom border of a box.
   * </p>
   * 
   * <h3>Examples:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.BORDER_BOTTOM, LineWidth.THICK,LineStyle.DASHED, RGBColor.BLACK);
   * $("#myId2").css(CSS.BORDER_BOTTOM, LineWidth.length(3, Unit.PX),LineStyle.SOLID, RGBColor.rgb("#000000"));
   * </pre>
   */
  public static BorderProperty BORDER_BOTTOM;

  /**
   * <p>
   * The <i>border-bottom-color</i> property specifies the color of the bottom
   * border of a box.
   * </p>
   * 
   * <p>
   * This property takes a {@link RGBColor} object as value.
   * </p>
   * 
   * <h3>Examples:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.BORDER_BOTTOM_COLOR, RGBColor.BLACK);
   * $("#myId2").css(CSS.BORDER_BOTTOM_COLOR, RGBColor.rgb("#000000"));
   * </pre>
   */
  public static BorderColorProperty BORDER_BOTTOM_COLOR;

  /**
   * <p>
   * The <i>border-bottom-style</i> property specifies the line style of a box's
   * bottom border (solid, double, dashed, etc.).
   * </p>
   * 
   * <p>
   * This property takes a {@link LineStyle} object as value.
   * </p>
   * 
   * <h3>Example:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.BORDER_BOTTOM_STYLE, LineStyle.DASHED);
   * </pre>
   */
  public static BorderStyleProperty BORDER_BOTTOM_STYLE;

  /**
   * <p>
   * The <i>border-bottom-width</i> property specifies the width of the bottom
   * border of a box.
   * </p>
   * 
   * <p>
   * This property takes a {@link LineWidth} or a {@link Length} object as
   * value.
   * </p>
   * 
   * <h3>Examples:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.BORDER_BOTTOM_WIDTH, LineWidth.THICK);
   * $("#myId2").css(CSS.BORDER_BOTTOM_WIDTH, Length.px(2));
   * </pre>
   */
  public static BorderWidthProperty BORDER_BOTTOM_WIDTH;

  /**
   * <p>
   * The <i>border-collapse</i> selects a table's border model.
   * </p>
   * 
   * <p>
   * This property takes a {@link BorderCollapse} object as value.
   * </p>
   * 
   * <h3>Example:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.BORDER_COLLAPSE, BorderCollapse.COLLAPSE);
   * </pre>
   */
  public static BorderCollapseProperty BORDER_COLLAPSE;

  /**
   * <p>
   * The <i>border-color</i> property specifies the color of the 4 borders of a
   * box.
   * </p>
   * 
   * <p>
   * This property takes a {@link RGBColor} object as value.
   * </p>
   * 
   * <h3>Examples:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.BORDER_COLOR, RGBColor.BLACK);
   * $("#myId2").css(CSS.BORDER_COLOR, RGBColor.rgb("#000000"));
   * </pre>
   */
  public static BorderColorProperty BORDER_COLOR;

  /**
   * <p>
   * The <i>border-left</i> property is a shorthand property for setting the
   * width, style, and color of the left border of a box.
   * </p>
   * 
   * <h3>Examples:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.BORDER_LEFT, LineWidth.THICK, LineStyle.DASHED, RGBColor.BLACK);
   * $("#myId2").css(CSS.BORDER_LEFT, LineWidth.length(3, Unit.PX), LineStyle.SOLID, RGBColor.rgb("#000000"));
   * </pre>
   */
  public static BorderProperty BORDER_LEFT;

  /**
   * <p>
   * The <i>border-left-color</i> property specifies the color of the left
   * border of a box.
   * </p>
   * 
   * <p>
   * This property takes a {@link RGBColor} object as value.
   * </p>
   * 
   * <h3>Examples:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.BORDER_LEFT_COLOR, RGBColor.BLACK);
   * $("#myId2").css(CSS.BORDER_LEFT_COLOR, RGBColor.rgb("#000000"));
   * </pre>
   */
  public static BorderColorProperty BORDER_LEFT_COLOR;

  /**
   * <p>
   * The <i>border-left-style</i> property specifies the line style of a box's
   * left border (solid, double, dashed, etc.).
   * </p>
   * 
   * <p>
   * This property takes a {@link LineStyle} object as value.
   * </p>
   * 
   * <h3>Example:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.BORDER_LEFT_STYLE, LineStyle.DASHED);
   * </pre>
   */
  public static BorderStyleProperty BORDER_LEFT_STYLE;

  /**
   * <p>
   * The <i>border-left-width</i> property specifies the width of the left
   * border of a box.
   * </p>
   * 
   * <p>
   * This property takes a {@link LineWidth} or a {@link Length} object as
   * value.
   * </p>
   * 
   * <h3>Example:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.BORDER_BOTTOM_WIDTH, LineWidth.THICK);
   * $("#myId2").css(CSS.BORDER_BOTTOM_WIDTH, Length.px(2));
   * </pre>
   */
  public static BorderWidthProperty BORDER_LEFT_WIDTH;

  /**
   * <p>
   * The <i>border-right</i> property is a shorthand property for setting the
   * width, style, and color of the right border of a box.
   * </p>
   * 
   * <h3>Examples:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.BORDER_RIGHT, LineWidth.THICK,LineStyle.DASHED, RGBColor.BLACK);
   * $("#myId2").css(CSS.BORDER_RIGHT, LineWidth.length(3, Unit.PX),LineStyle.SOLID, RGBColor.rgb("#000000"));
   * </pre>
   */
  public static BorderProperty BORDER_RIGHT;

  /**
   * <p>
   * The <i>border-right-color</i> property specifies the color of the right
   * border of a box.
   * </p>
   * 
   * <p>
   * This property takes a {@link RGBColor} object as value.
   * </p>
   * 
   * <h3>Examples:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.BORDER_RIGHT_COLOR, RGBColor.BLACK);
   * $("#myId2").css(CSS.BORDER_RIGHT_COLOR, RGBColor.rgb("#000000"));
   * </pre>
   */
  public static BorderColorProperty BORDER_RIGHT_COLOR;

  /**
   * <p>
   * The <i>border-right-style</i> property specifies the line style of a box's
   * right border (solid, double, dashed, etc.).
   * </p>
   * 
   * <p>
   * This property takes a {@link LineStyle} object as value.
   * </p>
   * 
   * <h3>Example:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.BORDER_RIGHT_STYLE, LineStyle.DASHED);
   * </pre>
   */
  public static BorderStyleProperty BORDER_RIGHT_STYLE;

  /**
   * <p>
   * The <i>border-right-width</i> property specifies the width of the right
   * border of a box.
   * </p>
   * 
   * <p>
   * This property takes a {@link LineWidth} or a {@link Length} object as
   * value.
   * </p>
   * 
   * <h3>Examples:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.BORDER_RIGHT_WIDTH, LineWidth.THICK);
   * $("#myId2").css(CSS.BORDER_RIGHT_WIDTH, Length.px(2));
   * </pre>
   */
  public static BorderWidthProperty BORDER_RIGHT_WIDTH;

  /**
   * <p>
   * The <i>border-spacing</i> property specifies the distance that separates
   * adjacent cell borders in a table context. If one length is specified, it
   * gives both the horizontal and vertical spacing. If two are specified, the
   * first gives the horizontal spacing and the second the vertical spacing.
   * Lengths may not be negative.
   * </p>
   * 
   * <p>
   * This property takes a {@link BorderSpacing} object as value.
   * </p>
   * 
   * <h3>Exampls:</h3>
   * 
   * <pre class="code">
   * // specify a horizontal and vertical spacing of 15px
   * $("#myId").css(CSS.BORDER_SPACING, new BorderSpacing(Length.px(15)));
   *
   * // specify a horizontal spacing of 10px and a vertical spacing of 20em
   * $("#myId2").css(CSS.BORDER_SPACING, new BorderSpacing(Length.px(10), Length.em(20)));
   * </pre>
   */
  public static BorderSpacingProperty BORDER_SPACING;

  /**
   * <p>
   * The <i>border-style</i> property specifies the line style of the 4 borders
   * of a box (solid, double, dashed, etc.).
   * </p>
   * 
   * <p>
   * This property takes a {@link LineStyle} object as value.
   * </p>
   * 
   * <h3>Example:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.BORDER_STYLE, LineStyle.DASHED);
   * </pre>
   */
  public static BorderStyleProperty BORDER_STYLE;

  /**
   * <p>
   * The <i>border-top</i> property is a shorthand property for setting the
   * width, style, and color of the top border of a box.
   * </p>
   * 
   * <h3>Examples:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.BORDER_TOP, LineWidth.THICK, LineStyle.DASHED, RGBColor.BLACK);
   * $("#myId2").css(CSS.BORDER_TOP, LineWidth.length(3, Unit.PX), LineStyle.SOLID, RGBColor.rgb("#000000"));
   * </pre>
   */
  public static BorderProperty BORDER_TOP;

  /**
   * <p>
   * The <i>border-top-color</i> property specifies the color of the top border
   * of a box.
   * </p>
   * 
   * <p>
   * This property takes a {@link RGBColor} object as value.
   * </p>
   * 
   * <h3>Examples:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.BORDER_TOP_COLOR, RGBColor.BLACK);
   * $("#myId2").css(CSS.BORDER_TOP_COLOR, RGBColor.rgb("#000000"));
   * </pre>
   */
  public static BorderColorProperty BORDER_TOP_COLOR;

  /**
   * <p>
   * The <i>border-top-style</i> property specifies the line style of a box's
   * top border (solid, double, dashed, etc.).
   * </p>
   * 
   * <p>
   * This property takes a {@link LineStyle} object as value.
   * </p>
   * 
   * <h3>Example:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.BORDER_TOP_STYLE, LineStyle.DASHED);
   * </pre>
   */
  public static BorderStyleProperty BORDER_TOP_STYLE;

  /**
   * <p>
   * The <i>border-top-width</i> property specifies the width of the top border
   * of a box.
   * </p>
   * 
   * <p>
   * This property takes a {@link LineWidth} or a {@link Length} object as
   * value.
   * </p>
   * 
   * <h3>Example:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.BORDER_TOP_WIDTH, LineWidth.THICK);
   * $("#myId2").css(CSS.BORDER_TOP_WIDTH, Length.px(2));
   * </pre>
   */
  public static BorderWidthProperty BORDER_TOP_WIDTH;

  /**
   * <p>
   * The <i>border-width</i> property specifies the width of the 4 border of a
   * box.
   * </p>
   * 
   * <p>
   * This property takes a {@link LineWidth} or a {@link Length} object as
   * value.
   * </p>
   * 
   * <h3>Example:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.BORDER_WIDTH, LineWidth.THICK);
   * $("#myId2").css(CSS.BORDER_WIDTH, Length.px(2));
   * </pre>
   */
  public static BorderWidthProperty BORDER_WIDTH;

  /**
   * <p>
   * This property specifies how far a box's bottom content edge is offset above
   * the bottom of the box's containing block.
   * </p>
   * <p>
   * For absolutely positioned elements, the bottom property sets the bottom
   * edge of an element to a unit above the bottom edge of its containing
   * element.
   * </p>
   * <p>
   * For relatively positioned elements,it sets the bottom edge of an element to
   * a unit above its normal position.
   * </p>
   * <p>
   * For static positioned elements, the bottom property has no effect.
   * </p>
   * 
   * <p>
   * This property takes a {@link Length} object as value.
   * </p>
   * 
   * <h3>Example:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.BOTTOM,Length.px(20));
   * </pre>
   */
  public static EdgePositionProperty BOTTOM;

  /**
   * <p>
   * The <i>caption-side</i> property specifies the position of the caption box
   * with respect to the table box.
   * </p>
   * 
   * <p>
   * This property takes a {@link CaptionSide} object as value.
   * </p>
   * 
   * <h3>Example:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.CAPTION_SIDE, CaptionSide.BOTTOM);
   * </pre>
   */
  public static CaptionSideProperty CAPTION_SIDE;

  /**
   * <p>
   * The <i>clear</i> property indicates which sides of an element's box(es) may
   * not be adjacent to an earlier floating box. (It may be that the element
   * itself has floating descendants; the <i>clear</i> property has no effect on
   * those.)
   * </p>
   * 
   * <p>
   * This property takes a {@link Clear} object as value.
   * </p>
   * 
   * <h3>Example:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.CLEAR, Clear.BOTH);
   * </pre>
   */
  public static ClearProperty CLEAR;

  /**
   * <p>
   * A clipping region defines what portion of an element's rendered content is
   * visible. By default, the clipping region has the same size and shape as the
   * element's box(es). The <i>clip</i> property allows you to modify this
   * cliping region by defining a shape.
   * </p>
   * <p>
   * The <i>clip</i> property applies to elements that have a <i>overflow</i>
   * property with a value other than <i>visible</i>.
   * </p>
   * <p>
   * This property takes a {@link Shape} object as value.
   * </p>
   * 
   * <h3>Example:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.CLIP, Shape.rect(0, 10, 10, 0));
   * </pre>
   */
  public static ClipProperty CLIP;

  /**
   * <p>
   * The <i>color</i> property describes the foreground color of an element's
   * text content.
   * </p>
   * <p>
   * This property takes a {@link RGBColor} object as value.
   * </p>
   * 
   * <h3>Example:</h3>
   * 
   * <pre class="code">
   * // Three different ways to specify red as text color for the element with id 'myId'
   * $("#myId").css(CSS.COLOR, RGBColor.RED);
   * $("#myId").css(CSS.COLOR, RGBColor.rgb(255,0,0));
   * $("#myId").css(CSS.COLOR, RGBColor.rgb("#FF0000"));
   *
   * String color =  $("#myId").css(CSS.COLOR); 
   * // retrieve the color of the element with id 'myId'
   * </pre>
   */
  public static ColorProperty COLOR;

  /**
   * <p>
   * The <i>cursor</i> property specifies the type of cursor to be displayed for
   * the pointing device
   * </p>
   * <p>
   * This property takes a {@link Cursor} object as value.
   * </p>
   * 
   * <h3>Example:</h3>
   * 
   * <pre class="code">
   * // set a cursor to the element with id 'myId'
   * $("#myId").css(CSS.CURSOR, Cursor.WAIT);
   * // retrieve the cursor of the element with id 'myId'
   * String cursor = $("#myId").css(CSS.CURSOR);
   *
   * </pre>
   */
  public static CursorProperty CURSOR;

  /**
   * <p>
   * The <i>direction</i> specifies the base writing direction of blocks and the
   * direction of embeddings and overrides (see 'unicode-bidi') for the Unicode
   * bidirectional algorithm. In addition, it specifies the direction of table
   * column layout, the direction of horizontal overflow, and the position of an
   * incomplete last line in a block in case of 'text-align: justify'.
   * </p>
   * <p>
   * This property takes a {@link Direction} object as value.
   * </p>
   * 
   * <h3>Example:</h3>
   * 
   * <pre class="code">
   * // set the direction property to the element with id 'myId'
   * $("#myId").css(CSS.DIRECTION, DIRECTION.RTL);
   * // retrieve the direction property of the element with id 'myId'
   * String direction = $("#myId").css(CSS.DIRECTION);
   *
   * </pre>
   */
  public static DirectionProperty DIRECTION;

  /**
   * <p>
   * This property takes a {@link UnicodeBidi} object as value.
   * </p>
   * 
   * <h3>Example:</h3>
   * 
   * <pre class="code">
   * // set the display property to the element with id 'myId'
   * $("#myId").css(CSS.DISPLAY, Display.INLINE);
   * // retrieve the display property of the element with id 'myId'
   * String display = $("#myId").css(CSS.DISPLAY);
   *
   * </pre>
   */
  public static UnicodeBidiProperty UNICODE_BIDI;

  /**
   * <p>
   * The <i>display</i> property specifies the mechanism by which elements are
   * rendered.
   * </p>
   * <p>
   * This property takes a {@link Display} object as value.
   * </p>
   * 
   * <h3>Example:</h3>
   * 
   * <pre class="code">
   * // set the display property to the element with id 'myId'
   * $("#myId").css(CSS.DISPLAY, Display.INLINE);
   * // retrieve the display property of the element with id 'myId'
   * String display = $("#myId").css(CSS.DISPLAY);
   *
   * </pre>
   */
  public static DisplayProperty DISPLAY;

  /**
   * <p>
   * In the separated borders model, The <i>empty-cells/i> property controls the
   * rendering of borders around cells that have no visible content. Empty cells
   * and cells with the 'visibility' property set to 'hidden' are considered to
   * have no visible content. Visible content includes "&nbsp;" and other
   * whitespace except ASCII CR ("\0D"), LF ("\0A"), tab ("\09"), and space
   * ("\20").
   * </p>
   * <p>
   * When this property has the value <i>show</i>, borders are drawn around
   * empty cells (like normal cells).
   * </p>
   * <p>
   * A value of <i>hide</i> means that no borders are drawn around empty cells.
   * Furthermore, if all the cells in a row have a value of <i>hide</i> and have
   * no visible content, the entire row behaves as if it had <i>display:
   * none</i>.
   * </p>
   * 
   * <p>
   * This property takes a {@link EmptyCells} object as value.
   * </p>
   * 
   * <h3>Example:</h3>
   * 
   * <pre class="code">
   * // set the empty-cells property to the element with id 'myId'
   * $("#myId").css(CSS.EMPTY_CELLS, EmptyCells.HIDE);
   * </pre>
   */
  public static EmptyCellsProperty EMPTY_CELLS;

  /**
   * <p>
   * The <i>float</i> property specifies whether a box should float to the left,
   * right, or not at all. It may be set for elements that generate boxes that
   * are not absolutely positioned.
   * </p>
   * 
   * <p>
   * This property takes a {@link Float} object as value.
   * </p>
   * 
   * <h3>Example:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.FLOAT, Float.LEFT);
   * </pre>
   */
  public static FloatProperty FLOAT;

  /**
   * <p>
   * The <i>font-size</i> property requests normal (sometimes referred to as
   * "roman" or "upright"), italic, and oblique faces within a font family.
   * </p>
   * 
   * <p>
   * This property takes a {@link FontSize} or a {@link Length} object as value.
   * </p>
   * 
   * <h3>Examples:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.FONT_SIZE, FontSize.X_LARGE);
   * $("#myId").css(CSS.FONT_SIZE, Length.px(16));
   * </pre>
   */
  public static FontSizeProperty FONT_SIZE;

  /**
   * <p>
   * The <i>font-style</i> property requests normal (sometimes referred to as
   * "roman" or "upright"), italic, and oblique faces within a font family.
   * </p>
   * 
   * <p>
   * This property takes a {@link FontStyle} object as value.
   * </p>
   * 
   * <h3>Example:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.FONT_STYLE, FontStyle.ITALIC);
   * </pre>
   */
  public static FontStyleProperty FONT_STYLE;

  /**
   * <p>
   * In a small-caps font, the glyphs for lowercase letters look similar to the
   * uppercase ones, but in a smaller size and with slightly different
   * proportions. The <i>font-variant</i> property requests such a font for
   * bicameral (having two cases, as with Latin script). This property has no
   * visible effect for scripts that are unicameral (having only one case, as
   * with most of the world's writing systems).
   * </p>
   * 
   * <p>
   * This property takes a {@link FontVariant} object as value.
   * </p>
   * 
   * <h3>Example:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.FONT_VARIANT, FontVariant.SMALL_CAPS);
   * </pre>
   */
  public static FontVariantProperty FONT_VARIANT;

  /**
   * <p>
   * The <i>font-weight</i> property specifies the weight of the font.
   * </p>
   * 
   * <p>
   * This property takes a {@link FontWeight} object as value.
   * </p>
   * 
   * <h3>Example:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.FONT_WEIGHT, FontWeight.BOLD);
   * </pre>
   */
  public static FontWeightProperty FONT_WEIGHT;

  /**
   * <p>
   * The <i>height</i> property specifies the content height of boxes generated
   * by block-level, inline-block and replaced elements.
   * </p>
   * <p>
   * This property does not apply to non-replaced inline-level elements. See the
   * section on computing heights and margins for non-replaced inline elements
   * for the rules used instead.
   * </p>
   * 
   * <p>
   * This property takes a {@link Length} object as value.
   * </p>
   * 
   * <h3>Example:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.HEIGHT, Length.px(10))
   * </pre>
   */
  public static HeightProperty HEIGHT;

  /**
   * CSS value specifying that the setting of the related property should be
   * inherited from the parent element
   */
  public static final String INHERIT= "inherit";
 

  /**
   * <p>
   * This property specifies how far a box's left content edge is offset to the
   * right of the left edge of the box's containing block.
   * </p>
   * <p>
   * For absolutely positioned elements, the left property sets the left edge of
   * an element to a unit to the left/right of the left edge of its containing
   * element.
   * </p>
   * <p>
   * For relatively positioned elements, the left property sets the left edge of
   * an element to a unit to the left/right to its normal position.
   * </p>
   * <p>
   * For static positioned elements, the left property has no effect.
   * </p>
   * 
   * <p>
   * This property takes a {@link Length} object as value.
   * </p>
   * 
   * <h3>Example:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.LEFT,Length.px(20));
   * </pre>
   */
  public static EdgePositionProperty LEFT;

  /**
   * <p>
   * The <i>letter-spacing</i> property specifies spacing behavior between text
   * characters.
   * </p>
   * 
   * <p>
   * This property takes a {@link Length} object as value.
   * </p>
   * 
   * <h3>Example:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.LETTER_SPACING, Length.px(3));
   * </pre>
   */
  public static LetterSpacingProperty LETTER_SPACING;

  /**
   * <p>
   * If the property is set on a block-level element whose content is composed
   * of inline-level elements, it specifies the minimal height of each generated
   * inline box.
   * </p>
   * 
   * <p>
   * If the property is set on an inline-level element, it specifies the exact
   * height of each box generated by the element. (Except for inline replaced
   * elements, where the height of the box is given by the <i>height</i>
   * property.)
   * </p>
   * 
   * <p>
   * This property takes a {@link Length} object or a {@link CssNumber} object
   * as value.
   * </p>
   * 
   * <h3>Example:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.LINE_HEIGHT, Length.px(15));
   * $("#myId").css(CSS.LINE_HEIGHT, new CssNumber(2));
   * </pre>
   */
  public static LineHeightProperty LINE_HEIGHT;

  /**
   * <p>
   * The <i>list-style</i> property is a shorthand notation for setting the
   * three properties <i>list-style-type</i>, <i>list-style-image</i>, and
   * <i>list-style-position</i> at the same place in the style sheet.
   * </p>
   * 
   * <h3>Example:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.LIST_STYLE, ListStyleType.DISC,ListStylePosition.OUTSIDE, ImageValue.NONE);
   * </pre>
   */
  public static ListStyleProperty LIST_STYLE;

  /**
   * <p>
   * The <i>list-style-image</i> property sets the image that will be used as
   * the list item marker. When the image is available, it will replace the
   * marker set with the <i>list-style-type</i> marker.
   * </p>
   * 
   * <p>
   * This property takes a {@link ImageValue} object as value.
   * </p>
   * 
   * <h3>Example:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.LIST_STYLE_IMAGE, ImageValue.url("arrow.jpg"));
   * </pre>
   */
  public static ListStyleImageProperty LIST_STYLE_IMAGE;

  /**
   * <p>
   * The <i>list-style-position</i> property specifies the position of the
   * marker box in the principal block box.
   * </p>
   * 
   * <p>
   * This property takes a {@link ListStylePosition} object as value.
   * </p>
   * 
   * <h3>Example:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.LIST_STYLE_POSITION, ListStylePosition.OUTSIDE);
   * </pre>
   */
  public static ListStylePositionProperty LIST_STYLE_POSITION;

  /**
   * <p>
   * The <i>list-style-type</i> property specifies appearance of the list item
   * marker if <i>list-style-image</i> has the value 'none' or if the image
   * pointed to by the URI cannot be displayed. The value 'none' specifies no
   * marker, otherwise there are three types of marker: glyphs, numbering
   * systems, and alphabetic systems. Note. Numbered lists improve document
   * accessibility by making lists easier to navigate.
   * </p>
   * 
   * <p>
   * Glyphs are specified with disc, circle, and square. Their exact rendering
   * depends on the user agent.
   * </p>
   * 
   * <p>
   * This property takes a {@link ListStyleType} object as value.
   * </p>
   * 
   * <h3>Example:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.LIST_STYLE_TYPE, ListStyleType.DISC);
   * </pre>
   */
  public static ListStyleTypeProperty LIST_STYLE_TYPE;

  /**
   * <p>
   * The <i>margin</i> property is a shorthand property for setting
   * <i>margin-top</i>, <i>margin-right</i>, <i>margin-bottom</i>, and
   * <i>margin-left</i> at the same place in the style sheet.
   * </p>
   * 
   * <p>
   * If there is only one value, it applies to all sides. If there are two
   * values, the top and bottom margins are set to the first value and the right
   * and left margins are set to the second. If there are three values, the top
   * is set to the first value, the left and right are set to the second, and
   * the bottom is set to the third. If there are four values, they apply to the
   * top, right, bottom, and left, respectively
   * </p>
   * 
   * <h3>Example:</h3>
   * 
   * <pre class="code">
   * // set margin-top and margin-bottom to 10px and margin-left and margin-right to 20px
   * $("#myId").css(CSS.MARGIN, Length.px(10), Length.px(20), null, null);
   * // set margin-top to 10px, margin-right to 20px and margin-bottom to 30px and margin-right to 40px
   * $("#myId").css(CSS.MARGIN, Length.px(10), Length.px(20), Length.px(30), Length.px(40));
   * </pre>
   */
  public static ShorthandMarginProperty MARGIN;

  /**
   * <p>
   * The <i>margin-bottom</i> property specifies the width of the margin area of
   * the bottom of a box
   * </p>
   * 
   * <p>
   * This property takes a {@link Length} object as value.
   * </p>
   * 
   * <h3>Example:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.MARGIN_BOTTOM, Length.px(30));
   * </pre>
   */
  public static MarginProperty MARGIN_BOTTOM;

  /**
   * <p>
   * The <i>margin-left</i> property specifies the width of the margin area of
   * the left side of a box
   * </p>
   * 
   * <p>
   * This property takes a {@link Length} object as value.
   * </p>
   * 
   * <h3>Example:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.MARGIN_LEFT, Length.px(30));
   * </pre>
   */
  public static MarginProperty MARGIN_LEFT;

  /**
   * <p>
   * The <i>margin-right</i> property specifies the width of the margin area of
   * the right side of a box
   * </p>
   * 
   * <p>
   * This property takes a {@link Length} object as value.
   * </p>
   * 
   * <h3>Example:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.MARGIN_RIGHT, Length.px(30));
   * </pre>
   */
  public static MarginProperty MARGIN_RIGHT;

  /**
   * <p>
   * The <i>margin-top</i> property specifies the width of the margin area of
   * the top of a box
   * </p>
   * 
   * <p>
   * This property takes a {@link Length} object as value.
   * </p>
   * 
   * <h3>Example:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.MARGIN_TOP, Length.px(30));
   * </pre>
   */
  public static MarginProperty MARGIN_TOP;

  /**
   * <p>
   * The <i>max-height</i> property sets the maximum height of an element.
   * </p>
   * 
   * <p>
   * This property takes a {@link Length} object as value.
   * </p>
   * 
   * <h3>Example:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.MAX_HEIGHT, Length.px(15));
   * </pre>
   */
  public static HeightProperty MAX_HEIGHT;

  /**
   * <p>
   * The <i>max-width</i> property sets the maximum width of an element.
   * </p>
   * 
   * <p>
   * This property takes a {@link Length} object as value.
   * </p>
   * 
   * <h3>Example:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.MAX_WIDTH, Length.px(100));
   * </pre>
   */
  public static WidthProperty MAX_WIDTH;

  /**
   * <p>
   * The <i>min-height</i> property sets the minimum height of an element.
   * </p>
   * 
   * <p>
   * This property takes a {@link Length} object as value.
   * </p>
   * 
   * <h3>Example:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.MIN_HEIGHT, Length.px(15));
   * </pre>
   */
  public static HeightProperty MIN_HEIGHT;

  /**
   * <p>
   * The <i>max-width</i> property sets the minimum width of an element.
   * </p>
   * 
   * <p>
   * This property takes a {@link Length} object as value.
   * </p>
   * 
   * <h3>Example:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.MIN_WIDTH, Length.px(15));
   * </pre>
   */
  public static WidthProperty MIN_WIDTH;

  /**
   * <p>
   * An outline is a line that is drawn around elements (outside the borders) to
   * make the element "stand out".
   * <p>
   * </p>
   * The outline shorthand property sets all the outline properties in one
   * declaration. </p>
   * 
   * <h3>Example:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.OUTLINE, RGBColor.BLACK, LineStyle.DASHED, LineWidth.length(15, Unit.PX));
   * </pre>
   */
  public static OutlineProperty OUTLINE;

  /**
   * <p>
   * An outline is a line that is drawn around elements (outside the borders) to
   * make the element "stand out". The outline-color property specifies the
   * color of an outline.
   * </p>
   * 
   * <p>
   * This property takes a {@link RGBColor} object as value.
   * </p>
   * 
   * <h3>Example:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.OUTLINE_COLOR, RGBColor.GRAY);
   * </pre>
   */
  public static OutlineColorProperty OUTLINE_COLOR;

  /**
   * <p>
   * An outline is a line that is drawn around elements (outside the borders) to
   * make the element "stand out". The outline-color property specifies the
   * style of an outline.
   * </p>
   * <p>
   * This property takes a {@link LineStyle} object as value.
   * </p>
   * 
   * <h3>Example:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.OUTLINE_STYLE, LineStyle.DOTTED);
   * </pre>
   */
  public static OutlineStyleProperty OUTLINE_STYLE;

  /**
   * <p>
   * An outline is a line that is drawn around elements (outside the borders) to
   * make the element "stand out". The outline-width specifies the width of an
   * outline
   * </p>
   * <p>
   * This property takes a {@link LineWidth} object as value.
   * </p>
   * 
   * <h3>Example:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.OUTLINE_WIDTH, LineWidth.MEDIUM);
   * </pre>
   * 
   */
  public static OutlineWidthProperty OUTLINE_WIDTH;

  /**
   * <p>
   * The <i>overflow</i> property specifies whether the content of a block-level
   * element is clipped when it overflows the element's box (which is acting as
   * a containing block for the content).
   * </p>
   * 
   * <p>
   * This property takes a {@link Overflow} object as value.
   * </p>
   * 
   * <h3>Example:</h3>
   * 
   * <pre class="code">
  * $("#myId").css(CSS.OVERFLOW, Overflow.HIDDEN);
  * </pre>
   */
  public static OverflowProperty OVERFLOW;

  /**
   * <p>
   * The <i>padding</i> property is a shorthand property for setting
   * <i>padding-top</i>, <i>padding-right</i>, <i>padding-bottom</i>, and
   * <i>padding-left</i> at the same place in the style sheet.
   * </p>
   * 
   * <p>
   * If there is only one component value, it applies to all sides. If there are
   * two values, the top and bottom paddings are set to the first value and the
   * right and left paddings are set to the second. If there are three values,
   * the top is set to the first value, the left and right are set to the
   * second, and the bottom is set to the third. If there are four values, they
   * apply to the top, right, bottom, and left, respectively.
   * </p>
   * 
   * <h3>Example:</h3>
   * 
   * <pre class="code">
   * // set padding-top and padding-bottom to 10px and padding-left and padding-right to 20px
   * $("#myId").css(CSS.PADDING, Length.px(10), Length.px(20), null, null);
   * // set padding-top to 10px, padding-right to 20px and padding-bottom to 30px and padding-right to 40px
   * $("#myId").css(CSS.PADDING, Length.px(10), Length.px(20), Length.px(30), Length.px(40));
   * </pre>
   */
  public static ShorthandPaddingProperty PADDING;

  /**
   * <p>
   * The <i>padding-bottom</i> property specifies the width of the padding area
   * of the bottom of a box
   * </p>
   * 
   * <p>
   * This property takes a {@link Length} object as value.
   * </p>
   * 
   * <h3>Example:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.PADDING_BOTTOM, Length.px(30));
   * </pre>
   */
  public static PaddingProperty PADDING_BOTTOM;

  /**
   * <p>
   * The <i>padding-left</i> property specifies the width of the padding area of
   * the left side of a box
   * </p>
   * 
   * <p>
   * This property takes a {@link Length} object as value.
   * </p>
   * 
   * <h3>Example:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.PADDING_LEFT, Length.px(30));
   * </pre>
   */
  public static PaddingProperty PADDING_LEFT;

  /**
   * <p>
   * The <i>padding-right</i> property specifies the width of the padding area
   * of the right side of a box
   * </p>
   * 
   * <p>
   * This property takes a {@link Length} object as value.
   * </p>
   * 
   * <h3>Example:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.PADDING_RIGHT, Length.px(30));
   * </pre>
   */
  public static PaddingProperty PADDING_RIGHT;

  /**
   * <p>
   * The <i>padding-top</i> property specifies the width of the padding area of
   * the top of a box
   * </p>
   * 
   * <p>
   * This property takes a {@link Length} object as value.
   * </p>
   * 
   * <h3>Example:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.PADDING_TOP, Length.px(30));
   * </pre>
   */
  public static PaddingProperty PADDING_TOP;

  /**
   * <p>
   * The <i>position</i> property determines which of the CSS2 positioning
   * algorithms is used to calculate the position of a box.
   * </p>
   * 
   * <p>
   * This property takes a {@link Position} object as value.
   * </p>
   * 
   * <h3>Example:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.POSITION, Position.ABSOLUTE);
   * </pre>
   */
  public static PositionProperty POSITION;

  /**
   * <p>
   * This property specifies how far a box's right content edge is offset to the
   * left of the right edge of the box's containing block.
   * </p>
   * <p>
   * For absolutely positioned elements, the right property sets the right edge
   * of an element to a unit to the left/right of the right edge of its
   * containing element.
   * </p>
   * <p>
   * For relatively positioned elements, the right property sets the right edge
   * of an element to a unit to the left/right to its normal position.
   * </p>
   * <p>
   * For static positioned elements, the right property has no effect.
   * </p>
   * 
   * <p>
   * This property takes a {@link Length} object as value.
   * </p>
   * 
   * <h3>Example:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.RIGHT,Length.px(20));
   * </pre>
   */
  public static EdgePositionProperty RIGHT;

  /**
   * <p>
   * The <i>text-align</i> property describes how inline-level content of a
   * block container is aligned.
   * </p>
   * 
   * <p>
   * This property takes a {@link TextAlign} object as value.
   * </p>
   * 
   * <h3>Example:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.TEXT_ALIGN, TextAlign.CENTER);
   * </pre>
   */
  public static TextAlignProperty TEXT_ALIGN;

  /**
   * <p>
   * The <i>text-decoration</i> property describes decorations that are added to
   * the text of an element using the element's color. When specified on or
   * propagated to an inline element, it affects all the boxes generated by that
   * element, and is further propagated to any in-flow block-level boxes that
   * split the inline. For block containers that establish an inline formatting
   * context, the decorations are propagated to an anonymous inline element that
   * wraps all the in-flow inline-level children of the block container. For all
   * other elements it is propagated to any in-flow children. Note that text
   * decorations are not propagated to floating and absolutely positioned
   * descendants, nor to the contents of atomic inline-level descendants such as
   * inline blocks and inline tables.
   * </p>
   * <p>
   * Underlines, overlines, and line-throughs are applied only to text
   * (including white space, letter spacing, and word spacing): margins,
   * borders, and padding are skipped. User agents must not render these text
   * decorations on content that is not text. For example, images and inline
   * blocks must not be underlined.
   * </p>
   * 
   * <p>
   * This property takes a {@link TextDecoration} object as value.
   * </p>
   * 
   * <h3>Example:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.TEXT_DECORATION, TextDecoration.LINE_THROUGH);
   * </pre>
   */
  public static TextDecorationProperty TEXT_DECORATION;

  /**
   * <p>
   * The <i>text-ident</i> property specifies the indentation of the first line
   * of text in a block container. More precisely, it specifies the indentation
   * of the first box that flows into the block's first line box. The box is
   * indented with respect to the left (or right, for right-to-left layout) edge
   * of the line box. User agents must render this indentation as blank space.
   * 
   * </p>
   * <p>
   * <i>Text-ident</i> only affects a line if it is the first formatted line of
   * an element. For example, the first line of an anonymous block box is only
   * affected if it is the first child of its parent element.
   * </p>
   * 
   * <p>
   * This property takes a {@link Length} object as value.
   * </p>
   * 
   * <h3>Example:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.TEXT_IDENT, Length.px(15));
   * </pre>
   */
  public static TextIdentProperty TEXT_IDENT;

  /**
   * <p>
   * The <i>text-transform</i> property controls capitalization effects of an
   * element's text.
   * </p>
   * <p>
   * This property takes a {@link TextTransform} object as value.
   * </p>
   * 
   * <h3>Example:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.TEXT_TRANSFORM, TextTransform.UPPERCASE);
   * </pre>
   */
  public static TextTransformProperty TEXT_TRANSFORM;

  /**
   * <p>
   * This property specifies how far a box's top content edge is offset below
   * the top edge of the box's containing block.
   * </p>
   * <p>
   * For absolutely positioned elements, the top property sets the top edge of
   * an element to a unit above/below the top edge of its containing element.
   * </p>
   * <p>
   * For relatively positioned elements, the top property sets the top edge of
   * an element to a unit above/below its normal position.
   * </p>
   * <p>
   * For static positioned elements, the top property has no effect.
   * </p>
   * 
   * <p>
   * This property takes a {@link Length} object as value.
   * </p>
   * 
   * <h3>Example:</h3>
   * 
   * <pre class="code">
  * $("#myId").css(CSS.TOP,Length.px(20));
  * </pre>
   */
  public static EdgePositionProperty TOP;

  /**
   * <p>
   * The <i>vertical-align</i> property affects the vertical positioning inside
   * a line box of the boxes generated by an inline-level element.
   * </p>
   * 
   * <p>
   * This property takes a {@link VerticalAlign} object as value.
   * </p>
   * 
   * <h3>Example:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.VERTICAL_ALIGN, VerticalAlign.BOTTOM);
   * </pre>
   */
  public static VerticalAlignProperty VERTICAL_ALIGN;

  /**
   * <p>
   * The <i>visibility</i> property specifies whether the boxes generated by an
   * element are rendered. Invisible boxes still affect layout (set the
   * <i>display<i> property to <i>none</i> to suppress box generation
   * altogether).
   * </p>
   * 
   * <p>
   * This property takes a {@link Visibility} object as value.
   * </p>
   * 
   * <h3>Example:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.VISIBILITY, Visibility.HIDDEN);
   * </pre>
   */
  public static VisibilityProperty VISIBILITY;

  /**
   * <p>
   * The <i>white-space</i> property declares how white space inside the element
   * is handled
   * </p>
   * <p>
   * Newlines in the source can be represented by a carriage return (U+000D), a
   * linefeed (U+000A) or both (U+000D U+000A) or by some other mechanism that
   * identifies the beginning and end of document segments, such as the SGML
   * RECORD-START and RECORD-END tokens. The CSS 'white-space' processing model
   * assumes all newlines have been normalized to line feeds. UAs that recognize
   * other newline representations must apply the white space processing rules
   * as if this normalization has taken place. If no newline rules are specified
   * for the document language, each carriage return (U+000D) and CRLF sequence
   * (U+000D U+000A) in the document text is treated as single line feed
   * character. This default normalization rule also applies to generated
   * content.
   * </p>
   * <p>
   * This property takes a {@link WhiteSpace} object as value.
   * </p>
   * 
   * <h3>Example:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.WHITE_SPACE, WhiteSpace.NOWRAP);
   * </pre>
   */
  public static WhiteSpaceProperty WHITE_SPACE;

  /**
   * <p>
   * This property specifies the content width of boxes generated by block-level
   * and replaced elements.
   * </p>
   * <p>
   * This property does not apply to non-replaced inline-level elements. The
   * content width of a non-replaced inline element's boxes is that of the
   * rendered content within them (before any relative offset of children).
   * Recall that inline boxes flow into line boxes. The width of line boxes is
   * given by the their containing block, but may be shorted by the presence of
   * floats.
   * </p>
   * 
   * <p>
   * The width of a replaced element's box is intrinsic and may be scaled by the
   * user agent if the value of this property is different than <i>auto</i>.
   * </p>
   * <p>
   * This property takes a {@link Length} object as value.
   * </p>
   * 
   * <h3>Example:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.WIDTH, Length.px(100))
   * </pre>
   */
  public static WidthProperty WIDTH;

  /**
   * <p>
   * The <i>word-spacing</i> property specifies spacing behavior between words.
   * </p>
   * <p>
   * Word spacing algorithms are user agent-dependent. Word spacing is also
   * influenced by justification (see the <i>text-align</i> property). Word
   * spacing affects each space (U+0020) and non-breaking space (U+00A0), left
   * in the text after the white space processing rules have been applied. The
   * effect of the property on other word-separator characters is undefined.
   * However general punctuation, characters with zero advance width (such as
   * the zero with space U+200B) and fixed-width spaces (such as U+3000 and
   * U+2000 through U+200A) are not affected.
   * </p>
   * <p>
   * This property takes a {@link Length} object as value.
   * </p>
   * 
   * <h3>Example:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.WORD_SPACING, Length.pt(2));
   * </pre>
   */
  public static WordSpacingProperty WORD_SPACING;

  /**
   * <p>
   * For a positioned box, the i>z-index</i> property specifies:
   * <ul>
   * <li>The stack level of the box in the current stacking context.</li>
   * <li>Whether the box establishes a local stacking context.</li>
   * </ul>
   * 
   * </p>
   * 
   * <p>
   * This property takes a {@link CssNumber} object as value.
   * </p>
   * 
   * <h3>Example:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.ZINDEX, new CssNumber(1000));
   * </pre>
   */
  public static ZIndexProperty ZINDEX;

  static {
    BackgroundProperty.init();
    BorderProperty.init();
    BorderCollapseProperty.init();
    BorderSpacingProperty.init();
    CaptionSideProperty.init();
    ColorProperty.init();
    CursorProperty.init();
    ClearProperty.init();
    ClipProperty.init();
    DisplayProperty.init();
    EdgePositionProperty.init();
    EmptyCellsProperty.init();
    FloatProperty.init();
    FontStyleProperty.init();
    FontVariantProperty.init();
    FontWeightProperty.init();
    FontSizeProperty.init();
    HeightProperty.init();
    LetterSpacingProperty.init();
    LineHeightProperty.init();
    ListStyleProperty.init();
    MarginProperty.init();
    OutlineProperty.init();
    OverflowProperty.init();
    PaddingProperty.init();
    PositionProperty.init();
    TextAlignProperty.init();
    TextDecorationProperty.init();
    TextIdentProperty.init();
    TextTransformProperty.init();
    UnicodeBidiProperty.init();
    VerticalAlignProperty.init();
    VisibilityProperty.init();
    WidthProperty.init();
    WhiteSpaceProperty.init();
    WordSpacingProperty.init();
    ZIndexProperty.init();

  }

}