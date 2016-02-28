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

package dmjava.io;

import static dmjava.io.Zip.*;
import dmjava.Io;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author deme
 */
public class ZipTest {

  public ZipTest() {
  }

  @Test
  public void hello() {
    try {
      File root = Io.file(System.getProperty("user.home"), ".dmJavaApp",
        "ZipTest");
      File base = Io.file(root, "base");
      File d1 = Io.file(base, "d1");
      File d2 = Io.file(base, "d2");
      File d3 = Io.file(base, "d3");
      File d22 = Io.file(d2, "d22");
      File f1 = Io.file(base, "f1");
      File f2 = Io.file(d2, "f2");
      File z1 = Io.file(root, "z1.zip");

      base.mkdirs();
      d1.mkdir();
      d2.mkdir();
      d3.mkdir();
      d22.mkdir();

      Io.spit(f1, "Is f1 file");
      Io.spit(f2, "Is f2 file\n");
      zip(base, z1);
      Io.delete(base);
      unzip(z1, root);

      assertEquals("Is f1 file", Io.slurp(f1));
      assertEquals("Is f2 file\n", Io.slurp(f2));

      zip (base, z1, (File pathname) -> (pathname.getName().charAt(1) != '2'));
      Io.delete(base);
      unzip(z1, root);

      assertEquals("Is f1 file", Io.slurp(f1));
      assertFalse(f2.exists());

      Io.delete(root);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
