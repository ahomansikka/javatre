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

import com.sun.jna.*;
import static peltomaa.javatre.TRE.*;


/** Ordinary (non-fuzzy) regular expression matching using {@code tre_regwexec}.
 */
class Regex extends RE {
  /** Constructs an ordinary regular expression.
   *
   * @param regex  Regular expression.
   * @param cflags Compilation flags.
   */
  protected Regex (String regex, int cflags)
  {
    super (regex, cflags);
    nmatch = nm (regex);
    pmatch = new regmatch_t[nmatch.intValue()];
  }


  @Override
  public Matcher matcher (String string, int eflags)
  {
    return new Matcher (this, string, eflags);
  }


  /** Matches a regular expression by calling {@link TRE#tre_regwexec}.
   *
   * @param string String to be matched.
   * @param eflags Execution flags.
   *
   * @return 0, if string matches regular expression,
   *         {@code REG_NOMATCH}, if string does not match.
   */
  @Override
  public int wexec (String string, int eflags)
  {
    return getLib().tre_regwexec (preg, new WString(string), nmatch, pmatch, eflags);
  }


  @Override
  public regmatch_t[] pmatch()
  {
    return pmatch;
  }


  @Override
  protected int start (int n)
  {
    return pmatch[n].rm_so;
  }


  @Override
  protected int end (int n)
  {
    return pmatch[n].rm_eo;
  }


  @Override
  protected void setOffsets (int offset)
  {
    for (int i = 0; i < nmatch.intValue(); i++) {
//      System.out.println ("F " + start(i) + " " + end(i) + " " + offset + " " + string.substring(offset) + " " + pmatch[i].rm_so + " " + pmatch[i].rm_eo + " " + group(i));
      if (pmatch[i].rm_so >= 0) {
        pmatch[i].rm_so += offset;
        pmatch[i].rm_eo += offset;
      }
    }
  }


  @Override
  protected int nmatch()
  {
    return nmatch.intValue();
  }


  private NativeLong nmatch;
  private regmatch_t[] pmatch;
}
