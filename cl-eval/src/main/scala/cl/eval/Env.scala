package cl.eval
import cl.systems.sk.SK
import cl.systems.ski.SKI

case class Env(refs: Map[String, cl.Term]) {
  def :+(entry: (String, cl.Term)) = copy(refs = refs + entry)
}

object Env {

  val pureSK: Env  = Env(Map("K" -> SK.K, "S"  -> SK.S))
  val pureSKI: Env = Env(Map("I" -> SKI.I, "K" -> SKI.K, "S" -> SKI.S))

}
