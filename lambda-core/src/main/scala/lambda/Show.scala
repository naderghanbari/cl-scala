package lambda

trait Show { this: Term ⇒

  /** Full (official non-ambiguous) notation of this Lambda Term.
    *
    * Fully parenthesized, as in ((λx.yx)(zz)).
    */
  lazy val full: String = this match {
    case Atom(name)  ⇒ name.toString
    case _U $ _V     ⇒ s"(${_U.full}${_V.full})"
    case Var(x) λ _M ⇒ s"(λ${x.toString}.${_M.full})"
  }

  /** Short notation of this Lambda Term.
    *
    * Conventions:
    *  - Application is left-associative, and unnecessary parenthesises are removed.
    *  - Lambda scopes are aggregated.
    *
    * For example: Short for (Mx) is Mx and short for ((λx.yx)(zz)) is (λx.yx)(zz)
    */
  lazy val short: String = this match {
    case Atom(name)                  ⇒ name.toString
    case x λ (_M: Abstraction)       ⇒ s"λ${x.name}${_M.short.substring(1)}"
    case x λ _M                      ⇒ s"λ${x.name}.${_M.short}"
    case (_M: Abstraction) $ Atom(y) ⇒ s"(${_M.short})${y.toString}"
    case (_M: Abstraction) $ _N      ⇒ s"(${_M.short})(${_N.short})"
    case _M $ Atom(y)                ⇒ s"${_M.short}$y"
    case _U $ _V                     ⇒ s"${_U.short}(${_V.short})"
  }

}
