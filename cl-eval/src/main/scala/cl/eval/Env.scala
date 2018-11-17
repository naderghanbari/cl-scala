package cl.eval

import cl.systems.{SKISystem, SKSystem}

case class Env(refs: Map[String, cl.Term]) {
  def :+(entry: (String, cl.Term)) = copy(refs = refs + entry)
}

object Env {

  val pureSK: Env  = Env(Map("K" -> SKSystem.K, "S"  -> SKSystem.S))
  val pureSKI: Env = Env(Map("I" -> SKISystem.I, "K" -> SKISystem.K, "S" -> SKISystem.S))

}
