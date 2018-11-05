package cl.repl
import org.jline.reader.{LineReader, LineReaderBuilder}
import org.jline.terminal.{Terminal, TerminalBuilder}

import scala.io.AnsiColor.{CYAN, RESET}

trait JLineSupport {

  private lazy val terminal: Terminal = TerminalBuilder.terminal()
  private lazy val reader: LineReader = LineReaderBuilder.builder().terminal(terminal).build()

  val prompt: String           = s"${CYAN}CL > $RESET"
  def readCommand(): String    = reader.readLine(prompt)
  def put(s: String): Unit     = terminal.writer().print(s)
  def putLine(s: String): Unit = terminal.writer().println(s)

}
