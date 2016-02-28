/*
 * Copyright 08-abr-2014 ºDeme
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

import dmjava.It;
import java.util.HashMap;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author deme
 */
public class JsonTest {

  public JsonTest() {
  }

  @Test
  public void all() {
    try {
      assertEquals("\"a\"", new Json("a").toString());
      assertEquals("\"\"", new Json("").toString());
      assertEquals("\"\\\"\\\\\\b\\f\\n\\r\\t\"",
        new Json("\"\\\b\f\n\r\t").toString());
      assertEquals("\"\\u00f0\"", new Json("\u00f0").toString());

      assertEquals("0.0", new Json((double) 0).toString());
      assertEquals("0.1", new Json((double) 0.1).toString());
      assertEquals("1234.234", new Json((double) 1234.234).toString());
      assertEquals("1.234E23", new Json((double) 1.234e23).toString());

      assertEquals(0.0, new Json((double) 0).getNumber(), 0.00001);
      assertEquals(0.0, new Json((double) 0.).getNumber(), 0.00001);
      assertEquals(0.0, new Json((double) .0).getNumber(), 0.00001);
      assertEquals(0.1, new Json((double) 0.1).getNumber(), 0.00001);
      assertEquals(1234.234, new Json((double) 1234.234).getNumber(), 0.00001);
      assertEquals(1.234E23, new Json((double) 1.234e23).getNumber(), 0.00001);

      assertEquals(0, new Json(0).getNumber().intValue());
      assertEquals(22, new Json(22).getNumber().intValue());
      assertEquals((byte) 22, new Json(22).getNumber().byteValue());

      Json n0 = new Json();
      Json n = new Json(25.4);
      Json b = new Json(false);
      Json s = new Json("alter");
      Json a = new Json(It.from(new Json[]{}));

      assertEquals("[null]", new Json(It.from(new Json[]{n0})).toString());
      assertEquals("[25.4]", new Json(It.from(new Json[]{n})).toString());
      assertEquals("[false]", new Json(It.from(new Json[]{b})).toString());
      assertEquals("[\"alter\"]", new Json(It.from(new Json[]{s})).toString());
      assertEquals("[[]]", new Json(It.from(new Json[]{a})).toString());
      assertEquals("[null,25.4,false,\"alter\",[]]",
        new Json(It.from(new Json[]{n0, n, b, s, a})).toString());

      HashMap<String, Json> hn0 = new HashMap<>();
      hn0.put("null", n0);
      HashMap<String, Json> hn = new HashMap<>();
      hn.put("number", n);
      HashMap<String, Json> hb = new HashMap<>();
      hb.put("bool", b);
      HashMap<String, Json> hs = new HashMap<>();
      hs.put("str", s);
      HashMap<String, Json> ha = new HashMap<>();
      ha.put("arr", a);
      Json h0 = new Json(new HashMap<>());
      HashMap<String, Json> hh0 = new HashMap<>();
      hh0.put("hash0", h0);

      assertEquals("{\"null\":null}", new Json(hn0).toString());
      assertEquals("{\"number\":25.4}", new Json(hn).toString());
      assertEquals("{\"bool\":false}", new Json(hb).toString());
      assertEquals("{\"str\":\"alter\"}", new Json(hs).toString());
      assertEquals("{\"arr\":[]}", new Json(ha).toString());
      assertEquals("{\"hash0\":{}}", new Json(hh0).toString());

      HashMap<String, Json> hall = new HashMap<>();
      hall.put("null", n0);
      hall.put("number", n);
      hall.put("bool", b);
      hall.put("str", s);
      hall.put("arr", a);
      hall.put("hash0", h0);

      HashMap<String, Json> newHall = new Json(hall).getMap();

      assertEquals(newHall.get("null").getArray(), null);
      assertEquals(newHall.get("number").getNumber(), 25.4, 0.00001);
      assertEquals(newHall.get("bool").getBoolean(), false);
      assertEquals(newHall.get("str").getString(), "alter");
      assertTrue(newHall.get("arr").getArray().isEmpty());
      assertTrue(newHall.get("hash0").getMap().isEmpty());

      try {
        assertEquals("{trece=13, cierto={}}",
          Json.fromJs(
            "{\"cierto\": {}, \"trece\":13}").getObject().toString());
        assertEquals("{falso=[], nulo=null}",
          Json.fromJs(
            "{\"falso\": [],\"nulo\":null}").getObject().toString());
        assertEquals("{aNumber=[12], dos=2, tres={\"1\":1,\"2\":2}}",
          Json.fromJs(
            "{\"aNumber\": [12], \"dos\": 2, \"tres\":{\"1\":1,\"2\":2}}")
          .getObject().toString());
        assertEquals("{Several numb=[1.23e+3,\"a\",true,null,\"null\"]}",
          Json.fromJs(
            "{\"Several numb\":[1.23e+3, \"a\", true, null, \"null\"]}")
          .getObject().toString());
      } catch (Exception e) {
        e.printStackTrace();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
