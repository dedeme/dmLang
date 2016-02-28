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
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 *
 * @version 1.0
 * @since 07-Apr-2014
 * @author deme
 */
public class Io {

  private Io() {
  }

// Class In -----------------------------------------------
  public static class In implements AutoCloseable {

    InputStream is;

    public In(InputStream is) {
      this.is = is;
    }

    public In(File f) {
      try {
        is = new FileInputStream(f);
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
    }

    public In(URL u) {
      try {
        URLConnection uc = u.openConnection();
        uc.connect();
        is = uc.getInputStream();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    public It<String> readLines(final String charset) {
      return new It<String>() {
        BufferedReader rd;
        String lastLine;

        public It<String> init() {
          try {
            rd = new BufferedReader(new InputStreamReader(is, charset));
            lastLine = rd.readLine();
            return this;
          } catch (IOException e) {
            e.printStackTrace();
            return null;
          }
        }

        @Override
        public boolean hasNext() {
          return lastLine != null;
        }

        @Override
        public String next() {
          if (lastLine != null) {
            try {
              String r = lastLine;
              lastLine = rd.readLine();
              return r;
            } catch (IOException e) {
              e.printStackTrace();
              return null;
            }
          }
          throw new NoSuchElementException();
        }
      }.init();
    }

    public It<String> readLines() {
      return readLines("UTF-8");
    }

    public It<byte[]> read(final int bufferSize) {
      return new It<byte[]>() {
        byte[] buffer = new byte[bufferSize];
        BufferedInputStream rd;
        int readed;

        public It<byte[]> init() {
          try {
            rd = new BufferedInputStream(is, bufferSize);
            readed = rd.read(buffer, 0, bufferSize);
            return this;
          } catch (IOException e) {
            e.printStackTrace();
            return null;
          }
        }

        @Override
        public boolean hasNext() {
          return readed != -1;
        }

        @Override
        public byte[] next() {
          if (readed != -1) {
            try {
              byte[] r = new byte[readed];
              for (int i = 0; i < readed; ++i) {
                r[i] = buffer[i];
              }
              readed = rd.read(buffer, 0, bufferSize);
              return r;
            } catch (IOException e) {
              e.printStackTrace();
              return null;
            }
          }
          throw new NoSuchElementException();
        }
      }.init();
    }

    public It<byte[]> read() {
      return read(8192);
    }

    @Override
    public void close() {
      try {
        is.close();
      } catch (IOException e) {
        throw new IllegalStateException(e);
      }
    }
  }

// Class Out ----------------------------------------------
  public static class Out implements AutoCloseable {

    BufferedOutputStream os;

    public Out(OutputStream os) {
      this.os = new BufferedOutputStream(os);
    }

    public Out(File f) {
      try {
        os = new BufferedOutputStream(new FileOutputStream(f));
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
    }

    public Out(URL u) {
      try {
        os = new BufferedOutputStream(u.openConnection().getOutputStream());
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    public void write(byte[] data) {
      try {
        os.write(data, 0, data.length);
        os.flush();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    public void writeBytes(It<byte[]> data) {
      try {
        for (byte[] d : data) {
          os.write(d, 0, d.length);
        }
        os.flush();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    public void write(String s, String charset) {
      try {
        byte[] data = s.getBytes(charset);
        os.write(data, 0, data.length);
        os.flush();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    public void write(String s) {
      write(s, "UTF-8");
    }

    public void write(It<String> ss, String separator, String charset) {
      try {
        Charset chs = Charset.forName(charset);
        byte[] data;
        for (String s : ss) {
          data = (s + separator).getBytes(chs);
          os.write(data, 0, data.length);
        }
        os.flush();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    public void write(It<String> ss, String separator) {
      write(ss, separator, "UTF-8");
    }

    public void write(It<String> ss) {
      write(ss, "\n");
    }

    @Override
    public void close() {
      try {
        os.close();
      } catch (IOException e) {
        throw new IllegalStateException(e);
      }
    }
  }

// Utilities ----------------------------------------------
  public static File file(File f, String... ss) {
    for (String s : ss) {
      f = new File(f, s);
    }
    return f;
  }

  public static File file(String f, String... ss) {
    return file(new File(f), ss);
  }

  public static File file(URI f, String... ss) {
    return file(new File(f), ss);
  }

  public static File file(URL f, String... ss) {
    try {
      return file(new File(f.toURI()), ss);
    } catch (URISyntaxException e) {
      e.printStackTrace();
      return null;
    }
  }

  public static File file(File f, Iterable<String> ss) {
    for (String s : ss) {
      f = new File(f, s);
    }
    return f;
  }

  public static File file(String f, Iterable<String> ss) {
    return file(new File(f), ss);
  }

  public static File file(URI f, Iterable<String> ss) {
    return file(new File(f), ss);
  }

  public static File file(URL f, Iterable<String> ss) {
    try {
      return file(new File(f.toURI()), ss);
    } catch (URISyntaxException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Calls to File.createTempFile("dmLang", ".tmp")
   *
   * @return
   */
  public static File createTempFile() {
    try {
      return File.createTempFile("dmLang", ".tmp");
    } catch (IOException e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  /**
   * Calls to File.createTempFile("dmLang", ".tmp") in directory 'dir'
   *
   * @param dir
   * @return
   */
  public static File createTempFile(File dir) {
    try {
      return File.createTempFile("dmLang", ".tmp", dir);
    } catch (IOException e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  /**
   * Creates a temporal directory for reading and writing
   *
   * @return
   */
  public static File createTempDir() {
    try {
      Set<PosixFilePermission> perms
        = PosixFilePermissions.fromString("rwx------");
      return Files.createTempDirectory("dmLang",
        PosixFilePermissions.asFileAttribute(perms)).toFile();
    } catch (IOException e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  /**
   * Creates a temporal directory for reading and writing in directory 'dir'
   *
   * @param dir
   * @return
   */
  public static File createTempDir(File dir) {
    try {
      Set<PosixFilePermission> perms
        = PosixFilePermissions.fromString("rwx------");
      return Files.createTempDirectory(dir.toPath(), "dmLang",
        PosixFilePermissions.asFileAttribute(perms)).toFile();
    } catch (IOException e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  public static void delete(File f) {
    if (f.isFile()) {
      f.delete();
    } else if (Files.isSymbolicLink(f.toPath())) {
      f.delete();
    } else if (f.isDirectory()) {
      for (File fl : f.listFiles()) {
        delete(fl);
      }
      f.delete();
    }
  }

  /**
   * Returns a file filter from a String
   *
   * @param template
   * @return
   */
  public static F1<File, Boolean> filterFrom(final String template) {
    final F2<File, String, Boolean> filterString
      = (File file, String tp) -> {
        String f = file.getName();
        return (tp.equals("*") || tp.equals("*.*")) ? true
        : (tp.equals("*.-"))
        ? (f.indexOf('.') == -1)
        : (tp.equals("*."))
        ? (f.endsWith("."))
        : (tp.startsWith("*."))
        ? (f.endsWith(tp.substring(1)))
        : (tp.equals(".*"))
        ? (f.startsWith("."))
        : (tp.endsWith(".*"))
        ? (f.equals(tp.substring(0, tp.length() - 2))
        || f.startsWith(tp.substring(0, tp.length() - 2) + "."))
        : (f.equals(tp));
      };

    if (template.startsWith("f:")) {
      final String tp = template.substring(2);
      return (File f) -> !f.isDirectory() && filterString.run(f, tp);
    } else if (template.startsWith("d:")) {
      final String tp = template.substring(2);
      return (File f) -> f.isDirectory() && filterString.run(f, tp);
    } else {
      return (File f) -> filterString.run(f, template);
    }
  }

  /**
   * Returns a FileFilter from a template. See filterFrom
   * @param template
   * @return
   */
  public static FileFilter fileFilterFrom(final String template) {
    return filterFrom(template)::run;
  }

  /**
   * Filter for files.
   *
   * @param fs Iterator over files that will be filtered.
   * @param template It can have next formats:
   * <dl>
   * <dt>'*' or '*.*'</dt><dd>Returns every file.</dd>
   * <dt>'*.-'</dt><dd>Returns files without extension. (e.g. 'fil')</dd>
   * <dt>'*.'</dt><dd>Return files with empty extension. (e.g. 'fil.')</dd>
   * <dt>'.*'</dt><dd>Return files that begin with '.' (e.g. '.fil.ex')</dd>
   * <dt>ends with '.*'</dt><dd>Returns files that start equals to 'template'.
   * (e.g., if 'template' is "a.*", it will return 'a', 'a.', 'a.some', but
   * won't return 'al')</dd>
   * <dt>Otherwise</dt><dd>Return files that match exactly with 'template'.
   * </dl>
   * If template begins with "d:", it returns only directories.<p>
   * If template begins with "f:". it returns only no directories.
   * @return An iterator over filtered files.
   */
  public static It<File> filter(It<File> fs, final String template) {
    return fs.filter(filterFrom(template));
  }

  /**
   * Filters files conform with every template of 'templates' (See 'filter()')
   * @param fs
   * @param templates
   * @return
   */
  public static It<File> filterAnd(It<File> fs, final String... templates) {
    for (String tp : templates) {
      fs = filter(fs, tp);
    }
    return fs;
  }

  /**
   * Filters files conform with at less one template of 'templates' (See
   * 'filter()')
   * @param fs
   * @param templates
   * @return
   */
  public static It<File> filterOr(It<File> fs, final String... templates) {
    F1<File, Boolean> fn = (File f) -> {
      Boolean rs = false;
      for (String tp : templates) {
        rs = rs || filterFrom(tp).run(f);
        if (rs) {
          break;
        }
      }
      return rs;
    };
    return fs.filter(fn);
  }

  public static byte[] slurpBytes(File file) {
    try {
      try (
        In in = new In(file);
        ByteArrayOutputStream out = new ByteArrayOutputStream();) {
        new Out(out).writeBytes(in.read());
        return out.toByteArray();
      }
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  public static String slurp(In in, String charset) {
    try {
      try (
        ByteArrayOutputStream out = new ByteArrayOutputStream();) {
        new Out(out).writeBytes(in.read());
        return out.toString(charset);
      }
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  public static String slurp(In in) {
    return slurp(in, "UTF-8");
  }

  public static String slurp(File file, String charset) {
    try (In in = new In(file)) {
      return slurp(in, charset);
    }
  }

  public static String slurp(File file) {
    return slurp(file, "UTF-8");
  }

  public static void spit(File file, byte[] data) {
    try (
      Out out = new Out(file)) {
      out.write(data);
    }
  }

  public static void spit(Out out, String s, String charset) {
    out.write(s, charset);
  }

  public static void spit(Out out, String s) {
    spit(out, s, "UTF-8");
  }

  public static void spit(File file, String s, String charset) {
    try (Out out = new Out(file)) {
      spit(out, s, charset);
    }
  }

  public static void spit(File file, String s) {
    spit(file, s, "UTF-8");
  }

  public static void copy(In in, Out out) {
    out.writeBytes(in.read());
  }

  public static void copy(InputStream in, OutputStream out) {
    try (In i = new In(in); Out o = new Out(out)) {
      copy(i, o);
    }
  }

  public static void copy(InputStream in, File out) {
    try (In i = new In(in); Out o = new Out(out)) {
      copy(i, o);
    }
  }

  public static void copy(InputStream in, URL out) {
    try (In i = new In(in); Out o = new Out(out)) {
      copy(i, o);
    }
  }

  public static void copy(File in, OutputStream out) {
    try (In i = new In(in); Out o = new Out(out)) {
      copy(i, o);
    }
  }

  public static void copy(File in, File out) {
    if (in.isDirectory()) {
      out.mkdirs();
      for (File f : in.listFiles()) {
        copy(f, Io.file(out, f.getName()));
      }
    } else {
      if (out.isDirectory()) {
        try (In i = new In(in); Out o = new Out(Io.file(out, in.getName()))) {
          copy(i, o);
        }
      } else {
        try (In i = new In(in); Out o = new Out(out)) {
          copy(i, o);
        }
      }
    }
  }

  public static void copy(File in, URL out) {
    try (In i = new In(in); Out o = new Out(out)) {
      copy(i, o);
    }
  }

  public static void copy(URL in, OutputStream out) {
    try (In i = new In(in); Out o = new Out(out)) {
      copy(i, o);
    }
  }

  public static void copy(URL in, File out) {
    try (In i = new In(in); Out o = new Out(out)) {
      copy(i, o);
    }
  }

  public static void copy(URL in, URL out) {
    try (In i = new In(in); Out o = new Out(out)) {
      copy(i, o);
    }
  }

}
