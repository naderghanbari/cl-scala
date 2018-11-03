package cl.compiler

sealed trait CLCompileError {
  def message: String
}

/** Exception representing a failed attempt to parse an input by the lexer.
  *
  * @param input Rejected input.
  * @param details Lexer error message details.
  */
case class CLLexerError(input: String, details: String) extends CLCompileError {
  val message = s"""CLLexer error for input "$input": $details"""
}

/** Exception representing a failed attempt to parse tokens to AST.
  *
  * @param message Parser error message.
  */
case class CLParserError(message: String) extends CLCompileError
