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
import static peltomaa.javatre.TRE.*;


/** Fuzzy regular expression matching using {@code tre_regawexec}.
 */
class FuzzyRegex extends RE {
  /** Constructs a fuzzy regular expression.
   *
   * @param regex  Regular expression.
   * @param cflags Compilation flags.
   * @param params Fuzzy regular expression parameters.
   */
  protected FuzzyRegex (String regex, int cflags, regaparams_t.ByValue params)
  {
    super (regex, cflags);
    match = new regamatch_t.ByReference (nm (regex));
    this.params = params;
  }


  @Override
  public Matcher matcher (String string, int eflags)
  {
    return new Matcher (this, string, eflags);
  }


  /** Matches a regular expression by calling {@link TRE#tre_regawexec}.
   *
   * @param string String to be matched.
   * @param eflags Execution flags.
   *
   * @return 0, if string matches regular expression,
   *         {@code REG_NOMATCH}, if string does not match,
   *         {@code REG_ESPACE}, if {@code tre_regawexec()} ran out of memory.
   */
  @Override
  public int wexec (String string, int eflags)
  {
    final int matchResult = getLib().tre_regawexec (preg, new WString(string), match, params, eflags);

    regmatch_t.ByReference[] p = getPmatch (match);

    for (int i = 0; i < match.nmatch.intValue(); i++) {
      p[i].read();  // Read match data from C to Java.
//      System.out.println (string + " " + p[i].rm_so + " " + p[i].rm_eo);
    }
    return matchResult;
  }


  @Override
  public regmatch_t[] pmatch()
  {
    return getPmatch(match);
  }


  /** Returns parameters used to compile this regular expression.
   */
  @Override
  public regaparams_t aparams()
  {
    return params;
  }


  @Override
  protected int start (int n)
  {
    return getPmatch(match)[n].rm_so;
  }


  @Override
  protected int end (int n)
  {
    return getPmatch(match)[n].rm_eo;
  }


  @Override
  protected void setOffsets (int offset)
  {
    regmatch_t.ByReference[] p = getPmatch (match);

    for (int i = 0; i < match.nmatch.intValue(); i++) {
//      System.out.println ("F " + start(i) + " " + end(i) + " " + offset + " " + string.substring(offset) + " " + pmatch[i].rm_so + " " + pmatch[i].rm_eo + " " + group(i));
      if (p[i].rm_so >= 0) {
        p[i].rm_so += offset;
        p[i].rm_eo += offset;
      }
    }
  }


  @Override
  protected int nmatch()
  {
    return match.nmatch.intValue();
  }



  private regmatch_t.ByReference[] getPmatch (regamatch_t match)
  {
    return (regmatch_t.ByReference[])match.pmatch.toArray (nmatch());
  }


  private regamatch_t match;
  private regaparams_t.ByValue params;
}
