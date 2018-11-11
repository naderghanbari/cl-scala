package cl.repl

import cl.lang.{CLCompileError, CLCompiler}
import cl.eval.Eval.Out
import cl.eval.{Env, Eval, EvalError}
import org.jline.reader.EndOfFileException

import scala.io.AnsiColor._
import scala.util.control.Exception

object Repl extends App with JLineSupport {

  import ReplStateMachine._

  def welcome(): Unit = {
    putLine(s"Welcome to Simple CL.")
    putLine(s"A weakly eager pure ${`<u>`("IKS")} Combinatory Logic interpreter.")
    putLine("Type in expressions for evaluation. I, K, and S are predefined.")
    putLine(s"Try ${`<u>`(":q")} or ${`<u>`("<Ctrl-D>")} to quit and ${`<u>`(":r")} to refresh all variables.")
    putLine("")
  }

  def evalError(state: State): PartialFunction[Either[Any, _], State] = {
    case Left(err: CLCompileError) ⇒ goto(state) and putLine(s"Compile error: $MAGENTA${err.message}$RESET")
    case Left(err: EvalError)      ⇒ goto(state) and putLine(s"Evaluation error: $RED${err.message}$RESET")
    case Left(err)                 ⇒ goto(state) and putLine(s"${MAGENTA_B}Unknown error: $err$RESET")
  }

  def evalSuccess(state: State): PartialFunction[Either[_, Eval.Out], State] = {
    case Right(Out(None, updated)) ⇒
      goto(state.copy(lastResult = None, ρ = updated)) and putLine(s"${BLUE}Ok!$RESET")
    case Right(Out(Some(last), updated)) ⇒
      goto(state.copy(lastResult = None, ρ = updated)) and putLine(s"$GREEN${last.short}$RESET")
  }

  def transitionFunction: (State, Commands.Command) ⇒ State = {
    case (s, Commands.Quit | Commands.Blank) ⇒
      s
    case (s, Commands.AbsDirective(abs)) ⇒
      goto(s.copy(abs = abs)) and putLine(s"${BLUE}Ok! Abstraction strategy changed to ${abs.name}.$RESET")
    case (s, Commands.Refresh) ⇒
      goto(s.copy(ρ = Env.pure)) and putLine(s"${BLUE}Ok! Here's your Fresh Environment.$RESET")
    case (s, Commands.Statement(input)) ⇒
      val result = CLCompiler(input).flatMap(Eval.weakEagerEval(_)(s.ρ, s.abs))
      evalSuccess(s) orElse evalError(s) apply result
  }

  import cl.abstraction.Abstraction.Implicits.{eta => etaAbas}
  val initialState = State(None, Env.pure, etaAbas)

  def ignite(): Unit =
    Exception.failAsValue(classOf[EndOfFileException])(Unit) {
      Iterator
        .continually(readCommand())
        .map(Commands.classify)
        .takeWhile(_ != Commands.Quit)
        .foldLeft(initialState)(transitionFunction)
    }

  welcome()
  ignite()

}
