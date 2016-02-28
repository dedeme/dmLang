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

import static dmjava.Func.*;
import java.io.File;

import java.io.UnsupportedEncodingException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author deme
 */
public class IoTest {

  public IoTest() {
  }

  String toS (byte[] bs){try{
    return new String(bs, "UTF-8");
  }catch (UnsupportedEncodingException e){e.printStackTrace();return null;}}

  byte[] toB (String s){try{
    return s.getBytes("UTF-8");
  }catch (UnsupportedEncodingException e){e.printStackTrace();return null;}}

  @Test
  public void Io() {
    File root = Io.file(
      System.getProperty("user.home"), "/.dmJavaApp/testDir");
    root.mkdirs();
    final File f = Io.file(root, "target.txt");

    String[] sdata = new String[] {
      "", "a", "abc", "abc\n", "abc\ndef", "abc\ndef\n",
      "abc\ndef\nghi\n", "abc\ndef\n\nghi"
    };

    F1<String, Void> freads = (String p) -> {
      assertEquals (p, Io.slurp(f));
      assertEquals (p, toS(It.joinFromBytes(new Io.In(f).read())));
      assertEquals (p.trim(), (It.join(new Io.In(f).readLines())).trim());
      return null;
    };

    for (String s : sdata) {
      Io.spit(f, s);
      freads.run(s);
      new Io.Out(f).write(s);
      freads.run(s);
    }
    new Io.Out(f).write(It.from(sdata), "");
    freads.run(It.join(It.from(sdata), ""));

    for (byte[] bs : It.from (sdata).map((String s) -> toB(s))) {
      Io.spit(f, bs);
      freads.run(toS(bs));
      new Io.Out(f).write(bs);
      freads.run(toS(bs));
    }
    new Io.Out(f).writeBytes(It.from (sdata).map(this::toB));
    freads.run(It.join(It.from(sdata), ""));

    Io.delete(root);
  }
}
