package cl

import cl.Reduction._

trait WeakEquality { self: Term =>

  /** "Is Weakly Equal to" or "Is Weakly Convertible to" relation.
    *
    * Definition 2.29 in Hindley.
    *
    * Incomplete: Works only for Terms with weak normal forms! Uses Corollary 2.32.2:
    * "If X =w Y , then either X and Y have no weak normal form, or they both have the same weak normal form."
    *
    * @param V Subject Term.
    * @return True if this Term Weakly Equals to the Subject Term.
    */
  def weakEquals(V: Term): Boolean = reduceToWeakNormalForm(self) == reduceToWeakNormalForm(V)

}
