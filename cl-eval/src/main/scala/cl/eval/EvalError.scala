package cl.eval

import cl.compiler.ast.Ref

sealed abstract class EvalError(val message: String)

case class UnboundRefError(ref: Ref) extends EvalError(s"Unbound ref ${ref.name}!")

case class RefRebindError(ref: Ref) extends EvalError(s"Ref ${ref.name} is already bound!")

case object EmptyAbstractionBracket extends EvalError(s"Empty abstraction bracket is meaningless!")
