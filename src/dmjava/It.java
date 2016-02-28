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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 *
 * @version 1.0
 * @param <A>
 * @since 05-abr-2014
 * @author deme
 */
public abstract class It<A> implements Iterable<A>, Iterator<A> {

  private final It<A> self = this;

  @Override
  public Iterator<A> iterator() {
    return this;
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException("Not supported.");
  }

  @Override
  @SuppressWarnings("unchecked")
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o == null || !(o instanceof It<?>)) {
      return false;
    }
    for (A e : (It<A>) o) {
      if (hasNext()) {
        if (e.equals(next())) {
          // continue;
        } else {
          return false;
        }
      } else {
        return false;
      }
    }
    return !(hasNext());
  }

  @Override
  public int hashCode() {
    int hash = 3;
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder r = new StringBuilder();
    r.append("[");
    r.append(join(map(
      (A e) -> e.toString()), ", "));
    r.append("]");
    return r.toString();
  }

// Dynamic constructors -----------------------------------
  public ArrayList<A> toList() {
    ArrayList<A> r = new ArrayList<>();
    for (A e : this) {
      r.add(e);
    }
    return r;
  }

  public A[] toArray(A[] ar) {
    return toList().toArray(ar);
  }

// Functions (lazy ) --------------------------------------
  public It<A> add0(final A e) {
    return new It<A>() {
      boolean isFirst = true;

      @Override
      public boolean hasNext() {
        return (isFirst) ? true : self.hasNext();
      }

      @Override
      public A next() {
        if (isFirst) {
          isFirst = false;
          return e;
        } else {
          return self.next();
        }
      }
    };
  }

  public It<A> addIt(final It<A> it) {
    return new It<A>() {
      @Override
      public boolean hasNext() {
        return self.hasNext() || it.hasNext();
      }

      @Override
      public A next() {
        if (self.hasNext()) {
          return self.next();
        } else {
          return it.next();
        }
      }
    };
  }

  public It<A> add(final A e) {
    return new It<A>() {
      boolean here = true;

      @Override
      public boolean hasNext() {
        return here;
      }

      @Override
      public A next() {
        if (here) {
          if (self.hasNext()) {
            return self.next();
          } else {
            here = false;
            return e;
          }
        }
        throw new NoSuchElementException();
      }
    };
  }

  public It<A> addIt(final It<A> it, final int ix) {
    return new It<A>() {
      int c = 0;

      @Override
      public boolean hasNext() {
        return self.hasNext() || it.hasNext();
      }

      @Override
      public A next() {
        if (c < ix) {
          if (self.hasNext()) {
            ++c;
            return self.next();
          } else {
            c = ix;
            return next();  // yes call to next() of new It
          }
        } else {
          if (it.hasNext()) {
            return it.next();
          }
          return self.next();
        }
      }
    };
  }

  public It<A> add(final A e, final int ix) {
    return new It<A>() {
      int c = 0;

      @Override
      public boolean hasNext() {
        return self.hasNext() || c < ix;
      }

      @Override
      public A next() {
        if (c <= ix) {
          if (self.hasNext() && c < ix) {
            ++c;
            return self.next();
          } else {
            c = ix + 1;
            return e;
          }
        } else {
          return self.next();
        }
      }
    };
  }

  public It<A> drop(int n) {
    int c = 0;
    while (hasNext() && (c++ < n)) {
      next();
    }
    return this;
  }

  public It<A> dropWhile(final F1<A, Boolean> f) {
    return new It<A>() {
      boolean hnx = self.hasNext();
      A e = (hnx) ? self.next() : null;

      public It<A> init() {
        while (hnx && f.run(e)) {
          hnx = self.hasNext();
          if (hnx) {
            e = self.next();
          }
        }
        return this;
      }

      @Override
      public boolean hasNext() {
        return hnx;
      }

      @Override
      public A next() {
        if (hnx) {
          A r = e;
          hnx = self.hasNext();
          if (hnx) {
            e = self.next();
          }
          return r;
        }
        throw new NoSuchElementException();
      }
    }.init();
  }

  /**
   * use with Fn.equals
   *
   * @param f
   * @return
   */
  public It<A> filter(final F1<A, Boolean> f) {
    return new It<A>() {
      boolean hnx = self.hasNext();
      A e = (hnx) ? self.next() : null;

      public It<A> init() {
        while (hnx && !f.run(e)) {
          hnx = self.hasNext();
          if (hnx) {
            e = self.next();
          }
        }
        return this;
      }

      @Override
      public boolean hasNext() {
        return hnx;
      }

      @Override
      public A next() {
        if (hnx) {
          A r = e;
          do {
            hnx = self.hasNext();
            if (hnx) {
              e = self.next();
            }
          } while (hnx && !f.run(e));
          return r;
        }
        throw new NoSuchElementException();
      }
    }.init();
  }

  public <R> It<R> map(final F1<A, R> f) {
    return new It<R>() {
      @Override
      public boolean hasNext() {
        return self.hasNext();
      }

      @Override
      public R next() {
        return f.run(self.next());
      }
    };
  }

  public It<A> take(final int n) {
    return new It<A>() {
      int c = 0;

      @Override
      public boolean hasNext() {
        return self.hasNext() && (c < n);
      }

      @Override
      public A next() {
        if (c++ < n) {
          return self.next();
        }
        throw new NoSuchElementException();
      }
    };
  }

  public It<A> takeWhile(final F1<A, Boolean> f) {
    return new It<A>() {
      boolean hnx = self.hasNext();
      A e = (hnx) ? self.next() : null;

      @Override
      public boolean hasNext() {
        return hnx && f.run(e);
      }

      @Override
      public A next() {
        if (hnx && f.run(e)) {
          A r = e;
          hnx = self.hasNext();
          if (hnx) {
            e = self.next();
          }
          return r;
        }
        throw new NoSuchElementException();
      }
    };
  }

