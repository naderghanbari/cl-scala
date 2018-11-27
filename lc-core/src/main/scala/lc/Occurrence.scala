package lc

trait Occurrence { P: Term ⇒

  /** "Occurs in" or "Sub-Term" relation.
    *
    * Definition 1.7 For λ-terms P and Q, the relation P occurs in Q
    * (or P is a subterm of Q, or Q contains P) is defined by
    * induction on Q, thus:
    *
    * (a) P occurs in P;
    * (b) if P occurs in M or in N, then P occurs in (MN);
    * (c) if P occurs in M or P ≡ x,then P occurs in (λx.M).
    *
    * @param Q Subject term.
    * @return True if this term is a sub-term of the subject Term.
    */
  final def ⊆(Q: Term): Boolean = Q match {
    case P       ⇒ true
    case x λ _M  ⇒ (P ≡ x) || (P ⊆ _M)
    case _M ^ _N ⇒ (P ⊆ _M) || (P ⊆ _N)
    case _       ⇒ false
  }

  /** "Has Occurrence of" or "Super-Term" relation.
    *
    * @param Q Subject term.
    * @return True if subject term is a sub-term of this term.
    */
  final def ⊇(Q: Term) = Q ⊆ P

}
