package cl

trait Occurrence { X: Term ⇒

  /** "Occurs in" or "Sub-Term" relation.
    *
    * Definition 2.4 The relation X occurs in Y , or X is a subterm of Y,
    * is defined thus:
    *
    * (a) X occurs in X;
    * (b) if X occurs in U or in V, then X occurs in (UV).
    *
    * @param Y Subject term.
    * @return True if this term is a sub-term of the subject term.
    */
  final def ⊆(Y: Term): Boolean = Y match {
    case X       ⇒ true
    case _U ^ _V ⇒ (X ⊆ _U) || (X ⊆ _V)
    case _       ⇒ false
  }

  /** "Has Occurrence of" or "Super-Term" relation.
    *
    * @param Y Subject term.
    * @return True if subject term is a sub-term of this term.
    */
  final def ⊇(Y: Term) = Y ⊆ X

}
