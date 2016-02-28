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

import java.io.Serializable;
import java.text.Collator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

/**
 *
 * @version 1.0
 * @since 05-abr-2014
 * @author deme
 */
public class Func {

  private Func() {
  }

  @FunctionalInterface
  public static interface F0<R> {

    public R run();
  }

  @FunctionalInterface
  public static interface F1<P, R> {

    public R run(P p);
  }

  @FunctionalInterface
  public static interface F2<A, B, R> {

    public R run(A p1, B p2);
  }

  @FunctionalInterface
  public static interface F3<A, B, C, R> {

    public R run(A p1, B p2, C p3);
  }

  @FunctionalInterface
  public static interface F4<A, B, C, D, R> {

    public R run(A p1, B p2, C p3, D p4);
  }

  @FunctionalInterface
  public static interface F5<A, B, C, D, E, R> {

    public R run(A p1, B p2, C p3, D p4, E p5);
  }

  @FunctionalInterface
  public static interface F00 extends Runnable {

    @Override
    public void run();
  }

  @FunctionalInterface
  public static interface F10<P> {

    public void run(P p);
  }

  @FunctionalInterface
  public static interface F20<A, B> {

    public void run(A p1, B p2);
  }

  @FunctionalInterface
  public static interface F30<A, B, C> {

    public void run(A p1, B p2, C p3);
  }

  @FunctionalInterface
  public static interface F40<A, B, C, D> {

    public void run(A p1, B p2, C p3, D p4);
  }

  @FunctionalInterface
  public static interface F50<A, B, C, D, E> {

    public void run(A p1, B p2, C p3, D p4, E p5);
  }

  /**
   * Class for using no-final objects in inner classes or closures.
   *
   * @param <A>
   */
  public static class Wrapper<A> implements Serializable {

    A o;

    public Wrapper(A object) {
      o = object;
    }

    public void set(A obj) {
      o = obj;
    }

    public A get() {
      return o;
    }
  }

  /**
   * Fast constructor for HashMap<String, String>
   *
   * @param data
   * @return
   */
  public static HashMap<String, String> StrMap(String... data) {
    if (data.length % 2 == 1) {
      throw new IllegalArgumentException(
        "Data number must be even");
    }
    HashMap<String, String> r = new HashMap<>();
    for (int i = 0; i < data.length; i = i + 2) {
      r.put(data[i], data[i + 1]);
    }
    return r;
  }

  public static class Option<A> implements Serializable {

    A value;

    Option(A value) {
      this.value = value;
    }

    @Override
    public int hashCode() {
      int hash = 5;
      hash = 97 * hash + Objects.hashCode(this.value);
      return hash;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      final Option<A> other = (Option<A>) obj;
      return Objects.equals(this.value, other.value);
    }

    @Override
    public String toString() {
      return (isNone()) ? "None" : "Some{" + "value=" + value + '}';
    }

    public A get() {
      if (value == null) {
        throw new IllegalStateException("Option is null");
      }
      return value;
    }

    public boolean isNone() {
      return value == null;
    }
  }

  public static class None<A> extends Option<A> {

    public None() {
      super(null);
    }
  }

  public static class Some<A> extends Option<A> {

    public Some(A value) {
      super(value);
    }
  }

  public static class Either<L, R> implements Serializable {

    L left;
    R right;

    Either(L left, R right) {
      this.left = left;
      this.right = right;
    }

    @Override
    public int hashCode() {
      int hash = 7;
      hash = 53 * hash + Objects.hashCode(this.left);
      hash = 53 * hash + Objects.hashCode(this.right);
      return hash;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      final Either<L, R> other = (Either<L, R>) obj;
      if (!Objects.equals(this.left, other.left)) {
        return false;
      }
      return Objects.equals(this.right, other.right);
    }

    @Override
    public String toString() {
      return isLeft()
        ? "Left{" + left + '}'
        : "Right{" + right + '}';
    }

    public boolean isLeft() {
      return right == null;
    }

    public boolean isRight() {
      return left == null;
    }

