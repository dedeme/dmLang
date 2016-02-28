/*
 * Copyright 07-abr-2014 ºDeme
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
package dmjava.net;

import static dmjava.Func.*;
import dmjava.It;
import java.io.UnsupportedEncodingException;

import java.net.URLEncoder;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @version 1.0
 * @since 07-Apr-2014
 * @author deme
 */
public class Json {

  String value;

  /**
   * Creates the element null.
   */
  public Json() {
    value = "null";
  }

  /**
   * Constructor from a boolean.
   *
   * @param b value.
   */
  public Json(boolean b) {
    if (b) {
      value = "true";
    } else {
      value = "false";
    }
  }

  /**
   * Constructor for a String.
   *
   * @param str value.
   */
  public Json(final String str) {
    if (str == null) {
      value = "null";
      return;
    }

    final StringBuilder sb = new StringBuilder().append('"');
    It.from(str).each(
      (Character ch) -> {
        switch (ch) {
          case '"':
            sb.append("\\\"");
            break;
          case '\\':
            sb.append("\\\\");
            break;
          case '\b':
            sb.append("\\b");
            break;
          case '\f':
            sb.append("\\f");
            break;
          case '\n':
            sb.append("\\n");
            break;
          case '\r':
            sb.append("\\r");
            break;
          case '\t':
            sb.append("\\t");
            break;
          default: {
            if (ch < 32 || ch > 127) {
              String hx = "000" + Integer.toHexString((int) ch);
              sb.append("\\u").append(hx.substring(hx.length() - 4));
            } else {
              sb.append(ch);
            }
          }
        }
      });
    value = sb.append('"').toString();
  }

  /**
   * Constructor from a double.
   *
   * @param n value.
   */
  public Json(double n) {
    value = String.valueOf(n);
  }

  /**
   * Constructor from an It of Jsons.
   *
   * @param i value.
   */
  public Json(It<Json> i) {
    if (i.hasNext()) {
      StringBuilder sb = new StringBuilder(i.next().toString());
      while (i.hasNext()) {
        sb.append(",").append(i.next());
      }
      value = "[" + sb.toString() + "]";
    } else {
      value = "[]";
    }
  }

  /**
   * Constructor from a map.
   *
   * @param mp value.
   */
  public Json(Map<String, Json> mp) {
    if (mp == null) {
      value = "null";
      return;
    }

    value = fromTpItAux(It.from(mp));
  }

  public boolean getBoolean() {
    switch (value) {
      case "true":
        return true;
      case "false":
        return false;
      default:
        throw new IllegalArgumentException(
          "Boolean_value_is_not_true_or_false");
    }
  }

  public String getString() {
    if (value.equals("null")) {
      return null;
    }

    Reader rd = new Reader(value + "|");
    try {
      rd.readString();
      if (rd.peek == '|') {
        return rd.bf.toString();
      } else {
        throw new IllegalArgumentException(String.format(
          "'%s'_bad_string (%d)", value, rd.pos));
      }
    } catch (EofException e) {
      throw new IllegalArgumentException(String.format(
        "'%s'_quotes_not_closed (%d)", value, rd.pos));
    }
  }

  public Double getNumber() {
    if (value.equals("null")) {
      return null;
    }

    Reader rd = new Reader(value + "|");
    try {
      rd.readNumber();
      if (rd.peek == '|') {
        try {
          return new Double(value);
        } catch (NumberFormatException e2) {
          throw new IllegalArgumentException(String.format(
            "'%s'_bad_number (%d)", value, rd.pos));
        }
      } else {
        throw new IllegalArgumentException(String.format(
          "'%s'_bad_number (%d)", value, rd.pos));
      }
    } catch (EofException e) {
      throw new IllegalArgumentException(String.format(
        "'%s'_bad_number (%d)", value, rd.pos));
    }
  }

  public ArrayList<Json> getArray() {
    if (value.equals("null")) {
      return null;
    }

    try {
      ArrayList<Json> r = new ArrayList<>();
      Reader rd = new Reader(value.substring(1));
      if (value.charAt(0) != '[') {
        throw new IllegalArgumentException(String.format(
          "'%s'_array_without_'[' (%d)", value, rd.pos));
      }
      rd.skipBlanks();
      if (rd.peek == ']') {
        return r;
      }

      while (true) {
        r.add(Json.fromJs(rd.readValue()));
        if (rd.peek == ']') {
          break;
        } else if (rd.peek == ',') {
          rd.next();
          rd.skipBlanks();
        } else {
          throw new IllegalArgumentException(String.format(
            "'%s'_bad_array (%d)", value, rd.pos));
        }
      }

      return r;
    } catch (EofException e) {
      throw new IllegalArgumentException(String.format(
        "'%s'_array_without_closing (%d)", value, value.length()));
    }
  }

