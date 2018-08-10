/*
Copyright (©) 2017-2018 Hannu Väisänen

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

import com.sun.jna.*;
import com.sun.jna.ptr.*;
import java.util.Arrays;
import java.util.List;
import java.io.PrintStream;


/** Java mappings for functions and structures in
    <a href = "http://laurikari.net/tre/">TRE</a> library.<p>

 For documentation, see
 <a href = "http://laurikari.net/tre/documentation/regcomp/">Compiling Regexps</a>,
 <a href = "http://laurikari.net/tre/documentation/regexec/">Searching for Matches</a>, and
 <a href = "http://laurikari.net/tre/documentation/utilities/">Utility Functions and Macros</a>,
 or file {@code doc/tre-api.html} in the source distribution of
 <a href = "http://laurikari.net/tre/">TRE</a>.<p>

 Regexp syntax is described in file {@code doc/tre-syntax.html} in the source distribution of
 <a href = "http://laurikari.net/tre/">TRE</a>.<p>

You can use these bindigs directly like this
<PRE>
TRE lib = RE.getLib();
regex_t preg = new tre.regex_t();
int n = tre.tre_regwcomp (preg, ...);
n = tre.tre_regwexec (preg, ...);
</PRE>

but it is better to use classes {@link RE} and {@link Matcher}<p>

<PRE>
RE r = RE.compile (...);
Matcher m = r.matcher (...);
if (m.matches()) ...
</PRE>
See file {@code src/test/java/peltomaa/javatre/JavaTRETest.java} for examples.
*/
public interface TRE extends Library {

  /** Mapping for {@code regmatch_t}. */
  public static class regmatch_t extends Structure {
    public static class ByReference extends regmatch_t implements Structure.ByReference {}

    /** Offset from start of string to start of substring. */
    public int rm_so;  // We assume that regoff_t is int.

    /** Offset from start of string to the first character after the substring. */
    public int rm_eo;

    protected List<String> getFieldOrder()
    {
      return Arrays.asList (new String[] {"rm_so", "rm_eo"});
    }

    public regmatch_t() {rm_so = -1; rm_eo = -1;}
  }


  /** Mapping for {@code regex_t}. */
  public static class regex_t extends Structure {
    public static class ByReference extends regex_t implements Structure.ByReference {}

    /** Number of parenthesized subexpressions. */
    public NativeLong re_nsub;

    /** For internal use only. */
    public Pointer value;

    protected List<String> getFieldOrder()
    {
      return Arrays.asList (new String[] {"re_nsub", "value"});
    }
  }



  /** Mapping for {@code regaparams_t}.
   *  @see <a href="AparamsBuilder.html">AparamsBuilder</a>
   */
  public static class regaparams_t extends Structure {
    public static class ByValue extends regaparams_t implements Structure.ByValue {}

// Initial values are from TRE source code,
// file regexec.c, function tre_regaparams_default(regaparams_t *params)

    /** Default cost of an inserted character. */
    public int cost_ins = 1;

    /** Default cost of a deleted character. */
    public int cost_del = 1;

    /** Default cost of a substituted character. */
    public int cost_subst = 1;

    /** Maximum allowed cost of a match. */
    public int max_cost = Integer.MAX_VALUE;

    /** Maximum allowed number of inserts. */
    public int max_ins = Integer.MAX_VALUE;

    /** Maximum allowed number of deletes. */
    public int max_del = Integer.MAX_VALUE;

    /** Maximum allowed number of substitutes. */
    public int max_subst = Integer.MAX_VALUE;

    /** Maximum allowed number of errors total. */
    public int max_err = Integer.MAX_VALUE;

    protected List<String> getFieldOrder()
    {
      return Arrays.asList (new String[] {
        "cost_ins", "cost_del", "cost_subst", "max_cost",
        "max_ins",  "max_del",  "max_subst",  "max_err"
      });
    }

    /** Prints field values to stream.
     *
     * @param out Output stream.
     */
    public void print (PrintStream out)
    {
      out.println ("cost_ins   " + cost_ins);
      out.println ("cost_del   " + cost_del);
      out.println ("cost_subst " + cost_subst);
      out.println ("max_cost   " + max_cost);
      out.println ("max_ins    " + max_ins);
      out.println ("max_del    " + max_del);
      out.println ("max_subst  " + max_subst);
      out.println ("max_err    " + max_err);
    }
  }


