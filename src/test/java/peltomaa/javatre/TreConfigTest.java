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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * An example on how to call {@code TRE.tre_config (int query, Pointer result)}.
 */
public class TreConfigTest {
  private TRE lib;


  @Before
  public void setUp()
  {
    lib = RE.getLib();
  }


  @Test
  public void TreConfig()
  {
    Pointer result = new Memory (Pointer.SIZE);

    // Result is a pointer to int.
    //
    int n = lib.tre_config (TRE_CONFIG_APPROX, result);
    if (n == 0) System.out.println ("approx = " + result.getInt(0));

    // Result is a pointer to int.
    //
    n = lib.tre_config (TRE_CONFIG_WCHAR, result);
    if (n == 0) System.out.println ("wchar = " + result.getInt(0));

    // Result is a pointer to int.
    //
    n = lib.tre_config (TRE_CONFIG_MULTIBYTE, result);
    if (n == 0) System.out.println ("multibyte = " + result.getInt(0));

    // Result is a pointer to int.
    //
    n = lib.tre_config (TRE_CONFIG_SYSTEM_ABI, result);
    if (n == 0) System.out.println ("system_abi = " + result.getInt(0));

    // Result is a pointer to character string; "result.getPointer(0)" gets
    // the pointer, and then "getString(0)" gets the string.
    //
    n = lib.tre_config (TRE_CONFIG_VERSION, result);
    if (n == 0) System.out.println ("version = " + result.getPointer(0).getString(0));

    assertTrue (lib.tre_config (-1, result) == REG_NOMATCH);
  }
}
