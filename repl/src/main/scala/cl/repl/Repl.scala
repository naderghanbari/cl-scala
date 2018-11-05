package cl.repl

import cl.Term
import cl.compiler.{CLCompileError, CLCompiler}
import cl.eval.Eval.Out
import cl.eval.{Env, Eval, EvalError}
import cl.repl.Commands._

object Repl extends App with JLineSupport {

  import scala.io.AnsiColor._

  case class State(lastResult: Option[Term], ρ: Env)
  val initialState = State(None, Env.pure)

  def welcome(): Unit = {
    pln(s"Welcome to Simple CL.")
    pln(s"A weakly eager pure ${UNDERLINED}IKS$RESET Combinatory Logic interpreter.")
    pln("Type in expressions for evaluation. I, K, and S are predefined.")
    pln(s"Try $UNDERLINED:q$RESET to quit and $UNDERLINED:r$RESET to refresh all variables.")
    pln("")
  }

  def ignite(): Unit =
    Iterator
      .continually(readCommand())
      .takeWhile(_ != Quit)
      .foldLeft(initialState) {
        case (s, Quit | Blank) =>
          s
        case (State(last, _), Refresh) =>
          pln(s"${BLUE}Ok! Here's your Fresh Environment.$RESET")
          initialState.copy(lastResult = last)
        case (s, Statement(input)) =>
          CLCompiler(input).flatMap(Eval.weakEagerEval(_)(s.ρ)) match {
            case Left(err: CLCompileError) =>
              pln(s"Compile error: $MAGENTA${err.message}$RESET")
              s
            case Left(err: EvalError) =>
              pln(s"Evaluation error: $RED${err.message}$RESET")
              s
            case Left(err) =>
              pln(s"${MAGENTA_B}Unknown error: $err$RESET")
              s
            case Right(Out(None, updatedEnv)) =>
              pln(s"${BLUE}Ok!$RESET")
              s.copy(lastResult = None, ρ = updatedEnv)
            case Right(Out(Some(last), updatedEnv)) =>
              pln(s"$GREEN${last.short}$RESET")
              s.copy(lastResult = None, ρ = updatedEnv)
          }
      }

  welcome()
  ignite()

}
