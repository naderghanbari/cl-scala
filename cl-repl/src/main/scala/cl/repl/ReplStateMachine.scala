package cl.repl

import cl.Term
import cl.abstraction.{Abstraction => AbstractionStrategy}
import cl.eval.Env

object ReplStateMachine {

  case class State(lastResult: Option[Term], ρ: Env, abs: AbstractionStrategy)

  case class Transition(newState: State) {
    def and(f: ⇒ Unit): State = { f; newState }
  }

  def goto(newState: State): Transition = Transition(newState)

}
