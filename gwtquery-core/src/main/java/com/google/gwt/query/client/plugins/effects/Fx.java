package com.google.gwt.query.client.plugins.effects;

import com.google.gwt.dom.client.Element;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.css.BorderColorProperty;
import com.google.gwt.query.client.css.CSS;
import com.google.gwt.query.client.css.RGBColor;
import com.google.gwt.query.client.js.JsNamedArray;
import com.google.gwt.query.client.js.JsObjectArray;
import com.google.gwt.query.client.js.JsRegexp;

/**
 * A pojo to store effect values.
 */
public class Fx {
  
  /**
   * A pojo to store color effect values.
   */
  public static class ColorFx extends Fx {
    
    /**
     * Specific class handle specific borderColor shortcut properties
     */
    public static class BorderColorFx extends ColorFx {

      private static BorderColorProperty[] borderColorProperties = {
          CSS.BORDER_BOTTOM_COLOR, CSS.BORDER_TOP_COLOR, CSS.BORDER_LEFT_COLOR,
          CSS.BORDER_RIGHT_COLOR};
      private JsNamedArray<int[]> startColors;

      public BorderColorFx(Element e, String endColorString) {

        endColor = parseColor(endColorString);
        startColors = JsNamedArray.create();

        GQuery $e = GQuery.$(e);

        for (BorderColorProperty border : borderColorProperties) {
          int[] startColor = parseColor($e.css(border, true));
          startColors.put(border.getCssName(), startColor);
        }
      }

      @Override
      public void applyValue(GQuery g, double progress) {
        for (BorderColorProperty border : borderColorProperties) {
          startColor = startColors.get(border.getCssName());
          cssprop = border.getCssName();
          super.applyValue(g, progress);
        }
      }
    }

    // hexadecimal regex
    public static JsRegexp REGEX_HEX_COLOR_PATTERN = new JsRegexp(
        "#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})");
    
    private static JsNamedArray<int[]> htmlColorToRgb;

    // rgb and rgba regex
    public static JsRegexp REGEX_RGB_COLOR_PATTERN = new JsRegexp(
        "rgba?\\(\\s*([0-9]{1,3}%?)\\s*,\\s*([0-9]{1,3}%?)\\s*,\\s*([0-9]{1,3}%?).*\\)$");

    static {
      htmlColorToRgb = JsNamedArray.create();
      htmlColorToRgb.put("white", new int[]{255, 255, 255});
      htmlColorToRgb.put("aqua", new int[]{0, 255, 255});
      htmlColorToRgb.put("azure", new int[]{240, 255, 255});
      htmlColorToRgb.put("beige", new int[]{245, 245, 220});
      htmlColorToRgb.put("black", new int[]{0, 0, 0});
      htmlColorToRgb.put("blue", new int[]{0, 0, 255});
      htmlColorToRgb.put("brown", new int[]{165, 42, 42});
      htmlColorToRgb.put("cyan", new int[]{0, 255, 255});
      htmlColorToRgb.put("darkblue", new int[]{0, 0, 139});
      htmlColorToRgb.put("darkcyan", new int[]{0, 139, 139});
      htmlColorToRgb.put("darkgrey", new int[]{169, 169, 169});
      htmlColorToRgb.put("darkgreen", new int[]{0, 100, 0});
      htmlColorToRgb.put("darkkhaki", new int[]{189, 183, 107});
      htmlColorToRgb.put("darkmagenta", new int[]{139, 0, 139});
      htmlColorToRgb.put("darkolivegreen", new int[]{85, 107, 47});
      htmlColorToRgb.put("darkorange", new int[]{255, 140, 0});
      htmlColorToRgb.put("darkorchid", new int[]{153, 50, 204});
      htmlColorToRgb.put("darkred", new int[]{139, 0, 0});
      htmlColorToRgb.put("darksalmon", new int[]{233, 150, 122});
      htmlColorToRgb.put("darkviolet", new int[]{148, 0, 211});
      htmlColorToRgb.put("fuchsia", new int[]{255, 0, 255});
      htmlColorToRgb.put("gold", new int[]{255, 215, 0});
      htmlColorToRgb.put("green", new int[]{0, 128, 0});
      htmlColorToRgb.put("indigo", new int[]{75, 0, 130});
      htmlColorToRgb.put("khaki", new int[]{240, 230, 140});
      htmlColorToRgb.put("lightblue", new int[]{173, 216, 230});
      htmlColorToRgb.put("lightcyan", new int[]{224, 255, 255});
      htmlColorToRgb.put("lightgreen", new int[]{144, 238, 144});
      htmlColorToRgb.put("lightgrey", new int[]{211, 211, 211});
      htmlColorToRgb.put("lightpink", new int[]{255, 182, 193});
      htmlColorToRgb.put("lightyellow", new int[]{255, 255, 224});
      htmlColorToRgb.put("lime", new int[]{0, 255, 0});
      htmlColorToRgb.put("magenta", new int[]{255, 0, 255});
      htmlColorToRgb.put("maroon", new int[]{128, 0, 0});
      htmlColorToRgb.put("navy", new int[]{0, 0, 128});
      htmlColorToRgb.put("olive", new int[]{128, 128, 0});
      htmlColorToRgb.put("orange", new int[]{255, 165, 0});
      htmlColorToRgb.put("pink", new int[]{255, 192, 203});
      htmlColorToRgb.put("purple", new int[]{128, 0, 128});
      htmlColorToRgb.put("violet", new int[]{128, 0, 128});
      htmlColorToRgb.put("red", new int[]{255, 0, 0});
      htmlColorToRgb.put("silver", new int[]{192, 192, 192});
      htmlColorToRgb.put("white", new int[]{255, 255, 255});
      htmlColorToRgb.put("yellow", new int[]{255, 255, 0});
    }

