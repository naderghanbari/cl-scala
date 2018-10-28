package cl

trait Show { self: Term =>

  /** Full (official non-ambiguous) notation of this CL term.
    *
    * Fully parenthesized, as in (Kx) and ((Kx)y).
    */
  lazy val full: String = self match {
    case Atom(name) => name.toString
    case u $ v      => s"(${u.full}${v.full})"
  }

  /** Short notation of this CL term.
    *
    * Convention: Application ($) is left-associative, and unnecessary parenthesises are removed:
    * For example: Short for (Kx) is Kx and short for ((Kx)y) is Kxy.
    */
  lazy val short: String = self match {
    case Atom(name)  => name.toString
    case x $ Atom(y) => s"${x.short}${y.toString}"
    case x $ y       => s"${x.short}(${y.short})"
  }

}
