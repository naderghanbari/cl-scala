package cl.compiler

sealed abstract class CLCompileError(msg: String)

/** Exception representing a failed attempt to parse an input by the lexer.
  *
  * @param input Rejected input.
  * @param message Lexer error message.
  */
case class CLLexerError(input: String, message: String)
    extends CLCompileError(s"""CLLexer error for input "$input": $message""")

/** Exception representing a failed attempt to parse tokens to AST.
  *
  * @param message Parser error message.
  */
case class CLParserError(message: String) extends CLCompileError(message)
