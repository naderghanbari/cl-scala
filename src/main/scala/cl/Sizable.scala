package cl

trait Sizable { self: Term =>

  lazy val length: Int = self match {
    case Atom(_) => 1
    case $(u, v) => u.length + v.length
  }

}
