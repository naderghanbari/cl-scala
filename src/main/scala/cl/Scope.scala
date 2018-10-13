package cl

trait Scope { self: Term =>

  /** Set of Free Variables of this term.
    *
    * @return Set of Free Variables of this term.
    */
  final def FV: Set[Var] = self match {
    case x: Var => Set(x)
    case u $ v => u.FV union v.FV
    case _ => Set.empty
  }

  /** "Occurs in" or "Sub-Term" relation.
    *
    * @param that Subject term.
    * @return True if this term is a sub-term of the subject term.
    */
  final def ⊆(that: Term): Boolean =
    if (self.length > that.length) false
    else that match {
      case `self` => true
      case u $ _ if self ⊆ u => true
      case _ $ v if self ⊆ v => true
      case _ => false
    }

  /** "Has Occurrence of" or "Super-Term" relation.
    *
    * @param that Subject term.
    * @return True if subject term is a sub-term of this term.
    */

  final def ⊇(that: Term) = that ⊆ self

}
