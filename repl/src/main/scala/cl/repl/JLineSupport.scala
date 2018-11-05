package cl.repl
import cl.repl.Commands.classify
import org.jline.reader.{LineReader, LineReaderBuilder}
import org.jline.terminal.{Terminal, TerminalBuilder}

import scala.io.AnsiColor.{CYAN, RESET}

trait JLineSupport {

  private lazy val terminal: Terminal = TerminalBuilder.terminal()
  private lazy val reader: LineReader = LineReaderBuilder.builder().terminal(terminal).build()

  val prompt        = s"${CYAN}CL > $RESET"
  def readCommand() = classify(reader.readLine(prompt))

  def pln(s: String) = terminal.writer().println(s)

}
