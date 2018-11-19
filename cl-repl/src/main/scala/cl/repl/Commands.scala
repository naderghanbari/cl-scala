package cl.repl

import cl.abstraction.{Abstraction => AbstractionStrategy}
import cl.systems.ski.SKI
import cl.systems.sk.SK
import cl.systems.ski.abstraction.{Eta => SKIEtaAbs}
import cl.systems.ski.abstraction.{Primitive => SKIPrimitiveAbs}
import cl.systems.ski.abstraction.{Weak => SKIWeakAbs}
import cl.systems.CLSystem

object Commands {

  sealed trait Command

  case object Quit    extends Command
  case object Refresh extends Command
  case object Blank   extends Command

  case class SystemDirective(system: CLSystem)      extends Command
  case class AbsDirective(abs: AbstractionStrategy) extends Command

  case class Statement(input: String) extends Command

  private val Catalog = Map(
    ":q"             -> Quit,
    ":r"             -> Refresh,
    ""               -> Blank,
    "-sys:SKI"       -> SystemDirective(SKI),
    "-sys:SK"        -> SystemDirective(SK),
    "-abs:primitive" -> AbsDirective(SKIPrimitiveAbs),
    "-abs:weak"      -> AbsDirective(SKIWeakAbs),
    "-abs:eta"       -> AbsDirective(SKIEtaAbs),
  )

  def classify(s: String): Command = Catalog.getOrElse(s, Statement(s))

}
