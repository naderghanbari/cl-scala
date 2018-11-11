package cl.eval

import cl.abstraction.Abstraction.Implicits.eta
import cl.lang.ast._

object Eval extends ExpressionEval {

  import cl.Reduction.reduceToWeakNormalForm
  override implicit val abstractionStrategy = eta

  case class Out(result: Option[cl.Term], updatedEnv: Env)

  def weakEagerEval(ast: AST)(implicit ρ: Env): Either[EvalError, Out] = ast match {
    case Defn(Ref(_M), _) if ρ.refs.contains(_M) ⇒ Left(RefRebindError(_M))
    case Defn(Ref(_M), rhs)                      ⇒ eval(rhs).map(t ⇒ Out(None, ρ :+ (_M -> t)))
    case _M: Expr                                ⇒ eval(_M).map(t ⇒ Out(Some(reduceToWeakNormalForm(t)), ρ))
  }

}
