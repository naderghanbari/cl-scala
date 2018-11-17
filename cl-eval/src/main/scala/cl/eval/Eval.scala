package cl.eval

import cl.abstraction.{Abstraction => AbstractionStrategy}
import cl.Reduction.reduceToWeakNormalForm
import cl.lang.ast._
import cl.systems.CLSystem

object Eval extends ExpressionEval {

  case class Out(result: Option[cl.Term], updatedEnv: Env)

  def weakEagerEval(ast: AST)(implicit ρ: Env, abs: AbstractionStrategy, system: CLSystem): Either[EvalError, Out] =
    ast match {
      case Defn(Ref(_M), _) if ρ.refs.contains(_M) ⇒ Left(RefRebindError(_M))
      case Defn(Ref(_M), rhs)                      ⇒ eval(rhs).map(t ⇒ Out(None, ρ :+ (_M -> t)))
      case _M: Expr                                ⇒ eval(_M).map(t ⇒ Out(Some(reduceToWeakNormalForm(t)), ρ))
    }

}
