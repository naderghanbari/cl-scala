package cl.compiler.parser

import cl.compiler.CLParserError
import cl.compiler.ast._
import cl.compiler.lexer._

import scala.collection.immutable.Seq
import scala.util.parsing.combinator.Parsers
import scala.util.parsing.input.{NoPosition, Reader}

/** Parser for simple CL language.
  *
  * Supports only full format (official non-ambiguous syntax, i.e. fully parenthesizes).
  */
object CLParser extends Parsers {

  override type Elem = CLToken

  def `var`: Parser[Var]       = accept("Var", { case VAR(name) => Var(name) })
  def ref: Parser[Ref]         = accept("Ref", { case REF(name) => Ref(name) })
  def app: Parser[Application] = `(` ~ term ~ term ~ `)` ^^ { case _ ~ _X ~ _Y ~ _ => _X $ _Y }
  def term: Parser[Term]       = `var` | ref | app
  def defn: Parser[Defn]       = ref ~ := ~ term ^^ { case _M ~ _ ~ rhs => Defn(_M, rhs) }
  def ast: Parser[AST]         = phrase(defn | term)

  def apply(tokens: Seq[CLToken]): Either[CLParserError, AST] =
    ast(new CLTokenReader(tokens)) match {
      case Success(result, _) => Right(result)
      case NoSuccess(msg, _)  => Left(CLParserError(msg))
    }

}

class CLTokenReader(tokens: Seq[CLToken]) extends Reader[CLToken] {
  def first = tokens.head
  def atEnd = tokens.isEmpty
  def pos   = NoPosition
  def rest  = new CLTokenReader(tokens.tail)
}
