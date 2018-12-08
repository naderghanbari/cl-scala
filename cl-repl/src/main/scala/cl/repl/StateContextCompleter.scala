package cl.repl

import java.util.{List => JList}

import org.jline.reader.{Candidate, Completer, LineReader, ParsedLine}
import org.jline.reader.impl.completer.StringsCompleter
import scala.collection.JavaConverters._

class StateContextCompleter(var state: ReplStateMachine.State) extends Completer {

  def updateState(state: ReplStateMachine.State): Unit = this.state = state

  private val directive    = "-.*".r
  private val directives   = new StringsCompleter(Commands.Directives.keys.asJava)
  private val replCommand  = ":.*".r
  private val replCommands = new StringsCompleter(Commands.ReplCommands.keys.asJava)
  private def refs         = new StringsCompleter(state.ρ.refs.keys.asJava)

  override def complete(reader: LineReader, line: ParsedLine, candidates: JList[Candidate]): Unit =
    line.word match {
      case directive()   => directives.complete(reader, line, candidates)
      case replCommand() => replCommands.complete(reader, line, candidates)
      case _             => refs.complete(reader, line, candidates)
    }

}
