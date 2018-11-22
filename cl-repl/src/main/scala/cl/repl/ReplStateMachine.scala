package cl.repl

import cl.Term
import cl.abstraction.{Abstraction => AbstractionStrategy}
import cl.eval.Env
import cl.systems.CLSystem

trait ReplStateMachine {

  import ReplStateMachine.{State, Transition}

  def state: State
  def setState(newState: State)

  def onStateChange(newState: State, oldState: State): Unit = ()

  def goto(newState: State): Transition = {
    if (newState != state) {
      setState(newState)
      onStateChange(newState, state)
    }
    Transition(newState)
  }

}

object ReplStateMachine {

  case class State(lastResult: Option[Term], ρ: Env, abs: AbstractionStrategy, system: CLSystem)

  case class Transition(newState: State) {
    def and(f: ⇒ Unit): State = { f; newState }
  }

}
