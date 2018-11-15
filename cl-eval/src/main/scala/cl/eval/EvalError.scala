package cl.eval

sealed abstract class EvalError(val message: String)

case class UnboundRefError(ref: String) extends EvalError(s"Unbound ref $ref!")

case class RefRebindError(ref: String) extends EvalError(s"Ref $ref is already bound!")
