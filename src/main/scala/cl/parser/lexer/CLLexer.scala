package cl.parser.lexer

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

  private val term: Parser[List[CLToken]] =
    phrase {
      rep1 {
        "I"       ^^ (_ => I           ) |
        "K"       ^^ (_ => K           ) |
        "S"       ^^ (_ => S           ) |
        "("       ^^ (_ => OPEN        ) |
        ")"       ^^ (_ => CLOSE       ) |
        "[a-z]".r ^^ (s => VAR(s.head) )
      }
    }

  /** Applies the lexer to the provided input.
    *
    * @param input Input to parse.
    * @return List of parsed tokens on the right, or error on the left.
    */
  def apply(input: String): Either[CLLexerError, List[CLToken]] = {
    parse(term, input) match {
      case Success(result, _) => Right(result)
      case NoSuccess(m, _) => Left(CLLexerError(input, m))
    }
  }

  /** Exception representing a failed attempt to parse an inout by the lexer.
    *
    * @param input Rejected input.
    * @param message Lexer error message.
    */
  case class CLLexerError(input: String, message: String)
    extends IllegalArgumentException(s"""CLLexer error for input "$input": $message""")

}
