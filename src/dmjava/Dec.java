/*
 * Copyright 05-abr-2014 ºDeme
 *
 * This file is part of 'dmLang-8.1.0'.
 *
 * 'dmLang-8.1.0' is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License.
 *
 * 'dmLang-8.1.0' is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with 'dmLang-8.1.0'.  If not, see <http://www.gnu.org/licenses/>.
 */
package dmjava;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * <p>
 * Static functions for handling BigDecimals.</p>
 * <p>
 * Every Constructor uses 'RoundingMode.HALF_UP' for round.</p>
 * <p>
 * Attention!:<br>
 * 'equals' returns 'true' if values and scale are equals.<br>
 * 'compareTo' returns '0' only if values are equals.
 * </p>
 *
 * @version 1.0
 * @since 26-jun-2012
 * @author deme
 */
public class Dec {

  private Dec() {
  }

  /**
   * Constructor with 'value=0' and 'scale=0'.
   *
   * @return mk(0)
   */
  public static BigDecimal mk() {
    return mk(0);
  }

  /**
   * Constructor
   *
   * @param n
   * @param scale Number of decimal positions.
   * @return n.setScale(scale, RoundingMode.HALF_UP)
   */
  public static BigDecimal mk(BigDecimal n, int scale) {
    return n.setScale(scale, RoundingMode.HALF_UP);
  }

  /**
   * Constructor
   *
   * @param n
   * @return n.setScale(n.scale(), RoundingMode.HALF_UP)
   */
  public static BigDecimal mk(BigDecimal n) {
    return n.setScale(n.scale(), RoundingMode.HALF_UP);
  }

  /**
   * Constructor from string in "base" mode (the format that give formatBase())
   * and 'scale=0'.
   *
   * @param n String with a valid number.
   * @return mk(n, 0)
   */
  public static BigDecimal mk(String n) {
    return mk(n, 0);
  }

  /**
   * Constructor from string in "base" mode (the format that give formatBase()).
   *
   * @param n String with a valid number.
   * @param scale Number of decimal positions.
   * @return BigDecimal(n).setScale(scale, RoundingMode.HALF_UP). If 'n' is not
   * a valid number throw a message on console and returns null.
   */
  public static BigDecimal mk(String n, int scale) {
    try {
      return new BigDecimal(n).setScale(scale, RoundingMode.HALF_UP);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Constructor from string in 'lc' format and 'scale=0'.
   *
   * @param n String with a valid number.
   * @param lc Locale for reading 'n'
   * @return return mk(n, 0, lc)
   */
  public static BigDecimal mk(String n, Locale lc) {
    return mk(n, 0, lc);
  }

  /**
   * Constructor from string in 'lc' format
   *
   * @param n String with a valid number.
   * @param scale Number of decimal positions.
   * @param lc Locale for reading 'n'
   * @return New BigDecimal read in 'lc'. If 'n' is not a valid number throw a
   * message on console and returns null.
   */
  public static BigDecimal mk(String n, int scale, Locale lc) {
    try {
      return new BigDecimal(normalize(n, lc)).setScale(
        scale, RoundingMode.HALF_UP
      );
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Constructor from double and 'scale=0'.
   *
   * @param n
   * @return BigDecimal.valueOf(n).setScale(0, RoundingMode.HALF_UP)
   */
  public static BigDecimal mk(double n) {
    return BigDecimal.valueOf(n).setScale(0, RoundingMode.HALF_UP);
  }

  /**
   * Constructor
   *
   * @param n
   * @param scale Number of decimal positions.
   * @return BigDecimal.valueOf(n).setScale(scale, RoundingMode.HALF_UP)
   */
  public static BigDecimal mk(double n, int scale) {
    return BigDecimal.valueOf(n).setScale(scale, RoundingMode.HALF_UP);
  }

  static String removeZeros(String n) {
    while (n.endsWith("0")) {
      n = Str.sub(n, 0, -1);
    }
    if (n.endsWith(".")) {
      n = Str.sub(n, 0, -1);
    }
    return (n.equals("")) ? "0" : n;
  }

  /**
   * Returns 'n' in 'default locale' format
   *
   * @param n
   * @return 'n' with 'n.scale()' decimal positions.
   */
  public static String formatFix(BigDecimal n) {
    return String.format("%,." + n.scale() + "f", n);
  }

  /**
   * Returns 'n' in 'locale' format
   *
   * @param n
   * @param lc Locale for writing 'n'
   * @return 'n' with 'n.scale()' decimal positions and in 'lc'.
   */
  public static String formatFix(BigDecimal n, Locale lc) {
    return String.format(lc, "%,." + n.scale() + "f", n);
  }

  /**
   * Returns 'n' in 'default locale' format
   *
   * @param n
   * @return 'n' with 'n.scale()' decimal positions and zeros trimmed.
   */
  public static String format(BigDecimal n) {
    return removeZeros(String.format("%,." + n.scale() + "f", n));
  }

  /**
   * Returns 'n' in 'locale' format
   *
   * @param n
   * @param lc Locale for writing 'n'
   * @return 'n' with 'n.scale()' decimal positions, in 'lc' and zeros trimmed.
   */
  public static String format(BigDecimal n, Locale lc) {
    return removeZeros(String.format(lc, "%,." + n.scale() + "f", n));
  }

  /**
   * Returns 'n' only with the decimal separator '.'
   *
   * @param n
   * @return 'n' only with the decimal separator '.'
   */
  public static String formatBase(BigDecimal n) {
    return removeZeros(
      String.format("%." + n.scale() + "f", n
      )).replace(",", ".");
  }

  /**
   * Returns value in double
   *
   * @param d
   * @return
   */
  public static double value(BigDecimal d) {
    return d.doubleValue();
  }

  final public static Locale US = Locale.ENGLISH;
  final public static Locale EU = Locale.ITALIAN;

  static String normalize(String n, Locale lc) {
    DecimalFormatSymbols fs = new DecimalFormatSymbols(lc);
    String gsp
      = String.valueOf(fs.getGroupingSeparator());
    String sp
      = String.valueOf(fs.getDecimalSeparator());

    return (sp.equals("."))
      ? n.replace(gsp, "")
      : n.replace(gsp, "").replace(sp, ".");
  }

  /**
   * Indicate if 'n' contains a valid number in locale format
   *
   * @param n
   * @return true if 'n' is a valid number.
   */
  public static boolean isNumber(String n) {
    try {
      BigDecimal bigDecimal = new BigDecimal(n);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Indicate if 'n' contains a valid number in 'nf' format. You can use
   * 'Dec.US' and 'Dec.EU'.
   *
   * @param n
   * @param lc Locale for reading 'n'
   * @return true if 'n' is a valid number.
   */
  public static boolean isNumber(String n, Locale lc) {
    return isNumber(normalize(n, lc));
  }

}
