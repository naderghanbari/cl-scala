package cl.abstraction

import cl._

/** Abstraction Strategy.
  *
  * Clients can choose one of the strategies implicitly by importing them:
  * {{{
  *   import cl.abstraction.Abstraction.EtaAbstraction
  *   // use the imported Abstraction strategy implicitly
  * }}}
  */
trait Abstraction extends ((Var, Term) => Term) {

  /** Abstracts away x from M.
    *
    * @param x Variable to abstract away.
    * @param M Term to abstract x away from.
    * @return Abstracted term.
    */
  def apply(x: Var, M: Term): Term

}

object Abstraction {

  /** Syntactic sugar to simulate something similar to [x].M but due to Scala's syntax (square brackets
    * reserved) | is used.
    *
    * Syntactic building block of the Abstraction DSL.
    *
    * As a syntactic sugar use this alternative:
    * {{{
    *   import cl.abstraction.Abstraction._             // Abstraction DSL entry point
    *   import cl.abstraction.Implicits.EtaAbstraction  // Or other strategies
    *   val x = ....                                    // Some Var
    *   val M =                                         // Some CL Term
    *   |(x)| M                                         // Abstract x from M
    *   val bracketX = |(x)|                            // Same as val abstractionX = Bracket(x)
    *   bracketX.apply(M)                               // Apply the Bracket, i.e. do the actual Abstraction
    * }}}
    *
    * @param x variable to abstract away.
    */
  case class |(x: Var) {
    def | : Bracket = Bracket(x)
  }

}
