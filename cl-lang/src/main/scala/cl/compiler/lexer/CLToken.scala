package cl.compiler.lexer

sealed trait CLToken

case object PAROPEN extends CLToken
case object PARCLOSE extends CLToken

case object BRACKETOPEN extends CLToken
case object BRACKETCLOSE extends CLToken
case object COMMA extends CLToken

case object DEFN extends CLToken

case class VAR(name: Char) extends CLToken { require(name.isLower) }
case class REF(name: Char) extends CLToken { require(name.isUpper) }
