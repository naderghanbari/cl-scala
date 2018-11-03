package cl.eval

import cl.compiler.ast.{Application => $, _}

object Eval {

  import cl.Reduction.reduceToWeakNormalForm

  case class Out(result: Option[cl.Term], updatedEnv: Env)

  def toADT(term: Term)(implicit ρ: Env): Either[EvalError, cl.Term] = term match {
    case Var(x)      => Right(cl.Var(x))
    case r @ Ref(_M) => ρ.refs.get(_M).toRight(UnboundRefError(r))
    case _U $ _V     => toADT(_U).flatMap(op => toADT(_V).map(arg => op $ arg))
  }

  def weakEagerEval(ast: AST)(implicit ρ: Env): Either[EvalError, Out] = ast match {
    case Defn(r @ Ref(_M), _) if ρ.refs.contains(_M) => Left(RefRebindError(r))
    case Defn(Ref(_M), rhs)                          => toADT(rhs).map(t => Out(None, ρ :+ (_M -> t)))
    case _M: Term                                    => toADT(_M).map(t => Out(Some(reduceToWeakNormalForm(t)), ρ))
  }

}
