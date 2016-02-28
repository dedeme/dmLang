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

import static dmjava.Func.*;
import java.util.Arrays;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author deme
 */
public class CrypTest {

  public CrypTest() {
  }

  @Test
  public void test() {

    for (int i = 0; i < 10; i++) {
      assertEquals(Cryp.random(3).length, 3);
    }

    assertEquals(Cryp.encode("a".getBytes(), 5).length, 5);
    assertEquals(Cryp.encode("".getBytes(), 10).length, 10);
    assertEquals(Cryp.encode("b".getBytes(), 6).length, 6);
    assertEquals(Cryp.encode("adfgfdgewr".getBytes(), 3).length, 3);

    assertTrue(Arrays.equals(Cryp.encode("".getBytes(), 3),
      Cryp.encode(new byte[]{}, 3)));
    assertFalse(Arrays.equals(Cryp.encode("".getBytes(), 5),
      Cryp.encode(new byte[]{}, 3)));

    assertEquals(Cryp.createKey("zx".getBytes()).length, 16);

    assertTrue(Arrays.equals(Cryp.createKey("zx".getBytes()),
      Cryp.createKey("zx".getBytes())));
    assertFalse(Arrays.equals(Cryp.createKey("zx".getBytes()),
      Cryp.createKey("z".getBytes())));

    F2<String, Integer, Boolean> en1 = (String s, Integer i)
      -> Arrays.equals(
        Cryp.decodeAES(Cryp.encodeAES(s.getBytes(), i), i), s.getBytes()
      );

    assertTrue(en1.run("a", 5));
    assertTrue(en1.run("", 5));
    assertTrue(en1.run("a", 2));
    assertTrue(en1.run("", 2));
    assertTrue(en1.run("a", 16));
    assertTrue(en1.run("", 16));
    assertTrue(en1.run("12345678901234567sldkfjwiersdfksdiejfsdflesasddww", 5));
    assertTrue(en1.run("12345678901234567sldkfjwiersdejfsdflesasdsddasdww", 2));
    assertTrue(en1.run("12345678901sldkfjwiersdfksdiejfsdflesasdsddasdww", 16));

    assertEquals(Cryp.encodeHex(Cryp.decodeHex("")), "");
    assertEquals(Cryp.encodeHex(Cryp.decodeHex("ff")), "ff");
    assertEquals(Cryp.encodeHex(Cryp.decodeHex("0f")), "0f");
    assertEquals(Cryp.encodeHex(Cryp.decodeHex("00")), "00");
    assertEquals(Cryp.encodeHex(Cryp.decodeHex("ffff")), "ffff");
    assertEquals(Cryp.encodeHex(Cryp.decodeHex("0f0f0f")), "0f0f0f");
    assertEquals(Cryp.encodeHex(Cryp.decodeHex("00000000")), "00000000");

    assertTrue(Arrays.equals(
      Cryp.decodeB64(Cryp.encodeB64(new byte[]{})), new byte[]{}));
    assertTrue(Arrays.equals(
      Cryp.decodeB64(Cryp.encodeB64(new byte[]{1, 2})), new byte[]{1, 2}));
    assertTrue(Arrays.equals(
      Cryp.decodeB64(Cryp.encodeB64(new byte[]{1})), new byte[]{1}));
    assertTrue(Arrays.equals(
      Cryp.decodeB64(Cryp.encodeB64(new byte[]{-1})), new byte[]{-1}));
    assertTrue(Arrays.equals(
      Cryp.decodeB64(Cryp.encodeB64(new byte[]{-1, 126, 0})),
      new byte[]{-1, 126, 0}
    ));
  }

}
