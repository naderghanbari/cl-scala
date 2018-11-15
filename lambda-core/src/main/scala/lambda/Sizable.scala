package lambda

trait Sizable { self: Term ⇒

  /** Length of this Lambda Term.
    *
    * Aka `lgh` in the literature where:
    *  - lgh(a)    = 1                            if a is an Atom
    *  - lgh(MN)   = lgh(M) + lgh(N)
    *  - lgh(λx.M) = 1 + lgh(M)
    */
  lazy val length: Int = self match {
    case _: Atom ⇒ 1
    case _M ^ _N ⇒ _M.length + _N.length
    case _ λ _M  ⇒ 1 + _M.length
  }

}
