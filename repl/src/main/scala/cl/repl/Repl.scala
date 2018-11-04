package cl.repl

import cl.Term
import cl.compiler.{CLCompileError, CLCompiler}
import cl.eval.Eval.Out
import cl.eval.{Env, Eval, EvalError}
import org.jline.reader.LineReaderBuilder
import org.jline.terminal.TerminalBuilder
import scala.io.AnsiColor.{MAGENTA, RESET}

object Repl extends App {

  val terminal           = TerminalBuilder.terminal()
  val lineReader         = LineReaderBuilder.builder().terminal(terminal).build()
  def encolor(s: String) = s"$MAGENTA$s$RESET"
  lazy val prompt        = encolor("CL > ")

  sealed trait Command
  case object Quit extends Command
  case object Refresh extends Command
  case object Blank extends Command
  case class Statement(input: String) extends Command

  val CommandsMap              = Map(":q" -> Quit, ":r" -> Refresh, "" -> Blank)
  def asCommand(input: String) = CommandsMap.getOrElse(input, Statement(input))
  def readCommand()            = asCommand(lineReader.readLine(prompt))

  case class State(lastResult: Option[Term], ρ: Env)

  val initialState = State(None, Env.pure)

  println("Welcome to Simple CL. A weakly eager pure IKS Combinatory Logic interpreter.")
  println("Type in expressions for evaluation. I, K, and S are predefined.")
  println("Try :q to quit and :r to refresh all variables.")
  println()

  Iterator
    .continually(readCommand())
    .takeWhile(_ != Quit)
    .foldLeft(initialState) {
      case (s, Quit | Blank) =>
        s
      case (State(last, _), Refresh) =>
        println("OK! Here's your Fresh Environment.")
        initialState.copy(lastResult = last)
      case (s, Statement(input)) =>
        CLCompiler(input).flatMap(Eval.weakEagerEval(_)(s.ρ)) match {
          case Right(Out(None, updatedEnv)) =>
            println("OK!")
            s.copy(lastResult = None, ρ = updatedEnv)
          case Right(Out(Some(last), updatedEnv)) =>
            println(last.short)
            s.copy(lastResult = None, ρ = updatedEnv)
          case Left(err: EvalError) =>
            println(s"Evaluation error : ${err.message}")
            s
          case Left(err: CLCompileError) =>
            println(s"Evaluation error: ${err.message}")
            s
          case Left(err) =>
            println(s"Unknown error happened: $err")
            s
        }
    }

}
