package cl.compiler.parser

import cl.compiler.CLParserError
import cl.compiler.ast._
import cl.compiler.lexer._

import scala.collection.immutable.Seq
import scala.util.parsing.combinator.Parsers
import scala.util.parsing.input.{NoPosition, Reader}

/** Parser for simple CL language.
  *
  * Supports the left associativity of application.
  */
object CLParser extends Parsers {

  override type Elem = CLToken

  private def `var`: Parser[Var] = accept("Var", { case VAR(name) ⇒ Var(name) })
  private def ref: Parser[Ref]   = accept("Ref", { case REF(name) ⇒ Ref(name) })
  private def grp: Parser[Term]  = PAROPEN ~ term ~ PARCLOSE ^^ { case _ ~ _X ~ _ ⇒ _X }
  private def term: Parser[Term] = rep1(`var` | ref | grp) ^^ { _.reduceLeft(_ $ _) }
  private def defn: Parser[Defn] = ref ~ DEFN ~ term ^^ { case _M ~ _ ~ rhs ⇒ Defn(_M, rhs) }
  private def ast: Parser[AST]   = phrase(defn | term)

  def apply(tokens: Seq[CLToken]): Either[CLParserError, AST] =
    ast(new CLTokenReader(tokens)) match {
      case Success(result, _) ⇒ Right(result)
      case NoSuccess(msg, _)  ⇒ Left(CLParserError(msg))
    }

}

class CLTokenReader(tokens: Seq[CLToken]) extends Reader[CLToken] {
  def first = tokens.head
  def atEnd = tokens.isEmpty
  def pos   = NoPosition
  def rest  = new CLTokenReader(tokens.tail)
}
