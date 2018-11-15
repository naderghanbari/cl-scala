package cl

trait Scope { self: Term ⇒

  /** Set of Free Variables of this term.
    *
    * @return Set of Free Variables of this term.
    */
  lazy val FV: Set[Var] = self match {
    case x: Var ⇒ Set(x)
    case u ^ v  ⇒ u.FV union v.FV
    case _      ⇒ Set.empty
  }

  /** Determines if a Term is closed or not.
    *
    * Closed Terms are Terms without any Free Variable.
    */
  lazy val isClosed: Boolean = FV.isEmpty

}
