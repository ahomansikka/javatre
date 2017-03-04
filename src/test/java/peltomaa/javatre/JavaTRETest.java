/*
Copyright (©) 2017 Hannu Väisänen

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package peltomaa.javatre;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static peltomaa.javatre.TRE.*;

/**
 * Tests for JavaTRE.
 */
public class JavaTRETest
{
  private TRE.regaparams_t.ByValue p;
  private String s;


  @Before
  public void setUp()
  {
    p = new AparamsBuilder().cost_ins(1).cost_del(1).cost_subst(1)
        .max_cost(1).max_ins(1).max_del(1).max_subst(1).max_err(1).build();
    s = "xxxxxxxxx kukka yyyyyyyyyyyy";
  }


  @After
  public void tearDown()
  {
  }


  @Test
  public void initAndTerminate()
  {
    // Do nothing, just check that setUp and tearDown complete successfully.
  }


  @Test
  public void test1()
  {
    final String r = "tukka";
    final RE re = RE.compile (r, REG_EXTENDED, p);

    final int n = re.wexec (s, 0);
    assertTrue (n == 0);
  }


  @Test
  public void test2()
  {
    final String r = "kuka";
    final RE re = RE.compile (r, REG_EXTENDED, p);
    assertTrue (re.wmatch (s, 0));
  }


  @Test
  public void test3()
  {
    final String r = "kuka";
    final RE re = RE.compile (r, REG_EXTENDED, p);
    final Matcher m = re.matcher (s);
    assertTrue (!m.matches());
    assertTrue (!m.lookingAt());
   }


  @Test
  public void test4()
  {
    RE re = RE.compile ("tukka", REG_EXTENDED, p);
    Matcher m = re.matcher ("kukka");
    assertTrue (m.matches());
  }


  @Test
  public void test5()
  {
    RE re = RE.compile ("tukka", REG_EXTENDED, p);
    Matcher m = re.matcher ("kukka xxxx");
    assertTrue (m.lookingAt());
  }


  @Test
  public void test6()
  {
    String z = "To be or not to be or bee.";
    RE re = RE.compile ("be", REG_EXTENDED, p);

    Matcher m = re.matcher (z);
    StringBuffer sb = new StringBuffer();
    while (m.find()) {
      m.appendReplacement (sb, "X");
    }
    m.appendTail (sb);
    assertTrue ("To X or not to X or XX.".equals(sb.toString()));
  }


  @Test
  public void test7()
  {
    final String s = "aaxbbbxxcccxdddx";
    final String[] u = {"aa", "bbb", "", "ccc", "ddd"};
    RE re = RE.compile ("x", REG_EXTENDED, p);
    String[] p = re.split (s, 0);
    for (int i = 0; i < p.length; i++) {
//      System.out.println (p[i]);
      assertTrue (u[i].compareTo(p[i]) == 0);
    }
  }


  @Test
  public void test8()
  {
    final String s = "aabbbcccddd";
    RE re = RE.compile ("x", REG_EXTENDED); // Non-fuzzy regex.
    String[] u = re.split (s, 0);
    assertTrue (u[0].compareTo(s) == 0);
  }


  @Test
  public void test9()
  {
    final String s = "aabbbcccddd";
    RE re = RE.compile ("x", REG_EXTENDED, p); // Fuzzy regex.
    String[] u = re.split (s, 0);
    assertTrue (u.length == s.length());
    for (int i = 0; i < u.length; i++) {
      assertTrue (u[i].compareTo("") == 0);
    }
  }


  @Test
  public void test10()
  {
    String z = "To be or not to be or bee.";
    RE re = RE.compile ("be", REG_EXTENDED, p);
    Matcher m = re.matcher (z);
    String s = m.replaceAll ("X");
    assertTrue ("To X or not to X or XX.".equals(s));
  }


  @Test
  public void test11()
  {
    String z = "Testataan ääkkösiä.";
    RE re = RE.compile ("([äö])+", REG_EXTENDED, p);
    Matcher m = re.matcher (z);

    String[] u = {"ää", "ö", "ä", "."};
    int s[] = {10, 14, 17, 18};
    int e[] = {12, 15, 18, 19};
    int i = 0;
    while (m.find()) {
      assertTrue (m.group().compareTo(u[i]) == 0);
      assertTrue (m.start() == s[i]);
      assertTrue (m.end() == e[i]);
      i++;
//      System.out.println (m.nmatch() + " " + m.start() + " " + m.end() + " " + m.group());
    }
  }


  @Test
  public void test12()
  {
    // This test is copied from TRE's Python binding.

    TRE.regaparams_t.ByValue p = new AparamsBuilder().max_err(3).build();
    RE re = RE.compile ("Don(ald( Ervin)?)? Knuth", REG_EXTENDED, p);
    String data = "\nIn addition to fundamental contributions in several branches of\n" +
                  "theoretical computer science, Donnald Erwin Kuth is the creator of the\n" +
                  "TeX computer typesetting system, the related METAFONT font definition\n" +
                  "language and rendering system, and the Computer Modern family of\n" +
                  "typefaces.\n\n";
    Matcher m = re.matcher (data);
    if (m.wmatch()) {
      int[] s = { 95,  99, 102};
      int[] e = {113, 108, 108};
      for (int i = 0; i < m.nmatch(); i++) {
        assertTrue (m.start(i) == s[i]);
        assertTrue (m.end(i) == e[i]);
//        System.out.println (m.start(i) + " " + m.end(i));
      }
      assertTrue (m.group().compareTo("Donnald Erwin Kuth") == 0);
    }
  }
}
