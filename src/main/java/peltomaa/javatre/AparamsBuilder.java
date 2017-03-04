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


/** Build parameters for approximate (fuzzy) matching.<p>
 *
 * Example:
 * <pre>
 * {@code
 *   TRE.regaparams_t p = new AparamsBuilder().cost_ins(1).cost_del(1).cost_subst(1)
 *                      .max_cost(1).max_ins(1).max_del(1).max_subst(1).max_err(1).build();
 * }
 * </pre>
 *
 * (of course, the arguments can be something else than 1 :-)
 * or, more shortly
 * <pre>
 * {@code
 *   TRE.regaparams_t p = new AparamsBuilder()
 *                          .max_cost(1).max_ins(1).max_del(1).max_subst(1).max_err(1).build();
 * }
 * </pre>
 *
 * Default value for {@code cost_ins}, {@code cost_del} and {@code cost_subst} is 1.
 *
 * @see <a href="TRE.regaparams_t.html">regaparams_t</a>
 */
public class AparamsBuilder {
  private TRE.regaparams_t.ByValue p;


  public AparamsBuilder()
  {
    p = new TRE.regaparams_t.ByValue();
  }


  public AparamsBuilder cost_ins (int cost_ins)
  {
    p.cost_ins = cost_ins;
    return this;
  }


  public AparamsBuilder cost_del (int cost_del)
  {
    p.cost_del = cost_del;
    return this;
  }


  public AparamsBuilder cost_subst (int cost_subst)
  {
    p.cost_subst = cost_subst;
    return this;
  }


  public AparamsBuilder max_cost (int max_cost)
  {
    p.max_cost = max_cost;
    return this;
  }


  public AparamsBuilder max_ins (int max_ins)
  {
    p.max_ins = max_ins;
    return this;
  }


  public AparamsBuilder max_del (int max_del)
  {
    p.max_del = max_del;
    return this;
  }


  public AparamsBuilder max_subst (int max_subst)
  {
    p.max_subst = max_subst;
    return this;
  }


  public AparamsBuilder max_err (int max_err)
  {
    p.max_err = max_err;
    return this;
  }


  /** Returns the parameters.
   */
  public TRE.regaparams_t.ByValue build()
  {
    return p;
  }


  /** Convenience method that sets
   * {@code max_cost}, {@code max_ins}, {@code max_del},
   * {@code max_subst} and {@code max_err} to 1.
   */
  public static final TRE.regaparams_t.ByValue build1()
  {
     return new AparamsBuilder()
            .max_cost(1).max_ins(1).max_del(1).max_subst(1).max_err(1).build();
  }
}
