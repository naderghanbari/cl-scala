package cl.eval

case class Env(refs: Map[String, cl.Term]) extends AnyVal {
  def :+(entry: (String, cl.Term)) = copy(refs = refs + entry)
}

object Env {

  val pure: Env = Env(Map("I" -> cl.I, "K" -> cl.K, "S" -> cl.S))

}
