package cl.compiler
import cl.compiler.lexer.CLLexer
import cl.compiler.parser.CLParser

/** Compiler for simple CL Terms.
  *
  * Supports only full format (official non-ambiguous syntax, i.e. fully parenthesizes).
  */
object CLCompiler {

  def apply(code: String): Either[CLCompileError, cl.Term] = CLLexer(code).flatMap(CLParser.apply)

}
