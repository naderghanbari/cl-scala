package cl.compiler.lexer

sealed trait CLToken

case object `(` extends CLToken
case object `)` extends CLToken
case object := extends CLToken

case class VAR(name: Char) extends CLToken { require(name.isLower) }
case class REF(name: Char) extends CLToken { require(name.isUpper) }