  public HashMap<String, Json> getMap() {
    if (value.equals("null")) {
      return null;
    }

    try {
      HashMap<String, Json> r = new HashMap<>();
      Reader rd = new Reader(value.substring(1));
      if (value.charAt(0) != '{') {
        throw new IllegalArgumentException(String.format(
          "'%s'_object_without_'{' (%d)", value, rd.pos));
      }
      rd.skipBlanks();
      if (rd.peek == '}') {
        return r;
      }

      while (true) {
        rd.readString();
        String key = rd.bf.toString();
        if (rd.peek != ':') {
          throw new IllegalArgumentException(
            String.format(
              "'%s'_':'_is_missing_in_object_definition (%d)", key, rd.pos));
        }
        rd.next();
        rd.skipBlanks();
        String vl = rd.readValue();
        r.put(key, Json.fromJs(vl));
        if (rd.peek == '}') {
          break;
        } else if (rd.peek == ',') {
          rd.next();
          rd.skipBlanks();
        } else {
          throw new IllegalArgumentException(String.format(
            "'%s'_bad_object (%d)", value, rd.pos));
        }

      }

      return r;
    } catch (EofException e) {
      throw new IllegalArgumentException(String.format(
        "'%s'_object_without_closing (%d)", value, value.length()));
    }
  }

