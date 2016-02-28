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

import dmjava.Func.F1;
import dmjava.Func.F10;
import dmjava.Func.F2;
import dmjava.Func.F20;
import dmjava.Func.Fn;
import dmjava.Func.None;
import dmjava.Func.Some;
import dmjava.Func.Tp2;
import dmjava.Func.Wrapper;
import java.util.TreeMap;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author deme
 */
public class ItTest {

  public ItTest() {
  }

  @Test
  public void constructors() {
    String[] s0 = new String[]{};
    String[] s1 = new String[]{"one"};
    String[] s2 = new String[]{"one", "two", "three"};

    Integer[] i0 = new Integer[]{};
    Integer[] i1 = new Integer[]{1};
    Integer[] i2 = new Integer[]{1, 2, 3};

    assertEquals(It.from(s0), It.from(It.from(s0).toList()));
    assertEquals(It.from(s1), It.from(It.from(s1).toList()));
    assertEquals(It.from(s2), It.from(It.from(s2).toList()));
    assertEquals(It.from(s0), It.from(It.from(s0).toArray(s0)));
    assertEquals(It.from(s1), It.from(It.from(s1).toArray(s0)));
    assertEquals(It.from(s2), It.from(It.from(s2).toArray(s0)));

    assertEquals(It.from(s0), (It.empty()));
    assertEquals(It.from(i0), (It.empty()));
    assertEquals(It.from(s0), (It.from(s0)));
    assertEquals(It.from(s1), (It.from(s1)));
    assertEquals(It.from(s2), (It.from(s2)));
    assertEquals(It.from(i0), (It.from(i0)));
    assertEquals(It.from(i1), (It.from(i1)));
    assertEquals(It.from(i2), (It.from(i2)));

  }

  @Test
  public void lazy() {
    String[] s0 = new String[]{};
    String[] s2 = new String[]{"one", "two", "three"};

    Integer[] i0 = new Integer[]{};
    Integer[] i1 = new Integer[]{1};
    Integer[] i2 = new Integer[]{1, 2, 3};

    F1<Integer, Boolean> pr1 = (Integer p) -> p < 2;

    assertEquals("[1]", It.from(i0).add(1).toString());
    assertEquals("[1]", It.from(i0).add0(1).toString());
    assertEquals("[1, 2, 3, 1]", It.from(i2).add(1).toString());
    assertEquals("[1, 1, 2, 3]", It.from(i2).add0(1).toString());
    assertEquals("[1, 1, 2, 3]", It.from(i2).add(1, 1).toString());
    assertEquals("[1, 2, 3, 1]", It.from(i2).addIt(It.from(i1)).toString());
    assertEquals("[1, 1, 2, 3]", It.from(i2).addIt(It.from(i1), 1).toString());
    assertEquals("[1, 2, 3]", It.from(i2).addIt(It.from(i0)).toString());
    assertEquals("[1, 2, 3]", It.from(i2).addIt(It.from(i0), 1).toString());
    assertEquals("[]", It.from(i0).addIt(It.from(i0)).toString());
    assertEquals("[1, 2, 3]", It.from(i0).addIt(It.from(i2), 1).toString());

    assertEquals("[1]", It.empty().add(1).toString());
    assertEquals("[1]", It.empty().add0(1).toString());
    It<Integer> iti = It.empty();
    assertEquals("[]", iti.addIt(It.from(i0)).toString());
    assertEquals("[1, 2, 3]", iti.addIt(It.from(i2), 1).toString());

    assertEquals("[one, two, three]", It.from(s2).drop(0).toString());
    assertEquals("[two, three]", It.from(s2).drop(1).toString());
    assertEquals("[]", It.from(s2).drop(10).toString());
    assertEquals("[1, 2, 3]", It.from(i2).drop(0).toString());
    assertEquals("[]", It.from(i0).dropWhile(pr1).toString());
    assertEquals("[]", It.from(i1).dropWhile(pr1).toString());
    assertEquals("[2, 3]", It.from(i2).dropWhile(pr1).toString());

    F1<Integer, Boolean> even = (Integer p) -> p % 2 == 0;
    assertEquals("[]", It.from(i0).filter(even).toString());
    assertEquals("[]", It.from(i1).filter(even).toString());
    assertEquals("[2]", It.from(i2).filter(even).toString());
    assertEquals("[]", It.from(i0).filter(Fn.negate(even)).toString());
    assertEquals("[1]", It.from(i1).filter(Fn.negate(even)).toString());
    assertEquals("[1, 3]", It.from(i2).filter(Fn.negate(even)).toString());

    F1<Integer, Integer> mul2 = (Integer p) -> p * 2;
    assertEquals("[]", It.from(i0).map(mul2).toString());
    assertEquals("[2]", It.from(i1).map(mul2).toString());
    assertEquals("[2, 4, 6]", It.from(i2).map(mul2).toString());

    assertEquals("[]", It.from(s2).take(0).toString());
    assertEquals("[one]", It.from(s2).take(1).toString());
    assertEquals("[one, two, three]", It.from(s2).take(10).toString());
    assertEquals("[1, 2, 3]", It.from(i2).take(1000).toString());
    assertEquals("[]", It.from(i0).takeWhile(pr1).toString());
    assertEquals("[1]", It.from(i1).takeWhile(pr1).toString());
    assertEquals("[1]", It.from(i2).takeWhile(pr1).toString());
  }

