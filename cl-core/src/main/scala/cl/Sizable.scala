package cl

trait Sizable { self: Term ⇒

  /** Length of this CL Term.
    *
    * Aka `lgh` in the literature where:
    *  - lgh(a)  = 1                        if a is an Atom
    *  - lgh(UV) = lgh(U) + lgh(V)
    */
  lazy val length: Int = self match {
    case _: Atom ⇒ 1
    case u ^ v   ⇒ u.length + v.length
  }

}