    public L getLeft() {
      if (isRight()) {
        throw new IllegalStateException("Either is Right");
      }
      return left;
    }

    public R getRight() {
      if (isLeft()) {
        throw new IllegalStateException("Either is Left");
      }
      return right;
    }
  }

  public static class Left<A, B> extends Either<A, B> {

    public Left(A value) {
      super(value, null);
    }
  }

  public static class Right<A, B> extends Either<A, B> {

    public Right(B value) {
      super(null, value);
    }
  }

  public static class Tp2<A, B> implements Serializable {

    protected A e1;
    protected B e2;

    public Tp2(A e1, B e2) {
      this.e1 = e1;
      this.e2 = e2;
    }

    public A _1() {
      return e1;
    }

    public B _2() {
      return e2;
    }

    @Override
    public int hashCode() {
      int hash = 7;
      hash = 89 * hash + Objects.hashCode(this.e1);
      hash = 89 * hash + Objects.hashCode(this.e2);
      return hash;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      @SuppressWarnings("unchecked")
      final Tp2<A, B> other = (Tp2<A, B>) obj;
      if (!Objects.equals(this.e1, other.e1)) {
        return false;
      }
      return Objects.equals(this.e2, other.e2);
    }

    @Override
    public String toString() {
      return "(" + e1 + ", " + e2 + ")";
    }
  }

  public static class Tp3<A, B, C> extends Tp2<A, B> implements Serializable {

    protected C e3;

    public Tp3(A e1, B e2, C e3) {
      super(e1, e2);
      this.e3 = e3;
    }

    public C _3() {
      return e3;
    }

    @Override
    public int hashCode() {
      int hash = 7;
      hash = 29 * hash + Objects.hashCode(this.e1);
      hash = 29 * hash + Objects.hashCode(this.e2);
      hash = 29 * hash + Objects.hashCode(this.e3);
      return hash;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      @SuppressWarnings("unchecked")
      final Tp3<A, B, C> other = (Tp3<A, B, C>) obj;
      if (!Objects.equals(this.e1, other.e1)) {
        return false;
      }
      if (!Objects.equals(this.e2, other.e2)) {
        return false;
      }
      return Objects.equals(this.e3, other.e3);
    }

    @Override
    public String toString() {
      return "(" + e1 + ", " + e2 + ", " + e3 + ")";
    }
  }

  public static class Tp4<A, B, C, D> extends Tp3<A, B, C>
    implements Serializable {

    protected D e4;

    public Tp4(A e1, B e2, C e3, D e4) {
      super(e1, e2, e3);
      this.e4 = e4;
    }

    public D _4() {
      return e4;
    }

    @Override
    public int hashCode() {
      int hash = 7;
      hash = 41 * hash + Objects.hashCode(this.e1);
      hash = 41 * hash + Objects.hashCode(this.e2);
      hash = 41 * hash + Objects.hashCode(this.e3);
      hash = 41 * hash + Objects.hashCode(this.e4);
      return hash;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      @SuppressWarnings("unchecked")
      final Tp4<A, B, C, D> other = (Tp4<A, B, C, D>) obj;
      if (!Objects.equals(this.e1, other.e1)) {
        return false;
      }
      if (!Objects.equals(this.e2, other.e2)) {
        return false;
      }
      if (!Objects.equals(this.e3, other.e3)) {
        return false;
      }
      return Objects.equals(this.e4, other.e4);
    }

    @Override
    public String toString() {
      return "(" + e1 + ", " + e2 + ", " + e3 + ", " + e4 + ")";
    }
  }

