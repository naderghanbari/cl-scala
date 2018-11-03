package cl.eval

import cl.compiler.ast.{Application => $, _}

object Eval {

  import cl.Reduction.reduceToWeakNormalForm

  type Output = (Option[cl.Term], Env)

  def toADT(term: Term)(implicit ρ: Env): Either[EvalError, cl.Term] = term match {
    case Var(x)      => Right(cl.Var(x))
    case r @ Ref(_M) => ρ.refs.get(_M).toRight(UnboundRefError(r))
    case _U $ _V     => toADT(_U).flatMap(op => toADT(_V).map(arg => op $ arg))
  }

  def weakEagerEval(ast: AST)(implicit ρ: Env): Either[EvalError, Output] = ast match {
    case Defn(r @ Ref(_M), _) if ρ.refs.contains(_M) => Left(RefRebindError(r))
    case Defn(Ref(_M), rhs)                          => toADT(rhs).map(adt => None -> (ρ :+ (_M -> adt)))
    case _M: Term                                    => toADT(_M).map(reduceToWeakNormalForm).map(Some(_) -> ρ)
  }

}
