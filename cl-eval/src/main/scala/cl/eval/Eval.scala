package cl.eval

import cl.abstraction.{Bracket ⇒ ADTBacket}
import cl.compiler.ast.{Abstraction ⇒ `[]`, Application ⇒ $, _}

object Eval {

  import cl.Reduction.reduceToWeakNormalForm
  import cl.abstraction.Abstraction.Implicits.eta

  case class Out(result: Option[cl.Term], updatedEnv: Env)

  def evalExpr(expr: Expr)(implicit ρ: Env): Either[EvalError, cl.Term] = expr match {
    case Var(x)                   ⇒ Right(cl.Var(x))
    case r @ Ref(_M)              ⇒ ρ.refs.get(_M).toRight(UnboundRefError(r))
    case _U $ _V                  ⇒ evalExpr(_U).flatMap(op ⇒ evalExpr(_V).map(arg ⇒ op $ arg))
    case Nil `[]` _               ⇒ Left(EmptyAbstractionBracket)
    case (Var(x) :: tail) `[]` _V ⇒ evalExpr(_V).map(ADTBacket(cl.Var(x), tail.map(_.name).map(cl.Var): _*)(_))
  }

  def weakEagerEval(ast: AST)(implicit ρ: Env): Either[EvalError, Out] = ast match {
    case Defn(r @ Ref(_M), _) if ρ.refs.contains(_M) ⇒ Left(RefRebindError(r))
    case Defn(Ref(_M), rhs)                          ⇒ evalExpr(rhs).map(t ⇒ Out(None, ρ :+ (_M -> t)))
    case _M: Expr                                    ⇒ evalExpr(_M).map(t ⇒ Out(Some(reduceToWeakNormalForm(t)), ρ))
  }

}
