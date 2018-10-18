package cl.abstraction

import cl._

/** η Abstraction (Eta), aka Curry;s algorithm (abcf).
  *
  * Induction on the structure of the Term M (as in Definition 2.18 in Hindley):
  *
  *- Clause a -        [x].M ≡ KM                 if x ̸∈ FV(M)
  *- Clause b -        [x].x ≡ I
  *- Clause c -        [x].Ux ≡ U                 if x ̸∈ FV(U)
  *- Clause f -        [x].UV ≡ S([x].U)([x].V )  if neither (a) nor (c) applies
  *
  * Third clause (c) significantly simplifies the end results so this will be the default
  * abstraction used in most cases from now on, unless explicitly stated.
  */
object EtaAbstraction extends Abstraction {

  override def apply(x: Var, M: Term): Term = M match {
    case _ if !M.FV.contains(x) => K $ M
    case `x` => I
    case _U $ `x` if !_U.FV.contains(x) => _U
    case _U $ _V => S $ apply(x, _U) $ apply(x, _V)
  }

}
