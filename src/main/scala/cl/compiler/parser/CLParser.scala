package cl.compiler.parser

import cl.compiler.CLParserError
import cl.compiler.lexer._

import scala.util.parsing.combinator.Parsers
import scala.util.parsing.input.{NoPosition, Position, Reader}

/** Parser for simple CL Terms.
  *
  * Supports only full format (official non-ambiguous syntax, i.e. fully parenthesizes).
  */
object CLParser extends Parsers {

  override type Elem = CLToken

  class CLTokenReader(tokens: Seq[CLToken]) extends Reader[CLToken] {
    override def first: CLToken = tokens.head
    override def atEnd: Boolean = tokens.isEmpty
    override def pos: Position = NoPosition
    override def rest: Reader[CLToken] = new CLTokenReader(tokens.tail)
  }

  private def variable: Parser[cl.Var] =
    accept("Variable", { case VAR(name) => cl.Var(name) })

  private def atomicConstant: Parser[cl.AtomicConstant] =
    accept("Atomic Constant", {
      case I => cl.I
      case K => cl.K
      case S => cl.S
    })

  private def atom: Parser[cl.Atom] =
    variable | atomicConstant

  private def application: Parser[cl.$] =
    (`(` ~ term ~ term ~ `)`) ^^ { case _ ~ _X ~ _Y ~ _ => cl.$(_X, _Y) }

  private def term: Parser[cl.Term] =
    atom | application

  def apply(tokens: Seq[CLToken]): Either[CLParserError, cl.Term] =
    term(new CLTokenReader(tokens)) match {
      case Success(result, _) => Right(result)
      case NoSuccess(msg, _)  => Left(CLParserError(msg))
    }

}
