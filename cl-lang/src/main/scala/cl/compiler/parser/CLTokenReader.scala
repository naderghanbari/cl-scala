package cl.compiler.parser
import cl.compiler.lexer.CLToken

import scala.util.parsing.input.{NoPosition, Reader}

class CLTokenReader(tokens: Seq[CLToken]) extends Reader[CLToken] {
  def first = tokens.head
  def atEnd = tokens.isEmpty
  def pos   = NoPosition
  def rest  = new CLTokenReader(tokens.tail)
}
