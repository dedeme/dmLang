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
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

/**
 *
 * @version 1.0
 * @since 07-Apr-2014
 * @author deme
 */
public class FileMap implements AutoCloseable {

  File f;
  HashMap<String, String> mp;

  public FileMap(File f, String[] defaultValues) {
    this.f = f;
    mp = new HashMap<>();
    for (String s : (f.isFile()) ? Io.slurp(f).split("\n") : defaultValues) {
      int ix = s.indexOf("=");
      if (ix > 0) {
        String key = s.substring(0, ix).trim();
        if (!key.equals("")) {
          mp.put(key, s.substring(ix + 1).trim());
        }
      }
    }
    if (!f.isFile()) {
      if (!f.getParentFile().isDirectory()) {
        f.getParentFile().mkdirs();
      }
      wr();
    }
  }

  public FileMap(File f) {
    this(f, new String[]{});
  }

  FileMap(File f, HashMap<String, String> mp) {
    this.f = f;
    this.mp = mp;
  }

  final void wr() {
    Io.spit(f, It.join(It.from(mp).map((Tp2<String, String> p)
      -> p._1() + "=" + p._2())));
  }

  public void put(String key, String value) {
    if (key.contains("=")) {
      throw new IllegalArgumentException("key must not contain '='");
    }
    mp.put(key, value);
    wr();
  }

  public void remove(String key) {
    mp.remove(key);
    wr();
  }

  public Set<String> keySet() {
    return mp.keySet();
  }

  public Collection<String> values() {
    return mp.values();
  }

  public It<Tp2<String, String>> entrySet() {
    return It.from(mp);
  }

  public String get(String key) {
    return mp.get(key);
  }

  @Override
  public void close() {
    wr();
  }

  public FileMap fast() {
    return new FileMap(f, mp) {
      @Override
      public void put(String key, String value) {
        if (key.contains("=")) {
          throw new IllegalArgumentException("key must not contain '='");
        }
        mp.put(key, value);
      }

      @Override
      public void remove(String key) {
        mp.remove(key);
      }
    };
  }
}
