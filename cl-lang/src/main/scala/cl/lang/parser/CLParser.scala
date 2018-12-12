package cl.lang.parser

import cl.lang.CLParserError
import cl.lang.ast._
import cl.lang.lexer._

import scala.collection.immutable.Seq
import scala.util.parsing.combinator.Parsers

/** Parser for simple CL language.
  *
  * Supports the left associativity of application.
  */
object CLParser extends Parsers {

  override type Elem = CLToken

  def pair[T, U](p: Parser[T ~ U]): Parser[(T, U)] = p ^^ { case x ~ y => x -> y }

  private def `var`: Parser[Var]          = accept("Var", { case VAR(name) ⇒ Var(name) })
  private def ref: Parser[Ref]            = accept("Ref", { case REF(name) ⇒ Ref(name) })
  private def termGrp: Parser[Term]       = PAROPEN ~> term <~ PARCLOSE
  private def term: Parser[Term]          = rep1(termGrp | `var` | ref) ^^ { _.reduceLeft(_ ^ _) }
  private def abstBra: Parser[List[Var]]  = BRAOPEN ~> rep1sep(`var`, COMMA) <~ BRACLOSE
  private def abst: Parser[Abstraction]   = pair { (abstBra <~ DOT) ~ expr } ^^ Abstraction.tupled
  private def subBra: Parser[(Term, Var)] = pair { BRAOPEN ~> term ~ (SLASH ~> `var`) <~ BRACLOSE }
  private def sub: Parser[Substitution]   = pair { subBra ~ expr } ^^ Substitution.tupled
  private def exprGrp: Parser[Expr]       = PAROPEN ~> expr <~ PARCLOSE
  private def expr: Parser[Expr]          = phrase(exprGrp | sub | abst | term)
  private def defn: Parser[Defn]          = pair { (ref <~ DEFN) ~ phrase(expr) } ^^ Defn.tupled
  private def ast: Parser[AST]            = phrase(defn | expr)

  def apply(tokens: Seq[CLToken]): Either[CLParserError, AST] =
    ast(new CLTokenReader(tokens)) match {
      case Success(result, _) ⇒ Right(result)
      case NoSuccess(msg, _)  ⇒ Left(CLParserError(msg))
    }

}
