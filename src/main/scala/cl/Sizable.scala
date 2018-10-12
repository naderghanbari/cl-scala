package cl

trait Sizable { self: Term =>

  lazy val length: Int = self match {
    case _: Atom => 1
    case $(u, v) => u.length + v.length
  }

}
