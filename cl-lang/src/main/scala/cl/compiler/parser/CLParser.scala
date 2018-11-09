package cl.compiler.parser

import cl.compiler.CLParserError
import cl.compiler.ast._
import cl.compiler.lexer._

import scala.collection.immutable.Seq
import scala.util.parsing.combinator.Parsers

/** Parser for simple CL language.
  *
  * Supports the left associativity of application.
  */
object CLParser extends Parsers {

  override type Elem = CLToken

  def pair[T, U](p: Parser[T ~ U]): Parser[(T, U)] = p ^^ { case x ~ y => x -> y }

  private def `var`: Parser[Var]               = accept("Var", { case VAR(name) ⇒ Var(name) })
  private def ref: Parser[Ref]                 = accept("Ref", { case REF(name) ⇒ Ref(name) })
  private def group: Parser[Term]              = PAROPEN ~> term <~ PARCLOSE
  private def term: Parser[Term]               = rep1(`var` | ref | group) ^^ { _.reduceLeft(_ $ _) }
  private def bracket: Parser[List[Var]]       = BRAOPEN ~> rep1sep(`var`, COMMA) <~ BRACLOSE
  private def abstraction: Parser[Abstraction] = pair { bracket ~ term } ^^ Abstraction.tupled
  private def expr: Parser[Expr]               = abstraction | term
  private def defn: Parser[Defn]               = pair { (ref <~ DEFN) ~ expr } ^^ Defn.tupled
  private def ast: Parser[AST]                 = phrase(defn | expr)

  def apply(tokens: Seq[CLToken]): Either[CLParserError, AST] =
    ast(new CLTokenReader(tokens)) match {
      case Success(result, _) ⇒ Right(result)
      case NoSuccess(msg, _)  ⇒ Left(CLParserError(msg))
    }

}