  /** Mapping for {@code regamatch_t}. */
  public static class regamatch_t extends Structure {
    public static class ByReference extends regamatch_t implements Structure.ByReference {
      /**
       * Constructor.
       *
       * @param n Size of submatch data array (pmatch).
       */
      public ByReference (NativeLong n)
      {
        nmatch = n;
        if (n.longValue() == 0) {
          pmatch = null;
        }
        else {
          regmatch_t.ByReference r = new regmatch_t.ByReference();
          regmatch_t.ByReference[] p = (regmatch_t.ByReference[])r.toArray (n.intValue());
          pmatch = p[0];
        }
      }
    }

    /** Length of pmatch[] array. */
    public NativeLong nmatch;

    /** Submatch data. */
    public regmatch_t.ByReference pmatch;   /* C: regmatch_t *pmatch; */


    /** Cost of the match. */
    public int cost;

    /** Number of inserts in the match. */
    public int num_ins;

    /** Number of deletes in the match. */
    public int num_del;

    /** Number of substitutes in the match. */
    public int num_subst;

    protected List<String> getFieldOrder()
    {
      return Arrays.asList (new String[] {
        "nmatch", "pmatch", "cost", "num_ins", "num_del", "num_subst"
      });
    }

    private regamatch_t() {}
  }


// We use only wchar_t* which is WString in JNA.

  public int tre_regcomp  (regex_t preg,  String regex, int cflags);
  public int tre_regwcomp (regex_t preg, WString regex, int cflags);

  public void tre_regfree (regex_t preg);

  public int tre_regexec  (regex_t preg,  String string, NativeLong nmatch, regmatch_t[] pmatch, int eflags);
  public int tre_regwexec (regex_t preg, WString string, NativeLong nmatch, regmatch_t[] pmatch, int eflags);

  public int tre_regaexec  (regex_t preg,  String string, regamatch_t match, regaparams_t.ByValue params, int eflags);
  public int tre_regawexec (regex_t preg, WString string, regamatch_t match, regaparams_t.ByValue params, int eflags);

  public NativeLong tre_regerror (int error_code, regex_t preg, byte[] errbuf, NativeLong errbuf_size);


  public int tre_have_backrefs (regex_t preg);
  public int tre_have_approx (regex_t preg);


  /** Returns the version and the license of TRE library.
   */
  public String tre_version();

  /** Returns data on how TRE has been compiled.<p>
   *
   * An example on how this function should be called is in
   * file {@code src/test/java/peltomaa/javatre/TreConfigTest.java}.
   */
  public int tre_config (int query, Pointer result);


  public static final int REG_OK = 0;

  /* POSIX tre_regcomp() return error codes.  (In the order listed in the
     standard.)  */
  public static final int REG_NOMATCH = 1;
  public static final int REG_BADPAT = 2;
  public static final int REG_ECOLLATE = 3;
  public static final int REG_ECTYPE = 4;
  public static final int REG_EESCAPE = 5;
  public static final int REG_ESUBREG = 6;
  public static final int REG_EBRACK = 7;
  public static final int REG_EPAREN = 8;
  public static final int REG_EBRACE = 9;
  public static final int REG_BADBR = 10;
  public static final int REG_ERANGE = 11;
  public static final int REG_ESPACE = 12;
  public static final int REG_BADRPT = 13;


  /* POSIX tre_regcomp() flags. */
  public static final int REG_EXTENDED = 1;
  public static final int REG_ICASE = 2;
  public static final int REG_NEWLINE = 4;
  public static final int REG_NOSUB = 8;


  /* Extra tre_regcomp() flags. */
  public static final int REG_BASIC = 0;
  public static final int REG_LITERAL = 16;
  public static final int REG_RIGHT_ASSOC = 32;
  public static final int REG_UNGREEDY = 64;
  public static final int REG_USEBYTES = 128;


  /* POSIX tre_regexec() flags. */
  public static final int REG_NOTBOL = 1;
  public static final int REG_NOTEOL = 2;


  /* Extra tre_regexec() flags. */
  public static final int REG_APPROX_MATCHER = 4;
  public static final int REG_BACKTRACKING_MATCHER = 8;

  public static final int REG_NOSPEC = REG_LITERAL;

  public static final int TRE_CONFIG_APPROX = 0;
  public static final int TRE_CONFIG_WCHAR = 1;
  public static final int TRE_CONFIG_MULTIBYTE = 2;
  public static final int TRE_CONFIG_SYSTEM_ABI = 3;
  public static final int TRE_CONFIG_VERSION = 4;
}
