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

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.query.client.css.BackgroundAttachmentProperty.BackgroundAttachment;
import com.google.gwt.query.client.css.BackgroundPositionProperty.BackgroundPosition;
import com.google.gwt.query.client.css.BackgroundRepeatProperty.BackgroundRepeat;
import com.google.gwt.query.client.css.BorderCollapseProperty.BorderCollapse;
import com.google.gwt.query.client.css.BorderSpacingProperty.BorderSpacing;
import com.google.gwt.query.client.css.BorderStyleProperty.LineStyle;
import com.google.gwt.query.client.css.BorderWidthProperty.LineWidth;
import com.google.gwt.query.client.css.CaptionSideProperty.CaptionSide;
import com.google.gwt.query.client.css.ClearProperty.Clear;
import com.google.gwt.query.client.css.EmptyCellsProperty.EmptyCells;

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
   * initial position. Values have the following meanings:
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
   * $("#myId2").css(CSS.BACKGROUND_POSITION, BackgroundPosition.position(25, 25, Unit.PCT));
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
   * $("#myId").css(CSS.BORDER, LineWidth.THICK, LineStyle.DASHED, RGBColor.BLACK);
   * $("#myId2").css(CSS.BORDER, LineWidth.length(3, Unit.PX), LineStyle.SOLID, RGBColor.rgb("#000000"));
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
   * $("#myId").css(CSS.BORDER_BOTTOM, LineWidth.THICK, LineStyle.DASHED, RGBColor.BLACK);
   * $("#myId2").css(CSS.BORDER_BOTTOM, LineWidth.length(3, Unit.PX), LineStyle.SOLID, RGBColor.rgb("#000000"));
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
   * $("#myId").css(CSS.BORDER_RIGHT, LineWidth.THICK, LineStyle.DASHED, RGBColor.BLACK);
   * $("#myId2").css(CSS.BORDER_RIGHT, LineWidth.length(3, Unit.PX), LineStyle.SOLID, RGBColor.rgb("#000000"));
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
   * The clip property lets you specify the dimensions of an absolutely
   * positioned element that should be visible, and the element is clipped into
   * this shape, and displayed.
   * 
   * The clip property does not work if the overflow property is set to visible.
   */
  public static ClipProperty CLIP;

  /**
   * This property describes the foreground color of an element's text content.
   */
  public static ColorProperty COLOR;

  /**
   * This property specifies the type of cursor to be displayed for the pointing
   * device.
   */
  public static CursorProperty CURSOR;

  public static DirectionProperty DIRECTION;

  /**
   * This property specifies the mechanism by which elements are rendered.
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
   * 
   * When this property has the value 'show', borders are drawn around empty
   * cells (like normal cells).
   * 
   * A value of 'hide' means that no borders are drawn around empty cells.
   * Furthermore, if all the cells in a row have a value of 'hide' and have no
   * visible content, the entire row behaves as if it had 'display: none'.
   * 
   * 
   * </p>
   * 
   * <p>
   * This property takes a {@link EmptyCells} object as value.
   * </p>
   * 
   * <h3>Example:</h3>
   * 
   * <pre class="code">
   * $("#myId").css(CSS.EMPTY_CELLS, EmptyCells.HIDE);
   * </pre>
   */
  public static EmptyCellsProperty EMPTY_CELLS;

  /**
   * <p>
   * The <i>float</i> property specifies whether a box should float to the left,
   * right, or not at all. It may be set for elements that generate boxes that
   * are not absolutely positioned. The values of this property have the
   * following meanings:
   * 
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

  public static FontSizeProperty FONT_SIZE;

  public static FontStyleProperty FONT_STYLE;

  public static FontVariantProperty FONT_VARIANT;

  public static FontWeightProperty FONT_WEIGHT;

  /**
   * This property specifies the content height of boxes generated by
   * block-level, inline-block and replaced elements.
   * 
   * This property does not apply to non-replaced inline-level elements. See the
   * section on computing heights and margins for non-replaced inline elements
   * for the rules used instead.
   */
  public static HeightProperty HEIGHT;

  public static final String INHERIT_VALUE = "inherit";

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

  public static LetterSpacingProperty LETTER_SPACING;

  public static LineHeightProperty LINE_HEIGHT;

  public static ListStyleProperty LIST_STYLE;

  public static ListStyleImageProperty LIST_STYLE_IMAGE;

  public static ListStylePositionProperty LIST_STYLE_POSITION;

  public static ListStyleTypeProperty LIST_STYLE_TYPE;

  public static MarginProperty MARGIN;

  public static MarginProperty MARGIN_BOTTOM;

  public static MarginProperty MARGIN_LEFT;

  public static MarginProperty MARGIN_RIGHT;

  public static MarginProperty MARGIN_TOP;

  public static HeightProperty MAX_HEIGHT;

  public static WidthProperty MAX_WIDTH;

  public static HeightProperty MIN_HEIGHT;

  public static WidthProperty MIN_WIDTH;

  /**
   * An outline is a line that is drawn around elements (outside the borders) to
   * make the element "stand out".
   * 
   * The outline shorthand property sets all the outline properties in one
   * declaration.
   */
  public static OutlineProperty OUTLINE;

  /**
   * An outline is a line that is drawn around elements (outside the borders) to
   * make the element "stand out". The outline-color property specifies the
   * color of an outline.
   */
  public static OutlineColorProperty OUTLINE_COLOR;

  /**
   * An outline is a line that is drawn around elements (outside the borders) to
   * make the element "stand out". The outline-color property specifies the
   * style of an outline.
   */
  public static OutlineStyleProperty OUTLINE_STYLE;

  /**
   * An outline is a line that is drawn around elements (outside the borders) to
   * make the element "stand out". The outline-width specifies the width of an
   * outline
   */
  public static OutlineWidthProperty OUTLINE_WIDTH;

  /**
   * This property specifies what happens if content overflows an element's
   * box..
   */
  public static OverflowProperty OVERFLOW;

  public static PaddingProperty PADDING;

  public static PaddingProperty PADDING_BOTTOM;

  public static PaddingProperty PADDING_LEFT;

  public static PaddingProperty PADDING_RIGHT;

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
   * This property describes how inline content of a block is aligned.
   */
  public static TextAlignProperty TEXT_ALIGN;

  public static TextDecorationProperty TEXT_DECORATION;

  public static TextIdentProperty TEXT_IDENT;

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
   * This property affects the vertical positioning inside a line box of the
   * boxes generated by an inline-level element.
   */
  public static VerticalAlignProperty VERTICAL_ALIGN;

  /**
   * The 'visibility' property specifies whether the boxes generated by an
   * element are rendered. Invisible boxes still affect layout (set the
   * 'display' property to 'none' to suppress box generation altogether).
   */
  public static VisibilityProperty VISIBILITY;

  public static WhiteSpaceProperty WHITE_SPACE;

  /**
   * This property specifies the content width of boxes generated by block-level
   * and replaced elements.
   * 
   * This property does not apply to non-replaced inline-level elements. The
   * content width of a non-replaced inline element's boxes is that of the
   * rendered content within them (before any relative offset of children).
   * Recall that inline boxes flow into line boxes. The width of line boxes is
   * given by the their containing block, but may be shorted by the presence of
   * floats.
   * 
   * The width of a replaced element's box is intrinsic and may be scaled by the
   * user agent if the value of this property is different than 'auto'.
   */
  public static WidthProperty WIDTH;

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
    VerticalAlignProperty.init();
    VisibilityProperty.init();
    WidthProperty.init();
    WhiteSpaceProperty.init();
    WordSpacingProperty.init();
    ZIndexProperty.init();

  }

}
