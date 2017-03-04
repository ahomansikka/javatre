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


/** Match regular expressions to strings.<p>
 *
 *  A matcher is created by calling method
 *  {@link RE#matcher(java.lang.String,int)}
 *  or method {@link RE#matcher(java.lang.String)}.<p>
 *
 *  A matcher can do four different kind of match operations. See
 * {@link #find()}, {@link #lookingAt()}, {@link #matches()}, and {@link #wmatch()}.
 */
public class Matcher {
  /** Constructor.
   *
   * @param re     Regular expression.
   * @param string String to be matched.
   * @param eflags Execution flags.
   */
  protected Matcher (RE re, String string, int eflags)
  {
    this.re = re;
    this.string = string;
    this.eflags = eflags;
//System.out.println ("Matcher1 " + string + " " + re.pattern());
  }


  /** Constructor. This is equal to {@code Matcher (re, string, 0)}.<p>
   *
   * @param re     Regular expression.
   * @param string String to be matched.
   */
  protected Matcher (RE re, String string)
  {
    this (re, string, 0);
  }


  /** Returns the offset of the first character after the match.
   *  This is equal to {@code end(0)}.
   */
  public int end()
  {
    return end (0);
  }


  /** Returns the offset of the first character after {@code n}th submatch within the string.
   *  Returns -1 if {@code n}th submatch does not exist.
   *
   * @param n Index of the submatch.
   */
  public int end (int n)
  {
    return re.end (n);
  }


  /** Returns the substring matched by this regular expression.
   *  This is equal to {@code group(0)}.
   */
  public String group()
  {
    return group (0);
  }


  /** Returns the {@code n}th submatch.
   *
   * @param n Index of the submatch.
   */
  public String group (int n)
  {
    return string.substring (start(n), end(n));
  }


  /** Returns the size of submatch array.
   */
  public int nmatch()
  {
    return re.nmatch();
  }


  /** Returns the start offset of the match.
   *  This is equal to {@code start(0)}.
   */
  public int start()
  {
    return start (0);
  }


  /** Returns the start offset of the {@code n}th submatch within the string.
   *  Returns -1 if {@code n}th submatch does not exist.
   *
   * @param n Index of the submatch.
   */
  public int start (int n)
  {
    return re.start (n);
  }


  /** Returns the string the regex was matched against.
   */
  public String string() {return string;}


  /** Returns the regular expression that this Matcher uses as a string.
   */
  public String pattern() {return re.pattern();}


  /** Returns the regular expression that this Matcher uses.
   */
  public RE regex() {return re;}


  /** Matches a string against the regular expression. 
   *
   * @return true if any part of the string matches the regular expression;
   *         otherwise false. This is how {@code reg_*exec()}
   * functions work in TRE.
   */
  public boolean wmatch() {return re.wmatch (string, eflags);}


  /** Matches entire string against the regular expression.
   *
   * @return  true if entire string matches the regex.
   */
  public boolean matches()
  {
    final boolean b = re.wmatch (string, eflags);
    return (b && (start(0) == 0) && (end(0) == string.length()));
  }


  /** Matches start of the string against the regular expression.
   *
   * @return {@code true} if start of the string matches the regex.
   */
  public boolean lookingAt()
  {
    final boolean b = re.wmatch (string, eflags);
    return (b && (start(0) == 0));
  }


  /** Finds the next substring that matches the regular expression.
   */
  public boolean find()
  {
//System.out.println ("F " + string.substring(findOffset) + " " + re.pattern() + " " + (pmatch == null));

    if (firstCall) {
      firstCall = false;
      findOffset = 0;
    }
    else {
      findOffset = end();
    }

    if (findOffset >= string.length()) {
      return false;
    }

    boolean b = re.wmatch (string.substring(findOffset), eflags);
    if (!b) {
      return false;
    }

    re.setOffsets (findOffset);

    return true;
  }


  /** Resets this matcher; that is, deletes its state information.
   */
  public Matcher reset()
  {
    this.findOffset = 0;
    this.firstCall = true;
    return this;
  }


  /** Resets this matcher with a new string.
   *
   * @param string String to be matched.
   * @param eflags Execution flags.
   */
  public Matcher reset (String string, int eflags)
  {
    this.string = string;
    this.eflags = eflags;
    return reset();
  }


  /** Non-terminal append-and-replace step.<p>
   *  See {@code void test6()} in AppTest.java for an example.
   */
  public Matcher appendReplacement (StringBuffer sb, String replacement)
  {
    sb.append (string.substring (findOffset,start()))
      .append (parseReplacement (replacement));
//System.out.println ("app  " + findOffset + " " + start() + " " + end() + " " + replacement + " " + parseReplacement(replacement));
    return this;
  }


  /** Terminal append-and-replace step.<p>
   *  See {@code void test6()} in AppTest.java for an example.
   */
  public Matcher appendTail (StringBuffer sb)
  {
    sb.append (string.substring(end()));
//System.out.println ("tail " + findOffset + " " + start() + " " + end() + " " + string.substring(end()));
    return this;
  }


  /** Replaces all matches with replacement string.
   *
   * @param replacement Replacement string.
   */
  String replaceAll (String replacement)
  {
    reset();
    StringBuffer sb = new StringBuffer();
    while (find()) {
      appendReplacement (sb, "X");
    }
    appendTail (sb);
    return sb.toString();
  }


  /** Prints Matcher data to stream.
   *
   * @param out Output stream.
   * @param m   Data to be printed.
   */
  public static void print (java.io.PrintStream out, Matcher m)
  {
    if (m == null) return;

    out.println ("nmatch " + m.nmatch());
    for (int i = 0; i < m.nmatch(); i++) {
      out.println (m.start(i) + " " + m.end(i) + " " + m.group(i));
    }
  }


  private String parseReplacement (String s)
  {
    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < s.length(); i++) {
      if (s.charAt(i) == '\\') {
        if ((i == s.length() - 1) || (s.charAt(i+1) != '$')) {
          sb.append ('\\');
        }
        else {
          sb.append ('$');
          i++;
        }
      }
      else if (s.charAt(i) == '$') {
        if (i == s.length()-1) {
          throw new IllegalArgumentException ("Unquoted '$' can not be the last character in replacement string.");
        }
        else if (isLatin1Digit (s.charAt(i+1))) {
          int k = i + 1;
          while (k < s.length() && isLatin1Digit (s.charAt(k))) {
            k++;
          }
          final int g = Integer.valueOf (s.substring (i+1, k)).intValue();
          if (groupOK (g)) {
            sb.append (group (g));
            i = k;
          }
          else {
            throw new IndexOutOfBoundsException ("No group number " + g + ".");
          }
        }
        else {
          throw new IllegalArgumentException ("Not a group number.");
        }
      }
      else {
        sb.append (s.charAt(i));
      }
    }
    return sb.toString();
  }


  private boolean isLatin1Digit (char ch)
  {
    switch (ch) {
      case '0':
      case '1':
      case '2':
      case '3':
      case '4':
      case '5':
      case '6':
      case '7':
      case '8':
      case '9':
        return true;
      default:
        return false;
    }
  }


  private boolean groupOK (int g)
  {
    if (g >= nmatch()) {
      return false;
    }
    else {
      return (start(g) >= 0);
    }
  }

  private RE re;
  private String string;
  private int eflags;
  private int findOffset = 0;  /* Index to start searching in find(). */
  private boolean firstCall = true;
}
