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
package dmjava;

import java.io.File;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author deme
 */
public class FileMapTest {

  public FileMapTest() {
  }

  @Test
  public void fileMap() {

    File dtest = Io.file(System.getProperty("user.home"),
      "/.dmJavaApp/testDir");
    FileMap fm = new FileMap(Io.file(dtest, "testDir"),
      new String[]{"c1=One", "c2 = Two"});

    assertEquals("One", fm.get("c1"));
    assertEquals("Two", fm.get("c2"));
    assertEquals(null, fm.get("c3"));

    fm.put("c3", "Three");
    assertEquals("Three", fm.get("c3"));

    fm.remove("c2");
    assertEquals("One", fm.get("c1"));
    assertEquals(null, fm.get("c2"));
    assertEquals("Three", fm.get("c3"));

    fm.put("c4", "{:a 1 :b [\"a\" 2]}");
    assertEquals("{:a 1 :b [\"a\" 2]}", fm.get("c4"));
    fm.remove("c4");

    fm = new FileMap(Io.file(dtest, "testDir"),
      new String[]{"c1=One", "c2 = Two"});
    assertEquals("One", fm.get("c1"));
    assertEquals(null, fm.get("c2"));
    assertEquals("Three", fm.get("c3"));

    fm.put("c3", "");
    for (Integer i : It.range(10)) {
      fm.put("c3", fm.get("c3") + i);
    }
    for (Integer i : It.range(10)) {
      fm.put("c3", fm.get("c3") + i);
    }
    assertEquals(20, fm.get("c3").length());

    assertTrue(fm.keySet().contains("c1"));
    assertTrue(!fm.keySet().contains("c2"));
    assertTrue(fm.keySet().contains("c3"));
    assertTrue(!fm.keySet().contains("c4"));

    assertTrue(fm.values().contains("One"));
    assertTrue(!fm.keySet().contains("Two"));

    try (FileMap fm2 = fm.fast()) {
      fm2.put("c0", fm2.get("c3"));
      fm2.put("c2", "Two");
      fm2.put("c3", "Three");
    }
    assertEquals("01234567890123456789", fm.get("c0"));
    assertEquals("One", fm.get("c1"));
    assertEquals("Two", fm.get("c2"));
    assertEquals("Three", fm.get("c3"));

    fm = new FileMap(Io.file(dtest, "testDir"),
      new String[]{"c1=One", "c2 = Two"});
    assertEquals("01234567890123456789", fm.get("c0"));
    assertEquals("One", fm.get("c1"));
    assertEquals("Two", fm.get("c2"));
    assertEquals("Three", fm.get("c3"));

    Io.delete(dtest);
  }

}
