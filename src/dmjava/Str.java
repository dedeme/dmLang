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

import java.io.UnsupportedEncodingException;


/**
 *
 * @version 1.0
 * @since 24-jul-2013
 * @author deme
 */
public class Str {

  private Str() {
  }

  /**
   * <p>Returns one substring of 's'. Includes character 'begin' and excludes
   * character 'end'. If 'begin < 0' or 'end < 0' they become 's.length()+begin'
   * or 's.length()+end'.</p> <p>Next rules are applied, in indicated order:</p>
  * <ol>
   * <li> If 'begin < 0' or 'end < 0' they become 's.length()+begin' or
   * 's.length()+end'.</li> <li> If 'begin < 0' it becomes '0'.</li> <li> If 'e
   * nd >
   * s.length()' it becomes 's.length()'.</li>
   * <li> If 'end <= begin' then returns a empty string.</li>
   * </ol>
   *
   * @param s String for extracting a substring.
   * @param begin Position of first character, inclusive.
   * @param end Position of last character, exclusive.
   * @return A substring of 's'
   */
  public static String sub(String s, int begin, int end) {
    int lg = s.length();
    if (begin < 0) {
      begin += lg;
    }
    if (end < 0) {
      end += lg;
    }
    if (begin < 0) {
      begin = 0;
    }
    if (end > lg) {
      end = lg;
    }
    if (end <= begin) {
      return "";
    }
    return s.substring(begin, end);
  }

  /**
   * Does sub(s, begin, s.length())
   * @param s
   * @param begin
   * @return
   */
  public static String sub(String s, int begin) {
    return sub(s, begin, s.length());
  }

  /**
   * Removes extern spaces and convert duplicate internal spaces to single
   * spaces.
   * @param s
   * @return
   */
  public static String intrim(String s) {
    s = s.trim();
    while (s.contains(" ")) {
      s = s.replaceAll(" ", " ");
    }
    return s;
  }

  /**
   * Returns string resulting of repeating 's' 'times' times.
   * @param s
   * @param times
   * @return
   */
  public static String replicate(String s, int times) {
    StringBuilder r = new StringBuilder();
    for (int i = 0; i < times; i++) {
      r.append(s);
    }
    return r.toString();
  }

  /**
   * Returns string resulting of repeating 'c' 'times' times.
   * @param c
   * @param times
   * @return
   */
  public static String replicate(char c, int times) {
    StringBuilder r = new StringBuilder();
    for (int i = 0; i < times; i++) {
      r.append(c);
    }
    return r.toString();
  }

  /**
   * Returns string resulting of repeating one space 'times' times.
   * @param times
   * @return
   */
  public static String replicate(int times) {
    return replicate(' ', times);
  }

  /**
   * Justifies s to left in a width of 'n' characters. If 's.length() > n',
   * trucates them.
   * @param s
   * @param n
   * @return
   */
  public static String ljust(String s, int n) {
    if (s.length() >= n) {
      return s.substring(0, n);
    }

    return String.format("%-" + n + "s", s);
  }

  /**
   * Justifies s to right in a width of 'n' characters. If 's.length() > n',
   * trucates them.
   * @param s
   * @param n
   * @return
   */
  public static String rjust(String s, int n) {
    if (s.length() >= n) {
      return s.substring(0, n);
    }

    return String.format("%" + n + "s", s);
  }

  /**
   * Justifies s to center in a width of 'n' characters. If 's.length() > n',
   * trucates them.
   * @param s
   * @param n
   * @return
   */
  public static String cjust(String s, int n) {
    if (s.length() >= n) {
      return s.substring(0, n);
    }


    int l = (n - s.length()) / 2;
    s = String.format("%-" + l + "s", s);
    return String.format("%" + n + "s", s);
  }

  /**
   * Returns 's' with its first character capitalizes
   * @param s
   * @return
   */
  public static String capitalize(String s) {
    if (s.equals("")) {
      return s;
    }

    return s.substring(0, 1).toUpperCase() + s.substring(1);
  }

  /**
   * Returns 's' splited and trimed. 'separator' is a regular expresion.
   */
  /*
  public static It<String> splitTrim(String text, String separator) {
    return It.from(text.split(separator)).map(
      new Func.F1<String, String>() {
      @Override
      public String run(String l) {
        return l.trim();
      }
    });
  }
*/
  /**
   * Returns the underlaying string of [bs], using "UTF-8" encode.
   * @param bs
   * @return
   */
  public static String from(byte[] bs) {
    try {
      return new String(bs, "UTF-8");
    } catch (UnsupportedEncodingException ex) {
      throw new IllegalArgumentException(ex);
    }
  }

  /**
   * Converts s using "UTF-8" encode.
   * @param s
   * @return
   */
  public static byte[] toBytes(String s) {
    try {
      return s.getBytes("UTF-8");
    } catch (UnsupportedEncodingException ex) {
      throw new IllegalArgumentException(ex);
    }
  }
}