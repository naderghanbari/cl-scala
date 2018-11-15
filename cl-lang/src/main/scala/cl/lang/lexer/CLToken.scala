package cl.lang.lexer

sealed trait CLToken

case object PAROPEN  extends CLToken
case object PARCLOSE extends CLToken
case object BRAOPEN  extends CLToken
case object BRACLOSE extends CLToken
case object COMMA    extends CLToken
case object SLASH    extends CLToken
case object DOT      extends CLToken
case object STAR     extends CLToken
case object DEFN     extends CLToken

case class VAR(name: Char)   extends CLToken { require(name.isLower) }
case class REF(name: String) extends CLToken
