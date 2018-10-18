package cl.abstraction

import cl._

/** Abstraction function.
  *
  * Given a variable x and a term M, returns [x].M
  */
trait Abstraction extends ((Var, Term) => Term) {

  /** Applies the abstraction.
    *
    * @param x Variable to abstract away.
    * @param M Term
    * @return [x].M
    */
  def apply(x: Var, M: Term): Term

}

object Abstraction {

  object Implicits {

    implicit def primitiveAbstraction: Abstraction = PrimitiveAbstraction

    implicit def weakAbstraction: Abstraction = WeakAbstraction

    implicit def etaAbstraction: Abstraction = EtaAbstraction

  }

}

/** Syntactic sugar to simulate something similar to [x].M but due to Scala's syntax (square brackets
  * reserved) | is used.
  *
  * Syntactic building block of the Abstraction DSL.
  *
  * As a syntactic sugar use this alternative:
  * {{{
  *   import cl.abstraction._                          // Abstraction DSL entry point
  *   import Abstractions.Implicits.etaAbstraction    // Or other strategies
  *   val x = ....                                     // Some Var
  *   val M =                                          // Some CL Term
  *   |(x)| M                                          // Abstract x from M
  *   val bracketX = |(x)|                             // Same as val abstractionX = Bracket(x)
  *   bracketX.apply(M)                                // Apply the Bracket, i.e. do the actual Abstraction
  * }}}
  *
  * @param x variable to abstract away.
  */
case class |(x: Var) extends AnyVal {

  def | : Bracket = Bracket(x)

}
