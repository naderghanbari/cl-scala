package cl.systems.sk.abstraction

import cl.abstraction.Abstraction
import cl.systems.sk.SK.{K, S}
import cl.{^, Term, Var}

/** η Abstraction (Eta), aka Curry;s algorithm (abcf) in an SK System.
  *
  *- Clause a -        [x].M ≡ KM                 if x ̸∈ FV(M)
  *- Clause b -        [x].x ≡ SKK
  *- Clause c -        [x].Ux ≡ U                 if x ̸∈ FV(U)
  *- Clause f -        [x].UV ≡ S([x].U)([x].V )  if neither (a) nor (c) applies
  *
  * Third clause (c) significantly simplifies the end results so this will be the default
  * abstraction used in most cases from now on, unless explicitly stated.
  */
object SKEtaAbstraction extends Abstraction {

  val name = "SK Eta Abstraction"

  override def apply(x: Var, M: Term): Term = M match {
    case _ if !M.FV.contains(x)         ⇒ K ^ M
    case `x`                            ⇒ S ^ K ^ K
    case _U ^ `x` if !_U.FV.contains(x) ⇒ _U
    case _U ^ _V                        ⇒ S ^ apply(x, _U) ^ apply(x, _V)
  }

}
