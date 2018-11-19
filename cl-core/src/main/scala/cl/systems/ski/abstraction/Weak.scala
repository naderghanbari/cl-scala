package cl.systems.ski.abstraction
import cl.abstraction.Abstraction
import cl.systems.ski.SKI
import cl.{ Term, Var, ^ }

/** Weak Abstraction, aka  Curry's algorithm (abf) in an SKI System.
  *
  * Induction on the structure of the Term M:
  * - Clause a -        [x].M ≡ KM                 if x ̸∈ FV(M)
  * - Clause b -        [x].x ≡ I
  * - Clause f -        [x].UV ≡ S([x].U)([x].V )  if neither (a) nor (c) applies
  */
object Weak extends Abstraction {

  val name   = "Weak Abstraction"
  val system = SKI

  override def apply(x: Var, M: Term): Term = M match {
    case _ if !M.FV.contains(x) ⇒ system.K ^ M
    case `x`                    ⇒ system.I
    case _U ^ _V                ⇒ system.S ^ apply(x, _U) ^ apply(x, _V)
  }

}
