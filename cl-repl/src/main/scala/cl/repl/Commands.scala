package cl.repl

import cl.abstraction.{Abstraction => AbstractionStrategy}
import AbstractionStrategy.Implicits.{primitive => primitiveAbs}
import AbstractionStrategy.Implicits.{weak => weakAbs}
import AbstractionStrategy.Implicits.{eta => etaAbs}
import cl.systems.{CLSystem, SKISystem, SKSystem}

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
    "-sys:SKI"       -> SystemDirective(SKISystem),
    "-sys:SK"        -> SystemDirective(SKSystem),
    "-abs:primitive" -> AbsDirective(primitiveAbs),
    "-abs:weak"      -> AbsDirective(weakAbs),
    "-abs:eta"       -> AbsDirective(etaAbs),
  )

  def classify(s: String): Command = Catalog.getOrElse(s, Statement(s))

}
