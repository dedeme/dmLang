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

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author deme
 */
public class UtilTest {

  public UtilTest() {
  }

  @Test
  public void test() {
    assertEquals(Util.intToHex(3, 4), "0003");
    assertEquals(Util.intToHex(3, 2), "03");
    assertEquals(Util.intToHex(0, 2), "00");
    assertEquals(Util.intToHex(0, 4), "0000");
    assertEquals(Util.intToHex(12, 2), "0c");
    assertEquals(Util.intToHex(12, 4), "000c");
    assertEquals(Util.intToHex(32, 2), "20");
    assertEquals(Util.intToHex(32, 4), "0020");

    assertEquals((int) Util.restore(Util.serialize(4)), 4);
    assertEquals((Object) Util.restore(Util.serialize(null)), null);
    assertEquals(Util.restore(Util.serialize("")), "");
    assertEquals(Util.restore(Util.serialize("abc")), "abc");
  }
}
