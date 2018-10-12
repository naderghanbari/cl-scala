package cl

trait Reducible { self: Term =>

  def reduce: Term = self match {
    case a: Atom => a
    case I $ (t: Term) => t
    case _ => ???
  }

}
