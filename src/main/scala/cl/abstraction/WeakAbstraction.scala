package cl.abstraction

import cl._

/** Weak Abstraction, aka  Curry's algorithm (abf).
  *
  * Induction on the structure of the Term M:
  * - Clause a -        [x].M ≡ KM                 if x ̸∈ FV(M)
  * - Clause b -        [x].x ≡ I
  * - Clause f -        [x].UV ≡ S([x].U)([x].V )  if neither (a) nor (c) applies
  */
object WeakAbstraction extends Abstraction {

  override def apply(x: Var, M: Term): Term = M match {
    case _ if !M.FV.contains(x) => K $ M
    case `x` => I
    case _U $ _V => S $ apply(x, _U) $ apply(x, _V)
  }

}