  @Test
  public void progressive() {
    String[] s0 = new String[]{};
    String[] s1 = new String[]{"one"};
    String[] s2 = new String[]{"one", "two", "three"};

    Integer[] i0 = new Integer[]{};
    Integer[] i1 = new Integer[]{1};
    Integer[] i2 = new Integer[]{1, 2, 3};

    assertTrue(It.from(i0).all(Fn.eq(1)));
    assertTrue(It.from(i1).all(Fn.eq(1)));
    assertFalse(It.from(i2).all(Fn.eq(1)));
    assertFalse(It.from(i0).any(Fn.eq(1)));
    assertTrue(It.from(i2).any(Fn.eq(1)));
    assertFalse(It.from(i2).any(Fn.eq(9)));

    assertEquals(0, It.from(s0).count());
    assertEquals(1, It.from(s1).count());
    assertEquals(3, It.from(s2).count());

    final Wrapper<Integer> sdata = new Wrapper<>(0);
    final Wrapper<Integer> sindex = new Wrapper<>(0);
    F10<Integer> smdata = (Integer p) -> {
      sdata.o += p;
    };
    F20<Integer, Integer> smindex = (Integer p, Integer ix) -> {
      sdata.o += p;
      sindex.o += ix;
    };
    It.from(i0).each(smdata);
    assertEquals((Integer) 0, sdata.o);
    It.from(i1).each(smdata);
    assertEquals((Integer) 1, sdata.o);
    sdata.o = 0;
    It.from(i2).each(smdata);
    assertEquals((Integer) 6, sdata.o);

    sdata.o = 0;
    It.from(i0).eachIx(smindex);
    assertEquals((Integer) 0, sdata.o);
    assertEquals((Integer) 0, sindex.o);
    It.from(i1).eachIx(smindex);
    assertEquals((Integer) 1, sdata.o);
    assertEquals((Integer) 0, sindex.o);
    sdata.o = 0;
    It.from(i2).eachIx(smindex);
    assertEquals((Integer) 6, sdata.o);
    assertEquals((Integer) 3, sindex.o);

    F1<Integer, Boolean> even = (Integer p) -> p % 2 == 0;

    assertEquals(new None<>(), It.from(i0).find(even));
    assertEquals(new None<>(), It.from(i1).find(even));
    assertEquals(new Some<>(2), It.from(i2).find(even));
    assertEquals(new None<>(), It.from(i0).find(Fn.negate(even)));
    assertEquals(new Some<>(1), It.from(i1).find(Fn.negate(even)));
    assertEquals(new Some<>(1), It.from(i2).find(Fn.negate(even)));
    assertEquals(new None<>(), It.from(i0).findLast(even));
    assertEquals(new None<>(), It.from(i1).findLast(even));
    assertEquals(new Some<>(2), It.from(i2).findLast(even));
    assertEquals(new None<>(), It.from(i0).findLast(Fn.negate(even)));
    assertEquals(new Some<>(1), It.from(i1).findLast(Fn.negate(even)));
    assertEquals(new Some<>(3), It.from(i2).findLast(Fn.negate(even)));

    assertEquals((int) -1, It.from(i0).index(even));
    assertEquals((int) -1, It.from(i1).index(even));
    assertEquals((int) 1, It.from(i2).index(even));
    assertEquals((int) -1, It.from(i0).index(Fn.negate(even)));
    assertEquals((int) 0, It.from(i1).index(Fn.negate(even)));
    assertEquals((int) 0, It.from(i2).index(Fn.negate(even)));
    assertEquals((int) -1, It.from(i0).lastIndex(even));
    assertEquals((int) -1, It.from(i1).lastIndex(even));
    assertEquals((int) 1, It.from(i2).lastIndex(even));
    assertEquals((int) -1, It.from(i0).lastIndex(Fn.negate(even)));
    assertEquals((int) 0, It.from(i1).lastIndex(Fn.negate(even)));
    assertEquals((int) 2, It.from(i2).lastIndex(Fn.negate(even)));

    F2<Integer, Integer, Integer> summ = (Integer p1, Integer p2) -> p1 + p2;
    assertEquals((Integer) 1, It.from(i0).reduce(1, summ));
    assertEquals((Integer) 2, It.from(i1).reduce(1, summ));
    assertEquals((Integer) 7, It.from(i2).reduce(1, summ));

  }