// Progressive --------------------------------------------
  public boolean all(F1<A, Boolean> f) {
    for (A e : this) {
      if (!f.run(e)) {
        return false;
      }
    }
    return true;
  }

  public boolean any(F1<A, Boolean> f) {
    for (A e : this) {
      if (f.run(e)) {
        return true;
      }
    }
    return false;
  }

  public int count() {
    int c = 0;
    while (hasNext()) {
      ++c;
      next();
    }
    return c;
  }

  public void each(F10<A> f) {
    for (A e : this) {
      f.run(e);
    }
  }

  public void eachIx(F20<A, Integer> f) {
    int c = 0;
    for (A e : this) {
      f.run(e, c++);
    }
  }

  @SuppressWarnings("unchecked")
  public Option<A> find(F1<A, Boolean> f) {
    for (A e : this) {
      if (f.run(e)) {
        return new Some<>(e);
      }
    }

    return new None<>();
  }

  @SuppressWarnings("unchecked")
  public Option<A> findLast(F1<A, Boolean> f) {
    Option<A> r = new None<>();
    for (A e : this) {
      if (f.run(e)) {
        r = new Some<>(e);
      }
    }

    return r;
  }

  public int index(F1<A, Boolean> f) {
    int c = 0;
    for (A e : this) {
      if (f.run(e)) {
        return c;
      } else {
        ++c;
      }
    }

    return -1;
  }

  public int lastIndex(F1<A, Boolean> f) {
    int c = 0;
    int r = -1;
    for (A e : this) {
      if (f.run(e)) {
        r = c;
      }
      ++c;
    }

    return r;
  }

  public <R> R reduce(R seed, F2<R, A, R> f) {
    for (A e : this) {
      seed = f.run(seed, e);
    }
    return seed;
  }

// In block -----------------------------------------------
  public Tp2<It<A>, It<A>> duplicate() {
    ArrayList<A> tmp = toList();
    return new Tp2<>(from(tmp), from(tmp));
  }

  public It<A> reverse() {
    ArrayList<A> tmp = toList();
    Collections.reverse(tmp);
    return from(tmp);
  }

  public It<A> sort(final F2<A, A, Boolean> lesser) {
    ArrayList<A> tmp = toList();
    Collections.sort(tmp, (A o1, A o2)
      -> (o1.equals(o2)) ? 0 : (lesser.run(o1, o2)) ? -1 : 1);
    return from(tmp);
  }

  public It<A> shuffle() {
    ArrayList<A> tmp = toList();
    Collections.shuffle(tmp);
    return from(tmp);
  }

