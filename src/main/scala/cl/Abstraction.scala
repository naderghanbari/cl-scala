package cl

object Abstraction {

  /** Abstracts away x from M. Definition 2.18 in Hindley.
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
    * @param x Variable to abstract away.
    * @param M Term to abstract x away from.
    * @return Abstracted term.
    */
  def abstractAway(x: Var)(M: Term): Term = M match {
    case _ if !M.FV.contains(x) => K $ M
    case `x` => I
    case _U $ `x` if !_U.FV.contains(x) => _U
    case _U $ _V => S $ abstractAway(x)(_U) $ abstractAway(x)(_V)
  }

  /** Abstraction Bracket over variable x.
    *
    * Usage:
    * {{{
    *   import scala.language.postfixOps
    *   import cl.Abstraction._
    *   val x = ....                // Some Var
    *   val M =                     // Some CL Term
    *   val bracketX = Bracket(x)   // Same as val abstractionX = Bracket(x)
    *   bracketX.apply(M)           // Apply the Bracket, i.e. do the actual Abstraction
    * }}}
    *
    * @param x variable to abstract away.
    */
  case class Bracket(x: Var) {

    /** Abstracts the provided Term with this Bracket (i.e. by abstracting away x).
      *
      * See alo the syntactic sugar to create Brackets below in this object.
      *
      * @param M Term to abstract x away from.
      * @return Abstracted term, now free of x!
      */
    def apply(M: Term): Term = abstractAway(x)(M)

  }

  /** Syntactic sugar to simulate something similar to [x].M but due to Scala's syntax (square brackets
    * reserved) | is used.
    *
    * As a syntactic sugar use this alternative:
    * {{{
    *   import cl.Abstraction._
    *   val x = ....                     // Some Var
    *   val M =                          // Some CL Term
    *   |(x)| M                          // Abstract x from M
    *   val bracketX = |(x)|             // Same as val abstractionX = Bracket(x)
    *   bracketX.apply(M)                // Apply the Bracket, i.e. do the actual Abstraction
    * }}}
    *
    * @param x variable to abstract away.
    */
  case class |(x: Var) {
    def | : Bracket = Bracket(x)
  }

}
