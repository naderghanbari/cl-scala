package cl.eval

sealed abstract class EvalError(val message: String)

case class UnboundRefError(ref: Char) extends EvalError(s"Unbound ref $ref!")

case class RefRebindError(ref: Char) extends EvalError(s"Ref $ref is already bound!")
