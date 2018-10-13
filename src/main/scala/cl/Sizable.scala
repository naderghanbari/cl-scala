package cl

trait Sizable { self: Term =>

  /** Length of this CL term.
    *
    * Aka `lgh` in the literature where:
    *  - lgh(a) = 1                        if a is an atom
    *  - lgh(UV) = lgh(U) + lgh(V)         if U and V are CL terms
    */
  lazy val length: Int = self match {
    case _: Atom => 1
    case $(u, v) => u.length + v.length
  }

}
