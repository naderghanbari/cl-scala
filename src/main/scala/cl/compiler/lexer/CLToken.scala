package cl.compiler.lexer

sealed trait CLToken

case class VAR(name: Char) extends CLToken {
  require(name.isLower)
}
case object I extends CLToken
case object K extends CLToken
case object S extends CLToken
case object `(` extends CLToken
case object `)` extends CLToken
