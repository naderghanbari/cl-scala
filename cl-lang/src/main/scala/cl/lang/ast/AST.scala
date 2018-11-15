package cl.lang.ast

sealed trait AST

sealed trait Expr extends AST

sealed trait Term extends Expr {
  def $(arg: Term)     = Application(this, arg)
  def apply(arg: Term) = Application(this, arg)
}
case class Var(name: Char) extends Term { require(name.isLower) }
case class Ref(name: String) extends Term
case class Application(op: Term, arg: Term) extends Term

case class Defn(ref: Ref, rhs: Expr) extends AST

case class Abstraction(bracket: List[Var], body: Expr) extends Expr { require(bracket.nonEmpty) }

case class Substitution(bracket: (Term, Var), body: Expr) extends Expr
