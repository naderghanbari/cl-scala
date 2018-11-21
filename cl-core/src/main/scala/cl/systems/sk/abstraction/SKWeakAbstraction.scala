package cl.systems.sk.abstraction

import cl.abstraction.Abstraction
import cl.systems.sk.SK.{K, S}
import cl.{^, Term, Var}

/** Weak Abstraction, aka  Curry's algorithm (abf) in an SK System.
  *
  * Induction on the structure of the Term M:
  * - Clause a -        [x].M ≡ KM                 if x ̸∈ FV(M)
  * - Clause b -        [x].x ≡ SKK
  * - Clause f -        [x].UV ≡ S([x].U)([x].V )  if neither (a) nor (c) applies
  */
object SKWeakAbstraction extends Abstraction {

  val name = "SK Weak Abstraction"

  override def apply(x: Var, M: Term): Term = M match {
    case _ if !M.FV.contains(x) ⇒ K ^ M
    case `x`                    ⇒ S ^ K ^ K
    case _U ^ _V                ⇒ S ^ apply(x, _U) ^ apply(x, _V)
  }

}