  /**
   * Returns json string encoded for AJAX
   *
   * @param charset e.g. UTF-8 or ISO-8859-1
   * @return
   */
  public String encode(String charset) {
    try {
      return URLEncoder.encode(value, charset).replace("+", "%20");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Returns json string encoded for AJAX encoded in "UTF-8"
   *
   * @return
   */
  public String encode() {
    return encode("UTF-8");
  }

  /**
   * Returns an object of type null, boolean, double, string, array[json] or map
   * [string, json]
   *
   * @return
   */
  public Object getObject() {
    String v = value.trim();

    switch (v) {
      case "null":
        return null;
      case "true":
        return true;
      case "false":
        return false;
      default:
        switch (v.charAt(0)) {
          case '"':
            return getString();
          case '[':
            return getArray();
          case '{':
            return getMap();
          default:
            return getNumber();
        }
    }
  }

  @Override
  public int hashCode() {
    return value.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Json other = (Json) obj;
    return Objects.equals(this.value, other.value);
  }

  /**
   * Returns a json string. If this value is going to be passed to an ajax
   * server, is better to use encode()
   *
   * @return
   */
  @Override
  public String toString() {
    return value;
  }

  static String fromTpItAux(It<Tp2<String, Json>> i) {
    if (i.hasNext()) {
      StringBuilder sb = new StringBuilder();
      Tp2<String, Json> tp = i.next();
      sb.append(new Json(tp._1()).toString());
      sb.append(":");
      sb.append(tp._2().toString());
      while (i.hasNext()) {
        tp = i.next();
        sb.append(",");
        sb.append(new Json(tp._1()).toString());
        sb.append(":");
        sb.append(tp._2().toString());
      }
      return "{" + sb.toString() + "}";
    } else {
      return "{}";
    }
  }

  /**
   * Utility for an It gotten from a map.
   *
   * @param i
   * @return
   */
  public static Json fromTpIt(It<Tp2<String, Json>> i) {
    Json r = new Json();
    r.value = fromTpItAux(i);
    return r;
  }

  /**
   * Returns a Json from a json string
   *
   * @param js A json string.
   * @return The corresponding Json object.
   */
  public static Json fromJs(String js) {
    Json r = new Json();
    r.value = js.trim();
    return r;
  }

  /**
   * Returns a Json from a urlencoded json string. Used to read server
   * responses.
   *
   * @param js A json string.
   * @param charset e.g. UTF-8 or ISO-8859-1
   * @return The corresponding Json object.
   */
  public static Json decode(String js, String charset) {
    try {
      Json r = new Json();
      r.value = URLDecoder.decode(js, charset).trim();
      return r;
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Equals to decode (js, "UTF-8").
   *
   * @param js
   * @return
   */
  public static Json decode(String js) {
    return decode(js, "UTF-8");
  }

  /**
   * Entry for JMap
   */
  public static class JEntry extends Tp2<String, Json> {

    public JEntry(String key, Json value) {
      super(key, value);
    }

    /**
     * Alias for _1()
     * @return
     */
    public String key() {
      return _1();
    }

    /**
     * Alias for _2()
     * @return
     */
    public Json value() {
      return _2();
    }
  }

  /**
   * Utility for fast construction of HashMap<String, Json>
   */
  public static class JMap extends HashMap<String, Json> {

    public JMap() {
      super();
    }

    public JMap(JEntry... entries) {
      super();
      for (JEntry e : entries) {
        super.put(e._1(), e._2());
      }
    }
  }
}

@SuppressWarnings("serial")
class EofException extends Exception {
}

class Reader {

  int pos;
  It<Character> i;
  StringBuilder bf;
  char peek;

  Reader(String data) {
    pos = -1;
    i = It.from(data);
    try {
      next();
    } catch (EofException e) {
      throw new IllegalArgumentException(String.format(
        "'data'_is_empty (%d)", 0));
    }
  }

  final char next() throws EofException {
    pos++;
    if (i.hasNext()) {
      return peek = i.next();
    }
    throw new EofException();
  }

  void skipBlanks() throws EofException {
    while (true) {
      if (peek < 33) {
        next();
      } else {
        break;
      }
    }
  }

  String readValue() throws EofException {
    if (peek == 't') {
      readConstant("true");
      String v = bf.toString();
      if (!v.equals("true")) {
        throw new IllegalArgumentException(String.format(
          "'%s'_bad_boolean (%d)", v, pos));
      }
      return v;
    } else if (peek == 'f') {
      readConstant("false");
      String v = bf.toString();
      if (!v.equals("false")) {
        throw new IllegalArgumentException(String.format(
          "'%s'_bad_boolean (%d)", v, pos));
      }
      return v;
    } else if (peek == 'n') {
      readConstant("null");
      String v = bf.toString();
      if (!v.equals("null")) {
        throw new IllegalArgumentException(String.format(
          "'%s'_bad_null_value (%d)", v, pos));
      }
      return v;
    } else if (peek == '"') {
      return readStringValue();
    } else if (peek == '-' || (peek >= '0' && peek <= '9')) {
      bf = new StringBuilder();
      bf.append(peek);
      next();
      while (peek != '}' && peek != ']' && peek != ',' && peek > 32) {
        bf.append(peek);
        next();
      }
      skipBlanks();
      return bf.toString();
    } else if (peek == '[') {
      StringBuilder tmp = new StringBuilder();
      tmp.append('[');
      next();
      skipBlanks();
      if (peek == ']') {
        next();
        skipBlanks();
        return "[]";
      }

      while (true) {
        tmp.append(readValue());
        if (peek == ']') {
          break;
        } else if (peek == ',') {
          tmp.append(',');
          next();
          skipBlanks();
        } else {
          throw new IllegalArgumentException(String.format(
            "'%s'_bad_array (%d)", tmp.toString(), pos));
        }
      }
      tmp.append(']');
      next();
      skipBlanks();
      return tmp.toString();
    } else if (peek == '{') {
      StringBuilder tmp = new StringBuilder();
      tmp.append('{');
      next();
      skipBlanks();
      if (peek == '}') {
        next();
        skipBlanks();
        return "{}";
      }

      while (true) {
        tmp.append(readStringValue());
        if (peek != ':') {
          throw new IllegalArgumentException(String.format(
            "'%s'_':'_is_missing_in_object_definition (%d)",
            tmp.toString(), pos));
        }
        tmp.append(':');
        next();
        skipBlanks();
        tmp.append(readValue());
        if (peek == '}') {
          break;
        } else if (peek == ',') {
          tmp.append(',');
          next();
          skipBlanks();
        } else {
          throw new IllegalArgumentException(String.format(
            "'%s'_bad_object (%d)", tmp.toString(), pos));
        }
      }
      tmp.append('}');
      next();
      skipBlanks();
      return tmp.toString();
    } else {
      throw new IllegalArgumentException(String.format(
        "'%c'_bad_begin_of_value (%d)", peek, pos));
    }
  }

  String readStringValue() throws EofException {
    bf = new StringBuilder();
    bf.append('"');
    bf.append(next());
    while (peek != '"') {
      if (peek == '\\') {
        bf.append(next());
      }
      bf.append(next());
    }
    next();
    skipBlanks();
    return bf.toString();
  }

  void readConstant(String constant)
    throws EofException {
    bf = new StringBuilder();
    for (char c : It.from(constant)) {
      if (c != peek) {
        throw new IllegalArgumentException(String.format(
          "'%s'_bad_value (%d)", constant, pos));
      }
      bf.append(peek);
      next();
    }
    skipBlanks();
  }

  void readString() throws EofException {
    if (peek != '"') {
      throw new IllegalArgumentException(String.format(
        "Bad_string (%d)", pos));
    }
    bf = new StringBuilder();
    next();
    readString2();
  }

  void readString2() throws EofException {
    while (true) {
      if (peek == '"') {
        break;
      } else if (peek == '\\') {
        next();
        readString3();
      } else {
        bf.append(peek);
        next();
      }
    }
    next();
    skipBlanks();
  }

  void readString3() throws EofException {
    switch (peek) {
      case '"':
      case '\\':
      case '/':
        bf.append(peek);
        break;
      case 'b':
        bf.append('\b');
        break;
      case 'f':
        bf.append('\f');
        break;
      case 'n':
        bf.append('\n');
        break;
      case 'r':
        bf.append('\r');
        break;
      case 't':
        bf.append('\t');
        break;
      case 'u':
        next();
        readString4();
        return;
      default:
        throw new IllegalArgumentException(String.format(
          "'%s'_bad_escape_character (%d)", peek, pos));
    }
    next();
  }

  void readString4() throws EofException {
    String n = "" + peek + next() + next() + next();
    try {
      bf.append((char) Integer.parseInt(n, 16));
    } catch (NumberFormatException ex) {
      throw new IllegalArgumentException(String.format(
        "'%s'_bad_unicode_value (%d)", n, pos));
    }
    next();
  }

  void readNumber() throws EofException {
    bf = new StringBuilder();
    if (peek == '-') {
      bf.append('-');
      next();
    }
    if (peek == '0') {
      bf.append('0');
      next();
      if (peek == '.') {
        bf.append('.');
        next();
        readNumber3();
      } else if (peek < '0' || peek > '9') {
        return;
      } else {
        throw new IllegalArgumentException(String.format(
          "Bad_number (%d)", pos));
      }
    } else {
      if (peek < '1' || peek > '9') {
        throw new IllegalArgumentException(String.format(
          "Bad_number (%d)", pos));
      }
      bf.append(peek);
      next();
      readNumber2();
    }
    skipBlanks();
  }

  void readNumber2() throws EofException {
    while (true) {
      if (peek == '.') {
        bf.append('.');
        next();
        readNumber3();
      } else if (peek < '0' || peek > '9') {
        return;
      } else {
        bf.append(peek);
        next();
      }
    }
  }

  void readNumber3() throws EofException {
    while (true) {
      if (peek == 'e' || peek == 'E') {
        bf.append(peek);
        next();
        readNumber4();
      } else if (peek < '0' || peek > '9') {
        return;
      } else {
        bf.append(peek);
        next();
      }
    }
  }

  void readNumber4() throws EofException {
    if (peek == '-' || peek == '+') {
      bf.append(peek);
      next();
    }
    while (true) {
      if (peek < '0' || peek > '9') {
        return;
      } else {
        bf.append(peek);
        next();
      }
    }
  }

  static String fromTpItAux(It<Tp2<String, Json>> i) {
    if (i.hasNext()) {
      StringBuilder sb = new StringBuilder();
      Tp2<String, Json> tp = i.next();
      sb.append(new Json(tp._1()).toString());
      sb.append(":");
      sb.append(tp._2().toString());
      while (i.hasNext()) {
        tp = i.next();
        sb.append(",");
        sb.append(new Json(tp._1()).toString());
        sb.append(":");
        sb.append(tp._2().toString());
      }
      return "{" + sb.toString() + "}";
    } else {
      return "{}";
    }
  }

  /**
   * Utility for an It gotten from a map.
   *
   * @param i
   * @return
   */
  public static Json fromTpIt(It<Tp2<String, Json>> i) {
    Json r = new Json();
    r.value = fromTpItAux(i);
    return r;
  }

  /**
   * Returns a Json from a json string
   *
   * @param js A json string.
   * @return The corresponding Json object.
   */
  public static Json fromJs(String js) {
    Json r = new Json();
    r.value = js.trim();
    return r;
  }

  /**
   * Returns a Json from a urlencoded json string. Used to read server
   * responses.
   *
   * @param js A json string.
   * @param charset e.g. UTF-8 or ISO-8859-1
   * @return The corresponding Json object.
   */
  public static Json decode(String js, String charset) {
    try {
      Json r = new Json();
      r.value = URLDecoder.decode(js, charset).trim();
      return r;
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Equals to decode (js, "UTF-8").
   *
   * @param js
   * @return
   */
  public static Json decode(String js) {
    return decode(js, "UTF-8");
  }

}