  public static class Tp5<A, B, C, D, E> extends Tp4<A, B, C, D>
    implements Serializable {

    protected E e5;

    public Tp5(A e1, B e2, C e3, D e4, E e5) {
      super(e1, e2, e3, e4);
      this.e5 = e5;
    }

    public E _5() {
      return e5;
    }

    @Override
    public int hashCode() {
      int hash = 3;
      hash = 37 * hash + Objects.hashCode(this.e1);
      hash = 37 * hash + Objects.hashCode(this.e2);
      hash = 37 * hash + Objects.hashCode(this.e3);
      hash = 37 * hash + Objects.hashCode(this.e4);
      hash = 37 * hash + Objects.hashCode(this.e5);
      return hash;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      @SuppressWarnings("unchecked")
      final Tp5<A, B, C, D, E> other = (Tp5<A, B, C, D, E>) obj;
      if (!Objects.equals(this.e1, other.e1)) {
        return false;
      }
      if (!Objects.equals(this.e2, other.e2)) {
        return false;
      }
      if (!Objects.equals(this.e3, other.e3)) {
        return false;
      }
      if (!Objects.equals(this.e4, other.e4)) {
        return false;
      }
      return Objects.equals(this.e5, other.e5);
    }

    @Override
    public String toString() {
      return "(" + e1 + ", " + e2 + ", " + e3 + ", " + e4 + ", " + e5 + ")";
    }
  }

  /**
   * Utility for print '=System.out.print'
   * @param o
   */
  public static void print(Object o) {
    System.out.print(o);
  }

  /**
   * Utility for print '=System.out.println'
   * @param o
   */
  public static void println(Object o) {
    System.out.println(o);
  }

  /**
   * Set of second order functions.
   */
  public static class Fn {

    public static <A, B, R> F1<A, R> partial(final B p, final F2<A, B, R> f) {
      return (A p1) -> f.run(p1, p);
    }

    public static <A, B, C, R> F2<A, B, R> partial(
      final C p, final F3<A, B, C, R> f) {
      return (A p1, B p2) -> f.run(p1, p2, p);
    }

    public static <A, B, C, D, R> F3<A, B, C, R> partial(
      final D p, final F4<A, B, C, D, R> f) {
      return (A p1, B p2, C p3) -> f.run(p1, p2, p3, p);
    }

    public static <A, B, C, D, E, R> F4<A, B, C, D, R> partial(
      final E p, final F5<A, B, C, D, E, R> f) {
      return (A p1, B p2, C p3, D p4) -> f.run(p1, p2, p3, p4, p);
    }

    public static <A, B, R> F2<B, A, R> swap(final F2<A, B, R> f) {
      return (B p1, A p2) -> f.run(p2, p1);
    }

    public static <A, B, C, R> F3<C, A, B, R> swap(final F3<A, B, C, R> f) {
      return (C p1, A p2, B p3) -> f.run(p2, p3, p1);
    }

    public static <A, B, C, D, R> F4<D, A, B, C, R> swap(
      final F4<A, B, C, D, R> f) {
      return (D p1, A p2, B p3, C p4) -> f.run(p2, p3, p4, p1);
    }

    public static <A, B, C, D, E, R> F5<E, A, B, C, D, R> swap(
      final F5<A, B, C, D, E, R> f) {
      return (E p1, A p2, B p3, C p4, D p5) -> f.run(p2, p3, p4, p5, p1);
    }

    public static <A> F1<A, Boolean> negate(final F1<A, Boolean> f) {
      return (A p) -> !f.run(p);
    }

    public static <A> F2<A, A, Boolean> negate(final F2<A, A, Boolean> f) {
      return (A p1, A p2) -> (p1.equals(p2)) ? false : !f.run(p1, p2);
    }

    public static <A> F1<A, Boolean> eq(final A o) {
      return (A p) -> p.equals(o);
    }

    public static <A extends Comparable<A>> F2<A, A, Boolean> lesser() {
      return (A p1, A p2) -> ((Comparable<A>) p1).compareTo(p2) < 0;
    }

    public static F2<String, String, Boolean> collator
      = new F2<String, String, Boolean>() {
        final Collator c = Collator.getInstance();

        @Override
        public Boolean run(String p1, String p2) {
          return c.compare(p1, p2) < 0;
        }
      };

    public static F2<String, String, Boolean> collator(Locale lc) {
      final Collator c = Collator.getInstance(lc);
      return (String p1, String p2) -> c.compare(p1, p2) < 0;
    }
  }

}
