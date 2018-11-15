package cl.lang.lexer

import cl.lang.CLLexerError

import scala.util.parsing.combinator.RegexParsers

/** Lexer for CL Term language using Scala Parser Combinator.
  *
  * Whitespaces, except newlines, are ignored.
  *
  * Terms and atoms can have 1 character as names, as in the literature.
  */
object CLLexer extends RegexParsers {

  override val skipWhitespace = true
  override val whiteSpace     = "[ \t\r\f]+".r

  private def parOpen  = "("  ^^ (_ ⇒ PAROPEN)
  private def parClose = ")"  ^^ (_ ⇒ PARCLOSE)
  private def defn     = ":=" ^^ (_ ⇒ DEFN)

  private def braOpen  = "[" ^^ (_ ⇒ BRAOPEN)
  private def braClose = "]" ^^ (_ ⇒ BRACLOSE)
  private def comma    = "," ^^ (_ ⇒ COMMA)
  private def slash    = "/" ^^ (_ ⇒ SLASH)
  private def dot      = "." ^^ (_ ⇒ DOT)

  private def `var` = "[a-z]".r ^^ (s ⇒ VAR(s.head))
  private def ref   = "[A-Z]".r ^^ (s ⇒ REF(s))

  private def comment = "#.*".r
  private def passive = opt(comment)

  private def active = rep1 { parOpen | parClose | braOpen | braClose | slash | comma | dot | `var` | ref | defn }

  private def tokens = phrase { active <~ passive }

  /** Applies the lexer to the provided input.
    *
    * @param input Input to parse.
    * @return List of parsed tokens on the right, or error on the left.
    */
  def apply(input: String): Either[CLLexerError, List[CLToken]] =
    parse(tokens, input) match {
      case Success(result, _) ⇒ Right(result)
      case NoSuccess(m, _)    ⇒ Left(CLLexerError(input, m))
    }

}
