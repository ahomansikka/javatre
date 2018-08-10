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
import java.util.ArrayList;
import java.util.List;
import static peltomaa.javatre.TRE.*;


/**
 * Regular expression class.
 */
public abstract class RE {
  /** Compiles an ordinary regular expression.
   *
   * @param regex  Regular expression.
   * @param cflags Compilation flags.
   *
   * @throws RegexSyntaxException if the syntax of regex is incorrect.
   */
  public static final RE compile (String regex, int cflags)
  {
    return new Regex (regex, cflags);
  }


  /** Compiles an ordinary regular expression.<p>
   *
   * This is equal to {@code compile (regex, REG_EXTENDED)}.
   *
   * @param regex Regular expression.
   *
   * @throws RegexSyntaxException if the syntax of regex is incorrect.
   */
  public static final RE compile (String regex)
  {
    return new Regex (regex, REG_EXTENDED);
  }


  /** Compiles an approximate (fuzzy) regular expression.
   *
   * @param regex  Regular expression.
   * @param cflags Compilation flags.
   * @param params Fuzzy regular expression parameters.
   *
   * @throws RegexSyntaxException if the syntax of regex is incorrect.
   */
  public static final RE compile (String regex, int cflags, regaparams_t.ByValue params)
  {
    return new FuzzyRegex (regex, cflags, params);
  }


  /** Compiles an approximate (fuzzy) regular expression.<p>
   *
   * This is equal to {@code compile(regex, REG_EXTENDED, params)}.
   *
   * @param regex  Regular expression.
   * @param params Fuzzy regular expression parameters.
   *
   * @throws RegexSyntaxException if the syntax of regex is incorrect.
   */
  public static final RE compile (String regex, regaparams_t.ByValue params)
  {
    return new FuzzyRegex (regex, REG_EXTENDED, params);
  }


  /** Returns a Matcher object that matches string to this regular expression.
   *
   *  @param string String to be matched.
   *  @param eflags Execution flags.
   */
  public abstract Matcher matcher (String string, int eflags);



  /** Returns a Matcher object that matches string to this regular expression.
   *  This function is equal to {@code matcher(string,0)}.
   *
   *  @param string String to be matched.
   */
  public Matcher matcher (String string)
  {
    return matcher (string, 0);
  }


  /** Matches a regular expression by calling some function from the
   *  <a href = "http://laurikari.net/tre/">TRE</a> library.<p>
   *
   * For fuzzy regex, this methods calls {@code TRE.tre_regawexec},
   * and for ordinary regex, this method calls {@code TRE.tre_regwexec}.<p>
   *
   * String matches a regular expression if any part of the string matches.
   *
   * @param string String to be matched.
   * @param eflags Execution flags.
   *
   * @return 0, if string matches regular expression,
   *         {@code REG_NOMATCH}, if string does not match,
   *         or an error code, if an error occurs.
   */
  public abstract int wexec (String string, int eflags);


  /** Matches a regular expression.<p>
   *
   * This is a convenience method that calls {@link #wexec(java.lang.String,int)} and returns
   * {@code boolean} instead of {@code int}.
   *
   * @param string String to be matched.
   * @param eflags Execution flags.
   *
   * @return {@code true} if string matches regular expression, othewise returns {@code false}.
   *
   * @throws RegexSyntaxException if {@link #wexec(java.lang.String,int)} returns an error code.
   */
  public boolean wmatch (String string, int eflags)
  {
    final int matchResult = wexec (string, eflags);
    switch (matchResult) {
      case 0: return true;
      case REG_NOMATCH: return false;
      default: throw new RegexSyntaxException (getMessage (matchResult));
    }
  }


  /** Matches a regular expression.<p>
   *
   * This function is equal to {@code wmatch (string,0)}.
   *
   * @throws RegexSyntaxException if {@link #wexec(java.lang.String,int)} returns an error code.
   */
  public boolean wmatch (String string)
  {
    return wmatch (string, 0);
  }


  /** Compiles an ordinary {@code regex} and returns {@code true} if {@code string} matches it.<p>
   *
   * This code is equal to
   *
   * <pre>
   * {@code
   * RE re = RE.compile (regex, cflags);
   * return re.wmatch (string, eflags);
   * }
   * </pre>
   *
   * @param regex  Regular expression.
   * @param cflags Compilation flags.
   * @param string String to be matched.
   * @param eflags Execution flags.
   *
   * @throws RegexSyntaxException if the syntax of regex is incorrect.
   */
  public static final boolean wmatch (String regex, int cflags, String string, int eflags)
  {
    RE re = RE.compile (regex, cflags);
    return re.wmatch (string, eflags);
  }


  /** Compiles an approximate {@code regex} and returns {@code true} if {@code string} matches it.<p>
   *
   * This code is equal to
   *
   * <pre>
   * {@code
   * RE re = RE.compile (regex, cflags, params);
   * return re.wmatch (string, eflags);
   * }
   * </pre>
   *
   * @param regex  Regular expression.
   * @param cflags Compilation flags.
   * @param params Fuzzy regular expression parameters.
   * @param string String to be matched.
   * @param eflags Execution flags.
   *
   * @throws RegexSyntaxException if the syntax of regex is incorrect.
   */
  public static final boolean wmatch (String regex, int cflags, regaparams_t.ByValue params, String string, int eflags)
  {
    RE re = RE.compile (regex, cflags, params);
    return re.wmatch (string, eflags);
  }