// static Constructors ------------------------------------
  public static <K, V> It<Tp2<K, V>> from(final Map<K, V> mp) {
    return from(mp.entrySet()).map(
      (Map.Entry<K, V> p) -> new Tp2<>(p.getKey(), p.getValue()));
  }

  public static <A> It<A> from(final Iterator<A> it) {
    return new It<A>() {
      @Override
      public boolean hasNext() {
        return it.hasNext();
      }

      @Override
      public A next() {
        return it.next();
      }
    };
  }

  public static <A> It<A> from(Iterable<A> it) {
    return from(it.iterator());
  }

  public static <A> It<A> from(A[] ar) {
    return from(java.util.Arrays.asList(ar));
  }

  public static It<Byte> from(final byte[] ar) {
    return new It<Byte>() {
      int ix = 0;
      int size = ar.length;

      @Override
      public boolean hasNext() {
        return ix < size;
      }

      @Override
      public Byte next() {
        return ar[ix++];
      }
    };
  }

  public static It<Character> from(final CharSequence s) {
    return new It<Character>() {
      int c = 0;
      int lg = s.length();

      @Override
      public boolean hasNext() {
        return c < lg;
      }

      @Override
      public Character next() {
        if (c < lg) {
          char r = s.charAt(c++);
          return r;
        }
        throw new NoSuchElementException();
      }
    };
  }

  public static <A> It<A> empty() {
    return new It<A>() {
      @Override
      public boolean hasNext() {
        return false;
      }

      @Override
      public A next() {
        throw new NoSuchElementException();
      }
    };
  }

  public static It<Integer> range() {
    return new It<Integer>() {
      int c = 0;

      @Override
      public boolean hasNext() {
        return true;
      }

      @Override
      public Integer next() {
        return c++;
      }
    };
  }

  public static It<Integer> range(int end) {
    return range().take(end);
  }

  public static It<Integer> range(int begin, int end) {
    return range().take(end).drop(begin);
  }

  public static <A, B> It<Tp2<A, B>> zip(final It<A> it1, final It<B> it2) {
    return new It<Tp2<A, B>>() {
      @Override
      public boolean hasNext() {
        return it1.hasNext() && it2.hasNext();
      }

      @Override
      public Tp2<A, B> next() {
        if (it1.hasNext() && it2.hasNext()) {
          return new Tp2<>(it1.next(), it2.next());
        }
        throw new NoSuchElementException();
      }
    };
  }

// Static converters --------------------------------------
  public static <K, V> HashMap<K, V> toMap(It<Tp2<K, V>> it) {
    HashMap<K, V> r = new HashMap<>();
    for (Tp2<K, V> e : it) {
      r.put(e._1(), e._2());
    }
    return r;
  }

  public static String toString(It<Character> it) {
    StringBuilder r = new StringBuilder();
    for (Character e : it) {
      r.append(e);
    }
    return r.toString();
  }

  public static <A> It<A> flat(final It<It<A>> it) {
    return new It<A>() {
      It<A> tmp = It.empty();

      public It<A> init() {
        while (it.hasNext()) {
          tmp = it.next();
          if (tmp.hasNext()) {
            break;
          }
        }
        return this;
      }

      @Override
      public boolean hasNext() {
        return it.hasNext() || tmp.hasNext();
      }

      @Override
      public A next() {
        if (tmp.hasNext()) {
          return tmp.next();
        } else {
          tmp = it.next();
          return next();
        }
      }
    }.init();
  }

  public static It<Byte> flatFromBytes(final It<byte[]> it) {
    return flat(it.map((byte[] p) -> from(p)));
  }

  public static It<Character> flatFromStrings(final It<String> it) {
    return flat(it.map((String p) -> from(p)));
  }

  /**
   * returns join (it, System.getProperty("line.separator"))
   *
   * @param it
   * @return
   */
  public static String join(It<String> it) {
    return join(it, System.getProperty("line.separator"));
  }

  public static String join(It<String> it, String separator) {
    if (it.hasNext()) {
      StringBuilder r = new StringBuilder(it.next());
      for (String s : it) {
        r.append(separator);
        r.append(s);
      }
      return r.toString();
    }
    return "";
  }

  public static byte[] joinFromBytes(It<byte[]> it) {
    try {
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      while (it.hasNext()) {
        out.write(it.next());
      }
      out.close();
      return out.toByteArray();
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

// Static utilites ----------------------------------------
  public static <A extends Comparable<A>> It<A> sort(It<A> it) {
    ArrayList<A> tmp = it.toList();
    Collections.sort(tmp);
    return from(tmp);
  }

  /**
   * Discontinued short
   *
   * @param it
   * @return
   */
  public static It<String> strSort(It<String> it) {
    return strSort(it, Locale.getDefault());
  }

  public static It<String> strSort(It<String> it, final Locale lc) {
    return it.map((String p)
      -> new Tp2<>(p.replace('!', '¡')
        .replace(' ', '!'), p))
      .sort((p1, p2) -> {
        Collator c = Collator.getInstance(lc);
        return c.compare(p1._1(), p2._1()) < 0;
      }).map((Tp2<String, String> p) -> p._2());
  }
}
