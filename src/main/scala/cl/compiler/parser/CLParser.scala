package cl.compiler.parser

import cl.compiler.CLParserError
import cl.compiler.ast.{Application, Term, TermRef, Var}
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

  private def variable: Parser[Var] =
    accept("Variable", { case VAR(name) => Var(name) })

  private def reference: Parser[TermRef] =
    accept("Reference", { case REF(name) => TermRef(name) })

  private def application: Parser[Application] =
    (`(` ~ term ~ term ~ `)`) ^^ { case _ ~ _X ~ _Y ~ _ => Application(_X, _Y) }

  private def term: Parser[Term] =
    variable | reference | application

  def apply(tokens: Seq[CLToken]): Either[CLParserError, Term] =
    term(new CLTokenReader(tokens)) match {
      case Success(result, _) => Right(result)
      case NoSuccess(msg, _)  => Left(CLParserError(msg))
    }

}
