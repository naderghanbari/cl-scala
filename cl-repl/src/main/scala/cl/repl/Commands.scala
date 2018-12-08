package cl.repl

import cl.abstraction.{Abstraction => AbstractionStrategy}
import cl.systems.ski.SKI
import cl.systems.sk.SK
import cl.systems.ski.abstraction._
import cl.systems.sk.abstraction._
import cl.systems.CLSystem

object Commands {

  sealed trait Command

  case object Quit    extends Command
  case object Refresh extends Command
  case object Blank   extends Command

  case class SystemDirective(system: CLSystem)      extends Command
  case class AbsDirective(abs: AbstractionStrategy) extends Command

  case class Statement(input: String) extends Command

  val Directives = Map[String, Command](
    "-sys:SKI"           -> SystemDirective(SKI),
    "-sys:SK"            -> SystemDirective(SK),
    "-abs:SKI:primitive" -> AbsDirective(SKIPrimitiveAbstraction),
    "-abs:SKI:weak"      -> AbsDirective(SKIWeakAbstraction),
    "-abs:SKI:eta"       -> AbsDirective(SKIEtaAbstraction),
    "-abs:SK:primitive"  -> AbsDirective(SKPrimitiveAbstraction),
    "-abs:SK:weak"       -> AbsDirective(SKWeakAbstraction),
    "-abs:SK:eta"        -> AbsDirective(SKEtaAbstraction),
  )

  val ReplCommands = Map[String, Command](
    ":q" -> Quit,
    ":r" -> Refresh
  )

  val NoOp = Map[String, Command](
    "" -> Blank
  )

  def classify(s: String): Command =
    Directives.orElse(ReplCommands).orElse(NoOp).lift.apply(s).getOrElse(Statement(s))

}
