package cl.compiler.ast

sealed trait AST

sealed trait Term extends AST {
  def $(arg: Term)     = Application(this, arg)
  def apply(arg: Term) = Application(this, arg)
}
case class Var(name: Char) extends Term { require(name.isLower) }
case class Ref(name: Char) extends Term { require(name.isUpper) }
case class Application(op: Term, arg: Term) extends Term

case class Defn(ref: Ref, rhs: Term) extends AST
