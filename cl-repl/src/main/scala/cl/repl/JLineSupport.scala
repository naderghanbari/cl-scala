package cl.repl

import org.jline.reader.LineReader.Option.{DISABLE_EVENT_EXPANSION => NoEscape}
import org.jline.reader._
import org.jline.terminal.TerminalBuilder

import scala.io.AnsiColor._

trait JLineSupport {

  def state: ReplStateMachine.State

  private lazy val completer =
    new StateContextCompleter(state)

  def updateJLine(newState: ReplStateMachine.State) =
    completer.updateState(newState)

  private lazy val terminal =
    TerminalBuilder.terminal()

  private lazy val reader =
    LineReaderBuilder.builder().terminal(terminal).completer(completer).option(NoEscape, true).build()

  def prompt: String           = s"${CYAN}CL ${state.system.name} > $RESET"
  def readCommand(): String    = reader.readLine(prompt).trim
  def put(s: String): Unit     = terminal.writer().print(s)
  def putLine(s: String): Unit = terminal.writer().println(s)
  def `<u>`(s: String): String = s"$UNDERLINED$s$RESET"

}