  /** Returns the cflags that were used to compile this regex.
   */
  public int cflags()
  {
    return cflags;
  }


  /** Returns an array of <a href="TRE.regmatch_t.html">regmatch_t</a> objects
   *  that contain offsets of the submatches of regular expression.
   *
   * @see <a href="TRE.regmatch_t.html">regmatch_t</a>
   */
  public abstract regmatch_t[] pmatch();


  /** Returns the approximate matching parameter struct
   *  used to compile {@code this} regexp. If {@code this}
   *  is an ordinary regexp returns {@code null}.
   */
  public regaparams_t aparams()
  {
    return null;
  }


  /** Returns the start offset of the {@code n}th submatch within the string.
   *  Returns -1 if {@code n}th submatch does not exist.
   *
   * @param n Index of the submatch.
   */
  protected abstract int start (int n);


  /** Returns the offset of the first character after {@code n}th submatch within the string.
   *  Returns -1 if {@code n}th submatch does not exist.
   *
   * @param n Index of the submatch.
   */
  protected abstract int end (int n);


  protected abstract void setOffsets (int offset);


  /** Returns the size of the submatch array. */
  protected abstract int nmatch();


  /** Calculates the size of array that contains match addressing information.
   *  The return value is the number of left parenthesis "(" in the
   *  regular expresion + 1.
   *
   * @param re Regular expression.
   * @return   Size of array.
   * @see      <a href="TRE.regmatch_t.html">regmatch_t</a>
   */
  protected NativeLong nm (String re)
  {
    long n = 1; // Entire regular expression.
    for (int i = 0; i < re.length(); i++) {
      if (re.charAt(i) == '(') n++;
    }
    return new NativeLong (n);
  }


  public void regfree() {getLib().tre_regfree (preg);}

/*
  @Override
  protected void finalize()
  {
    // This causes JVM crash!
    getLib().tre_regfree (preg);
  }
*/


  /** Returns the regular expression.
   */
  public String pattern()
  {
    return string;
  }


  private final String getMessage (int errcode)
  {
     byte[] errbuf = new byte[1000];
     NativeLong errbuf_size = new NativeLong (errbuf.length);
     getLib().tre_regerror (errcode, preg, errbuf, errbuf_size);
     return Native.toString (errbuf);
  }


  /**
   * Splits a string around matches of this regex.<p>
   *
   * For an example, see {@code void test7()} in {@code peltomaa.javatre.JavaTRETest.java}.
   *
   * @param string String to split.
   * @param eflags Execution flags.
   */
  public String[] split (String string, int eflags)
  {
    List<String> list = new ArrayList();
    Matcher m = matcher (string, eflags);

    int offset = 0;

    while (m.find()) {
      list.add (string.substring (offset, m.start()));
      offset = m.end();
    }

    return list.toArray (new String[0]);
  }



  /** Constructor.
   *
   * @param regex  Regular expression.
   * @param cflags Compilation flags.
   *
   * @throws RegexSyntaxException if syntax of regex is incorrect (if
   *         {@code TRE.tre_regwcomp(regex_t preg, WString regex, int cflags)} returns an error code).
   */
  protected RE (String regex, int cflags)
  {
    this.cflags = cflags;
    this.string = regex;
    this.preg = new regex_t();
    this.regex = new WString (regex);
    int errcode = getLib().tre_regwcomp (preg, this.regex, cflags);
//System.out.println ("errcode = " + errcode);
    if (errcode != 0) {
      throw new RegexSyntaxException (getMessage (errcode));
    }
  }


  private static Library library = null;
  private int cflags;
  private String string;
  private WString regex;
  protected regex_t preg;


  // This code is modified from org.puimula.libvoikko.Voikko
  // See http://voikko.puimula.org/java.html
  //
 
  private static NativeLibrary tryLoadLibrary (String libName)
  {
    try {
      return NativeLibrary.getInstance (libName);
    } catch (UnsatisfiedLinkError e) {
      return null;
    }
  }

  private static final String[] LIBRARY_NAMES = {
    "libtre.so.5", // Use SONAME on Linux.
    "tre-5",       // On Windows major version of library API is part of the library name.
    "tre"          // Finally try to use platform dependent unversioned library.
  };


  /** Gets the native component of JavaTRE.
   */
  public synchronized static TRE getLib()
  {
    if (library == null) {
      NativeLibrary.addSearchPath ("tre", "/usr/local/lib");
      for (String libName : LIBRARY_NAMES) {
        NativeLibrary nativeLibrary = tryLoadLibrary (libName);
        if (nativeLibrary != null) {
          library = (TRE) Native.loadLibrary (nativeLibrary.getFile().getPath(), TRE.class);
          return (TRE)library;
        }
      }
      throw new UnsatisfiedLinkError ("Could not load the native component of javatre.");
    }
    return (TRE)library;
  }


  /**
   * Sets the explicit path to the folder containing shared library files.
   * @param libraryPath
   */
  public static final void addLibraryPath (String libraryPath)
  {
    for (String libraryName : LIBRARY_NAMES)
    {
      NativeLibrary.addSearchPath (libraryName, libraryPath);
    }
  }
}
