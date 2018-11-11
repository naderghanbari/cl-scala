package cl.eval

import cl.abstraction.{Bracket => ADTBracket, Abstraction => AbstractionStrategy}
import cl.lang.ast._

trait ExpressionEval {

  type E[T <: cl.Term] = Either[EvalError, T]

  implicit def abstractionStrategy: AbstractionStrategy

  def evalRef(r: Ref)(implicit ρ: Env): E[cl.Term] =
    ρ.refs.get(r.name).toRight(left = UnboundRefError(r.name))

  def evalApplication(a: Application)(implicit ρ: Env): E[cl.Application] = for {
    op  <- eval(a.op)
    arg <- eval(a.arg)
  } yield op $ arg

  def evalSubstitution(s: Substitution)(implicit ρ: Env): E[cl.Term] = for {
    replacingTerm <- eval(s.bracket._1)
    body          <- eval(s.body)
    Var(x) = s.bracket._2
  } yield (replacingTerm / cl.Var(x))(body)

  def evalAbstraction(a: Abstraction)(implicit ρ: Env): E[cl.Term] = for {
    body <- eval(a.body)
    Var(x) = a.bracket.head
    xs = a.bracket.tail.map(_.name)
  } yield ADTBracket(cl.Var(x), xs.map(cl.Var): _*)(body)

  def eval(expr: Expr)(implicit ρ: Env): E[cl.Term] = expr match {
    case Var(x)          ⇒ Right(cl.Var(x))
    case r: Ref          ⇒ evalRef(r)
    case a: Application  ⇒ evalApplication(a)
    case s: Substitution ⇒ evalSubstitution(s)
    case a: Abstraction  ⇒ evalAbstraction(a)
  }
}
