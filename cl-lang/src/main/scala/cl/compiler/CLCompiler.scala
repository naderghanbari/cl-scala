package cl.compiler

import cl.compiler.ast.AST
import cl.compiler.lexer.CLLexer
import cl.compiler.parser.CLParser

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
