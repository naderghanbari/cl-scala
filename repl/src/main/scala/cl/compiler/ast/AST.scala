package cl.compiler.ast

sealed trait AST

sealed trait Term extends AST {
  def $(that: Term) = Application(this, that)
}

case class Var(name: Char) extends Term {
  require(name.isLower)
}
case class TermRef(name: Char) extends Term {
  require(name.isUpper)
}
case class Application(operator: Term, argument: Term) extends Term
