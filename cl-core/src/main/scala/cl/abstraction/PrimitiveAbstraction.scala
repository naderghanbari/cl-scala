package cl.abstraction

import cl._

/** Primitive Abstraction, Aka Curry's algorithm (fab).
  *
  * Induction on the structure of the Term M:
  *
  * - Clause a -        [x].a ≡ Ka                 if a ≠ x and a is an atom
  * - Clause b -        [x].x ≡ I
  * - Clause f -        [x].UV ≡ S([x].U)([x].V )  if neither (a) nor (b) applies
  */
object PrimitiveAbstraction extends Abstraction {

  override val name = "Primitive Abstraction"

  override def apply(x: Var, M: Term): Term = M match {
    case a @ Atom(_) if a != x ⇒ K $ a
    case `x`                   ⇒ I
    case _U $ _V               ⇒ S $ apply(x, _U) $ apply(x, _V)
  }

}
