package cl.compiler

import cl.compiler.ast.AST
import cl.compiler.lexer.CLLexer
import cl.compiler.parser.CLParser

/** Compiler for the simple CL language.
  *
  * Supports only full format (official non-ambiguous syntax, i.e. fully parenthesizes).
  */
object CLCompiler {

  def apply(code: String): Either[CLCompileError, AST] = CLLexer(code).flatMap(CLParser.apply)

}
