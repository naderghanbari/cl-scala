package cl.abstraction

import cl._

object Implicits {

  /** Primitive Abstraction.
    *
    * Aka "algorithm (fab)" by Haskell B. Curry.
    */
  implicit object PrimitiveAbstraction extends Abstraction {

    /** Abstracts away x from M by Curry's algorithm (fab).
      *
      * Induction on the structure of the Term M:
      *
      * - Clause a -        [x].a ≡ Ka                 if a ≠ x and a is an atom
      * - Clause b -        [x].x ≡ I
      * - Clause f -        [x].UV ≡ S([x].U)([x].V )  if neither (a) nor (b) applies
      *
      * @param x Term to abstract x away from.
      * @param M Term to abstract x away from.
      * @return Abstracted term.
      */
    override def apply(x: Var, M: Term): Term = M match {
      case a@Atom(_) if a != x => K $ a
      case `x` => I
      case _U $ _V => S $ apply(x, _U) $ apply(x, _V)
    }

  }

  /** η Abstraction (Eta) as in Definition 2.18 in Hindley.
    *
    * Aka "algorithm (abcf)" by Haskell B. Curry.
    */
  implicit object EtaAbstraction extends Abstraction {

    /** Abstracts away x from M by Definition 2.18 in Hindley.
      *
      * Induction on the structure of the Term M:
      *
      * - Clause a -        [x].M ≡ KM                 if x ̸∈ FV(M)
      * - Clause b -        [x].x ≡ I
      * - Clause c -        [x].Ux ≡ U                 if x ̸∈ FV(U)
      * - Clause f -        [x].UV ≡ S([x].U)([x].V )  if neither (a) nor (c) applies
      *
      * Third clause (c), according to a footnote in Hindley's book,
      * significantly simplifies the end results, though the theorem works even with this clause.
      *
      * @param x Term to abstract x away from.
      * @param M Term to abstract x away from.
      * @return Abstracted term.
      */
    override def apply(x: Var, M: Term): Term = M match {
      case _ if !M.FV.contains(x) => K $ M
      case `x` => I
      case _U $ `x` if !_U.FV.contains(x) => _U
      case _U $ _V => S $ apply(x, _U) $ apply(x, _V)
    }

  }

}