  @Test
  public void inBloc() {
    String[] s0 = new String[]{};
    String[] s1 = new String[]{"one"};
    String[] s2 = new String[]{"one", "two", "three"};

    Integer[] i0 = new Integer[]{};
    Integer[] i1 = new Integer[]{1};
    Integer[] i2 = new Integer[]{1, 2, 3};

    Tp2<It<String>, It<String>> tpS;
    tpS = It.from(s0).duplicate();
    assertEquals(It.from(s0), tpS._1());
    assertEquals(It.from(s0), tpS._2());
    tpS = It.from(s1).duplicate();
    assertEquals(It.from(s1), tpS._1());
    assertEquals(It.from(s1), tpS._2());
    tpS = It.from(s2).duplicate();
    assertEquals(It.from(s2), tpS._1());
    assertEquals(It.from(s2), tpS._2());

    assertEquals("[]", It.from(i0).reverse().toString());
    assertEquals("[1]", It.from(i1).reverse().toString());
    assertEquals("[3, 2, 1]", It.from(i2).reverse().toString());

    assertEquals("[]", It.from(s0).sort(Fn.collator).toString());
    assertEquals("[one]", It.from(s1).reverse().toString());
    assertEquals("[three, two, one]", It.from(s2).reverse().toString());

    assertEquals("[]", It.sort(It.from(i0)).toString());
    assertEquals("[1]", It.sort(It.from(i1)).toString());
    assertEquals("[1, 2, 3]", It.sort(It.from(i2).reverse()).toString());

    assertEquals("[]", It.from(s0).sort(Fn.collator).toString());
    assertEquals("[one]", It.strSort(It.from(s1)).toString());
    assertEquals("[one, three, two]", It.strSort(It.from(s2).reverse()).toString());

    println(It.strSort(It.from(new String[]{"pérez", "pera", "p!zarra", "p¡zarra"})));

    assertEquals("[]", It.from(s0).shuffle().toString());

  }

  @Test
  public void staticConstructors() {
    String[] s0 = new String[]{};
    String[] s1 = new String[]{"one"};
    String[] s2 = new String[]{"one", "two", "three"};

    Integer[] i0 = new Integer[]{};
    Integer[] i1 = new Integer[]{1};
    Integer[] i2 = new Integer[]{1, 2, 3};

    TreeMap<Integer, String> hm = new TreeMap<>();
    assertEquals("[]", It.from(hm).toString());
    hm.put(12, "twelve");
    assertEquals("[(12, twelve)]", It.from(hm).toString());
    assertEquals("twelve", It.toMap(It.from(hm)).get(12));
    hm.put(44, "forty four");
    assertEquals("[(12, twelve), (44, forty four)]", It.from(hm).toString());
    assertEquals("twelve", It.toMap(It.from(hm)).get(12));
    assertEquals("forty four", It.toMap(It.from(hm)).get(44));

    assertEquals("[]", It.from(new byte[]{}).toString());
    assertEquals("[1]", It.from(new byte[]{1}).toString());
    assertEquals("[3, 2, 1]", It.from(new byte[]{3, 2, 1}).toString());

    assertEquals("[]", It.from("").toString());
    assertEquals("[1]", It.from("1").toString());
    assertEquals("[3, 2, 1]", It.from("321").toString());

    assertEquals("[0, 1, 2, 3, 4]", It.range(5).toString());
    assertEquals("[2, 3, 4]", It.range(2, 5).toString());
    assertEquals("[]", It.range(0).toString());
    assertEquals("[]", It.range(2, 2).toString());

    assertEquals("[]", It.zip(It.from(s0), It.from(s2)).toString());
    assertEquals("[(one, one)]", It.zip(It.from(s1), It.from(s2)).toString());
    assertEquals("[(1, one)]", It.zip(It.from(i1), It.from(s2)).toString());
    assertEquals("[(1, 1), (2, 2), (3, 3)]", It.zip(It.from(i2), It.from(i2)).toString()
    );
  }

