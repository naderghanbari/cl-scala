package cl

trait Show { self: Term =>

  lazy val full: String = self match {
    case Atom(name) => name.toString
    case u $ v => s"(${u.full}${v.full})"
  }

  lazy val short: String = self match {
    case Atom(name) => name.toString
    case x $ Atom(y) => s"${x.short}${y.toString}"
    case x $ y => s"${x.short}(${y.short})"
  }

}
