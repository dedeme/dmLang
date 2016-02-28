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

import java.awt.Toolkit;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @version 1.0
 * @since Apr-05-2014
 * @author deme
 */
public class Util {

  private Util() {
  }
  /**
   * The sign of euro
   */
  public static final char EURO = '\u20AC';
  /**
   * The sign of euro in HTML format
   */
  public static final String EURO_HTML = "&#8364;";

  /**
   * Returns the 'ClassLoader' of 'dmjava'.
   *
   * @return
   */
  public static ClassLoader classLoader() {
    try {
      return Class.forName("dmjava.Util").getClassLoader();
    } catch (ClassNotFoundException e) {
      System.out.println(e);
      return null;
    }
  }

  /**
   * Returns a resource located in 'dmjava'.
   *
   * @param path Relative to 'dmjava' ClassLoader.
   * @return
   */
  public static InputStream resource(String path) {
    try {
      return classLoader().getResource(path).openStream();
    } catch (IOException ex) {
      ex.printStackTrace();
      return null;
    }
  }

  /**
   * Issues a beep
   */
  public static void beep() {
    Toolkit.getDefaultToolkit().beep();
  }

  /**
   * Stops program 'n' milliseconds
   *
   * @param n
   */
  public static void sleep(int n) {
    try {
      Thread.sleep(n);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  /**
   * Converter
   *
   * @param n
   * @return
   */
  public static byte[] toBytes(int n) {
    byte[] r = new byte[4];
    r[0] = (byte) (n % 256);
    n /= 256;
    r[1] = (byte) (n % 256);
    n /= 256;
    r[2] = (byte) (n % 256);
    r[3] = (byte) (n / 256);
    return r;
  }

  /**
   * Converter
   *
   * @param n
   * @return
   */
  public static byte[] toBytes(long n) {
    byte[] r = new byte[8];
    for (int i = 0; i < 7; i++) {
      r[i] = (byte) (n % 256);
      n /= 256;
    }
    r[7] = (byte) n;
    return r;
  }

  /**
   * Converter
   *
   * @param n
   * @return
   */
  public static int toInt(byte[] n) {
    int a = n[0];
    if (a < 0) {
      a += 256;
    }
    int b = n[1];
    if (b < 0) {
      b += 256;
    }
    int c = n[2];
    if (c < 0) {
      c += 256;
    }
    int d = n[3];
    if (d < 0) {
      d += 256;
    }
    return a + b * 256 + c * 65536 + d * 16777216;
  }

  /**
   * Converter
   *
   * @param n
   * @return
   */
  public static long toLong(byte[] n) {
    long r = 0;
    long m = 1;
    for (int i = 0; i < 8; i++) {
      int b = n[i];
      if (b < 0) {
        b += 256;
      }
      r += b * m;
      m *= 256;
    }
    return r;
  }

  /**
   * Returns 'n' in a hexadecimal with at least two digits.
   *
   * @param n
   * @return
   */
  public static String intToHex(Integer n) {
    String r = Integer.toHexString(n);
    return (r.length() == 1) ? "0" + r : r;
  }

  /**
   * Returns 'n' in a hexadecimal with at least 'n' digits.
   *
   * @param n
   * @param digits
   * @return
   */
  public static String intToHex(Integer n, int digits) {
    String r = Integer.toHexString(n);
    int lg = r.length();
    return (lg < digits) ? Str.replicate('0', digits - lg) + r : r;
  }

  /**
   * Converter
   *
   * @param n
   * @return
   */
  public static int hexToInt(String n) {
    return Integer.parseInt(n, 16);
  }

  /**
   * Serializes an object
   *
   * @param obj
   * @return
   */
  public static byte[] serialize(java.io.Serializable obj) {
    try (
      ByteArrayOutputStream out = new ByteArrayOutputStream()) {
      new ObjectOutputStream(out).writeObject(obj);
      return out.toByteArray();
    } catch (IOException ex) {
      throw new IllegalArgumentException(ex);
    }
  }

  /**
   * Restore an serialized object
   *
   * @param <A>
   * @param serial
   * @return
   */
  @SuppressWarnings("unchecked")
  public static <A> A restore(byte[] serial) {
    try (
      ByteArrayInputStream in = new ByteArrayInputStream(serial)) {
      return (A) new ObjectInputStream(in).readObject();
    } catch (ClassNotFoundException | IOException ex) {
      throw new IllegalArgumentException(ex);
    }
  }

  /**
   * Calculates the letter of NIF
   *
   * @param dni
   * @return
   */
  public static char NIF(int dni) {
    String data = "TRWAGMYFPDXBNJZSQVHLCKE";
    return data.charAt(dni % 23);
  }

  /**
   * Equals to numeroAPalabras(n, false)
   *
   * @param n
   * @return
   */
  public static String numeroAPalabras(int n) {
    return numeroAPalabras(n, 0);
  }

  /**
   * Converts a integer to word (in Spanish)
   *
   * @param n Number to convert
   * @param female <b>true</b> if number refers a female subject.
   * @return The number converts to words. If number value is 0 return an empty
   * string. Endings in 1 are converted to "uno" if female is false and to "una"
   * if female is true.
   */
  public static String numeroAPalabras(int n, boolean female) {
    if (female) {
      return numeroAPalabras(n, 2);
    } else {
      return numeroAPalabras(n, 1);
    }
  }

  static String numeroAPalabras(int n, int tipo) {
    String p1, p2;

    if (n > 999999) {
      if (n == 1000000) {
        return "un millón";
      } else {
        if (n / 1000000 == 1) {
          p1 = "un millón ";
        } else {
          p1 = numeroAPalabras(n / 1000000, 0);
          p1 = p1 + " millones ";
        }
        p2 = numeroAPalabras(n - n / 1000000 * 1000000, tipo);
        return (p1 + p2).trim();
      }
    } else if (n > 999) {
      if (n / 1000 == 1) {
        p1 = "mil ";
      } else {
        if (tipo == 2) {
          p1 = numeroAPalabras(n / 1000, 2);
        } else {
          p1 = numeroAPalabras(n / 1000, 0);
        }
        p1 = p1 + " mil ";
      }
      p2 = numeroAPalabras(n - n / 1000 * 1000, tipo);
      return (p1 + p2).trim();
    } else if (n > 99) {
      if (n == 100) {
        return "cien";
      } else {
        String terminacion = "os";
        if (tipo == 2) {
          terminacion = "as";
        }
        if (n / 100 == 1) {
          p1 = "ciento ";
        } else if (n / 100 == 5) {
          p1 = "quinient" + terminacion + " ";
        } else if (n / 100 == 7) {
          p1 = "setecient" + terminacion + " ";
        } else if (n / 100 == 9) {
          p1 = "novecient" + terminacion + " ";
        } else {
          p1 = numeroAPalabras(n / 100);
          p1 = p1 + "cient" + terminacion + " ";
        }
        p2 = numeroAPalabras(n - n / 100 * 100, tipo);
        return (p1 + p2).trim();
      }
    } else if (n > 9) {
      if (n == 10) {
        return "diez";
      } else if (n == 11) {
        return "once";
      } else if (n == 12) {
        return "doce";
      } else if (n == 13) {
        return "trece";
      } else if (n == 14) {
        return "catorce";
      } else if (n == 15) {
        return "quince";
      } else if (n == 16) {
        return "dieciseis";
      } else if (n == 17) {
        return "diecisiete";
      } else if (n == 18) {
        return "dieciocho";
      } else if (n == 19) {
        return "diecinueve";
      } else if (n == 20) {
        return "veinte";
      } else {
        if (n / 10 == 2) {
          p1 = "venti";
        } else if (n / 10 == 3) {
          p1 = "treinta";
        } else if (n / 10 == 4) {
          p1 = "cuarenta";
        } else if (n / 10 == 5) {
          p1 = "cincuenta";
        } else if (n / 10 == 6) {
          p1 = "sesenta";
        } else if (n / 10 == 7) {
          p1 = "setenta";
        } else if (n / 10 == 8) {
          p1 = "ochenta";
        } else {
          p1 = "noventa";
        }

        p2 = numeroAPalabras(n - n / 10 * 10, tipo);
        if (!p2.equals("")) {
          if (p1.equals("venti")) {
            return (p1 + p2).trim();
          } else {
            return (p1 + " y " + p2).trim();
          }
        } else {
          return p1;
        }
      }
    } else {
      if (n == 1) {
        if (tipo == 1) {
          return "uno";
        }
        if (tipo == 2) {
          return "una";
        } else {
          return "un";
        }
      } else if (n == 2) {
        return "dos";
      } else if (n == 3) {
        return "tres";
      } else if (n == 4) {
        return "cuatro";
      } else if (n == 5) {
        return "cinco";
      } else if (n == 6) {
        return "seis";
      } else if (n == 7) {
        return "siete";
      } else if (n == 8) {
        return "ocho";
      } else if (n == 9) {
        return "nueve";
      } else {
        return "";
      }
    }
  }

}
