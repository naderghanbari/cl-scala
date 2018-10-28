package cl.compiler.lexer

import cl.compiler.CLLexerError

import scala.util.parsing.combinator.RegexParsers

/** Lexer for CL Term language using Scala Parser Combinator.
  *
  * Whitespaces, except newline, are ignored.
  *
  * Terms and atoms can have 1 character as names, as in every book and paper on CL. For now Term references
  * are not part of the syntax (i.e. something like "SUVW" is not supported). This will be added once an
  * environment is implemented (in order to actually evaluate terms).
  */
object CLLexer extends RegexParsers {

  override val skipWhitespace = true
  override val whiteSpace = "[ \t\r\f]+".r

  private def openRoundBracket = "("  ^^ (_ => `(`)
  private def closeRoundBracket = ")" ^^ (_ => `)`)
  private def variable = "[a-z]".r    ^^ (s => VAR(s.head))
  private def termRef = "[A-Z]".r     ^^ (s => REF(s.head))
  private def definition = "≡"        ^^ (_ => ≡)

  private def tokens =
    phrase {
      rep1 {
        openRoundBracket |
        closeRoundBracket |
        variable |
        termRef |
        definition
      }
    }

  /** Applies the lexer to the provided input.
    *
    * @param input Input to parse.
    * @return List of parsed tokens on the right, or error on the left.
    */
  def apply(input: String): Either[CLLexerError, List[CLToken]] =
    parse(tokens, input) match {
      case Success(result, _) => Right(result)
      case NoSuccess(m, _)    => Left(CLLexerError(input, m))
    }

}
