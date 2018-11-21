package cl.systems.sk.abstraction

import cl.abstraction.Abstraction
import cl.systems.sk.SK.{K, S}
import cl.{^, Atom, Term, Var}

/** Primitive Abstraction, Aka Curry's algorithm (fab) in an SK System.
  *
  * Induction on the structure of the Term M:
  *
  * - Clause a -        [x].a ≡ Ka                 if a ≠ x and a is an atom
  * - Clause b -        [x].x ≡ SKK
  * - Clause f -        [x].UV ≡ S([x].U)([x].V )  if neither (a) nor (b) applies
  */
object SKPrimitiveAbstraction extends Abstraction {

  val name = "SK Primitive Abstraction"

  override def apply(x: Var, M: Term): Term = M match {
    case a @ Atom(_) if a != x ⇒ K ^ a
    case `x`                   ⇒ S ^ K ^ K
    case _U ^ _V               ⇒ S ^ apply(x, _U) ^ apply(x, _V)
  }

}
