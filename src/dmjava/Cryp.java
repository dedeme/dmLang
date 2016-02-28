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

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.NoSuchElementException;
import java.util.Random;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * Functions for encryption.
 *
 * @version 1.0
 * @since 05-Apr-2014
 * @author deme
 */
public class Cryp {

  private Cryp() {
  }

  /**
   * Returns random string with 'digits' digits.
   *
   * @param digits
   * @return
   */
  public static byte[] random(int digits) {
    byte[] r = new byte[digits];
    Random rd = new Random();
    for (int i = 0; i < digits; i++) {
      byte rep = (byte) rd.nextInt();
      for (byte j = 0; j < rep; j++) {
        rd.nextInt();
      }
      r[i] = (byte) rd.nextInt();
    }
    return r;
  }

  /**
   * Encrypts a text irreversibly with 'digits' digits.
   *
   * @param bytes
   * @param digits
   * @return
   */
  public static byte[] encode(byte[] bytes, int digits) {
    int dg2 = digits * 2;

    int len = bytes.length;
    byte[] r = new byte[dg2];
    int pos = 0;
    for (int i = 0; i < dg2; i++) {
      if (pos == len) {
        r[i] = 1;
        pos = 0;
      } else {
        r[i] = bytes[pos];
        pos++;
      }
    }

    for (int i = 0; i < len; i++) {
      bytes[i] = 0;
    }

    for (int j = 0; j < 2; j++) {
      for (int i = 1; i < dg2; i++) {
        r[i] = (byte) (r[i - 1] + r[i] + i - 1);
      }

      for (int i = r.length - 2; i >= 0; i--) {
        r[i] = (byte) (r[i + 1] + r[i] + i - 1);
      }
    }

    byte[] rr = new byte[digits];
    for (int i = 0, j = 0; i < dg2; i += 2, j++) {
      rr[j] = r[i];
    }

    return rr;
  }

  /**
   * Creates an encrypt code for using as seed with AES
   *
   * @param text
   * @return
   */
  public static byte[] createKey(byte[] text) {
    byte[] tmp = Str.toBytes("cryp = \"A2efRt¬2·tlkj543\"appJarTarget");
    byte[] tmp2 = new byte[text.length + tmp.length];
    for (int i = 0; i < text.length; i++) {
      tmp2[i] = text[i];
      text[i] = '0';
    }
    System.arraycopy(tmp, 0, tmp2, text.length, tmp.length);

    return encode(tmp2, 16);
  }

  /**
   * Encrypt a string with a seed as the returned with 'createKey'.
   *
   * @param text
   * @param key
   * @return
   */
  public static byte[] encodeAES(byte[] text, byte[] key) {
    try {
      Cipher cryp = Cipher.getInstance("AES");
      cryp.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"));
      return cryp.doFinal(text);
    } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Decrypts a string encrypts with 'encodeAES'
   *
   * @param text
   * @param key
   * @return
   */
  public static byte[] decodeAES(byte[] text, byte[] key) {
    try {
      Cipher cryp = Cipher.getInstance("AES");
      cryp.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"));
      return cryp.doFinal(text);
    } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Encrypts a text with a random key of 'length' (from 2 to 16) size.
   *
   * @param text
   * @param length From 2 to 16
   * @return
   */
  public static byte[] encodeAES(byte[] text, int length) {
    if (length < 2) {
      length = 2;
    }
    if (length > 16) {
      length = 16;
    }
    byte[] seed = random(length);
    byte[] endSeed = new byte[16];
    for (byte i = 0; i < 16; i++) {
      if (i < length) {
        endSeed[i] = seed[i];
      } else {
        endSeed[i] = i;
      }
    }
    byte[] encrypt = encodeAES(text, endSeed);
    byte[] r = new byte[length + encrypt.length];
    System.arraycopy(seed, 0, r, 0, length);
    System.arraycopy(encrypt, 0, r, length, encrypt.length);
    return r;
  }

  /**
   * Decrypts a text encrypted with encodeAES(text, length)
   *
   * @param text
   * @param length From 2 to 16
   * @return
   */
  public static byte[] decodeAES(byte[] text, int length) {
    byte[] seed = new byte[length];
    byte[] tx = new byte[text.length - length];
    System.arraycopy(text, 0, seed, 0, length);
    System.arraycopy(text, length, tx, 0, tx.length);
    byte[] endSeed = new byte[16];
    for (byte i = 0; i < 16; i++) {
      if (i < length) {
        endSeed[i] = seed[i];
      } else {
        endSeed[i] = i;
      }
    }
    return decodeAES(tx, endSeed);
  }

  /**
   * Add two digits in base-'module'
   *
   * @param module
   * @param a
   * @param b
   * @return
   */
  public static int addMod(int module, int a, int b) {
    if (a >= module || a < 0) {
      throw (new IllegalArgumentException(
        String.format("'%d' is out of module '%d'", a, module)));
    }
    if (b >= module || b < 0) {
      throw (new IllegalArgumentException(
        String.format("'%d' is out of module '%d'", b, module)));
    }

    int r = a + b;
    if (r >= module) {
      r = r - module;
    }
    return r;
  }

  /**
   * Subtract two digits in base-'module'
   *
   * @param module
   * @param a
   * @param b
   * @return
   */
  public static int subMod(int module, int a, int b) {
    if (a >= module || a < 0) {
      throw (new IllegalArgumentException(
        String.format("'%d' is out of module '%d'", a, module)));
    }
    if (b >= module || b < 0) {
      throw (new IllegalArgumentException(
        String.format("'%d' is out of module '%d'", b, module)));
    }

    int r = a - b;
    if (r < 0) {
      r = r + module;
    }
    return r;
  }

  /**
   * Encode 'bytes' in hexadecimal. Return is twice long of 'bytes'.
   *
   * @param bytes
   * @return
   */
  public static String encodeHex(byte[] bytes) {
    final StringBuilder sb = new StringBuilder();
    It.from(bytes).each(
      (Byte b) -> sb.append(Util.intToHex(((b < 0) ? b + 256 : b)))
    );
    return sb.toString();
  }

  /**
   * Decode a string encoded with 'encodeHex'
   *
   * @param s
   * @return
   */
  public static byte[] decodeHex(String s) {
    byte[] r = new byte[s.length() / 2];
    It.range(r.length).each(i -> {
      int ix = i + i;
      r[i] = (byte) Integer.parseInt(s.substring(ix, ix + 2), 16);
    });
    return r;
  }

  /**
   * Decode a string encoded with 'encodeB64'
   *
   * @param code
   * @return
   */
  public static byte[] decodeB64(String code) {
    return Base64.getDecoder().decode(code);
  }

  /**
   * Encode 'in' in Base-64. Return is less than twice long of 'in'.
   *
   * @param in
   * @return
   */
  public static String encodeB64(byte[] in) {
    return Base64.getEncoder().encodeToString(in);
  }

  public static byte[] md5(It<byte[]> data) {
    try {
      final MessageDigest md = MessageDigest.getInstance("MD5");
      data.each(md::update);
      return md.digest();
    } catch (NoSuchAlgorithmException ex) {
      throw new NoSuchElementException(ex.getMessage());
    }
  }

  public static String md5Str(It<byte[]> data) {
    final StringBuilder sb = new StringBuilder();
    It.from(md5(data)).each((Byte b) -> {
      sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
    });
    return sb.toString();
  }
}
