package cl.lang

import cl.lang.ast.AST
import cl.lang.lexer.CLLexer
import cl.lang.parser.CLParser

/** Compiler for the simple CL language statements.
  *
  * Usage:
  * {{{
  * import cl.compiler.CLCompiler
  * CLCompiler("SKI")  // S(K)(I)
  * }}}
  */
object CLCompiler {

  /** Compiles the given statement.
    *
    * @param statement Simple CL statement.
    * @return AST if successful, compiler error otherwise.
    */
  def apply(statement: String): Either[CLCompileError, AST] = CLLexer(statement).flatMap(CLParser.apply)

}