    protected int[] endColor;
    protected int[] startColor;

    ColorFx(String attr, String startColorString, String endColorString) {
      assert startColorString != null && endColorString != null;
      this.cssprop = attr;
      startColor = parseColor(startColorString);
      endColor = parseColor(endColorString);
    }

    private ColorFx() {
    }

    @Override
    public void applyValue(GQuery g, double progress) {
      int[] result = new int[3];
      for (int i = 0; i < 3; i++) {
        int composante = (int) Math.round(startColor[i] + progress
            * (endColor[i] - startColor[i]));
        result[i] = Math.max(0, Math.min(255, composante));
      }
      String value = RGBColor.rgb(result[0], result[1], result[2]).getCssName();

      g.css(cssprop, value);
    }

    public int[] getEndColor() {
      return endColor;
    }

    public int[] getStartColor() {
      return startColor;
    }

    protected int[] parseColor(String color) {
      JsObjectArray<String> matches = REGEX_RGB_COLOR_PATTERN.exec(color);
      if (matches != null) {
        return parseRGBColor(matches);
      }

      matches = REGEX_HEX_COLOR_PATTERN.exec(color);
      if (matches != null) {
        return parseHexColor(matches);
      }

      return parseLiteralColor(color);
    }

    private int[] parseHexColor(JsObjectArray<String> matches) {
      assert matches.length() == 2;
      int[] result = new int[3];

      String hexCode = matches.get(1);

      int step = (hexCode.length() == 3) ? 1 : 2;

      for (int i = 0; i < 3; i++) {
        String color = hexCode.substring(i * step, (i * step) + step);
        if (step == 1) {
          color += color;
        }
        result[i] = Math.max(0, Math.min(255, Integer.parseInt(color, 16)));

      }

      return result;
    }

    private int[] parseLiteralColor(String color) {
      return htmlColorToRgb.get(color);
    }

    private int[] parseRGBColor(JsObjectArray<String> matches) {
      assert matches.length() == 4;
      int[] result = new int[3];

      for (int i = 1; i < 4; i++) {
        String valueString = matches.get(i);
        int value = -1;
        if (valueString.endsWith("%")) {
          int percentage = Integer.parseInt(valueString.substring(0,
              valueString.length() - 1));
          value = Math.round((float) 2.55 * percentage);
        } else {
          value = Integer.parseInt(valueString);
        }
        result[i - 1] = Math.max(0, Math.min(255, value));
      }
      return result;
    }

  }
  
  public String cssprop;
  public double end;
  public double start;
  public String unit;
  public String value;
  public String attribute;

  Fx() {
    end = start = -1;
  }

  Fx(String attr, String value, double start, double end, String unit, String rkey) {
    this.cssprop = attr;
    this.value = value;
    this.start = start;
    this.end = end;
    this.unit = unit;
    this.attribute = rkey;
  }

  public void applyValue(GQuery g, double progress) {
    double ret = (start + ((end - start) * progress));
    String value = ("px".equals(unit) ? ((int) ret) : ret) + unit;
    
    if (attribute != null) {
      g.attr(attribute, value);
    } else {
      g.css(cssprop, value);
    }

  }

  public String toString() {
    return ("cssprop=" + cssprop + (attribute != null ? " attr=" + attribute : "") 
        + " value=" + value + " start=" + start + " end="
        + end + " unit=" + unit).replaceAll("\\.0([^\\d])", "$1");
  }
  
}
