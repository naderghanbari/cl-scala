package cl.repl

import java.util

import org.jline.reader.impl.completer.StringsCompleter
import org.jline.reader._
import org.jline.terminal.{Terminal, TerminalBuilder}

import scala.collection.JavaConverters.asJavaIterable
import scala.io.AnsiColor._

trait JLineSupport {

  def state: ReplStateMachine.State

  private lazy val completer = new DelegateCompleter(new StringsCompleter(asJavaIterable(state.ρ.refs.keys)))
  def updateJLine(newState: ReplStateMachine.State) =
    completer.setCompleter(new StringsCompleter(asJavaIterable(state.ρ.refs.keys)))

  private lazy val terminal: Terminal = TerminalBuilder.terminal()
  private lazy val reader: LineReader = LineReaderBuilder.builder().terminal(terminal).completer(completer).build()

  def prompt: String = s"${CYAN}CL ${state.system.name} > $RESET"

  def readCommand(): String    = reader.readLine(prompt)
  def put(s: String): Unit     = terminal.writer().print(s)
  def putLine(s: String): Unit = terminal.writer().println(s)

  def `<u>`(s: String) = s"$UNDERLINED$s$RESET"

  class DelegateCompleter(var delegate: Completer) extends Completer {
    def setCompleter(delegate: Completer): Unit = this.delegate = delegate
    override def complete(reader: LineReader, line: ParsedLine, candidates: util.List[Candidate]): Unit =
      delegate.complete(reader, line, candidates)
  }

}
