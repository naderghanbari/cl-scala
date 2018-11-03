package cl.eval

case class Env(refs: Map[Char, cl.Term]) extends AnyVal {
  def :+(entry: (Char, cl.Term)) = copy(refs = refs + entry)
}

object Env {

  val pure: Env = Env(Map('I' -> cl.I, 'K' -> cl.K, 'S' -> cl.S))

}