  @Test
  public void staticComverters() {
    assertEquals("", It.toString(It.from("")));
    assertEquals("a", It.toString(It.from("a")));
    assertEquals("cba", It.toString(It.from("cba")));
    assertEquals("acba", It.toString(It.flat(It.from(
      new String[]{"", "a", "cba"}
    ).map((String p) -> It.from(p)))));
    assertEquals("", It.toString(It.flat(It.from(
      new String[]{"", "", ""}
    ).map((String p) -> It.from(p)))));
    assertEquals("", It.toString(It.flat(It.from(
      new String[]{}
    ).map((String p) -> It.from(p)))));

    assertEquals("[]", It.flat(It.from(
      new byte[][]{}
    ).map(It::from)).toString());
    assertEquals("[]", It.flat(It.from(
      new byte[][]{new byte[]{}, new byte[]{}, new byte[]{}}
    ).map(It::from)).toString());
    assertEquals("[1, 3, 2, 1]", It.flat(It.from(
      new byte[][]{new byte[]{}, new byte[]{1}, new byte[]{3, 2, 1}}
    ).map(It::from)).toString());

    assertEquals("", It.toString(It.flatFromStrings(It.from(
      new String[]{}
    ))));
    assertEquals("", It.toString(It.flatFromStrings(It.from(
      new String[]{"", "", ""}
    ))));
    assertEquals("acba", It.toString(It.flatFromStrings(It.from(
      new String[]{"", "a", "cba"}
    ))));

    assertEquals("[]", It.flatFromBytes(It.from(
      new byte[][]{}
    )).toString());
    assertEquals("[]", It.flatFromBytes(It.from(
      new byte[][]{new byte[]{}, new byte[]{}, new byte[]{}}
    )).toString());
    assertEquals("[1, 3, 2, 1]", It.flatFromBytes(It.from(
      new byte[][]{new byte[]{}, new byte[]{1}, new byte[]{3, 2, 1}}
    )).toString());

    assertEquals("", It.join(It.from(
      new String[]{}
    ), ""));
    assertEquals("", It.join(It.from(
      new String[]{"", "", ""}
    ), ""));
    assertEquals("acba", It.join(It.from(
      new String[]{"", "a", "cba"}
    ), ""));
    assertEquals("", It.join(It.from(
      new String[]{}
    ), ":-:"));
    assertEquals(":-::-:", It.join(It.from(
      new String[]{"", "", ""}
    ), ":-:"));
    assertEquals(":-:a:-:cba", It.join(It.from(
      new String[]{"", "a", "cba"}
    ), ":-:"));
    String sp = System.getProperty("line.separator");
    assertEquals("", It.join(It.from(
      new String[]{}
    )));
    assertEquals(sp + sp, It.join(It.from(
      new String[]{"", "", ""}
    )));
    assertEquals(sp + "a" + sp + "cba", It.join(It.from(
      new String[]{"", "a", "cba"}
    )));

    assertEquals("[]", It.from(It.joinFromBytes(It.from(
      new byte[][]{}
    ))).toString());
    assertEquals("[]", It.from(It.joinFromBytes(It.from(
      new byte[][]{new byte[]{}, new byte[]{}, new byte[]{}}
    ))).toString());
    assertEquals("[1, 3, 2, 1]", It.from(It.joinFromBytes(It.from(
      new byte[][]{new byte[]{}, new byte[]{1}, new byte[]{3, 2, 1}}
    ))).toString());

  }

}
