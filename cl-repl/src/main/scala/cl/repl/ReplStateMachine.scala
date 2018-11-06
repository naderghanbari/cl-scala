package cl.repl
import cl.Term
import cl.eval.Env

trait ReplStateMachine {

  def initialState: State

  case class State(lastResult: Option[Term], ρ: Env)

  case class Transition(newState: State) {
    def and(f: => Unit): State = { f; newState }
  }

  def goto(newState: State): Transition = Transition(newState)

}
